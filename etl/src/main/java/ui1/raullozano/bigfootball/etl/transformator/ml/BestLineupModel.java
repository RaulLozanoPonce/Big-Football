package ui1.raullozano.bigfootball.etl.transformator.ml;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.feature.*;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.ml.tuning.CrossValidator;
import org.apache.spark.ml.tuning.CrossValidatorModel;
import org.apache.spark.ml.tuning.ParamGridBuilder;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.model.transformator.Player;
import ui1.raullozano.bigfootball.common.model.transformator.Position;
import ui1.raullozano.bigfootball.common.model.transformator.Team;
import ui1.raullozano.bigfootball.common.model.transformator.ml.BestPlayerCombination;
import ui1.raullozano.bigfootball.common.model.transformator.ml.PlayerCombination;
import ui1.raullozano.bigfootball.common.model.transformator.temp_stats.LineupStats;
import ui1.raullozano.bigfootball.common.model.transformator.temp_stats.PlayerStats;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static ui1.raullozano.bigfootball.common.model.transformator.Position.*;

public class BestLineupModel {

    private final FileAccessor fileAccessor;
    private Map<String, List<PlayerStats>> playerLastStats;
    private Map<String, List<Integer>> teamLastStats;
    private Map<String, LineupStats> lineupStats;

    public BestLineupModel(FileAccessor fileAccessor) {
        this.fileAccessor = fileAccessor;
    }

    public void get() {

        saveAllTeamsCombinations();

        SparkSession session = getSession();
        Dataset<Row> dataset = getDataset(session, fileAccessor.getPlayerCombinationsToTrainPath());
        CrossValidatorModel cvModel = calculateCvModel(dataset);

        for (String competition : fileAccessor.competitionFolderNames()) {
            for (String season : fileAccessor.seasonFolderNames(competition)) {

                Dataset<Row> teamCombinationDataset = getDataset(session, fileAccessor.getPlayerCombinationToTestPath(competition, season));
                Dataset<Row> transform = cvModel.transform(teamCombinationDataset);

                List<Row> predictions = transform.select("thisTeam", "otherTeam", "lineup", "players", "prediction").collectAsList();
                Map<String, List<Row>> predictionsByTeam = predictions.stream().collect(Collectors.groupingBy(r -> r.getString(0)));

                for (String thisTeam : predictionsByTeam.keySet()) {
                    Map<String, List<Row>> predictionsByOtherTeam = predictionsByTeam.get(thisTeam).stream()
                            .collect(Collectors.groupingBy(r -> r.getString(1)));
                    for (String otherTeam : predictionsByOtherTeam.keySet()) {
                        Map<String, List<Row>> predictionsByLineup = predictionsByOtherTeam.get(otherTeam).stream()
                                .collect(Collectors.groupingBy(r -> r.getString(2)));
                        for (String lineup : predictionsByLineup.keySet()) {
                            List<Row> lineupPredictions = predictionsByLineup.get(lineup);
                            if(!lineupPredictions.isEmpty()) {

                                int nRow = 0;
                                double bestPrediction = lineupPredictions.get(0).getDouble(4);

                                for (int i = 1; i < lineupPredictions.size(); i++) {
                                    if (lineupPredictions.get(i).getDouble(4) > bestPrediction) {
                                        nRow = i;
                                        bestPrediction = lineupPredictions.get(i).getDouble(4);
                                    }
                                }

                                BestPlayerCombination bestPlayerCombination = new BestPlayerCombination()
                                        .thisTeam(thisTeam)
                                        .otherTeam(otherTeam)
                                        .lineup(lineup)
                                        .goalDifference(Math.round(bestPrediction))
                                        .players(Arrays.asList(lineupPredictions.get(nRow).getString(3).replaceAll("\\[|]]", "").split(", ")));

                                this.fileAccessor.saveBestPlayerCombination(competition, season, bestPlayerCombination);
                            }
                        }
                    }
                }
            }
        }

        this.fileAccessor.removePlayerCombinationToTest();
    }

    private SparkSession getSession() {
        SparkConf conf = new SparkConf()
                .setAppName("BestLineup")
                .setMaster(fileAccessor.parameters().get("ml-master"));
        SparkContext sc = new SparkContext(conf);
        sc.setLogLevel("ERROR");
        return new SparkSession(sc);
    }

    private CrossValidatorModel calculateCvModel(Dataset<Row> dataset) {

        PCA pca = new PCA().setInputCol("finalFeatures").setOutputCol("features");

        LinearRegression lr = new LinearRegression();
        Pipeline pipeline = new Pipeline().setStages(getPipeline(pca, lr, dataset));

        ParamMap[] paramGrid = new ParamGridBuilder()
                .addGrid(pca.k(), new int[] {220})
                .addGrid(lr.maxIter(), new int[] {100})
                .addGrid(lr.regParam(), new double[] {0.1})
                .addGrid(lr.elasticNetParam(), new double[] {0.25})
                .build();

        RegressionEvaluator evaluator = new RegressionEvaluator().setMetricName("r2");

        CrossValidator cv = new CrossValidator()
                .setEstimator(pipeline)
                .setEvaluator(evaluator)
                .setEstimatorParamMaps(paramGrid)
                .setNumFolds(5);

        return cv.fit(dataset);
    }

    private Dataset<Row> getDataset(SparkSession session, Path path) {
        return session.read()
                .option("header", true)
                .option("delimiter", ";")
                .option("inferSchema", true)
                .csv(path.toString());
    }

    private PipelineStage[] getPipeline(PCA pca, org.apache.spark.ml.regression.LinearRegression lr, Dataset<Row> dataset) {

        List<String> otherAttributes = new ArrayList<>();

        for (String field : dataset.schema().fieldNames()) {
            if(!field.equals("label") && !field.equals("thisTeam") && !field.equals("otherTeam")) {
                otherAttributes.add(field);
            }
        }

        StringIndexer stringIndexer = new StringIndexer()
                .setInputCols(new String[] {"thisTeam", "otherTeam"})
                .setOutputCols(new String[] {"thisTeam-i", "otherTeam-i"});

        OneHotEncoder oneHotEncoder = new OneHotEncoder()
                .setInputCols(new String[] {"thisTeam-i", "otherTeam-i"})
                .setOutputCols(new String[] {"thisTeam-b", "otherTeam-b"})
                .setDropLast(false);

        VectorAssembler vectorAssembler1 = new VectorAssembler()
                .setInputCols(otherAttributes.toArray(new String[0]))
                .setOutputCol("assembledColumns");

        StandardScaler scaler = new StandardScaler().setInputCol("assembledColumns")
                .setOutputCol("scaledFeatures")
                .setWithStd(true)
                .setWithMean(true);

        List<String> finalFields = new ArrayList<>();
        finalFields.add("thisTeam-b");
        finalFields.add("otherTeam-b");
        finalFields.add("scaledFeatures");

        VectorAssembler vectorAssembler2 = new VectorAssembler()
                .setInputCols(finalFields.toArray(new String[0]))
                .setOutputCol("finalFeatures");

        return new PipelineStage[] {
                stringIndexer,
                oneHotEncoder,
                vectorAssembler1,
                scaler,
                vectorAssembler2,
                pca,
                lr
        };
    }

    private void saveAllTeamsCombinations() {
        for (String competition : fileAccessor.competitionFolderNames()) {
            for (String season : fileAccessor.seasonFolderNames(competition)) {

                this.playerLastStats = fileAccessor.getPlayerLastStats(competition, season);
                this.teamLastStats = fileAccessor.getTeamLastStats(competition, season);
                this.lineupStats = fileAccessor.getLineupStats(competition, season);

                for (String team : fileAccessor.teamsFileNames(competition, season)) {
                    saveAllCombinationsOf(competition, season, team);
                }
            }
        }
    }

    private void saveAllCombinationsOf(String competition, String season, String team) {
        for (String otherTeam : fileAccessor.teamsFileNames(competition, season)) {
            if(!otherTeam.equals(team)) {
                saveCombinationsOf(competition, season, team, otherTeam);
            }
        }
    }

    private void saveCombinationsOf(String competition, String season, String thisTeamName, String otherTeamName) {
        Team thisTeam = fileAccessor.getTeam(competition, season, thisTeamName);
        Team otherTeam = fileAccessor.getTeam(competition, season, otherTeamName);
        List<Player> otherTeamLineup = startingLineupOf(otherTeam);

        saveCombinationsOf(competition, season, thisTeam, otherTeam, otherTeamLineup, new Integer[] {3, 4, 3});
        saveCombinationsOf(competition, season, thisTeam, otherTeam, otherTeamLineup, new Integer[] {3, 5, 2});
        saveCombinationsOf(competition, season, thisTeam, otherTeam, otherTeamLineup, new Integer[] {3, 6, 1});
        saveCombinationsOf(competition, season, thisTeam, otherTeam, otherTeamLineup, new Integer[] {4, 3, 3});
        saveCombinationsOf(competition, season, thisTeam, otherTeam, otherTeamLineup, new Integer[] {4, 4, 2});
        saveCombinationsOf(competition, season, thisTeam, otherTeam, otherTeamLineup, new Integer[] {4, 5, 1});
        saveCombinationsOf(competition, season, thisTeam, otherTeam, otherTeamLineup, new Integer[] {5, 3, 2});
        saveCombinationsOf(competition, season, thisTeam, otherTeam, otherTeamLineup, new Integer[] {5, 4, 1});
    }

    private void saveCombinationsOf(String competition, String season, Team thisTeam, Team otherTeam, List<Player> otherTeamLineup, Integer[] lineup) {

        List<List<Player>> defenseCombination = allCombinationsOf(lineup[0], thisTeam.players().stream()
                .filter(p -> p.matches().minutes() >= 350)
                .filter(p -> p.finalPosition() == Position.DF)
                .collect(Collectors.toList()));
        List<List<Player>> midfieldCombination = allCombinationsOf(lineup[1], thisTeam.players().stream()
                .filter(p -> p.matches().minutes() >= 350)
                .filter(p -> p.finalPosition() == Position.CC)
                .collect(Collectors.toList()));
        List<List<Player>> forwarderCombination = allCombinationsOf(lineup[2], thisTeam.players().stream()
                .filter(p -> p.matches().minutes() >= 350)
                .filter(p -> p.finalPosition() == Position.DL)
                .collect(Collectors.toList()));

        List<List<Player>> finalDefenseCombination = defenseCombination.stream()
                .sorted((l1, l2) -> Integer.compare(minutesOf(l2), minutesOf(l1)))
                .collect(Collectors.toList()).subList(0, Math.min(Math.min(lineup[0] + 2, 3), defenseCombination.size()));
        List<List<Player>> finalMidfielderCombination = midfieldCombination.stream()
                .sorted((l1, l2) -> Integer.compare(minutesOf(l2), minutesOf(l1)))
                .collect(Collectors.toList()).subList(0, Math.min(Math.min(lineup[1] + 2, 3), midfieldCombination.size()));
        List<List<Player>> finalForwarderCombination = forwarderCombination.stream()
                .sorted((l1, l2) -> Integer.compare(minutesOf(l2), minutesOf(l1)))
                .collect(Collectors.toList()).subList(0, Math.min(Math.min(lineup[2] + 2, 3), forwarderCombination.size()));

        List<PlayerCombination> playerCombinations = new ArrayList<>();

        for (Player goalkeeper : thisTeam.players().stream().filter(p -> p.matches().minutes() >= 350).filter(p -> p.finalPosition() == PT).collect(Collectors.toList())) {
            for (List<Player> defenses : finalDefenseCombination) {
                for (List<Player> midfielders : finalMidfielderCombination) {
                    for (List<Player> forwarders : finalForwarderCombination) {
                        List<Player> thisPlayers = new ArrayList<>();
                        thisPlayers.add(goalkeeper);
                        thisPlayers.addAll(defenses);
                        thisPlayers.addAll(midfielders);
                        thisPlayers.addAll(forwarders);
                        PlayerCombination line = playerCombinationOf(thisTeam, otherTeam, thisPlayers, otherTeamLineup, lineupOf(lineup));
                        if(line != null) {
                            playerCombinations.add(line);
                        }
                    }
                }
            }
        }

        for (PlayerCombination playerCombination : playerCombinations) {
            this.fileAccessor.savePlayerCombinationToTest(competition, season, playerCombination);
        }
    }

    private String lineupOf(Integer[] lineup) {
        return lineup[0] + "-" + lineup[1] + "-" + lineup[2];
    }

    private int minutesOf(List<Player> lineup) {
        return lineup.stream().mapToInt(p -> p.matches().minutes()).sum();
    }

    private PlayerCombination playerCombinationOf(Team thisTeam, Team otherTeam, List<Player> thisPlayers, List<Player> otherPlayers, String lineup) {

        if(thisPlayers.stream().map(p -> lineupStats.get(p.id())).anyMatch(Objects::isNull)) return null;
        if(otherPlayers.stream().map(p -> lineupStats.get(p.id())).anyMatch(Objects::isNull)) return null;

        double homeLastGoalDifference = thisPlayers.stream().mapToDouble(p -> lineupStats.get(p.id()).get()).average().orElse(0);
        double awayLastGoalDifference = otherPlayers.stream().mapToDouble(p -> lineupStats.get(p.id()).get()).average().orElse(0);

        return new PlayerCombination()
                .addPlayerStats(getPlayerMatchStatsOf(thisPlayers), getPlayerMatchStatsOf(otherPlayers))
                .addTeamsStreak(teamLastStats.get(thisTeam.name()), teamLastStats.get(otherTeam.name()))
                .addTeams(thisTeam.name(), otherTeam.name())
                .addLastGoalDifference(homeLastGoalDifference, awayLastGoalDifference)
                .addAttribute("lineup", lineup)
                .addAttribute("players", thisPlayers.stream().map(Player::id).collect(Collectors.toList()));
    }

    private Map<Player, List<PlayerStats>> getPlayerMatchStatsOf(List<Player> players) {

        Map<Player, List<PlayerStats>> completeStats = new HashMap<>();

        for (Player player : players) {
            completeStats.put(player, playerLastStats.get(player.id()));
        }

        return completeStats;
    }

    private List<List<Player>> allCombinationsOf(int number, List<Player> players) {

        List<List<Player>> combinations = new ArrayList<>();

        if(number > players.size()) return combinations;

        if(number == 1) {
            for (Player player : players) {
                combinations.add(List.of(player));
            }
            return combinations;
        }

        for (int i = 0; i < players.size(); i++) {

            List<List<Player>> subCombinations = allCombinationsOf(number - 1, players.subList(i + 1, players.size()));
            for (List<Player> playerSubCombination : subCombinations) {
                List<Player> newCombination = new ArrayList<>();
                newCombination.add(players.get(i));
                newCombination.addAll(playerSubCombination);
                combinations.add(newCombination);
            }
        }

        return combinations;
    }

    private List<Player> startingLineupOf(Team team) {
        List<Player> players = team.players().stream()
                .sorted((p1, p2) -> Integer.compare(p2.matches().starters(), p1.matches().starters())).collect(Collectors.toList());
        return firstLineupOf(players);
    }

    private List<Player> firstLineupOf(List<Player> players) {

        List<Player> finalLineup = new ArrayList<>();
        add(players, finalLineup, 1, Position.PT);
        add(players, finalLineup, 3, DF);
        add(players, finalLineup, 3, CC);
        add(players, finalLineup, 1, DL);

        int restDf = 2;
        int resCc = 3;
        int resDl = 2;
        int res = 3;

        for (Player player : players) {
            if(res == 0) break;
            switch (player.finalPosition()) {
                case DF:
                    if(restDf > 0) {
                        finalLineup.add(player);
                        restDf--;
                        res--;
                    }
                    break;
                case CC:
                    if(resCc > 0) {
                        finalLineup.add(player);
                        resCc--;
                        res--;
                    }
                    break;
                case DL:
                    if(resDl > 0) {
                        finalLineup.add(player);
                        resDl--;
                        res--;
                    }
            }
        }

        finalLineup.sort((p1, p2) -> {
            int compare = compare(p1.finalPosition(), p2.finalPosition());
            if(compare == 0) {
                return p1.name().compareTo(p2.name());
            } else {
                return compare;
            }
        });

        return finalLineup;
    }

    private void add(List<Player> players, List<Player> finalLineup, int n, Position position) {
        List<Player> removed = new ArrayList<>();
        for (Player player : players) {
            if(n == 0) break;
            if(player.finalPosition() == position) {
                finalLineup.add(player);
                removed.add(player);
                n--;
            }
        }
        for (Player player : removed) {
            players.remove(player);
        }
    }
}

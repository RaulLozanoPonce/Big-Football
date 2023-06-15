package ui1.raullozano.bigfootball.etl.transformator.ml;

import com.google.gson.Gson;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.feature.PCA;
import org.apache.spark.ml.feature.StandardScaler;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.ml.tuning.CrossValidator;
import org.apache.spark.ml.tuning.CrossValidatorModel;
import org.apache.spark.ml.tuning.ParamGridBuilder;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.model.transformator.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.*;
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

        //Map<String, List<Path>> teamCombinations = getAllTeamsCombinations();

        SparkSession session = getSession();
        Dataset<Row> dataset = getDataset(session, fileAccessor.getPlayerCombinationsFilePath());
        CrossValidatorModel cvModel = calculateCvModel(dataset);

        Dataset<Row> teamCombinationDataset = getDataset(session, Path.of("./temp/ml/players.csv").toString());
        Dataset<Row> transform = cvModel.transform(teamCombinationDataset);

        List<Row> predictions = transform.select("this-team", "other-team", "lineup", "players", "prediction").collectAsList();
        Map<String, List<Row>> predictionsByTeam = predictions.stream().collect(Collectors.groupingBy(r -> r.getString(0)));

        for (String team : predictionsByTeam.keySet()) {
            Map<String, List<Row>> predictionsByOtherTeam = predictionsByTeam.get(team).stream().collect(Collectors.groupingBy(r -> r.getString(1)));
            for (String otherTeam : predictionsByOtherTeam.keySet()) {
                Map<String, List<Row>> predictionsByLineup = predictionsByOtherTeam.get(otherTeam).stream().collect(Collectors.groupingBy(r -> r.getString(2)));
                for (String lineup : predictionsByLineup.keySet()) {

                    List<Row> lineupPredictions = predictionsByLineup.get(lineup);

                    int nRow = 0;
                    Double bestPrediction = null;

                    for (int i = 0; i < lineupPredictions.size(); i++) {
                        if(bestPrediction == null || lineupPredictions.get(i).getDouble(4) > bestPrediction) {
                            nRow = i;
                            bestPrediction = lineupPredictions.get(i).getDouble(4);
                        }
                    }

                    try {
                        Files.writeString(Path.of("./temp/ml/best.csv"), (team + ";" + otherTeam + ";" + lineup + ";" + bestPrediction + ";" + lineupPredictions.get(nRow).getString(3) + "\n"), WRITE, CREATE, APPEND);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }
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
        Dataset<Row>[] splits = dataset.randomSplit(new double[] {0.7, 0.3});
        Dataset<Row> trainingData = splits[0];
        Dataset<Row> testData = splits[1];

        PCA pca = new PCA().setInputCol("transformedFeatures").setOutputCol("features");

        LinearRegression lr = new LinearRegression();
        Pipeline pipeline = new Pipeline().setStages(getPipeline(pca, lr, dataset));

        ParamMap[] paramGrid = new ParamGridBuilder()
                .addGrid(pca.k(), new int[] {220})
                .addGrid(lr.maxIter(), new int[] {100})
                .addGrid(lr.regParam(), new double[] {0.1})
                .addGrid(lr.elasticNetParam(), new double[] {0.25})
                .addGrid(lr.aggregationDepth(), new int[] {2})
                .build();

        RegressionEvaluator evaluator = new RegressionEvaluator().setMetricName("r2");

        CrossValidator cv = new CrossValidator()
                .setEstimator(pipeline)
                .setEvaluator(evaluator)
                .setEstimatorParamMaps(paramGrid)
                .setNumFolds(5);

        return cv.fit(trainingData);
    }

    private Dataset<Row> getDataset(SparkSession session, String path) {
        return session.read()
                .option("header", true)
                .option("delimiter", ";")
                .option("inferSchema", true)
                .csv(path);
    }

    private PipelineStage[] getPipeline(PCA pca, org.apache.spark.ml.regression.LinearRegression lr, Dataset<Row> dataset) {

        VectorAssembler vectorAssembler = new VectorAssembler()
                .setInputCols(Arrays.stream(dataset.schema().fieldNames()).filter(f -> !f.equals("label")).toArray(String[]::new))
                .setOutputCol("assembledColumns");

        StandardScaler scaler = new StandardScaler().setInputCol("assembledColumns")
                .setOutputCol("transformedFeatures")
                .setWithStd(true)
                .setWithMean(true);

        return new PipelineStage[] {vectorAssembler, scaler, pca, lr};
    }

    private Map<String, List<Path>> getAllTeamsCombinations() {

        Map<String, List<Path>> teamCombinationPaths = new HashMap<>();

        //for (String competition : fileAccessor.competitionFolderNames()) {
            //for (String season : fileAccessor.seasonFolderNames(competition)) {
        String competition = "Premier League";
        String season = "2021";

                this.playerLastStats = fileAccessor.getPlayerLastStats(competition, season);
                this.teamLastStats = fileAccessor.getTeamLastStats(competition, season);
                this.lineupStats = fileAccessor.getLineupStats(competition, season);

                for (String team : fileAccessor.teamsFileNames(competition, season)) {
                    teamCombinationPaths.putAll(getAllCombinationsOf(competition, season, team));
                }
            //}
        //}
        return teamCombinationPaths;
    }

    private Map<String, List<Path>> getAllCombinationsOf(String competition, String season, String team) {

        Map<String, List<Path>> teamCombinationPaths = new HashMap<>();

        for (String otherTeam : fileAccessor.teamsFileNames(competition, season)) {
            if(!otherTeam.equals(team)) {
                teamCombinationPaths.put(team + " - " + otherTeam, getCombinationsOf(competition, season, team, otherTeam));
            }
        }

        return teamCombinationPaths;
    }

    private List<Path> getCombinationsOf(String competition, String season, String thisTeamName, String otherTeamName) {
        Team thisTeam = fileAccessor.getTeam(competition, season, thisTeamName);
        Team otherTeam = fileAccessor.getTeam(competition, season, otherTeamName);
        List<Player> otherTeamLineup = startingLineupOf(otherTeam);

        List<Path> combinationPaths = new ArrayList<>();
        combinationPaths.add(getCombinationsOf(thisTeam, otherTeam, otherTeamLineup, new Integer[] {3, 4, 3}, season));
        combinationPaths.add(getCombinationsOf(thisTeam, otherTeam, otherTeamLineup, new Integer[] {4, 3, 3}, season));
        combinationPaths.add(getCombinationsOf(thisTeam, otherTeam, otherTeamLineup, new Integer[] {5, 3, 2}, season));
        return combinationPaths;
    }

    private Path getCombinationsOf(Team thisTeam, Team otherTeam, List<Player> otherTeamLineup, Integer[] lineup, String season) {

        Path path = Path.of("./temp/ml/players.csv");

        List<List<Player>> defenseCombination = allCombinationsOf(lineup[0], thisTeam.players().stream().filter(p -> p.finalPosition() == Position.DF)
                .collect(Collectors.toList()));
        List<List<Player>> midfieldCombination = allCombinationsOf(lineup[1], thisTeam.players().stream().filter(p -> p.finalPosition() == Position.CC)
                .collect(Collectors.toList()));
        List<List<Player>> forwarderCombination = allCombinationsOf(lineup[2], thisTeam.players().stream().filter(p -> p.finalPosition() == Position.DL)
                .collect(Collectors.toList()));

        List<List<Player>> finalDefenseCombination = defenseCombination.stream()
                .sorted((l1, l2) -> Integer.compare(minutesOf(l2), minutesOf(l1)))
                .collect(Collectors.toList()).subList(0, Math.min(Math.min(lineup[0] + 1, 3), defenseCombination.size()));
        List<List<Player>> finalMidfielderCombination = midfieldCombination.stream()
                .sorted((l1, l2) -> Integer.compare(minutesOf(l2), minutesOf(l1)))
                .collect(Collectors.toList()).subList(0, Math.min(Math.min(lineup[1] + 1, 3), midfieldCombination.size()));
        List<List<Player>> finalForwarderCombination = forwarderCombination.stream()
                .sorted((l1, l2) -> Integer.compare(minutesOf(l2), minutesOf(l1)))
                .collect(Collectors.toList()).subList(0, Math.min(Math.min(lineup[2] + 1, 3), forwarderCombination.size()));

        List<PlayerCombination> lines = new ArrayList<>();
        List<LineupCombination> combinations = new ArrayList<>();

        for (Player goalkeeper : thisTeam.players().stream().filter(p -> p.finalPosition() == PT).collect(Collectors.toList())) {
            for (List<Player> defenses : finalDefenseCombination) {
                for (List<Player> midfielders : finalMidfielderCombination) {
                    for (List<Player> forwarders : finalForwarderCombination) {
                        List<Player> thisPlayers = new ArrayList<>();
                        thisPlayers.add(goalkeeper);
                        thisPlayers.addAll(defenses);
                        thisPlayers.addAll(midfielders);
                        thisPlayers.addAll(forwarders);
                        PlayerCombination line = lineOf(thisTeam, otherTeam, thisPlayers, otherTeamLineup);
                        if(line != null) {
                            lines.add(line);
                            combinations.add(new LineupCombination(thisPlayers.stream().map(Player::id).collect(Collectors.toList()), lineupOf(lineup), otherTeam.name()));
                        }
                    }
                }
            }
        }

        if(lines.size() > 0) {
            try {
                Files.createDirectories(path.getParent());
                if (!Files.exists(path)) Files.writeString(path, "this-team;other-team;lineup;players;" + lines.get(0).header(), CREATE, APPEND, WRITE);
                for (int i = 0; i < lines.size(); i++) {
                    Files.writeString(path,
                            thisTeam.name() + ";" +
                                    otherTeam.name() + ";" +
                                    lineupOf(lineup) + ";" +
                                    new Gson().toJson(combinations.get(0).players) + ";" +
                                    lines.get(i).toString(),
                            CREATE, APPEND, WRITE);

                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        return path;
    }

    private String lineupOf(Integer[] lineup) {
        return lineup[0] + "-" + lineup[1] + "-" + lineup[2];
    }

    private int minutesOf(List<Player> lineup) {
        return lineup.stream().mapToInt(p -> p.matches().minutes()).sum();
    }

    private PlayerCombination lineOf(Team thisTeam, Team otherTeam, List<Player> thisPlayers, List<Player> otherPlayers) {

        if(thisPlayers.stream().map(p -> lineupStats.get(p.id())).anyMatch(Objects::isNull)) return null;
        if(otherPlayers.stream().map(p -> lineupStats.get(p.id())).anyMatch(Objects::isNull)) return null;

        double homeLastGoalDifference = thisPlayers.stream().mapToDouble(p -> lineupStats.get(p.id()).get()).average().orElse(0);
        double awayLastGoalDifference = otherPlayers.stream().mapToDouble(p -> lineupStats.get(p.id()).get()).average().orElse(0);

        return new PlayerCombination()
                .addPlayerStats(getPlayerMatchStatsOf(thisPlayers), getPlayerMatchStatsOf(otherPlayers))
                .addTeamsStreak(teamLastStats.get(thisTeam.name()), teamLastStats.get(otherTeam.name()))
                //TODO
                //.addTeams()
                .addLastGoalDifference(homeLastGoalDifference, awayLastGoalDifference);
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

    public static class LineupCombination {

        private final List<String> players;
        private final String schema;
        private final String otherTeam;
        private double goalDifference;

        public LineupCombination(List<String> players, String schema, String otherTeam) {
            this.players = players;
            this.schema = schema;
            this.otherTeam = otherTeam;
        }

        public LineupCombination goalDifference(double goalDifference) {
            this.goalDifference = goalDifference;
            return this;
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }
}

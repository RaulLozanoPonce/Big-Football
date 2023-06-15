package ui1.raullozano.bigfootball.bigfootball.api;

import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.model.transformator.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.*;
import static ui1.raullozano.bigfootball.common.model.transformator.Position.*;

public class BestLineupApi {

    private final FileAccessor fileAccessor;
    private final Map<String, String> queryParams;
    private final Team thisTeam;
    private final Team otherTeam;
    private final Map<String, List<PlayerStats>> playerLastStats;
    private final Map<String, List<Integer>> teamLastStats;
    private final Map<String, LineupStats> lineupStats;

    public BestLineupApi(FileAccessor fileAccessor, Map<String, String> queryParams) {
        this.fileAccessor = fileAccessor;
        this.queryParams = queryParams;
        this.thisTeam = fileAccessor.getTeam(queryParams.get("competition"), queryParams.get("season"), queryParams.get("this-team"));
        this.otherTeam = fileAccessor.getTeam(queryParams.get("competition"), queryParams.get("season"), queryParams.get("other-team"));
        this.playerLastStats = fileAccessor.getPlayerLastStats(queryParams.get("competition"), queryParams.get("season"));
        this.teamLastStats = fileAccessor.getTeamLastStats(queryParams.get("competition"), queryParams.get("season"));
        this.lineupStats = fileAccessor.getLineupStats(queryParams.get("competition"), queryParams.get("season"));
    }

    public String getResponse() {

        LineupComponent otherTeamLineup = startingLineupOf(otherTeam);

        Integer[] lineup = new Integer[] {4, 3, 3};

        Path path = Path.of("./temp/players.csv");

        int i = 0;

        List<List<Player>> defenseCombination = allCombinationsOf(4, thisTeam.players().stream().filter(p -> p.finalPosition() == Position.DF).collect(Collectors.toList()));
        List<List<Player>> midfieldCombination = allCombinationsOf(3, thisTeam.players().stream().filter(p -> p.finalPosition() == Position.CC).collect(Collectors.toList()));
        List<List<Player>> forwarderCombination = allCombinationsOf(3, thisTeam.players().stream().filter(p -> p.finalPosition() == Position.DL).collect(Collectors.toList()));

        for (Player goalkeeper : thisTeam.players().stream().filter(p -> p.finalPosition() == PT).collect(Collectors.toList())) {
            for (List<Player> defenses : defenseCombination.subList(0, 10)) {
                for (List<Player> midfielders : midfieldCombination.subList(0, 10)) {
                    for (List<Player> forwarders : forwarderCombination.subList(0, 10)) {
                        List<Player> thisPlayers = new ArrayList<>();
                        thisPlayers.add(goalkeeper);
                        thisPlayers.addAll(defenses);
                        thisPlayers.addAll(midfielders);
                        thisPlayers.addAll(forwarders);
                        PlayerCombination line = lineOf(thisPlayers, otherTeamLineup);
                        if(line != null) {
                            try {
                                System.out.println(i);
                                if (!Files.exists(path)) Files.writeString(path, line.header(), CREATE, APPEND, WRITE);
                                Files.writeString(path, line.toString(), CREATE, APPEND, WRITE);
                                i++;
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        return "ok";
    }

    private PlayerCombination lineOf(List<Player> thisPlayers, LineupComponent otherTeamLineup) {

        if(thisPlayers.stream().map(p -> lineupStats.get(p.id())).anyMatch(Objects::isNull)) return null;
        if(otherTeamLineup.players.stream().map(p -> lineupStats.get(p.id())).anyMatch(Objects::isNull)) return null;

        double homeLastGoalDifference = thisPlayers.stream().mapToDouble(p -> lineupStats.get(p.id()).get()).average().orElse(0);
        double awayLastGoalDifference = otherTeamLineup.players.stream().mapToDouble(p -> lineupStats.get(p.id()).get()).average().orElse(0);

        return new PlayerCombination()
                .addPlayerStats(getPlayerMatchStatsOf(thisPlayers), getPlayerMatchStatsOf(otherTeamLineup.players))
                .addTeamsStreak(teamLastStats.get(thisTeam.name()), teamLastStats.get(otherTeam.name()))
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

    private LineupComponent startingLineupOf(Team team) {
        List<Player> players = team.players().stream()
                .sorted((p1, p2) -> Integer.compare(p2.matches().starters(), p1.matches().starters())).collect(Collectors.toList());
        return firstLineupOf(players);
    }

    private LineupComponent firstLineupOf(List<Player> players) {

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

        return new LineupComponent(finalLineup);
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

    private static class LineupComponent {

        private final List<Player> players;
        private final Map<String, Double> attributes = new HashMap<>();

        public LineupComponent(List<Player> players) {
            this.players = players;
        }

        public LineupComponent(List<Player> players, Integer minutes) {
            this.players = players;
            this.attributes.put("minutes", Double.valueOf(minutes));
        }

        public LineupComponent(List<Player> players, Integer minutes, Double goals) {
            this.players = players;
            this.attributes.put("minutes", Double.valueOf(minutes));
            this.attributes.put("goals", goals);
        }

        public LineupComponent(List<Player> players, Double score) {
            this.players = players;
            this.attributes.put("score", score);
        }

        public Double minutes() {
            return this.attributes.get("minutes");
        }

        public Double goals() {
            return this.attributes.get("goals");
        }

        public Double score() {
            return this.attributes.get("score");
        }
    }
}

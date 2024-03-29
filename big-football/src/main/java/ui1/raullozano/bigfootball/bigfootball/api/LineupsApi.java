package ui1.raullozano.bigfootball.bigfootball.api;

import com.google.gson.Gson;
import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.model.transformator.Player;
import ui1.raullozano.bigfootball.common.model.transformator.Position;
import ui1.raullozano.bigfootball.common.model.transformator.Team;

import java.util.*;
import java.util.stream.Collectors;

import static ui1.raullozano.bigfootball.common.model.transformator.Position.compare;

public class LineupsApi {

    private final Team team;
    private final int[] lineup;

    public LineupsApi(FileAccessor fileAccessor, Map<String, String> params) {
        this.team = fileAccessor.getTeam(params.get("competition"), params.get("season"), params.get("team"));
        this.lineup = lineupOf(params.get("lineup"));
    }

    private int[] lineupOf(String lineupParam) {
        int[] lineup = new int[4];
        lineup[0] = 1;

        String[] lineupComponents = lineupParam.split("-");

        for (int i = 0; i < lineupComponents.length; i++) {
            lineup[i+1] = Integer.parseInt(lineupComponents[i]);
        }

        return lineup;
    }

    public String getResponse() {

        if(team == null) return "{}";

        LineupsTempleteResponse response = new LineupsTempleteResponse()
                .bestDefensiveLineup(bestDefensiveLineupOf(team))
                .bestPassingLineup(bestPassingLineupOf(team))
                .bestAttackingLineup(bestAttackingLineupOf(team))
                .bestFoulsLineup(bestFoulsLineupOf(team));
        return new Gson().toJson(response);
    }

    private LineupsTempleteResponse.LineupComponent bestDefensiveLineupOf(Team team) {
        List<Player> players = team.players().stream()
                .filter(p -> p.matches().minutes() >= 400)
                .sorted((p1, p2) -> {
                    double p1Factor = (p1.matches().tacklesFactor() + p1.matches().challengesFactor() + p1.matches().aerialDuelsFactor())/3.0;
                    double p2Factor = (p2.matches().tacklesFactor() + p2.matches().challengesFactor() + p2.matches().aerialDuelsFactor())/3.0;

                    if (p1Factor > p2Factor) {
                        return -1;
                    } else if(p1Factor < p2Factor) {
                        return 1;
                    } else {
                        return Integer.compare(p1.matches().minutes(), p2.matches().minutes());
                    }
                }).collect(Collectors.toList());
        return firstLineupOf(players);
    }

    private LineupsTempleteResponse.LineupComponent bestPassingLineupOf(Team team) {
        List<Player> players = team.players().stream()
                .filter(p -> p.matches().minutes() >= 400)
                .sorted((p1, p2) -> {
                    if (p1.matches().passesFactor() > p2.matches().passesFactor()) {
                        return -1;
                    } else if(p1.matches().passesFactor() < p2.matches().passesFactor()) {
                        return 1;
                    } else {
                        return Integer.compare(p1.matches().minutes(), p2.matches().minutes());
                    }
                }).collect(Collectors.toList());
        return firstLineupOf(players);
    }

    private LineupsTempleteResponse.LineupComponent bestAttackingLineupOf(Team team) {
        List<Player> players = team.players().stream()
                .filter(p -> p.matches().minutes() >= 400)
                .sorted((p1, p2) -> {
                    if (p1.matches().xGFactor() > p2.matches().xGFactor()) {
                        return -1;
                    } else if(p1.matches().xGFactor() < p2.matches().xGFactor()) {
                        return 1;
                    } else {
                        return Integer.compare(p1.matches().minutes(), p2.matches().minutes());
                    }
                }).collect(Collectors.toList());
        return firstLineupOf(players);
    }

    private LineupsTempleteResponse.LineupComponent bestFoulsLineupOf(Team team) {
        List<Player> players = team.players().stream()
                .filter(p -> p.matches().minutes() >= 400)
                .sorted((p1, p2) -> {
                    if (p1.matches().foulsFactor() > p2.matches().foulsFactor()) {
                        return -1;
                    } else if(p1.matches().foulsFactor() < p2.matches().foulsFactor()) {
                        return 1;
                    } else {
                        return Integer.compare(p1.matches().minutes(), p2.matches().minutes());
                    }
                }).collect(Collectors.toList());
        return firstLineupOf(players);
    }

    private LineupsTempleteResponse.LineupComponent firstLineupOf(List<Player> players) {

        try {
            Map<Position, List<Player>> playersByPosition = new LinkedHashMap<>();
            for (Player player : players) {
                playersByPosition.putIfAbsent(player.finalPosition(), new ArrayList<>());
                playersByPosition.get(player.finalPosition()).add(player);
            }

            List<Player> returnPlayers = new ArrayList<>();

            returnPlayers.addAll(playersByPosition.get(Position.PT).subList(0, this.lineup[0]));
            returnPlayers.addAll(playersByPosition.get(Position.DF).subList(0, this.lineup[1]));
            returnPlayers.addAll(playersByPosition.get(Position.CC).subList(0, this.lineup[2]));
            returnPlayers.addAll(playersByPosition.get(Position.DL).subList(0, this.lineup[3]));

            returnPlayers = returnPlayers.stream().sorted((p1, p2) -> {
                int compare = compare(p1.finalPosition(), p2.finalPosition());
                if (compare == 0) {
                    return p1.name().compareTo(p2.name());
                } else {
                    return compare;
                }
            }).collect(Collectors.toList());

            return new LineupsTempleteResponse.LineupComponent(returnPlayers);
        } catch (IndexOutOfBoundsException e) {
            return new LineupsTempleteResponse.LineupComponent(new ArrayList<>());
        }
    }

    private static class LineupsTempleteResponse {

        private List<PlayerSquad> bestDefensiveLineup;
        private List<PlayerSquad> bestPassingLineup;
        private List<PlayerSquad> bestAttackingLineup;
        private List<PlayerSquad> bestFoulsLineup;

        public LineupsTempleteResponse bestDefensiveLineup(LineupComponent lineupComponent) {
            this.bestDefensiveLineup = lineupComponent.players.stream().map(PlayerSquad::new).collect(Collectors.toList());
            return this;
        }

        public LineupsTempleteResponse bestPassingLineup(LineupComponent lineupComponent) {
            this.bestPassingLineup = lineupComponent.players.stream().map(PlayerSquad::new).collect(Collectors.toList());
            return this;
        }

        public LineupsTempleteResponse bestAttackingLineup(LineupComponent lineupComponent) {
            this.bestAttackingLineup = lineupComponent.players.stream().map(PlayerSquad::new).collect(Collectors.toList());
            return this;
        }

        public LineupsTempleteResponse bestFoulsLineup(LineupComponent lineupComponent) {
            this.bestFoulsLineup = lineupComponent.players.stream().map(PlayerSquad::new).collect(Collectors.toList());
            return this;
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

        public static class PlayerSquad {

            private final String id;
            private final String name;
            private final Position position;
            private final int age;
            private final int playedMatches;
            private final int minutes;
            private final int goals;
            private final int assists;
            private final int starters;

            public PlayerSquad(Player player) {
                this.id = player.id();
                this.name = player.name();
                this.position = player.finalPosition();
                this.age = player.age();
                this.playedMatches = player.matches().playedMatches();
                this.minutes = player.matches().minutes();
                this.goals = player.matches().goals();
                this.assists = player.matches().assists();
                this.starters = player.matches().starters();
            }
        }
    }
}

package ui1.raullozano.bigfootball.bigfootball.api;

import com.google.gson.Gson;
import ui1.raullozano.bigfootball.bigfootball.Maths;
import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.model.transformator.LineupStatistics;
import ui1.raullozano.bigfootball.common.model.transformator.Player;
import ui1.raullozano.bigfootball.common.model.transformator.Position;
import ui1.raullozano.bigfootball.common.model.transformator.Team;

import java.util.*;
import java.util.stream.Collectors;

import static ui1.raullozano.bigfootball.common.model.transformator.Position.*;

public class StatisticsApi {

    private final Team team;
    private double maxMinutes = 0;
    private double maxGoalsForByMinute = 0;
    private double maxGoalsAgainsByMinute = 0;
    private double maxWonMinutes = 0;

    public StatisticsApi(FileAccessor fileAccessor, Map<String, String> params) {
        this.team = fileAccessor.getTeam(params.get("competition"), params.get("season"), params.get("team"));
        initMaxValues(fileAccessor.getTeams(params.get("competition"), params.get("season")));
    }

    private void initMaxValues(List<Team> teams) {
        for (Team team: teams) {
            this.maxMinutes = Math.max(this.maxMinutes, team.lineupStatistics().values().stream().mapToDouble(LineupStatistics::minutes).max().orElse(0));
            this.maxGoalsForByMinute = Math.max(this.maxGoalsForByMinute, team.lineupStatistics().values().stream().mapToDouble(v -> v.goalsFor() / (double) v.minutes()).max().orElse(0));
            this.maxGoalsAgainsByMinute = Math.max(this.maxGoalsAgainsByMinute, team.lineupStatistics().values().stream().mapToDouble(v -> v.goalsAgainst() / (double) v.minutes()).max().orElse(0));
            this.maxWonMinutes = Math.max(this.maxWonMinutes, team.lineupStatistics().values().stream().mapToDouble(LineupStatistics::wonMinutes).max().orElse(0));
        }
    }

    public String getResponse() {
        if(team == null) return "{}";
        StatisticsTemplateResponse response = new StatisticsTemplateResponse()
                .regularLineup(regularLineupOf(team))
                .startingLineup(startingLineupOf(team))
                .moreGoalsForByMinuteLineup(moreGoalsForByMinuteLineupOf(team))
                .lessGoalsAgainstByMinuteLineup(lessGoalsAgainstByMinuteLineupOf(team))
                .moreWonMinutesLineup(moreWonMinutesLineupOf(team))
                .bestLineup(bestLineupOf(team));
        return new Gson().toJson(response);
    }

    private StatisticsTemplateResponse.LineupComponent regularLineupOf(Team team) {
        return team.lineupStatistics().entrySet().stream()
                .reduce((e1, e2) -> {
                    if (e1.getValue().minutes() > e2.getValue().minutes()) {
                        return e1;
                    } else {
                        return e2;
                    }
                }).map(e -> new StatisticsTemplateResponse.LineupComponent(playersOf(team, e.getKey()), e.getValue().minutes()))
                .orElse(new StatisticsTemplateResponse.LineupComponent(new ArrayList<>(), 0, 0.0));
    }

    private StatisticsTemplateResponse.LineupComponent startingLineupOf(Team team) {
        List<Player> players = team.players().stream()
                .sorted((p1, p2) -> Integer.compare(p2.matches().starters(), p1.matches().starters())).collect(Collectors.toList());
        return firstLineupOf(players);
    }

    private StatisticsTemplateResponse.LineupComponent moreGoalsForByMinuteLineupOf(Team team) {
        return team.lineupStatistics().entrySet().stream()
                .filter(e -> e.getValue().minutes() >= 30)
                .reduce((e1, e2) -> {
                    double e1GoalsByMinute = e1.getValue().goalsFor() / (double) e1.getValue().minutes();
                    double e2GoalsByMinute = e2.getValue().goalsFor() / (double) e2.getValue().minutes();

                    if (e1GoalsByMinute > e2GoalsByMinute) {
                        return e1;
                    } else if(e1GoalsByMinute < e2GoalsByMinute) {
                        return e2;
                    } else {
                        if (e1.getValue().minutes() < e2.getValue().minutes()) {
                            return e1;
                        } else {
                            return e2;
                        }
                    }
                }).map(e -> new StatisticsTemplateResponse.LineupComponent(
                        playersOf(team, e.getKey()),
                        e.getValue().minutes(),
                        e.getValue().goalsFor() / (double) e.getValue().minutes()
                )).orElse(new StatisticsTemplateResponse.LineupComponent(new ArrayList<>(), 0, 0.0));
    }

    private StatisticsTemplateResponse.LineupComponent lessGoalsAgainstByMinuteLineupOf(Team team) {
        return team.lineupStatistics().entrySet().stream()
                .filter(e -> e.getValue().minutes() >= 70)
                .reduce((e1, e2) -> {
                    double e1GoalsByMinute = e1.getValue().goalsAgainst() / (double) e1.getValue().minutes();
                    double e2GoalsByMinute = e2.getValue().goalsAgainst() / (double) e2.getValue().minutes();

                    if (e1GoalsByMinute < e2GoalsByMinute) {
                        return e1;
                    } else if(e1GoalsByMinute > e2GoalsByMinute) {
                        return e2;
                    } else {
                        if (e1.getValue().minutes() > e2.getValue().minutes()) {
                            return e1;
                        } else {
                            return e2;
                        }
                    }
                }).map(e -> new StatisticsTemplateResponse.LineupComponent(
                        playersOf(team, e.getKey()),
                        e.getValue().minutes(),
                        e.getValue().goalsAgainst() / (double) e.getValue().minutes()
                )).orElse(new StatisticsTemplateResponse.LineupComponent(new ArrayList<>(), 0, 0.0));
    }

    private StatisticsTemplateResponse.LineupComponent moreWonMinutesLineupOf(Team team) {
        return team.lineupStatistics().entrySet().stream()
                .reduce((e1, e2) -> {
                    if (e1.getValue().wonMinutes() > e2.getValue().wonMinutes()) {
                        return e1;
                    } else {
                        return e2;
                    }
                }).map(e -> new StatisticsTemplateResponse.LineupComponent(playersOf(team, e.getKey()), e.getValue().wonMinutes()))
                .orElse(new StatisticsTemplateResponse.LineupComponent(new ArrayList<>(), 0, 0.0));
    }

    private StatisticsTemplateResponse.LineupComponent bestLineupOf(Team team) {
        return team.lineupStatistics().entrySet().stream()
                .reduce((e1, e2) -> {
                    if (scoreOf(e1.getValue()) > scoreOf(e2.getValue())) {
                        return e1;
                    } else {
                        return e2;
                    }
                }).map(e -> new StatisticsTemplateResponse.LineupComponent(playersOf(team, e.getKey()), scoreOf(e.getValue())))
                .orElse(new StatisticsTemplateResponse.LineupComponent(new ArrayList<>(), 0, 0.0));
    }

    private double scoreOf(LineupStatistics value) {
        return (10 * value.minutes() / maxMinutes) +
                (25 * (value.goalsFor() / (value.minutes() * maxGoalsForByMinute))) +
                (25 * (1 - (value.goalsAgainst() / (value.minutes() * maxGoalsAgainsByMinute)))) +
                (40 * value.wonMinutes() / maxWonMinutes);
    }

    private List<Player> playersOf(Team team, String playerIds) {
        List<String> playerIdsList = List.of(playerIds.substring(1, playerIds.length() - 1).replaceAll(",", "").split(" "));
        return playerIdsList.stream()
                .map(pid -> team.players().stream()
                        .filter(p -> p.id().equals(pid))
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .sorted((p1, p2) -> {
                    int compare = compare(p1.finalPosition(), p2.finalPosition());
                    if(compare == 0) {
                        return p1.name().compareTo(p2.name());
                    } else {
                        return compare;
                    }
                })
                .collect(Collectors.toList());
    }

    private StatisticsTemplateResponse.LineupComponent firstLineupOf(List<Player> players) {

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

        return new StatisticsTemplateResponse.LineupComponent(finalLineup);
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

    private static class StatisticsTemplateResponse {

        private List<PlayerSquad> regularLineup;
        private Double regularLineupMinutes;
        private List<PlayerSquad> startingLineup;
        private List<PlayerSquad> moreGoalsForByMinuteLineup;
        private Double moreGoalsForByMinuteLineupMinutes;
        private Double moreGoalsForByMinuteLineupGoals;
        private List<PlayerSquad> lessGoalsAgainstByMinuteLineup;
        private Double lessGoalsAgainstByMinuteLineupMinutes;
        private Double lessGoalsAgainstByMinuteLineupGoals;
        private List<PlayerSquad> moreWonMinutesLineup;
        private Double moreWonMinutesLineupMinutes;
        private List<PlayerSquad> bestLineup;
        private Double bestLineupScore;

        public StatisticsTemplateResponse regularLineup(LineupComponent lineupComponent) {
            this.regularLineup = lineupComponent.players.stream().map(PlayerSquad::new).collect(Collectors.toList());
            this.regularLineupMinutes = lineupComponent.minutes();
            return this;
        }

        public StatisticsTemplateResponse startingLineup(LineupComponent lineupComponent) {
            this.startingLineup = lineupComponent.players.stream().map(PlayerSquad::new).collect(Collectors.toList());
            return this;
        }

        public StatisticsTemplateResponse moreGoalsForByMinuteLineup(LineupComponent lineupComponent) {
            this.moreGoalsForByMinuteLineup = lineupComponent.players.stream().map(PlayerSquad::new).collect(Collectors.toList());;
            this.moreGoalsForByMinuteLineupMinutes = lineupComponent.minutes();
            this.moreGoalsForByMinuteLineupGoals = Maths.round(lineupComponent.goals(), 2);
            return this;
        }

        public StatisticsTemplateResponse lessGoalsAgainstByMinuteLineup(LineupComponent lineupComponent) {
            this.lessGoalsAgainstByMinuteLineup = lineupComponent.players.stream().map(PlayerSquad::new).collect(Collectors.toList());
            this.lessGoalsAgainstByMinuteLineupMinutes = lineupComponent.minutes();
            this.lessGoalsAgainstByMinuteLineupGoals = Maths.round(lineupComponent.goals(), 2);
            return this;
        }

        public StatisticsTemplateResponse moreWonMinutesLineup(LineupComponent lineupComponent) {
            this.moreWonMinutesLineup = lineupComponent.players.stream().map(PlayerSquad::new).collect(Collectors.toList());
            this.moreWonMinutesLineupMinutes = lineupComponent.minutes();
            return this;
        }

        public StatisticsTemplateResponse bestLineup(LineupComponent lineupComponent) {
            this.bestLineup = lineupComponent.players.stream().map(PlayerSquad::new).collect(Collectors.toList());
            this.bestLineupScore = Maths.round(lineupComponent.score(), 2);
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

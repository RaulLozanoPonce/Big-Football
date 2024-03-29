package ui1.raullozano.bigfootball.common.model.transformator.ml;

import ui1.raullozano.bigfootball.common.model.transformator.Player;
import ui1.raullozano.bigfootball.common.model.transformator.Position;
import ui1.raullozano.bigfootball.common.model.transformator.temp_stats.PlayerStats;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlayerCombination {

    private static final List<String> gameStatistics = List.of("goals", "penaltyGoals", "penaltyTried", "shots", "targetShots",
            "xG", "noPenaltyXG", "shotActions", "goalActions", "passesCompleted", "passes", "passesTotalDistance", "passesProgressiveDistance",
            "passesCompletedShort", "passesShort", "passesCompletedMedium", "passesMedium", "passesCompletedLong", "passesLong",
            "assists", "xgAssists", "passXa", "assistedShots", "passesIntoFinalThird", "passesIntoPenaltyArea", "crossesIntoPenaltyArea",
            "progressivePasses", "passesLive", "passesDead", "passesFreeKick", "throughBalls", "passesSwitches", "crosses",
            "throwIns", "cornerKicks", "cornerKicksIn", "cornerKicksOut", "cornerKicksStraight", "passesOffsides", "passesBlocked",
            "tackles", "tacklesWon", "tacklesDef3rd", "tacklesMid3rd", "tacklesAtt3rd", "challengeTackles", "challenges",
            "challengesLost", "blocks", "blockedShots", "blockedPasses", "interceptions", "clearances", "errors", "touches",
            "touchesDefPenArea", "touchesDef3rd", "touchesMid3rd", "touchesAtt3rd", "touchesAttPenArea", "touchesLiveBall",
            "takeOns", "takeOnsWon", "takeOnsTackled", "takeOnsTackledPct", "carries", "carriesDistance", "carriesProgressiveDistance",
            "progressiveCarries", "carriesIntoFinalThird", "carriesIntoPenaltyArea", "miscontrols", "dispossessed", "passesReceived",
            "progressivePassesReceived", "cardsYellow", "cardsRed", "cardsYellowRed", "fouls", "fouled", "offsides", "pensConceded",
            "ballRecoveries", "aerialsWon", "aerialsLost", "gkPsxg", "gkPassesCompletedLaunched", "gkPassesLaunched", "gkPasses",
            "gkPassesThrows", "gkPctPassesLaunched", "gkPassesLengthAvg", "gkGoalKicks", "gkPctGoalKicksLaunched", "gkGoalKickLengthAvg",
            "gkCrosses", "gkCrossesStopped", "gkDefActionsOutsidePenArea", "gkAvgDistanceDefActions");

    private final LinkedHashMap<String, Object> line = new LinkedHashMap<>();

    public PlayerCombination addPlayerStats(Map<Player, List<PlayerStats>> thisPlayersStats, Map<Player, List<PlayerStats>> otherPlayersStats) {
        gameStatistics.forEach(s -> {
            this.line.put("this-gk-" + s, thisPlayersStats.entrySet().stream().filter(e -> e.getKey().finalPosition() == Position.PT).mapToDouble(e -> e.getValue().stream().mapToDouble(p -> p.get(s)).average().getAsDouble()).average().orElse(0));
            this.line.put("this-" + s, thisPlayersStats.entrySet().stream().filter(e -> e.getKey().finalPosition() != Position.PT).mapToDouble(e -> e.getValue().stream().mapToDouble(p -> p.get(s)).average().getAsDouble()).average().orElse(0));
            this.line.put("other-gk-" + s, otherPlayersStats.entrySet().stream().filter(e -> e.getKey().finalPosition() == Position.PT).mapToDouble(e -> e.getValue().stream().mapToDouble(p -> p.get(s)).average().getAsDouble()).average().orElse(0));
            this.line.put("other-" + s, otherPlayersStats.entrySet().stream().filter(e -> e.getKey().finalPosition() != Position.PT).mapToDouble(e -> e.getValue().stream().mapToDouble(p -> p.get(s)).average().getAsDouble()).average().orElse(0));
        });
        return this;
    }

    public PlayerCombination addTeamsStreak(List<Integer> thisTeamStreak, List<Integer> otherTeamStreak) {
        this.line.put("thisTeamStreak", thisTeamStreak.stream().mapToDouble(v -> v).average().orElse(0));
        this.line.put("otherTeamStreak", otherTeamStreak.stream().mapToDouble(v -> v).average().orElse(0));
        return this;
    }

    public PlayerCombination addLastGoalDifference(double thisLastGoalDifference, double otherLastGoalDifference) {
        this.line.put("thisLastGoalDifference", thisLastGoalDifference);
        this.line.put("otherLastGoalDifference", otherLastGoalDifference);
        return this;
    }

    public PlayerCombination addTeams(String thisTeam, String otherTeam) {
        this.line.put("thisTeam", thisTeam);
        this.line.put("otherTeam", otherTeam);
        return this;
    }

    public PlayerCombination addGoalDifference(int goalsDifference, double minutes) {
        this.line.put("label", 90 * goalsDifference / minutes);
        return this;
    }

    public Set<String> attributes() {
        return line.keySet();
    }

    public Object attribute(String attribute) {
        return this.line.get(attribute);
    }

    public PlayerCombination addAttribute(String name, Object value) {
        this.line.put(name, value);
        return this;
    }

    public void delete(String attribute) {
        this.line.remove(attribute);
    }

    public String header() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : line.entrySet()) {
            sb.append(";").append(entry.getKey());
        }
        sb.append("\n");
        return sb.substring(1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : line.entrySet()) {
            sb.append(";").append(entry.getValue().toString());
        }
        sb.append("\n");
        return sb.substring(1);
    }
}

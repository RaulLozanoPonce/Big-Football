package ui1.raullozano.bigfootball.common.model.transformator;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlayerCombination {

    private static final List<String> attributes = List.of("goals", "penaltyGoals", "penaltyTried", "shots", "targetShots",
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

    private final LinkedHashMap<String, Object> dataset = new LinkedHashMap<>();

    public PlayerCombination addPlayerStats(Map<Player, List<PlayerStats>> thisPlayersStats, Map<Player, List<PlayerStats>> otherPlayersStats) {
        attributes.forEach(s -> {
            this.dataset.put("this-gk-" + s, thisPlayersStats.entrySet().stream().filter(e -> e.getKey().finalPosition() == Position.PT).mapToDouble(e -> e.getValue().stream().mapToDouble(p -> p.get(s)).average().getAsDouble()).average().orElse(0));
            this.dataset.put("this-" + s, thisPlayersStats.entrySet().stream().filter(e -> e.getKey().finalPosition() != Position.PT).mapToDouble(e -> e.getValue().stream().mapToDouble(p -> p.get(s)).average().getAsDouble()).average().orElse(0));
            this.dataset.put("other-gk-" + s, otherPlayersStats.entrySet().stream().filter(e -> e.getKey().finalPosition() == Position.PT).mapToDouble(e -> e.getValue().stream().mapToDouble(p -> p.get(s)).average().getAsDouble()).average().orElse(0));
            this.dataset.put("other-" + s, otherPlayersStats.entrySet().stream().filter(e -> e.getKey().finalPosition() != Position.PT).mapToDouble(e -> e.getValue().stream().mapToDouble(p -> p.get(s)).average().getAsDouble()).average().orElse(0));
        });
        /*attributes.forEach(s -> {
            this.dataset.put("this-gk-" + s, thisPlayersStats.entrySet().stream().filter(e -> e.getKey().finalPosition() == Position.PT).mapToDouble(e -> e.getValue().stream().mapToDouble(p -> p.get(s)).average().getAsDouble()).average().orElse(0));
            this.dataset.put("this-df-" + s, thisPlayersStats.entrySet().stream().filter(e -> e.getKey().finalPosition() == Position.DF).mapToDouble(e -> e.getValue().stream().mapToDouble(p -> p.get(s)).average().getAsDouble()).average().orElse(0));
            this.dataset.put("this-cc-" + s, thisPlayersStats.entrySet().stream().filter(e -> e.getKey().finalPosition() == Position.CC).mapToDouble(e -> e.getValue().stream().mapToDouble(p -> p.get(s)).average().getAsDouble()).average().orElse(0));
            this.dataset.put("this-dl-" + s, thisPlayersStats.entrySet().stream().filter(e -> e.getKey().finalPosition() == Position.DL).mapToDouble(e -> e.getValue().stream().mapToDouble(p -> p.get(s)).average().getAsDouble()).average().orElse(0));
            this.dataset.put("other-gk-" + s, otherPlayersStats.entrySet().stream().filter(e -> e.getKey().finalPosition() == Position.PT).mapToDouble(e -> e.getValue().stream().mapToDouble(p -> p.get(s)).average().getAsDouble()).average().orElse(0));
            this.dataset.put("other-df-" + s, otherPlayersStats.entrySet().stream().filter(e -> e.getKey().finalPosition() == Position.DF).mapToDouble(e -> e.getValue().stream().mapToDouble(p -> p.get(s)).average().getAsDouble()).average().orElse(0));
            this.dataset.put("other-cc-" + s, otherPlayersStats.entrySet().stream().filter(e -> e.getKey().finalPosition() == Position.CC).mapToDouble(e -> e.getValue().stream().mapToDouble(p -> p.get(s)).average().getAsDouble()).average().orElse(0));
            this.dataset.put("other-dl-" + s, otherPlayersStats.entrySet().stream().filter(e -> e.getKey().finalPosition() == Position.DL).mapToDouble(e -> e.getValue().stream().mapToDouble(p -> p.get(s)).average().getAsDouble()).average().orElse(0));
        });*/
        return this;
    }

    public PlayerCombination addPreviousGoals(int previousGoals) {
        this.dataset.put("previousGoals", previousGoals);
        return this;
    }

    public PlayerCombination addIsHome(boolean isHome) {
        this.dataset.put("isHome", isHome ? 1 : 0);
        return this;
    }

    public PlayerCombination addTeamsStreak(List<Integer> thisTeamStreak, List<Integer> otherTeamStreak) {
        this.dataset.put("thisTeamStreak", thisTeamStreak.stream().mapToDouble(v -> v).average().orElse(0));
        this.dataset.put("otherTeamStreak", otherTeamStreak.stream().mapToDouble(v -> v).average().orElse(0));
        return this;
    }

    public PlayerCombination addLastGoalDifference(double thisLastGoalDifference, double otherLastGoalDifference) {
        this.dataset.put("thisLastGoalDifference", thisLastGoalDifference);
        this.dataset.put("otherLastGoalDifference", otherLastGoalDifference);
        return this;
    }

    public PlayerCombination addGoalDifference(int goalsDifference, double minutes) {
        this.dataset.put("label", 90 * goalsDifference / minutes);
        return this;
    }

    public Set<String> attributes() {
        return dataset.keySet();
    }

    public Object attribute(String attribute) {
        return this.dataset.get(attribute);
    }

    public void delete(String attribute) {
        this.dataset.remove(attribute);
    }

    public String header() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : dataset.entrySet()) {
            sb.append(";").append(entry.getKey());
        }
        sb.append("\n");
        return sb.substring(1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : dataset.entrySet()) {
            sb.append(";").append(entry.getValue().toString());
        }
        sb.append("\n");
        return sb.substring(1);
    }
}

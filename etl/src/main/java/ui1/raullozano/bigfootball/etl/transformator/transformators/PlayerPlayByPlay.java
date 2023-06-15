package ui1.raullozano.bigfootball.etl.transformator.transformators;

import ui1.raullozano.bigfootball.common.model.extractor.*;
import ui1.raullozano.bigfootball.common.model.transformator.*;
import ui1.raullozano.bigfootball.common.utils.MatchUtils;
import ui1.raullozano.bigfootball.etl.transformator.Utils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayerPlayByPlay {

    public static void transformData(Team team, String teamType, List<MatchEvent> events, Set<String> playerNames) {
        int matchResult = Utils.getMatchResult(teamType, events);
        transformClutchData(team, teamType, events, playerNames, matchResult);
        transformSubstitutionsData(team, teamType, events, playerNames, matchResult);
    }

    private static void transformClutchData(Team team, String teamType, List<MatchEvent> events, Set<String> playerNames, int matchResult) {

        if(Utils.getMatchResultUntil(Player.ClutchStatistics.StartingMinute, teamType, events) < matchResult) {

            int minute = initClutchMinute(teamType, events, matchResult);

            List<MatchEvent> clutchGoalEvents = events.stream()
                    .filter(e -> e.type() == MatchEvent.MatchEventType.Goal || e.type() == MatchEvent.MatchEventType.PenaltyGoal || e.type() == MatchEvent.MatchEventType.OwnGoal)
                    .filter(e -> MatchUtils.minuteOf(e.minute()) >= minute)
                    .collect(Collectors.toList());

            int matchResultUntilMinuteClutch = Utils.getMatchResultUntil(minute, teamType, events);

            for (String playerName : playerNames) {
                transformClutchData(events, clutchGoalEvents, team.player(playerName), teamType, matchResultUntilMinuteClutch);
            }
        }
    }

    private static int initClutchMinute(String teamType, List<MatchEvent> events, int matchResult) {

        int minute = 90;
        int partialResult = matchResult;

        for (int i = events.size() - 1; i >= 0; i--) {
            MatchEvent event = events.get(i);
            if(event.type() == MatchEvent.MatchEventType.Goal || event.type() == MatchEvent.MatchEventType.PenaltyGoal || event.type() == MatchEvent.MatchEventType.OwnGoal) {
                if(MatchUtils.minuteOf(event.minute()) >= Player.ClutchStatistics.StartingMinute) {

                    if (event.team().equals(teamType)) {
                        partialResult--;
                    } else {
                        partialResult++;
                    }

                    if((matchResult == 0 && partialResult == 0) ||
                            (matchResult != 0 && partialResult != 0 && matchResult/Math.abs(matchResult) == partialResult/Math.abs(partialResult))) {
                        break;
                    }

                    minute = MatchUtils.minuteOf(event.minute());
                }
            }
        }

        return minute;
    }

    private static void transformSubstitutionsData(Team team, String teamType, List<MatchEvent> events, Set<String> playerNames, int matchResult) {
        for (String playerName : playerNames) {
            transformSubstitutionsData(teamType, events, matchResult, team.player(playerName));
        }
    }

    private static void transformClutchData(List<MatchEvent> events, List<MatchEvent> clutchGoalEvents, Player player, String teamType, int matchResultUntilMinuteClutch) {

        if(notSubstitutedUntil(Player.ClutchStatistics.StartingMinute, player, events)) {

            Player.ClutchStatistics playerInClutch = player.clutch();

            playerInClutch.played(1);

            int newMatchResult = matchResultUntilMinuteClutch;

            for (int i = 0; i < clutchGoalEvents.size(); i++) {

                MatchEvent event = clutchGoalEvents.get(i);

                if(event.team().equals(teamType)) {
                    newMatchResult++;
                } else {
                    newMatchResult--;
                }

                if(event.player().equals(player.name()) || (event.otherPlayer() != null && event.otherPlayer().equals(player.name()))) {
                    int points = 1;
                    if(i == 0) points = 2 * points;
                    if(newMatchResult == 0 || newMatchResult == 1) points = 3 * points;
                    playerInClutch.contributions(points);
                }
            }
        }
    }

    private static void transformSubstitutionsData(String teamType, List<MatchEvent> events, int matchResult, Player player) {

        Integer matchResultWhenIsSubstituted = getMatchResultWhenIsSubstituted(teamType, player, events);
        Integer matchResultWhenIsSubstitute = getMatchResultWhenIsSubstitute(teamType, player, events);

        Player.SubstitutionsStatistics playerInSubstitution = player.substitutions();

        if(matchResultWhenIsSubstituted != null) {
            playerInSubstitution.substituted(1);
            if(matchResultWhenIsSubstituted != matchResult) {
                int pointsDifference = getPointsDifferenceBetween(matchResultWhenIsSubstituted, matchResult);
                if(matchResultWhenIsSubstituted > matchResult) {
                    playerInSubstitution.lostPointsWhenSubstituted(pointsDifference);
                } else {
                    playerInSubstitution.wonPointsWhenSubstituted(pointsDifference);
                }
            }
        }

        if(matchResultWhenIsSubstitute != null) {
            playerInSubstitution.substitute(1);
            int finalResult = matchResultWhenIsSubstituted == null ? matchResult : matchResultWhenIsSubstituted;
            if(matchResultWhenIsSubstitute != finalResult) {
                int pointsDifference = getPointsDifferenceBetween(matchResultWhenIsSubstitute, finalResult);
                if (matchResultWhenIsSubstitute > finalResult) {
                    playerInSubstitution.lostPointsWhenSubstitute(pointsDifference);
                } else {
                    playerInSubstitution.wonPointsWhenSubstitute(pointsDifference);
                }
            }
        }
    }

    private static boolean notSubstitutedUntil(int minute, Player player, List<MatchEvent> events) {
        return events.stream()
                .filter(e -> e.type() == MatchEvent.MatchEventType.Substitution)
                .filter(e -> MatchUtils.minuteOf(e.minute()) < minute)
                .noneMatch(e -> e.otherPlayer().equals(player.name()));
    }

    private static Integer getMatchResultWhenIsSubstituted(String teamType, Player player, List<MatchEvent> events) {

        MatchEvent substitutedEvent = events.stream()
                .filter(e -> e.type() == MatchEvent.MatchEventType.Substitution)
                .filter(e -> e.otherPlayer() != null)
                .filter(e -> e.otherPlayer().equals(player.name()))
                .findFirst().orElse(null);

        if(substitutedEvent == null) return null;

        int diffGoals = events.stream()
                .filter(e -> e.type() == MatchEvent.MatchEventType.Goal || e.type() == MatchEvent.MatchEventType.PenaltyGoal)
                .filter(e -> MatchUtils.minuteOf(e.minute()) < MatchUtils.minuteOf(substitutedEvent.minute()))
                .mapToInt(e -> e.team().equals(teamType) ? 1 : -1)
                .sum();
        if(diffGoals == 0) return 0;
        return diffGoals / Math.abs(diffGoals);
    }

    private static Integer getMatchResultWhenIsSubstitute(String teamType, Player player, List<MatchEvent> events) {

        MatchEvent substitutedEvent = events.stream()
                .filter(e -> e.type() == MatchEvent.MatchEventType.Substitution)
                .filter(e -> e.player().equals(player.name()))
                .findFirst().orElse(null);

        if(substitutedEvent == null) return null;

        int diffGoals = events.stream()
                .filter(e -> e.type() == MatchEvent.MatchEventType.Goal || e.type() == MatchEvent.MatchEventType.PenaltyGoal)
                .filter(e -> MatchUtils.minuteOf(e.minute()) < MatchUtils.minuteOf(substitutedEvent.minute()))
                .mapToInt(e -> e.team().equals(teamType) ? 1 : -1)
                .sum();
        if(diffGoals == 0) return 0;
        return diffGoals / Math.abs(diffGoals);
    }

    private static int getPointsDifferenceBetween(Integer matchBefore, int matchAfter) {
        if(matchBefore != 1 && matchAfter != 1) {
            return 1;
        }

        if(matchBefore != -1 && matchAfter != -1) {
            return 2;
        }

        return 3;
    }
}

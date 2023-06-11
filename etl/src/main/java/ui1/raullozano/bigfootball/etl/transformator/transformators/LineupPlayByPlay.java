package ui1.raullozano.bigfootball.etl.transformator.transformators;

import ui1.raullozano.bigfootball.common.model.extractor.*;
import ui1.raullozano.bigfootball.common.model.transformator.*;
import ui1.raullozano.bigfootball.common.utils.MatchUtils;
import ui1.raullozano.bigfootball.etl.transformator.Utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LineupPlayByPlay {

    public static void transformData(Team team, TeamMatch teamMatch, String teamType, List<MatchEvent> events) {

        List<String> currentLineup = starterLineupOf(teamMatch);

        String lastMinute = null;
        int minute = 0;
        int goalsFor = 0;
        int goalsAgainst = 0;

        int lastResultChangeMinute = 0;
        int result = 0;
        int wonMinutes = 0;

        for (MatchEvent event : events) {

            if(lastMinute != null && lastMinute.contains("+") && !event.minute().contains("+")) {
                minute = 45;
            }

            lastMinute = event.minute();

            int eventMinute = MatchUtils.minuteOf(event.minute());

            if(event.type() == MatchEvent.MatchEventType.Goal || event.type() == MatchEvent.MatchEventType.PenaltyGoal || event.type() == MatchEvent.MatchEventType.OwnGoal) {

                int beforeResult = result;

                if(event.team().equals(teamType)) {
                    goalsFor++;
                    result++;
                } else {
                    goalsAgainst++;
                    result--;
                }

                if(beforeResult <= 0 && result > 0) {
                    lastResultChangeMinute = eventMinute;
                } else if(beforeResult > 0 && result <= 0) {
                    wonMinutes += eventMinute - lastResultChangeMinute;
                }
            }

            if(event.type() == MatchEvent.MatchEventType.Substitution || event.type() == MatchEvent.MatchEventType.DoubleYellowCard || event.type() == MatchEvent.MatchEventType.RedCard) {
                if(event.team().equals(teamType)) {

                    if(eventMinute != minute) {

                        LineupStatistics lineupStatistics = new LineupStatistics()
                                .minutes(eventMinute - minute)
                                .goalsFor(goalsFor)
                                .goalsAgainst(goalsAgainst)
                                .wonMinutes(wonMinutes + eventMinute - lastResultChangeMinute);
                        team.registerLineup(currentLineup, lineupStatistics);
                    }

                    minute = eventMinute;
                    goalsFor = 0;
                    goalsAgainst = 0;
                    lastResultChangeMinute = eventMinute;
                    wonMinutes = 0;

                    if (event.type() == MatchEvent.MatchEventType.Substitution) {
                        currentLineup.add(playerId(teamMatch.name(), event.player()));
                        currentLineup.remove(playerId(teamMatch.name(), event.otherPlayer()));
                    } else {
                        currentLineup.remove(playerId(teamMatch.name(), event.player()));
                    }

                    currentLineup = currentLineup.stream()
                            .sorted(Comparator.naturalOrder())
                            .collect(Collectors.toList());
                }
            }
        }

        if(minute < 90) {

            if(lastMinute != null && lastMinute.contains("+")) {
                minute = 45;
            }

            LineupStatistics lineupStatistics = new LineupStatistics()
                    .minutes(90 - minute)
                    .goalsFor(goalsFor)
                    .goalsAgainst(goalsAgainst)
                    .wonMinutes(wonMinutes + 90 - lastResultChangeMinute);
            team.registerLineup(currentLineup, lineupStatistics);
        }
    }

    private static List<String> starterLineupOf(TeamMatch team) {
        return team.stats().entrySet().stream()
                .filter(e -> e.getValue().summaryStat().starting())
                .map(e -> playerId(team.name(), e.getKey()))
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    private static String playerId(String teamName, String playerName) {
        return Base64.getEncoder().encodeToString((teamName + "-" + playerName).getBytes(StandardCharsets.UTF_8));
    }
}

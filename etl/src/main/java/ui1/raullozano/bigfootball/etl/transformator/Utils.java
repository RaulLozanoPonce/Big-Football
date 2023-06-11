package ui1.raullozano.bigfootball.etl.transformator;

import ui1.raullozano.bigfootball.common.model.extractor.*;
import ui1.raullozano.bigfootball.common.utils.MatchUtils;

import java.util.List;

public class Utils {

    public static int getMatchResultUntil(int minute, String teamType, List<MatchEvent> events) {
        int diffGoals = events.stream()
                .filter(e -> e.type() == MatchEvent.MatchEventType.Goal || e.type() == MatchEvent.MatchEventType.PenaltyGoal || e.type() == MatchEvent.MatchEventType.OwnGoal)
                .filter(e -> MatchUtils.minuteOf(e.minute()) < minute)
                .mapToInt(e -> e.team().equals(teamType) ? 1 : -1)
                .sum();
        if(diffGoals == 0) return 0;
        return diffGoals / Math.abs(diffGoals);
    }

    public static int getMatchResult(String teamType, List<MatchEvent> events) {
        int diffGoals = events.stream()
                .filter(e -> e.type() == MatchEvent.MatchEventType.Goal || e.type() == MatchEvent.MatchEventType.PenaltyGoal || e.type() == MatchEvent.MatchEventType.OwnGoal)
                .mapToInt(e -> e.team().equals(teamType) ? 1 : -1)
                .sum();
        if(diffGoals == 0) return 0;
        return diffGoals / Math.abs(diffGoals);
    }
}

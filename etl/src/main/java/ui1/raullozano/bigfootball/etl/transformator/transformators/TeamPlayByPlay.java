package ui1.raullozano.bigfootball.etl.transformator.transformators;

import ui1.raullozano.bigfootball.common.model.extractor.*;
import ui1.raullozano.bigfootball.common.model.transformator.*;
import ui1.raullozano.bigfootball.etl.transformator.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class TeamPlayByPlay {

    public static void transformData(Team team, String teamType, List<MatchEvent> events) {

        team.playedMatches(1);

        int matchResult = Utils.getMatchResult(teamType, events);

        if(matchResult > 0) {
            team.win(1);
        } else if(matchResult == 0) {
            team.draw(1);
        } else {
            team.lost(1);
        }

        int matchResultUntilClutch = Utils.getMatchResultUntil(Player.ClutchStatistics.StartingMinute, teamType, events);
        if(matchResultUntilClutch > matchResult) {
            team.totalNegativeClutchMatches(1);
        } else if(matchResultUntilClutch < matchResult) {
            team.totalPositiveClutchMatches(1);
        }

        team.goalsFor(events.stream()
                .filter(e -> e.team().equals(teamType))
                .filter(e -> e.type() == MatchEvent.MatchEventType.Goal || e.type() == MatchEvent.MatchEventType.PenaltyGoal || e.type() == MatchEvent.MatchEventType.OwnGoal)
                .map(MatchEvent::minute).collect(Collectors.toList()));

        team.goalsAgainst(events.stream()
                .filter(e -> !e.team().equals(teamType))
                .filter(e -> e.type() == MatchEvent.MatchEventType.Goal || e.type() == MatchEvent.MatchEventType.PenaltyGoal || e.type() == MatchEvent.MatchEventType.OwnGoal)
                .map(MatchEvent::minute).collect(Collectors.toList()));
    }
}

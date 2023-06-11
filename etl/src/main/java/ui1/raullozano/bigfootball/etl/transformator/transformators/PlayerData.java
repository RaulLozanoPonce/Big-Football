package ui1.raullozano.bigfootball.etl.transformator.transformators;

import ui1.raullozano.bigfootball.common.model.extractor.stats.*;
import ui1.raullozano.bigfootball.common.model.transformator.*;

import java.util.Map;
import java.util.Set;

public class PlayerData {

    public static void transformData(Team team, Map<String, Stat> stats) {

        for (String playerName : stats.keySet()) {
            Stat stat = stats.get(playerName);

            Player player = new Player(team.name(), playerName)
                    .position(positionOf(stat.summaryStat().position()))
                    .age(ageOf(stat.summaryStat().age()));

            try {
                player.matches()
                        .playedMatches(1)
                        .starters(stat.summaryStat().starting() ? 1 : 0)
                        .minutes(stat.summaryStat().minutes())
                        .goals(stat.summaryStat().goals())
                        .assists(stat.passStat().assists())
                        .tackles(stat.defensiveStat().tacklesWon(), stat.defensiveStat().tackles())
                        .challenges(stat.defensiveStat().challengeTackles(), stat.defensiveStat().challenges())
                        .aerialDuels(stat.otherStat().aerialsWon(), stat.otherStat().aerialsLost())
                        .passes(stat.passStat().passesCompleted(), stat.passStat().passes())
                        .xG(stat.summaryStat().xG())
                        .fouls(stat.otherStat().fouls(), stat.otherStat().fouled());
            } catch (Throwable t) {
                t.printStackTrace();
            }

            team.registerPlayer(player);
        }
    }

    private static Set<Position> positionOf(String position) {
        return Position.positionOf(position);
    }

    private static int ageOf(String age) {
        try {
            return Integer.parseInt(age.split("-")[0]);
        } catch(Throwable t) {
            return 0;
        }
    }
}

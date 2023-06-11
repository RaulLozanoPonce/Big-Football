package ui1.raullozano.bigfootball.etl.extractor.getter.match.stats;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ui1.raullozano.bigfootball.common.model.extractor.stats.*;

import java.util.Map;

public class GoalkeeperGetter {

    public static void addStats(Map<String, Stat> stats, Element table) {
        for (Element row : table.getElementsByTag("tbody").get(0).getElementsByTag("tr")) {
            String player = row.getElementsByTag("th").get(0).child(0).html();
            if(!stats.containsKey(player)) stats.put(player, new Stat());
            GoalkeeperStat stat = stats.get(player).goalkeeperStat();

            Elements columns = row.getElementsByTag("td");
            stat.gkShotsOnTargetAgainst(columns.get(3).html());
            stat.gkGoalsAgainst(columns.get(4).html());
            stat.gkSaves(columns.get(5).html());

            if(columns.size() > 7) {
                stat.gkPsxg(columns.get(7).html());
                stat.gkPassesCompletedLaunched(columns.get(8).html());
                stat.gkPassesLaunched(columns.get(9).html());
                stat.gkPasses(columns.get(11).html());
                stat.gkPassesThrows(columns.get(12).html());
                stat.gkPctPassesLaunched(columns.get(13).html());
                stat.gkPassesLengthAvg(columns.get(14).html());
                stat.gkGoalKicks(columns.get(15).html());
                stat.gkPctGoalKicksLaunched(columns.get(16).html());
                stat.gkGoalKickLengthAvg(columns.get(17).html());
                stat.gkCrosses(columns.get(18).html());
                stat.gkCrossesStopped(columns.get(19).html());
                stat.gkDefActionsOutsidePenArea(columns.get(21).html());
                stat.gkAvgDistanceDefActions(columns.get(22).html());
            }
        }
    }
}

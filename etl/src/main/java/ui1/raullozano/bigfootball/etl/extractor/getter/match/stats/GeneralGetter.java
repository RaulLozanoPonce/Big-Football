package ui1.raullozano.bigfootball.etl.extractor.getter.match.stats;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ui1.raullozano.bigfootball.common.model.extractor.stats.Stat;
import ui1.raullozano.bigfootball.common.model.extractor.stats.SummaryStat;

import java.util.Map;

public class GeneralGetter {

    public static void addStats(Map<String, Stat> stats, Element table) {
        for (Element row : table.getElementsByTag("tbody").get(0).getElementsByTag("tr")) {
            String player = row.getElementsByTag("th").get(0).child(0).html();
            if(!stats.containsKey(player)) stats.put(player, new Stat());
            SummaryStat stat = stats.get(player).summaryStat();

            Elements columns = row.getElementsByTag("td");
            stat.starting(!row.getElementsByTag("th").get(0).html().contains("&nbsp;&nbsp;&nbsp;"));
            stat.shirtNumber(columns.get(0).html());
            stat.nationality(columns.get(1).html());
            stat.position(columns.get(2).html());
            stat.age(columns.get(3).html());
            stat.minutes(columns.get(4).html());
            stat.goals(columns.get(5).html());
            stat.penaltyGoals(columns.get(7).html());
            stat.penaltyTried(columns.get(8).html());
            stat.shots(columns.get(9).html());
            stat.targetShots(columns.get(10).html());
            stat.xG(columns.get(17).html());
            stat.noPenaltyXG(columns.get(18).html());
            stat.shotActions(columns.get(20).html());
            stat.goalActions(columns.get(21).html());
        }
    }
}

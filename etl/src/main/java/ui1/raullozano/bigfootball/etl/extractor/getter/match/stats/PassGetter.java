package ui1.raullozano.bigfootball.etl.extractor.getter.match.stats;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ui1.raullozano.bigfootball.common.model.extractor.stats.PassStat;
import ui1.raullozano.bigfootball.common.model.extractor.stats.Stat;

import java.util.Map;

public class PassGetter {

    public static void addStats(Map<String, Stat> stats, Element table) {
        for (Element row : table.getElementsByTag("tbody").get(0).getElementsByTag("tr")) {
            String player = row.getElementsByTag("th").get(0).child(0).html();
            if(!stats.containsKey(player)) stats.put(player, new Stat());
            PassStat stat = stats.get(player).passStat();

            Elements columns = row.getElementsByTag("td");
            stat.passesCompleted(columns.get(5).html());
            stat.passes(columns.get(6).html());
            stat.passesTotalDistance(columns.get(8).html());
            stat.passesProgressiveDistance(columns.get(9).html());
            stat.passesCompletedShort(columns.get(10).html());
            stat.passesShort(columns.get(11).html());
            stat.passesCompletedMedium(columns.get(13).html());
            stat.passesMedium(columns.get(14).html());
            stat.passesCompletedLong(columns.get(16).html());
            stat.passesLong(columns.get(17).html());
            stat.assists(columns.get(19).html());
            stat.xgAssists(columns.get(20).html());
            stat.passXa(columns.get(21).html());
            stat.assistedShots(columns.get(22).html());
            stat.passesIntoFinalThird(columns.get(23).html());
            stat.passesIntoPenaltyArea(columns.get(24).html());
            stat.crossesIntoPenaltyArea(columns.get(25).html());
            stat.progressivePasses(columns.get(26).html());
        }
    }
}

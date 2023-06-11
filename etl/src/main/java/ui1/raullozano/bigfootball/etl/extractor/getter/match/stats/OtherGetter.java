package ui1.raullozano.bigfootball.etl.extractor.getter.match.stats;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ui1.raullozano.bigfootball.common.model.extractor.stats.*;

import java.util.Map;

public class OtherGetter {

    public static void addStats(Map<String, Stat> stats, Element table) {
        for (Element row : table.getElementsByTag("tbody").get(0).getElementsByTag("tr")) {
            String player = row.getElementsByTag("th").get(0).child(0).html();
            if(!stats.containsKey(player)) stats.put(player, new Stat());
            OtherStat stat = stats.get(player).otherStat();

            Elements columns = row.getElementsByTag("td");
            stat.cardsYellow(columns.get(5).html());
            stat.cardsRed(columns.get(6).html());
            stat.cardsYellowRed(columns.get(7).html());
            stat.fouls(columns.get(8).html());
            stat.fouled(columns.get(9).html());
            stat.offsides(columns.get(10).html());
            stat.pensConceded(columns.get(15).html());
            stat.ballRecoveries(columns.get(17).html());
            stat.aerialsWon(columns.get(18).html());
            stat.aerialsLost(columns.get(19).html());
        }
    }
}

package ui1.raullozano.bigfootball.etl.extractor.getter.match.stats;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ui1.raullozano.bigfootball.common.model.extractor.stats.*;

import java.util.Map;

public class PossessionGetter {

    public static void addStats(Map<String, Stat> stats, Element table) {
        for (Element row : table.getElementsByTag("tbody").get(0).getElementsByTag("tr")) {
            String player = row.getElementsByTag("th").get(0).child(0).html();
            if(!stats.containsKey(player)) stats.put(player, new Stat());
            PossessionStat stat = stats.get(player).possessionStat();

            Elements columns = row.getElementsByTag("td");
            stat.touches(columns.get(5).html());
            stat.touchesDefPenArea(columns.get(6).html());
            stat.touchesDef3rd(columns.get(7).html());
            stat.touchesMid3rd(columns.get(8).html());
            stat.touchesAtt3rd(columns.get(9).html());
            stat.touchesAttPenArea(columns.get(10).html());
            stat.touchesLiveBall(columns.get(11).html());
            stat.takeOns(columns.get(12).html());
            stat.takeOnsWon(columns.get(13).html());
            stat.takeOnsTackled(columns.get(15).html());
            stat.takeOnsTackledPct(columns.get(16).html());
            stat.carries(columns.get(17).html());
            stat.carriesDistance(columns.get(18).html());
            stat.carriesProgressiveDistance(columns.get(19).html());
            stat.progressiveCarries(columns.get(20).html());
            stat.carriesIntoFinalThird(columns.get(21).html());
            stat.carriesIntoPenaltyArea(columns.get(22).html());
            stat.miscontrols(columns.get(23).html());
            stat.dispossessed(columns.get(24).html());
            stat.passesReceived(columns.get(25).html());
            stat.progressivePassesReceived(columns.get(26).html());
        }
    }
}

package ui1.raullozano.bigfootball.etl.extractor.getter.match.stats;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ui1.raullozano.bigfootball.common.model.extractor.stats.*;

import java.util.Map;

public class DefensiveGetter {

    public static void addStats(Map<String, Stat> stats, Element table) {
        for (Element row : table.getElementsByTag("tbody").get(0).getElementsByTag("tr")) {
            String player = row.getElementsByTag("th").get(0).child(0).html();
            if(!stats.containsKey(player)) stats.put(player, new Stat());
            DefensiveStat stat = stats.get(player).defensiveStat();

            Elements columns = row.getElementsByTag("td");
            stat.tackles(columns.get(5).html());
            stat.tacklesWon(columns.get(6).html());
            stat.tacklesDef3rd(columns.get(7).html());
            stat.tacklesMid3rd(columns.get(8).html());
            stat.tacklesAtt3rd(columns.get(9).html());
            stat.challengeTackles(columns.get(10).html());
            stat.challenges(columns.get(11).html());
            stat.challengesLost(columns.get(13).html());
            stat.blocks(columns.get(14).html());
            stat.blockedShots(columns.get(15).html());
            stat.blockedPasses(columns.get(16).html());
            stat.interceptions(columns.get(17).html());
            stat.clearances(columns.get(19).html());
            stat.errors(columns.get(20).html());
        }
    }
}

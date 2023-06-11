package ui1.raullozano.bigfootball.etl.extractor.getter.match.stats;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ui1.raullozano.bigfootball.common.model.extractor.stats.*;

import java.util.Map;

public class PassTypeGetter {

    public static void addStats(Map<String, Stat> stats, Element table) {
        for (Element row : table.getElementsByTag("tbody").get(0).getElementsByTag("tr")) {
            String player = row.getElementsByTag("th").get(0).child(0).html();
            if(!stats.containsKey(player)) stats.put(player, new Stat());
            PassTypeStat stat = stats.get(player).passTypeStat();

            Elements columns = row.getElementsByTag("td");
            stat.passesLive(columns.get(6).html());
            stat.passesDead(columns.get(7).html());
            stat.passesFreeKick(columns.get(8).html());
            stat.throughBalls(columns.get(9).html());
            stat.passesSwitches(columns.get(10).html());
            stat.crosses(columns.get(11).html());
            stat.throwIns(columns.get(12).html());
            stat.cornerKicks(columns.get(13).html());
            stat.cornerKicksIn(columns.get(14).html());
            stat.cornerKicksOut(columns.get(15).html());
            stat.cornerKicksStraight(columns.get(16).html());
            stat.passesOffsides(columns.get(18).html());
            stat.passesBlocked(columns.get(19).html());
        }
    }
}

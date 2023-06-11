package ui1.raullozano.bigfootball.etl.extractor.getter.match.stats;

import org.jsoup.nodes.Document;
import ui1.raullozano.bigfootball.common.model.extractor.stats.Stat;

import java.util.HashMap;
import java.util.Map;

public class StatsGetter {

    private final Document doc;
    private int currentIndex = 0;

    public StatsGetter(Document doc) {
        this.doc = doc;
    }

    public Map<String, Stat> homeStats() {
        return stats(0);
    }

    public Map<String, Stat> awayStats() {
        return stats(currentIndex);
    }

    private Map<String, Stat> stats(int index) {

        Map<String, Stat> stats = new HashMap<>();

        switch (doc.getElementsByClass("stats_table").size()) {
            case 2:
                GeneralGetter.addStats(stats, doc.getElementsByClass("stats_table").get(index));
                currentIndex = 1;
                break;
            case 4:
                GeneralGetter.addStats(stats, doc.getElementsByClass("stats_table").get(index));
                GoalkeeperGetter.addStats(stats, doc.getElementsByClass("stats_table").get(index + 1));
                currentIndex = 2;
                break;
            default:
                GeneralGetter.addStats(stats, doc.getElementsByClass("stats_table").get(index));
                PassGetter.addStats(stats, doc.getElementsByClass("stats_table").get(index + 1));
                PassTypeGetter.addStats(stats, doc.getElementsByClass("stats_table").get(index + 2));
                DefensiveGetter.addStats(stats, doc.getElementsByClass("stats_table").get(index + 3));
                PossessionGetter.addStats(stats, doc.getElementsByClass("stats_table").get(index + 4));
                OtherGetter.addStats(stats, doc.getElementsByClass("stats_table").get(index + 5));
                GoalkeeperGetter.addStats(stats, doc.getElementsByClass("stats_table").get(index + 6));
                currentIndex = 7;
        }

        return stats;
    }
}

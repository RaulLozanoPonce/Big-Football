package ui1.raullozano.bigfootball.etl.extractor.getter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ui1.raullozano.bigfootball.etl.extractor.getter.match.EventGetter;
import ui1.raullozano.bigfootball.etl.extractor.getter.match.HeaderGetter;
import ui1.raullozano.bigfootball.common.model.extractor.Match;
import ui1.raullozano.bigfootball.etl.extractor.getter.match.stats.StatsGetter;

public class MatchGetter {

    private final String url;

    public MatchGetter(String url) {
        this.url = url;
    }

    public Match get() {

        try {
            Document doc = Jsoup.connect(url).get();

            Match match = new Match();
            HeaderGetter headerGetter = new HeaderGetter(doc);
            EventGetter eventGetter = new EventGetter(doc);
            StatsGetter statsGetter = new StatsGetter(doc);

            if(doc.getElementsByClass("scorebox").get(0).children().get(0)
                    .children().get(1).getElementsByClass("score").html().equals("--")) return null;

            if(doc.getElementsByClass("scorebox").get(0).children().get(1)
                    .children().get(1).getElementsByClass("score").html().equals("--")) return null;

            match.homeTeam(headerGetter.team("home"));
            match.awayTeam(headerGetter.team("away"));
            match.ts(headerGetter.ts());
            match.competition(headerGetter.competition());
            match.attendance(headerGetter.attendance());
            match.stadium(headerGetter.stadium());
            match.referee(headerGetter.referee());

            match.events(eventGetter.events());

            match.homeTeam().stats(statsGetter.homeStats());
            match.awayTeam().stats(statsGetter.awayStats());

            return match;
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
}

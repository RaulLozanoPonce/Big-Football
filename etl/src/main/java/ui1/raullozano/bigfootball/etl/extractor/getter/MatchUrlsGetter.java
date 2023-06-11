package ui1.raullozano.bigfootball.etl.extractor.getter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ui1.raullozano.bigfootball.common.model.Competition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchUrlsGetter {

    private final String urlBase;
    private final String urlComplement;
    private final String urlDate;
    private final List<Competition> competitions;

    public MatchUrlsGetter(String urlBase, String urlComplement, String urlDate, List<Competition> competitions) {
        this.urlBase = urlBase;
        this.urlComplement = urlComplement;
        this.urlDate = urlDate;
        this.competitions = competitions;
    }

    public Map<Competition, List<String>> get() {

        try {
            Document doc = Jsoup.connect(urlBase + urlComplement + "/" + urlDate).get();

            Map<Competition, List<String>> matchesUrls = new HashMap<>();

            for (Element element : doc.getElementsByClass("table_wrapper")) {
                Competition competition = competitionOf(element);
                if(competition != null) {
                    for (Element match : element.child(2).child(0).getElementsByTag("tbody").get(0)
                            .getElementsByTag("tr")) {
                        matchesUrls.putIfAbsent(competition, new ArrayList<>());
                        matchesUrls.get(competition).add(this.urlBase + match.getElementsByAttributeValue("data-stat", "score")
                                .get(0).child(0).attr("href"));
                    }
                }
            }

            return matchesUrls;
        } catch (Throwable t) {
            t.printStackTrace();
            return new HashMap<>();
        }
    }

    private Competition competitionOf(Element element) {
        String competitionUrl = element.getElementsByTag("a").get(0).attr("href");
        for (Competition competition : competitions) {
            if(competitionUrl.contains("comps/" + competition.id() + "/")) {
                return competition;
            }
        }

        return null;
    }
}

package ui1.raullozano.bigfootball.etl.extractor.getter.match;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ui1.raullozano.bigfootball.common.model.extractor.TeamMatch;

import java.time.Instant;

public class HeaderGetter {

    private final Document doc;

    public HeaderGetter(Document doc) {
        this.doc = doc;
    }

    public TeamMatch team(String type) {
        int nElement = type.equals("home") ? 0 : 1;
        Elements elements = doc.getElementsByClass("scorebox").get(0).children().get(nElement).children();
        TeamMatch team = new TeamMatch().typeTeam(type);
        team.name(elements.get(0).getElementsByTag("strong").get(0).getElementsByTag("a").html().replaceAll("amp;", ""));
        team.fGoals(Integer.parseInt(elements.get(1).getElementsByClass("score").html().replaceAll("\\*", "")));
        if(elements.get(1).getElementsByClass("score_xg").size() > 0) {
            team.xGoals(Double.parseDouble(elements.get(1).getElementsByClass("score_xg").html().replaceAll(",", ".")));
        }
        team.streak(elements.get(2).html());
        if(elements.size() > 4) {
            team.coach(elements.get(4).childNodes().get(1).outerHtml().replaceAll(": ", "").replaceAll("&nbsp;", " "));
            if (elements.size() > 5) {
                team.captain(elements.get(5).getElementsByTag("a").html().replaceAll("&nbsp;", " "));
            }
        }
        return team;
    }

    public Instant ts() {
        Element element = doc.getElementsByClass("venueTime").get(0);
        return Instant.parse(element.attr("data-venue-date") + "T" + element.attr("data-venue-time") + ":00Z");
    }

    public String competition() {
        return doc.getElementsByClass("scorebox_meta").get(0).children().get(1).getElementsByTag("a").get(0).html();
    }

    public Integer attendance() {
        Element element = doc.getElementsByClass("scorebox_meta").get(0).children().get(4);
        if(element.html().contains("Asistencia")) {
            return Integer.parseInt(element.getElementsByTag("small").get(1).html().replaceAll(",", ""));
        }
        return null;
    }

    public String stadium() {

        for (int i = 4; i <= 5; i++) {
            Element element = doc.getElementsByClass("scorebox_meta").get(0).children().get(i);
            if(element.html().contains("Sedes")) {
                return element.getElementsByTag("small").get(1).html();
            }
        }

        return null;
    }

    public String referee() {

        for (int i = 4; i <= 6; i++) {
            Element element = doc.getElementsByClass("scorebox_meta").get(0).children().get(i);
            if(element.html().contains("Autoridades")) {
                if(element.getElementsByTag("small").get(1).children().size() > 0) {
                    return element.getElementsByTag("small").get(1).children().get(0).html()
                            .replaceAll("&nbsp;", " ")
                            .replaceAll(" \\(Árbitro\\)", "");
                }
            }
        }

        return null;
    }
}

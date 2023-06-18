package ui1.raullozano.bigfootball.etl.extractor.getter.match;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ui1.raullozano.bigfootball.common.model.extractor.MatchEvent;

import java.util.ArrayList;
import java.util.List;

public class EventGetter {

    private final Document doc;

    public EventGetter(Document doc) {
        this.doc = doc;
    }

    public List<MatchEvent> events() {
        List<MatchEvent> events = new ArrayList<>();

        Element element = doc.getElementById("events_wrap").children().get(0);
        for (Element child : element.children()) {
            if(child.tagName().equals("div")) {
                if(child.hasClass("a")) {
                    MatchEvent event = new MatchEvent().team("home");
                    event.minute(minute(child));
                    event.type(type(child));
                    event.player(player(child));
                    if(event.type() == MatchEvent.MatchEventType.Goal || event.type() == MatchEvent.MatchEventType.Substitution) {
                        event.otherPlayer(otherPlayer(child));
                    }
                    events.add(event);
                } else if(child.hasClass("b")) {
                    MatchEvent event = new MatchEvent().team("away");
                    event.minute(minute(child));
                    event.type(type(child));
                    event.player(player(child));
                    if(event.type() == MatchEvent.MatchEventType.Goal || event.type() == MatchEvent.MatchEventType.Substitution) {
                        event.otherPlayer(otherPlayer(child));
                    }
                    events.add(event);
                }
            }
        }
        return events;
    }

    private String minute(Element element) {
        return element.child(0).childNode(0).outerHtml().replaceAll("\n", "").replaceAll("&nbsp;", "").replaceAll("’ ", "");
    }

    private MatchEvent.MatchEventType type(Element element) {
        Element iconElement = element.child(1).child(0);
        if(iconElement.hasClass("goal")) return MatchEvent.MatchEventType.Goal;
        if(iconElement.hasClass("penalty_goal")) return MatchEvent.MatchEventType.PenaltyGoal;
        if(iconElement.hasClass("own_goal")) return MatchEvent.MatchEventType.OwnGoal;
        if(iconElement.hasClass("penalty_miss")) return MatchEvent.MatchEventType.MissedPenalty;
        if(iconElement.hasClass("substitute_in")) return MatchEvent.MatchEventType.Substitution;
        if(iconElement.hasClass("yellow_card")) return MatchEvent.MatchEventType.YellowCard;
        if(iconElement.hasClass("yellow_red_card")) return MatchEvent.MatchEventType.DoubleYellowCard;
        if(iconElement.hasClass("red_card")) return MatchEvent.MatchEventType.RedCard;
        throw new RuntimeException("No existe tipo para " + iconElement);
    }

    private String player(Element element) {
        Element subelement = element.child(1).child(1).child(0);
        if(subelement.children().isEmpty()) return subelement.html();
        return subelement.child(0).html();
    }

    private String otherPlayer(Element element) {
        if(element.child(1).child(1).children().size() > 1) {
            if(element.child(1).child(1).child(1).children().size() > 0) {
                return element.child(1).child(1).child(1).child(0).html();
            } else {
                return element.child(1).child(1).child(1).html();
            }
        } else {
            return null;
        }
    }
}

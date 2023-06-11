package ui1.raullozano.bigfootball.etl.extractor;

import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.model.Competition;
import ui1.raullozano.bigfootball.common.model.extractor.Match;
import ui1.raullozano.bigfootball.common.utils.Time;
import ui1.raullozano.bigfootball.etl.extractor.getter.MatchGetter;
import ui1.raullozano.bigfootball.etl.extractor.getter.MatchUrlsGetter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ui1.raullozano.bigfootball.common.utils.Time.*;

public class Extractor {

    private final FileAccessor fileAccessor;

    public Extractor(FileAccessor fileAccessor) {
        this.fileAccessor = fileAccessor;
    }

    public Map<Competition, List<Match>> extractMatches(Instant instant) {

        System.out.println("Se extraen los partidos de " + instant.toString());

        Map<Competition, List<Match>> matches = new HashMap<>();

        String urlDate = yearOf(instant) + "-" + monthStyled(monthOf(instant)) + "-" + dayStyled(monthDayOf(instant));
        Map<Competition, List<String>> urls = new MatchUrlsGetter("https://fbref.com", "/es/partidos",
                urlDate, fileAccessor.competitions()).get();

        try {
            for (Competition competition : urls.keySet()) {
                for (String matchUrl : urls.get(competition)) {
                    System.out.println("Se va a extraer: " + matchUrl);
                    matches.putIfAbsent(competition, new ArrayList<>());
                    Match match = new MatchGetter(matchUrl).get();
                    matches.get(competition).add(match);
                    Thread.sleep(10000);
                }
            }
            Thread.sleep(10000);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        System.out.println("Se han extraído todos los partidos");

        return matches;
    }
}

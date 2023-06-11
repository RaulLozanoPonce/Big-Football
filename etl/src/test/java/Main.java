import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.files.LocalFileAccessor;
import ui1.raullozano.bigfootball.common.model.Competition;
import ui1.raullozano.bigfootball.common.model.extractor.Match;
import ui1.raullozano.bigfootball.common.utils.Time;
import ui1.raullozano.bigfootball.etl.extractor.Extractor;
import ui1.raullozano.bigfootball.etl.transformator.Transformator;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        FileAccessor fileAccessor = new LocalFileAccessor();

        for (Instant instant : Time.getInstantsBetween(Time.getInstantOf(2018, 11, 11), Time.getInstantOf(2019, 8, 12), Time.Scale.D)) {
            Map<Competition, List<Match>> matches = new Extractor(fileAccessor).extractMatches(instant);

            for (Competition competition : matches.keySet()) {
                int year = competition.yearOf(instant);
                Transformator transformator = new Transformator(fileAccessor, competition.folderName(), year);
                for (Match match : matches.get(competition)) {
                    fileAccessor.saveMatch(competition.folderName(), year, match, instant);
                    transformator.transform(match);
                }
                transformator.saveFiles();
            }
        }
    }
}

import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.files.LocalFileAccessor;
import ui1.raullozano.bigfootball.common.model.Competition;
import ui1.raullozano.bigfootball.common.model.extractor.Match;
import ui1.raullozano.bigfootball.common.utils.Time;
import ui1.raullozano.bigfootball.etl.extractor.Extractor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class GetMatches {

    public static void main(String[] args) {

        FileAccessor fileAccessor = new LocalFileAccessor();

        for (Instant instant : Time.getInstantsBetween(Time.getInstantOf(2021, 3, 14), Time.getInstantOf(2021, 3, 15), Time.Scale.D)) {
            Map<Competition, List<Match>> matches = new Extractor(fileAccessor).extractMatches(instant);

            for (Competition competition : matches.keySet()) {
                int year = competition.yearOf(instant);
                for (Match match : matches.get(competition)) {
                    fileAccessor.saveMatch(competition.folderName(), year, match, instant);
                }
            }
        }
    }
}

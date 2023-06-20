package ui1.raullozano.bigfootball.etl;

import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.model.Competition;
import ui1.raullozano.bigfootball.common.model.extractor.Match;
import ui1.raullozano.bigfootball.common.utils.Time;
import ui1.raullozano.bigfootball.etl.extractor.Extractor;
import ui1.raullozano.bigfootball.etl.transformator.Transformator;
import ui1.raullozano.bigfootball.etl.transformator.ml.BestLineupModel;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class ETLBox {

    private final FileAccessor fileAccessor;

    public ETLBox(FileAccessor fileAccessor) {
        this.fileAccessor = fileAccessor;
    }

    public void start() {
        initScheduler();
    }

    private void initScheduler() {
        Scheduler scheduler = new Scheduler(u -> etl(), fileAccessor.scale(), fileAccessor.timeComponents());
        scheduler.start();
    }

    private void etl() {

        Instant instant = Time.previousInstant(Time.truncateTo(Time.currentInstant(), fileAccessor.scale()), fileAccessor.scale());

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

        new BestLineupModel(fileAccessor).get();
    }
}

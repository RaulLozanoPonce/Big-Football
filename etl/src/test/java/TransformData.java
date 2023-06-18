import com.google.gson.Gson;
import ui1.raullozano.bigfootball.common.files.LocalFileAccessor;
import ui1.raullozano.bigfootball.common.model.extractor.Match;
import ui1.raullozano.bigfootball.etl.transformator.Transformator;
import ui1.raullozano.bigfootball.etl.transformator.ml.BestLineupModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TransformData {

    public static void main(String[] args) throws IOException {

        LocalFileAccessor fileAccessor = new LocalFileAccessor();

        for (File competition : new File(fileAccessor.parameters().get("data") + "/raw/matches/").listFiles()) {
            for (File season : new File(fileAccessor.parameters().get("data") + "/raw/matches/" + competition.getName() + "/").listFiles()) {
                Transformator transformator = new Transformator(fileAccessor, competition.getName(), Integer.parseInt(season.getName()));
                for (File matchFile : new File(fileAccessor.parameters().get("data") + "/raw/matches/" + competition.getName() + "/" + season.getName() + "/").listFiles()) {
                    Match match = new Gson().fromJson(Files.readString(matchFile.toPath()), Match.class);
                    transformator.transform(match);
                }
                transformator.saveFiles();
            }
        }

        new BestLineupModel(fileAccessor).get();
    }
}

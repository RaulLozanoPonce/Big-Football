package other;

import com.google.gson.Gson;
import ui1.raullozano.bigfootball.common.files.LocalFileAccessor;
import ui1.raullozano.bigfootball.common.model.extractor.Match;
import ui1.raullozano.bigfootball.common.model.transformator.Team;
import ui1.raullozano.bigfootball.etl.transformator.Transformator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws IOException {

        LocalFileAccessor fileAccessor = new LocalFileAccessor();

        Map<String, Team> teams = new HashMap<>();

        for (File competition : new File(fileAccessor.parameters().get("data") + "/transformed/teams/").listFiles()) {
            for (File season : new File(fileAccessor.parameters().get("data") + "/transformed/teams/" + competition.getName() + "/").listFiles()) {
                for (File teamFile : new File(fileAccessor.parameters().get("data") + "/transformed/teams/" + competition.getName() + "/" + season.getName() + "/").listFiles()) {
                    Team team = new Gson().fromJson(Files.readString(teamFile.toPath()), Team.class);
                    teams.put(season.getName() + " - " + teamFile.getName(), team);
                }
            }
        }

        teams.entrySet().stream().sorted(Comparator.comparingInt(o -> o.getValue().lineupStatistics().size()));


        System.out.println(teams);
    }
}

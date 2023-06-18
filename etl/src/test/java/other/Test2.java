package other;

import com.google.gson.Gson;
import ui1.raullozano.bigfootball.common.files.LocalFileAccessor;
import ui1.raullozano.bigfootball.common.model.transformator.Team;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Test2 {

    public static void main(String[] args) throws IOException {

        LocalFileAccessor fileAccessor = new LocalFileAccessor();

        Map<String, Set<String>> teams = new HashMap<>();

        for (File competition : new File(fileAccessor.parameters().get("data") + "/transformed/machine-learning/best/").listFiles()) {
            for (File season : new File(fileAccessor.parameters().get("data") + "/transformed/machine-learning/best/" + competition.getName() + "/").listFiles()) {
                List<String> lines = Files.readAllLines(Path.of(fileAccessor.parameters().get("data") + "/transformed/machine-learning/best/" + competition.getName() + "/" + season.getName() + "/player_combinations.csv"));
                for (String line : lines) {
                    String[] components = line.split(";");
                    teams.putIfAbsent(season.getName() + " - " + components[0] + " - " + components[2], new HashSet<>());
                    teams.get(season.getName() + " - " + components[0] + " - " + components[2]).add(components[3]);
                }
            }
        }

        //teams.entrySet().stream().sorted(Comparator.comparingInt(o -> o.getValue().lineupStatistics().size()));


        System.out.println(teams);
    }
}

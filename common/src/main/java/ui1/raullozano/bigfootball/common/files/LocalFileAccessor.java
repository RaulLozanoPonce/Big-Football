package ui1.raullozano.bigfootball.common.files;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ui1.raullozano.bigfootball.common.model.Competition;
import ui1.raullozano.bigfootball.common.model.extractor.Match;
import ui1.raullozano.bigfootball.common.model.transformator.ml.BestPlayerCombination;
import ui1.raullozano.bigfootball.common.model.transformator.temp_stats.LineupStats;
import ui1.raullozano.bigfootball.common.model.transformator.ml.PlayerCombination;
import ui1.raullozano.bigfootball.common.model.transformator.temp_stats.PlayerStats;
import ui1.raullozano.bigfootball.common.model.transformator.Team;
import ui1.raullozano.bigfootball.common.utils.Time;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.*;

public class LocalFileAccessor implements FileAccessor {

    @Override
    public synchronized Map<String, String> parameters() {

        File file = new File("./conf/parameters");
        try {
            return Files.readAllLines(file.toPath()).stream()
                    .filter(l -> !l.startsWith("#"))
                    .collect(Collectors.toMap(l -> l.split("=")[0], l -> l.split("=")[1]));
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return new HashMap<>();
    }

    @Override
    public synchronized Path data() {
        return Path.of(parameters().get("data"));
    }

    @Override
    public synchronized Integer port() {
        return Integer.valueOf(parameters().get("port"));
    }

    @Override
    public synchronized Time.Scale scale() {
        return Time.Scale.valueOf(parameters().get("scale"));
    }

    @Override
    public synchronized List<Integer> timeComponents() {
        return new Gson().fromJson(parameters().get("time-components"), new TypeToken<List<Integer>>(){}.getType());
    }

    @Override
    public synchronized String mlMaster() {
        return parameters().get("ml-master");
    }

    @Override
    public synchronized String urlBase() {
        return parameters().get("url-base");
    }

    @Override
    public synchronized List<Competition> competitions() {

        File file = new File("./conf/competitions");
        try {
            return Files.readAllLines(file.toPath()).stream().filter(l -> !l.startsWith("#")).map(l -> {
                String[] parameters = l.split(";");
                return new Competition(parameters[0], parameters[1], parameters[2]);
            }).collect(Collectors.toList());
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    @SuppressWarnings("all")
    public synchronized List<String> competitionFolderNames() {
        return Arrays.stream(new File(parameters().get("data") + "/transformed/teams/").listFiles()).map(File::getName).collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("all")
    public synchronized List<String> seasonFolderNames(String competition) {
        return Arrays.stream(new File(parameters().get("data") + "/transformed/teams/" + competition + "/").listFiles()).map(File::getName).collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("all")
    public synchronized List<String> teamsFileNames(String competition, String season) {
        return Arrays.stream(new File(parameters().get("data") + "/transformed/teams/" + competition + "/" + season + "/").listFiles()).map(f -> f.getName().replaceAll("\\.json", "")).collect(Collectors.toList());
    }

    @Override
    public synchronized void saveMatch(String competition, int year, Match match, Instant instant) {
        try {
            File file = new File(data() + "/raw/matches/" + competition + "/" + year + "/" +
                    instant.toString().replaceAll(":", "_").replaceAll("-", "_") + " - " +
                    match.homeTeam().name() + " - " + match.awayTeam().name() + ".json");
            Files.createDirectories(file.getParentFile().toPath());
            if(file.exists()) Files.delete(file.toPath());
            Files.writeString(file.toPath(), match.toString(), CREATE, WRITE);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public synchronized Team getTeam(String competition, String season, String team) {
        return getTeam(new File(data() + "/transformed/teams/" + competition + "/" + season + "/" + team + ".json"));
    }

    @SuppressWarnings("all")
    public synchronized List<Team> getTeams(String competition, String season) {
        File file = new File(data() + "/transformed/teams/" + competition + "/" + season + "/");
        if(file.exists()) {
            return Arrays.stream(file.listFiles()).map(this::getTeam).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public synchronized void saveTeam(String competition, String season, Team team) {
        try {
            File file = new File(data() + "/transformed/teams/" + competition + "/" + season + "/" +
                    team.name().replaceAll("&", "and") + ".json");
            Files.createDirectories(file.getParentFile().toPath());
            if(file.exists()) Files.delete(file.toPath());
            Files.writeString(file.toPath(), team.toString(), CREATE, WRITE);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public synchronized Map<String, List<PlayerStats>> getPlayerLastStats(String competition, String season) {
        File file = new File(data() + "/transformed/player-last-stats/" + competition + "/" + season + "/stats.json");
        if(file.exists()) {
            try {
                return new Gson().fromJson(Files.readString(file.toPath()), new TypeToken<Map<String, List<PlayerStats>>>() {}.getType());
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        return new HashMap<>();
    }

    @Override
    public synchronized void saveTeamLastStats(String competition, String season, Map<String, List<Integer>> teamLastStats) {
        try {
            File file = new File(data() + "/transformed/team-last-stats/" + competition + "/" + season + "/stats.json");
            Files.createDirectories(file.getParentFile().toPath());
            if(file.exists()) Files.delete(file.toPath());
            Files.writeString(file.toPath(), new Gson().toJson(teamLastStats), CREATE, WRITE);
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public synchronized Map<String, List<Integer>> getTeamLastStats(String competition, String season) {
        File file = new File(data() + "/transformed/team-last-stats/" + competition + "/" + season + "/stats.json");
        if(file.exists()) {
            try {
                return new Gson().fromJson(Files.readString(file.toPath()), new TypeToken<Map<String, List<Integer>>>() {}.getType());
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        return new HashMap<>();
    }

    @Override
    public synchronized void saveLineupStats(String competition, String season, Map<String, LineupStats> lineupStats) {
        try {
            File file = new File(data() + "/transformed/lineup-stats/" + competition + "/" + season + "/stats.json");
            Files.createDirectories(file.getParentFile().toPath());
            if(file.exists()) Files.delete(file.toPath());
            Files.writeString(file.toPath(), new Gson().toJson(lineupStats), CREATE, WRITE);
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public synchronized Map<String, LineupStats> getLineupStats(String competition, String season) {
        File file = new File(data() + "/transformed/lineup-stats/" + competition + "/" + season + "/stats.json");
        if(file.exists()) {
            try {
                return new Gson().fromJson(Files.readString(file.toPath()), new TypeToken<Map<String, LineupStats>>() {}.getType());
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        return new HashMap<>();
    }

    @Override
    public synchronized void savePlayerLastStats(String competition, String season, Map<String, List<PlayerStats>> lastStats) {
        try {
            File file = new File(data() + "/transformed/player-last-stats/" + competition + "/" + season + "/stats.json");
            Files.createDirectories(file.getParentFile().toPath());
            if(file.exists()) Files.delete(file.toPath());
            Files.writeString(file.toPath(), new Gson().toJson(lastStats), CREATE, WRITE);
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public Path getPlayerCombinationsToTrainPath() {
        return Path.of(data() + "/transformed/machine-learning/train/player_combinations.csv");
    }

    @Override
    public void savePlayerCombinationsToTrain(PlayerCombination playerCombination) {
        try {
            File file = new File(data() + "/transformed/machine-learning/train/player_combinations.csv");
            Files.createDirectories(file.getParentFile().toPath());
            if (!file.exists()) Files.writeString(file.toPath(), playerCombination.header(), CREATE, APPEND, WRITE);
            Files.writeString(file.toPath(), playerCombination.toString(), CREATE, APPEND, WRITE);
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public Path getPlayerCombinationToTestPath(String competition, String season) {
        return Path.of(data() + "/transformed/machine-learning/test/" + competition + "/" + season + "/player_combinations.csv");
    }

    @Override
    public void savePlayerCombinationToTest(String competition, String season, PlayerCombination playerCombination) {
        try {
            File file = new File(data() + "/transformed/machine-learning/test/" + competition + "/" + season + "/player_combinations.csv");
            Files.createDirectories(file.getParentFile().toPath());
            if (!file.exists()) Files.writeString(file.toPath(), playerCombination.header(), CREATE, APPEND, WRITE);
            Files.writeString(file.toPath(), playerCombination.toString(), CREATE, APPEND, WRITE);
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void removePlayerCombinationToTest() {
        try {
            remove(new File(data() + "/transformed/machine-learning/test/"));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private synchronized void remove(File file) {
        if(file.listFiles() != null) {
            for (File children : Objects.requireNonNull(file.listFiles())) {
                remove(children);
            }
        }
        file.delete();
    }

    @Override
    public String getBestPlayerCombination(String competition, String season, String thisTeam, String otherTeam, String lineup) {
        try(BufferedReader reader = new BufferedReader(new FileReader(data() + "/transformed/machine-learning/best/" + competition + "/" + season + "/player_combinations.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if(!fields[0].equals(thisTeam)) continue;
                if(!fields[1].equals(otherTeam)) continue;
                if(!fields[2].equals(lineup)) continue;
                return line;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return null;
    }

    @Override
    public void saveBestPlayerCombination(String competition, String season, BestPlayerCombination bestPlayerCombination) {
        try {
            File file = new File(data() + "/transformed/machine-learning/best/" + competition + "/" + season + "/player_combinations.csv");
            Files.createDirectories(file.getParentFile().toPath());
            Files.writeString(file.toPath(), bestPlayerCombination.toString(), WRITE, CREATE, APPEND);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private synchronized Team getTeam(File file) {
        try {
            return new Gson().fromJson(Files.readString(file.toPath()), Team.class);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return null;
    }
}

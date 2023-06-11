package ui1.raullozano.bigfootball.common.files;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ui1.raullozano.bigfootball.common.model.Competition;
import ui1.raullozano.bigfootball.common.model.extractor.Match;
import ui1.raullozano.bigfootball.common.model.transformator.LineupStats;
import ui1.raullozano.bigfootball.common.model.transformator.PlayerCombination;
import ui1.raullozano.bigfootball.common.model.transformator.PlayerStats;
import ui1.raullozano.bigfootball.common.model.transformator.Team;
import ui1.raullozano.bigfootball.common.utils.Time;

import java.io.File;
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
    public Path data() {
        return Path.of(parameters().get("data"));
    }

    @Override
    public Integer port() {
        return Integer.valueOf(parameters().get("port"));
    }

    @Override
    public Time.Scale scale() {
        return Time.Scale.valueOf(parameters().get("scale"));
    }

    @Override
    public List<Integer> timeComponents() {
        return new Gson().fromJson(parameters().get("timeComponents"), new TypeToken<List<Integer>>(){}.getType());
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
    public synchronized void saveMatch(String competition, int year, Match match, Instant instant) {
        try {
            File file = new File(data().toFile().getAbsoluteFile() + "/raw/matches/" + competition + "/" + year + "/" +
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
        return getTeam(new File(data().toFile().getAbsoluteFile() + "/transformed/teams/" + competition + "/" + season + "/" + team + ".json"));
    }

    @SuppressWarnings("all")
    public synchronized List<Team> getTeams(String competition, String season) {
        File file = new File(data().toFile().getAbsoluteFile() + "/transformed/teams/" + competition + "/" + season + "/");
        if(file.exists()) {
            return Arrays.stream(file.listFiles()).map(this::getTeam).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public synchronized void saveTeam(String competition, String season, Team team) {
        try {
            File file = new File(data().toFile().getAbsoluteFile() + "/transformed/teams/" + competition + "/" + season + "/" +
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
        File file = new File(data().toFile().getAbsoluteFile() + "/transformed/player-last-stats/" + competition + "/" + season + "/stats.json");
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
    public void saveTeamLastStats(String competition, String season, Map<String, List<Integer>> teamLastStats) {

    }

    @Override
    public Map<String, List<Integer>> getTeamLastStats(String competition, String season) {
        File file = new File(data().toFile().getAbsoluteFile() + "/transformed/team-last-stats/" + competition + "/" + season + "/stats.json");
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
    public void saveLineupStats(String competition, String season, Map<String, LineupStats> lineupStats) {

    }

    @Override
    public Map<String, LineupStats> getLineupStats(String competition, String season) {
        File file = new File(data().toFile().getAbsoluteFile() + "/transformed/lineup-stats/" + competition + "/" + season + "/stats.json");
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
    public synchronized void savePlayerLastStats(String competition, String season, Map<String, List<PlayerStats>> lastStats) {
        try {
            File file = new File(data().toFile().getAbsoluteFile() + "/transformed/player-last-stats/" + competition + "/" + season + "/stats.json");
            Files.createDirectories(file.getParentFile().toPath());
            if(file.exists()) Files.delete(file.toPath());
            Files.writeString(file.toPath(), new Gson().toJson(lastStats), CREATE, WRITE);
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public synchronized void savePlayerCombination(PlayerCombination playerCombination) {
        try {
            File file = new File(data().toFile().getAbsoluteFile() + "/transformed/player_combinations.csv");
            if (!file.exists()) Files.writeString(file.toPath(), playerCombination.header(), CREATE, APPEND, WRITE);
            Files.writeString(file.toPath(), playerCombination.toString(), CREATE, APPEND, WRITE);
        } catch(Throwable t) {
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

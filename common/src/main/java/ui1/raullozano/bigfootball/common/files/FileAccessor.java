package ui1.raullozano.bigfootball.common.files;

import ui1.raullozano.bigfootball.common.model.Competition;
import ui1.raullozano.bigfootball.common.model.extractor.Match;
import ui1.raullozano.bigfootball.common.model.transformator.LineupStats;
import ui1.raullozano.bigfootball.common.model.transformator.PlayerCombination;
import ui1.raullozano.bigfootball.common.model.transformator.PlayerStats;
import ui1.raullozano.bigfootball.common.model.transformator.Team;
import ui1.raullozano.bigfootball.common.utils.Time;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface FileAccessor {

    Map<String, String> parameters();
    Path data();
    Integer port();
    Time.Scale scale();
    List<Integer> timeComponents();
    List<Competition> competitions();

    void saveMatch(String competition, int year, Match match, Instant instant);
    void saveTeam(String competition, String season, Team team);
    Team getTeam(String competition, String season, String team);
    List<Team> getTeams(String competition, String season);
    void savePlayerLastStats(String competition, String season, Map<String, List<PlayerStats>> lastStats);
    Map<String, List<PlayerStats>> getPlayerLastStats(String competition, String season);
    void saveTeamLastStats(String competition, String season, Map<String, List<Integer>> teamLastStats);
    Map<String, List<Integer>> getTeamLastStats(String competition, String season);
    void saveLineupStats(String competition, String season, Map<String, LineupStats> lineupStats);
    Map<String, LineupStats> getLineupStats(String competition, String season);
    void savePlayerCombination(PlayerCombination playerCombination);
}

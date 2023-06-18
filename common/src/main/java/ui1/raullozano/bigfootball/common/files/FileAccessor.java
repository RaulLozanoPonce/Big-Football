package ui1.raullozano.bigfootball.common.files;

import ui1.raullozano.bigfootball.common.model.Competition;
import ui1.raullozano.bigfootball.common.model.extractor.Match;
import ui1.raullozano.bigfootball.common.model.transformator.ml.BestPlayerCombination;
import ui1.raullozano.bigfootball.common.model.transformator.ml.PlayerCombination;
import ui1.raullozano.bigfootball.common.model.transformator.temp_stats.LineupStats;
import ui1.raullozano.bigfootball.common.model.transformator.temp_stats.PlayerStats;
import ui1.raullozano.bigfootball.common.model.transformator.Team;
import ui1.raullozano.bigfootball.common.utils.Time;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface FileAccessor {

    /* ---------- PARAMETERS ---------------------------------------------------------------------------------------- */
    Map<String, String> parameters();
    Path data();
    Integer port();
    Time.Scale scale();
    List<Integer> timeComponents();
    List<Competition> competitions();

    /* ---------- FILES --------------------------------------------------------------------------------------------- */
    List<String> competitionFolderNames();
    List<String> seasonFolderNames(String competition);
    List<String> teamsFileNames(String competition, String season);

    /* ---------- TRANSFORM ----------------------------------------------------------------------------------------- */
    void saveMatch(String competition, int year, Match match, Instant instant);
    Team getTeam(String competition, String season, String team);
    void saveTeam(String competition, String season, Team team);
    List<Team> getTeams(String competition, String season);
    Map<String, List<PlayerStats>> getPlayerLastStats(String competition, String season);
    void savePlayerLastStats(String competition, String season, Map<String, List<PlayerStats>> lastStats);
    Map<String, List<Integer>> getTeamLastStats(String competition, String season);
    void saveTeamLastStats(String competition, String season, Map<String, List<Integer>> teamLastStats);
    Map<String, LineupStats> getLineupStats(String competition, String season);
    void saveLineupStats(String competition, String season, Map<String, LineupStats> lineupStats);

    /* ---------- MACHINE LEARNING ---------------------------------------------------------------------------------- */
    Path getPlayerCombinationsToTrainPath();
    void savePlayerCombinationsToTrain(PlayerCombination playerCombination);
    Path getPlayerCombinationToTestPath(String competition, String season);
    void savePlayerCombinationToTest(String competition, String season, PlayerCombination playerCombination);
    void removePlayerCombinationToTest();
    String getBestPlayerCombination(String competition, String season, String thisTeam, String otherTeam, String lineup);
    void saveBestPlayerCombination(String competition, String season, BestPlayerCombination bestPlayerCombination);
}

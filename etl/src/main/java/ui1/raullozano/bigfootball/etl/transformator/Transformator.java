package ui1.raullozano.bigfootball.etl.transformator;

import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.model.extractor.Match;
import ui1.raullozano.bigfootball.common.model.extractor.TeamMatch;
import ui1.raullozano.bigfootball.common.model.transformator.LineupStats;
import ui1.raullozano.bigfootball.common.model.transformator.PlayerCombination;
import ui1.raullozano.bigfootball.common.model.transformator.PlayerStats;
import ui1.raullozano.bigfootball.common.model.transformator.Team;
import ui1.raullozano.bigfootball.etl.transformator.transformators.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Transformator {

    private final FileAccessor fileAccessor;
    private final String competition;
    private final int year;
    private final Map<String, Team> teams;
    private final Map<String, List<PlayerStats>> playerLastStats;
    private final Map<String, List<Integer>> teamLastStats;
    private final Map<String, LineupStats> lineupStats;
    private final List<PlayerCombination> playerCombinations = new ArrayList<>();

    public Transformator(FileAccessor fileAccessor, String competition, int year) {
        this.fileAccessor = fileAccessor;
        this.competition = competition;
        this.year = year;
        this.teams = loadTeamsOf(competition, year);
        this.playerLastStats = loadPlayerLastStats(competition, year);
        this.teamLastStats = loadTeamLastStats(competition, year);
        this.lineupStats = loadLineupStats(competition, year);
    }

    private Map<String, Team> loadTeamsOf(String competition, int year) {
        return fileAccessor.getTeams(competition, String.valueOf(year)).stream().collect(Collectors.toMap(Team::name, t -> t));
    }

    private Map<String, List<PlayerStats>> loadPlayerLastStats(String competition, int year) {
        return fileAccessor.getPlayerLastStats(competition, String.valueOf(year));
    }

    private Map<String, List<Integer>> loadTeamLastStats(String competition, int year) {
        return fileAccessor.getTeamLastStats(competition, String.valueOf(year));
    }

    private Map<String, LineupStats> loadLineupStats(String competition, int year) {
        return fileAccessor.getLineupStats(competition, String.valueOf(year));
    }

    public void transform(Match match) {
        getDescriptiveDataOf(match, true);
        getDescriptiveDataOf(match, false);
        playerCombinations.addAll(playerCombinationsOf(match));
    }

    private void getDescriptiveDataOf(Match match, boolean isHome) {

        String teamType = isHome ? "home" : "away";

        TeamMatch teamMatch = isHome ? match.homeTeam() : match.awayTeam();
        Team finalTeam = teams.get(teamMatch.name()) != null ? teams.get(teamMatch.name()) : new Team(teamMatch.name());

        PlayerData.transformData(finalTeam, teamMatch.stats());
        TeamPlayByPlay.transformData(finalTeam, teamType, match.events());
        PlayerPlayByPlay.transformData(finalTeam, teamType, match.events(), teamMatch.stats().keySet());
        LineupPlayByPlay.transformData(finalTeam, teamMatch, teamType, match.events());

        teams.put(teamMatch.name(), finalTeam);
    }

    private List<PlayerCombination> playerCombinationsOf(Match match) {
        return new LineupTransformatorResults(teams, playerLastStats, teamLastStats, lineupStats).getCombinationResults(match);
    }

    public void saveFiles() {
        for (Team team : teams.values()) {
            this.fileAccessor.saveTeam(competition, String.valueOf(year), team);
        }

        for (PlayerCombination playerCombination : playerCombinations) {
            this.fileAccessor.savePlayerCombination(playerCombination);
        }

        this.fileAccessor.savePlayerLastStats(competition, String.valueOf(year), playerLastStats);
        this.fileAccessor.saveTeamLastStats(competition, String.valueOf(year), teamLastStats);
        this.fileAccessor.saveLineupStats(competition, String.valueOf(year), lineupStats);
    }
}

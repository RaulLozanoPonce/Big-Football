package ui1.raullozano.bigfootball.etl.transformator.transformators;

import ui1.raullozano.bigfootball.common.model.extractor.Match;
import ui1.raullozano.bigfootball.common.model.extractor.MatchEvent;
import ui1.raullozano.bigfootball.common.model.extractor.TeamMatch;
import ui1.raullozano.bigfootball.common.model.extractor.stats.Stat;
import ui1.raullozano.bigfootball.common.model.transformator.*;
import ui1.raullozano.bigfootball.common.utils.MatchUtils;
import ui1.raullozano.bigfootball.etl.transformator.Utils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class LineupTransformatorResults {

    private final Map<String, Team> teams;
    private final Map<String, List<PlayerStats>> playerLastStats;
    private final Map<String, List<Integer>> teamLastStats;
    private final Map<String, LineupStats> lineupStats;

    public LineupTransformatorResults(Map<String, Team> teams, Map<String, List<PlayerStats>> playerLastStats, Map<String, List<Integer>> teamLastStats, Map<String, LineupStats> lineupStats) {
        this.teams = teams;
        this.playerLastStats = playerLastStats;
        this.teamLastStats = teamLastStats;
        this.lineupStats = lineupStats;
    }

    public List<PlayerCombination> getCombinationResults(Match match) {
        List<PlayerCombination> playerCombinations = getPlayerCombinations(match.homeTeam(), match.awayTeam(), match.events());
        aggregateNewStats(match);
        return playerCombinations;
    }

    private void aggregateNewStats(Match match) {
        aggregateNewPlayerStats(match.homeTeam());
        aggregateNewPlayerStats(match.awayTeam());
        List<Integer> homeTeamStats = getNewTeamStats(match, match.homeTeam());
        List<Integer> awayTeamStats = getNewTeamStats(match, match.awayTeam());

        teamLastStats.put(match.homeTeam().name(), homeTeamStats);
        teamLastStats.put(match.awayTeam().name(), awayTeamStats);
    }

    private void aggregateNewPlayerStats(TeamMatch team) {
        for (String playerName : team.stats().keySet()) {
            String playerId = playerId(team.name(), playerName);

            playerLastStats.putIfAbsent(playerId, new ArrayList<>());
            List<PlayerStats> playerStats = playerLastStats.get(playerId);
            playerStats.add(getPlayerStats(team.stats().get(playerName)));
            if(playerStats.size() > 5) {
                playerLastStats.put(playerId, playerStats.subList(playerStats.size() - 5, playerStats.size()));
            }
        }
    }

    private List<Integer> getNewTeamStats(Match match, TeamMatch team) {
        teamLastStats.putIfAbsent(team.name(), new ArrayList<>());
        List<Integer> teamStats = teamLastStats.get(team.name());
        teamStats.add(getPoints(match, team.typeTeam()));
        if(teamStats.size() > 5) {
            return teamStats.subList(teamStats.size() - 5, teamStats.size());
        } else {
            return teamStats;
        }
    }

    private int getPoints(Match match, String typeTeam) {
        int goalDifference = match.homeTeam().fGoals() - match.awayTeam().fGoals();
        int teamGoalsDifference = match.homeTeam().typeTeam().equals(typeTeam) ? goalDifference : - goalDifference;
        if(teamGoalsDifference == 0) return 1;
        if(teamGoalsDifference > 0) return 3;
        return 0;
    }

    private List<PlayerCombination> getPlayerCombinations(TeamMatch homeTeam, TeamMatch awayTeam, List<MatchEvent> events) {

        List<Integer> localTeamStreak = teamLastStats.get(homeTeam.name()) == null ? new ArrayList<>() : teamLastStats.get(homeTeam.name());
        List<Integer> awayTeamStreak = teamLastStats.get(awayTeam.name()) == null ? new ArrayList<>() : teamLastStats.get(awayTeam.name());

        List<PlayerCombination> playerResults = new ArrayList<>();

        List<String> homeCurrentLineup = starterLineupOf(homeTeam);
        List<String> awayCurrentLineup = starterLineupOf(awayTeam);

        String minute = "0";
        int goalDifference = 0;
        int previousGoals = 0;

        for (MatchEvent event : events) {

            if (event.type() == MatchEvent.MatchEventType.Goal || event.type() == MatchEvent.MatchEventType.PenaltyGoal || event.type() == MatchEvent.MatchEventType.OwnGoal) {
                if (event.team().equals(homeTeam.typeTeam())) {
                    goalDifference++;
                } else {
                    goalDifference--;
                }
            }

            if(event.type() == MatchEvent.MatchEventType.DoubleYellowCard || event.type() == MatchEvent.MatchEventType.RedCard) {
                return playerResults;
            }

            if(event.type() == MatchEvent.MatchEventType.Substitution) {

                String eventMinute = event.minute();

                double minutes = minutesBetween(minute, eventMinute);

                for (String playerId : homeCurrentLineup) lineupStats.putIfAbsent(playerId, new LineupStats());
                for (String playerId : awayCurrentLineup) lineupStats.putIfAbsent(playerId, new LineupStats());
                double homeLastGoalDifference = homeCurrentLineup.stream().mapToDouble(p -> lineupStats.get(p).get()).average().orElse(0);
                double awayLastGoalDifference = awayCurrentLineup.stream().mapToDouble(p -> lineupStats.get(p).get()).average().orElse(0);
                for (String playerId : homeCurrentLineup) lineupStats.get(playerId).add(goalDifference, minutes);
                for (String playerId : awayCurrentLineup) lineupStats.get(playerId).add(-goalDifference, minutes);

                boolean allPlayerHaveRegister = allPlayerHaveRegister(homeCurrentLineup, awayCurrentLineup);

                if(allPlayerHaveRegister && minutes > 30) {

                    Map<Player, List<PlayerStats>> homePlayersStats = getPlayerMatchStatsOf(homeTeam, homeCurrentLineup);
                    Map<Player, List<PlayerStats>> awayPlayersStats = getPlayerMatchStatsOf(awayTeam, awayCurrentLineup);

                    PlayerCombination homeResults = new PlayerCombination()
                            .addPlayerStats(homePlayersStats, awayPlayersStats)
                            .addPreviousGoals(previousGoals)
                            .addIsHome(true)
                            .addTeamsStreak(localTeamStreak, awayTeamStreak)
                            .addLastGoalDifference(homeLastGoalDifference, awayLastGoalDifference)
                            .addGoalDifference(goalDifference, minutes);

                    playerResults.add(homeResults);

                    PlayerCombination awayResults = new PlayerCombination()
                            .addPlayerStats(awayPlayersStats, homePlayersStats)
                            .addPreviousGoals(-previousGoals)
                            .addIsHome(false)
                            .addTeamsStreak(awayTeamStreak, localTeamStreak)
                            .addLastGoalDifference(awayLastGoalDifference, homeLastGoalDifference)
                            .addGoalDifference(-goalDifference, minutes);

                    playerResults.add(awayResults);
                }

                minute = eventMinute;
                previousGoals += goalDifference;
                goalDifference = 0;

                if(event.team().equals("home")) {
                    if (event.type() == MatchEvent.MatchEventType.Substitution) {
                        homeCurrentLineup.add(playerId(homeTeam.name(), event.player()));
                        homeCurrentLineup.remove(playerId(homeTeam.name(), event.otherPlayer()));
                    } else {
                        homeCurrentLineup.remove(playerId(homeTeam.name(), event.player()));
                    }

                    homeCurrentLineup = homeCurrentLineup.stream()
                            .sorted(Comparator.naturalOrder())
                            .collect(Collectors.toList());
                } else {
                    if (event.type() == MatchEvent.MatchEventType.Substitution) {
                        awayCurrentLineup.add(playerId(awayTeam.name(), event.player()));
                        awayCurrentLineup.remove(playerId(awayTeam.name(), event.otherPlayer()));
                    } else {
                        awayCurrentLineup.remove(playerId(awayTeam.name(), event.player()));
                    }

                    awayCurrentLineup = awayCurrentLineup.stream()
                            .sorted(Comparator.naturalOrder())
                            .collect(Collectors.toList());
                }
            }
        }

        if(MatchUtils.minuteOf(minute) < 90) {

            double minutes = minutesBetween(minute, "90");

            for (String playerId : homeCurrentLineup) lineupStats.putIfAbsent(playerId, new LineupStats());
            for (String playerId : awayCurrentLineup) lineupStats.putIfAbsent(playerId, new LineupStats());
            double homeLastGoalDifference = homeCurrentLineup.stream().mapToDouble(p -> lineupStats.get(p).get()).average().orElse(0);
            double awayLastGoalDifference = awayCurrentLineup.stream().mapToDouble(p -> lineupStats.get(p).get()).average().orElse(0);
            for (String playerId : homeCurrentLineup) lineupStats.get(playerId).add(goalDifference, minutes);
            for (String playerId : awayCurrentLineup) lineupStats.get(playerId).add(-goalDifference, minutes);

            boolean allPlayerHaveRegister = allPlayerHaveRegister(homeCurrentLineup, awayCurrentLineup);

            if (MatchUtils.minuteOf(minute) <= 60 && allPlayerHaveRegister) {

                Map<Player, List<PlayerStats>> homePlayersStats = getPlayerMatchStatsOf(homeTeam, homeCurrentLineup);
                Map<Player, List<PlayerStats>> awayPlayersStats = getPlayerMatchStatsOf(awayTeam, awayCurrentLineup);

                PlayerCombination homeResults = new PlayerCombination()
                        .addPlayerStats(homePlayersStats, awayPlayersStats)
                        .addPreviousGoals(previousGoals)
                        .addIsHome(true)
                        .addTeamsStreak(localTeamStreak, awayTeamStreak)
                        .addLastGoalDifference(homeLastGoalDifference, awayLastGoalDifference)
                        .addGoalDifference(goalDifference, minutes);

                playerResults.add(homeResults);

                PlayerCombination awayResults = new PlayerCombination()
                        .addPlayerStats(awayPlayersStats, homePlayersStats)
                        .addPreviousGoals(-previousGoals)
                        .addIsHome(false)
                        .addTeamsStreak(awayTeamStreak, localTeamStreak)
                        .addLastGoalDifference(awayLastGoalDifference, homeLastGoalDifference)
                        .addGoalDifference(-goalDifference, minutes);

                playerResults.add(awayResults);
            }
        }

        return playerResults;
    }

    private List<String> starterLineupOf(TeamMatch team) {
        return team.stats().entrySet().stream()
                .filter(e -> e.getValue().summaryStat().starting())
                .map(e -> playerId(team.name(), e.getKey()))
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    private double minutesBetween(String minuteFrom, String minuteTo) {
        int numericFrom = MatchUtils.minuteOf(minuteFrom);
        int numericTo = MatchUtils.minuteOf(minuteTo);
        if(minuteFrom.contains("+")) {
            int truncatedFrom = truncatedMinute(numericFrom);
            if(minuteTo.contains("+")) {
                int truncatedTo = truncatedMinute(numericTo);
                return truncatedTo - truncatedFrom;
            } else {
                return numericTo - truncatedFrom;
            }
        } else {
            if(minuteTo.contains("+")) {
                int truncatedTo = truncatedMinute(numericTo);
                return truncatedTo - numericFrom;
            } else {
                return numericTo - numericFrom;
            }
        }
    }

    private int truncatedMinute(int numericFrom) {
        if(numericFrom >= 90) return 90;
        if(numericFrom >= 45) return 45;
        return 0;
    }

    private PlayerStats getPlayerStats(Stat matchStats) {

        PlayerStats playerStats = new PlayerStats();

        double minutes = matchStats.summaryStat().minutes();

        playerStats.position(Position.singlePositionOf(matchStats.summaryStat().position().split(",")[0]));
        playerStats.add("goals", matchStats.summaryStat().goals(), minutes);
        playerStats.add("penaltyGoals", matchStats.summaryStat().penaltyGoals(), minutes);
        playerStats.add("penaltyTried", matchStats.summaryStat().penaltyTried(), minutes);
        playerStats.add("shots", matchStats.summaryStat().shots(), minutes);
        playerStats.add("targetShots", matchStats.summaryStat().targetShots(), minutes);
        playerStats.add("xG", matchStats.summaryStat().xG(), minutes);
        playerStats.add("noPenaltyXG", matchStats.summaryStat().noPenaltyXG(), minutes);
        playerStats.add("shotActions", matchStats.summaryStat().shotActions(), minutes);
        playerStats.add("goalActions", matchStats.summaryStat().goalActions(), minutes);
        playerStats.add("passesCompleted", matchStats.passStat().passesCompleted(), minutes);
        playerStats.add("passes", matchStats.passStat().passes(), minutes);
        playerStats.add("passesTotalDistance", matchStats.passStat().passesTotalDistance(), minutes);
        playerStats.add("passesProgressiveDistance", matchStats.passStat().passesProgressiveDistance(), minutes);
        playerStats.add("passesCompletedShort", matchStats.passStat().passesCompletedShort(), minutes);
        playerStats.add("passesShort", matchStats.passStat().passesShort(), minutes);
        playerStats.add("passesCompletedMedium", matchStats.passStat().passesCompletedMedium(), minutes);
        playerStats.add("passesMedium", matchStats.passStat().passesMedium(), minutes);
        playerStats.add("passesCompletedLong", matchStats.passStat().passesCompletedLong(), minutes);
        playerStats.add("passesLong", matchStats.passStat().passesLong(), minutes);
        playerStats.add("assists", matchStats.passStat().assists(), minutes);
        playerStats.add("xgAssists", matchStats.passStat().xgAssists(), minutes);
        playerStats.add("passXa", matchStats.passStat().passXa(), minutes);
        playerStats.add("assistedShots", matchStats.passStat().assistedShots(), minutes);
        playerStats.add("passesIntoFinalThird", matchStats.passStat().passesIntoFinalThird(), minutes);
        playerStats.add("passesIntoPenaltyArea", matchStats.passStat().passesIntoPenaltyArea(), minutes);
        playerStats.add("crossesIntoPenaltyArea", matchStats.passStat().crossesIntoPenaltyArea(), minutes);
        playerStats.add("progressivePasses", matchStats.passStat().progressivePasses(), minutes);
        playerStats.add("passesLive", matchStats.passTypeStat().passesLive(), minutes);
        playerStats.add("passesDead", matchStats.passTypeStat().passesDead(), minutes);
        playerStats.add("passesFreeKick", matchStats.passTypeStat().passesFreeKick(), minutes);
        playerStats.add("throughBalls", matchStats.passTypeStat().throughBalls(), minutes);
        playerStats.add("passesSwitches", matchStats.passTypeStat().passesSwitches(), minutes);
        playerStats.add("crosses", matchStats.passTypeStat().crosses(), minutes);
        playerStats.add("throwIns", matchStats.passTypeStat().throwIns(), minutes);
        playerStats.add("cornerKicks", matchStats.passTypeStat().cornerKicks(), minutes);
        playerStats.add("cornerKicksIn", matchStats.passTypeStat().cornerKicksIn(), minutes);
        playerStats.add("cornerKicksOut", matchStats.passTypeStat().cornerKicksOut(), minutes);
        playerStats.add("cornerKicksStraight", matchStats.passTypeStat().cornerKicksStraight(), minutes);
        playerStats.add("passesOffsides", matchStats.passTypeStat().passesOffsides(), minutes);
        playerStats.add("passesBlocked", matchStats.passTypeStat().passesBlocked(), minutes);
        playerStats.add("tackles", matchStats.defensiveStat().tackles(), minutes);
        playerStats.add("tacklesWon", matchStats.defensiveStat().tacklesWon(), minutes);
        playerStats.add("tacklesDef3rd", matchStats.defensiveStat().tacklesDef3rd(), minutes);
        playerStats.add("tacklesMid3rd", matchStats.defensiveStat().tacklesMid3rd(), minutes);
        playerStats.add("tacklesAtt3rd", matchStats.defensiveStat().tacklesAtt3rd(), minutes);
        playerStats.add("challengeTackles", matchStats.defensiveStat().challengeTackles(), minutes);
        playerStats.add("challenges", matchStats.defensiveStat().challenges(), minutes);
        playerStats.add("challengesLost", matchStats.defensiveStat().challengesLost(), minutes);
        playerStats.add("blocks", matchStats.defensiveStat().blocks(), minutes);
        playerStats.add("blockedShots", matchStats.defensiveStat().blockedShots(), minutes);
        playerStats.add("blockedPasses", matchStats.defensiveStat().blockedPasses(), minutes);
        playerStats.add("interceptions", matchStats.defensiveStat().interceptions(), minutes);
        playerStats.add("clearances", matchStats.defensiveStat().clearances(), minutes);
        playerStats.add("errors", matchStats.defensiveStat().errors(), minutes);
        playerStats.add("touches", matchStats.possessionStat().touches(), minutes);
        playerStats.add("touchesDefPenArea", matchStats.possessionStat().touchesDefPenArea(), minutes);
        playerStats.add("touchesDef3rd", matchStats.possessionStat().touchesDef3rd(), minutes);
        playerStats.add("touchesMid3rd", matchStats.possessionStat().touchesMid3rd(), minutes);
        playerStats.add("touchesAtt3rd", matchStats.possessionStat().touchesAtt3rd(), minutes);
        playerStats.add("touchesAttPenArea", matchStats.possessionStat().touchesAttPenArea(), minutes);
        playerStats.add("touchesLiveBall", matchStats.possessionStat().touchesLiveBall(), minutes);
        playerStats.add("takeOns", matchStats.possessionStat().takeOns(), minutes);
        playerStats.add("takeOnsWon", matchStats.possessionStat().takeOnsWon(), minutes);
        playerStats.add("takeOnsTackled", matchStats.possessionStat().takeOnsTackled(), minutes);
        playerStats.add("takeOnsTackledPct", matchStats.possessionStat().takeOnsTackledPct(), minutes);
        playerStats.add("carries", matchStats.possessionStat().carries(), minutes);
        playerStats.add("carriesDistance", matchStats.possessionStat().carriesDistance(), minutes);
        playerStats.add("carriesProgressiveDistance", matchStats.possessionStat().carriesProgressiveDistance(), minutes);
        playerStats.add("progressiveCarries", matchStats.possessionStat().progressiveCarries(), minutes);
        playerStats.add("carriesIntoFinalThird", matchStats.possessionStat().carriesIntoFinalThird(), minutes);
        playerStats.add("carriesIntoPenaltyArea", matchStats.possessionStat().carriesIntoPenaltyArea(), minutes);
        playerStats.add("miscontrols", matchStats.possessionStat().miscontrols(), minutes);
        playerStats.add("dispossessed", matchStats.possessionStat().dispossessed(), minutes);
        playerStats.add("passesReceived", matchStats.possessionStat().passesReceived(), minutes);
        playerStats.add("progressivePassesReceived", matchStats.possessionStat().progressivePassesReceived(), minutes);
        playerStats.add("cardsYellow", matchStats.otherStat().cardsYellow(), minutes);
        playerStats.add("cardsRed", matchStats.otherStat().cardsRed(), minutes);
        playerStats.add("cardsYellowRed", matchStats.otherStat().cardsYellowRed(), minutes);
        playerStats.add("fouls", matchStats.otherStat().fouls(), minutes);
        playerStats.add("fouled", matchStats.otherStat().fouled(), minutes);
        playerStats.add("offsides", matchStats.otherStat().offsides(), minutes);
        playerStats.add("pensConceded", matchStats.otherStat().pensConceded(), minutes);
        playerStats.add("ballRecoveries", matchStats.otherStat().ballRecoveries(), minutes);
        playerStats.add("aerialsWon", matchStats.otherStat().aerialsWon(), minutes);
        playerStats.add("aerialsLost", matchStats.otherStat().aerialsLost(), minutes);
        playerStats.add("gkPsxg", matchStats.goalkeeperStat().gkPsxg(), minutes);
        playerStats.add("gkPassesCompletedLaunched", matchStats.goalkeeperStat().gkPassesCompletedLaunched(), minutes);
        playerStats.add("gkPassesLaunched", matchStats.goalkeeperStat().gkPassesLaunched(), minutes);
        playerStats.add("gkPasses", matchStats.goalkeeperStat().gkPasses(), minutes);
        playerStats.add("gkPassesThrows", matchStats.goalkeeperStat().gkPassesThrows(), minutes);
        playerStats.add("gkPctPassesLaunched", matchStats.goalkeeperStat().gkPctPassesLaunched(), minutes);
        playerStats.add("gkPassesLengthAvg", matchStats.goalkeeperStat().gkPassesLengthAvg(), minutes);
        playerStats.add("gkGoalKicks", matchStats.goalkeeperStat().gkGoalKicks(), minutes);
        playerStats.add("gkPctGoalKicksLaunched", matchStats.goalkeeperStat().gkPctGoalKicksLaunched(), minutes);
        playerStats.add("gkGoalKickLengthAvg", matchStats.goalkeeperStat().gkGoalKickLengthAvg(), minutes);
        playerStats.add("gkCrosses", matchStats.goalkeeperStat().gkCrosses(), minutes);
        playerStats.add("gkCrossesStopped", matchStats.goalkeeperStat().gkCrossesStopped(), minutes);
        playerStats.add("gkDefActionsOutsidePenArea", matchStats.goalkeeperStat().gkDefActionsOutsidePenArea(), minutes);
        playerStats.add("gkAvgDistanceDefActions", matchStats.goalkeeperStat().gkAvgDistanceDefActions(), minutes);

        return playerStats;
    }

    private String playerId(String teamName, String playerName) {
        return Base64.getEncoder().encodeToString((teamName + "-" + playerName).getBytes(StandardCharsets.UTF_8));
    }

    private boolean allPlayerHaveRegister(List<String> currentHomeLineup, List<String> currentAwayLineup) {
        boolean allPlayerHaveRegister = true;

        for (String playerId : currentHomeLineup) {
            if (!playerLastStats.containsKey(playerId)) {
                allPlayerHaveRegister = false;
                break;
            }
        }

        for (String playerId : currentAwayLineup) {
            if (!playerLastStats.containsKey(playerId)) {
                allPlayerHaveRegister = false;
                break;
            }
        }
        return allPlayerHaveRegister;
    }

    private Map<Player, List<PlayerStats>> getPlayerMatchStatsOf(TeamMatch teamMatch, List<String> lineup) {

        Map<Player, List<PlayerStats>> completeStats = new HashMap<>();

        for (String playerId : sorted(lineup, teamMatch)) {
            completeStats.put(teams.get(teamMatch.name()).player(nameOf(playerId, teamMatch)), playerLastStats.get(playerId));
        }

        return completeStats;
    }

    private List<String> sorted(List<String> lineup, TeamMatch teamMatch) {
        return lineup.stream()
                .sorted((p1, p2) -> Position.compare(statOf(p1, teamMatch).summaryStat().position(), statOf(p2, teamMatch).summaryStat().position()))
                .collect(Collectors.toList());
    }

    private String nameOf(String playerId, TeamMatch teamMatch) {
        for (String playerName : teamMatch.stats().keySet()) {
            if(playerId(teamMatch.name(), playerName).equals(playerId)) {
                return playerName;
            }
        }

        return null;
    }

    private Stat statOf(String playerId, TeamMatch teamMatch) {
        for (String playerName : teamMatch.stats().keySet()) {
            if(playerId(teamMatch.name(), playerName).equals(playerId)) {
                return teamMatch.stats().get(playerName);
            }
        }

        return null;
    }
}

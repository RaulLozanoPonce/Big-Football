package ui1.raullozano.bigfootball.common.model.transformator;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class Team {

    private final String id;
    private final String name;
    private final Map<String, Player> players = new HashMap<>();
    private int playedMatches = 0;
    private int win = 0;
    private int draw = 0;
    private int lost = 0;
    private int totalPositiveClutchMatches = 0;
    private int totalNegativeClutchMatches = 0;
    private final Map<String, Integer> goalsFor = new HashMap<>();
    private final Map<String, Integer> goalsAgainst = new HashMap<>();
    private final Map<String, LineupStatistics> lineupStatistics = new HashMap<>();

    public Team(String name) {
        this.id = Base64.getEncoder().encodeToString(name.getBytes(StandardCharsets.UTF_8));
        this.name = name;
    }

    public String name() {
        return name;
    }

    public int playedMatches() {
        return playedMatches;
    }

    public Team playedMatches(int playedMatches) {
        this.playedMatches += playedMatches;
        return this;
    }

    public int win() {
        return win;
    }

    public Team win(int win) {
        this.win += win;
        return this;
    }

    public int draw() {
        return draw;
    }

    public Team draw(int draw) {
        this.draw += draw;
        return this;
    }

    public int lost() {
        return lost;
    }

    public Team lost(int lost) {
        this.lost += lost;
        return this;
    }

    public Team totalPositiveClutchMatches(int totalPositiveClutchMatches) {
        this.totalPositiveClutchMatches += totalPositiveClutchMatches;
        return this;
    }

    public Team totalNegativeClutchMatches(int totalNegativeClutchMatches) {
        this.totalNegativeClutchMatches += totalNegativeClutchMatches;
        return this;
    }

    public Map<String, Integer> goalsFor() {
        return goalsFor;
    }

    public Team goalsFor(List<String> goalsFor) {
        for (String goalMinute : goalsFor) {
            this.goalsFor.putIfAbsent(goalMinute, 0);
            this.goalsFor.put(goalMinute, this.goalsFor.get(goalMinute) + 1);
        }
        return this;
    }

    public Map<String, Integer> goalsAgainst() {
        return goalsAgainst;
    }

    public Team goalsAgainst(List<String> goalsAgainst) {
        for (String goalMinute : goalsAgainst) {
            this.goalsAgainst.putIfAbsent(goalMinute, 0);
            this.goalsAgainst.put(goalMinute, this.goalsAgainst.get(goalMinute) + 1);
        }
        return this;
    }

    public Team registerPlayer(Player player) {

        Player mapPlayer = this.players.get(player.name());

        if(mapPlayer != null) {
            mapPlayer.position(player.position());

            mapPlayer.matches().playedMatches(player.matches().playedMatches())
                    .starters(player.matches().starters())
                    .minutes(player.matches().minutes())
                    .goals(player.matches().goals())
                    .assists(player.matches().assists())
                    .tackles(player.matches().successfulTackles(), player.matches().totalTackles())
                    .challenges(player.matches().successfulChallenges(), player.matches().totalChallenges())
                    .aerialDuels(player.matches().successfulAerialDuels(), player.matches().totalAerialDuels())
                    .passes(player.matches().successfulPasses(), player.matches().totalPasses())
                    .xG(player.matches().totalXG())
                    .fouls(player.matches().totalFoulsCommitted(), player.matches().totalFoulsReceived());

            mapPlayer.clutch()
                    .played(player.clutch().played())
                    .contributions(player.clutch().contributions());

            mapPlayer.substitutions()
                    .substituted(player.substitutions().substituted())
                    .substitute(player.substitutions().substitute())
                    .wonPointsWhenSubstituted(player.substitutions().wonPointsWhenSubstituted())
                    .lostPointsWhenSubstituted(player.substitutions().lostPointsWhenSubstituted())
                    .wonPointsWhenSubstitute(player.substitutions().wonPointsWhenSubstitute())
                    .lostPointsWhenSubstitute(player.substitutions().lostPointsWhenSubstitute());
        } else {
            this.players.put(player.name(), player);
        }

        return this;
    }

    public Team registerLineup(List<String> lineup, LineupStatistics lineupStatistics) {
        String lineupString = Arrays.toString(lineup.toArray());
        if(!this.lineupStatistics.containsKey(lineupString)) {
            this.lineupStatistics.put(lineupString, lineupStatistics);
        } else {
            this.lineupStatistics.get(lineupString)
                    .minutes(lineupStatistics.minutes())
                    .goalsFor(lineupStatistics.goalsFor())
                    .goalsAgainst(lineupStatistics.goalsAgainst())
                    .wonMinutes(lineupStatistics.wonMinutes());
        }
        return this;
    }

    public List<Player> players() {
        return new ArrayList<>(players.values());
    }

    public Player player(String playerName) {
        return this.players.get(playerName);
    }

    public Map<String, LineupStatistics> lineupStatistics() {
        return lineupStatistics;
    }

    public int totalPositiveClutchMatches() {
        return totalPositiveClutchMatches;
    }

    public int totalNegativeClutchMatches() {
        return totalNegativeClutchMatches;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

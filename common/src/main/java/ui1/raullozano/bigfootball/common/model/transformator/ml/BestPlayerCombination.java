package ui1.raullozano.bigfootball.common.model.transformator.ml;

import com.google.gson.Gson;
import org.sparkproject.guava.reflect.TypeToken;

import java.util.List;

public class BestPlayerCombination {

    public BestPlayerCombination() {

    }

    public BestPlayerCombination(String line) {
        this.goals = Long.parseLong(line.split(";")[3]);
        this.players = new Gson().fromJson(line.split(";")[4], new TypeToken<List<String>>(){}.getType());
    }

    private String thisTeam;
    private String otherTeam;
    private String lineup;
    private long goals;
    private List<String> players;

    public BestPlayerCombination thisTeam(String thisTeam) {
        this.thisTeam = thisTeam;
        return this;
    }

    public BestPlayerCombination otherTeam(String otherTeam) {
        this.otherTeam = otherTeam;
        return this;
    }

    public BestPlayerCombination lineup(String lineup) {
        this.lineup = lineup;
        return this;
    }

    public BestPlayerCombination goalDifference(long goals) {
        this.goals = goals;
        return this;
    }

    public BestPlayerCombination players(List<String> players) {
        this.players = players;
        return this;
    }

    public long goals() {
        return goals;
    }

    public List<String> players() {
        return players;
    }

    @Override
    public String toString() {
        return thisTeam + ";" + otherTeam + ";" + lineup + ";" + goals + ";" + new Gson().toJson(players) + "\n";
    }
}

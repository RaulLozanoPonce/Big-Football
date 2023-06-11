package ui1.raullozano.bigfootball.common.model.extractor;

import com.google.gson.Gson;
import ui1.raullozano.bigfootball.common.model.extractor.stats.Stat;

import java.util.Map;

public class TeamMatch {

    private String typeTeam;
    private String name;
    private String streak;
    private int fGoals;
    private double xGoals;
    private String coach;
    private String captain;
    private Map<String, Stat> stats;

    public String name() {
        return name;
    }

    public TeamMatch name(String name) {
        this.name = name;
        return this;
    }

    public String streak() {
        return streak;
    }

    public TeamMatch streak(String streak) {
        this.streak = streak;
        return this;
    }

    public int fGoals() {
        return fGoals;
    }

    public TeamMatch fGoals(int fGoals) {
        this.fGoals = fGoals;
        return this;
    }

    public double xGoals() {
        return xGoals;
    }

    public TeamMatch xGoals(double xGoals) {
        this.xGoals = xGoals;
        return this;
    }

    public String coach() {
        return coach;
    }

    public TeamMatch coach(String coach) {
        this.coach = coach;
        return this;
    }

    public String captain() {
        return captain;
    }

    public TeamMatch captain(String captain) {
        this.captain = captain;
        return this;
    }

    public String typeTeam() {
        return typeTeam;
    }

    public TeamMatch typeTeam(String typeTeam) {
        this.typeTeam = typeTeam;
        return this;
    }

    public Map<String, Stat> stats() {
        return stats;
    }

    public TeamMatch stats(Map<String, Stat> stats) {
        this.stats = stats;
        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

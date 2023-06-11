package ui1.raullozano.bigfootball.common.model.extractor;

import com.google.gson.Gson;

import java.time.Instant;
import java.util.List;

public class Match {

    private TeamMatch homeTeam;
    private TeamMatch awayTeam;

    private Instant ts;
    private String competition;
    private Integer attendance;
    private String stadium;
    private String referee;

    private List<MatchEvent> events;

    public TeamMatch homeTeam() {
        return homeTeam;
    }

    public Match homeTeam(TeamMatch homeTeam) {
        this.homeTeam = homeTeam;
        return this;
    }

    public TeamMatch awayTeam() {
        return awayTeam;
    }

    public Match awayTeam(TeamMatch awayTeam) {
        this.awayTeam = awayTeam;
        return this;
    }

    public Instant ts() {
        return ts;
    }

    public Match ts(Instant ts) {
        this.ts = ts;
        return this;
    }

    public String competition() {
        return competition;
    }

    public Object competition(String competition) {
        this.competition = competition;
        return this;
    }

    public int attendance() {
        return attendance;
    }

    public Match attendance(Integer attendance) {
        this.attendance = attendance;
        return this;
    }

    public String stadium() {
        return stadium;
    }

    public Match stadium(String stadium) {
        this.stadium = stadium;
        return this;
    }

    public String referee() {
        return referee;
    }

    public Match referee(String referee) {
        this.referee = referee;
        return this;
    }

    public List<MatchEvent> events() {
        return events;
    }

    public Match events(List<MatchEvent> events) {
        this.events = events;
        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

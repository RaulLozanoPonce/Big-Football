package ui1.raullozano.bigfootball.common.model.extractor;

import com.google.gson.Gson;

public class MatchEvent {

    private String minute;
    private MatchEventType type;
    private String team;
    private String player;
    private String otherPlayer;

    public String minute() {
        return minute;
    }

    public MatchEvent minute(String minute) {
        this.minute = minute;
        return this;
    }

    public MatchEventType type() {
        return type;
    }

    public MatchEvent type(MatchEventType type) {
        this.type = type;
        return this;
    }

    public String team() {
        return team;
    }

    public MatchEvent team(String team) {
        this.team = team;
        return this;
    }

    public String player() {
        return player;
    }

    public MatchEvent player(String player) {
        this.player = player;
        return this;
    }

    public String otherPlayer() {
        return otherPlayer;
    }

    public MatchEvent otherPlayer(String otherPlayer) {
        this.otherPlayer = otherPlayer;
        return this;
    }

    public enum MatchEventType {
        MissedPenalty, Goal, OwnGoal, PenaltyGoal, YellowCard, DoubleYellowCard, RedCard, Substitution
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

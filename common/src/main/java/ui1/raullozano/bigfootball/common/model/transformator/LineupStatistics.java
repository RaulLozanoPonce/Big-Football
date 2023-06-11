package ui1.raullozano.bigfootball.common.model.transformator;

public class LineupStatistics {

    private int minutes;
    private int goalsFor;
    private int goalsAgainst;
    private int wonMinutes;

    public int minutes() {
        return minutes;
    }

    public LineupStatistics minutes(int minutes) {
        this.minutes += minutes;
        return this;
    }

    public int goalsFor() {
        return goalsFor;
    }

    public LineupStatistics goalsFor(int goalsFor) {
        this.goalsFor += goalsFor;
        return this;
    }

    public int goalsAgainst() {
        return goalsAgainst;
    }

    public LineupStatistics goalsAgainst(int goalsAgainst) {
        this.goalsAgainst += goalsAgainst;
        return this;
    }

    public int wonMinutes() {
        return wonMinutes;
    }

    public LineupStatistics wonMinutes(int wonMinutes) {
        this.wonMinutes += wonMinutes;
        return this;
    }
}

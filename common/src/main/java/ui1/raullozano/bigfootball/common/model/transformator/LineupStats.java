package ui1.raullozano.bigfootball.common.model.transformator;

public class LineupStats {

    private double goalDifference = 0;
    private double minutes = 0;

    public void add(double goalDifference, double minutes) {
        this.goalDifference += goalDifference;
        this.minutes += minutes;
    }

    public double get() {
        if(minutes == 0) return 0;
        return goalDifference / minutes;
    }
}

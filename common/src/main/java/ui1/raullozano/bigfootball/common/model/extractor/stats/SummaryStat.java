package ui1.raullozano.bigfootball.common.model.extractor.stats;

import com.google.gson.Gson;

public class SummaryStat {

    private boolean starting;
    private Integer shirtNumber;
    private String nationality;
    private String position;
    private String age;
    private Integer minutes;
    private Integer goals;
    private Integer penaltyGoals;
    private Integer penaltyTried;
    private Integer shots;
    private Integer targetShots;
    private Double xG;
    private Double noPenaltyXG;
    private Integer shotActions;
    private Integer goalActions;

    public boolean starting() {
        return starting;
    }

    public SummaryStat starting(boolean starting) {
        this.starting = starting;
        return this;
    }

    public Integer shirtNumber() {
        return shirtNumber;
    }

    public SummaryStat shirtNumber(String shirtNumber) {
        if(!shirtNumber.isEmpty()) {
            this.shirtNumber = Integer.valueOf(shirtNumber);
        }

        return this;
    }

    public String nationality() {
        return nationality;
    }

    public SummaryStat nationality(String nationality) {
        this.nationality = nationality;
        return this;
    }

    public String position() {
        return position;
    }

    public SummaryStat position(String position) {
        this.position = position;
        return this;
    }

    public String age() {
        return age;
    }

    public SummaryStat age(String age) {
        this.age = age;
        return this;
    }

    public Integer minutes() {
        return minutes;
    }

    public SummaryStat minutes(String minutes) {
        if(!minutes.isEmpty()) {
            this.minutes = Integer.valueOf(minutes);
        } else {
            this.minutes = 1;
        }

        return this;
    }

    public Integer goals() {
        return goals;
    }

    public SummaryStat goals(String goals) {
        if(!goals.isEmpty()) {
            this.goals = Integer.valueOf(goals);
        }

        return this;
    }

    public Integer penaltyGoals() {
        return penaltyGoals;
    }

    public SummaryStat penaltyGoals(String penaltyGoals) {
        if(!penaltyGoals.isEmpty()) {
            this.penaltyGoals = Integer.valueOf(penaltyGoals);
        }

        return this;
    }

    public Integer penaltyTried() {
        return penaltyTried;
    }

    public SummaryStat penaltyTried(String penaltyTried) {
        if(!penaltyTried.isEmpty()) {
            this.penaltyTried = Integer.valueOf(penaltyTried);
        }

        return this;
    }

    public Integer shots() {
        return shots;
    }

    public SummaryStat shots(String shots) {
        if(!shots.isEmpty()) {
            this.shots = Integer.valueOf(shots);
        }

        return this;
    }

    public Integer targetShots() {
        return targetShots;
    }

    public SummaryStat targetShots(String targetShots) {
        if(!targetShots.isEmpty()) {
            this.targetShots = Integer.valueOf(targetShots);
        }

        return this;
    }

    public Double xG() {
        return xG;
    }

    public SummaryStat xG(String xG) {
        if(!xG.isEmpty()) {
            this.xG = Double.valueOf(xG);
        }

        return this;
    }

    public Double noPenaltyXG() {
        return noPenaltyXG;
    }

    public SummaryStat noPenaltyXG(String noPenaltyXG) {
        if(!noPenaltyXG.isEmpty()) {
            this.noPenaltyXG = Double.valueOf(noPenaltyXG);
        }

        return this;
    }

    public Integer shotActions() {
        return shotActions;
    }

    public SummaryStat shotActions(String shotActions) {
        if(!shotActions.isEmpty()) {
            this.shotActions = Integer.valueOf(shotActions);
        }

        return this;
    }

    public Integer goalActions() {
        return goalActions;
    }

    public SummaryStat goalActions(String goalActions) {
        if(!goalActions.isEmpty()) {
            this.goalActions = Integer.valueOf(goalActions);
        }

        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

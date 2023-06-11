package ui1.raullozano.bigfootball.common.model.extractor.stats;

import com.google.gson.Gson;

public class GoalkeeperStat {

    private Integer gkShotsOnTargetAgainst;
    private Integer gkGoalsAgainst = null;
    private Integer gkSaves = null;
    private Double gkPsxg;
    private Integer gkPassesCompletedLaunched;
    private Integer gkPassesLaunched;
    private Integer gkPasses;
    private Integer gkPassesThrows;
    private Double gkPctPassesLaunched = null;
    private Double gkPassesLengthAvg = null;
    private Integer gkGoalKicks;
    private Double gkPctGoalKicksLaunched = null;
    private Double gkGoalKickLengthAvg = null;
    private Integer gkCrosses;
    private Integer gkCrossesStopped = null;
    private Integer gkDefActionsOutsidePenArea = null;
    private Double gkAvgDistanceDefActions = null;

    public Integer gkShotsOnTargetAgainst() {
        return gkShotsOnTargetAgainst;
    }

    public GoalkeeperStat gkShotsOnTargetAgainst(String gkShotsOnTargetAgainst) {
        if(!gkShotsOnTargetAgainst.isEmpty()) {
            this.gkShotsOnTargetAgainst = Integer.valueOf(gkShotsOnTargetAgainst);
        }

        return this;
    }

    public Integer gkGoalsAgainst() {
        return gkGoalsAgainst;
    }

    public GoalkeeperStat gkGoalsAgainst(String gkGoalsAgainst) {
        if(!gkGoalsAgainst.isEmpty()) {
            this.gkGoalsAgainst = Integer.valueOf(gkGoalsAgainst);
        }

        return this;
    }

    public Integer gkSaves() {
        return gkSaves;
    }

    public GoalkeeperStat gkSaves(String gkSaves) {
        if(!gkSaves.isEmpty()) {
            this.gkSaves = Integer.valueOf(gkSaves);
        }

        return this;
    }

    public Double gkPsxg() {
        return gkPsxg;
    }

    public GoalkeeperStat gkPsxg(String gkPsxg) {
        if(!gkPsxg.isEmpty()) {
            this.gkPsxg = Double.valueOf(gkPsxg);
        }

        return this;
    }

    public Integer gkPassesCompletedLaunched() {
        return gkPassesCompletedLaunched;
    }

    public GoalkeeperStat gkPassesCompletedLaunched(String gkPassesCompletedLaunched) {
        if(!gkPassesCompletedLaunched.isEmpty()) {
            this.gkPassesCompletedLaunched = Integer.valueOf(gkPassesCompletedLaunched);
        }

        return this;
    }

    public Integer gkPassesLaunched() {
        return gkPassesLaunched;
    }

    public GoalkeeperStat gkPassesLaunched(String gkPassesLaunched) {
        if(!gkPassesLaunched.isEmpty()) {
            this.gkPassesLaunched = Integer.valueOf(gkPassesLaunched);
        }

        return this;
    }

    public Integer gkPasses() {
        return gkPasses;
    }

    public GoalkeeperStat gkPasses(String gkPasses) {
        if(!gkPasses.isEmpty()) {
            this.gkPasses = Integer.valueOf(gkPasses);
        }

        return this;
    }

    public Integer gkPassesThrows() {
        return gkPassesThrows;
    }

    public GoalkeeperStat gkPassesThrows(String gkPassesThrows) {
        if(!gkPassesThrows.isEmpty()) {
            this.gkPassesThrows = Integer.valueOf(gkPassesThrows);
        }

        return this;
    }

    public Double gkPctPassesLaunched() {
        return gkPctPassesLaunched;
    }

    public GoalkeeperStat gkPctPassesLaunched(String gkPctPassesLaunched) {
        if(!gkPctPassesLaunched.isEmpty()) {
            this.gkPctPassesLaunched = Double.valueOf(gkPctPassesLaunched);
        }

        return this;
    }

    public Double gkPassesLengthAvg() {
        return gkPassesLengthAvg;
    }

    public GoalkeeperStat gkPassesLengthAvg(String gkPassesLengthAvg) {
        if(!gkPassesLengthAvg.isEmpty()) {
            this.gkPassesLengthAvg = Double.valueOf(gkPassesLengthAvg);
        }

        return this;
    }

    public Integer gkGoalKicks() {
        return gkGoalKicks;
    }

    public GoalkeeperStat gkGoalKicks(String gkGoalKicks) {
        if(!gkGoalKicks.isEmpty()) {
            this.gkGoalKicks = Integer.valueOf(gkGoalKicks);
        }

        return this;
    }

    public Double gkPctGoalKicksLaunched() {
        return gkPctGoalKicksLaunched;
    }

    public GoalkeeperStat gkPctGoalKicksLaunched(String gkPctGoalKicksLaunched) {
        if(!gkPctGoalKicksLaunched.isEmpty()) {
            this.gkPctGoalKicksLaunched = Double.valueOf(gkPctGoalKicksLaunched);
        }

        return this;
    }

    public Double gkGoalKickLengthAvg() {
        return gkGoalKickLengthAvg;
    }

    public GoalkeeperStat gkGoalKickLengthAvg(String gkGoalKickLengthAvg) {
        if(!gkGoalKickLengthAvg.isEmpty()) {
            this.gkGoalKickLengthAvg = Double.valueOf(gkGoalKickLengthAvg);
        }

        return this;
    }

    public Integer gkCrosses() {
        return gkCrosses;
    }

    public GoalkeeperStat gkCrosses(String gkCrosses) {
        if(!gkCrosses.isEmpty()) {
            this.gkCrosses = Integer.valueOf(gkCrosses);
        }

        return this;
    }

    public Integer gkCrossesStopped() {
        return gkCrossesStopped;
    }

    public GoalkeeperStat gkCrossesStopped(String gkCrossesStopped) {
        if(!gkCrossesStopped.isEmpty()) {
            this.gkCrossesStopped = Integer.valueOf(gkCrossesStopped);
        }

        return this;
    }

    public Integer gkDefActionsOutsidePenArea() {
        return gkDefActionsOutsidePenArea;
    }

    public GoalkeeperStat gkDefActionsOutsidePenArea(String gkDefActionsOutsidePenArea) {
        if(!gkDefActionsOutsidePenArea.isEmpty()) {
            this.gkDefActionsOutsidePenArea = Integer.valueOf(gkDefActionsOutsidePenArea);
        }

        return this;
    }

    public Double gkAvgDistanceDefActions() {
        return gkAvgDistanceDefActions;
    }

    public GoalkeeperStat gkAvgDistanceDefActions(String gkAvgDistanceDefActions) {
        if(!gkAvgDistanceDefActions.isEmpty()) {
            this.gkAvgDistanceDefActions = Double.valueOf(gkAvgDistanceDefActions);
        }

        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

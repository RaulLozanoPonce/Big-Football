package ui1.raullozano.bigfootball.common.model.extractor.stats;

import com.google.gson.Gson;

public class PassStat {

    private Integer passesCompleted;
    private Integer passes;
    private Integer passesTotalDistance;
    private Integer passesProgressiveDistance;
    private Integer passesCompletedShort;
    private Integer passesShort;
    private Integer passesCompletedMedium;
    private Integer passesMedium;
    private Integer passesCompletedLong;
    private Integer passesLong;
    private Integer assists;
    private Double xgAssists;
    private Double passXa;
    private Integer assistedShots;
    private Integer passesIntoFinalThird;
    private Integer passesIntoPenaltyArea;
    private Integer crossesIntoPenaltyArea;
    private Integer progressivePasses;

    public Integer passesCompleted() {
        return passesCompleted;
    }

    public PassStat passesCompleted(String passesCompleted) {
        if(!passesCompleted.isEmpty()) {
            this.passesCompleted = Integer.valueOf(passesCompleted);
        }

        return this;
    }

    public Integer passes() {
        return passes;
    }

    public PassStat passes(String passes) {
        if(!passes.isEmpty()) {
            this.passes = Integer.valueOf(passes);
        }

        return this;
    }

    public Integer passesTotalDistance() {
        return passesTotalDistance;
    }

    public PassStat passesTotalDistance(String passesTotalDistance) {
        if(!passesTotalDistance.isEmpty()) {
            this.passesTotalDistance = Integer.valueOf(passesTotalDistance);
        }

        return this;
    }

    public Integer passesProgressiveDistance() {
        return passesProgressiveDistance;
    }

    public PassStat passesProgressiveDistance(String passesProgressiveDistance) {
        if(!passesProgressiveDistance.isEmpty()) {
            this.passesProgressiveDistance = Integer.valueOf(passesProgressiveDistance);
        }

        return this;
    }

    public Integer passesCompletedShort() {
        return passesCompletedShort;
    }

    public PassStat passesCompletedShort(String passesCompletedShort) {
        if(!passesCompletedShort.isEmpty()) {
            this.passesCompletedShort = Integer.valueOf(passesCompletedShort);
        }

        return this;
    }

    public Integer passesShort() {
        return passesShort;
    }

    public PassStat passesShort(String passesShort) {
        if(!passesShort.isEmpty()) {
            this.passesShort = Integer.valueOf(passesShort);
        }

        return this;
    }

    public Integer passesCompletedMedium() {
        return passesCompletedMedium;
    }

    public PassStat passesCompletedMedium(String passesCompletedMedium) {
        if(!passesCompletedMedium.isEmpty()) {
            this.passesCompletedMedium = Integer.valueOf(passesCompletedMedium);
        }

        return this;
    }

    public Integer passesMedium() {
        return passesMedium;
    }

    public PassStat passesMedium(String passesMedium) {
        if(!passesMedium.isEmpty()) {
            this.passesMedium = Integer.valueOf(passesMedium);
        }

        return this;
    }

    public Integer passesCompletedLong() {
        return passesCompletedLong;
    }

    public PassStat passesCompletedLong(String passesCompletedLong) {
        if(!passesCompletedLong.isEmpty()) {
            this.passesCompletedLong = Integer.valueOf(passesCompletedLong);
        }

        return this;
    }

    public Integer passesLong() {
        return passesLong;
    }

    public PassStat passesLong(String passesLong) {
        if(!passesLong.isEmpty()) {
            this.passesLong = Integer.valueOf(passesLong);
        }

        return this;
    }

    public Integer assists() {
        return assists;
    }

    public PassStat assists(String assists) {
        if(!assists.isEmpty()) {
            this.assists = Integer.valueOf(assists);
        }

        return this;
    }

    public Double xgAssists() {
        return xgAssists;
    }

    public PassStat xgAssists(String xgAssists) {
        if(!xgAssists.isEmpty()) {
            this.xgAssists = Double.valueOf(xgAssists);
        }

        return this;
    }

    public Double passXa() {
        return passXa;
    }

    public PassStat passXa(String passXa) {
        if(!passXa.isEmpty()) {
            this.passXa = Double.valueOf(passXa);
        }

        return this;
    }

    public Integer assistedShots() {
        return assistedShots;
    }

    public PassStat assistedShots(String assistedShots) {
        if(!assistedShots.isEmpty()) {
            this.assistedShots = Integer.valueOf(assistedShots);
        }

        return this;
    }

    public Integer passesIntoFinalThird() {
        return passesIntoFinalThird;
    }

    public PassStat passesIntoFinalThird(String passesIntoFinalThird) {
        if(!passesIntoFinalThird.isEmpty()) {
            this.passesIntoFinalThird = Integer.valueOf(passesIntoFinalThird);
        }

        return this;
    }

    public Integer passesIntoPenaltyArea() {
        return passesIntoPenaltyArea;
    }

    public PassStat passesIntoPenaltyArea(String passesIntoPenaltyArea) {
        if(!passesIntoPenaltyArea.isEmpty()) {
            this.passesIntoPenaltyArea = Integer.valueOf(passesIntoPenaltyArea);
        }

        return this;
    }

    public Integer crossesIntoPenaltyArea() {
        return crossesIntoPenaltyArea;
    }

    public PassStat crossesIntoPenaltyArea(String crossesIntoPenaltyArea) {
        if(!crossesIntoPenaltyArea.isEmpty()) {
            this.crossesIntoPenaltyArea = Integer.valueOf(crossesIntoPenaltyArea);
        }

        return this;
    }

    public Integer progressivePasses() {
        return progressivePasses;
    }

    public PassStat progressivePasses(String progressivePasses) {
        if(!progressivePasses.isEmpty()) {
            this.progressivePasses = Integer.valueOf(progressivePasses);
        }

        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

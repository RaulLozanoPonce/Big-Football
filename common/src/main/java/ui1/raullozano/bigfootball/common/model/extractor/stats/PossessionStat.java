package ui1.raullozano.bigfootball.common.model.extractor.stats;

import com.google.gson.Gson;

public class PossessionStat {

    private Integer touches;
    private Integer touchesDefPenArea;
    private Integer touchesDef3rd;
    private Integer touchesMid3rd;
    private Integer touchesAtt3rd;
    private Integer touchesAttPenArea;
    private Integer touchesLiveBall;
    private Integer takeOns;
    private Integer takeOnsWon;
    private Integer takeOnsTackled;
    private Double takeOnsTackledPct;
    private Integer carries;
    private Integer carriesDistance;
    private Integer carriesProgressiveDistance;
    private Integer progressiveCarries;
    private Integer carriesIntoFinalThird;
    private Integer carriesIntoPenaltyArea;
    private Integer miscontrols;
    private Integer dispossessed;
    private Integer passesReceived;
    private Integer progressivePassesReceived;

    public Integer touches() {
        return touches;
    }

    public PossessionStat touches(String touches) {
        if(!touches.isEmpty()) {
            this.touches = Integer.valueOf(touches);
        }

        return this;
    }

    public Integer touchesDefPenArea() {
        return touchesDefPenArea;
    }

    public PossessionStat touchesDefPenArea(String touchesDefPenArea) {
        if(!touchesDefPenArea.isEmpty()) {
            this.touchesDefPenArea = Integer.valueOf(touchesDefPenArea);
        }

        return this;
    }

    public Integer touchesDef3rd() {
        return touchesDef3rd;
    }

    public PossessionStat touchesDef3rd(String touchesDef3rd) {
        if(!touchesDef3rd.isEmpty()) {
            this.touchesDef3rd = Integer.valueOf(touchesDef3rd);
        }

        return this;
    }

    public Integer touchesMid3rd() {
        return touchesMid3rd;
    }

    public PossessionStat touchesMid3rd(String touchesMid3rd) {
        if(!touchesMid3rd.isEmpty()) {
            this.touchesMid3rd = Integer.valueOf(touchesMid3rd);
        }

        return this;
    }

    public Integer touchesAtt3rd() {
        return touchesAtt3rd;
    }

    public PossessionStat touchesAtt3rd(String touchesAtt3rd) {
        if(!touchesAtt3rd.isEmpty()) {
            this.touchesAtt3rd = Integer.valueOf(touchesAtt3rd);
        }

        return this;
    }

    public Integer touchesAttPenArea() {
        return touchesAttPenArea;
    }

    public PossessionStat touchesAttPenArea(String touchesAttPenArea) {
        if(!touchesAttPenArea.isEmpty()) {
            this.touchesAttPenArea = Integer.valueOf(touchesAttPenArea);
        }

        return this;
    }

    public Integer touchesLiveBall() {
        return touchesLiveBall;
    }

    public PossessionStat touchesLiveBall(String touchesLiveBall) {
        if(!touchesLiveBall.isEmpty()) {
            this.touchesLiveBall = Integer.valueOf(touchesLiveBall);
        }

        return this;
    }

    public Integer takeOns() {
        return takeOns;
    }

    public PossessionStat takeOns(String takeOns) {
        if(!takeOns.isEmpty()) {
            this.takeOns = Integer.valueOf(takeOns);
        }

        return this;
    }

    public Integer takeOnsWon() {
        return takeOnsWon;
    }

    public PossessionStat takeOnsWon(String takeOnsWon) {
        if(!takeOnsWon.isEmpty()) {
            this.takeOnsWon = Integer.valueOf(takeOnsWon);
        }

        return this;
    }

    public Integer takeOnsTackled() {
        return takeOnsTackled;
    }

    public PossessionStat takeOnsTackled(String takeOnsTackled) {
        if(!takeOnsTackled.isEmpty()) {
            this.takeOnsTackled = Integer.valueOf(takeOnsTackled);
        }

        return this;
    }

    public Double takeOnsTackledPct() {
        return takeOnsTackledPct;
    }

    public PossessionStat takeOnsTackledPct(String takeOnsTackledPct) {
        if(!takeOnsTackledPct.isEmpty()) {
            this.takeOnsTackledPct = Double.valueOf(takeOnsTackledPct);
        }

        return this;
    }

    public Integer carries() {
        return carries;
    }

    public PossessionStat carries(String carries) {
        if(!carries.isEmpty()) {
            this.carries = Integer.valueOf(carries);
        }

        return this;
    }

    public Integer carriesDistance() {
        return carriesDistance;
    }

    public PossessionStat carriesDistance(String carriesDistance) {
        if(!carriesDistance.isEmpty()) {
            this.carriesDistance = Integer.valueOf(carriesDistance);
        }

        return this;
    }

    public Integer carriesProgressiveDistance() {
        return carriesProgressiveDistance;
    }

    public PossessionStat carriesProgressiveDistance(String carriesProgressiveDistance) {
        if(!carriesProgressiveDistance.isEmpty()) {
            this.carriesProgressiveDistance = Integer.valueOf(carriesProgressiveDistance);
        }

        return this;
    }

    public Integer progressiveCarries() {
        return progressiveCarries;
    }

    public PossessionStat progressiveCarries(String progressiveCarries) {
        if(!progressiveCarries.isEmpty()) {
            this.progressiveCarries = Integer.valueOf(progressiveCarries);
        }

        return this;
    }

    public Integer carriesIntoFinalThird() {
        return carriesIntoFinalThird;
    }

    public PossessionStat carriesIntoFinalThird(String carriesIntoFinalThird) {
        if(!carriesIntoFinalThird.isEmpty()) {
            this.carriesIntoFinalThird = Integer.valueOf(carriesIntoFinalThird);
        }

        return this;
    }

    public Integer carriesIntoPenaltyArea() {
        return carriesIntoPenaltyArea;
    }

    public PossessionStat carriesIntoPenaltyArea(String carriesIntoPenaltyArea) {
        if(!carriesIntoPenaltyArea.isEmpty()) {
            this.carriesIntoPenaltyArea = Integer.valueOf(carriesIntoPenaltyArea);
        }

        return this;
    }

    public Integer miscontrols() {
        return miscontrols;
    }

    public PossessionStat miscontrols(String miscontrols) {
        if(!miscontrols.isEmpty()) {
            this.miscontrols = Integer.valueOf(miscontrols);
        }

        return this;
    }

    public Integer dispossessed() {
        return dispossessed;
    }

    public PossessionStat dispossessed(String dispossessed) {
        if(!dispossessed.isEmpty()) {
            this.dispossessed = Integer.valueOf(dispossessed);
        }

        return this;
    }

    public Integer passesReceived() {
        return passesReceived;
    }

    public PossessionStat passesReceived(String passesReceived) {
        if(!passesReceived.isEmpty()) {
            this.passesReceived = Integer.valueOf(passesReceived);
        }

        return this;
    }

    public Integer progressivePassesReceived() {
        return progressivePassesReceived;
    }

    public PossessionStat progressivePassesReceived(String progressivePassesReceived) {
        if(!progressivePassesReceived.isEmpty()) {
            this.progressivePassesReceived = Integer.valueOf(progressivePassesReceived);
        }

        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

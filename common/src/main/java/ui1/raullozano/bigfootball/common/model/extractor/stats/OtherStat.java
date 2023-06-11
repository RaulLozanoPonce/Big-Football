package ui1.raullozano.bigfootball.common.model.extractor.stats;

import com.google.gson.Gson;

public class OtherStat {

    private Integer cardsYellow;
    private Integer cardsRed;
    private Integer cardsYellowRed;
    private Integer fouls;
    private Integer fouled;
    private Integer offsides;
    private Integer pensConceded;
    private Integer ballRecoveries;
    private Integer aerialsWon;
    private Integer aerialsLost;

    public Integer cardsYellow() {
        return cardsYellow;
    }

    public OtherStat cardsYellow(String cardsYellow) {
        if(!cardsYellow.isEmpty()) {
            this.cardsYellow = Integer.valueOf(cardsYellow);
        }

        return this;
    }

    public Integer cardsRed() {
        return cardsRed;
    }

    public OtherStat cardsRed(String cardsRed) {
        if(!cardsRed.isEmpty()) {
            this.cardsRed = Integer.valueOf(cardsRed);
        }

        return this;
    }

    public Integer cardsYellowRed() {
        return cardsYellowRed;
    }

    public OtherStat cardsYellowRed(String cardsYellowRed) {
        if(!cardsYellowRed.isEmpty()) {
            this.cardsYellowRed = Integer.valueOf(cardsYellowRed);
        }

        return this;
    }

    public Integer fouls() {
        return fouls;
    }

    public OtherStat fouls(String fouls) {
        if(!fouls.isEmpty()) {
            this.fouls = Integer.valueOf(fouls);
        }

        return this;
    }

    public Integer fouled() {
        return fouled;
    }

    public OtherStat fouled(String fouled) {
        if(!fouled.isEmpty()) {
            this.fouled = Integer.valueOf(fouled);
        }

        return this;
    }

    public Integer offsides() {
        return offsides;
    }

    public OtherStat offsides(String offsides) {
        if(!offsides.isEmpty()) {
            this.offsides = Integer.valueOf(offsides);
        }

        return this;
    }

    public Integer pensConceded() {
        return pensConceded;
    }

    public OtherStat pensConceded(String pensConceded) {
        if(!pensConceded.isEmpty()) {
            this.pensConceded = Integer.valueOf(pensConceded);
        }

        return this;
    }

    public Integer ballRecoveries() {
        return ballRecoveries;
    }

    public OtherStat ballRecoveries(String ballRecoveries) {
        if(!ballRecoveries.isEmpty()) {
            this.ballRecoveries = Integer.valueOf(ballRecoveries);
        }

        return this;
    }

    public Integer aerialsWon() {
        return aerialsWon;
    }

    public OtherStat aerialsWon(String aerialsWon) {
        if(!aerialsWon.isEmpty()) {
            this.aerialsWon = Integer.valueOf(aerialsWon);
        }

        return this;
    }

    public Integer aerialsLost() {
        return aerialsLost;
    }

    public OtherStat aerialsLost(String aerialsLost) {
        if(!aerialsLost.isEmpty()) {
            this.aerialsLost = Integer.valueOf(aerialsLost);
        }

        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

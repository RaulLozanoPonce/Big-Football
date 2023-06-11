package ui1.raullozano.bigfootball.common.model.extractor.stats;

import com.google.gson.Gson;

public class DefensiveStat {

    private Integer tackles;
    private Integer tacklesWon;
    private Integer tacklesDef3rd;
    private Integer tacklesMid3rd;
    private Integer tacklesAtt3rd;
    private Integer challengeTackles;
    private Integer challenges;
    private Integer challengesLost;
    private Integer blocks;
    private Integer blockedShots;
    private Integer blockedPasses;
    private Integer interceptions;
    private Integer clearances;
    private Integer errors;

    public Integer tackles() {
        return tackles;
    }

    public DefensiveStat tackles(String tackles) {
        if(!tackles.isEmpty()) {
            this.tackles = Integer.valueOf(tackles);
        }

        return this;
    }

    public Integer tacklesWon() {
        return tacklesWon;
    }

    public DefensiveStat tacklesWon(String tacklesWon) {
        if(!tacklesWon.isEmpty()) {
            this.tacklesWon = Integer.valueOf(tacklesWon);
        }

        return this;
    }

    public Integer tacklesDef3rd() {
        return tacklesDef3rd;
    }

    public DefensiveStat tacklesDef3rd(String tacklesDef3rd) {
        if(!tacklesDef3rd.isEmpty()) {
            this.tacklesDef3rd = Integer.valueOf(tacklesDef3rd);
        }

        return this;
    }

    public Integer tacklesMid3rd() {
        return tacklesMid3rd;
    }

    public DefensiveStat tacklesMid3rd(String tacklesMid3rd) {
        if(!tacklesMid3rd.isEmpty()) {
            this.tacklesMid3rd = Integer.valueOf(tacklesMid3rd);
        }

        return this;
    }

    public Integer tacklesAtt3rd() {
        return tacklesAtt3rd;
    }

    public DefensiveStat tacklesAtt3rd(String tacklesAtt3rd) {
        if(!tacklesAtt3rd.isEmpty()) {
            this.tacklesAtt3rd = Integer.valueOf(tacklesAtt3rd);
        }

        return this;
    }

    public Integer challengeTackles() {
        return challengeTackles;
    }

    public DefensiveStat challengeTackles(String challengeTackles) {
        if(!challengeTackles.isEmpty()) {
            this.challengeTackles = Integer.valueOf(challengeTackles);
        }

        return this;
    }

    public Integer challenges() {
        return challenges;
    }

    public DefensiveStat challenges(String challenges) {
        if(!challenges.isEmpty()) {
            this.challenges = Integer.valueOf(challenges);
        }

        return this;
    }

    public Integer challengesLost() {
        return challengesLost;
    }

    public DefensiveStat challengesLost(String challengesLost) {
        if(!challengesLost.isEmpty()) {
            this.challengesLost = Integer.valueOf(challengesLost);
        }

        return this;
    }

    public Integer blocks() {
        return blocks;
    }

    public DefensiveStat blocks(String blocks) {
        if(!blocks.isEmpty()) {
            this.blocks = Integer.valueOf(blocks);
        }

        return this;
    }

    public Integer blockedShots() {
        return blockedShots;
    }

    public DefensiveStat blockedShots(String blockedShots) {
        if(!blockedShots.isEmpty()) {
            this.blockedShots = Integer.valueOf(blockedShots);
        }

        return this;
    }

    public Integer blockedPasses() {
        return blockedPasses;
    }

    public DefensiveStat blockedPasses(String blockedPasses) {
        if(!blockedPasses.isEmpty()) {
            this.blockedPasses = Integer.valueOf(blockedPasses);
        }

        return this;
    }

    public Integer interceptions() {
        return interceptions;
    }

    public DefensiveStat interceptions(String interceptions) {
        if(!interceptions.isEmpty()) {
            this.interceptions = Integer.valueOf(interceptions);
        }

        return this;
    }

    public Integer clearances() {
        return clearances;
    }

    public DefensiveStat clearances(String clearances) {
        if(!clearances.isEmpty()) {
            this.clearances = Integer.valueOf(clearances);
        }

        return this;
    }

    public Integer errors() {
        return errors;
    }

    public DefensiveStat errors(String errors) {
        if(!errors.isEmpty()) {
            this.errors = Integer.valueOf(errors);
        }

        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

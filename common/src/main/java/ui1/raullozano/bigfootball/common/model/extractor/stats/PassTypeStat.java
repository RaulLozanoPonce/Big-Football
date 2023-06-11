package ui1.raullozano.bigfootball.common.model.extractor.stats;

import com.google.gson.Gson;

public class PassTypeStat {

    private Integer passesLive;
    private Integer passesDead;
    private Integer passesFreeKick;
    private Integer throughBalls;
    private Integer passesSwitches;
    private Integer crosses;
    private Integer throwIns;
    private Integer cornerKicks;
    private Integer cornerKicksIn;
    private Integer cornerKicksOut;
    private Integer cornerKicksStraight;
    private Integer passesOffsides;
    private Integer passesBlocked;

    public Integer passesLive() {
        return passesLive;
    }

    public PassTypeStat passesLive(String passesLive) {
        if(!passesLive.isEmpty()) {
            this.passesLive = Integer.valueOf(passesLive);
        }

        return this;
    }

    public Integer passesDead() {
        return passesDead;
    }

    public PassTypeStat passesDead(String passesDead) {
        if(!passesDead.isEmpty()) {
            this.passesDead = Integer.valueOf(passesDead);
        }

        return this;
    }

    public Integer passesFreeKick() {
        return passesFreeKick;
    }

    public PassTypeStat passesFreeKick(String passesFreeKick) {
        if(!passesFreeKick.isEmpty()) {
            this.passesFreeKick = Integer.valueOf(passesFreeKick);
        }

        return this;
    }

    public Integer throughBalls() {
        return throughBalls;
    }

    public PassTypeStat throughBalls(String throughBalls) {
        if(!throughBalls.isEmpty()) {
            this.throughBalls = Integer.valueOf(throughBalls);
        }

        return this;
    }

    public Integer passesSwitches() {
        return passesSwitches;
    }

    public PassTypeStat passesSwitches(String passesSwitches) {
        if(!passesSwitches.isEmpty()) {
            this.passesSwitches = Integer.valueOf(passesSwitches);
        }

        return this;
    }

    public Integer crosses() {
        return crosses;
    }

    public PassTypeStat crosses(String crosses) {
        if(!crosses.isEmpty()) {
            this.crosses = Integer.valueOf(crosses);
        }

        return this;
    }

    public Integer throwIns() {
        return throwIns;
    }

    public PassTypeStat throwIns(String throwIns) {
        if(!throwIns.isEmpty()) {
            this.throwIns = Integer.valueOf(throwIns);
        }

        return this;
    }

    public Integer cornerKicks() {
        return cornerKicks;
    }

    public PassTypeStat cornerKicks(String cornerKicks) {
        if(!cornerKicks.isEmpty()) {
            this.cornerKicks = Integer.valueOf(cornerKicks);
        }

        return this;
    }

    public Integer cornerKicksIn() {
        return cornerKicksIn;
    }

    public PassTypeStat cornerKicksIn(String cornerKicksIn) {
        if(!cornerKicksIn.isEmpty()) {
            this.cornerKicksIn = Integer.valueOf(cornerKicksIn);
        }

        return this;
    }

    public Integer cornerKicksOut() {
        return cornerKicksOut;
    }

    public PassTypeStat cornerKicksOut(String cornerKicksOut) {
        if(!cornerKicksOut.isEmpty()) {
            this.cornerKicksOut = Integer.valueOf(cornerKicksOut);
        }

        return this;
    }

    public Integer cornerKicksStraight() {
        return cornerKicksStraight;
    }

    public PassTypeStat cornerKicksStraight(String cornerKicksStraight) {
        if(!cornerKicksStraight.isEmpty()) {
            this.cornerKicksStraight = Integer.valueOf(cornerKicksStraight);
        }

        return this;
    }

    public Integer passesOffsides() {
        return passesOffsides;
    }

    public PassTypeStat passesOffsides(String passesOffsides) {
        if(!passesOffsides.isEmpty()) {
            this.passesOffsides = Integer.valueOf(passesOffsides);
        }

        return this;
    }

    public Integer passesBlocked() {
        return passesBlocked;
    }

    public PassTypeStat passesBlocked(String passesBlocked) {
        if(!passesBlocked.isEmpty()) {
            this.passesBlocked = Integer.valueOf(passesBlocked);
        }

        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

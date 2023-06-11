package ui1.raullozano.bigfootball.common.model.extractor.stats;

import com.google.gson.Gson;

public class Stat {

    private final SummaryStat summaryStat;
    private final PassStat passStat;
    private final PassTypeStat passTypeStat;
    private final DefensiveStat defensiveStat;
    private final PossessionStat possessionStat;
    private final OtherStat otherStat;
    private final GoalkeeperStat goalkeeperStat;

    public Stat() {
        this.summaryStat = new SummaryStat();
        this.passStat = new PassStat();
        this.passTypeStat = new PassTypeStat();
        this.defensiveStat = new DefensiveStat();
        this.possessionStat = new PossessionStat();
        this.otherStat = new OtherStat();
        this.goalkeeperStat = new GoalkeeperStat();
    }

    public SummaryStat summaryStat() {
        return summaryStat;
    }

    public PassStat passStat() {
        return passStat;
    }

    public PassTypeStat passTypeStat() {
        return passTypeStat;
    }

    public DefensiveStat defensiveStat() {
        return defensiveStat;
    }

    public PossessionStat possessionStat() {
        return possessionStat;
    }

    public OtherStat otherStat() {
        return otherStat;
    }

    public GoalkeeperStat goalkeeperStat() {
        return goalkeeperStat;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

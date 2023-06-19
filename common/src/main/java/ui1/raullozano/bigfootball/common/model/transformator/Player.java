package ui1.raullozano.bigfootball.common.model.transformator;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Player {

    private final String id;
    private final String name;
    private final Map<Position, Integer> position = new HashMap<>();
    private int age;
    private final MatchesStatistics matchesStatistics;
    private final ClutchStatistics clutchStatistics;
    private final SubstitutionsStatistics substitutionsStatistics;

    public Player(String teamName, String name) {
        this.id = Base64.getEncoder().encodeToString((teamName + "-" + name).getBytes(StandardCharsets.UTF_8));
        this.name = name;
        this.matchesStatistics = new MatchesStatistics();
        this.clutchStatistics = new ClutchStatistics();
        this.substitutionsStatistics = new SubstitutionsStatistics();
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Map<Position, Integer> position() {
        return position;
    }

    public Position finalPosition() {
        return position.entrySet().stream().reduce((e1, e2) -> {
            if (e1.getValue() > e2.getValue()) {
                return e1;
            } else {
                return e2;
            }
        }).map(Map.Entry::getKey).orElse(null);
    }

    public Player position(Map<Position, Integer> position) {
        for (Position key : position.keySet()) {
            for (int i = 0; i < position.get(key); i++) {
                position(key);
            }
        }
        return this;
    }

    public Player position(Set<Position> positions) {
        positions.forEach(this::position);
        return this;
    }

    private Player position(Position position) {
        this.position.putIfAbsent(position, 0);
        this.position.put(position, this.position.get(position) + 1);
        return this;
    }

    public int age() {
        return age;
    }

    public Player age(int age) {
        this.age = age;
        return this;
    }

    public MatchesStatistics matches() {
        return matchesStatistics;
    }

    public ClutchStatistics clutch() {
        return clutchStatistics;
    }

    public SubstitutionsStatistics substitutions() {
        return substitutionsStatistics;
    }

    public static class MatchesStatistics {

        private int playedMatches = 0;
        private int starters = 0;
        private int minutes = 0;
        private int goals = 0;
        private int assists = 0;
        private int totalTackles = 0;
        private int successfulTackles = 0;
        private int totalChallenges = 0;
        private int successfulChallenges = 0;
        private int totalAerialDuels = 0;
        private int successfulAerialDuels = 0;
        private int totalPasses = 0;
        private int successfulPasses = 0;
        private double totalXG = 0;
        private int totalFoulsCommitted = 0;
        private int totalFoulsReceived = 0;

        public int playedMatches() {
            return playedMatches;
        }

        public MatchesStatistics playedMatches(Integer playedMatches) {
            if(playedMatches == null) return this;
            this.playedMatches += playedMatches;
            return this;
        }

        public int starters() {
            return starters;
        }

        public MatchesStatistics starters(Integer starters) {
            if(starters == null) return this;
            this.starters += starters;
            return this;
        }

        public int minutes() {
            return minutes;
        }

        public MatchesStatistics minutes(Integer minutes) {
            if(minutes == null) return this;
            this.minutes += minutes;
            return this;
        }

        public int goals() {
            return goals;
        }

        public MatchesStatistics goals(Integer goals) {
            if(goals == null) return this;
            this.goals += goals;
            return this;
        }

        public int assists() {
            return assists;
        }

        public MatchesStatistics assists(Integer assists) {
            if(assists == null) return this;
            this.assists += assists;
            return this;
        }

        public double tacklesFactor() {
            if(totalTackles == 0.0) return 0.0;
            return successfulTackles / (double) totalTackles;
        }

        public MatchesStatistics tackles(Integer successfulTackles, Integer totalTackles) {
            if(successfulTackles != null) this.successfulTackles += successfulTackles;
            if(totalTackles != null) this.totalTackles += totalTackles;
            return this;
        }

        public double challengesFactor() {
            if(totalChallenges == 0.0) return 0.0;
            return successfulChallenges / (double) totalChallenges;
        }

        public MatchesStatistics challenges(Integer successfulChallenges, Integer totalChallenges) {
            if(successfulChallenges != null) this.successfulChallenges += successfulChallenges;
            if(totalChallenges != null) this.totalChallenges += totalChallenges;
            return this;
        }

        public double aerialDuelsFactor() {
            if(totalAerialDuels == 0.0) return 0.0;
            return successfulAerialDuels / (double) totalAerialDuels;
        }

        public MatchesStatistics aerialDuels(Integer successfulAerialDuels, Integer lostAerialDuels) {
            if(successfulAerialDuels != null) this.successfulAerialDuels += successfulAerialDuels;
            if(successfulAerialDuels != null && lostAerialDuels != null) this.totalAerialDuels += successfulAerialDuels + lostAerialDuels;
            return this;
        }

        public double passesFactor() {
            if(totalPasses == 0.0) return 0.0;
            return successfulPasses / (double) totalPasses;
        }

        public MatchesStatistics passes(Integer successfulPasses, Integer totalPasses) {
            if(successfulPasses != null) this.successfulPasses += successfulPasses;
            if(totalPasses != null) this.totalPasses += totalPasses;
            return this;
        }

        public double xGFactor() {
            if(minutes == 0.0) return 0.0;
            return totalXG / (double) minutes;
        }

        public MatchesStatistics xG(Double xG) {
            if(xG != null) this.totalXG += xG;
            return this;
        }

        public double foulsFactor() {
            if(minutes == 0.0) return 0.0;
            return (totalFoulsReceived - totalFoulsCommitted) / (double) minutes;
        }

        public MatchesStatistics fouls(Integer totalFoulsCommitted, Integer totalFoulsReceived) {
            if(totalFoulsCommitted != null) this.totalFoulsCommitted += totalFoulsCommitted;
            if(totalFoulsReceived != null) this.totalFoulsReceived += totalFoulsReceived;
            return this;
        }

        public int totalTackles() {
            return totalTackles;
        }

        public int successfulTackles() {
            return successfulTackles;
        }

        public int totalChallenges() {
            return totalChallenges;
        }

        public int successfulChallenges() {
            return successfulChallenges;
        }

        public int totalAerialDuels() {
            return totalAerialDuels;
        }

        public int successfulAerialDuels() {
            return successfulAerialDuels;
        }

        public int totalPasses() {
            return totalPasses;
        }

        public int successfulPasses() {
            return successfulPasses;
        }

        public double totalXG() {
            return totalXG;
        }

        public int totalFoulsCommitted() {
            return totalFoulsCommitted;
        }

        public int totalFoulsReceived() {
            return totalFoulsReceived;
        }
    }

    public static class ClutchStatistics {

        public static final int StartingMinute = 80;

        private int played = 0;
        private int contributions = 0;

        public ClutchStatistics played(int played) {
            this.played += played;
            return this;
        }

        public ClutchStatistics contributions(int contributions) {
            this.contributions += contributions;
            return this;
        }

        public int played() {
            return played;
        }

        public int contributions() {
            return contributions;
        }
    }

    public static class SubstitutionsStatistics {

        private int substituted = 0;
        private int substitute = 0;
        private int wonPointsWhenSubstituted = 0;
        private int lostPointsWhenSubstituted = 0;
        private int wonPointsWhenSubstitute = 0;
        private int lostPointsWhenSubstitute = 0;

        public int substituted() {
            return substituted;
        }

        public int substitute() {
            return substitute;
        }

        public int wonPointsWhenSubstituted() {
            return wonPointsWhenSubstituted;
        }

        public int lostPointsWhenSubstituted() {
            return lostPointsWhenSubstituted;
        }

        public int wonPointsWhenSubstitute() {
            return wonPointsWhenSubstitute;
        }

        public int lostPointsWhenSubstitute() {
            return lostPointsWhenSubstitute;
        }

        public SubstitutionsStatistics substituted(int substituted) {
            this.substituted += substituted;
            return this;
        }

        public SubstitutionsStatistics substitute(int substitute) {
            this.substitute += substitute;
            return this;
        }

        public SubstitutionsStatistics wonPointsWhenSubstituted(int wonPointsWhenSubstituted) {
            this.wonPointsWhenSubstituted += wonPointsWhenSubstituted;
            return this;
        }

        public SubstitutionsStatistics lostPointsWhenSubstituted(int lostPointsWhenSubstituted) {
            this.lostPointsWhenSubstituted += lostPointsWhenSubstituted;
            return this;
        }

        public SubstitutionsStatistics wonPointsWhenSubstitute(int wonPointsWhenSubstitute) {
            this.wonPointsWhenSubstitute += wonPointsWhenSubstitute;
            return this;
        }

        public SubstitutionsStatistics lostPointsWhenSubstitute(int lostPointsWhenSubstitute) {
            this.lostPointsWhenSubstitute += lostPointsWhenSubstitute;
            return this;
        }
    }
}

package ui1.raullozano.bigfootball.common.model.transformator.temp_stats;

import ui1.raullozano.bigfootball.common.model.transformator.Position;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PlayerStats {

    private Position position;
    private final LinkedHashMap<String, Triple> attributes = new LinkedHashMap<>();


    public Position position() {
        return position;
    }

    public PlayerStats position(Position position) {
        this.position = position;
        return this;
    }

    public Double get(String attribute) {
        return this.attributes.get(attribute).get();
    }

    public PlayerStats add(String attribute, Integer accumulated, Double minutes) {
        this.attributes.putIfAbsent(attribute, new Triple());
        this.attributes.get(attribute).add(accumulated, minutes);
        return this;
    }

    public PlayerStats add(String attribute, Double accumulated, Double minutes) {
        this.attributes.putIfAbsent(attribute, new Triple());
        this.attributes.get(attribute).add(accumulated, minutes);
        return this;
    }

    public List<String> attributes() {
        return new ArrayList<>(attributes.keySet());
    }

    private static class Triple {

        private double accumulated = 0;
        private double matches = 0;

        public void add(Double accumulated, Double minutes) {
            if(accumulated != null) {
                this.accumulated += accumulated * minutes/90.0;
            }
            this.matches += 1;
        }

        public void add(Integer accumulated, Double minutes) {
            if(accumulated != null) {
                this.accumulated += accumulated * minutes/90.0;
            }
            this.matches += 1;
        }

        public Double get() {
            if(matches == 0) return null;
            return accumulated / matches;
        }
    }
}

package ui1.raullozano.bigfootball.bigfootball.chart.utils;

import java.util.ArrayList;
import java.util.List;

public class ChartInfo {

    private String title;
    private String type;
    private List<Serie> series = new ArrayList<>();
    private String unit;
    private List<String> xAxisValues;
    private String xAxisUnit;
    private Integer tickInterval;
    private Value yAxisMin;
    private Value yAxisMax;
    private List<Band> plotBands;
    private int decimals;

    public String title() {
        return title;
    }

    public String type() {
        return type;
    }

    public List<Serie> series() {
        return series;
    }

    public String unit() {
        return unit;
    }

    public List<String> xAxisValues() {
        return xAxisValues;
    }

    public String xAxisUnit() {
        return xAxisUnit;
    }

    public Integer tickInterval() {
        return tickInterval;
    }

    public Value yAxisMin() {
        return yAxisMin;
    }

    public Value yAxisMax() {
        return yAxisMax;
    }

    public List<Band> plotBands() {
        return plotBands;
    }

    public void title(String title) {
        this.title = title;
    }

    public void type(String type) {
        this.type = type;
    }

    public void series(List<Serie> series) {
        this.series = series;
    }

    public void unit(String unit) {
        this.unit = unit;
    }

    public void xAxisValues(List<String> xAxisValues) {
        this.xAxisValues = xAxisValues;
    }

    public void xAxisUnit(String xAxisUnit) {
        this.xAxisUnit = xAxisUnit;
    }

    public void tickInterval(Integer tickInterval) {
        this.tickInterval = tickInterval;
    }

    public int decimals() {
        return decimals;
    }

    public void decimals(int decimals) {
        this.decimals = decimals;
    }

    public abstract static class Serie {
        private String name;
        private String color;
        private MarkerAttribute marker;

        public void name(String name) {
            this.name = name;
        }

        public void color(String color) {
            this.color = color;
        }

        public void marker(boolean marker) {
            this.marker = new MarkerAttribute(marker);
        }
    }

    public static class LineSerie extends Serie {
        private List<Double[]> data;

        public void data(List<Double[]> data) {
            this.data = data;
        }
    }

    public static class PieSerie extends Serie {
        private List<PieData> data = new ArrayList<>();

        public void data(PieData data) {
            this.data.add(data);
        }

        public static class PieData {
            private final String name;
            private final Double y;
            private String color;

            public PieData(String name, Double y, String color) {
                this.name = name;
                this.y = y;
                this.color = color;
            }
        }
    }

    public static class MarkerAttribute {
        private final Boolean enabled;

        public MarkerAttribute(Boolean enabled) {
            this.enabled = enabled;
        }
    }

    private static class Value {
        private Double value;
    }

    private static class Band {
        private Double from;
        private Double to;
        private String color;
    }
}
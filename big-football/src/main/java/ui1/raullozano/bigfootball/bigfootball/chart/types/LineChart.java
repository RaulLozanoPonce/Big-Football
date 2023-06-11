package ui1.raullozano.bigfootball.bigfootball.chart.types;

import ui1.raullozano.bigfootball.bigfootball.chart.utils.ChartInfo;

import java.util.*;

public abstract class LineChart extends Chart {

    private final Map<String, List<Double[]>> data = new HashMap<>();

    protected LineChart(String type, String title, String xUnit, String yUnit, int decimals) {
        super(type, title, xUnit, yUnit, decimals);
    }

    public LineChart addData(String name, LinkedHashMap<String, Double[]> data, String color) {

        for (String key : data.keySet()) {
            this.data.computeIfAbsent(key, k -> new ArrayList<>()).add(data.get(key));
        }

        ChartInfo.LineSerie serie = new ChartInfo.LineSerie();
        serie.data(new ArrayList<>(data.values()));
        serie.color(color);
        serie.name(name);
        serie.marker(true);

        addData(serie, new ArrayList<>(data.keySet()));

        return this;
    }
}
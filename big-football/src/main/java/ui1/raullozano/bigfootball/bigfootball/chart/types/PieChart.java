package ui1.raullozano.bigfootball.bigfootball.chart.types;

import ui1.raullozano.bigfootball.bigfootball.chart.utils.ChartInfo;

public class PieChart extends Chart {

    private final ChartInfo.PieSerie serie = new ChartInfo.PieSerie();

    public PieChart(String title, String xUnit, String yUnit, int decimals) {
        super("pie", title, xUnit, yUnit, decimals);
        this.plotInfo.series().add(serie);
    }

    public PieChart addData(String name, double value, String color) {
        ChartInfo.PieSerie.PieData data = new ChartInfo.PieSerie.PieData(name, value, color);
        serie.data(data);
        return this;
    }
}
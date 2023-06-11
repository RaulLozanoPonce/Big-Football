package ui1.raullozano.bigfootball.bigfootball.chart.types;

import ui1.raullozano.bigfootball.bigfootball.chart.utils.ChartInfo;

import java.util.List;

public abstract class Chart {

    protected ChartInfo plotInfo = new ChartInfo();

    protected Chart(String type, String title, String xUnit, String yUnit, int decimals) {
        plotInfo.type(type);
        plotInfo.title(title);
        plotInfo.xAxisUnit(xUnit);
        plotInfo.unit(yUnit);
        plotInfo.decimals(decimals);
    }

    public Chart addData(ChartInfo.Serie serie, List<String> xAxis) {

        plotInfo.series().add(serie);
        plotInfo.xAxisValues(xAxis);
        plotInfo.tickInterval((int) Math.round(xAxis.size() / 15.0));

        return this;
    }

    public ChartInfo getInfo() {
        return plotInfo;
    }
}
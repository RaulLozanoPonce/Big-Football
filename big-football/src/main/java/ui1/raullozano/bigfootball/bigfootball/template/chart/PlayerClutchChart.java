package ui1.raullozano.bigfootball.bigfootball.template.chart;

import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.model.transformator.Player;
import ui1.raullozano.bigfootball.common.model.transformator.Team;
import ui1.raullozano.bigfootball.bigfootball.chart.types.ScatterChart;
import ui1.raullozano.bigfootball.bigfootball.chart.utils.ChartHelper;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerClutchChart {

    private final Team team;

    public PlayerClutchChart(FileAccessor fileAccessor, Map<String, String> params) {
        this.team = fileAccessor.getTeam(params.get("competition"), params.get("season"), params.get("team"));
    }

    public String getChart() {

        if(team == null) return "<html></html>";

        ScatterChart chart = new ScatterChart("Puntuación de contribuciones por partido jugado a partir del minuto " + Player.ClutchStatistics.StartingMinute + " en los que se han ganado puntos", "partidos jugados a partir del minuto " + Player.ClutchStatistics.StartingMinute + " en los que se han ganado puntos", "puntos", 0);

        for (Player player : team.players()) {
            if(player.clutch().played() > 0) {
                chart.addData(player.name(), getContributionsByClutchDataOf(player), "#000000");
            }
        }

        return "<html>" +
                    "<header>" +
                        "<script src='https://code.highcharts.com/highcharts.js'></script>" +
                    "</header>" +
                    "<body>" +
                        "<div id='player-clutch' style='height:100%; width: 100%;'>" + ChartHelper.getGraph(getClass(), "player-clutch", chart.getInfo()) + "</div>" +
                    "</body>" +
                "</html>";
    }

    private LinkedHashMap<String, Double[]> getContributionsByClutchDataOf(Player player) {
        LinkedHashMap<String, Double[]> data = new LinkedHashMap<>();
        data.put(player.name(), new Double[]{(double) player.clutch().played(), (double) player.clutch().contributions()});
        return data;
    }
}

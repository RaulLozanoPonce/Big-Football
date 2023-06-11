package ui1.raullozano.bigfootball.bigfootball.template.chart;

import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.model.transformator.Player;
import ui1.raullozano.bigfootball.common.model.transformator.Team;
import ui1.raullozano.bigfootball.bigfootball.chart.types.PieChart;
import ui1.raullozano.bigfootball.bigfootball.chart.utils.ChartHelper;

import java.util.Map;

public class TeamClutchChart {

    private final Team team;

    public TeamClutchChart(FileAccessor fileAccessor, Map<String, String> params) {
        this.team = fileAccessor.getTeam(params.get("competition"), params.get("season"), params.get("team"));
    }

    public String getChart() {

        if(team == null) return "<html></html>";

        PieChart chart = new PieChart("Partidos en los que cambia la distribución de puntos a partir del minuto " + Player.ClutchStatistics.StartingMinute, "", "", 0);
        chart.addData("Clutch positivos", team.totalPositiveClutchMatches(), "limegreen");
        chart.addData("Clutch negativos", team.totalNegativeClutchMatches(), "red");
        chart.addData("Resto de partidos", team.playedMatches() - team.totalPositiveClutchMatches() - team.totalNegativeClutchMatches(), "grey");

        return "<html>" +
                    "<header>" +
                        "<script src='https://code.highcharts.com/highcharts.js'></script>" +
                    "</header>" +
                    "<body>" +
                        "<div id='player-clutch-contributions-by-clutches' style='height:100%; width: 100%;'>" + ChartHelper.getGraph(getClass(), "player-clutch-contributions-by-clutches", chart.getInfo()) + "</div>" +
                    "</body>" +
                "</html>";
    }
}

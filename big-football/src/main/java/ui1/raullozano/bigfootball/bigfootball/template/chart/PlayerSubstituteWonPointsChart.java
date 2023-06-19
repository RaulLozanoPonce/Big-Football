package ui1.raullozano.bigfootball.bigfootball.template.chart;

import ui1.raullozano.bigfootball.bigfootball.chart.types.ScatterChart;
import ui1.raullozano.bigfootball.bigfootball.chart.utils.ChartHelper;
import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.model.transformator.Player;
import ui1.raullozano.bigfootball.common.model.transformator.Team;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerSubstituteWonPointsChart {

    private final Team team;

    public PlayerSubstituteWonPointsChart(FileAccessor fileAccessor, Map<String, String> params) {
        this.team = fileAccessor.getTeam(params.get("competition"), params.get("season"), params.get("team"));
    }

    public String getChart() {

        if(team == null) return "<html></html>";

        ScatterChart chart = new ScatterChart("Puntos ganados desde que sustituye", "partidos como sustituto", "puntos ganados", 0);

        for (Player player : team.players()) {
            if(player.substitutions().substitute() > 0) {
                chart.addData(player.name(), getSubstituteDataOf(player), "#228B22");
            }
        }

        return "<html>" +
                    "<header>" +
                        "<script src='https://code.highcharts.com/highcharts.js'></script>" +
                    "</header>" +
                    "<body>" +
                        "<div id='player-substitute-won-points' style='height:100%; width: 100%;'>" + ChartHelper.getGraph(getClass(), "player-substitute-won-points", chart.getInfo()) + "</div>" +
                    "</body>" +
                "</html>";
    }

    private LinkedHashMap<String, Double[]> getSubstituteDataOf(Player player) {
        LinkedHashMap<String, Double[]> data = new LinkedHashMap<>();
        data.put(player.name(), new Double[]{(double) player.substitutions().substitute(), (double) player.substitutions().wonPointsWhenSubstitute()});
        return data;
    }
}

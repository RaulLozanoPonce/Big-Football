package ui1.raullozano.bigfootball.bigfootball.template.chart;

import ui1.raullozano.bigfootball.bigfootball.chart.types.ScatterChart;
import ui1.raullozano.bigfootball.bigfootball.chart.utils.ChartHelper;
import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.model.transformator.Player;
import ui1.raullozano.bigfootball.common.model.transformator.Team;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerSubstitutionContributedPointsChart {

    private final Team team;

    public PlayerSubstitutionContributedPointsChart(FileAccessor fileAccessor, Map<String, String> params) {
        this.team = fileAccessor.getTeam(params.get("competition"), params.get("season"), params.get("team"));
    }

    public String getChart() {

        if(team == null) return "<html></html>";

        ScatterChart chart = new ScatterChart("Puntos que aporta en sustituciones", "partidos en los que rota", "puntos ganados/perdidos", 0);

        for (Player player : team.players()) {
            if(player.substitutions().substitute() + player.substitutions().substituted() > 0) {
                chart.addData(player.name(), getDataOf(player), "#B8D8EB");
            }
        }

        return "<html>" +
                    "<header>" +
                        "<script src='https://code.highcharts.com/highcharts.js'></script>" +
                    "</header>" +
                    "<body>" +
                        "<div id='player-substitutions-contributed-points' style='height:100%; width: 100%;'>" + ChartHelper.getGraph(getClass(), "player-substitutions-contributed-points", chart.getInfo()) + "</div>" +
                    "</body>" +
                "</html>";
    }

    private LinkedHashMap<String, Double[]> getDataOf(Player player) {
        LinkedHashMap<String, Double[]> data = new LinkedHashMap<>();
        data.put(player.name(), new Double[]{(double) player.substitutions().substitute() + player.substitutions().substituted(), contributesPoints(player)});
        return data;
    }

    private static double contributesPoints(Player player) {
        double value = 0.0;
        if(player.substitutions().substitute() > 0) {
            value += player.substitutions().wonPointsWhenSubstitute() - player.substitutions().lostPointsWhenSubstitute();
        }
        if(player.substitutions().substituted() > 0) {
            value += player.substitutions().lostPointsWhenSubstituted() - player.substitutions().wonPointsWhenSubstituted();
        }

        return value;
    }
}

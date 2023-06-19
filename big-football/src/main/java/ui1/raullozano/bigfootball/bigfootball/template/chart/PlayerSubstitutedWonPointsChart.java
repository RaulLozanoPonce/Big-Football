package ui1.raullozano.bigfootball.bigfootball.template.chart;

import ui1.raullozano.bigfootball.bigfootball.chart.types.ScatterChart;
import ui1.raullozano.bigfootball.bigfootball.chart.utils.ChartHelper;
import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.model.transformator.Player;
import ui1.raullozano.bigfootball.common.model.transformator.Team;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerSubstitutedWonPointsChart {

    private final Team team;

    public PlayerSubstitutedWonPointsChart(FileAccessor fileAccessor, Map<String, String> params) {
        this.team = fileAccessor.getTeam(params.get("competition"), params.get("season"), params.get("team"));
    }

    public String getChart() {

        if(team == null) return "<html></html>";

        ScatterChart chart = new ScatterChart("Puntos ganados desde que se ha sustituido", "partidos sustituidos", "puntos ganados", 0);

        for (Player player : team.players()) {
            if(player.substitutions().substituted() > 0) {
                chart.addData(player.name(), getSubstitutedDataOf(player), "#228B22");
            }
        }

        return "<html>" +
                    "<header>" +
                        "<script src='https://code.highcharts.com/highcharts.js'></script>" +
                    "</header>" +
                    "<body>" +
                        "<div id='player-substituted-won-points' style='height:100%; width: 100%;'>" + ChartHelper.getGraph(getClass(), "player-substituted-won-points", chart.getInfo()) + "</div>" +
                    "</body>" +
                "</html>";
    }

    private LinkedHashMap<String, Double[]> getSubstitutedDataOf(Player player) {
        LinkedHashMap<String, Double[]> data = new LinkedHashMap<>();
        data.put(player.name(), new Double[]{(double) player.substitutions().substituted(), (double) player.substitutions().wonPointsWhenSubstituted()});
        return data;
    }
}

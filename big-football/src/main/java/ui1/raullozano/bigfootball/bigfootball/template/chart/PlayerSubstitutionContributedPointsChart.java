package ui1.raullozano.bigfootball.bigfootball.template.chart;

import com.google.gson.Gson;
import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.model.transformator.Player;
import ui1.raullozano.bigfootball.common.model.transformator.Team;
import ui1.raullozano.bigfootball.bigfootball.chart.types.ColumnChart;
import ui1.raullozano.bigfootball.bigfootball.chart.utils.ChartHelper;
import ui1.raullozano.bigfootball.bigfootball.chart.utils.ChartInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerSubstitutionContributedPointsChart {

    private final Team team;

    public PlayerSubstitutionContributedPointsChart(FileAccessor fileAccessor, Map<String, String> params) {
        this.team = fileAccessor.getTeam(params.get("competition"), params.get("season"), params.get("team"));
    }

    public String getChart() {

        if(team == null) return "<html></html>";

        ChartInfo chartInfo = new ColumnChart("Puntos que aporta en sustituciones", "jugador", "puntos aportados", 0)
                .addData("Puntos que aporta en sustituciones", getDataOf(team), "#B8D8EB")
                .getInfo();

        return "<html>" +
                    "<header>" +
                        "<script src='https://code.highcharts.com/highcharts.js'></script>" +
                    "</header>" +
                    "<body>" +
                        "<div id='player-substitutions-contributed-points' style='height:100%; width: 100%;'>" + ChartHelper.getGraph(getClass(), "player-substitutions-contributed-points", chartInfo) + "</div>" +
                    "</body>" +
                "</html>";
    }

    private LinkedHashMap<String, Double[]> getDataOf(Team team) {
        List<Player> players = team.players().stream()
                .filter(p -> p.substitutions().substituted() > 0 || p.substitutions().substitute() > 0)
                .sorted((p1, p2) -> Double.compare(contributesPoints(p2), contributesPoints(p1)))
                .collect(Collectors.toList());

        LinkedHashMap<String, Double[]> data = new LinkedHashMap<>();

        for (Player player : players) {
            data.put(player.name(), new Double[]{contributesPoints(player)});
        }

        return data;
    }

    private static double contributesPoints(Player player) {
        double value = 0.0;
        if(player.substitutions().substitute() > 0) {
            value += (player.substitutions().wonPointsWhenSubstitute() - player.substitutions().lostPointsWhenSubstitute()) / (double) player.substitutions().substitute();
        }
        if(player.substitutions().substituted() > 0) {
            value += (player.substitutions().lostPointsWhenSubstituted() - player.substitutions().wonPointsWhenSubstituted()) / (double) player.substitutions().substituted();
        }

        return value;
    }
}

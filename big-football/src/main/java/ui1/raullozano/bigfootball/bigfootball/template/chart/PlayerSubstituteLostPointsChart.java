package ui1.raullozano.bigfootball.bigfootball.template.chart;

import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.model.transformator.Player;
import ui1.raullozano.bigfootball.common.model.transformator.Team;
import ui1.raullozano.bigfootball.bigfootball.chart.types.ColumnChart;
import ui1.raullozano.bigfootball.bigfootball.chart.utils.ChartHelper;
import ui1.raullozano.bigfootball.bigfootball.chart.utils.ChartInfo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerSubstituteLostPointsChart {

    private final Team team;

    public PlayerSubstituteLostPointsChart(FileAccessor fileAccessor, Map<String, String> params) {
        this.team = fileAccessor.getTeam(params.get("competition"), params.get("season"), params.get("team"));
    }

    public String getChart() {

        if(team == null) return "<html></html>";

        ChartInfo chartInfo = new ColumnChart("Puntos perdidos desde que sustituye", "jugador", "puntos perdidos por partido", 2)
                .addData("Puntos perdidos desde que sustituye", getSubstituteDataOf(team), "#F50000")
                .getInfo();

        return "<html>" +
                    "<header>" +
                        "<script src='https://code.highcharts.com/highcharts.js'></script>" +
                    "</header>" +
                    "<body>" +
                        "<div id='player-substitute-lost-points' style='height:100%; width: 100%;'>" + ChartHelper.getGraph(getClass(), "player-substitute-lost-points", chartInfo) + "</div>" +
                    "</body>" +
                "</html>";
    }

    private LinkedHashMap<String, Double[]> getSubstituteDataOf(Team team) {
        List<Player> players = team.players().stream()
                .filter(p -> p.substitutions().substitute() > 0)
                .sorted((p1, p2) -> Double.compare(p2.substitutions().lostPointsWhenSubstitute()/ (double) p2.substitutions().substitute(), p1.substitutions().lostPointsWhenSubstitute()/ (double) p1.substitutions().substitute()))
                .collect(Collectors.toList()).subList(0, 5);

        LinkedHashMap<String, Double[]> data = new LinkedHashMap<>();

        for (Player player : players) {
            data.put(player.name(), new Double[]{player.substitutions().lostPointsWhenSubstitute()/ (double) player.substitutions().substitute()});
        }

        return data;
    }
}

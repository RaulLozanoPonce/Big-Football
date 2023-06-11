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

public class PlayerSubstitutedLostPointsChart {

    private final Team team;

    public PlayerSubstitutedLostPointsChart(FileAccessor fileAccessor, Map<String, String> params) {
        this.team = fileAccessor.getTeam(params.get("competition"), params.get("season"), params.get("team"));
    }

    public String getChart() {

        if(team == null) return "<html></html>";

        ChartInfo chartInfo = new ColumnChart("Puntos perdidos desde que se ha sustituido", "jugador", "puntos perdidos por partido", 2)
                .addData("Puntos perdidos desde que se ha sustituido", getSubstitutedDataOf(team), "#F50000")
                .getInfo();

        return "<html>" +
                    "<header>" +
                        "<script src='https://code.highcharts.com/highcharts.js'></script>" +
                    "</header>" +
                    "<body>" +
                        "<div id='player-substituted-lost-points' style='height:100%; width: 100%;'>" + ChartHelper.getGraph(getClass(), "player-substituted-lost-points", chartInfo) + "</div>" +
                    "</body>" +
                "</html>";
    }

    private LinkedHashMap<String, Double[]> getSubstitutedDataOf(Team team) {
        List<Player> players = team.players().stream()
                .filter(p -> p.substitutions().substituted() > 0)
                .sorted((p1, p2) -> Double.compare(p2.substitutions().lostPointsWhenSubstituted()/ (double) p2.substitutions().substituted(), p1.substitutions().lostPointsWhenSubstituted()/ (double) p1.substitutions().substituted()))
                .collect(Collectors.toList()).subList(0, 5);

        LinkedHashMap<String, Double[]> data = new LinkedHashMap<>();

        for (Player player : players) {
            data.put(player.name(), new Double[]{player.substitutions().lostPointsWhenSubstituted() / (double) player.substitutions().substituted()});
        }

        return data;
    }
}
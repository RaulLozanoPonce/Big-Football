package ui1.raullozano.bigfootball.bigfootball.template.chart;

import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.model.transformator.Team;
import ui1.raullozano.bigfootball.bigfootball.chart.types.ColumnChart;
import ui1.raullozano.bigfootball.bigfootball.chart.utils.ChartHelper;
import ui1.raullozano.bigfootball.bigfootball.chart.utils.ChartInfo;
import ui1.raullozano.bigfootball.common.utils.MatchUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TeamGoalsForChart {

    private final Team team;

    public TeamGoalsForChart(FileAccessor fileAccessor, Map<String, String> params) {
        this.team = fileAccessor.getTeam(params.get("competition"), params.get("season"), params.get("team"));
    }

    public String getChart() {

        if(team == null) return "<html></html>";

        ChartInfo chartInfo = new ColumnChart("Frecuencia de goles marcados según el minuto", "minuto",
                "goles marcados", 0)
                .addData("Goles marcados", getDataOf(team), "#228B22")
                .getInfo();

        return "<html>" +
                    "<header>" +
                        "<script src='https://code.highcharts.com/highcharts.js'></script>" +
                    "</header>" +
                    "<body>" +
                        "<div id='team-goals-for' style='height:100%; width: 100%;'>" + ChartHelper.getGraph(getClass(),
                "team-goals-for", chartInfo) + "</div>" +
                    "</body>" +
                "</html>";
    }

    private LinkedHashMap<String, Double[]> getDataOf(Team team) {

        Map<String, Integer> goalsFor = team.goalsFor();

        Map<String, Integer> aggregatedGoals = new LinkedHashMap<>();

        for (String minute : minutesOf(goalsFor.keySet())) {
            int minuteInt = MatchUtils.minuteOf(minute);
            String label;
            if(minuteInt <= 15) {
                label = "0-15";
            } else if(minuteInt <= 30) {
                label = "16-30";
            } else if(minuteInt <= 45) {
                label = "31-45";
            } else {
                if(minute.contains("+")) {
                    if(minuteInt < 90) {
                        label = "Descuento 1a";
                    } else {
                        label = "Descuento 2a";
                    }
                } else {
                    if(minuteInt <= 60) {
                        label = "45-60";
                    } else if(minuteInt <= 75) {
                        label = "61-75";
                    } else {
                        label = "76-90";
                    }
                }
            }

            aggregatedGoals.putIfAbsent(label, 0);
            aggregatedGoals.put(label, aggregatedGoals.get(label) + goalsFor.get(minute));
        }

        LinkedHashMap<String, Double[]> data = new LinkedHashMap<>();

        for (String range : aggregatedGoals.keySet()) {
            data.put(range, new Double[]{Double.valueOf(aggregatedGoals.get(range))});
        }

        return data;
    }

    private List<String> minutesOf(Set<String> minutes) {
        return minutes.stream().sorted(this::compareMinutes).collect(Collectors.toList());
    }

    private int compareMinutes(String m1, String m2) {
        Integer m1Int = MatchUtils.minuteOf(m1);
        Integer m2Int = MatchUtils.minuteOf(m2);

        if(m1Int.equals(m2Int)) {
            if(m1.contains("+")) {
                if(m2.contains("+")) {
                    return Integer.valueOf(m1.split("\\+")[1]).compareTo(Integer.valueOf(m2.split("\\+")[1]));
                } else {
                    return -1;
                }
            } else {
                return 1;
            }
        } else {
            return m1Int.compareTo(m2Int);
        }
    }
}

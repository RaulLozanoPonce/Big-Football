package ui1.raullozano.bigfootball.bigfootball.template.chart;

import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.model.transformator.Team;
import ui1.raullozano.bigfootball.common.utils.MatchUtils;
import ui1.raullozano.bigfootball.bigfootball.chart.types.ColumnChart;
import ui1.raullozano.bigfootball.bigfootball.chart.utils.ChartHelper;
import ui1.raullozano.bigfootball.bigfootball.chart.utils.ChartInfo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TeamGoalsAgainstChart {

    private final Team team;

    public TeamGoalsAgainstChart(FileAccessor fileAccessor, Map<String, String> params) {
        this.team = fileAccessor.getTeam(params.get("competition"), params.get("season"), params.get("team"));
    }

    public String getChart() {

        if(team == null) return "<html></html>";

        ChartInfo chartInfo = new ColumnChart("Frecuencia de goles recibidos según el minuto", "minuto", "goles recibidos", 0)
                .addData("Goles recibidos", getDataOf(team), "#F50000")
                .getInfo();

        return "<html>" +
                    "<header>" +
                        "<script src='https://code.highcharts.com/highcharts.js'></script>" +
                    "</header>" +
                    "<body>" +
                        "<div id='team-goals-against' style='height:100%; width: 100%;'>" + ChartHelper.getGraph(getClass(), "team-goals-against", chartInfo) + "</div>" +
                    "</body>" +
                "</html>";
    }

    private LinkedHashMap<String, Double[]> getDataOf(Team team) {

        Map<String, Integer> goalsAgainst = team.goalsAgainst();

        Map<String, Integer> aggregatedGoals = new LinkedHashMap<>();
        aggregatedGoals.put("0-15", (int) minutesOf(goalsAgainst.keySet()).stream().filter(minutesBetween("0", "15")).count());
        aggregatedGoals.put("16-30", (int) minutesOf(goalsAgainst.keySet()).stream().filter(minutesBetween("16", "30")).count());
        aggregatedGoals.put("31-45", (int) minutesOf(goalsAgainst.keySet()).stream().filter(minutesBetween("31", "45")).count());
        aggregatedGoals.put("Descuento 1a", (int) minutesOf(goalsAgainst.keySet()).stream().filter(minutesBetween("45", "+")).count());
        aggregatedGoals.put("46-60", (int) minutesOf(goalsAgainst.keySet()).stream().filter(minutesBetween("46", "60")).count());
        aggregatedGoals.put("61-75", (int) minutesOf(goalsAgainst.keySet()).stream().filter(minutesBetween("61", "75")).count());
        aggregatedGoals.put("76-90", (int) minutesOf(goalsAgainst.keySet()).stream().filter(minutesBetween("76", "90")).count());
        aggregatedGoals.put("Descuento 2a", (int) minutesOf(goalsAgainst.keySet()).stream().filter(minutesBetween("90", "+")).count());

        LinkedHashMap<String, Double[]> data = new LinkedHashMap<>();

        for (String range : aggregatedGoals.keySet()) {
            data.put(range, new Double[]{Double.valueOf(aggregatedGoals.get(range))});
        }

        return data;
    }

    private Predicate<String> minutesBetween(String minuteFrom, String minuteTo) {
        int minuteFromInt = MatchUtils.minuteOf(minuteFrom);
        if(minuteTo.equals("+")) {
            return minute -> {
                if (minute.contains("+")) {
                    int minuteInt = Integer.parseInt(minute.split("\\+")[0]);
                    return minuteInt == minuteFromInt;
                } else {
                    return false;
                }
            };
        } else {
            int minuteToInt = MatchUtils.minuteOf(minuteTo);
            return minute -> {
                if (minute.contains("+")) {
                    return false;
                } else {
                    int minuteInt = MatchUtils.minuteOf(minute);
                    return minuteInt >= minuteFromInt && minuteInt <= minuteToInt;
                }
            };
        }
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
                    return 1;
                }
            } else {
                return -1;
            }
        } else {
            return m1Int.compareTo(m2Int);
        }
    }
}

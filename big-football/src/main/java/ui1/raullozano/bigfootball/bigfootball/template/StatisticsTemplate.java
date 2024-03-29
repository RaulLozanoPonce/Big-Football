package ui1.raullozano.bigfootball.bigfootball.template;

import java.util.List;

public class StatisticsTemplate extends WebTemplate {

    public StatisticsTemplate(String urlBase) {
        super(urlBase);
    }

    @Override
    protected String getBaseHtmlName() {
        return "statistics";
    }

    @Override
    protected List<String> importedScripts() {
        return List.of(
                "https://code.highcharts.com/highcharts.js"
        );
    }
}

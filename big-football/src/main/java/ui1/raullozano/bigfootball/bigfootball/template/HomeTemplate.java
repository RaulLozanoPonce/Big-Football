package ui1.raullozano.bigfootball.bigfootball.template;

import java.util.List;

public class HomeTemplate extends WebTemplate {

    @Override
    protected String getBaseHtmlName() {
        return "home";
    }

    @Override
    protected List<String> importedScripts() {
        return List.of(
                "https://code.highcharts.com/highcharts.js"
        );
    }

    @Override
    protected String preRenderScript() {
        return "console.log('PRE RENDER ---------------------------')";
    }
}

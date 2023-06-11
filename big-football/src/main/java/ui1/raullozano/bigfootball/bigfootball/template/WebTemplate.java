package ui1.raullozano.bigfootball.bigfootball.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;

public abstract class WebTemplate {

    public String getHtml() {

        String baseHtml = getResource("/public/" + getBaseHtmlName() + ".html");
        if(baseHtml != null) {

            StringBuilder sb = new StringBuilder();
            importedScripts().forEach(url -> sb.append("<script src='").append(url).append("'></script>"));
            baseHtml = baseHtml.replaceAll("::import-script::", sb.toString());

            String preRenderScript = preRenderScript();
            if(preRenderScript == null) preRenderScript = "";
            baseHtml = baseHtml.replaceAll("::prerender-script::", preRenderScript);

            baseHtml = baseHtml.replaceAll("::url-base::", "http://localhost:9000");

            return baseHtml;
        }

        return "<html></html>";
    }

    private String getResource(String path) {
        try {
            return new String(getClass().getResource(path).openStream().readAllBytes());
        } catch (Throwable t) {
            t.fillInStackTrace();
            return null;
        }
    }

    protected abstract String getBaseHtmlName();

    protected List<String> importedScripts() {
        return new ArrayList<>();
    }

    protected String preRenderScript() {
        return null;
    }
}

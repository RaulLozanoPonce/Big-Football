package ui1.raullozano.bigfootball.bigfootball.template;

public class TeamTemplate extends WebTemplate {

    public TeamTemplate(String urlBase) {
        super(urlBase);
    }

    @Override
    protected String getBaseHtmlName() {
        return "team";
    }
}

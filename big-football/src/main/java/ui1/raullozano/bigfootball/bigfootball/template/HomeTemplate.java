package ui1.raullozano.bigfootball.bigfootball.template;

public class HomeTemplate extends WebTemplate {

    public HomeTemplate(String urlBase) {
        super(urlBase);
    }

    @Override
    protected String getBaseHtmlName() {
        return "home";
    }
}

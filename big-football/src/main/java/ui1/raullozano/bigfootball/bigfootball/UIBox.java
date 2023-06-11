package ui1.raullozano.bigfootball.bigfootball;

import spark.Spark;
import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.bigfootball.api.LineupApi;
import ui1.raullozano.bigfootball.bigfootball.api.TeamApi;
import ui1.raullozano.bigfootball.bigfootball.template.HomeTemplate;
import ui1.raullozano.bigfootball.bigfootball.template.LineupsTemplate;
import ui1.raullozano.bigfootball.bigfootball.template.StatisticsTemplate;
import ui1.raullozano.bigfootball.bigfootball.template.chart.*;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.port;

public class UIBox {

    private final FileAccessor fileAccessor;

    public UIBox( FileAccessor fileAccessor) {
        this.fileAccessor = fileAccessor;
    }

    public void start() {
        initPort();
        initStaticFiles();
        initApis();
        initUI();
        initCharts();
    }

    private void initPort() {
        port(fileAccessor.port());
    }

    private void initStaticFiles() {
        Spark.staticFiles.location("/public");
    }

    private void initApis() {
        get("/team/:season/:competition/:team", (req, res) -> {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("season", req.params("season"));
            queryParams.put("competition", req.params("competition"));
            queryParams.put("team", req.params("team"));
            return new TeamApi(fileAccessor, queryParams).getResponse();
        });

        get("/lineup/:season/:competition/:team/:lineup", (req, res) -> {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("season", req.params("season"));
            queryParams.put("competition", req.params("competition"));
            queryParams.put("team", req.params("team"));
            queryParams.put("lineup", req.params("lineup"));
            return new LineupApi(fileAccessor, queryParams).getResponse();
        });
    }

    private void initCharts() {
        get("/team/:season/:competition/:team/team-clutch", (req, res) -> {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("season", req.params("season"));
            queryParams.put("competition", req.params("competition"));
            queryParams.put("team", req.params("team"));
            return new TeamClutchChart(fileAccessor, queryParams).getChart();
        });

        get("/team/:season/:competition/:team/player-clutch", (req, res) -> {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("season", req.params("season"));
            queryParams.put("competition", req.params("competition"));
            queryParams.put("team", req.params("team"));
            return new PlayerClutchChart(fileAccessor, queryParams).getChart();
        });

        get("/team/:season/:competition/:team/team-goals-for", (req, res) -> {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("season", req.params("season"));
            queryParams.put("competition", req.params("competition"));
            queryParams.put("team", req.params("team"));
            return new TeamGoalsForChart(fileAccessor, queryParams).getChart();
        });

        get("/team/:season/:competition/:team/team-goals-against", (req, res) -> {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("season", req.params("season"));
            queryParams.put("competition", req.params("competition"));
            queryParams.put("team", req.params("team"));
            return new TeamGoalsAgainstChart(fileAccessor, queryParams).getChart();
        });

        get("/team/:season/:competition/:team/player-substituted-won-points", (req, res) -> {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("season", req.params("season"));
            queryParams.put("competition", req.params("competition"));
            queryParams.put("team", req.params("team"));
            return new PlayerSubstitutedWonPointsChart(fileAccessor, queryParams).getChart();
        });

        get("/team/:season/:competition/:team/player-substituted-lost-points", (req, res) -> {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("season", req.params("season"));
            queryParams.put("competition", req.params("competition"));
            queryParams.put("team", req.params("team"));
            return new PlayerSubstitutedLostPointsChart(fileAccessor, queryParams).getChart();
        });

        get("/team/:season/:competition/:team/player-substitute-won-points", (req, res) -> {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("season", req.params("season"));
            queryParams.put("competition", req.params("competition"));
            queryParams.put("team", req.params("team"));
            return new PlayerSubstituteWonPointsChart(fileAccessor, queryParams).getChart();
        });

        get("/team/:season/:competition/:team/player-substitute-lost-points", (req, res) -> {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("season", req.params("season"));
            queryParams.put("competition", req.params("competition"));
            queryParams.put("team", req.params("team"));
            return new PlayerSubstituteLostPointsChart(fileAccessor, queryParams).getChart();
        });

        get("/team/:season/:competition/:team/player-substitution-contributed-points", (req, res) -> {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("season", req.params("season"));
            queryParams.put("competition", req.params("competition"));
            queryParams.put("team", req.params("team"));
            return new PlayerSubstitutionContributedPointsChart(fileAccessor, queryParams).getChart();
        });
    }

    private void initUI() {
        get("/home", (req, res) -> new HomeTemplate().getHtml());
        get("/statistics", (req, res) -> new StatisticsTemplate().getHtml());
        get("/lineups", (req, res) -> new LineupsTemplate().getHtml());
    }
}

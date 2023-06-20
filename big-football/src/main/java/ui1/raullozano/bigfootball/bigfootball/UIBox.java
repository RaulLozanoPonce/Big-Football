package ui1.raullozano.bigfootball.bigfootball;

import spark.Spark;
import ui1.raullozano.bigfootball.bigfootball.api.*;
import ui1.raullozano.bigfootball.bigfootball.template.*;
import ui1.raullozano.bigfootball.bigfootball.template.chart.*;
import ui1.raullozano.bigfootball.common.files.FileAccessor;

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
        get("/api/competitions", (req, res) -> {
            return new CompetitionsApi(fileAccessor).getResponse();
        });

        get("/api/seasons/:competition", (req, res) -> {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("competition", req.params("competition"));
            return new SeasonsApi(fileAccessor, queryParams).getResponse();
        });

        get("/api/teams/:season/:competition", (req, res) -> {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("season", req.params("season"));
            queryParams.put("competition", req.params("competition"));
            return new TeamsApi(fileAccessor, queryParams).getResponse();
        });

        get("/api/team-base/:season/:competition/:team", (req, res) -> {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("season", req.params("season"));
            queryParams.put("competition", req.params("competition"));
            queryParams.put("team", req.params("team"));
            return new TeamBaseApi(fileAccessor, queryParams).getResponse();
        });

        get("/api/statistics/:season/:competition/:team", (req, res) -> {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("season", req.params("season"));
            queryParams.put("competition", req.params("competition"));
            queryParams.put("team", req.params("team"));
            return new StatisticsApi(fileAccessor, queryParams).getResponse();
        });

        get("/api/lineups/:season/:competition/:team/:lineup", (req, res) -> {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("season", req.params("season"));
            queryParams.put("competition", req.params("competition"));
            queryParams.put("team", req.params("team"));
            queryParams.put("lineup", req.params("lineup"));
            return new LineupsApi(fileAccessor, queryParams).getResponse();
        });

        get("/api/best-lineup/:season/:competition/:this-team/:other-team/:lineup", (req, res) -> {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("season", req.params("season"));
            queryParams.put("competition", req.params("competition"));
            queryParams.put("this-team", req.params("this-team"));
            queryParams.put("other-team", req.params("other-team"));
            queryParams.put("lineup", req.params("lineup"));
            return new BestLineupApi(fileAccessor, queryParams).getResponse();
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
        get("/", (req, res) -> new HomeTemplate(fileAccessor.urlBase()).getHtml());
        get("/home", (req, res) -> new HomeTemplate(fileAccessor.urlBase()).getHtml());
        get("/competition", (req, res) -> new CompetitionTemplate(fileAccessor.urlBase()).getHtml());
        get("/season", (req, res) -> new SeasonTemplate(fileAccessor.urlBase()).getHtml());
        get("/team", (req, res) -> new TeamTemplate(fileAccessor.urlBase()).getHtml());
        get("/statistics", (req, res) -> new StatisticsTemplate(fileAccessor.urlBase()).getHtml());
        get("/lineups", (req, res) -> new LineupsTemplate(fileAccessor.urlBase()).getHtml());
        get("/best-lineup-prediction", (req, res) -> new BestLineupPredictionTemplate(fileAccessor.urlBase()).getHtml());
    }
}

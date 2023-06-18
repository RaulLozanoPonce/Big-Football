package ui1.raullozano.bigfootball.bigfootball.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.model.transformator.Player;
import ui1.raullozano.bigfootball.common.model.transformator.Position;
import ui1.raullozano.bigfootball.common.model.transformator.Team;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BestLineupApi {

    private final FileAccessor fileAccessor;
    private final Team thisTeam;
    private final Team otherTeam;
    private final String competition;
    private final String season;
    private final String lineup;

    public BestLineupApi(FileAccessor fileAccessor, Map<String, String> queryParams) {
        this.fileAccessor = fileAccessor;
        this.thisTeam = fileAccessor.getTeam(queryParams.get("competition"), queryParams.get("season"), queryParams.get("this-team"));
        this.otherTeam = fileAccessor.getTeam(queryParams.get("competition"), queryParams.get("season"), queryParams.get("other-team"));
        this.competition = queryParams.get("competition");
        this.season = queryParams.get("season");
        this.lineup = queryParams.get("lineup");
    }

    public String getResponse() {
        String line = fileAccessor.getBestPlayerCombination(competition, season, thisTeam.name(), otherTeam.name(), lineup);
        if(line == null) return null;
        LineupsTempleteResponse response = new LineupsTempleteResponse()
                .bestLineup(bestLineupOf(line.split(";")[4]))
                .goalDifference(Integer.valueOf(line.split(";")[3]));
        return new Gson().toJson(response);
    }

    private LineupsTempleteResponse.LineupComponent bestLineupOf(String field) {

        List<String> playersIds = Arrays.asList(field.replaceAll("\\[|]|\"", "").replaceAll("\\\\u003d", "=").split(","));

        return new LineupsTempleteResponse.LineupComponent(thisTeam.players().stream()
                .filter(p -> playersIds.contains(p.id()))
                .sorted(Comparator.comparingInt(p -> playersIds.indexOf(p.id())))
                .collect(Collectors.toList()));
    }

    private static class LineupsTempleteResponse {

        private List<PlayerSquad> bestLineup;
        private Integer goalDifference;

        public LineupsTempleteResponse bestLineup(LineupComponent lineupComponent) {
            this.bestLineup = lineupComponent.players.stream().map(PlayerSquad::new).collect(Collectors.toList());
            return this;
        }

        public LineupsTempleteResponse goalDifference(Integer goalDifference) {
            this.goalDifference = goalDifference;
            return this;
        }

        private static class LineupComponent {

            private final List<Player> players;

            public LineupComponent(List<Player> players) {
                this.players = players;
            }
        }

        public static class PlayerSquad {

            private final String id;
            private final String name;
            private final Position position;
            private final int age;
            private final int playedMatches;
            private final int minutes;
            private final int goals;
            private final int assists;

            public PlayerSquad(Player player) {
                this.id = player.id();
                this.name = player.name();
                this.position = player.finalPosition();
                this.age = player.age();
                this.playedMatches = player.matches().playedMatches();
                this.minutes = player.matches().minutes();
                this.goals = player.matches().goals();
                this.assists = player.matches().assists();
            }
        }
    }
}

package ui1.raullozano.bigfootball.bigfootball.api;

import com.google.gson.Gson;
import ui1.raullozano.bigfootball.common.files.FileAccessor;
import ui1.raullozano.bigfootball.common.model.transformator.Player;
import ui1.raullozano.bigfootball.common.model.transformator.Position;
import ui1.raullozano.bigfootball.common.model.transformator.Team;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TeamBaseApi {

    private final Team team;

    public TeamBaseApi(FileAccessor fileAccessor, Map<String, String> params) {
        this.team = fileAccessor.getTeam(params.get("competition"), params.get("season"), params.get("team"));
    }

    public String getResponse() {
        if(team == null) return "{}";
        TeamBaseResponse response = new TeamBaseResponse()
                .teamName(team.name())
                .playedMatches(team.playedMatches())
                .won(team.win())
                .draw(team.draw())
                .lost(team.lost())
                .squad(team.players());
        return new Gson().toJson(response);
    }

    private static class TeamBaseResponse {

        private String teamName;
        private int playedMatches;
        private int won;
        private int draw;
        private int lost;
        private List<PlayerSquad> squad;

        public TeamBaseResponse teamName(String teamName) {
            this.teamName = teamName;
            return this;
        }

        public TeamBaseResponse playedMatches(int playedMatches) {
            this.playedMatches = playedMatches;
            return this;
        }

        public TeamBaseResponse won(int won) {
            this.won = won;
            return this;
        }

        public TeamBaseResponse draw(int draw) {
            this.draw = draw;
            return this;
        }
        public TeamBaseResponse lost(int lost) {
            this.lost = lost;
            return this;
        }

        public TeamBaseResponse squad(List<Player> players) {
            this.squad = players.stream().sorted(Comparator.comparing(Player::name)).map(PlayerSquad::new).collect(Collectors.toList());
            return this;
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
            private final int starters;

            public PlayerSquad(Player player) {
                this.id = player.id();
                this.name = player.name();
                this.position = player.finalPosition();
                this.age = player.age();
                this.playedMatches = player.matches().playedMatches();
                this.minutes = player.matches().minutes();
                this.goals = player.matches().goals();
                this.assists = player.matches().assists();
                this.starters = player.matches().starters();
            }
        }
    }
}

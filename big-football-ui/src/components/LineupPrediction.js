import React, { Component } from 'react';

import Navigation from './subcomponents/Navigation';
import TeamHeader from './subcomponents/TeamHeader';
import LineupTeamSelector from './subcomponents/LineupTeamSelector';
import Table from './subcomponents/Table';

import { AjaxGet } from './../utils/requests.js';

import './styles/LineupPrediction.css'

class LineupPrediction extends Component {

    constructor() {
        super();
        var url = new URL(window.location.href);
        this.season = url.searchParams.get("season");
        this.competition = url.searchParams.get("competition");
        this.team = url.searchParams.get("team");

        this.bestLineup = [];
        this.otherLineup = [];

        this.loadOtherTeams = this.loadOtherTeams.bind(this);
        this.loadBestLineup = this.loadBestLineup.bind(this);
    }

    render() {
        return (
            <div className="LineupPrediction">
                <Navigation title={ "Big-Football > " + this.competition + " > " + this.season + " > " + this.team }/>
                <TeamHeader />
                <LineupTeamSelector loadFunction={this.loadBestLineup} selectLineupId="lineup-selector" selectTeamId="other-team-selector" />
                <p id="goal-difference"></p>
                <div id="lineup-prediction-content">
                    <div className="lineup-prediction-content-column">
                        <div className="lineup-prediction-content-row">
                            <Table title={ "Mejor alineación" } content={ this.bestLineup } />
                        </div>
                    </div>
                    <div className="lineup-prediction-content-column">
                        <div className="lineup-prediction-content-row">
                            <Table title={ "Alineación más probable del equipo rival" } content={ this.otherLineup } />
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    componentDidMount() {
        document.getElementById("lineup-selector").value = "4-3-3";

        var self = this;
        AjaxGet(urlBase + "/api/team-base/" + this.season + "/" + this.competition + "/" + this.team, {}, function(content) {

            var team = JSON.parse(content);

            document.getElementById("played-matches").innerHTML = team.playedMatches;
            document.getElementById("won-matches").innerHTML = team.won;
            document.getElementById("draw-matches").innerHTML = team.draw;
            document.getElementById("lost-matches").innerHTML = team.lost;
            document.getElementById("participating-players").innerHTML = team.squad.length;

            self.forceUpdate();
        });

        this.loadOtherTeams();
    }

    loadOtherTeams() {
        var self = this;
        AjaxGet(urlBase + "/api/teams/" + this.season + "/" + this.competition, {}, function(content) {

            document.getElementById("other-team-selector").innerHTML = "";

            var teams = JSON.parse(content);
            var firstIndex = null;
            for(var index in teams) {
                if(teams[index] != self.team) {
                    if(firstIndex == null) {
                        firstIndex = index;
                    }
                    var opt = document.createElement('option');
                    opt.value = teams[index];
                    opt.innerHTML = teams[index];
                    document.getElementById("other-team-selector").appendChild(opt);
                }
            }

            document.getElementById("other-team-selector").value = teams[firstIndex];
            self.loadBestLineup(self);
        });
    }

    loadBestLineup() {

        var self = this;
        AjaxGet(urlBase + "/api/best-lineup/" + this.season + "/" + this.competition + "/" + this.team + "/" + document.getElementById("other-team-selector").value + "/" + document.getElementById("lineup-selector").value, {}, function(content) {
            var lineup = JSON.parse(content);
            self.bestLineup = self.getSquadTable(lineup.bestLineup);
            document.getElementById("goal-difference").value =
                "La diferencia de goles estimado si te enfretas contra " + document.getElementById("other-team-selector").value +
                " es de " + lineup.goalDifference + " goles";
            self.forceUpdate();
        });

        AjaxGet(urlBase + "/api/statistics/" + this.season + "/" + this.competition + "/" + document.getElementById("other-team-selector").value, {}, function(content) {
            var statistics = JSON.parse(content);
            self.otherLineup = self.getSquadTable(statistics.startingLineup);
            self.forceUpdate();
        });
    }

    getSquadTable(players) {
        var collection = [["Nombre", "Posición", "Edad", "Partidos Jugados", "Minutos", "Goles", "Asistencias"]];
        for(var index in players) {
            var player = players[index];
            collection.push([player.name, player.position, player.age, player.playedMatches, player.minutes, player.goals, player.assists]);
        }
        return collection;
    }
}

export default LineupPrediction;

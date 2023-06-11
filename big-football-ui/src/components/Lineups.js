import React, { Component } from 'react';

import Navigation from './Navigation';
import TeamHeader from './subcomponents/TeamHeader';
import LineupSelector from './subcomponents/LineupSelector';
import Table from './subcomponents/Table';

import { AjaxGet } from './../utils/requests.js';

import './styles/Lineups.css'

class Statistics extends Component {

    constructor() {
        super();
        var url = new URL(window.location.href);
        this.season = url.searchParams.get("season");
        this.competition = url.searchParams.get("competition");
        this.team = url.searchParams.get("team");

        this.bestDefensiveLineup = [];
        this.bestPassingLineup = [];
        this.bestAttackingLineup = [];
        this.bestFoulsLineup = [];

        this.lineup = null;

        this.loadLineups = this.loadLineups.bind(this);
    }

    render() {
        return (
            <div className="Lineups">
                <Navigation title="FBig Data"/>
                <TeamHeader />
                <LineupSelector loadFunction={this.loadLineups} selectId="lineup-selector" />
                <div id="lineups-content">
                    <div className="lineups-content-column">
                        <div className="lineups-content-row">
                            <Table title={ "Mejor alineación defensiva" } content={ this.bestDefensiveLineup } />
                        </div>
                        <div className="lineups-content-row">
                            <Table title={ "Mejor alineación atacante" } content={ this.bestAttackingLineup } />
                        </div>
                    </div>
                    <div className="lineups-content-column">
                        <div className="lineups-content-row">
                            <Table title={ "Mejor alineación en los pases" } content={ this.bestPassingLineup } />
                        </div>
                        <div className="lineups-content-row">
                            <Table title={ "Mejor alineación en las faltas" } content={ this.bestFoulsLineup } />
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    loadLineups(e) {

        var self = this;
        AjaxGet(urlBase + "/lineup/" + this.season + "/" + this.competition + "/" + this.team + "/" + document.getElementById("lineup-selector").value, {}, function(content) {

            var team = JSON.parse(content);

            document.getElementById("team-name").innerHTML = team.teamName;
            document.getElementById("played-matches").innerHTML = team.playedMatches;
            document.getElementById("won-matches").innerHTML = team.won;
            document.getElementById("draw-matches").innerHTML = team.draw;
            document.getElementById("lost-matches").innerHTML = team.lost;
            document.getElementById("participating-players").innerHTML = team.participatingPlayers;

            self.bestDefensiveLineup = self.getSquadTable(team.bestDefensiveLineup);
            self.bestPassingLineup = self.getSquadTable(team.bestPassingLineup);
            self.bestAttackingLineup = self.getSquadTable(team.bestAttackingLineup);
            self.bestFoulsLineup = self.getSquadTable(team.bestFoulsLineup);

            self.forceUpdate();
        });
    }

    componentDidMount() {
        document.getElementById("lineup-selector").value = "4-3-3";
        this.loadLineups();
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

export default Statistics;

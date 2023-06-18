import React, { Component } from 'react';

import Navigation from './subcomponents/Navigation';
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

        this.loadLineups = this.loadLineups.bind(this);
    }

    render() {
        return (
            <div className="Lineups">
                <Navigation title={ "Big-Football > " + this.competition + " > " + this.season + " > " + this.team }/>
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

    componentDidMount() {
        document.getElementById("lineup-selector").value = "4-3-3";

        AjaxGet(urlBase + "/api/team-base/" + this.season + "/" + this.competition + "/" + this.team, {}, function(content) {

            var team = JSON.parse(content);

            document.getElementById("played-matches").innerHTML = team.playedMatches;
            document.getElementById("won-matches").innerHTML = team.won;
            document.getElementById("draw-matches").innerHTML = team.draw;
            document.getElementById("lost-matches").innerHTML = team.lost;
            document.getElementById("participating-players").innerHTML = team.squad.length;

            self.forceUpdate();
        });

        this.loadLineups();
    }

    loadLineups(e) {

        var self = this;

        AjaxGet(urlBase + "/api/lineups/" + this.season + "/" + this.competition + "/" + this.team + "/" + document.getElementById("lineup-selector").value, {}, function(content) {

            var lineups = JSON.parse(content);

            self.bestDefensiveLineup = self.getSquadTable(lineups.bestDefensiveLineup);
            self.bestPassingLineup = self.getSquadTable(lineups.bestPassingLineup);
            self.bestAttackingLineup = self.getSquadTable(lineups.bestAttackingLineup);
            self.bestFoulsLineup = self.getSquadTable(lineups.bestFoulsLineup);

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

export default Statistics;

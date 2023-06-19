import React, { Component } from 'react';

import Navigation from './subcomponents/Navigation';
import TeamHeader from './subcomponents/TeamHeader';
import Table from './subcomponents/Table';

import { AjaxGet } from './../utils/requests.js';

import './styles/Statistics.css'

class Statistics extends Component {

    constructor() {
        super();
        var url = new URL(window.location.href);
        this.season = url.searchParams.get("season");
        this.competition = url.searchParams.get("competition");
        this.team = url.searchParams.get("team");

        this.squad = [];
        this.regularLineup = [];
        this.regularLineupMinutes = 0;
        this.starterLineup = [];
        this.moreWonMinutesLineup = [];
        this.moreWonMinutesLineupMinutes = 0;
        this.moreGoalsForByMinuteLineup = [];
        this.moreGoalsForByMinuteLineupMinutes = 0;
        this.moreGoalsForByMinuteLineupGoals = 0;
        this.lessGoalsAgainstByMinuteLineup = [];
        this.lessGoalsAgainstByMinuteLineupMinutes = 0;
        this.lessGoalsAgainstByMinuteLineupGoals = 0;
        this.bestLineup = [];
        this.bestLineupScore = 0;
    }

    render() {
        return (
            <div className="Statistics">
                <Navigation title={ "Big-Football > " + this.competition + " > " + this.season + " > " + this.team }
                    prevUrl={"team?competition=" + this.competition + "&season=" + this.season + "&team=" + this.team}/>
                <TeamHeader />
                <div id="statistics-content">
                    <div className="statistics-content-column" style={{ width:'34%' }}>
                        <div className="statistics-content-row">
                            <iframe className="statistics-content-row-iframe" src={ urlBase + "/team/" + this.season + "/" + this.competition + "/" + this.team + "/team-clutch" }></iframe>
                        </div>
                        <div className="statistics-content-row">
                            <iframe className="statistics-content-row-iframe" src={ urlBase + "/team/" + this.season + "/" + this.competition + "/" + this.team + "/player-clutch" }></iframe>
                        </div>
                        <div className="statistics-content-row">
                            <iframe className="statistics-content-row-iframe" src={ urlBase + "/team/" + this.season + "/" + this.competition + "/" + this.team + "/team-goals-for" }></iframe>
                            <iframe className="statistics-content-row-iframe" src={ urlBase + "/team/" + this.season + "/" + this.competition + "/" + this.team + "/team-goals-against" }></iframe>
                        </div>
                    </div>
                    <div className="statistics-content-column">
                        <div className="statistics-content-row">
                            <iframe className="statistics-content-row-iframe" src={ urlBase + "/team/" + this.season + "/" + this.competition + "/" + this.team + "/player-substituted-won-points" }></iframe>
                            <iframe className="statistics-content-row-iframe" src={ urlBase + "/team/" + this.season + "/" + this.competition + "/" + this.team + "/player-substituted-lost-points" }></iframe>
                            <iframe className="statistics-content-row-iframe" src={ urlBase + "/team/" + this.season + "/" + this.competition + "/" + this.team + "/player-substitute-won-points" }></iframe>
                            <iframe className="statistics-content-row-iframe" src={ urlBase + "/team/" + this.season + "/" + this.competition + "/" + this.team + "/player-substitute-lost-points" }></iframe>
                            <iframe className="statistics-content-row-iframe" src={ urlBase + "/team/" + this.season + "/" + this.competition + "/" + this.team + "/player-substitution-contributed-points" }></iframe>
                        </div>
                    </div>
                    <div className="statistics-content-column">
                        <div className="statistics-content-row">
                            <Table id="squad" title={ "Plantilla" } content={ this.squad } />
                        </div>
                        <div className="statistics-content-row">
                            <Table id="regular-lineup" title={ "Alineación con más frecuencia (" + this.regularLineupMinutes + " minutos)" } content={ this.regularLineup } />
                            <Table id="regular-lineup" title={ "Alineación con más titularidades" } content={ this.starterLineup } />
                        </div>
                        <div className="statistics-content-row">
                            <Table title={ "Alineación con más minutos ganando (" + this.moreWonMinutesLineupMinutes + " minutos)" } content={ this.moreWonMinutesLineup } />
                            <Table title={ "Alineación con más goles marcados por minuto (" + this.moreGoalsForByMinuteLineupGoals + " g/m / " + this.moreGoalsForByMinuteLineupMinutes + " minutos)" } content={ this.moreGoalsForByMinuteLineup } />
                            <Table title={ "Alineación con menos goles recibidos por minuto (" + this.lessGoalsAgainstByMinuteLineupGoals + " g/m / " + this.lessGoalsAgainstByMinuteLineupMinutes + " minutos)" } content={ this.lessGoalsAgainstByMinuteLineup } />
                            <Table title={ "Alineación con mejor puntuación (" + this.bestLineupScore + "/100 puntos)" } content={ this.bestLineup } />
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    componentDidMount() {
        var self = this;

        AjaxGet(urlBase + "/api/team-base/" + this.season + "/" + this.competition + "/" + this.team, {}, function(content) {

            var team = JSON.parse(content);

            document.getElementById("played-matches").innerHTML = team.playedMatches;
            document.getElementById("won-matches").innerHTML = team.won;
            document.getElementById("draw-matches").innerHTML = team.draw;
            document.getElementById("lost-matches").innerHTML = team.lost;
            document.getElementById("participating-players").innerHTML = team.squad.length;

            self.squad = self.getSquadTable(team.squad);

            self.forceUpdate();
        });

        AjaxGet(urlBase + "/api/statistics/" + this.season + "/" + this.competition + "/" + this.team, {}, function(content) {
            var statistics = JSON.parse(content);

            self.regularLineup = self.getSquadTable(statistics.regularLineup);
            self.regularLineupMinutes = statistics.regularLineupMinutes;
            self.starterLineup = self.getStarterSquadTable(statistics.startingLineup);
            self.moreWonMinutesLineup = self.getSquadTable(statistics.moreWonMinutesLineup);
            self.moreWonMinutesLineupMinutes = statistics.moreWonMinutesLineupMinutes;
            self.moreGoalsForByMinuteLineup = self.getSquadTable(statistics.moreGoalsForByMinuteLineup);
            self.moreGoalsForByMinuteLineupMinutes = statistics.moreGoalsForByMinuteLineupMinutes;
            self.moreGoalsForByMinuteLineupGoals = statistics.moreGoalsForByMinuteLineupGoals;
            self.lessGoalsAgainstByMinuteLineup = self.getSquadTable(statistics.lessGoalsAgainstByMinuteLineup);
            self.lessGoalsAgainstByMinuteLineupMinutes = statistics.lessGoalsAgainstByMinuteLineupMinutes;
            self.lessGoalsAgainstByMinuteLineupGoals = statistics.lessGoalsAgainstByMinuteLineupGoals;
            self.bestLineup = self.getSquadTable(statistics.bestLineup);
            self.bestLineupScore = statistics.bestLineupScore;

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

    getStarterSquadTable(players) {
        var collection = [["Nombre", "Posición", "Edad", "Partidos Jugados", "Minutos", "Goles", "Asistencias", "Titularidades"]];
        for(var index in players) {
            var player = players[index];
            collection.push([player.name, player.position, player.age, player.playedMatches, player.minutes, player.goals, player.assists, player.starters]);
        }
        return collection;
    }
}

export default Statistics;

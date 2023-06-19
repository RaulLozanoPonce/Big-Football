import React, { Component } from 'react';
import Navigation from './subcomponents/Navigation';
import Option from './subcomponents/Option';
import './styles/Team.css';
import { AjaxGet,redirect } from './../utils/requests.js';

class Team extends Component {

    constructor() {
        super();
        var url = new URL(window.location.href);
        this.competition = url.searchParams.get("competition");
        this.season = url.searchParams.get("season");
        this.team = url.searchParams.get("team");
    }

    render() {
        return (
            <div className="Team">
                <Navigation title={ "Big-Football > " + this.competition + " > " + this.season + " > " + this.team }
                    prevUrl={"season?competition=" + this.competition + "&season=" + this.season}/>
                <div id="teamOptions">
                    <Option title="Estadísticas del equipo" actionFunction={ () => this.loadStatistics() } />
                    <Option title="Mejores alineaciones" actionFunction={ () => this.loadLineups() } />
                    <Option title="Alineación prepartido" actionFunction={ () => this.loadLineupPrediction() } />
                </div>
            </div>
        );
    }

    loadStatistics() {
        redirect("statistics?competition=" + this.competition + "&season=" + this.season + "&team=" + this.team);
    }

    loadLineups() {
        redirect("lineups?competition=" + this.competition + "&season=" + this.season + "&team=" + this.team);
    }

    loadLineupPrediction() {
        redirect("best-lineup-prediction?competition=" + this.competition + "&season=" + this.season + "&team=" + this.team);
    }
}

export default Team;

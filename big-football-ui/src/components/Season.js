import React, { Component } from 'react';
import Navigation from './subcomponents/Navigation';
import Option from './subcomponents/Option';
import './styles/Season.css';
import { AjaxGet,redirect } from './../utils/requests.js';

class Season extends Component {

    constructor() {
        super();
        var url = new URL(window.location.href);
        this.competition = url.searchParams.get("competition");
        this.season = url.searchParams.get("season");
        this.teams = [];
    }

    render() {

        var teamsOptions = [];

        for(var index in this.teams) {
            const team = this.teams[index];
            teamsOptions.push(<Option title={ team } actionFunction={ () => this.loadTeam(team) } />);
        }

        return (
            <div className="Season">
                <Navigation title={ "Big-Football > " + this.competition + " > " + this.season }/>
                <div id="teams">
                    { teamsOptions }
                </div>
            </div>
        );
    }

    componentDidMount() {
        var self = this;
        AjaxGet(urlBase + "/api/teams/" + this.season + "/" + this.competition , {}, function(content) {
            var teams = JSON.parse(content);
            self.teams = teams;
            self.forceUpdate();
        });
    }

    loadTeam(team) {
        redirect("team?competition=" + this.competition + "&season=" + this.season + "&team=" + team);
    }
}

export default Season;

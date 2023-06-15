import React, { Component } from 'react';
import Navigation from './subcomponents/Navigation';
import Option from './subcomponents/Option';
import './styles/Competition.css';
import { AjaxGet,redirect } from './../utils/requests.js';

class Competition extends Component {

    constructor() {
        super();
        var url = new URL(window.location.href);
        this.competition = url.searchParams.get("competition");
        this.seasons = [];
    }

    render() {

        var seasonOptions = [];

        for(var index in this.seasons) {
            const season = this.seasons[index];
            seasonOptions.push(<Option title={ season } actionFunction={ () => this.loadSeason(season) } />);
        }

        return (
            <div className="Competition">
                <Navigation title={ "Big-Football > " + this.competition }/>
                <div id="seasons">
                    { seasonOptions }
                </div>
            </div>
        );
    }

    componentDidMount() {
        var self = this;
        AjaxGet(urlBase + "/api/seasons/" + this.competition , {}, function(content) {
            var seasons = JSON.parse(content);
            self.seasons = seasons;
            self.forceUpdate();
        });
    }

    loadSeason(season) {
        redirect("season?competition=" + this.competition + "&season=" + season);
    }
}

export default Competition;

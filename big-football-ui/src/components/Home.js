import React, { Component } from 'react';
import Navigation from './subcomponents/Navigation';
import Option from './subcomponents/Option';
import './styles/Home.css';
import { AjaxGet,redirect } from './../utils/requests.js';

class Home extends Component {

    constructor() {
        super();
        this.competitions = [];
    }

    render() {

        var competitionOptions = [];

        for(var index in this.competitions) {
            const competition = this.competitions[index];
            competitionOptions.push(<Option title={ competition } actionFunction={ () => this.loadCompetition(competition) } />);
        }

        return (
            <div className="Home">
                <Navigation title="Big-Football"/>
                <div id="competitions">
                    { competitionOptions }
                </div>
            </div>
        );
    }

    componentDidMount() {
        var self = this;
        AjaxGet(urlBase + "/api/competitions", {}, function(content) {
            var competitions = JSON.parse(content);
            self.competitions = competitions;
            self.forceUpdate();
        });
    }

    loadCompetition(competition) {
        redirect("competition?competition=" + competition);
    }
}

export default Home;

import React, { Component } from 'react';
import '../styles/Navigation.css';
import { redirect } from './../../utils/requests.js';


class Navigation extends Component {

    constructor() {
        super();
        this.homeButtonAction = this.homeButtonAction.bind(this);
        this.prevButtonAction = this.prevButtonAction.bind(this);
    }

    prevButtonAction(e) {
        redirect(this.props.prevUrl);
    }

    homeButtonAction(e) {
        redirect("home");
    }

    render() {
        console.log(this.props);
        return (
            <div id="navigation">
                <div className="prevButton" onClick={this.prevButtonAction}>
                    <svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24" fill="#FFFFFF"><path d="M480-160 160-480l320-320 57 56-224 224h487v80H313l224 224-57 56Z"/></svg>
                </div>
                <div className="homeButton" onClick={this.homeButtonAction}>
                    <svg xmlns="http://www.w3.org/2000/svg" height="48px" viewBox="0 0 24 24" width="48px" fill="#FFFFFF"><path d="M0 0h24v24H0z" fill="none"/><path d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z"/></svg>
                </div>
                <h1>{this.props.title}</h1>
            </div>
        )
    }
}

export default Navigation;
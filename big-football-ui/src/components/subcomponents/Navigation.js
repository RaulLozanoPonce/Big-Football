import React, { Component } from 'react';
import '../styles/Navigation.css';
import { redirect } from './../../utils/requests.js';


class Navigation extends Component {

    constructor() {
        super();
        this.homeButtonAction = this.homeButtonAction.bind(this);
    }

    homeButtonAction(e) {
        redirect("home");
    }

    render() {
        return (
            <div id="navigation">
                <div className="homeButton" onClick={this.homeButtonAction}>
                    <svg xmlns="http://www.w3.org/2000/svg" height="48px" viewBox="0 0 24 24" width="48px" fill="#FFFFFF"><path d="M0 0h24v24H0z" fill="none"/><path d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z"/></svg>
                </div>
                <h1>{this.props.title}</h1>
            </div>
        )
    }
}

export default Navigation;
import React, { Component } from 'react';
import './styles/Navigation.css';
import { redirect } from './../utils/requests.js';

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
                <button onClick={this.homeButtonAction}>Pulsame</button>
                <p>{this.props.title}</p>
            </div>
        )
    }
}

export default Navigation;
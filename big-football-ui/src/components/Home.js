import React, { Component } from 'react';

import Navigation from './Navigation';

import './styles/Home.css';

class Home extends Component {

    constructor() {
        super();
    }

    render() {
        return (
            <div className="Home">
                <Navigation title="FBig Data"/>
                <div className="content">
                    <div className="content-component-1"></div>
                    <div className="content-component-2"></div>
                    <div className="content-component-3"></div>
                </div>
            </div>
        );
    }
}

export default Home;

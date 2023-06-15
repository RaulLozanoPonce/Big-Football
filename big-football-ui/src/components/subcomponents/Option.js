import React, { Component } from 'react';
import '../styles/Option.css';

class Option extends Component {

    constructor() {
        super();
    }

    render() {
        return (
            <div class="option" onClick={ this.props.actionFunction }>
                <p>{ this.props.title }</p>
            </div>
        )
    }
}

export default Option;
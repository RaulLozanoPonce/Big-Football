import React, { Component } from 'react';
import './../styles/LineupSelector.css';

class LineupSelector extends Component {

    render() {
        return (
            <div class="lineup-selector">
                <p>Formación:</p>
                <select id={this.props.selectLineupId}>
                    <option value="3-6-1">"3-6-1"</option>
                    <option value="3-5-2">"3-5-2"</option>
                    <option value="3-4-3">"3-4-3"</option>
                    <option value="4-5-1">"4-5-1"</option>
                    <option value="4-4-2">"4-4-2"</option>
                    <option value="4-3-3">"4-3-3"</option>
                    <option value="5-4-1">"5-4-1"</option>
                    <option value="5-3-2">"5-3-2"</option>
                </select>
                <p>Equipo rival:</p>
                <select id={this.props.selectTeamId}></select>
                <button onClick={this.props.loadFunction}>Consultar</button>
            </div>
        )
    }
}

export default LineupSelector;
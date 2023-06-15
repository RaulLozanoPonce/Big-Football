import React, { Component } from 'react';
import './../styles/TeamHeader.css';

class TeamHeader extends Component {

    render() {
        return (
            <div id="team-header">
                <div className="team-header-column" style={{ display:'flex', alignItems:'center'}}>
                    <div className="team-header-row">
                        <p id="played-matches" className="team-header-value"></p>
                        <p>partidos jugados</p>
                    </div>
                </div>
                <div className="team-header-column" style={{ display:'flex', alignItems:'center'}}>
                    <div className="team-header-row">
                        <p id="won-matches" className="team-header-value"></p>
                        <p>partidos ganados</p>
                    </div>
                </div>
                <div className="team-header-column" style={{ display:'flex', alignItems:'center'}}>
                    <div className="team-header-row">
                        <p id="draw-matches" className="team-header-value"></p>
                        <p>partidos empatados</p>
                    </div>
                </div>
                <div className="team-header-column" style={{ display:'flex', alignItems:'center'}}>
                    <div className="team-header-row">
                        <p id="lost-matches" className="team-header-value"></p>
                        <p>partidos perdidos</p>
                    </div>
                </div>
                <div className="team-header-column" style={{ display:'flex', alignItems:'center'}}>
                    <div className="team-header-row">
                        <p id="participating-players" className="team-header-value"></p>
                        <p>jugadores participantes</p>
                    </div>
                </div>
            </div>
        )
    }
}

export default TeamHeader;
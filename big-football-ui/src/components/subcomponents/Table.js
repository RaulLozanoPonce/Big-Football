import React, { Component } from 'react';

import './../styles/Table.css';

class Table extends Component {
    
    render() {

        var content = "";
        for(var i = 0; i < this.props.content.length; i++) {
            content += "<tr>";
            for(var j = 0; j < this.props.content[i].length; j++) {
                if(i == 0) {
                    content += "<th>" + this.props.content[i][j] + "</th>";
                } else {
                    if(j == 0) {
                        content += "<td>" + this.props.content[i][j] + "</td>";
                    } else {
                        content += "<td class='center-column'>" + this.props.content[i][j] + "</td>";
                    }
                }
            }
            content += "</tr>";
        }

        return (
            <div>
                <h4>{this.props.title}</h4>
                <table className="table" dangerouslySetInnerHTML={{__html: content}}>
                </table>
            </div>
        )
    }
}

export default Table;
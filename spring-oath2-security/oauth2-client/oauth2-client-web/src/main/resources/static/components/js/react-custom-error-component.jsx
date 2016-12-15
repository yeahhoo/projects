import React, { Component } from 'react';

class CustomErrorComponent extends Component {

    constructor(props) {
        super(props);
        this.printDate = this.printDate.bind(this);
        this.state = JSON.parse(document.getElementById('jsonResponse').innerHTML);
    }

    printDate(timeLong) {
        return new Date(timeLong).toString();
    }

    render() {
        return (
            <div>
                <div>Status: <span>{this.state.status}</span></div>
                <div>Error: <span>{this.state.error}</span></div>
                <div>Message: <span>{this.state.message}</span></div>
                <div>Timestamp: <span>{this.printDate(this.state.timestamp)}</span></div>
                Try to <a href="/client">start over</a>
            </div>
        )
    }
}

export default CustomErrorComponent
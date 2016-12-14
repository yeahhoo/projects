import React, { Component, PropTypes } from 'react';

class RestResultFormComponent extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <div className="col-sm-12">
                    <label className="col-sm-3">Username</label>
                    <span id="userPlaceholder">{this.props.usernameMsg}</span>
                </div>
                <div className="col-sm-12">
                    <label className="col-sm-3">Hello Msg</label>
                    <span id="helloPlaceholder">{this.props.helloMsg}</span>
                </div>
                <div className="col-sm-12">
                    <label className="col-sm-3">Server Msg</label>
                    <span id="serverPlaceholder">{this.props.serverMsg}</span>
                </div>
                <div className="col-sm-12">
                    <label className="col-sm-3">CORS Msg</label>
                    <span id="corsPlaceholder">{this.props.corsMsg}</span>
                </div>
            </div>
        );
    }
}

RestResultFormComponent.propTypes = {
    helloMsg: PropTypes.string.isRequired,
    corsMsg: PropTypes.string.isRequired,
    serverMsg: PropTypes.string.isRequired,
    usernameMsg: PropTypes.string.isRequired
}

export default RestResultFormComponent
import React, { Component, PropTypes } from 'react';
import { createClient, requestException } from './actions/createclient-action';

class CreateClientComponent extends Component {

    constructor(props) {
        super(props);
        this.onChange = this.onChange.bind(this);
        this.addClient = this.addClient.bind(this);
        this.processException = this.processException.bind(this);
    }

    addClient(e) {
        e.preventDefault();
        console.log('client created button pressed');
        var params = {
            client_id: this.state.client,
            client_secret: this.state.password,
            authorized_grant_types: this.state.grantTypes,
            scope: this.state.scopes
        };
        const { dispatch } = this.props;
        dispatch(createClient(params)).then(function() {
            this.refs.client_form.reset();
        }.bind(this));
        return false;
    }

    processException(e) {
        e.preventDefault();
        console.log('client exception button pressed');
        const { dispatch } = this.props;
        dispatch(requestException());
        return false;
    }

    onChange(e) {
        var state = {};
        state[e.target.name] = $.trim(e.target.value);
        this.setState(state);
    }

    render() {
        return (
            <div>
                {this.props.isLogined
                    ?
                        <form role="form" method="POST" ref='client_form' onSubmit={this.addClient}>
                            <div className="form-group">
                                <label htmlFor="client">Client:</label>
                                <input name="client" type="text" className="form-control" id="client" ref="client" placeholder="client" onChange={this.onChange} />
                            </div>
                            <div className="form-group">
                                <label htmlFor="password">Password:</label>
                                <input type="password" className="form-control" name="password" id="password" ref="password" placeholder="password" onChange={this.onChange}/>
                            </div>
                            <div className="form-group">
                                <label htmlFor="grantTypes">Grant Types:</label>
                                <input type="text" className="form-control" id="grantTypes" name="grantTypes" ref="grantTypes" placeholder="grantTypes" onChange={this.onChange}/>
                            </div>
                            <div className="form-group">
                                <label htmlFor="scopes">Scopes:</label>
                                <input type="text" className="form-control" id="scopes" name="scopes" ref="scopes" placeholder="scopes" onChange={this.onChange}/>
                            </div>
                            <button type="submit" id="submitBtn" className="btn btn-primary" disabled={this.props.isClientCreating}>
                                Submit
                            </button>
                            <br/>
                            <button type="button" id="expBtn" className="btn btn-primary" disabled={this.props.isExceptionFetching} onClick={this.processException}>Exception</button>
                        </form>
                    : <a href="/client/login">To create client you should login first</a>
                }
            </div>
        )
    }
}

CreateClientComponent.propTypes = {
    client: PropTypes.string.isRequired,
    clientSecret: PropTypes.string.isRequired,
    authorizedGrantTypes: PropTypes.string.isRequired,
    scopes: PropTypes.string.isRequired,
    isClientCreating: PropTypes.bool.isRequired,
    isExceptionFetching: PropTypes.bool.isRequired,
    isLogined: PropTypes.bool.isRequired,
    dispatch: PropTypes.func.isRequired
}

export default CreateClientComponent;
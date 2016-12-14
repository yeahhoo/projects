import React, { Component, PropTypes } from 'react';
import { fetchHelloMsg, fetchCorsMsg, fetchUsernameMsg, fetchServerMsg } from './actions/menu-action';


class NavigationMenuComponent extends Component {

    // https://github.com/reactjs/redux/blob/master/docs/advanced/ExampleRedditAPI.md
    constructor(props) {
        super(props);
        this.getHelloMsg = this.getHelloMsg.bind(this);
        this.getCorsMsg = this.getCorsMsg.bind(this);
        this.getUsername = this.getUsername.bind(this);
        this.getServerMsg = this.getServerMsg.bind(this);
        this.showCreateUserForm = this.showCreateUserForm.bind(this);
        this.showCreateClientForm = this.showCreateClientForm.bind(this);
        this.sendRequest = this.sendRequest.bind(this);
    }

    sendRequest(e, func) {
        e.preventDefault();
        const { dispatch } = this.props;
        dispatch({type: 'SHOW_HOME'});
        dispatch(func());
        return false;
    }

    getHelloMsg(e) {
        this.sendRequest(e, fetchHelloMsg);
    }

    getCorsMsg(e) {
        this.sendRequest(e, fetchCorsMsg);
    }

    getUsername(e) {
        this.sendRequest(e, fetchUsernameMsg);
    }

    getServerMsg(e) {
        this.sendRequest(e, fetchServerMsg);
    }

    //https://github.com/reactjs/redux/blob/master/docs/basics/ExampleTodoList.md
    showCreateUserForm(e) {
        e.preventDefault();
        console.log('showing user_create_form');
        const { dispatch } = this.props;
        dispatch({type: 'SHOW_CREATE_USER_FORM'});
        return false;
    }

    showCreateClientForm(e) {
        e.preventDefault();
        console.log('showing client_create_form');
        const { dispatch } = this.props;
        dispatch({type: 'SHOW_CREATE_CLIENT_FORM'});
        return false;
    }

    /* Example from: https://getbootstrap.com/examples/navbar-fixed-top/ */
    render() {
        return (
            <nav className="navbar navbar-default navbar-fixed-top">
                <div className="container">
                    <div className="navbar-header">
                        <button type="button" className="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                            <span className="sr-only">Toggle navigation</span>
                            <span className="icon-bar"></span>
                            <span className="icon-bar"></span>
                            <span className="icon-bar"></span>
                        </button>
                        <div className="navbar-brand" href="#">Oauth2 client</div>
                    </div>

                    <div id="navbar" className="navbar-collapse collapse">
                        <ul className="nav navbar-nav">
                            <li className="active"><a href="#">Home</a></li>
                            <li><a href="#about">About</a></li>
                            <li><a href="#contact">Contact</a></li>
                            <li className="dropdown">
                                <a href="#" className="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                                    Actions <span className="caret"></span>
                                </a>
                                <ul className="dropdown-menu">
                                    <li><a href="#hello" onClick={this.getHelloMsg}>Hello Message</a></li>
                                    <li><a href="#cors" onClick={this.getCorsMsg}>CORS Message</a></li>
                                    <li><a href="#createUserForm" onClick={this.showCreateUserForm}>Create OAuth User</a></li>
                                    <li><a href="#createClientForm" onClick={this.showCreateClientForm}>Create OAuth Client</a></li>
                                    <li role="separator" className="divider"></li>
                                    <li className="dropdown-header">Secured Actions</li>
                                    <li><a href="#userName" onClick={this.getUsername}>Get Username</a></li>
                                    <li><a href="#serverMsg" onClick={this.getServerMsg}>Get User Message</a></li>
                                    <li role="separator" className="divider"></li>
                                    <li className="dropdown-header">Open In New Pages</li>
                                    <li><a href="/client/createUser">Create OAuth User</a></li>
                                    <li><a href="/client/createClient">Create OAuth Client</a></li>
                                </ul>
                            </li>
                        </ul>
                        <ul className="nav navbar-nav navbar-right">
                            <li>
                                {this.props.isLogined
                                    ? <a href="/client/requestLogout" id="logoutServerBtn">Logout as {this.props.username}</a>
                                    : <a href="/client/login">Login</a>
                                }
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
        );
    }

}

NavigationMenuComponent.propTypes = {
    username: PropTypes.string.isRequired,
    isLogined: PropTypes.bool.isRequired,
    dispatch: PropTypes.func.isRequired
}

export default NavigationMenuComponent
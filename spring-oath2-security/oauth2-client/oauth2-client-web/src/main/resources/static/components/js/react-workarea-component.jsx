import React, { Component, PropTypes } from 'react';
import CreateUserForm from './containers/createuser-container'
import CreateClientForm from './containers/createclient-container'
import RestResultFormComponent from './containers/rest-result-form-container'
import CustomErrorComponent from './react-custom-error-component'

class WorkAreaComponent extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="container">
                <div className="jumbotron">
                    <h1>OAuth2 Spring Example</h1>
                    {this.props.activeComponent === 'SHOW_CREATE_USER_FORM' && <CreateUserForm />}
                    {this.props.activeComponent === 'SHOW_CREATE_CLIENT_FORM' && <CreateClientForm />}
                    {this.props.activeComponent === 'SHOW_HOME' && <RestResultFormComponent />}
                    {this.props.activeComponent === 'SHOW_CONTACT_PAGE' && <RestResultFormComponent />}
                    {this.props.activeComponent === 'SHOW_ABOUT_PAGE' && <RestResultFormComponent />}
                    {this.props.activeComponent === 'SHOW_ERROR_PAGE' && <CustomErrorComponent />}
                </div>
            </div>
        );
    }
}

WorkAreaComponent.propTypes = {
    activeComponent: PropTypes.string.isRequired
}

export default WorkAreaComponent;
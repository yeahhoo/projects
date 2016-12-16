import React, { Component, PropTypes } from 'react';
import { createUser } from './actions/createuser-action';

class CreateUserComponent extends Component {

    constructor(props) {
        super(props);
        this.onChange = this.onChange.bind(this);
        this.addUser = this.addUser.bind(this);
        this.state = props;
    }

    addUser(e) {
        e.preventDefault();
        console.log('user created button pressed');
        var params = {
            user: this.state.user,
            password: this.state.password
        };
        const { dispatch } = this.props;
        dispatch(createUser(params)).then((json => {
            alert('user created: ' + JSON.stringify(json.data));
            this.refs.user_form.reset();
        }).bind(this)).fail(e => {
            var data = JSON.parse(e.responseText);
            alert('user caused: \nerror: ' + data.error + '\nexception: ' + data.exception + '\nmessage: ' + data.message + '\nstatus: ' + data.status + '\ntrace: ' + data.trace);
        });
        return false;
    }

    onChange(e) {
        var state = {};
        state[e.target.name] = $.trim(e.target.value);
        this.setState(state);
    }

    render() {
        return (
            <form role="form" method="POST" ref='user_form' onSubmit={this.addUser}>
                <div className="form-group">
                    <label htmlFor="user">User:</label>
                    <input name="user" type="text" className="form-control" id="user" ref="user" placeholder="user" onChange={this.onChange} />
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password:</label>
                    <input type="password" className="form-control" name="password" id="password" ref="password" placeholder="password" onChange={this.onChange}/>
                </div>
                <button type="submit" id="submitBtn" className="btn btn-primary" disabled={this.props.isUserCreating}>
                    Create User
                </button>
            </form>
        )
    }

}

CreateUserComponent.propTypes = {
    user: PropTypes.string.isRequired,
    password: PropTypes.string.isRequired,
    isUserCreating: PropTypes.bool.isRequired,
    dispatch: PropTypes.func.isRequired
}

export default CreateUserComponent
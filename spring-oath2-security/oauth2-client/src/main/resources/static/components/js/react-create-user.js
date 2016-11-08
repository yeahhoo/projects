var CreateUserForm = React.createClass({

    getInitialState: function () {
        return {user: '', password: '', errors: {}, loading: false}
    },

    addUser: function(e) {
        e.preventDefault();
        console.log('clicked');

        var params = {
            user: this.state.user,
            password: this.state.password
        };

        var request = this.createRequest('/client/server/oauth_user/create', 'POST', JSON.stringify(params), 'application/json; charset=utf-8');
        request.done(function(data) {
            this.refs.user_form.reset();
            this.setState(this.getInitialState());
            alert('user created: ' + data);
        }.bind(this))
        .fail(this.onError)
        .always(this.hideLoading);

        return false;
    },

    createRequest: function (url, type, data, contentType) {
        return $.ajax({
            url: url,
            type: type,
            data: data,
            contentType: contentType,
            beforeSend: function () {
                this.setState({loading: true});
            }.bind(this)
        });
    },

    onError: function (e) {
        var data = JSON.parse(e.responseText);
        alert('client caused: \nerror: ' + data.error + '\nexception: ' + data.exception
            + '\nmessage: ' + data.message + '\nstatus: ' + data.status + '\ntrace: ' + data.trace);
        if (data) {
            this.setState({
                errors: data
            });
        }
    },

    hideLoading: function () {
        this.setState({loading: false});
    },

    onChange: function (e) {
        var state = {};
        state[e.target.name] = $.trim(e.target.value);
        this.setState(state);
    },

    render: function() {
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
                <button type="submit" id="submitBtn" className="btn btn-primary" disabled={this.state.loading}>
                    Create User
                </button>
            </form>
        )
    }
});

ReactDOM.render(
    <CreateUserForm/>,
    document.getElementById('react-create-client-container')
);
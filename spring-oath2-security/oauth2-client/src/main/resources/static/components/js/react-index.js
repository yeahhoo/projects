var IndexPage = React.createClass({

    getInitialState: function () {
        return {username: '', helloMsg: '', serverMsg: '', corsMsg: ''}
    },

    getUsername: function(e) {
        e.preventDefault();
        console.log('getUsername clicked');
        var request = this.createRequest('/client/server/user', 'GET');
        request.done(function(data) {
            this.setState({username: data.name});
        }.bind(this));
        return false;
    },

    getHelloMsg: function(e) {
        e.preventDefault();
        console.log('getHelloMsg clicked');
        var request = this.createRequest('/client/server/hello', 'GET');
        request.done(function(data) {
            this.setState({helloMsg: data});
        }.bind(this));
        return false;
    },

    getServerMsg: function(e) {
        e.preventDefault();
        console.log('getServerMsg clicked');
        var request = this.createRequest('/client/server/hellouser', 'GET');
        request.done(function(data) {
            this.setState({serverMsg: data});
        }.bind(this));
        return false;
    },

    getCorsMsg: function(e) {
        e.preventDefault();
        console.log('getCorsMsg clicked');
        var request = this.createRequest('http://localhost:9001/server/cors', 'GET');
        request.done(function(data) {
            this.setState({corsMsg: data});
        }.bind(this));
        return false;
    },

    createRequest: function (url, type, data, contentType) {
        return $.ajax({
            url: url,
            type: type,
            data: data,
            contentType: contentType
        }).done(function(data) {
            console.log('request succeeded: ' + JSON.stringify(data));
        }).fail(this.onError);
    },

    onError: function (e) {
        console.log('request failed: ' + JSON.stringify(e));
    },

    render: function() {
        return (
            <div>
                <div>With Facebook: <a href="/client/login">click here</a></div>
                <h1 className="col-sm-12">Foo Details</h1>
                <div className="col-sm-12">
                    <label className="col-sm-3">Username</label>
                    <span id="userPlaceholder">{this.state.username}</span>
                </div>

                <div className="col-sm-12">
                    <label className="col-sm-3">Hello Msg</label>
                    <span id="helloPlaceholder">{this.state.helloMsg}</span>
                </div>

                <div className="col-sm-12">
                    <label className="col-sm-3">Server Msg</label>
                    <span id="serverPlaceholder">{this.state.serverMsg}</span>
                </div>

                <div className="col-sm-12">
                    <label className="col-sm-3">CORS Msg</label>
                    <span id="corsPlaceholder">{this.state.corsMsg}</span>
                </div>

                <div className="col-sm-12">
                    <br/>
                    <br/>
                    <button type="button" id="userBtn" className="btn btn-info" onClick={this.getUsername}>Get username</button>
                </div>

                <div className="col-sm-12">
                    <br/>
                    <br/>
                    <button type="button" id="helloBtn" className="btn btn-info" onClick={this.getHelloMsg}>Get hello msg</button>
                </div>

                <div className="col-sm-12">
                    <br/>
                    <br/>
                    <button type="button" id="serverBtn" className="btn btn-info" onClick={this.getServerMsg}>Get server msg</button>
                </div>

                <div className="col-sm-12">
                    <br/>
                    <br/>
                    <button type="button" id="corsBtn" className="btn btn-info" onClick={this.getCorsMsg}>CORS server msg</button>
                </div>

                <div className="col-sm-12">
                    <br/>
                    <br/>
                    <a className="btn btn-info" href="/client/createClient" id="createClient">Create OAuth Client</a>
                </div>

                <div className="col-sm-12">
                    <br/>
                    <br/>
                    <a className="btn btn-info" href="/client/createUser" id="createUser">Create User</a>
                </div>

                <div className="col-sm-12">
                    <br/>
                    <br/>
                    <a className="btn btn-info" href="/client/requestLogout" id="logoutServerBtn">Logout</a>
                </div>
            </div>
        )
    }
});

ReactDOM.render(
    <IndexPage />,
    document.getElementById('react-index-container')
);
(function() {

    'use strict';

    var IndexPage = React.createClass({

        getInitialState: function () {
            return {username: '', helloMsg: '', serverMsg: '', corsMsg: '', serverJson: JSON.parse(document.getElementById('jsonResponse').innerHTML)}
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

        /* Example from: https://getbootstrap.com/examples/navbar-fixed-top/ */
        render: function() {
            return (
                <div>
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
                                            <li><a href="/client/createClient">Create OAuth Client</a></li>
                                            <li role="separator" className="divider"></li>
                                            <li className="dropdown-header">Secured Actions</li>
                                            <li><a href="#userName" onClick={this.getUsername}>Get Username</a></li>
                                            <li><a href="/client/createClient">Create OAuth Client</a></li>
                                        </ul>
                                    </li>
                                </ul>
                                <ul className="nav navbar-nav navbar-right">
                                    <li>
                                        {this.state.serverJson.isLogined
                                            ? <a href="/client/requestLogout" id="logoutServerBtn">Logout as {this.state.serverJson.username}</a>
                                            : <a href="/client/login">Login</a>
                                        }
                                    </li>
                                </ul>
                            </div>
                        </div>
            	    </nav>

                    <div className="container">
                        <div className="jumbotron">
                            <h1>OAuth2 Spring Example</h1>

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
                        </div>
                    </div>
                </div>
            )
        }
    });

    ReactDOM.render(
        <IndexPage />,
        document.getElementById('react-index-container')
    );

})();
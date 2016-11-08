    /*
    the reason of sending header 'X-XSRF-TOKEN' with the same value as cookie 'XSRF-TOKEN' is to comply with authorisation mechanism:
    1) server gets value of the token from session;
    2) server extracts value of the header from request;
    3) compare them to make sure that user has sent it.
    so if to make a special filter on server side that will add the header for every request then it will compromise security.

    the source: org.springframework.security.web.csrf.CsrfFilter
    */
    /*
    function doSecureRequest(url, type, params, contentType) {
        return $.ajax({
            type: type,
            url: url,
            data: params,
            headers: {'X-XSRF-TOKEN': readCookie('XSRF-TOKEN')},
            contentType: contentType
        }).done(function(data) {
            console.log('non secured POST successful request: ' + JSON.stringify(data));
        }).fail(function(e) {
            console.log('non secured POST failed request: ' + JSON.stringify(e));
        });
    };

    function readCookie(name) {
        var nameEQ = encodeURIComponent(name) + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) === ' ') c = c.substring(1, c.length);
            if (c.indexOf(nameEQ) === 0) return decodeURIComponent(c.substring(nameEQ.length, c.length));
        }
        return null;
    }
*/


var CreateClientForm = React.createClass({

    getInitialState: function () {
        return {client: '', password: '', grantTypes: '', scopes: '', errors: {}, loading: false}
    },

    addClient: function(e) {
        e.preventDefault();
        console.log('clicked');

        var params = {
            client: this.state.client,
            secret: this.state.password,
            grantTypes: this.state.grantTypes.split(','),
            scopes: this.state.scopes.split(',')
        };

        var request = this.createRequest('/client/server/oauth_client/create', 'POST', JSON.stringify(params), 'application/json; charset=utf-8');
        request.done(function(data) {
            this.refs.user_form.reset();
            this.setState(this.getInitialState());
            alert('client created: ' + data);
        }.bind(this))
        .fail(this.onError)
        .always(this.hideLoading);

        return false;
    },

    processException: function(e) {
        e.preventDefault();
        console.log('exception clicked');
        this.createRequest('/client/server/oauth_client/exception', 'POST', {}, 'application/json; charset=utf-8')
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
            <form role="form" method="POST" ref='user_form' onSubmit={this.addClient}>
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
                <button type="submit" id="submitBtn" className="btn btn-primary" disabled={this.state.loading}>
                    Submit
                </button>
                <br/>
                <button type="button" id="expBtn" className="btn btn-primary" onClick={this.processException}>Exception</button>
            </form>
        )
    }
});

ReactDOM.render(
    <CreateClientForm/>,
    document.getElementById('react-create-client-container')
);
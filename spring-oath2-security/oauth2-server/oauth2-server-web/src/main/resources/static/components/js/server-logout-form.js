var ServerLogoutForm, renderClientLogoutForm, renderServerLogoutForm;

(function() {

    'use strict';

    ServerLogoutForm = React.createClass({

        getInitialState: function () {
            return {user: this.props.user, client: this.props.client, token: this.props.token, url: this.props.url, csrfToken: this.props.csrfToken};
        },

        componentDidMount: function () {
            console.log('ServerLogoutForm component mounted');
        },

        submitForm: function(e) {
            e.preventDefault();
            console.log('FORM submitted');
            var headers = {'authorization': 'bearer ' + this.state.token};
            var redirectUrl = this.state.url;
            this.createRequest('/server/mylogout', 'GET', headers, {client: this.state.client})
            .done(function(data) {
                console.log('Server logout successful' + data);
                window.location.href = redirectUrl;
            });
            return false;
        },

        createRequest: function (url, type, headers, data, contentType) {
            return $.ajax({
                url: url,
                type: type,
                data: data,
                headers: headers,
                contentType: contentType
            }).fail(this.onError);
        },

        onError: function (e) {
            var data = JSON.parse(e.responseText);
            alert('client caused: \nerror: ' + data.error + '\ndescription: ' + data.error_description);
        },

        render: function() {
            return (
                React.createElement('form', {id: 'confirmationForm', name: 'confirmationForm', method: 'POST', action: '/server/mylogout', onSubmit: this.submitForm},
                    React.createElement('input', {name: 'token', type: 'hidden', value: this.state.token}),
                    React.createElement('input', {name: 'userName', type: 'hidden', value: this.state.user}),
                    React.createElement('input', {name: 'clientName', type: 'hidden', value: this.state.client}),
            	    React.createElement('input', {name: 'urlToReturn', type: 'hidden', value: this.state.url}),
            	    React.createElement('input', {name: '_csrf', type: 'hidden', value: this.state.csrfToken}),
            	    React.createElement('button', {id: 'logoutBtn', type: 'submit', className: 'btn btn-primary'}, 'Logout Fellow')
                )
            );
        }
    });


    renderClientLogoutForm = function (data) {
        ReactDOM.render(
            React.createElement(ServerLogoutForm, {user: data.user, client: data.client, token: data.token, url: data.urlToReturn, csrfToken: data.csrfToken}),
            document.getElementById("react-logout-container")
        );
    };

    renderServerLogoutForm = function (user, client, token, urlToReturn, csrfToken) {
        return ReactDOMServer.renderToString(
            React.createElement(ServerLogoutForm, {user: user, client: client, token: token, url: urlToReturn, csrfToken: csrfToken})
        );
    };

})();

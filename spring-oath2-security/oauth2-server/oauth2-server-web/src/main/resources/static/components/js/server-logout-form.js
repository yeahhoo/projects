
var ServerLogoutForm = React.createClass({

    getInitialState: function () {
        'use strict';
        return {user: this.props.user, token: this.props.token, url: this.props.url, csrfToken: this.props.csrfToken};
    },

    componentDidMount: function () {
        'use strict';
        console.log('ServerLogoutForm component mounted');
    },

    submitForm: function(e) {
        'use strict';
        e.preventDefault();
        console.log('FORM submitted');
        var headers = {'authorization': 'bearer ' + this.state.token};
        var redirectUrl = this.state.url;
        this.createRequest('/server/mylogout', 'GET', headers)
        .done(function(data) {
            console.log('Server logout successful' + data);
            window.location.href = redirectUrl;
        });
        return false;
    },

    createRequest: function (url, type, headers, data, contentType) {
        'use strict';
        return $.ajax({
            url: url,
            type: type,
            data: data,
            headers: headers,
            contentType: contentType
        }).fail(this.onError);
    },

    onError: function (e) {
        'use strict';
        var data = JSON.parse(e.responseText);
        alert('client caused: \nerror: ' + data.error + '\ndescription: ' + data.error_description);
    },

    render: function() {
        'use strict';
        return (
            React.createElement('form', {id: 'confirmationForm', name: 'confirmationForm', method: 'POST', action: '/server/mylogout', onSubmit: this.submitForm},
                React.createElement('input', {name: 'token', type: 'hidden', value: this.state.token}),
                React.createElement('input', {name: 'userName', type: 'hidden', value: this.state.user}),
            	React.createElement('input', {name: 'urlToReturn', type: 'hidden', value: this.state.url}),
            	React.createElement('input', {name: '_csrf', type: 'hidden', value: this.state.csrfToken}),
            	React.createElement('button', {id: 'logoutBtn', type: 'submit', className: 'btn btn-primary'}, 'Logout Fellow')
            )
        );
    }
});

/*exported renderClientLogoutForm, renderServerLogoutForm */
var renderClientLogoutForm = function (data) {
    'use strict';
    ReactDOM.render(
        React.createElement(ServerLogoutForm, {user: data.user, token: data.token, url: data.urlToReturn, csrfToken: data.csrfToken}),
        document.getElementById("react-logout-container")
    );
};

var renderServerLogoutForm = function (user, token, urlToReturn, csrfToken) {
    'use strict';
    return ReactDOMServer.renderToString(
        React.createElement(ServerLogoutForm, {user: user, token: token, url: urlToReturn, csrfToken: csrfToken})
    );
};
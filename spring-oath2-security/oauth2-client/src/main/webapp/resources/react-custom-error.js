
var CustomErrorForm = React.createClass({

    getInitialState: function () {
        return {
            status: document.getElementById('status').innerHTML,
            error: document.getElementById('error').innerHTML,
            message: document.getElementById('message').innerHTML,
            timestamp: document.getElementById('timestamp').innerHTML
        }
    },

    render: function() {
        return (
            <div>
                <div>Status: <span>{this.state.status}</span></div>
                <div>Error: <span>{this.state.error}</span></div>
                <div>Message: <span>{this.state.message}</span></div>
                <div>Timestamp: <span>{this.state.timestamp}</span></div>
                Try to <a href="/client">start over</a>
            </div>
        )
    }
});

ReactDOM.render(
    <CustomErrorForm />,
    document.getElementById('react-custom-error-container')
);
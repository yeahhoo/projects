(function() {

    'use strict';

    var CustomErrorForm = React.createClass({

        getInitialState: function () {
            return {
                errorJson: JSON.parse(document.getElementById('jsonError').innerHTML)
            }
        },

        printDate: function(timeLong) {
            var timeStr = new Date(timeLong).toString();
            return timeStr;
        },

        render: function() {
            var timeStamp = this.state.errorJson.timestamp;
            return (
                <div>
                    <div>Status: <span>{this.state.errorJson.status}</span></div>
                    <div>Error: <span>{this.state.errorJson.error}</span></div>
                    <div>Message: <span>{this.state.errorJson.message}</span></div>
                    <div>Timestamp: <span>{this.printDate(timeStamp)}</span></div>
                    Try to <a href="/client">start over</a>
                </div>
            )
        }
    });

    ReactDOM.render(
        <CustomErrorForm />,
        document.getElementById('react-custom-error-container')
    );

})();
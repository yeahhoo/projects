


    function createRequest(url, type, data, contentType) {
        return $.ajax({
            url: url,
            type: type,
            data: data,
            contentType: contentType
        }).done(function(data) {
            console.log('request succeeded: ' + JSON.stringify(data));
        }).fail(function(e) {
            console.log('request failed: ' + e);
        });
    }
    // helloMsg
    function requestHelloMsg() {
        return {
            type: 'HELLO_MSG'
        };
    }

    function receiveHelloMsg(txtData) {
        return {
            type: 'RECEIVE_HELLO_MSG',
            data: txtData,
            receivedAt: Date.now()
        };
    }

     /*jshint unused:false*/
    function fetchHelloMsg() {
        return dispatch => {
            dispatch(requestHelloMsg());
            return createRequest('/client/server/hello', 'GET')
                //.then(response => response.json())
                .then(txt => dispatch(receiveHelloMsg(txt)));
        };
    }

    // corsMsg
    function requestCorsMsg() {
        return {
            type: 'CORS_MSG'
        };
    }

    function receiveCorsMsg(txtData) {
        return {
            type: 'RECEIVE_CORS_MSG',
            data: txtData,
            receivedAt: Date.now()
        };
    }

    /* jshint unused:false */
    function fetchCorsMsg() {
        return dispatch => {
            dispatch(requestCorsMsg());
            return createRequest('http://localhost:9001/server/cors', 'GET')
                //.then(response => response.json())
                .then(txt => dispatch(receiveCorsMsg(txt)));
        };
    }

    // usernameMsg
    function requestUsernameMsg() {
        return {
            type: 'USERNAME_MSG'
        };
    }

    function receiveUsernameMsg(data) {
        return {
            type: 'RECEIVE_USERNAME_MSG',
            data: data.name,
            receivedAt: Date.now()
        };
    }

    /* jshint unused:false */
    function fetchUsernameMsg() {
        return dispatch => {
            dispatch(requestUsernameMsg());
            return createRequest('/client/server/user', 'GET')
                //.then(response => response.json())
                .then(json => dispatch(receiveUsernameMsg(json)));
        };
    }

    // serverMsg
    function requestServerMsg() {
        return {
            type: 'SERVER_MSG'
        };
    }

    function receiveServerMsg(data) {
        return {
            type: 'RECEIVE_SERVER_MSG',
            data: data,
            receivedAt: Date.now()
        };
    }

    /* jshint unused:false */
    function fetchServerMsg() {
        return dispatch => {
            dispatch(requestServerMsg());
            return createRequest('/client/server/hellouser', 'GET')
                //.then(response => response.json())
                .then(json => dispatch(receiveServerMsg(json)));
        };
    }


    /*
    function fetchHelloMsg() {
        return dispatch => {
            dispatch(requestHelloMsg())
            return fetch('/client/server/hello')
                //.then(response => response.json())
                .then(txt => dispatch(receiveHelloMsg(txt)))
        }
    }
    */

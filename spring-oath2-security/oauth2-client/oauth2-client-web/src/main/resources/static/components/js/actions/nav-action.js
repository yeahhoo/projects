
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

    function wrapDispatchAction(actionName) {
        return {
            type: actionName
        };
    }

    // helloMsg
    function receiveHelloMsg(txtData) {
        return {
            type: 'RECEIVE_HELLO_MSG',
            data: txtData,
            receivedAt: Date.now()
        };
    }

    export const fetchHelloMsg = function() {
        return dispatch => {
            dispatch(wrapDispatchAction('HELLO_MSG'));
            return createRequest('/client/server/hello', 'GET')
                //.then(response => response.json())
                .then(txt => dispatch(receiveHelloMsg(txt)));
        };
    }

    // corsMsg
    function receiveCorsMsg(txtData) {
        return {
            type: 'RECEIVE_CORS_MSG',
            data: txtData,
            receivedAt: Date.now()
        };
    }

    export const fetchCorsMsg = function() {
        return dispatch => {
            dispatch(wrapDispatchAction('CORS_MSG'));
            return createRequest('http://localhost:9001/server/cors', 'GET')
                //.then(response => response.json())
                .then(txt => dispatch(receiveCorsMsg(txt)));
        };
    }

    // usernameMsg
    function receiveUsernameMsg(data) {
        return {
            type: 'RECEIVE_USERNAME_MSG',
            data: data.name,
            receivedAt: Date.now()
        };
    }

    export const fetchUsernameMsg = function() {
        return dispatch => {
            dispatch(wrapDispatchAction('USERNAME_MSG'));
            return createRequest('/client/server/user', 'GET')
                //.then(response => response.json())
                .then(json => dispatch(receiveUsernameMsg(json)));
        };
    }

    // serverMsg
    function receiveServerMsg(data) {
        return {
            type: 'RECEIVE_SERVER_MSG',
            data: data,
            receivedAt: Date.now()
        };
    }

    export const fetchServerMsg = function() {
        return dispatch => {
            dispatch(wrapDispatchAction('SERVER_MSG'));
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

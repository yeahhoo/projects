

const createRequest = function(url, type, data, contentType) {
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

const wrapDispatchAction = function(actionName, params) {
    return {
        type: actionName,
        params: params
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

export { createRequest, wrapDispatchAction };
import { createRequest, wrapDispatchAction } from './util';

function clientCreated(response) {
    alert('client created: ' + JSON.stringify(response));
    return {
        type: 'RECEIVE_CLIENT_CREATE_RESPONSE',
        data: response,
        receivedAt: Date.now()
    };
}

function failHandler(e, type) {
    var data = JSON.parse(e.responseText);
    alert('client caused: \nerror: ' + data.error + '\nexception: ' + data.exception + '\nmessage: ' + data.message + '\nstatus: ' + data.status + '\ntrace: ' + data.trace);
    return {
        type: type,
        error: e,
        receivedAt: Date.now()
    };
}

const createClient = function(params) {
    return dispatch => {
        dispatch(wrapDispatchAction('CREATE_CLIENT_REQUEST'));
        return createRequest('/client/server/oauth_client/create', 'POST', JSON.stringify(params), 'application/json; charset=utf-8')
            .then(json => dispatch(clientCreated(json)))
            .fail(e => dispatch(failHandler(e, 'CLIENT_CREATE_FAILED')));
    };
}

const requestException = function() {
    return dispatch => {
        dispatch(wrapDispatchAction('CREATE_CLIENT_CREATE_EXCEPTION_REQUEST'));
        return createRequest('/client/server/oauth_client/exception', 'POST', {}, 'application/json; charset=utf-8')
            .fail(e => dispatch(failHandler(e, 'CLIENT_CREATE_EXCEPTION_FAILED')));
    };
}

export { createClient, requestException };
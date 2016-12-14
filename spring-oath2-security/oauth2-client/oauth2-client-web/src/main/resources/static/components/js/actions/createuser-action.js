import { createRequest, wrapDispatchAction } from './util';

function userCreated(response) {
    alert('user created: ' + JSON.stringify(response));
    return {
        type: 'RECEIVE_USER_CREATE_RESPONSE',
        data: response,
        receivedAt: Date.now()
    };
}

function failHandler(e) {
    var data = JSON.parse(e.responseText);
    alert('user caused: \nerror: ' + data.error + '\nexception: ' + data.exception + '\nmessage: ' + data.message + '\nstatus: ' + data.status + '\ntrace: ' + data.trace);
    return {
        type: 'USER_CREATE_FAILED',
        error: e,
        receivedAt: Date.now()
    };
}

const createUser = function(params) {
    return dispatch => {
        dispatch(wrapDispatchAction('CREATE_USER_REQUEST'));
        return createRequest('/client/server/oauth_user/create', 'POST', JSON.stringify(params), 'application/json; charset=utf-8')
            .then(json => dispatch(userCreated(json)))
            .fail(e => dispatch(failHandler(e)));
    };
}

export { createUser };
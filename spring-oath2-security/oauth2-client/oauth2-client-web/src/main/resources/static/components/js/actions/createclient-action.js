import { doRequest, wrapPreRequestDispatchAction, wrapAfterRequestDispatchAction } from './util';

const createClient = function(params) {
    return dispatch => {
        dispatch(wrapPreRequestDispatchAction('CREATE_CLIENT_REQUEST'));
        return doRequest('/client/server/oauth_client/create', 'POST', 'application/json; charset=utf-8', JSON.stringify(params))
            //.then(response => response.json())
            .then(json => dispatch(wrapAfterRequestDispatchAction('RECEIVE_CLIENT_CREATE_RESPONSE', json)))
            .fail(e => dispatch(wrapAfterRequestDispatchAction('CLIENT_CREATE_FAILED', e)));
    };
}

const requestException = function() {
    return dispatch => {
        dispatch(wrapPreRequestDispatchAction('CREATE_CLIENT_CREATE_EXCEPTION_REQUEST'));
        return doRequest('/client/server/oauth_client/exception', 'POST', 'application/json; charset=utf-8', {})
            //.then(response => response.json())
            .then(() => console.error('OOPS, it had to fail'))
            .fail(e => dispatch(wrapAfterRequestDispatchAction('CLIENT_CREATE_EXCEPTION_FAILED', e)));
    };
}

export { createClient, requestException };
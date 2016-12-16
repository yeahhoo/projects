import { doRequest, wrapPreRequestDispatchAction, wrapAfterRequestDispatchAction } from './util';

const createUser = function(params) {
    return dispatch => {
        dispatch(wrapPreRequestDispatchAction('CREATE_USER_REQUEST'));
        return doRequest('/client/server/oauth_user/create', 'POST', 'application/json; charset=utf-8', JSON.stringify(params))
            //.then(response => response.json())
            .then(json => dispatch(wrapAfterRequestDispatchAction('RECEIVE_USER_CREATE_RESPONSE', json)))
            .fail(e => dispatch(wrapAfterRequestDispatchAction('USER_CREATE_FAILED', e)));
    };
}

export { createUser };
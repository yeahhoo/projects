import { doRequest, wrapPreRequestDispatchAction, wrapAfterRequestDispatchAction } from './util';

const fetchAboutMsg = function() {
    return dispatch => {
        dispatch(wrapPreRequestDispatchAction('REQUEST_ABOUT_MSG'));
        return doRequest('/client/server/about', 'GET', 'text/plain')
            //.then(response => response.text())
            .then(txt => dispatch(wrapAfterRequestDispatchAction('RECEIVE_ABOUT_MSG', txt.replace(/(?:\r\n|\r|\n)/g, '<br />'))));
    };
}

const fetchContactMsg = function() {
    return dispatch => {
        dispatch(wrapPreRequestDispatchAction('REQUEST_CONTACT_MSG'));
        return doRequest('http://localhost:9001/server/contact', 'GET', 'text/plain')
            //.then(response => response.text())
            .then(txt => dispatch(wrapAfterRequestDispatchAction('RECEIVE_CONTACT_MSG', txt.replace(/(?:\r\n|\r|\n)/g, '<br />'))));
    };
}

const fetchUsernameMsg = function() {
    return dispatch => {
        dispatch(wrapPreRequestDispatchAction('REQUEST_USERNAME_MSG'));
        return doRequest('/client/server/user', 'GET', 'application/json; charset=utf-8')
            //.then(response => response.json())
            .then(json => dispatch(wrapAfterRequestDispatchAction('RECEIVE_USERNAME_MSG', json.name)));
    };
}

const fetchServerMsg = function() {
    return dispatch => {
        dispatch(wrapPreRequestDispatchAction('REQUEST_SERVER_MSG'));
        return doRequest('/client/server/hellouser', 'GET', 'application/json; charset=utf-8')
            //.then(response => response.json())
            .then(json => dispatch(wrapAfterRequestDispatchAction('RECEIVE_SERVER_MSG', json)));
    };
}

export { fetchAboutMsg, fetchContactMsg, fetchUsernameMsg, fetchServerMsg };
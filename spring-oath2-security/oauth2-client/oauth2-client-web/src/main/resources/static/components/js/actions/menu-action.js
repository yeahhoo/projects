import { createRequest, wrapDispatchAction } from './util';

// aboutMsg
function receiveAboutMsg(txtData) {
    return {
        type: 'RECEIVE_ABOUT_MSG',
        data: txtData.replace(/(?:\r\n|\r|\n)/g, '<br />'),
        receivedAt: Date.now()
    };
}

const fetchAboutMsg = function() {
    return dispatch => {
        dispatch(wrapDispatchAction('REQUEST_ABOUT_MSG'));
        return createRequest('/client/server/about', 'GET')
            //.then(response => response.json())
            .then(txt => dispatch(receiveAboutMsg(txt)));
    };
}

// contactMsg
function receiveContactMsg(txtData) {
    return {
        type: 'RECEIVE_CONTACT_MSG',
        data: txtData.replace(/(?:\r\n|\r|\n)/g, '<br />'),
        receivedAt: Date.now()
    };
}

const fetchContactMsg = function() {
    return dispatch => {
        dispatch(wrapDispatchAction('REQUEST_CONTACT_MSG'));
        return createRequest('http://localhost:9001/server/contact', 'GET')
            //.then(response => response.json())
            .then(txt => dispatch(receiveContactMsg(txt)));
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

const fetchUsernameMsg = function() {
    return dispatch => {
        dispatch(wrapDispatchAction('REQUEST_USERNAME_MSG'));
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

const fetchServerMsg = function() {
    return dispatch => {
        dispatch(wrapDispatchAction('REQUEST_SERVER_MSG'));
        return createRequest('/client/server/hellouser', 'GET')
            //.then(response => response.json())
            .then(json => dispatch(receiveServerMsg(json)));
    };
}

export { fetchAboutMsg, fetchContactMsg, fetchUsernameMsg, fetchServerMsg };
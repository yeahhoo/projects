import { combineReducers } from 'redux'

function menuReducer(state, action) {

    if (typeof state === 'undefined') {
        console.log('Reading server config from: #jsonResponse');
        let serverJson = JSON.parse(document.getElementById('jsonResponse').innerHTML);
        return {isLogined: serverJson.isLogined, username: serverJson.username, helloMsg: '', corsMsg: '', serverMsg: '', usernameMsg: '', isFetching: false, didInvalidate: false};
    }

    var requestState = Object.assign({}, state, {
        isFetching: true,
        didInvalidate: false
    });
    var responseState = Object.assign({}, state, {
        isFetching: false,
        didInvalidate: false
    });
    switch (action.type) {
        case 'HELLO_MSG':
            return requestState;
        case 'RECEIVE_HELLO_MSG':
            return Object.assign({}, responseState, {helloMsg: action.data});
        case 'CORS_MSG':
            return requestState;
        case 'RECEIVE_CORS_MSG':
            return Object.assign({}, responseState, {corsMsg: action.data});
        case 'USERNAME_MSG':
            return requestState;
        case 'RECEIVE_USERNAME_MSG':
            return Object.assign({}, responseState, {usernameMsg: action.data});
        case 'SERVER_MSG':
            return requestState;
        case 'RECEIVE_SERVER_MSG':
            return Object.assign({}, responseState, {serverMsg: action.data});
        default:
            return state
    }
}

const appReducer = combineReducers({menuReducer})

export default appReducer
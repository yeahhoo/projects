function menuReducer(state, action) {

    if (typeof state === 'undefined') {
        console.log('Reading server config from: #jsonResponse');
        let serverJson = JSON.parse(document.getElementById('jsonResponse').innerHTML);
        return {isLogined: serverJson.isLogined, username: serverJson.username, helloMsg: '', corsMsg: '', serverMsg: '', usernameMsg: ''};
    }

    switch (action.type) {
        case 'HELLO_MSG':
            return state;
        case 'RECEIVE_HELLO_MSG':
            return Object.assign({}, state, {helloMsg: action.data});
        case 'CORS_MSG':
            return state;
        case 'RECEIVE_CORS_MSG':
            return Object.assign({}, state, {corsMsg: action.data});
        case 'USERNAME_MSG':
            return state;
        case 'RECEIVE_USERNAME_MSG':
            return Object.assign({}, state, {usernameMsg: action.data});
        case 'SERVER_MSG':
            return state;
        case 'RECEIVE_SERVER_MSG':
            return Object.assign({}, state, {serverMsg: action.data});
        default:
            return state
    }
}

export default menuReducer
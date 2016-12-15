function menuReducer(state, action) {

    if (typeof state === 'undefined') {
        console.log('Reading server config from: #jsonResponse');
        let serverJson = JSON.parse(document.getElementById('jsonResponse').innerHTML);
        return {isLogined: serverJson.isLogined, username: serverJson.username, aboutMsg: '', contactMsg: '', serverMsg: '', usernameMsg: ''};
    }

    switch (action.type) {
        case 'REQUEST_ABOUT_MSG':
        case 'REQUEST_CONTACT_MSG':
        case 'REQUEST_USERNAME_MSG':
        case 'REQUEST_SERVER_MSG':
            return state;
        case 'RECEIVE_ABOUT_MSG':
            return Object.assign({}, state, {aboutMsg: action.data});
        case 'RECEIVE_CONTACT_MSG':
            return Object.assign({}, state, {contactMsg: action.data});
        case 'RECEIVE_USERNAME_MSG':
            return Object.assign({}, state, {usernameMsg: action.data});
        case 'RECEIVE_SERVER_MSG':
            return Object.assign({}, state, {serverMsg: action.data});
        default:
            return state
    }
}

export default menuReducer
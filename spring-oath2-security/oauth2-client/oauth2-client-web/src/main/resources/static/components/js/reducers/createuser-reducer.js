function userCreatingReducer(state, action) {

    if (typeof state === 'undefined') {
        return {user: '', password: '', isUserCreating: false};
    }

    var requestState = Object.assign({}, state, {isUserCreating: true});
    var responseState = Object.assign({}, state, {isUserCreating: false});
    switch (action.type) {
        case 'CREATE_USER_REQUEST':
            return requestState;
        case 'USER_CREATE_FAILED':
            return responseState;
        case 'RECEIVE_USER_CREATE_RESPONSE':
            return Object.assign({}, responseState, {user: '', password: ''});
        default:
            return state
    }
}

export default userCreatingReducer
function clientCreatingReducer(state, action) {

    if (typeof state === 'undefined') {
        return {client: '', clientSecret: '', authorizedGrantTypes: '', scopes: '', isClientCreating: false, isExceptionFetching: false, isLogined: false};
    }

    switch (action.type) {
        case 'CREATE_CLIENT_REQUEST':
            return Object.assign({}, state, {isClientCreating: true});
        case 'CREATE_CLIENT_CREATE_EXCEPTION_REQUEST':
            return Object.assign({}, state, {isExceptionFetching: true});
        case 'CLIENT_CREATE_FAILED':
            return Object.assign({}, state, {isClientCreating: false});
        case 'CLIENT_CREATE_EXCEPTION_FAILED':
            return Object.assign({}, state, {isExceptionFetching: false});
        case 'RECEIVE_CLIENT_CREATE_RESPONSE':
            return Object.assign({}, state, {client: '', clientSecret: '', authorizedGrantTypes: '', scopes: '', isClientCreating: false});
        default:
            return state
    }
}

export default clientCreatingReducer
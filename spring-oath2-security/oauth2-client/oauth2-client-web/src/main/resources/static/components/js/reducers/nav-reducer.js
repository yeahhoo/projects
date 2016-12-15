function navReducer(state, action) {

    if (typeof state === 'undefined') {
        return {activeComponent: 'SHOW_HOME'};
    }

    switch (action.type) {
        case 'SHOW_HOME':
            return Object.assign({}, {activeComponent: 'SHOW_HOME'});
        case 'SHOW_CREATE_USER_FORM':
            return Object.assign({}, {activeComponent: 'SHOW_CREATE_USER_FORM'});
        case 'SHOW_CREATE_CLIENT_FORM':
            return Object.assign({}, {activeComponent: 'SHOW_CREATE_CLIENT_FORM'});
        case 'SHOW_ERROR_PAGE':
            return Object.assign({}, {activeComponent: 'SHOW_ERROR_PAGE'});
        default:
            return state;
    }
}

export default navReducer
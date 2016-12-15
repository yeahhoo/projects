const dispatchMap = {
    createClient: 'SHOW_CREATE_CLIENT_FORM',
    createUser: 'SHOW_CREATE_USER_FORM',
    home: 'SHOW_HOME',
    error: 'SHOW_ERROR_PAGE',
    DEFAULT: 'SHOW_HOME'
};

function findAppView(serverArg) {
    var view = dispatchMap[serverArg];
    if (!view) {
        return dispatchMap.DEFAULT;
    }
    return view;
}

function findFirstView() {
    let serverJson = JSON.parse(document.getElementById('jsonResponse').innerHTML);
    return findAppView(serverJson.viewName);
}

export { findAppView, findFirstView };
(function() {

    'use strict';

    // todo replace it with standard function
    function thunkMiddleware(store) {
        return function(next) {
            return function(action) {
                if (typeof action === "function") {
                    return action(store.dispatch, store.getState);
                } else {
                    return next(action);
                }
            }
        }
    }

    let store = Redux.applyMiddleware(thunkMiddleware)(Redux.createStore)(appReducer);

    ReactDOM.render(
        <ReactRedux.Provider store={store}>
            <App />
        </ReactRedux.Provider>,
        document.getElementById('react-index-container')
    );

})();
import React from 'react'
import { render } from 'react-dom'
import { createStore, applyMiddleware, combineReducers } from 'redux'
import { Provider } from 'react-redux'
import thunk from 'redux-thunk'
import menuReducer from './reducers/menu-reducer'
import userCreatingReducer from './reducers/createuser-reducer'
import clientCreatingReducer from './reducers/createclient-reducer'
import navReducer from './reducers/nav-reducer'
import { findFirstView } from './server-client-dispatch-map'
import App from './react-app'

let appReducer = combineReducers({navReducer, menuReducer, userCreatingReducer, clientCreatingReducer});
let store = applyMiddleware(thunk)(createStore)(appReducer);

render(
    <Provider store={store}>
        <App />
    </Provider>,
    document.getElementById('react-index-container')
)

store.dispatch({type: findFirstView()});
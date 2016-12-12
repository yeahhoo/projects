import React from 'react'
import { render } from 'react-dom'
import { createStore, applyMiddleware } from 'redux'
import { Provider } from 'react-redux'
import thunk from 'redux-thunk'
import appReducer from './reducers/nav-reducer'
import App from './react-app'

let store = applyMiddleware(thunk)(createStore)(appReducer);

render(
    <Provider store={store}>
        <App />
    </Provider>,
    document.getElementById('react-index-container')
)
import React, { Component } from 'react';
import NavigationMenu from './containers/nav-container'
import WorkArea from './containers/workarea-container'

class App extends Component {

    render() {
        return (
            <div>
                <NavigationMenu />
                <WorkArea />
            </div>
        );
    }
}

export default App
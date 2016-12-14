import React, { Component } from 'react';
import NavigationMenu from './containers/menu-container'
import WorkArea from './containers/nav-container'

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
import { connect } from 'react-redux';
import NavigationMenuComponent from '../react-nav-component';

const mapNavigationStateToProps = (state/*, ownProps*/) => {

    'use strict';
    return {
        isLogined: state.menuReducer.isLogined,
        username: state.menuReducer.username
    };
};
    /*
    const mapDispatchToProps = (dispatch, ownProps) => {
        return {
            getUsername: () => {
                dispatch({ type: 'USERNAME_MSG' })
            },
            getHelloMsg: () => {
                dispatch({ type: 'HELLO_MSG' })
            },
            getServerMsg: () => {
                dispatch({ type: 'SERVER_MSG' })
            },
            getCorsMsg: () => {
                dispatch({ type: 'CORS_MSG' })
            }
        }
    }
    */
const NavigationMenu = connect(mapNavigationStateToProps/*, mapDispatchToProps*/) (NavigationMenuComponent);
export default NavigationMenu;





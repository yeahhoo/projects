import { connect } from 'react-redux';
import WorkAreaComponent from '../react-workarea-component';

const mapNavigationStateToProps = (state) => {
    // resetting values if it's not home page
    if (state.navReducer.activeComponent === 'SHOW_ABOUT_PAGE') {
        state.menuReducer.contactMsg = '';
        state.menuReducer.serverMsg = '';
        state.menuReducer.usernameMsg = '';
    } else if (state.navReducer.activeComponent === 'SHOW_CONTACT_PAGE') {
        state.menuReducer.aboutMsg = '';
        state.menuReducer.serverMsg = '';
        state.menuReducer.usernameMsg = '';
    } else if (state.navReducer.activeComponent === 'SHOW_HOME') {
        state.menuReducer.aboutMsg = '';
        state.menuReducer.contactMsg = '';
    } else {
        state.menuReducer.aboutMsg = '';
        state.menuReducer.contactMsg = '';
        state.menuReducer.serverMsg = '';
        state.menuReducer.usernameMsg = '';
    }
    return {
        activeComponent: state.navReducer.activeComponent
    };
};

const WorkArea = connect(mapNavigationStateToProps) (WorkAreaComponent);
export default WorkArea;

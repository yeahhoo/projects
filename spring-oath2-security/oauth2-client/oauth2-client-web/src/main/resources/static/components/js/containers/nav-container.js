import { connect } from 'react-redux';
import WorkAreaComponent from '../react-workarea-component';

const mapNavigationStateToProps = (state) => {
    // resetting values if it's not home page
    if (state.navReducer.activeComponent !== 'SHOW_HOME') {
        state.menuReducer.helloMsg = '';
        state.menuReducer.corsMsg = '';
        state.menuReducer.serverMsg = '';
        state.menuReducer.usernameMsg = '';
    }
    return {
        activeComponent: state.navReducer.activeComponent
    };
};

const WorkArea = connect(mapNavigationStateToProps) (WorkAreaComponent);
export default WorkArea;

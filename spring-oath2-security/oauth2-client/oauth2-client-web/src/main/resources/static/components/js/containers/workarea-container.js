import { connect } from 'react-redux';
import WorkAreaComponent from '../react-workarea-component';

const mapWorkareaStateToProps = (state/*, ownProps*/) => {
    return {
        helloMsg: state.menuReducer.helloMsg,
        corsMsg: state.menuReducer.corsMsg,
        serverMsg: state.menuReducer.serverMsg,
        usernameMsg: state.menuReducer.usernameMsg
    }
};

const WorkArea = connect(mapWorkareaStateToProps) (WorkAreaComponent);
export default WorkArea;
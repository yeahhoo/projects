import { connect } from 'react-redux';
import RestResultFormComponent from '../react-rest-result-form-component';

const mapResultFormStateToProps = (state) => {
    return {
        aboutMsg: state.menuReducer.aboutMsg,
        contactMsg: state.menuReducer.contactMsg,
        serverMsg: state.menuReducer.serverMsg,
        usernameMsg: state.menuReducer.usernameMsg
    }
};

const RestResultForm = connect(mapResultFormStateToProps) (RestResultFormComponent);
export default RestResultForm;
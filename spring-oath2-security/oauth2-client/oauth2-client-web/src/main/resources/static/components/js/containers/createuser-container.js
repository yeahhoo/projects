import { connect } from 'react-redux';
import CreateUserComponent from '../react-create-user-component';

const mapCreateUserStateToProps = (state) => {

    return {
        user: state.userCreatingReducer.user,
        password: state.userCreatingReducer.password,
        isUserCreating: state.userCreatingReducer.isUserCreating
    };
};

const CreateUserForm = connect(mapCreateUserStateToProps) (CreateUserComponent);
export default CreateUserForm;

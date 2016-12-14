import { connect } from 'react-redux';
import CreateClientComponent from '../react-create-client-component';

const mapCreateClientStateToProps = (state) => {

    return {
        client: state.clientCreatingReducer.client,
        clientSecret: state.clientCreatingReducer.clientSecret,
        authorizedGrantTypes: state.clientCreatingReducer.authorizedGrantTypes,
        scopes: state.clientCreatingReducer.scopes,
        isClientCreating: state.clientCreatingReducer.isClientCreating,
        isExceptionFetching: state.clientCreatingReducer.isExceptionFetching,
        isLogined: state.menuReducer.isLogined
    };
};

const CreateClientForm = connect(mapCreateClientStateToProps) (CreateClientComponent);
export default CreateClientForm;

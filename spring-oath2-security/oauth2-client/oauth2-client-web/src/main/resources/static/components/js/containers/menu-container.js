import { connect } from 'react-redux';
import NavigationMenuComponent from '../react-nav-component';

const mapMenuStateToProps = (state) => {

    return {
        isLogined: state.menuReducer.isLogined,
        username: state.menuReducer.username
    };

};

const NavigationMenu = connect(mapMenuStateToProps) (NavigationMenuComponent);
export default NavigationMenu;





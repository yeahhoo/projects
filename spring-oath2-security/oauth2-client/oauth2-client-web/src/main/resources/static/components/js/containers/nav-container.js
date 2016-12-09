

    const mapNavigationStateToProps = (state/*, ownProps*/) => {
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
    /* jshint unused:false */
    const NavigationMenu = ReactRedux.connect(mapNavigationStateToProps/*, mapDispatchToProps*/) (NavigationMenuComponent);

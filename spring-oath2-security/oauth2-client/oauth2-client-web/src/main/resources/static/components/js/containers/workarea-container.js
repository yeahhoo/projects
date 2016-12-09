const mapWorkareaStateToProps = (state, ownProps) => {
    return {
        helloMsg: state.menuReducer.helloMsg,
        corsMsg: state.menuReducer.corsMsg,
        serverMsg: state.menuReducer.serverMsg,
        usernameMsg: state.menuReducer.usernameMsg
    }
}

const WorkArea = ReactRedux.connect(mapWorkareaStateToProps) (WorkAreaComponent)
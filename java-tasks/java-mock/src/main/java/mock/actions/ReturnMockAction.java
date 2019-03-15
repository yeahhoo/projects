package mock.actions;

/** Action for returning mocked value. */
public final class ReturnMockAction implements MockAction {

    private final Object returnValue;

    public ReturnMockAction(Object returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public Object performAction() {
        return returnValue;
    }
}

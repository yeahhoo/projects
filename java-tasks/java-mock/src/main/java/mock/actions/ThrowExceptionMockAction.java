package mock.actions;

/** Action for throwing exceptions. */
public final class ThrowExceptionMockAction implements MockAction {

    private final Throwable exception;

    public ThrowExceptionMockAction(Throwable exception) {
        this.exception = exception;
    }

    @Override
    public Object performAction() throws Throwable  {
        throw exception;
    }
}

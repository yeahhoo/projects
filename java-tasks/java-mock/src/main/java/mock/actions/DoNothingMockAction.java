package mock.actions;

/** Simply NO_OP implementation. */
public final class DoNothingMockAction implements MockAction {

    @Override
    public Object performAction() {
        return new Object();
    }
}

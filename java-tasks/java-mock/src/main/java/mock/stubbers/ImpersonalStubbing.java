package mock.stubbers;

import mock.actions.DoNothingMockAction;
import mock.actions.ThrowExceptionMockAction;
import mock.utils.MockCreator;

/** Implementation of {@link Stubbing} for methods which don't return values. */
public final class ImpersonalStubbing implements Stubbing {

    private final Runnable action;

    public ImpersonalStubbing(Runnable action) {
        this.action = action;
    }

    /** Finishes mock flow with NO_OP. */
    public void thenDoNothing() {
        MockCreator.registerMockAction(action, new DoNothingMockAction());
    }

    @Override
    public <T extends Throwable> void thenThrowException(T exception) {
        MockCreator.registerMockAction(action, new ThrowExceptionMockAction(exception));
    }
}

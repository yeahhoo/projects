package mock.stubbers;

import mock.actions.DoNothingMockAction;
import mock.actions.ThrowExceptionMockAction;
import mock.utils.MockCreatorDelegator;

/** Implementation of {@link Stubbing} for methods which don't return values. */
public final class ImpersonalStubbing implements Stubbing {

    private final RunnableWithException action;

    public ImpersonalStubbing(RunnableWithException action) {
        this.action = action;
    }

    /** Finishes mock flow with NO_OP. */
    public void thenDoNothing() {
        MockCreatorDelegator.registerMockAction(action, new DoNothingMockAction());
    }

    @Override
    public <T extends Throwable> void thenThrowException(T exception) {
        MockCreatorDelegator.registerMockAction(action, new ThrowExceptionMockAction(exception));
    }
}

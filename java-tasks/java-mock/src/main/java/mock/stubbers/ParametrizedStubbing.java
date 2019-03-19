package mock.stubbers;

import mock.actions.ReturnMockAction;
import mock.actions.ThrowExceptionMockAction;
import mock.utils.MockCreatorDelegator;

/** Implementation of {@link Stubbing} for methods which return values. */
public final class ParametrizedStubbing<T> implements Stubbing {

    private final SupplierWithException<T> action;

    public ParametrizedStubbing(SupplierWithException<T> action) {
        this.action = action;
    }

    /** Finish mocking flow with returning given value. */
    public void thenReturn(T value) {
        MockCreatorDelegator.registerMockAction(() -> action.get(), new ReturnMockAction(value));
    }

    @Override
    public <T extends Throwable> void thenThrowException(T exception) {
        MockCreatorDelegator.registerMockAction(() -> action.get(), new ThrowExceptionMockAction(exception));
    }
}

package mock.stubbers;

import mock.actions.ReturnMockAction;
import mock.actions.ThrowExceptionMockAction;
import mock.utils.MockCreator;

import java.util.function.Supplier;

/** Implementation of {@link Stubbing} for methods which return values. */
public final class ParametrizedStubbing<T> implements Stubbing {

    private final Supplier<T> action;

    public ParametrizedStubbing(Supplier<T> action) {
        this.action = action;
    }

    /** Finish mocking flow with returning given value. */
    public void thenReturn(T value) {
        MockCreator.registerMockAction(() -> action.get(), new ReturnMockAction(value));
    }

    @Override
    public <T extends Throwable> void thenThrowException(T exception) {
        MockCreator.registerMockAction(() -> action.get(), new ThrowExceptionMockAction(exception));
    }
}

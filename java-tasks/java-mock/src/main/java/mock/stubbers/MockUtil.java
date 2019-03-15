package mock.stubbers;

import java.util.function.Supplier;

/** Starts mocking flow. */
public class MockUtil {

    /** Starts stubbing flow for methods which don't return values. */
    public static ImpersonalStubbing when(Runnable action) {
        return new ImpersonalStubbing(action);
    }

    /** Starts stubbing flow for methods which return values. */
    public static <T> ParametrizedStubbing<T> when(Supplier<T> action) {
        return new ParametrizedStubbing<>(action);
    }
}

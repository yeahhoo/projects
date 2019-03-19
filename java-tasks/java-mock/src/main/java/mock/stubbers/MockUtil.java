package mock.stubbers;

/** Starts mocking flow. */
public class MockUtil {

    /** Starts stubbing flow for methods which don't return values. */
    public static ImpersonalStubbing when(RunnableWithException action) {
        return new ImpersonalStubbing(action);
    }

    /** Starts stubbing flow for methods which return values. */
    public static <T> ParametrizedStubbing<T> when(SupplierWithException<T> action) {
        return new ParametrizedStubbing<>(action);
    }
}

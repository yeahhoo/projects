package mock.utils;

import mock.actions.MockAction;

/** Delegates methods calls to {@link MockCreator} to keep API of the latter clean. */
public class MockCreatorDelegator {

    /** Simple object to be returned when mocking static method. */
    public static final Object PROCEED = new Object();

    /** Delegates static calls from {@link mock.utils.MockActionClassLoader}. */
    public static Object proxyStaticMethodCall(Object mock, String methodName, String returnType, Object[] args) throws Throwable {
        return MockCreator.proxyStaticMethodCall(mock, methodName, returnType, args);
    }

    /** Delegates static calls to {@link MockCreator#registerMockAction} */
    public static void registerMockAction(Runnable action, MockAction response) {
        MockCreator.registerMockAction(action, response);
    }
}

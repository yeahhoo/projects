package mock.utils;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;
import mock.actions.MockAction;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;

/** Does mocking routine like creating mocks, registering new actions and returning mocked values. */
public class MockCreator {

    // trigger that distinguishes when we need register mock invocation and when we just need to return mocked result
    private static MODE mode = MODE.VIEW;
    // action to mock in EDIT mode
    private static MockAction mockAction = null;

    /** Creates new mock. */
    public static <T> T createMock(Class<T> clazz) throws Exception {
        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(clazz);
        Class enhancedClazz = factory.createClass();
        MethodHandler handler = new MethodHandler() {
            @Override
            public Object invoke(Object mock, Method method, Method proxy, Object[] args) throws Throwable {
                return proxyMethodCall(mock, method.getName(), proxy, args);
            }
        };
        Object instance = ObjectFactory.createInstance(enhancedClazz);
        ((ProxyObject) instance).setHandler(handler);
        MockCache.putNewObject(instance);
        return (T) instance;
    }

    private static Object proxyMethodCall(Object mock, String methodName, Method proxy, Object[] args) throws Throwable {
        if ("equals".equals(methodName) ||
            "hashCode".equals(methodName) ||
            "getClass".equals(methodName) ||
            "toString".equals(methodName)) {
            return proxy.invoke(mock, args);
        }

        MockCache.MockState state = MockCache.getState(mock);
        if (mode == MODE.EDIT) {
            return recordMock(state, methodName, new ArgKey(args), null);
        } else {
            MockAction returnValue = state.getResponse(methodName, new ArgKey(args));
            // no action mocked - calling real method
            return (returnValue == null) ? proxy.invoke(mock, args) : returnValue.performAction();
        }
    }

    /** Mocks class with static methods. */
    public static void mockStatic(Class clazz) {
        MockCache.putNewObject(clazz.getCanonicalName());
    }

    public static final Object PROCEED = new Object();

    /**
     * Used for mocking static methods while class loading class via {@link MyMockClassLoader}.
     * Don't call this method outside.
     */
    public static Object proxyStaticMethodCall(Object mock, String methodName, String returnType, Object[] args) throws Throwable {
        MockCache.MockState state = MockCache.getState(mock);
        if (mode == MODE.EDIT) {
            return recordMock(state, methodName, new ArgKey(args), PROCEED);
        } else {
            MockAction returnValue = state.getResponse(methodName, new ArgKey(args));
            return (returnValue == null) ? callRealMethod(mock.toString(), methodName, args) : returnValue.performAction();
        }
    }

    private static Object callRealMethod(String className, String methodName, Object[] args) throws Exception {
        return MethodUtils.invokeStaticMethod(Class.forName(className), methodName, args);
    }

    /** Registers new mock action. */
    public static void registerMockAction(Runnable action, MockAction response) {
        mode = MODE.EDIT;
        mockAction = response;
        action.run();
        mode = MODE.VIEW;
    }

    private static Object recordMock(MockCache.MockState state, String methodName, ArgKey key, Object returnValue) {
        if (mockAction == null) {
            throw new IllegalArgumentException("Wrong API usage, perhaps several calls of mocks were made in one action");
        }
        state.addResponse(methodName, key, mockAction);
        // drain mock action
        mockAction = null;
        return returnValue;
    }

    private enum MODE {
        VIEW, EDIT
    }
}

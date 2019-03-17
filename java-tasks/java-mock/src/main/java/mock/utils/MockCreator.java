package mock.utils;

import mock.actions.MockAction;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static net.bytebuddy.matcher.ElementMatchers.any;

/** Does mocking routine like creating mocks, registering new actions and returning mocked values. */
public class MockCreator {

    // trigger that distinguishes when we need register mock invocation and when we just need to return mocked result
    private static MODE mode = MODE.VIEW;
    // action to mock in EDIT mode
    private static MockAction mockAction = null;

    /** Creates new mock. */
    public static <T> T createMock(Class<T> clazz) {
        Class createdClass = new ByteBuddy()
                .subclass(clazz)
                .method(any())
                .intercept(MethodDelegation.to(new MethodInterceptor()))
                //.method(ElementMatchers.named("hashCode"))
                //.intercept(FixedValue.value(new AtomicInteger(0).getAndIncrement()))
                .make()
                .load(clazz.getClassLoader())
                .getLoaded();
        T instance = (T) ObjectFactory.createInstance(createdClass);
        MockCache.putNewObject(getMockId(instance));
        return instance;
    }

    /** Intercepts method invocations and proxies them. */
    public static class MethodInterceptor {

        /**
         * Proxies method call.
         *
         * @param mock        mocked object
         * @param realMethod  forwards invocation to the real method of the mocked object
         * @param proxyMethod being called in this iteration
         * @param args        supplied to the method
         */
        @RuntimeType
        public Object delegate(@This Object mock, @SuperCall Callable<Object> realMethod,
                               @Origin Method proxyMethod, @AllArguments Object... args) throws Throwable {
            if ("getClass".equals(proxyMethod.getName())) {
                return realMethod.call();
            }

            MockCache.MockState state = MockCache.getState(getMockId(mock));
            if (mode == MODE.EDIT) {
                Object returnValue = findReturnType(mock, proxyMethod.getName(), args);
                return recordMock(state, proxyMethod.getName(), new ArgKey(args), returnValue);
            } else {
                MockAction returnValue = state.getResponse(proxyMethod.getName(), new ArgKey(args));
                // no action mocked - calling real method
                return (returnValue == null) ? realMethod.call() : returnValue.performAction();
            }
        }
    }

    /** Mocks class with static methods. */
    public static void mockStatic(Class clazz) {
        MockCache.putNewObject(clazz.getCanonicalName());
    }

    /**
     * Used for mocking static methods while class loading class via {@link MockActionClassLoader}.
     * Don't call this method outside.
     */
    static Object proxyStaticMethodCall(Object mock, String methodName, String returnType, Object[] args) throws Throwable {
        MockCache.MockState state = MockCache.getState(mock.toString());
        if (mode == MODE.EDIT) {
            return recordMock(state, methodName, new ArgKey(args), MockCreatorDelegator.PROCEED);
        } else {
            MockAction returnValue = state.getResponse(methodName, new ArgKey(args));
            return (returnValue == null) ? callRealMethod(mock.toString(), methodName, args) : returnValue.performAction();
        }
    }

    private static String getMockId(Object mock) {
        // we rely on the fact that ByteBuddy generates uniques class names when mock is created.
        return mock.getClass().getName();
    }

    private static Object findReturnType(Object mock, String methodName, Object[] args) {
        List<Class<?>> list = Arrays.stream(args).map(o -> o.getClass()).collect(Collectors.toList());
        Class[] types = list.toArray(new Class[list.size()]);
        Method method = Arrays.stream(mock.getClass().getDeclaredMethods()).filter(m ->
                m.getName().equals(methodName) && Arrays.equals(m.getParameterTypes(), types)).findFirst().get();
        return ObjectFactory.createInstance(method.getReturnType());
    }

    private static Object callRealMethod(String className, String methodName, Object[] args) throws Exception {
        return MethodUtils.invokeStaticMethod(Class.forName(className), methodName, args);
    }

    /** Registers new mock action. */
    static void registerMockAction(Runnable action, MockAction response) {
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

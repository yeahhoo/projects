package mock.utils;

import mock.states.DoNothingState;

import java.lang.reflect.Method;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

/**
 * @author Aleksandr_Savchenko
 */
public class MockCreator {

    public static <T> T createMock(Class<T> clazz) throws Exception {
        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(clazz);
        Class enhancedClazz = factory.createClass();
        MethodHandler handler = new MethodHandler() {
            @Override
            public Object invoke(Object mock, Method method, Method proxy, Object[] args) throws Throwable {
                return proxyMethodCall(mock, method.getName(), method.getReturnType(), proxy, args);
            }
        };
        Object instance = enhancedClazz.newInstance();
        ((ProxyObject) instance).setHandler(handler);
        MockCache.putNewObject(instance);
        return (T) instance;
    }

    public static Object proxyMethodCall(Object mock, String methodName, Class<?> returnType, Method proxy, Object[] args) throws Throwable {
        if ("equals".equals(methodName) ||
            "hashCode".equals(methodName) ||
            "getClass".equals(methodName) ||
            "toString".equals(methodName)) {
            return proxy.invoke(mock, args);
        }

        ArgKey key = new ArgKey(args);

        MockCache.MockState state = MockCache.getState(mock);
        Object returnValue = state.getResponse(methodName, key);
        if (returnValue == null) {
            // first call -> try to find onAddingMockValue, if there isn't calling real method
            if (state.isCallbackStackEmpty()) {
                return proxy.invoke(mock, args);
            }
            Callback callback = state.popCallback();
            returnValue = callback.onAddingMockValue(methodName, key);
        }
        // do validation
        if (!state.isCallbackStackEmpty()) {
            throw new RuntimeException("Wrong API usage, probably when() doesn't entail method call");
        }
        if (returnValue == null) {
            return proxy.invoke(mock, args);
        } else if (DoNothingState.VOID.equals(returnValue)) {
            return null;
        } else if (returnValue instanceof Exception) {
            throw (Exception) returnValue;
        } else if (returnValue.getClass() != returnType) {
            throw new RuntimeException("Wrong API usage, probably when() doesn't entail method call");
        } else {
            return returnValue;
        }
    }

    public static void mockStatic(Class clazz) throws Exception {
        MockCache.putNewObject(clazz.getCanonicalName());
    }

    public static final Object PROCEED = new Object();

    public static Object proxyStaticMethodCall(Object mock, String methodName, String returnType, Object[] args) throws Throwable {
        ArgKey key = new ArgKey(args);
        MockCache.MockState state = MockCache.getState(mock);
        Object returnValue = state.getResponse(methodName, key);
        if (returnValue == null) {
            state.addResponse(methodName, key, ReturnValueHolder.getValue());
            ReturnValueHolder.remove();
            return PROCEED;
        } if (DoNothingState.VOID.equals(returnValue)) {
            return null;
        } else if (returnValue instanceof Exception) {
            throw (Exception) returnValue;
        } else if (returnValue.getClass().toString().equals(returnType)) {
            throw new RuntimeException("Wrong API usage, probably when() doesn't entail method call");
        } else {
            return returnValue;
        }
    }

}

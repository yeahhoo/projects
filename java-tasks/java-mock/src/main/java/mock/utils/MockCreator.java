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
                if ("equals".equals(method.getName()) ||
                    "hashCode".equals(method.getName()) ||
                    "getClass".equals(method.getName()) ||
                    "toString".equals(method.getName())) {
                    return proxy.invoke(mock, args);
                }

                String methodName = method.getName();
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
                } else if (returnValue.getClass() != method.getReturnType()) {
                    throw new RuntimeException("Wrong API usage, probably when() doesn't entail method call");
                } else {
                    return returnValue;
                }
            }
        };
        Object instance = enhancedClazz.newInstance();
        ((ProxyObject) instance).setHandler(handler);
        MockCache.putNewObject(instance);
        return (T) instance;
    }

}

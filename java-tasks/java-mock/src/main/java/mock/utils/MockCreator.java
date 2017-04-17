package mock.utils;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import mock.states.DoNothingState;

import java.lang.reflect.Method;

/**
 * @author Aleksandr_Savchenko
 */
public class MockCreator {

    public static <T> T createMock(Class clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object mock, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                if ("equals".equals(method.getName()) ||
                    "hashCode".equals(method.getName()) ||
                    "getClass".equals(method.getName()) ||
                    "toString".equals(method.getName())) {
                    return proxy.invokeSuper(mock, args);
                }

                String methodName = method.getName();
                ArgKey key = new ArgKey(args);

                MockCache.MockState state = MockCache.getState(mock);
                Object returnValue = state.getResponse(methodName, key);
                if (returnValue == null) {
                    // first call -> try to find onAddingMockValue, if there isn't calling real method
                    if (state.isCallbackStackEmpty()) {
                        return proxy.invokeSuper(mock, args);
                    }
                    Callback callback = state.popCallback();
                    returnValue = callback.onAddingMockValue(methodName, key);
                }
                // do validation
                if (!state.isCallbackStackEmpty()) {
                    throw new RuntimeException("Wrong API usage, probably when() doesn't entail method call");
                }
                if (returnValue == null) {
                    return proxy.invokeSuper(mock, args);
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
        });
        Object proxy =  enhancer.create();
        MockCache.putNewObject(proxy);
        return (T) proxy;
    }

}

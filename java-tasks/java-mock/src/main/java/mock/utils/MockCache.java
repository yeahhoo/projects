package mock.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author Aleksandr_Savchenko
 */
public class MockCache {

    private static Map<Object, MockState> mockStates = new HashMap<>();

    public static void putNewObject(Object mock) {
        mockStates.put(mock, new MockState());
    }

    public static MockState getState(Object mock) {
        return mockStates.get(mock);
    }

    public static void registerCallback(Object mock, Callback callback) {
        MockState mockState = mockStates.get(mock);
        mockState.pushCallback(callback);
    }

    public static class MockState {

        private Stack<Callback> callbackStack;
        // <methodName, <Request, Response>>
        private Map<String, Map<ArgKey, Object>> readyMap;

        public MockState() {
            readyMap = new HashMap<>();
            callbackStack = new Stack<>();
        }

        public Callback pushCallback(Callback callback) {
            return callbackStack.push(callback);
        }

        public Callback popCallback() {
            return callbackStack.pop();
        }

        public boolean isCallbackStackEmpty() {
            return callbackStack.isEmpty();
        }

        public Object getResponse(String methodName, ArgKey key) {
            Map<ArgKey, Object> map = readyMap.get(methodName);
            if (map != null) {
                return map.get(key);
            }
            return null;
        }

        public void addResponse(String methodName, ArgKey key, Object response) {
            Map<ArgKey, Object> map = readyMap.get(methodName);
            if (map == null) {
                map = new HashMap<>();
                readyMap.put(methodName, map);
            }
            map.put(key, response);
        }

    }


}

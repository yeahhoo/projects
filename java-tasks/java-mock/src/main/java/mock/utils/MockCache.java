package mock.utils;

import mock.actions.MockAction;

import java.util.HashMap;
import java.util.Map;

/** Contains recorded mapping for everything which was mocked. */
public final class MockCache {

    private static Map<Object, MockState> mockStates = new HashMap<>();

    /** Registers new mock for recording. */
    public static void putNewObject(String mock) {
        mockStates.put(mock, new MockState());
    }

    /** Returns recording for requested mock object. */
    public static MockState getState(String mock) {
        return mockStates.get(mock);
    }

    /** Contains recordings for particular mocked object. */
    public static class MockState {

        // <methodName, <Request, Response>>
        private final Map<String, Map<ArgKey, MockAction>> readyMap;

        public MockState() {
            readyMap = new HashMap<>();
        }

        /** Read recorded response. */
        public MockAction getResponse(String methodName, ArgKey key) {
            Map<ArgKey, MockAction> map = readyMap.get(methodName);
            if (map != null) {
                return map.get(key);
            }
            return null;
        }

        /** Record new response. */
        public void addResponse(String methodName, ArgKey key, MockAction response) {
            Map<ArgKey, MockAction> map = readyMap.get(methodName);
            if (map == null) {
                map = new HashMap<>();
                readyMap.put(methodName, map);
            }
            map.put(key, response);
        }
    }
}

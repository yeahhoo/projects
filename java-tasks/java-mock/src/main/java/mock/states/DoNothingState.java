package mock.states;

import mock.utils.ArgKey;
import mock.utils.MockCache;
import mock.utils.Callback;

/**
 * @author Aleksandr_Savchenko
 */
public class DoNothingState implements Statetable {

    public static final String VOID = "NoAction";

    @Override
    public void registerCallbackForMock(Object mockService) {
        // inject some flag meaning Void
        MockCache.registerCallback(mockService, new Callback() {
            @Override
            public Object onAddingMockValue(String methodName, ArgKey key) {
                // add value to map
                MockCache.MockState state = MockCache.getState(mockService);
                state.addResponse(methodName, key, VOID);
                return VOID;
            }
        });
    }

}

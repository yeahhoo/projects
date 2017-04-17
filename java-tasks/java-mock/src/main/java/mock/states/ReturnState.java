package mock.states;

import mock.utils.ArgKey;
import mock.utils.MockCache;
import mock.utils.Callback;

/**
 * @author Aleksandr_Savchenko
 */
public class ReturnState implements Statetable {

    private Object returnValue;

    public ReturnState(Object returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public void registerCallbackForMock(Object mockService) {
        MockCache.registerCallback(mockService, new Callback() {
            @Override
            public Object onAddingMockValue(String methodName, ArgKey key) {
                // add value to map
                MockCache.MockState state = MockCache.getState(mockService);
                state.addResponse(methodName, key, returnValue);
                return returnValue;
            }
        });
    }

}

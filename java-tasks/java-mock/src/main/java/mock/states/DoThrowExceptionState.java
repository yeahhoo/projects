package mock.states;

import mock.utils.ArgKey;
import mock.utils.Callback;
import mock.utils.MockCache;

/**
 * @author Aleksandr_Savchenko
 */
public class DoThrowExceptionState implements Statetable {

    private Exception exception;

    public DoThrowExceptionState(Exception exception) {
        this.exception = exception;
    }

    @Override
    public void registerCallbackForMock(Object mockService) {
        MockCache.registerCallback(mockService, new Callback() {
            @Override
            public Object onAddingMockValue(String methodName, ArgKey key) {
                // add value to map
                MockCache.MockState state = MockCache.getState(mockService);
                state.addResponse(methodName, key, exception);
                return DoNothingState.VOID;
            }
        });
    }

}

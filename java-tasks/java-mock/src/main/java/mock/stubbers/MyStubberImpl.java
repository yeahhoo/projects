package mock.stubbers;

import mock.states.Statetable;
import mock.stubbers.MyStubber;

/**
 * @author Aleksandr_Savchenko
 */
public class MyStubberImpl implements MyStubber {

    private Statetable state;

    public MyStubberImpl(Statetable state) {
        this.state = state;
    }

    public <T> T when(T t) {
        state.registerCallbackForMock(t);
        return t;
    }

}

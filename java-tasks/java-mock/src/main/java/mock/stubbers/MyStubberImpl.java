package mock.stubbers;

import mock.states.Statetable;

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

    @Override
    public <T> void whenStatic(T t) {
        // just markable method where static call takes place
    }

}

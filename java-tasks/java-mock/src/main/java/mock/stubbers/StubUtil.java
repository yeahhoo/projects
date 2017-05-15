package mock.stubbers;

import mock.states.DoNothingState;
import mock.states.DoThrowExceptionState;
import mock.states.ReturnState;
import mock.utils.ReturnValueHolder;

/**
 * @author Aleksandr_Savchenko
 */
public class StubUtil {

    public static MyStubber doNothing() {
        MyStubber stubber = new MyStubberImpl(new DoNothingState());
        return stubber;
    }

    public static MyStubber doReturn(Object o) {
        MyStubber stubber = new MyStubberImpl(new ReturnState(o));
        return stubber;
    }

    public static MyStubber doThrow(Exception e) {
        MyStubber stubber = new MyStubberImpl(new DoThrowExceptionState(e));
        return stubber;
    }

    public static MyStubber doStaticReturn(Object o) {
        // no need state here because call of methods 'overtakes' stage of getting mock object
        // (it comes together with method call as it's static)
        MyStubber stubber = new MyStubberImpl(null);
        if (!ReturnValueHolder.setCleanValue(o)) {
            throw new RuntimeException("Wrong API usage, probably doStaticReturn() doesn't entail method call");
        };
        return stubber;
    }

    public static MyStubber doStaticThrow(Exception o) {
        return doStaticReturn(o);
    }

    public static MyStubber doStaticNothing() {
        return doStaticReturn(DoNothingState.VOID);
    }

}

package mock.stubbers;

import mock.states.DoNothingState;
import mock.states.DoThrowExceptionState;
import mock.states.ReturnState;
import mock.stubbers.MyStubber;
import mock.stubbers.MyStubberImpl;

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

}

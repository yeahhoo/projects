package mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import mock.stubbers.StubUtil;
import mock.utils.MockCreator;


/**
 * @author Aleksandr_Savchenko
 */
public class MyTest {

    private MyClass myClass;

    @Before
    public void setUp() {
        myClass = MockCreator.createMock(MyClass.class);
        StubUtil.doReturn("a").when(myClass).method1("b");
        StubUtil.doNothing().when(myClass).method1("c");
        StubUtil.doReturn(4).when(myClass).method2(2, 1);
        StubUtil.doThrow(new IllegalArgumentException("why not")).when(myClass).method1("error");
        //StubUtil.doReturn(2).when(myClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testException() {
        myClass.method1("error"); // test exception
    }

    @Test
    public void testMyClass() {
        assertEquals("a", myClass.method1("b")); // test mock
        assertNull(myClass.method1("c")); // test mock
        assertTrue(4 == myClass.method2(2, 1)); // test mock
        assertTrue(8 == myClass.method2(4, 4));  // test real
        assertEquals("hey: yo", myClass.method1("yo")); // test real
    }

}

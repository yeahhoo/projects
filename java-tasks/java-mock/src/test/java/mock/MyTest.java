package mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mock.annotations.PrepareFinalClassMock;
import mock.stubbers.StubUtil;
import mock.utils.MockCreator;
import mock.utils.MyMockJUnitRunner;

/**
 *
 * Test shows the basic idea of how powermock works on example of the below feature:
 * <pre>
 *    {@literal @}PrepareForTest(MyFinalClass.class)
 *    {@literal @}RunWith(PowerMockRunner.class)
 *    ...
 *    // https://dzone.com/articles/mock-final-class#mock-final-class
 *    MyClass mock = PowerMockito.mock(MyClass.class);
 *    Mockito.when(mock.method1("ee")).thenReturn("works");
 *
 * </pre>
 *
 * @author Aleksandr_Savchenko
 */
@RunWith(MyMockJUnitRunner.class)
@PrepareFinalClassMock(classes = {MyFinalClass.class})
public class MyTest {

    private MyFinalClass myFinalClass;
    private MyClass myClass;

    @Before
    public void setUp() throws Exception {
        myClass = MockCreator.createMock(MyClass.class);
        StubUtil.doReturn("mock").when(myClass).method1("something");

        myFinalClass = MockCreator.createMock(MyFinalClass.class);
        StubUtil.doReturn("a").when(myFinalClass).method1("b");
        StubUtil.doNothing().when(myFinalClass).method1("c");
        StubUtil.doReturn(4).when(myFinalClass).method2(2, 1);
        StubUtil.doThrow(new IllegalArgumentException("why not")).when(myFinalClass).method1("error");
        //StubUtil.doReturn(2).when(myFinalClass);
    }

    @Test
    public void testClass() {
        assertEquals("mock", myClass.method1("something")); // test mock
        assertNotEquals("mock", myClass.method1("everything")); // // test real
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFinalException() {
        myFinalClass.method1("error"); // test exception
    }

    @Test
    public void testMyFinalClass() {
        assertEquals("a", myFinalClass.method1("b")); // test mock
        assertNull(myFinalClass.method1("c")); // test mock
        assertTrue(4 == myFinalClass.method2(2, 1)); // test mock
        assertTrue(8 == myFinalClass.method2(4, 4));  // test real
        assertEquals("hey: yo", myFinalClass.method1("yo")); // test real
    }

}

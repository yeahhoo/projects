package mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import mock.stubbers.MockUtil;
import org.junit.Test;
import org.junit.runner.RunWith;

import mock.annotations.PrepareFinalClassMock;
import mock.utils.MockCreator;
import mock.utils.MyMockJUnitRunner;

/** Test demonstrates basic mock scenarios with extendable and final classes. */
@RunWith(MyMockJUnitRunner.class)
@PrepareFinalClassMock(classes = {MyFinalClass.class})
public class MyTest {

    /*
    @Test
    public void mockObjectWithNoDefaultConstructor() throws Exception {
        // todo find a way to create the object
        //Mockito.mock(MyFinalClass.class);
        MyEntityClass entity = MockCreator.createMock(MyEntityClass.class);
        MockUtil.when(() -> entity.getAge()).thenReturn(1);
        assertEquals(entity.getAge(), 1);
    }
    */

    @Test
    public void testClass() throws Exception {
        MyClass myClass = MockCreator.createMock(MyClass.class);
        MockUtil.when(() -> myClass.method1("something")).thenReturn("mock");
        assertEquals("mock", myClass.method1("something")); // test mock
        assertNotEquals("mock", myClass.method1("everything")); // // test real
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFinalException() throws Exception {
        MyFinalClass myFinalClass = MockCreator.createMock(MyFinalClass.class);
        MockUtil.when(() -> myFinalClass.method1("error")).thenThrowException(new IllegalArgumentException("why not"));
        myFinalClass.method1("error"); // test exception
    }

    @Test
    public void testMyFinalClassMethod1() throws Exception {
        MyFinalClass myFinalClass = MockCreator.createMock(MyFinalClass.class);
        MockUtil.when(() -> myFinalClass.method1("b")).thenReturn("a");
        MockUtil.when(() -> myFinalClass.method1("c")).thenReturn(null);
        assertEquals("a", myFinalClass.method1("b")); // test mock
        assertNull(myFinalClass.method1("c")); // test mock
        assertEquals("hey: pale", myFinalClass.method1("pale")); // test real

    }

    @Test
    public void testMyFinalClassMethod2() throws Exception {
        MyFinalClass myFinalClass = MockCreator.createMock(MyFinalClass.class);
        MockUtil.when(() -> myFinalClass.method2(2, 1)).thenReturn(4);
        assertTrue(4 == myFinalClass.method2(2, 1)); // test mock
        assertTrue(8 == myFinalClass.method2(4, 4));  // test real
    }
}

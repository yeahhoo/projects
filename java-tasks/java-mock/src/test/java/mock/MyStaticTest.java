package mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import mock.stubbers.MockUtil;
import org.junit.Test;
import org.junit.runner.RunWith;

import mock.annotations.PrepareFinalClassMock;
import mock.utils.MockCreator;
import mock.utils.MyMockJUnitRunner;

/** Example tests for static methods. */
@RunWith(MyMockJUnitRunner.class)
@PrepareFinalClassMock(classes = {MyFinalClass.class})
public class MyStaticTest {

    @Test
    public void testMockStaticClass() {
        MockCreator.mockStatic(MyFinalClass.class);
        MockUtil.when(() -> MyFinalClass.incStaticMethod(2)).thenReturn(9);
        assertEquals(9, MyFinalClass.incStaticMethod(2));
        assertEquals(8, MyFinalClass.incStaticMethod(7)); // test real method
    }

    @Test
    public void testNonStatic() throws Exception {
        MyFinalClass myObject = MockCreator.createMock(MyFinalClass.class);
        MockUtil.when(() -> myObject.method1("hey")).thenReturn("yeah");
        assertEquals("yeah", myObject.method1("hey"));
    }


    @Test
    public void testMyFinalClassMocks() {
        MockCreator.mockStatic(MyFinalClass.class);
        MockUtil.when(() -> MyFinalClass.createEntity("pale", 19)).thenReturn(new MyEntityClass("me", 28));
        MockUtil.when(() -> MyFinalClass.createEntity("rogue", 15)).thenReturn(null);
        assertEquals(new MyEntityClass("me", 28), MyFinalClass.createEntity("pale", 19)); // test mock
        assertNull(MyFinalClass.createEntity("rogue", 15));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStaticFinalException() {
        MockCreator.mockStatic(MyFinalClass.class);
        MockUtil.when(() -> MyFinalClass.incStaticMethod(89)).thenThrowException(new IllegalArgumentException("static exception"));
        MyFinalClass.incStaticMethod(89);
    }

    @Test(expected = IllegalStateException.class)
    public void staticVoidMethodThrowsException() {
        MockCreator.mockStatic(MyFinalClass.class);
        MockUtil.when(() -> MyFinalClass.voidMethodThrowsException("oops")).thenThrowException(new IllegalStateException("why not"));
        MyFinalClass.voidMethodThrowsException("oops");
    }

    @Test
    public void staticDoNothing() {
        MockCreator.mockStatic(MyFinalClass.class);
        MockUtil.when(() -> MyFinalClass.voidMethodThrowsException("oops")).thenDoNothing();
        MyFinalClass.voidMethodThrowsException("oops");
    }
}

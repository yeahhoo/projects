package mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import mock.stubbers.MockUtil;
import org.junit.Test;
import org.junit.runner.RunWith;

import mock.annotations.PrepareFinalClassMock;
import mock.utils.MockCreator;
import mock.utils.MockActionJUnitRunner;

/** Example tests for static methods. */
@RunWith(MockActionJUnitRunner.class)
@PrepareFinalClassMock(classes = {MyFinalClass.class})
public class MyStaticTest {

    @Test
    public void testMockStaticMethodAndPlainClass() {
        MockCreator.mockStatic(MyFinalClass.class);
        MyFinalClass finalClass = MockCreator.createMock(MyFinalClass.class);
        MockUtil.when(() -> MyFinalClass.incStaticMethod(3)).thenReturn(1);
        MockUtil.when(() -> finalClass.nonStaticSum(3, 1)).thenReturn(9);

        assertEquals(1, MyFinalClass.incStaticMethod(3));
        assertEquals(Integer.valueOf(9), finalClass.nonStaticSum(3, 1));
        assertEquals(2, MyFinalClass.incStaticMethod(1)); // test real
        assertEquals(Integer.valueOf(4), finalClass.nonStaticSum(1, 3)); // test real
    }

    @Test
    public void testMockStaticClass() {
        MockCreator.mockStatic(MyFinalClass.class);
        MockUtil.when(() -> MyFinalClass.incStaticMethod(2)).thenReturn(9);
        assertEquals(9, MyFinalClass.incStaticMethod(2));
        assertEquals(8, MyFinalClass.incStaticMethod(7)); // test real method
    }

    @Test
    public void testNonStatic() {
        MyFinalClass myObject = MockCreator.createMock(MyFinalClass.class);
        MockUtil.when(() -> myObject.nonStaticSayHey("hey")).thenReturn("yeah");
        assertEquals("yeah", myObject.nonStaticSayHey("hey"));
    }


    @Test
    public void testMyFinalClassMocks() {
        MockCreator.mockStatic(MyFinalClass.class);
        MyEntityClass entity = MockCreator.createMock(MyEntityClass.class);
        MockUtil.when(() -> entity.getAge()).thenReturn(28);
        MockUtil.when(() -> entity.getName()).thenReturn("me");
        MockUtil.when(() -> MyFinalClass.createEntity("pale", 19)).thenReturn(entity);
        MockUtil.when(() -> MyFinalClass.createEntity("rogue", 15)).thenReturn(null);
        assertEquals(28, entity.getAge());
        assertEquals("me", entity.getName());
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

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
import mock.utils.MockActionJUnitRunner;

import java.io.IOException;

/** Test demonstrates basic mock scenarios with extendable and final classes. */
@RunWith(MockActionJUnitRunner.class)
@PrepareFinalClassMock(classes = {MyFinalClass.class})
public class MyTest {

    @Test
    public void mockObjectWithNoDefaultConstructor() {

        MyEntityClass entity1 = MockCreator.createMock(MyEntityClass.class);
        MockUtil.when(() -> entity1.getAge()).thenReturn(1);
        MyEntityClass entity2 = MockCreator.createMock(MyEntityClass.class);
        MockUtil.when(() -> entity2.getAge()).thenReturn(2);

        assertEquals(entity1.getAge(), 1);
        assertEquals(entity2.getAge(), 2);
    }

    @Test
    public void testClass() {
        MyClass myClass = MockCreator.createMock(MyClass.class);
        MockUtil.when(() -> myClass.nonStaticSayHi("something")).thenReturn("mock");
        assertEquals("mock", myClass.nonStaticSayHi("something")); // test mock
        assertNotEquals("mock", myClass.nonStaticSayHi("everything")); // // test real
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFinalException() {
        MyFinalClass myFinalClass = MockCreator.createMock(MyFinalClass.class);
        MockUtil.when(() -> myFinalClass.nonStaticSayHey("error")).thenThrowException(new IllegalArgumentException("why not"));
        myFinalClass.nonStaticSayHey("error"); // test exception
    }

    @Test
    public void testMyFinalClassMethod1() {
        MyFinalClass myFinalClass = MockCreator.createMock(MyFinalClass.class);
        MockUtil.when(() -> myFinalClass.nonStaticSayHey("b")).thenReturn("a");
        MockUtil.when(() -> myFinalClass.nonStaticSayHey("c")).thenReturn(null);
        assertEquals("a", myFinalClass.nonStaticSayHey("b")); // test mock
        assertNull(myFinalClass.nonStaticSayHey("c")); // test mock
        assertEquals("hey: pale", myFinalClass.nonStaticSayHey("pale")); // test real
    }

    @Test
    public void testMyFinalClassMethod2() {
        MyFinalClass myFinalClass = MockCreator.createMock(MyFinalClass.class);
        MockUtil.when(() -> myFinalClass.nonStaticSum(2, 1)).thenReturn(4);
        assertTrue(4 == myFinalClass.nonStaticSum(2, 1)); // test mock
        assertTrue(8 == myFinalClass.nonStaticSum(4, 4));  // test real
    }

    @Test
    public void throwsNonStaticException() throws IOException {
        MyClass myClass = MockCreator.createMock(MyClass.class);
        MockUtil.when(() -> myClass.throwException()).thenDoNothing();
        myClass.throwException();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTwoActionsInOneMock() {
        MyClass myClass = MockCreator.createMock(MyClass.class);
        MockUtil.when(() -> {
            myClass.nonStaticSayHi("arg1");
            return myClass.nonStaticSayHi("arg2");
        }).thenReturn("exception");
    }
}

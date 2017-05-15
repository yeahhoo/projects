package mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mock.annotations.PrepareFinalClassMock;
import mock.stubbers.StubUtil;
import mock.utils.MockCreator;
import mock.utils.MyMockJUnitRunner;

/**
 * @author Aleksandr_Savchenko
 */
@RunWith(MyMockJUnitRunner.class)
@PrepareFinalClassMock(classes = {MyFinalClass.class})
public class MyStaticTest {

    @Before
    public void setUp() throws Exception {
        MockCreator.mockStatic(MyFinalClass.class);
        StubUtil.doStaticReturn(9).whenStatic(MyFinalClass.staticMethod(2));
        StubUtil.doStaticReturn(new MyEntityClass("me", 28)).whenStatic(MyFinalClass.createEntity("pale", 19));
        StubUtil.doStaticThrow(new IllegalArgumentException("static exception")).whenStatic(MyFinalClass.staticMethod(89));
        StubUtil.doStaticNothing().whenStatic(MyFinalClass.createEntity("rogue", 15));
        //StubUtil.doStaticReturn(2);
    }

    @Test
    public void testStaticClass() {
        assertEquals(9, MyFinalClass.staticMethod(2)); // test mock
        assertEquals(new MyEntityClass("me", 28), MyFinalClass.createEntity("pale", 19)); // test mock
        assertNull(MyFinalClass.createEntity("rogue", 15));
        assertEquals(0, MyFinalClass.staticMethod(8)); // test real -> always return 'default' type values
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStaticFinalException() {
        MyFinalClass.staticMethod(89); // test exception
    }

}

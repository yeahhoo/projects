**Java Mock Action**

Yet another small library for creating mocks to be used in testing. Developed just for fun.
Main motivation for me was curiosity about how popular frameworks such as Mockito and PowerMock deal with it.
So I simply wanted to practice development of fluent API and proxying over existing classes.
I will try to make it less buggy with time.

The main benefit of the approach applied in this implementation consists in using "actions" in mocking.
It enables to control mocking flow and makes the API for mocking static methods and final classes the same.

**Mocking not final class**

Typical usage for mocking a non-final class shown below:

```java
    @Test
    public void testClass() throws Exception {
        MyClass myClass = MockCreator.createMock(MyClass.class);
        MockUtil.when(() -> myClass.method1("something")).thenReturn("mock");
        assertEquals("mock", myClass.method1("something"));
    }
```

**Mocking final classes and static methods**

At first you need to mark the problematic classes with annotation @PrepareFinalClassMock and set runner to MockActionJUnitRunner

```java
    @RunWith(MockActionJUnitRunner.class)
    @PrepareFinalClassMock(classes = {MyFinalClass.class})
```

If you need to mock static method you need to create proxy for it as follows:

```java
MockCreator.mockStatic(MyFinalClass.class);
```

The API for mocking static and object methods are the same which is a good thing about it.
Full examples of doing this shown below:

```java
@RunWith(MockActionJUnitRunner.class)
@PrepareFinalClassMock(classes = {MyFinalClass.class})
public class MyStaticTest {

    @Test
    public void testMockStaticClass() {
        MockCreator.mockStatic(MyFinalClass.class);
        MockUtil.when(() -> MyFinalClass.staticMethod(2)).thenReturn(9);
        assertEquals(9, MyFinalClass.staticMethod(2));
        assertEquals(9, MyFinalClass.staticMethod(8)); // test real method
    }

    @Test
    public void testNonStatic() throws Exception {
        MyFinalClass myObject = MockCreator.createMock(MyFinalClass.class);
        MockUtil.when(() -> myObject.method1("hey")).thenReturn("yeah");
        assertEquals("yeah", myObject.method1("hey"));
    }
}
```

**Tested**

Last version of JVM it was tested on was "10.0.1".

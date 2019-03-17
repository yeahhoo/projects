package mock;

/** Example final class for testing. */
public final class MyFinalClass {

    public final String nonStaticSayHey(String arg) {
        return "hey: " + arg;
    }

    public final Integer nonStaticSum(Integer i1, Integer i2) {
        return i1 + i2;
    }

    public static int incStaticMethod(int i) {
        return i + 1;
    }

    public static void voidMethodThrowsException(String str) {
        throw new IllegalArgumentException("something wrong with this method");
    }

    public static MyEntityClass createEntity(String name, int age) {
        MyEntityClass entity = new MyEntityClass(name, age);
        return entity;
    }
}

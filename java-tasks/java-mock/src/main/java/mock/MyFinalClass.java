package mock;

/**
 * @author Aleksandr_Savchenko
 */
public final class MyFinalClass {

    public String method1(String arg) {
        return "hey: " + arg;
    }

    public Integer method2(Integer i1, Integer i2) {
        return i1 + i2;
    }

    public static int staticMethod(int i) {
        return i + 1;
    }

    public static MyEntityClass createEntity(String name, int age) {
        MyEntityClass entity = new MyEntityClass(name, age);
        return entity;
    }

}

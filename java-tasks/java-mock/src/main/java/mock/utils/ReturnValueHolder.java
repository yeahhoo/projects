package mock.utils;

/**
 * ugly hack, is used because it's hard to pass value returned by method to proxyStaticMethodCall().
 * @author Aleksandr_Savchenko
 */
public class ReturnValueHolder {

    private static ThreadLocal<Object> threadLocal = new ThreadLocal<>();

    public static boolean setCleanValue(Object returnValue) {
        if (getValue() != null) {
            threadLocal.set(returnValue);
            return false;
        }
        threadLocal.set(returnValue);
        return true;
    }

    public static Object getValue() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }

}


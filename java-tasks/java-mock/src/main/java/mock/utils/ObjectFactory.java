package mock.utils;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import java.util.HashMap;
import java.util.Map;

/** Creates objects by specified conditions. */
public final class ObjectFactory {

    /** Creates object of given class. */
    public static <T> T createInstance(Class<T> mockedType) {
        Object primitiveValue = getDefaultPrimitiveValue(mockedType);
        if (primitiveValue != null) {
            return (T) primitiveValue;
        }
        Objenesis objenesis = new ObjenesisStd();
        ObjectInstantiator instantiator = objenesis.getInstantiatorOf(mockedType);
        return  (T) instantiator.newInstance();
    }

    private static Object getDefaultPrimitiveValue(Class<?> clazz) {
        return DEFAULT_VALS_OF_PRIMITIVES.get(clazz);
    }

    private static final Map<Class, Object> DEFAULT_VALS_OF_PRIMITIVES = new HashMap<>();

    static {
        DEFAULT_VALS_OF_PRIMITIVES.put(void.class, new Object());
        DEFAULT_VALS_OF_PRIMITIVES.put(short.class, 0);
        DEFAULT_VALS_OF_PRIMITIVES.put(int.class, 0);
        DEFAULT_VALS_OF_PRIMITIVES.put(long.class, 0L);
        DEFAULT_VALS_OF_PRIMITIVES.put(float.class, 0f);
        DEFAULT_VALS_OF_PRIMITIVES.put(double.class, 0d);
        DEFAULT_VALS_OF_PRIMITIVES.put(char.class, Character.MIN_VALUE);
        DEFAULT_VALS_OF_PRIMITIVES.put(byte.class, 0);
        DEFAULT_VALS_OF_PRIMITIVES.put(boolean.class, false);
    }
}

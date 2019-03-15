package mock.utils;

import java.lang.reflect.Constructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Creates objects by specified conditions. */
public class ObjectFactory {

    /**
     * Creates object by given class. It's a dirty hack and normal library should be for this purpose.
     * Perhaps http://bytebuddy.net is a good fit here.
     */
    public static Object createInstance(Class<?> mockedType) throws Exception {
        Constructor<?> shortestConstructor = Arrays.stream(mockedType.getDeclaredConstructors())
                .sorted((o1, o2) -> Integer.valueOf(o1.getParameterCount()).compareTo(o2.getParameterCount()))
                .limit(1).collect(Collectors.toList()).get(0);
        List<Object> defArgs = Arrays.stream(shortestConstructor.getParameterTypes())
                .map(clazz -> DEFAULT_VALS.getOrDefault(clazz, null)).collect(Collectors.toList());
        return shortestConstructor.newInstance(defArgs.toArray(new Object[defArgs.size()]));
    }

    private static final Map<Class, Object> DEFAULT_VALS = new HashMap<>();

    static {
        DEFAULT_VALS.put(Short.class, 0);
        DEFAULT_VALS.put(short.class, 0);
        DEFAULT_VALS.put(Integer.class, 0);
        DEFAULT_VALS.put(int.class, 0);
        DEFAULT_VALS.put(Long.class, 0L);
        DEFAULT_VALS.put(long.class, 0L);
        DEFAULT_VALS.put(Float.class, 0f);
        DEFAULT_VALS.put(float.class, 0f);
        DEFAULT_VALS.put(Double.class, 0d);
        DEFAULT_VALS.put(double.class, 0d);
        DEFAULT_VALS.put(Character.class, Character.MIN_VALUE);
        DEFAULT_VALS.put(char.class, Character.MIN_VALUE);
        DEFAULT_VALS.put(Byte.class, 0);
        DEFAULT_VALS.put(byte.class, 0);
        DEFAULT_VALS.put(Boolean.class, false);
        DEFAULT_VALS.put(boolean.class, false);
    }
}

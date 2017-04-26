package mock.utils;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import mock.annotations.PrepareFinalClassMock;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * analogue of {@link org.powermock.modules.junit4.PowerMockRunner}
 * @author Aleksandr_Savchenko
 */
public class MyMockJUnitRunner extends BlockJUnit4ClassRunner {

    public MyMockJUnitRunner(Class<?> clazz) throws InitializationError {
        super(getFromMockClassloader(clazz));
    }

    private static Class<?> getFromMockClassloader(Class<?> clazz) throws InitializationError {
        try {
            Set<String> classesToMock = findClassesToMock(clazz);
            ClassLoader classLoader = new MyMockClassLoader(classesToMock);
            return Class.forName(clazz.getName(), true, classLoader);
        } catch (Exception e) {
            throw new InitializationError(e);
        }
    }

    private static Set<String> findClassesToMock(Class clazz) {
        Set<String> set = new LinkedHashSet<>();
        set.add(clazz.getCanonicalName());
        PrepareFinalClassMock annotation = (PrepareFinalClassMock) clazz.getAnnotation(PrepareFinalClassMock.class);
        if (annotation != null) {
            Class[] classes = annotation.classes();
            Arrays.asList(classes).forEach(c -> set.add(c.getCanonicalName()));
        }
        return set;
    }

}

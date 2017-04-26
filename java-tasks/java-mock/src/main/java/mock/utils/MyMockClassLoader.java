package mock.utils;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.Modifier;

import java.util.Set;

/**
 * analogue of {@link org.powermock.core.classloader.MockClassLoader}
 * http://stackoverflow.com/questions/42102/using-different-classloaders-for-different-junit-tests
 * @author Aleksandr_Savchenko
 */
public class MyMockClassLoader extends ClassLoader {

    private ClassPool pool;
    private Set<String> classesToMock;

    public MyMockClassLoader(Set<String> classesToMock) {
        this.classesToMock = classesToMock;
        pool = ClassPool.getDefault();
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
            if (classesToMock.contains(name)) {
                Class c = loadMockClass(name);
                return c;
            }
            return super.loadClass(name);
        } catch (Exception e) {
            throw new RuntimeException(String.format("couldn't find class: %s", name), e);
        }
    }

    private Class<?> loadMockClass(String name) {
        byte[] clazz = null;
        try {
            CtClass type = pool.get(name);
            // removing all final class modifiers if there any
            if (Modifier.isFinal(type.getModifiers())) {
                type.setModifiers(type.getModifiers() ^ Modifier.FINAL);
            }
            /*
            // right way to do it
            for (MockTransformer transformer : mockTransformerChain) {
                type = transformer.transform(type);
            }
            */
            clazz = type.toBytecode();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to transform class with name " + name + ". Reason: " + e.getMessage(), e);
        }

        return defineClass(name, clazz, 0, clazz.length);
    }


}

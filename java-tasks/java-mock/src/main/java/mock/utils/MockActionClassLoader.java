package mock.utils;

import javassist.*;

import java.util.Set;

/**
 * Analogue of {@link org.powermock.core.classloader.MockClassLoader}.
 * http://stackoverflow.com/questions/42102/using-different-classloaders-for-different-junit-tests
 */
public final class MockActionClassLoader extends ClassLoader {

    private ClassPool pool;
    private Set<String> classesToMock;

    public MockActionClassLoader(Set<String> classesToMock) {
        this.classesToMock = classesToMock;
        pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(this.getClass()));
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
            // type is singleton and shared across all tests
            synchronized (type) {
                if (type.isFrozen()) {
                    // defrosting class in synchronized mode to reload it for new test
                    type.defrost();
                }
                // removing all final class modifiers if there any
                if (Modifier.isFinal(type.getModifiers())) {
                    type.setModifiers(type.getModifiers() ^ Modifier.FINAL);
                }
                // mocking methods
                for (CtMethod method : type.getMethods()) {
                    // removing all finals
                    if (Modifier.isFinal(method.getModifiers())) {
                        method.setModifiers(method.getModifiers() ^ Modifier.FINAL);
                    }

                    // proxying statics
                    if (Modifier.isStatic(method.getModifiers())) {
                        modifyMethod(name, method);
                    }
                }
                /*
                // right way to do it
                for (MockTransformer transformer : mockTransformerChain) {
                    type = transformer.transform(type);
                }
                */
                clazz = type.toBytecode();
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to transform class with name " + name + ". Reason: " + e.getMessage(), e);
        }
        return defineClass(name, clazz, 0, clazz.length);
    }

    /**
     * copy of the method "modifyMethod" from #{@link org.powermock.core.transformers.impl.MainMockTransformer} with minor changes.
     * */
    public void modifyMethod(final String mockName, final CtMethod method) throws Exception {
        if (!Modifier.isAbstract(method.getModifiers())) {
            // Lookup the method return type
            final CtClass returnTypeAsCtClass = method.getReturnType();
            final String returnTypeAsString = getReturnTypeAsString(method);
            final String returnValue = getCorrectReturnValueType(returnTypeAsCtClass);

            /**
             * The code proxies static method. Instead of calling static method it calls method {@link MockCreatorDelegator#proxyStaticMethodCall}.
             * If proxyStaticMethodCall returns "PROCEED" object then it means call has been mocked and default type value returned.
             * Otherwise returns mocked value.
             * Note that this feature never calls original code - on any unplanned issues default type value returned.
             * */
            String code = "Object value = " + MockCreatorDelegator.class.getName() + ".proxyStaticMethodCall(\"" + mockName + "\" , \"" + method.getName()
                          + "\", \"" + returnTypeAsString + "\", $args);"
                          + "if (value == " + MockCreatorDelegator.class.getName() + ".PROCEED) "
                          + "return " + getDefaultValueForReturnType(returnValue) + ";"
                          + "else return " + returnValue + ";";
            method.setBody("{ " + code + "}");
        }
    }

    private static final String VOID = "";

    private String getDefaultValueForReturnType(final String returnType) {
        if (VOID.equals(returnType)) {
            return "";
        } else if (returnType.contains("java.lang.Boolean")) {
            return "false";
        } else if (returnType.contains("java.lang.Character")) {
            return "'a'";
        } else if (returnType.contains("java.lang.Number")) {
            return "0";
        }
        return "null";
    }

    private String getCorrectReturnValueType(final CtClass returnTypeAsCtClass) {
        final String returnTypeAsString = returnTypeAsCtClass.getName();
        String returnValue = "($r)value";
        if (returnTypeAsCtClass.equals(CtClass.voidType)) {
            returnValue = VOID;
        } else if (returnTypeAsCtClass.isPrimitive()) {
            if (returnTypeAsString.equals("char")) {
                returnValue = "((java.lang.Character)value).charValue()";
            } else if (returnTypeAsString.equals("boolean")) {
                returnValue = "((java.lang.Boolean)value).booleanValue()";
            } else {
                returnValue = "((java.lang.Number)value)." + returnTypeAsString + "Value()";
            }
        } else {
            returnValue = "(" + returnTypeAsString + ")value";
        }
        return returnValue;
    }

    private String getReturnTypeAsString(final CtMethod method) throws NotFoundException {
        CtClass returnType = method.getReturnType();
        String returnTypeAsString = VOID;
        if (!returnType.equals(CtClass.voidType)) {
            returnTypeAsString = returnType.getName();
        }
        return returnTypeAsString;
    }
}

package com.example.dependency_resolver.contexts;

import com.example.dependency_resolver.utils.ClassDependency;
import com.example.dependency_resolver.utils.ConcurrentListProcessor;
import com.example.dependency_resolver.utils.Dependency;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Aleksandr_Savchenko
 */
public class Context {

    private final Map<Class, Object> beans = new HashMap<>();

    public Context(String packName) {
        this.scanPackage(packName, 3);
    }

    public <T> T getBean(Class clazz) {
        return (T) beans.get(clazz);
    }

    private void scanPackage(String packName, int threadNumber) {
        try {
            final ConcurrentHashMap<Class, Dependency> dependencyPool = new ConcurrentHashMap<>();
            List<ClassDependency> classDeps = Arrays.asList(getClasses(packName)).stream()
                    .map(clazz -> new ClassDependency(dependencyPool, clazz)).collect(Collectors.toList());
            // phase one
            ConcurrentListProcessor<ClassDependency> proc1 = new ConcurrentListProcessor<>(classDeps, threadNumber);
            proc1.processCollection();

            // phase two
            ConcurrentListProcessor<Dependency> proc2 = new ConcurrentListProcessor<>(dependencyPool.values(), threadNumber);
            proc2.processCollectionConsumingInParallel(d -> {
                beans.put(d.getClazz(), d.getInstance());
            });

            /*
            dependencyPool.values().parallelStream().forEach(d -> {
                try {
                    d.resolve();
                    beans.put(d.getClazz(), d.getInstance());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            });
            */
        } catch (Exception e) {
            throw new RuntimeException("error appeared while scanning package\n", e);
        }
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * source: https://dzone.com/articles/get-all-classes-within-package
     *
     * @param packageName The base package
     * @return The classes
     */
    private static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }



}

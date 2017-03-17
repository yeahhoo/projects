package com.example.dependency_resolver.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Aleksandr_Savchenko
 */
public class Dependency {

    private final Map<Dependency, Field> depMap = new HashMap<>();
    private Class clazz;
    private Object instance;

    public Dependency(Class clazz) {
        this.clazz = clazz;
    }

    public void resolve() throws IllegalAccessException, InstantiationException  {
        for (Dependency dependency : depMap.keySet()) {
            if (!dependency.isResolved()) {
                dependency.resolve();
            }
        }
        initDependency();
    }

    private void initDependency() throws IllegalAccessException, InstantiationException {
        if (isResolved()) {
            return;
        }
        instance = clazz.newInstance();
        for (Map.Entry<Dependency, Field> entry : depMap.entrySet()) {
            entry.getValue().setAccessible(true);
            entry.getValue().set(instance,  entry.getKey().getInstance());
        }
    }

    private boolean isResolved() {
        return instance != null;
    }

    public void addDependency(Dependency dependency, Field field) {
        depMap.put(dependency, field);
    }

    public Object getInstance() {
        return instance;
    }

    public Class getClazz() {
        return clazz;
    }

    @Override
    public String toString() {
        return clazz.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dependency)) {
            return false;
        }

        Dependency that = (Dependency) o;

        if (clazz != null ? !clazz.equals(that.clazz) : that.clazz != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return clazz != null ? clazz.hashCode() : 0;
    }

}

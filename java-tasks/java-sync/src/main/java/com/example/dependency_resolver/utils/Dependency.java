package com.example.dependency_resolver.utils;

import com.example.dependency_resolver.contexts.Context;

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

    public boolean isResolved() {
        return instance != null;
    }

    public void addDependency(Dependency dependency, Field field) {
        depMap.put(dependency, field);
    }

    public void resolve(Context context) throws IllegalAccessException, InstantiationException  {
        for (Dependency dependency : depMap.keySet()) {
            if (!dependency.isResolved()) {
                dependency.resolve(context);
            }
        }
        context.initDependency(this);
    }

    public Map<Dependency, Field> getDepMap() {
        return depMap;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
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

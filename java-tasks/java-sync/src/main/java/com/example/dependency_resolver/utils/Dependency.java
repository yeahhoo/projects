package com.example.dependency_resolver.utils;

import com.example.dependency_resolver.annotations.Autowired;
import com.example.dependency_resolver.contexts.Context;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aleksandr_Savchenko
 */
public class Dependency {

    private final List<Dependency> dependencies = new ArrayList<>();
    private boolean isResolved;
    private Class clazz;
    private Object instance;

    public Dependency(Class clazz) {
        this.clazz = clazz;
    }

    public void setResolved(boolean isResolved) {
        this.isResolved = isResolved;
    }

    public boolean isResolved() {
        return isResolved;
    }

    public void addDependency(Dependency dependency) {
        dependencies.add(dependency);
    }

    public boolean resolve(Context context) throws IllegalAccessException, InstantiationException  {
        for (Dependency dependency : dependencies) {
            if (!dependency.isResolved()) {
                dependency.resolve(context);
            }
        }
        initBean(context);
        isResolved = true;
        return isResolved;
    }

    private void initBean(Context context) throws IllegalAccessException, InstantiationException  {
        if (context.getBean(clazz) != null) {
            return;
        }
        instance = clazz.newInstance();
        for (Field field : instance.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                field.set(instance, context.getBean(field.getType()));
            }
        }
        context.addBean(clazz, instance);
    }

    public Object getInstance() {
        return instance;
    }

    public Class getClazz() {
        return clazz;
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

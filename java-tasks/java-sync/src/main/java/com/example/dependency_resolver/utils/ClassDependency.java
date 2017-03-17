package com.example.dependency_resolver.utils;

import com.example.dependency_resolver.annotations.Autowired;
import com.example.dependency_resolver.annotations.Bean;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author Aleksandr_Savchenko
 */
public class ClassDependency implements WorkableItem<Class> {

    private final Map<Class, Dependency> dependencyPool;
    private final Class clazz;

    public ClassDependency(Map<Class, Dependency> dependencyPool, Class clazz) {
        this.dependencyPool = dependencyPool;
        this.clazz = clazz;
    }

    @Override
    public Class processItem() {
        if (clazz.isAnnotationPresent(Bean.class)) {
            // first atomic
            Dependency dependency = null;
            // todo split up synchronization on dependency pool
            synchronized (dependencyPool) {
                dependency = dependencyPool.get(clazz);
                if (dependency == null) {
                    dependency = new Dependency(clazz);
                    dependencyPool.put(clazz, dependency);
                }
            }
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class) && isValid(clazz, field)) {
                    // second atomic
                    synchronized (dependencyPool) {
                        if (!dependencyPool.containsKey(field.getType())) {
                            Dependency d = new Dependency(field.getType());
                            dependencyPool.put(field.getType(), d);
                            dependency.addDependency(d, field);
                        } else {
                            dependency.addDependency(dependencyPool.get(field.getType()), field);
                        }
                    }
                }
            }

        }
        return clazz;
    }

    private boolean isValid(Class clazz, Field field) {
        if (!field.getType().isAnnotationPresent(Bean.class)) {
            System.err.println("field " + field.getName() +  " of " + clazz + " is marked Autowired but that class is not declared as Bean");
            return false;
        }
        return true;
    }
}

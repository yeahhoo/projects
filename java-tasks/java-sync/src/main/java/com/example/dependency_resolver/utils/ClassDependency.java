package com.example.dependency_resolver.utils;

import com.example.dependency_resolver.annotations.Autowired;
import com.example.dependency_resolver.annotations.Bean;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Aleksandr_Savchenko
 */
public class ClassDependency implements WorkableItem<Class> {

    private final ConcurrentHashMap<Class, Dependency> dependencyPool;
    private final Class clazz;

    public ClassDependency(ConcurrentHashMap<Class, Dependency> dependencyPool, Class clazz) {
        this.dependencyPool = dependencyPool;
        this.clazz = clazz;
    }

    @Override
    public Class processItem() {
        if (clazz.isAnnotationPresent(Bean.class)) {
            // first atomic
            Dependency dependency = dependencyPool.get(clazz);
            if (dependency == null) {
                dependency = putSafelyInMap(clazz, new Dependency(clazz));
            }

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class) && isValid(clazz, field)) {
                    // second atomic
                    Dependency subDep = dependencyPool.get(field.getType());
                    if (subDep == null) {
                        subDep = putSafelyInMap(field.getType(), new Dependency(field.getType()));
                        dependency.addDependency(subDep, field);
                    } else {
                        dependency.addDependency(dependencyPool.get(field.getType()), field);
                    }

                }
            }
        }
        return clazz;
    }

    private Dependency putSafelyInMap(Class key, Dependency d) {
        Dependency actualDep = dependencyPool.putIfAbsent(key, d);
        if (actualDep != null) {
            return actualDep;
        }
        return d;
    }

    private boolean isValid(Class clazz, Field field) {
        if (!field.getType().isAnnotationPresent(Bean.class)) {
            System.err.println("field " + field.getName() +  " of " + clazz + " is marked Autowired but that class is not declared as Bean");
            return false;
        }
        return true;
    }
}

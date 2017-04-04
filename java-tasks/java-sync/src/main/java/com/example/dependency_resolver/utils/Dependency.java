package com.example.dependency_resolver.utils;

import com.example.concurrent_task_lib.WorkableItem;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Aleksandr_Savchenko
 */
public class Dependency implements WorkableItem<Dependency> {

    private final Map<Dependency, Field> depMap = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private Class clazz;
    private volatile Object instance;
    private int timesCreated = 0;

    public Dependency(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public Dependency processItem() {
        try {
            resolve();
        } catch (Exception e) {
            throw new RuntimeException(String.format("Error occurred while resolving dependency %s", this), e);
        }
        return this;
    }

    private void resolve() throws IllegalAccessException, InstantiationException  {
        for (Dependency dependency : depMap.keySet()) {
            if (!dependency.isResolved()) {
                dependency.resolve();
            }
        }
        initDependency();
    }

    private void initDependency() throws IllegalAccessException, InstantiationException {
        // must be atomic operation
        if (atomicInit()) {
            for (Map.Entry<Dependency, Field> entry : depMap.entrySet()) {
                entry.getValue().setAccessible(true);
                entry.getValue().set(instance,  entry.getKey().getInstance());
            }
            System.out.println(Thread.currentThread() + " created / " + ++timesCreated + " / " + this);
        }
    }

    private boolean atomicInit() throws IllegalAccessException, InstantiationException {
        lock.writeLock().lock();
        try {
            if (instance == null) {
                instance = clazz.newInstance();
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private boolean isResolved() {
        lock.readLock().lock();
        try {
            return instance != null;
        } finally {
            lock.readLock().unlock();
        }
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
        StringBuilder sb = new StringBuilder(clazz.getName());
        sb.append("[");
        for (Dependency dep : depMap.keySet()) {
            sb.append(dep.clazz).append(", ");
        }
        sb.append("]");
        return sb.toString();
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

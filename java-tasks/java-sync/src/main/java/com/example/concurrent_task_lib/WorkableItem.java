package com.example.concurrent_task_lib;

/**
 * @author Aleksandr_Savchenko
 */
public interface WorkableItem<T> {

    T processItem();

}

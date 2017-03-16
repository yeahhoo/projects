package com.example.dependency_resolver.beans;

import com.example.dependency_resolver.annotations.Autowired;
import com.example.dependency_resolver.annotations.Bean;

/**
 * @author Aleksandr_Savchenko
 */
@Bean
public class ClassB {

    @Autowired
    private ClassA a;

    public ClassA getA() {
        return a;
    }

}

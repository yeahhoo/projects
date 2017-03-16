package com.example.dependency_resolver.beans;

import com.example.dependency_resolver.annotations.Autowired;
import com.example.dependency_resolver.annotations.Bean;

/**
 * @author Aleksandr_Savchenko
 */
@Bean
public class ClassC {

    @Autowired
    private ClassA a;

    @Autowired
    private ClassB b;

    public ClassA getA() {
        return a;
    }

    public ClassB getB() {
        return b;
    }

}

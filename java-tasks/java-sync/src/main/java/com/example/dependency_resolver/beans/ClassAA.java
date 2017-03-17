package com.example.dependency_resolver.beans;

import com.example.dependency_resolver.annotations.Autowired;
import com.example.dependency_resolver.annotations.Bean;

/**
 * @author Aleksandr_Savchenko
 */
@Bean
public class ClassAA {

    @Autowired
    private ClassB b;

    @Autowired
    private ClassC c;

    @Autowired
    private ClassD d;

    @Autowired
    private ClassNoBean noBean;

    public ClassB getB() {
        return b;
    }

    public ClassC getC() {
        return c;
    }

    public ClassD getD() {
        return d;
    }
}

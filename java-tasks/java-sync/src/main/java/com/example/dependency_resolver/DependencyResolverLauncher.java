package com.example.dependency_resolver;

import com.example.dependency_resolver.beans.ClassA;
import com.example.dependency_resolver.beans.ClassAA;
import com.example.dependency_resolver.beans.ClassB;
import com.example.dependency_resolver.beans.ClassC;
import com.example.dependency_resolver.beans.ClassD;
import com.example.dependency_resolver.contexts.Context;

/**
 * @author Aleksandr_Savchenko
 */
public class DependencyResolverLauncher {

    public static void main(String[] args) {
        System.out.println("Hello");
        Context context = new Context("com.example.dependency_resolver.beans");
        ClassAA aa = context.getBean(ClassAA.class);
        ClassD d = context.getBean(ClassD.class);
        ClassC c = context.getBean(ClassC.class);
        ClassB b = context.getBean(ClassB.class);
        ClassA a = context.getBean(ClassA.class);
        assert c.hashCode() == d.getC().hashCode();
        assert b.hashCode() == d.getB().hashCode();
        assert b.hashCode() == c.getB().hashCode();
        assert a.hashCode() == c.getA().hashCode();
        assert a.hashCode() == b.getA().hashCode();
        assert b.hashCode() == aa.getD().getB().hashCode();
        assert a.hashCode() == aa.getD().getB().getA().hashCode();
    }

}

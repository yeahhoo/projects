package mock.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use to load classes required pre-processing by javaassist (static, final classes).
 * @author Aleksandr_Savchenko
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrepareFinalClassMock {
    Class[] classes();
}

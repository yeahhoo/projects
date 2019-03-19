package mock.stubbers;

import java.util.function.Supplier;

/** Differs from {@link Supplier} only in that this class throws exception. */
public interface SupplierWithException<T> {

    /** {@link Supplier#get()} that throws exception. */
    T get() throws Throwable;
}

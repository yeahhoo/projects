package mock.stubbers;

/**
 * @author Aleksandr_Savchenko
 */
public interface MyStubber {

    <T> T when(T t);

    <T> void whenStatic(T t);

}

package mock.stubbers;

/** Specifies basic operations to finish mocking flow for mocked object. */
public interface Stubbing {

    /** Finish flow with throwing given exception. */
    <T extends Throwable> void thenThrowException(T exception);
}

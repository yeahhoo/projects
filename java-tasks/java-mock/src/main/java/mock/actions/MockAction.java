package mock.actions;

/** Present itself action to do when mocked method gets called. */
public interface MockAction {

    /** Action to perform on mocked method. */
    Object performAction() throws Throwable;
}

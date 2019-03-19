package mock;

import java.io.IOException;

/** Simple example class for testing. */
public class MyClass {

    public String nonStaticSayHi(String arg) {
        return "hey: " + arg;
    }

    public void throwException() throws IOException {
        throw new IOException("I'm mad at this world");
    }
}

package mock.utils;

import java.util.Arrays;

/** Contains arguments with which mocked method was called. Something similar to the "Matcher" concept in Mockito. */
public final class ArgKey {

    private final Object[] keys;

    public ArgKey(Object[] keys) {
        this.keys = keys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArgKey)) {
            return false;
        }

        ArgKey argKey = (ArgKey) o;

        if (!Arrays.equals(keys, argKey.keys)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return keys != null ? Arrays.hashCode(keys) : 0;
    }
}

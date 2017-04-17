package mock.utils;

import java.util.Arrays;

/**
 * @author Aleksandr_Savchenko
 */
public class ArgKey {

    private Object[] keys;

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

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
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

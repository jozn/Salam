package cz.msebera.android.httpclient.client.utils;

import java.lang.reflect.InvocationTargetException;

public final class CloneUtils {
    public static <T> T cloneObject(T obj) throws CloneNotSupportedException {
        T t = null;
        if (obj != null) {
            if (obj instanceof Cloneable) {
                try {
                    try {
                        t = obj.getClass().getMethod("clone", null).invoke(obj, null);
                    } catch (InvocationTargetException e) {
                        Throwable cause = e.getCause();
                        if (cause instanceof CloneNotSupportedException) {
                            throw ((CloneNotSupportedException) cause);
                        }
                        throw new Error("Unexpected exception", cause);
                    } catch (IllegalAccessException ex) {
                        throw new IllegalAccessError(ex.getMessage());
                    }
                } catch (NoSuchMethodException ex2) {
                    throw new NoSuchMethodError(ex2.getMessage());
                }
            }
            throw new CloneNotSupportedException();
        }
        return t;
    }
}

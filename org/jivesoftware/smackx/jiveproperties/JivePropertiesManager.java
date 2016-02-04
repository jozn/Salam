package org.jivesoftware.smackx.jiveproperties;

public final class JivePropertiesManager {
    private static boolean javaObjectEnabled;

    static {
        javaObjectEnabled = false;
    }

    public static boolean isJavaObjectEnabled() {
        return javaObjectEnabled;
    }
}

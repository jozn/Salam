package org.jivesoftware.smack.initializer;

import java.util.List;

public abstract class SmackAndOsgiInitializer implements SmackInitializer {
    public List<Exception> initialize(ClassLoader classLoader) {
        return initialize();
    }
}

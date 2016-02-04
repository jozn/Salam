package org.jivesoftware.smack.initializer;

import java.util.Collections;
import java.util.List;

public class VmArgInitializer extends UrlInitializer {
    public final List<Exception> initialize() {
        if (System.getProperty("smack.provider.file") != null) {
            super.initialize();
        }
        return Collections.emptyList();
    }
}

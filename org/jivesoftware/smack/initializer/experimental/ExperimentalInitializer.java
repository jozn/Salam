package org.jivesoftware.smack.initializer.experimental;

import org.jivesoftware.smack.initializer.UrlInitializer;

public class ExperimentalInitializer extends UrlInitializer {
    protected final String getProvidersUrl() {
        return "classpath:org.jivesoftware.smackx/experimental.providers";
    }

    protected final String getConfigUrl() {
        return "classpath:org.jivesoftware.smackx/experimental.xml";
    }
}

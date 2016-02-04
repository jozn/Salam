package org.jivesoftware.smack.initializer.extensions;

import org.jivesoftware.smack.initializer.UrlInitializer;

public class ExtensionsInitializer extends UrlInitializer {
    protected final String getProvidersUrl() {
        return "classpath:org.jivesoftware.smackx/extensions.providers";
    }

    protected final String getConfigUrl() {
        return "classpath:org.jivesoftware.smackx/extensions.xml";
    }
}

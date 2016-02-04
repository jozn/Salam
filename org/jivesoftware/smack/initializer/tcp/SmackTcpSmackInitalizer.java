package org.jivesoftware.smack.initializer.tcp;

import org.jivesoftware.smack.initializer.UrlInitializer;

public class SmackTcpSmackInitalizer extends UrlInitializer {
    protected final String getProvidersUrl() {
        return "classpath:org.jivesoftware.smack/smacktcp.providers";
    }
}

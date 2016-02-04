package org.jivesoftware.smack.initializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.SmackInitialization;
import org.jivesoftware.smack.provider.ProviderFileLoader;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.FileUtils;

public abstract class UrlInitializer extends SmackAndOsgiInitializer {
    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(UrlInitializer.class.getName());
    }

    public List<Exception> initialize() {
        return initialize(getClass().getClassLoader());
    }

    public final List<Exception> initialize(ClassLoader classLoader) {
        List<Exception> exceptions = new LinkedList();
        String providerUrl = getProvidersUrl();
        if (providerUrl != null) {
            try {
                InputStream is = FileUtils.getStreamForUrl(providerUrl, classLoader);
                if (is != null) {
                    LOGGER.log(Level.FINE, "Loading providers for providerUrl [" + providerUrl + "]");
                    ProviderFileLoader pfl = new ProviderFileLoader(is, classLoader);
                    ProviderManager.addLoader(pfl);
                    exceptions.addAll(Collections.unmodifiableList(pfl.exceptions));
                } else {
                    LOGGER.log(Level.WARNING, "No input stream created for " + providerUrl);
                    exceptions.add(new IOException("No input stream created for " + providerUrl));
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error trying to load provider file " + providerUrl, e);
                exceptions.add(e);
            }
        }
        String configUrl = getConfigUrl();
        if (configUrl != null) {
            try {
                SmackInitialization.processConfigFile(FileUtils.getStreamForUrl(configUrl, classLoader), exceptions, classLoader);
            } catch (Exception e2) {
                exceptions.add(e2);
            }
        }
        return exceptions;
    }

    public String getProvidersUrl() {
        return null;
    }

    public String getConfigUrl() {
        return null;
    }
}

package org.jivesoftware.smack.sasl.provided;

import java.util.List;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.initializer.SmackAndOsgiInitializer;

public class SASLProvidedSmackInitializer extends SmackAndOsgiInitializer {
    public final List<Exception> initialize() {
        SASLAuthentication.registerSASLMechanism(new SASLDigestMD5Mechanism());
        SASLAuthentication.registerSASLMechanism(new SASLExternalMechanism());
        SASLAuthentication.registerSASLMechanism(new SASLPlainMechanism());
        return null;
    }

    public final List<Exception> initialize(ClassLoader classLoader) {
        return initialize();
    }
}

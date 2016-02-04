package org.jivesoftware.smack.sasl;

import java.util.HashMap;
import java.util.Map;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.sasl.packet.SaslStreamElements.SASLFailure;

public final class SASLErrorException extends XMPPException {
    private final String mechanism;
    private final SASLFailure saslFailure;
    private final Map<String, String> texts;

    public SASLErrorException(String mechanism, SASLFailure saslFailure) {
        this(mechanism, saslFailure, new HashMap());
    }

    private SASLErrorException(String mechanism, SASLFailure saslFailure, Map<String, String> texts) {
        super("SASLError using " + mechanism + ": " + saslFailure.saslErrorString);
        this.mechanism = mechanism;
        this.saslFailure = saslFailure;
        this.texts = texts;
    }
}

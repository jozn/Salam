package org.jivesoftware.smack.sasl;

import org.jivesoftware.smack.SmackException;

public final class SASLAnonymous extends SASLMechanism {
    public final /* bridge */ /* synthetic */ SASLMechanism newInstance() {
        return new SASLAnonymous();
    }

    public final String getName() {
        return "ANONYMOUS";
    }

    public final int getPriority() {
        return 500;
    }

    protected final void authenticateInternal$4e2ceb8() throws SmackException {
    }

    protected final byte[] getAuthenticationText() throws SmackException {
        return null;
    }
}

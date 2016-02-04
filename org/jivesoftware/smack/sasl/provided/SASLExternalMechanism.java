package org.jivesoftware.smack.sasl.provided;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.util.StringUtils;
import org.jxmpp.util.XmppStringUtils;

public final class SASLExternalMechanism extends SASLMechanism {
    protected final void authenticateInternal$4e2ceb8() throws SmackException {
        throw new UnsupportedOperationException("CallbackHandler not (yet) supported");
    }

    protected final byte[] getAuthenticationText() throws SmackException {
        if (this.authenticationId == null) {
            return null;
        }
        return StringUtils.toBytes(XmppStringUtils.completeJidFrom(this.authenticationId, this.serviceName));
    }

    public final String getName() {
        return "EXTERNAL";
    }

    public final int getPriority() {
        return 510;
    }

    protected final SASLMechanism newInstance() {
        return new SASLExternalMechanism();
    }
}

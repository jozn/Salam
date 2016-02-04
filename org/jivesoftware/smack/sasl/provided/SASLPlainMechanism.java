package org.jivesoftware.smack.sasl.provided;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.util.ByteUtils;
import org.jivesoftware.smack.util.StringUtils;

public final class SASLPlainMechanism extends SASLMechanism {
    public final /* bridge */ /* synthetic */ SASLMechanism newInstance() {
        return new SASLPlainMechanism();
    }

    protected final void authenticateInternal$4e2ceb8() throws SmackException {
        throw new UnsupportedOperationException("CallbackHandler not (yet) supported");
    }

    protected final byte[] getAuthenticationText() throws SmackException {
        return ByteUtils.concact(StringUtils.toBytes("\u0000" + this.authenticationId), StringUtils.toBytes("\u0000" + this.password));
    }

    public final String getName() {
        return "PLAIN";
    }

    public final int getPriority() {
        return 410;
    }
}

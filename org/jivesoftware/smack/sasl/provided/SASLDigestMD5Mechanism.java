package org.jivesoftware.smack.sasl.provided;

import android.support.v7.appcompat.BuildConfig;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.util.ByteUtils;
import org.jivesoftware.smack.util.StringUtils;

public class SASLDigestMD5Mechanism extends SASLMechanism {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static boolean verifyServerResponse;
    private String cnonce;
    private String digestUri;
    private String hex_hashed_a1;
    private String nonce;
    private int state$6a4c5dbf;

    /* renamed from: org.jivesoftware.smack.sasl.provided.SASLDigestMD5Mechanism.1 */
    static /* synthetic */ class C12881 {
        static final /* synthetic */ int[] f40xa1fe489b;

        static {
            f40xa1fe489b = new int[State.values$46149379().length];
            try {
                f40xa1fe489b[State.INITIAL$6a4c5dbf - 1] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f40xa1fe489b[State.RESPONSE_SENT$6a4c5dbf - 1] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    private enum DigestType {
        ;

        static {
            ClientResponse$57bb22c6 = 1;
            ServerResponse$57bb22c6 = 2;
            $VALUES$18f64021 = new int[]{ClientResponse$57bb22c6, ServerResponse$57bb22c6};
        }
    }

    private enum State {
        ;

        public static int[] values$46149379() {
            return (int[]) $VALUES$30cc5806.clone();
        }

        static {
            INITIAL$6a4c5dbf = 1;
            RESPONSE_SENT$6a4c5dbf = 2;
            VALID_SERVER_RESPONSE$6a4c5dbf = 3;
            $VALUES$30cc5806 = new int[]{INITIAL$6a4c5dbf, RESPONSE_SENT$6a4c5dbf, VALID_SERVER_RESPONSE$6a4c5dbf};
        }
    }

    static {
        boolean z;
        if (SASLDigestMD5Mechanism.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
        verifyServerResponse = true;
    }

    public SASLDigestMD5Mechanism() {
        this.state$6a4c5dbf = State.INITIAL$6a4c5dbf;
    }

    public final /* bridge */ /* synthetic */ SASLMechanism newInstance() {
        return new SASLDigestMD5Mechanism();
    }

    protected final void authenticateInternal$4e2ceb8() throws SmackException {
        throw new UnsupportedOperationException("CallbackHandler not (yet) supported");
    }

    protected final byte[] getAuthenticationText() throws SmackException {
        return null;
    }

    public final String getName() {
        return "DIGEST-MD5";
    }

    public final int getPriority() {
        return 210;
    }

    protected final byte[] evaluateChallenge(byte[] challenge) throws SmackException {
        if (challenge.length == 0) {
            throw new SmackException("Initial challenge has zero length");
        }
        String[] challengeParts = new String(challenge).split(",");
        String[] arr$;
        int len$;
        int i$;
        String[] keyValue;
        String key;
        String value;
        switch (C12881.f40xa1fe489b[this.state$6a4c5dbf - 1]) {
            case Logger.SEVERE /*1*/:
                arr$ = challengeParts;
                len$ = challengeParts.length;
                i$ = 0;
                while (i$ < len$) {
                    keyValue = arr$[i$].split("=");
                    if ($assertionsDisabled || keyValue.length == 2) {
                        key = keyValue[0];
                        value = keyValue[1];
                        if ("nonce".equals(key)) {
                            if (this.nonce != null) {
                                throw new SmackException("Nonce value present multiple times");
                            }
                            this.nonce = value.replace("\"", BuildConfig.VERSION_NAME);
                        } else if ("qop".equals(key)) {
                            value = value.replace("\"", BuildConfig.VERSION_NAME);
                            if (!value.equals("auth")) {
                                throw new SmackException("Unsupported qop operation: " + value);
                            }
                        } else {
                            continue;
                        }
                        i$++;
                    } else {
                        throw new AssertionError();
                    }
                }
                if (this.nonce == null) {
                    throw new SmackException("nonce value not present in initial challenge");
                }
                byte[] a1FirstPart = ByteUtils.md5(StringUtils.toBytes(this.authenticationId + ':' + this.serviceName + ':' + this.password));
                this.cnonce = StringUtils.randomString(32);
                byte[] a1 = ByteUtils.concact(a1FirstPart, StringUtils.toBytes(":" + this.nonce + ':' + this.cnonce));
                this.digestUri = "xmpp/" + this.serviceName;
                this.hex_hashed_a1 = StringUtils.encodeHex(ByteUtils.md5(a1));
                byte[] response = StringUtils.toBytes("username=\"" + this.authenticationId + '\"' + ",realm=\"" + this.serviceName + '\"' + ",nonce=\"" + this.nonce + '\"' + ",cnonce=\"" + this.cnonce + '\"' + ",nc=00000001,qop=auth,digest-uri=\"" + this.digestUri + '\"' + ",response=" + calcResponse$596a9abb(DigestType.ClientResponse$57bb22c6) + ",charset=utf-8");
                this.state$6a4c5dbf = State.RESPONSE_SENT$6a4c5dbf;
                return response;
            case Logger.WARNING /*2*/:
                if (verifyServerResponse) {
                    String serverResponse = null;
                    arr$ = challengeParts;
                    len$ = challengeParts.length;
                    i$ = 0;
                    while (i$ < len$) {
                        keyValue = arr$[i$].split("=");
                        if ($assertionsDisabled || keyValue.length == 2) {
                            key = keyValue[0];
                            value = keyValue[1];
                            if ("rspauth".equals(key)) {
                                serverResponse = value;
                                if (serverResponse == null) {
                                    throw new SmackException("No server response received while performing DIGEST-MD5 authentication");
                                } else if (!serverResponse.equals(calcResponse$596a9abb(DigestType.ServerResponse$57bb22c6))) {
                                    throw new SmackException("Invalid server response  while performing DIGEST-MD5 authentication");
                                }
                            }
                            i$++;
                        } else {
                            throw new AssertionError();
                        }
                    }
                    if (serverResponse == null) {
                        throw new SmackException("No server response received while performing DIGEST-MD5 authentication");
                    } else if (serverResponse.equals(calcResponse$596a9abb(DigestType.ServerResponse$57bb22c6))) {
                        throw new SmackException("Invalid server response  while performing DIGEST-MD5 authentication");
                    }
                }
                this.state$6a4c5dbf = State.VALID_SERVER_RESPONSE$6a4c5dbf;
                return null;
            default:
                throw new IllegalStateException();
        }
    }

    private String calcResponse$596a9abb(int digestType) {
        StringBuilder a2 = new StringBuilder();
        if (digestType == DigestType.ClientResponse$57bb22c6) {
            a2.append("AUTHENTICATE");
        }
        a2.append(':');
        a2.append(this.digestUri);
        String hex_hashed_a2 = StringUtils.encodeHex(ByteUtils.md5(StringUtils.toBytes(a2.toString())));
        StringBuilder kd_argument = new StringBuilder();
        kd_argument.append(this.hex_hashed_a1);
        kd_argument.append(':');
        kd_argument.append(this.nonce);
        kd_argument.append(':');
        kd_argument.append("00000001");
        kd_argument.append(':');
        kd_argument.append(this.cnonce);
        kd_argument.append(':');
        kd_argument.append("auth");
        kd_argument.append(':');
        kd_argument.append(hex_hashed_a2);
        return StringUtils.encodeHex(ByteUtils.md5(StringUtils.toBytes(kd_argument.toString())));
    }
}

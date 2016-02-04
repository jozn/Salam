package cz.msebera.android.httpclient.impl.auth;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import cz.msebera.android.httpclient.Consts;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpEntityEnclosingRequest;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.auth.AuthenticationException;
import cz.msebera.android.httpclient.auth.Credentials;
import cz.msebera.android.httpclient.auth.MalformedChallengeException;
import cz.msebera.android.httpclient.message.BasicHeaderValueFormatter;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.message.BufferedHeader;
import cz.msebera.android.httpclient.protocol.BasicHttpContext;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.CharArrayBuffer;
import cz.msebera.android.httpclient.util.EncodingUtils;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

public final class DigestScheme extends RFC2617Scheme {
    private static final char[] HEXADECIMAL;
    private String a1;
    private String a2;
    private String cnonce;
    private boolean complete;
    private String lastNonce;
    private long nounceCount;

    static {
        HEXADECIMAL = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    }

    public DigestScheme(Charset credentialsCharset) {
        super(credentialsCharset);
        this.complete = false;
    }

    public DigestScheme() {
        this(Consts.ASCII);
    }

    public final void processChallenge(Header header) throws MalformedChallengeException {
        super.processChallenge(header);
        this.complete = true;
        if (this.params.isEmpty()) {
            throw new MalformedChallengeException("Authentication challenge is empty");
        }
    }

    public final boolean isComplete() {
        if ("true".equalsIgnoreCase(getParameter("stale"))) {
            return false;
        }
        return this.complete;
    }

    public final String getSchemeName() {
        return "digest";
    }

    public final boolean isConnectionBased() {
        return false;
    }

    @Deprecated
    public final Header authenticate(Credentials credentials, HttpRequest request) throws AuthenticationException {
        BasicHttpContext basicHttpContext = new BasicHttpContext();
        return authenticate$f1343fe(credentials, request);
    }

    public final Header authenticate$f1343fe(Credentials credentials, HttpRequest request) throws AuthenticationException {
        Args.notNull(credentials, "Credentials");
        Args.notNull(request, "HTTP request");
        if (getParameter("realm") == null) {
            throw new AuthenticationException("missing realm in challenge");
        } else if (getParameter("nonce") == null) {
            throw new AuthenticationException("missing nonce in challenge");
        } else {
            this.params.put("methodname", request.getRequestLine().getMethod());
            this.params.put("uri", request.getRequestLine().getUri());
            if (getParameter("charset") == null) {
                this.params.put("charset", getCredentialsCharset(request));
            }
            return createDigestHeader(credentials, request);
        }
    }

    private static MessageDigest createMessageDigest(String digAlg) throws UnsupportedDigestAlgorithmException {
        try {
            return MessageDigest.getInstance(digAlg);
        } catch (Exception e) {
            throw new UnsupportedDigestAlgorithmException("Unsupported algorithm in HTTP Digest authentication: " + digAlg);
        }
    }

    private Header createDigestHeader(Credentials credentials, HttpRequest request) throws AuthenticationException {
        String uri = getParameter("uri");
        String realm = getParameter("realm");
        String nonce = getParameter("nonce");
        String opaque = getParameter("opaque");
        String method = getParameter("methodname");
        String algorithm = getParameter("algorithm");
        if (algorithm == null) {
            algorithm = "MD5";
        }
        Set<String> hashSet = new HashSet(8);
        int qop = -1;
        String qoplist = getParameter("qop");
        if (qoplist != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(qoplist, ",");
            while (stringTokenizer.hasMoreTokens()) {
                hashSet.add(stringTokenizer.nextToken().trim().toLowerCase(Locale.ROOT));
            }
            if (request instanceof HttpEntityEnclosingRequest) {
                if (hashSet.contains("auth-int")) {
                    qop = 1;
                }
            }
            if (hashSet.contains("auth")) {
                qop = 2;
            }
        } else {
            qop = 0;
        }
        if (qop == -1) {
            throw new AuthenticationException("None of the qop methods is supported: " + qoplist);
        }
        String charset = getParameter("charset");
        if (charset == null) {
            charset = "ISO-8859-1";
        }
        String digAlg = algorithm;
        if (algorithm.equalsIgnoreCase("MD5-sess")) {
            digAlg = "MD5";
        }
        try {
            String digestValue;
            MessageDigest digester = createMessageDigest(digAlg);
            String uname = credentials.getUserPrincipal().getName();
            String pwd = credentials.getPassword();
            if (nonce.equals(this.lastNonce)) {
                this.nounceCount++;
            } else {
                this.nounceCount = 1;
                this.cnonce = null;
                this.lastNonce = nonce;
            }
            Appendable stringBuilder = new StringBuilder(AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY);
            Formatter formatter = new Formatter(stringBuilder, Locale.US);
            Long[] lArr = new Object[1];
            lArr[0] = Long.valueOf(this.nounceCount);
            formatter.format("%08x", lArr);
            formatter.close();
            String nc = stringBuilder.toString();
            if (this.cnonce == null) {
                byte[] bArr = new byte[8];
                new SecureRandom().nextBytes(bArr);
                this.cnonce = encode(bArr);
            }
            this.a1 = null;
            this.a2 = null;
            if (algorithm.equalsIgnoreCase("MD5-sess")) {
                stringBuilder.setLength(0);
                stringBuilder.append(uname).append(':').append(realm).append(':').append(pwd);
                String checksum = encode(digester.digest(EncodingUtils.getBytes(stringBuilder.toString(), charset)));
                stringBuilder.setLength(0);
                stringBuilder.append(checksum).append(':').append(nonce).append(':').append(this.cnonce);
                this.a1 = stringBuilder.toString();
            } else {
                stringBuilder.setLength(0);
                stringBuilder.append(uname).append(':').append(realm).append(':').append(pwd);
                this.a1 = stringBuilder.toString();
            }
            String hasha1 = encode(digester.digest(EncodingUtils.getBytes(this.a1, charset)));
            if (qop == 2) {
                this.a2 = method + ':' + uri;
            } else if (qop == 1) {
                HttpEntity entity = null;
                if (request instanceof HttpEntityEnclosingRequest) {
                    entity = ((HttpEntityEnclosingRequest) request).getEntity();
                }
                if (entity == null || entity.isRepeatable()) {
                    HttpEntityDigester entityDigester = new HttpEntityDigester(digester);
                    if (entity != null) {
                        try {
                            entity.writeTo(entityDigester);
                        } catch (IOException ex) {
                            throw new AuthenticationException("I/O error reading entity content", ex);
                        }
                    }
                    entityDigester.close();
                    this.a2 = method + ':' + uri + ':' + encode(entityDigester.digest);
                } else {
                    if (hashSet.contains("auth")) {
                        qop = 2;
                        this.a2 = method + ':' + uri;
                    } else {
                        throw new AuthenticationException("Qop auth-int cannot be used with a non-repeatable entity");
                    }
                }
            } else {
                this.a2 = method + ':' + uri;
            }
            String hasha2 = encode(digester.digest(EncodingUtils.getBytes(this.a2, charset)));
            if (qop == 0) {
                stringBuilder.setLength(0);
                stringBuilder.append(hasha1).append(':').append(nonce).append(':').append(hasha2);
                digestValue = stringBuilder.toString();
            } else {
                stringBuilder.setLength(0);
                stringBuilder.append(hasha1).append(':').append(nonce).append(':').append(nc).append(':').append(this.cnonce).append(':').append(qop == 1 ? "auth-int" : "auth").append(':').append(hasha2);
                digestValue = stringBuilder.toString();
            }
            Args.notNull(digestValue, "Input");
            String digest = encode(digester.digest(digestValue.getBytes(Consts.ASCII)));
            CharArrayBuffer buffer = new CharArrayBuffer(AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
            if (isProxy()) {
                buffer.append("Proxy-Authorization");
            } else {
                buffer.append("Authorization");
            }
            buffer.append(": Digest ");
            List<BasicNameValuePair> arrayList = new ArrayList(20);
            arrayList.add(new BasicNameValuePair("username", uname));
            arrayList.add(new BasicNameValuePair("realm", realm));
            arrayList.add(new BasicNameValuePair("nonce", nonce));
            arrayList.add(new BasicNameValuePair("uri", uri));
            arrayList.add(new BasicNameValuePair("response", digest));
            if (qop != 0) {
                arrayList.add(new BasicNameValuePair("qop", qop == 1 ? "auth-int" : "auth"));
                arrayList.add(new BasicNameValuePair("nc", nc));
                arrayList.add(new BasicNameValuePair("cnonce", this.cnonce));
            }
            arrayList.add(new BasicNameValuePair("algorithm", algorithm));
            if (opaque != null) {
                arrayList.add(new BasicNameValuePair("opaque", opaque));
            }
            for (int i = 0; i < arrayList.size(); i++) {
                boolean noQuotes;
                BasicHeaderValueFormatter basicHeaderValueFormatter;
                boolean z;
                BasicNameValuePair param = (BasicNameValuePair) arrayList.get(i);
                if (i > 0) {
                    buffer.append(", ");
                }
                String name = param.name;
                if (!"nc".equals(name)) {
                    if (!"qop".equals(name)) {
                        if (!"algorithm".equals(name)) {
                            noQuotes = false;
                            basicHeaderValueFormatter = BasicHeaderValueFormatter.INSTANCE;
                            if (noQuotes) {
                                z = true;
                            } else {
                                z = false;
                            }
                            BasicHeaderValueFormatter.formatNameValuePair(buffer, param, z);
                        }
                    }
                }
                noQuotes = true;
                basicHeaderValueFormatter = BasicHeaderValueFormatter.INSTANCE;
                if (noQuotes) {
                    z = false;
                } else {
                    z = true;
                }
                BasicHeaderValueFormatter.formatNameValuePair(buffer, param, z);
            }
            return new BufferedHeader(buffer);
        } catch (UnsupportedDigestAlgorithmException e) {
            throw new AuthenticationException("Unsuppported digest algorithm: " + digAlg);
        }
    }

    private static String encode(byte[] binaryData) {
        int n = binaryData.length;
        char[] buffer = new char[(n * 2)];
        for (int i = 0; i < n; i++) {
            int low = binaryData[i] & 15;
            buffer[i * 2] = HEXADECIMAL[(binaryData[i] & 240) >> 4];
            buffer[(i * 2) + 1] = HEXADECIMAL[low];
        }
        return new String(buffer);
    }

    public final String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DIGEST [complete=").append(this.complete).append(", nonce=").append(this.lastNonce).append(", nc=").append(this.nounceCount).append("]");
        return builder.toString();
    }
}

package cz.msebera.android.httpclient.conn.ssl;

import cz.msebera.android.httpclient.conn.util.InetAddressUtils;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import cz.msebera.android.httpclient.util.Args;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

@Deprecated
public abstract class AbstractVerifier implements X509HostnameVerifier {
    static final String[] BAD_COUNTRY_2LDS;
    public HttpClientAndroidLog log;

    public AbstractVerifier() {
        this.log = new HttpClientAndroidLog(getClass());
    }

    static {
        String[] strArr = new String[]{"ac", "co", "com", "ed", "edu", "go", "gouv", "gov", "info", "lg", "ne", "net", "or", "org"};
        BAD_COUNTRY_2LDS = strArr;
        Arrays.sort(strArr);
    }

    public final void verify(String host, SSLSocket ssl) throws IOException {
        Args.notNull(host, "Host");
        SSLSession session = ssl.getSession();
        if (session == null) {
            ssl.getInputStream().available();
            session = ssl.getSession();
            if (session == null) {
                ssl.startHandshake();
                session = ssl.getSession();
            }
        }
        verify(host, session.getPeerCertificates()[0]);
    }

    public final boolean verify(String host, SSLSession session) {
        try {
            verify(host, session.getPeerCertificates()[0]);
            return true;
        } catch (SSLException ex) {
            if (!this.log.debugEnabled) {
                return false;
            }
            this.log.debug(ex.getMessage(), ex);
            return false;
        }
    }

    private void verify(String host, X509Certificate cert) throws SSLException {
        String[] strArr = null;
        int subjectType = (InetAddressUtils.isIPv4Address(host) || InetAddressUtils.isIPv6Address(host)) ? 7 : 2;
        List<String> subjectAlts = DefaultHostnameVerifier.extractSubjectAlts(cert, subjectType);
        String[] strArr2 = new DistinguishedNameParser(cert.getSubjectX500Principal()).findMostSpecific("cn") != null ? new String[]{new DistinguishedNameParser(cert.getSubjectX500Principal()).findMostSpecific("cn")} : null;
        if (!(subjectAlts == null || subjectAlts.isEmpty())) {
            strArr = (String[]) subjectAlts.toArray(new String[subjectAlts.size()]);
        }
        verify(host, strArr2, strArr);
    }

    public static void verify(String host, String[] cns, String[] subjectAlts, boolean strictWithSubDomains) throws SSLException {
        String cn;
        String normalizedHost;
        List<String> subjectAltList = null;
        if (cns == null || cns.length <= 0) {
            cn = null;
        } else {
            cn = cns[0];
        }
        if (subjectAlts != null && subjectAlts.length > 0) {
            subjectAltList = Arrays.asList(subjectAlts);
        }
        if (InetAddressUtils.isIPv6Address(host)) {
            normalizedHost = DefaultHostnameVerifier.normaliseAddress(host.toLowerCase(Locale.ROOT));
        } else {
            normalizedHost = host;
        }
        if (subjectAltList != null) {
            for (String subjectAlt : subjectAltList) {
                String normalizedAltSubject;
                if (InetAddressUtils.isIPv6Address(subjectAlt)) {
                    normalizedAltSubject = DefaultHostnameVerifier.normaliseAddress(subjectAlt);
                } else {
                    normalizedAltSubject = subjectAlt;
                }
                if (matchIdentity(normalizedHost, normalizedAltSubject, strictWithSubDomains)) {
                    return;
                }
            }
            throw new SSLException("Certificate for <" + host + "> doesn't match any of the subject alternative names: " + subjectAltList);
        } else if (cn != null) {
            String normalizedCN;
            if (InetAddressUtils.isIPv6Address(cn)) {
                normalizedCN = DefaultHostnameVerifier.normaliseAddress(cn);
            } else {
                normalizedCN = cn;
            }
            if (!matchIdentity(normalizedHost, normalizedCN, strictWithSubDomains)) {
                throw new SSLException("Certificate for <" + host + "> doesn't match common name of the certificate subject: " + cn);
            }
        } else {
            throw new SSLException("Certificate subject for <" + host + "> doesn't contain a common name and does not have alternative names");
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean matchIdentity(java.lang.String r10, java.lang.String r11, boolean r12) {
        /*
        if (r10 != 0) goto L_0x0004;
    L_0x0002:
        r8 = 0;
    L_0x0003:
        return r8;
    L_0x0004:
        r8 = java.util.Locale.ROOT;
        r3 = r10.toLowerCase(r8);
        r8 = java.util.Locale.ROOT;
        r4 = r11.toLowerCase(r8);
        r8 = "\\.";
        r5 = r4.split(r8);
        r8 = r5.length;
        r9 = 3;
        if (r8 < r9) goto L_0x008c;
    L_0x001a:
        r8 = 0;
        r8 = r5[r8];
        r9 = "*";
        r8 = r8.endsWith(r9);
        if (r8 == 0) goto L_0x008c;
    L_0x0025:
        if (r12 == 0) goto L_0x0038;
    L_0x0027:
        r8 = r5.length;
        r9 = 3;
        if (r8 != r9) goto L_0x0035;
    L_0x002b:
        r8 = 2;
        r8 = r5[r8];
        r8 = r8.length();
        r9 = 2;
        if (r8 == r9) goto L_0x007d;
    L_0x0035:
        r8 = 1;
    L_0x0036:
        if (r8 == 0) goto L_0x008c;
    L_0x0038:
        r8 = 1;
    L_0x0039:
        if (r8 == 0) goto L_0x009d;
    L_0x003b:
        r8 = 0;
        r0 = r5[r8];
        r8 = r0.length();
        r9 = 1;
        if (r8 <= r9) goto L_0x0090;
    L_0x0045:
        r8 = 0;
        r9 = r0.length();
        r9 = r9 + -1;
        r6 = r0.substring(r8, r9);
        r8 = r0.length();
        r7 = r4.substring(r8);
        r8 = r6.length();
        r1 = r3.substring(r8);
        r8 = r3.startsWith(r6);
        if (r8 == 0) goto L_0x008e;
    L_0x0066:
        r8 = r1.endsWith(r7);
        if (r8 == 0) goto L_0x008e;
    L_0x006c:
        r2 = 1;
    L_0x006d:
        if (r2 == 0) goto L_0x009a;
    L_0x006f:
        if (r12 == 0) goto L_0x007b;
    L_0x0071:
        r8 = countDots(r3);
        r9 = countDots(r4);
        if (r8 != r9) goto L_0x009a;
    L_0x007b:
        r8 = 1;
        goto L_0x0003;
    L_0x007d:
        r8 = BAD_COUNTRY_2LDS;
        r9 = 1;
        r9 = r5[r9];
        r8 = java.util.Arrays.binarySearch(r8, r9);
        if (r8 >= 0) goto L_0x008a;
    L_0x0088:
        r8 = 1;
        goto L_0x0036;
    L_0x008a:
        r8 = 0;
        goto L_0x0036;
    L_0x008c:
        r8 = 0;
        goto L_0x0039;
    L_0x008e:
        r2 = 0;
        goto L_0x006d;
    L_0x0090:
        r8 = 1;
        r8 = r4.substring(r8);
        r2 = r3.endsWith(r8);
        goto L_0x006d;
    L_0x009a:
        r8 = 0;
        goto L_0x0003;
    L_0x009d:
        r8 = r3.equals(r4);
        goto L_0x0003;
        */
        throw new UnsupportedOperationException("Method not decompiled: cz.msebera.android.httpclient.conn.ssl.AbstractVerifier.matchIdentity(java.lang.String, java.lang.String, boolean):boolean");
    }

    private static int countDots(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '.') {
                count++;
            }
        }
        return count;
    }
}

package cz.msebera.android.httpclient.conn.ssl;

import cz.msebera.android.httpclient.conn.util.InetAddressUtils;
import cz.msebera.android.httpclient.conn.util.PublicSuffixMatcher;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import java.net.IDN;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;

public final class DefaultHostnameVerifier implements HostnameVerifier {
    public HttpClientAndroidLog log;
    private final PublicSuffixMatcher publicSuffixMatcher;

    public final boolean verify(String host, SSLSession session) {
        try {
            X509Certificate x509 = session.getPeerCertificates()[0];
            boolean isIPv4Address = InetAddressUtils.isIPv4Address(host);
            boolean isIPv6Address = InetAddressUtils.isIPv6Address(host);
            int i = (isIPv4Address || isIPv6Address) ? 7 : 2;
            List extractSubjectAlts = extractSubjectAlts(x509, i);
            if (extractSubjectAlts == null || extractSubjectAlts.isEmpty()) {
                String findMostSpecific = new DistinguishedNameParser(x509.getSubjectX500Principal()).findMostSpecific("cn");
                if (findMostSpecific == null) {
                    throw new SSLException("Certificate subject for <" + host + "> doesn't contain a common name and does not have alternative names");
                } else if (!matchIdentityStrict(host, findMostSpecific, this.publicSuffixMatcher)) {
                    throw new SSLException("Certificate for <" + host + "> doesn't match common name of the certificate subject: " + findMostSpecific);
                }
            } else if (isIPv4Address) {
                int i2 = 0;
                while (i2 < extractSubjectAlts.size()) {
                    if (!host.equals((String) extractSubjectAlts.get(i2))) {
                        i2++;
                    }
                }
                throw new SSLException("Certificate for <" + host + "> doesn't match any of the subject alternative names: " + extractSubjectAlts);
            } else if (isIPv6Address) {
                matchIPv6Address(host, extractSubjectAlts);
            } else {
                matchDNSName(host, extractSubjectAlts, this.publicSuffixMatcher);
            }
            return true;
        } catch (SSLException ex) {
            if (this.log.debugEnabled) {
                this.log.debug(ex.getMessage(), ex);
            }
            return false;
        }
    }

    private static void matchIPv6Address(String host, List<String> subjectAlts) throws SSLException {
        String normalisedHost = normaliseAddress(host);
        int i = 0;
        while (i < subjectAlts.size()) {
            if (!normalisedHost.equals(normaliseAddress((String) subjectAlts.get(i)))) {
                i++;
            } else {
                return;
            }
        }
        throw new SSLException("Certificate for <" + host + "> doesn't match any of the subject alternative names: " + subjectAlts);
    }

    private static void matchDNSName(String host, List<String> subjectAlts, PublicSuffixMatcher publicSuffixMatcher) throws SSLException {
        String normalizedHost = host.toLowerCase(Locale.ROOT);
        int i = 0;
        while (i < subjectAlts.size()) {
            if (!matchIdentityStrict(normalizedHost, ((String) subjectAlts.get(i)).toLowerCase(Locale.ROOT), publicSuffixMatcher)) {
                i++;
            } else {
                return;
            }
        }
        throw new SSLException("Certificate for <" + host + "> doesn't match any of the subject alternative names: " + subjectAlts);
    }

    private static boolean matchIdentityStrict(String host, String identity, PublicSuffixMatcher publicSuffixMatcher) {
        String str;
        String str2;
        int i;
        if (publicSuffixMatcher != null && host.contains(".")) {
            if (identity == null) {
                str = null;
            } else if (identity.startsWith(".")) {
                str = null;
            } else {
                str = identity.toLowerCase(Locale.ROOT);
                str2 = null;
                while (str != null) {
                    if (publicSuffixMatcher.exceptions == null || !publicSuffixMatcher.exceptions.containsKey(IDN.toUnicode(str))) {
                        if (!publicSuffixMatcher.rules.containsKey(IDN.toUnicode(str))) {
                            int indexOf = str.indexOf(46);
                            String substring = indexOf != -1 ? str.substring(indexOf + 1) : null;
                            if (substring != null && publicSuffixMatcher.rules.containsKey("*." + IDN.toUnicode(substring))) {
                                break;
                            }
                            if (indexOf == -1) {
                                str = str2;
                            }
                            str2 = str;
                            str = substring;
                        } else {
                            break;
                        }
                    }
                    break;
                }
                str = str2;
            }
            i = (str != null && host.endsWith(str) && (host.length() == str.length() || host.charAt((host.length() - str.length()) - 1) == '.')) ? 1 : 0;
            if (i == 0) {
                return false;
            }
        }
        i = identity.indexOf(42);
        if (i == -1) {
            return host.equalsIgnoreCase(identity);
        }
        str2 = identity.substring(0, i);
        str = identity.substring(i + 1);
        if (!str2.isEmpty() && !host.startsWith(str2)) {
            return false;
        }
        if (str.isEmpty() || host.endsWith(str)) {
            return !host.substring(str2.length(), host.length() - str.length()).contains(".");
        } else {
            return false;
        }
    }

    static List<String> extractSubjectAlts(X509Certificate cert, int subjectType) {
        Collection<List<?>> c = null;
        try {
            c = cert.getSubjectAlternativeNames();
        } catch (CertificateParsingException e) {
        }
        List<String> subjectAltList = null;
        if (c != null) {
            for (List<?> list : c) {
                if (((Integer) list.get(0)).intValue() == subjectType) {
                    String s = (String) list.get(1);
                    if (subjectAltList == null) {
                        subjectAltList = new ArrayList();
                    }
                    subjectAltList.add(s);
                }
            }
        }
        return subjectAltList;
    }

    static String normaliseAddress(String hostname) {
        if (hostname != null) {
            try {
                hostname = InetAddress.getByName(hostname).getHostAddress();
            } catch (UnknownHostException e) {
            }
        }
        return hostname;
    }
}

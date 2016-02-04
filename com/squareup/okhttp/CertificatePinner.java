package com.squareup.okhttp;

import com.squareup.okhttp.internal.Util;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLPeerUnverifiedException;
import okio.Base64;
import okio.ByteString;

public final class CertificatePinner {
    public static final CertificatePinner DEFAULT;
    private final Map<String, List<ByteString>> hostnameToPins;

    public static final class Builder {
        private final Map<String, List<ByteString>> hostnameToPins;

        public Builder() {
            this.hostnameToPins = new LinkedHashMap();
        }

        public final Builder add(String hostname, String... pins) {
            if (hostname == null) {
                throw new IllegalArgumentException("hostname == null");
            }
            List<ByteString> hostPins = new ArrayList();
            List<ByteString> previousPins = (List) this.hostnameToPins.put(hostname, Collections.unmodifiableList(hostPins));
            if (previousPins != null) {
                hostPins.addAll(previousPins);
            }
            int length = pins.length;
            int i = 0;
            while (i < length) {
                String pin = pins[i];
                if (pin.startsWith("sha1/")) {
                    ByteString decodedPin = ByteString.decodeBase64(pin.substring(5));
                    if (decodedPin == null) {
                        throw new IllegalArgumentException("pins must be base64: " + pin);
                    }
                    hostPins.add(decodedPin);
                    i++;
                } else {
                    throw new IllegalArgumentException("pins must start with 'sha1/': " + pin);
                }
            }
            return this;
        }

        public final CertificatePinner build() {
            return new CertificatePinner();
        }
    }

    static {
        DEFAULT = new Builder().build();
    }

    private CertificatePinner(Builder builder) {
        this.hostnameToPins = Util.immutableMap(builder.hostnameToPins);
    }

    public final void check(String hostname, List<Certificate> peerCertificates) throws SSLPeerUnverifiedException {
        List<ByteString> pins = (List) this.hostnameToPins.get(hostname);
        if (pins != null) {
            int i = 0;
            int size = peerCertificates.size();
            while (i < size) {
                if (!pins.contains(sha1((X509Certificate) peerCertificates.get(i)))) {
                    i++;
                } else {
                    return;
                }
            }
            StringBuilder message = new StringBuilder("Certificate pinning failure!\n  Peer certificate chain:");
            size = peerCertificates.size();
            for (i = 0; i < size; i++) {
                X509Certificate x509Certificate = (X509Certificate) peerCertificates.get(i);
                message.append("\n    ").append(pin(x509Certificate)).append(": ").append(x509Certificate.getSubjectDN().getName());
            }
            message.append("\n  Pinned certificates for ").append(hostname).append(":");
            size = pins.size();
            for (i = 0; i < size; i++) {
                message.append("\n    sha1/").append(Base64.encode(((ByteString) pins.get(i)).data));
            }
            throw new SSLPeerUnverifiedException(message.toString());
        }
    }

    public final void check(String hostname, Certificate... peerCertificates) throws SSLPeerUnverifiedException {
        check(hostname, Arrays.asList(peerCertificates));
    }

    public static String pin(Certificate certificate) {
        if (certificate instanceof X509Certificate) {
            return "sha1/" + Base64.encode(sha1((X509Certificate) certificate).data);
        }
        throw new IllegalArgumentException("Certificate pinning requires X509 certificates");
    }

    private static ByteString sha1(X509Certificate x509Certificate) {
        return Util.sha1(ByteString.of(x509Certificate.getPublicKey().getEncoded()));
    }
}

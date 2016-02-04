package org.jivesoftware.smack.android;

import java.util.List;
import org.apache.http.conn.ssl.StrictHostnameVerifier;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.initializer.SimpleSmackInitializer;
import org.jivesoftware.smack.util.stringencoder.Base64;
import org.jivesoftware.smack.util.stringencoder.Base64.Encoder;
import org.jivesoftware.smack.util.stringencoder.Base64UrlSafeEncoder;
import org.jivesoftware.smack.util.stringencoder.StringEncoder;
import org.jivesoftware.smack.util.stringencoder.android.AndroidBase64Encoder;
import org.jivesoftware.smack.util.stringencoder.android.AndroidBase64UrlSafeEncoder;

public class AndroidSmackInitializer extends SimpleSmackInitializer {
    public final List<Exception> initialize() {
        SmackConfiguration.setDefaultHostnameVerifier(new StrictHostnameVerifier());
        Encoder instance = AndroidBase64Encoder.getInstance();
        if (instance == null) {
            throw new IllegalArgumentException("encoder must no be null");
        }
        Base64.base64encoder = instance;
        StringEncoder instance2 = AndroidBase64UrlSafeEncoder.getInstance();
        if (instance2 == null) {
            throw new IllegalArgumentException("encoder must no be null");
        }
        Base64UrlSafeEncoder.base64UrlSafeEncoder = instance2;
        return null;
    }
}

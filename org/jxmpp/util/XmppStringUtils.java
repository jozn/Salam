package org.jxmpp.util;

import android.support.v7.appcompat.BuildConfig;
import org.jxmpp.util.cache.LruCache;

public final class XmppStringUtils {
    private static final LruCache<String, String> LOCALPART_ESACPE_CACHE;
    private static final LruCache<String, String> LOCALPART_UNESCAPE_CACHE;

    public static String parseLocalpart(String jid) {
        int atIndex = jid.indexOf(64);
        if (atIndex <= 0) {
            return BuildConfig.VERSION_NAME;
        }
        int slashIndex = jid.indexOf(47);
        if (slashIndex < 0 || slashIndex >= atIndex) {
            return jid.substring(0, atIndex);
        }
        return BuildConfig.VERSION_NAME;
    }

    public static String parseDomain(String jid) {
        int atIndex = jid.indexOf(64);
        if (atIndex + 1 > jid.length()) {
            return BuildConfig.VERSION_NAME;
        }
        int slashIndex = jid.indexOf(47);
        if (slashIndex <= 0) {
            return jid.substring(atIndex + 1);
        }
        if (slashIndex > atIndex) {
            return jid.substring(atIndex + 1, slashIndex);
        }
        return jid.substring(0, slashIndex);
    }

    public static String parseResource(String jid) {
        int slashIndex = jid.indexOf(47);
        if (slashIndex + 1 > jid.length() || slashIndex < 0) {
            return BuildConfig.VERSION_NAME;
        }
        return jid.substring(slashIndex + 1);
    }

    public static String parseBareAddress(String jid) {
        int slashIndex = jid.indexOf(47);
        if (slashIndex < 0) {
            return jid;
        }
        if (slashIndex == 0) {
            return BuildConfig.VERSION_NAME;
        }
        return jid.substring(0, slashIndex);
    }

    static {
        LOCALPART_ESACPE_CACHE = new LruCache(100);
        LOCALPART_UNESCAPE_CACHE = new LruCache(100);
    }

    public static String completeJidFrom(String localpart, String domainpart) {
        if (domainpart == null) {
            throw new IllegalArgumentException("domainpart must not be null");
        }
        int length = localpart != null ? localpart.length() : 0;
        StringBuilder stringBuilder = new StringBuilder(((domainpart.length() + length) + 0) + 2);
        if (length > 0) {
            stringBuilder.append(localpart).append('@');
        }
        stringBuilder.append(domainpart);
        return stringBuilder.toString();
    }

    public static String generateKey(String element, String namespace) {
        return element + '#' + namespace;
    }
}

package org.jivesoftware.smackx.bytestreams.socks5;

import org.jivesoftware.smackx.bytestreams.socks5.packet.Bytestream;
import org.jxmpp.util.cache.Cache;
import org.jxmpp.util.cache.ExpirationCache;

public final class Socks5BytestreamRequest {
    private static final Cache<String, Integer> ADDRESS_BLACKLIST;
    private static int CONNECTION_FAILURE_THRESHOLD;
    private Bytestream bytestreamRequest;
    private Socks5BytestreamManager manager;
    private int minimumConnectTimeout;
    private int totalConnectTimeout;

    static {
        ADDRESS_BLACKLIST = new ExpirationCache(100, 7200000);
        CONNECTION_FAILURE_THRESHOLD = 2;
    }

    protected Socks5BytestreamRequest(Socks5BytestreamManager manager, Bytestream bytestreamRequest) {
        this.totalConnectTimeout = 10000;
        this.minimumConnectTimeout = 2000;
        this.manager = manager;
        this.bytestreamRequest = bytestreamRequest;
    }
}

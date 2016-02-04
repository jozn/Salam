package com.squareup.okhttp.internal;

import java.net.InetAddress;
import java.net.UnknownHostException;

public interface Network {
    public static final Network DEFAULT;

    /* renamed from: com.squareup.okhttp.internal.Network.1 */
    static class C12021 implements Network {
        C12021() {
        }

        public final InetAddress[] resolveInetAddresses(String host) throws UnknownHostException {
            if (host != null) {
                return InetAddress.getAllByName(host);
            }
            throw new UnknownHostException("host == null");
        }
    }

    InetAddress[] resolveInetAddresses(String str) throws UnknownHostException;

    static {
        DEFAULT = new C12021();
    }
}

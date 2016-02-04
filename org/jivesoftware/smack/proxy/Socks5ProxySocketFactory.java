package org.jivesoftware.smack.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.SocketFactory;
import org.jivesoftware.smack.proxy.ProxyInfo.ProxyType;

public final class Socks5ProxySocketFactory extends SocketFactory {
    private ProxyInfo proxy;

    public Socks5ProxySocketFactory(ProxyInfo proxy) {
        this.proxy = proxy;
    }

    public final Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return socks5ProxifiedSocket(host, port);
    }

    public final Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
        return socks5ProxifiedSocket(host, port);
    }

    public final Socket createSocket(InetAddress host, int port) throws IOException {
        return socks5ProxifiedSocket(host.getHostAddress(), port);
    }

    public final Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return socks5ProxifiedSocket(address.getHostAddress(), port);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.net.Socket socks5ProxifiedSocket(java.lang.String r25, int r26) throws java.io.IOException {
        /*
        r24 = this;
        r17 = 0;
        r0 = r24;
        r0 = r0.proxy;
        r20 = r0;
        r0 = r20;
        r15 = r0.proxyAddress;
        r0 = r24;
        r0 = r0.proxy;
        r20 = r0;
        r0 = r20;
        r0 = r0.proxyPort;
        r16 = r0;
        r0 = r24;
        r0 = r0.proxy;
        r20 = r0;
        r0 = r20;
        r0 = r0.proxyUsername;
        r19 = r0;
        r0 = r24;
        r0 = r0.proxy;
        r20 = r0;
        r0 = r20;
        r14 = r0.proxyPassword;
        r18 = new java.net.Socket;	 Catch:{ RuntimeException -> 0x01f0, Exception -> 0x01ee }
        r0 = r18;
        r1 = r16;
        r0.<init>(r15, r1);	 Catch:{ RuntimeException -> 0x01f0, Exception -> 0x01ee }
        r8 = r18.getInputStream();	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r13 = r18.getOutputStream();	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = 1;
        r0 = r18;
        r1 = r20;
        r0.setTcpNoDelay(r1);	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0 = r20;
        r4 = new byte[r0];	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = 0;
        r21 = 5;
        r4[r20] = r21;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = 1;
        r21 = 2;
        r4[r20] = r21;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = 2;
        r21 = 0;
        r4[r20] = r21;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = 3;
        r21 = 2;
        r4[r20] = r21;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = 0;
        r21 = 4;
        r0 = r20;
        r1 = r21;
        r13.write(r4, r0, r1);	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = 2;
        r0 = r20;
        fill(r8, r4, r0);	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r5 = 0;
        r20 = 1;
        r20 = r4[r20];	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r0 = r20;
        r0 = r0 & 255;
        r20 = r0;
        switch(r20) {
            case 0: goto L_0x0099;
            case 1: goto L_0x0086;
            case 2: goto L_0x009b;
            default: goto L_0x0086;
        };
    L_0x0086:
        if (r5 != 0) goto L_0x010c;
    L_0x0088:
        r18.close();	 Catch:{ Exception -> 0x01e7, RuntimeException -> 0x0095 }
    L_0x008b:
        r20 = new org.jivesoftware.smack.proxy.ProxyException;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r21 = org.jivesoftware.smack.proxy.ProxyInfo.ProxyType.SOCKS5;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r22 = "fail in SOCKS5 proxy";
        r20.<init>(r21, r22);	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        throw r20;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
    L_0x0095:
        r20 = move-exception;
        r17 = r18;
    L_0x0098:
        throw r20;
    L_0x0099:
        r5 = 1;
        goto L_0x0086;
    L_0x009b:
        if (r19 == 0) goto L_0x0086;
    L_0x009d:
        if (r14 == 0) goto L_0x0086;
    L_0x009f:
        r20 = 0;
        r21 = 1;
        r4[r20] = r21;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = 1;
        r21 = r19.length();	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r0 = r21;
        r0 = (byte) r0;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r21 = r0;
        r4[r20] = r21;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = r19.getBytes();	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r21 = 0;
        r22 = 2;
        r23 = r19.length();	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r0 = r20;
        r1 = r21;
        r2 = r22;
        r3 = r23;
        java.lang.System.arraycopy(r0, r1, r4, r2, r3);	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = r19.length();	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r9 = r20 + 2;
        r10 = r9 + 1;
        r20 = r14.length();	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r0 = r20;
        r0 = (byte) r0;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = r0;
        r4[r9] = r20;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = r14.getBytes();	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r21 = 0;
        r22 = r14.length();	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r0 = r20;
        r1 = r21;
        r2 = r22;
        java.lang.System.arraycopy(r0, r1, r4, r10, r2);	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = r14.length();	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r9 = r10 + r20;
        r20 = 0;
        r0 = r20;
        r13.write(r4, r0, r9);	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = 2;
        r0 = r20;
        fill(r8, r4, r0);	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = 1;
        r20 = r4[r20];	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        if (r20 != 0) goto L_0x0086;
    L_0x0109:
        r5 = 1;
        goto L_0x0086;
    L_0x010c:
        r20 = 0;
        r21 = 5;
        r4[r20] = r21;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = 1;
        r21 = 1;
        r4[r20] = r21;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = 2;
        r21 = 0;
        r4[r20] = r21;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r7 = r25.getBytes();	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r11 = r7.length;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = 3;
        r21 = 3;
        r4[r20] = r21;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = 4;
        r0 = (byte) r11;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r21 = r0;
        r4[r20] = r21;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = 0;
        r21 = 5;
        r0 = r20;
        r1 = r21;
        java.lang.System.arraycopy(r7, r0, r4, r1, r11);	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r9 = r11 + 5;
        r10 = r9 + 1;
        r20 = r26 >>> 8;
        r0 = r20;
        r0 = (byte) r0;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = r0;
        r4[r9] = r20;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r9 = r10 + 1;
        r0 = r26;
        r0 = r0 & 255;
        r20 = r0;
        r0 = r20;
        r0 = (byte) r0;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = r0;
        r4[r10] = r20;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = 0;
        r0 = r20;
        r13.write(r4, r0, r9);	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = 4;
        r0 = r20;
        fill(r8, r4, r0);	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = 1;
        r20 = r4[r20];	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        if (r20 == 0) goto L_0x01b0;
    L_0x016b:
        r18.close();	 Catch:{ Exception -> 0x01ea, RuntimeException -> 0x0095 }
    L_0x016e:
        r20 = new org.jivesoftware.smack.proxy.ProxyException;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r21 = org.jivesoftware.smack.proxy.ProxyInfo.ProxyType.SOCKS5;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r22 = new java.lang.StringBuilder;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r23 = "server returns ";
        r22.<init>(r23);	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r23 = 1;
        r23 = r4[r23];	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r22 = r22.append(r23);	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r22 = r22.toString();	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20.<init>(r21, r22);	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        throw r20;	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
    L_0x0189:
        r6 = move-exception;
        r17 = r18;
    L_0x018c:
        if (r17 == 0) goto L_0x0191;
    L_0x018e:
        r17.close();	 Catch:{ Exception -> 0x01ec }
    L_0x0191:
        r20 = new java.lang.StringBuilder;
        r21 = "ProxySOCKS5: ";
        r20.<init>(r21);
        r21 = r6.toString();
        r20 = r20.append(r21);
        r12 = r20.toString();
        r20 = new org.jivesoftware.smack.proxy.ProxyException;
        r21 = org.jivesoftware.smack.proxy.ProxyInfo.ProxyType.SOCKS5;
        r0 = r20;
        r1 = r21;
        r0.<init>(r1, r12, r6);
        throw r20;
    L_0x01b0:
        r20 = 3;
        r20 = r4[r20];	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r0 = r20;
        r0 = r0 & 255;
        r20 = r0;
        switch(r20) {
            case 1: goto L_0x01be;
            case 2: goto L_0x01bd;
            case 3: goto L_0x01c6;
            case 4: goto L_0x01df;
            default: goto L_0x01bd;
        };	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
    L_0x01bd:
        return r18;
    L_0x01be:
        r20 = 6;
        r0 = r20;
        fill(r8, r4, r0);	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        goto L_0x01bd;
    L_0x01c6:
        r20 = 1;
        r0 = r20;
        fill(r8, r4, r0);	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r20 = 0;
        r20 = r4[r20];	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        r0 = r20;
        r0 = r0 & 255;
        r20 = r0;
        r20 = r20 + 2;
        r0 = r20;
        fill(r8, r4, r0);	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        goto L_0x01bd;
    L_0x01df:
        r20 = 18;
        r0 = r20;
        fill(r8, r4, r0);	 Catch:{ RuntimeException -> 0x0095, Exception -> 0x0189 }
        goto L_0x01bd;
    L_0x01e7:
        r20 = move-exception;
        goto L_0x008b;
    L_0x01ea:
        r20 = move-exception;
        goto L_0x016e;
    L_0x01ec:
        r20 = move-exception;
        goto L_0x0191;
    L_0x01ee:
        r6 = move-exception;
        goto L_0x018c;
    L_0x01f0:
        r20 = move-exception;
        goto L_0x0098;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smack.proxy.Socks5ProxySocketFactory.socks5ProxifiedSocket(java.lang.String, int):java.net.Socket");
    }

    private static void fill(InputStream in, byte[] buf, int len) throws IOException {
        int s = 0;
        while (s < len) {
            int i = in.read(buf, s, len - s);
            if (i <= 0) {
                throw new ProxyException(ProxyType.SOCKS5, "stream is closed");
            }
            s += i;
        }
    }
}

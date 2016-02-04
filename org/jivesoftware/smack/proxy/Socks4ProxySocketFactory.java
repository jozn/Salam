package org.jivesoftware.smack.proxy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.SocketFactory;

public final class Socks4ProxySocketFactory extends SocketFactory {
    private ProxyInfo proxy;

    public Socks4ProxySocketFactory(ProxyInfo proxy) {
        this.proxy = proxy;
    }

    public final Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return socks4ProxifiedSocket(host, port);
    }

    public final Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
        return socks4ProxifiedSocket(host, port);
    }

    public final Socket createSocket(InetAddress host, int port) throws IOException {
        return socks4ProxifiedSocket(host.getHostAddress(), port);
    }

    public final Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return socks4ProxifiedSocket(address.getHostAddress(), port);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.net.Socket socks4ProxifiedSocket(java.lang.String r26, int r27) throws java.io.IOException {
        /*
        r25 = this;
        r16 = 0;
        r0 = r25;
        r0 = r0.proxy;
        r21 = r0;
        r0 = r21;
        r13 = r0.proxyAddress;
        r0 = r25;
        r0 = r0.proxy;
        r21 = r0;
        r0 = r21;
        r14 = r0.proxyPort;
        r0 = r25;
        r0 = r0.proxy;
        r21 = r0;
        r0 = r21;
        r0 = r0.proxyUsername;
        r20 = r0;
        r17 = new java.net.Socket;	 Catch:{ RuntimeException -> 0x0168, Exception -> 0x0166 }
        r0 = r17;
        r0.<init>(r13, r14);	 Catch:{ RuntimeException -> 0x0168, Exception -> 0x0166 }
        r8 = r17.getInputStream();	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r12 = r17.getOutputStream();	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r21 = 1;
        r0 = r17;
        r1 = r21;
        r0.setTcpNoDelay(r1);	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r21 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0 = r21;
        r4 = new byte[r0];	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r9 = 0;
        r21 = 0;
        r9 = r9 + 1;
        r22 = 4;
        r4[r21] = r22;	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r21 = 1;
        r9 = r9 + 1;
        r22 = 1;
        r4[r21] = r22;	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r21 = 2;
        r9 = r9 + 1;
        r22 = r27 >>> 8;
        r0 = r22;
        r0 = (byte) r0;	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r22 = r0;
        r4[r21] = r22;	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r21 = 3;
        r9 = r9 + 1;
        r0 = r27;
        r0 = r0 & 255;
        r22 = r0;
        r0 = r22;
        r0 = (byte) r0;	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r22 = r0;
        r4[r21] = r22;	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r21 = java.net.InetAddress.getByName(r26);	 Catch:{ UnknownHostException -> 0x008a }
        r5 = r21.getAddress();	 Catch:{ UnknownHostException -> 0x008a }
        r7 = 0;
        r10 = r9;
    L_0x0079:
        r0 = r5.length;	 Catch:{ UnknownHostException -> 0x016b }
        r21 = r0;
        r0 = r21;
        if (r7 >= r0) goto L_0x00a3;
    L_0x0080:
        r9 = r10 + 1;
        r21 = r5[r7];	 Catch:{ UnknownHostException -> 0x008a }
        r4[r10] = r21;	 Catch:{ UnknownHostException -> 0x008a }
        r7 = r7 + 1;
        r10 = r9;
        goto L_0x0079;
    L_0x008a:
        r19 = move-exception;
    L_0x008b:
        r21 = new org.jivesoftware.smack.proxy.ProxyException;	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r22 = org.jivesoftware.smack.proxy.ProxyInfo.ProxyType.SOCKS4;	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r23 = r19.toString();	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r0 = r21;
        r1 = r22;
        r2 = r23;
        r3 = r19;
        r0.<init>(r1, r2, r3);	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        throw r21;	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
    L_0x009f:
        r21 = move-exception;
        r16 = r17;
    L_0x00a2:
        throw r21;
    L_0x00a3:
        if (r20 == 0) goto L_0x00bf;
    L_0x00a5:
        r21 = r20.getBytes();	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r22 = 0;
        r23 = r20.length();	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r0 = r21;
        r1 = r22;
        r2 = r23;
        java.lang.System.arraycopy(r0, r1, r4, r10, r2);	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r21 = r20.length();	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r9 = r10 + r21;
        r10 = r9;
    L_0x00bf:
        r9 = r10 + 1;
        r21 = 0;
        r4[r10] = r21;	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r21 = 0;
        r0 = r21;
        r12.write(r4, r0, r9);	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r15 = 0;
    L_0x00cd:
        r21 = 6;
        r0 = r21;
        if (r15 >= r0) goto L_0x00fd;
    L_0x00d3:
        r21 = 6 - r15;
        r0 = r21;
        r7 = r8.read(r4, r15, r0);	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        if (r7 > 0) goto L_0x00fb;
    L_0x00dd:
        r21 = new org.jivesoftware.smack.proxy.ProxyException;	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r22 = org.jivesoftware.smack.proxy.ProxyInfo.ProxyType.SOCKS4;	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r23 = "stream is closed";
        r21.<init>(r22, r23);	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        throw r21;	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
    L_0x00e7:
        r6 = move-exception;
        r16 = r17;
    L_0x00ea:
        if (r16 == 0) goto L_0x00ef;
    L_0x00ec:
        r16.close();	 Catch:{ Exception -> 0x0164 }
    L_0x00ef:
        r21 = new org.jivesoftware.smack.proxy.ProxyException;
        r22 = org.jivesoftware.smack.proxy.ProxyInfo.ProxyType.SOCKS4;
        r23 = r6.toString();
        r21.<init>(r22, r23);
        throw r21;
    L_0x00fb:
        r15 = r15 + r7;
        goto L_0x00cd;
    L_0x00fd:
        r21 = 0;
        r21 = r4[r21];	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        if (r21 == 0) goto L_0x011e;
    L_0x0103:
        r21 = new org.jivesoftware.smack.proxy.ProxyException;	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r22 = org.jivesoftware.smack.proxy.ProxyInfo.ProxyType.SOCKS4;	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r23 = new java.lang.StringBuilder;	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r24 = "server returns VN ";
        r23.<init>(r24);	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r24 = 0;
        r24 = r4[r24];	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r23 = r23.append(r24);	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r23 = r23.toString();	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r21.<init>(r22, r23);	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        throw r21;	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
    L_0x011e:
        r21 = 1;
        r21 = r4[r21];	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r22 = 90;
        r0 = r21;
        r1 = r22;
        if (r0 == r1) goto L_0x014c;
    L_0x012a:
        r17.close();	 Catch:{ Exception -> 0x0162, RuntimeException -> 0x009f }
    L_0x012d:
        r21 = new java.lang.StringBuilder;	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r22 = "ProxySOCKS4: server returns CD ";
        r21.<init>(r22);	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r22 = 1;
        r22 = r4[r22];	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r21 = r21.append(r22);	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r11 = r21.toString();	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r21 = new org.jivesoftware.smack.proxy.ProxyException;	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r22 = org.jivesoftware.smack.proxy.ProxyInfo.ProxyType.SOCKS4;	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r0 = r21;
        r1 = r22;
        r0.<init>(r1, r11);	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        throw r21;	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
    L_0x014c:
        r21 = 2;
        r0 = r21;
        r0 = new byte[r0];	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        r18 = r0;
        r21 = 0;
        r22 = 2;
        r0 = r18;
        r1 = r21;
        r2 = r22;
        r8.read(r0, r1, r2);	 Catch:{ RuntimeException -> 0x009f, Exception -> 0x00e7 }
        return r17;
    L_0x0162:
        r21 = move-exception;
        goto L_0x012d;
    L_0x0164:
        r21 = move-exception;
        goto L_0x00ef;
    L_0x0166:
        r6 = move-exception;
        goto L_0x00ea;
    L_0x0168:
        r21 = move-exception;
        goto L_0x00a2;
    L_0x016b:
        r19 = move-exception;
        r9 = r10;
        goto L_0x008b;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smack.proxy.Socks4ProxySocketFactory.socks4ProxifiedSocket(java.lang.String, int):java.net.Socket");
    }
}

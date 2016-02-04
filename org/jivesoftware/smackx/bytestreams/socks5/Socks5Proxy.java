package org.jivesoftware.smackx.bytestreams.socks5;

import android.support.v4.internal.view.SupportMenu;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Socks5Proxy {
    private static final Logger LOGGER;
    private static boolean localSocks5ProxyEnabled;
    private static int localSocks5ProxyPort;
    private static Socks5Proxy socks5Server;
    private final List<String> allowedConnections;
    private final Map<String, Socket> connectionMap;
    private final Set<String> localAddresses;
    private Socks5ServerProcess serverProcess;
    private ServerSocket serverSocket;
    private Thread serverThread;

    private class Socks5ServerProcess implements Runnable {
        private Socks5ServerProcess() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void run() {
            /*
            r13 = this;
            r12 = 3;
            r2 = 1;
            r11 = 5;
            r3 = 0;
        L_0x0004:
            r0 = 0;
            r1 = org.jivesoftware.smackx.bytestreams.socks5.Socks5Proxy.this;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r1 = r1.serverSocket;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r1 = r1.isClosed();	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            if (r1 != 0) goto L_0x001b;
        L_0x0011:
            r1 = java.lang.Thread.currentThread();	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r1 = r1.isInterrupted();	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            if (r1 == 0) goto L_0x001c;
        L_0x001b:
            return;
        L_0x001c:
            r1 = org.jivesoftware.smackx.bytestreams.socks5.Socks5Proxy.this;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r1 = r1.serverSocket;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r0 = r1.accept();	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r4 = new java.io.DataOutputStream;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r1 = r0.getOutputStream();	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r4.<init>(r1);	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r5 = new java.io.DataInputStream;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r1 = r0.getInputStream();	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r5.<init>(r1);	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r1 = r5.read();	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            if (r1 == r11) goto L_0x0048;
        L_0x003e:
            r1 = new org.jivesoftware.smack.SmackException;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r4 = "Only SOCKS5 supported";
            r1.<init>(r4);	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            throw r1;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
        L_0x0046:
            r1 = move-exception;
            goto L_0x0004;
        L_0x0048:
            r6 = r5.read();	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r7 = new byte[r6];	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r5.readFully(r7);	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r1 = 2;
            r8 = new byte[r1];	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r1 = 0;
            r9 = 5;
            r8[r1] = r9;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r1 = r3;
        L_0x0059:
            if (r1 >= r6) goto L_0x00ee;
        L_0x005b:
            r9 = r7[r1];	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            if (r9 != 0) goto L_0x007d;
        L_0x005f:
            r1 = r2;
        L_0x0060:
            if (r1 != 0) goto L_0x0080;
        L_0x0062:
            r1 = 1;
            r5 = -1;
            r8[r1] = r5;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r4.write(r8);	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r4.flush();	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r1 = new org.jivesoftware.smack.SmackException;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r4 = "Authentication method not supported";
            r1.<init>(r4);	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            throw r1;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
        L_0x0074:
            r1 = move-exception;
            if (r0 == 0) goto L_0x0004;
        L_0x0077:
            r0.close();	 Catch:{ IOException -> 0x007b }
            goto L_0x0004;
        L_0x007b:
            r1 = move-exception;
            goto L_0x0004;
        L_0x007d:
            r1 = r1 + 1;
            goto L_0x0059;
        L_0x0080:
            r1 = 1;
            r6 = 0;
            r8[r1] = r6;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r4.write(r8);	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r4.flush();	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r1 = 5;
            r1 = new byte[r1];	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r6 = 0;
            r7 = 5;
            r5.readFully(r1, r6, r7);	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r6 = 3;
            r6 = r1[r6];	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            if (r6 == r12) goto L_0x009f;
        L_0x0097:
            r1 = new org.jivesoftware.smack.SmackException;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r4 = "Unsupported SOCKS5 address type";
            r1.<init>(r4);	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            throw r1;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
        L_0x009f:
            r6 = 4;
            r6 = r1[r6];	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r7 = r6 + 7;
            r7 = new byte[r7];	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r8 = 0;
            r9 = 0;
            r10 = 5;
            java.lang.System.arraycopy(r1, r8, r7, r9, r10);	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r1 = 5;
            r6 = r6 + 2;
            r5.readFully(r7, r1, r6);	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r1 = new java.lang.String;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r5 = 5;
            r6 = 4;
            r6 = r7[r6];	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r1.<init>(r7, r5, r6);	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r5 = org.jivesoftware.smackx.bytestreams.socks5.Socks5Proxy.this;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r5 = r5.allowedConnections;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r5 = r5.contains(r1);	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            if (r5 != 0) goto L_0x00d9;
        L_0x00c7:
            r1 = 1;
            r5 = 5;
            r7[r1] = r5;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r4.write(r7);	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r4.flush();	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r1 = new org.jivesoftware.smack.SmackException;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r4 = "Connection is not allowed";
            r1.<init>(r4);	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            throw r1;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
        L_0x00d9:
            r5 = 1;
            r6 = 0;
            r7[r5] = r6;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r4.write(r7);	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r4.flush();	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r4 = org.jivesoftware.smackx.bytestreams.socks5.Socks5Proxy.this;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r4 = r4.connectionMap;	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            r4.put(r1, r0);	 Catch:{ SocketException -> 0x0046, Exception -> 0x0074 }
            goto L_0x0004;
        L_0x00ee:
            r1 = r3;
            goto L_0x0060;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smackx.bytestreams.socks5.Socks5Proxy.Socks5ServerProcess.run():void");
        }
    }

    static {
        LOGGER = Logger.getLogger(Socks5Proxy.class.getName());
        localSocks5ProxyEnabled = true;
        localSocks5ProxyPort = -7777;
    }

    private Socks5Proxy() {
        this.connectionMap = new ConcurrentHashMap();
        this.allowedConnections = Collections.synchronizedList(new LinkedList());
        this.localAddresses = new LinkedHashSet(4);
        this.serverProcess = new Socks5ServerProcess();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            Set<String> localHostAddresses = new HashSet();
            Iterator it = Collections.list(networkInterfaces).iterator();
            while (it.hasNext()) {
                Iterator i$ = Collections.list(((NetworkInterface) it.next()).getInetAddresses()).iterator();
                while (i$.hasNext()) {
                    localHostAddresses.add(((InetAddress) i$.next()).getHostAddress());
                }
            }
            if (localHostAddresses.isEmpty()) {
                throw new IllegalStateException("Could not determine any local host address");
            }
            synchronized (this.localAddresses) {
                this.localAddresses.clear();
                this.localAddresses.addAll(localHostAddresses);
            }
        } catch (SocketException e) {
            throw new IllegalStateException(e);
        }
    }

    public static synchronized Socks5Proxy getSocks5Proxy() {
        Socks5Proxy socks5Proxy;
        synchronized (Socks5Proxy.class) {
            if (socks5Server == null) {
                socks5Server = new Socks5Proxy();
            }
            if (localSocks5ProxyEnabled) {
                socks5Server.start();
            }
            socks5Proxy = socks5Server;
        }
        return socks5Proxy;
    }

    private synchronized void start() {
        if (!isRunning()) {
            try {
                if (localSocks5ProxyPort < 0) {
                    int port = Math.abs(localSocks5ProxyPort);
                    int i = 0;
                    while (i < SupportMenu.USER_MASK - port) {
                        try {
                            this.serverSocket = new ServerSocket(port + i);
                            break;
                        } catch (IOException e) {
                            i++;
                        }
                    }
                } else {
                    this.serverSocket = new ServerSocket(localSocks5ProxyPort);
                }
                if (this.serverSocket != null) {
                    this.serverThread = new Thread(this.serverProcess);
                    this.serverThread.start();
                }
            } catch (IOException e2) {
                LOGGER.log(Level.SEVERE, "couldn't setup local SOCKS5 proxy on port " + localSocks5ProxyPort, e2);
            }
        }
    }

    public final synchronized void stop() {
        if (isRunning()) {
            try {
                this.serverSocket.close();
            } catch (IOException e) {
            }
            if (this.serverThread != null && this.serverThread.isAlive()) {
                try {
                    this.serverThread.interrupt();
                    this.serverThread.join();
                } catch (InterruptedException e2) {
                }
            }
            this.serverThread = null;
            this.serverSocket = null;
        }
    }

    private boolean isRunning() {
        return this.serverSocket != null;
    }
}

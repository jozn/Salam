package cz.msebera.android.httpclient.impl.conn;

import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.conn.ClientConnectionOperator;
import cz.msebera.android.httpclient.conn.DnsResolver;
import cz.msebera.android.httpclient.conn.OperatedClientConnection;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.scheme.SchemeLayeredSocketFactory;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.Asserts;
import java.io.IOException;
import java.net.Socket;

@Deprecated
public final class DefaultClientConnectionOperator implements ClientConnectionOperator {
    protected final DnsResolver dnsResolver;
    public HttpClientAndroidLog log;
    protected final SchemeRegistry schemeRegistry;

    public DefaultClientConnectionOperator(SchemeRegistry schemes) {
        this.log = new HttpClientAndroidLog(getClass());
        Args.notNull(schemes, "Scheme registry");
        this.schemeRegistry = schemes;
        this.dnsResolver = new SystemDefaultDnsResolver();
    }

    public final OperatedClientConnection createConnection() {
        return new DefaultClientConnection();
    }

    private SchemeRegistry getSchemeRegistry(HttpContext context) {
        SchemeRegistry reg = (SchemeRegistry) context.getAttribute("http.scheme-registry");
        if (reg == null) {
            return this.schemeRegistry;
        }
        return reg;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void openConnection(cz.msebera.android.httpclient.conn.OperatedClientConnection r18, cz.msebera.android.httpclient.HttpHost r19, java.net.InetAddress r20, cz.msebera.android.httpclient.protocol.HttpContext r21, cz.msebera.android.httpclient.params.HttpParams r22) throws java.io.IOException {
        /*
        r17 = this;
        r14 = "Connection";
        r0 = r18;
        cz.msebera.android.httpclient.util.Args.notNull(r0, r14);
        r14 = "Target host";
        r0 = r19;
        cz.msebera.android.httpclient.util.Args.notNull(r0, r14);
        r14 = "HTTP parameters";
        r0 = r22;
        cz.msebera.android.httpclient.util.Args.notNull(r0, r14);
        r14 = r18.isOpen();
        if (r14 != 0) goto L_0x00b1;
    L_0x001b:
        r14 = 1;
    L_0x001c:
        r15 = "Connection must not be open";
        cz.msebera.android.httpclient.util.Asserts.check(r14, r15);
        r0 = r17;
        r1 = r21;
        r14 = r0.getSchemeRegistry(r1);
        r15 = r19.getSchemeName();
        r11 = r14.getScheme(r15);
        r12 = r11.socketFactory;
        r14 = r19.getHostName();
        r0 = r17;
        r15 = r0.dnsResolver;
        r3 = r15.resolve(r14);
        r14 = r19.getPort();
        r9 = r11.resolvePort(r14);
        r6 = 0;
    L_0x0048:
        r14 = r3.length;
        if (r6 >= r14) goto L_0x00b0;
    L_0x004b:
        r2 = r3[r6];
        r14 = r3.length;
        r14 = r14 + -1;
        if (r6 != r14) goto L_0x00b4;
    L_0x0052:
        r7 = 1;
    L_0x0053:
        r0 = r22;
        r13 = r12.createSocket(r0);
        r0 = r18;
        r1 = r19;
        r0.opening(r13, r1);
        r10 = new cz.msebera.android.httpclient.conn.HttpInetSocketAddress;
        r0 = r19;
        r10.<init>(r0, r2, r9);
        r8 = 0;
        if (r20 == 0) goto L_0x0072;
    L_0x006a:
        r8 = new java.net.InetSocketAddress;
        r14 = 0;
        r0 = r20;
        r8.<init>(r0, r14);
    L_0x0072:
        r0 = r17;
        r14 = r0.log;
        r14 = r14.debugEnabled;
        if (r14 == 0) goto L_0x0090;
    L_0x007a:
        r0 = r17;
        r14 = r0.log;
        r15 = new java.lang.StringBuilder;
        r16 = "Connecting to ";
        r15.<init>(r16);
        r15 = r15.append(r10);
        r15 = r15.toString();
        r14.debug(r15);
    L_0x0090:
        r0 = r22;
        r4 = r12.connectSocket(r13, r10, r8, r0);	 Catch:{ ConnectException -> 0x00b6, ConnectTimeoutException -> 0x00ba }
        if (r13 == r4) goto L_0x00a0;
    L_0x0098:
        r13 = r4;
        r0 = r18;
        r1 = r19;
        r0.opening(r13, r1);	 Catch:{ ConnectException -> 0x00b6, ConnectTimeoutException -> 0x00ba }
    L_0x00a0:
        r0 = r22;
        prepareSocket$6941cd43(r13, r0);	 Catch:{ ConnectException -> 0x00b6, ConnectTimeoutException -> 0x00ba }
        r14 = r12.isSecure(r13);	 Catch:{ ConnectException -> 0x00b6, ConnectTimeoutException -> 0x00ba }
        r0 = r18;
        r1 = r22;
        r0.openCompleted(r14, r1);	 Catch:{ ConnectException -> 0x00b6, ConnectTimeoutException -> 0x00ba }
    L_0x00b0:
        return;
    L_0x00b1:
        r14 = 0;
        goto L_0x001c;
    L_0x00b4:
        r7 = 0;
        goto L_0x0053;
    L_0x00b6:
        r5 = move-exception;
        if (r7 == 0) goto L_0x00be;
    L_0x00b9:
        throw r5;
    L_0x00ba:
        r5 = move-exception;
        if (r7 == 0) goto L_0x00be;
    L_0x00bd:
        throw r5;
    L_0x00be:
        r0 = r17;
        r14 = r0.log;
        r14 = r14.debugEnabled;
        if (r14 == 0) goto L_0x00e2;
    L_0x00c6:
        r0 = r17;
        r14 = r0.log;
        r15 = new java.lang.StringBuilder;
        r16 = "Connect to ";
        r15.<init>(r16);
        r15 = r15.append(r10);
        r16 = " timed out. Connection will be retried using another IP address";
        r15 = r15.append(r16);
        r15 = r15.toString();
        r14.debug(r15);
    L_0x00e2:
        r6 = r6 + 1;
        goto L_0x0048;
        */
        throw new UnsupportedOperationException("Method not decompiled: cz.msebera.android.httpclient.impl.conn.DefaultClientConnectionOperator.openConnection(cz.msebera.android.httpclient.conn.OperatedClientConnection, cz.msebera.android.httpclient.HttpHost, java.net.InetAddress, cz.msebera.android.httpclient.protocol.HttpContext, cz.msebera.android.httpclient.params.HttpParams):void");
    }

    public final void updateSecureConnection(OperatedClientConnection conn, HttpHost target, HttpContext context, HttpParams params) throws IOException {
        Args.notNull(conn, "Connection");
        Args.notNull(target, "Target host");
        Args.notNull(params, "Parameters");
        Asserts.check(conn.isOpen(), "Connection must be open");
        Scheme schm = getSchemeRegistry(context).getScheme(target.getSchemeName());
        Asserts.check(schm.socketFactory instanceof SchemeLayeredSocketFactory, "Socket factory must implement SchemeLayeredSocketFactory");
        SchemeLayeredSocketFactory lsf = schm.socketFactory;
        Socket sock = lsf.createLayeredSocket$2b77d450(conn.getSocket(), target.getHostName(), schm.resolvePort(target.getPort()));
        prepareSocket$6941cd43(sock, params);
        conn.update(sock, target, lsf.isSecure(sock), params);
    }

    private static void prepareSocket$6941cd43(Socket sock, HttpParams params) throws IOException {
        boolean z = true;
        Args.notNull(params, "HTTP parameters");
        sock.setTcpNoDelay(params.getBooleanParameter("http.tcp.nodelay", true));
        sock.setSoTimeout(HttpConnectionParams.getSoTimeout(params));
        Args.notNull(params, "HTTP parameters");
        int linger = params.getIntParameter("http.socket.linger", -1);
        if (linger >= 0) {
            if (linger <= 0) {
                z = false;
            }
            sock.setSoLinger(z, linger);
        }
    }
}

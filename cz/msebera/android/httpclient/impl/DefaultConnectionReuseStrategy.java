package cz.msebera.android.httpclient.impl;

import cz.msebera.android.httpclient.ConnectionReuseStrategy;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpVersion;
import cz.msebera.android.httpclient.ParseException;
import cz.msebera.android.httpclient.ProtocolVersion;
import cz.msebera.android.httpclient.TokenIterator;
import cz.msebera.android.httpclient.message.BasicHeaderIterator;
import cz.msebera.android.httpclient.message.BasicTokenIterator;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.util.Args;

public final class DefaultConnectionReuseStrategy implements ConnectionReuseStrategy {
    public static final DefaultConnectionReuseStrategy INSTANCE;

    static {
        INSTANCE = new DefaultConnectionReuseStrategy();
    }

    public final boolean keepAlive(HttpResponse response, HttpContext context) {
        Args.notNull(response, "HTTP response");
        Args.notNull(context, "HTTP context");
        ProtocolVersion ver = response.getStatusLine().getProtocolVersion();
        Header teh = response.getFirstHeader("Transfer-Encoding");
        if (teh == null) {
            boolean z;
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode < 200 || statusCode == 204 || statusCode == 304 || statusCode == 205) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                Header[] clhs = response.getHeaders("Content-Length");
                if (clhs.length != 1) {
                    return false;
                }
                try {
                    if (Integer.parseInt(clhs[0].getValue()) < 0) {
                        return false;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        } else if (!"chunked".equalsIgnoreCase(teh.getValue())) {
            return false;
        }
        Header[] connHeaders = response.getHeaders("Connection");
        if (connHeaders.length == 0) {
            connHeaders = response.getHeaders("Proxy-Connection");
        }
        if (connHeaders.length != 0) {
            try {
                TokenIterator ti = new BasicTokenIterator(new BasicHeaderIterator(connHeaders));
                boolean keepalive = false;
                while (ti.hasNext()) {
                    String token = ti.nextToken();
                    if ("Close".equalsIgnoreCase(token)) {
                        return false;
                    }
                    if ("Keep-Alive".equalsIgnoreCase(token)) {
                        keepalive = true;
                    }
                }
                if (keepalive) {
                    return true;
                }
            } catch (ParseException e2) {
                return false;
            }
        }
        if (ver.lessEquals(HttpVersion.HTTP_1_0)) {
            return false;
        }
        return true;
    }
}

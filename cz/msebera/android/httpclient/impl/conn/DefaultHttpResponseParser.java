package cz.msebera.android.httpclient.impl.conn;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpResponseFactory;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import cz.msebera.android.httpclient.impl.io.AbstractMessageParser;
import cz.msebera.android.httpclient.io.SessionInputBuffer;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.CharArrayBuffer;

public final class DefaultHttpResponseParser extends AbstractMessageParser<HttpResponse> {
    private final CharArrayBuffer lineBuf;
    public HttpClientAndroidLog log;
    private final HttpResponseFactory responseFactory;

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected final /* bridge */ /* synthetic */ cz.msebera.android.httpclient.HttpMessage parseHead(cz.msebera.android.httpclient.io.SessionInputBuffer r8) throws java.io.IOException, cz.msebera.android.httpclient.HttpException, cz.msebera.android.httpclient.ParseException {
        /*
        r7 = this;
        r6 = -1;
        r1 = 0;
        r0 = r1;
    L_0x0003:
        r2 = r7.lineBuf;
        r2.len = r1;
        r2 = r7.lineBuf;
        r2 = r8.readLine(r2);
        if (r2 != r6) goto L_0x0019;
    L_0x000f:
        if (r0 != 0) goto L_0x0019;
    L_0x0011:
        r0 = new cz.msebera.android.httpclient.NoHttpResponseException;
        r1 = "The target server failed to respond";
        r0.<init>(r1);
        throw r0;
    L_0x0019:
        r3 = new cz.msebera.android.httpclient.message.ParserCursor;
        r4 = r7.lineBuf;
        r4 = r4.length();
        r3.<init>(r1, r4);
        r4 = r7.lineParser;
        r5 = r7.lineBuf;
        r4 = r4.hasProtocolVersion(r5, r3);
        if (r4 != 0) goto L_0x005b;
    L_0x002e:
        if (r2 != r6) goto L_0x0038;
    L_0x0030:
        r0 = new cz.msebera.android.httpclient.ProtocolException;
        r1 = "The server failed to respond with a valid HTTP response";
        r0.<init>(r1);
        throw r0;
    L_0x0038:
        r2 = r7.log;
        r2 = r2.debugEnabled;
        if (r2 == 0) goto L_0x0058;
    L_0x003e:
        r2 = r7.log;
        r3 = new java.lang.StringBuilder;
        r4 = "Garbage in response: ";
        r3.<init>(r4);
        r4 = r7.lineBuf;
        r4 = r4.toString();
        r3 = r3.append(r4);
        r3 = r3.toString();
        r2.debug(r3);
    L_0x0058:
        r0 = r0 + 1;
        goto L_0x0003;
    L_0x005b:
        r0 = r7.lineParser;
        r1 = r7.lineBuf;
        r0 = r0.parseStatusLine(r1, r3);
        r1 = r7.responseFactory;
        r0 = r1.newHttpResponse$6da01fe5(r0);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: cz.msebera.android.httpclient.impl.conn.DefaultHttpResponseParser.parseHead(cz.msebera.android.httpclient.io.SessionInputBuffer):cz.msebera.android.httpclient.HttpMessage");
    }

    @Deprecated
    public DefaultHttpResponseParser(SessionInputBuffer buffer, HttpResponseFactory responseFactory, HttpParams params) {
        super(buffer, params);
        this.log = new HttpClientAndroidLog(getClass());
        Args.notNull(responseFactory, "Response factory");
        this.responseFactory = responseFactory;
        this.lineBuf = new CharArrayBuffer(AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
    }
}

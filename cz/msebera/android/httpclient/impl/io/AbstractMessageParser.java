package cz.msebera.android.httpclient.impl.io;

import cz.msebera.android.httpclient.HttpException;
import cz.msebera.android.httpclient.HttpMessage;
import cz.msebera.android.httpclient.ParseException;
import cz.msebera.android.httpclient.ProtocolException;
import cz.msebera.android.httpclient.config.MessageConstraints;
import cz.msebera.android.httpclient.config.MessageConstraints.Builder;
import cz.msebera.android.httpclient.io.HttpMessageParser;
import cz.msebera.android.httpclient.io.SessionInputBuffer;
import cz.msebera.android.httpclient.message.BasicLineParser;
import cz.msebera.android.httpclient.message.LineParser;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.CharArrayBuffer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public abstract class AbstractMessageParser<T extends HttpMessage> implements HttpMessageParser<T> {
    private final List<CharArrayBuffer> headerLines;
    public final LineParser lineParser;
    private T message;
    private final MessageConstraints messageConstraints;
    private final SessionInputBuffer sessionBuffer;
    private int state;

    public abstract T parseHead(SessionInputBuffer sessionInputBuffer) throws IOException, HttpException, ParseException;

    @Deprecated
    public AbstractMessageParser(SessionInputBuffer buffer, HttpParams params) {
        Args.notNull(buffer, "Session input buffer");
        Args.notNull(params, "HTTP parameters");
        this.sessionBuffer = buffer;
        Builder custom = MessageConstraints.custom();
        custom.maxHeaderCount = params.getIntParameter("http.connection.max-header-count", -1);
        custom.maxLineLength = params.getIntParameter("http.connection.max-line-length", -1);
        this.messageConstraints = custom.build();
        this.lineParser = BasicLineParser.INSTANCE;
        this.headerLines = new ArrayList();
        this.state = 0;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static cz.msebera.android.httpclient.Header[] parseHeaders(cz.msebera.android.httpclient.io.SessionInputBuffer r9, int r10, int r11, cz.msebera.android.httpclient.message.LineParser r12, java.util.List<cz.msebera.android.httpclient.util.CharArrayBuffer> r13) throws cz.msebera.android.httpclient.HttpException, java.io.IOException {
        /*
        r7 = "Session input buffer";
        cz.msebera.android.httpclient.util.Args.notNull(r9, r7);
        r7 = "Line parser";
        cz.msebera.android.httpclient.util.Args.notNull(r12, r7);
        r7 = "Header line list";
        cz.msebera.android.httpclient.util.Args.notNull(r13, r7);
        r2 = 0;
        r6 = 0;
    L_0x0011:
        if (r2 != 0) goto L_0x0051;
    L_0x0013:
        r2 = new cz.msebera.android.httpclient.util.CharArrayBuffer;
        r7 = 64;
        r2.<init>(r7);
    L_0x001a:
        r7 = r9.readLine(r2);
        r8 = -1;
        if (r7 == r8) goto L_0x0094;
    L_0x0021:
        r7 = r2.length();
        if (r7 <= 0) goto L_0x0094;
    L_0x0027:
        r7 = 0;
        r7 = r2.charAt(r7);
        r8 = 32;
        if (r7 == r8) goto L_0x0039;
    L_0x0030:
        r7 = 0;
        r7 = r2.charAt(r7);
        r8 = 9;
        if (r7 != r8) goto L_0x008e;
    L_0x0039:
        if (r6 == 0) goto L_0x008e;
    L_0x003b:
        r5 = 0;
    L_0x003c:
        r7 = r2.length();
        if (r5 >= r7) goto L_0x0055;
    L_0x0042:
        r1 = r2.charAt(r5);
        r7 = 32;
        if (r1 == r7) goto L_0x004e;
    L_0x004a:
        r7 = 9;
        if (r1 != r7) goto L_0x0055;
    L_0x004e:
        r5 = r5 + 1;
        goto L_0x003c;
    L_0x0051:
        r7 = 0;
        r2.len = r7;
        goto L_0x001a;
    L_0x0055:
        if (r11 <= 0) goto L_0x006d;
    L_0x0057:
        r7 = r6.length();
        r7 = r7 + 1;
        r8 = r2.length();
        r7 = r7 + r8;
        r7 = r7 - r5;
        if (r7 <= r11) goto L_0x006d;
    L_0x0065:
        r7 = new cz.msebera.android.httpclient.MessageConstraintException;
        r8 = "Maximum line length limit exceeded";
        r7.<init>(r8);
        throw r7;
    L_0x006d:
        r7 = 32;
        r6.append(r7);
        r7 = r2.length();
        r7 = r7 - r5;
        if (r2 == 0) goto L_0x007e;
    L_0x0079:
        r8 = r2.buffer;
        r6.append(r8, r5, r7);
    L_0x007e:
        if (r10 <= 0) goto L_0x0011;
    L_0x0080:
        r7 = r13.size();
        if (r7 < r10) goto L_0x0011;
    L_0x0086:
        r7 = new cz.msebera.android.httpclient.MessageConstraintException;
        r8 = "Maximum header count exceeded";
        r7.<init>(r8);
        throw r7;
    L_0x008e:
        r13.add(r2);
        r6 = r2;
        r2 = 0;
        goto L_0x007e;
    L_0x0094:
        r7 = r13.size();
        r4 = new cz.msebera.android.httpclient.Header[r7];
        r5 = 0;
    L_0x009b:
        r7 = r13.size();
        if (r5 >= r7) goto L_0x00bb;
    L_0x00a1:
        r0 = r13.get(r5);
        r0 = (cz.msebera.android.httpclient.util.CharArrayBuffer) r0;
        r7 = r12.parseHeader(r0);	 Catch:{ ParseException -> 0x00b0 }
        r4[r5] = r7;	 Catch:{ ParseException -> 0x00b0 }
        r5 = r5 + 1;
        goto L_0x009b;
    L_0x00b0:
        r3 = move-exception;
        r7 = new cz.msebera.android.httpclient.ProtocolException;
        r8 = r3.getMessage();
        r7.<init>(r8);
        throw r7;
    L_0x00bb:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: cz.msebera.android.httpclient.impl.io.AbstractMessageParser.parseHeaders(cz.msebera.android.httpclient.io.SessionInputBuffer, int, int, cz.msebera.android.httpclient.message.LineParser, java.util.List):cz.msebera.android.httpclient.Header[]");
    }

    public final T parse() throws IOException, HttpException {
        switch (this.state) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                try {
                    this.message = parseHead(this.sessionBuffer);
                    this.state = 1;
                    break;
                } catch (ParseException px) {
                    throw new ProtocolException(px.getMessage(), px);
                }
            case Logger.SEVERE /*1*/:
                break;
            default:
                throw new IllegalStateException("Inconsistent parser state");
        }
        this.message.setHeaders(parseHeaders(this.sessionBuffer, this.messageConstraints.maxHeaderCount, this.messageConstraints.maxLineLength, this.lineParser, this.headerLines));
        T result = this.message;
        this.message = null;
        this.headerLines.clear();
        this.state = 0;
        return result;
    }
}

package cz.msebera.android.httpclient.impl.conn;

import android.support.v4.media.TransportMediator;
import android.support.v7.appcompat.BuildConfig;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import cz.msebera.android.httpclient.util.Args;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class Wire {
    private final String id;
    public HttpClientAndroidLog log;

    private Wire(HttpClientAndroidLog log, String id) {
        this.log = log;
        this.id = id;
    }

    public Wire(HttpClientAndroidLog log) {
        this(log, BuildConfig.VERSION_NAME);
    }

    final void wire(String header, InputStream instream) throws IOException {
        StringBuilder buffer = new StringBuilder();
        while (true) {
            int ch = instream.read();
            if (ch == -1) {
                break;
            } else if (ch == 13) {
                buffer.append("[\\r]");
            } else if (ch == 10) {
                buffer.append("[\\n]\"");
                buffer.insert(0, "\"");
                buffer.insert(0, header);
                this.log.debug(this.id + " " + buffer.toString());
                buffer.setLength(0);
            } else if (ch < 32 || ch > TransportMediator.KEYCODE_MEDIA_PAUSE) {
                buffer.append("[0x");
                buffer.append(Integer.toHexString(ch));
                buffer.append("]");
            } else {
                buffer.append((char) ch);
            }
        }
        if (buffer.length() > 0) {
            buffer.append('\"');
            buffer.insert(0, '\"');
            buffer.insert(0, header);
            this.log.debug(this.id + " " + buffer.toString());
        }
    }

    public final void output(byte[] b) throws IOException {
        Args.notNull(b, "Output");
        wire(">> ", new ByteArrayInputStream(b));
    }

    public final void input(byte[] b) throws IOException {
        Args.notNull(b, "Input");
        wire("<< ", new ByteArrayInputStream(b));
    }
}

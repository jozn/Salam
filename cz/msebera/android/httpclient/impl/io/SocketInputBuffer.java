package cz.msebera.android.httpclient.impl.io;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import cz.msebera.android.httpclient.Consts;
import cz.msebera.android.httpclient.io.EofSensor;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.ByteArrayBuffer;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;

@Deprecated
public final class SocketInputBuffer extends AbstractSessionInputBuffer implements EofSensor {
    private boolean eof;
    private final Socket socket;

    public SocketInputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
        Args.notNull(socket, "Socket");
        this.socket = socket;
        this.eof = false;
        int n = buffersize;
        if (buffersize < 0) {
            n = socket.getReceiveBufferSize();
        }
        if (n < AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT) {
            n = AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT;
        }
        InputStream inputStream = socket.getInputStream();
        Args.notNull(inputStream, "Input stream");
        Args.notNegative(n, "Buffer size");
        Args.notNull(params, "HTTP parameters");
        this.instream = inputStream;
        this.buffer = new byte[n];
        this.bufferpos = 0;
        this.bufferlen = 0;
        this.linebuffer = new ByteArrayBuffer(n);
        String str = (String) params.getParameter("http.protocol.element-charset");
        this.charset = str != null ? Charset.forName(str) : Consts.ASCII;
        this.ascii = this.charset.equals(Consts.ASCII);
        this.decoder = null;
        this.maxLineLen = params.getIntParameter("http.connection.max-line-length", -1);
        this.minChunkLimit = params.getIntParameter("http.connection.min-chunk-limit", AccessibilityNodeInfoCompat.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY);
        this.metrics = new HttpTransportMetricsImpl();
        CodingErrorAction codingErrorAction = (CodingErrorAction) params.getParameter("http.malformed.input.action");
        if (codingErrorAction == null) {
            codingErrorAction = CodingErrorAction.REPORT;
        }
        this.onMalformedCharAction = codingErrorAction;
        codingErrorAction = (CodingErrorAction) params.getParameter("http.unmappable.input.action");
        if (codingErrorAction == null) {
            codingErrorAction = CodingErrorAction.REPORT;
        }
        this.onUnmappableCharAction = codingErrorAction;
    }

    protected final int fillBuffer() throws IOException {
        int i = super.fillBuffer();
        this.eof = i == -1;
        return i;
    }

    public final boolean isDataAvailable(int timeout) throws IOException {
        boolean result = hasBufferedData();
        if (!result) {
            int oldtimeout = this.socket.getSoTimeout();
            try {
                this.socket.setSoTimeout(timeout);
                fillBuffer();
                result = hasBufferedData();
            } finally {
                this.socket.setSoTimeout(oldtimeout);
            }
        }
        return result;
    }

    public final boolean isEof() {
        return this.eof;
    }
}

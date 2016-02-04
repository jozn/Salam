package cz.msebera.android.httpclient.impl.io;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import cz.msebera.android.httpclient.Consts;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.ByteArrayBuffer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;

@Deprecated
public final class SocketOutputBuffer extends AbstractSessionOutputBuffer {
    public SocketOutputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
        Args.notNull(socket, "Socket");
        int n = buffersize;
        if (buffersize < 0) {
            n = socket.getSendBufferSize();
        }
        if (n < AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT) {
            n = AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT;
        }
        OutputStream outputStream = socket.getOutputStream();
        Args.notNull(outputStream, "Input stream");
        Args.notNegative(n, "Buffer size");
        Args.notNull(params, "HTTP parameters");
        this.outstream = outputStream;
        this.buffer = new ByteArrayBuffer(n);
        String str = (String) params.getParameter("http.protocol.element-charset");
        this.charset = str != null ? Charset.forName(str) : Consts.ASCII;
        this.ascii = this.charset.equals(Consts.ASCII);
        this.encoder = null;
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
}

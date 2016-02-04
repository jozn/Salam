package cz.msebera.android.httpclient.impl;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpClientConnection;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpEntityEnclosingRequest;
import cz.msebera.android.httpclient.HttpException;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpResponseFactory;
import cz.msebera.android.httpclient.entity.BasicHttpEntity;
import cz.msebera.android.httpclient.impl.entity.EntityDeserializer;
import cz.msebera.android.httpclient.impl.entity.EntitySerializer;
import cz.msebera.android.httpclient.impl.entity.LaxContentLengthStrategy;
import cz.msebera.android.httpclient.impl.entity.StrictContentLengthStrategy;
import cz.msebera.android.httpclient.impl.io.ChunkedInputStream;
import cz.msebera.android.httpclient.impl.io.ChunkedOutputStream;
import cz.msebera.android.httpclient.impl.io.ContentLengthInputStream;
import cz.msebera.android.httpclient.impl.io.ContentLengthOutputStream;
import cz.msebera.android.httpclient.impl.io.DefaultHttpResponseParser;
import cz.msebera.android.httpclient.impl.io.IdentityInputStream;
import cz.msebera.android.httpclient.impl.io.IdentityOutputStream;
import cz.msebera.android.httpclient.io.EofSensor;
import cz.msebera.android.httpclient.io.HttpMessageParser;
import cz.msebera.android.httpclient.io.HttpMessageWriter;
import cz.msebera.android.httpclient.io.SessionInputBuffer;
import cz.msebera.android.httpclient.io.SessionOutputBuffer;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.Args;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;

@Deprecated
public abstract class AbstractHttpClientConnection implements HttpClientConnection {
    private final EntityDeserializer entitydeserializer;
    private final EntitySerializer entityserializer;
    EofSensor eofSensor;
    SessionInputBuffer inbuffer;
    HttpConnectionMetricsImpl metrics;
    SessionOutputBuffer outbuffer;
    HttpMessageWriter<HttpRequest> requestWriter;
    HttpMessageParser<HttpResponse> responseParser;

    protected abstract void assertOpen() throws IllegalStateException;

    public AbstractHttpClientConnection() {
        this.inbuffer = null;
        this.outbuffer = null;
        this.eofSensor = null;
        this.responseParser = null;
        this.requestWriter = null;
        this.metrics = null;
        this.entityserializer = new EntitySerializer(new StrictContentLengthStrategy((byte) 0));
        this.entitydeserializer = new EntityDeserializer(new LaxContentLengthStrategy((byte) 0));
    }

    public HttpMessageParser<HttpResponse> createResponseParser(SessionInputBuffer buffer, HttpResponseFactory responseFactory, HttpParams params) {
        return new DefaultHttpResponseParser(buffer, responseFactory, params);
    }

    public final boolean isResponseAvailable(int timeout) throws IOException {
        assertOpen();
        try {
            return this.inbuffer.isDataAvailable(timeout);
        } catch (SocketTimeoutException e) {
            return false;
        }
    }

    public void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        assertOpen();
        this.requestWriter.write(request);
        HttpConnectionMetricsImpl httpConnectionMetricsImpl = this.metrics;
        httpConnectionMetricsImpl.requestCount++;
    }

    public final void sendRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        assertOpen();
        if (request.getEntity() != null) {
            EntitySerializer entitySerializer = this.entityserializer;
            SessionOutputBuffer sessionOutputBuffer = this.outbuffer;
            HttpEntity entity = request.getEntity();
            Args.notNull(sessionOutputBuffer, "Session output buffer");
            Args.notNull(request, "HTTP message");
            Args.notNull(entity, "HTTP entity");
            long determineLength = entitySerializer.lenStrategy.determineLength(request);
            OutputStream chunkedOutputStream = determineLength == -2 ? new ChunkedOutputStream(sessionOutputBuffer) : determineLength == -1 ? new IdentityOutputStream(sessionOutputBuffer) : new ContentLengthOutputStream(sessionOutputBuffer, determineLength);
            entity.writeTo(chunkedOutputStream);
            chunkedOutputStream.close();
        }
    }

    protected final void doFlush() throws IOException {
        this.outbuffer.flush();
    }

    public final void flush() throws IOException {
        assertOpen();
        doFlush();
    }

    public HttpResponse receiveResponseHeader() throws HttpException, IOException {
        assertOpen();
        HttpResponse response = (HttpResponse) this.responseParser.parse();
        if (response.getStatusLine().getStatusCode() >= 200) {
            HttpConnectionMetricsImpl httpConnectionMetricsImpl = this.metrics;
            httpConnectionMetricsImpl.responseCount++;
        }
        return response;
    }

    public final void receiveResponseEntity(HttpResponse response) throws HttpException, IOException {
        Args.notNull(response, "HTTP response");
        assertOpen();
        EntityDeserializer entityDeserializer = this.entitydeserializer;
        SessionInputBuffer sessionInputBuffer = this.inbuffer;
        Args.notNull(sessionInputBuffer, "Session input buffer");
        Args.notNull(response, "HTTP message");
        BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
        long determineLength = entityDeserializer.lenStrategy.determineLength(response);
        if (determineLength == -2) {
            basicHttpEntity.setChunked(true);
            basicHttpEntity.length = -1;
            basicHttpEntity.content = new ChunkedInputStream(sessionInputBuffer, (byte) 0);
        } else if (determineLength == -1) {
            basicHttpEntity.setChunked(false);
            basicHttpEntity.length = -1;
            basicHttpEntity.content = new IdentityInputStream(sessionInputBuffer);
        } else {
            basicHttpEntity.setChunked(false);
            basicHttpEntity.length = determineLength;
            basicHttpEntity.content = new ContentLengthInputStream(sessionInputBuffer, determineLength);
        }
        Header firstHeader = response.getFirstHeader("Content-Type");
        if (firstHeader != null) {
            basicHttpEntity.setContentType(firstHeader);
        }
        firstHeader = response.getFirstHeader("Content-Encoding");
        if (firstHeader != null) {
            basicHttpEntity.setContentEncoding(firstHeader);
        }
        response.setEntity(basicHttpEntity);
    }

    private boolean isEof() {
        return this.eofSensor != null && this.eofSensor.isEof();
    }

    public final boolean isStale() {
        boolean z = true;
        if (!isOpen() || isEof()) {
            return z;
        }
        try {
            this.inbuffer.isDataAvailable(1);
            return isEof();
        } catch (SocketTimeoutException e) {
            return false;
        } catch (IOException e2) {
            return z;
        }
    }
}

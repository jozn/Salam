package cz.msebera.android.httpclient.protocol;

import cz.msebera.android.httpclient.HttpClientConnection;
import cz.msebera.android.httpclient.HttpEntityEnclosingRequest;
import cz.msebera.android.httpclient.HttpException;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpVersion;
import cz.msebera.android.httpclient.ProtocolException;
import cz.msebera.android.httpclient.ProtocolVersion;
import cz.msebera.android.httpclient.util.Args;
import java.io.IOException;

public final class HttpRequestExecutor {
    private final int waitForContinue;

    private HttpRequestExecutor() {
        this.waitForContinue = Args.positive(3000, "Wait for continue time");
    }

    public HttpRequestExecutor(byte b) {
        this();
    }

    private static boolean canResponseHaveBody(HttpRequest request, HttpResponse response) {
        if ("HEAD".equalsIgnoreCase(request.getRequestLine().getMethod())) {
            return false;
        }
        int status = response.getStatusLine().getStatusCode();
        if (status < 200 || status == 204 || status == 304 || status == 205) {
            return false;
        }
        return true;
    }

    public final HttpResponse execute(HttpRequest request, HttpClientConnection conn, HttpContext context) throws IOException, HttpException {
        HttpResponse response = null;
        Args.notNull(request, "HTTP request");
        Args.notNull(conn, "Client connection");
        Args.notNull(context, "HTTP context");
        try {
            Args.notNull(request, "HTTP request");
            Args.notNull(conn, "Client connection");
            Args.notNull(context, "HTTP context");
            context.setAttribute("http.connection", conn);
            context.setAttribute("http.request_sent", Boolean.FALSE);
            conn.sendRequestHeader(request);
            if (request instanceof HttpEntityEnclosingRequest) {
                int statusCode;
                Object obj;
                ProtocolVersion protocolVersion = request.getRequestLine().getProtocolVersion();
                if (((HttpEntityEnclosingRequest) request).expectContinue() && !protocolVersion.lessEquals(HttpVersion.HTTP_1_0)) {
                    conn.flush();
                    if (conn.isResponseAvailable(this.waitForContinue)) {
                        HttpResponse receiveResponseHeader = conn.receiveResponseHeader();
                        if (canResponseHaveBody(request, receiveResponseHeader)) {
                            conn.receiveResponseEntity(receiveResponseHeader);
                        }
                        statusCode = receiveResponseHeader.getStatusLine().getStatusCode();
                        if (statusCode >= 200) {
                            obj = null;
                            response = receiveResponseHeader;
                        } else if (statusCode != 100) {
                            throw new ProtocolException("Unexpected response: " + receiveResponseHeader.getStatusLine());
                        } else {
                            obj = 1;
                        }
                        if (obj != null) {
                            conn.sendRequestEntity((HttpEntityEnclosingRequest) request);
                        }
                    }
                }
                statusCode = 1;
                if (obj != null) {
                    conn.sendRequestEntity((HttpEntityEnclosingRequest) request);
                }
            }
            conn.flush();
            context.setAttribute("http.request_sent", Boolean.TRUE);
            if (response == null) {
                response = doReceiveResponse(request, conn, context);
            }
            return response;
        } catch (IOException ex) {
            closeConnection(conn);
            throw ex;
        } catch (HttpException ex2) {
            closeConnection(conn);
            throw ex2;
        } catch (RuntimeException ex3) {
            closeConnection(conn);
            throw ex3;
        }
    }

    private static void closeConnection(HttpClientConnection conn) {
        try {
            conn.close();
        } catch (IOException e) {
        }
    }

    public static void preProcess(HttpRequest request, HttpProcessor processor, HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        Args.notNull(processor, "HTTP processor");
        Args.notNull(context, "HTTP context");
        context.setAttribute("http.request", request);
        processor.process(request, context);
    }

    private static HttpResponse doReceiveResponse(HttpRequest request, HttpClientConnection conn, HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        Args.notNull(conn, "Client connection");
        Args.notNull(context, "HTTP context");
        HttpResponse response = null;
        int statusCode = 0;
        while (true) {
            if (response != null && statusCode >= 200) {
                return response;
            }
            response = conn.receiveResponseHeader();
            if (canResponseHaveBody(request, response)) {
                conn.receiveResponseEntity(response);
            }
            statusCode = response.getStatusLine().getStatusCode();
        }
    }

    public static void postProcess(HttpResponse response, HttpProcessor processor, HttpContext context) throws HttpException, IOException {
        Args.notNull(response, "HTTP response");
        Args.notNull(processor, "HTTP processor");
        Args.notNull(context, "HTTP context");
        context.setAttribute("http.response", response);
        processor.process(response, context);
    }
}

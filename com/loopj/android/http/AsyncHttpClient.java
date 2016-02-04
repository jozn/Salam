package com.loopj.android.http;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpException;
import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.HttpRequestInterceptor;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpResponseInterceptor;
import cz.msebera.android.httpclient.HttpVersion;
import cz.msebera.android.httpclient.auth.AuthScope;
import cz.msebera.android.httpclient.auth.AuthState;
import cz.msebera.android.httpclient.auth.Credentials;
import cz.msebera.android.httpclient.client.CredentialsProvider;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.conn.ClientConnectionManager;
import cz.msebera.android.httpclient.conn.params.ConnManagerParams;
import cz.msebera.android.httpclient.conn.params.ConnPerRouteBean;
import cz.msebera.android.httpclient.conn.scheme.PlainSocketFactory;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.conn.scheme.SocketFactory;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.entity.HttpEntityWrapper;
import cz.msebera.android.httpclient.impl.auth.BasicScheme;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.impl.conn.tsccm.ThreadSafeClientConnManager;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.params.HttpProtocolParams;
import cz.msebera.android.httpclient.protocol.BasicHttpContext;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.protocol.SyncBasicHttpContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;

public class AsyncHttpClient {
    public static LogInterface log;
    private final Map<String, String> clientHeaderMap;
    private int connectTimeout;
    private final DefaultHttpClient httpClient;
    private final HttpContext httpContext;
    private boolean isUrlEncodingEnabled;
    private int maxConnections;
    private final Map<Context, List<RequestHandle>> requestMap;
    private int responseTimeout;
    private ExecutorService threadPool;

    /* renamed from: com.loopj.android.http.AsyncHttpClient.1 */
    class C04741 implements HttpRequestInterceptor {
        C04741() {
        }

        public final void process(HttpRequest request, HttpContext context) {
            if (!request.containsHeader("Accept-Encoding")) {
                request.addHeader("Accept-Encoding", "gzip");
            }
            for (String header : AsyncHttpClient.this.clientHeaderMap.keySet()) {
                if (request.containsHeader(header)) {
                    AsyncHttpClient.log.m15d("AsyncHttpClient", String.format("Headers were overwritten! (%s | %s) overwrites (%s | %s)", new Object[]{header, AsyncHttpClient.this.clientHeaderMap.get(header), overwritten.getName(), request.getFirstHeader(header).getValue()}));
                    request.removeHeader(overwritten);
                }
                request.addHeader(header, (String) AsyncHttpClient.this.clientHeaderMap.get(header));
            }
        }
    }

    /* renamed from: com.loopj.android.http.AsyncHttpClient.2 */
    class C04752 implements HttpResponseInterceptor {
        C04752() {
        }

        public final void process(HttpResponse response, HttpContext context) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                Header encoding = entity.getContentEncoding();
                if (encoding != null) {
                    for (HeaderElement name : encoding.getElements()) {
                        if (name.getName().equalsIgnoreCase("gzip")) {
                            response.setEntity(new InflatingEntity(entity));
                            return;
                        }
                    }
                }
            }
        }
    }

    /* renamed from: com.loopj.android.http.AsyncHttpClient.3 */
    class C04763 implements HttpRequestInterceptor {
        C04763() {
        }

        public final void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
            AuthState authState = (AuthState) context.getAttribute("http.auth.target-scope");
            CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute("http.auth.credentials-provider");
            HttpHost targetHost = (HttpHost) context.getAttribute("http.target_host");
            if (authState.authScheme == null) {
                Credentials creds = credsProvider.getCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()));
                if (creds != null) {
                    authState.authScheme = new BasicScheme();
                    authState.credentials = creds;
                }
            }
        }
    }

    private static class InflatingEntity extends HttpEntityWrapper {
        GZIPInputStream gzippedStream;
        PushbackInputStream pushbackStream;
        InputStream wrappedStream;

        public InflatingEntity(HttpEntity wrapped) {
            super(wrapped);
        }

        public final InputStream getContent() throws IOException {
            this.wrappedStream = this.wrappedEntity.getContent();
            this.pushbackStream = new PushbackInputStream(this.wrappedStream, 2);
            if (!AsyncHttpClient.isInputStreamGZIPCompressed(this.pushbackStream)) {
                return this.pushbackStream;
            }
            this.gzippedStream = new GZIPInputStream(this.pushbackStream);
            return this.gzippedStream;
        }

        public final long getContentLength() {
            return this.wrappedEntity == null ? 0 : this.wrappedEntity.getContentLength();
        }

        public final void consumeContent() throws IOException {
            AsyncHttpClient.silentCloseInputStream(this.wrappedStream);
            AsyncHttpClient.silentCloseInputStream(this.pushbackStream);
            AsyncHttpClient.silentCloseInputStream(this.gzippedStream);
            super.consumeContent();
        }
    }

    static {
        log = new LogHandler();
    }

    public AsyncHttpClient() {
        this((byte) 0);
    }

    public AsyncHttpClient(byte fixNoHttpResponseException) {
        SocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", new PlainSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", socketFactory, 443));
        this(schemeRegistry);
    }

    private AsyncHttpClient(SchemeRegistry schemeRegistry) {
        this.maxConnections = 10;
        this.connectTimeout = 10000;
        this.responseTimeout = 10000;
        this.isUrlEncodingEnabled = true;
        HttpParams httpParams = new BasicHttpParams();
        ConnManagerParams.setTimeout(httpParams, (long) this.connectTimeout);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(this.maxConnections));
        ConnManagerParams.setMaxTotalConnections$465ac0a8(httpParams);
        HttpConnectionParams.setSoTimeout(httpParams, this.responseTimeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, this.connectTimeout);
        HttpConnectionParams.setTcpNoDelay$465b0079(httpParams);
        HttpConnectionParams.setSocketBufferSize$465ac0a8(httpParams);
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        ClientConnectionManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
        Utils.asserts(true, "Custom implementation of #createConnectionManager(SchemeRegistry, BasicHttpParams) returned null");
        this.threadPool = Executors.newCachedThreadPool();
        this.requestMap = Collections.synchronizedMap(new WeakHashMap());
        this.clientHeaderMap = new HashMap();
        this.httpContext = new SyncBasicHttpContext(new BasicHttpContext());
        this.httpClient = new DefaultHttpClient(cm, httpParams);
        this.httpClient.addRequestInterceptor(new C04741());
        this.httpClient.addResponseInterceptor(new C04752());
        this.httpClient.addRequestInterceptor$62d44063(new C04763());
        this.httpClient.setHttpRequestRetryHandler(new RetryHandler(5, 1500));
    }

    private static String getUrlWithQueryString$3ace9a33(boolean shouldEncodeUrl, String url) {
        if (url == null) {
            return null;
        }
        if (shouldEncodeUrl) {
            try {
                URL _url = new URL(URLDecoder.decode(url, "UTF-8"));
                url = new URI(_url.getProtocol(), _url.getUserInfo(), _url.getHost(), _url.getPort(), _url.getPath(), _url.getQuery(), _url.getRef()).toASCIIString();
            } catch (Exception ex) {
                log.m17e("AsyncHttpClient", "getUrlWithQueryString encoding URL", ex);
            }
        }
        return url;
    }

    public static boolean isInputStreamGZIPCompressed(PushbackInputStream inputStream) throws IOException {
        if (inputStream == null) {
            return false;
        }
        byte[] signature = new byte[2];
        int count = 0;
        while (count < 2) {
            try {
                int readCount = inputStream.read(signature, count, 2 - count);
                if (readCount < 0) {
                    return false;
                }
                count += readCount;
            } finally {
                inputStream.unread(signature, 0, count);
            }
        }
        inputStream.unread(signature, 0, count);
        if (35615 == ((signature[0] & MotionEventCompat.ACTION_MASK) | ((signature[1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK))) {
            return true;
        }
        return false;
    }

    public static void silentCloseInputStream(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                log.m21w("AsyncHttpClient", "Cannot close input stream", e);
            }
        }
    }

    public static void silentCloseOutputStream(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                log.m21w("AsyncHttpClient", "Cannot close output stream", e);
            }
        }
    }

    public static void endEntityViaReflection(HttpEntity entity) {
        if (entity instanceof HttpEntityWrapper) {
            Field f = null;
            try {
                for (Field ff : HttpEntityWrapper.class.getDeclaredFields()) {
                    if (ff.getName().equals("wrappedEntity")) {
                        f = ff;
                        break;
                    }
                }
                if (f != null) {
                    f.setAccessible(true);
                    HttpEntity wrapped = (HttpEntity) f.get(entity);
                    if (wrapped != null) {
                        wrapped.consumeContent();
                    }
                }
            } catch (Throwable t) {
                log.m17e("AsyncHttpClient", "wrappedEntity consume", t);
            }
        }
    }

    public final void setTimeout(int value) {
        int i = 10000;
        if (value < 1000) {
            value = 10000;
        }
        if (value >= 1000) {
            i = value;
        }
        this.connectTimeout = i;
        HttpParams params = this.httpClient.getParams();
        ConnManagerParams.setTimeout(params, (long) this.connectTimeout);
        HttpConnectionParams.setConnectionTimeout(params, this.connectTimeout);
        setResponseTimeout(value);
    }

    public final void setResponseTimeout(int value) {
        if (value < 1000) {
            value = 10000;
        }
        this.responseTimeout = value;
        HttpConnectionParams.setSoTimeout(this.httpClient.getParams(), this.responseTimeout);
    }

    public final void setMaxRetriesAndTimeout(int retries, int timeout) {
        this.httpClient.setHttpRequestRetryHandler(new RetryHandler(retries, timeout));
    }

    public final RequestHandle get(String url, ResponseHandlerInterface responseHandler) {
        return get$5f79a0a4$3a5bfd5a(url, responseHandler);
    }

    public final RequestHandle get$3a5bfd5a(String url, ResponseHandlerInterface responseHandler) {
        return get$5f79a0a4$3a5bfd5a(url, responseHandler);
    }

    private RequestHandle get$5f79a0a4$3a5bfd5a(String url, ResponseHandlerInterface responseHandler) {
        return sendRequest$56c1b3a2(this.httpClient, this.httpContext, new HttpGet(getUrlWithQueryString$3ace9a33(this.isUrlEncodingEnabled, url)), responseHandler, null);
    }

    public final RequestHandle post(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        HttpEntity paramsToEntity = paramsToEntity(params, responseHandler);
        DefaultHttpClient defaultHttpClient = this.httpClient;
        HttpContext httpContext = this.httpContext;
        HttpUriRequest httpPost = new HttpPost(URI.create(url).normalize());
        if (paramsToEntity != null) {
            httpPost.entity = paramsToEntity;
        }
        return sendRequest$56c1b3a2(defaultHttpClient, httpContext, httpPost, responseHandler, context);
    }

    protected static AsyncHttpRequest newAsyncHttpRequest$bbf1bb6(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest, ResponseHandlerInterface responseHandler) {
        return new AsyncHttpRequest(client, httpContext, uriRequest, responseHandler);
    }

    protected RequestHandle sendRequest$56c1b3a2(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest, ResponseHandlerInterface responseHandler, Context context) {
        if (responseHandler == null) {
            throw new IllegalArgumentException("ResponseHandler must not be null");
        } else if (!responseHandler.getUseSynchronousMode() || responseHandler.getUsePoolThread()) {
            responseHandler.setRequestHeaders(uriRequest.getAllHeaders());
            responseHandler.setRequestURI(uriRequest.getURI());
            AsyncHttpRequest request = newAsyncHttpRequest$bbf1bb6(client, httpContext, uriRequest, responseHandler);
            this.threadPool.submit(request);
            RequestHandle requestHandle = new RequestHandle(request);
            if (context != null) {
                List<RequestHandle> requestList;
                synchronized (this.requestMap) {
                    requestList = (List) this.requestMap.get(context);
                    if (requestList == null) {
                        requestList = Collections.synchronizedList(new LinkedList());
                        this.requestMap.put(context, requestList);
                    }
                }
                requestList.add(requestHandle);
                Iterator<RequestHandle> iterator = requestList.iterator();
                while (iterator.hasNext()) {
                    Object obj;
                    RequestHandle requestHandle2 = (RequestHandle) iterator.next();
                    AsyncHttpRequest asyncHttpRequest = (AsyncHttpRequest) requestHandle2.request.get();
                    if (asyncHttpRequest == null || asyncHttpRequest.isCancelled()) {
                        obj = 1;
                    } else {
                        obj = null;
                    }
                    if (obj == null) {
                        asyncHttpRequest = (AsyncHttpRequest) requestHandle2.request.get();
                        if (asyncHttpRequest != null) {
                            obj = (asyncHttpRequest.isCancelled() || asyncHttpRequest.isFinished) ? 1 : null;
                            if (obj == null) {
                                obj = null;
                                if (obj == null) {
                                    obj = null;
                                    if (obj != null) {
                                        requestHandle2.request.clear();
                                    }
                                    if (obj == null) {
                                        iterator.remove();
                                    }
                                }
                            }
                        }
                        obj = 1;
                        if (obj == null) {
                            obj = null;
                            if (obj != null) {
                                requestHandle2.request.clear();
                            }
                            if (obj == null) {
                                iterator.remove();
                            }
                        }
                    }
                    obj = 1;
                    if (obj != null) {
                        requestHandle2.request.clear();
                    }
                    if (obj == null) {
                        iterator.remove();
                    }
                }
            }
            return requestHandle;
        } else {
            throw new IllegalArgumentException("Synchronous ResponseHandler used in AsyncHttpClient. You should create your response handler in a looper thread or use SyncHttpClient instead.");
        }
    }

    private static HttpEntity paramsToEntity(RequestParams params, ResponseHandlerInterface responseHandler) {
        HttpEntity entity = null;
        if (params != null) {
            try {
                entity = params.getEntity(responseHandler);
            } catch (IOException e) {
                if (responseHandler != null) {
                    responseHandler.sendFailureMessage(0, null, null, e);
                } else {
                    e.printStackTrace();
                }
            }
        }
        return entity;
    }
}

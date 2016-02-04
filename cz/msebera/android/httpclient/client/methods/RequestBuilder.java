package cz.msebera.android.httpclient.client.methods;

import cz.msebera.android.httpclient.Consts;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpEntityEnclosingRequest;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.ProtocolVersion;
import cz.msebera.android.httpclient.client.config.RequestConfig;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.utils.URIBuilder;
import cz.msebera.android.httpclient.client.utils.URLEncodedUtils;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.message.HeaderGroup;
import cz.msebera.android.httpclient.protocol.HTTP;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public final class RequestBuilder {
    private Charset charset;
    private RequestConfig config;
    private HttpEntity entity;
    private HeaderGroup headergroup;
    private String method;
    private List<NameValuePair> parameters;
    public URI uri;
    private ProtocolVersion version;

    static class InternalEntityEclosingRequest extends HttpEntityEnclosingRequestBase {
        private final String method;

        InternalEntityEclosingRequest(String method) {
            this.method = method;
        }

        public final String getMethod() {
            return this.method;
        }
    }

    static class InternalRequest extends HttpRequestBase {
        private final String method;

        InternalRequest(String method) {
            this.method = method;
        }

        public final String getMethod() {
            return this.method;
        }
    }

    private RequestBuilder() {
        this.charset = Consts.UTF_8;
        this.method = null;
    }

    public RequestBuilder(byte b) {
        this();
    }

    public final RequestBuilder doCopy(HttpRequest request) {
        if (request != null) {
            URI originalUri;
            this.method = request.getRequestLine().getMethod();
            this.version = request.getRequestLine().getProtocolVersion();
            if (this.headergroup == null) {
                this.headergroup = new HeaderGroup();
            }
            this.headergroup.clear();
            this.headergroup.setHeaders(request.getAllHeaders());
            this.parameters = null;
            this.entity = null;
            if (request instanceof HttpEntityEnclosingRequest) {
                HttpEntity originalEntity = ((HttpEntityEnclosingRequest) request).getEntity();
                ContentType contentType = ContentType.get(originalEntity);
                if (contentType == null || !contentType.mimeType.equals(ContentType.APPLICATION_FORM_URLENCODED.mimeType)) {
                    this.entity = originalEntity;
                } else {
                    try {
                        List<NameValuePair> formParams = URLEncodedUtils.parse(originalEntity);
                        if (!formParams.isEmpty()) {
                            this.parameters = formParams;
                        }
                    } catch (IOException e) {
                    }
                }
            }
            if (request instanceof HttpUriRequest) {
                originalUri = ((HttpUriRequest) request).getURI();
            } else {
                originalUri = URI.create(request.getRequestLine().getUri());
            }
            URIBuilder uriBuilder = new URIBuilder(originalUri);
            if (this.parameters == null) {
                List<NameValuePair> queryParams = uriBuilder.queryParams != null ? new ArrayList(uriBuilder.queryParams) : new ArrayList();
                if (queryParams.isEmpty()) {
                    this.parameters = null;
                } else {
                    this.parameters = queryParams;
                    uriBuilder.queryParams = null;
                    uriBuilder.encodedQuery = null;
                    uriBuilder.encodedSchemeSpecificPart = null;
                }
            }
            try {
                this.uri = uriBuilder.build();
            } catch (URISyntaxException e2) {
                this.uri = originalUri;
            }
            if (request instanceof Configurable) {
                this.config = ((Configurable) request).getConfig();
            } else {
                this.config = null;
            }
        }
        return this;
    }

    public final HttpUriRequest build() {
        HttpRequestBase result;
        URI uriNotNull = this.uri != null ? this.uri : URI.create(MqttTopic.TOPIC_LEVEL_SEPARATOR);
        HttpEntity entityCopy = this.entity;
        if (!(this.parameters == null || this.parameters.isEmpty())) {
            if (entityCopy == null && ("POST".equalsIgnoreCase(this.method) || "PUT".equalsIgnoreCase(this.method))) {
                entityCopy = new UrlEncodedFormEntity(this.parameters, HTTP.DEF_CONTENT_CHARSET);
            } else {
                try {
                    URIBuilder uRIBuilder = new URIBuilder(uriNotNull);
                    uRIBuilder.charset = this.charset;
                    Collection collection = this.parameters;
                    if (uRIBuilder.queryParams == null) {
                        uRIBuilder.queryParams = new ArrayList();
                    }
                    uRIBuilder.queryParams.addAll(collection);
                    uRIBuilder.encodedQuery = null;
                    uRIBuilder.encodedSchemeSpecificPart = null;
                    uRIBuilder.query = null;
                    uriNotNull = uRIBuilder.build();
                } catch (URISyntaxException e) {
                }
            }
        }
        if (entityCopy == null) {
            result = new InternalRequest(this.method);
        } else {
            HttpRequestBase request = new InternalEntityEclosingRequest(this.method);
            request.entity = entityCopy;
            result = request;
        }
        result.version = this.version;
        result.uri = uriNotNull;
        if (this.headergroup != null) {
            result.setHeaders(this.headergroup.getAllHeaders());
        }
        result.config = this.config;
        return result;
    }
}

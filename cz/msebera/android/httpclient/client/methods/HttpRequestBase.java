package cz.msebera.android.httpclient.client.methods;

import cz.msebera.android.httpclient.ProtocolVersion;
import cz.msebera.android.httpclient.RequestLine;
import cz.msebera.android.httpclient.client.config.RequestConfig;
import cz.msebera.android.httpclient.message.BasicRequestLine;
import cz.msebera.android.httpclient.params.HttpProtocolParams;
import java.net.URI;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public abstract class HttpRequestBase extends AbstractExecutionAwareRequest implements Configurable, HttpUriRequest {
    RequestConfig config;
    public URI uri;
    ProtocolVersion version;

    public abstract String getMethod();

    public final ProtocolVersion getProtocolVersion() {
        return this.version != null ? this.version : HttpProtocolParams.getVersion(getParams());
    }

    public final URI getURI() {
        return this.uri;
    }

    public final RequestLine getRequestLine() {
        String method = getMethod();
        ProtocolVersion ver = getProtocolVersion();
        URI uriCopy = this.uri;
        String uritext = null;
        if (uriCopy != null) {
            uritext = uriCopy.toASCIIString();
        }
        if (uritext == null || uritext.isEmpty()) {
            uritext = MqttTopic.TOPIC_LEVEL_SEPARATOR;
        }
        return new BasicRequestLine(method, uritext, ver);
    }

    public final RequestConfig getConfig() {
        return this.config;
    }

    public String toString() {
        return getMethod() + " " + this.uri + " " + getProtocolVersion();
    }
}

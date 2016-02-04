package cz.msebera.android.httpclient.params;

import cz.msebera.android.httpclient.HttpVersion;
import cz.msebera.android.httpclient.ProtocolVersion;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.Args;

@Deprecated
public final class HttpProtocolParams {
    public static String getHttpElementCharset(HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        String charset = (String) params.getParameter("http.protocol.element-charset");
        if (charset == null) {
            return HTTP.DEF_PROTOCOL_CHARSET.name();
        }
        return charset;
    }

    public static ProtocolVersion getVersion(HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        Object param = params.getParameter("http.protocol.version");
        if (param == null) {
            return HttpVersion.HTTP_1_1;
        }
        return (ProtocolVersion) param;
    }

    public static void setVersion(HttpParams params, ProtocolVersion version) {
        Args.notNull(params, "HTTP parameters");
        params.setParameter("http.protocol.version", version);
    }
}

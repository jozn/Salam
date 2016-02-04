package cz.msebera.android.httpclient.message;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpVersion;
import cz.msebera.android.httpclient.ProtocolVersion;
import cz.msebera.android.httpclient.ReasonPhraseCatalog;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.util.Args;
import java.util.Locale;

public final class BasicHttpResponse extends AbstractHttpMessage implements HttpResponse {
    private int code;
    private HttpEntity entity;
    private Locale locale;
    private final ReasonPhraseCatalog reasonCatalog;
    private String reasonPhrase;
    private StatusLine statusline;
    private ProtocolVersion ver;

    public BasicHttpResponse(StatusLine statusline, ReasonPhraseCatalog catalog, Locale locale) {
        super((byte) 0);
        this.statusline = (StatusLine) Args.notNull(statusline, "Status line");
        this.ver = statusline.getProtocolVersion();
        this.code = statusline.getStatusCode();
        this.reasonPhrase = statusline.getReasonPhrase();
        this.reasonCatalog = catalog;
        this.locale = locale;
    }

    public final ProtocolVersion getProtocolVersion() {
        return this.ver;
    }

    public final StatusLine getStatusLine() {
        if (this.statusline == null) {
            String str;
            ProtocolVersion protocolVersion = this.ver != null ? this.ver : HttpVersion.HTTP_1_1;
            int i = this.code;
            if (this.reasonPhrase != null) {
                str = this.reasonPhrase;
            } else {
                int i2 = this.code;
                if (this.reasonCatalog != null) {
                    ReasonPhraseCatalog reasonPhraseCatalog = this.reasonCatalog;
                    if (this.locale == null) {
                        Locale.getDefault();
                    }
                    str = reasonPhraseCatalog.getReason$520367a3(i2);
                } else {
                    str = null;
                }
            }
            this.statusline = new BasicStatusLine(protocolVersion, i, str);
        }
        return this.statusline;
    }

    public final HttpEntity getEntity() {
        return this.entity;
    }

    public final void setEntity(HttpEntity entity) {
        this.entity = entity;
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getStatusLine());
        sb.append(' ');
        sb.append(this.headergroup);
        if (this.entity != null) {
            sb.append(' ');
            sb.append(this.entity);
        }
        return sb.toString();
    }
}

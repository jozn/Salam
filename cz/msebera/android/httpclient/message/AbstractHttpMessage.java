package cz.msebera.android.httpclient.message;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderIterator;
import cz.msebera.android.httpclient.HttpMessage;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.Args;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHttpMessage implements HttpMessage {
    public HeaderGroup headergroup;
    @Deprecated
    public HttpParams params;

    @Deprecated
    private AbstractHttpMessage() {
        this.headergroup = new HeaderGroup();
        this.params = null;
    }

    public AbstractHttpMessage(byte b) {
        this();
    }

    public final boolean containsHeader(String name) {
        HeaderGroup headerGroup = this.headergroup;
        for (int i = 0; i < headerGroup.headers.size(); i++) {
            if (((Header) headerGroup.headers.get(i)).getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public final Header[] getHeaders(String name) {
        HeaderGroup headerGroup = this.headergroup;
        List list = null;
        for (int i = 0; i < headerGroup.headers.size(); i++) {
            Header header = (Header) headerGroup.headers.get(i);
            if (header.getName().equalsIgnoreCase(name)) {
                if (list == null) {
                    list = new ArrayList();
                }
                list.add(header);
            }
        }
        return list != null ? (Header[]) list.toArray(new Header[list.size()]) : headerGroup.EMPTY;
    }

    public final Header getFirstHeader(String name) {
        HeaderGroup headerGroup = this.headergroup;
        for (int i = 0; i < headerGroup.headers.size(); i++) {
            Header header = (Header) headerGroup.headers.get(i);
            if (header.getName().equalsIgnoreCase(name)) {
                return header;
            }
        }
        return null;
    }

    public final Header[] getAllHeaders() {
        return this.headergroup.getAllHeaders();
    }

    public final void addHeader(Header header) {
        this.headergroup.addHeader(header);
    }

    public final void addHeader(String name, String value) {
        Args.notNull(name, "Header name");
        this.headergroup.addHeader(new BasicHeader(name, value));
    }

    public final void setHeader(String name, String value) {
        Args.notNull(name, "Header name");
        HeaderGroup headerGroup = this.headergroup;
        Header basicHeader = new BasicHeader(name, value);
        for (int i = 0; i < headerGroup.headers.size(); i++) {
            if (((Header) headerGroup.headers.get(i)).getName().equalsIgnoreCase(basicHeader.getName())) {
                headerGroup.headers.set(i, basicHeader);
                return;
            }
        }
        headerGroup.headers.add(basicHeader);
    }

    public final void setHeaders(Header[] headers) {
        this.headergroup.setHeaders(headers);
    }

    public final void removeHeader(Header header) {
        HeaderGroup headerGroup = this.headergroup;
        if (header != null) {
            headerGroup.headers.remove(header);
        }
    }

    public final void removeHeaders(String name) {
        HeaderIterator i = this.headergroup.iterator();
        while (i.hasNext()) {
            if (name.equalsIgnoreCase(i.nextHeader().getName())) {
                i.remove();
            }
        }
    }

    public final HeaderIterator headerIterator() {
        return this.headergroup.iterator();
    }

    public final HeaderIterator headerIterator(String name) {
        return new BasicListHeaderIterator(this.headergroup.headers, name);
    }

    @Deprecated
    public final HttpParams getParams() {
        if (this.params == null) {
            this.params = new BasicHttpParams();
        }
        return this.params;
    }

    @Deprecated
    public final void setParams(HttpParams params) {
        this.params = (HttpParams) Args.notNull(params, "HTTP parameters");
    }
}

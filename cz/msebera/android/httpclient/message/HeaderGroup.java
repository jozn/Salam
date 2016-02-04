package cz.msebera.android.httpclient.message;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderIterator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class HeaderGroup implements Serializable, Cloneable {
    final Header[] EMPTY;
    final List<Header> headers;

    public HeaderGroup() {
        this.EMPTY = new Header[0];
        this.headers = new ArrayList(16);
    }

    public final void clear() {
        this.headers.clear();
    }

    public final void addHeader(Header header) {
        if (header != null) {
            this.headers.add(header);
        }
    }

    public final void setHeaders(Header[] headers) {
        clear();
        if (headers != null) {
            Collections.addAll(this.headers, headers);
        }
    }

    public final Header[] getAllHeaders() {
        return (Header[]) this.headers.toArray(new Header[this.headers.size()]);
    }

    public final HeaderIterator iterator() {
        return new BasicListHeaderIterator(this.headers, null);
    }

    public final Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public final String toString() {
        return this.headers.toString();
    }
}

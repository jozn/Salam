package cz.msebera.android.httpclient.message;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderIterator;
import cz.msebera.android.httpclient.util.Args;
import java.util.NoSuchElementException;

public final class BasicHeaderIterator implements HeaderIterator {
    protected final Header[] allHeaders;
    protected int currentIndex;
    protected String headerName;

    public BasicHeaderIterator(Header[] headers) {
        this.allHeaders = (Header[]) Args.notNull(headers, "Header array");
        this.headerName = null;
        this.currentIndex = findNext(-1);
    }

    private int findNext(int pos) {
        int from = pos;
        if (pos < -1) {
            return -1;
        }
        int to = this.allHeaders.length - 1;
        boolean found = false;
        while (!found && from < to) {
            from++;
            found = this.headerName == null || this.headerName.equalsIgnoreCase(this.allHeaders[from].getName());
        }
        if (found) {
            return from;
        }
        return -1;
    }

    public final boolean hasNext() {
        return this.currentIndex >= 0;
    }

    public final Header nextHeader() throws NoSuchElementException {
        int current = this.currentIndex;
        if (current < 0) {
            throw new NoSuchElementException("Iteration already finished.");
        }
        this.currentIndex = findNext(current);
        return this.allHeaders[current];
    }

    public final Object next() throws NoSuchElementException {
        return nextHeader();
    }

    public final void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Removing headers is not supported.");
    }
}

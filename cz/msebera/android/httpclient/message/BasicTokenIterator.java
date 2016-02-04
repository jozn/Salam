package cz.msebera.android.httpclient.message;

import cz.msebera.android.httpclient.HeaderIterator;
import cz.msebera.android.httpclient.ParseException;
import cz.msebera.android.httpclient.TokenIterator;
import cz.msebera.android.httpclient.util.Args;
import java.util.NoSuchElementException;

public final class BasicTokenIterator implements TokenIterator {
    protected String currentHeader;
    protected String currentToken;
    protected final HeaderIterator headerIt;
    protected int searchPos;

    public BasicTokenIterator(HeaderIterator headerIterator) {
        this.headerIt = (HeaderIterator) Args.notNull(headerIterator, "Header iterator");
        this.searchPos = findNext(-1);
    }

    public final boolean hasNext() {
        return this.currentToken != null;
    }

    public final String nextToken() throws NoSuchElementException, ParseException {
        if (this.currentToken == null) {
            throw new NoSuchElementException("Iteration already finished.");
        }
        String result = this.currentToken;
        this.searchPos = findNext(this.searchPos);
        return result;
    }

    public final Object next() throws NoSuchElementException, ParseException {
        return nextToken();
    }

    public final void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Removing tokens is not supported.");
    }

    private int findNext(int pos) throws ParseException {
        int from = pos;
        if (pos >= 0) {
            from = Args.notNegative(from, "Search position");
            Object obj = null;
            int length = this.currentHeader.length();
            while (obj == null && from < length) {
                char charAt = this.currentHeader.charAt(from);
                if (isTokenSeparator(charAt)) {
                    obj = 1;
                } else if (isWhitespace(charAt)) {
                    from++;
                } else if (isTokenChar(charAt)) {
                    throw new ParseException("Tokens without separator (pos " + from + "): " + this.currentHeader);
                } else {
                    throw new ParseException("Invalid character after token (pos " + from + "): " + this.currentHeader);
                }
            }
        } else if (!this.headerIt.hasNext()) {
            return -1;
        } else {
            this.currentHeader = this.headerIt.nextHeader().getValue();
            from = 0;
        }
        int start = findTokenStart(from);
        if (start < 0) {
            this.currentToken = null;
            return -1;
        }
        int end = findTokenEnd(start);
        this.currentToken = this.currentHeader.substring(start, end);
        return end;
    }

    private int findTokenStart(int pos) {
        int from = Args.notNegative(pos, "Search position");
        boolean found = false;
        while (!found && this.currentHeader != null) {
            int to = this.currentHeader.length();
            while (!found && from < to) {
                char ch = this.currentHeader.charAt(from);
                if (isTokenSeparator(ch) || isWhitespace(ch)) {
                    from++;
                } else if (isTokenChar(this.currentHeader.charAt(from))) {
                    found = true;
                } else {
                    throw new ParseException("Invalid character before token (pos " + from + "): " + this.currentHeader);
                }
            }
            if (!found) {
                if (this.headerIt.hasNext()) {
                    this.currentHeader = this.headerIt.nextHeader().getValue();
                    from = 0;
                } else {
                    this.currentHeader = null;
                }
            }
        }
        return found ? from : -1;
    }

    private int findTokenEnd(int from) {
        Args.notNegative(from, "Search position");
        int to = this.currentHeader.length();
        int end = from + 1;
        while (end < to && isTokenChar(this.currentHeader.charAt(end))) {
            end++;
        }
        return end;
    }

    private static boolean isTokenSeparator(char ch) {
        return ch == ',';
    }

    private static boolean isWhitespace(char ch) {
        return ch == '\t' || Character.isSpaceChar(ch);
    }

    private static boolean isTokenChar(char ch) {
        if (Character.isLetterOrDigit(ch)) {
            return true;
        }
        if (Character.isISOControl(ch)) {
            return false;
        }
        boolean z;
        if (" ,;=()<>@:\\\"/[]?{}\t".indexOf(ch) >= 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            return false;
        }
        return true;
    }
}

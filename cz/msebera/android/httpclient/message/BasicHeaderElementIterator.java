package cz.msebera.android.httpclient.message;

import cz.msebera.android.httpclient.FormattedHeader;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.HeaderElementIterator;
import cz.msebera.android.httpclient.HeaderIterator;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.CharArrayBuffer;
import java.util.NoSuchElementException;

public final class BasicHeaderElementIterator implements HeaderElementIterator {
    private CharArrayBuffer buffer;
    private HeaderElement currentElement;
    private ParserCursor cursor;
    private final HeaderIterator headerIt;
    private final HeaderValueParser parser;

    private BasicHeaderElementIterator(HeaderIterator headerIterator, HeaderValueParser parser) {
        this.currentElement = null;
        this.buffer = null;
        this.cursor = null;
        this.headerIt = (HeaderIterator) Args.notNull(headerIterator, "Header iterator");
        this.parser = (HeaderValueParser) Args.notNull(parser, "Parser");
    }

    public BasicHeaderElementIterator(HeaderIterator headerIterator) {
        this(headerIterator, BasicHeaderValueParser.INSTANCE);
    }

    private void parseNextElement() {
        HeaderElement e;
        loop0:
        while (true) {
            if (this.headerIt.hasNext() || this.cursor != null) {
                if (this.cursor == null || this.cursor.atEnd()) {
                    this.cursor = null;
                    this.buffer = null;
                    while (this.headerIt.hasNext()) {
                        Header nextHeader = this.headerIt.nextHeader();
                        if (!(nextHeader instanceof FormattedHeader)) {
                            String value = nextHeader.getValue();
                            if (value != null) {
                                this.buffer = new CharArrayBuffer(value.length());
                                this.buffer.append(value);
                                this.cursor = new ParserCursor(0, this.buffer.length());
                                break;
                            }
                        }
                        this.buffer = ((FormattedHeader) nextHeader).getBuffer();
                        this.cursor = new ParserCursor(0, this.buffer.length());
                        this.cursor.updatePos(((FormattedHeader) nextHeader).getValuePos());
                        break;
                    }
                }
                if (this.cursor != null) {
                    while (!this.cursor.atEnd()) {
                        e = this.parser.parseHeaderElement(this.buffer, this.cursor);
                        if (e.getName().length() == 0) {
                            if (e.getValue() != null) {
                                break loop0;
                            }
                        }
                        break loop0;
                    }
                    if (this.cursor.atEnd()) {
                        this.cursor = null;
                        this.buffer = null;
                    }
                }
            } else {
                return;
            }
        }
        this.currentElement = e;
    }

    public final boolean hasNext() {
        if (this.currentElement == null) {
            parseNextElement();
        }
        return this.currentElement != null;
    }

    public final HeaderElement nextElement() throws NoSuchElementException {
        if (this.currentElement == null) {
            parseNextElement();
        }
        if (this.currentElement == null) {
            throw new NoSuchElementException("No more header elements available");
        }
        HeaderElement element = this.currentElement;
        this.currentElement = null;
        return element;
    }

    public final Object next() throws NoSuchElementException {
        return nextElement();
    }

    public final void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Remove not supported");
    }
}

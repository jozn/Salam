package cz.msebera.android.httpclient.message;

import android.support.v7.appcompat.BuildConfig;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpVersion;
import cz.msebera.android.httpclient.ParseException;
import cz.msebera.android.httpclient.ProtocolVersion;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.CharArrayBuffer;

public final class BasicLineParser implements LineParser {
    @Deprecated
    public static final BasicLineParser DEFAULT;
    public static final BasicLineParser INSTANCE;
    protected final ProtocolVersion protocol;

    static {
        DEFAULT = new BasicLineParser((byte) 0);
        INSTANCE = new BasicLineParser((byte) 0);
    }

    private BasicLineParser() {
        this.protocol = HttpVersion.HTTP_1_1;
    }

    private BasicLineParser(byte b) {
        this();
    }

    private ProtocolVersion parseProtocolVersion(CharArrayBuffer buffer, ParserCursor cursor) throws ParseException {
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(cursor, "Parser cursor");
        String protoname = this.protocol.getProtocol();
        int protolength = protoname.length();
        int indexFrom = cursor.pos;
        int indexTo = cursor.upperBound;
        skipWhitespace(buffer, cursor);
        int i = cursor.pos;
        if ((i + protolength) + 4 > indexTo) {
            throw new ParseException("Not a valid protocol version: " + buffer.substring(indexFrom, indexTo));
        }
        boolean ok = true;
        int j = 0;
        while (ok && j < protolength) {
            ok = buffer.charAt(i + j) == protoname.charAt(j);
            j++;
        }
        if (ok) {
            ok = buffer.charAt(i + protolength) == '/';
        }
        if (ok) {
            i += protolength + 1;
            int period = buffer.indexOf(46, i, indexTo);
            if (period == -1) {
                throw new ParseException("Invalid protocol version number: " + buffer.substring(indexFrom, indexTo));
            }
            try {
                int major = Integer.parseInt(buffer.substringTrimmed(i, period));
                i = period + 1;
                int blank = buffer.indexOf(32, i, indexTo);
                if (blank == -1) {
                    blank = indexTo;
                }
                try {
                    int minor = Integer.parseInt(buffer.substringTrimmed(i, blank));
                    cursor.updatePos(blank);
                    return this.protocol.forVersion(major, minor);
                } catch (NumberFormatException e) {
                    throw new ParseException("Invalid protocol minor version number: " + buffer.substring(indexFrom, indexTo));
                }
            } catch (NumberFormatException e2) {
                throw new ParseException("Invalid protocol major version number: " + buffer.substring(indexFrom, indexTo));
            }
        }
        throw new ParseException("Not a valid protocol version: " + buffer.substring(indexFrom, indexTo));
    }

    public final boolean hasProtocolVersion(CharArrayBuffer buffer, ParserCursor cursor) {
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(cursor, "Parser cursor");
        int index = cursor.pos;
        String protoname = this.protocol.getProtocol();
        int protolength = protoname.length();
        if (buffer.length() < protolength + 4) {
            return false;
        }
        if (index < 0) {
            index = (buffer.length() - 4) - protolength;
        } else if (index == 0) {
            while (index < buffer.length() && HTTP.isWhitespace(buffer.charAt(index))) {
                index++;
            }
        }
        if ((index + protolength) + 4 > buffer.length()) {
            return false;
        }
        boolean ok = true;
        int j = 0;
        while (ok && j < protolength) {
            if (buffer.charAt(index + j) == protoname.charAt(j)) {
                ok = true;
            } else {
                ok = false;
            }
            j++;
        }
        if (ok) {
            if (buffer.charAt(index + protolength) == '/') {
                ok = true;
            } else {
                ok = false;
            }
        }
        return ok;
    }

    public final StatusLine parseStatusLine(CharArrayBuffer buffer, ParserCursor cursor) throws ParseException {
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(cursor, "Parser cursor");
        int indexFrom = cursor.pos;
        int indexTo = cursor.upperBound;
        try {
            String reasonPhrase;
            ProtocolVersion ver = parseProtocolVersion(buffer, cursor);
            skipWhitespace(buffer, cursor);
            int i = cursor.pos;
            int blank = buffer.indexOf(32, i, indexTo);
            if (blank < 0) {
                blank = indexTo;
            }
            String s = buffer.substringTrimmed(i, blank);
            int j = 0;
            while (j < s.length()) {
                if (Character.isDigit(s.charAt(j))) {
                    j++;
                } else {
                    throw new ParseException("Status line contains invalid status code: " + buffer.substring(indexFrom, indexTo));
                }
            }
            int statusCode = Integer.parseInt(s);
            i = blank;
            if (blank < indexTo) {
                reasonPhrase = buffer.substringTrimmed(i, indexTo);
            } else {
                reasonPhrase = BuildConfig.VERSION_NAME;
            }
            return new BasicStatusLine(ver, statusCode, reasonPhrase);
        } catch (NumberFormatException e) {
            throw new ParseException("Status line contains invalid status code: " + buffer.substring(indexFrom, indexTo));
        } catch (IndexOutOfBoundsException e2) {
            throw new ParseException("Invalid status line: " + buffer.substring(indexFrom, indexTo));
        }
    }

    public final Header parseHeader(CharArrayBuffer buffer) throws ParseException {
        return new BufferedHeader(buffer);
    }

    private static void skipWhitespace(CharArrayBuffer buffer, ParserCursor cursor) {
        int pos = cursor.pos;
        int indexTo = cursor.upperBound;
        while (pos < indexTo && HTTP.isWhitespace(buffer.charAt(pos))) {
            pos++;
        }
        cursor.updatePos(pos);
    }
}

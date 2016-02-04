package cz.msebera.android.httpclient.message;

import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.ParseException;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.CharArrayBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public final class BasicHeaderValueParser implements HeaderValueParser {
    @Deprecated
    public static final BasicHeaderValueParser DEFAULT;
    public static final BasicHeaderValueParser INSTANCE;
    private static final BitSet TOKEN_DELIMS;
    private static final BitSet VALUE_DELIMS;
    private final TokenParser tokenParser;

    static {
        DEFAULT = new BasicHeaderValueParser();
        INSTANCE = new BasicHeaderValueParser();
        TOKEN_DELIMS = TokenParser.INIT_BITSET(61, 59, 44);
        VALUE_DELIMS = TokenParser.INIT_BITSET(59, 44);
    }

    public BasicHeaderValueParser() {
        this.tokenParser = TokenParser.INSTANCE;
    }

    public static HeaderElement[] parseElements$2d622134(String value) throws ParseException {
        Args.notNull(value, "Value");
        CharArrayBuffer buffer = new CharArrayBuffer(value.length());
        buffer.append(value);
        return INSTANCE.parseElements(buffer, new ParserCursor(0, value.length()));
    }

    public final HeaderElement[] parseElements(CharArrayBuffer buffer, ParserCursor cursor) {
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(cursor, "Parser cursor");
        List<HeaderElement> elements = new ArrayList();
        while (!cursor.atEnd()) {
            HeaderElement element = parseHeaderElement(buffer, cursor);
            if (element.getName().length() != 0 || element.getValue() != null) {
                elements.add(element);
            }
        }
        return (HeaderElement[]) elements.toArray(new HeaderElement[elements.size()]);
    }

    public final HeaderElement parseHeaderElement(CharArrayBuffer buffer, ParserCursor cursor) {
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(cursor, "Parser cursor");
        NameValuePair nvp = parseNameValuePair(buffer, cursor);
        NameValuePair[] params = null;
        if (!(cursor.atEnd() || buffer.charAt(cursor.pos - 1) == ',')) {
            Args.notNull(buffer, "Char array buffer");
            Args.notNull(cursor, "Parser cursor");
            TokenParser.skipWhiteSpace(buffer, cursor);
            List arrayList = new ArrayList();
            while (!cursor.atEnd()) {
                arrayList.add(parseNameValuePair(buffer, cursor));
                if (buffer.charAt(cursor.pos - 1) == ',') {
                    break;
                }
            }
            params = (NameValuePair[]) arrayList.toArray(new NameValuePair[arrayList.size()]);
        }
        return new BasicHeaderElement(nvp.getName(), nvp.getValue(), params);
    }

    private static NameValuePair parseNameValuePair(CharArrayBuffer buffer, ParserCursor cursor) {
        Args.notNull(buffer, "Char array buffer");
        Args.notNull(cursor, "Parser cursor");
        String name = TokenParser.parseToken(buffer, cursor, TOKEN_DELIMS);
        if (cursor.atEnd()) {
            return new BasicNameValuePair(name, null);
        }
        int delim = buffer.charAt(cursor.pos);
        cursor.updatePos(cursor.pos + 1);
        if (delim != 61) {
            return createNameValuePair(name, null);
        }
        String value = TokenParser.parseValue(buffer, cursor, VALUE_DELIMS);
        if (!cursor.atEnd()) {
            cursor.updatePos(cursor.pos + 1);
        }
        return createNameValuePair(name, value);
    }

    private static NameValuePair createNameValuePair(String name, String value) {
        return new BasicNameValuePair(name, value);
    }
}

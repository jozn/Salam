package cz.msebera.android.httpclient.message;

import cz.msebera.android.httpclient.util.CharArrayBuffer;
import java.util.BitSet;

public final class TokenParser {
    public static final TokenParser INSTANCE;

    public static BitSet INIT_BITSET(int... b) {
        BitSet bitset = new BitSet();
        for (int aB : b) {
            bitset.set(aB);
        }
        return bitset;
    }

    private static boolean isWhitespace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
    }

    static {
        INSTANCE = new TokenParser();
    }

    public static String parseToken(CharArrayBuffer buf, ParserCursor cursor, BitSet delimiters) {
        StringBuilder dst = new StringBuilder();
        boolean whitespace = false;
        while (!cursor.atEnd()) {
            char current = buf.charAt(cursor.pos);
            if (delimiters != null && delimiters.get(current)) {
                break;
            } else if (isWhitespace(current)) {
                skipWhiteSpace(buf, cursor);
                whitespace = true;
            } else {
                if (whitespace && dst.length() > 0) {
                    dst.append(' ');
                }
                copyContent(buf, cursor, delimiters, dst);
                whitespace = false;
            }
        }
        return dst.toString();
    }

    public static String parseValue(CharArrayBuffer buf, ParserCursor cursor, BitSet delimiters) {
        StringBuilder dst = new StringBuilder();
        boolean whitespace = false;
        while (!cursor.atEnd()) {
            char current = buf.charAt(cursor.pos);
            if (delimiters != null && delimiters.get(current)) {
                break;
            } else if (isWhitespace(current)) {
                skipWhiteSpace(buf, cursor);
                whitespace = true;
            } else if (current == '\"') {
                if (whitespace && dst.length() > 0) {
                    dst.append(' ');
                }
                copyQuotedContent(buf, cursor, dst);
                whitespace = false;
            } else {
                if (whitespace && dst.length() > 0) {
                    dst.append(' ');
                }
                copyUnquotedContent(buf, cursor, delimiters, dst);
                whitespace = false;
            }
        }
        return dst.toString();
    }

    public static void skipWhiteSpace(CharArrayBuffer buf, ParserCursor cursor) {
        int pos = cursor.pos;
        int indexFrom = cursor.pos;
        int indexTo = cursor.upperBound;
        int i = indexFrom;
        while (i < indexTo && isWhitespace(buf.charAt(i))) {
            pos++;
            i++;
        }
        cursor.updatePos(pos);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void copyContent(cz.msebera.android.httpclient.util.CharArrayBuffer r6, cz.msebera.android.httpclient.message.ParserCursor r7, java.util.BitSet r8, java.lang.StringBuilder r9) {
        /*
        r4 = r7.pos;
        r2 = r7.pos;
        r3 = r7.upperBound;
        r1 = r2;
    L_0x0007:
        if (r1 >= r3) goto L_0x0023;
    L_0x0009:
        r0 = r6.charAt(r1);
        if (r8 == 0) goto L_0x0015;
    L_0x000f:
        r5 = r8.get(r0);
        if (r5 != 0) goto L_0x0023;
    L_0x0015:
        r5 = isWhitespace(r0);
        if (r5 != 0) goto L_0x0023;
    L_0x001b:
        r4 = r4 + 1;
        r9.append(r0);
        r1 = r1 + 1;
        goto L_0x0007;
    L_0x0023:
        r7.updatePos(r4);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: cz.msebera.android.httpclient.message.TokenParser.copyContent(cz.msebera.android.httpclient.util.CharArrayBuffer, cz.msebera.android.httpclient.message.ParserCursor, java.util.BitSet, java.lang.StringBuilder):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void copyUnquotedContent(cz.msebera.android.httpclient.util.CharArrayBuffer r6, cz.msebera.android.httpclient.message.ParserCursor r7, java.util.BitSet r8, java.lang.StringBuilder r9) {
        /*
        r4 = r7.pos;
        r2 = r7.pos;
        r3 = r7.upperBound;
        r1 = r2;
    L_0x0007:
        if (r1 >= r3) goto L_0x0027;
    L_0x0009:
        r0 = r6.charAt(r1);
        if (r8 == 0) goto L_0x0015;
    L_0x000f:
        r5 = r8.get(r0);
        if (r5 != 0) goto L_0x0027;
    L_0x0015:
        r5 = isWhitespace(r0);
        if (r5 != 0) goto L_0x0027;
    L_0x001b:
        r5 = 34;
        if (r0 == r5) goto L_0x0027;
    L_0x001f:
        r4 = r4 + 1;
        r9.append(r0);
        r1 = r1 + 1;
        goto L_0x0007;
    L_0x0027:
        r7.updatePos(r4);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: cz.msebera.android.httpclient.message.TokenParser.copyUnquotedContent(cz.msebera.android.httpclient.util.CharArrayBuffer, cz.msebera.android.httpclient.message.ParserCursor, java.util.BitSet, java.lang.StringBuilder):void");
    }

    private static void copyQuotedContent(CharArrayBuffer buf, ParserCursor cursor, StringBuilder dst) {
        if (!cursor.atEnd()) {
            int pos = cursor.pos;
            int indexFrom = cursor.pos;
            int indexTo = cursor.upperBound;
            if (buf.charAt(pos) == '\"') {
                pos++;
                boolean escaped = false;
                int i = indexFrom + 1;
                while (i < indexTo) {
                    char current = buf.charAt(i);
                    if (escaped) {
                        if (!(current == '\"' || current == '\\')) {
                            dst.append('\\');
                        }
                        dst.append(current);
                        escaped = false;
                    } else if (current == '\"') {
                        pos++;
                        break;
                    } else if (current == '\\') {
                        escaped = true;
                    } else if (!(current == '\r' || current == '\n')) {
                        dst.append(current);
                    }
                    i++;
                    pos++;
                }
                cursor.updatePos(pos);
            }
        }
    }
}

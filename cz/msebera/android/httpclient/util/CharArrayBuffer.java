package cz.msebera.android.httpclient.util;

import android.support.v4.view.MotionEventCompat;
import cz.msebera.android.httpclient.protocol.HTTP;
import java.io.Serializable;
import java.nio.CharBuffer;

public final class CharArrayBuffer implements Serializable, CharSequence {
    public char[] buffer;
    public int len;

    public CharArrayBuffer(int capacity) {
        Args.notNegative(capacity, "Buffer capacity");
        this.buffer = new char[capacity];
    }

    private void expand(int newlen) {
        char[] newbuffer = new char[Math.max(this.buffer.length << 1, newlen)];
        System.arraycopy(this.buffer, 0, newbuffer, 0, this.len);
        this.buffer = newbuffer;
    }

    public final void append(char[] b, int off, int len) {
        if (b != null) {
            if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length) {
                throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
            } else if (len != 0) {
                int newlen = this.len + len;
                if (newlen > this.buffer.length) {
                    expand(newlen);
                }
                System.arraycopy(b, off, this.buffer, this.len, len);
                this.len = newlen;
            }
        }
    }

    public final void append(String str) {
        String s = str != null ? str : "null";
        int strlen = s.length();
        int newlen = this.len + strlen;
        if (newlen > this.buffer.length) {
            expand(newlen);
        }
        s.getChars(0, strlen, this.buffer, this.len);
        this.len = newlen;
    }

    public final void append(char ch) {
        int newlen = this.len + 1;
        if (newlen > this.buffer.length) {
            expand(newlen);
        }
        this.buffer[this.len] = ch;
        this.len = newlen;
    }

    public final void append(byte[] b, int off, int len) {
        if (b != null) {
            if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length) {
                throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
            } else if (len != 0) {
                int oldlen = this.len;
                int newlen = oldlen + len;
                if (newlen > this.buffer.length) {
                    expand(newlen);
                }
                int i1 = off;
                for (int i2 = oldlen; i2 < newlen; i2++) {
                    this.buffer[i2] = (char) (b[i1] & MotionEventCompat.ACTION_MASK);
                    i1++;
                }
                this.len = newlen;
            }
        }
    }

    public final char charAt(int i) {
        return this.buffer[i];
    }

    public final int length() {
        return this.len;
    }

    public final void ensureCapacity(int required) {
        if (required > 0 && required > this.buffer.length - this.len) {
            expand(this.len + required);
        }
    }

    public final int indexOf(int ch, int from, int to) {
        int beginIndex = from;
        if (from < 0) {
            beginIndex = 0;
        }
        int endIndex = to;
        if (to > this.len) {
            endIndex = this.len;
        }
        if (beginIndex > endIndex) {
            return -1;
        }
        for (int i = beginIndex; i < endIndex; i++) {
            if (this.buffer[i] == ch) {
                return i;
            }
        }
        return -1;
    }

    public final int indexOf(int ch) {
        return indexOf(ch, 0, this.len);
    }

    public final String substring(int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("Negative beginIndex: " + beginIndex);
        } else if (endIndex > this.len) {
            throw new IndexOutOfBoundsException("endIndex: " + endIndex + " > length: " + this.len);
        } else if (beginIndex <= endIndex) {
            return new String(this.buffer, beginIndex, endIndex - beginIndex);
        } else {
            throw new IndexOutOfBoundsException("beginIndex: " + beginIndex + " > endIndex: " + endIndex);
        }
    }

    public final String substringTrimmed(int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("Negative beginIndex: " + beginIndex);
        } else if (endIndex > this.len) {
            throw new IndexOutOfBoundsException("endIndex: " + endIndex + " > length: " + this.len);
        } else if (beginIndex > endIndex) {
            throw new IndexOutOfBoundsException("beginIndex: " + beginIndex + " > endIndex: " + endIndex);
        } else {
            int beginIndex0 = beginIndex;
            int endIndex0 = endIndex;
            while (beginIndex0 < endIndex && HTTP.isWhitespace(this.buffer[beginIndex0])) {
                beginIndex0++;
            }
            while (endIndex0 > beginIndex0 && HTTP.isWhitespace(this.buffer[endIndex0 - 1])) {
                endIndex0--;
            }
            return new String(this.buffer, beginIndex0, endIndex0 - beginIndex0);
        }
    }

    public final CharSequence subSequence(int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("Negative beginIndex: " + beginIndex);
        } else if (endIndex > this.len) {
            throw new IndexOutOfBoundsException("endIndex: " + endIndex + " > length: " + this.len);
        } else if (beginIndex <= endIndex) {
            return CharBuffer.wrap(this.buffer, beginIndex, endIndex);
        } else {
            throw new IndexOutOfBoundsException("beginIndex: " + beginIndex + " > endIndex: " + endIndex);
        }
    }

    public final String toString() {
        return new String(this.buffer, 0, this.len);
    }
}

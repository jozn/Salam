package okio;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public final class ByteString implements Serializable {
    public static final ByteString EMPTY;
    static final char[] HEX_DIGITS;
    public final byte[] data;
    transient int hashCode;
    transient String utf8;

    static {
        HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        EMPTY = of(new byte[0]);
    }

    ByteString(byte[] data) {
        this.data = data;
    }

    public static ByteString of(byte... data) {
        if (data != null) {
            return new ByteString((byte[]) data.clone());
        }
        throw new IllegalArgumentException("data == null");
    }

    public static ByteString encodeUtf8(String s) {
        if (s == null) {
            throw new IllegalArgumentException("s == null");
        }
        ByteString byteString = new ByteString(s.getBytes(Util.UTF_8));
        byteString.utf8 = s;
        return byteString;
    }

    public final String utf8() {
        String result = this.utf8;
        if (result != null) {
            return result;
        }
        result = new String(this.data, Util.UTF_8);
        this.utf8 = result;
        return result;
    }

    private ByteString digest(String digest) {
        try {
            return of(MessageDigest.getInstance(digest).digest(this.data));
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }

    public static ByteString decodeBase64(String base64) {
        if (base64 == null) {
            throw new IllegalArgumentException("base64 == null");
        }
        byte[] decoded = Base64.decode(base64);
        return decoded != null ? new ByteString(decoded) : null;
    }

    public final String hex() {
        char[] result = new char[(this.data.length * 2)];
        int c = 0;
        for (byte b : this.data) {
            int i = c + 1;
            result[c] = HEX_DIGITS[(b >> 4) & 15];
            c = i + 1;
            result[i] = HEX_DIGITS[b & 15];
        }
        return new String(result);
    }

    public final ByteString toAsciiLowercase() {
        int i = 0;
        while (i < this.data.length) {
            byte c = this.data[i];
            if (c < (byte) 65 || c > (byte) 90) {
                i++;
            } else {
                byte[] lowercase = (byte[]) this.data.clone();
                int i2 = i + 1;
                lowercase[i] = (byte) (c + 32);
                for (i = i2; i < lowercase.length; i++) {
                    c = lowercase[i];
                    if (c >= (byte) 65 && c <= (byte) 90) {
                        lowercase[i] = (byte) (c + 32);
                    }
                }
                return new ByteString(lowercase);
            }
        }
        return this;
    }

    public final byte[] toByteArray() {
        return (byte[]) this.data.clone();
    }

    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if ((o instanceof ByteString) && ((ByteString) o).data.length == this.data.length) {
            ByteString byteString = (ByteString) o;
            byte[] bArr = this.data;
            int length = this.data.length;
            Object obj = (byteString.data.length - length < 0 || bArr.length - length < 0 || !Util.arrayRangeEquals$5c8eef72(byteString.data, bArr, length)) ? null : 1;
            if (obj != null) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        result = Arrays.hashCode(this.data);
        this.hashCode = result;
        return result;
    }

    public final String toString() {
        if (this.data.length == 0) {
            return "ByteString[size=0]";
        }
        if (this.data.length <= 16) {
            return String.format("ByteString[size=%s data=%s]", new Object[]{Integer.valueOf(this.data.length), hex()});
        }
        return String.format("ByteString[size=%s md5=%s]", new Object[]{Integer.valueOf(this.data.length), digest("MD5").hex()});
    }
}

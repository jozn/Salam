package cz.msebera.android.httpclient.entity;

import cz.msebera.android.httpclient.Consts;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.ParseException;
import cz.msebera.android.httpclient.message.BasicHeaderValueFormatter;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.CharArrayBuffer;
import cz.msebera.android.httpclient.util.TextUtils;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

public final class ContentType implements Serializable {
    public static final ContentType APPLICATION_ATOM_XML;
    public static final ContentType APPLICATION_FORM_URLENCODED;
    public static final ContentType APPLICATION_JSON;
    public static final ContentType APPLICATION_OCTET_STREAM;
    public static final ContentType APPLICATION_SVG_XML;
    public static final ContentType APPLICATION_XHTML_XML;
    public static final ContentType APPLICATION_XML;
    public static final ContentType DEFAULT_BINARY;
    public static final ContentType DEFAULT_TEXT;
    public static final ContentType MULTIPART_FORM_DATA;
    public static final ContentType TEXT_HTML;
    public static final ContentType TEXT_PLAIN;
    public static final ContentType TEXT_XML;
    public static final ContentType WILDCARD;
    public final Charset charset;
    public final String mimeType;
    private final NameValuePair[] params;

    static {
        APPLICATION_ATOM_XML = create("application/atom+xml", Consts.ISO_8859_1);
        APPLICATION_FORM_URLENCODED = create("application/x-www-form-urlencoded", Consts.ISO_8859_1);
        APPLICATION_JSON = create("application/json", Consts.UTF_8);
        APPLICATION_OCTET_STREAM = create("application/octet-stream", null);
        APPLICATION_SVG_XML = create("application/svg+xml", Consts.ISO_8859_1);
        APPLICATION_XHTML_XML = create("application/xhtml+xml", Consts.ISO_8859_1);
        APPLICATION_XML = create("application/xml", Consts.ISO_8859_1);
        MULTIPART_FORM_DATA = create("multipart/form-data", Consts.ISO_8859_1);
        TEXT_HTML = create("text/html", Consts.ISO_8859_1);
        TEXT_PLAIN = create("text/plain", Consts.ISO_8859_1);
        TEXT_XML = create("text/xml", Consts.ISO_8859_1);
        WILDCARD = create("*/*", null);
        DEFAULT_TEXT = TEXT_PLAIN;
        DEFAULT_BINARY = APPLICATION_OCTET_STREAM;
    }

    private ContentType(String mimeType, Charset charset) {
        this.mimeType = mimeType;
        this.charset = charset;
        this.params = null;
    }

    private ContentType(String mimeType, Charset charset, NameValuePair[] params) {
        this.mimeType = mimeType;
        this.charset = charset;
        this.params = params;
    }

    public final String toString() {
        CharArrayBuffer buf = new CharArrayBuffer(64);
        buf.append(this.mimeType);
        if (this.params != null) {
            int i;
            buf.append("; ");
            BasicHeaderValueFormatter basicHeaderValueFormatter = BasicHeaderValueFormatter.INSTANCE;
            Object obj = this.params;
            Args.notNull(obj, "Header parameter array");
            if (obj == null || obj.length <= 0) {
                i = 0;
            } else {
                i = (obj.length - 1) * 2;
                int i2 = 0;
                while (i2 < obj.length) {
                    int estimateNameValuePairLen = BasicHeaderValueFormatter.estimateNameValuePairLen(obj[i2]) + i;
                    i2++;
                    i = estimateNameValuePairLen;
                }
            }
            buf.ensureCapacity(i);
            for (i = 0; i < obj.length; i++) {
                if (i > 0) {
                    buf.append("; ");
                }
                BasicHeaderValueFormatter.formatNameValuePair(buf, obj[i], false);
            }
        } else if (this.charset != null) {
            buf.append("; charset=");
            buf.append(this.charset.name());
        }
        return buf.toString();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static cz.msebera.android.httpclient.entity.ContentType create(java.lang.String r5, java.nio.charset.Charset r6) {
        /*
        r2 = 0;
        r1 = "MIME type";
        r1 = cz.msebera.android.httpclient.util.Args.notBlank(r5, r1);
        r1 = (java.lang.String) r1;
        r3 = java.util.Locale.ROOT;
        r0 = r1.toLowerCase(r3);
        r1 = r2;
    L_0x0010:
        r3 = r0.length();
        if (r1 >= r3) goto L_0x0034;
    L_0x0016:
        r3 = r0.charAt(r1);
        r4 = 34;
        if (r3 == r4) goto L_0x0026;
    L_0x001e:
        r4 = 44;
        if (r3 == r4) goto L_0x0026;
    L_0x0022:
        r4 = 59;
        if (r3 != r4) goto L_0x0031;
    L_0x0026:
        r1 = "MIME type may not contain reserved characters";
        cz.msebera.android.httpclient.util.Args.check(r2, r1);
        r1 = new cz.msebera.android.httpclient.entity.ContentType;
        r1.<init>(r0, r6);
        return r1;
    L_0x0031:
        r1 = r1 + 1;
        goto L_0x0010;
    L_0x0034:
        r2 = 1;
        goto L_0x0026;
        */
        throw new UnsupportedOperationException("Method not decompiled: cz.msebera.android.httpclient.entity.ContentType.create(java.lang.String, java.nio.charset.Charset):cz.msebera.android.httpclient.entity.ContentType");
    }

    public static ContentType create(String mimeType, String charset) throws UnsupportedCharsetException {
        return create(mimeType, !TextUtils.isBlank(charset) ? Charset.forName(charset) : null);
    }

    private static ContentType create$471b0095(String mimeType, NameValuePair[] params) {
        Charset charset = null;
        for (NameValuePair param : params) {
            if (param.getName().equalsIgnoreCase("charset")) {
                String s = param.getValue();
                if (!TextUtils.isBlank(s)) {
                    try {
                        charset = Charset.forName(s);
                    } catch (UnsupportedCharsetException e) {
                        throw e;
                    }
                }
                if (params == null || params.length <= 0) {
                    params = null;
                }
                return new ContentType(mimeType, charset, params);
            }
        }
        params = null;
        return new ContentType(mimeType, charset, params);
    }

    public static ContentType get(HttpEntity entity) throws ParseException, UnsupportedCharsetException {
        if (entity == null) {
            return null;
        }
        Header header = entity.getContentType();
        if (header == null) {
            return null;
        }
        HeaderElement[] elements = header.getElements();
        if (elements.length <= 0) {
            return null;
        }
        HeaderElement headerElement = elements[0];
        return create$471b0095(headerElement.getName(), headerElement.getParameters());
    }
}

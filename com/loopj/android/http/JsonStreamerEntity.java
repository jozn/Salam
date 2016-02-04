package com.loopj.android.http;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.appcompat.C0170R;
import android.text.TextUtils;
import com.kyleduo.switchbutton.C0473R;
import com.loopj.android.http.RequestParams.FileWrapper;
import com.loopj.android.http.RequestParams.StreamWrapper;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;

public final class JsonStreamerEntity implements HttpEntity {
    private static final UnsupportedOperationException ERR_UNSUPPORTED;
    private static final Header HEADER_GZIP_ENCODING;
    private static final Header HEADER_JSON_CONTENT;
    private static final byte[] JSON_FALSE;
    private static final byte[] JSON_NULL;
    private static final byte[] JSON_TRUE;
    private static final byte[] STREAM_CONTENTS;
    private static final byte[] STREAM_NAME;
    private static final byte[] STREAM_TYPE;
    private final byte[] buffer;
    private final Header contentEncoding;
    private final byte[] elapsedField;
    private final Map<String, Object> jsonParams;
    private final ResponseHandlerInterface progressHandler;

    static {
        ERR_UNSUPPORTED = new UnsupportedOperationException("Unsupported operation in this implementation.");
        JSON_TRUE = "true".getBytes();
        JSON_FALSE = "false".getBytes();
        JSON_NULL = "null".getBytes();
        STREAM_NAME = escape("name");
        STREAM_TYPE = escape("type");
        STREAM_CONTENTS = escape("contents");
        HEADER_JSON_CONTENT = new BasicHeader("Content-Type", "application/json");
        HEADER_GZIP_ENCODING = new BasicHeader("Content-Encoding", "gzip");
    }

    public JsonStreamerEntity(ResponseHandlerInterface progressHandler, boolean useGZipCompression, String elapsedField) {
        Header header;
        byte[] bArr = null;
        this.buffer = new byte[AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD];
        this.jsonParams = new HashMap();
        this.progressHandler = progressHandler;
        if (useGZipCompression) {
            header = HEADER_GZIP_ENCODING;
        } else {
            header = null;
        }
        this.contentEncoding = header;
        if (!TextUtils.isEmpty(elapsedField)) {
            bArr = escape(elapsedField);
        }
        this.elapsedField = bArr;
    }

    private static byte[] escape(String string) {
        if (string == null) {
            return JSON_NULL;
        }
        StringBuilder sb = new StringBuilder(AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
        sb.append('\"');
        int length = string.length();
        int pos = -1;
        while (true) {
            pos++;
            if (pos < length) {
                char ch = string.charAt(pos);
                switch (ch) {
                    case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                        sb.append("\\b");
                        break;
                    case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                        sb.append("\\t");
                        break;
                    case C0473R.styleable.SwitchButton_onColor /*10*/:
                        sb.append("\\n");
                        break;
                    case C0473R.styleable.SwitchButton_thumbColor /*12*/:
                        sb.append("\\f");
                        break;
                    case C0473R.styleable.SwitchButton_thumbPressedColor /*13*/:
                        sb.append("\\r");
                        break;
                    case C0170R.styleable.Theme_actionModePasteDrawable /*34*/:
                        sb.append("\\\"");
                        break;
                    case C0170R.styleable.Theme_alertDialogButtonGroupStyle /*92*/:
                        sb.append("\\\\");
                        break;
                    default:
                        if (ch > '\u001f' && ((ch < '\u007f' || ch > '\u009f') && (ch < '\u2000' || ch > '\u20ff'))) {
                            sb.append(ch);
                            break;
                        }
                        String intString = Integer.toHexString(ch);
                        sb.append("\\u");
                        int intLength = 4 - intString.length();
                        for (int zero = 0; zero < intLength; zero++) {
                            sb.append('0');
                        }
                        sb.append(intString.toUpperCase(Locale.US));
                        break;
                        break;
                }
            }
            sb.append('\"');
            return sb.toString().getBytes();
        }
    }

    public final void addPart(String key, Object value) {
        this.jsonParams.put(key, value);
    }

    public final boolean isRepeatable() {
        return false;
    }

    public final boolean isChunked() {
        return false;
    }

    public final boolean isStreaming() {
        return false;
    }

    public final long getContentLength() {
        return -1;
    }

    public final Header getContentEncoding() {
        return this.contentEncoding;
    }

    public final Header getContentType() {
        return HEADER_JSON_CONTENT;
    }

    public final void consumeContent() throws IOException, UnsupportedOperationException {
    }

    public final InputStream getContent() throws IOException, UnsupportedOperationException {
        throw ERR_UNSUPPORTED;
    }

    public final void writeTo(OutputStream out) throws IOException {
        if (out == null) {
            throw new IllegalStateException("Output stream cannot be null.");
        }
        OutputStream os;
        long now = System.currentTimeMillis();
        if (this.contentEncoding != null) {
            os = new GZIPOutputStream(out, AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD);
        } else {
            os = out;
        }
        os.write(123);
        Set<String> keys = this.jsonParams.keySet();
        int keysCount = keys.size();
        if (keysCount > 0) {
            int keysProcessed = 0;
            for (String key : keys) {
                keysProcessed++;
                try {
                    Object value = this.jsonParams.get(key);
                    os.write(escape(key));
                    os.write(58);
                    if (value == null) {
                        os.write(JSON_NULL);
                    } else {
                        boolean isFileWrapper = value instanceof FileWrapper;
                        if (isFileWrapper || (value instanceof StreamWrapper)) {
                            os.write(123);
                            if (isFileWrapper) {
                                writeToFromFile(os, (FileWrapper) value);
                            } else {
                                writeToFromStream(os, (StreamWrapper) value);
                            }
                            os.write(125);
                        } else if (value instanceof JsonValueInterface) {
                            os.write(((JsonValueInterface) value).getEscapedJsonValue());
                        } else if (value instanceof JSONObject) {
                            os.write(value.toString().getBytes());
                        } else if (value instanceof JSONArray) {
                            os.write(value.toString().getBytes());
                        } else if (value instanceof Boolean) {
                            os.write(((Boolean) value).booleanValue() ? JSON_TRUE : JSON_FALSE);
                        } else if (value instanceof Long) {
                            os.write((((Number) value).longValue()).getBytes());
                        } else if (value instanceof Double) {
                            os.write((((Number) value).doubleValue()).getBytes());
                        } else if (value instanceof Float) {
                            os.write((((Number) value).floatValue()).getBytes());
                        } else if (value instanceof Integer) {
                            os.write((((Number) value).intValue()).getBytes());
                        } else {
                            os.write(escape(value.toString()));
                        }
                    }
                    if (this.elapsedField != null || keysProcessed < keysCount) {
                        os.write(44);
                    }
                } catch (Throwable th) {
                    if (this.elapsedField != null || keysProcessed < keysCount) {
                        os.write(44);
                    }
                }
            }
            long elapsedTime = System.currentTimeMillis() - now;
            if (this.elapsedField != null) {
                os.write(this.elapsedField);
                os.write(58);
                os.write(String.valueOf(elapsedTime).getBytes());
            }
            AsyncHttpClient.log.m18i("JsonStreamerEntity", "Uploaded JSON in " + Math.floor((double) (elapsedTime / 1000)) + " seconds");
        }
        os.write(125);
        os.flush();
        AsyncHttpClient.silentCloseOutputStream(os);
    }

    private void writeToFromStream(OutputStream os, StreamWrapper entry) throws IOException {
        writeMetaData(os, entry.name, entry.contentType);
        Base64OutputStream bos = new Base64OutputStream(os);
        while (true) {
            int bytesRead = entry.inputStream.read(this.buffer);
            if (bytesRead == -1) {
                break;
            }
            bos.write(this.buffer, 0, bytesRead);
        }
        AsyncHttpClient.silentCloseOutputStream(bos);
        os.write(34);
        if (entry.autoClose) {
            AsyncHttpClient.silentCloseInputStream(entry.inputStream);
        }
    }

    private void writeToFromFile(OutputStream os, FileWrapper wrapper) throws IOException {
        writeMetaData(os, wrapper.file.getName(), wrapper.contentType);
        long bytesWritten = 0;
        long totalSize = wrapper.file.length();
        FileInputStream in = new FileInputStream(wrapper.file);
        Base64OutputStream bos = new Base64OutputStream(os);
        while (true) {
            int bytesRead = in.read(this.buffer);
            if (bytesRead != -1) {
                bos.write(this.buffer, 0, bytesRead);
                bytesWritten += (long) bytesRead;
                this.progressHandler.sendProgressMessage(bytesWritten, totalSize);
            } else {
                AsyncHttpClient.silentCloseOutputStream(bos);
                os.write(34);
                AsyncHttpClient.silentCloseInputStream(in);
                return;
            }
        }
    }

    private static void writeMetaData(OutputStream os, String name, String contentType) throws IOException {
        os.write(STREAM_NAME);
        os.write(58);
        os.write(escape(name));
        os.write(44);
        os.write(STREAM_TYPE);
        os.write(58);
        os.write(escape(contentType));
        os.write(44);
        os.write(STREAM_CONTENTS);
        os.write(58);
        os.write(34);
    }
}

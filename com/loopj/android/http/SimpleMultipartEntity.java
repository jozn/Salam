package com.loopj.android.http;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.text.TextUtils;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

final class SimpleMultipartEntity implements HttpEntity {
    static final byte[] CR_LF;
    private static final char[] MULTIPART_CHARS;
    static final byte[] TRANSFER_ENCODING_BINARY;
    private final String boundary;
    private final byte[] boundaryEnd;
    final byte[] boundaryLine;
    private long bytesWritten;
    private final List<FilePart> fileParts;
    boolean isRepeatable;
    final ByteArrayOutputStream out;
    private final ResponseHandlerInterface progressHandler;
    private long totalSize;

    private class FilePart {
        public final File file;
        public final byte[] header;

        public FilePart(String key, File file, String type, String customFileName) {
            if (TextUtils.isEmpty(customFileName)) {
                customFileName = file.getName();
            }
            this.header = createHeader(key, customFileName, type);
            this.file = file;
        }

        private byte[] createHeader(String key, String filename, String type) {
            ByteArrayOutputStream headerStream = new ByteArrayOutputStream();
            try {
                headerStream.write(SimpleMultipartEntity.this.boundaryLine);
                headerStream.write(SimpleMultipartEntity.createContentDisposition(key, filename));
                headerStream.write(SimpleMultipartEntity.createContentType(type));
                headerStream.write(SimpleMultipartEntity.TRANSFER_ENCODING_BINARY);
                headerStream.write(SimpleMultipartEntity.CR_LF);
            } catch (IOException e) {
                AsyncHttpClient.log.m17e("SimpleMultipartEntity", "createHeader ByteArrayOutputStream exception", e);
            }
            return headerStream.toByteArray();
        }
    }

    static {
        CR_LF = "\r\n".getBytes();
        TRANSFER_ENCODING_BINARY = "Content-Transfer-Encoding: binary\r\n".getBytes();
        MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    }

    public SimpleMultipartEntity(ResponseHandlerInterface progressHandler) {
        this.fileParts = new ArrayList();
        this.out = new ByteArrayOutputStream();
        StringBuilder buf = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 30; i++) {
            buf.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        this.boundary = buf.toString();
        this.boundaryLine = ("--" + this.boundary + "\r\n").getBytes();
        this.boundaryEnd = ("--" + this.boundary + "--\r\n").getBytes();
        this.progressHandler = progressHandler;
    }

    public final void addPartWithCharset(String key, String value, String charset) {
        if (charset == null) {
            charset = "UTF-8";
        }
        String str = "text/plain; charset=" + charset;
        try {
            this.out.write(this.boundaryLine);
            this.out.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n").getBytes());
            this.out.write(createContentType(str));
            this.out.write(CR_LF);
            this.out.write(value.getBytes());
            this.out.write(CR_LF);
        } catch (Throwable e) {
            AsyncHttpClient.log.m17e("SimpleMultipartEntity", "addPart ByteArrayOutputStream exception", e);
        }
    }

    public final void addPart(String key, File file, String type, String customFileName) {
        this.fileParts.add(new FilePart(key, file, normalizeContentType(type), customFileName));
    }

    private static String normalizeContentType(String type) {
        return type == null ? "application/octet-stream" : type;
    }

    static byte[] createContentType(String type) {
        return ("Content-Type: " + normalizeContentType(type) + "\r\n").getBytes();
    }

    static byte[] createContentDisposition(String key, String fileName) {
        return ("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + fileName + "\"\r\n").getBytes();
    }

    private void updateProgress(long count) {
        this.bytesWritten += count;
        this.progressHandler.sendProgressMessage(this.bytesWritten, this.totalSize);
    }

    public final long getContentLength() {
        long contentLen = (long) this.out.size();
        for (FilePart filePart : this.fileParts) {
            long len = ((long) filePart.header.length) + (filePart.file.length() + ((long) CR_LF.length));
            if (len < 0) {
                return -1;
            }
            contentLen += len;
        }
        return ((long) this.boundaryEnd.length) + contentLen;
    }

    public final Header getContentType() {
        return new BasicHeader("Content-Type", "multipart/form-data; boundary=" + this.boundary);
    }

    public final boolean isChunked() {
        return false;
    }

    public final boolean isRepeatable() {
        return this.isRepeatable;
    }

    public final boolean isStreaming() {
        return false;
    }

    public final void writeTo(OutputStream outstream) throws IOException {
        this.bytesWritten = 0;
        this.totalSize = (long) ((int) getContentLength());
        this.out.writeTo(outstream);
        updateProgress((long) this.out.size());
        for (FilePart filePart : this.fileParts) {
            outstream.write(filePart.header);
            filePart.this$0.updateProgress((long) filePart.header.length);
            InputStream fileInputStream = new FileInputStream(filePart.file);
            byte[] bArr = new byte[AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                outstream.write(bArr, 0, read);
                filePart.this$0.updateProgress((long) read);
            }
            outstream.write(CR_LF);
            filePart.this$0.updateProgress((long) CR_LF.length);
            outstream.flush();
            AsyncHttpClient.silentCloseInputStream(fileInputStream);
        }
        outstream.write(this.boundaryEnd);
        updateProgress((long) this.boundaryEnd.length);
    }

    public final Header getContentEncoding() {
        return null;
    }

    public final void consumeContent() throws IOException, UnsupportedOperationException {
    }

    public final InputStream getContent() throws IOException, UnsupportedOperationException {
        throw new UnsupportedOperationException("getContent() is not supported. Use writeTo() instead.");
    }
}

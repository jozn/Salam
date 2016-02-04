package com.loopj.android.http;

import cz.msebera.android.httpclient.Header;
import java.io.File;

public abstract class FileAsyncHttpResponseHandler extends AsyncHttpResponseHandler {
    protected final boolean append;
    protected final File file;
    protected File frontendFile;
    protected final boolean renameIfExists;

    public abstract void onFailure$5442d429(File file);

    public abstract void onSuccess$2d604dda(File file);

    public FileAsyncHttpResponseHandler(File file) {
        this(file, (byte) 0);
    }

    private FileAsyncHttpResponseHandler(File file, byte b) {
        this(file, '\u0000');
    }

    private FileAsyncHttpResponseHandler(File file, char c) {
        this(file, (short) 0);
    }

    private FileAsyncHttpResponseHandler(File file, short s) {
        super('\u0000');
        Utils.asserts(file != null, "File passed into FileAsyncHttpResponseHandler constructor must not be null");
        if (!(file.isDirectory() || file.getParentFile().isDirectory())) {
            Utils.asserts(file.getParentFile().mkdirs(), "Cannot create parent directories for requested File location");
        }
        if (file.isDirectory() && !file.mkdirs()) {
            AsyncHttpClient.log.m15d("FileAsyncHttpRH", "Cannot create directories for requested Directory location, might not be a problem");
        }
        this.file = file;
        this.append = false;
        this.renameIfExists = false;
    }

    private File getOriginalFile() {
        Utils.asserts(this.file != null, "Target file is null, fatal!");
        return this.file;
    }

    private File getTargetFile() {
        if (this.frontendFile == null) {
            File file;
            if (getOriginalFile().isDirectory()) {
                Utils.asserts(getOriginalFile().isDirectory(), "Target file is not a directory, cannot proceed");
                Utils.asserts(this.requestURI != null, "RequestURI is null, cannot proceed");
                String uri = this.requestURI.toString();
                String substring = uri.substring(uri.lastIndexOf(47) + 1, uri.length());
                file = new File(getOriginalFile(), substring);
                if (file.exists() && this.renameIfExists) {
                    File file2;
                    uri = !substring.contains(".") ? substring + " (%d)" : substring.substring(0, substring.lastIndexOf(46)) + " (%d)" + substring.substring(substring.lastIndexOf(46), substring.length());
                    int i = 0;
                    while (true) {
                        file2 = new File(getOriginalFile(), String.format(uri, new Object[]{Integer.valueOf(i)}));
                        if (!file2.exists()) {
                            break;
                        }
                        i++;
                    }
                    file = file2;
                }
            } else {
                file = getOriginalFile();
            }
            this.frontendFile = file;
        }
        return this.frontendFile;
    }

    public final void onFailure(int statusCode, Header[] headers, byte[] responseBytes, Throwable throwable) {
        onFailure$5442d429(getTargetFile());
    }

    public final void onSuccess(int statusCode, Header[] headers, byte[] responseBytes) {
        onSuccess$2d604dda(getTargetFile());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected final byte[] getResponseData(cz.msebera.android.httpclient.HttpEntity r11) throws java.io.IOException {
        /*
        r10 = this;
        if (r11 == 0) goto L_0x004b;
    L_0x0002:
        r4 = r11.getContent();
        r2 = r11.getContentLength();
        r0 = new java.io.FileOutputStream;
        r7 = r10.getTargetFile();
        r8 = r10.append;
        r0.<init>(r7, r8);
        if (r4 == 0) goto L_0x004b;
    L_0x0017:
        r7 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r6 = new byte[r7];	 Catch:{ all -> 0x0037 }
        r1 = 0;
    L_0x001c:
        r5 = r4.read(r6);	 Catch:{ all -> 0x0037 }
        r7 = -1;
        if (r5 == r7) goto L_0x0042;
    L_0x0023:
        r7 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x0037 }
        r7 = r7.isInterrupted();	 Catch:{ all -> 0x0037 }
        if (r7 != 0) goto L_0x0042;
    L_0x002d:
        r1 = r1 + r5;
        r7 = 0;
        r0.write(r6, r7, r5);	 Catch:{ all -> 0x0037 }
        r8 = (long) r1;	 Catch:{ all -> 0x0037 }
        r10.sendProgressMessage(r8, r2);	 Catch:{ all -> 0x0037 }
        goto L_0x001c;
    L_0x0037:
        r7 = move-exception;
        com.loopj.android.http.AsyncHttpClient.silentCloseInputStream(r4);
        r0.flush();
        com.loopj.android.http.AsyncHttpClient.silentCloseOutputStream(r0);
        throw r7;
    L_0x0042:
        com.loopj.android.http.AsyncHttpClient.silentCloseInputStream(r4);
        r0.flush();
        com.loopj.android.http.AsyncHttpClient.silentCloseOutputStream(r0);
    L_0x004b:
        r7 = 0;
        return r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loopj.android.http.FileAsyncHttpResponseHandler.getResponseData(cz.msebera.android.httpclient.HttpEntity):byte[]");
    }
}

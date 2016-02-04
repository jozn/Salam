package com.shamchat.utils;

import android.os.Environment;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Interceptor.Chain;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.File;
import java.io.IOException;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public final class OKHttpProgress {
    private final OkHttpClient client;
    private File downloadPath;
    private String downloadUrl;
    private File downloadedFile;
    public ProgressListener progressListener;

    public interface ProgressListener {
        void update(long j, long j2, boolean z);
    }

    /* renamed from: com.shamchat.utils.OKHttpProgress.1 */
    class C11711 implements Interceptor {
        C11711() {
        }

        public final Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(), OKHttpProgress.this.progressListener)).build();
        }
    }

    private static class ProgressResponseBody extends ResponseBody {
        private BufferedSource bufferedSource;
        private final ProgressListener progressListener;
        private final ResponseBody responseBody;

        /* renamed from: com.shamchat.utils.OKHttpProgress.ProgressResponseBody.1 */
        class C11721 extends ForwardingSource {
            long totalBytesRead;

            C11721(Source x0) {
                super(x0);
                this.totalBytesRead = 0;
            }

            public final long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                this.totalBytesRead = (bytesRead != -1 ? bytesRead : 0) + this.totalBytesRead;
                ProgressResponseBody.this.progressListener.update(this.totalBytesRead, ProgressResponseBody.this.responseBody.contentLength(), bytesRead == -1);
                return bytesRead;
            }
        }

        public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        public final MediaType contentType() {
            return this.responseBody.contentType();
        }

        public final long contentLength() throws IOException {
            return this.responseBody.contentLength();
        }

        public final BufferedSource source() throws IOException {
            if (this.bufferedSource == null) {
                this.bufferedSource = Okio.buffer(new C11721(this.responseBody.source()));
            }
            return this.bufferedSource;
        }
    }

    public OKHttpProgress(String downloadUrl, File downloadPath) throws Exception {
        this.client = new OkHttpClient();
        this.downloadUrl = null;
        this.progressListener = null;
        this.downloadedFile = null;
        this.downloadPath = null;
        this.downloadUrl = downloadUrl;
        this.downloadPath = downloadPath;
    }

    public final void start() throws Exception {
        Request request = new Builder().url(this.downloadUrl).tag(this.downloadUrl).build();
        this.client.networkInterceptors().add(new C11711());
        Response response = this.client.newCall(request).execute();
        if (response.isSuccessful()) {
            new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "salam/videos").mkdir();
            BufferedSink sink = Okio.buffer(Okio.sink(this.downloadPath));
            sink.writeAll(response.body().source());
            sink.close();
            return;
        }
        throw new IOException("Unexpected code " + response);
    }
}

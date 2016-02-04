package com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Picasso.Priority;
import com.squareup.picasso.RequestHandler.Result;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

final class BitmapHunter implements Runnable {
    private static final Object DECODE_LOCK;
    private static final RequestHandler ERRORING_HANDLER;
    private static final ThreadLocal<StringBuilder> NAME_BUILDER;
    private static final AtomicInteger SEQUENCE_GENERATOR;
    Action action;
    List<Action> actions;
    final Cache cache;
    final Request data;
    final Dispatcher dispatcher;
    Exception exception;
    int exifRotation;
    Future<?> future;
    final String key;
    LoadedFrom loadedFrom;
    final int memoryPolicy;
    int networkPolicy;
    final Picasso picasso;
    int priority$159b5429;
    final RequestHandler requestHandler;
    Bitmap result;
    int retryCount;
    final int sequence;
    final Stats stats;

    /* renamed from: com.squareup.picasso.BitmapHunter.1 */
    static class C12251 extends ThreadLocal<StringBuilder> {
        C12251() {
        }

        protected final /* bridge */ /* synthetic */ Object initialValue() {
            return new StringBuilder("Picasso-");
        }
    }

    /* renamed from: com.squareup.picasso.BitmapHunter.2 */
    static class C12262 extends RequestHandler {
        C12262() {
        }

        public final boolean canHandleRequest(Request data) {
            return true;
        }

        public final Result load$71fa0c91(Request request) throws IOException {
            throw new IllegalStateException("Unrecognized type of request: " + request);
        }
    }

    /* renamed from: com.squareup.picasso.BitmapHunter.3 */
    static class C12273 implements Runnable {
        final /* synthetic */ RuntimeException val$e;
        final /* synthetic */ Transformation val$transformation;

        C12273(Transformation transformation, RuntimeException runtimeException) {
            this.val$transformation = transformation;
            this.val$e = runtimeException;
        }

        public final void run() {
            throw new RuntimeException("Transformation " + this.val$transformation.key() + " crashed with exception.", this.val$e);
        }
    }

    /* renamed from: com.squareup.picasso.BitmapHunter.4 */
    static class C12284 implements Runnable {
        final /* synthetic */ StringBuilder val$builder;

        C12284(StringBuilder stringBuilder) {
            this.val$builder = stringBuilder;
        }

        public final void run() {
            throw new NullPointerException(this.val$builder.toString());
        }
    }

    /* renamed from: com.squareup.picasso.BitmapHunter.5 */
    static class C12295 implements Runnable {
        final /* synthetic */ Transformation val$transformation;

        C12295(Transformation transformation) {
            this.val$transformation = transformation;
        }

        public final void run() {
            throw new IllegalStateException("Transformation " + this.val$transformation.key() + " returned input Bitmap but recycled it.");
        }
    }

    /* renamed from: com.squareup.picasso.BitmapHunter.6 */
    static class C12306 implements Runnable {
        final /* synthetic */ Transformation val$transformation;

        C12306(Transformation transformation) {
            this.val$transformation = transformation;
        }

        public final void run() {
            throw new IllegalStateException("Transformation " + this.val$transformation.key() + " mutated input Bitmap but failed to recycle the original.");
        }
    }

    static {
        DECODE_LOCK = new Object();
        NAME_BUILDER = new C12251();
        SEQUENCE_GENERATOR = new AtomicInteger();
        ERRORING_HANDLER = new C12262();
    }

    private BitmapHunter(Picasso picasso, Dispatcher dispatcher, Cache cache, Stats stats, Action action, RequestHandler requestHandler) {
        this.sequence = SEQUENCE_GENERATOR.incrementAndGet();
        this.picasso = picasso;
        this.dispatcher = dispatcher;
        this.cache = cache;
        this.stats = stats;
        this.action = action;
        this.key = action.key;
        this.data = action.request;
        this.priority$159b5429 = action.request.priority$159b5429;
        this.memoryPolicy = action.memoryPolicy;
        this.networkPolicy = action.networkPolicy;
        this.requestHandler = requestHandler;
        this.retryCount = requestHandler.getRetryCount();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r28 = this;
        r0 = r28;
        r3 = r0.data;	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r4 = r3.uri;	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        if (r4 == 0) goto L_0x006c;
    L_0x0008:
        r3 = r3.uri;	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r3 = r3.getPath();	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r3 = java.lang.String.valueOf(r3);	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r4 = r3;
    L_0x0013:
        r3 = NAME_BUILDER;	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r3 = r3.get();	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r3 = (java.lang.StringBuilder) r3;	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r5 = r4.length();	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r5 = r5 + 8;
        r3.ensureCapacity(r5);	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r5 = 8;
        r6 = r3.length();	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r3.replace(r5, r6, r4);	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r4 = java.lang.Thread.currentThread();	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r3 = r3.toString();	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r4.setName(r3);	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r0 = r28;
        r3 = r0.picasso;	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r3 = r3.loggingEnabled;	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        if (r3 == 0) goto L_0x004b;
    L_0x0040:
        r3 = "Hunter";
        r4 = "executing";
        r5 = com.squareup.picasso.Utils.getLogIdsForHunter(r28);	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        com.squareup.picasso.Utils.log(r3, r4, r5);	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
    L_0x004b:
        r3 = r28.hunt();	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r0 = r28;
        r0.result = r3;	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r0 = r28;
        r3 = r0.result;	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        if (r3 != 0) goto L_0x0074;
    L_0x0059:
        r0 = r28;
        r3 = r0.dispatcher;	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r0 = r28;
        r3.dispatchFailed(r0);	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
    L_0x0062:
        r3 = java.lang.Thread.currentThread();
        r4 = "Picasso-Idle";
        r3.setName(r4);
    L_0x006b:
        return;
    L_0x006c:
        r3 = r3.resourceId;	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r3 = java.lang.Integer.toHexString(r3);	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r4 = r3;
        goto L_0x0013;
    L_0x0074:
        r0 = r28;
        r3 = r0.dispatcher;	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r4 = r3.handler;	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r3 = r3.handler;	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r5 = 4;
        r0 = r28;
        r3 = r3.obtainMessage(r5, r0);	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        r4.sendMessage(r3);	 Catch:{ ResponseException -> 0x0087, ContentLengthException -> 0x00a9, IOException -> 0x00c1, OutOfMemoryError -> 0x00d9, Exception -> 0x0214 }
        goto L_0x0062;
    L_0x0087:
        r2 = move-exception;
        r3 = r2.localCacheOnly;	 Catch:{ all -> 0x022d }
        if (r3 == 0) goto L_0x0092;
    L_0x008c:
        r3 = r2.responseCode;	 Catch:{ all -> 0x022d }
        r4 = 504; // 0x1f8 float:7.06E-43 double:2.49E-321;
        if (r3 == r4) goto L_0x0096;
    L_0x0092:
        r0 = r28;
        r0.exception = r2;	 Catch:{ all -> 0x022d }
    L_0x0096:
        r0 = r28;
        r3 = r0.dispatcher;	 Catch:{ all -> 0x022d }
        r0 = r28;
        r3.dispatchFailed(r0);	 Catch:{ all -> 0x022d }
        r3 = java.lang.Thread.currentThread();
        r4 = "Picasso-Idle";
        r3.setName(r4);
        goto L_0x006b;
    L_0x00a9:
        r2 = move-exception;
        r0 = r28;
        r0.exception = r2;	 Catch:{ all -> 0x022d }
        r0 = r28;
        r3 = r0.dispatcher;	 Catch:{ all -> 0x022d }
        r0 = r28;
        r3.dispatchRetry(r0);	 Catch:{ all -> 0x022d }
        r3 = java.lang.Thread.currentThread();
        r4 = "Picasso-Idle";
        r3.setName(r4);
        goto L_0x006b;
    L_0x00c1:
        r2 = move-exception;
        r0 = r28;
        r0.exception = r2;	 Catch:{ all -> 0x022d }
        r0 = r28;
        r3 = r0.dispatcher;	 Catch:{ all -> 0x022d }
        r0 = r28;
        r3.dispatchRetry(r0);	 Catch:{ all -> 0x022d }
        r3 = java.lang.Thread.currentThread();
        r4 = "Picasso-Idle";
        r3.setName(r4);
        goto L_0x006b;
    L_0x00d9:
        r2 = move-exception;
        r27 = new java.io.StringWriter;	 Catch:{ all -> 0x022d }
        r27.<init>();	 Catch:{ all -> 0x022d }
        r0 = r28;
        r0 = r0.stats;	 Catch:{ all -> 0x022d }
        r24 = r0;
        r3 = new com.squareup.picasso.StatsSnapshot;	 Catch:{ all -> 0x022d }
        r0 = r24;
        r4 = r0.cache;	 Catch:{ all -> 0x022d }
        r4 = r4.maxSize();	 Catch:{ all -> 0x022d }
        r0 = r24;
        r5 = r0.cache;	 Catch:{ all -> 0x022d }
        r5 = r5.size();	 Catch:{ all -> 0x022d }
        r0 = r24;
        r6 = r0.cacheHits;	 Catch:{ all -> 0x022d }
        r0 = r24;
        r8 = r0.cacheMisses;	 Catch:{ all -> 0x022d }
        r0 = r24;
        r10 = r0.totalDownloadSize;	 Catch:{ all -> 0x022d }
        r0 = r24;
        r12 = r0.totalOriginalBitmapSize;	 Catch:{ all -> 0x022d }
        r0 = r24;
        r14 = r0.totalTransformedBitmapSize;	 Catch:{ all -> 0x022d }
        r0 = r24;
        r0 = r0.averageDownloadSize;	 Catch:{ all -> 0x022d }
        r16 = r0;
        r0 = r24;
        r0 = r0.averageOriginalBitmapSize;	 Catch:{ all -> 0x022d }
        r18 = r0;
        r0 = r24;
        r0 = r0.averageTransformedBitmapSize;	 Catch:{ all -> 0x022d }
        r20 = r0;
        r0 = r24;
        r0 = r0.downloadCount;	 Catch:{ all -> 0x022d }
        r22 = r0;
        r0 = r24;
        r0 = r0.originalBitmapCount;	 Catch:{ all -> 0x022d }
        r23 = r0;
        r0 = r24;
        r0 = r0.transformedBitmapCount;	 Catch:{ all -> 0x022d }
        r24 = r0;
        r25 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x022d }
        r3.<init>(r4, r5, r6, r8, r10, r12, r14, r16, r18, r20, r22, r23, r24, r25);	 Catch:{ all -> 0x022d }
        r4 = new java.io.PrintWriter;	 Catch:{ all -> 0x022d }
        r0 = r27;
        r4.<init>(r0);	 Catch:{ all -> 0x022d }
        r5 = "===============BEGIN PICASSO STATS ===============";
        r4.println(r5);	 Catch:{ all -> 0x022d }
        r5 = "Memory Cache Stats";
        r4.println(r5);	 Catch:{ all -> 0x022d }
        r5 = "  Max Cache Size: ";
        r4.print(r5);	 Catch:{ all -> 0x022d }
        r5 = r3.maxSize;	 Catch:{ all -> 0x022d }
        r4.println(r5);	 Catch:{ all -> 0x022d }
        r5 = "  Cache Size: ";
        r4.print(r5);	 Catch:{ all -> 0x022d }
        r5 = r3.size;	 Catch:{ all -> 0x022d }
        r4.println(r5);	 Catch:{ all -> 0x022d }
        r5 = "  Cache % Full: ";
        r4.print(r5);	 Catch:{ all -> 0x022d }
        r5 = r3.size;	 Catch:{ all -> 0x022d }
        r5 = (float) r5;	 Catch:{ all -> 0x022d }
        r6 = r3.maxSize;	 Catch:{ all -> 0x022d }
        r6 = (float) r6;	 Catch:{ all -> 0x022d }
        r5 = r5 / r6;
        r6 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r5 = r5 * r6;
        r6 = (double) r5;	 Catch:{ all -> 0x022d }
        r6 = java.lang.Math.ceil(r6);	 Catch:{ all -> 0x022d }
        r5 = (int) r6;	 Catch:{ all -> 0x022d }
        r4.println(r5);	 Catch:{ all -> 0x022d }
        r5 = "  Cache Hits: ";
        r4.print(r5);	 Catch:{ all -> 0x022d }
        r6 = r3.cacheHits;	 Catch:{ all -> 0x022d }
        r4.println(r6);	 Catch:{ all -> 0x022d }
        r5 = "  Cache Misses: ";
        r4.print(r5);	 Catch:{ all -> 0x022d }
        r6 = r3.cacheMisses;	 Catch:{ all -> 0x022d }
        r4.println(r6);	 Catch:{ all -> 0x022d }
        r5 = "Network Stats";
        r4.println(r5);	 Catch:{ all -> 0x022d }
        r5 = "  Download Count: ";
        r4.print(r5);	 Catch:{ all -> 0x022d }
        r5 = r3.downloadCount;	 Catch:{ all -> 0x022d }
        r4.println(r5);	 Catch:{ all -> 0x022d }
        r5 = "  Total Download Size: ";
        r4.print(r5);	 Catch:{ all -> 0x022d }
        r6 = r3.totalDownloadSize;	 Catch:{ all -> 0x022d }
        r4.println(r6);	 Catch:{ all -> 0x022d }
        r5 = "  Average Download Size: ";
        r4.print(r5);	 Catch:{ all -> 0x022d }
        r6 = r3.averageDownloadSize;	 Catch:{ all -> 0x022d }
        r4.println(r6);	 Catch:{ all -> 0x022d }
        r5 = "Bitmap Stats";
        r4.println(r5);	 Catch:{ all -> 0x022d }
        r5 = "  Total Bitmaps Decoded: ";
        r4.print(r5);	 Catch:{ all -> 0x022d }
        r5 = r3.originalBitmapCount;	 Catch:{ all -> 0x022d }
        r4.println(r5);	 Catch:{ all -> 0x022d }
        r5 = "  Total Bitmap Size: ";
        r4.print(r5);	 Catch:{ all -> 0x022d }
        r6 = r3.totalOriginalBitmapSize;	 Catch:{ all -> 0x022d }
        r4.println(r6);	 Catch:{ all -> 0x022d }
        r5 = "  Total Transformed Bitmaps: ";
        r4.print(r5);	 Catch:{ all -> 0x022d }
        r5 = r3.transformedBitmapCount;	 Catch:{ all -> 0x022d }
        r4.println(r5);	 Catch:{ all -> 0x022d }
        r5 = "  Total Transformed Bitmap Size: ";
        r4.print(r5);	 Catch:{ all -> 0x022d }
        r6 = r3.totalTransformedBitmapSize;	 Catch:{ all -> 0x022d }
        r4.println(r6);	 Catch:{ all -> 0x022d }
        r5 = "  Average Bitmap Size: ";
        r4.print(r5);	 Catch:{ all -> 0x022d }
        r6 = r3.averageOriginalBitmapSize;	 Catch:{ all -> 0x022d }
        r4.println(r6);	 Catch:{ all -> 0x022d }
        r5 = "  Average Transformed Bitmap Size: ";
        r4.print(r5);	 Catch:{ all -> 0x022d }
        r6 = r3.averageTransformedBitmapSize;	 Catch:{ all -> 0x022d }
        r4.println(r6);	 Catch:{ all -> 0x022d }
        r3 = "===============END PICASSO STATS ===============";
        r4.println(r3);	 Catch:{ all -> 0x022d }
        r4.flush();	 Catch:{ all -> 0x022d }
        r3 = new java.lang.RuntimeException;	 Catch:{ all -> 0x022d }
        r4 = r27.toString();	 Catch:{ all -> 0x022d }
        r3.<init>(r4, r2);	 Catch:{ all -> 0x022d }
        r0 = r28;
        r0.exception = r3;	 Catch:{ all -> 0x022d }
        r0 = r28;
        r3 = r0.dispatcher;	 Catch:{ all -> 0x022d }
        r0 = r28;
        r3.dispatchFailed(r0);	 Catch:{ all -> 0x022d }
        r3 = java.lang.Thread.currentThread();
        r4 = "Picasso-Idle";
        r3.setName(r4);
        goto L_0x006b;
    L_0x0214:
        r2 = move-exception;
        r0 = r28;
        r0.exception = r2;	 Catch:{ all -> 0x022d }
        r0 = r28;
        r3 = r0.dispatcher;	 Catch:{ all -> 0x022d }
        r0 = r28;
        r3.dispatchFailed(r0);	 Catch:{ all -> 0x022d }
        r3 = java.lang.Thread.currentThread();
        r4 = "Picasso-Idle";
        r3.setName(r4);
        goto L_0x006b;
    L_0x022d:
        r3 = move-exception;
        r4 = java.lang.Thread.currentThread();
        r5 = "Picasso-Idle";
        r4.setName(r5);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.picasso.BitmapHunter.run():void");
    }

    private Bitmap hunt() throws IOException {
        int i;
        Request request;
        Bitmap bitmap = null;
        if (MemoryPolicy.shouldReadFromMemoryCache(this.memoryPolicy)) {
            bitmap = this.cache.get(this.key);
            if (bitmap != null) {
                this.stats.dispatchCacheHit();
                this.loadedFrom = LoadedFrom.MEMORY;
                if (this.picasso.loggingEnabled) {
                    Utils.log("Hunter", "decoded", this.data.logId(), "from cache");
                }
                return bitmap;
            }
        }
        Request request2 = this.data;
        if (this.retryCount == 0) {
            i = NetworkPolicy.OFFLINE.index;
        } else {
            i = this.networkPolicy;
        }
        request2.networkPolicy = i;
        Result result = this.requestHandler.load$71fa0c91(this.data);
        if (result != null) {
            this.loadedFrom = result.loadedFrom;
            this.exifRotation = result.exifOrientation;
            bitmap = result.bitmap;
            if (bitmap == null) {
                InputStream is = result.stream;
                try {
                    request = this.data;
                    InputStream markableInputStream = new MarkableInputStream(is);
                    long savePosition = markableInputStream.savePosition(AccessibilityNodeInfoCompat.ACTION_CUT);
                    Options createBitmapOptions = RequestHandler.createBitmapOptions(request);
                    boolean requiresInSampleSize = RequestHandler.requiresInSampleSize(createBitmapOptions);
                    boolean isWebPFile = Utils.isWebPFile(markableInputStream);
                    markableInputStream.reset(savePosition);
                    if (isWebPFile) {
                        byte[] toByteArray = Utils.toByteArray(markableInputStream);
                        if (requiresInSampleSize) {
                            BitmapFactory.decodeByteArray(toByteArray, 0, toByteArray.length, createBitmapOptions);
                            RequestHandler.calculateInSampleSize(request.targetWidth, request.targetHeight, createBitmapOptions, request);
                        }
                        bitmap = BitmapFactory.decodeByteArray(toByteArray, 0, toByteArray.length, createBitmapOptions);
                    } else {
                        if (requiresInSampleSize) {
                            BitmapFactory.decodeStream(markableInputStream, null, createBitmapOptions);
                            RequestHandler.calculateInSampleSize(request.targetWidth, request.targetHeight, createBitmapOptions, request);
                            markableInputStream.reset(savePosition);
                        }
                        bitmap = BitmapFactory.decodeStream(markableInputStream, null, createBitmapOptions);
                        if (bitmap == null) {
                            throw new IOException("Failed to decode stream.");
                        }
                    }
                    Utils.closeQuietly(is);
                } catch (Throwable th) {
                    Utils.closeQuietly(is);
                }
            }
        }
        if (bitmap != null) {
            if (this.picasso.loggingEnabled) {
                Utils.log("Hunter", "decoded", this.data.logId());
            }
            this.stats.processBitmap(bitmap, 2);
            request = this.data;
            Object obj = (request.needsMatrixTransform() || request.hasCustomTransformations()) ? 1 : null;
            if (!(obj == null && this.exifRotation == 0)) {
                synchronized (DECODE_LOCK) {
                    if (this.data.needsMatrixTransform() || this.exifRotation != 0) {
                        int i2;
                        int i3;
                        int i4;
                        Bitmap createBitmap;
                        Request request3 = this.data;
                        int i5 = this.exifRotation;
                        int width = bitmap.getWidth();
                        i = bitmap.getHeight();
                        boolean z = request3.onlyScaleDown;
                        Matrix matrix = new Matrix();
                        if (request3.needsMatrixTransform()) {
                            int i6 = request3.targetWidth;
                            int i7 = request3.targetHeight;
                            float f = request3.rotationDegrees;
                            if (f != 0.0f) {
                                if (request3.hasRotationPivot) {
                                    matrix.setRotate(f, request3.rotationPivotX, request3.rotationPivotY);
                                } else {
                                    matrix.setRotate(f);
                                }
                            }
                            float f2;
                            if (request3.centerCrop) {
                                int ceil;
                                float f3;
                                int i8;
                                float f4;
                                f = ((float) i6) / ((float) width);
                                f2 = ((float) i7) / ((float) i);
                                if (f > f2) {
                                    ceil = (int) Math.ceil((double) (((float) i) * (f2 / f)));
                                    f3 = f;
                                    i8 = 0;
                                    i2 = ceil;
                                    ceil = (i - ceil) / 2;
                                    f4 = ((float) i7) / ((float) ceil);
                                    i3 = width;
                                } else {
                                    i8 = (int) Math.ceil((double) (((float) width) * (f / f2)));
                                    f3 = ((float) i6) / ((float) i8);
                                    i2 = i;
                                    float f5 = f2;
                                    ceil = 0;
                                    i3 = i8;
                                    i8 = (width - i8) / 2;
                                    f4 = f5;
                                }
                                if (shouldResize(z, width, i, i6, i7)) {
                                    matrix.preScale(f3, f4);
                                }
                                width = ceil;
                                i = i8;
                            } else if (request3.centerInside) {
                                f2 = ((float) i6) / ((float) width);
                                f = ((float) i7) / ((float) i);
                                if (f2 >= f) {
                                    f2 = f;
                                }
                                if (shouldResize(z, width, i, i6, i7)) {
                                    matrix.preScale(f2, f2);
                                }
                                i4 = i;
                                i = 0;
                                i2 = i4;
                                i3 = width;
                                width = 0;
                            } else if (!((i6 == 0 && i7 == 0) || (i6 == width && i7 == i))) {
                                f = i6 != 0 ? ((float) i6) / ((float) width) : ((float) i7) / ((float) i);
                                f2 = i7 != 0 ? ((float) i7) / ((float) i) : ((float) i6) / ((float) width);
                                if (shouldResize(z, width, i, i6, i7)) {
                                    matrix.preScale(f, f2);
                                }
                            }
                            if (i5 != 0) {
                                matrix.preRotate((float) i5);
                            }
                            createBitmap = Bitmap.createBitmap(bitmap, i, width, i3, i2, matrix, true);
                            if (createBitmap != bitmap) {
                                bitmap.recycle();
                                bitmap = createBitmap;
                            }
                            if (this.picasso.loggingEnabled) {
                                Utils.log("Hunter", "transformed", this.data.logId());
                            }
                        }
                        i4 = i;
                        i = 0;
                        i2 = i4;
                        i3 = width;
                        width = 0;
                        if (i5 != 0) {
                            matrix.preRotate((float) i5);
                        }
                        createBitmap = Bitmap.createBitmap(bitmap, i, width, i3, i2, matrix, true);
                        if (createBitmap != bitmap) {
                            bitmap.recycle();
                            bitmap = createBitmap;
                        }
                        if (this.picasso.loggingEnabled) {
                            Utils.log("Hunter", "transformed", this.data.logId());
                        }
                    }
                    if (this.data.hasCustomTransformations()) {
                        bitmap = applyCustomTransformations(this.data.transformations, bitmap);
                        if (this.picasso.loggingEnabled) {
                            Utils.log("Hunter", "transformed", this.data.logId(), "from custom transformations");
                        }
                    }
                }
                if (bitmap != null) {
                    this.stats.processBitmap(bitmap, 3);
                }
            }
        }
        return bitmap;
    }

    final void detach(Action action) {
        int i = 1;
        int i2 = 0;
        boolean detached = false;
        if (this.action == action) {
            this.action = null;
            detached = true;
        } else if (this.actions != null) {
            detached = this.actions.remove(action);
        }
        if (detached && action.request.priority$159b5429 == this.priority$159b5429) {
            int i3;
            int i4 = Priority.LOW$159b5429;
            if (this.actions == null || this.actions.isEmpty()) {
                i3 = 0;
            } else {
                i3 = 1;
            }
            if (this.action == null && i3 == 0) {
                i = 0;
            }
            if (i != 0) {
                if (this.action != null) {
                    i = this.action.request.priority$159b5429;
                } else {
                    i = i4;
                }
                if (i3 != 0) {
                    i4 = this.actions.size();
                    while (i2 < i4) {
                        i3 = ((Action) this.actions.get(i2)).request.priority$159b5429;
                        if (i3 - 1 <= i - 1) {
                            i3 = i;
                        }
                        i2++;
                        i = i3;
                    }
                }
            } else {
                i = i4;
            }
            this.priority$159b5429 = i;
        }
        if (this.picasso.loggingEnabled) {
            Utils.log("Hunter", "removed", action.request.logId(), Utils.getLogIdsForHunter(this, "from "));
        }
    }

    final boolean cancel() {
        if (this.action != null) {
            return false;
        }
        if ((this.actions == null || this.actions.isEmpty()) && this.future != null && this.future.cancel(false)) {
            return true;
        }
        return false;
    }

    final boolean isCancelled() {
        return this.future != null && this.future.isCancelled();
    }

    static BitmapHunter forRequest(Picasso picasso, Dispatcher dispatcher, Cache cache, Stats stats, Action action) {
        Request request = action.request;
        List<RequestHandler> requestHandlers = picasso.requestHandlers;
        int count = requestHandlers.size();
        for (int i = 0; i < count; i++) {
            RequestHandler requestHandler = (RequestHandler) requestHandlers.get(i);
            if (requestHandler.canHandleRequest(request)) {
                return new BitmapHunter(picasso, dispatcher, cache, stats, action, requestHandler);
            }
        }
        return new BitmapHunter(picasso, dispatcher, cache, stats, action, ERRORING_HANDLER);
    }

    private static Bitmap applyCustomTransformations(List<Transformation> transformations, Bitmap result) {
        int i = 0;
        int count = transformations.size();
        while (i < count) {
            Transformation transformation = (Transformation) transformations.get(i);
            try {
                Bitmap newResult = transformation.transform(result);
                if (newResult == null) {
                    StringBuilder builder = new StringBuilder("Transformation ").append(transformation.key()).append(" returned null after ").append(i).append(" previous transformation(s).\n\nTransformation list:\n");
                    for (Transformation t : transformations) {
                        builder.append(t.key()).append('\n');
                    }
                    Picasso.HANDLER.post(new C12284(builder));
                    return null;
                } else if (newResult == result && result.isRecycled()) {
                    Picasso.HANDLER.post(new C12295(transformation));
                    return null;
                } else if (newResult == result || result.isRecycled()) {
                    result = newResult;
                    i++;
                } else {
                    Picasso.HANDLER.post(new C12306(transformation));
                    return null;
                }
            } catch (RuntimeException e) {
                Picasso.HANDLER.post(new C12273(transformation, e));
                return null;
            }
        }
        return result;
    }

    private static boolean shouldResize(boolean onlyScaleDown, int inWidth, int inHeight, int targetWidth, int targetHeight) {
        return !onlyScaleDown || inWidth > targetWidth || inHeight > targetHeight;
    }
}

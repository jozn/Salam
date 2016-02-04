package com.squareup.picasso;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class Stats {
    long averageDownloadSize;
    long averageOriginalBitmapSize;
    long averageTransformedBitmapSize;
    final Cache cache;
    long cacheHits;
    long cacheMisses;
    int downloadCount;
    final Handler handler;
    int originalBitmapCount;
    public final HandlerThread statsThread;
    long totalDownloadSize;
    long totalOriginalBitmapSize;
    long totalTransformedBitmapSize;
    int transformedBitmapCount;

    private static class StatsHandler extends Handler {
        private final Stats stats;

        /* renamed from: com.squareup.picasso.Stats.StatsHandler.1 */
        class C12371 implements Runnable {
            final /* synthetic */ Message val$msg;

            C12371(Message message) {
                this.val$msg = message;
            }

            public final void run() {
                throw new AssertionError("Unhandled stats message." + this.val$msg.what);
            }
        }

        public StatsHandler(Looper looper, Stats stats) {
            super(looper);
            this.stats = stats;
        }

        public final void handleMessage(Message msg) {
            Stats stats;
            long j;
            switch (msg.what) {
                case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                    stats = this.stats;
                    stats.cacheHits++;
                case Logger.SEVERE /*1*/:
                    stats = this.stats;
                    stats.cacheMisses++;
                case Logger.WARNING /*2*/:
                    stats = this.stats;
                    j = (long) msg.arg1;
                    stats.originalBitmapCount++;
                    stats.totalOriginalBitmapSize = j + stats.totalOriginalBitmapSize;
                    stats.averageOriginalBitmapSize = stats.totalOriginalBitmapSize / ((long) stats.originalBitmapCount);
                case Logger.INFO /*3*/:
                    stats = this.stats;
                    j = (long) msg.arg1;
                    stats.transformedBitmapCount++;
                    stats.totalTransformedBitmapSize = j + stats.totalTransformedBitmapSize;
                    stats.averageTransformedBitmapSize = stats.totalTransformedBitmapSize / ((long) stats.originalBitmapCount);
                case Logger.CONFIG /*4*/:
                    Stats stats2 = this.stats;
                    Long l = (Long) msg.obj;
                    stats2.downloadCount++;
                    stats2.totalDownloadSize += l.longValue();
                    stats2.averageDownloadSize = stats2.totalDownloadSize / ((long) stats2.downloadCount);
                default:
                    Picasso.HANDLER.post(new C12371(msg));
            }
        }
    }

    Stats(Cache cache) {
        this.cache = cache;
        this.statsThread = new HandlerThread("Picasso-Stats", 10);
        this.statsThread.start();
        Utils.flushStackLocalLeaks(this.statsThread.getLooper());
        this.handler = new StatsHandler(this.statsThread.getLooper(), this);
    }

    final void dispatchCacheHit() {
        this.handler.sendEmptyMessage(0);
    }

    final void processBitmap(Bitmap bitmap, int what) {
        this.handler.sendMessage(this.handler.obtainMessage(what, Utils.getBitmapBytes(bitmap), 0));
    }
}

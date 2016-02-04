package com.google.android.gms.common.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.support.v4.util.LruCache;
import android.util.Log;
import com.google.android.gms.internal.zzmx;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public final class ImageManager {
    private static final Object zzahR;
    private static HashSet<Uri> zzahS;
    private final Context mContext;
    private final Handler mHandler;
    private final ExecutorService zzahV;
    private final zzb zzahW;
    private final zzmx zzahX;
    private final Map<zza, ImageReceiver> zzahY;
    private final Map<Uri, ImageReceiver> zzahZ;
    private final Map<Uri, Long> zzaia;

    private final class ImageReceiver extends ResultReceiver {
        private final Uri mUri;
        private final ArrayList<zza> zzaib;
        final /* synthetic */ ImageManager zzaic;

        public final void onReceiveResult(int resultCode, Bundle resultData) {
            this.zzaic.zzahV.execute(new zzc(this.zzaic, this.mUri, (ParcelFileDescriptor) resultData.getParcelable("com.google.android.gms.extra.fileDescriptor")));
        }
    }

    public interface OnImageLoadedListener {
    }

    private static final class zzb extends LruCache<zza, Bitmap> {
        protected final /* synthetic */ void entryRemoved(boolean x0, Object x1, Object x2, Object x3) {
            super.entryRemoved(x0, (zza) x1, (Bitmap) x2, (Bitmap) x3);
        }

        protected final /* synthetic */ int sizeOf(Object x0, Object x1) {
            Bitmap bitmap = (Bitmap) x1;
            return bitmap.getHeight() * bitmap.getRowBytes();
        }
    }

    private final class zzc implements Runnable {
        private final Uri mUri;
        final /* synthetic */ ImageManager zzaic;
        private final ParcelFileDescriptor zzaid;

        public zzc(ImageManager imageManager, Uri uri, ParcelFileDescriptor parcelFileDescriptor) {
            this.zzaic = imageManager;
            this.mUri = uri;
            this.zzaid = parcelFileDescriptor;
        }

        public final void run() {
            String str = "LoadBitmapFromDiskRunnable can't be executed in the main thread";
            if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                Log.e("Asserts", "checkNotMainThread: current thread " + Thread.currentThread() + " IS the main thread " + Looper.getMainLooper().getThread() + "!");
                throw new IllegalStateException(str);
            }
            boolean z = false;
            Bitmap bitmap = null;
            if (this.zzaid != null) {
                try {
                    bitmap = BitmapFactory.decodeFileDescriptor(this.zzaid.getFileDescriptor());
                } catch (Throwable e) {
                    Log.e("ImageManager", "OOM while loading bitmap for uri: " + this.mUri, e);
                    z = true;
                }
                try {
                    this.zzaid.close();
                } catch (Throwable e2) {
                    Log.e("ImageManager", "closed failed", e2);
                }
            }
            CountDownLatch countDownLatch = new CountDownLatch(1);
            this.zzaic.mHandler.post(new zzf(this.zzaic, this.mUri, bitmap, z, countDownLatch));
            try {
                countDownLatch.await();
            } catch (InterruptedException e3) {
                Log.w("ImageManager", "Latch interrupted while posting " + this.mUri);
            }
        }
    }

    private final class zzf implements Runnable {
        private final Bitmap mBitmap;
        private final Uri mUri;
        final /* synthetic */ ImageManager zzaic;
        private boolean zzaif;
        private final CountDownLatch zzpy;

        public zzf(ImageManager imageManager, Uri uri, Bitmap bitmap, boolean z, CountDownLatch countDownLatch) {
            this.zzaic = imageManager;
            this.mUri = uri;
            this.mBitmap = bitmap;
            this.zzaif = z;
            this.zzpy = countDownLatch;
        }

        private void zza(ImageReceiver imageReceiver, boolean z) {
            ArrayList zza = imageReceiver.zzaib;
            int size = zza.size();
            for (int i = 0; i < size; i++) {
                zza com_google_android_gms_common_images_zza = (zza) zza.get(i);
                if (z) {
                    com_google_android_gms_common_images_zza.zza$1cfc2d67(this.zzaic.mContext, this.mBitmap);
                } else {
                    this.zzaic.zzaia.put(this.mUri, Long.valueOf(SystemClock.elapsedRealtime()));
                    com_google_android_gms_common_images_zza.zza$4e565205(this.zzaic.mContext, this.zzaic.zzahX);
                }
                if (!(com_google_android_gms_common_images_zza instanceof com.google.android.gms.common.images.zza.zzc)) {
                    this.zzaic.zzahY.remove(com_google_android_gms_common_images_zza);
                }
            }
        }

        public final void run() {
            String str = "OnBitmapLoadedRunnable must be executed in the main thread";
            if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
                Log.e("Asserts", "checkMainThread: current thread " + Thread.currentThread() + " IS NOT the main thread " + Looper.getMainLooper().getThread() + "!");
                throw new IllegalStateException(str);
            }
            boolean z = this.mBitmap != null;
            if (this.zzaic.zzahW != null) {
                if (this.zzaif) {
                    this.zzaic.zzahW.evictAll();
                    System.gc();
                    this.zzaif = false;
                    this.zzaic.mHandler.post(this);
                    return;
                } else if (z) {
                    this.zzaic.zzahW.put(new zza(this.mUri), this.mBitmap);
                }
            }
            ImageReceiver imageReceiver = (ImageReceiver) this.zzaic.zzahZ.remove(this.mUri);
            if (imageReceiver != null) {
                zza(imageReceiver, z);
            }
            this.zzpy.countDown();
            synchronized (ImageManager.zzahR) {
                ImageManager.zzahS.remove(this.mUri);
            }
        }
    }

    static {
        zzahR = new Object();
        zzahS = new HashSet();
    }
}

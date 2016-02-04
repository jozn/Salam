package com.squareup.picasso;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.StatFs;
import android.provider.Settings.System;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ThreadFactory;

final class Utils {
    static final StringBuilder MAIN_THREAD_KEY_BUILDER;

    /* renamed from: com.squareup.picasso.Utils.1 */
    static class C12391 extends Handler {
        C12391(Looper x0) {
            super(x0);
        }

        public final void handleMessage(Message msg) {
            sendMessageDelayed(obtainMessage(), 1000);
        }
    }

    private static class PicassoThread extends Thread {
        public PicassoThread(Runnable r) {
            super(r);
        }

        public final void run() {
            Process.setThreadPriority(10);
            super.run();
        }
    }

    static class PicassoThreadFactory implements ThreadFactory {
        PicassoThreadFactory() {
        }

        public final Thread newThread(Runnable r) {
            return new PicassoThread(r);
        }
    }

    static {
        MAIN_THREAD_KEY_BUILDER = new StringBuilder();
    }

    static int getBitmapBytes(Bitmap bitmap) {
        int result;
        if (VERSION.SDK_INT >= 12) {
            result = bitmap.getByteCount();
        } else {
            result = bitmap.getRowBytes() * bitmap.getHeight();
        }
        if (result >= 0) {
            return result;
        }
        throw new IllegalStateException("Negative size: " + bitmap);
    }

    static <T> T checkNotNull(T value, String message) {
        if (value != null) {
            return value;
        }
        throw new NullPointerException(message);
    }

    static void checkMain() {
        if ((Looper.getMainLooper().getThread() == Thread.currentThread() ? 1 : null) == null) {
            throw new IllegalStateException("Method call should happen from the main thread.");
        }
    }

    static String getLogIdsForHunter(BitmapHunter hunter) {
        return getLogIdsForHunter(hunter, BuildConfig.VERSION_NAME);
    }

    static String getLogIdsForHunter(BitmapHunter hunter, String prefix) {
        StringBuilder builder = new StringBuilder(prefix);
        Action action = hunter.action;
        if (action != null) {
            builder.append(action.request.logId());
        }
        List<Action> actions = hunter.actions;
        if (actions != null) {
            int count = actions.size();
            for (int i = 0; i < count; i++) {
                if (i > 0 || action != null) {
                    builder.append(", ");
                }
                builder.append(((Action) actions.get(i)).request.logId());
            }
        }
        return builder.toString();
    }

    static void log(String owner, String verb, String logId) {
        log(owner, verb, logId, BuildConfig.VERSION_NAME);
    }

    static void log(String owner, String verb, String logId, String extras) {
        Log.d("Picasso", String.format("%1$-11s %2$-12s %3$s %4$s", new Object[]{owner, verb, logId, extras}));
    }

    static String createKey(Request data) {
        StringBuilder stringBuilder = MAIN_THREAD_KEY_BUILDER;
        if (data.stableKey != null) {
            stringBuilder.ensureCapacity(data.stableKey.length() + 50);
            stringBuilder.append(data.stableKey);
        } else if (data.uri != null) {
            String uri = data.uri.toString();
            stringBuilder.ensureCapacity(uri.length() + 50);
            stringBuilder.append(uri);
        } else {
            stringBuilder.ensureCapacity(50);
            stringBuilder.append(data.resourceId);
        }
        stringBuilder.append('\n');
        if (data.rotationDegrees != 0.0f) {
            stringBuilder.append("rotation:").append(data.rotationDegrees);
            if (data.hasRotationPivot) {
                stringBuilder.append('@').append(data.rotationPivotX).append('x').append(data.rotationPivotY);
            }
            stringBuilder.append('\n');
        }
        if (data.hasSize()) {
            stringBuilder.append("resize:").append(data.targetWidth).append('x').append(data.targetHeight);
            stringBuilder.append('\n');
        }
        if (data.centerCrop) {
            stringBuilder.append("centerCrop\n");
        } else if (data.centerInside) {
            stringBuilder.append("centerInside\n");
        }
        if (data.transformations != null) {
            int size = data.transformations.size();
            for (int i = 0; i < size; i++) {
                stringBuilder.append(((Transformation) data.transformations.get(i)).key());
                stringBuilder.append('\n');
            }
        }
        String result = stringBuilder.toString();
        MAIN_THREAD_KEY_BUILDER.setLength(0);
        return result;
    }

    static void closeQuietly(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
    }

    static boolean parseResponseSourceHeader(String header) {
        if (header == null) {
            return false;
        }
        String[] parts = header.split(" ", 2);
        if ("CACHE".equals(parts[0])) {
            return true;
        }
        if (parts.length == 1) {
            return false;
        }
        try {
            if ("CONDITIONAL_CACHE".equals(parts[0]) && Integer.parseInt(parts[1]) == 304) {
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static Downloader createDefaultDownloader(Context context) {
        try {
            Class.forName("com.squareup.okhttp.OkHttpClient");
            return new OkHttpDownloader(context);
        } catch (ClassNotFoundException e) {
            return new UrlConnectionDownloader(context);
        }
    }

    static File createDefaultCacheDir(Context context) {
        File cache = new File(context.getApplicationContext().getCacheDir(), "picasso-cache");
        if (!cache.exists()) {
            cache.mkdirs();
        }
        return cache;
    }

    static long calculateDiskCacheSize(File dir) {
        long size = 5242880;
        try {
            StatFs statFs = new StatFs(dir.getAbsolutePath());
            size = (((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize())) / 50;
        } catch (IllegalArgumentException e) {
        }
        return Math.max(Math.min(size, 52428800), 5242880);
    }

    static int calculateMemoryCacheSize(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService("activity");
        boolean largeHeap = (context.getApplicationInfo().flags & AccessibilityNodeInfoCompat.ACTION_DISMISS) != 0;
        int memoryClass = am.getMemoryClass();
        if (largeHeap && VERSION.SDK_INT >= 11) {
            memoryClass = am.getLargeMemoryClass();
        }
        return (AccessibilityNodeInfoCompat.ACTION_DISMISS * memoryClass) / 7;
    }

    static boolean isAirplaneModeOn(Context context) {
        try {
            if (System.getInt(context.getContentResolver(), "airplane_mode_on", 0) != 0) {
                return true;
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    static <T> T getService(Context context, String service) {
        return context.getSystemService(service);
    }

    static boolean hasPermission(Context context, String permission) {
        return context.checkCallingOrSelfPermission(permission) == 0;
    }

    static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD];
        while (true) {
            int n = input.read(buffer);
            if (-1 == n) {
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(buffer, 0, n);
        }
    }

    static boolean isWebPFile(InputStream stream) throws IOException {
        byte[] fileHeaderBytes = new byte[12];
        if (stream.read(fileHeaderBytes, 0, 12) != 12) {
            return false;
        }
        if ("RIFF".equals(new String(fileHeaderBytes, 0, 4, "US-ASCII")) && "WEBP".equals(new String(fileHeaderBytes, 8, 4, "US-ASCII"))) {
            return true;
        }
        return false;
    }

    static int getResourceId(Resources resources, Request data) throws FileNotFoundException {
        if (data.resourceId != 0 || data.uri == null) {
            return data.resourceId;
        }
        String pkg = data.uri.getAuthority();
        if (pkg == null) {
            throw new FileNotFoundException("No package provided: " + data.uri);
        }
        List<String> segments = data.uri.getPathSegments();
        if (segments == null || segments.isEmpty()) {
            throw new FileNotFoundException("No path segments: " + data.uri);
        } else if (segments.size() == 1) {
            try {
                return Integer.parseInt((String) segments.get(0));
            } catch (NumberFormatException e) {
                throw new FileNotFoundException("Last path segment is not a resource ID: " + data.uri);
            }
        } else if (segments.size() == 2) {
            return resources.getIdentifier((String) segments.get(1), (String) segments.get(0), pkg);
        } else {
            throw new FileNotFoundException("More than two path segments: " + data.uri);
        }
    }

    static Resources getResources(Context context, Request data) throws FileNotFoundException {
        if (data.resourceId != 0 || data.uri == null) {
            return context.getResources();
        }
        String pkg = data.uri.getAuthority();
        if (pkg == null) {
            throw new FileNotFoundException("No package provided: " + data.uri);
        }
        try {
            return context.getPackageManager().getResourcesForApplication(pkg);
        } catch (NameNotFoundException e) {
            throw new FileNotFoundException("Unable to obtain resources for package: " + data.uri);
        }
    }

    static void flushStackLocalLeaks(Looper looper) {
        Handler c12391 = new C12391(looper);
        c12391.sendMessageDelayed(c12391.obtainMessage(), 1000);
    }
}

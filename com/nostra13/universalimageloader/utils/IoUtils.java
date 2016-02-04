package com.nostra13.universalimageloader.utils;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class IoUtils {

    public interface CopyListener {
        boolean onBytesCopied(int i, int i2);
    }

    public static boolean copyStream$6bf5717a(InputStream is, OutputStream os, CopyListener listener) throws IOException {
        int current = 0;
        int total = is.available();
        byte[] bytes = new byte[AccessibilityNodeInfoCompat.ACTION_PASTE];
        if (shouldStopLoading(listener, 0, total)) {
            return false;
        }
        do {
            int count = is.read(bytes, 0, AccessibilityNodeInfoCompat.ACTION_PASTE);
            if (count == -1) {
                return true;
            }
            os.write(bytes, 0, count);
            current += count;
        } while (!shouldStopLoading(listener, current, total));
        return false;
    }

    private static boolean shouldStopLoading(CopyListener listener, int current, int total) {
        if (listener == null || listener.onBytesCopied(current, total) || (current * 100) / total >= 75) {
            return false;
        }
        return true;
    }

    public static void closeSilently(Closeable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
        }
    }
}

package com.nostra13.universalimageloader.utils;

import android.util.Log;
import com.nostra13.universalimageloader.core.ImageLoader;

/* renamed from: com.nostra13.universalimageloader.utils.L */
public final class C0496L {
    private static volatile boolean DISABLED;

    static {
        DISABLED = false;
    }

    public static void m29d(String message, Object... args) {
        C0496L.log(3, null, message, args);
    }

    public static void m31e(Throwable ex) {
        C0496L.log(6, ex, null, new Object[0]);
    }

    public static void m30e(String message, Object... args) {
        C0496L.log(6, null, message, args);
    }

    private static void log(int priority, Throwable ex, String message, Object... args) {
        if (!DISABLED) {
            String log;
            if (args.length > 0) {
                message = String.format(message, args);
            }
            if (ex == null) {
                log = message;
            } else {
                String logMessage;
                if (message == null) {
                    logMessage = ex.getMessage();
                } else {
                    logMessage = message;
                }
                String logBody = Log.getStackTraceString(ex);
                log = String.format("%1$s\n%2$s", new Object[]{logMessage, logBody});
            }
            Log.println(priority, ImageLoader.TAG, log);
        }
    }
}

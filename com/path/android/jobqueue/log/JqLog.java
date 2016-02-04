package com.path.android.jobqueue.log;

public final class JqLog {
    private static CustomLogger customLogger;

    /* renamed from: com.path.android.jobqueue.log.JqLog.1 */
    static class C05001 implements CustomLogger {
        C05001() {
        }

        public final boolean isDebugEnabled() {
            return false;
        }

        public final void m35d(String text, Object... args) {
        }

        public final void m37e(Throwable t, String text, Object... args) {
        }

        public final void m36e(String text, Object... args) {
        }
    }

    static {
        customLogger = new C05001();
    }

    public static void setCustomLogger(CustomLogger customLogger) {
        customLogger = customLogger;
    }

    public static boolean isDebugEnabled() {
        return customLogger.isDebugEnabled();
    }

    public static void m38d(String text, Object... args) {
        customLogger.m32d(text, args);
    }

    public static void m40e(Throwable t, String text, Object... args) {
        customLogger.m34e(t, text, args);
    }

    public static void m39e(String text, Object... args) {
        customLogger.m33e(text, args);
    }
}

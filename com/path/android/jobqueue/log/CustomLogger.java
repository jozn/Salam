package com.path.android.jobqueue.log;

public interface CustomLogger {
    void m32d(String str, Object... objArr);

    void m33e(String str, Object... objArr);

    void m34e(Throwable th, String str, Object... objArr);

    boolean isDebugEnabled();
}

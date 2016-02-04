package com.arellomobile.android.push.utils.executor;

import android.os.AsyncTask;
import android.os.Build.VERSION;

public final class ExecutorHelper {
    public static void executeAsyncTask(AsyncTask<Void, Void, Void> asyncTask) {
        if (asyncTask == null) {
            return;
        }
        if (VERSION.SDK_INT >= 11) {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null});
            return;
        }
        asyncTask.execute(new Void[]{null});
    }
}

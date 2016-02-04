package com.arellomobile.android.push.utils;

import android.content.Context;
import android.os.AsyncTask;

public abstract class WorkerTask extends AsyncTask<Void, Void, Void> {
    private Context mContext;

    public WorkerTask(Context context) {
        this.mContext = context;
    }

    private Void doInBackground$10299ca() {
        try {
            doWork(this.mContext);
        } catch (Throwable th) {
        } finally {
            this.mContext = null;
        }
        return null;
    }

    protected /* bridge */ /* synthetic */ Object doInBackground(Object[] objArr) {
        return doInBackground$10299ca();
    }

    public abstract void doWork(Context context) throws Exception;
}

package com.appaholics.updatechecker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public final class UpdateChecker {
    public String TAG;
    public DownloadManager downloadManager;
    public boolean haveValidContext;
    public Context mContext;
    public boolean updateAvailable;
    public boolean useToasts;

    public UpdateChecker(Context context) {
        this.TAG = "UpdateChecker";
        this.updateAvailable = false;
        this.haveValidContext = false;
        this.useToasts = false;
        this.mContext = context;
        if (this.mContext != null) {
            this.haveValidContext = true;
            this.useToasts = true;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final int getVersionCode() {
        /*
        r3 = this;
        r0 = r3.mContext;	 Catch:{ NameNotFoundException -> 0x0014, NullPointerException -> 0x001e }
        r0 = r0.getPackageManager();	 Catch:{ NameNotFoundException -> 0x0014, NullPointerException -> 0x001e }
        r1 = r3.mContext;	 Catch:{ NameNotFoundException -> 0x0014, NullPointerException -> 0x001e }
        r1 = r1.getPackageName();	 Catch:{ NameNotFoundException -> 0x0014, NullPointerException -> 0x001e }
        r2 = 0;
        r0 = r0.getPackageInfo(r1, r2);	 Catch:{ NameNotFoundException -> 0x0014, NullPointerException -> 0x001e }
        r0 = r0.versionCode;	 Catch:{ NameNotFoundException -> 0x0014, NullPointerException -> 0x001e }
    L_0x0013:
        return r0;
    L_0x0014:
        r0 = move-exception;
        r0 = r3.TAG;
        r1 = "Version Code not available";
        android.util.Log.e(r0, r1);
    L_0x001c:
        r0 = -1;
        goto L_0x0013;
    L_0x001e:
        r0 = move-exception;
        r0 = r3.TAG;
        r1 = "Context is null";
        android.util.Log.e(r0, r1);
        goto L_0x001c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.appaholics.updatechecker.UpdateChecker.getVersionCode():int");
    }

    public final boolean isOnline() {
        if (!this.haveValidContext) {
            return false;
        }
        try {
            return ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressLint({"ShowToast"})
    public final Toast makeToastFromString(String text) {
        return Toast.makeText(this.mContext, text, 0);
    }

    public final String readFile(String url) {
        try {
            return new BufferedReader(new InputStreamReader(new URL(url).openStream())).readLine();
        } catch (MalformedURLException e) {
            Log.e(this.TAG, "Invalid URL");
            Log.e(this.TAG, "There was an error reading the file");
            return "Problem reading the file";
        } catch (IOException e2) {
            Log.e(this.TAG, "There was an IO exception");
            Log.e(this.TAG, "There was an error reading the file");
            return "Problem reading the file";
        }
    }
}

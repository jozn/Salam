package com.commonsware.cwac.updater;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleHttpDownloadStrategy implements DownloadStrategy {
    public static final Creator<SimpleHttpDownloadStrategy> CREATOR;

    /* renamed from: com.commonsware.cwac.updater.SimpleHttpDownloadStrategy.1 */
    static class C02351 implements Creator<SimpleHttpDownloadStrategy> {
        C02351() {
        }

        public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
            return new SimpleHttpDownloadStrategy();
        }

        public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
            return new SimpleHttpDownloadStrategy[i];
        }
    }

    public final Uri downloadAPK(Context ctxt, String url) throws Exception {
        File apk = getDownloadFile(ctxt);
        if (apk.exists()) {
            apk.delete();
        }
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        try {
            conn.connect();
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                OutputStream f = openDownloadFile(ctxt, apk);
                byte[] buffer = new byte[AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD];
                while (true) {
                    int len1 = is.read(buffer);
                    if (len1 <= 0) {
                        break;
                    }
                    f.write(buffer, 0, len1);
                }
                f.close();
                is.close();
                return Uri.fromFile(apk);
            }
            throw new RuntimeException(String.format("Received %d from server", new Object[]{Integer.valueOf(conn.getResponseCode())}));
        } finally {
            conn.disconnect();
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
    }

    protected File getDownloadFile(Context ctxt) {
        File updateDir = new File(ctxt.getExternalFilesDir(null), ".CWAC-Update");
        updateDir.mkdirs();
        return new File(updateDir, "update.apk");
    }

    protected OutputStream openDownloadFile(Context ctxt, File apk) throws FileNotFoundException {
        return new FileOutputStream(apk);
    }

    static {
        CREATOR = new C02351();
    }
}

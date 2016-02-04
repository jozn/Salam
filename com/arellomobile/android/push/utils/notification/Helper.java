package com.arellomobile.android.push.utils.notification;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public final class Helper {
    public static Bitmap tryToGetBitmapFromInternet(String str, Context context, int i) {
        Throwable th;
        Bitmap bitmap = null;
        if (str != null) {
            InputStream inputStream;
            try {
                URLConnection openConnection = new URL(str).openConnection();
                openConnection.connect();
                inputStream = openConnection.getInputStream();
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] bArr = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
                    while (true) {
                        int read = inputStream.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        byteArrayOutputStream.write(bArr, 0, read);
                    }
                    inputStream.close();
                    byteArrayOutputStream.close();
                    bArr = byteArrayOutputStream.toByteArray();
                    Options options = new Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
                    int max = Math.max(options.outWidth, options.outHeight);
                    float f = 1.0f;
                    if (-1 != i) {
                        f = ((float) max) / (((float) i) * context.getResources().getDisplayMetrics().density);
                    }
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = Math.round(f);
                    bitmap = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                        }
                    }
                    System.gc();
                } catch (Throwable th2) {
                    th = th2;
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e2) {
                        }
                    }
                    System.gc();
                    throw th;
                }
            } catch (Throwable th3) {
                Throwable th4 = th3;
                inputStream = null;
                th = th4;
                if (inputStream != null) {
                    inputStream.close();
                }
                System.gc();
                throw th;
            }
        }
        return bitmap;
    }

    public static int tryToGetIconFormStringOrGetFromApplication(String str, Context context) {
        int i = context.getApplicationInfo().icon;
        if (str != null) {
            int identifier = context.getResources().getIdentifier(str, "drawable", context.getPackageName());
            if (identifier != 0) {
                return identifier;
            }
        }
        return i;
    }
}

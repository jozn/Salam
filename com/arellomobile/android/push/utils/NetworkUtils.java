package com.arellomobile.android.push.utils;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import org.json.JSONObject;

public final class NetworkUtils {
    public static boolean useSSL;

    public static class NetworkResult {
        public int mPushwooshCode;
        public int mResultCode;
        public JSONObject mResultData;

        public NetworkResult() {
            this.mResultCode = 500;
            this.mPushwooshCode = 0;
            this.mResultData = null;
        }
    }

    static {
        useSSL = false;
    }

    public static NetworkResult makeRequest(Map<String, Object> map, String str) throws Exception {
        InputStream bufferedInputStream;
        Throwable th;
        OutputStream outputStream = null;
        NetworkResult networkResult = new NetworkResult();
        try {
            String str2 = "http://cp.pushwoosh.com/json/1.3/" + str;
            if (useSSL) {
                str2 = "https://cp.pushwoosh.com/json/1.3/" + str;
            }
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str2).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            httpURLConnection.setDoOutput(true);
            JSONObject jSONObject = new JSONObject();
            for (String str3 : map.keySet()) {
                jSONObject.put(str3, map.get(str3));
            }
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("request", jSONObject);
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(jSONObject2.toString().getBytes().length));
            OutputStream outputStream2 = httpURLConnection.getOutputStream();
            try {
                outputStream2.write(jSONObject2.toString().getBytes());
                outputStream2.flush();
                outputStream2.close();
                bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] bArr = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
                    while (true) {
                        int read = bufferedInputStream.read(bArr);
                        if (read < 0) {
                            break;
                        }
                        byteArrayOutputStream.write(bArr, 0, read);
                    }
                    byteArrayOutputStream.close();
                    String trim = new String(byteArrayOutputStream.toByteArray()).trim();
                    Log.w("PushWoosh: NetworkUtils", "PushWooshResult: " + trim);
                    JSONObject jSONObject3 = new JSONObject(trim);
                    networkResult.mResultData = jSONObject3;
                    networkResult.mResultCode = httpURLConnection.getResponseCode();
                    networkResult.mPushwooshCode = jSONObject3.getInt("status_code");
                    bufferedInputStream.close();
                    if (outputStream2 != null) {
                        outputStream2.close();
                    }
                    return networkResult;
                } catch (Throwable th2) {
                    th = th2;
                    outputStream = outputStream2;
                    if (bufferedInputStream != null) {
                        bufferedInputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedInputStream = null;
                outputStream = outputStream2;
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            bufferedInputStream = null;
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            throw th;
        }
    }
}

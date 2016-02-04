package com.shamchat.map;

import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONException;
import org.json.JSONObject;

public final class Parser_Json {
    public static JSONObject getJSONfromURL(String url) {
        InputStream is = null;
        String result = BuildConfig.VERSION_NAME;
        try {
            is = new DefaultHttpClient().execute(new HttpPost(url)).getEntity().getContent();
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e2) {
            Log.e("log_tag", "Error converting result " + e2.toString());
        }
        try {
            return new JSONObject(result);
        } catch (JSONException e3) {
            Log.e("log_tag", "Error parsing data " + e3.toString());
            return null;
        }
    }
}

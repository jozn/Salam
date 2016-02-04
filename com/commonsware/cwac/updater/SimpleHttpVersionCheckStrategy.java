package com.commonsware.cwac.updater;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class SimpleHttpVersionCheckStrategy implements VersionCheckStrategy {
    public static final Creator<SimpleHttpVersionCheckStrategy> CREATOR;
    protected String updateURL;
    protected String url;

    /* renamed from: com.commonsware.cwac.updater.SimpleHttpVersionCheckStrategy.1 */
    static class C02361 implements Creator<SimpleHttpVersionCheckStrategy> {
        C02361() {
        }

        public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
            return new SimpleHttpVersionCheckStrategy((byte) 0);
        }

        public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
            return new SimpleHttpVersionCheckStrategy[i];
        }
    }

    private SimpleHttpVersionCheckStrategy(Parcel in) {
        this.url = null;
        this.updateURL = null;
        this.url = in.readString();
    }

    public final int getVersionCode() throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(this.url).openConnection();
        try {
            conn.connect();
            if (conn.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder buf = new StringBuilder();
                while (true) {
                    String str = in.readLine();
                    if (str == null) {
                        break;
                    }
                    buf.append(str);
                    buf.append('\n');
                }
                in.close();
                JSONObject json = new JSONObject(buf.toString());
                int result = json.getInt("versionCode");
                this.updateURL = json.getString("updateURL");
                return result;
            }
            throw new RuntimeException(String.format("Received %d from server", new Object[]{Integer.valueOf(conn.getResponseCode())}));
        } finally {
            conn.disconnect();
        }
    }

    public final String getUpdateURL() {
        return this.updateURL;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
    }

    static {
        CREATOR = new C02361();
    }
}

package com.google.android.gms.playlog.internal;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class LogEvent implements SafeParcelable {
    public static final zzc CREATOR;
    public final String tag;
    public final int versionCode;
    public final long zzaYn;
    public final long zzaYo;
    public final byte[] zzaYp;
    public final Bundle zzaYq;

    static {
        CREATOR = new zzc();
    }

    LogEvent(int versionCode, long eventTime, long eventUptime, String tag, byte[] sourceExtensionBytes, Bundle keyValuePairs) {
        this.versionCode = versionCode;
        this.zzaYn = eventTime;
        this.zzaYo = eventUptime;
        this.tag = tag;
        this.zzaYp = sourceExtensionBytes;
        this.zzaYq = keyValuePairs;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("tag=").append(this.tag).append(",");
        stringBuilder.append("eventTime=").append(this.zzaYn).append(",");
        stringBuilder.append("eventUptime=").append(this.zzaYo).append(",");
        if (!(this.zzaYq == null || this.zzaYq.isEmpty())) {
            stringBuilder.append("keyValues=");
            for (String str : this.zzaYq.keySet()) {
                stringBuilder.append("(").append(str).append(",");
                stringBuilder.append(this.zzaYq.getString(str)).append(")");
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        zzc.zza$298c9f3b(this, out);
    }
}

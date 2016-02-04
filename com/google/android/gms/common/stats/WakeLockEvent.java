package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v7.appcompat.BuildConfig;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.List;

public final class WakeLockEvent extends zzf implements SafeParcelable {
    public static final Creator<WakeLockEvent> CREATOR;
    final long mTimeout;
    final int mVersionCode;
    final String zzalZ;
    final long zzaln;
    int zzalo;
    final long zzalv;
    private long zzalx;
    final int zzama;
    final List<String> zzamb;
    final String zzamc;
    int zzamd;
    final String zzame;
    final String zzamf;
    final float zzamg;

    static {
        CREATOR = new zzh();
    }

    WakeLockEvent(int versionCode, long timeMillis, int eventType, String wakelockName, int wakelockType, List<String> callingPackages, String eventKey, long elapsedRealtime, int deviceState, String secondaryWakeLockName, String hostPackageName, float beginPowerPercentage, long timeout) {
        this.mVersionCode = versionCode;
        this.zzaln = timeMillis;
        this.zzalo = eventType;
        this.zzalZ = wakelockName;
        this.zzame = secondaryWakeLockName;
        this.zzama = wakelockType;
        this.zzalx = -1;
        this.zzamb = callingPackages;
        this.zzamc = eventKey;
        this.zzalv = elapsedRealtime;
        this.zzamd = deviceState;
        this.zzamf = hostPackageName;
        this.zzamg = beginPowerPercentage;
        this.mTimeout = timeout;
    }

    public final int describeContents() {
        return 0;
    }

    public final int getEventType() {
        return this.zzalo;
    }

    public final long getTimeMillis() {
        return this.zzaln;
    }

    public final void writeToParcel(Parcel out, int flags) {
        zzh.zza$7ab08521(this, out);
    }

    public final long zzrv() {
        return this.zzalx;
    }

    public final String zzry() {
        return "\t" + this.zzalZ + "\t" + this.zzama + "\t" + (this.zzamb == null ? BuildConfig.VERSION_NAME : TextUtils.join(",", this.zzamb)) + "\t" + this.zzamd + "\t" + (this.zzame == null ? BuildConfig.VERSION_NAME : this.zzame) + "\t" + (this.zzamf == null ? BuildConfig.VERSION_NAME : this.zzamf) + "\t" + this.zzamg;
    }
}

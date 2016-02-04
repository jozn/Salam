package com.google.android.gms.clearcut;

import android.os.Parcel;
import com.google.android.gms.clearcut.zza.zzb;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzv;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.internal.zztp.zzd;
import com.google.android.gms.playlog.internal.PlayLoggerContext;
import java.util.Arrays;

public class LogEventParcelable implements SafeParcelable {
    public static final zzc CREATOR;
    public final int versionCode;
    public final zzd zzadA;
    public final zzb zzadB;
    public final zzb zzadC;
    public PlayLoggerContext zzadx;
    public byte[] zzady;
    public int[] zzadz;

    static {
        CREATOR = new zzc();
    }

    LogEventParcelable(int versionCode, PlayLoggerContext playLoggerContext, byte[] logEventBytes, int[] testCodes) {
        this.versionCode = versionCode;
        this.zzadx = playLoggerContext;
        this.zzady = logEventBytes;
        this.zzadz = testCodes;
        this.zzadA = null;
        this.zzadB = null;
        this.zzadC = null;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof LogEventParcelable)) {
            return false;
        }
        LogEventParcelable logEventParcelable = (LogEventParcelable) other;
        return this.versionCode == logEventParcelable.versionCode && zzw.equal(this.zzadx, logEventParcelable.zzadx) && Arrays.equals(this.zzady, logEventParcelable.zzady) && Arrays.equals(this.zzadz, logEventParcelable.zzadz) && zzw.equal(this.zzadA, logEventParcelable.zzadA) && zzw.equal(this.zzadB, logEventParcelable.zzadB) && zzw.equal(this.zzadC, logEventParcelable.zzadC);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.versionCode), this.zzadx, this.zzady, this.zzadz, this.zzadA, this.zzadB, this.zzadC});
    }

    public String toString() {
        String str = null;
        StringBuilder stringBuilder = new StringBuilder("LogEventParcelable[");
        stringBuilder.append(this.versionCode);
        stringBuilder.append(", ");
        stringBuilder.append(this.zzadx);
        stringBuilder.append(", ");
        stringBuilder.append(this.zzady == null ? null : new String(this.zzady));
        stringBuilder.append(", ");
        if (this.zzadz != null) {
            str = new zzv(", ").zza(new StringBuilder(), Arrays.asList(new int[][]{this.zzadz})).toString();
        }
        stringBuilder.append(str);
        stringBuilder.append(", ");
        stringBuilder.append(this.zzadA);
        stringBuilder.append(", ");
        stringBuilder.append(this.zzadB);
        stringBuilder.append(", ");
        stringBuilder.append(this.zzadC);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        zzc.zza(this, out, flags);
    }
}

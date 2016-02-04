package com.google.android.gms.auth.firstparty.shared;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class FACLData implements SafeParcelable {
    public static final zzb CREATOR;
    final int version;
    FACLConfig zzWN;
    String zzWO;
    boolean zzWP;
    String zzWQ;

    static {
        CREATOR = new zzb();
    }

    FACLData(int version, FACLConfig faclConfig, String activityText, boolean isSpeedbumpNeeded, String speedbumpText) {
        this.version = version;
        this.zzWN = faclConfig;
        this.zzWO = activityText;
        this.zzWP = isSpeedbumpNeeded;
        this.zzWQ = speedbumpText;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzb.zza(this, dest, flags);
    }
}

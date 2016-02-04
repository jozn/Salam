package com.google.android.gms.auth.firstparty.shared;

import android.os.Parcel;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.Arrays;

public class FACLConfig implements SafeParcelable {
    public static final zza CREATOR;
    final int version;
    boolean zzWH;
    String zzWI;
    boolean zzWJ;
    boolean zzWK;
    boolean zzWL;
    boolean zzWM;

    static {
        CREATOR = new zza();
    }

    FACLConfig(int version, boolean isAllCirclesVisible, String visibleEdges, boolean isAllContactsVisible, boolean showCircles, boolean showContacts, boolean hasShowCircles) {
        this.version = version;
        this.zzWH = isAllCirclesVisible;
        this.zzWI = visibleEdges;
        this.zzWJ = isAllContactsVisible;
        this.zzWK = showCircles;
        this.zzWL = showContacts;
        this.zzWM = hasShowCircles;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (!(o instanceof FACLConfig)) {
            return false;
        }
        FACLConfig fACLConfig = (FACLConfig) o;
        return this.zzWH == fACLConfig.zzWH && TextUtils.equals(this.zzWI, fACLConfig.zzWI) && this.zzWJ == fACLConfig.zzWJ && this.zzWK == fACLConfig.zzWK && this.zzWL == fACLConfig.zzWL && this.zzWM == fACLConfig.zzWM;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Boolean.valueOf(this.zzWH), this.zzWI, Boolean.valueOf(this.zzWJ), Boolean.valueOf(this.zzWK), Boolean.valueOf(this.zzWL), Boolean.valueOf(this.zzWM)});
    }

    public void writeToParcel(Parcel dest, int flags) {
        zza.zza$5331385a(this, dest);
    }
}

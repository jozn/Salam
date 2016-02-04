package com.google.android.gms.auth.firstparty.shared;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.List;

public class ScopeDetail implements SafeParcelable {
    public static final zzc CREATOR;
    String description;
    final int version;
    String zzWR;
    String zzWS;
    String zzWT;
    String zzWU;
    List<String> zzWV;
    public FACLData zzWW;

    static {
        CREATOR = new zzc();
    }

    ScopeDetail(int version, String description, String detail, String iconBase64, String paclPickerDataBase64, String service, List<String> warnings, FACLData friendPickerData) {
        this.version = version;
        this.description = description;
        this.zzWR = detail;
        this.zzWS = iconBase64;
        this.zzWT = paclPickerDataBase64;
        this.zzWU = service;
        this.zzWV = warnings;
        this.zzWW = friendPickerData;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzc.zza(this, dest, flags);
    }
}

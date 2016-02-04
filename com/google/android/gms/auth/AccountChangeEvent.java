package com.google.android.gms.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;
import java.util.Arrays;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class AccountChangeEvent implements SafeParcelable {
    public static final Creator<AccountChangeEvent> CREATOR;
    final int mVersion;
    final long zzTC;
    final String zzTD;
    final int zzTE;
    final int zzTF;
    final String zzTG;

    static {
        CREATOR = new zza();
    }

    AccountChangeEvent(int version, long id, String accountName, int changeType, int eventIndex, String changeData) {
        this.mVersion = version;
        this.zzTC = id;
        this.zzTD = (String) zzx.zzy(accountName);
        this.zzTE = changeType;
        this.zzTF = eventIndex;
        this.zzTG = changeData;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object that) {
        if (that == this) {
            return true;
        }
        if (!(that instanceof AccountChangeEvent)) {
            return false;
        }
        AccountChangeEvent accountChangeEvent = (AccountChangeEvent) that;
        return this.mVersion == accountChangeEvent.mVersion && this.zzTC == accountChangeEvent.zzTC && zzw.equal(this.zzTD, accountChangeEvent.zzTD) && this.zzTE == accountChangeEvent.zzTE && this.zzTF == accountChangeEvent.zzTF && zzw.equal(this.zzTG, accountChangeEvent.zzTG);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.mVersion), Long.valueOf(this.zzTC), this.zzTD, Integer.valueOf(this.zzTE), Integer.valueOf(this.zzTF), this.zzTG});
    }

    public String toString() {
        String str = "UNKNOWN";
        switch (this.zzTE) {
            case Logger.SEVERE /*1*/:
                str = "ADDED";
                break;
            case Logger.WARNING /*2*/:
                str = "REMOVED";
                break;
            case Logger.INFO /*3*/:
                str = "RENAMED_FROM";
                break;
            case Logger.CONFIG /*4*/:
                str = "RENAMED_TO";
                break;
        }
        return "AccountChangeEvent {accountName = " + this.zzTD + ", changeType = " + str + ", changeData = " + this.zzTG + ", eventIndex = " + this.zzTF + "}";
    }

    public void writeToParcel(Parcel dest, int flags) {
        zza.zza$119e69c0(this, dest);
    }
}

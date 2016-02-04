package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class Tile implements SafeParcelable {
    public static final zzn CREATOR;
    public final byte[] data;
    public final int height;
    final int mVersionCode;
    public final int width;

    static {
        CREATOR = new zzn();
    }

    public Tile() {
        this(1, -1, -1, null);
    }

    Tile(int versionCode, int width, int height, byte[] data) {
        this.mVersionCode = versionCode;
        this.width = width;
        this.height = height;
        this.data = data;
    }

    public final int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel out, int flags) {
        zzn.zza$2bae1718(this, out);
    }
}

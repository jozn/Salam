package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class BinderWrapper implements Parcelable {
    public static final Creator<BinderWrapper> CREATOR;
    private IBinder zzaiT;

    /* renamed from: com.google.android.gms.common.internal.BinderWrapper.1 */
    static class C02411 implements Creator<BinderWrapper> {
        C02411() {
        }

        public final /* synthetic */ Object createFromParcel(Parcel x0) {
            return new BinderWrapper((byte) 0);
        }

        public final /* synthetic */ Object[] newArray(int x0) {
            return new BinderWrapper[x0];
        }
    }

    static {
        CREATOR = new C02411();
    }

    public BinderWrapper() {
        this.zzaiT = null;
    }

    private BinderWrapper(Parcel in) {
        this.zzaiT = null;
        this.zzaiT = in.readStrongBinder();
    }

    public final int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel dest, int flags) {
        dest.writeStrongBinder(this.zzaiT);
    }
}

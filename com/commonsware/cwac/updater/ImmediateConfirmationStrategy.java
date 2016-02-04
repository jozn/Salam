package com.commonsware.cwac.updater;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class ImmediateConfirmationStrategy implements ConfirmationStrategy {
    public static final Creator<ImmediateConfirmationStrategy> CREATOR;

    /* renamed from: com.commonsware.cwac.updater.ImmediateConfirmationStrategy.1 */
    static class C02331 implements Creator<ImmediateConfirmationStrategy> {
        C02331() {
        }

        public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
            return new ImmediateConfirmationStrategy();
        }

        public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
            return new ImmediateConfirmationStrategy[i];
        }
    }

    public final boolean confirm(Context ctxt, PendingIntent contentIntent) {
        return true;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
    }

    static {
        CREATOR = new C02331();
    }
}

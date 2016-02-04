package com.commonsware.cwac.updater;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class NotificationConfirmationStrategy implements ConfirmationStrategy {
    public static final Creator<NotificationConfirmationStrategy> CREATOR;
    private Notification notification;

    /* renamed from: com.commonsware.cwac.updater.NotificationConfirmationStrategy.1 */
    static class C02341 implements Creator<NotificationConfirmationStrategy> {
        C02341() {
        }

        public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
            return new NotificationConfirmationStrategy((byte) 0);
        }

        public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
            return new NotificationConfirmationStrategy[i];
        }
    }

    private NotificationConfirmationStrategy(Parcel in) {
        this.notification = null;
        this.notification = (Notification) in.readParcelable(null);
    }

    public final boolean confirm(Context ctxt, PendingIntent contentIntent) {
        NotificationManager mgr = (NotificationManager) ctxt.getSystemService("notification");
        this.notification.contentIntent = contentIntent;
        mgr.notify(99369921, this.notification);
        return false;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.notification, 0);
    }

    static {
        CREATOR = new C02341();
    }
}

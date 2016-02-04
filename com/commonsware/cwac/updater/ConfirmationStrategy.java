package com.commonsware.cwac.updater;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Parcelable;

public interface ConfirmationStrategy extends Parcelable {
    boolean confirm(Context context, PendingIntent pendingIntent);
}

package com.commonsware.cwac.updater;

import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;

public interface DownloadStrategy extends Parcelable {
    Uri downloadAPK(Context context, String str) throws Exception;
}

package com.commonsware.cwac.updater;

import android.os.Parcelable;

public interface VersionCheckStrategy extends Parcelable {
    String getUpdateURL() throws Exception;

    int getVersionCode() throws Exception;
}

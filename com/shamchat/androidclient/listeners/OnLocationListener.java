package com.shamchat.androidclient.listeners;

import android.location.Location;

public interface OnLocationListener {
    void onAddressReceived(Location location, String str);
}

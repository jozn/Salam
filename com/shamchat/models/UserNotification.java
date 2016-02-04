package com.shamchat.models;

import com.shamchat.androidclient.util.PreferenceConstants.AllowDeniedStatus;
import com.shamchat.androidclient.util.PreferenceConstants.EnableDisableStatus;
import com.shamchat.androidclient.util.PreferenceConstants.FeatureAlertStatus;

public final class UserNotification {
    public AllowDeniedStatus messageAlertStatus;
    public EnableDisableStatus movementUpdateStatus;
    public String notificationSound;
    public String notificationTimingEnd;
    public String notificationTimingStart;
    public FeatureAlertStatus otherFeatureAlertStatus;
    public EnableDisableStatus soundAlertStatus;
    public String userId;
    public int userNotificationId;
    public EnableDisableStatus vibrateStatus;

    public final String toString() {
        return String.valueOf(this.userNotificationId);
    }
}

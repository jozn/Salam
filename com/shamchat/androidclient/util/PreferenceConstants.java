package com.shamchat.androidclient.util;

public final class PreferenceConstants {
    public static boolean CONNECTED_TO_NETWORK;
    public static String CONTACT_LAST_COUNT;
    public static String CONTACT_LAST_ID;
    public static String FORGOT_PASSWORD;
    public static String INITIAL_CONTACT_SYNC;
    public static String INITIAL_LOGIN;
    public static String IS_OLD_USER;
    public static String LIKES_AND_COMMENTS_LAST_SYNC_TIME;
    public static String MOMENT_LAST_SYNC_TIME;
    public static String STATUS;
    public static String SUCCESS;
    public static String UPDATE_CHECK_COUNTER;

    public enum AllowDeniedStatus {
        ALLOW(1),
        DENIED(0);
        
        public int status;

        private AllowDeniedStatus(int status) {
            this.status = 0;
            this.status = status;
        }
    }

    public enum EnableDisableStatus {
        ENABLE(1),
        DISABLE(0);
        
        public int status;

        private EnableDisableStatus(int status) {
            this.status = 0;
            this.status = status;
        }
    }

    public enum FeatureAlertStatus {
        NIGHT_MODE(2),
        ENABLE(1),
        DISABLE(0);
        
        public int status;

        private FeatureAlertStatus(int status) {
            this.status = 0;
            this.status = status;
        }
    }

    static {
        INITIAL_CONTACT_SYNC = "initial_contact_sync";
        CONTACT_LAST_ID = "contact_last_id";
        CONTACT_LAST_COUNT = "contact_last_count";
        UPDATE_CHECK_COUNTER = "update_check_counter";
        IS_OLD_USER = "is_new_user";
        MOMENT_LAST_SYNC_TIME = "moment_last_sync_time";
        LIKES_AND_COMMENTS_LAST_SYNC_TIME = "likes_comments_last_sync_time";
        CONNECTED_TO_NETWORK = true;
        INITIAL_LOGIN = "initial_login";
        FORGOT_PASSWORD = "forgot";
        STATUS = NotificationCompatApi21.CATEGORY_STATUS;
        SUCCESS = "success";
    }
}

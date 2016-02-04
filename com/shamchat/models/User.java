package com.shamchat.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v7.appcompat.BuildConfig;
import android.util.Base64;
import com.shamchat.utils.Utils;
import java.text.Collator;
import org.json.JSONObject;

public class User implements Parcelable, Comparable<User> {
    public static final Creator<User> CREATOR;
    private String NULL;
    public String chatId;
    public boolean checked;
    public String cityOrRegion;
    public String coverPhoto;
    public String dbRowId;
    public String email;
    public String emailVerificationStatus;
    public BooleanStatus findMeByPhoneNo;
    public BooleanStatus findMeByShamId;
    public String gender;
    public String inAppAlert;
    public boolean isAddedToRoster;
    public boolean isBlocked;
    public BooleanStatus isInChat;
    private Boolean isMentionChecked;
    public boolean isVCardDownloaded;
    public String jabberdResource;
    public String mobileNo;
    public String myStatus;
    public String newMessageAlert;
    public int onlineStatus;
    public String profileImage;
    public String profileImageUrl;
    public boolean shamMyContactUser;
    public int statusMode;
    public boolean takenFromPhoneContacts;
    public String tmpUserId;
    public String userId;
    public String username;

    /* renamed from: com.shamchat.models.User.1 */
    class C11011 extends Thread {
        C11011() {
        }

        public final void run() {
            if (!(User.this.profileImageUrl == null || User.this.profileImageUrl.length() <= 0 || User.this.profileImageUrl.equalsIgnoreCase(MqttServiceConstants.TRACE_EXCEPTION) || User.this.profileImageUrl.equalsIgnoreCase("null"))) {
                Utils utils = new Utils();
                byte[] blobMessage = Utils.downloadImageFromUrl(User.this.profileImageUrl);
                if (blobMessage != null) {
                    User.this.profileImage = Base64.encodeToString(blobMessage, 0);
                }
            }
            super.run();
        }
    }

    /* renamed from: com.shamchat.models.User.2 */
    static class C11022 implements Creator<User> {
        C11022() {
        }

        public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
            return new User((byte) 0);
        }

        public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
            return new User[i];
        }
    }

    public enum BooleanStatus {
        FALSE(0),
        TRUE(1);
        
        public int status;

        private BooleanStatus(int status) {
            this.status = status;
        }
    }

    public /* bridge */ /* synthetic */ int compareTo(Object obj) {
        return Collator.getInstance().compare(this.username.toUpperCase(), ((User) obj).username.toUpperCase());
    }

    public User() {
        this.isMentionChecked = Boolean.valueOf(false);
        this.NULL = "null";
        this.takenFromPhoneContacts = false;
        this.shamMyContactUser = false;
    }

    public User(JSONObject userJsonObject) throws Exception {
        this.isMentionChecked = Boolean.valueOf(false);
        this.NULL = "null";
        this.takenFromPhoneContacts = false;
        this.shamMyContactUser = false;
        if (userJsonObject.has("userId")) {
            this.userId = userJsonObject.getInt("userId");
        }
        if (userJsonObject.has("name")) {
            this.username = !userJsonObject.getString("name").equals(this.NULL) ? userJsonObject.getString("name") : BuildConfig.VERSION_NAME;
        }
        if (userJsonObject.has("chatId")) {
            this.chatId = !userJsonObject.getString("chatId").equals(this.NULL) ? userJsonObject.getString("chatId") : BuildConfig.VERSION_NAME;
        }
        if (userJsonObject.has("mobileNo")) {
            this.mobileNo = !userJsonObject.getString("mobileNo").equals(this.NULL) ? userJsonObject.getString("mobileNo") : BuildConfig.VERSION_NAME;
        }
        if (userJsonObject.has(NotificationCompatApi21.CATEGORY_EMAIL)) {
            this.email = !userJsonObject.getString(NotificationCompatApi21.CATEGORY_EMAIL).equals(this.NULL) ? userJsonObject.getString(NotificationCompatApi21.CATEGORY_EMAIL) : BuildConfig.VERSION_NAME;
        }
        if (userJsonObject.has("profileImageUrl")) {
            this.profileImage = BuildConfig.VERSION_NAME;
            this.profileImageUrl = !userJsonObject.getString("profileImageUrl").equals(this.NULL) ? userJsonObject.getString("profileImageUrl") : BuildConfig.VERSION_NAME;
            Thread imageDownloader = new C11011();
            imageDownloader.start();
            imageDownloader.join();
        }
        if (userJsonObject.has("tempUserId")) {
            this.tmpUserId = !userJsonObject.getString("tempUserId").equals(this.NULL) ? userJsonObject.getString("tempUserId") : BuildConfig.VERSION_NAME;
        }
        if (userJsonObject.has("inAppAlert")) {
            this.inAppAlert = !userJsonObject.getString("inAppAlert").equals(this.NULL) ? userJsonObject.getString("inAppAlert") : BuildConfig.VERSION_NAME;
        }
        if (userJsonObject.has("emailVerificationStatus")) {
            this.emailVerificationStatus = !userJsonObject.getString("emailVerificationStatus").equals(this.NULL) ? userJsonObject.getString("emailVerificationStatus") : BuildConfig.VERSION_NAME;
        }
        if (userJsonObject.has("newMessageAlert")) {
            this.newMessageAlert = !userJsonObject.getString("newMessageAlert").equals(this.NULL) ? userJsonObject.getString("newMessageAlert") : BuildConfig.VERSION_NAME;
        }
        if (userJsonObject.has("gender")) {
            this.gender = !userJsonObject.getString("gender").equals(this.NULL) ? userJsonObject.getString("gender") : BuildConfig.VERSION_NAME;
        }
        if (userJsonObject.has("myStatus")) {
            this.myStatus = !userJsonObject.getString("myStatus").equals(this.NULL) ? userJsonObject.getString("myStatus") : BuildConfig.VERSION_NAME;
            System.out.println("User status in user class " + this.myStatus);
        }
        if (userJsonObject.has("region")) {
            this.cityOrRegion = !userJsonObject.getString("region").equals(this.NULL) ? userJsonObject.getString("region") : BuildConfig.VERSION_NAME;
        }
        this.isInChat = BooleanStatus.TRUE;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User other = (User) o;
        if (this.userId.length() == 0 && other.userId.length() == 0) {
            return false;
        }
        return other.userId.equals(this.userId);
    }

    public String toString() {
        return "Id:" + this.userId + " Name:" + this.username;
    }

    private User(Parcel in) {
        this.isMentionChecked = Boolean.valueOf(false);
        this.NULL = "null";
        this.takenFromPhoneContacts = false;
        this.shamMyContactUser = false;
        this.userId = in.readString();
        this.username = in.readString();
        this.chatId = in.readString();
        this.mobileNo = in.readString();
        this.email = in.readString();
        this.gender = in.readString();
        this.profileImage = in.readString();
        this.onlineStatus = in.readInt();
        this.myStatus = in.readString();
        this.newMessageAlert = in.readString();
        this.inAppAlert = in.readString();
        this.emailVerificationStatus = in.readString();
        this.tmpUserId = in.readString();
        this.cityOrRegion = in.readString();
        this.coverPhoto = in.readString();
        this.jabberdResource = in.readString();
        this.profileImageUrl = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    static {
        CREATOR = new C11022();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.userId);
        out.writeString(this.username);
        out.writeString(this.chatId);
        out.writeString(this.mobileNo);
        out.writeString(this.email);
        out.writeString(this.gender);
        out.writeString(this.profileImage);
        out.writeInt(this.onlineStatus);
        out.writeString(this.myStatus);
        out.writeString(this.newMessageAlert);
        out.writeString(this.inAppAlert);
        out.writeString(this.emailVerificationStatus);
        out.writeString(this.tmpUserId);
        out.writeString(this.cityOrRegion);
        out.writeString(this.coverPhoto);
        out.writeString(this.jabberdResource);
        out.writeString(this.profileImageUrl);
    }
}

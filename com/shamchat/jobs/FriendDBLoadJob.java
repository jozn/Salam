package com.shamchat.jobs;

import android.content.ContentResolver;
import android.database.Cursor;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.RosterProvider;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.events.FriendDBLoadCompletedEvent;
import com.shamchat.models.User;
import com.shamchat.models.User.BooleanStatus;
import com.shamchat.utils.Utils;
import de.greenrobot.event.EventBus;
import java.util.concurrent.atomic.AtomicInteger;
import org.jivesoftware.smack.packet.Presence.Type;

public final class FriendDBLoadJob extends Job {
    private static final AtomicInteger jobCounter;
    private String friendId;
    private final int id;

    static {
        jobCounter = new AtomicInteger(0);
    }

    public FriendDBLoadJob(String friendId) {
        Params params = new Params(100);
        params.persistent = true;
        super(params);
        this.id = jobCounter.incrementAndGet();
        this.friendId = friendId;
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        if (this.id != jobCounter.get()) {
            System.out.println("I am returning");
            return;
        }
        Cursor friendCursor = null;
        Cursor rosterCursor = null;
        try {
            ContentResolver contentResolver = SHAMChatApplication.getMyApplicationContext().getContentResolver();
            String[] strArr = new String[1];
            strArr[0] = this.friendId;
            friendCursor = contentResolver.query(UserProvider.CONTENT_URI_USER, null, "userId=?", strArr, null);
            friendCursor.moveToFirst();
            System.out.println("PROFILE ACTIVITY 1");
            String friendJID = Utils.createXmppUserIdByUserId(this.friendId);
            rosterCursor = contentResolver.query(RosterProvider.CONTENT_URI, null, "jid=?", new String[]{friendJID}, null);
            System.out.println("PROFILE ACTIVITY 2");
            User user = new User();
            user.userId = this.friendId;
            user.username = friendCursor.getString(friendCursor.getColumnIndex("name"));
            user.myStatus = friendCursor.getString(friendCursor.getColumnIndex("myStatus"));
            user.mobileNo = friendCursor.getString(friendCursor.getColumnIndex("mobileNo"));
            System.out.println("PROFILE ACTIVITY  3");
            user.profileImageUrl = friendCursor.getString(friendCursor.getColumnIndex("profileimage_url"));
            System.out.println("PROFILE ACTIVITY 4");
            if (rosterCursor != null && rosterCursor.getCount() > 0) {
                int OnlineStatus;
                rosterCursor.moveToFirst();
                System.out.println("PROFILE ACTIVITY 5");
                if (rosterCursor != null) {
                    rosterCursor.getCount();
                }
                int blockStatus = rosterCursor.getInt(rosterCursor.getColumnIndex("user_status"));
                System.out.println("User blck status " + blockStatus);
                if (blockStatus == 1) {
                    user.isBlocked = true;
                } else {
                    user.isBlocked = false;
                }
                String userStatus = friendCursor.getString(friendCursor.getColumnIndex("onlineStatus"));
                Type.unavailable.toString();
                if (!userStatus.equalsIgnoreCase(Type.unavailable.toString())) {
                    if (!userStatus.equalsIgnoreCase("offline")) {
                        OnlineStatus = Type.available.ordinal();
                        user.onlineStatus = OnlineStatus;
                        user.statusMode = rosterCursor.getInt(rosterCursor.getColumnIndex("status_mode"));
                    }
                }
                OnlineStatus = Type.unavailable.ordinal();
                user.onlineStatus = OnlineStatus;
                user.statusMode = rosterCursor.getInt(rosterCursor.getColumnIndex("status_mode"));
            }
            System.out.println("PROFILE ACTIVITY 6");
            if (friendCursor.getInt(friendCursor.getColumnIndex("user_type")) == 2) {
                user.isInChat = BooleanStatus.TRUE;
            } else {
                user.isInChat = BooleanStatus.FALSE;
            }
            System.out.println("PROFILE ACTIVITY 7");
            user.gender = friendCursor.getString(friendCursor.getColumnIndex("gender"));
            user.cityOrRegion = friendCursor.getString(friendCursor.getColumnIndex("region"));
            user.chatId = friendCursor.getString(friendCursor.getColumnIndex("chatId"));
            if (friendCursor.getInt(friendCursor.getColumnIndex("is_vcard_downloaded")) == 1) {
                user.isVCardDownloaded = true;
            } else {
                user.isVCardDownloaded = false;
            }
            if (friendCursor.getInt(friendCursor.getColumnIndex("is_added_to_roster")) == 1) {
                user.isAddedToRoster = true;
            } else {
                user.isAddedToRoster = false;
            }
            System.out.println("PROFILE ACTIVITY 8");
            EventBus.getDefault().post(new FriendDBLoadCompletedEvent(user));
            System.out.println("PROFILE ACTIVITY 9");
        } catch (Exception e) {
            System.out.println("PROFILE ACTIVITY ex " + e);
            System.out.println("PROFILE ACTIVITY exception " + e);
        } finally {
            friendCursor.close();
            rosterCursor.close();
        }
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}

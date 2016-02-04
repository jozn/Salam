package com.shamchat.jobs;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.RosterProvider;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.events.CitContactsDBLoadCompletedEvent;
import com.shamchat.models.ContactFriend;
import com.shamchat.utils.Utils;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public final class SHAMContactsDBLoadJob extends Job {
    private static final AtomicInteger jobCounter;
    private final int id;
    public String search;

    static {
        jobCounter = new AtomicInteger(0);
    }

    public SHAMContactsDBLoadJob() {
        super(new Params(100));
        this.id = jobCounter.incrementAndGet();
        System.out.println("AAA ContactsDBLoadJob constructor ");
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        System.out.println("ContactShamFragment id != jobCounter.get()" + this.id + " job" + jobCounter.get());
        if (this.id != jobCounter.get()) {
            System.out.println("ContactShamFragment return");
            return;
        }
        System.out.println("ContactShamFragment not return");
        Cursor cursor = SHAMChatApplication.getMyApplicationContext().getContentResolver().query(RosterProvider.CONTENT_URI, RosterProvider.ROSTER_ALL_COLUMNS, "user_status!=?", new String[]{"2"}, "alias COLLATE NOCASE ASC");
        Map<String, List<ContactFriend>> allContacts = new HashMap();
        Cursor myCursor = SHAMChatApplication.getMyApplicationContext().getContentResolver().query(Uri.parse(UserProvider.CONTENT_URI_USER.toString() + MqttTopic.TOPIC_LEVEL_SEPARATOR + SHAMChatApplication.getConfig().userId), null, null, null, null);
        myCursor.moveToFirst();
        ArrayList<ContactFriend> me = new ArrayList();
        ContactFriend currentUser = new ContactFriend();
        currentUser.userId = SHAMChatApplication.getConfig().jabberID;
        currentUser.userName = myCursor.getString(myCursor.getColumnIndex("name"));
        currentUser.userStartingLetter = "You";
        currentUser.profileImg = myCursor.getString(myCursor.getColumnIndex("profileimage_url"));
        System.out.println("current user " + currentUser.profileImg);
        myCursor.close();
        me.add(currentUser);
        allContacts.put("You", me);
        ContentResolver cr = SHAMChatApplication.getMyApplicationContext().getContentResolver();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String username = cursor.getString(cursor.getColumnIndex("alias"));
                String userId = Utils.getUserIdFromXmppUserId(cursor.getString(cursor.getColumnIndex("jid")));
                System.out.println("REFRESH  SHAM username " + username + " USER ID " + userId);
                Cursor friendUserCursor = cr.query(UserProvider.CONTENT_URI_USER, null, "userId=?", new String[]{userId}, null);
                if (friendUserCursor != null) {
                    if (friendUserCursor.getCount() > 0) {
                        friendUserCursor.moveToFirst();
                        String mobileNumber = friendUserCursor.getString(friendUserCursor.getColumnIndex("mobileNo"));
                        if (friendUserCursor.getInt(friendUserCursor.getColumnIndex("is_added_to_roster")) == 1) {
                            if (username.contains(this.search) || mobileNumber.contains(this.search)) {
                                System.out.println("FOUND username " + username + "  mobile number " + mobileNumber + " search " + this.search);
                                String profileImageUrl = friendUserCursor.getString(friendUserCursor.getColumnIndex("profileimage_url"));
                                String startLetter = username.substring(0, 1);
                                if (!allContacts.containsKey(startLetter)) {
                                    allContacts.put(startLetter, new ArrayList());
                                }
                                List<ContactFriend> subContacts = (List) allContacts.get(startLetter);
                                ContactFriend contactFriend = new ContactFriend();
                                contactFriend.userId = userId;
                                contactFriend.userName = username;
                                contactFriend.userStartingLetter = startLetter;
                                contactFriend.status = cursor.getString(cursor.getColumnIndex("status_message"));
                                contactFriend.profileImg = profileImageUrl;
                                subContacts.add(contactFriend);
                                friendUserCursor.close();
                            } else {
                                System.out.println("FOUND NOT username " + username + "  mobile number " + mobileNumber + " search " + this.search);
                                friendUserCursor.close();
                            }
                        } else {
                            System.out.println("Entries is Added to roster false " + userId);
                            friendUserCursor.close();
                        }
                    }
                }
                System.out.println("db cit cursor null " + userId);
                try {
                    friendUserCursor.close();
                } catch (Exception e) {
                    System.out.println("xxxxxxxxx " + e);
                    return;
                } catch (Throwable th) {
                    friendUserCursor.close();
                }
            }
        }
        cursor.close();
        System.out.println("getAllContacts before " + allContacts.size());
        EventBus.getDefault().post(new CitContactsDBLoadCompletedEvent(allContacts));
        System.out.println("getAllContacts end " + allContacts.size());
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        System.out.println("AAA db load should re run on throwable");
        return false;
    }
}

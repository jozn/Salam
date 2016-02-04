package com.shamchat.jobs;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.events.AllContactsDBLoadCompletedEvent;
import com.shamchat.models.ContactFriend;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public final class AllContactsDBLoadJob extends Job {
    private static final AtomicInteger jobCounter;
    private final int id;
    public String search;

    static {
        jobCounter = new AtomicInteger(0);
    }

    public AllContactsDBLoadJob() {
        super(new Params(1000));
        this.id = jobCounter.incrementAndGet();
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        Exception e;
        if (this.id != jobCounter.get()) {
            System.out.println("Change happened but returning");
            return;
        }
        System.out.println("all get all start");
        List<ArrayList<ContactFriend>> allContacts = new ArrayList();
        String currentUserId = SHAMChatApplication.getConfig().userId;
        ContentResolver cr = SHAMChatApplication.getMyApplicationContext().getContentResolver();
        String[] strArr = new String[3];
        strArr[0] = "%" + this.search + "%";
        strArr[1] = "%" + this.search + "%";
        strArr[2] = currentUserId;
        Cursor cursor = cr.query(UserProvider.CONTENT_URI_USER, null, "name LIKE ? OR mobileNo LIKE ? AND userId!=?", strArr, "name COLLATE NOCASE ASC");
        ArrayList<ContactFriend> subContacts = new ArrayList();
        Cursor myCursor = cr.query(Uri.parse(UserProvider.CONTENT_URI_USER.toString() + MqttTopic.TOPIC_LEVEL_SEPARATOR + currentUserId), null, null, null, null);
        myCursor.moveToFirst();
        ArrayList<ContactFriend> me = new ArrayList();
        ContactFriend currentUser = new ContactFriend();
        currentUser.userId = SHAMChatApplication.getConfig().jabberID;
        currentUser.userName = myCursor.getString(myCursor.getColumnIndex("name"));
        currentUser.userStartingLetter = "You";
        currentUser.profileImg = myCursor.getString(myCursor.getColumnIndex("profileimage_url"));
        myCursor.close();
        me.add(currentUser);
        allContacts.add(me);
        String startLetter = null;
        while (cursor.moveToNext()) {
            try {
                String username = cursor.getString(cursor.getColumnIndex("name"));
                if (startLetter == null) {
                    startLetter = username.substring(0, 1);
                }
                ContactFriend contactFriend;
                if (username.substring(0, 1).equalsIgnoreCase(startLetter)) {
                    contactFriend = new ContactFriend();
                    contactFriend.userId = cursor.getString(cursor.getColumnIndex("userId"));
                    contactFriend.userName = username;
                    contactFriend.userStartingLetter = startLetter;
                    contactFriend.status = cursor.getString(cursor.getColumnIndex("myStatus"));
                    contactFriend.inChat = cursor.getInt(cursor.getColumnIndex("user_type"));
                    contactFriend.profileImg = cursor.getString(cursor.getColumnIndex("profileimage_url"));
                    subContacts.add(contactFriend);
                    if (cursor.getPosition() == cursor.getCount() - 1) {
                        allContacts.add(subContacts);
                    }
                } else {
                    startLetter = username.substring(0, 1);
                    allContacts.add(subContacts);
                    ArrayList<ContactFriend> subContacts2 = new ArrayList();
                    try {
                        contactFriend = new ContactFriend();
                        contactFriend.userId = cursor.getString(cursor.getColumnIndex("userId"));
                        contactFriend.userName = username;
                        contactFriend.userStartingLetter = startLetter;
                        contactFriend.status = cursor.getString(cursor.getColumnIndex("myStatus"));
                        contactFriend.inChat = cursor.getInt(cursor.getColumnIndex("user_type"));
                        contactFriend.profileImg = cursor.getString(cursor.getColumnIndex("profileimage_url"));
                        subContacts2.add(contactFriend);
                        if (cursor.getPosition() == cursor.getCount() - 1) {
                            allContacts.add(subContacts2);
                        }
                        subContacts = subContacts2;
                    } catch (Exception e2) {
                        e = e2;
                        subContacts = subContacts2;
                        System.out.println("all exception all db " + e);
                    }
                }
            } catch (Exception e3) {
                e = e3;
                System.out.println("all exception all db " + e);
            }
        }
        cursor.close();
        System.out.println("all getAllContacts before " + allContacts.size());
        EventBus.getDefault().post(new AllContactsDBLoadCompletedEvent(allContacts));
        System.out.println("all getAllContacts end " + allContacts.size());
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}

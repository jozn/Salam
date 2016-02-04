package com.shamchat.jobs;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.rokhgroup.mqtt.ActionListener;
import com.rokhgroup.mqtt.ActionListener.Action;
import com.rokhgroup.mqtt.Connections;
import com.rokhgroup.utils.RokhPref;
import com.shamchat.activity.ChatActivity;
import com.shamchat.activity.ChatInitialForGroupChatActivity;
import com.shamchat.adapters.MyMessageType;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.events.NewMessageEvent;
import com.shamchat.models.FriendGroup;
import com.shamchat.models.FriendGroupMember;
import com.shamchat.models.MessageThread;
import com.shamchat.utils.Utils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;
import de.greenrobot.event.EventBus;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONArray;
import org.json.JSONObject;

public final class RoomRestoreJob extends Job {
    private static final AtomicInteger jobCounter;
    private final int id;
    MqttAndroidClient mqttClient;

    static {
        jobCounter = new AtomicInteger(0);
    }

    public RoomRestoreJob() {
        Params params = new Params(1000);
        params.persistent = true;
        params.requiresNetwork = true;
        super(params);
        this.id = jobCounter.incrementAndGet();
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        UserProvider userProvider = new UserProvider();
        UserProvider.getCurrentUserForMyProfile();
        String clientHandle = new RokhPref(SHAMChatApplication.getInstance().getApplicationContext()).getClientHandle();
        ContentResolver mContentResolver = SHAMChatApplication.getInstance().getApplicationContext().getContentResolver();
        String userId = SHAMChatApplication.getConfig().userId;
        String URL = "http://social.rabtcdn.com/groups/api/v1/topics/user/" + userId + MqttTopic.TOPIC_LEVEL_SEPARATOR;
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(60, TimeUnit.SECONDS);
        client.setReadTimeout(60, TimeUnit.SECONDS);
        Response response = client.newCall(new Builder().url(URL).build()).execute();
        if (response.isSuccessful()) {
            String stringResponse = response.body().string();
            response.body().close();
            System.out.println(stringResponse);
            JSONObject jSONObject = new JSONObject(stringResponse);
            String status = jSONObject.getString(NotificationCompatApi21.CATEGORY_STATUS);
            if (status.equals("200")) {
                JSONArray allTopicsArray = jSONObject.getJSONArray("objects");
                String[] allTopics = new String[allTopicsArray.length()];
                int[] qosArray = new int[allTopicsArray.length()];
                if (allTopicsArray.length() > 0) {
                    for (int i = 0; i < allTopicsArray.length(); i++) {
                        JSONObject allTopicsList = allTopicsArray.getJSONObject(i);
                        String groupAlias = allTopicsList.getString("title");
                        allTopicsList.getString("sub_path");
                        String.valueOf(allTopicsList.getInt("cnt_members"));
                        String groupHashCode = allTopicsList.getString("hashcode");
                        allTopics[i] = allTopicsList.getString("hashcode");
                        qosArray[i] = 1;
                        JSONObject groupAdmin = allTopicsList.getJSONObject("admin");
                        String groupAdminID = groupAdmin.getString(ChatActivity.INTENT_EXTRA_USER_ID);
                        groupAdmin.getString("phone");
                        Cursor groupCursor = mContentResolver.query(UserProvider.CONTENT_URI_GROUP, new String[]{FriendGroup.DB_ID}, FriendGroup.CHAT_ROOM_NAME + "=?", new String[]{groupHashCode}, null);
                        boolean isUpdate = false;
                        if (groupCursor.getCount() > 0) {
                            isUpdate = true;
                        }
                        groupCursor.close();
                        FriendGroup group = new FriendGroup();
                        group.id = groupHashCode;
                        group.name = groupAlias;
                        group.recordOwnerId = groupAdminID;
                        group.chatRoomName = groupHashCode;
                        ContentValues values = new ContentValues();
                        values.put(FriendGroup.DB_ID, groupHashCode);
                        values.put(FriendGroup.DB_NAME, groupAlias);
                        values.put(FriendGroup.DB_RECORD_OWNER, groupAdminID);
                        values.put(FriendGroup.CHAT_ROOM_NAME, groupHashCode);
                        MessageThread thread = new MessageThread();
                        thread.friendId = groupHashCode;
                        thread.isGroupChat = true;
                        thread.threadOwner = userId;
                        ContentValues vals = new ContentValues();
                        vals.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, thread.getThreadId());
                        ContentValues contentValues = vals;
                        contentValues.put("friend_id", thread.friendId);
                        vals.put("read_status", Integer.valueOf(0));
                        vals.put("last_updated_datetime", Utils.formatDate(new Date().getTime(), "yyyy/MM/dd HH:mm:ss"));
                        vals.put("is_group_chat", Integer.valueOf(1));
                        contentValues = vals;
                        contentValues.put("thread_owner", thread.threadOwner);
                        if (isUpdate) {
                            mContentResolver.update(UserProvider.CONTENT_URI_GROUP, values, FriendGroup.DB_ID + "=?", new String[]{group.id});
                            contentValues = vals;
                            mContentResolver.update(ChatProviderNew.CONTENT_URI_THREAD, contentValues, "thread_id=?", new String[]{thread.getThreadId()});
                        } else {
                            mContentResolver.insert(UserProvider.CONTENT_URI_GROUP, values);
                            vals.put("last_message", SHAMChatApplication.getInstance().getString(2131493220));
                            vals.put("last_message_direction", Integer.valueOf(MyMessageType.INCOMING_MSG.ordinal()));
                            vals.put("last_message_content_type", Integer.valueOf(0));
                            mContentResolver.insert(ChatProviderNew.CONTENT_URI_THREAD, vals);
                        }
                        FriendGroupMember admin = new FriendGroupMember(group.id, groupAdminID);
                        admin.assignUniqueId(userId);
                        ContentValues adminCv = new ContentValues();
                        adminCv.put(FriendGroupMember.DB_ID, admin.id);
                        adminCv.put(FriendGroupMember.DB_FRIEND, admin.friendId);
                        adminCv.put(FriendGroupMember.DB_GROUP, admin.groupID);
                        adminCv.put(FriendGroupMember.DB_FRIEND_IS_ADMIN, Integer.valueOf(1));
                        adminCv.put(FriendGroupMember.PHONE_NUMBER, admin.phoneNumber);
                        if (mContentResolver.update(UserProvider.CONTENT_URI_GROUP_MEMBER, adminCv, FriendGroupMember.DB_GROUP + "=? AND " + FriendGroupMember.DB_FRIEND + "=?", new String[]{admin.groupID, groupAdminID}) == 0) {
                            mContentResolver.insert(UserProvider.CONTENT_URI_GROUP_MEMBER, adminCv);
                        }
                        JSONArray users = allTopicsList.getJSONArray("users");
                        if (users.length() > 0) {
                            for (int j = 0; j < users.length(); j++) {
                                JSONObject member = users.getJSONObject(j);
                                String memberId = member.getString(ChatActivity.INTENT_EXTRA_USER_ID);
                                String memberPhone = member.getString("phone");
                                boolean memberisAdmin = member.getBoolean("is_admin");
                                FriendGroupMember friendGroupMember = new FriendGroupMember(group.id, memberId);
                                friendGroupMember.assignUniqueId(userId);
                                ContentValues groupMembers = new ContentValues();
                                groupMembers.put(FriendGroupMember.DB_ID, friendGroupMember.id);
                                groupMembers.put(FriendGroupMember.DB_FRIEND, friendGroupMember.friendId);
                                groupMembers.put(FriendGroupMember.DB_GROUP, friendGroupMember.groupID);
                                groupMembers.put(FriendGroupMember.PHONE_NUMBER, memberPhone);
                                if (memberisAdmin) {
                                    groupMembers.put(FriendGroupMember.DB_FRIEND_IS_ADMIN, Integer.valueOf(1));
                                }
                                groupMembers.put(FriendGroupMember.DB_FRIEND_DID_JOIN, Integer.valueOf(1));
                                if (mContentResolver.update(UserProvider.CONTENT_URI_GROUP_MEMBER, groupMembers, FriendGroupMember.DB_GROUP + "=? AND " + FriendGroupMember.DB_FRIEND + "=?", new String[]{friendGroupMember.groupID, memberId}) == 0) {
                                    mContentResolver.insert(UserProvider.CONTENT_URI_GROUP_MEMBER, groupMembers);
                                }
                            }
                        }
                        NewMessageEvent newMessageEvent = new NewMessageEvent();
                        newMessageEvent.threadId = thread.getThreadId();
                        newMessageEvent.direction = MyMessageType.INCOMING_MSG.ordinal();
                        EventBus.getDefault().postSticky(newMessageEvent);
                    }
                    IMqttActionListener callback = new ActionListener(SHAMChatApplication.getInstance().getApplicationContext(), Action.SUBSCRIBE$621bd8f2, clientHandle, allTopics);
                    this.mqttClient = Connections.getInstance(SHAMChatApplication.getInstance().getApplicationContext()).getConnection(clientHandle).client;
                    this.mqttClient.subscribe(allTopics, qosArray, null, callback);
                    return;
                }
                return;
            }
            throw new IOException("Unexpected reponse code " + status);
        }
        throw new IOException("Unexpected code " + response);
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        System.out.println("Room Restore Job run again");
        return true;
    }
}

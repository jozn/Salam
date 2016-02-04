package com.shamchat.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.rokhgroup.mqtt.ActionListener;
import com.rokhgroup.mqtt.ActionListener.Action;
import com.rokhgroup.mqtt.Connection;
import com.rokhgroup.mqtt.Connections;
import com.rokhgroup.utils.RokhPref;
import com.shamchat.adapters.MyMessageType;
import com.shamchat.adapters.UserAdapter;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.androidclient.util.PreferenceConstants;
import com.shamchat.models.FriendGroup;
import com.shamchat.models.FriendGroupMember;
import com.shamchat.models.User;
import com.shamchat.utils.PopUpUtil;
import com.shamchat.utils.Utils;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import cz.msebera.android.httpclient.entity.StringEntity;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONArray;
import org.json.JSONObject;

public class CreateGroupActivity extends AppCompatActivity {
    public static final String EXTRA_CONTACTS = "extra_contacts";
    public static final String EXTRA_GROUP_ID = "extra_group_id";
    public static final String EXTRA_IS_ADD = "is_add";
    public static final String EXTRA_IS_DELETE = "is_delete";
    static final int PICK_CONTACTS_REQUEST = 1;
    protected static final String TAG = "CreateGroupActivity";
    RokhPref Session;
    private UserAdapter adapter;
    private ChangeListener changeListener;
    private String clientHandle;
    Connection connection;
    private boolean editMode;
    private String groupHashcode;
    private String groupId;
    private TextView groupNameTextView;
    List<Model> groupUsers;
    private List<User> initialUserList;
    private boolean isDelete;
    private ContentResolver mContentResolver;
    private User me;
    ArrayList<User> newUsers;
    private View noUsersImage;
    private TextView noUsersText;
    private ImageButton saveButton;
    private StringEntity stringEntity;
    private ListView usersListView;

    /* renamed from: com.shamchat.activity.CreateGroupActivity.1 */
    class C07421 implements Runnable {

        /* renamed from: com.shamchat.activity.CreateGroupActivity.1.1 */
        class C07401 implements Runnable {
            final /* synthetic */ FriendGroup val$group;

            C07401(FriendGroup friendGroup) {
                this.val$group = friendGroup;
            }

            public final void run() {
                try {
                    ProgressBarDialogLogin.getInstance().dismiss();
                } catch (Exception e) {
                }
                Intent i = new Intent(CreateGroupActivity.this.getApplicationContext(), ChatInitialForGroupChatActivity.class);
                i.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, CreateGroupActivity.this.me.userId + "-" + this.val$group.id);
                i.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_GROUP_ID, this.val$group.id);
                CreateGroupActivity.this.startActivity(i);
                CreateGroupActivity.this.finish();
            }
        }

        /* renamed from: com.shamchat.activity.CreateGroupActivity.1.2 */
        class C07412 implements Runnable {
            C07412() {
            }

            public final void run() {
                try {
                    ProgressBarDialogLogin.getInstance().dismiss();
                } catch (Exception e) {
                }
                new PopUpUtil().getPopFailed$40a28a58(CreateGroupActivity.this, CreateGroupActivity.this.getApplicationContext().getString(2131493159)).show();
            }
        }

        C07421() {
        }

        public final void run() {
            Boolean isSubscribeToMqtt;
            List<String> friendsToBeInvited = new ArrayList();
            User[] list = CreateGroupActivity.this.adapter.getUsers();
            ArrayList arrayList = new ArrayList();
            List<User> selectedUserList = Arrays.asList(list);
            for (User user : selectedUserList) {
                Model model = new Model((byte) 0);
                model.name = user.username;
                model.phone = user.mobileNo;
                model.userId = user.userId;
                CreateGroupActivity.this.groupUsers.add(model);
            }
            if (CreateGroupActivity.this.groupId != null) {
                isSubscribeToMqtt = Boolean.valueOf(CreateGroupActivity.this.SubscribeToMqtt(CreateGroupActivity.this.groupId));
            } else {
                isSubscribeToMqtt = Boolean.valueOf(CreateGroupActivity.this.SubscribeToMqtt(BuildConfig.VERSION_NAME));
            }
            if (isSubscribeToMqtt.booleanValue()) {
                ContentResolver access$500;
                Uri uri;
                FriendGroup group;
                ContentValues values;
                String str;
                String[] strArr;
                if (CreateGroupActivity.this.groupId != null) {
                    access$500 = CreateGroupActivity.this.mContentResolver;
                    uri = UserProvider.CONTENT_URI_GROUP;
                    String str2 = FriendGroup.DB_ID + "=?";
                    String[] strArr2 = new String[CreateGroupActivity.PICK_CONTACTS_REQUEST];
                    strArr2[0] = CreateGroupActivity.this.groupId;
                    Cursor cursor = access$500.query(uri, null, str2, strArr2, null);
                    group = UserProvider.groupFromCursor(cursor);
                    values = new ContentValues();
                    values.put(FriendGroup.DB_NAME, CreateGroupActivity.this.getGroupName());
                    values.put(FriendGroup.DB_RECORD_OWNER, group.recordOwnerId);
                    access$500 = CreateGroupActivity.this.mContentResolver;
                    uri = UserProvider.CONTENT_URI_GROUP;
                    str = FriendGroup.DB_ID + "=?";
                    strArr = new String[CreateGroupActivity.PICK_CONTACTS_REQUEST];
                    strArr[0] = group.id;
                    access$500.update(uri, values, str, strArr);
                    cursor.close();
                } else {
                    group = new FriendGroup(CreateGroupActivity.this.getGroupName(), CreateGroupActivity.this.me.userId);
                    group.id = CreateGroupActivity.this.groupHashcode;
                    group.chatRoomName = CreateGroupActivity.this.groupHashcode;
                    values = new ContentValues();
                    values.put(FriendGroup.DB_ID, CreateGroupActivity.this.groupHashcode);
                    values.put(FriendGroup.DB_NAME, CreateGroupActivity.this.getGroupName());
                    values.put(FriendGroup.DB_RECORD_OWNER, group.recordOwnerId);
                    values.put(FriendGroup.CHAT_ROOM_NAME, CreateGroupActivity.this.groupHashcode);
                    CreateGroupActivity.this.mContentResolver.insert(UserProvider.CONTENT_URI_GROUP, values);
                    ContentValues fakeValues = new ContentValues();
                    String threadOwner = SHAMChatApplication.getConfig().userId;
                    String friendId = CreateGroupActivity.this.groupHashcode;
                    fakeValues.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, threadOwner + "-" + friendId);
                    fakeValues.put("friend_id", friendId);
                    fakeValues.put("read_status", Integer.valueOf(CreateGroupActivity.PICK_CONTACTS_REQUEST));
                    fakeValues.put("last_message", "Welcome to Group".trim());
                    fakeValues.put("last_message_content_type", Integer.valueOf(MessageContentType.TEXT.ordinal()));
                    fakeValues.put("last_updated_datetime", Utils.formatDate(new Date().getTime(), "yyyy/MM/dd HH:mm:ss"));
                    fakeValues.put("is_group_chat", Boolean.valueOf(true));
                    fakeValues.put("thread_owner", SHAMChatApplication.getConfig().userId);
                    fakeValues.put("last_message_direction", Integer.valueOf(MyMessageType.OUTGOING_MSG.ordinal()));
                    CreateGroupActivity.this.mContentResolver.insert(ChatProviderNew.CONTENT_URI_THREAD, fakeValues);
                }
                access$500 = CreateGroupActivity.this.mContentResolver;
                uri = UserProvider.CONTENT_URI_GROUP_MEMBER;
                str = FriendGroup.DB_ID + "=?";
                strArr = new String[CreateGroupActivity.PICK_CONTACTS_REQUEST];
                strArr[0] = group.id;
                access$500.delete(uri, str, strArr);
                FriendGroupMember friendGroupMember = new FriendGroupMember(group.id, CreateGroupActivity.this.me.userId);
                friendGroupMember.assignUniqueId(CreateGroupActivity.this.me.userId);
                values = new ContentValues();
                values.put(FriendGroupMember.DB_ID, friendGroupMember.id);
                values.put(FriendGroupMember.DB_FRIEND, friendGroupMember.friendId);
                values.put(FriendGroupMember.DB_GROUP, friendGroupMember.groupID);
                values.put(FriendGroupMember.PHONE_NUMBER, CreateGroupActivity.this.me.mobileNo);
                values.put(FriendGroupMember.DB_FRIEND_IS_ADMIN, Integer.valueOf(CreateGroupActivity.PICK_CONTACTS_REQUEST));
                values.put(FriendGroupMember.DB_FRIEND_DID_JOIN, Integer.valueOf(CreateGroupActivity.PICK_CONTACTS_REQUEST));
                CreateGroupActivity.this.mContentResolver.insert(UserProvider.CONTENT_URI_GROUP_MEMBER, values);
                for (User user2 : selectedUserList) {
                    if (user2.checked) {
                        friendGroupMember = new FriendGroupMember(group.id, user2.userId);
                        friendGroupMember.assignUniqueId(CreateGroupActivity.this.me.userId);
                        ContentValues vals = new ContentValues();
                        vals.put(FriendGroupMember.DB_ID, friendGroupMember.id);
                        vals.put(FriendGroupMember.DB_FRIEND, friendGroupMember.friendId);
                        vals.put(FriendGroupMember.DB_GROUP, friendGroupMember.groupID);
                        vals.put(FriendGroupMember.PHONE_NUMBER, user2.mobileNo);
                        vals.put(FriendGroupMember.DB_FRIEND_DID_JOIN, Integer.valueOf(CreateGroupActivity.PICK_CONTACTS_REQUEST));
                        CreateGroupActivity.this.mContentResolver.insert(UserProvider.CONTENT_URI_GROUP_MEMBER, vals);
                        friendsToBeInvited.add(user2.userId);
                    }
                }
                SHAMChatApplication.getInstance().runOnUiThread(new C07401(group));
                return;
            }
            SHAMChatApplication.getInstance().runOnUiThread(new C07412());
        }
    }

    /* renamed from: com.shamchat.activity.CreateGroupActivity.2 */
    class C07452 implements Runnable {

        /* renamed from: com.shamchat.activity.CreateGroupActivity.2.1 */
        class C07431 implements Runnable {
            C07431() {
            }

            public final void run() {
                try {
                    ProgressBarDialogLogin.getInstance().dismiss();
                } catch (Exception e) {
                }
                CreateGroupActivity.this.finish();
            }
        }

        /* renamed from: com.shamchat.activity.CreateGroupActivity.2.2 */
        class C07442 implements Runnable {
            C07442() {
            }

            public final void run() {
                new PopUpUtil().getPopFailed$40a28a58(CreateGroupActivity.this, "Group could not be created").show();
            }
        }

        C07452() {
        }

        public final void run() {
            Boolean isSubscribeToMqtt;
            List<String> friendsToBeInvited = new ArrayList();
            User[] list = CreateGroupActivity.this.adapter.getUsers();
            ArrayList arrayList = new ArrayList();
            List<User> selectedUserList = Arrays.asList(list);
            for (User user : selectedUserList) {
                Model model = new Model((byte) 0);
                model.name = user.username;
                model.phone = user.mobileNo;
                model.userId = user.userId;
                CreateGroupActivity.this.groupUsers.add(model);
            }
            if (CreateGroupActivity.this.groupId != null) {
                isSubscribeToMqtt = Boolean.valueOf(CreateGroupActivity.this.SubscribeToMqtt(CreateGroupActivity.this.groupId));
            } else {
                isSubscribeToMqtt = Boolean.valueOf(CreateGroupActivity.this.SubscribeToMqtt(BuildConfig.VERSION_NAME));
            }
            if (isSubscribeToMqtt.booleanValue()) {
                ContentResolver access$500;
                Uri uri;
                FriendGroup group;
                ContentValues values;
                String str;
                String[] strArr;
                if (CreateGroupActivity.this.groupId != null) {
                    access$500 = CreateGroupActivity.this.mContentResolver;
                    uri = UserProvider.CONTENT_URI_GROUP;
                    String str2 = FriendGroup.DB_ID + "=?";
                    String[] strArr2 = new String[CreateGroupActivity.PICK_CONTACTS_REQUEST];
                    strArr2[0] = CreateGroupActivity.this.groupId;
                    Cursor cursor = access$500.query(uri, null, str2, strArr2, null);
                    group = UserProvider.groupFromCursor(cursor);
                    values = new ContentValues();
                    values.put(FriendGroup.DB_NAME, CreateGroupActivity.this.getGroupName());
                    values.put(FriendGroup.DB_RECORD_OWNER, group.recordOwnerId);
                    access$500 = CreateGroupActivity.this.mContentResolver;
                    uri = UserProvider.CONTENT_URI_GROUP;
                    str = FriendGroup.DB_ID + "=?";
                    strArr = new String[CreateGroupActivity.PICK_CONTACTS_REQUEST];
                    strArr[0] = group.id;
                    access$500.update(uri, values, str, strArr);
                    cursor.close();
                } else {
                    group = new FriendGroup(CreateGroupActivity.this.getGroupName(), CreateGroupActivity.this.me.userId);
                    group.id = CreateGroupActivity.this.groupHashcode;
                    group.chatRoomName = CreateGroupActivity.this.groupHashcode;
                    values = new ContentValues();
                    values.put(FriendGroup.DB_ID, CreateGroupActivity.this.groupHashcode);
                    values.put(FriendGroup.DB_NAME, CreateGroupActivity.this.getGroupName());
                    values.put(FriendGroup.DB_RECORD_OWNER, group.recordOwnerId);
                    values.put(FriendGroup.CHAT_ROOM_NAME, CreateGroupActivity.this.groupHashcode);
                    CreateGroupActivity.this.mContentResolver.insert(UserProvider.CONTENT_URI_GROUP, values);
                }
                access$500 = CreateGroupActivity.this.mContentResolver;
                uri = UserProvider.CONTENT_URI_GROUP_MEMBER;
                str = FriendGroup.DB_ID + "=?";
                strArr = new String[CreateGroupActivity.PICK_CONTACTS_REQUEST];
                strArr[0] = group.id;
                access$500.delete(uri, str, strArr);
                FriendGroupMember myself = new FriendGroupMember(group.id, CreateGroupActivity.this.me.userId);
                myself.assignUniqueId(CreateGroupActivity.this.me.userId);
                values = new ContentValues();
                values.put(FriendGroupMember.DB_ID, myself.id);
                values.put(FriendGroupMember.DB_FRIEND, myself.friendId);
                values.put(FriendGroupMember.DB_FRIEND_IS_ADMIN, Integer.valueOf(CreateGroupActivity.PICK_CONTACTS_REQUEST));
                values.put(FriendGroupMember.DB_GROUP, myself.groupID);
                values.put(FriendGroupMember.PHONE_NUMBER, CreateGroupActivity.this.me.mobileNo);
                CreateGroupActivity.this.mContentResolver.insert(UserProvider.CONTENT_URI_GROUP_MEMBER, values);
                for (User user2 : selectedUserList) {
                    if (user2.checked) {
                        FriendGroupMember member = new FriendGroupMember(group.id, user2.userId);
                        member.assignUniqueId(CreateGroupActivity.this.me.userId);
                        ContentValues vals = new ContentValues();
                        vals.put(FriendGroupMember.DB_ID, member.id);
                        vals.put(FriendGroupMember.DB_FRIEND, member.friendId);
                        vals.put(FriendGroupMember.DB_GROUP, member.groupID);
                        vals.put(FriendGroupMember.PHONE_NUMBER, user2.mobileNo);
                        CreateGroupActivity.this.mContentResolver.insert(UserProvider.CONTENT_URI_GROUP_MEMBER, vals);
                        friendsToBeInvited.add(user2.userId);
                    }
                }
                SHAMChatApplication.getInstance().runOnUiThread(new C07431());
                return;
            }
            SHAMChatApplication.getInstance().runOnUiThread(new C07442());
        }
    }

    /* renamed from: com.shamchat.activity.CreateGroupActivity.3 */
    class C07463 implements OnEditorActionListener {
        C07463() {
        }

        public final boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == 6) {
                boolean z;
                CreateGroupActivity createGroupActivity = CreateGroupActivity.this;
                if (CreateGroupActivity.this.editMode) {
                    z = false;
                } else {
                    z = true;
                }
                createGroupActivity.setEdittingGroupName(z);
            }
            return false;
        }
    }

    private class ChangeListener implements PropertyChangeListener {
        private ChangeListener() {
        }

        public final void propertyChange(PropertyChangeEvent event) {
            Log.e("MQTT STATUS", event.toString());
        }
    }

    private class Model {
        String name;
        String phone;
        String userId;

        private Model() {
        }
    }

    public CreateGroupActivity() {
        this.editMode = false;
        this.isDelete = false;
        this.clientHandle = null;
        this.connection = null;
        this.changeListener = null;
        this.groupUsers = new ArrayList();
        this.groupHashcode = null;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903080);
        this.Session = new RokhPref(this);
        this.clientHandle = this.Session.getClientHandle();
        this.connection = Connections.getInstance(this).getConnection(this.clientHandle);
        this.changeListener = new ChangeListener();
        this.connection.registerChangeListener(this.changeListener);
        this.saveButton = (ImageButton) findViewById(2131361969);
        this.saveButton.setVisibility(-1);
        ImageButton composeButton = (ImageButton) findViewById(2131361968);
        ImageButton addButton = (ImageButton) findViewById(2131361958);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(31);
        setEdittingGroupName(false);
        this.usersListView = (ListView) findViewById(2131361967);
        this.noUsersImage = findViewById(2131361965);
        this.noUsersText = (TextView) findViewById(2131361966);
        this.adapter = new UserAdapter(this);
        UserAdapter userAdapter = this.adapter;
        userAdapter.editMode = true;
        if (userAdapter.editMode) {
            userAdapter.setCheckboxes$1385ff();
        }
        userAdapter.notifyDataSetChanged();
        this.usersListView.setAdapter(this.adapter);
        this.groupNameTextView = (TextView) actionBar.getCustomView().findViewById(2131361837);
        this.groupNameTextView.setText(FriendGroup.getNextAvailableGroupName(getApplicationContext()));
        Bundle extras = getIntent().getExtras();
        this.mContentResolver = getContentResolver();
        String[] strArr = new String[PICK_CONTACTS_REQUEST];
        strArr[0] = SHAMChatApplication.getConfig().userId;
        Cursor cursor = this.mContentResolver.query(UserProvider.CONTENT_URI_USER, null, "userId=?", strArr, null);
        cursor.moveToFirst();
        this.me = UserProvider.userFromCursor(cursor);
        cursor.close();
        if (extras != null && extras.containsKey(EXTRA_GROUP_ID)) {
            this.groupId = extras.getString(EXTRA_GROUP_ID);
            if (extras.containsKey(EXTRA_IS_DELETE)) {
                this.isDelete = extras.getBoolean(EXTRA_IS_DELETE);
                composeButton.setVisibility(8);
                addButton.setVisibility(8);
            } else if (extras.containsKey(EXTRA_IS_ADD)) {
                composeButton.setVisibility(8);
            }
            ContentResolver contentResolver = this.mContentResolver;
            Uri uri = UserProvider.CONTENT_URI_GROUP;
            String str = FriendGroup.DB_ID + "=?";
            strArr = new String[PICK_CONTACTS_REQUEST];
            strArr[0] = this.groupId;
            Cursor cursor2 = contentResolver.query(uri, null, str, strArr, null);
            FriendGroup group = UserProvider.groupFromCursor(cursor2);
            cursor2.close();
            setGroupName(group.name);
            UserProvider userProvider = new UserProvider();
            Cursor cursor3 = UserProvider.getUsersInGroup$31479a3c(this.groupId);
            if (cursor3 != null && cursor3.getCount() > 0) {
                this.initialUserList = UserProvider.usersFromCursor(cursor3);
                this.initialUserList.remove(this.me);
                userAdapter = this.adapter;
                Collection collection = this.initialUserList;
                userAdapter.list.clear();
                userAdapter.list.addAll(collection);
                userAdapter.setCheckboxes$1385ff();
                userAdapter.notifyDataSetChanged();
                this.adapter.notifyDataSetChanged();
            }
            cursor3.close();
        }
        refreshInfo();
    }

    protected void onResume() {
        super.onResume();
        if (ProgressBarLoadingDialog.getInstance().isAdded()) {
            ProgressBarLoadingDialog.getInstance().dismiss();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(2131623941, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        boolean z = false;
        switch (item.getItemId()) {
            case 16908332:
                finish();
                return true;
            case 2131362562:
                if (!this.editMode) {
                    z = true;
                }
                setEdittingGroupName(z);
                return true;
            default:
                return false;
        }
    }

    public void onClickCompose(View view) {
        if (this.groupId != null) {
            Intent intent = new Intent(this, ChatInitialForGroupChatActivity.class);
            intent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, this.me.userId + "-" + this.groupId);
            intent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_GROUP_ID, this.groupId);
            startActivity(intent);
        } else if (this.newUsers != null) {
            if (getActionBarEditText() != null) {
                Utils.hideKeyboard(getActionBarEditText(), getApplicationContext());
            }
            if (this.adapter.getCount() != 0 && isAnyoneSelected()) {
                ProgressBarDialogLogin.getInstance().show(getSupportFragmentManager(), BuildConfig.VERSION_NAME);
                new Thread(new C07421()).start();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please add users to the group before composing", 0).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACTS_REQUEST && resultCode == -1) {
            this.newUsers = data.getExtras().getParcelableArrayList(EXTRA_CONTACTS);
            Iterator it = this.newUsers.iterator();
            while (it.hasNext()) {
                Log.v(TAG, "onActivityResult: adding selected user " + ((User) it.next()));
            }
            this.adapter.addUsers(this.newUsers);
        }
        refreshInfo();
    }

    public void onClickAdd(View view) {
        Intent intent = new Intent(this, CreateGroupAddUsersActivity.class);
        intent.putExtra(CreateGroupAddUsersActivity.EXTRA_IGNORED_USERS, this.adapter.getUsers());
        startActivityForResult(intent, PICK_CONTACTS_REQUEST);
    }

    private boolean isAnyoneSelected() {
        User[] users = this.adapter.getUsers();
        int length = users.length;
        for (int i = 0; i < length; i += PICK_CONTACTS_REQUEST) {
            if (users[i].checked) {
                return true;
            }
        }
        return false;
    }

    public void onClickSave(View view) {
        if (!PreferenceConstants.CONNECTED_TO_NETWORK) {
            new PopUpUtil().getPopFailed$40a28a58(this, "Group could not be created").show();
        } else if (this.adapter.getCount() == PICK_CONTACTS_REQUEST) {
            Toast.makeText(getApplicationContext(), 2131493003, 0).show();
        } else {
            if (getActionBarEditText() != null) {
                Utils.hideKeyboard(getActionBarEditText(), getApplicationContext());
            }
            if (this.adapter.getCount() != 0 && isAnyoneSelected()) {
                ProgressBarDialogLogin.getInstance().show(getSupportFragmentManager(), BuildConfig.VERSION_NAME);
                new Thread(new C07452()).start();
            }
        }
    }

    private void setEdittingGroupName(boolean editting) {
        String lastName = getGroupName();
        ActionBar actionBar = getSupportActionBar();
        this.editMode = editting;
        TextView view;
        if (this.editMode) {
            actionBar.setCustomView(2130903041);
            TextView editText = (EditText) actionBar.getCustomView().findViewById(2131361836);
            editText.clearFocus();
            editText.setOnEditorActionListener(new C07463());
            view = editText;
            if (lastName != null) {
                view.setText(lastName);
            }
            editText.clearFocus();
            editText.setSelection(0, editText.getText().length());
            Utils.showKeyboard(editText, this);
            return;
        }
        if (actionBar.getCustomView() != null) {
            Utils.hideKeyboard((EditText) actionBar.getCustomView().findViewById(2131361836), this);
        }
        actionBar.setCustomView(2130903042);
        view = (TextView) getSupportActionBar().getCustomView().findViewById(2131361837);
        if (lastName != null) {
            view.setText(lastName);
        }
    }

    private EditText getActionBarEditText() {
        if (this.editMode) {
            return (EditText) getSupportActionBar().getCustomView().findViewById(2131361836);
        }
        return null;
    }

    private String getGroupName() {
        if (getSupportActionBar().getCustomView() == null) {
            return null;
        }
        TextView view;
        if (this.editMode) {
            view = getActionBarEditText();
        } else {
            view = (TextView) getSupportActionBar().getCustomView().findViewById(2131361837);
        }
        return view.getText().toString();
    }

    private void setGroupName(String name) {
        if (getSupportActionBar().getCustomView() != null) {
            TextView view;
            if (this.editMode) {
                view = (EditText) getSupportActionBar().getCustomView().findViewById(2131361836);
            } else {
                view = (TextView) getSupportActionBar().getCustomView().findViewById(2131361837);
            }
            view.setText(name);
        }
    }

    private void refreshInfo() {
        if (this.adapter.getCount() == 0) {
            this.noUsersImage.setVisibility(0);
            this.noUsersText.setVisibility(0);
            this.usersListView.setVisibility(8);
        } else {
            this.usersListView.setVisibility(0);
            this.noUsersImage.setVisibility(8);
            this.noUsersText.setVisibility(8);
        }
        this.adapter.getCount();
        this.saveButton.setVisibility(8);
    }

    private boolean SubscribeToMqtt(String groupHashCode) {
        String canonicalName;
        StringBuilder stringBuilder;
        String groupAdminUserId = this.me.userId;
        String groupAdminUserPhone = this.me.username;
        if (groupAdminUserPhone.startsWith(MqttTopic.SINGLE_LEVEL_WILDCARD)) {
            groupAdminUserPhone = groupAdminUserPhone.substring(PICK_CONTACTS_REQUEST);
        }
        String groupName = getGroupName();
        JSONObject groupCreationObject = new JSONObject();
        JSONArray groupAddedUsers = new JSONArray();
        Log.e("TOPIC ADMIN ID", groupAdminUserId);
        Log.e("TOPIC ADMIN USERNAME", groupAdminUserPhone);
        try {
            groupCreationObject.put("topic_title", groupName);
            JSONObject adminDetails = new JSONObject();
            adminDetails.put("phone", groupAdminUserPhone);
            adminDetails.put(ChatActivity.INTENT_EXTRA_USER_ID, Integer.parseInt(groupAdminUserId));
            groupCreationObject.put("admin", adminDetails);
            int i = 0;
            while (true) {
                if (i >= this.groupUsers.size()) {
                    break;
                }
                JSONObject groupAddedUser = new JSONObject();
                String userPhoneNumber = ((Model) this.groupUsers.get(i)).phone;
                if (userPhoneNumber.startsWith(MqttTopic.SINGLE_LEVEL_WILDCARD)) {
                    userPhoneNumber = userPhoneNumber.substring(PICK_CONTACTS_REQUEST);
                }
                groupAddedUser.put("phone", userPhoneNumber);
                groupAddedUser.put(ChatActivity.INTENT_EXTRA_USER_ID, Integer.parseInt(((Model) this.groupUsers.get(i)).userId));
                groupAddedUsers.put(groupAddedUser);
                i += PICK_CONTACTS_REQUEST;
            }
            groupCreationObject.put("users", groupAddedUsers);
            Log.e("GROUP CREATION JSON STRING", groupCreationObject.toString());
            System.out.println("create group users:" + groupCreationObject.toString());
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(60, TimeUnit.SECONDS);
            client.setReadTimeout(60, TimeUnit.SECONDS);
            RequestBody formBody = RequestBody.create(JSON, groupCreationObject.toString());
            Response response = client.newCall(new Builder().url("http://social.rabtcdn.com/groups/api/v1/topics/create/").post(formBody).build()).execute();
            if (response.isSuccessful()) {
                String stringResponse = response.body().string();
                response.body().close();
                System.out.println(stringResponse);
                JSONObject jSONObject = new JSONObject(stringResponse);
                if (jSONObject.getString(NotificationCompatApi21.CATEGORY_STATUS).equals("200")) {
                    Log.e("Group Creation Response", stringResponse);
                    try {
                        this.groupHashcode = jSONObject.getJSONObject("topic").getString("hashcode");
                        String topic = "groups/" + this.groupHashcode;
                        String[] topics = new String[PICK_CONTACTS_REQUEST];
                        topics[0] = topic;
                        IMqttActionListener callback = new ActionListener(SHAMChatApplication.getMyApplicationContext(), Action.SUBSCRIBE$621bd8f2, this.clientHandle, topics);
                        try {
                            Connections.getInstance(SHAMChatApplication.getMyApplicationContext()).getConnection(this.clientHandle).client.subscribe(topic, PICK_CONTACTS_REQUEST, null, callback);
                        } catch (MqttSecurityException e) {
                            canonicalName = getClass().getCanonicalName();
                            stringBuilder = new StringBuilder("Failed to subscribe to");
                            Log.e(canonicalName, r27.append(topic).append(" the client with the handle ").append(this.clientHandle).toString(), e);
                            this.groupHashcode = null;
                            callback.onFailure(null, e);
                        } catch (MqttException e2) {
                            canonicalName = getClass().getCanonicalName();
                            stringBuilder = new StringBuilder("Failed to subscribe to");
                            Log.e(canonicalName, r27.append(topic).append(" the client with the handle ").append(this.clientHandle).toString(), e2);
                            this.groupHashcode = null;
                            callback.onFailure(null, e2);
                        }
                    } catch (Exception e1) {
                        this.groupHashcode = null;
                        e1.printStackTrace();
                    }
                } else {
                    this.groupHashcode = null;
                    Log.e("****************** GROUP CREATION ERROR", "server API status error");
                }
                if (this.groupHashcode == null) {
                    return false;
                }
                return true;
            }
            throw new IOException("Unexpected code " + response);
        } catch (Exception e3) {
            e3.printStackTrace();
            this.groupHashcode = null;
            return false;
        }
    }
}

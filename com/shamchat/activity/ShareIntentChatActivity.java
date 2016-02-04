package com.shamchat.activity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.path.android.jobqueue.JobManager;
import com.shamchat.adapters.ShareIntentChatAdapter;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.events.SyncContactsCompletedEvent;
import com.shamchat.jobs.SyncContactsJob;
import com.shamchat.models.FriendGroup;
import com.shamchat.models.ShareIntentItem;
import com.shamchat.utils.PopUpUtil;
import com.shamchat.utils.Utils;
import de.greenrobot.event.EventBus;
import java.io.File;
import java.util.ArrayList;

public class ShareIntentChatActivity extends AppCompatActivity {
    private ShareIntentChatAdapter adapter;
    private EditText editSearch;
    private String forwardedMessage;
    private Intent intentShare;
    private String intentShareType;
    private JobManager jobManager;
    private ListView list;
    private ArrayList<ShareIntentItem> listItemsArray;
    Runnable mFilterTask;
    private Handler mHandler;
    private String packetId;
    private Dialog popUp;
    private ImageView syncContacts;
    private ProgressBar syncContactsLoader;
    TextWatcher textWatcher;

    /* renamed from: com.shamchat.activity.ShareIntentChatActivity.1 */
    class C08931 implements Runnable {
        C08931() {
        }

        public final void run() {
            ShareIntentChatActivity.this.searchTrigger();
        }
    }

    /* renamed from: com.shamchat.activity.ShareIntentChatActivity.2 */
    class C08942 implements OnClickListener {
        C08942() {
        }

        public final void onClick(View v) {
            ShareIntentChatActivity.this.syncContacts.setVisibility(8);
            ShareIntentChatActivity.this.syncContactsLoader.setVisibility(0);
            ShareIntentChatActivity.this.jobManager.addJobInBackground(new SyncContactsJob(0));
        }
    }

    /* renamed from: com.shamchat.activity.ShareIntentChatActivity.3 */
    class C08953 implements OnClickListener {
        C08953() {
        }

        public final void onClick(View v) {
            ShareIntentChatActivity.this.popUp.dismiss();
        }
    }

    /* renamed from: com.shamchat.activity.ShareIntentChatActivity.4 */
    class C08964 implements TextWatcher {
        C08964() {
        }

        public final void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public final void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public final void afterTextChanged(Editable s) {
            ShareIntentChatActivity.this.mHandler.removeCallbacks(ShareIntentChatActivity.this.mFilterTask);
            ShareIntentChatActivity.this.mHandler.postDelayed(ShareIntentChatActivity.this.mFilterTask, 1000);
        }
    }

    public ShareIntentChatActivity() {
        this.listItemsArray = new ArrayList();
        this.forwardedMessage = null;
        this.packetId = null;
        this.intentShareType = null;
        this.intentShare = null;
        this.mHandler = new Handler();
        this.mFilterTask = new C08931();
        this.textWatcher = new C08964();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903079);
        this.list = (ListView) findViewById(2131361964);
        this.editSearch = (EditText) findViewById(2131361960);
        this.editSearch.addTextChangedListener(this.textWatcher);
        initializeActionBar();
        this.syncContacts = (ImageView) findViewById(2131361962);
        this.syncContactsLoader = (ProgressBar) findViewById(2131361963);
        try {
            EventBus.getDefault().register(this, true, 9);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        this.syncContacts.setOnClickListener(new C08942());
        this.forwardedMessage = getIntent().getStringExtra("chatMessage");
        this.packetId = getIntent().getStringExtra("packetID");
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if ("android.intent.action.SEND".equals(action) && type != null) {
            this.intentShare = intent;
            if ("text/plain".equals(type)) {
                this.intentShareType = AddFavoriteTextActivity.EXTRA_RESULT_TEXT;
            } else if (type.startsWith("image/")) {
                this.intentShareType = "image";
            } else if (type.startsWith("video/")) {
                this.intentShareType = "video";
            } else if (type.startsWith("audio/")) {
                this.intentShareType = "audio";
            }
        } else if ("android.intent.action.SEND_MULTIPLE".equals(action) && type != null) {
            this.intentShare = intent;
            if ("text/plain".equals(type)) {
                this.intentShareType = "text_multi";
            } else if (type.startsWith("image/")) {
                this.intentShareType = "image_multi";
            } else if (type.startsWith("video/")) {
                this.intentShareType = "video_multi";
            } else if (type.startsWith("audio/")) {
                this.intentShareType = "audio_multi";
            }
        }
    }

    public void onEventMainThread(SyncContactsCompletedEvent event) {
        this.syncContacts.setVisibility(0);
        this.syncContactsLoader.setVisibility(8);
    }

    public void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        ArrayList<ShareIntentItem> allUsers = getAllUsers();
        ArrayList<ShareIntentItem> allGroups = getAllGroups();
        ArrayList<ShareIntentItem> usersAndGroups = new ArrayList();
        if (allGroups != null) {
            usersAndGroups.addAll(allGroups);
        }
        if (allUsers != null) {
            usersAndGroups.addAll(allUsers);
        }
        this.adapter = new ShareIntentChatAdapter(getApplicationContext(), usersAndGroups, this);
        this.list.setAdapter(this.adapter);
    }

    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                setResult(0);
                finish();
                return true;
            default:
                return false;
        }
    }

    public void sendToChat(String recipientId, String threadId, boolean isGroup) {
        if (this.intentShare != null) {
            String messageBody = null;
            int messageContentType = 0;
            if (this.intentShareType.equals(AddFavoriteTextActivity.EXTRA_RESULT_TEXT)) {
                messageContentType = MessageContentType.TEXT.ordinal();
                messageBody = this.intentShare.getStringExtra("android.intent.extra.TEXT");
            } else if (this.intentShareType.equals("image") || this.intentShareType.equals("video") || this.intentShareType.equals("audio")) {
                Uri imageUri = (Uri) this.intentShare.getParcelableExtra("android.intent.extra.STREAM");
                if (this.intentShareType.equals("image")) {
                    messageContentType = MessageContentType.IMAGE.ordinal();
                } else if (this.intentShareType.equals("video")) {
                    messageContentType = MessageContentType.VIDEO.ordinal();
                } else if (this.intentShareType.equals("audio")) {
                    messageContentType = MessageContentType.VOICE_RECORD.ordinal();
                }
                if (imageUri != null) {
                    if (imageUri.toString().startsWith("file://")) {
                        messageBody = imageUri.getPath();
                    } else if (imageUri.toString().startsWith("content://")) {
                        messageBody = Utils.getFilePathImage(this, imageUri);
                    } else {
                        File file = new File(imageUri.getPath());
                        if (file.exists()) {
                            messageBody = file.getAbsolutePath();
                        }
                    }
                }
                messageBody = null;
                popupNotSupported();
            }
            if (messageBody == null) {
                return;
            }
            Intent intent;
            if (isGroup) {
                intent = new Intent(getApplicationContext(), ChatInitialForGroupChatActivity.class);
                intent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_GROUP_ID, recipientId);
                intent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, threadId);
                intent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_MESSAGE_BODY, messageBody);
                intent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_MESSAGE_CONTENT_TYPE, String.valueOf(messageContentType));
                startActivity(intent);
                return;
            }
            intent = new Intent(getApplicationContext(), ChatActivity.class);
            intent.putExtra(ChatActivity.INTENT_EXTRA_USER_ID, recipientId);
            intent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, threadId);
            intent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_MESSAGE_BODY, messageBody);
            intent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_MESSAGE_CONTENT_TYPE, String.valueOf(messageContentType));
            startActivity(intent);
        }
    }

    public void popupNotSupported() {
        PopUpUtil popUpUtil = new PopUpUtil();
        getResources().getString(2131493375);
        this.popUp = PopUpUtil.getPopFailed$478dbc03(this, getResources().getString(2131493376), new C08953());
        this.popUp.show();
    }

    public void createChatSingle(String friendUserId) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        ContentResolver contentResolver = getContentResolver();
        String currentUserId = SHAMChatApplication.getConfig().userId;
        String threadId1 = currentUserId + "-" + friendUserId;
        String threadId2 = friendUserId + "-" + currentUserId;
        Cursor threadCursor = contentResolver.query(ChatProviderNew.CONTENT_URI_THREAD, new String[]{ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID}, "thread_id=? OR thread_id=?", new String[]{threadId1, threadId2}, null);
        if (threadCursor.getCount() > 0) {
            threadCursor.moveToFirst();
            threadCursor.getString(threadCursor.getColumnIndex(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID));
        }
        threadCursor.close();
        sendToChat(friendUserId, threadId1, false);
    }

    private void initializeActionBar() {
        getSupportActionBar().setDisplayOptions(31);
    }

    public void searchTrigger() {
        this.adapter.getFilter().filter(this.editSearch.getText());
    }

    public ArrayList<ShareIntentItem> getAllGroups() {
        Cursor cursor = getContentResolver().query(UserProvider.CONTENT_URI_GROUP, null, null, null, FriendGroup.DB_NAME + " ASC");
        ArrayList<ShareIntentItem> list = new ArrayList();
        try {
            if (cursor.moveToFirst()) {
                while (true) {
                    int indexColName = cursor.getColumnIndex(FriendGroup.DB_NAME);
                    int indexGroupiD = cursor.getColumnIndex(FriendGroup.DB_ID);
                    String phoneNumberOrGroupAlias = cursor.getString(indexColName);
                    String userIdOrGroupId = cursor.getString(indexGroupiD);
                    if (phoneNumberOrGroupAlias == null) {
                        phoneNumberOrGroupAlias = "NOTHING";
                    }
                    list.add(new ShareIntentItem(phoneNumberOrGroupAlias, userIdOrGroupId, true));
                    if (cursor.moveToNext() == false) goto L_0x0054;
                }
                return list;
            }
            break;
            return list;
        } finally {
            try {
                cursor.close();
            } catch (Exception e) {
            }
        }
    }

    public ArrayList<ShareIntentItem> getAllUsers() {
        Cursor cursor = getContentResolver().query(UserProvider.CONTENT_URI_USER, null, "user_type=? AND userId!=?", new String[]{"2", SHAMChatApplication.getConfig().userId}, null);
        ArrayList<ShareIntentItem> list = new ArrayList();
        try {
            if (cursor.moveToFirst()) {
                while (true) {
                    int indexColName = cursor.getColumnIndex("name");
                    int indexUserId = cursor.getColumnIndex("userId");
                    int indexMobileNo = cursor.getColumnIndex("mobileNo");
                    String displayName = cursor.getString(indexColName);
                    String phoneNumberOrGroupAlias = cursor.getString(indexMobileNo);
                    String userIdOrGroupId = cursor.getString(indexUserId);
                    if (phoneNumberOrGroupAlias == null) {
                        phoneNumberOrGroupAlias = "NOTHING";
                    }
                    ShareIntentItem item = new ShareIntentItem(phoneNumberOrGroupAlias, userIdOrGroupId, false);
                    if (displayName != null) {
                        item.displayName = displayName;
                    }
                    list.add(item);
                    if (cursor.moveToNext() == false) goto L_0x005f;
                }
                return list;
            }
            break;
            return list;
        } finally {
            try {
                cursor.close();
            } catch (Exception e) {
            }
        }
    }
}

package com.shamchat.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.path.android.jobqueue.JobManager;
import com.shamchat.adapters.ChatComposeIndivdualAdapter;
import com.shamchat.adapters.MyMessageType;
import com.shamchat.androidclient.ChatController;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.events.SyncContactsCompletedEvent;
import com.shamchat.jobs.SyncContactsJob;
import com.shamchat.models.ChatMessage;
import com.shamchat.models.ChatMessage.MessageStatusType;
import com.shamchat.utils.Utils;
import de.greenrobot.event.EventBus;
import java.util.Date;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.jivesoftware.smack.packet.Message;

public class ComposeIndividualChatActivity extends AppCompatActivity {
    private ChatComposeIndivdualAdapter adapter;
    Button all;
    private EditText editSearch;
    private String forwardedMessage;
    private JobManager jobManager;
    private ListView list;
    private String packetId;
    Button salami;
    private ImageView syncContacts;
    private ProgressBar syncContactsLoader;
    TextWatcher textWatcher;

    /* renamed from: com.shamchat.activity.ComposeIndividualChatActivity.1 */
    class C07231 implements OnClickListener {
        C07231() {
        }

        public final void onClick(View v) {
            ComposeIndividualChatActivity.this.syncContacts.setVisibility(8);
            ComposeIndividualChatActivity.this.syncContactsLoader.setVisibility(0);
            ComposeIndividualChatActivity.this.jobManager.addJobInBackground(new SyncContactsJob(0));
            ComposeIndividualChatActivity.this.adapter = new ChatComposeIndivdualAdapter(ComposeIndividualChatActivity.this.getApplicationContext(), ComposeIndividualChatActivity.this.getContentResolver().query(UserProvider.CONTENT_URI_USER, null, "user_type=? AND userId!=?", new String[]{"2", SHAMChatApplication.getConfig().userId}, null), false, ComposeIndividualChatActivity.this);
            ComposeIndividualChatActivity.this.list.setAdapter(ComposeIndividualChatActivity.this.adapter);
        }
    }

    /* renamed from: com.shamchat.activity.ComposeIndividualChatActivity.2 */
    class C07242 implements OnClickListener {
        C07242() {
        }

        public final void onClick(View v) {
            ComposeIndividualChatActivity.this.all.setBackgroundResource(2130837724);
            ComposeIndividualChatActivity.this.salami.setBackgroundResource(2130837720);
            ComposeIndividualChatActivity.this.adapter = new ChatComposeIndivdualAdapter(ComposeIndividualChatActivity.this.getApplicationContext(), ComposeIndividualChatActivity.this.getContentResolver().query(UserProvider.CONTENT_URI_USER, null, "user_type<? AND userId!=?", new String[]{"3", SHAMChatApplication.getConfig().userId}, null), false, ComposeIndividualChatActivity.this);
            ComposeIndividualChatActivity.this.list.setAdapter(ComposeIndividualChatActivity.this.adapter);
        }
    }

    /* renamed from: com.shamchat.activity.ComposeIndividualChatActivity.3 */
    class C07253 implements OnClickListener {
        C07253() {
        }

        public final void onClick(View v) {
            ComposeIndividualChatActivity.this.all.setBackgroundResource(2130837723);
            ComposeIndividualChatActivity.this.salami.setBackgroundResource(2130837731);
            ComposeIndividualChatActivity.this.adapter = new ChatComposeIndivdualAdapter(ComposeIndividualChatActivity.this.getApplicationContext(), ComposeIndividualChatActivity.this.getContentResolver().query(UserProvider.CONTENT_URI_USER, null, "user_type=? AND userId!=?", new String[]{"2", SHAMChatApplication.getConfig().userId}, null), false, ComposeIndividualChatActivity.this);
            ComposeIndividualChatActivity.this.list.setAdapter(ComposeIndividualChatActivity.this.adapter);
        }
    }

    /* renamed from: com.shamchat.activity.ComposeIndividualChatActivity.4 */
    class C07274 implements Runnable {
        final /* synthetic */ ContentResolver val$contentResolver;
        final /* synthetic */ String val$currentUserId;
        final /* synthetic */ String val$finalThreadId;
        final /* synthetic */ String val$friendUserId;
        final /* synthetic */ boolean val$isThreadUpdate;

        /* renamed from: com.shamchat.activity.ComposeIndividualChatActivity.4.1 */
        class C07261 implements Runnable {
            C07261() {
            }

            public final void run() {
                Intent intent = new Intent(ComposeIndividualChatActivity.this.getApplicationContext(), ChatActivity.class);
                intent.putExtra(ChatActivity.INTENT_EXTRA_USER_ID, C07274.this.val$friendUserId);
                intent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, C07274.this.val$finalThreadId);
                ComposeIndividualChatActivity.this.startActivity(intent);
                ProgressBarLoadingDialog.getInstance().dismiss();
                ComposeIndividualChatActivity.this.finish();
            }
        }

        C07274(ContentResolver contentResolver, boolean z, String str, String str2, String str3) {
            this.val$contentResolver = contentResolver;
            this.val$isThreadUpdate = z;
            this.val$finalThreadId = str;
            this.val$friendUserId = str2;
            this.val$currentUserId = str3;
        }

        public final void run() {
            Cursor cursor = this.val$contentResolver.query(ChatProviderNew.CONTENT_URI_CHAT, null, "packet_id=?", new String[]{ComposeIndividualChatActivity.this.packetId}, null);
            if (cursor.getCount() > 0) {
                String lastMessage;
                ContentValues values;
                ContentValues contentValues;
                cursor.moveToFirst();
                ChatProviderNew chatProviderNew = new ChatProviderNew();
                ChatMessage chatMessage = ChatProviderNew.getChatMessageByCursor(cursor);
                cursor.close();
                MessageContentType messageContentType = chatMessage.messageContentType;
                boolean isLocation = false;
                switch (C07296.f13x39e42954[messageContentType.ordinal()]) {
                    case Logger.SEVERE /*1*/:
                        lastMessage = ComposeIndividualChatActivity.this.getString(2131493360);
                        break;
                    case Logger.WARNING /*2*/:
                        lastMessage = ComposeIndividualChatActivity.this.getString(2131493363);
                        break;
                    case Logger.INFO /*3*/:
                        lastMessage = ComposeIndividualChatActivity.this.getString(2131493364);
                        break;
                    case Logger.CONFIG /*4*/:
                        lastMessage = ComposeIndividualChatActivity.this.getString(2131493362);
                        break;
                    case Logger.FINE /*5*/:
                        lastMessage = ComposeIndividualChatActivity.this.getString(2131493361);
                        isLocation = true;
                        break;
                    default:
                        lastMessage = "Unknown file type";
                        break;
                }
                if (this.val$isThreadUpdate) {
                    values = new ContentValues();
                    values.put("last_message", lastMessage);
                    values.put("last_message_content_type", Integer.valueOf(messageContentType.ordinal()));
                    values.put("read_status", Integer.valueOf(0));
                    values.put("last_message_direction", Integer.valueOf(MyMessageType.OUTGOING_MSG.ordinal()));
                    values.put("last_updated_datetime", Utils.formatDate(new Date().getTime(), "yyyy/MM/dd HH:mm:ss"));
                    contentValues = values;
                    this.val$contentResolver.update(ChatProviderNew.CONTENT_URI_THREAD, contentValues, "thread_id=?", new String[]{this.val$finalThreadId});
                } else {
                    values = new ContentValues();
                    values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, this.val$finalThreadId);
                    contentValues = values;
                    contentValues.put("friend_id", this.val$friendUserId);
                    values.put("read_status", Integer.valueOf(0));
                    values.put("last_message", lastMessage);
                    values.put("last_message_content_type", Integer.valueOf(messageContentType.ordinal()));
                    values.put("last_updated_datetime", Utils.formatDate(new Date().getTime(), "yyyy/MM/dd HH:mm:ss"));
                    values.put("is_group_chat", Integer.valueOf(0));
                    contentValues = values;
                    contentValues.put("thread_owner", this.val$currentUserId);
                    values.put("last_message_direction", Integer.valueOf(MyMessageType.OUTGOING_MSG.ordinal()));
                    this.val$contentResolver.insert(ChatProviderNew.CONTENT_URI_THREAD, values);
                }
                Message message = new Message();
                values = new ContentValues();
                contentValues = values;
                contentValues.put("message_recipient", this.val$friendUserId);
                values.put("message_type", Integer.valueOf(MyMessageType.OUTGOING_MSG.ordinal()));
                values.put("packet_id", message.packetID);
                values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, this.val$finalThreadId);
                values.put("text_message", chatMessage.textMessage);
                contentValues = values;
                contentValues.put("message_sender", this.val$currentUserId);
                values.put("message_last_updated_datetime", Utils.formatDate(new Date().getTime(), "yyyy/MM/dd HH:mm:ss"));
                values.put("description", chatMessage.description);
                values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_MESSAGE_CONTENT_TYPE, Integer.valueOf(messageContentType.ordinal()));
                values.put("message_status", Integer.valueOf(MessageStatusType.SENDING.ordinal()));
                values.put("blob_message", chatMessage.blobMessage);
                values.put("file_url", chatMessage.fileUrl);
                values.put("uploaded_file_url", chatMessage.uploadedFileUrl);
                values.put("uploaded_percentage", Integer.valueOf(100));
                values.put("text_message", BuildConfig.VERSION_NAME);
                ChatController chatController = ChatController.getInstance(SHAMChatApplication.getMyApplicationContext());
                if (isLocation) {
                    values.put("latitude", Double.valueOf(chatMessage.latitude));
                    values.put("longitude", Double.valueOf(chatMessage.longitude));
                    this.val$contentResolver.insert(ChatProviderNew.CONTENT_URI_CHAT, values);
                    chatController.sendMessage(Utils.createXmppUserIdByUserId(this.val$friendUserId), chatMessage.uploadedFileUrl, messageContentType.toString(), false, message.packetID, String.valueOf(chatMessage.latitude), String.valueOf(chatMessage.longitude), chatMessage.description);
                } else {
                    this.val$contentResolver.insert(ChatProviderNew.CONTENT_URI_CHAT, values);
                    chatController.sendMessage(Utils.createXmppUserIdByUserId(this.val$friendUserId), chatMessage.uploadedFileUrl, messageContentType.toString(), false, message.packetID, null, null, chatMessage.description);
                }
                SHAMChatApplication.getInstance().runOnUiThread(new C07261());
            }
        }
    }

    /* renamed from: com.shamchat.activity.ComposeIndividualChatActivity.5 */
    class C07285 implements TextWatcher {
        C07285() {
        }

        public final void onTextChanged(CharSequence s, int start, int before, int count) {
            String changedText = s.toString();
            ComposeIndividualChatActivity.this.adapter.changeCursor(ComposeIndividualChatActivity.this.getContentResolver().query(UserProvider.CONTENT_URI_USER, null, "name LIKE ? AND user_type=? AND userId!=?", new String[]{"%" + changedText + "%", "2", SHAMChatApplication.getConfig().userId}, null));
        }

        public final void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public final void afterTextChanged(Editable s) {
        }
    }

    /* renamed from: com.shamchat.activity.ComposeIndividualChatActivity.6 */
    static /* synthetic */ class C07296 {
        static final /* synthetic */ int[] f13x39e42954;

        static {
            f13x39e42954 = new int[MessageContentType.values().length];
            try {
                f13x39e42954[MessageContentType.IMAGE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f13x39e42954[MessageContentType.VIDEO.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f13x39e42954[MessageContentType.TEXT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f13x39e42954[MessageContentType.STICKER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f13x39e42954[MessageContentType.LOCATION.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public ComposeIndividualChatActivity() {
        this.forwardedMessage = null;
        this.packetId = null;
        this.textWatcher = new C07285();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903079);
        this.list = (ListView) findViewById(2131361964);
        this.all = (Button) findViewById(2131361958);
        this.salami = (Button) findViewById(2131361957);
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
        this.syncContacts.setOnClickListener(new C07231());
        this.adapter = new ChatComposeIndivdualAdapter(getApplicationContext(), getContentResolver().query(UserProvider.CONTENT_URI_USER, null, "user_type=? AND userId!=?", new String[]{"2", SHAMChatApplication.getConfig().userId}, null), false, this);
        this.list.setAdapter(this.adapter);
        this.forwardedMessage = getIntent().getStringExtra("chatMessage");
        this.packetId = getIntent().getStringExtra("packetID");
        this.all.setOnClickListener(new C07242());
        this.salami.setOnClickListener(new C07253());
    }

    public void onEventMainThread(SyncContactsCompletedEvent event) {
        this.syncContacts.setVisibility(0);
        this.syncContactsLoader.setVisibility(8);
    }

    public void onPause() {
        super.onPause();
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

    public void finish() {
        Utils.hideKeyboard(this.editSearch, getApplicationContext());
        super.finish();
    }

    public void createChat(String friendUserId) {
        boolean isThreadUpdate;
        String finalThreadId;
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        ContentResolver contentResolver = getContentResolver();
        String currentUserId = SHAMChatApplication.getConfig().userId;
        String threadId1 = currentUserId + "-" + friendUserId;
        String threadId2 = friendUserId + "-" + currentUserId;
        Cursor threadCursor = contentResolver.query(ChatProviderNew.CONTENT_URI_THREAD, new String[]{ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID}, "thread_id=? OR thread_id=?", new String[]{threadId1, threadId2}, null);
        if (threadCursor.getCount() > 0) {
            threadCursor.moveToFirst();
            isThreadUpdate = true;
            finalThreadId = threadCursor.getString(threadCursor.getColumnIndex(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID));
        } else {
            finalThreadId = threadId1;
            isThreadUpdate = false;
        }
        threadCursor.close();
        intent.putExtra(ChatActivity.INTENT_EXTRA_USER_ID, friendUserId);
        intent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, finalThreadId);
        if (this.forwardedMessage != null) {
            intent.putExtra(ChatInitialForGroupChatActivity.FORWARDED_MESSAGE, this.forwardedMessage);
            intent.putExtra(ChatActivity.INTENT_EXTRA_USER_ID, friendUserId);
            intent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, finalThreadId);
        } else if (this.packetId != null) {
            ProgressBarLoadingDialog.getInstance().show(getSupportFragmentManager(), BuildConfig.VERSION_NAME);
            new Thread(new C07274(contentResolver, isThreadUpdate, finalThreadId, friendUserId, currentUserId)).start();
            return;
        }
        startActivity(intent);
    }

    private void initializeActionBar() {
        getSupportActionBar().setDisplayOptions(31);
    }
}

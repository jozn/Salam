package com.shamchat.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;
import com.agimind.widget.SlideHolder;
import com.path.android.jobqueue.JobManager;
import com.rokhgroup.mqtt.Notify;
import com.rokhgroup.utils.RokhPref;
import com.rokhgroup.utils.Utils;
import com.shamchat.adapters.EndlessScrollListener;
import com.shamchat.adapters.GroupChatMessageAdapter;
import com.shamchat.adapters.GroupChatMessageAdapter.C09521;
import com.shamchat.adapters.GroupOptionsAdapter;
import com.shamchat.adapters.IncomingGroupMsg;
import com.shamchat.adapters.MyMessageType;
import com.shamchat.adapters.OutgoingGroupMsg;
import com.shamchat.adapters.OutgoingGroupMsg.ViewHolder;
import com.shamchat.adapters.Row;
import com.shamchat.adapters.UserAdapter;
import com.shamchat.androidclient.ChatController;
import com.shamchat.androidclient.IXMPPChatCallback.Stub;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.androidclient.data.ZaminConfiguration;
import com.shamchat.androidclient.service.GenericService;
import com.shamchat.components.BlockingListView;
import com.shamchat.events.ChatMessagesDBLoadCompletedEvent;
import com.shamchat.events.ChatMessagesPartDBLoadCompletedEvent;
import com.shamchat.events.CloseGroupActivityEvent;
import com.shamchat.events.FileUploadingProgressEvent;
import com.shamchat.events.ImageDownloadProgressEvent;
import com.shamchat.events.MessageDeletedEvent;
import com.shamchat.events.MessageStateChangedEvent;
import com.shamchat.events.NewGroupMessageSentFailedEvent;
import com.shamchat.events.NewGroupMessageSentSuccessEvent;
import com.shamchat.events.NewMessageEvent;
import com.shamchat.events.RemoveFromGroupMembersList;
import com.shamchat.events.SendStickerToGroupEvent;
import com.shamchat.events.TypingStatusEvent;
import com.shamchat.events.UpdateGroupMembersList;
import com.shamchat.events.VideoDownloadProgressEvent;
import com.shamchat.jobs.ChatMessagesPartDBLoadJob;
import com.shamchat.jobs.ChatThreadsDBLoadJob;
import com.shamchat.jobs.FileUploadJob;
import com.shamchat.jobs.PublishToTopicJob;
import com.shamchat.jobs.RoomInviteJob;
import com.shamchat.jobs.RoomLeaveJob;
import com.shamchat.models.ChatMessage;
import com.shamchat.models.ChatMessage.MessageStatusType;
import com.shamchat.models.FriendGroup;
import com.shamchat.models.FriendGroupMember;
import com.shamchat.models.User;
import com.shamchat.stickers.SmileyAndStickerFragment;
import com.shamchat.utils.Emoticons;
import com.squareup.picasso.Picasso;
import cz.msebera.android.httpclient.entity.StringEntity;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatInitialForGroupChatActivity extends AppCompatActivity implements OnClickListener {
    public static final String EXTRA_CONTACTS = "extra_contacts";
    public static final String FORWARDED_MESSAGE = "forwarded_message";
    public static final String INTENT_EXTRA_GROUP_ID = "group_id";
    public static final String INTENT_EXTRA_MESSAGE_BODY = "message_body";
    public static final String INTENT_EXTRA_MESSAGE_CONTENT_TYPE = "message_content_type";
    public static final String INTENT_EXTRA_THREAD_ID = "thread_id";
    private static TextView chatStateText;
    private static String groupId;
    RokhPref Session;
    ActionBar actionBar;
    GroupOptionsAdapter adapter;
    private int allMessageCount;
    private ImageView btnAddChatItems;
    private ImageView btnAddSmilies;
    private ImageView btnAddVoice;
    private Button btnLoadMore;
    private ImageView btnSend;
    private Stub callback;
    private ChatController chatController;
    private Handler chatHandler;
    private GroupChatMessageAdapter chatMessageAdapter;
    private ChatProviderNew chatProvider;
    String clientHandle;
    private int currentLoadedIndex;
    private String currentUserId;
    private Bundle fragmentBundle;
    private FragmentManager fragmentManager;
    private FriendGroup group;
    private String groupHashcode;
    List<FriendGroupMember> groupMembers;
    private String groupOwnerId;
    List<Model> groupUsers;
    private ArrayList<User> initialUserList;
    private boolean isAlreadyVisible;
    private JobManager jobManager;
    private LinearLayout layoutSendItems;
    private LinearLayout linn;
    private BlockingListView list;
    private int loadIndex;
    private RelativeLayout loadingView;
    protected ZaminConfiguration mConfig;
    private ContentResolver mContentResolver;
    private MainChatItemsFragment mainChatItems;
    private Handler mainHandler;
    private User me;
    private boolean newMessagesAreThereToBeDisplayed;
    private ArrayList<User> newUsers;
    ProgressDialog pd;
    private ProgressBar progressBarChatLoading;
    private ProgressBar progressBarLoad;
    private boolean scrollToBottom;
    List<User> selectedUserList;
    private boolean showInitialLoadButton;
    public int silents;
    SlideHolder slid;
    private SmileyAndStickerFragment smileyAndSticker;
    private SharedPreferences sp;
    private StringEntity stringEntity;
    private String threadId;
    private FragmentTransaction transaction;
    private TextView txtLastSeen;
    private EditText txtMessage;
    private TextWatcher txtMessageWatcher;
    private UserAdapter uadapter;
    private Map<String, String> uploadedMessagesRequestedToBeSent;
    ImageView vl;
    private ShamVoiceRecording voiceRcording;

    /* renamed from: com.shamchat.activity.ChatInitialForGroupChatActivity.15 */
    class AnonymousClass15 extends Thread {
        final /* synthetic */ MessageStateChangedEvent val$event;

        AnonymousClass15(MessageStateChangedEvent messageStateChangedEvent) {
            this.val$event = messageStateChangedEvent;
        }

        public final void run() {
            int i = 0;
            while (i < 4) {
                try {
                    boolean result = ChatInitialForGroupChatActivity.this.chatMessageAdapter.updateMessageStatus(this.val$event.packetId, this.val$event.messageStatusType);
                    Thread.sleep(2000);
                    if (!result) {
                        i++;
                    } else {
                        return;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    /* renamed from: com.shamchat.activity.ChatInitialForGroupChatActivity.18 */
    class AnonymousClass18 implements Runnable {
        final /* synthetic */ boolean val$isTyping;
        final /* synthetic */ String val$username;

        AnonymousClass18(boolean z, String str) {
            this.val$isTyping = z;
            this.val$username = str;
        }

        public final void run() {
            if (this.val$isTyping) {
                ChatInitialForGroupChatActivity.chatStateText.setText(BuildConfig.VERSION_NAME);
                ChatInitialForGroupChatActivity.chatStateText.setVisibility(0);
                ChatInitialForGroupChatActivity.chatStateText.setText(this.val$username + " " + SHAMChatApplication.getInstance().getString(2131493177));
                return;
            }
            ChatInitialForGroupChatActivity.chatStateText.setVisibility(8);
            ChatInitialForGroupChatActivity.chatStateText.setText(BuildConfig.VERSION_NAME);
        }
    }

    /* renamed from: com.shamchat.activity.ChatInitialForGroupChatActivity.1 */
    class C07011 implements OnTouchListener {
        C07011() {
        }

        public final boolean onTouch(View v, MotionEvent event) {
            return false;
        }
    }

    /* renamed from: com.shamchat.activity.ChatInitialForGroupChatActivity.2 */
    class C07082 extends EndlessScrollListener {

        /* renamed from: com.shamchat.activity.ChatInitialForGroupChatActivity.2.1 */
        class C07041 extends Thread {
            final /* synthetic */ int val$firstVisibleItem;

            /* renamed from: com.shamchat.activity.ChatInitialForGroupChatActivity.2.1.1 */
            class C07031 implements Runnable {
                final /* synthetic */ int val$numberOfItemsAdded;

                /* renamed from: com.shamchat.activity.ChatInitialForGroupChatActivity.2.1.1.1 */
                class C07021 implements Runnable {
                    C07021() {
                    }

                    public final void run() {
                        Log.i("EndlessScroll", "loading finished...");
                        C07082.this.loading = false;
                    }
                }

                C07031(int i) {
                    this.val$numberOfItemsAdded = i;
                }

                public final void run() {
                    int top = 0;
                    ChatInitialForGroupChatActivity.this.list.setVerticalScrollBarEnabled(false);
                    View v = ChatInitialForGroupChatActivity.this.list.getChildAt(0);
                    if (v != null) {
                        top = v.getTop() - v.getPaddingTop();
                    }
                    int index = C07041.this.val$firstVisibleItem + this.val$numberOfItemsAdded;
                    ChatInitialForGroupChatActivity.this.chatMessageAdapter.notifyDataSetChanged();
                    ChatInitialForGroupChatActivity.this.list.setSelectionFromTop(index, top);
                    ChatInitialForGroupChatActivity.this.list.setVerticalScrollBarEnabled(true);
                    ChatInitialForGroupChatActivity.this.list.post(new C07021());
                }
            }

            C07041(int i) {
                this.val$firstVisibleItem = i;
            }

            public final void run() {
                Log.i("EndlessScroll", "loading started...");
                C07082.this.loading = true;
                ArrayList arrayList = new ArrayList();
                ArrayList<ChatMessage> loadedMessages = ChatInitialForGroupChatActivity.this.chatMessageAdapter.loadDataSet$6ba208b4(0, ChatInitialForGroupChatActivity.this.threadId, 0, ChatInitialForGroupChatActivity.this.loadIndex, "Desc");
                ChatInitialForGroupChatActivity.this.chatMessageAdapter.addList(loadedMessages, 0);
                int numberOfItemsAdded = loadedMessages.size();
                if (ChatInitialForGroupChatActivity.this != null) {
                    ChatInitialForGroupChatActivity.this.runOnUiThread(new C07031(numberOfItemsAdded));
                }
            }
        }

        /* renamed from: com.shamchat.activity.ChatInitialForGroupChatActivity.2.2 */
        class C07072 extends Thread {
            final /* synthetic */ int val$firstVisibleItem;

            /* renamed from: com.shamchat.activity.ChatInitialForGroupChatActivity.2.2.1 */
            class C07061 implements Runnable {

                /* renamed from: com.shamchat.activity.ChatInitialForGroupChatActivity.2.2.1.1 */
                class C07051 implements Runnable {
                    C07051() {
                    }

                    public final void run() {
                        Log.i("EndlessScroll", "loading finished...");
                        C07082.this.loading = false;
                    }
                }

                C07061() {
                }

                public final void run() {
                    int top = 0;
                    ChatInitialForGroupChatActivity.this.list.setVerticalScrollBarEnabled(false);
                    View v = ChatInitialForGroupChatActivity.this.list.getChildAt(0);
                    if (v != null) {
                        top = v.getTop() - v.getPaddingTop();
                    }
                    int index = C07072.this.val$firstVisibleItem;
                    Log.i("EndlessScroll", "refreshing list adapter...");
                    Log.i("EndlessScroll", "chatMessageAdapter last item: " + ChatInitialForGroupChatActivity.this.chatMessageAdapter.getItem(ChatInitialForGroupChatActivity.this.chatMessageAdapter.getCount() - 1).getChatMessageObject().textMessage);
                    ChatInitialForGroupChatActivity.this.chatMessageAdapter.notifyDataSetChanged();
                    ChatInitialForGroupChatActivity.this.list.setSelectionFromTop(index, top);
                    ChatInitialForGroupChatActivity.this.list.setVerticalScrollBarEnabled(true);
                    ChatInitialForGroupChatActivity.this.list.post(new C07051());
                }
            }

            C07072(int i) {
                this.val$firstVisibleItem = i;
            }

            public final void run() {
                Log.i("EndlessScroll", "loading started...");
                C07082.this.loading = true;
                GroupChatMessageAdapter access$300 = ChatInitialForGroupChatActivity.this.chatMessageAdapter;
                String str = BuildConfig.VERSION_NAME;
                int i = 1;
                while (!str.contains("packet")) {
                    str = (String) access$300.arrkey.get(access$300.arrkey.size() - i);
                    i++;
                }
                int index = access$300.arrkey.size() - (i - 1);
                ArrayList arrayList = new ArrayList();
                ArrayList<ChatMessage> loadedMessages = ChatInitialForGroupChatActivity.this.chatMessageAdapter.loadDataSet$6ba208b4(index, ChatInitialForGroupChatActivity.this.threadId, 1, ChatInitialForGroupChatActivity.this.loadIndex, "Asc");
                ChatInitialForGroupChatActivity.this.chatMessageAdapter.addList(loadedMessages, 1);
                Log.i("EndlessScroll", "Last item added: " + ((ChatMessage) loadedMessages.get(loadedMessages.size() - 1)).textMessage);
                loadedMessages.size();
                if (ChatInitialForGroupChatActivity.this != null) {
                    ChatInitialForGroupChatActivity.this.runOnUiThread(new C07061());
                }
            }
        }

        C07082() {
        }

        public final void onScrollStateChanged(AbsListView view, int scrollState) {
            super.onScrollStateChanged(view, scrollState);
            Picasso picasso = Picasso.with(ChatInitialForGroupChatActivity.this);
            if (scrollState == 2) {
                picasso.pauseTag("groupChatListViewImages");
                if (ChatInitialForGroupChatActivity.this.loadingView.getVisibility() == 0) {
                    ChatInitialForGroupChatActivity.this.loadingView.setVisibility(8);
                    return;
                }
                return;
            }
            picasso.resumeTag("groupChatListViewImages");
        }

        public final boolean onLoadMore$255f299(int firstVisibleItem) {
            Thread c07041;
            if (this.scrollDirection == 0) {
                c07041 = new C07041(firstVisibleItem);
                if (!ChatInitialForGroupChatActivity.this.chatMessageAdapter.reachedLastRecord(ChatInitialForGroupChatActivity.this.threadId, 0)) {
                    c07041.start();
                    try {
                        c07041.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                c07041 = new C07072(firstVisibleItem);
                if (!ChatInitialForGroupChatActivity.this.chatMessageAdapter.reachedLastRecord(ChatInitialForGroupChatActivity.this.threadId, 1)) {
                    c07041.start();
                    try {
                        c07041.join();
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
            }
            return true;
        }
    }

    /* renamed from: com.shamchat.activity.ChatInitialForGroupChatActivity.3 */
    class C07093 implements OnTouchListener {
        C07093() {
        }

        public final boolean onTouch(View v, MotionEvent event) {
            try {
                ChatInitialForGroupChatActivity.this.layoutSendItems.setVisibility(8);
                ChatInitialForGroupChatActivity.this.fragmentManager.beginTransaction().remove(ChatInitialForGroupChatActivity.this.mainChatItems).commit();
                ChatInitialForGroupChatActivity.this.fragmentManager.beginTransaction().remove(ChatInitialForGroupChatActivity.this.voiceRcording).commit();
                ChatInitialForGroupChatActivity.this.fragmentManager.beginTransaction().remove(ChatInitialForGroupChatActivity.this.smileyAndSticker).commit();
            } catch (Exception e) {
            }
            return false;
        }
    }

    /* renamed from: com.shamchat.activity.ChatInitialForGroupChatActivity.4 */
    class C07104 implements OnClickListener {
        C07104() {
        }

        public final void onClick(View v) {
            UserProvider userProvider = new UserProvider();
            Cursor cursor3 = UserProvider.getUsersInGroup$31479a3c(ChatInitialForGroupChatActivity.groupId);
            if (cursor3 != null && cursor3.getCount() > 0) {
                ChatInitialForGroupChatActivity.this.initialUserList = UserProvider.usersFromCursorArray(cursor3);
            }
            Intent intent = new Intent(ChatInitialForGroupChatActivity.this.getApplicationContext(), CreateGroupAddUsersActivity.class);
            intent.putExtra(CreateGroupAddUsersActivity.EXTRA_IGNORED_USERS_GROUP, ChatInitialForGroupChatActivity.this.initialUserList);
            ChatInitialForGroupChatActivity.this.startActivityForResult(intent, 1);
        }
    }

    /* renamed from: com.shamchat.activity.ChatInitialForGroupChatActivity.5 */
    class C07135 implements OnClickListener {

        /* renamed from: com.shamchat.activity.ChatInitialForGroupChatActivity.5.1 */
        class C07111 implements DialogInterface.OnClickListener {
            C07111() {
            }

            public final void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }

        /* renamed from: com.shamchat.activity.ChatInitialForGroupChatActivity.5.2 */
        class C07122 implements DialogInterface.OnClickListener {
            C07122() {
            }

            public final void onClick(DialogInterface arg0, int arg1) {
                ChatInitialForGroupChatActivity.this.jobManager.addJobInBackground(new RoomLeaveJob(ChatInitialForGroupChatActivity.this.threadId, ChatInitialForGroupChatActivity.groupId));
            }
        }

        C07135() {
        }

        public final void onClick(View v) {
            Builder builder = new Builder(ChatInitialForGroupChatActivity.this);
            builder.setTitle(SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493315)).setMessage(SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493318)).setCancelable(false).setPositiveButton(SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493349), new C07122()).setNegativeButton(SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493334), new C07111());
            builder.create().show();
        }
    }

    /* renamed from: com.shamchat.activity.ChatInitialForGroupChatActivity.6 */
    class C07146 implements Runnable {
        C07146() {
        }

        public final void run() {
            Cursor cursor = null;
            try {
                cursor = ChatInitialForGroupChatActivity.this.getContentResolver().query(ChatProviderNew.CONTENT_URI_CHAT, new String[]{"packet_id"}, "message_status!=? AND message_type=? AND thread_id=?", new String[]{MessageStatusType.SENDING.ordinal(), MyMessageType.INCOMING_MSG.ordinal(), ChatInitialForGroupChatActivity.this.threadId}, null);
                while (cursor.moveToNext()) {
                    String packetId = cursor.getString(cursor.getColumnIndex("packet_id"));
                    ContentValues cv = new ContentValues();
                    cv.put("message_status", Integer.valueOf(MessageStatusType.SENDING.ordinal()));
                    ChatInitialForGroupChatActivity.this.getContentResolver().update(ChatProviderNew.CONTENT_URI_CHAT, cv, "packet_id=?", new String[]{packetId});
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }

    /* renamed from: com.shamchat.activity.ChatInitialForGroupChatActivity.7 */
    class C07167 implements Runnable {

        /* renamed from: com.shamchat.activity.ChatInitialForGroupChatActivity.7.1 */
        class C07151 implements Runnable {
            C07151() {
            }

            public final void run() {
                ChatInitialForGroupChatActivity.this.txtMessage.setText(BuildConfig.VERSION_NAME);
            }
        }

        C07167() {
        }

        public final void run() {
            ChatInitialForGroupChatActivity.this.sendTextMessage(ChatInitialForGroupChatActivity.this.txtMessage.getText().toString().toString().trim());
            ChatInitialForGroupChatActivity.this.chatHandler.post(new C07151());
        }
    }

    /* renamed from: com.shamchat.activity.ChatInitialForGroupChatActivity.8 */
    class C07178 implements TextWatcher {
        C07178() {
        }

        public final void afterTextChanged(Editable s) {
        }

        public final void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public final void onTextChanged(CharSequence s, int start, int before, int count) {
            String text = s.toString();
            if (text.length() != 0) {
                text.length();
            }
        }
    }

    /* renamed from: com.shamchat.activity.ChatInitialForGroupChatActivity.9 */
    static class C07189 implements Runnable {
        final /* synthetic */ SHAMChatApplication val$application;
        final /* synthetic */ String val$friendGroupId;
        final /* synthetic */ boolean val$isTyping;
        final /* synthetic */ String val$username;

        C07189(String str, boolean z, String str2, SHAMChatApplication sHAMChatApplication) {
            this.val$friendGroupId = str;
            this.val$isTyping = z;
            this.val$username = str2;
            this.val$application = sHAMChatApplication;
        }

        public final void run() {
            if (!this.val$friendGroupId.equalsIgnoreCase(ChatInitialForGroupChatActivity.groupId)) {
                return;
            }
            if (this.val$isTyping) {
                ChatInitialForGroupChatActivity.chatStateText.setText(BuildConfig.VERSION_NAME);
                ChatInitialForGroupChatActivity.chatStateText.setVisibility(0);
                ChatInitialForGroupChatActivity.chatStateText.setText(this.val$username + " " + this.val$application.getResources().getString(2131493177));
                return;
            }
            ChatInitialForGroupChatActivity.chatStateText.setVisibility(8);
            ChatInitialForGroupChatActivity.chatStateText.setText(BuildConfig.VERSION_NAME);
        }
    }

    private class Model {
        String phone;
        String userId;

        private Model() {
        }
    }

    public ChatInitialForGroupChatActivity() {
        this.threadId = null;
        this.silents = 0;
        this.clientHandle = null;
        this.selectedUserList = new ArrayList();
        this.groupUsers = new ArrayList();
        this.groupMembers = null;
        this.groupHashcode = null;
        this.mainHandler = new Handler();
        this.showInitialLoadButton = true;
        this.loadIndex = 40;
        this.currentLoadedIndex = 0;
        this.allMessageCount = 0;
        this.txtMessageWatcher = new C07178();
    }

    @SuppressLint({"InflateParams"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(2130968594, 2130968591);
        setContentView(2130903078);
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        try {
            EventBus.getDefault().register(this, true, 9);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(12);
        actionBar.setCustomView(2130903078);
        this.vl = (ImageView) actionBar.getCustomView().findViewById(2131362557);
        this.uploadedMessagesRequestedToBeSent = new HashMap();
        this.currentUserId = SHAMChatApplication.getConfig().userId;
        groupId = getIntent().getStringExtra(INTENT_EXTRA_GROUP_ID);
        this.threadId = getIntent().getStringExtra(INTENT_EXTRA_THREAD_ID);
        Log.i("intent", groupId);
        Log.i("intent", this.threadId);
        this.mContentResolver = getContentResolver();
        String[] strArr = new String[1];
        strArr[0] = SHAMChatApplication.getConfig().userId;
        Cursor cursor = this.mContentResolver.query(UserProvider.CONTENT_URI_USER, null, "userId=?", strArr, null);
        cursor.moveToFirst();
        this.me = UserProvider.userFromCursor(cursor);
        cursor.close();
        this.chatHandler = new Handler();
        try {
            this.group = UserProvider.groupFromCursor(getContentResolver().query(UserProvider.CONTENT_URI_GROUP, null, FriendGroup.DB_ID + "=?", new String[]{groupId}, null));
        } catch (Exception e2) {
            finish();
        }
        if (this.group != null) {
            actionBar.setTitle(this.group.name);
        }
        this.chatProvider = new ChatProviderNew();
        this.fragmentManager = getSupportFragmentManager();
        this.chatMessageAdapter = new GroupChatMessageAdapter(this, this.currentUserId, this);
        this.list = (BlockingListView) findViewById(2131361931);
        this.loadingView = (RelativeLayout) findViewById(2131361934);
        this.btnLoadMore = (Button) findViewById(2131361935);
        this.progressBarLoad = (ProgressBar) findViewById(2131361936);
        this.progressBarChatLoading = (ProgressBar) findViewById(2131361933);
        chatStateText = (TextView) findViewById(2131361938);
        this.slid = (SlideHolder) findViewById(2131361949);
        this.slid.setDirection$13462e();
        this.slid.setEnabled(false);
        this.btnLoadMore.setOnClickListener(this);
        int pass = getApplicationContext().getSharedPreferences("setting", 0).getInt("background", 1);
        if (VERSION.SDK_INT >= 11) {
            this.list.setBackground(null);
        }
        switch (pass) {
            case Logger.SEVERE /*1*/:
                this.list.setBackgroundResource(2130837614);
                break;
            case Logger.WARNING /*2*/:
                this.list.setBackgroundResource(2130837609);
                break;
            case Logger.INFO /*3*/:
                this.list.setBackgroundResource(2130837610);
                break;
            case Logger.CONFIG /*4*/:
                this.list.setBackgroundResource(2130837611);
                break;
            case Logger.FINE /*5*/:
                this.list.setBackgroundResource(2130837612);
                break;
            case Logger.FINER /*6*/:
                this.list.setBackgroundResource(2130837613);
                break;
            default:
                this.list.setBackgroundResource(2130837614);
                break;
        }
        this.list.setOnTouchListener(new C07011());
        this.list.setOnScrollListener(new C07082());
        this.btnAddChatItems = (ImageView) findViewById(2131361941);
        this.btnAddChatItems.setOnClickListener(this);
        this.layoutSendItems = (LinearLayout) findViewById(2131361947);
        this.btnAddVoice = (ImageView) findViewById(2131361942);
        this.btnAddVoice.setOnClickListener(this);
        this.btnAddSmilies = (ImageView) findViewById(2131361944);
        this.linn = (LinearLayout) findViewById(2131361937);
        this.btnAddSmilies.setOnClickListener(this);
        this.list.setAdapter(this.chatMessageAdapter);
        loadFragments();
        updateTitleBar();
        if (this.threadId != null) {
            this.currentLoadedIndex = this.loadIndex;
            ChatProviderNew chatProviderNew = new ChatProviderNew();
            if (ChatProviderNew.getUnreadMessagesCount(this.threadId) > 0) {
                String str = this.threadId;
                ChatMessage message = null;
                User user = ChatProviderNew.getUser(SHAMChatApplication.getConfig().userId);
                Cursor query = SHAMChatApplication.getMyApplicationContext().getContentResolver().query(ChatProviderNew.CONTENT_URI_CHAT, null, "message_status=? AND message_type=? AND thread_id=?", new String[]{MessageStatusType.QUEUED.ordinal(), MyMessageType.INCOMING_MSG.ordinal(), str}, null);
                if (query != null && query.getCount() > 0) {
                    query.moveToFirst();
                    message = ChatProviderNew.getChatMessageByCursor(query);
                    String str2 = message.sender;
                    System.out.println("chat message sender " + message.sender);
                    if (user.userId.equals(str2)) {
                        message.user = user;
                    } else if (!str2.startsWith("g")) {
                        message.user = ChatProviderNew.getUser(message.sender);
                    }
                }
                query.close();
                String packetId = message.packetId;
                ArrayList arrayList = new ArrayList();
                ArrayList<ChatMessage> loadedMessages = ChatProviderNew.loadDataSet$4f484b25(packetId, this.threadId, 0, this.loadIndex / 2, "Desc");
                this.chatMessageAdapter.addList(loadedMessages, 0);
                GroupChatMessageAdapter groupChatMessageAdapter = this.chatMessageAdapter;
                groupChatMessageAdapter.add(message, MyMessageType.HEADER_MSG, 1);
                groupChatMessageAdapter.add(message, message.incomingMessage, 1);
                int itemsAddedUpCount = loadedMessages.size();
                loadedMessages = ChatProviderNew.loadDataSet$4f484b25(packetId, this.threadId, 1, this.loadIndex / 2, "Asc");
                this.chatMessageAdapter.addList(loadedMessages, 1);
                int itemsAddedBelowCount = loadedMessages.size();
                int totalItemsAddedCount = itemsAddedBelowCount + itemsAddedUpCount;
                ChatMessage chatMsg = new ChatMessage();
                chatMsg.messageContentType = MessageContentType.GROUP_INFO;
                chatMsg.packetId = "msgRandomInvisible!!11";
                chatMsg.messageStatus = MessageStatusType.QUEUED;
                chatMsg.textMessage = "!!msgRandomInvisible!!";
                this.chatMessageAdapter.add(chatMsg, MyMessageType.INCOMING_MSG);
                chatMsg.packetId = "msgRandomInvisible!!22";
                chatMsg.textMessage = "!!msgRandomInvisible!!";
                this.chatMessageAdapter.add(chatMsg, MyMessageType.INCOMING_MSG);
                this.list.setVerticalScrollBarEnabled(false);
                View v = this.list.getChildAt(0);
                if (v != null) {
                    v.getTop();
                    v.getPaddingTop();
                }
                int index = totalItemsAddedCount - itemsAddedBelowCount;
                hideProgressBarChatLoading();
                this.chatMessageAdapter.notifyDataSetChanged();
                Log.v("UnreadMessages", "setSelection to postion: " + index);
                this.list.setSelectionFromTop(index, 0);
                this.list.setVerticalScrollBarEnabled(true);
            } else {
                loadPartMessages(0, this.loadIndex, 1);
            }
        }
        this.txtMessage = (EditText) findViewById(2131361945);
        this.txtMessage.setHint(getString(2131493011));
        this.txtMessage.addTextChangedListener(this.txtMessageWatcher);
        this.txtMessage.setOnTouchListener(new C07093());
        this.btnSend = (ImageView) findViewById(2131361946);
        this.btnSend.setOnClickListener(this);
        String messageBody = getIntent().getStringExtra(INTENT_EXTRA_MESSAGE_BODY);
        if (messageBody != null) {
            String s = getIntent().getStringExtra(INTENT_EXTRA_MESSAGE_CONTENT_TYPE);
            int messageContentType = 0;
            if (s != null) {
                messageContentType = Integer.valueOf(s).intValue();
            }
            if (messageContentType == MessageContentType.TEXT.ordinal()) {
                sendTextMessage(messageBody);
            } else if (messageContentType == MessageContentType.IMAGE.ordinal()) {
                sendMediaMessage(messageBody, MessageContentType.IMAGE.ordinal());
            } else if (messageContentType == MessageContentType.VIDEO.ordinal()) {
                sendMediaMessage(messageBody, MessageContentType.VIDEO.ordinal());
            } else if (messageContentType == MessageContentType.VOICE_RECORD.ordinal()) {
                sendMediaMessage(messageBody, MessageContentType.VOICE_RECORD.ordinal());
            }
        }
        this.list.requestFocus();
    }

    private void updateTitleBar() {
        try {
            View view = getSupportActionBar().getCustomView();
            ImageView imageStatus = (ImageView) view.findViewById(2131361845);
            this.txtLastSeen = (TextView) view.findViewById(2131361844);
            if (this.txtLastSeen != null) {
                this.txtLastSeen.setVisibility(8);
            }
            if (imageStatus != null) {
                imageStatus.setVisibility(8);
            }
            view.findViewById(2131361839);
            if (this.group == null || this.group.name == null || this.actionBar != null) {
                this.actionBar.setTitle(this.group.name.toString());
            }
            view.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onResume() {
        super.onResume();
        try {
            GenericService.currentOpenedThreadId = this.threadId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        SHAMChatApplication.getMyApplicationContext().getSharedPreferences("notify", 0).edit().remove(this.group.id).apply();
        Notify.updateNotidication(getApplicationContext());
        sendSeenReceipts();
        if (this.pd != null) {
            this.pd.dismiss();
        }
    }

    protected void onPause() {
        super.onPause();
        overridePendingTransition(2130968591, 2130968595);
        try {
            GenericService.currentOpenedThreadId = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    private void goBack() {
        hideSoftKeyboard(this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                this.slid.close();
                finish();
                break;
            case 2131362557:
                String s = this.group.id;
                this.sp = getApplicationContext().getSharedPreferences("silent", 0);
                Editor editor;
                if (this.silents != 0) {
                    this.silents = 0;
                    editor = this.sp.edit();
                    editor.putInt(s, 0);
                    editor.commit();
                    break;
                }
                this.silents = 1;
                Toast.makeText(getApplicationContext(), s, 0).show();
                editor = this.sp.edit();
                editor.putInt(s, 1);
                editor.commit();
                break;
            case 2131362558:
                boolean isUserAdmin;
                if (this.slid.open()) {
                    this.slid.close();
                } else {
                    this.slid.close();
                }
                ((LayoutInflater) getBaseContext().getSystemService("layout_inflater")).inflate(2130903148, null);
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int i = metrics.densityDpi;
                Button btAddParticipants = (Button) findViewById(2131361953);
                Button btLeaveGroup = (Button) findViewById(2131361952);
                ListView memberList = (ListView) findViewById(2131361954);
                if (this.groupMembers == null) {
                    this.groupMembers = getGroupMembers();
                }
                Cursor cursor2 = getContentResolver().query(UserProvider.CONTENT_URI_GROUP, new String[]{FriendGroup.DB_RECORD_OWNER}, FriendGroup.DB_ID + "=?", new String[]{groupId}, null);
                cursor2.moveToFirst();
                String groupOwnerId = cursor2.getString(cursor2.getColumnIndex(FriendGroup.DB_RECORD_OWNER));
                cursor2.close();
                if (groupOwnerId.equalsIgnoreCase(SHAMChatApplication.getConfig().userId)) {
                    btLeaveGroup.setVisibility(8);
                    isUserAdmin = true;
                } else {
                    btAddParticipants.setVisibility(8);
                    isUserAdmin = false;
                }
                this.uadapter = new UserAdapter(this);
                if (memberList.getAdapter() == null) {
                    this.adapter = new GroupOptionsAdapter(this, this.groupMembers, getContentResolver(), getSupportFragmentManager(), isUserAdmin, groupId);
                    memberList.setAdapter(this.adapter);
                } else {
                    this.adapter.groupMembers = this.groupMembers;
                    this.adapter.notifyDataSetChanged();
                }
                Utils.getTotalHeightofListView(memberList);
                btAddParticipants.setOnClickListener(new C07104());
                btLeaveGroup.setOnClickListener(new C07135());
                break;
        }
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(2131623938, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void sendSeenReceipts() {
        new Thread(new C07146()).start();
    }

    private void loadFragments() {
        this.transaction = this.fragmentManager.beginTransaction();
        this.fragmentBundle = new Bundle();
        this.fragmentBundle.putString(INTENT_EXTRA_THREAD_ID, this.threadId);
        this.fragmentBundle.putString("message_sender", this.currentUserId);
        this.fragmentBundle.putString("message_recipient", groupId);
        this.fragmentBundle.putBoolean("is_group_chat", true);
        this.mainChatItems = new MainChatItemsFragment();
        this.mainChatItems.setArguments(this.fragmentBundle);
        this.smileyAndSticker = new SmileyAndStickerFragment();
        this.smileyAndSticker.setArguments(this.fragmentBundle);
        this.voiceRcording = new ShamVoiceRecording();
        this.voiceRcording.setArguments(this.fragmentBundle);
        this.transaction.commit();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case 2131361935:
                this.progressBarLoad.setVisibility(0);
                this.btnLoadMore.setVisibility(8);
                refreshAdapter(true);
            case 2131361941:
                if (this.layoutSendItems.getVisibility() == 8) {
                    this.layoutSendItems.setVisibility(0);
                    this.btnAddChatItems.setImageResource(2130837660);
                    this.isAlreadyVisible = true;
                } else {
                    if (this.isAlreadyVisible) {
                        this.fragmentManager.beginTransaction().remove(this.mainChatItems).commit();
                        this.layoutSendItems.setVisibility(8);
                        this.btnAddChatItems.setImageResource(2130837659);
                    }
                    this.isAlreadyVisible = false;
                }
                if (this.isAlreadyVisible) {
                    this.fragmentManager.beginTransaction().setCustomAnimations(2130968589, 2130968590).replace(2131361948, this.mainChatItems).addToBackStack(null).commit();
                    hideSoftKeyboard(this);
                }
            case 2131361942:
                this.btnAddChatItems.setImageResource(2130837659);
                this.transaction = this.fragmentManager.beginTransaction();
                if (this.layoutSendItems.getVisibility() == 8) {
                    this.layoutSendItems.setVisibility(0);
                    this.isAlreadyVisible = true;
                } else {
                    if (this.isAlreadyVisible) {
                        this.fragmentManager.beginTransaction().remove(this.voiceRcording).commit();
                        this.layoutSendItems.setVisibility(8);
                        this.btnAddChatItems.setImageResource(2130837659);
                    }
                    this.isAlreadyVisible = false;
                }
                if (this.isAlreadyVisible) {
                    this.fragmentManager.beginTransaction().setCustomAnimations(2130968589, 2130968590).replace(2131361948, this.voiceRcording).addToBackStack(null).commit();
                    hideSoftKeyboard(this);
                }
            case 2131361944:
                this.transaction = this.fragmentManager.beginTransaction();
                if (this.layoutSendItems.getVisibility() == 8) {
                    this.layoutSendItems.setVisibility(0);
                    this.btnAddSmilies.setImageResource(2130837662);
                    this.isAlreadyVisible = true;
                } else {
                    if (this.isAlreadyVisible) {
                        this.fragmentManager.beginTransaction().remove(this.smileyAndSticker).commit();
                        this.layoutSendItems.setVisibility(8);
                        this.btnAddChatItems.setImageResource(2130837659);
                    }
                    this.isAlreadyVisible = false;
                }
                if (this.isAlreadyVisible) {
                    this.fragmentManager.beginTransaction().setCustomAnimations(2130968589, 2130968590).replace(2131361948, this.smileyAndSticker).addToBackStack(null).commit();
                    hideSoftKeyboard(this);
                }
            case 2131361946:
                if (!this.txtMessage.getText().toString().isEmpty()) {
                    new Thread(new C07167()).start();
                }
            default:
        }
    }

    public void sendTextMessage(String messageBody) {
        JSONObject jsonMessageObject = new JSONObject();
        String timestamp = com.shamchat.utils.Utils.getTimeStamp();
        try {
            jsonMessageObject.put("packet_type", AddFavoriteTextActivity.EXTRA_MESSAGE);
            jsonMessageObject.put("to", this.group.id);
            jsonMessageObject.put("from", this.me.mobileNo);
            jsonMessageObject.put("from_userid", this.me.userId);
            jsonMessageObject.put("messageBody", messageBody);
            jsonMessageObject.put("messageType", MessageContentType.TEXT.ordinal());
            jsonMessageObject.put("messageTypeDesc", BuildConfig.VERSION_NAME);
            jsonMessageObject.put("timestamp", timestamp);
            jsonMessageObject.put("groupAlias", this.group.name);
            jsonMessageObject.put("isGroupChat", 1);
            jsonMessageObject.put("packetId", com.shamchat.utils.Utils.makePacketId(this.me.userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.jobManager.addJobInBackground(new PublishToTopicJob(jsonMessageObject.toString(), "groups/" + this.group.id));
    }

    public void sendMediaMessage(String fullFilePath, int messageContentType) {
        String description = BuildConfig.VERSION_NAME;
        if (messageContentType == MessageContentType.VOICE_RECORD.ordinal()) {
            description = "VOICE_RECORD";
        }
        this.jobManager.addJobInBackground(new FileUploadJob(fullFilePath, this.me.userId, this.group.id, true, messageContentType, description, false, null, 0.0d, 0.0d));
    }

    public void hideSoftKeyboard(Activity activity) {
        ((InputMethodManager) activity.getSystemService("input_method")).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void addSmiley(Entry<Pattern, Integer> entry) {
        this.txtMessage.setText(Emoticons.getSmiledText(SHAMChatApplication.getMyApplicationContext(), this.txtMessage.getText() + ((Pattern) entry.getKey()).toString().replace("\\Q", BuildConfig.VERSION_NAME).replace("\\E", BuildConfig.VERSION_NAME)), BufferType.SPANNABLE);
    }

    public static void updateTypingStatus(boolean isTyping, String username, String friendGroupId) {
        SHAMChatApplication application = SHAMChatApplication.getInstance();
        if (application != null) {
            application.runOnUiThread(new C07189(friendGroupId, isTyping, username, application));
        }
    }

    private Stub getUICallback() {
        this.callback = new Stub() {

            /* renamed from: com.shamchat.activity.ChatInitialForGroupChatActivity.10.1 */
            class C07001 implements Runnable {
                C07001() {
                }

                @TargetApi(11)
                public final void run() {
                }
            }

            public final void connectionStateChanged(int connectionstate) throws RemoteException {
                ChatInitialForGroupChatActivity.this.mainHandler.post(new C07001());
            }

            public final void roomCreated(boolean created) throws RemoteException {
            }

            public final void didJoinRoom(boolean joined) throws RemoteException {
            }

            public final void onFriendComposing(String jabberId, boolean isTypng) throws RemoteException {
                System.out.println("User " + jabberId + "is Composing");
            }
        };
        return this.callback;
    }

    protected void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(this);
            GenericService.currentOpenedThreadId = null;
            unbindDrawables(findViewById(2131361929));
            if (VERSION.SDK_INT >= 11) {
                this.list.setBackground(null);
            }
            this.jobManager.addJobInBackground(new ChatThreadsDBLoadJob());
        } catch (Exception e) {
        }
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if ((view instanceof ViewGroup) && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        try {
            if (this.layoutSendItems.getVisibility() == 0) {
                this.btnAddChatItems.setImageResource(2130837659);
                this.layoutSendItems.setVisibility(8);
                this.fragmentManager.beginTransaction().remove(this.mainChatItems).commit();
                this.fragmentManager.beginTransaction().remove(this.voiceRcording).commit();
                this.fragmentManager.beginTransaction().remove(this.smileyAndSticker).commit();
            } else {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public ArrayList<FriendGroupMember> getGroupMembers() {
        ArrayList<FriendGroupMember> groupMembers = new ArrayList();
        Cursor cursor = getContentResolver().query(UserProvider.CONTENT_URI_GROUP_MEMBER, null, FriendGroupMember.DB_GROUP + "=?", new String[]{groupId}, null);
        while (cursor.moveToNext()) {
            FriendGroupMember friendGroupMember = new FriendGroupMember();
            String userId = cursor.getString(cursor.getColumnIndex(FriendGroupMember.DB_FRIEND));
            if (userId.contains("_")) {
                userId = userId.substring(1, userId.indexOf("_"));
            }
            if (!userId.contains("Contact")) {
                friendGroupMember.friendId = userId;
                friendGroupMember.groupID = cursor.getString(cursor.getColumnIndex(FriendGroupMember.DB_GROUP));
                friendGroupMember.phoneNumber = cursor.getString(cursor.getColumnIndex(FriendGroupMember.PHONE_NUMBER));
                int admin = cursor.getInt(cursor.getColumnIndex(FriendGroupMember.DB_FRIEND_IS_ADMIN));
                int joined = cursor.getInt(cursor.getColumnIndex(FriendGroupMember.DB_FRIEND_DID_JOIN));
                if (admin != 1) {
                    friendGroupMember.isAdmin = false;
                } else {
                    friendGroupMember.isAdmin = true;
                }
                if (joined == 1) {
                    friendGroupMember.didJoin = true;
                } else {
                    friendGroupMember.didJoin = false;
                }
                groupMembers.add(friendGroupMember);
            }
        }
        cursor.close();
        return groupMembers;
    }

    public void onEventBackgroundThread(ChatMessagesDBLoadCompletedEvent event) {
        if (event.threadId.equals(this.threadId)) {
            this.newMessagesAreThereToBeDisplayed = false;
            List<ChatMessage> chatMessages = event.messages;
            this.chatMessageAdapter.clear();
            this.chatMessageAdapter.addList(chatMessages, GroupChatMessageAdapter.addToBottom);
            ChatMessage chatMsg = new ChatMessage();
            chatMsg.messageContentType = MessageContentType.GROUP_INFO;
            chatMsg.packetId = "msgRandomInvisible!!11";
            chatMsg.messageStatus = MessageStatusType.QUEUED;
            chatMsg.textMessage = "!!msgRandomInvisible!!";
            this.chatMessageAdapter.add(chatMsg, MyMessageType.INCOMING_MSG);
            chatMsg.packetId = "msgRandomInvisible!!22";
            chatMsg.textMessage = "!!msgRandomInvisible!!";
            this.chatMessageAdapter.add(chatMsg, MyMessageType.INCOMING_MSG);
            if (this != null) {
                runOnUiThread(new Runnable() {
                    public final void run() {
                        ChatInitialForGroupChatActivity.this.hideProgressBarChatLoading();
                        ChatInitialForGroupChatActivity.this.chatMessageAdapter.notifyDataSetChanged();
                        if (ChatInitialForGroupChatActivity.this.scrollToBottom) {
                            ChatInitialForGroupChatActivity.this.list.setSelection(ChatInitialForGroupChatActivity.this.chatMessageAdapter.getCount() - 1);
                        } else {
                            System.out.println("Scrolling to bottom db load completed else");
                        }
                    }
                });
            }
        }
    }

    private void loadPartMessages(int fromOffset, int toOffset, int appendDirection) {
        this.jobManager.addJobInBackground(new ChatMessagesPartDBLoadJob(this.threadId, fromOffset, toOffset, appendDirection));
    }

    public void onEventBackgroundThread(ChatMessagesPartDBLoadCompletedEvent event) {
        if (event.threadId.equals(this.threadId)) {
            List<ChatMessage> chatMessages = event.messages;
            this.chatMessageAdapter.addList(chatMessages, event.appendDirection);
            if (chatMessages.size() > 0) {
                ChatMessage chatMsg = new ChatMessage();
                chatMsg.messageContentType = MessageContentType.GROUP_INFO;
                chatMsg.packetId = "msgRandomInvisible!!11";
                chatMsg.messageStatus = MessageStatusType.QUEUED;
                chatMsg.textMessage = "!!msgRandomInvisible!!";
                this.chatMessageAdapter.add(chatMsg, MyMessageType.INCOMING_MSG);
                chatMsg.packetId = "msgRandomInvisible!!22";
                chatMsg.textMessage = "!!msgRandomInvisible!!";
                this.chatMessageAdapter.add(chatMsg, MyMessageType.INCOMING_MSG);
            }
            if (this != null) {
                runOnUiThread(new Runnable() {
                    public final void run() {
                        ChatInitialForGroupChatActivity.this.hideProgressBarChatLoading();
                        ChatInitialForGroupChatActivity.this.chatMessageAdapter.notifyDataSetChanged();
                        if (ChatInitialForGroupChatActivity.this.scrollToBottom) {
                            ChatInitialForGroupChatActivity.this.list.setSelection(ChatInitialForGroupChatActivity.this.chatMessageAdapter.getCount() - 1);
                        } else {
                            System.out.println("Scrolling to bottom db load completed else");
                        }
                    }
                });
            }
        }
    }

    private void refreshAdapter(boolean isLoadMore) {
    }

    public void onEventBackgroundThread(NewMessageEvent event) {
        System.out.println("NewMessageEvent from ChatInitialForGroup:  " + event.threadId);
        String tId = event.threadId;
        if (this.list != null && this.chatMessageAdapter != null) {
            if (this.threadId.equals(tId)) {
                ChatMessage message;
                event.consumed = true;
                String packetId = null;
                try {
                    packetId = event.packetId;
                } catch (Exception e) {
                }
                if (packetId == null) {
                    packetId = com.shamchat.utils.Utils.getPacketId(event.jsonMessageString);
                }
                if (packetId != null) {
                    message = GroupChatMessageAdapter.getMessageFromDB(packetId);
                } else {
                    message = GroupChatMessageAdapter.getLastMessageFromDB(this.threadId);
                }
                if (this.chatMessageAdapter.getCount() > 0) {
                    if (!this.chatMessageAdapter.previousToLastRecord$505cff18(this.threadId) && event.direction == MyMessageType.INCOMING_MSG.ordinal()) {
                        return;
                    }
                    if (!this.chatMessageAdapter.previousToLastRecord$505cff18(this.threadId) && event.direction == MyMessageType.OUTGOING_MSG.ordinal()) {
                        this.chatMessageAdapter.clear();
                        this.scrollToBottom = true;
                        loadPartMessages(0, this.loadIndex, 1);
                        return;
                    }
                }
                if (!this.chatMessageAdapter.chatMap.containsKey(message.packetId)) {
                    this.chatMessageAdapter.add(message, MyMessageType.HEADER_MSG, 1);
                    this.chatMessageAdapter.add(message, message.incomingMessage, 1);
                    if (this != null) {
                        runOnUiThread(new Runnable() {
                            public final void run() {
                                ChatInitialForGroupChatActivity.this.chatMessageAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
                if (event.direction == MyMessageType.OUTGOING_MSG.ordinal()) {
                    this.scrollToBottom = true;
                } else {
                    this.scrollToBottom = false;
                }
                if (this.scrollToBottom) {
                    this.list.post(new Runnable() {
                        public final void run() {
                            ChatInitialForGroupChatActivity.this.list.setSelection(ChatInitialForGroupChatActivity.this.chatMessageAdapter.getCount() - 1);
                        }
                    });
                }
            }
            sendSeenReceipts();
        }
    }

    public void onEventMainThread(MessageStateChangedEvent event) {
        if (this.threadId != null && this.threadId.equals(event.threadId)) {
            new AnonymousClass15(event).start();
        }
    }

    public void onEventBackgroundThread(FileUploadingProgressEvent event) {
        if (this.threadId != null && event.threadId != null) {
            if (!this.threadId.equals(event.threadId)) {
                System.out.println("FileUploadingProgressEvent different thread");
            } else if (event.uploadedPercentage == 9999) {
                this.chatMessageAdapter.setUploadedPercentage(event.packetId, event.uploadedPercentage);
                if (this != null) {
                    runOnUiThread(new Runnable() {
                        public final void run() {
                            ChatInitialForGroupChatActivity.this.refreshVisibleViewsImages();
                        }
                    });
                }
            } else if (event.uploadedPercentage == 100) {
                GroupChatMessageAdapter groupChatMessageAdapter = this.chatMessageAdapter;
                String str = event.packetId;
                String str2 = BuildConfig.VERSION_NAME;
                if (groupChatMessageAdapter.chatMap.containsKey(str)) {
                    System.out.println("TEST UPLOADING URL " + str2);
                    Row row = (Row) groupChatMessageAdapter.chatMap.get(str);
                    if (row instanceof OutgoingGroupMsg) {
                        System.out.println("row instanceof OutgoingMsg");
                        OutgoingGroupMsg outgoingGroupMsg = (OutgoingGroupMsg) row;
                        ChatMessage chatMessage = outgoingGroupMsg.chatMessage;
                        chatMessage.uploadedPercentage = 100;
                        chatMessage.uploadedFileUrl = str2;
                        ViewHolder viewHolder = outgoingGroupMsg.viewHolder;
                        if (viewHolder != null) {
                            groupChatMessageAdapter.chatInitialForGroupChatActivity.runOnUiThread(new C09521(viewHolder));
                        }
                    }
                }
                this.chatMessageAdapter.updateMessageStatus(event.packetId, MessageStatusType.SENT);
                if (this != null) {
                    runOnUiThread(new Runnable() {
                        public final void run() {
                            ChatInitialForGroupChatActivity.this.refreshVisibleViewsImages();
                        }
                    });
                }
            } else {
                this.chatMessageAdapter.setUploadedPercentage(event.packetId, event.uploadedPercentage);
            }
        }
    }

    private void sendBLOBMessage(String tId, String packetId) {
        Cursor cursor = getContentResolver().query(ChatProviderNew.CONTENT_URI_CHAT, null, "thread_id=? AND packet_id=?", new String[]{this.threadId, packetId}, "_id DESC LIMIT " + this.currentLoadedIndex + " ");
        if (this.chatProvider != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ChatMessage message = ChatProviderNew.getChatMessageByCursor(cursor);
                String str = message.packetId;
                if (message.threadId.equals(this.threadId)) {
                    String uploadedUrl = message.uploadedFileUrl;
                    JSONObject jsonMessageObject = new JSONObject();
                    try {
                        jsonMessageObject.put("packet_type", AddFavoriteTextActivity.EXTRA_MESSAGE);
                        jsonMessageObject.put("to", this.group.id);
                        jsonMessageObject.put("from", this.me.mobileNo);
                        jsonMessageObject.put("from_userid", this.me.userId);
                        jsonMessageObject.put("messageBody", uploadedUrl);
                        jsonMessageObject.put("messageType", message.messageContentType.ordinal());
                        jsonMessageObject.put("messageTypeDesc", message.description);
                        jsonMessageObject.put("timestamp", com.shamchat.utils.Utils.getTimeStamp());
                        jsonMessageObject.put("groupAlias", this.group.name);
                        jsonMessageObject.put("latitude", message.latitude);
                        jsonMessageObject.put("longitude", message.longitude);
                        jsonMessageObject.put("isGroupChat", 1);
                        jsonMessageObject.put("packetId", message.packetId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    this.jobManager.addJobInBackground(new PublishToTopicJob(jsonMessageObject.toString(), "groups/" + this.group.id));
                }
            }
        }
        cursor.close();
    }

    public void showProgressBarChatLoading() {
        this.progressBarChatLoading.setVisibility(0);
    }

    public void hideProgressBarChatLoading() {
        this.progressBarChatLoading.setVisibility(8);
    }

    public void onEventBackgroundThread(MessageDeletedEvent event) {
        this.scrollToBottom = false;
        refreshAdapter(false);
    }

    public void onEventBackgroundThread(TypingStatusEvent event) {
        String groupId = event.groupId;
        if (groupId != null) {
            boolean isTyping = event.typing;
            String username = event.username;
            if (groupId.equalsIgnoreCase(groupId) && this != null) {
                runOnUiThread(new AnonymousClass18(isTyping, username));
            }
        }
    }

    public void onEventMainThread(CloseGroupActivityEvent event) {
        if (this.threadId != null && this.threadId.equals(event.threadId)) {
            finish();
        }
    }

    public void onEventMainThread(RemoveFromGroupMembersList event) {
        if (this.threadId != null && this.threadId.equals(event.threadId) && this.groupMembers != null) {
            this.groupMembers.remove(event.userPositionInListView);
            this.adapter.notifyDataSetChanged();
        }
    }

    public void onEventMainThread(UpdateGroupMembersList event) {
        if (this.threadId != null && this.threadId.equals(event.threadId) && this.groupMembers != null) {
            try {
                this.groupMembers.clear();
                Cursor cursor = getContentResolver().query(UserProvider.CONTENT_URI_GROUP_MEMBER, null, FriendGroupMember.DB_GROUP + "=?", new String[]{groupId}, null);
                while (cursor.moveToNext()) {
                    FriendGroupMember friendGroupMember = new FriendGroupMember();
                    String userId = cursor.getString(cursor.getColumnIndex(FriendGroupMember.DB_FRIEND));
                    if (userId.contains("_")) {
                        userId = userId.substring(1, userId.indexOf("_"));
                    }
                    if (!userId.contains("Contact")) {
                        friendGroupMember.friendId = userId;
                        friendGroupMember.groupID = cursor.getString(cursor.getColumnIndex(FriendGroupMember.DB_GROUP));
                        friendGroupMember.phoneNumber = cursor.getString(cursor.getColumnIndex(FriendGroupMember.PHONE_NUMBER));
                        int admin = cursor.getInt(cursor.getColumnIndex(FriendGroupMember.DB_FRIEND_IS_ADMIN));
                        int joined = cursor.getInt(cursor.getColumnIndex(FriendGroupMember.DB_FRIEND_DID_JOIN));
                        if (admin != 1) {
                            friendGroupMember.isAdmin = false;
                        } else {
                            friendGroupMember.isAdmin = true;
                        }
                        if (joined == 1) {
                            friendGroupMember.didJoin = true;
                        } else {
                            friendGroupMember.didJoin = false;
                        }
                        this.groupMembers.add(friendGroupMember);
                    }
                }
                cursor.close();
                this.adapter.notifyDataSetChanged();
            } catch (Exception e) {
            }
        }
    }

    public void onEventBackgroundThread(NewGroupMessageSentFailedEvent event) {
    }

    public void onEventBackgroundThread(NewGroupMessageSentSuccessEvent event) {
    }

    public void onEventBackgroundThread(SendStickerToGroupEvent event) {
        String stikerUrl = event.stikerUrl;
        JSONObject jsonMessageObject = new JSONObject();
        try {
            jsonMessageObject.put("packet_type", AddFavoriteTextActivity.EXTRA_MESSAGE);
            jsonMessageObject.put("to", this.group.id);
            jsonMessageObject.put("from", this.me.mobileNo);
            jsonMessageObject.put("from_userid", this.me.userId);
            jsonMessageObject.put("messageBody", stikerUrl);
            jsonMessageObject.put("messageType", MessageContentType.STICKER.ordinal());
            jsonMessageObject.put("messageTypeDesc", BuildConfig.VERSION_NAME);
            jsonMessageObject.put("timestamp", com.shamchat.utils.Utils.getTimeStamp());
            jsonMessageObject.put("groupAlias", this.group.name);
            jsonMessageObject.put("isGroupChat", 1);
            jsonMessageObject.put("packetId", com.shamchat.utils.Utils.makePacketId(this.me.userId));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.jobManager.addJobInBackground(new PublishToTopicJob(jsonMessageObject.toString(), "groups/" + this.group.id));
    }

    public void onEventMainThread(ImageDownloadProgressEvent event) {
        if (this.chatMessageAdapter.chatMap.containsKey(event.packetId)) {
            Row row = (Row) this.chatMessageAdapter.chatMap.get(event.packetId);
            if (row instanceof IncomingGroupMsg) {
                IncomingGroupMsg incomingGroupMsg = (IncomingGroupMsg) row;
                ChatMessage chatMessage = incomingGroupMsg.chatMessage;
                IncomingGroupMsg.ViewHolder viewHolder = incomingGroupMsg.holder;
                if (event.isDone || viewHolder == null) {
                    viewHolder.downloadingProgress.setVisibility(8);
                    viewHolder.downloadStart.setVisibility(8);
                    chatMessage.fileUrl = event.downloadedFilePath;
                    new Handler().postDelayed(new Runnable() {
                        public final void run() {
                            ChatInitialForGroupChatActivity.this.refreshVisibleViewsImages();
                        }
                    }, 1500);
                } else if (viewHolder.downloadingProgress.getVisibility() != 0) {
                    viewHolder.downloadingProgress.setVisibility(0);
                    viewHolder.downloadStart.setVisibility(8);
                    refreshVisibleViewsImages();
                }
            }
        }
    }

    public void onEventMainThread(VideoDownloadProgressEvent event) {
        if (this.chatMessageAdapter.chatMap.containsKey(event.packetId)) {
            Row row = (Row) this.chatMessageAdapter.chatMap.get(event.packetId);
            if (row instanceof IncomingGroupMsg) {
                IncomingGroupMsg incomingGroupMsg = (IncomingGroupMsg) row;
                ChatMessage chatMessage = incomingGroupMsg.chatMessage;
                IncomingGroupMsg.ViewHolder viewHolder = incomingGroupMsg.holder;
                if (event.isDone || viewHolder == null) {
                    viewHolder.downloadingProgress.setVisibility(8);
                    viewHolder.downloadStart.setVisibility(8);
                    viewHolder.btnPlay.setVisibility(0);
                    chatMessage.fileUrl = event.downloadedFilePath;
                    refreshVisibleViewsImages();
                }
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == -1) {
            this.newUsers = data.getExtras().getParcelableArrayList(EXTRA_CONTACTS);
            this.uadapter.addUsers(this.newUsers);
            String groupHashCode = this.group.id;
            this.selectedUserList = Arrays.asList(this.uadapter.getUsers());
            JSONObject params = new JSONObject();
            try {
                JSONObject actor = new JSONObject();
                actor.put(ChatActivity.INTENT_EXTRA_USER_ID, SHAMChatApplication.getConfig().userId);
                actor.put("phone", this.me.username);
                JSONArray users = new JSONArray();
                Model userModel = new Model();
                for (int j = 0; j < this.selectedUserList.size(); j++) {
                    userModel.phone = ((User) this.selectedUserList.get(j)).mobileNo;
                    userModel.userId = ((User) this.selectedUserList.get(j)).userId;
                    String user_id = userModel.userId;
                    String phone = userModel.phone;
                    JSONObject member = new JSONObject();
                    member.put(ChatActivity.INTENT_EXTRA_USER_ID, user_id);
                    member.put("phone", phone);
                    users.put(member);
                }
                params.put("actor", actor);
                params.put("users", users);
                Log.e("INVITE JSON", params.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SHAMChatApplication.getInstance().jobManager.addJobInBackground(new RoomInviteJob(params.toString(), groupHashCode));
        }
    }

    void refreshVisibleViews() {
        if (this.chatMessageAdapter != null) {
            for (int i = this.list.getFirstVisiblePosition(); i <= this.list.getLastVisiblePosition(); i++) {
                int dataPosition = i - this.list.getHeaderViewsCount();
                int childPosition = i - this.list.getFirstVisiblePosition();
                if (dataPosition >= 0 && dataPosition < this.chatMessageAdapter.getCount() && this.list.getChildAt(childPosition) != null) {
                    Log.v("ChatInitialForGroupChatActivity", "Refreshing view (data=" + dataPosition + ",child=" + childPosition + ")");
                    this.chatMessageAdapter.getView(dataPosition, this.list.getChildAt(childPosition), this.list);
                }
            }
        }
    }

    void refreshVisibleViewsImages() {
        if (this.chatMessageAdapter != null) {
            for (int i = this.list.getFirstVisiblePosition(); i <= this.list.getLastVisiblePosition(); i++) {
                int dataPosition = i - this.list.getHeaderViewsCount();
                int childPosition = i - this.list.getFirstVisiblePosition();
                if (dataPosition >= 0 && dataPosition < this.chatMessageAdapter.getCount() && this.list.getChildAt(childPosition) != null) {
                    ImageView imageView = (ImageView) this.list.getChildAt(childPosition).findViewById(2131362134);
                    if (imageView != null && imageView.getVisibility() == 0) {
                        this.chatMessageAdapter.getView(dataPosition, this.list.getChildAt(childPosition), this.list);
                    }
                }
            }
        }
    }
}

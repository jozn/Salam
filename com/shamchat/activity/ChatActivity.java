package com.shamchat.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.os.RemoteException;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
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
import com.shamchat.adapters.ChatMessageAdapter;
import com.shamchat.adapters.IncomingMsg;
import com.shamchat.adapters.IncomingMsg.ViewHolder;
import com.shamchat.adapters.MyMessageType;
import com.shamchat.adapters.Row;
import com.shamchat.androidclient.ChatController;
import com.shamchat.androidclient.IXMPPChatCallback.Stub;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.androidclient.data.RosterProvider;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.androidclient.listeners.InfiniteScrollListener;
import com.shamchat.androidclient.service.GenericService;
import com.shamchat.androidclient.service.SmackableImp;
import com.shamchat.androidclient.util.PreferenceConstants;
import com.shamchat.androidclient.util.StatusMode;
import com.shamchat.events.ChatMessagesDBLoadCompletedEvent;
import com.shamchat.events.FileUploadingProgressEvent;
import com.shamchat.events.FriendDBLoadCompletedEvent;
import com.shamchat.events.ImageDownloadProgressEvent;
import com.shamchat.events.MessageDeletedEvent;
import com.shamchat.events.MessageStateChangedEvent;
import com.shamchat.events.NewMessageEvent;
import com.shamchat.events.PresenceChangedEvent;
import com.shamchat.events.VideoDownloadProgressEvent;
import com.shamchat.jobs.ChatMessagesDBLoadJob;
import com.shamchat.jobs.ChatThreadsDBLoadJob;
import com.shamchat.jobs.FileUploadJob;
import com.shamchat.jobs.FriendDBLoadJob;
import com.shamchat.models.ChatMessage;
import com.shamchat.models.ChatMessage.MessageStatusType;
import com.shamchat.models.User;
import com.shamchat.stickers.SmileyAndStickerFragment;
import com.shamchat.utils.Emoticons;
import com.shamchat.utils.SoftKeyboardUtil.C11771;
import com.shamchat.utils.SoftKeyboardUtil.OnSoftKeyBoardHideListener;
import com.shamchat.utils.Utils;
import com.shamchat.utils.Utils.ContactDetails;
import com.squareup.picasso.Picasso;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;

@SuppressLint({"NewApi"})
public class ChatActivity extends AppCompatActivity implements OnClickListener {
    public static final String FORWARDED_MESSAGE = "forwarded_message";
    public static final String INTENT_EXTRA_MESSAGE_BODY = "message_body";
    public static final String INTENT_EXTRA_MESSAGE_CONTENT_TYPE = "message_content_type";
    public static final String INTENT_EXTRA_THREAD_ID = "thread_id";
    public static final String INTENT_EXTRA_USER_ID = "user_id";
    private static TextView chatStateText;
    private static String friendJID;
    private View addOrBlockview;
    private int allMessageCount;
    private BlankFragment blankFragment;
    private ImageView btnAddChatItems;
    private ImageView btnAddSmilies;
    private ImageView btnAddVoice;
    private Button btnLoadMore;
    private ImageView btnSend;
    private Stub callback;
    private ChatController chatController;
    private Handler chatHandler;
    private ChatMessageAdapter chatMessageAdapter;
    private ChatProviderNew chatProvider;
    private int currentLoadedIndex;
    private String currentUserId;
    Dialog dialog;
    Boolean first;
    private Bundle fragmentBundle;
    private FragmentManager fragmentManager;
    private String friendId;
    private String friendMobileNo;
    private String friendProfileImageUrl;
    private ImageView imageStatus;
    private ImageView improfile;
    private boolean isAlreadyVisible;
    private boolean isSubUriSupported;
    private boolean isUpdated;
    private JobManager jobManager;
    private TextView lastSeen;
    private LinearLayout layoutSendItems;
    private ListView list;
    private int loadIndex;
    private RelativeLayout loadingView;
    private MainChatItemsFragment mainChatItems;
    private Handler mainHandler;
    private String myProfileImageUrl;
    private boolean newMessagesAreThereToBeDisplayed;
    ProgressDialog pd;
    String pic;
    private ProgressBar progressBarChatLoading;
    private ProgressBar progressBarLoad;
    private boolean scrollToBottom;
    private boolean showInitialLoadButton;
    private SmileyAndStickerFragment smileyAndSticker;
    private TextView textName;
    private String threadId;
    private FragmentTransaction transaction;
    private EditText txtMessage;
    private TextWatcher txtMessageWatcher;
    private ShamVoiceRecording voiceRcording;

    /* renamed from: com.shamchat.activity.ChatActivity.10 */
    class AnonymousClass10 implements Runnable {
        final /* synthetic */ String val$jabberId;

        /* renamed from: com.shamchat.activity.ChatActivity.10.1 */
        class C06871 implements Runnable {
            final /* synthetic */ String val$lastSeenText;

            C06871(String str) {
                this.val$lastSeenText = str;
            }

            public final void run() {
                System.out.println("Last seen updating UI");
                ChatActivity.this.lastSeen.setVisibility(0);
                ChatActivity.this.lastSeen.setText(this.val$lastSeenText);
            }
        }

        AnonymousClass10(String str) {
            this.val$jabberId = str;
        }

        public final void run() {
            try {
                Process.setThreadPriority(19);
                ChatController access$2100 = ChatActivity.this.chatController;
                long lastActivity = access$2100.chatServiceAdapter.getLastActivity(this.val$jabberId);
                System.out.println("Last activity " + lastActivity);
                if (lastActivity != 0) {
                    String temp = null;
                    for (Entry<String, Long> entry : Utils.getDurationBreakdownArray(1000 * lastActivity).entrySet()) {
                        temp = "Last seen " + entry.getValue() + " " + ((String) entry.getKey()) + " ago";
                    }
                    ChatActivity.this.mainHandler.postDelayed(new C06871(temp), 500);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.shamchat.activity.ChatActivity.12 */
    class AnonymousClass12 implements Runnable {
        final /* synthetic */ String val$messageBody;

        /* renamed from: com.shamchat.activity.ChatActivity.12.1 */
        class C06881 implements Runnable {
            C06881() {
            }

            public final void run() {
                ChatActivity.this.txtMessage.setText(BuildConfig.VERSION_NAME);
            }
        }

        AnonymousClass12(String str) {
            this.val$messageBody = str;
        }

        public final void run() {
            ChatActivity.this.scrollToBottom = true;
            ChatActivity.this.chatController.sendMessage(ChatActivity.friendJID, this.val$messageBody, MessageContentType.TEXT.toString(), false, null, BuildConfig.VERSION_NAME, BuildConfig.VERSION_NAME, BuildConfig.VERSION_NAME);
            ChatActivity.this.chatHandler.post(new C06881());
        }
    }

    /* renamed from: com.shamchat.activity.ChatActivity.15 */
    static class AnonymousClass15 implements Runnable {
        final /* synthetic */ boolean val$isTyping;
        final /* synthetic */ String val$jabberId;

        AnonymousClass15(String str, boolean z) {
            this.val$jabberId = str;
            this.val$isTyping = z;
        }

        public final void run() {
            if (ChatActivity.friendJID != null && this.val$jabberId != null && ChatActivity.friendJID.equalsIgnoreCase(this.val$jabberId)) {
                if (this.val$isTyping) {
                    ChatActivity.chatStateText.setVisibility(0);
                } else {
                    ChatActivity.chatStateText.setVisibility(8);
                }
            }
        }
    }

    /* renamed from: com.shamchat.activity.ChatActivity.16 */
    class AnonymousClass16 implements Runnable {
        final /* synthetic */ List val$chatMessages;

        AnonymousClass16(List list) {
            this.val$chatMessages = list;
        }

        public final void run() {
            ChatMessageAdapter access$1200 = ChatActivity.this.chatMessageAdapter;
            access$1200.chatMap = new HashMap();
            access$1200.arrkey = new ArrayList();
            for (ChatMessage message : this.val$chatMessages) {
                ChatActivity.this.chatMessageAdapter.add(message, MyMessageType.HEADER_MSG);
                ChatActivity.this.chatMessageAdapter.add(message, message.incomingMessage);
            }
            ChatActivity.this.hideProgressBarChatLoading();
            ChatActivity.this.loadingView.setVisibility(8);
            ChatActivity.this.chatMessageAdapter.notifyDataSetChanged();
            if (ChatActivity.this.scrollToBottom) {
                System.out.println("Scrolling to bottom db load completed if");
                ChatActivity.this.list.setSelection(ChatActivity.this.chatMessageAdapter.getCount() - 1);
                return;
            }
            System.out.println("Scrolling to bottom db load completed else");
        }
    }

    /* renamed from: com.shamchat.activity.ChatActivity.17 */
    class AnonymousClass17 extends Thread {
        final /* synthetic */ MessageStateChangedEvent val$event;

        AnonymousClass17(MessageStateChangedEvent messageStateChangedEvent) {
            this.val$event = messageStateChangedEvent;
        }

        public final void run() {
            int i = 0;
            while (i < 4) {
                try {
                    boolean result = ChatActivity.this.chatMessageAdapter.updateMessageStatus(this.val$event.packetId, this.val$event.messageStatusType);
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

    /* renamed from: com.shamchat.activity.ChatActivity.19 */
    class AnonymousClass19 implements Runnable {
        final /* synthetic */ ChatMessage val$message;
        final /* synthetic */ String val$uploadedUrl;

        AnonymousClass19(String str, ChatMessage chatMessage) {
            this.val$uploadedUrl = str;
            this.val$message = chatMessage;
        }

        public final void run() {
            ChatActivity.this.chatController.sendMessage(ChatActivity.friendJID, this.val$uploadedUrl, this.val$message.messageContentType.toString(), false, this.val$message.packetId, String.valueOf(this.val$message.latitude), String.valueOf(this.val$message.longitude), this.val$message.description);
        }
    }

    /* renamed from: com.shamchat.activity.ChatActivity.1 */
    class C06901 implements Runnable {
        C06901() {
        }

        public final void run() {
            if (PreferenceConstants.CONNECTED_TO_NETWORK && SmackableImp.isXmppConnected()) {
                Presence presence = new Presence(Type.available);
                presence.setPriority(0);
                try {
                    SmackableImp.getXmppConnection().sendPacket(presence);
                } catch (NotConnectedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* renamed from: com.shamchat.activity.ChatActivity.2 */
    class C06912 implements Runnable {
        C06912() {
        }

        public final void run() {
            Cursor msgCountCursor = ChatActivity.this.getContentResolver().query(ChatProviderNew.CONTENT_URI_CHAT, new String[]{"_id"}, "thread_id=?", new String[]{ChatActivity.this.threadId}, null);
            if (msgCountCursor.getCount() < ChatActivity.this.loadIndex) {
                ChatActivity.this.showInitialLoadButton = false;
            }
            ChatActivity.this.allMessageCount = msgCountCursor.getCount();
            msgCountCursor.close();
        }
    }

    /* renamed from: com.shamchat.activity.ChatActivity.3 */
    class C06923 implements OnClickListener {
        C06923() {
        }

        public final void onClick(View v) {
            Intent addContactIntent = new Intent("android.intent.action.INSERT");
            addContactIntent.setType("vnd.android.cursor.dir/contact");
            addContactIntent.putExtra("phone", ChatActivity.this.friendMobileNo);
            ChatActivity.this.startActivity(addContactIntent);
        }
    }

    /* renamed from: com.shamchat.activity.ChatActivity.4 */
    class C06934 implements OnClickListener {
        C06934() {
        }

        public final void onClick(View v) {
            ContentValues values = new ContentValues();
            values.put("user_status", Integer.valueOf(1));
            ChatActivity.this.getContentResolver().update(RosterProvider.CONTENT_URI, values, "jid=?", new String[]{ChatActivity.friendJID});
            Toast.makeText(ChatActivity.this.getApplicationContext(), 2131493149, 0).show();
            ChatActivity.this.list.removeHeaderView(ChatActivity.this.addOrBlockview);
        }
    }

    /* renamed from: com.shamchat.activity.ChatActivity.5 */
    class C06945 extends InfiniteScrollListener {
        C06945() {
        }

        public final void loadMore$255f295() {
            if (!ChatActivity.this.showInitialLoadButton || ChatActivity.this.currentLoadedIndex >= ChatActivity.this.allMessageCount) {
                ChatActivity.this.showInitialLoadButton = true;
                return;
            }
            ChatActivity.this.loadingView.setVisibility(0);
            ChatActivity.this.progressBarLoad.setVisibility(4);
            ChatActivity.this.btnLoadMore.setVisibility(0);
        }

        public final void onScrollStateChanged(AbsListView view, int scrollState) {
            super.onScrollStateChanged(view, scrollState);
            Picasso picasso = Picasso.with(ChatActivity.this);
            if (scrollState == 2) {
                picasso.pauseTag("singleChatListViewImages");
                if (ChatActivity.this.loadingView.getVisibility() == 0) {
                    ChatActivity.this.loadingView.setVisibility(8);
                    return;
                }
                return;
            }
            picasso.resumeTag("singleChatListViewImages");
        }

        public final void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            System.out.println("Visible Item count " + visibleItemCount + " totalItemCount " + totalItemCount + "  firstVisibleItem " + firstVisibleItem);
            if (ChatActivity.this.list.getLastVisiblePosition() == ChatActivity.this.chatMessageAdapter.getCount() - 1 && ChatActivity.this.newMessagesAreThereToBeDisplayed) {
                ChatActivity.this.scrollToBottom = true;
                System.out.println("New messages are there to be displayed, I am at the bottom so refreshing");
                ChatActivity.this.refreshAdapter(false);
            }
        }
    }

    /* renamed from: com.shamchat.activity.ChatActivity.6 */
    class C06956 implements OnTouchListener {
        C06956() {
        }

        public final boolean onTouch(View v, MotionEvent event) {
            try {
                ChatActivity.this.layoutSendItems.setVisibility(8);
                ChatActivity.this.fragmentManager.beginTransaction().remove(ChatActivity.this.mainChatItems).commit();
                ChatActivity.this.fragmentManager.beginTransaction().remove(ChatActivity.this.voiceRcording).commit();
                ChatActivity.this.fragmentManager.beginTransaction().remove(ChatActivity.this.smileyAndSticker).commit();
            } catch (Exception e) {
            }
            return false;
        }
    }

    /* renamed from: com.shamchat.activity.ChatActivity.7 */
    class C06967 implements OnSoftKeyBoardHideListener {
        C06967() {
        }

        public final void onSoftKeyBoardVisible(boolean visible) {
            if (!visible) {
                return;
            }
            if (VERSION.SDK_INT >= 11) {
                if (ChatActivity.this.first.booleanValue()) {
                    ChatActivity.this.list.smoothScrollToPositionFromTop(ChatActivity.this.chatMessageAdapter.getCount(), 20, 1);
                    ChatActivity.this.first = Boolean.valueOf(false);
                }
            } else if (VERSION.SDK_INT >= 8) {
                int firstVisible = ChatActivity.this.list.getFirstVisiblePosition();
                int lastVisible = ChatActivity.this.list.getLastVisiblePosition();
                if (ChatActivity.this.first.booleanValue()) {
                    if (ChatActivity.this.chatMessageAdapter.getCount() < firstVisible) {
                        ChatActivity.this.list.smoothScrollToPosition(ChatActivity.this.chatMessageAdapter.getCount());
                    } else {
                        ChatActivity.this.list.smoothScrollToPosition(((ChatActivity.this.chatMessageAdapter.getCount() + lastVisible) - firstVisible) - 2);
                    }
                    ChatActivity.this.first = Boolean.valueOf(false);
                }
            } else if (ChatActivity.this.first.booleanValue()) {
                ChatActivity.this.list.setSelectionFromTop(ChatActivity.this.chatMessageAdapter.getCount(), 0);
                ChatActivity.this.first = Boolean.valueOf(false);
            }
        }
    }

    /* renamed from: com.shamchat.activity.ChatActivity.8 */
    class C06978 implements Runnable {
        C06978() {
        }

        public final void run() {
            if (SmackableImp.isXmppConnected() && PreferenceConstants.CONNECTED_TO_NETWORK) {
                ChatActivity.this.chatController.onComposing$3b99f9eb(ChatActivity.friendJID, BuildConfig.VERSION_NAME);
            }
        }
    }

    /* renamed from: com.shamchat.activity.ChatActivity.9 */
    class C06999 implements Runnable {
        final /* synthetic */ User val$user;

        /* renamed from: com.shamchat.activity.ChatActivity.9.1 */
        class C06981 implements OnClickListener {
            C06981() {
            }

            public final void onClick(View v) {
                if (ChatActivity.this.friendId != null) {
                    ChatActivity.this.goBack();
                    ChatActivity.this.finish();
                }
            }
        }

        C06999(User user) {
            this.val$user = user;
        }

        public final void run() {
            try {
                int onlineStatus;
                if (!ChatActivity.this.isUpdated) {
                    ChatActivity.this.lastSeen.setVisibility(8);
                    ChatActivity.this.isUpdated = true;
                }
                if (this.val$user.onlineStatus == Type.unavailable.ordinal()) {
                    onlineStatus = StatusMode.offline.ordinal();
                } else {
                    onlineStatus = StatusMode.available.ordinal();
                }
                if (SmackableImp.isXmppConnected()) {
                    ChatActivity.this.imageStatus.setImageResource(StatusMode.values()[onlineStatus].drawableId);
                } else {
                    ChatActivity.this.imageStatus.setImageResource(StatusMode.values()[StatusMode.offline.ordinal()].drawableId);
                }
                ChatActivity.this.imageStatus.setVisibility(0);
                String username = this.val$user.username;
                ChatActivity.this.dialog.setTitle(username);
                if (this.val$user.mobileNo != null) {
                    ContactDetails details = Utils.getConactExists(SHAMChatApplication.getMyApplicationContext(), this.val$user.mobileNo);
                    if (details.isExist && details.displayName.length() > 0) {
                        username = details.displayName;
                    }
                }
                ChatActivity.this.textName.setText(username);
                ChatActivity.this.textName.setOnClickListener(new C06981());
                if (onlineStatus == StatusMode.offline.ordinal() && SmackableImp.isXmppConnected()) {
                    System.out.println("user is offline , Last seen fetchAndDisplayLastSeenActivity called ");
                    ChatActivity.this.fetchAndDisplayLastSeenActivity(ChatActivity.friendJID);
                    return;
                }
                System.out.println("hide Last seen text since he is available");
                ChatActivity.this.lastSeen.setVisibility(8);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ChatActivity() {
        this.threadId = null;
        this.friendId = null;
        this.isSubUriSupported = false;
        this.mainHandler = new Handler();
        this.allMessageCount = 0;
        this.showInitialLoadButton = true;
        this.loadIndex = 40;
        this.currentLoadedIndex = 0;
        this.scrollToBottom = true;
        this.txtMessageWatcher = new TextWatcher() {
            public final void afterTextChanged(Editable s) {
            }

            public final void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public final void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if ((text.length() == 0 || text.length() == 1) && SmackableImp.isXmppConnected() && PreferenceConstants.CONNECTED_TO_NETWORK) {
                    ChatActivity.this.chatController.onComposing$3b99f9eb(ChatActivity.friendJID, s.toString());
                }
            }
        };
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903078);
        this.first = Boolean.valueOf(true);
        SlideHolder slid = (SlideHolder) findViewById(2131361949);
        slid.setDirection$13462e();
        slid.setEnabled(false);
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        try {
            EventBus.getDefault().register(this, true, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(31);
        actionBar.setCustomView(2130903045);
        View view = getSupportActionBar().getCustomView();
        this.imageStatus = (ImageView) view.findViewById(2131361845);
        this.textName = (TextView) view.findViewById(2131361839);
        this.lastSeen = (TextView) view.findViewById(2131361844);
        this.currentUserId = SHAMChatApplication.getConfig().userId;
        this.friendId = getIntent().getStringExtra(INTENT_EXTRA_USER_ID);
        this.threadId = getIntent().getStringExtra(INTENT_EXTRA_THREAD_ID);
        this.dialog = new Dialog(this);
        new Thread(new C06901()).start();
        friendJID = Utils.createXmppUserIdByUserId(this.friendId);
        this.chatProvider = new ChatProviderNew();
        this.chatHandler = new Handler();
        this.fragmentManager = getSupportFragmentManager();
        new Thread(new C06912()).start();
        this.list = (ListView) findViewById(2131361931);
        this.loadingView = (RelativeLayout) findViewById(2131361934);
        this.btnLoadMore = (Button) findViewById(2131361935);
        this.progressBarLoad = (ProgressBar) findViewById(2131361936);
        this.progressBarChatLoading = (ProgressBar) findViewById(2131361933);
        chatStateText = (TextView) findViewById(2131361938);
        this.btnLoadMore.setOnClickListener(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService("layout_inflater");
        int blockCount = 0;
        Cursor cursorBlock = getContentResolver().query(RosterProvider.CONTENT_URI, new String[]{"user_status"}, "jid=? AND user_status=?", new String[]{friendJID, "1"}, null);
        if (cursorBlock != null) {
            blockCount = cursorBlock.getCount();
        }
        Cursor friendCursor = getContentResolver().query(UserProvider.CONTENT_URI_USER, new String[]{"mobileNo", "profileimage_url"}, "userId=?", new String[]{this.friendId}, null);
        friendCursor.moveToFirst();
        try {
            this.friendMobileNo = friendCursor.getString(friendCursor.getColumnIndex("mobileNo"));
            this.friendProfileImageUrl = friendCursor.getString(friendCursor.getColumnIndex("profileimage_url"));
            if (blockCount == 0 && !Utils.contactExists(getApplicationContext(), this.friendMobileNo)) {
                this.addOrBlockview = inflater.inflate(2130903151, null);
                Button btnBlockFriend = (Button) this.addOrBlockview.findViewById(2131362267);
                ((Button) this.addOrBlockview.findViewById(2131362266)).setOnClickListener(new C06923());
                btnBlockFriend.setOnClickListener(new C06934());
            }
            cursorBlock.close();
            friendCursor.close();
            if (this.addOrBlockview != null) {
                this.list.addHeaderView(this.addOrBlockview);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        Cursor myCursor = getContentResolver().query(UserProvider.CONTENT_URI_USER, new String[]{"profileimage_url"}, "userId=?", new String[]{this.currentUserId}, null);
        myCursor.moveToFirst();
        try {
            this.myProfileImageUrl = myCursor.getString(myCursor.getColumnIndex("profileimage_url"));
            myCursor.close();
        } catch (Exception e22) {
            e22.printStackTrace();
        }
        this.chatMessageAdapter = new ChatMessageAdapter(this, this.currentUserId, this.myProfileImageUrl, this.friendId, this.friendProfileImageUrl, this);
        this.list.setOnScrollListener(new C06945());
        this.btnAddChatItems = (ImageView) findViewById(2131361941);
        this.btnAddChatItems.setOnClickListener(this);
        this.layoutSendItems = (LinearLayout) findViewById(2131361947);
        this.btnAddVoice = (ImageView) findViewById(2131361942);
        this.btnAddVoice.setOnClickListener(this);
        this.btnAddSmilies = (ImageView) findViewById(2131361944);
        this.btnAddSmilies.setOnClickListener(this);
        this.list.setAdapter(this.chatMessageAdapter);
        loadFragments();
        this.chatController = ChatController.getInstance(this);
        this.chatController.registerXMPPService(getUICallback());
        this.jobManager.addJobInBackground(new FriendDBLoadJob(this.friendId));
        if (this.threadId != null) {
            this.currentLoadedIndex = this.loadIndex;
            System.out.println("With jobs for chats");
            this.jobManager.addJobInBackground(new ChatMessagesDBLoadJob(this.threadId, this.currentLoadedIndex));
        }
        this.txtMessage = (EditText) findViewById(2131361945);
        this.txtMessage.setHint(2131493011);
        this.txtMessage.addTextChangedListener(this.txtMessageWatcher);
        this.txtMessage.setOnTouchListener(new C06956());
        this.btnSend = (ImageView) findViewById(2131361946);
        this.btnSend.setOnClickListener(this);
        String forwardedMessage = getIntent().getStringExtra(FORWARDED_MESSAGE);
        if (forwardedMessage != null) {
            this.txtMessage.setText(forwardedMessage);
        }
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
        OnSoftKeyBoardHideListener c06967 = new C06967();
        View decorView = getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new C11771(decorView, c06967));
    }

    private void goBack() {
        hideSoftKeyboard(this);
        new Thread(new C06978()).start();
    }

    private void updateTitleBar(User user) {
        runOnUiThread(new C06999(user));
    }

    private void fetchAndDisplayLastSeenActivity(String jabberId) {
        new Thread(new AnonymousClass10(jabberId)).start();
    }

    protected void onResume() {
        super.onResume();
        try {
            GenericService.currentOpenedThreadId = this.threadId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((NotificationManager) getSystemService("notification")).cancelAll();
        sendSeenReceipts();
        TextView textName = (TextView) getSupportActionBar().getCustomView().findViewById(2131361839);
        ContactDetails details = Utils.getConactExists(this, this.friendMobileNo);
        if (!details.isExist || details.displayName.length() <= 0) {
            try {
                if (details.isExist && details.displayName.length() == 0) {
                    Cursor friendUsernameCursor = getContentResolver().query(UserProvider.CONTENT_URI_USER, new String[]{"name"}, "userId=?", new String[]{this.friendId}, null);
                    friendUsernameCursor.moveToFirst();
                    textName.setText(friendUsernameCursor.getString(friendUsernameCursor.getColumnIndex("name")));
                    this.list.removeHeaderView(this.addOrBlockview);
                    friendUsernameCursor.close();
                    this.chatController.bindXMPPService();
                    if (this.pd == null) {
                        this.pd.dismiss();
                    }
                }
                textName.setText(this.friendMobileNo);
                this.chatController.bindXMPPService();
                if (this.pd == null) {
                    this.pd.dismiss();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else {
            textName.setText(details.displayName);
            this.list.removeHeaderView(this.addOrBlockview);
            this.chatController.bindXMPPService();
            if (this.pd == null) {
                this.pd.dismiss();
            }
        }
    }

    protected void onPause() {
        super.onPause();
        try {
            GenericService.currentOpenedThreadId = null;
            if (SmackableImp.isXmppConnected() && PreferenceConstants.CONNECTED_TO_NETWORK) {
                this.chatController.onComposing$3b99f9eb(friendJID, BuildConfig.VERSION_NAME);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                goBack();
                finish();
                break;
        }
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater();
        return super.onCreateOptionsMenu(menu);
    }

    private void sendSeenReceipts() {
        new Thread(new Runnable() {
            public final void run() {
                Cursor cursor = ChatActivity.this.getContentResolver().query(ChatProviderNew.CONTENT_URI_CHAT, new String[]{"packet_id", "message_sender", ChatActivity.INTENT_EXTRA_THREAD_ID}, "message_status!=? AND message_type=? AND thread_id=?", new String[]{MessageStatusType.SEEN.ordinal(), MyMessageType.INCOMING_MSG.ordinal(), ChatActivity.this.threadId}, null);
                while (cursor.moveToNext()) {
                    String packetId = cursor.getString(cursor.getColumnIndex("packet_id"));
                    String to = cursor.getString(cursor.getColumnIndex("message_sender"));
                    String threadId = cursor.getString(cursor.getColumnIndex(ChatActivity.INTENT_EXTRA_THREAD_ID));
                    ContentValues cv = new ContentValues();
                    cv.put("message_status", Integer.valueOf(MessageStatusType.SENDING.ordinal()));
                    ChatActivity.this.getContentResolver().update(ChatProviderNew.CONTENT_URI_CHAT, cv, "packet_id=?", new String[]{packetId});
                    if (SmackableImp.isXmppConnected() && PreferenceConstants.CONNECTED_TO_NETWORK) {
                        ChatActivity.this.chatController.sendSeenPacket(packetId, to, threadId);
                    }
                }
                cursor.close();
            }
        }).start();
    }

    private void loadFragments() {
        this.transaction = this.fragmentManager.beginTransaction();
        this.fragmentBundle = new Bundle();
        this.fragmentBundle.putString(INTENT_EXTRA_THREAD_ID, this.threadId);
        this.fragmentBundle.putString("message_sender", this.currentUserId);
        this.fragmentBundle.putString("message_recipient", this.friendId);
        this.fragmentBundle.putBoolean("is_group_chat", false);
        this.mainChatItems = new MainChatItemsFragment();
        this.mainChatItems.setArguments(this.fragmentBundle);
        this.smileyAndSticker = new SmileyAndStickerFragment();
        this.smileyAndSticker.setArguments(this.fragmentBundle);
        this.voiceRcording = new ShamVoiceRecording();
        this.voiceRcording.setArguments(this.fragmentBundle);
        this.blankFragment = new BlankFragment();
        this.transaction.add(2131361948, this.blankFragment, BuildConfig.VERSION_NAME);
        this.transaction.commit();
    }

    @SuppressLint({"NewApi"})
    public void onClick(View v) {
        switch (v.getId()) {
            case 2131361935:
                this.progressBarLoad.setVisibility(0);
                this.btnLoadMore.setVisibility(4);
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
                String msg = this.txtMessage.getText().toString().trim();
                if (msg != null && msg.length() > 0) {
                    sendTextMessage(msg);
                }
            default:
        }
    }

    public void sendTextMessage(String messageBody) {
        new Thread(new AnonymousClass12(messageBody)).start();
    }

    public void sendMediaMessage(String fullFilePath, int messageContentType) {
        String description = BuildConfig.VERSION_NAME;
        if (messageContentType == MessageContentType.VOICE_RECORD.ordinal()) {
            description = "VOICE_RECORD";
        }
        this.jobManager.addJobInBackground(new FileUploadJob(fullFilePath, this.currentUserId, Utils.getUserIdFromXmppUserId(friendJID), false, messageContentType, description, false, null, 0.0d, 0.0d));
    }

    private void refreshAdapter(boolean isLoadMore) {
        if (isLoadMore) {
            this.scrollToBottom = false;
            this.currentLoadedIndex += this.loadIndex;
        } else {
            this.currentLoadedIndex++;
        }
        this.jobManager.addJobInBackground(new ChatMessagesDBLoadJob(this.threadId, this.currentLoadedIndex));
    }

    public void hideSoftKeyboard(Activity activity) {
        ((InputMethodManager) activity.getSystemService("input_method")).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void addSmiley(Entry<Pattern, Integer> entry) {
        this.txtMessage.setText(Emoticons.getSmiledText(SHAMChatApplication.getMyApplicationContext(), this.txtMessage.getText() + ((Pattern) entry.getKey()).toString().replace("\\Q", BuildConfig.VERSION_NAME).replace("\\E", BuildConfig.VERSION_NAME)), BufferType.SPANNABLE);
    }

    private Stub getUICallback() {
        this.callback = new Stub() {

            /* renamed from: com.shamchat.activity.ChatActivity.14.1 */
            class C06891 implements Runnable {
                C06891() {
                }

                @TargetApi(11)
                public final void run() {
                }
            }

            public final void connectionStateChanged(int connectionstate) throws RemoteException {
                ChatActivity.this.mainHandler.post(new C06891());
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

    public static void updateTypingStatus(boolean isTyping, String jabberId) {
        SHAMChatApplication.getInstance().runOnUiThread(new AnonymousClass15(jabberId, isTyping));
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        this.jobManager.addJobInBackground(new ChatThreadsDBLoadJob());
        if (SmackableImp.isXmppConnected() && PreferenceConstants.CONNECTED_TO_NETWORK) {
            this.chatController.onComposing$3b99f9eb(friendJID, BuildConfig.VERSION_NAME);
        }
        this.chatController.unbindXMPPService();
        GenericService.currentOpenedThreadId = null;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        if (this.layoutSendItems.getVisibility() == 0) {
            this.layoutSendItems.setVisibility(8);
            this.fragmentManager.beginTransaction().remove(this.mainChatItems).commit();
            this.fragmentManager.beginTransaction().remove(this.voiceRcording).commit();
            this.fragmentManager.beginTransaction().remove(this.smileyAndSticker).commit();
            this.btnAddChatItems.setImageResource(2130837659);
            return true;
        }
        finish();
        MainWindow.closedd = Boolean.valueOf(true);
        return true;
    }

    public void showProgressBarChatLoading() {
        this.progressBarChatLoading.setVisibility(0);
    }

    public void hideProgressBarChatLoading() {
        this.progressBarChatLoading.setVisibility(8);
    }

    public void onEventBackgroundThread(ChatMessagesDBLoadCompletedEvent event) {
        if (event.threadId.equals(this.threadId)) {
            this.newMessagesAreThereToBeDisplayed = false;
            runOnUiThread(new AnonymousClass16(event.messages));
        }
    }

    public void onEventBackgroundThread(MessageDeletedEvent event) {
        this.scrollToBottom = false;
        refreshAdapter(false);
    }

    public void onEventBackgroundThread(NewMessageEvent event) {
        System.out.println("NewMessageEvent from ChatActivity:  " + event.threadId);
        String tId = event.threadId;
        if (event.direction == MyMessageType.OUTGOING_MSG.ordinal()) {
            this.scrollToBottom = true;
            System.out.println("NewMessageEvent from ChatActivity:  scroll to bottom true");
        } else {
            this.scrollToBottom = true;
        }
        if (this.threadId.equals(tId)) {
            System.out.println("Chat test thread id equals new message");
            if (this.scrollToBottom) {
                refreshAdapter(false);
            } else {
                this.newMessagesAreThereToBeDisplayed = true;
            }
            sendSeenReceipts();
            return;
        }
        System.out.println("Chat test thread id not equals new message");
    }

    public void onEventMainThread(MessageStateChangedEvent event) {
        this.threadId.equals(event.threadId);
        if (this.threadId.equals(event.threadId)) {
            this.chatMessageAdapter.updateMessageStatus(event.packetId, event.messageStatusType);
            new AnonymousClass17(event).start();
        }
    }

    public void onEventBackgroundThread(FileUploadingProgressEvent event) {
        System.out.println("FileUploadingProgressEvent threadId " + this.threadId + " event.getThreadId " + event.threadId);
        if (this.threadId.equals(event.threadId)) {
            System.out.println("FileUploadingProgressEvent thread equals");
            if (event.uploadedPercentage == 9999) {
                System.out.println("FileUploadingProgressEvent == 9999");
                this.chatMessageAdapter.setUploadedPercentage(event.packetId, event.uploadedPercentage);
                return;
            } else if (event.uploadedPercentage == 100) {
                System.out.println("FileUploadingProgressEvent == 100");
                this.chatMessageAdapter.hideProgresssBarAndSetUrl(event.packetId, BuildConfig.VERSION_NAME);
                sendBOBMessages(event.threadId);
                return;
            } else {
                this.chatMessageAdapter.setUploadedPercentage(event.packetId, event.uploadedPercentage);
                return;
            }
        }
        System.out.println("FileUploadingProgressEvent different thread");
    }

    public void onEventMainThread(ImageDownloadProgressEvent event) {
        if (this.chatMessageAdapter.chatMap.containsKey(event.packetId)) {
            Row row = (Row) this.chatMessageAdapter.chatMap.get(event.packetId);
            if (row instanceof IncomingMsg) {
                IncomingMsg incomingMsg = (IncomingMsg) row;
                ChatMessage chatMessage = incomingMsg.chatMessage;
                ViewHolder viewHolder = incomingMsg.viewHolder;
                if (event.isDone || viewHolder == null) {
                    viewHolder.downloadingProgress.setVisibility(8);
                    viewHolder.downloadStart.setVisibility(8);
                    chatMessage.fileUrl = event.downloadedFilePath;
                    refreshVisibleViewsImages();
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
            if (row instanceof IncomingMsg) {
                IncomingMsg incomingMsg = (IncomingMsg) row;
                ChatMessage chatMessage = incomingMsg.chatMessage;
                ViewHolder viewHolder = incomingMsg.viewHolder;
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

    void refreshVisibleViews() {
        if (this.chatMessageAdapter != null) {
            for (int i = this.list.getFirstVisiblePosition(); i <= this.list.getLastVisiblePosition(); i++) {
                int dataPosition = i - this.list.getHeaderViewsCount();
                int childPosition = i - this.list.getFirstVisiblePosition();
                if (dataPosition >= 0 && dataPosition < this.chatMessageAdapter.getCount() && this.list.getChildAt(childPosition) != null) {
                    Log.v("ChatActivity", "Refreshing view (data=" + dataPosition + ",child=" + childPosition + ")");
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
                if (dataPosition >= 0 && dataPosition < this.chatMessageAdapter.getCount() && this.list.getChildAt(childPosition) != null && ((ImageView) this.list.getChildAt(childPosition).findViewById(2131362134)).getVisibility() == 0) {
                    this.chatMessageAdapter.getView(dataPosition, this.list.getChildAt(childPosition), this.list);
                }
            }
        }
    }

    private void sendBOBMessages(String tId) {
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(ChatProviderNew.CONTENT_URI_CHAT, null, "thread_id=?", new String[]{tId}, "_id DESC LIMIT " + this.currentLoadedIndex + " ");
            if (this.chatProvider != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ChatMessage message = ChatProviderNew.getChatMessageByCursor(cursor);
                    String packetID = message.packetId;
                    if (message.threadId.equals(this.threadId)) {
                        if (this.chatMessageAdapter.arrkey.contains(message.packetId)) {
                            this.chatMessageAdapter.updateMessageStatus(packetID, message.messageStatus);
                            if (message.messageStatus == MessageStatusType.SENDING && !(message.messageContentType == MessageContentType.TEXT && message.messageContentType == MessageContentType.STICKER)) {
                                System.out.println("ChatActivity  image/video/voice already exists in chatmap no need to add: " + message.textMessage);
                                String uploadedUrl = message.uploadedFileUrl;
                                if (uploadedUrl == null) {
                                    this.chatMessageAdapter.setUploadedPercentage(packetID, message.uploadedPercentage);
                                } else {
                                    this.chatMessageAdapter.hideProgresssBarAndSetUrl(packetID, uploadedUrl);
                                    new Thread(new AnonymousClass19(uploadedUrl, message)).start();
                                }
                            }
                        } else {
                            this.currentLoadedIndex++;
                            this.chatMessageAdapter.add(message, MyMessageType.HEADER_MSG);
                            this.chatMessageAdapter.add(message, message.incomingMessage);
                            System.out.println("ChatActivity adding image/video/voice to chatmap chatMessageAdapter: " + message.textMessage);
                            runOnUiThread(new Runnable() {
                                public final void run() {
                                    ChatActivity.this.chatMessageAdapter.notifyDataSetChanged();
                                }
                            });
                            if (message.sender.equals(this.friendId)) {
                                sendSeenReceipts();
                            }
                        }
                    }
                }
            }
            cursor.close();
        } catch (Throwable th) {
            cursor.close();
        }
    }

    public void onEventBackgroundThread(FriendDBLoadCompletedEvent event) {
        User user = event.user;
        if (user != null && user.userId.equals(this.friendId)) {
            updateTitleBar(user);
        }
    }

    public void onEventBackgroundThread(PresenceChangedEvent event) {
        String userId = event.userId;
        if (userId != null && userId.equals(this.friendId)) {
            this.jobManager.addJobInBackground(new FriendDBLoadJob(this.friendId));
        }
    }
}

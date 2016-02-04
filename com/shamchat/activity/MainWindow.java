package com.shamchat.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.agimind.widget.SlideHolder;
import com.appaholics.updatechecker.DownloadManager;
import com.appaholics.updatechecker.UpdateChecker;
import com.path.android.jobqueue.JobManager;
import com.rokhgroup.activities.Notifications;
import com.rokhgroup.activities.SearchInPost;
import com.rokhgroup.activities.SearchInUsers;
import com.rokhgroup.activities.SendNewPost;
import com.rokhgroup.activities.UserProfile;
import com.rokhgroup.dialog.DialogBuilder;
import com.rokhgroup.fragments.JahanbinFragment;
import com.rokhgroup.fragments.UserStreamFragment;
import com.rokhgroup.mqtt.MQTTService;
import com.rokhgroup.mqtt.NotifySimple;
import com.rokhgroup.utils.RokhPref;
import com.rokhgroup.utils.Utils;
import com.shamchat.adapters.MyMessageType;
import com.shamchat.androidclient.ChatController;
import com.shamchat.androidclient.IXMPPChatCallback.Stub;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.androidclient.data.ZaminConfiguration;
import com.shamchat.androidclient.service.SmackableImp;
import com.shamchat.androidclient.service.XMPPService;
import com.shamchat.androidclient.util.ConnectionState;
import com.shamchat.androidclient.util.PreferenceConstants;
import com.shamchat.events.SyncContactsCompletedEvent;
import com.shamchat.jobs.MarkFailedStatusToFailedFilesDBLoadJob;
import com.shamchat.jobs.MessageStateChangedJob;
import com.shamchat.jobs.RemoveOldMomentsDBJob;
import com.shamchat.jobs.StickersDownloadJob;
import com.shamchat.models.ChatMessage.MessageStatusType;
import com.shamchat.moments.MomentComposerActivity;
import com.shamchat.pushnotification.SHAMPushManager;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import de.greenrobot.event.EventBus;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;

public class MainWindow extends AppCompatActivity implements TabListener, OnClickListener {
    public static boolean DEBUG = false;
    private static Boolean NotifFlag = null;
    private static final int PICK_CONTACT = 0;
    private static final int PICK_CONTACT_REQUEST = 1;
    private static final int TAB_GROUPS = 0;
    private static final int TAB_MESSAGE = 2;
    private static final int TAB_MOMENTS = 1;
    private static final String TAG = "com.shamchat.androidclient.MainWindow";
    private static long back_pressed;
    public static int cash1;
    public static Boolean closedd;
    public static String serch_titel;
    public static int shake;
    String CURRENT_USER_ID;
    String CURRENT_USER_NAME;
    String CURRENT_USER_TOKEN;
    private String ReconnectionInProgressString;
    RokhPref Session;
    private ActionBar actionBar;
    private ImageButton addNewButton;
    private ImageButton addNewPost;
    private ImageButton addcon;
    private Stub callback;
    private ChangeListener changeListener;
    private ChatController chatController;
    private ChatThreadFragment chatThreadFragment;
    String clientHandle;
    private ImageButton composeButton;
    private ImageButton composeButtonGroup;
    private ImageButton contact;
    private ContactGroupsFragment contactGroupsFragment;
    private String currentUserID;
    private SharedPreferences defaultPref;
    Dialog dialog;
    private ImageButton favorit;
    private ImageButton favoriteButton;
    private ImageButton goAheadButton;
    private int goToFragment;
    private ImageButton goaheadButton;
    private ImageButton group;
    private boolean isSearchBarAlreadyVisible;
    private JahanbinFragment jahanbinFragment;
    private JobManager jobManager;
    private TextView keyboardTextview;
    private ImageButton keypadButton;
    private ZaminConfiguration mConfig;
    private ContentResolver mContentResolver;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private String mTheme;
    private ViewPager mViewPager;
    private Handler mainHandler;
    private ImageButton menuButton;
    private Handler messageHandler;
    Runnable messageHandlingService;
    private ImageButton momentComposeButton;
    private Handler momentSyncHandler;
    MqttAndroidClient mqttClient;
    private View myPostButton;
    Runnable networkServiceRunnable;
    private ImageView notifBack;
    private LinearLayout notifLinear;
    private TextView notifText;
    private String phoneNumber;
    private ImageButton rithMenu;
    SlideHolder f14s;
    private ImageButton searchButton;
    private ViewGroup searchContainerLinearLayout;
    private EditText searchEditText;
    private ImageButton searchInPost;
    private TextWatcher searchTextWatcher;
    private Handler serviceConnectivityHandler;
    private SHAMPushManager shamPushManager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sp;
    private ImageView syncContacts;
    private ProgressBar syncContactsLoader;
    private UserStreamFragment userStreamFragment;
    private int f15x;

    /* renamed from: com.shamchat.activity.MainWindow.1 */
    class C07681 implements OnClickListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ Editor val$edit;

        C07681(Editor editor, Dialog dialog) {
            this.val$edit = editor;
            this.val$dialog = dialog;
        }

        public final void onClick(View v) {
            this.val$edit.putInt("a", MainWindow.TAB_MOMENTS);
            this.val$edit.apply();
            this.val$dialog.cancel();
        }
    }

    /* renamed from: com.shamchat.activity.MainWindow.24 */
    class AnonymousClass24 extends DialogBuilder {

        /* renamed from: com.shamchat.activity.MainWindow.24.1 */
        class C07711 implements OnClickListener {
            final /* synthetic */ InputMethodManager val$imm;

            C07711(InputMethodManager inputMethodManager) {
                this.val$imm = inputMethodManager;
            }

            public final void onClick(View v) {
                this.val$imm.toggleSoftInput(MainWindow.TAB_MESSAGE, MainWindow.TAB_GROUPS);
                MainWindow.this.dialog.dismiss();
            }
        }

        /* renamed from: com.shamchat.activity.MainWindow.24.2 */
        class C07722 implements OnClickListener {
            final /* synthetic */ View val$contentView;
            final /* synthetic */ InputMethodManager val$imm;
            final /* synthetic */ RadioGroup val$mRadioGroup;
            final /* synthetic */ EditText val$mSearch;

            C07722(RadioGroup radioGroup, View view, EditText editText, InputMethodManager inputMethodManager) {
                this.val$mRadioGroup = radioGroup;
                this.val$contentView = view;
                this.val$mSearch = editText;
                this.val$imm = inputMethodManager;
            }

            public final void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(MainWindow.this, 2130968603);
                RadioButton SelectedRadio = (RadioButton) this.val$contentView.findViewById(this.val$mRadioGroup.getCheckedRadioButtonId());
                String mSearchText = this.val$mSearch.getText().toString();
                if (mSearchText.matches(BuildConfig.VERSION_NAME)) {
                    this.val$mSearch.startAnimation(animation);
                    this.val$mSearch.requestFocus();
                    return;
                }
                Intent i = new Intent(MainWindow.this, SearchInPost.class);
                Intent j = new Intent(MainWindow.this, SearchInUsers.class);
                MainWindow.serch_titel = mSearchText.toString();
                if (SelectedRadio.getText().equals(MainWindow.this.getString(2131493344))) {
                    i.putExtra("SearchText", mSearchText);
                    i.addFlags(67108864);
                    i.setFlags(ClientDefaults.MAX_MSG_SIZE);
                    MainWindow.this.startActivity(i);
                } else if (SelectedRadio.getText().equals(MainWindow.this.getString(2131493345))) {
                    j.putExtra("SearchText", mSearchText);
                    j.addFlags(67108864);
                    j.setFlags(ClientDefaults.MAX_MSG_SIZE);
                    MainWindow.this.startActivity(j);
                }
                this.val$imm.toggleSoftInput(MainWindow.TAB_MESSAGE, MainWindow.TAB_GROUPS);
                MainWindow.this.dialog.dismiss();
            }
        }

        AnonymousClass24(Context context) {
            super(context);
        }

        public final Dialog build() {
            InputMethodManager imm = (InputMethodManager) MainWindow.this.getSystemService("input_method");
            this.ParsvidBuilder.ParsvidTitle = BuildConfig.VERSION_NAME;
            View contentView = this.ParsvidInflater.inflate(2130903237, null);
            EditText mSearch = (EditText) contentView.findViewById(2131362551);
            RadioGroup mRadioGroup = (RadioGroup) contentView.findViewById(2131362552);
            RadioButton mSearchVideo = (RadioButton) contentView.findViewById(2131362554);
            RadioButton mSearchUser = (RadioButton) contentView.findViewById(2131362553);
            mSearch.setTypeface(Utils.GetNaskhRegular(MainWindow.this));
            mSearch.requestFocus();
            imm.toggleSoftInput(MainWindow.TAB_MESSAGE, MainWindow.TAB_GROUPS);
            mSearchVideo.setTypeface(Utils.GetNaskhRegular(MainWindow.this));
            mSearchUser.setTypeface(Utils.GetNaskhRegular(MainWindow.this));
            this.ParsvidBuilder.ParsvidContentView = contentView;
            this.ParsvidBuilder.addButton(MainWindow.this.getString(2131493333), new C07711(imm));
            this.ParsvidBuilder.addButton(MainWindow.this.getString(2131493342), new C07722(mRadioGroup, contentView, mSearch, imm));
            return this.ParsvidBuilder.create();
        }
    }

    /* renamed from: com.shamchat.activity.MainWindow.2 */
    class C07762 implements OnCheckedChangeListener {
        final /* synthetic */ Dialog val$dialog;
        final /* synthetic */ Editor val$edit;

        C07762(Editor editor, Dialog dialog) {
            this.val$edit = editor;
            this.val$dialog = dialog;
        }

        public final void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            this.val$edit.putInt("a", MainWindow.TAB_MOMENTS);
            this.val$edit.apply();
            this.val$dialog.cancel();
        }
    }

    /* renamed from: com.shamchat.activity.MainWindow.3 */
    class C07803 implements Runnable {

        /* renamed from: com.shamchat.activity.MainWindow.3.1 */
        class C07791 implements Runnable {
            final /* synthetic */ UpdateChecker val$checker;

            /* renamed from: com.shamchat.activity.MainWindow.3.1.1 */
            class C07771 implements DialogInterface.OnClickListener {
                C07771() {
                }

                public final void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            }

            /* renamed from: com.shamchat.activity.MainWindow.3.1.2 */
            class C07782 implements DialogInterface.OnClickListener {
                C07782() {
                }

                public final void onClick(DialogInterface dialog, int id) {
                    UpdateChecker updateChecker = C07791.this.val$checker;
                    String str = "http://updatesalam.mohavere.com/updater9/update.apk";
                    if (updateChecker.isOnline()) {
                        updateChecker.downloadManager = new DownloadManager(updateChecker.mContext);
                        DownloadManager downloadManager = updateChecker.downloadManager;
                        String[] strArr = new String[MainWindow.TAB_MOMENTS];
                        strArr[MainWindow.TAB_GROUPS] = str;
                        downloadManager.execute(strArr);
                    } else if (updateChecker.useToasts) {
                        updateChecker.makeToastFromString("App update failed. No internet connection available").show();
                    }
                }
            }

            C07791(UpdateChecker updateChecker) {
                this.val$checker = updateChecker;
            }

            public final void run() {
                Builder alertDialogBuilder = new Builder(MainWindow.this);
                alertDialogBuilder.setTitle(MainWindow.this.getString(2131492992));
                alertDialogBuilder.setMessage(MainWindow.this.getString(2131492977)).setCancelable(true).setPositiveButton(MainWindow.this.getString(2131492992), new C07782()).setNegativeButton(MainWindow.this.getString(2131492991), new C07771());
                alertDialogBuilder.create().show();
            }
        }

        C07803() {
        }

        public final void run() {
            Looper.prepare();
            UpdateChecker checker = new UpdateChecker(MainWindow.this);
            Log.i("revision", Integer.toString(checker.getVersionCode()));
            String str = "http://updatesalam.mohavere.com/updater9/versioncode.txt";
            if (checker.isOnline()) {
                if (checker.haveValidContext) {
                    int versionCode = checker.getVersionCode();
                    if (versionCode >= 0) {
                        try {
                            if (Integer.parseInt(checker.readFile(str)) > versionCode) {
                                checker.updateAvailable = true;
                            }
                        } catch (NumberFormatException e) {
                            Log.e(checker.TAG, "Invalid number online");
                        }
                    } else {
                        Log.e(checker.TAG, "Invalid version code in app");
                    }
                } else {
                    Log.e(checker.TAG, "Context is null");
                }
            } else if (checker.useToasts) {
                checker.makeToastFromString("App update check failed. No internet connection available").show();
            }
            if (checker.updateAvailable) {
                MainWindow.this.mainHandler.post(new C07791(checker));
            }
            Looper.loop();
        }
    }

    /* renamed from: com.shamchat.activity.MainWindow.4 */
    class C07824 extends Stub {

        /* renamed from: com.shamchat.activity.MainWindow.4.1 */
        class C07811 implements Runnable {
            final /* synthetic */ int val$connectionstate;

            C07811(int i) {
                this.val$connectionstate = i;
            }

            @TargetApi(11)
            public final void run() {
                Log.d(MainWindow.TAG, "MainWindow connectionStatusChanged: " + ConnectionState.values()[this.val$connectionstate]);
            }
        }

        C07824() {
        }

        public final void connectionStateChanged(int connectionstate) throws RemoteException {
            MainWindow.this.mainHandler.post(new C07811(connectionstate));
        }

        public final void roomCreated(boolean created) throws RemoteException {
            Log.d(MainWindow.TAG, "MainWindow room created " + created);
        }

        public final void didJoinRoom(boolean joined) throws RemoteException {
            Log.d(MainWindow.TAG, "MainWindow joined room " + joined);
        }

        public final void onFriendComposing(String jabberId, boolean isTypng) throws RemoteException {
        }
    }

    /* renamed from: com.shamchat.activity.MainWindow.5 */
    class C07835 implements OnClickListener {
        C07835() {
        }

        public final void onClick(View arg0) {
            MainWindow.NotifFlag = Boolean.valueOf(false);
            MainWindow.this.startActivity(new Intent(MainWindow.this, Notifications.class));
        }
    }

    /* renamed from: com.shamchat.activity.MainWindow.6 */
    class C07846 implements OnClickListener {
        C07846() {
        }

        public final void onClick(View arg0) {
            MainWindow.this.startActivity(new Intent(MainWindow.this, SendNewPost.class));
        }
    }

    /* renamed from: com.shamchat.activity.MainWindow.7 */
    class C07857 implements OnClickListener {
        C07857() {
        }

        public final void onClick(View arg0) {
            MainWindow.this.SearchDialog();
        }
    }

    /* renamed from: com.shamchat.activity.MainWindow.8 */
    class C07868 extends SimpleOnPageChangeListener {
        C07868() {
        }

        public final void onPageSelected(int position) {
            MainWindow.this.actionBar.setSelectedNavigationItem(position);
        }
    }

    /* renamed from: com.shamchat.activity.MainWindow.9 */
    class C07879 implements OnClickListener {
        C07879() {
        }

        public final void onClick(View v) {
            if (MainWindow.this.f14s.open()) {
                MainWindow.this.f14s.close();
                return;
            }
            MainWindow.this.f14s.open();
            MainWindow.this.f14s.close();
        }
    }

    private class ChangeListener implements PropertyChangeListener {
        private ChangeListener() {
        }

        public final void propertyChange(PropertyChangeEvent event) {
            Log.e("MQTT STATUS", event.toString());
            Intent intent = new Intent();
            intent.setClassName(MainWindow.this, "org.eclipse.paho.android.service.sample.ConnectionDetails");
            intent.putExtra("handle", MainWindow.this.clientHandle);
            if (MainWindow.DEBUG) {
                NotifySimple.notifcation$34410889(MainWindow.this, "MainWindow :" + event.getSource(), intent);
            }
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        FragmentTransaction fragmentTransaction;
        FragmentManager manager;
        Fragment tempfragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.tempfragment = null;
            this.fragmentTransaction = fm.beginTransaction();
            this.manager = fm;
        }

        public Fragment getItem(int position) {
            switch (position) {
                case MainWindow.TAB_GROUPS /*0*/:
                    MainWindow.this.jahanbinFragment = JahanbinFragment.newInstance(MainWindow.this);
                    return MainWindow.this.jahanbinFragment;
                case MainWindow.TAB_MOMENTS /*1*/:
                    MainWindow.this.userStreamFragment = UserStreamFragment.newInstance(MainWindow.this);
                    return MainWindow.this.userStreamFragment;
                case MainWindow.TAB_MESSAGE /*2*/:
                    MainWindow.this.chatThreadFragment = new ChatThreadFragment();
                    return MainWindow.this.chatThreadFragment;
                default:
                    return null;
            }
        }

        public int getCount() {
            return 3;
        }

        public void replaceFragment(Fragment fragment) {
        }
    }

    public MainWindow() {
        this.changeListener = new ChangeListener();
        this.mainHandler = new Handler();
        this.ReconnectionInProgressString = null;
        this.isSearchBarAlreadyVisible = false;
        this.messageHandler = new Handler();
        this.serviceConnectivityHandler = new Handler();
        this.mContentResolver = null;
        this.currentUserID = null;
        this.f15x = TAB_MOMENTS;
        this.goToFragment = TAB_GROUPS;
        this.momentSyncHandler = new Handler();
        this.searchTextWatcher = new TextWatcher() {
            public final void afterTextChanged(Editable s) {
            }

            public final void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public final void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
        this.networkServiceRunnable = new Runnable() {

            /* renamed from: com.shamchat.activity.MainWindow.22.1 */
            class C07691 implements Runnable {
                C07691() {
                }

                public final void run() {
                    Cursor cursor;
                    try {
                        if (!InetAddress.getByName("s9.rabtcdn.com").isReachable(5000)) {
                            PreferenceConstants.CONNECTED_TO_NETWORK = false;
                            MainWindow.this.ReconnectionInProgressString = "reconnect_required";
                        } else if (SmackableImp.isXmppConnected()) {
                            PreferenceConstants.CONNECTED_TO_NETWORK = true;
                            cursor = null;
                            ContentResolver access$1000 = MainWindow.this.mContentResolver;
                            Uri uri = ChatProviderNew.CONTENT_URI_CHAT;
                            String[] strArr = new String[]{"packet_id", "uploaded_file_url", ChatInitialForGroupChatActivity.INTENT_EXTRA_MESSAGE_CONTENT_TYPE, ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID};
                            String[] strArr2 = new String[MainWindow.TAB_MESSAGE];
                            strArr2[MainWindow.TAB_GROUPS] = String.valueOf(MessageStatusType.SENDING.ordinal());
                            strArr2[MainWindow.TAB_MOMENTS] = String.valueOf(MyMessageType.OUTGOING_MSG.ordinal());
                            cursor = access$1000.query(uri, strArr, "message_status=? AND message_type=?", strArr2, null);
                            while (cursor.moveToNext()) {
                                int contentType = cursor.getInt(cursor.getColumnIndex(ChatInitialForGroupChatActivity.INTENT_EXTRA_MESSAGE_CONTENT_TYPE));
                                boolean isGroup = false;
                                if (cursor.getString(cursor.getColumnIndex(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID)).contains("g")) {
                                    isGroup = true;
                                }
                                if (!isGroup) {
                                    if (contentType != MessageContentType.TEXT.ordinal()) {
                                        String uploadedFile = cursor.getString(cursor.getColumnIndex("uploaded_file_url"));
                                        if (uploadedFile != null && uploadedFile.length() > 0) {
                                            MainWindow.this.updateSentStatus(cursor.getString(cursor.getColumnIndex("packet_id")));
                                        }
                                    } else {
                                        MainWindow.this.updateSentStatus(cursor.getString(cursor.getColumnIndex("packet_id")));
                                    }
                                }
                            }
                            cursor.close();
                        } else {
                            PreferenceConstants.CONNECTED_TO_NETWORK = false;
                            if (MainWindow.this.ReconnectionInProgressString != null && !MainWindow.this.ReconnectionInProgressString.equalsIgnoreCase("reconnect")) {
                                MainWindow.this.ReconnectionInProgressString = "reconnect";
                                Intent xmppServiceIntent = new Intent(MainWindow.this.getApplicationContext(), XMPPService.class);
                                xmppServiceIntent.setAction("reconnect");
                                MainWindow.this.startService(xmppServiceIntent);
                            }
                        }
                    } catch (UnknownHostException e) {
                        PreferenceConstants.CONNECTED_TO_NETWORK = false;
                        MainWindow.this.ReconnectionInProgressString = "reconnect_required";
                    } catch (IOException e2) {
                        PreferenceConstants.CONNECTED_TO_NETWORK = false;
                        MainWindow.this.ReconnectionInProgressString = "reconnect_required";
                    } catch (Throwable th) {
                        cursor.close();
                    }
                }
            }

            public final void run() {
                new Thread(new C07691()).start();
                MainWindow.this.serviceConnectivityHandler.postDelayed(this, 2000);
            }
        };
        this.messageHandlingService = new Runnable() {

            /* renamed from: com.shamchat.activity.MainWindow.23.1 */
            class C07701 implements Runnable {
                public final void run() {
                    /* JADX: method processing error */
/*
                    Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:50:0x0080 in {2, 38, 40, 42, 44, 49, 51, 52, 53, 54, 55, 56, 57, 58, 59, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 78} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.rerun(BlockProcessor.java:44)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:57)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
                    /*
                    r21 = this;
                    r7 = 0;
                    r0 = r21;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = com.shamchat.activity.MainWindow.AnonymousClass23.this;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = com.shamchat.activity.MainWindow.this;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = r1.mContentResolver;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r2 = com.shamchat.androidclient.data.ChatProviderNew.CONTENT_URI_CHAT;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r3 = 0;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r4 = "message_status!=? OR message_status!=? AND message_sender=?";	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r5 = 3;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r5 = new java.lang.String[r5];	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r6 = 0;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r20 = "3";	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r5[r6] = r20;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r6 = 1;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r20 = "4";	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r5[r6] = r20;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r6 = 2;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r0 = r21;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r0 = com.shamchat.activity.MainWindow.AnonymousClass23.this;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r20 = r0;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r0 = r20;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r0 = com.shamchat.activity.MainWindow.this;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r20 = r0;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r20 = r20.currentUserID;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r5[r6] = r20;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r6 = 0;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r7 = r1.query(r2, r3, r4, r5, r6);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                L_0x0035:
                    r1 = r7.moveToNext();	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    if (r1 == 0) goto L_0x0179;
                L_0x003b:
                    r17 = 0;
                    r1 = "thread_id";	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = r7.getColumnIndex(r1);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r18 = r7.getString(r1);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r0 = r21;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = com.shamchat.activity.MainWindow.AnonymousClass23.this;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = com.shamchat.activity.MainWindow.this;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = r1.mContentResolver;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r2 = com.shamchat.androidclient.data.ChatProviderNew.CONTENT_URI_THREAD;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r3 = 1;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r3 = new java.lang.String[r3];	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r4 = 0;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r5 = "is_group_chat";	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r3[r4] = r5;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r4 = "thread_id=?";	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r5 = 1;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r5 = new java.lang.String[r5];	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r6 = 0;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r5[r6] = r18;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r6 = 0;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r17 = r1.query(r2, r3, r4, r5, r6);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r17.moveToFirst();	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = "is_group_chat";	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r0 = r17;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = r0.getColumnIndex(r1);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r0 = r17;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = r0.getInt(r1);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r2 = 1;
                    if (r1 != r2) goto L_0x008a;
                L_0x007c:
                    r17.close();
                    goto L_0x0035;
                L_0x0080:
                    r1 = move-exception;
                    r1.printStackTrace();	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    if (r7 == 0) goto L_0x0089;
                L_0x0086:
                    r7.close();
                L_0x0089:
                    return;
                L_0x008a:
                    r17.close();	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = "message_recipient";	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = r7.getColumnIndex(r1);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = r7.getString(r1);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r15 = com.shamchat.utils.Utils.createXmppUserIdByUserId(r1);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r10 = new org.jivesoftware.smack.packet.Message;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = org.jivesoftware.smack.packet.Message.Type.chat;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r10.<init>(r15, r1);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r8 = 0;
                    r0 = r21;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = com.shamchat.activity.MainWindow.AnonymousClass23.this;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = com.shamchat.activity.MainWindow.this;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = r1.mContentResolver;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r2 = com.shamchat.androidclient.data.RosterProvider.CONTENT_URI;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r3 = 1;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r3 = new java.lang.String[r3];	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r4 = 0;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r5 = "status_mode";	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r3[r4] = r5;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r4 = "jid=?";	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r5 = 1;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r5 = new java.lang.String[r5];	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r6 = 0;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r5[r6] = r15;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r6 = 0;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r8 = r1.query(r2, r3, r4, r5, r6);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r8.moveToFirst();	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = "status_mode";	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = r8.getColumnIndex(r1);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r14 = r8.getInt(r1);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r8.close();
                    r1 = com.shamchat.androidclient.util.StatusMode.available;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = r1.ordinal();	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    if (r14 != r1) goto L_0x0035;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                L_0x00dc:
                    r1 = "text_message";	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = r7.getColumnIndex(r1);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r16 = r7.getString(r1);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = "packet_id";	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = r7.getColumnIndex(r1);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = r7.getString(r1);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r10.packetID = r1;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r0 = r16;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r10.setBody(r0);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r0 = r21;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = com.shamchat.activity.MainWindow.AnonymousClass23.this;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = com.shamchat.activity.MainWindow.this;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = r1.mConfig;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = r1.jabberID;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r10.from = r1;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = new org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1.<init>();	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r10.addExtension(r1);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = "message_content_type";	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = r7.getColumnIndex(r1);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r11 = r7.getInt(r1);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = "description";	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = r7.getColumnIndex(r1);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r9 = r7.getString(r1);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r12 = new com.shamchat.androidclient.chat.extension.MessageContentTypeExtention;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = java.lang.String.valueOf(r11);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r12.<init>(r1, r9);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r10.addExtension(r12);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType.TEXT;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = r1.ordinal();	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    if (r11 == r1) goto L_0x0147;
                L_0x0135:
                    r1 = "uploaded_file_url";	 Catch:{ Exception -> 0x0180 }
                    r1 = r7.getColumnIndex(r1);	 Catch:{ Exception -> 0x0180 }
                    r19 = r7.getString(r1);	 Catch:{ Exception -> 0x0180 }
                    r1 = r19.length();	 Catch:{ Exception -> 0x0180 }
                    if (r1 == 0) goto L_0x0035;
                L_0x0145:
                    if (r19 == 0) goto L_0x0035;
                L_0x0147:
                    r1 = com.shamchat.androidclient.service.SmackableImp.getXmppConnection();	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1.sendPacket(r10);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r13 = new com.shamchat.events.NewMessageEvent;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r13.<init>();	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r0 = r18;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r13.threadId = r0;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = com.shamchat.adapters.MyMessageType.OUTGOING_MSG;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = r1.ordinal();	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r13.direction = r1;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1 = de.greenrobot.event.EventBus.getDefault();	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r1.postSticky(r13);	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    goto L_0x0035;
                L_0x0168:
                    r1 = move-exception;
                    if (r7 == 0) goto L_0x016e;
                L_0x016b:
                    r7.close();
                L_0x016e:
                    throw r1;
                L_0x016f:
                    r1 = move-exception;
                    r17.close();	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    throw r1;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                L_0x0174:
                    r1 = move-exception;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                    r8.close();
                    throw r1;	 Catch:{ all -> 0x0174, all -> 0x016f, Exception -> 0x0080, all -> 0x0168 }
                L_0x0179:
                    if (r7 == 0) goto L_0x0089;
                L_0x017b:
                    r7.close();
                    goto L_0x0089;
                L_0x0180:
                    r1 = move-exception;
                    goto L_0x0035;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.shamchat.activity.MainWindow.23.1.run():void");
                }

                C07701() {
                }
            }

            public final void run() {
                if (!(MainWindow.this.mContentResolver == null || MainWindow.this.currentUserID == null || !SmackableImp.isXmppConnected())) {
                    new Thread(new C07701()).start();
                }
                MainWindow.this.messageHandler.postDelayed(this, 8000);
            }
        };
    }

    static {
        DEBUG = false;
        NotifFlag = Boolean.valueOf(false);
        closedd = Boolean.valueOf(false);
        back_pressed = 0;
    }

    public void onCreate(Bundle savedInstanceState) {
        initNotification(false);
        cash1 = TAB_MOMENTS;
        this.defaultPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        this.phoneNumber = this.defaultPref.getString("user_mobileNo", null);
        try {
            EventBus.getDefault().register(this, true, 9);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, getString(2131492994));
        this.sp = getApplicationContext().getSharedPreferences("active", TAB_GROUPS);
        Editor edit = this.sp.edit();
        if (this.sp.getInt("a", TAB_GROUPS) != TAB_MOMENTS) {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(TAB_MOMENTS);
            dialog.setContentView(2130903191);
            dialog.show();
            ((TextView) dialog.findViewById(2131362391)).setText(2131493329);
            CheckBox ch = (CheckBox) dialog.findViewById(2131362393);
            ((LinearLayout) dialog.findViewById(2131362392)).setOnClickListener(new C07681(edit, dialog));
            ch.setOnCheckedChangeListener(new C07762(edit, dialog));
        }
        this.mConfig = SHAMChatApplication.getConfig();
        this.currentUserID = this.mConfig.userId;
        this.mTheme = this.mConfig.theme;
        this.mConfig.theme.equals("light");
        setTheme(2131558408);
        super.onCreate(savedInstanceState);
        this.Session = new RokhPref(this);
        this.CURRENT_USER_ID = this.Session.getUSERID();
        this.CURRENT_USER_TOKEN = this.Session.getUSERTOKEN();
        this.CURRENT_USER_NAME = this.Session.getUsername();
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        this.mContentResolver = getContentResolver();
        this.serviceConnectivityHandler.postDelayed(this.networkServiceRunnable, 4000);
        boolean register = getIntent().getBooleanExtra("register", false);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.sharedPreferences.edit();
        if (VERSION.SDK_INT >= 16) {
            Boolean.valueOf(true);
        }
        new Thread(new C07803()).start();
        if (this.currentUserID == null || this.currentUserID.length() <= 0) {
            System.exit(TAB_GROUPS);
        } else {
            this.goToFragment = getIntent().getIntExtra("goto", TAB_GROUPS);
            setupContenView();
            this.chatController = ChatController.getInstance(this);
            this.chatController.registerXMPPService(getUICallback());
            String password = this.mConfig.password;
            startService(new Intent(this, MQTTService.class));
            if (register) {
                ChatController chatController = this.chatController;
                String str = this.currentUserID;
                Log.d("com.shamchat.androidclient.ChatController", "Login start");
                Editor edit2 = PreferenceManager.getDefaultSharedPreferences(chatController.f23c).edit();
                chatController.mWithJabberID = str + "@rabtcdn.com";
                edit2.putString("account_jabberID", chatController.mWithJabberID);
                edit2.putString("account_jabberPW", password);
                edit2.commit();
                Object obj = (chatController.isConnected() || chatController.isConnecting()) ? TAB_MOMENTS : null;
                PreferenceManager.getDefaultSharedPreferences(chatController.f23c).edit().putBoolean("connstartup", obj == null).commit();
                if (obj != null) {
                    chatController.chatServiceAdapter.disconnect();
                    chatController.f23c.stopService(chatController.xmppServiceIntent);
                } else {
                    chatController.startConnection(true);
                }
            } else {
                this.chatController.login(this.currentUserID, password);
            }
        }
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if ("android.intent.action.SEND".equals(action) && type != null && ("text/plain".equals(type) || type.startsWith("image/") || type.startsWith("video/") || type.startsWith("audio/"))) {
            intent.setClass(this, ShareIntentChatActivity.class);
            startActivity(intent);
        }
        setFailedStatusToFailedFiles();
        removeOldMoments();
        downloadStickers();
    }

    public void onEventMainThread(SyncContactsCompletedEvent event) {
    }

    private void downloadStickers() {
        this.jobManager.addJobInBackground(new StickersDownloadJob());
    }

    private void removeOldMoments() {
        this.jobManager.addJobInBackground(new RemoveOldMomentsDBJob());
    }

    private void setFailedStatusToFailedFiles() {
        this.jobManager.addJobInBackground(new MarkFailedStatusToFailedFilesDBLoadJob());
    }

    private Stub getUICallback() {
        this.callback = new C07824();
        return this.callback;
    }

    public void onDestroy() {
        super.onDestroy();
        this.serviceConnectivityHandler.removeCallbacks(this.networkServiceRunnable);
        EventBus.getDefault().unregister(this);
    }

    private void setupContenView() {
        setContentView(2130903082);
        this.contact = (ImageButton) findViewById(2131361972);
        this.group = (ImageButton) findViewById(2131361973);
        this.rithMenu = (ImageButton) findViewById(2131361975);
        this.favorit = (ImageButton) findViewById(2131361977);
        this.searchButton = (ImageButton) findViewById(2131362002);
        this.composeButton = (ImageButton) findViewById(2131362006);
        this.menuButton = (ImageButton) findViewById(2131362009);
        this.favoriteButton = (ImageButton) findViewById(2131362004);
        this.addNewPost = (ImageButton) findViewById(2131361980);
        this.searchInPost = (ImageButton) findViewById(2131361979);
        this.composeButtonGroup = (ImageButton) findViewById(2131362003);
        this.addNewButton = (ImageButton) findViewById(2131362005);
        this.keypadButton = (ImageButton) findViewById(2131362000);
        this.myPostButton = (ImageButton) findViewById(2131362007);
        this.momentComposeButton = (ImageButton) findViewById(2131362001);
        this.keyboardTextview = (TextView) findViewById(2131362008);
        this.goaheadButton = (ImageButton) findViewById(2131361998);
        this.searchEditText = (EditText) findViewById(2131361960);
        this.searchEditText.addTextChangedListener(this.searchTextWatcher);
        this.searchContainerLinearLayout = (ViewGroup) findViewById(2131361955);
        this.notifLinear = (LinearLayout) findViewById(2131361981);
        this.notifBack = (ImageView) findViewById(2131361983);
        this.notifText = (TextView) findViewById(2131361984);
        this.notifLinear.setOnClickListener(new C07835());
        if (!NotifFlag.booleanValue()) {
            this.notifBack.setBackgroundResource(2130837918);
            this.notifText.setVisibility(4);
        }
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        this.addNewPost.setOnClickListener(new C07846());
        this.searchInPost.setOnClickListener(new C07857());
        this.actionBar = getSupportActionBar();
        this.actionBar.setNavigationMode(TAB_MESSAGE);
        this.actionBar.setDisplayShowTitleEnabled(false);
        this.actionBar.setDisplayShowHomeEnabled(false);
        this.mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        this.mViewPager = (ViewPager) findViewById(2131361997);
        this.mViewPager.setAdapter(this.mSectionsPagerAdapter);
        this.mViewPager.setOnPageChangeListener(new C07868());
        this.mViewPager.setOffscreenPageLimit(TAB_MESSAGE);
        for (int i = TAB_GROUPS; i < this.mSectionsPagerAdapter.getCount(); i += TAB_MOMENTS) {
            this.actionBar.addTab(this.actionBar.newTab().setText(this.mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
        }
        this.actionBar.getTabAt(TAB_MOMENTS).setIcon(2130838083);
        this.actionBar.getTabAt(TAB_MESSAGE).setIcon(2130838081);
        this.actionBar.getTabAt(TAB_GROUPS).setIcon(2130838086);
        this.actionBar.hide();
        this.actionBar.getTabAt(this.goToFragment).select();
        this.rithMenu.setOnClickListener(new C07879());
        this.keypadButton.setOnClickListener(new OnClickListener() {
            public final void onClick(View v) {
                MainWindow.this.actionBar.getTabAt(MainWindow.TAB_GROUPS).select();
            }
        });
        this.composeButtonGroup.setOnClickListener(new OnClickListener() {
            public final void onClick(View v) {
                MainWindow.this.actionBar.getTabAt(MainWindow.TAB_MOMENTS).select();
            }
        });
        this.contact.setOnClickListener(new OnClickListener() {
            public final void onClick(View v) {
                MainWindow.this.startActivity(new Intent(MainWindow.this, ComposeIndividualChatActivity.class));
            }
        });
        this.group.setOnClickListener(new OnClickListener() {
            public final void onClick(View v) {
                MainWindow.this.startActivity(new Intent(MainWindow.this, CreateGroupActivity.class));
            }
        });
        this.addNewButton.setOnClickListener(new OnClickListener() {
            public final void onClick(View v) {
                MainWindow.this.actionBar.getTabAt(MainWindow.TAB_MESSAGE).select();
            }
        });
        this.searchButton.setOnClickListener(new OnClickListener() {
            public final void onClick(View v) {
                MainWindow.this.actionBar.getTabAt(MainWindow.TAB_MESSAGE).select();
            }
        });
        this.myPostButton.setOnClickListener(new OnClickListener() {
            public final void onClick(View v) {
                Intent i = new Intent(MainWindow.this, UserProfile.class);
                i.putExtra("USER_ID", MainWindow.this.CURRENT_USER_ID);
                MainWindow.this.startActivity(i);
            }
        });
        this.menuButton.setOnClickListener(new OnClickListener() {
            public final void onClick(View v) {
                MainWindow.this.menuButton.setVisibility(8);
                MainWindow.this.myPostButton.setVisibility(MainWindow.TAB_GROUPS);
                MainWindow.this.searchContainerLinearLayout.setVisibility(8);
            }
        });
        this.goaheadButton.setOnClickListener(new OnClickListener() {
            public final void onClick(View v) {
                MainWindow.this.searchContainerLinearLayout.setVisibility(8);
            }
        });
        if (closedd.booleanValue()) {
            this.actionBar.getTabAt(TAB_MESSAGE).select();
            closedd = Boolean.valueOf(false);
        }
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case TAB_GROUPS /*0*/:
                if (resultCode == -1) {
                    Cursor c = getContentResolver().query(data.getData(), null, null, null, null);
                    if (c.moveToFirst()) {
                        Toast.makeText(getApplicationContext(), c.getString(c.getColumnIndexOrThrow("display_name")), TAB_GROUPS).show();
                    }
                }
            default:
        }
    }

    protected void onNewIntent(Intent i) {
        setIntent(i);
    }

    protected void onPause() {
        super.onPause();
        cash1 = shake;
        shake = TAB_GROUPS;
        SHAMPushManager sHAMPushManager = this.shamPushManager;
        try {
            sHAMPushManager.context.unregisterReceiver(sHAMPushManager.mReceiver);
        } catch (Exception e) {
        }
        try {
            sHAMPushManager.context.unregisterReceiver(sHAMPushManager.mBroadcastReceiver);
        } catch (Exception e2) {
        }
        this.chatController.unbindXMPPService();
    }

    protected void onResume() {
        shake = cash1;
        super.onResume();
        initNotification(true);
        if (!this.mConfig.theme.equals(this.mTheme)) {
            Intent restartIntent = new Intent(this, MainWindow.class);
            restartIntent.setFlags(67108864);
            startActivity(restartIntent);
            finish();
        }
        if (getIntent() != null) {
            this.goToFragment = getIntent().getIntExtra("goto", TAB_GROUPS);
            if (!(this.actionBar == null || this.goToFragment == 0)) {
                this.actionBar.getTabAt(this.goToFragment).select();
            }
            this.goToFragment = TAB_GROUPS;
            if (getIntent().hasExtra("goto")) {
                getIntent().removeExtra("goto");
            }
        }
        try {
            if (ProgressBarDialogLogin.getInstance().isAdded() || ProgressBarDialogLogin.getInstance().isVisible()) {
                ProgressBarDialogLogin.getInstance().dismiss();
            }
            this.shamPushManager = new SHAMPushManager(this, getSupportFragmentManager());
            this.shamPushManager.registerReceivers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.chatController.bindXMPPService();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged");
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    public static Intent createIntent(Context context) {
        Intent i = new Intent(context, MainWindow.class);
        i.addFlags(67108864);
        return i;
    }

    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        this.mViewPager.setCurrentItem(tab.getPosition());
        tab.getPosition();
        this.f14s = (SlideHolder) findViewById(2131361985);
        this.f14s.setDirection$13462e();
        shake = TAB_GROUPS;
        switch (tab.getPosition()) {
            case TAB_GROUPS /*0*/:
                this.composeButton.setVisibility(8);
                this.favoriteButton.setVisibility(8);
                this.menuButton.setVisibility(8);
                this.searchButton.setVisibility(8);
                this.addNewPost.setVisibility(TAB_GROUPS);
                this.searchInPost.setVisibility(TAB_GROUPS);
                this.composeButtonGroup.setVisibility(TAB_GROUPS);
                this.addNewButton.setVisibility(TAB_GROUPS);
                this.searchContainerLinearLayout.setVisibility(8);
                this.keypadButton.setVisibility(8);
                this.myPostButton.setVisibility(TAB_GROUPS);
                this.keyboardTextview.setVisibility(8);
                this.momentComposeButton.setVisibility(TAB_GROUPS);
                this.contact.setVisibility(8);
                this.rithMenu.setVisibility(8);
                this.favorit.setVisibility(8);
                this.group.setVisibility(8);
                shake = TAB_MOMENTS;
                this.f14s.setEnabled(false);
            case TAB_MOMENTS /*1*/:
                this.composeButton.setVisibility(8);
                this.favoriteButton.setVisibility(TAB_GROUPS);
                this.addNewPost.setVisibility(TAB_GROUPS);
                this.searchInPost.setVisibility(8);
                this.menuButton.setVisibility(8);
                this.searchButton.setVisibility(8);
                this.composeButtonGroup.setVisibility(8);
                this.addNewButton.setVisibility(TAB_GROUPS);
                this.searchContainerLinearLayout.setVisibility(8);
                this.keypadButton.setVisibility(TAB_GROUPS);
                this.myPostButton.setVisibility(TAB_GROUPS);
                this.keyboardTextview.setVisibility(8);
                this.momentComposeButton.setVisibility(8);
                this.contact.setVisibility(8);
                this.rithMenu.setVisibility(8);
                this.favorit.setVisibility(8);
                this.group.setVisibility(8);
                this.f14s.setEnabled(false);
            case TAB_MESSAGE /*2*/:
                this.composeButton.setVisibility(TAB_GROUPS);
                this.favoriteButton.setVisibility(8);
                this.menuButton.setVisibility(8);
                this.searchButton.setVisibility(8);
                this.addNewPost.setVisibility(8);
                this.searchInPost.setVisibility(8);
                this.composeButtonGroup.setVisibility(TAB_GROUPS);
                this.addNewButton.setVisibility(8);
                this.searchContainerLinearLayout.setVisibility(8);
                this.keypadButton.setVisibility(TAB_GROUPS);
                this.myPostButton.setVisibility(TAB_GROUPS);
                this.keyboardTextview.setVisibility(8);
                this.momentComposeButton.setVisibility(8);
                this.contact.setVisibility(TAB_GROUPS);
                this.rithMenu.setVisibility(8);
                this.favorit.setVisibility(8);
                this.group.setVisibility(TAB_GROUPS);
                this.f14s.setEnabled(false);
            default:
        }
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case 2131362001:
                startActivityForResult(new Intent(this, MomentComposerActivity.class), 100);
            case 2131362003:
                startActivity(new Intent(this, ComposeGroupChatActivity.class));
            case 2131362004:
                startActivity(new Intent(this, FavoriteMessagesActivity.class));
            case 2131362006:
                startActivity(new Intent(this, ComposeIndividualChatActivity.class));
            case 2131362007:
                Intent i4 = new Intent(this, FriendMomentActivity.class);
                i4.putExtra("userId", this.mConfig.userId);
                startActivity(i4);
            case 2131362009:
                Intent i6 = new Intent(this, UserProfile.class);
                i6.putExtra("USER_ID", this.CURRENT_USER_ID);
                i6.setFlags(ClientDefaults.MAX_MSG_SIZE);
                startActivity(i6);
            default:
        }
    }

    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            AlertDialog altd = new Builder(this).create();
            altd.setTitle(getString(2131493316));
            altd.setMessage(getString(2131493318));
            altd.setButton(getString(2131493349), new DialogInterface.OnClickListener() {
                public final void onClick(DialogInterface dialog, int which) {
                    MainWindow.this.moveTaskToBack(true);
                }
            });
            altd.setButton2(getString(2131493334), new DialogInterface.OnClickListener() {
                public final void onClick(DialogInterface dialog, int which) {
                }
            });
            altd.show();
        } else {
            Toast.makeText(getBaseContext(), getString(2131493317), TAB_GROUPS).show();
        }
        back_pressed = System.currentTimeMillis();
    }

    private void updateSentStatus(String packetId) {
        Cursor cursor = null;
        try {
            ContentValues cv = new ContentValues();
            cv.put("message_status", Integer.valueOf(MessageStatusType.SENT.ordinal()));
            cv.put("message_last_updated_datetime", com.shamchat.utils.Utils.formatDate(new Date().getTime(), "yyyy/MM/dd HH:mm:ss"));
            ContentResolver contentResolver = this.mContentResolver;
            Uri uri = ChatProviderNew.CONTENT_URI_CHAT;
            String[] strArr = new String[TAB_MESSAGE];
            strArr[TAB_GROUPS] = "_id";
            strArr[TAB_MOMENTS] = ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID;
            String[] strArr2 = new String[TAB_MOMENTS];
            strArr2[TAB_GROUPS] = packetId;
            cursor = contentResolver.query(uri, strArr, "packet_id=?", strArr2, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String columnID = Integer.toString(cursor.getInt(cursor.getColumnIndex("_id")));
                String threadId = cursor.getString(cursor.getColumnIndex(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID));
                Uri rowuri = Uri.parse("content://org.zamin.androidclient.provider.Messages/chat_message/" + columnID);
                contentResolver = this.mContentResolver;
                String str = "_id = ? AND message_status != " + MessageStatusType.SEEN.ordinal() + " AND message_type = " + MyMessageType.OUTGOING_MSG.ordinal();
                strArr = new String[TAB_MOMENTS];
                strArr[TAB_GROUPS] = columnID;
                contentResolver.update(rowuri, cv, str, strArr);
                this.jobManager.addJobInBackground(new MessageStateChangedJob(threadId, packetId, MessageStatusType.SENT.type));
            }
            cursor.close();
        } catch (Throwable th) {
            cursor.close();
        }
    }

    public void SearchDialog() {
        this.dialog = new AnonymousClass24(this).build();
        this.dialog.show();
    }

    public void initNotification(boolean resume) {
        if (!(NotifFlag.booleanValue() || resume || this.notifBack == null || this.notifText == null)) {
            this.notifBack.setBackgroundResource(2130837918);
            this.notifText.setVisibility(4);
        }
        String URL = "http://social.rabtcdn.com/pin/api/notif/count/?token=" + this.CURRENT_USER_TOKEN + "&timeStamp=" + System.currentTimeMillis();
        Log.e("##########################NOTIF COUNT URL", URL);
        new OkHttpClient().newCall(new Request.Builder().url(URL).build()).enqueue(new Callback() {

            /* renamed from: com.shamchat.activity.MainWindow.25.1 */
            class C07731 implements Runnable {
                C07731() {
                }

                public final void run() {
                }
            }

            /* renamed from: com.shamchat.activity.MainWindow.25.2 */
            class C07742 implements Runnable {
                final /* synthetic */ String val$stringResponse;

                C07742(String str) {
                    this.val$stringResponse = str;
                }

                public final void run() {
                    try {
                        String count = this.val$stringResponse;
                        Log.e("##########################NOTIF COUNT RESPONSE", this.val$stringResponse);
                        if (count.equals("0")) {
                            MainWindow.NotifFlag = Boolean.valueOf(false);
                            MainWindow.this.notifBack.setBackgroundResource(2130837918);
                            MainWindow.this.notifText.setVisibility(4);
                            return;
                        }
                        if (Integer.parseInt(this.val$stringResponse) > 10) {
                            count = "+10";
                        }
                        MainWindow.NotifFlag = Boolean.valueOf(true);
                        MainWindow.this.notifBack.setBackgroundResource(2130837919);
                        MainWindow.this.notifText.setVisibility(MainWindow.TAB_GROUPS);
                        MainWindow.this.notifText.setTypeface(Utils.GetNaskhRegular(MainWindow.this));
                        MainWindow.this.notifText.setText(count);
                    } catch (Exception e) {
                    }
                }
            }

            /* renamed from: com.shamchat.activity.MainWindow.25.3 */
            class C07753 implements Runnable {
                C07753() {
                }

                public final void run() {
                }
            }

            public final void onFailure(Request request, IOException e) {
                MainWindow.this.runOnUiThread(new C07731());
                e.printStackTrace();
            }

            public final void onResponse(Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        MainWindow.this.runOnUiThread(new C07742(response.body().string()));
                        return;
                    }
                    response.body().close();
                    throw new IOException("Unexpected code " + response);
                } catch (Exception e) {
                    MainWindow.this.runOnUiThread(new C07753());
                    e.printStackTrace();
                }
            }
        });
    }
}

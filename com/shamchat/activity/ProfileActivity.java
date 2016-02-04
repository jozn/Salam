package com.shamchat.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.path.android.jobqueue.JobManager;
import com.shamchat.adapters.ProfileBottomAdapter;
import com.shamchat.adapters.ProfileBottomAdapter.BottomListItem;
import com.shamchat.adapters.ProfileDetailsAdapter;
import com.shamchat.adapters.ProfileDetailsAdapter.DetailsListItem;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.RosterProvider;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.androidclient.util.FontUtil;
import com.shamchat.androidclient.util.StatusMode;
import com.shamchat.events.FriendDBLoadCompletedEvent;
import com.shamchat.events.PresenceChangedEvent;
import com.shamchat.jobs.FriendDBLoadJob;
import com.shamchat.jobs.VCardLoadRequestJob;
import com.shamchat.models.User;
import com.shamchat.models.User.BooleanStatus;
import com.shamchat.utils.Utils;
import com.squareup.picasso.Picasso;
import de.greenrobot.event.EventBus;
import java.util.Locale;
import org.jivesoftware.smack.packet.Presence.Type;

public class ProfileActivity extends AppCompatActivity implements OnClickListener {
    public static final String EXTRA_USER = "extra_user";
    public static final String EXTRA_USER_ID = "extra_user_id";
    protected static final String TAG = "ProfileActivity";
    private ProfileBottomAdapter bottomAdapter;
    private ProfileDetailsAdapter detailsAdapter;
    private Button freeCallButton;
    private Button freeMessagesButton;
    private String friendJabberId;
    private String friendUserId;
    private ImageView imageProfile;
    private ImageView imageStatus;
    private JobManager jobManager;
    private LinearLayout listBottom;
    private LinearLayout listDetails;
    private Button statusButton;
    private TextView textName;
    private ProgressBar tvProgress;
    private TextView txtLastSeen;
    private User user;

    /* renamed from: com.shamchat.activity.ProfileActivity.1 */
    class C08351 implements Runnable {
        private boolean isControllersDisplayed;
        final /* synthetic */ User val$user;

        /* renamed from: com.shamchat.activity.ProfileActivity.1.1 */
        class C08311 implements OnClickListener {
            C08311() {
            }

            public final void onClick(View v) {
            }
        }

        /* renamed from: com.shamchat.activity.ProfileActivity.1.2 */
        class C08322 implements OnClickListener {
            C08322() {
            }

            public final void onClick(View v) {
            }
        }

        /* renamed from: com.shamchat.activity.ProfileActivity.1.3 */
        class C08333 implements OnClickListener {
            C08333() {
            }

            public final void onClick(View v) {
            }
        }

        /* renamed from: com.shamchat.activity.ProfileActivity.1.4 */
        class C08344 implements OnClickListener {
            C08344() {
            }

            public final void onClick(View v) {
            }
        }

        C08351(User user) {
            this.val$user = user;
        }

        public final void run() {
            System.out.println("PROFILE ACTIVITY update ui setting values");
            ProfileActivity.this.tvProgress.setVisibility(8);
            ProfileActivity.this.textName.setText(this.val$user.username);
            if (this.val$user.isInChat == BooleanStatus.TRUE) {
                int onlineStatus;
                System.out.println("PROFILE ACTIVITY user.getIsInChat() == BooleanStatus.TRUE");
                String profileImageUrl = this.val$user.profileImageUrl;
                if (profileImageUrl == null || !profileImageUrl.contains("http://")) {
                    System.out.println("PROFILE ACTIVITY profileImageUrl empty)");
                    ProfileActivity.this.imageProfile.setImageResource(2130837944);
                } else {
                    System.out.println("PROFILE ACTIVITY profileImageUrl.contains(http://)" + profileImageUrl);
                    Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(Uri.parse(profileImageUrl)).into(ProfileActivity.this.imageProfile, null);
                }
                if (this.val$user.onlineStatus == Type.unavailable.ordinal()) {
                    onlineStatus = StatusMode.offline.ordinal();
                } else {
                    onlineStatus = StatusMode.available.ordinal();
                }
                String statusMessage = this.val$user.myStatus;
                System.out.println("PROFILE ACTIVITY onlineStatus " + onlineStatus + " statusMessage " + statusMessage);
                ProfileActivity.this.imageStatus.setImageResource(StatusMode.values()[onlineStatus].drawableId);
                if (statusMessage == null) {
                    ProfileActivity.this.statusButton.setHint("What's up");
                } else if (statusMessage.equalsIgnoreCase("null")) {
                    ProfileActivity.this.statusButton.setHint("What's up");
                } else if (statusMessage.equalsIgnoreCase(BuildConfig.VERSION_NAME)) {
                    ProfileActivity.this.statusButton.setHint("What's up");
                } else {
                    ProfileActivity.this.statusButton.setText(statusMessage);
                }
                ProfileActivity.this.detailsAdapter.list.clear();
                ProfileActivity.this.bottomAdapter.list.clear();
                ProfileActivity.this.listDetails.removeAllViews();
                ProfileActivity.this.listBottom.removeAllViews();
                ProfileActivity.this.addDetail(ProfileActivity.this.getResources().getString(2131493372), this.val$user.chatId, false, new C08311());
                ProfileActivity.this.addDetail(ProfileActivity.this.getResources().getString(2131493286), this.val$user.cityOrRegion, false, new C08322());
                ProfileActivity.this.addDetail(ProfileActivity.this.getResources().getString(2131493197), this.val$user.mobileNo, false, new C08333());
                ProfileActivity.this.addDetail(ProfileActivity.this.getResources().getString(2131493153), this.val$user.gender, new C08344());
                if (!this.isControllersDisplayed) {
                    ProfileActivity.this.displayControllers();
                    this.isControllersDisplayed = true;
                }
            }
        }
    }

    /* renamed from: com.shamchat.activity.ProfileActivity.2 */
    class C08362 implements OnClickListener {
        C08362() {
        }

        public final void onClick(View v) {
            ContentValues values = new ContentValues();
            values.put("user_status", Integer.valueOf(1));
            ProfileActivity.this.getContentResolver().update(RosterProvider.CONTENT_URI, values, "jid=?", new String[]{ProfileActivity.this.friendJabberId});
            ProfileActivity.this.bottomAdapter.notifyDataSetChanged();
            Toast.makeText(ProfileActivity.this.getApplicationContext(), 2131493149, 0).show();
            ProfileActivity.this.finish();
        }
    }

    /* renamed from: com.shamchat.activity.ProfileActivity.3 */
    class C08373 implements OnClickListener {
        C08373() {
        }

        public final void onClick(View v) {
            ContentValues values = new ContentValues();
            values.put("user_status", Integer.valueOf(0));
            ProfileActivity.this.getContentResolver().update(RosterProvider.CONTENT_URI, values, "jid=?", new String[]{ProfileActivity.this.friendJabberId});
            ProfileActivity.this.bottomAdapter.notifyDataSetChanged();
            Toast.makeText(ProfileActivity.this.getApplicationContext(), 2131493151, 0).show();
            ProfileActivity.this.finish();
        }
    }

    /* renamed from: com.shamchat.activity.ProfileActivity.4 */
    class C08394 implements OnClickListener {

        /* renamed from: com.shamchat.activity.ProfileActivity.4.1 */
        class C08381 implements Runnable {
            C08381() {
            }

            public final void run() {
                ProfileActivity.this.getContentResolver().delete(UserProvider.CONTENT_URI_USER, "userId=?", new String[]{ProfileActivity.this.user.userId});
            }
        }

        C08394() {
        }

        public final void onClick(View v) {
            ContentValues values = new ContentValues();
            values.put("user_status", Integer.valueOf(2));
            ProfileActivity.this.getContentResolver().update(RosterProvider.CONTENT_URI, values, "jid=?", new String[]{ProfileActivity.this.user.userId});
            new Handler().postDelayed(new C08381(), 2000);
            ProfileActivity.this.bottomAdapter.notifyDataSetChanged();
            Toast.makeText(ProfileActivity.this.getApplicationContext(), 2131493150, 0).show();
            ProfileActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903097);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(31);
        actionBar.setCustomView(2130903045);
        this.imageProfile = (ImageView) findViewById(2131361914);
        this.statusButton = (Button) findViewById(2131362063);
        this.listDetails = (LinearLayout) findViewById(2131362045);
        this.listBottom = (LinearLayout) findViewById(2131362046);
        this.imageStatus = (ImageView) findViewById(2131361845);
        this.textName = (TextView) findViewById(2131361839);
        this.txtLastSeen = (TextView) findViewById(2131361844);
        this.txtLastSeen.setVisibility(8);
        this.tvProgress = (ProgressBar) findViewById(2131362059);
        this.tvProgress.setVisibility(0);
        this.freeCallButton = (Button) findViewById(2131362061);
        this.freeMessagesButton = (Button) findViewById(2131362062);
        this.freeMessagesButton.setOnClickListener(this);
        this.freeCallButton.setOnClickListener(this);
        FontUtil.applyFont(this.statusButton);
        FontUtil.applyFont(this.freeCallButton);
        FontUtil.applyFont(this.freeMessagesButton);
        Toast.makeText(getApplicationContext(), Locale.getDefault().toString(), 1).show();
        this.detailsAdapter = new ProfileDetailsAdapter(this);
        this.bottomAdapter = new ProfileBottomAdapter(this);
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        try {
            EventBus.getDefault().register(this, true, 0);
        } catch (Throwable th) {
        }
        Bundle extras = getIntent().getExtras();
        System.out.println("PROFILE ACTIVITY LOADING");
        if (extras == null) {
            System.out.println("PROFILE ACTIVITY bundle null");
        } else if (extras.getString(EXTRA_USER_ID) != null) {
            System.out.println("PROFILE ACTIVITY bundle not null");
            this.friendUserId = extras.getString(EXTRA_USER_ID);
            refreshDetails(this.friendUserId);
        }
    }

    private void refreshDetails(String userId) {
        this.jobManager.addJobInBackground(new FriendDBLoadJob(userId));
        System.out.println("PROFILE ACTIVITY job executed");
    }

    public void onEventBackgroundThread(FriendDBLoadCompletedEvent event) {
        this.user = event.user;
        if (this.user == null) {
            return;
        }
        if (this.user.isVCardDownloaded) {
            this.friendJabberId = Utils.createXmppUserIdByUserId(this.friendUserId);
            System.out.println("PROFILE ACTIVITY user id and jabber id " + this.friendUserId + " " + this.friendJabberId);
            if (this.user == null || this.user.userId == null) {
                System.out.println("User null or a different user");
                return;
            } else if (this.user.userId.equals(this.friendUserId)) {
                System.out.println("PROFILE ACTIVITY update ui");
                updateUi(this.user);
                return;
            } else {
                System.out.println("PROFILE ACTIVITY no user id from the returned object");
                return;
            }
        }
        System.out.println("PROFILE ACTIVITY vCard not downloaded yet" + Utils.createXmppUserIdByUserId(this.user.userId));
        this.jobManager.addJobInBackground(new VCardLoadRequestJob(Utils.createXmppUserIdByUserId(this.user.userId)));
    }

    private void updateUi(User user) {
        if (user != null) {
            System.out.println("PROFILE ACTIVITY update ui user not null");
            runOnUiThread(new C08351(user));
            return;
        }
        System.out.println("PROFILE ACTIVITY update ui user null");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                break;
        }
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(2131623937, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onClickFreeCalls(View view) {
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onClick(View v) {
        if (this.user != null) {
            switch (v.getId()) {
                case 2131362061:
                    Intent intent;
                    if (this.user.isInChat == BooleanStatus.TRUE) {
                        intent = new Intent(getApplicationContext(), FriendMomentActivity.class);
                        intent.putExtra("userId", this.user.userId);
                        startActivity(intent);
                        return;
                    }
                    String uri = "tel:" + this.user.mobileNo.trim();
                    intent = new Intent("android.intent.action.CALL");
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                case 2131362062:
                    if (this.user.isInChat == BooleanStatus.TRUE) {
                        Intent chatIntent = new Intent(this, ChatActivity.class);
                        chatIntent.putExtra(ChatActivity.INTENT_EXTRA_USER_ID, this.user.userId);
                        chatIntent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, SHAMChatApplication.getConfig().userId + "-" + this.user.userId);
                        startActivity(chatIntent);
                    } else if (this.user.mobileNo != null) {
                        System.out.println(" user.getMobileNo().trim() " + this.user.mobileNo.trim());
                        smsIntent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + this.user.mobileNo.trim()));
                        smsIntent.putExtra("sms_body", "Add me on SHAM3 Chat! http://sham3.com/");
                        startActivity(smsIntent);
                    } else {
                        System.out.println(" user.getUsername().trim() " + this.user.username.trim());
                        smsIntent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + this.user.username.trim()));
                        smsIntent.putExtra("sms_body", "Add me on SHAM3 Chat! http://sham3.com/");
                        startActivity(smsIntent);
                    }
                default:
            }
        }
    }

    private void displayControllers() {
        if (this.user.isBlocked) {
            addBottomItem(getResources().getString(2131493430), 2130837874, new C08373(), true);
        } else {
            addBottomItem(getResources().getString(2131492987), 2130837874, new C08362(), true);
        }
        addBottomItem(getResources().getString(2131493089), 2130837875, new C08394());
    }

    private void addBottomItem(String text, int iconRes, OnClickListener listener) {
        addBottomItem(text, iconRes, listener, true);
    }

    private void addBottomItem(String text, int iconRes, OnClickListener listener, boolean refresh) {
        this.bottomAdapter.list.add(new BottomListItem(text, iconRes));
        int position = this.bottomAdapter.list.size() - 1;
        this.listBottom.addView(LayoutInflater.from(this).inflate(2130903192, null));
        View rowView = this.bottomAdapter.getView(position, null, null);
        rowView.setBackgroundResource(2130837574);
        this.listBottom.addView(rowView);
        rowView.setOnClickListener(listener);
        if (refresh) {
            this.bottomAdapter.notifyDataSetChanged();
        }
    }

    private void addDetail(String left, String right, OnClickListener listener) {
        addDetail(left, right, true, listener);
    }

    private void addDetail(String left, String right, boolean refresh, OnClickListener listener) {
        if (right != null && right.equalsIgnoreCase("null")) {
            right = BuildConfig.VERSION_NAME;
        }
        this.detailsAdapter.list.add(new DetailsListItem(left, right));
        View view = this.detailsAdapter.getView(this.detailsAdapter.list.size() - 1, null, null);
        View clickView = view.findViewById(2131362096);
        this.listDetails.addView(view);
        clickView.setOnClickListener(listener);
        clickView.setBackgroundResource(2130837575);
        if (refresh) {
            this.detailsAdapter.notifyDataSetChanged();
        }
        System.out.println("PROFILE ACTIVITY addDetail left " + left + " right " + right);
    }

    public void onEventBackgroundThread(PresenceChangedEvent event) {
        String userId = event.userId;
        if (this.user != null && this.user.userId != null && userId != null && userId.equals(this.user.userId)) {
            this.jobManager.addJobInBackground(new FriendDBLoadJob(userId));
        }
    }
}

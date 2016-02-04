package com.shamchat.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.C0170R;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.shamchat.adapters.NotifcationItemsAdapter;
import com.shamchat.adapters.NotifcationItemsAdapter.NotificationListItem;
import com.shamchat.androidclient.ChatController;
import com.shamchat.androidclient.IXMPPChatCallback.Stub;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.androidclient.data.UserProvider.UserNotificationUpdateType;
import com.shamchat.androidclient.data.ZaminConfiguration;
import com.shamchat.androidclient.service.SmackableImp;
import com.shamchat.androidclient.util.ConnectionState;
import com.shamchat.androidclient.util.PreferenceConstants;
import com.shamchat.androidclient.util.PreferenceConstants.AllowDeniedStatus;
import com.shamchat.androidclient.util.PreferenceConstants.EnableDisableStatus;
import com.shamchat.models.UserNotification;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;
import org.jivesoftware.smack.SmackException.NotConnectedException;

public class NotificationsActivity extends AppCompatActivity {
    private static CharSequence[] messageToneArray;
    private static MessageTone[] messageTones;
    private AlertDialog alert;
    private Stub callback;
    private Context context;
    private Handler mainHandler;
    private LinearLayout notificationList;
    private NotifcationItemsAdapter notificationsListAdapter;
    private String selectedUri;
    private String userId;
    private UserNotification userNotification;

    /* renamed from: com.shamchat.activity.NotificationsActivity.1 */
    class C08171 implements OnCheckedChangeListener {
        C08171() {
        }

        public final void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            AllowDeniedStatus allowDenied = isChecked ? AllowDeniedStatus.ALLOW : AllowDeniedStatus.DENIED;
            UserProvider userProvider = new UserProvider();
            UserProvider.updateNotification$3b3d5b85(NotificationsActivity.this.userId, UserNotificationUpdateType.MESSAGE_ALERT_STATUS$16247172, allowDenied);
        }
    }

    /* renamed from: com.shamchat.activity.NotificationsActivity.2 */
    class C08202 implements OnCheckedChangeListener {

        /* renamed from: com.shamchat.activity.NotificationsActivity.2.1 */
        class C08181 implements OnClickListener {
            C08181() {
            }

            public final void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }

        /* renamed from: com.shamchat.activity.NotificationsActivity.2.2 */
        class C08192 implements OnClickListener {
            C08192() {
            }

            public final void onClick(DialogInterface dialog, int id) {
                Editor editor = PreferenceManager.getDefaultSharedPreferences(NotificationsActivity.this.getApplicationContext()).edit();
                editor.putBoolean("foregroundService", false);
                editor.apply();
                ChatController.getInstance(NotificationsActivity.this.getApplicationContext()).unRegisterChatCallback(NotificationsActivity.this.getUICallback());
                if (SmackableImp.isXmppConnected() && PreferenceConstants.CONNECTED_TO_NETWORK) {
                    try {
                        SmackableImp.getXmppConnection().disconnect();
                    } catch (NotConnectedException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.HOME");
                intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
                NotificationsActivity.this.startActivity(intent);
                Process.killProcess(Process.myPid());
            }
        }

        C08202() {
        }

        public final void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            EnableDisableStatus enableDisable = isChecked ? EnableDisableStatus.ENABLE : EnableDisableStatus.DISABLE;
            UserProvider userProvider = new UserProvider();
            UserProvider.updateNotification$3b3d5b85(NotificationsActivity.this.userId, UserNotificationUpdateType.SOUND_ALERT_STATUS$16247172, enableDisable);
            if (isChecked) {
                Editor editor = PreferenceManager.getDefaultSharedPreferences(NotificationsActivity.this.getApplicationContext()).edit();
                editor.putBoolean("foregroundService", true);
                editor.apply();
                ChatController chatController = ChatController.getInstance(NotificationsActivity.this.getApplicationContext());
                ZaminConfiguration configuration = SHAMChatApplication.getConfig();
                chatController.registerXMPPService(NotificationsActivity.this.getUICallback());
                chatController.login(configuration.userName, configuration.password);
                return;
            }
            Builder alertDialogBuilder = new Builder(NotificationsActivity.this.context);
            alertDialogBuilder.setTitle("Are you sure?");
            alertDialogBuilder.setMessage("Disabling this notification will stop the app from running in foregroud. NOTE: Please restart the app for changes to take effect").setCancelable(false).setPositiveButton("Disable", new C08192()).setNegativeButton("Cancel", new C08181());
            alertDialogBuilder.create().show();
        }
    }

    /* renamed from: com.shamchat.activity.NotificationsActivity.3 */
    class C08213 implements View.OnClickListener {
        C08213() {
        }

        public final void onClick(View arg0) {
            NotificationsActivity.this.userNotification = NotificationsActivity.this.LoadUserNotificationSettings();
            if (NotificationsActivity.messageToneArray == null || NotificationsActivity.messageToneArray.length == 0) {
                NotificationsActivity.this.getAllMessageTones(NotificationsActivity.this.userNotification.notificationSound);
            } else {
                NotificationsActivity.this.selectNotificationSound(NotificationsActivity.this.userNotification.notificationSound);
            }
        }
    }

    /* renamed from: com.shamchat.activity.NotificationsActivity.4 */
    class C08224 extends AsyncTask<Void, Void, MessageTone[]> {
        final /* synthetic */ ProgressDialog val$progressDialog;
        final /* synthetic */ String val$uri;

        C08224(ProgressDialog progressDialog, String str) {
            this.val$progressDialog = progressDialog;
            this.val$uri = str;
        }

        protected final /* bridge */ /* synthetic */ Object doInBackground(Object[] objArr) {
            return doInBackground$26b420a7();
        }

        protected final /* bridge */ /* synthetic */ void onPostExecute(Object obj) {
            MessageTone[] messageToneArr = (MessageTone[]) obj;
            if (messageToneArr != null) {
                try {
                    NotificationsActivity.messageTones = messageToneArr;
                    NotificationsActivity.this.selectNotificationSound(this.val$uri);
                    super.onPostExecute(messageToneArr);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(NotificationsActivity.this.getApplicationContext(), "Cannot access notifcation sound library", 0).show();
                    return;
                }
            }
            Toast.makeText(NotificationsActivity.this.getApplicationContext(), "Cannot access notifcation sound library", 0).show();
        }

        protected final void onPreExecute() {
            this.val$progressDialog.setCancelable(true);
            this.val$progressDialog.setMessage("Loading...");
            this.val$progressDialog.setProgressStyle(0);
            this.val$progressDialog.setProgress(0);
            this.val$progressDialog.show();
            super.onPreExecute();
        }

        private MessageTone[] doInBackground$26b420a7() {
            try {
                RingtoneManager ringtoneMgr = new RingtoneManager(NotificationsActivity.this);
                ringtoneMgr.setType(2);
                Cursor alarmsCursor = ringtoneMgr.getCursor();
                int alarmsCount = alarmsCursor.getCount();
                if (alarmsCount == 0 && !alarmsCursor.moveToFirst()) {
                    return null;
                }
                MessageTone[] toneArray = new MessageTone[alarmsCount];
                NotificationsActivity.messageToneArray = new CharSequence[alarmsCount];
                while (!alarmsCursor.isAfterLast() && alarmsCursor.moveToNext()) {
                    int currentPosition = alarmsCursor.getPosition();
                    Uri uri = ringtoneMgr.getRingtoneUri(currentPosition);
                    String toneName = alarmsCursor.getString(1);
                    NotificationsActivity.messageToneArray[currentPosition] = toneName;
                    toneArray[currentPosition] = new MessageTone(toneName, uri, ringtoneMgr.getRingtone(currentPosition));
                }
                return toneArray;
            } catch (Exception e) {
                return null;
            }
        }
    }

    /* renamed from: com.shamchat.activity.NotificationsActivity.5 */
    class C08235 implements OnClickListener {
        C08235() {
        }

        public final void onClick(DialogInterface dialog, int item) {
            if (NotificationsActivity.messageTones[item].ringtone.isPlaying()) {
                NotificationsActivity.messageTones[item].ringtone.stop();
            }
            NotificationsActivity.messageTones[item].ringtone.play();
            NotificationsActivity.this.selectedUri = NotificationsActivity.messageTones[item].uri.getPath();
        }
    }

    /* renamed from: com.shamchat.activity.NotificationsActivity.6 */
    class C08246 implements OnClickListener {
        C08246() {
        }

        public final void onClick(DialogInterface dialog, int which) {
            UserProvider userProvider = new UserProvider();
            UserProvider.updateNotification$3b3d5b85(NotificationsActivity.this.userId, UserNotificationUpdateType.NOTIFICATION_SOUND$16247172, NotificationsActivity.this.selectedUri);
        }
    }

    /* renamed from: com.shamchat.activity.NotificationsActivity.7 */
    class C08257 implements OnClickListener {
        C08257() {
        }

        public final void onClick(DialogInterface dialog, int which) {
        }
    }

    /* renamed from: com.shamchat.activity.NotificationsActivity.8 */
    class C08278 extends Stub {

        /* renamed from: com.shamchat.activity.NotificationsActivity.8.1 */
        class C08261 implements Runnable {
            final /* synthetic */ int val$connectionstate;

            C08261(int i) {
                this.val$connectionstate = i;
            }

            @TargetApi(11)
            public final void run() {
                ConnectionState.values();
            }
        }

        C08278() {
        }

        public final void connectionStateChanged(int connectionstate) throws RemoteException {
            NotificationsActivity.this.mainHandler.post(new C08261(connectionstate));
        }

        public final void roomCreated(boolean created) throws RemoteException {
        }

        public final void didJoinRoom(boolean joined) throws RemoteException {
        }

        public final void onFriendComposing(String jabberId, boolean isTypng) throws RemoteException {
        }
    }

    private class MessageTone {
        public Ringtone ringtone;
        public String toneName;
        public Uri uri;

        public MessageTone(String toneName, Uri uri, Ringtone ringtone) {
            this.toneName = toneName;
            this.uri = uri;
            this.ringtone = ringtone;
        }
    }

    public NotificationsActivity() {
        this.mainHandler = new Handler();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903095);
        this.context = this;
        this.userId = SHAMChatApplication.getConfig().userId;
        initializeActionBar();
        this.notificationList = (LinearLayout) findViewById(2131362057);
        this.notificationsListAdapter = new NotifcationItemsAdapter(this);
        addSettingsListItems();
    }

    private void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(31);
        actionBar.setCustomView(2130903043);
        ((TextView) findViewById(2131361839)).setText(getResources().getString(2131493239));
    }

    private void addSettingsListItems() {
        boolean z;
        this.userNotification = LoadUserNotificationSettings();
        String string = getResources().getString(2131493221);
        C08171 c08171 = new C08171();
        if (this.userNotification.messageAlertStatus == AllowDeniedStatus.ALLOW) {
            z = true;
        } else {
            z = false;
        }
        addListItem(string, c08171, false, false, z);
        string = getResources().getString(2131493379);
        C08202 c08202 = new C08202();
        if (this.userNotification.soundAlertStatus == EnableDisableStatus.ENABLE) {
            z = true;
        } else {
            z = false;
        }
        addListItem(string, c08202, false, false, z);
        addListItem(getResources().getString(2131493240), new C08213(), false, true, false);
    }

    private void addListItem(String text, Object listener, boolean refresh, boolean includeSelector, boolean isChecked) {
        this.notificationsListAdapter.list.add(new NotificationListItem(text));
        int position = this.notificationsListAdapter.list.size() - 1;
        this.notificationList.addView(LayoutInflater.from(this).inflate(2130903192, null));
        View rowView = this.notificationsListAdapter.getView(position, null, null);
        CheckBox checkBox = (CheckBox) rowView.findViewById(C0170R.id.checkbox);
        checkBox.setChecked(isChecked);
        if (includeSelector) {
            rowView.setBackgroundResource(2130837576);
            checkBox.setVisibility(8);
        }
        this.notificationList.addView(rowView);
        if (listener instanceof OnCheckedChangeListener) {
            checkBox.setOnCheckedChangeListener((OnCheckedChangeListener) listener);
        } else {
            rowView.setOnClickListener((View.OnClickListener) listener);
        }
        if (refresh) {
            this.notificationsListAdapter.notifyDataSetChanged();
        }
    }

    private void getAllMessageTones(String uri) {
        new C08224(new ProgressDialog(this.context), uri).execute(new Void[0]);
    }

    private void selectNotificationSound(String uri) {
        int selectedIndex = getTheIndexFromUri(uri);
        Builder builder = new Builder(this);
        builder.setTitle("Sounds");
        builder.setSingleChoiceItems(messageToneArray, selectedIndex, new C08235());
        builder.setPositiveButton("Save", new C08246());
        builder.setNegativeButton("Cancel", new C08257());
        this.alert = builder.create();
        this.alert.show();
    }

    private int getTheIndexFromUri(String uri) {
        if (uri == null) {
            return -1;
        }
        for (int i = 0; i < messageTones.length; i++) {
            MessageTone msgTone = messageTones[i];
            if (msgTone.uri.toString().contains(uri)) {
                System.out.println(msgTone.uri.toString() + " = content:/" + uri + " = matced");
                return i;
            }
        }
        return -1;
    }

    private UserNotification LoadUserNotificationSettings() {
        Cursor cursor = getContentResolver().query(UserProvider.CONTENT_URI_NOTIFICATION, null, "user_id=?", new String[]{this.userId}, null);
        cursor.moveToFirst();
        return UserProvider.userNotificationFromCursor(cursor);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                break;
        }
        return false;
    }

    protected void onPause() {
        if (this.alert != null) {
            this.alert.dismiss();
        }
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    private Stub getUICallback() {
        this.callback = new C08278();
        return this.callback;
    }
}

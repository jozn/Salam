package com.shamchat.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Images.Media;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.path.android.jobqueue.JobManager;
import com.shamchat.adapters.ProfileBottomAdapter;
import com.shamchat.adapters.ProfileBottomAdapter.BottomListItem;
import com.shamchat.adapters.ProfileDetailsAdapter;
import com.shamchat.adapters.ProfileDetailsAdapter.DetailsListItem;
import com.shamchat.androidclient.AndroidDatabaseManager;
import com.shamchat.androidclient.AndroidDatabaseManagerMoment;
import com.shamchat.androidclient.ChatController;
import com.shamchat.androidclient.IXMPPChatCallback.Stub;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.androidclient.service.SmackableImp;
import com.shamchat.androidclient.util.ConnectionState;
import com.shamchat.androidclient.util.FontUtil;
import com.shamchat.androidclient.util.PreferenceConstants;
import com.shamchat.androidclient.util.StatusMode;
import com.shamchat.events.FriendDBLoadCompletedEvent;
import com.shamchat.jobs.FriendDBLoadJob;
import com.shamchat.models.User;
import com.shamchat.utils.Utils;
import com.squareup.picasso.Picasso;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import de.greenrobot.event.EventBus;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyProfileActivity extends AppCompatActivity {
    public static int CROPED_IMAGE = 0;
    public static final String EXTRA_USER_ID = "extra_user_id";
    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_PICTURE = 1;
    protected static final String TAG = "MyProfileActivity";
    private ProfileBottomAdapter bottomAdapter;
    private Stub callback;
    private ChatController chatController;
    private String city;
    private ProfileDetailsAdapter detailsAdapter;
    private boolean edittingProfileName;
    private boolean edittingStatus;
    private FriendDBLoadJob friendDBLoadJob;
    private String friendJabberId;
    private String gender;
    private TextView genderTextView;
    private ImageView imageProfile;
    private ImageView imageStatus;
    private String imgPath;
    private boolean isProfileImageUpdated;
    private boolean isUpdated;
    private JobManager jobManager;
    private LinearLayout listBottom;
    private LinearLayout listDetails;
    private UserProvider mUserProvider;
    private Handler mainHandler;
    private SharedPreferences preference;
    private byte[] profileImageByteArray;
    private String profilePicture;
    private TextView regionTextView;
    private SharedPreferences sp;
    private View statusContainer;
    private EditText statusEditText;
    private String statusString;
    private TextView statusText;
    private TextView textName;
    private User user;

    /* renamed from: com.shamchat.activity.MyProfileActivity.10 */
    class AnonymousClass10 implements OnClickListener {
        final /* synthetic */ CharSequence[] val$items;

        AnonymousClass10(CharSequence[] charSequenceArr) {
            this.val$items = charSequenceArr;
        }

        public final void onClick(DialogInterface dialog, int item) {
            MyProfileActivity.this.setGender(this.val$items[item].toString(), true);
        }
    }

    /* renamed from: com.shamchat.activity.MyProfileActivity.1 */
    class C07961 implements View.OnClickListener {
        C07961() {
        }

        public final void onClick(View v) {
            MyProfileActivity.this.isUpdated = true;
            if (MyProfileActivity.this.isEdittingStatus()) {
                MyProfileActivity.this.setStatus(MyProfileActivity.this.setEdittingStatus(false), true);
                return;
            }
            MyProfileActivity.this.setEdittingStatus(true);
        }
    }

    /* renamed from: com.shamchat.activity.MyProfileActivity.1HttpAsyncTask */
    class AnonymousClass1HttpAsyncTask extends AsyncTask<String, String, String> {
        JSONArray array;
        JSONObject arrayObject;
        JSONObject main;
        final /* synthetic */ String val$email;
        final /* synthetic */ String val$gender;
        final /* synthetic */ String val$myStatus;
        final /* synthetic */ String val$profilePicture;
        final /* synthetic */ String val$region;
        final /* synthetic */ String val$shamID;
        final /* synthetic */ String val$userID;
        final /* synthetic */ String val$userName;

        AnonymousClass1HttpAsyncTask(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8) {
            this.val$profilePicture = str;
            this.val$shamID = str2;
            this.val$userName = str3;
            this.val$email = str4;
            this.val$gender = str5;
            this.val$myStatus = str6;
            this.val$region = str7;
            this.val$userID = str8;
            this.main = new JSONObject();
            this.array = new JSONArray();
            this.arrayObject = new JSONObject();
        }

        protected final /* bridge */ /* synthetic */ Object doInBackground(Object[] objArr) {
            return doInBackground$4af589aa();
        }

        protected final /* bridge */ /* synthetic */ void onPostExecute(Object obj) {
            String str = (String) obj;
            super.onPostExecute(str);
            if (str != null) {
                try {
                    if (new JSONObject(str).getString(NotificationCompatApi21.CATEGORY_STATUS).equals("success")) {
                        System.out.println("EDITED PROFILE myStatus " + this.val$myStatus);
                        MyProfileActivity.this.user.userId = this.val$userID;
                        MyProfileActivity.this.user.chatId = this.val$shamID;
                        MyProfileActivity.this.user.username = this.val$userName;
                        MyProfileActivity.this.user.email = this.val$email;
                        MyProfileActivity.this.user.myStatus = this.val$myStatus;
                        MyProfileActivity.this.user.profileImageUrl = this.val$profilePicture;
                        MyProfileActivity.this.mUserProvider;
                        UserProvider.updateUser(MyProfileActivity.this.user);
                        MyProfileActivity.this.saveVcard(MyProfileActivity.this.user);
                        MyProfileActivity.this.chatController.setStatus(StatusMode.available, MyProfileActivity.this.statusEditText.getText().toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        protected final void onPreExecute() {
            super.onPreExecute();
            if (this.val$profilePicture != null) {
                Log.d("profileurl", this.val$profilePicture);
            }
            try {
                this.arrayObject.put("chatId", this.val$shamID);
                this.arrayObject.put("userName", this.val$userName);
                this.arrayObject.put(NotificationCompatApi21.CATEGORY_EMAIL, this.val$email);
                this.arrayObject.put("profileImageUrl", this.val$profilePicture);
                this.arrayObject.put("mobileNo", MyProfileActivity.this.user.mobileNo);
                this.arrayObject.put("gender", this.val$gender);
                this.arrayObject.put("myStatus", this.val$myStatus);
                this.arrayObject.put("region", this.val$region);
                this.main.put("userId", this.val$userID);
                this.array.put(this.arrayObject);
                this.main.put("dataFields", this.array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private String doInBackground$4af589aa() {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            if (this.main == null) {
                System.out.println("null main");
            } else {
                System.out.println(this.main.toString());
            }
            try {
                HttpPost httpPost = new HttpPost(MyProfileActivity.this.getResources().getString(2131493167) + "updateUserDataField.htm");
                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                List data = new ArrayList();
                data.add(new BasicNameValuePair("userDataFiledJson", this.main.toString()));
                httpPost.entity = new UrlEncodedFormEntity(data, "UTF-8");
                System.out.println("httppost " + httpPost.uri);
                InputStream inputStream = httpclient.execute((HttpUriRequest) httpPost).getEntity().getContent();
                BufferedReader bufferreader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder responseStr = new StringBuilder();
                while (true) {
                    String responseLineStr = bufferreader.readLine();
                    if (responseLineStr != null) {
                        responseStr.append(responseLineStr);
                    } else {
                        bufferreader.close();
                        inputStream.close();
                        return responseStr.toString();
                    }
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    /* renamed from: com.shamchat.activity.MyProfileActivity.2 */
    class C07972 implements OnEditorActionListener {
        C07972() {
        }

        public final boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            MyProfileActivity.this.isUpdated = true;
            if (actionId != 6) {
                return false;
            }
            String newStatus = MyProfileActivity.this.setEdittingStatus(false);
            if (newStatus.equalsIgnoreCase(BuildConfig.VERSION_NAME)) {
                return true;
            }
            MyProfileActivity.this.setStatus(newStatus, true);
            return true;
        }
    }

    /* renamed from: com.shamchat.activity.MyProfileActivity.3 */
    class C07983 implements View.OnClickListener {
        C07983() {
        }

        public final void onClick(View v) {
            MyProfileActivity.this.selectOrTakePhoto();
        }
    }

    /* renamed from: com.shamchat.activity.MyProfileActivity.4 */
    class C07994 implements OnClickListener {
        final /* synthetic */ CharSequence[] val$items;

        C07994(CharSequence[] charSequenceArr) {
            this.val$items = charSequenceArr;
        }

        public final void onClick(DialogInterface dialog, int item) {
            Intent intent;
            if (this.val$items[item].equals("Take Photo")) {
                intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra("output", MyProfileActivity.this.setImageUri());
                MyProfileActivity.this.startActivityForResult(intent, MyProfileActivity.REQUEST_CAMERA);
            } else if (this.val$items[item].equals("Choose from Library")) {
                intent = new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                MyProfileActivity.this.startActivityForResult(Intent.createChooser(intent, "Select File"), MyProfileActivity.SELECT_PICTURE);
            } else if (this.val$items[item].equals("Reset")) {
                MyProfileActivity.this.user.profileImage = null;
                MyProfileActivity.this.imageProfile.setImageResource(2130838019);
                MyProfileActivity.this.mUserProvider;
                UserProvider.updateUser(MyProfileActivity.this.user);
            } else if (this.val$items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        }
    }

    /* renamed from: com.shamchat.activity.MyProfileActivity.5 */
    class C08005 implements Runnable {
        final /* synthetic */ User val$user;

        C08005(User user) {
            this.val$user = user;
        }

        public final void run() {
            try {
                System.out.println("Save vCard");
                VCard vcard = new VCard();
                if (SmackableImp.getXmppConnection() != null) {
                    vcard.setJabberId(Utils.createXmppUserIdByUserId(this.val$user.userId));
                    vcard.setNickName(this.val$user.chatId);
                    vcard.setFirstName(this.val$user.username);
                    vcard.setPhoneHome("CELL", this.val$user.mobileNo);
                    vcard.setMiddleName(this.val$user.profileImageUrl);
                    vcard.setAddressFieldHome("LOCALITY", this.val$user.cityOrRegion);
                    vcard.setField("GENDER", this.val$user.gender);
                    SHAMChatApplication.USER_IMAGES.remove(this.val$user.userId);
                    vcard.save(SmackableImp.getXmppConnection());
                    MyProfileActivity.this.chatController.setStatus(StatusMode.available, MyProfileActivity.this.statusEditText.getText().toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.shamchat.activity.MyProfileActivity.6 */
    class C08116 implements Runnable {
        final /* synthetic */ User val$user;

        /* renamed from: com.shamchat.activity.MyProfileActivity.6.1 */
        class C08011 implements View.OnClickListener {
            C08011() {
            }

            public final void onClick(View v) {
                System.out.println("id");
            }
        }

        /* renamed from: com.shamchat.activity.MyProfileActivity.6.2 */
        class C08022 implements View.OnClickListener {
            C08022() {
            }

            public final void onClick(View v) {
                MyProfileActivity.this.isUpdated = true;
                MyProfileActivity.this.showDialogEditRegion();
            }
        }

        /* renamed from: com.shamchat.activity.MyProfileActivity.6.3 */
        class C08033 implements View.OnClickListener {
            C08033() {
            }

            public final void onClick(View v) {
                System.out.println("Mobile");
            }
        }

        /* renamed from: com.shamchat.activity.MyProfileActivity.6.4 */
        class C08044 implements View.OnClickListener {
            C08044() {
            }

            public final void onClick(View v) {
                MyProfileActivity.this.isUpdated = true;
                MyProfileActivity.this.showDialogEditGender();
            }
        }

        /* renamed from: com.shamchat.activity.MyProfileActivity.6.5 */
        class C08055 implements View.OnClickListener {
            C08055() {
            }

            public final void onClick(View v) {
                Log.d(MyProfileActivity.TAG, "Settings pressed");
                MyProfileActivity.this.startActivity(new Intent(MyProfileActivity.this, SettingsActivity.class));
            }
        }

        /* renamed from: com.shamchat.activity.MyProfileActivity.6.6 */
        class C08066 implements View.OnClickListener {
            C08066() {
            }

            public final void onClick(View v) {
                MyProfileActivity.this.startActivity(new Intent(MyProfileActivity.this, AndroidDatabaseManager.class));
            }
        }

        /* renamed from: com.shamchat.activity.MyProfileActivity.6.7 */
        class C08077 implements View.OnClickListener {
            C08077() {
            }

            public final void onClick(View v) {
                MyProfileActivity.this.startActivity(new Intent(MyProfileActivity.this, AndroidDatabaseManagerMoment.class));
            }
        }

        /* renamed from: com.shamchat.activity.MyProfileActivity.6.8 */
        class C08088 implements View.OnClickListener {
            C08088() {
            }

            public final void onClick(View v) {
                MyProfileActivity.this.startActivity(new Intent(MyProfileActivity.this.getApplicationContext(), AboutShamActivity.class));
            }
        }

        /* renamed from: com.shamchat.activity.MyProfileActivity.6.9 */
        class C08109 implements View.OnClickListener {

            /* renamed from: com.shamchat.activity.MyProfileActivity.6.9.1 */
            class C08091 implements Runnable {
                C08091() {
                }

                public final void run() {
                    try {
                        SmackableImp.getXmppConnection().disconnect();
                    } catch (NotConnectedException e) {
                        e.printStackTrace();
                    }
                }
            }

            C08109() {
            }

            public final void onClick(View v) {
                Log.d(MyProfileActivity.TAG, "Exit pressed");
                if (SmackableImp.isXmppConnected() && PreferenceConstants.CONNECTED_TO_NETWORK) {
                    new Thread(new C08091()).start();
                }
                ChatController.getInstance(MyProfileActivity.this.getApplicationContext()).unRegisterChatCallback(MyProfileActivity.this.getUICallback());
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.HOME");
                intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
                MyProfileActivity.this.startActivity(intent);
                Process.killProcess(Process.myPid());
                MyProfileActivity.this.finish();
            }
        }

        C08116(User user) {
            this.val$user = user;
        }

        public final void run() {
            MyProfileActivity.this.textName.setText(this.val$user.username);
            String profileImageUrl = this.val$user.profileImageUrl;
            System.out.println("Profile image " + profileImageUrl);
            if (profileImageUrl == null || !profileImageUrl.contains("http://")) {
                MyProfileActivity.this.imageProfile.setImageResource(2130837944);
            } else {
                Uri.parse(profileImageUrl);
                MyProfileActivity.this.sp = MyProfileActivity.this.getApplicationContext().getSharedPreferences("picture", 0);
                Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(MyProfileActivity.this.sp.getString("picture", BuildConfig.VERSION_NAME)).into(MyProfileActivity.this.imageProfile, null);
            }
            String statusMessage = this.val$user.myStatus;
            System.out.println("Status from the db " + statusMessage);
            if (statusMessage == null) {
                MyProfileActivity.this.setStatus("What's up", false);
            } else if (statusMessage.equalsIgnoreCase("null")) {
                MyProfileActivity.this.setStatus("What's up", false);
            } else if (statusMessage.equalsIgnoreCase(BuildConfig.VERSION_NAME)) {
                MyProfileActivity.this.setStatus("What's up", false);
            } else {
                MyProfileActivity.this.setStatus(statusMessage, false);
            }
            MyProfileActivity.this.detailsAdapter.list.clear();
            MyProfileActivity.this.bottomAdapter.list.clear();
            MyProfileActivity.this.listDetails.removeAllViews();
            MyProfileActivity.this.listBottom.removeAllViews();
            MyProfileActivity.this.addDetail(MyProfileActivity.this.getResources().getString(2131493372), this.val$user.chatId, false, new C08011());
            MyProfileActivity.this.regionTextView = (TextView) MyProfileActivity.this.addDetail(MyProfileActivity.this.getResources().getString(2131493286), this.val$user.cityOrRegion, false, new C08022()).findViewById(2131362098);
            MyProfileActivity.this.addDetail(MyProfileActivity.this.getResources().getString(2131493197), this.val$user.mobileNo, false, new C08033());
            MyProfileActivity.this.genderTextView = (TextView) MyProfileActivity.this.addDetail(MyProfileActivity.this.getResources().getString(2131493153), this.val$user.gender, new C08044()).findViewById(2131362098);
            MyProfileActivity.this.addBottomItem(MyProfileActivity.this.getResources().getString(2131493369), 2130837982, new C08055(), false);
            MyProfileActivity.this.addBottomItem(MyProfileActivity.this.getResources().getString(2131493085), 2130837980, new C08066(), false);
            MyProfileActivity.this.addBottomItem(MyProfileActivity.this.getResources().getString(2131493086), 2130837980, new C08077(), false);
            MyProfileActivity.this.addBottomItem(MyProfileActivity.this.getResources().getString(2131492927), 2130837980, new C08088(), false);
            MyProfileActivity.this.addBottomItem(MyProfileActivity.this.getResources().getString(2131493122), 2130837981, new C08109());
        }
    }

    /* renamed from: com.shamchat.activity.MyProfileActivity.7 */
    class C08137 implements Runnable {
        final /* synthetic */ String val$email;
        final /* synthetic */ String val$gender;
        final /* synthetic */ String val$myStatus;
        final /* synthetic */ String val$profilePicture;
        final /* synthetic */ String val$region;
        final /* synthetic */ String val$shamID;
        final /* synthetic */ String val$userID;
        final /* synthetic */ String val$userName;

        /* renamed from: com.shamchat.activity.MyProfileActivity.7.1 */
        class C08121 extends JsonHttpResponseHandler {
            C08121() {
            }

            public final void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println(response);
                try {
                    if (response.getString(NotificationCompatApi21.CATEGORY_STATUS).equalsIgnoreCase("success")) {
                        System.out.println("FileUploadManager result is success");
                        String url = response.getJSONObject("data").getString("file_view_url");
                        if (url == null || url.length() <= MyProfileActivity.SELECT_PICTURE) {
                            MyProfileActivity.this.updateUserInformation(C08137.this.val$userID, C08137.this.val$shamID, C08137.this.val$userName, C08137.this.val$profilePicture, C08137.this.val$email, C08137.this.val$gender, C08137.this.val$myStatus, C08137.this.val$region);
                        } else {
                            MyProfileActivity.this.updateUserInformation(C08137.this.val$userID, C08137.this.val$shamID, C08137.this.val$userName, url, C08137.this.val$email, C08137.this.val$gender, C08137.this.val$myStatus, C08137.this.val$region);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public final void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println(responseString);
            }

            public final void onRetry(int retryNo) {
                super.onRetry(retryNo);
            }

            public final void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
            }
        }

        C08137(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8) {
            this.val$userID = str;
            this.val$shamID = str2;
            this.val$userName = str3;
            this.val$email = str4;
            this.val$gender = str5;
            this.val$myStatus = str6;
            this.val$region = str7;
            this.val$profilePicture = str8;
        }

        public final void run() {
            RequestParams params = new RequestParams();
            Looper.prepare();
            try {
                if (MyProfileActivity.this.isProfileImageUpdated) {
                    File directory = new File(Environment.getExternalStorageDirectory() + "/salam/profileimage");
                    if (!directory.exists()) {
                        directory.mkdir();
                    }
                    File photo = new File(directory.getAbsolutePath() + File.separator + MyProfileActivity.this.user.userId + ".png");
                    if (photo.exists()) {
                        photo.delete();
                        photo.createNewFile();
                    } else {
                        photo.createNewFile();
                    }
                    try {
                        FileOutputStream fos = new FileOutputStream(photo.getPath());
                        fos.write(Base64.decode(MyProfileActivity.this.user.profileImage, 0));
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    params.put("userId", MyProfileActivity.this.user.userId);
                    params.put("file", photo);
                    SyncHttpClient client = new SyncHttpClient();
                    client.setMaxRetriesAndTimeout(5, 10000);
                    client.setTimeout(300000);
                    client.setResponseTimeout(300000);
                    client.post(SHAMChatApplication.getMyApplicationContext(), "http://static.rabtcdn.com/upload_file.php", params, new C08121());
                } else {
                    MyProfileActivity.this.updateUserInformation(this.val$userID, this.val$shamID, this.val$userName, this.val$profilePicture, this.val$email, this.val$gender, this.val$myStatus, this.val$region);
                }
                Looper.loop();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /* renamed from: com.shamchat.activity.MyProfileActivity.8 */
    class C08158 extends Stub {

        /* renamed from: com.shamchat.activity.MyProfileActivity.8.1 */
        class C08141 implements Runnable {
            final /* synthetic */ int val$connectionstate;

            C08141(int i) {
                this.val$connectionstate = i;
            }

            @TargetApi(11)
            public final void run() {
                Log.d(MyProfileActivity.TAG, "MainWindow connectionStatusChanged: " + ConnectionState.values()[this.val$connectionstate]);
            }
        }

        C08158() {
        }

        public final void connectionStateChanged(int connectionstate) throws RemoteException {
            MyProfileActivity.this.mainHandler.post(new C08141(connectionstate));
        }

        public final void roomCreated(boolean created) throws RemoteException {
            Log.d(MyProfileActivity.TAG, "MainWindow room created " + created);
        }

        public final void didJoinRoom(boolean joined) throws RemoteException {
            Log.d(MyProfileActivity.TAG, "MainWindow joined room " + joined);
        }

        public final void onFriendComposing(String jabberId, boolean isTypng) throws RemoteException {
        }
    }

    /* renamed from: com.shamchat.activity.MyProfileActivity.9 */
    class C08169 implements OnClickListener {
        final /* synthetic */ CharSequence[] val$items;

        C08169(CharSequence[] charSequenceArr) {
            this.val$items = charSequenceArr;
        }

        public final void onClick(DialogInterface dialog, int item) {
            MyProfileActivity.this.setRegion(this.val$items[item].toString(), true);
        }
    }

    public MyProfileActivity() {
        this.mainHandler = new Handler();
        this.isProfileImageUpdated = false;
        this.edittingProfileName = false;
        this.isUpdated = false;
    }

    static {
        CROPED_IMAGE = 2;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903092);
        this.preference = PreferenceManager.getDefaultSharedPreferences(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(31);
        actionBar.setCustomView(2130903044);
        this.textName = (TextView) findViewById(2131361839);
        this.imageProfile = (ImageView) findViewById(2131361914);
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        this.mUserProvider = new UserProvider();
        this.chatController = ChatController.getInstance(getApplicationContext());
        String currentUserId = SHAMChatApplication.getConfig().userId;
        System.out.println("currentUserId user id " + currentUserId);
        this.friendDBLoadJob = new FriendDBLoadJob(currentUserId);
        refreshDetails(currentUserId);
        this.statusContainer = findViewById(2131362038);
        this.statusContainer.setOnClickListener(new C07961());
        this.statusText = (TextView) findViewById(2131362042);
        this.statusEditText = (EditText) findViewById(2131362039);
        this.statusEditText.setOnEditorActionListener(new C07972());
        FontUtil.applyFont((TextView) findViewById(2131362394));
        FontUtil.applyFont(this.statusText);
        FontUtil.applyFont(this.statusEditText);
        this.detailsAdapter = new ProfileDetailsAdapter(this);
        this.bottomAdapter = new ProfileBottomAdapter(this);
        this.listDetails = (LinearLayout) findViewById(2131362045);
        this.listBottom = (LinearLayout) findViewById(2131362046);
        this.imageProfile.setOnClickListener(new C07983());
    }

    private void selectOrTakePhoto() {
        CharSequence[] items = new CharSequence[]{"Take Photo", "Choose from Library", "Reset", "Cancel"};
        Builder builder = new Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new C07994(items));
        builder.show();
    }

    public Uri setImageUri() {
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }

    private boolean isEdittingStatus() {
        return this.edittingStatus;
    }

    private void setStatus(String status, boolean save) {
        this.statusText.setText(status);
        if (save) {
            this.user.myStatus = status;
            UserProvider.updateUser(this.user);
            this.chatController.setStatus(StatusMode.available, status);
            this.statusString = status;
            saveVcard(this.user);
        }
    }

    private String setEdittingStatus(boolean editting) {
        this.edittingStatus = editting;
        if (editting) {
            this.statusText.setVisibility(8);
            this.statusEditText.setVisibility(0);
            this.statusEditText.requestFocus();
            this.statusEditText.setText(this.statusText.getText().toString());
            Utils.showKeyboard(this.statusEditText, getApplicationContext());
        } else {
            this.statusEditText.setVisibility(8);
            this.statusText.setVisibility(0);
            this.statusText.setText(this.statusEditText.getText().toString());
            Utils.hideKeyboard(this.statusEditText, getApplicationContext());
        }
        return this.statusText.getText().toString();
    }

    private void saveVcard(User user) {
        new Thread(new C08005(user)).start();
    }

    private void refreshDetails(String userId) {
        this.jobManager.addJobInBackground(this.friendDBLoadJob);
    }

    public void onEventBackgroundThread(FriendDBLoadCompletedEvent event) {
        this.user = event.user;
        this.friendJabberId = Utils.createXmppUserIdByUserId(this.user.userId);
        System.out.println("friend jabber id " + this.friendJabberId);
        if (this.user == null || !this.user.userId.equals(SHAMChatApplication.getConfig().userId)) {
            System.out.println("User null");
        } else {
            updateUi(this.user);
        }
    }

    private void updateUi(User user) {
        System.out.println("User not null " + user.username);
        runOnUiThread(new C08116(user));
    }

    private void addBottomItem(String text, int iconRes, View.OnClickListener listener) {
        addBottomItem(text, iconRes, listener, true);
    }

    private void addBottomItem(String text, int iconRes, View.OnClickListener listener, boolean refresh) {
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

    protected void onResume() {
        super.onResume();
        try {
            EventBus.getDefault().register(this, true, 0);
        } catch (Throwable th) {
        }
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        if (this.user != null) {
            updateUserInformationThread(this.user.userId, this.user.chatId, this.user.username, this.user.profileImageUrl, this.user.email, this.user.gender, this.user.myStatus, this.user.cityOrRegion);
            finish();
        }
        return false;
    }

    private void updateUserInformationThread(String userID, String shamID, String userName, String profilePicture, String email, String gender, String myStatus, String region) {
        new Thread(new C08137(userID, shamID, userName, email, gender, myStatus, region, profilePicture)).start();
    }

    private void updateUserInformation(String userID, String shamID, String userName, String profilePicture, String email, String gender, String myStatus, String region) {
        AnonymousClass1HttpAsyncTask asyncTask = new AnonymousClass1HttpAsyncTask(profilePicture, shamID, userName, email, gender, myStatus, region, userID);
        if (asyncTask.getStatus() != Status.RUNNING) {
            asyncTask.execute(new String[0]);
        }
    }

    private Stub getUICallback() {
        this.callback = new C08158();
        return this.callback;
    }

    private View addDetail(String left, String right, View.OnClickListener listener) {
        return addDetail(left, right, true, listener);
    }

    private View addDetail(String left, String right, boolean refresh, View.OnClickListener listener) {
        this.detailsAdapter.list.add(new DetailsListItem(left, right));
        View view = this.detailsAdapter.getView(this.detailsAdapter.list.size() - 1, null, null);
        View clickView = view.findViewById(2131362096);
        this.listDetails.addView(view);
        clickView.setOnClickListener(listener);
        clickView.setBackgroundResource(2130837575);
        if (refresh) {
            this.detailsAdapter.notifyDataSetChanged();
        }
        return clickView;
    }

    private void showDialogEditRegion() {
        CharSequence[] items = new CharSequence[]{"Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua & Deps", "Argentina", "Armenia", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan", "Bolivia", "Bosnia Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Central African Rep", "Chad", "Chile", "China", "Colombia", "Comoros", "Congo", "Congo {Democratic Rep}", "Costa Rica", "Croatia", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Fiji", "Finland", "France", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Greece", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland {Republic}", "Italy", "Ivory Coast", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea North", "Korea South", "Kosovo", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Morocco", "Mozambique", "Myanmar, {Burma}", "Namibia", "Nauru", "Nepal", "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Qatar", "Romania", "Russian Federation", "Rwanda", "St Kitts & Nevis", "St Lucia", "Saint Vincent & the Grenadines", "Samoa", "San Marino", "Sao Tome & Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Swaziland", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Togo", "Tonga", "Trinidad & Tobago", "Tunisia", "Turkey", "Turkmenistan", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe"};
        Builder builder = new Builder(this);
        builder.setTitle("Choose Country");
        builder.setItems(items, new C08169(items));
        builder.create().show();
    }

    private void setRegion(String cityOrRegion, boolean save) {
        this.regionTextView.setText(cityOrRegion);
        if (save) {
            this.user.cityOrRegion = cityOrRegion;
            UserProvider.updateUser(this.user);
        }
        saveVcard(this.user);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void showDialogEditGender() {
        CharSequence[] items = new CharSequence[]{getResources().getString(2131493189), getResources().getString(2131492875)};
        Builder builder = new Builder(this);
        builder.setTitle(getResources().getString(2131492873));
        builder.setItems(items, new AnonymousClass10(items));
        builder.create().show();
    }

    private void setGender(String gender, boolean save) {
        this.genderTextView.setText(gender);
        this.gender = gender;
        if (save) {
            this.user.gender = gender;
            UserProvider.updateUser(this.user);
        }
        saveVcard(this.user);
    }

    public void cropCapturedImage(Uri picUri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(picUri, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", SELECT_PICTURE);
        cropIntent.putExtra("aspectY", SELECT_PICTURE);
        cropIntent.putExtra("outputX", AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY);
        cropIntent.putExtra("outputY", AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, ProfilePictureActivity.CROPED_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            this.isUpdated = true;
            if (requestCode == REQUEST_CAMERA) {
                cropCapturedImage(Uri.fromFile(new File(getImagePath())));
            } else if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String tempPath = getPath(selectedImageUri, this);
                this.imgPath = getPath(selectedImageUri, this);
                if (tempPath != null) {
                    cropCapturedImage(Uri.fromFile(new File(tempPath)));
                } else {
                    Toast.makeText(getApplicationContext(), "Incompatible Gallery", SELECT_PICTURE).show();
                }
            } else if (requestCode == CROPED_IMAGE) {
                Bitmap thePic = Utils.fixOrientation((Bitmap) data.getExtras().getParcelable("data"));
                ByteArrayOutputStream imageByteStream = new ByteArrayOutputStream();
                thePic.compress(CompressFormat.JPEG, 100, imageByteStream);
                thePic.recycle();
                this.profileImageByteArray = imageByteStream.toByteArray();
                this.imageProfile.setImageBitmap(BitmapFactory.decodeByteArray(this.profileImageByteArray, 0, this.profileImageByteArray.length));
            }
        }
        setProfileImage(true);
        this.isProfileImageUpdated = true;
    }

    private void setProfileImage(boolean save) {
        if (save) {
            String base64EncodedProfileImage;
            if (this.profileImageByteArray == null) {
                base64EncodedProfileImage = this.user.profileImage;
            } else {
                base64EncodedProfileImage = Base64.encodeToString(this.profileImageByteArray, 0);
            }
            this.user.profileImage = base64EncodedProfileImage;
            UserProvider.updateUser(this.user);
        }
    }

    public String getImagePath() {
        return this.imgPath;
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = new String[SELECT_PICTURE];
        projection[0] = "_data";
        Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                if (this.user != null) {
                    updateUserInformationThread(this.user.userId, this.user.chatId, this.user.username, this.user.profileImageUrl, this.user.email, this.user.gender, this.statusEditText.getText().toString().trim(), this.user.cityOrRegion);
                    finish();
                    break;
                }
                break;
        }
        return false;
    }
}

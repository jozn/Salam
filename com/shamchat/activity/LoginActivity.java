package com.shamchat.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.path.android.jobqueue.JobManager;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.androidclient.util.PreferenceConstants;
import com.shamchat.jobs.UpdatePhoneContactsDBJob;
import com.shamchat.models.User;
import com.shamchat.utils.PopUpUtil;
import com.shamchat.utils.Utils;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import de.greenrobot.event.EventBus;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private Button buttonLogin;
    private Button buttonSignUp;
    private EditText editTextPassword;
    private EditText editTextPhoneOrId;
    private TextView forgotPassword;
    private JobManager jobManager;
    private Dialog popUp;

    /* renamed from: com.shamchat.activity.LoginActivity.1 */
    class C07621 implements OnEditorActionListener {
        C07621() {
        }

        public final boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId != 5) {
                return false;
            }
            LoginActivity.this.editTextPassword.requestFocus();
            return true;
        }
    }

    /* renamed from: com.shamchat.activity.LoginActivity.1HttpAsyncTask */
    class AnonymousClass1HttpAsyncTask extends AsyncTask<String, String, String> {
        final /* synthetic */ String val$password;

        AnonymousClass1HttpAsyncTask(String str) {
            this.val$password = str;
        }

        protected final /* bridge */ /* synthetic */ void onPostExecute(Object obj) {
            String str = (String) obj;
            super.onPostExecute(str);
            try {
                ProgressBarDialogLogin.getInstance().dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (str != null) {
                try {
                    JSONObject jSONObject = new JSONObject(str);
                    String string = jSONObject.getString(PreferenceConstants.STATUS);
                    if (string.equals(PreferenceConstants.SUCCESS)) {
                        jSONObject = jSONObject.getJSONObject("data").getJSONObject("user");
                        string = jSONObject.getString("userId");
                        Editor edit = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                        edit.putString("account_jabberID", string + "@rabtcdn.com");
                        edit.putString("user_password", this.val$password);
                        edit.putString("account_jabberPW", this.val$password);
                        edit.putString("registration_status", "r_v_l_i");
                        User user = new User(jSONObject);
                        edit.putString("user_mobileNo", user.mobileNo);
                        edit.commit();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("name", user.username);
                        contentValues.put("chatId", user.chatId);
                        contentValues.put("userId", user.userId);
                        contentValues.put("mobileNo", user.mobileNo);
                        contentValues.put(NotificationCompatApi21.CATEGORY_EMAIL, user.email);
                        contentValues.put("gender", user.gender);
                        contentValues.put("myStatus", user.myStatus);
                        contentValues.put("newMessageAlert", user.newMessageAlert);
                        contentValues.put("inAppAlert", user.inAppAlert);
                        contentValues.put("emailVerificationStatus", user.emailVerificationStatus);
                        contentValues.put("tempUserId", user.tmpUserId);
                        contentValues.put("region", user.cityOrRegion);
                        contentValues.put("jabberd_resource", user.jabberdResource);
                        contentValues.put("profileimage_url", user.profileImageUrl);
                        if (!(user.findMeByPhoneNo == null || user.findMeByShamId == null)) {
                            contentValues.put("find_me_by_mobile_no", Integer.valueOf(user.findMeByPhoneNo.status));
                            contentValues.put("find_me_by_chat_id", Integer.valueOf(user.findMeByShamId.status));
                        }
                        Cursor query = LoginActivity.this.getContentResolver().query(UserProvider.CONTENT_URI_USER, new String[]{"userId"}, "userId=?", new String[]{user.userId}, null);
                        if (query.getCount() > 0) {
                            LoginActivity.this.getContentResolver().update(Uri.parse(UserProvider.CONTENT_URI_USER.toString() + MqttTopic.TOPIC_LEVEL_SEPARATOR + user.userId), contentValues, null, null);
                        } else {
                            LoginActivity.this.getContentResolver().insert(UserProvider.CONTENT_URI_USER, contentValues);
                        }
                        query.close();
                        query = LoginActivity.this.getContentResolver().query(UserProvider.CONTENT_URI_NOTIFICATION, new String[]{ChatActivity.INTENT_EXTRA_USER_ID}, "user_id=?", new String[]{user.userId}, null);
                        if (query.getCount() == 0) {
                            ContentValues contentValues2 = new ContentValues();
                            contentValues2.put(ChatActivity.INTENT_EXTRA_USER_ID, user.userId);
                            LoginActivity.this.getContentResolver().insert(UserProvider.CONTENT_URI_NOTIFICATION, contentValues2);
                        }
                        query.close();
                        LoginActivity.this.jobManager.addJobInBackground(new UpdatePhoneContactsDBJob());
                        Intent intent = new Intent(LoginActivity.this, MainWindow.class);
                        intent.setFlags(339738624);
                        LoginActivity.this.startActivity(intent);
                        LoginActivity.this.finish();
                    } else if (string.equals("incorrect_mobile_no")) {
                        LoginActivity.this.editTextPhoneOrId.setText(BuildConfig.VERSION_NAME);
                        LoginActivity.this.editTextPhoneOrId.setHint(LoginActivity.this.getResources().getString(2131493172));
                        LoginActivity.this.editTextPhoneOrId.setHintTextColor(SupportMenu.CATEGORY_MASK);
                    } else if (string.equals("incorrect_password")) {
                        LoginActivity.this.editTextPassword.setText(BuildConfig.VERSION_NAME);
                        LoginActivity.this.editTextPassword.setHint(LoginActivity.this.getResources().getString(2131493173));
                        LoginActivity.this.editTextPassword.setHintTextColor(SupportMenu.CATEGORY_MASK);
                    } else if (string.equals("incorrect_mobile_no_or_chat_id")) {
                        LoginActivity.this.editTextPhoneOrId.setText(BuildConfig.VERSION_NAME);
                        LoginActivity.this.editTextPhoneOrId.setHint(LoginActivity.this.getResources().getString(2131493172));
                        LoginActivity.this.editTextPhoneOrId.setHintTextColor(SupportMenu.CATEGORY_MASK);
                    } else if (string.equalsIgnoreCase(MqttServiceConstants.TRACE_EXCEPTION)) {
                        LoginActivity.this.editTextPhoneOrId.setText(BuildConfig.VERSION_NAME);
                        LoginActivity.this.editTextPhoneOrId.setHint(LoginActivity.this.getResources().getString(2131493172));
                        LoginActivity.this.editTextPhoneOrId.setHintTextColor(SupportMenu.CATEGORY_MASK);
                        LoginActivity.this.editTextPassword.setText(BuildConfig.VERSION_NAME);
                        LoginActivity.this.editTextPassword.setHint(LoginActivity.this.getResources().getString(2131493173));
                        LoginActivity.this.editTextPassword.setHintTextColor(SupportMenu.CATEGORY_MASK);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }

        protected final void onPreExecute() {
            super.onPreExecute();
        }

        private String doInBackground(String... params) {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            try {
                String encodedMobileNo = URLEncoder.encode(params[0], "UTF-8");
                InputStream inputStream = httpclient.execute((HttpUriRequest) new HttpGet(LoginActivity.this.getResources().getString(2131493167) + "userLogin.htm?mobileNoOrChatId=" + encodedMobileNo + "&password=" + URLEncoder.encode(params[1], "UTF-8"))).getEntity().getContent();
                BufferedReader bufferreader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder responseStr = new StringBuilder();
                while (true) {
                    String responseLineStr = bufferreader.readLine();
                    if (responseLineStr != null) {
                        responseStr.append(responseLineStr);
                    } else {
                        bufferreader.close();
                        inputStream.close();
                        return responseStr.toString().trim();
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

    /* renamed from: com.shamchat.activity.LoginActivity.2 */
    class C07642 implements OnClickListener {

        /* renamed from: com.shamchat.activity.LoginActivity.2.1 */
        class C07631 implements OnClickListener {
            C07631() {
            }

            public final void onClick(View v) {
                LoginActivity.this.popUp.dismiss();
            }
        }

        C07642() {
        }

        public final void onClick(View v) {
            if (Utils.isEditTextEmpty(LoginActivity.this.editTextPhoneOrId)) {
                LoginActivity.this.editTextPhoneOrId.setHint(LoginActivity.this.getResources().getString(2131493448));
                LoginActivity.this.editTextPhoneOrId.setHintTextColor(SupportMenu.CATEGORY_MASK);
            } else if (Utils.isEditTextEmpty(LoginActivity.this.editTextPassword)) {
                LoginActivity.this.editTextPassword.setHint(LoginActivity.this.getResources().getString(2131493448));
                LoginActivity.this.editTextPassword.setHintTextColor(SupportMenu.CATEGORY_MASK);
            } else if (Utils.isInternetAvailable(LoginActivity.this)) {
                try {
                    ProgressBarDialogLogin.getInstance().show(LoginActivity.this.getSupportFragmentManager(), BuildConfig.VERSION_NAME);
                } catch (Exception e) {
                }
                String phoneNoOrId = LoginActivity.this.editTextPhoneOrId.getText().toString().trim();
                if (!phoneNoOrId.contains(MqttTopic.SINGLE_LEVEL_WILDCARD) && phoneNoOrId.matches("[0-9]+") && phoneNoOrId.substring(0, 2).equalsIgnoreCase("09")) {
                    phoneNoOrId = "+989" + phoneNoOrId.substring(2);
                }
                LoginActivity.this.loginWithMobileNo(Utils.convertToEnglishDigits(phoneNoOrId), Utils.convertToEnglishDigits(LoginActivity.this.editTextPassword.getText().toString().trim().replaceAll(" ", "_")));
            } else {
                System.out.println("Came here no internet");
                PopUpUtil popUpUtil = new PopUpUtil();
                LoginActivity loginActivity = LoginActivity.this;
                Context context = LoginActivity.this;
                LoginActivity.this.getResources().getString(2131493124);
                loginActivity.popUp = PopUpUtil.getPopFailed$478dbc03(context, LoginActivity.this.getResources().getString(2131493226), new C07631());
                LoginActivity.this.popUp.show();
            }
        }
    }

    /* renamed from: com.shamchat.activity.LoginActivity.3 */
    class C07653 implements OnClickListener {
        C07653() {
        }

        public final void onClick(View v) {
            Intent i = new Intent(LoginActivity.this, RegisterPhoneActivity.class);
            i.setFlags(67108864);
            LoginActivity.this.startActivity(i);
            LoginActivity.this.finish();
        }
    }

    /* renamed from: com.shamchat.activity.LoginActivity.4 */
    class C07664 implements OnClickListener {
        C07664() {
        }

        public final void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this.getApplicationContext(), RegisterPhoneActivity.class);
            Bundle b = new Bundle();
            b.putBoolean(PreferenceConstants.FORGOT_PASSWORD, true);
            intent.putExtras(b);
            LoginActivity.this.startActivity(intent);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        getWindow().addFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        super.onCreate(savedInstanceState);
        setContentView(2130903089);
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        this.forgotPassword = (TextView) findViewById(2131362033);
        this.buttonLogin = (Button) findViewById(2131362034);
        this.buttonSignUp = (Button) findViewById(2131362035);
        this.editTextPhoneOrId = (EditText) findViewById(2131362028);
        this.editTextPassword = (EditText) findViewById(2131362030);
        this.editTextPhoneOrId.setOnEditorActionListener(new C07621());
        this.buttonLogin.setOnClickListener(new C07642());
        this.buttonSignUp.setOnClickListener(new C07653());
        this.forgotPassword.setOnClickListener(new C07664());
    }

    public void onResume() {
        super.onResume();
        try {
            EventBus.getDefault().register(this, true, 0);
        } catch (Throwable th) {
        }
    }

    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void loginWithMobileNo(String phoneNumber, String password) {
        new AnonymousClass1HttpAsyncTask(password).execute(new String[]{phoneNumber, password});
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        Process.killProcess(Process.myPid());
        return false;
    }
}

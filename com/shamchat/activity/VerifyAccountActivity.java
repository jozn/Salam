package com.shamchat.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.preference.PreferenceManager;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.path.android.jobqueue.JobManager;
import com.rokhgroup.utils.RokhPref;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.androidclient.util.PreferenceConstants;
import com.shamchat.jobs.UpdatePhoneContactsDBJob;
import com.shamchat.models.User;
import com.shamchat.utils.PopUpUtil;
import com.shamchat.utils.Utils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import de.greenrobot.event.EventBus;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

public class VerifyAccountActivity extends AppCompatActivity implements OnClickListener {
    private static String USER_ID;
    private static String USER_TOKEN;
    private boolean CheckNeeded;
    private RokhPref Session;
    private String Username;
    private Button buttonContinue;
    private Button buttonResend;
    private EditText editText;
    private EditText editTextUser;
    private TextView infoText;
    private boolean isRunning;
    private JobManager jobManager;
    private JSONObject jsonResponse;
    private Context mContext;
    private String phoneNumber;
    private Dialog popUp;
    private SharedPreferences preferences;
    private String previousVerificationCode;
    private JSONObject serverResponseJsonObject;
    private String status;
    private Boolean statusBoolean;
    private String stringResponse;
    private String wisUsername;

    /* renamed from: com.shamchat.activity.VerifyAccountActivity.10 */
    class AnonymousClass10 implements Callback {
        final /* synthetic */ String val$chatId;
        final /* synthetic */ String val$email;
        final /* synthetic */ Intent val$i;
        final /* synthetic */ String val$password;
        final /* synthetic */ String val$phoneNumber;
        final /* synthetic */ User val$user;
        final /* synthetic */ String val$username;

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.10.1 */
        class C08971 implements Runnable {
            final /* synthetic */ IOException val$e;

            C08971(IOException iOException) {
                this.val$e = iOException;
            }

            public final void run() {
                VerifyAccountActivity.this.isRunning = false;
                VerifyAccountActivity.this.showErrorPopup(this.val$e);
            }
        }

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.10.2 */
        class C08982 implements Runnable {
            final /* synthetic */ JSONObject val$jsonResponse;

            C08982(JSONObject jSONObject) {
                this.val$jsonResponse = jSONObject;
            }

            public final void run() {
                try {
                    Log.e("****************** REGISTER RESPONSE", this.val$jsonResponse.toString());
                    if (this.val$jsonResponse.getBoolean(NotificationCompatApi21.CATEGORY_STATUS)) {
                        VerifyAccountActivity.this.logintoSocial(AnonymousClass10.this.val$i, AnonymousClass10.this.val$user, AnonymousClass10.this.val$username, AnonymousClass10.this.val$phoneNumber, AnonymousClass10.this.val$chatId, AnonymousClass10.this.val$password, AnonymousClass10.this.val$email);
                        return;
                    }
                    VerifyAccountActivity.this.isRunning = false;
                    VerifyAccountActivity.this.showErrorPopup(new Exception("registerOnSocial status not ok"));
                } catch (Exception e) {
                    VerifyAccountActivity.this.isRunning = false;
                    VerifyAccountActivity.this.showErrorPopup(e);
                }
            }
        }

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.10.3 */
        class C08993 implements Runnable {
            final /* synthetic */ Exception val$e;

            C08993(Exception exception) {
                this.val$e = exception;
            }

            public final void run() {
                VerifyAccountActivity.this.isRunning = false;
                VerifyAccountActivity.this.showErrorPopup(this.val$e);
            }
        }

        AnonymousClass10(Intent intent, User user, String str, String str2, String str3, String str4, String str5) {
            this.val$i = intent;
            this.val$user = user;
            this.val$username = str;
            this.val$phoneNumber = str2;
            this.val$chatId = str3;
            this.val$password = str4;
            this.val$email = str5;
        }

        public final void onFailure(Request request, IOException e) {
            VerifyAccountActivity.this.runOnUiThread(new C08971(e));
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String stringResponse = response.body().string();
                    response.body().close();
                    System.out.println(stringResponse);
                    JSONObject jsonResponse = new JSONObject(stringResponse);
                    System.out.println(stringResponse);
                    VerifyAccountActivity.this.runOnUiThread(new C08982(jsonResponse));
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                VerifyAccountActivity.this.runOnUiThread(new C08993(e));
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.shamchat.activity.VerifyAccountActivity.11 */
    class AnonymousClass11 implements Callback {
        final /* synthetic */ Intent val$i;
        final /* synthetic */ String val$password;
        final /* synthetic */ User val$user;

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.11.1 */
        class C09001 implements Runnable {
            final /* synthetic */ IOException val$e;

            C09001(IOException iOException) {
                this.val$e = iOException;
            }

            public final void run() {
                VerifyAccountActivity.this.isRunning = false;
                VerifyAccountActivity.this.showErrorPopup(this.val$e);
            }
        }

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.11.2 */
        class C09012 implements Runnable {
            final /* synthetic */ JSONObject val$jsonResponse;

            C09012(JSONObject jSONObject) {
                this.val$jsonResponse = jSONObject;
            }

            public final void run() {
                try {
                    if (this.val$jsonResponse.getBoolean("success")) {
                        VerifyAccountActivity.USER_TOKEN = this.val$jsonResponse.getString("token");
                        VerifyAccountActivity.USER_ID = this.val$jsonResponse.getString("id");
                        Log.e("LOGIN RESPONSE_OD", VerifyAccountActivity.USER_ID);
                        Log.e("LOGIN RESPONSE_TOKEN", VerifyAccountActivity.USER_TOKEN);
                        Log.e("LOGIN PASSWORD", AnonymousClass11.this.val$password);
                        VerifyAccountActivity.this.postSaveUserData(AnonymousClass11.this.val$i, AnonymousClass11.this.val$user, AnonymousClass11.this.val$password, VerifyAccountActivity.USER_TOKEN, VerifyAccountActivity.USER_ID);
                        return;
                    }
                    VerifyAccountActivity.this.isRunning = false;
                    VerifyAccountActivity.this.showErrorPopup(new Exception("logintoSocial status not ok"));
                } catch (Exception e) {
                    VerifyAccountActivity.this.isRunning = false;
                    VerifyAccountActivity.this.showErrorPopup(e);
                }
            }
        }

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.11.3 */
        class C09023 implements Runnable {
            final /* synthetic */ Exception val$e;

            C09023(Exception exception) {
                this.val$e = exception;
            }

            public final void run() {
                VerifyAccountActivity.this.isRunning = false;
                VerifyAccountActivity.this.showErrorPopup(this.val$e);
            }
        }

        AnonymousClass11(String str, Intent intent, User user) {
            this.val$password = str;
            this.val$i = intent;
            this.val$user = user;
        }

        public final void onFailure(Request request, IOException e) {
            VerifyAccountActivity.this.runOnUiThread(new C09001(e));
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String stringResponse = response.body().string();
                    response.body().close();
                    System.out.println(stringResponse);
                    JSONObject jsonResponse = new JSONObject(stringResponse);
                    Log.e("****************** LOGIN RESPONSE", stringResponse);
                    VerifyAccountActivity.this.runOnUiThread(new C09012(jsonResponse));
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                VerifyAccountActivity.this.runOnUiThread(new C09023(e));
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.shamchat.activity.VerifyAccountActivity.12 */
    class AnonymousClass12 implements Callback {
        final /* synthetic */ Intent val$i;
        final /* synthetic */ String val$token;
        final /* synthetic */ String val$userId;

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.12.1 */
        class C09031 implements Runnable {
            final /* synthetic */ IOException val$e;

            C09031(IOException iOException) {
                this.val$e = iOException;
            }

            public final void run() {
                VerifyAccountActivity.this.isRunning = false;
                VerifyAccountActivity.this.showErrorPopup(this.val$e);
            }
        }

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.12.2 */
        class C09042 implements Runnable {
            final /* synthetic */ JSONObject val$jsonResponse;

            C09042(JSONObject jSONObject) {
                this.val$jsonResponse = jSONObject;
            }

            public final void run() {
                try {
                    if (Boolean.valueOf(this.val$jsonResponse.getString(NotificationCompatApi21.CATEGORY_STATUS)).booleanValue()) {
                        VerifyAccountActivity.this.Session.setFirstRun(VerifyAccountActivity.this.CheckNeeded);
                        RokhPref access$2800 = VerifyAccountActivity.this.Session;
                        String str = AnonymousClass12.this.val$token;
                        String str2 = AnonymousClass12.this.val$userId;
                        access$2800.editor.putBoolean("IsLoggedin", true);
                        access$2800.editor.putString("token", str);
                        access$2800.editor.putString("userId", str2);
                        access$2800.editor.commit();
                        System.out.println("Social side tasks successfully done!");
                        VerifyAccountActivity.this.startActivity(AnonymousClass12.this.val$i);
                        VerifyAccountActivity.this.hideProgressBar();
                        VerifyAccountActivity.this.isRunning = false;
                        VerifyAccountActivity.this.finish();
                        return;
                    }
                    VerifyAccountActivity.this.isRunning = false;
                    VerifyAccountActivity.this.showErrorPopup(new Exception("postSaveUserData status not ok"));
                } catch (Exception e) {
                    VerifyAccountActivity.this.isRunning = false;
                    VerifyAccountActivity.this.showErrorPopup(e);
                }
            }
        }

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.12.3 */
        class C09053 implements Runnable {
            final /* synthetic */ Exception val$e;

            C09053(Exception exception) {
                this.val$e = exception;
            }

            public final void run() {
                VerifyAccountActivity.this.isRunning = false;
                VerifyAccountActivity.this.showErrorPopup(this.val$e);
            }
        }

        AnonymousClass12(String str, String str2, Intent intent) {
            this.val$token = str;
            this.val$userId = str2;
            this.val$i = intent;
        }

        public final void onFailure(Request request, IOException e) {
            VerifyAccountActivity.this.runOnUiThread(new C09031(e));
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String stringResponse = response.body().string();
                    response.body().close();
                    System.out.println(stringResponse);
                    JSONObject jsonResponse = new JSONObject(stringResponse);
                    System.out.println(stringResponse);
                    VerifyAccountActivity.this.runOnUiThread(new C09042(jsonResponse));
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                VerifyAccountActivity.this.runOnUiThread(new C09053(e));
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.shamchat.activity.VerifyAccountActivity.1 */
    class C09061 implements Runnable {
        C09061() {
        }

        public final void run() {
            VerifyAccountActivity.this.infoText.setText(VerifyAccountActivity.this.getResources().getString(2131493435) + " " + VerifyAccountActivity.this.phoneNumber);
        }
    }

    /* renamed from: com.shamchat.activity.VerifyAccountActivity.2 */
    class C09072 implements OnClickListener {
        C09072() {
        }

        public final void onClick(View v) {
            if (VerifyAccountActivity.this.editText.getText().length() == 0) {
                VerifyAccountActivity.this.hideProgressBar();
                VerifyAccountActivity.this.isRunning = false;
                VerifyAccountActivity.this.editText.setText(BuildConfig.VERSION_NAME);
                VerifyAccountActivity.this.editText.setHint(VerifyAccountActivity.this.getString(2131493117));
                VerifyAccountActivity.this.editText.setHintTextColor(SupportMenu.CATEGORY_MASK);
            } else if (VerifyAccountActivity.this.CheckNeeded) {
                VerifyAccountActivity.this.wisUsername = VerifyAccountActivity.this.editTextUser.getText().toString();
                VerifyAccountActivity.this.wisUsername = VerifyAccountActivity.this.editTextUser.getText().toString();
                if (Pattern.compile("^[a-zA-Z0-9]*$").matcher(VerifyAccountActivity.this.wisUsername.toString()).find()) {
                    VerifyAccountActivity.this.wisUsername = VerifyAccountActivity.this.editTextUser.getText().toString();
                    if (VerifyAccountActivity.this.wisUsername.length() == 0) {
                        VerifyAccountActivity.this.editTextUser.setText(BuildConfig.VERSION_NAME);
                        VerifyAccountActivity.this.editTextUser.setHint("Enter another username");
                        VerifyAccountActivity.this.editTextUser.setHintTextColor(SupportMenu.CATEGORY_MASK);
                        return;
                    } else if (VerifyAccountActivity.this.editText.getText().length() == 0) {
                        VerifyAccountActivity.this.editText.setText(BuildConfig.VERSION_NAME);
                        VerifyAccountActivity.this.editText.setHint(VerifyAccountActivity.this.getString(2131493117));
                        VerifyAccountActivity.this.editText.setHintTextColor(SupportMenu.CATEGORY_MASK);
                        return;
                    } else {
                        VerifyAccountActivity.this.showProgressBar();
                        VerifyAccountActivity.this.isRunning = true;
                        VerifyAccountActivity.this.checkUsernameAvailability(VerifyAccountActivity.this.wisUsername);
                        return;
                    }
                }
                Toast.makeText(VerifyAccountActivity.this.mContext, 2131493325, 0).show();
                VerifyAccountActivity.this.editTextUser.setText(BuildConfig.VERSION_NAME);
                VerifyAccountActivity.this.editTextUser.setHint(2131493311);
                VerifyAccountActivity.this.editTextUser.setHintTextColor(SupportMenu.CATEGORY_MASK);
            } else {
                VerifyAccountActivity.this.showProgressBar();
                VerifyAccountActivity.this.isRunning = true;
                VerifyAccountActivity.this.verifyAccount(VerifyAccountActivity.this.phoneNumber, Utils.convertToEnglishDigits(VerifyAccountActivity.this.editText.getText().toString().trim()));
            }
        }
    }

    /* renamed from: com.shamchat.activity.VerifyAccountActivity.3 */
    class C09113 implements Callback {
        final /* synthetic */ String val$phoneNumber;
        final /* synthetic */ String val$verificationCode;

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.3.1 */
        class C09081 implements Runnable {
            C09081() {
            }

            public final void run() {
                Toast.makeText(VerifyAccountActivity.this, VerifyAccountActivity.this.getResources().getString(2131492929), 1).show();
                VerifyAccountActivity.this.hideProgressBar();
                VerifyAccountActivity.this.isRunning = false;
            }
        }

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.3.2 */
        class C09092 implements Runnable {
            C09092() {
            }

            public final void run() {
                String code;
                try {
                    code = VerifyAccountActivity.this.serverResponseJsonObject.getJSONObject("data").getString("previously_confirmed_verification_code");
                } catch (Exception e) {
                    code = null;
                }
                Editor editor;
                if (VerifyAccountActivity.this.status.equals("success")) {
                    editor = VerifyAccountActivity.this.preferences.edit();
                    editor.putString("registration_status", "r_v");
                    editor.commit();
                    if (!code.equalsIgnoreCase("null")) {
                        VerifyAccountActivity.this.previousVerificationCode = code;
                    }
                    VerifyAccountActivity.this.assignPasswordToUser(C09113.this.val$phoneNumber, C09113.this.val$verificationCode);
                } else if (VerifyAccountActivity.this.status.equals("invalid_verification_code")) {
                    VerifyAccountActivity.this.editText.setText(BuildConfig.VERSION_NAME);
                    VerifyAccountActivity.this.editText.setHint(VerifyAccountActivity.this.getResources().getString(2131493175));
                    VerifyAccountActivity.this.editText.setHintTextColor(SupportMenu.CATEGORY_MASK);
                    VerifyAccountActivity.this.isRunning = false;
                    VerifyAccountActivity.this.hideProgressBar();
                } else if (VerifyAccountActivity.this.status.equals("user_already_verified")) {
                    editor = VerifyAccountActivity.this.preferences.edit();
                    editor.putString("registration_status", "r_v");
                    editor.commit();
                    VerifyAccountActivity.this.assignPasswordToUser(C09113.this.val$phoneNumber, C09113.this.val$verificationCode);
                }
            }
        }

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.3.3 */
        class C09103 implements Runnable {
            C09103() {
            }

            public final void run() {
                Toast.makeText(VerifyAccountActivity.this, VerifyAccountActivity.this.getResources().getString(2131492929), 1).show();
                VerifyAccountActivity.this.hideProgressBar();
                VerifyAccountActivity.this.isRunning = false;
            }
        }

        C09113(String str, String str2) {
            this.val$phoneNumber = str;
            this.val$verificationCode = str2;
        }

        public final void onFailure(Request request, IOException e) {
            VerifyAccountActivity.this.runOnUiThread(new C09081());
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            VerifyAccountActivity.this.stringResponse = null;
            VerifyAccountActivity.this.serverResponseJsonObject = null;
            VerifyAccountActivity.this.status = null;
            try {
                if (response.isSuccessful()) {
                    VerifyAccountActivity.this.stringResponse = response.body().string();
                    response.body().close();
                    VerifyAccountActivity.this.serverResponseJsonObject = new JSONObject(VerifyAccountActivity.this.stringResponse);
                    VerifyAccountActivity.this.status = VerifyAccountActivity.this.serverResponseJsonObject.getString(NotificationCompatApi21.CATEGORY_STATUS);
                    VerifyAccountActivity.this.runOnUiThread(new C09092());
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                VerifyAccountActivity.this.runOnUiThread(new C09103());
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.shamchat.activity.VerifyAccountActivity.4 */
    class C09154 implements Callback {

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.4.1 */
        class C09121 implements Runnable {
            C09121() {
            }

            public final void run() {
                Toast.makeText(VerifyAccountActivity.this.getApplicationContext(), 2131493293, 0).show();
                VerifyAccountActivity.this.hideProgressBar();
                VerifyAccountActivity.this.isRunning = false;
            }
        }

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.4.2 */
        class C09132 implements Runnable {
            C09132() {
            }

            public final void run() {
                if (VerifyAccountActivity.this.status.equals("success")) {
                    Toast.makeText(VerifyAccountActivity.this.getApplicationContext(), 2131493294, 0).show();
                    VerifyAccountActivity.this.hideProgressBar();
                    VerifyAccountActivity.this.isRunning = false;
                } else if (VerifyAccountActivity.this.status.equals("verification_code_invalid")) {
                    VerifyAccountActivity.this.editText.setText(BuildConfig.VERSION_NAME);
                    VerifyAccountActivity.this.editText.setHint(VerifyAccountActivity.this.getResources().getString(2131493175));
                    VerifyAccountActivity.this.editText.setHintTextColor(SupportMenu.CATEGORY_MASK);
                    Toast.makeText(VerifyAccountActivity.this.getApplicationContext(), 2131493293, 0).show();
                    VerifyAccountActivity.this.hideProgressBar();
                    VerifyAccountActivity.this.isRunning = false;
                }
            }
        }

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.4.3 */
        class C09143 implements Runnable {
            C09143() {
            }

            public final void run() {
                Toast.makeText(VerifyAccountActivity.this.getApplicationContext(), 2131493293, 0).show();
                VerifyAccountActivity.this.hideProgressBar();
                VerifyAccountActivity.this.isRunning = false;
            }
        }

        C09154() {
        }

        public final void onFailure(Request request, IOException e) {
            VerifyAccountActivity.this.runOnUiThread(new C09121());
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            VerifyAccountActivity.this.stringResponse = null;
            VerifyAccountActivity.this.serverResponseJsonObject = null;
            VerifyAccountActivity.this.status = null;
            VerifyAccountActivity.this.statusBoolean = null;
            try {
                if (response.isSuccessful()) {
                    VerifyAccountActivity.this.stringResponse = response.body().string();
                    response.body().close();
                    VerifyAccountActivity.this.serverResponseJsonObject = new JSONObject(VerifyAccountActivity.this.stringResponse);
                    VerifyAccountActivity.this.status = VerifyAccountActivity.this.serverResponseJsonObject.getString(NotificationCompatApi21.CATEGORY_STATUS);
                    System.out.println("resend code: " + VerifyAccountActivity.this.serverResponseJsonObject.getJSONObject("data").getString("verification_code"));
                    VerifyAccountActivity.this.runOnUiThread(new C09132());
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                VerifyAccountActivity.this.runOnUiThread(new C09143());
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.shamchat.activity.VerifyAccountActivity.5 */
    class C09195 implements Callback {
        final /* synthetic */ String val$password;
        final /* synthetic */ String val$phoneNumber;

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.5.1 */
        class C09161 implements Runnable {
            final /* synthetic */ IOException val$e;

            C09161(IOException iOException) {
                this.val$e = iOException;
            }

            public final void run() {
                VerifyAccountActivity.this.isRunning = false;
                VerifyAccountActivity.this.showErrorPopup(this.val$e);
            }
        }

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.5.2 */
        class C09172 implements Runnable {
            final /* synthetic */ JSONObject val$jsonResponse;

            C09172(JSONObject jSONObject) {
                this.val$jsonResponse = jSONObject;
            }

            public final void run() {
                try {
                    String status = this.val$jsonResponse.getString(NotificationCompatApi21.CATEGORY_STATUS);
                    if (status.equals("success")) {
                        VerifyAccountActivity.this.loginWithMobileNo(C09195.this.val$phoneNumber, C09195.this.val$password);
                    } else if (status.equals("user_not_verified")) {
                        Toast.makeText(VerifyAccountActivity.this, 2131493434, 1).show();
                        VerifyAccountActivity.this.hideProgressBar();
                        VerifyAccountActivity.this.isRunning = false;
                    } else if (status.equals("user_not_exists")) {
                        Toast.makeText(VerifyAccountActivity.this, 2131493232, 1).show();
                        VerifyAccountActivity.this.hideProgressBar();
                        VerifyAccountActivity.this.isRunning = false;
                    }
                } catch (Exception e) {
                    VerifyAccountActivity.this.isRunning = false;
                    VerifyAccountActivity.this.showErrorPopup(e);
                }
            }
        }

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.5.3 */
        class C09183 implements Runnable {
            final /* synthetic */ Exception val$e;

            C09183(Exception exception) {
                this.val$e = exception;
            }

            public final void run() {
                VerifyAccountActivity.this.isRunning = false;
                VerifyAccountActivity.this.showErrorPopup(this.val$e);
            }
        }

        C09195(String str, String str2) {
            this.val$phoneNumber = str;
            this.val$password = str2;
        }

        public final void onFailure(Request request, IOException e) {
            VerifyAccountActivity.this.runOnUiThread(new C09161(e));
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String stringResponse = response.body().string();
                    response.body().close();
                    System.out.println(stringResponse);
                    VerifyAccountActivity.this.runOnUiThread(new C09172(new JSONObject(stringResponse)));
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                VerifyAccountActivity.this.runOnUiThread(new C09183(e));
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.shamchat.activity.VerifyAccountActivity.6 */
    class C09236 implements Callback {
        final /* synthetic */ String val$password;
        final /* synthetic */ String val$phoneNumber;

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.6.1 */
        class C09201 implements Runnable {
            final /* synthetic */ IOException val$e;

            C09201(IOException iOException) {
                this.val$e = iOException;
            }

            public final void run() {
                VerifyAccountActivity.this.isRunning = false;
                VerifyAccountActivity.this.showErrorPopup(this.val$e);
            }
        }

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.6.2 */
        class C09212 implements Runnable {
            final /* synthetic */ JSONObject val$jsonResponse;

            C09212(JSONObject jSONObject) {
                this.val$jsonResponse = jSONObject;
            }

            public final void run() {
                try {
                    String status = this.val$jsonResponse.getString(NotificationCompatApi21.CATEGORY_STATUS);
                    if (status.equals("success")) {
                        User user = new User(this.val$jsonResponse.getJSONObject("data").getJSONObject("user"));
                        user.chatId = C09236.this.val$phoneNumber;
                        user.username = C09236.this.val$phoneNumber;
                        VerifyAccountActivity.this.updateUserInformation(user, user.userId, user.chatId, user.username, C09236.this.val$password, user.profileImage, user.profileImageUrl);
                        return;
                    }
                    Toast.makeText(VerifyAccountActivity.this, status, 1).show();
                    VerifyAccountActivity.this.hideProgressBar();
                    VerifyAccountActivity.this.isRunning = false;
                } catch (Exception e) {
                    VerifyAccountActivity.this.isRunning = false;
                    VerifyAccountActivity.this.showErrorPopup(e);
                }
            }
        }

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.6.3 */
        class C09223 implements Runnable {
            final /* synthetic */ Exception val$e;

            C09223(Exception exception) {
                this.val$e = exception;
            }

            public final void run() {
                VerifyAccountActivity.this.isRunning = false;
                VerifyAccountActivity.this.showErrorPopup(this.val$e);
            }
        }

        C09236(String str, String str2) {
            this.val$phoneNumber = str;
            this.val$password = str2;
        }

        public final void onFailure(Request request, IOException e) {
            VerifyAccountActivity.this.runOnUiThread(new C09201(e));
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String stringResponse = response.body().string();
                    response.body().close();
                    System.out.println(stringResponse);
                    VerifyAccountActivity.this.runOnUiThread(new C09212(new JSONObject(stringResponse)));
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                VerifyAccountActivity.this.runOnUiThread(new C09223(e));
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.shamchat.activity.VerifyAccountActivity.7 */
    class C09277 implements Callback {
        final /* synthetic */ String val$password;
        final /* synthetic */ String val$profileImageUrl;
        final /* synthetic */ String val$shamID;
        final /* synthetic */ User val$user;
        final /* synthetic */ String val$userID;
        final /* synthetic */ String val$userName;

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.7.1 */
        class C09241 implements Runnable {
            final /* synthetic */ IOException val$e;

            C09241(IOException iOException) {
                this.val$e = iOException;
            }

            public final void run() {
                VerifyAccountActivity.this.isRunning = false;
                VerifyAccountActivity.this.showErrorPopup(this.val$e);
            }
        }

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.7.2 */
        class C09252 implements Runnable {
            final /* synthetic */ JSONObject val$jsonResponse;

            C09252(JSONObject jSONObject) {
                this.val$jsonResponse = jSONObject;
            }

            public final void run() {
                try {
                    String status = this.val$jsonResponse.getString(NotificationCompatApi21.CATEGORY_STATUS);
                    Log.d("UpdateUserInformation status", "UpdateUserInformation: " + status);
                    if (status.equals("success")) {
                        ContentValues values = new ContentValues();
                        values.put("userId", C09277.this.val$userID);
                        values.put("chatId", C09277.this.val$shamID);
                        values.put("name", C09277.this.val$userName);
                        values.put("mobileNo", VerifyAccountActivity.this.phoneNumber);
                        if (C09277.this.val$profileImageUrl != null) {
                            values.put("profileimage_url", C09277.this.val$profileImageUrl);
                        }
                        VerifyAccountActivity.this.getContentResolver().delete(UserProvider.CONTENT_URI_USER, null, null);
                        VerifyAccountActivity.this.getContentResolver().insert(UserProvider.CONTENT_URI_USER, values);
                        VerifyAccountActivity.this.getContentResolver().delete(UserProvider.CONTENT_URI_NOTIFICATION, null, null);
                        ContentValues vals = new ContentValues();
                        vals.put(ChatActivity.INTENT_EXTRA_USER_ID, C09277.this.val$userID);
                        VerifyAccountActivity.this.getContentResolver().insert(UserProvider.CONTENT_URI_NOTIFICATION, vals);
                        Editor editor = PreferenceManager.getDefaultSharedPreferences(VerifyAccountActivity.this).edit();
                        String mWithJabberID = C09277.this.val$userID + "@rabtcdn.com";
                        Intent i = new Intent(VerifyAccountActivity.this, MainWindow.class);
                        if (VerifyAccountActivity.this.previousVerificationCode == null || VerifyAccountActivity.this.previousVerificationCode.length() <= 0) {
                            i.putExtra("register", true);
                            editor.putString("account_jabberPW", C09277.this.val$password);
                        } else {
                            i.putExtra("register", false);
                            editor.putString("account_jabberPW", VerifyAccountActivity.this.previousVerificationCode);
                            editor.putBoolean(PreferenceConstants.IS_OLD_USER, true);
                        }
                        editor.putBoolean("carbons", false);
                        editor.putBoolean("require_ssl", false);
                        editor.putString("account_jabberID", mWithJabberID);
                        editor.putString("current_user_id", C09277.this.val$userID);
                        editor.putString("user_password", C09277.this.val$password);
                        editor.putString("registration_status", "r_v_l_i");
                        editor.putString("user_mobileNo", VerifyAccountActivity.this.phoneNumber);
                        editor.commit();
                        VerifyAccountActivity.this.jobManager.addJobInBackground(new UpdatePhoneContactsDBJob());
                        i.setFlags(339738624);
                        if (VerifyAccountActivity.this.CheckNeeded) {
                            VerifyAccountActivity.this.registerOnSocial(i, C09277.this.val$user, VerifyAccountActivity.this.wisUsername, C09277.this.val$user.username, C09277.this.val$user.userId, C09277.this.val$password);
                            return;
                        }
                        String generatedEmail = VerifyAccountActivity.this.phoneNumber + "@" + VerifyAccountActivity.this.phoneNumber + ".com";
                        String URL = "http://sync.rabtcdn.com/testserver/passM.php?userId=" + C09277.this.val$user.userId + "&userPass=" + C09277.this.val$password;
                        OkHttpClient client2 = new OkHttpClient();
                        client2.setConnectTimeout(120, TimeUnit.SECONDS);
                        client2.setReadTimeout(120, TimeUnit.SECONDS);
                        Response response2 = client2.newCall(new Builder().url(URL).build()).execute();
                        if (response2.isSuccessful()) {
                            String stringResponse2 = response2.body().string();
                            System.out.println(stringResponse2);
                            if (new JSONObject(stringResponse2).getString(NotificationCompatApi21.CATEGORY_STATUS).equals("success")) {
                                VerifyAccountActivity.this.logintoSocial(i, C09277.this.val$user, VerifyAccountActivity.this.wisUsername, C09277.this.val$user.username, C09277.this.val$user.userId, C09277.this.val$password, generatedEmail);
                                return;
                            }
                            VerifyAccountActivity.this.isRunning = false;
                            VerifyAccountActivity.this.showErrorPopup(new Exception("updateUserInformation status not ok"));
                            return;
                        }
                        throw new IOException("Unexpected code " + response2);
                    }
                } catch (Exception e) {
                    VerifyAccountActivity.this.isRunning = false;
                    VerifyAccountActivity.this.showErrorPopup(e);
                }
            }
        }

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.7.3 */
        class C09263 implements Runnable {
            final /* synthetic */ Exception val$e;

            C09263(Exception exception) {
                this.val$e = exception;
            }

            public final void run() {
                VerifyAccountActivity.this.isRunning = false;
                VerifyAccountActivity.this.showErrorPopup(this.val$e);
            }
        }

        C09277(String str, String str2, String str3, String str4, String str5, User user) {
            this.val$userID = str;
            this.val$shamID = str2;
            this.val$userName = str3;
            this.val$profileImageUrl = str4;
            this.val$password = str5;
            this.val$user = user;
        }

        public final void onFailure(Request request, IOException e) {
            VerifyAccountActivity.this.runOnUiThread(new C09241(e));
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String stringResponse = response.body().string();
                    response.body().close();
                    System.out.println(stringResponse);
                    JSONObject jsonResponse = new JSONObject(stringResponse);
                    Log.d("response after updating fields", stringResponse);
                    VerifyAccountActivity.this.runOnUiThread(new C09252(jsonResponse));
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                VerifyAccountActivity.this.runOnUiThread(new C09263(e));
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.shamchat.activity.VerifyAccountActivity.8 */
    class C09288 implements OnClickListener {
        C09288() {
        }

        public final void onClick(View v) {
            VerifyAccountActivity.this.popUp.dismiss();
        }
    }

    /* renamed from: com.shamchat.activity.VerifyAccountActivity.9 */
    class C09329 implements Callback {

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.9.1 */
        class C09291 implements Runnable {
            final /* synthetic */ IOException val$e;

            C09291(IOException iOException) {
                this.val$e = iOException;
            }

            public final void run() {
                VerifyAccountActivity.this.showErrorPopup(this.val$e);
                VerifyAccountActivity.this.hideProgressBar();
                VerifyAccountActivity.this.isRunning = false;
            }
        }

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.9.2 */
        class C09302 implements Runnable {
            C09302() {
            }

            public final void run() {
                if (VerifyAccountActivity.this.statusBoolean.booleanValue()) {
                    VerifyAccountActivity.this.verifyAccount(VerifyAccountActivity.this.phoneNumber, Utils.convertToEnglishDigits(VerifyAccountActivity.this.editText.getText().toString().trim()));
                    return;
                }
                VerifyAccountActivity.this.hideProgressBar();
                VerifyAccountActivity.this.isRunning = false;
                VerifyAccountActivity.this.editTextUser.setText(BuildConfig.VERSION_NAME);
                VerifyAccountActivity.this.editTextUser.setHint("Username has been taken, Try another");
                VerifyAccountActivity.this.editTextUser.setHintTextColor(SupportMenu.CATEGORY_MASK);
            }
        }

        /* renamed from: com.shamchat.activity.VerifyAccountActivity.9.3 */
        class C09313 implements Runnable {
            final /* synthetic */ Exception val$e;

            C09313(Exception exception) {
                this.val$e = exception;
            }

            public final void run() {
                VerifyAccountActivity.this.showErrorPopup(this.val$e);
                VerifyAccountActivity.this.hideProgressBar();
                VerifyAccountActivity.this.isRunning = false;
            }
        }

        C09329() {
        }

        public final void onFailure(Request request, IOException e) {
            VerifyAccountActivity.this.runOnUiThread(new C09291(e));
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            VerifyAccountActivity.this.stringResponse = null;
            VerifyAccountActivity.this.serverResponseJsonObject = null;
            VerifyAccountActivity.this.status = null;
            VerifyAccountActivity.this.statusBoolean = null;
            try {
                if (response.isSuccessful()) {
                    VerifyAccountActivity.this.stringResponse = response.body().string();
                    response.body().close();
                    System.out.println(VerifyAccountActivity.this.stringResponse);
                    VerifyAccountActivity.this.jsonResponse = new JSONObject(VerifyAccountActivity.this.stringResponse);
                    VerifyAccountActivity.this.statusBoolean = Boolean.valueOf(VerifyAccountActivity.this.jsonResponse.getBoolean(NotificationCompatApi21.CATEGORY_STATUS));
                    VerifyAccountActivity.this.runOnUiThread(new C09302());
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                VerifyAccountActivity.this.runOnUiThread(new C09313(e));
                e.printStackTrace();
            }
        }
    }

    public VerifyAccountActivity() {
        this.isRunning = false;
        this.previousVerificationCode = null;
        this.CheckNeeded = true;
    }

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        getWindow().addFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        getWindow().addFlags(AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new ThreadPolicy.Builder().permitAll().build());
        this.mContext = this;
        this.Session = new RokhPref(this);
        this.Username = this.Session.getUsername();
        setContentView(2130903104);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        Bundle b = getIntent().getExtras();
        if (b != null) {
            String[] arr = b.getStringArray("user_mobileNo");
            if (arr != null) {
                this.phoneNumber = arr[0];
            }
        }
        if (this.phoneNumber == null) {
            this.phoneNumber = b.getStringArray("user_mobileNo")[0];
        }
        this.infoText = (TextView) findViewById(2131362051);
        new Handler().postDelayed(new C09061(), 2000);
        this.buttonContinue = (Button) findViewById(2131362091);
        this.buttonResend = (Button) findViewById(2131362090);
        this.buttonResend.setOnClickListener(this);
        this.editText = (EditText) findViewById(2131362089);
        this.editTextUser = (EditText) findViewById(2131362088);
        if (!(this.Username == null || this.Username.equals(BuildConfig.VERSION_NAME))) {
            this.editTextUser.setVisibility(8);
            this.wisUsername = this.Username;
            this.CheckNeeded = false;
        }
        this.buttonContinue.setOnClickListener(new C09072());
    }

    public void onResume() {
        super.onResume();
        try {
            EventBus.getDefault().register(this, true, 0);
            if (this.isRunning) {
                showProgressBar();
            }
        } catch (Throwable th) {
        }
    }

    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        hideProgressBar();
    }

    private void verifyAccount(String phoneNumber, String verificationCode) {
        this.isRunning = true;
        try {
            String URL = getResources().getString(2131493167) + "verifyAccount.htm?mobileNo=" + URLEncoder.encode(phoneNumber, "UTF-8") + "&verificationCode=" + verificationCode;
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(600, TimeUnit.SECONDS);
            client.setReadTimeout(600, TimeUnit.SECONDS);
            client.newCall(new Builder().url(URL).build()).enqueue(new C09113(phoneNumber, verificationCode));
        } catch (Exception e) {
            Toast.makeText(this, getResources().getString(2131492929), 1).show();
            this.isRunning = false;
            hideProgressBar();
            e.printStackTrace();
        }
    }

    public boolean hideProgressBar() {
        try {
            ProgressBarDialogLogin.getInstance().dismiss();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean showProgressBar() {
        try {
            ProgressBarDialogLogin.getInstance().show(getSupportFragmentManager(), BuildConfig.VERSION_NAME);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void onClick(View v) {
        if (showProgressBar()) {
            this.isRunning = true;
            resendVerficationCode();
        }
    }

    private void resendVerficationCode() {
        try {
            String URL = getResources().getString(2131493167) + "getVerificationCode.htm?mobileNo=" + URLEncoder.encode(this.phoneNumber, "UTF-8");
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(120, TimeUnit.SECONDS);
            client.setReadTimeout(120, TimeUnit.SECONDS);
            client.newCall(new Builder().url(URL).build()).enqueue(new C09154());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), 2131493293, 0).show();
            hideProgressBar();
            this.isRunning = false;
        }
    }

    private void assignPasswordToUser(String phoneNumber, String password) {
        try {
            String encodedData = URLEncoder.encode(phoneNumber, "UTF-8");
            String URL = getResources().getString(2131493167) + "assignedPasswordToUser.htm?mobileNo=" + encodedData + "&password=" + URLEncoder.encode(password, "UTF-8");
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(180, TimeUnit.SECONDS);
            client.setReadTimeout(180, TimeUnit.SECONDS);
            client.newCall(new Builder().url(URL).build()).enqueue(new C09195(phoneNumber, password));
        } catch (Exception e) {
            this.isRunning = false;
            showErrorPopup(e);
            e.printStackTrace();
        }
    }

    private void loginWithMobileNo(String phoneNumber, String password) {
        try {
            String URL = getResources().getString(2131493167) + "loginWithMobileNo.htm?mobileNo=" + URLEncoder.encode(phoneNumber, "UTF-8") + "&password=" + password;
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(240, TimeUnit.SECONDS);
            client.setReadTimeout(240, TimeUnit.SECONDS);
            client.newCall(new Builder().url(URL).build()).enqueue(new C09236(phoneNumber, password));
        } catch (Exception e) {
            this.isRunning = false;
            showErrorPopup(e);
        }
    }

    private void updateUserInformation(User user, String userID, String shamID, String userName, String password, String profileImage, String profileImageUrl) {
        JSONObject main = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject arrayObject = new JSONObject();
        try {
            arrayObject.put("chatId", shamID);
            arrayObject.put(NotificationCompatApi21.CATEGORY_EMAIL, BuildConfig.VERSION_NAME);
            arrayObject.put("userName", userName);
            arrayObject.put("mobileNo", this.phoneNumber);
            main.put("userId", userID);
            array.put(arrayObject);
            main.put("dataFields", array);
            String URL = getResources().getString(2131493167) + "updateUserDataField.htm";
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(300, TimeUnit.SECONDS);
            client.setReadTimeout(300, TimeUnit.SECONDS);
            Call newCall = client.newCall(new Builder().url(URL).post(new FormEncodingBuilder().add("userDataFiledJson", main.toString()).build()).build());
            r18.enqueue(new C09277(userID, shamID, userName, profileImageUrl, password, user));
        } catch (Exception e) {
            this.isRunning = false;
            showErrorPopup(e);
        }
    }

    private void showErrorPopup(Exception e) {
        PopUpUtil popUpUtil = new PopUpUtil();
        hideProgressBar();
        this.isRunning = false;
        getResources().getString(2131493124);
        this.popUp = PopUpUtil.getPopFailed$478dbc03(this, getResources().getString(2131493073), new C09288());
        this.popUp.show();
        Toast.makeText(this, e.getMessage(), 1).show();
    }

    private void checkUsernameAvailability(String username) {
        String URL = "http://social.rabtcdn.com/api/v4/user/check/username/";
        try {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(120, TimeUnit.SECONDS);
            client.setReadTimeout(120, TimeUnit.SECONDS);
            client.newCall(new Builder().url(URL).post(new FormEncodingBuilder().add("username", username).build()).build()).enqueue(new C09329());
        } catch (Exception e) {
            this.isRunning = false;
            showErrorPopup(e);
            e.printStackTrace();
        }
    }

    private void registerOnSocial(Intent i, User user, String username, String phoneNumber, String chatId, String password) {
        String email = phoneNumber + "@" + phoneNumber + ".com";
        JSONObject registerParams = new JSONObject();
        try {
            registerParams.put("token", "e622c330c77a17c8426e638d7a85da6c2ec9f455");
            registerParams.put("username", username);
            registerParams.put("password", password);
            registerParams.put(NotificationCompatApi21.CATEGORY_EMAIL, email);
            Log.e("****************** REGISTER JSON", registerParams.toString());
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(120, TimeUnit.SECONDS);
            client.setReadTimeout(120, TimeUnit.SECONDS);
            Call newCall = client.newCall(new Builder().url("http://social.rabtcdn.com/api/user/register/").post(RequestBody.create(JSON, registerParams.toString())).build());
            r17.enqueue(new AnonymousClass10(i, user, username, phoneNumber, chatId, password, email));
        } catch (Exception e) {
            this.isRunning = false;
            showErrorPopup(e);
            e.printStackTrace();
        }
    }

    private void logintoSocial(Intent i, User user, String username, String phoneNumber, String chatIf, String password, String email) {
        JSONObject loginParams = new JSONObject();
        try {
            loginParams.put("token", "e622c330c77a17c8426e638d7a85da6c2ec9f455");
            loginParams.put("username", username);
            loginParams.put("password", password);
            loginParams.put(NotificationCompatApi21.CATEGORY_EMAIL, email);
            Log.e("****************** LOGIN JSON", loginParams.toString());
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(120, TimeUnit.SECONDS);
            client.setReadTimeout(120, TimeUnit.SECONDS);
            client.newCall(new Builder().url("http://social.rabtcdn.com/api/user/login/").post(RequestBody.create(JSON, loginParams.toString())).build()).enqueue(new AnonymousClass11(password, i, user));
        } catch (Exception e) {
            this.isRunning = false;
            showErrorPopup(e);
            e.printStackTrace();
        }
    }

    private void postSaveUserData(Intent i, User user, String password, String token, String userId) {
        String URL = "http://social.rabtcdn.com/api/v4/user/salam/change/profile/jid/?token=" + token + "&jid=" + new BigDecimal(user.userId).toString();
        Log.e("****************** GET USER DATA URL", URL);
        try {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(120, TimeUnit.SECONDS);
            client.setReadTimeout(120, TimeUnit.SECONDS);
            client.newCall(new Builder().url(URL).build()).enqueue(new AnonymousClass12(token, userId, i));
        } catch (Exception e) {
            this.isRunning = false;
            showErrorPopup(e);
        }
    }
}

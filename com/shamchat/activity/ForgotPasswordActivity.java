package com.shamchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.shamchat.utils.Utils;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import org.json.JSONObject;

public class ForgotPasswordActivity extends Activity {
    private EditText ConfirmPass;
    private EditText newPass;
    SharedPreferences preferences;
    private Button submit;

    /* renamed from: com.shamchat.activity.ForgotPasswordActivity.1 */
    class C07611 implements OnClickListener {
        final /* synthetic */ String val$phoneNumber;

        C07611(String str) {
            this.val$phoneNumber = str;
        }

        public final void onClick(View v) {
            if (ForgotPasswordActivity.this.ConfirmPass.getText().toString().equalsIgnoreCase(BuildConfig.VERSION_NAME)) {
                Toast.makeText(ForgotPasswordActivity.this.getApplicationContext(), 2131493057, 0).show();
            } else if (ForgotPasswordActivity.this.newPass.getText().toString().equalsIgnoreCase(BuildConfig.VERSION_NAME)) {
                Toast.makeText(ForgotPasswordActivity.this.getApplicationContext(), 2131493057, 0).show();
            } else if (ForgotPasswordActivity.this.ConfirmPass.getText().toString().equalsIgnoreCase(ForgotPasswordActivity.this.newPass.getText().toString())) {
                new AnonymousClass1HttpAsyncTask(Utils.convertToEnglishDigits(ForgotPasswordActivity.this.newPass.getText().toString())).execute(new String[]{this.val$phoneNumber, Utils.convertToEnglishDigits(ForgotPasswordActivity.this.newPass.getText().toString())});
            } else {
                Toast.makeText(ForgotPasswordActivity.this.getApplicationContext(), 2131493255, 0).show();
            }
        }
    }

    /* renamed from: com.shamchat.activity.ForgotPasswordActivity.1HttpAsyncTask */
    class AnonymousClass1HttpAsyncTask extends AsyncTask<String, String, String> {
        final /* synthetic */ String val$password;

        AnonymousClass1HttpAsyncTask(String str) {
            this.val$password = str;
        }

        protected final /* bridge */ /* synthetic */ void onPostExecute(Object obj) {
            String str = (String) obj;
            super.onPostExecute(str);
            if (str != null) {
                try {
                    String string = new JSONObject(str).getString(NotificationCompatApi21.CATEGORY_STATUS);
                    Log.d(NotificationCompatApi21.CATEGORY_STATUS, string);
                    if (string.equals("success")) {
                        Editor edit = ForgotPasswordActivity.this.preferences.edit();
                        edit.putString("user_password", this.val$password);
                        edit.putString("account_jabberPW", this.val$password);
                        edit.commit();
                        Toast.makeText(ForgotPasswordActivity.this.getApplicationContext(), 2131493251, 0).show();
                        ForgotPasswordActivity.this.startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                        ForgotPasswordActivity.this.finish();
                    } else if (!string.equals("user_not_verified")) {
                        string.equals("user_not_exists");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        protected final void onPreExecute() {
            super.onPreExecute();
        }

        private String doInBackground(String... params) {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            try {
                InputStream inputStream = httpclient.execute((HttpUriRequest) new HttpGet(ForgotPasswordActivity.this.getResources().getString(2131493167) + "changePassword.htm?mobileNo=" + URLEncoder.encode(params[0], "UTF-8") + "&password=" + params[1])).getEntity().getContent();
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

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        getWindow().addFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        setContentView(2130903094);
        this.newPass = (EditText) findViewById(2131362052);
        this.ConfirmPass = (EditText) findViewById(2131362054);
        this.submit = (Button) findViewById(2131362056);
        String phoneNumber = getIntent().getExtras().getString("user_mobileNo");
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.submit.setOnClickListener(new C07611(phoneNumber));
        super.onCreate(savedInstanceState);
    }
}

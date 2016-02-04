package com.shamchat.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.preference.PreferenceManager;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.path.android.jobqueue.JobManager;
import com.rokhgroup.utils.RokhPref;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.util.PreferenceConstants;
import com.shamchat.events.RegisterServerResponseFailed;
import com.shamchat.events.RegisterServerResponseSuccess;
import com.shamchat.jobs.RegisterPhoneJob;
import com.shamchat.utils.PopUpUtil;
import com.shamchat.utils.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import de.greenrobot.event.EventBus;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.json.JSONObject;

public class RegisterPhoneActivity extends AppCompatActivity implements OnItemSelectedListener {
    private RokhPref Session;
    private String currentSelectedCountryCode;
    private int currentSelectedPos;
    private EditText editTextMobileNo;
    private boolean forgotPassword;
    private boolean isRunning;
    private JobManager jobManager;
    private Dialog popUp;
    private SharedPreferences preferences;

    /* renamed from: com.shamchat.activity.RegisterPhoneActivity.1 */
    class C08551 implements OnClickListener {

        /* renamed from: com.shamchat.activity.RegisterPhoneActivity.1.1 */
        class C08541 implements OnClickListener {
            C08541() {
            }

            public final void onClick(View v) {
                RegisterPhoneActivity.this.popUp.dismiss();
            }
        }

        C08551() {
        }

        public final void onClick(View v) {
            PopUpUtil popUpUtil = new PopUpUtil();
            String phoneNumber = RegisterPhoneActivity.this.editTextMobileNo.getText().toString().trim();
            if (phoneNumber.length() == 0) {
                RegisterPhoneActivity.this.editTextMobileNo.setText(BuildConfig.VERSION_NAME);
                RegisterPhoneActivity.this.editTextMobileNo.setHint(RegisterPhoneActivity.this.getResources().getString(2131493116));
                RegisterPhoneActivity.this.editTextMobileNo.setHintTextColor(SupportMenu.CATEGORY_MASK);
            } else if (phoneNumber.length() == 10 || ((phoneNumber.substring(0, 1).equals("0") || phoneNumber.substring(0, 1).equals("\u0660")) && phoneNumber.length() == 11)) {
                if (phoneNumber.substring(0, 1).equals("0") || phoneNumber.substring(0, 1).equals("\u0660")) {
                    phoneNumber = phoneNumber.trim().substring(1);
                }
                if (RegisterPhoneActivity.this.currentSelectedCountryCode == null) {
                    RegisterPhoneActivity.this.currentSelectedCountryCode = BuildConfig.VERSION_NAME;
                }
                if (Utils.isInternetAvailable(RegisterPhoneActivity.this.getApplicationContext())) {
                    RegisterPhoneActivity.this.showProgressBar();
                    RegisterPhoneActivity.this.jobManager.addJobInBackground(new RegisterPhoneJob(new StringBuilder(MqttTopic.SINGLE_LEVEL_WILDCARD).append(Utils.convertToEnglishDigits(RegisterPhoneActivity.this.currentSelectedCountryCode + phoneNumber)).toString()));
                    return;
                }
                RegisterPhoneActivity registerPhoneActivity = RegisterPhoneActivity.this;
                Context context = RegisterPhoneActivity.this;
                RegisterPhoneActivity.this.getResources().getString(2131493124);
                registerPhoneActivity.popUp = PopUpUtil.getPopFailed$478dbc03(context, RegisterPhoneActivity.this.getResources().getString(2131493226), new C08541());
                RegisterPhoneActivity.this.popUp.show();
            } else {
                RegisterPhoneActivity.this.editTextMobileNo.setText(BuildConfig.VERSION_NAME);
                RegisterPhoneActivity.this.editTextMobileNo.setHint(2131493256);
                RegisterPhoneActivity.this.editTextMobileNo.setHintTextColor(SupportMenu.CATEGORY_MASK);
            }
        }
    }

    /* renamed from: com.shamchat.activity.RegisterPhoneActivity.2 */
    class C08592 implements Callback {
        final /* synthetic */ String val$phoneNumber;

        /* renamed from: com.shamchat.activity.RegisterPhoneActivity.2.1 */
        class C08561 implements Runnable {
            C08561() {
            }

            public final void run() {
                Toast.makeText(RegisterPhoneActivity.this.getApplicationContext(), 2131493314, 0).show();
                RegisterPhoneActivity.this.hideProgressBar();
                RegisterPhoneActivity.this.isRunning = false;
            }
        }

        /* renamed from: com.shamchat.activity.RegisterPhoneActivity.2.2 */
        class C08572 implements Runnable {
            C08572() {
            }

            public final void run() {
                RegisterPhoneActivity.this.hideProgressBar();
                RegisterPhoneActivity.this.isRunning = false;
                Editor editor = RegisterPhoneActivity.this.preferences.edit();
                editor.putString("user_mobileNo", C08592.this.val$phoneNumber);
                editor.commit();
                Intent i = new Intent(RegisterPhoneActivity.this, VerifyAccountActivity.class);
                Bundle b = new Bundle();
                b.putStringArray("user_mobileNo", new String[]{C08592.this.val$phoneNumber});
                i.putExtras(b);
                i.setFlags(67108864);
                RegisterPhoneActivity.this.startActivity(i);
                RegisterPhoneActivity.this.finish();
            }
        }

        /* renamed from: com.shamchat.activity.RegisterPhoneActivity.2.3 */
        class C08583 implements Runnable {
            C08583() {
            }

            public final void run() {
                Toast.makeText(RegisterPhoneActivity.this.getApplicationContext(), 2131493314, 0).show();
                RegisterPhoneActivity.this.hideProgressBar();
                RegisterPhoneActivity.this.isRunning = false;
            }
        }

        C08592(String str) {
            this.val$phoneNumber = str;
        }

        public final void onFailure(Request request, IOException e) {
            RegisterPhoneActivity.this.runOnUiThread(new C08561());
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String stringResponse = response.body().string();
                    response.body().close();
                    System.out.println(stringResponse);
                    JSONObject jsonResponse = new JSONObject(stringResponse);
                    if (jsonResponse.getBoolean(NotificationCompatApi21.CATEGORY_STATUS)) {
                        String Username = jsonResponse.getString("username");
                        RokhPref access$500 = RegisterPhoneActivity.this.Session;
                        access$500.editor.putString("username", Username);
                        access$500.editor.commit();
                    }
                    RegisterPhoneActivity.this.runOnUiThread(new C08572());
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                RegisterPhoneActivity.this.runOnUiThread(new C08583());
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.shamchat.activity.RegisterPhoneActivity.3 */
    class C08603 implements OnClickListener {
        C08603() {
        }

        public final void onClick(View v) {
            RegisterPhoneActivity.this.popUp.dismiss();
        }
    }

    public RegisterPhoneActivity() {
        this.isRunning = false;
    }

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        getWindow().addFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        getWindow().addFlags(AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new Builder().permitAll().build());
        setContentView(2130903099);
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        try {
            EventBus.getDefault().register(this, true, 9);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.Session = new RokhPref(this);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey(PreferenceConstants.FORGOT_PASSWORD)) {
            this.forgotPassword = b.getBoolean(PreferenceConstants.FORGOT_PASSWORD, false);
        }
        ((Button) findViewById(2131362080)).setOnClickListener(new C08551());
        Spinner spinner = (Spinner) findViewById(2131362074);
        spinner.setOnItemSelectedListener(this);
        this.editTextMobileNo = (EditText) findViewById(2131362078);
        if (this.forgotPassword) {
            this.editTextMobileNo.setHint(2131493146);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(this, 17367048, getCountriesWithCodes());
        adapter.setDropDownViewResource(17367049);
        spinner.setAdapter(adapter);
        spinner.setSelection(this.currentSelectedPos);
    }

    private List<String> getCountriesWithCodes() {
        String countryID;
        List<String> myCountryList = new ArrayList();
        String[] rl = getResources().getStringArray(2131099648);
        if (Boolean.valueOf(isSimAvailable()).booleanValue()) {
            countryID = ((TelephonyManager) getSystemService("phone")).getSimCountryIso().toUpperCase();
        } else {
            countryID = "IR";
        }
        for (int i = 1; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            String countryNameWithCode = "(+" + g[0] + ") " + new Locale(BuildConfig.VERSION_NAME, g[1]).getDisplayCountry();
            if (g[1].equalsIgnoreCase(countryID.trim())) {
                this.currentSelectedPos = i - 1;
            }
            myCountryList.add(countryNameWithCode);
        }
        return myCountryList;
    }

    public void onItemSelected(AdapterView<?> adapterView, View arg1, int index, long arg3) {
        try {
            TextView txtSelectedCountry = (TextView) arg1;
            if (txtSelectedCountry != null && txtSelectedCountry.getText() != null && txtSelectedCountry.getText().length() > 2) {
                System.out.println("code " + txtSelectedCountry.getText());
                txtSelectedCountry.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                String countryCode = txtSelectedCountry.getText().toString().substring(2, txtSelectedCountry.getText().toString().lastIndexOf(")"));
                System.out.println("code " + countryCode);
                this.currentSelectedCountryCode = countryCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private boolean isSimAvailable() {
        switch (((TelephonyManager) getSystemService("phone")).getSimState()) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                return false;
            case Logger.SEVERE /*1*/:
                return false;
            case Logger.CONFIG /*4*/:
                return false;
            case Logger.FINE /*5*/:
                return true;
            default:
                return false;
        }
    }

    protected void onResume() {
        super.onResume();
        try {
            if (this.isRunning) {
                showProgressBar();
            }
        } catch (Exception e) {
        }
    }

    protected void onPause() {
        super.onPause();
        hideProgressBar();
    }

    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEventMainThread(RegisterServerResponseSuccess event) {
        try {
            JSONObject serverResponseJsonObject = event.serverResponseJsonObject;
            String status = event.status;
            String phoneNumber = event.phoneNumber;
            String verification_code = serverResponseJsonObject.getString("verification_code");
            Toast.makeText(SHAMChatApplication.getMyApplicationContext(), verification_code, 1).show();
            Editor editor;
            Intent i;
            Bundle b;
            String str;
            if (status.equals("success")) {
                hideProgressBar();
                this.isRunning = false;
                editor = this.preferences.edit();
                editor.putString("registration_status", "r_n_v");
                editor.putString("user_mobileNo", phoneNumber);
                editor.commit();
                i = new Intent(this, VerifyAccountActivity.class);
                b = new Bundle();
                str = "user_mobileNo";
                b.putStringArray(r16, new String[]{phoneNumber});
                b.putBoolean("Forgot", false);
                i.putExtras(b);
                i.setFlags(67108864);
                startActivity(i);
                finish();
                return;
            }
            if (status.equals("already_registered_and_verified")) {
                String Rokhgroupemail = phoneNumber + "@" + phoneNumber + ".com";
                String URL = "http://social.rabtcdn.com/api/v4/user/salam/is/user/registered/";
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody formBody = new FormEncodingBuilder().add(NotificationCompatApi21.CATEGORY_EMAIL, Rokhgroupemail).build();
                    client.newCall(new Request.Builder().url(URL).post(formBody).build()).enqueue(new C08592(phoneNumber));
                    return;
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), 2131493314, 0).show();
                    hideProgressBar();
                    this.isRunning = false;
                    return;
                }
            }
            if (status.equals("incorrect_mobile_no")) {
                this.editTextMobileNo.setText(BuildConfig.VERSION_NAME);
                this.editTextMobileNo.setHint(getString(2131493172));
                this.editTextMobileNo.setHintTextColor(SupportMenu.CATEGORY_MASK);
                hideProgressBar();
                this.isRunning = false;
                return;
            }
            if (status.equals(MqttServiceConstants.TRACE_EXCEPTION)) {
                Toast.makeText(getApplicationContext(), 2131493382, 0).show();
                hideProgressBar();
                this.isRunning = false;
                return;
            }
            if (status.equals("already_registered_and_not_verified")) {
                hideProgressBar();
                this.isRunning = false;
                editor = this.preferences.edit();
                editor.putString("registration_status", "r_n_v");
                editor.putString("user_mobileNo", phoneNumber);
                editor.commit();
                i = new Intent(this, VerifyAccountActivity.class);
                b = new Bundle();
                str = "user_mobileNo";
                b.putStringArray(r16, new String[]{phoneNumber});
                i.putExtras(b);
                i.setFlags(67108864);
                startActivity(i);
                finish();
            }
        } catch (Exception e1) {
            Toast.makeText(getApplicationContext(), 2131493314, 0).show();
            hideProgressBar();
            this.isRunning = false;
            e1.printStackTrace();
        }
    }

    public void onEventMainThread(RegisterServerResponseFailed event) {
        hideProgressBar();
        this.isRunning = false;
        PopUpUtil popUpUtil = new PopUpUtil();
        getResources().getString(2131493124);
        this.popUp = PopUpUtil.getPopFailed$478dbc03(this, getResources().getString(2131493073), new C08603());
        this.popUp.show();
    }

    public void hideProgressBar() {
        try {
            ProgressBarDialogLogin.getInstance().dismiss();
        } catch (Exception e) {
        }
    }

    public void showProgressBar() {
        try {
            ProgressBarDialogLogin.getInstance().show(getSupportFragmentManager(), BuildConfig.VERSION_NAME);
        } catch (Exception e) {
        }
    }
}

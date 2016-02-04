package com.shamchat.androidclient;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.shamchat.activity.MainWindow;
import com.shamchat.activity.RegisterPhoneActivity;
import com.shamchat.activity.VerifyAccountActivity;
import java.lang.Thread.UncaughtExceptionHandler;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "com.shamchat.androidclient.SplashActvity";
    private UncaughtExceptionHandler androidDefaultUEH;
    private UncaughtExceptionHandler handler;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sp;

    /* renamed from: com.shamchat.androidclient.SplashActivity.1 */
    class C10621 implements UncaughtExceptionHandler {
        C10621() {
        }

        public final void uncaughtException(Thread thread, Throwable ex) {
            Log.e("TestApplication", "Uncaught exception is: ", ex);
            SplashActivity.this.androidDefaultUEH.uncaughtException(thread, ex);
        }
    }

    /* renamed from: com.shamchat.androidclient.SplashActivity.2 */
    class C10652 implements Runnable {
        final /* synthetic */ Intent val$intent;
        final /* synthetic */ String val$intentAction;

        /* renamed from: com.shamchat.androidclient.SplashActivity.2.1 */
        class C10631 implements OnClickListener {
            final /* synthetic */ EditText val$newpass;
            final /* synthetic */ String val$pass;

            C10631(EditText editText, String str) {
                this.val$newpass = editText;
                this.val$pass = str;
            }

            public final void onClick(View v) {
                if (this.val$newpass.getText().toString().equals(this.val$pass)) {
                    Intent i = new Intent(SplashActivity.this, MainWindow.class);
                    if (C10652.this.val$intentAction != null) {
                        i = C10652.this.val$intent;
                        i.setClass(SplashActivity.this, MainWindow.class);
                    }
                    i.setFlags(536870912);
                    SplashActivity.this.startActivity(i);
                    SplashActivity.this.finish();
                    return;
                }
                Toast.makeText(SplashActivity.this.getApplicationContext(), SplashActivity.this.getResources().getString(2131493253), 0).show();
            }
        }

        /* renamed from: com.shamchat.androidclient.SplashActivity.2.2 */
        class C10642 implements OnClickListener {
            C10642() {
            }

            public final void onClick(View v) {
                SplashActivity.this.finish();
            }
        }

        C10652(String str, Intent intent) {
            this.val$intentAction = str;
            this.val$intent = intent;
        }

        public final void run() {
            String registrationStatus = SplashActivity.this.sharedPreferences.getString("registration_status", null);
            String userMobileNo = SplashActivity.this.sharedPreferences.getString("user_mobileNo", null);
            if (registrationStatus == null) {
                SplashActivity.this.setDefaulSettings();
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, RegisterPhoneActivity.class));
                SplashActivity.this.finish();
                Log.d(SplashActivity.TAG, "1st time execution");
            } else if (registrationStatus.equals("r_v_l_i")) {
                Log.d(SplashActivity.TAG, "User account status : REGISTERED_VERIFIED_LOGGED_IN ");
                SplashActivity.this.sp = SplashActivity.this.getApplicationContext().getSharedPreferences("setting", 0);
                String pass = SplashActivity.this.sp.getString("pass", BuildConfig.VERSION_NAME);
                if (pass.isEmpty()) {
                    i = new Intent(SplashActivity.this, MainWindow.class);
                    if (this.val$intentAction != null) {
                        i = this.val$intent;
                        i.setClass(SplashActivity.this, MainWindow.class);
                    }
                    i.setFlags(536870912);
                    SplashActivity.this.startActivity(i);
                    SplashActivity.this.finish();
                    return;
                }
                Dialog dialog = new Dialog(SplashActivity.this);
                dialog.requestWindowFeature(1);
                dialog.setContentView(2130903100);
                dialog.show();
                Button ok = (Button) dialog.findViewById(2131362083);
                Button cancel = (Button) dialog.findViewById(2131362084);
                ok.setOnClickListener(new C10631((EditText) dialog.findViewById(2131362082), pass));
                cancel.setOnClickListener(new C10642());
            } else if (registrationStatus.equals("r_v") && userMobileNo != null) {
                i = new Intent(SplashActivity.this, VerifyAccountActivity.class);
                i.setFlags(ClientDefaults.MAX_MSG_SIZE);
                i.putExtra("user_mobileNo", userMobileNo);
                SplashActivity.this.startActivity(i);
                SplashActivity.this.finish();
            } else if (!registrationStatus.equals("r_n_v") || userMobileNo == null) {
                i = new Intent(SplashActivity.this, RegisterPhoneActivity.class);
                i.setFlags(ClientDefaults.MAX_MSG_SIZE);
                SplashActivity.this.startActivity(i);
                SplashActivity.this.finish();
            } else {
                i = new Intent(SplashActivity.this, VerifyAccountActivity.class);
                Bundle b = new Bundle();
                b.putStringArray("user_mobileNo", new String[]{userMobileNo});
                b.putBoolean("Forgot", false);
                i.putExtras(b);
                SplashActivity.this.startActivity(i);
                SplashActivity.this.finish();
            }
        }
    }

    public SplashActivity() {
        this.handler = new C10621();
    }

    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, getString(2131492994));
        requestWindowFeature(1);
        getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        super.onCreate(savedInstanceState);
        this.androidDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this.handler);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Intent intent = getIntent();
        String intentAction = intent.getAction();
        intent.getType();
        if (isTaskRoot() || !intent.hasCategory("android.intent.category.LAUNCHER") || intentAction == null || !intentAction.equals("android.intent.action.MAIN")) {
            setContentView(2130903102);
            new Handler().postDelayed(new C10652(intentAction, intent), 1000);
            if (!Boolean.valueOf(isOnline()).booleanValue()) {
                Toast.makeText(getBaseContext(), 2131493335, 0).show();
                return;
            }
            return;
        }
        finish();
    }

    public void setDefaulSettings() {
        Editor editor = getApplicationContext().getSharedPreferences("setting", 0).edit();
        editor.putInt("background", 1);
        editor.putInt("notif_icon", 1);
        editor.putString("pass", BuildConfig.VERSION_NAME);
        editor.putInt("sound", 1);
        editor.putInt("font", 2);
        editor.putInt("sizee", 14);
        editor.putInt("shakee", 1);
        editor.commit();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    protected void onNewIntent(Intent i) {
        setIntent(i);
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    public boolean isOnline() {
        NetworkInfo netInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}

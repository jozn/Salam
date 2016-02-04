package com.shamchat.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.shamchat.adapters.SettingsItemsAdapter;
import com.shamchat.adapters.SettingsItemsAdapter.SettingListItem;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.ChatProviderNew.ChatDatabaseHelper;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.models.User;

public class SettingsActivity extends AppCompatActivity {
    int icnum;
    private ContentResolver mContentResolver;
    private User me;
    private TextView mobile;
    private int music;
    LinearLayout settingsItems;
    private SettingsItemsAdapter settingsListAdapter;
    private SharedPreferences sp;
    private TextView verc;

    /* renamed from: com.shamchat.activity.SettingsActivity.1 */
    class C08691 implements OnClickListener {

        /* renamed from: com.shamchat.activity.SettingsActivity.1.1 */
        class C08631 implements OnClickListener {
            final /* synthetic */ RadioButton val$rd1;
            final /* synthetic */ RadioButton val$rd2;
            final /* synthetic */ RadioButton val$rd3;
            final /* synthetic */ RadioButton val$rd4;
            final /* synthetic */ RadioButton val$rd5;

            C08631(RadioButton radioButton, RadioButton radioButton2, RadioButton radioButton3, RadioButton radioButton4, RadioButton radioButton5) {
                this.val$rd1 = radioButton;
                this.val$rd2 = radioButton2;
                this.val$rd3 = radioButton3;
                this.val$rd4 = radioButton4;
                this.val$rd5 = radioButton5;
            }

            public final void onClick(View v) {
                this.val$rd1.setChecked(true);
                this.val$rd2.setChecked(false);
                this.val$rd3.setChecked(false);
                this.val$rd4.setChecked(false);
                this.val$rd5.setChecked(false);
                MediaPlayer.create(SettingsActivity.this.getApplicationContext(), 2131034112).start();
                SettingsActivity.this.music = 1;
            }
        }

        /* renamed from: com.shamchat.activity.SettingsActivity.1.2 */
        class C08642 implements OnClickListener {
            final /* synthetic */ RadioButton val$rd1;
            final /* synthetic */ RadioButton val$rd2;
            final /* synthetic */ RadioButton val$rd3;
            final /* synthetic */ RadioButton val$rd4;
            final /* synthetic */ RadioButton val$rd5;

            C08642(RadioButton radioButton, RadioButton radioButton2, RadioButton radioButton3, RadioButton radioButton4, RadioButton radioButton5) {
                this.val$rd1 = radioButton;
                this.val$rd2 = radioButton2;
                this.val$rd3 = radioButton3;
                this.val$rd4 = radioButton4;
                this.val$rd5 = radioButton5;
            }

            public final void onClick(View v) {
                this.val$rd1.setChecked(false);
                this.val$rd2.setChecked(true);
                this.val$rd3.setChecked(false);
                this.val$rd4.setChecked(false);
                this.val$rd5.setChecked(false);
                MediaPlayer.create(SettingsActivity.this.getApplicationContext(), 2131034113).start();
                SettingsActivity.this.music = 2;
            }
        }

        /* renamed from: com.shamchat.activity.SettingsActivity.1.3 */
        class C08653 implements OnClickListener {
            final /* synthetic */ RadioButton val$rd1;
            final /* synthetic */ RadioButton val$rd2;
            final /* synthetic */ RadioButton val$rd3;
            final /* synthetic */ RadioButton val$rd4;
            final /* synthetic */ RadioButton val$rd5;

            C08653(RadioButton radioButton, RadioButton radioButton2, RadioButton radioButton3, RadioButton radioButton4, RadioButton radioButton5) {
                this.val$rd1 = radioButton;
                this.val$rd2 = radioButton2;
                this.val$rd3 = radioButton3;
                this.val$rd4 = radioButton4;
                this.val$rd5 = radioButton5;
            }

            public final void onClick(View v) {
                this.val$rd1.setChecked(false);
                this.val$rd2.setChecked(false);
                this.val$rd3.setChecked(true);
                this.val$rd4.setChecked(false);
                this.val$rd5.setChecked(false);
                MediaPlayer.create(SettingsActivity.this.getApplicationContext(), 2131034114).start();
                SettingsActivity.this.music = 3;
            }
        }

        /* renamed from: com.shamchat.activity.SettingsActivity.1.4 */
        class C08664 implements OnClickListener {
            final /* synthetic */ RadioButton val$rd1;
            final /* synthetic */ RadioButton val$rd2;
            final /* synthetic */ RadioButton val$rd3;
            final /* synthetic */ RadioButton val$rd4;
            final /* synthetic */ RadioButton val$rd5;

            C08664(RadioButton radioButton, RadioButton radioButton2, RadioButton radioButton3, RadioButton radioButton4, RadioButton radioButton5) {
                this.val$rd1 = radioButton;
                this.val$rd2 = radioButton2;
                this.val$rd3 = radioButton3;
                this.val$rd4 = radioButton4;
                this.val$rd5 = radioButton5;
            }

            public final void onClick(View v) {
                this.val$rd1.setChecked(false);
                this.val$rd2.setChecked(false);
                this.val$rd3.setChecked(false);
                this.val$rd4.setChecked(true);
                this.val$rd5.setChecked(false);
                MediaPlayer.create(SettingsActivity.this.getApplicationContext(), 2131034116).start();
                SettingsActivity.this.music = 4;
            }
        }

        /* renamed from: com.shamchat.activity.SettingsActivity.1.5 */
        class C08675 implements OnClickListener {
            final /* synthetic */ RadioButton val$rd1;
            final /* synthetic */ RadioButton val$rd2;
            final /* synthetic */ RadioButton val$rd3;
            final /* synthetic */ RadioButton val$rd4;
            final /* synthetic */ RadioButton val$rd5;

            C08675(RadioButton radioButton, RadioButton radioButton2, RadioButton radioButton3, RadioButton radioButton4, RadioButton radioButton5) {
                this.val$rd1 = radioButton;
                this.val$rd2 = radioButton2;
                this.val$rd3 = radioButton3;
                this.val$rd4 = radioButton4;
                this.val$rd5 = radioButton5;
            }

            public final void onClick(View v) {
                this.val$rd1.setChecked(false);
                this.val$rd2.setChecked(false);
                this.val$rd3.setChecked(false);
                this.val$rd4.setChecked(false);
                this.val$rd5.setChecked(true);
                MediaPlayer.create(SettingsActivity.this.getApplicationContext(), 2131034118).start();
                SettingsActivity.this.music = 5;
            }
        }

        /* renamed from: com.shamchat.activity.SettingsActivity.1.6 */
        class C08686 implements OnClickListener {
            final /* synthetic */ Dialog val$dialog;

            C08686(Dialog dialog) {
                this.val$dialog = dialog;
            }

            public final void onClick(View v) {
                SettingsActivity.this.sp = SettingsActivity.this.getApplicationContext().getSharedPreferences("setting", 0);
                Editor editor = SettingsActivity.this.sp.edit();
                editor.putInt("sound", SettingsActivity.this.music);
                editor.commit();
                this.val$dialog.cancel();
            }
        }

        C08691() {
        }

        public final void onClick(View v) {
            Dialog dialog = new Dialog(SettingsActivity.this);
            dialog.requestWindowFeature(1);
            dialog.setContentView(2130903230);
            dialog.show();
            LinearLayout sou2 = (LinearLayout) dialog.findViewById(2131362525);
            LinearLayout sou3 = (LinearLayout) dialog.findViewById(2131362527);
            LinearLayout sou4 = (LinearLayout) dialog.findViewById(2131362529);
            LinearLayout sou5 = (LinearLayout) dialog.findViewById(2131362531);
            RadioButton rd1 = (RadioButton) dialog.findViewById(2131362524);
            RadioButton rd2 = (RadioButton) dialog.findViewById(2131362526);
            RadioButton rd3 = (RadioButton) dialog.findViewById(2131362528);
            RadioButton rd4 = (RadioButton) dialog.findViewById(2131362530);
            RadioButton rd5 = (RadioButton) dialog.findViewById(2131362532);
            Button set = (Button) dialog.findViewById(2131362534);
            ((LinearLayout) dialog.findViewById(2131362523)).setOnClickListener(new C08631(rd1, rd2, rd3, rd4, rd5));
            sou2.setOnClickListener(new C08642(rd1, rd2, rd3, rd4, rd5));
            sou3.setOnClickListener(new C08653(rd1, rd2, rd3, rd4, rd5));
            sou4.setOnClickListener(new C08664(rd1, rd2, rd3, rd4, rd5));
            sou5.setOnClickListener(new C08675(rd1, rd2, rd3, rd4, rd5));
            set.setOnClickListener(new C08686(dialog));
        }
    }

    /* renamed from: com.shamchat.activity.SettingsActivity.2 */
    class C08702 implements OnClickListener {
        C08702() {
        }

        public final void onClick(View v) {
            SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, Quality.class));
        }
    }

    /* renamed from: com.shamchat.activity.SettingsActivity.3 */
    class C08713 implements OnClickListener {
        C08713() {
        }

        public final void onClick(View v) {
            SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, FontStyle.class));
        }
    }

    /* renamed from: com.shamchat.activity.SettingsActivity.4 */
    class C08744 implements OnClickListener {

        /* renamed from: com.shamchat.activity.SettingsActivity.4.1 */
        class C08721 implements OnClickListener {
            final /* synthetic */ EditText val$confpass;
            final /* synthetic */ Dialog val$dialog;
            final /* synthetic */ EditText val$newpass;

            C08721(EditText editText, EditText editText2, Dialog dialog) {
                this.val$confpass = editText;
                this.val$newpass = editText2;
                this.val$dialog = dialog;
            }

            public final void onClick(View v) {
                String confp = this.val$confpass.getText().toString();
                String npass = this.val$newpass.getText().toString();
                if (npass == null || npass.length() <= 0 || !npass.equals(confp)) {
                    Toast.makeText(SettingsActivity.this.getApplicationContext(), SettingsActivity.this.getResources().getString(2131493254), 0).show();
                    this.val$newpass.setText(BuildConfig.VERSION_NAME);
                    this.val$confpass.setText(BuildConfig.VERSION_NAME);
                    return;
                }
                SettingsActivity.this.sp = SettingsActivity.this.getApplicationContext().getSharedPreferences("setting", 0);
                String pass = this.val$newpass.getText().toString();
                Editor editor = SettingsActivity.this.sp.edit();
                editor.putString("pass", pass);
                editor.commit();
                this.val$dialog.cancel();
                Toast.makeText(SettingsActivity.this.getApplicationContext(), SettingsActivity.this.getResources().getString(2131493252), 0).show();
            }
        }

        /* renamed from: com.shamchat.activity.SettingsActivity.4.2 */
        class C08732 implements OnClickListener {
            final /* synthetic */ Dialog val$dialog;
            final /* synthetic */ EditText val$newpass;

            C08732(EditText editText, Dialog dialog) {
                this.val$newpass = editText;
                this.val$dialog = dialog;
            }

            public final void onClick(View v) {
                SettingsActivity.this.sp = SettingsActivity.this.getApplicationContext().getSharedPreferences("setting", 0);
                this.val$newpass.getText().toString();
                Editor editor = SettingsActivity.this.sp.edit();
                editor.putString("pass", BuildConfig.VERSION_NAME);
                editor.commit();
                this.val$dialog.cancel();
                Toast.makeText(SettingsActivity.this.getApplicationContext(), SettingsActivity.this.getResources().getString(2131493250), 0).show();
            }
        }

        C08744() {
        }

        public final void onClick(View v) {
            Dialog dialog = new Dialog(SettingsActivity.this);
            dialog.requestWindowFeature(1);
            dialog.setContentView(2130903229);
            dialog.show();
            EditText newpass = (EditText) dialog.findViewById(2131362082);
            Button set = (Button) dialog.findViewById(2131362083);
            Button remove = (Button) dialog.findViewById(2131362084);
            set.setOnClickListener(new C08721((EditText) dialog.findViewById(2131362522), newpass, dialog));
            remove.setOnClickListener(new C08732(newpass, dialog));
        }
    }

    /* renamed from: com.shamchat.activity.SettingsActivity.5 */
    class C08845 implements OnClickListener {

        /* renamed from: com.shamchat.activity.SettingsActivity.5.1 */
        class C08751 implements OnClickListener {
            final /* synthetic */ Dialog val$dialog;

            C08751(Dialog dialog) {
                this.val$dialog = dialog;
            }

            public final void onClick(View v) {
                SettingsActivity.this.sp = SettingsActivity.this.getApplicationContext().getSharedPreferences("setting", 0);
                Editor editor = SettingsActivity.this.sp.edit();
                editor.putInt("notif_icon", 1);
                editor.commit();
                Toast.makeText(SettingsActivity.this.getApplicationContext(), SettingsActivity.this.getResources().getString(2131493194), 0).show();
                this.val$dialog.cancel();
            }
        }

        /* renamed from: com.shamchat.activity.SettingsActivity.5.2 */
        class C08762 implements OnClickListener {
            final /* synthetic */ Dialog val$dialog;

            C08762(Dialog dialog) {
                this.val$dialog = dialog;
            }

            public final void onClick(View v) {
                SettingsActivity.this.sp = SettingsActivity.this.getApplicationContext().getSharedPreferences("setting", 0);
                Editor editor = SettingsActivity.this.sp.edit();
                editor.putInt("notif_icon", 2);
                editor.commit();
                Toast.makeText(SettingsActivity.this.getApplicationContext(), SettingsActivity.this.getResources().getString(2131493194), 0).show();
                this.val$dialog.cancel();
            }
        }

        /* renamed from: com.shamchat.activity.SettingsActivity.5.3 */
        class C08773 implements OnClickListener {
            final /* synthetic */ Dialog val$dialog;

            C08773(Dialog dialog) {
                this.val$dialog = dialog;
            }

            public final void onClick(View v) {
                SettingsActivity.this.sp = SettingsActivity.this.getApplicationContext().getSharedPreferences("setting", 0);
                Editor editor = SettingsActivity.this.sp.edit();
                editor.putInt("notif_icon", 3);
                editor.commit();
                Toast.makeText(SettingsActivity.this.getApplicationContext(), SettingsActivity.this.getResources().getString(2131493194), 0).show();
                this.val$dialog.cancel();
            }
        }

        /* renamed from: com.shamchat.activity.SettingsActivity.5.4 */
        class C08784 implements OnClickListener {
            final /* synthetic */ Dialog val$dialog;

            C08784(Dialog dialog) {
                this.val$dialog = dialog;
            }

            public final void onClick(View v) {
                SettingsActivity.this.sp = SettingsActivity.this.getApplicationContext().getSharedPreferences("setting", 0);
                Editor editor = SettingsActivity.this.sp.edit();
                editor.putInt("notif_icon", 4);
                editor.commit();
                Toast.makeText(SettingsActivity.this.getApplicationContext(), SettingsActivity.this.getResources().getString(2131493194), 0).show();
                this.val$dialog.cancel();
            }
        }

        /* renamed from: com.shamchat.activity.SettingsActivity.5.5 */
        class C08795 implements OnClickListener {
            final /* synthetic */ Dialog val$dialog;

            C08795(Dialog dialog) {
                this.val$dialog = dialog;
            }

            public final void onClick(View v) {
                SettingsActivity.this.sp = SettingsActivity.this.getApplicationContext().getSharedPreferences("setting", 0);
                Editor editor = SettingsActivity.this.sp.edit();
                editor.putInt("notif_icon", 5);
                editor.commit();
                Toast.makeText(SettingsActivity.this.getApplicationContext(), "\u0622\u06cc\u06a9\u0646 \u067e\u06cc\u063a\u0627\u0645 \u062a\u063a\u06cc\u06cc\u0631 \u06cc\u0627\u0641\u062a", 0).show();
                this.val$dialog.cancel();
            }
        }

        /* renamed from: com.shamchat.activity.SettingsActivity.5.6 */
        class C08806 implements OnClickListener {
            final /* synthetic */ Dialog val$dialog;

            C08806(Dialog dialog) {
                this.val$dialog = dialog;
            }

            public final void onClick(View v) {
                SettingsActivity.this.sp = SettingsActivity.this.getApplicationContext().getSharedPreferences("setting", 0);
                Editor editor = SettingsActivity.this.sp.edit();
                editor.putInt("notif_icon", 6);
                editor.commit();
                Toast.makeText(SettingsActivity.this.getApplicationContext(), SettingsActivity.this.getResources().getString(2131493194), 0).show();
                this.val$dialog.cancel();
            }
        }

        /* renamed from: com.shamchat.activity.SettingsActivity.5.7 */
        class C08817 implements OnClickListener {
            final /* synthetic */ Dialog val$dialog;

            C08817(Dialog dialog) {
                this.val$dialog = dialog;
            }

            public final void onClick(View v) {
                SettingsActivity.this.sp = SettingsActivity.this.getApplicationContext().getSharedPreferences("setting", 0);
                Editor editor = SettingsActivity.this.sp.edit();
                editor.putInt("notif_icon", 7);
                editor.commit();
                Toast.makeText(SettingsActivity.this.getApplicationContext(), SettingsActivity.this.getResources().getString(2131493194), 0).show();
                this.val$dialog.cancel();
            }
        }

        /* renamed from: com.shamchat.activity.SettingsActivity.5.8 */
        class C08828 implements OnClickListener {
            final /* synthetic */ Dialog val$dialog;

            C08828(Dialog dialog) {
                this.val$dialog = dialog;
            }

            public final void onClick(View v) {
                SettingsActivity.this.sp = SettingsActivity.this.getApplicationContext().getSharedPreferences("setting", 0);
                Editor editor = SettingsActivity.this.sp.edit();
                editor.putInt("notif_icon", 8);
                editor.commit();
                Toast.makeText(SettingsActivity.this.getApplicationContext(), SettingsActivity.this.getResources().getString(2131493194), 0).show();
                this.val$dialog.cancel();
            }
        }

        /* renamed from: com.shamchat.activity.SettingsActivity.5.9 */
        class C08839 implements OnClickListener {
            final /* synthetic */ Dialog val$dialog;

            C08839(Dialog dialog) {
                this.val$dialog = dialog;
            }

            public final void onClick(View v) {
                SettingsActivity.this.sp = SettingsActivity.this.getApplicationContext().getSharedPreferences("setting", 0);
                Editor editor = SettingsActivity.this.sp.edit();
                editor.putInt("notif_icon", 9);
                editor.commit();
                Toast.makeText(SettingsActivity.this.getApplicationContext(), SettingsActivity.this.getResources().getString(2131493194), 0).show();
                this.val$dialog.cancel();
            }
        }

        C08845() {
        }

        public final void onClick(View v) {
            Dialog dialog = new Dialog(SettingsActivity.this);
            dialog.requestWindowFeature(1);
            dialog.setContentView(2130903150);
            dialog.show();
            ImageView ic2 = (ImageView) dialog.findViewById(2131362258);
            ImageView ic3 = (ImageView) dialog.findViewById(2131362259);
            ImageView ic4 = (ImageView) dialog.findViewById(2131362260);
            ImageView ic5 = (ImageView) dialog.findViewById(2131362261);
            ImageView ic6 = (ImageView) dialog.findViewById(2131362262);
            ImageView ic7 = (ImageView) dialog.findViewById(2131362263);
            ImageView ic8 = (ImageView) dialog.findViewById(2131362264);
            ImageView ic9 = (ImageView) dialog.findViewById(2131362265);
            ((ImageView) dialog.findViewById(2131362257)).setOnClickListener(new C08751(dialog));
            ic2.setOnClickListener(new C08762(dialog));
            ic3.setOnClickListener(new C08773(dialog));
            ic4.setOnClickListener(new C08784(dialog));
            ic5.setOnClickListener(new C08795(dialog));
            ic6.setOnClickListener(new C08806(dialog));
            ic7.setOnClickListener(new C08817(dialog));
            ic8.setOnClickListener(new C08828(dialog));
            ic9.setOnClickListener(new C08839(dialog));
        }
    }

    /* renamed from: com.shamchat.activity.SettingsActivity.6 */
    class C08876 implements OnClickListener {

        /* renamed from: com.shamchat.activity.SettingsActivity.6.1 */
        class C08851 implements DialogInterface.OnClickListener {
            C08851() {
            }

            public final void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase db = new ChatDatabaseHelper(SHAMChatApplication.getMyApplicationContext()).getWritableDatabase();
                if (db.isOpen()) {
                    db.execSQL("DELETE  from chat_message");
                }
            }
        }

        /* renamed from: com.shamchat.activity.SettingsActivity.6.2 */
        class C08862 implements DialogInterface.OnClickListener {
            C08862() {
            }

            public final void onClick(DialogInterface dialog, int which) {
            }
        }

        C08876() {
        }

        public final void onClick(View v) {
            AlertDialog altd = new Builder(SettingsActivity.this).create();
            altd.setTitle(SettingsActivity.this.getResources().getString(2131493016));
            altd.setMessage(SettingsActivity.this.getResources().getString(2131493095));
            altd.setButton(SettingsActivity.this.getResources().getString(2131493243), new C08851());
            altd.setButton2(SettingsActivity.this.getResources().getString(2131492872), new C08862());
            altd.show();
        }
    }

    /* renamed from: com.shamchat.activity.SettingsActivity.7 */
    class C08907 implements OnClickListener {

        /* renamed from: com.shamchat.activity.SettingsActivity.7.1 */
        class C08881 implements DialogInterface.OnClickListener {
            C08881() {
            }

            public final void onClick(DialogInterface dialog, int which) {
                SettingsActivity.this.sp = SettingsActivity.this.getApplicationContext().getSharedPreferences("setting", 0);
                Editor editor = SettingsActivity.this.sp.edit();
                editor.putInt("background", 1);
                editor.putInt("notif_icon", 1);
                editor.putString("pass", BuildConfig.VERSION_NAME);
                editor.putInt("sound", 1);
                editor.putInt("font", 2);
                editor.putInt("sizee", 14);
                editor.putInt("shakee", 1);
                editor.commit();
            }
        }

        /* renamed from: com.shamchat.activity.SettingsActivity.7.2 */
        class C08892 implements DialogInterface.OnClickListener {
            final /* synthetic */ AlertDialog val$altd;

            C08892(AlertDialog alertDialog) {
                this.val$altd = alertDialog;
            }

            public final void onClick(DialogInterface dialog, int which) {
                this.val$altd.cancel();
            }
        }

        C08907() {
        }

        public final void onClick(View v) {
            AlertDialog altd = new Builder(SettingsActivity.this).create();
            altd.setTitle(SettingsActivity.this.getResources().getString(2131493296));
            altd.setMessage(SettingsActivity.this.getResources().getString(2131493295));
            altd.setButton(SettingsActivity.this.getResources().getString(2131493243), new C08881());
            altd.setButton2(SettingsActivity.this.getResources().getString(2131492872), new C08892(altd));
            altd.show();
        }
    }

    public SettingsActivity() {
        this.icnum = 1;
        this.music = 1;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903101);
        initializeActionBar();
        this.settingsItems = (LinearLayout) findViewById(2131362085);
        this.settingsListAdapter = new SettingsItemsAdapter(this);
        addSettingsListItems();
        this.verc = (TextView) findViewById(2131362086);
        this.mobile = (TextView) findViewById(2131362077);
        this.mContentResolver = getContentResolver();
        Cursor cursor = this.mContentResolver.query(UserProvider.CONTENT_URI_USER, null, "userId=?", new String[]{SHAMChatApplication.getConfig().userId}, null);
        cursor.moveToFirst();
        this.me = UserProvider.userFromCursor(cursor);
        String mo = this.me.mobileNo;
        cursor.close();
        this.mobile.setText("Mobile Number: " + mo);
        this.verc.setText("Salam Version Application: " + getString(2131492993));
    }

    private void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(31);
        actionBar.setCustomView(2130903043);
        ((TextView) findViewById(2131361839)).setText(2131493369);
    }

    private void addSettingsListItems() {
        addBottomItem(2130837996, getResources().getString(2131493239), Boolean.valueOf(false), BuildConfig.VERSION_NAME, BuildConfig.VERSION_NAME, new C08691(), false);
        addBottomItem(2130837615, getResources().getString(2131492986), Boolean.valueOf(false), BuildConfig.VERSION_NAME, BuildConfig.VERSION_NAME, new C08702(), false);
        addBottomItem(2130837871, getResources().getString(2131493142), Boolean.valueOf(false), BuildConfig.VERSION_NAME, BuildConfig.VERSION_NAME, new C08713(), false);
        addBottomItem(2130838005, getResources().getString(2131493249), Boolean.valueOf(false), BuildConfig.VERSION_NAME, BuildConfig.VERSION_NAME, new C08744(), false);
        addBottomItem(2130837995, getResources().getString(2131493233), Boolean.valueOf(false), BuildConfig.VERSION_NAME, BuildConfig.VERSION_NAME, new C08845(), false);
        addBottomItem(2130837671, getResources().getString(2131493016), Boolean.valueOf(false), BuildConfig.VERSION_NAME, BuildConfig.VERSION_NAME, new C08876(), false);
        addBottomItem(2130838025, getResources().getString(2131493296), Boolean.valueOf(false), BuildConfig.VERSION_NAME, BuildConfig.VERSION_NAME, new C08907(), false);
    }

    private void addBottomItem(int iconRes, String text, Boolean vis, String wifi, String data, OnClickListener listener, boolean refresh) {
        this.settingsListAdapter.list.add(new SettingListItem(iconRes, text, vis, wifi, data));
        int position = this.settingsListAdapter.list.size() - 1;
        this.settingsItems.addView(LayoutInflater.from(this).inflate(2130903192, null));
        View rowView = this.settingsListAdapter.getView(position, null, null);
        rowView.setBackgroundResource(2130837576);
        this.settingsItems.addView(rowView);
        rowView.setOnClickListener(listener);
        if (refresh) {
            this.settingsListAdapter.notifyDataSetChanged();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                break;
        }
        return false;
    }
}

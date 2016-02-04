package com.shamchat.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.util.Calendar;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.logging.Logger;

@SuppressLint({"ResourceAsColor"})
public class Quality extends AppCompatActivity {
    public static String rec;
    public static String res;
    public static String us;
    ActionBar actionBar;
    AlertDialog alert;
    private Cursor f16c;
    Button camera;
    private int count;
    View forms;
    Button galery;
    int f17i;
    ImageView im1;
    ImageView im2;
    ImageView im3;
    ImageView im4;
    ImageView im5;
    ImageView im6;
    private byte[] img_byte;
    Button ok;
    private String pass;
    String path;
    String picss;
    int serverResponseCode;
    private SharedPreferences sp;
    private String f18u;

    /* renamed from: com.shamchat.activity.Quality.1 */
    class C08441 implements OnClickListener {
        C08441() {
        }

        public final void onClick(View v) {
            Quality.this.forms.setBackground(null);
            Quality.this.forms.setBackgroundResource(2130837614);
            Quality.this.f17i = 1;
        }
    }

    /* renamed from: com.shamchat.activity.Quality.2 */
    class C08452 implements OnClickListener {
        C08452() {
        }

        public final void onClick(View v) {
            Quality.this.forms.setBackground(null);
            Quality.this.forms.setBackgroundResource(2130837609);
            Quality.this.f17i = 2;
        }
    }

    /* renamed from: com.shamchat.activity.Quality.3 */
    class C08463 implements OnClickListener {
        C08463() {
        }

        public final void onClick(View v) {
            Quality.this.forms.setBackground(null);
            Quality.this.forms.setBackgroundResource(2130837610);
            Quality.this.f17i = 3;
        }
    }

    /* renamed from: com.shamchat.activity.Quality.4 */
    class C08474 implements OnClickListener {
        C08474() {
        }

        public final void onClick(View v) {
            Quality.this.forms.setBackground(null);
            Quality.this.forms.setBackgroundResource(2130837611);
            Quality.this.f17i = 4;
        }
    }

    /* renamed from: com.shamchat.activity.Quality.5 */
    class C08485 implements OnClickListener {
        C08485() {
        }

        public final void onClick(View v) {
            Quality.this.forms.setBackground(null);
            Quality.this.forms.setBackgroundResource(2130837612);
            Quality.this.f17i = 5;
        }
    }

    /* renamed from: com.shamchat.activity.Quality.6 */
    class C08496 implements OnClickListener {
        C08496() {
        }

        public final void onClick(View v) {
            Quality.this.forms.setBackground(null);
            Quality.this.forms.setBackgroundResource(2130837613);
            Quality.this.f17i = 6;
        }
    }

    /* renamed from: com.shamchat.activity.Quality.7 */
    class C08507 implements OnClickListener {
        C08507() {
        }

        public final void onClick(View v) {
            Quality.this.sp = Quality.this.getApplicationContext().getSharedPreferences("setting", 0);
            Editor editor = Quality.this.sp.edit();
            editor.putInt("background", Quality.this.f17i);
            editor.commit();
            Toast.makeText(Quality.this.getApplicationContext(), "\u062a\u0635\u0648\u06cc\u0631 \u0632\u0645\u06cc\u0646\u0647 \u062a\u063a\u06cc\u06cc\u0631 \u06cc\u0627\u0641\u062a", 0).show();
        }
    }

    /* renamed from: com.shamchat.activity.Quality.8 */
    class C08518 implements OnClickListener {
        C08518() {
        }

        public final void onClick(View v) {
            Quality.this.takePhoto();
            Quality.this.f17i = 7;
        }
    }

    /* renamed from: com.shamchat.activity.Quality.9 */
    class C08529 implements OnClickListener {
        C08529() {
        }

        public final void onClick(View v) {
            Intent photoPickerIntent = new Intent("android.intent.action.PICK");
            photoPickerIntent.setType("image/*");
            Quality.this.startActivityForResult(photoPickerIntent, 1);
            Quality.this.f17i = 8;
        }
    }

    public Quality() {
        this.f17i = 1;
        this.count = 0;
        this.pass = BuildConfig.VERSION_NAME;
        this.f18u = BuildConfig.VERSION_NAME;
        this.picss = BuildConfig.VERSION_NAME;
        this.img_byte = null;
        this.f16c = null;
        this.alert = null;
        this.serverResponseCode = 0;
    }

    static {
        res = BuildConfig.VERSION_NAME;
        rec = BuildConfig.VERSION_NAME;
        us = BuildConfig.VERSION_NAME;
    }

    @SuppressLint({"ResourceAsColor"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903110);
        this.im1 = (ImageView) findViewById(2131362112);
        this.im2 = (ImageView) findViewById(2131362111);
        this.im3 = (ImageView) findViewById(2131362110);
        this.im4 = (ImageView) findViewById(2131362109);
        this.im5 = (ImageView) findViewById(2131362108);
        this.im6 = (ImageView) findViewById(2131362107);
        this.ok = (Button) findViewById(2131362115);
        this.galery = (Button) findViewById(2131362113);
        this.camera = (Button) findViewById(2131362114);
        this.forms = findViewById(2131362105);
        this.actionBar = getSupportActionBar();
        this.actionBar.setDisplayOptions(28);
        this.actionBar.setDisplayHomeAsUpEnabled(true);
        View cView = getLayoutInflater().inflate(2130903126, null);
        ((Yekantext) cView.findViewById(2131361839)).setText(getResources().getString(2131492986));
        this.actionBar.setCustomView(cView);
        this.im1.setOnClickListener(new C08441());
        this.im2.setOnClickListener(new C08452());
        this.im3.setOnClickListener(new C08463());
        this.im4.setOnClickListener(new C08474());
        this.im5.setOnClickListener(new C08485());
        this.im6.setOnClickListener(new C08496());
        this.ok.setOnClickListener(new C08507());
        this.camera.setOnClickListener(new C08518());
        this.galery.setOnClickListener(new C08529());
        this.sp = getApplicationContext().getSharedPreferences("setting", 0);
        int pass = this.sp.getInt("background", 1);
        this.forms.setBackground(null);
        switch (pass) {
            case Logger.SEVERE /*1*/:
                this.forms.setBackgroundResource(2130837614);
                break;
            case Logger.WARNING /*2*/:
                this.forms.setBackgroundResource(2130837609);
                break;
            case Logger.INFO /*3*/:
                this.forms.setBackgroundResource(2130837610);
                break;
            case Logger.CONFIG /*4*/:
                this.forms.setBackgroundResource(2130837611);
                break;
            case Logger.FINE /*5*/:
                this.forms.setBackgroundResource(2130837612);
                break;
            case Logger.FINER /*6*/:
                this.forms.setBackgroundResource(2130837613);
                break;
            default:
                this.forms.setBackgroundResource(2130837614);
                break;
        }
        if (getIntent().getExtras() != null) {
            this.img_byte = this.f16c.getBlob(this.f16c.getColumnIndex("image"));
            this.forms.setBackgroundDrawable(new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(this.img_byte, 0, this.img_byte.length)));
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        this.forms.setBackground(null);
        this.im1 = null;
        this.im2 = null;
        this.im3 = null;
        this.im4 = null;
        this.im5 = null;
        this.im6 = null;
        this.img_byte = null;
        this.camera = null;
        this.galery = null;
        this.ok = null;
        this.forms = null;
        System.gc();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            try {
                Uri photoUri = data.getData();
                if (photoUri != null) {
                    String[] filePathColumn = new String[]{"_data"};
                    Cursor cursor = getContentResolver().query(photoUri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    this.path = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
                    cursor.close();
                    this.forms.setBackgroundDrawable(new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(BitmapFactory.decodeFile(this.path.toString().trim()), 500, 500, true)));
                    this.f18u = this.path.substring(this.path.lastIndexOf(MqttTopic.TOPIC_LEVEL_SEPARATOR) + 1);
                }
            } catch (Throwable th) {
                this.path = BuildConfig.VERSION_NAME;
                return;
            }
        }
        if (requestCode == 2) {
            this.forms.setBackgroundDrawable(new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(BitmapFactory.decodeFile(this.path.toString().trim()), 500, 500, true)));
        }
    }

    public void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File folder = new File(Environment.getExternalStorageDirectory() + "/LoadImg");
        if (!folder.exists()) {
            folder.mkdir();
        }
        Calendar c = Calendar.getInstance();
        String new_Date = c.get(5) + "-" + (c.get(2) + 1) + "-" + c.get(1) + " " + c.get(10) + "-" + c.get(12) + "-" + c.get(13);
        this.path = String.format(Environment.getExternalStorageDirectory() + "/LoadImg/%s.png", new Object[]{"LoadImg(" + new_Date + ")"});
        File photo = new File(this.path);
        this.f18u = "LoadImg(" + new_Date + ").png";
        intent.putExtra("output", Uri.fromFile(photo));
        startActivityForResult(intent, 2);
    }

    public void load_image(View v) {
        CharSequence[] items = new CharSequence[]{"\u06af\u0631\u0641\u062a\u0646 \u0639\u06a9\u0633 \u062c\u062f\u06cc\u062f", " \u0627\u0646\u062a\u062e\u0627\u0628 \u0627\u0632 \u06af\u0627\u0644\u0631\u06cc"};
        Builder builder = new Builder(this);
        builder.setTitle("\u0627\u0646\u062a\u062e\u0627\u0628 \u062a\u0635\u0648\u06cc\u0631");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    Quality.this.takePhoto();
                }
                if (item == 1) {
                    Intent photoPickerIntent = new Intent("android.intent.action.PICK");
                    photoPickerIntent.setType("image/*");
                    Quality.this.startActivityForResult(photoPickerIntent, 1);
                }
            }
        });
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

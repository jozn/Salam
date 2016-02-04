package com.shamchat.activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.shamchat.androidclient.SHAMChatApplication;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class FontStyle extends AppCompatActivity {
    ActionBar actionBar;
    private int size;
    private SharedPreferences sp;

    /* renamed from: com.shamchat.activity.FontStyle.1 */
    class C07561 implements OnClickListener {
        final /* synthetic */ RadioButton val$rd2;
        final /* synthetic */ RadioButton val$rd3;
        final /* synthetic */ TextView val$txtsample;
        final /* synthetic */ TextView val$txtsample2;
        final /* synthetic */ TextView val$txtsample3;

        C07561(TextView textView, TextView textView2, TextView textView3, RadioButton radioButton, RadioButton radioButton2) {
            this.val$txtsample = textView;
            this.val$txtsample2 = textView2;
            this.val$txtsample3 = textView3;
            this.val$rd2 = radioButton;
            this.val$rd3 = radioButton2;
        }

        public final void onClick(View v) {
            this.val$txtsample.setVisibility(0);
            this.val$txtsample2.setVisibility(8);
            this.val$txtsample3.setVisibility(8);
            this.val$rd2.setChecked(false);
            this.val$rd3.setChecked(false);
        }
    }

    /* renamed from: com.shamchat.activity.FontStyle.2 */
    class C07572 implements OnClickListener {
        final /* synthetic */ RadioButton val$rd1;
        final /* synthetic */ RadioButton val$rd3;
        final /* synthetic */ TextView val$txtsample;
        final /* synthetic */ TextView val$txtsample2;
        final /* synthetic */ TextView val$txtsample3;

        C07572(TextView textView, TextView textView2, TextView textView3, RadioButton radioButton, RadioButton radioButton2) {
            this.val$txtsample2 = textView;
            this.val$txtsample = textView2;
            this.val$txtsample3 = textView3;
            this.val$rd1 = radioButton;
            this.val$rd3 = radioButton2;
        }

        public final void onClick(View v) {
            this.val$txtsample2.setVisibility(0);
            this.val$txtsample.setVisibility(8);
            this.val$txtsample3.setVisibility(8);
            this.val$rd1.setChecked(false);
            this.val$rd3.setChecked(false);
        }
    }

    /* renamed from: com.shamchat.activity.FontStyle.3 */
    class C07583 implements OnClickListener {
        final /* synthetic */ RadioButton val$rd1;
        final /* synthetic */ RadioButton val$rd2;
        final /* synthetic */ TextView val$txtsample;
        final /* synthetic */ TextView val$txtsample2;
        final /* synthetic */ TextView val$txtsample3;

        C07583(TextView textView, TextView textView2, TextView textView3, RadioButton radioButton, RadioButton radioButton2) {
            this.val$txtsample3 = textView;
            this.val$txtsample2 = textView2;
            this.val$txtsample = textView3;
            this.val$rd1 = radioButton;
            this.val$rd2 = radioButton2;
        }

        public final void onClick(View v) {
            this.val$txtsample3.setVisibility(0);
            this.val$txtsample2.setVisibility(8);
            this.val$txtsample.setVisibility(8);
            this.val$rd1.setChecked(false);
            this.val$rd2.setChecked(false);
        }
    }

    /* renamed from: com.shamchat.activity.FontStyle.4 */
    class C07594 implements OnClickListener {
        final /* synthetic */ RadioButton val$rd1;
        final /* synthetic */ RadioButton val$rd2;
        final /* synthetic */ RadioButton val$rd3;

        C07594(RadioButton radioButton, RadioButton radioButton2, RadioButton radioButton3) {
            this.val$rd1 = radioButton;
            this.val$rd2 = radioButton2;
            this.val$rd3 = radioButton3;
        }

        public final void onClick(View v) {
            FontStyle.this.sp = FontStyle.this.getApplicationContext().getSharedPreferences("setting", 0);
            Editor editor = FontStyle.this.sp.edit();
            editor.putInt("sizee", FontStyle.this.size);
            editor.commit();
            if (this.val$rd1.isChecked()) {
                editor.putInt("font", 1);
                editor.commit();
            } else if (this.val$rd2.isChecked()) {
                editor.putInt("font", 2);
                editor.commit();
            } else if (this.val$rd3.isChecked()) {
                editor.putInt("font", 3);
                editor.commit();
            } else {
                editor.putInt("font", 1);
                editor.commit();
            }
            Toast.makeText(FontStyle.this.getApplicationContext(), FontStyle.this.getResources().getString(2131493141), 0).show();
        }
    }

    /* renamed from: com.shamchat.activity.FontStyle.5 */
    class C07605 implements OnSeekBarChangeListener {
        final /* synthetic */ TextView val$txtsample;
        final /* synthetic */ TextView val$txtsample2;
        final /* synthetic */ TextView val$txtsample3;

        C07605(TextView textView, TextView textView2, TextView textView3) {
            this.val$txtsample = textView;
            this.val$txtsample2 = textView2;
            this.val$txtsample3 = textView3;
        }

        public final void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            FontStyle.this.size = progress + 12;
            this.val$txtsample.setTextSize((float) (progress + 12));
            this.val$txtsample2.setTextSize((float) (progress + 12));
            this.val$txtsample3.setTextSize((float) (progress + 12));
        }

        public final void onStartTrackingTouch(SeekBar seekBar) {
        }

        public final void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903135);
        this.actionBar = getSupportActionBar();
        this.actionBar.setDisplayOptions(28);
        this.actionBar.setDisplayHomeAsUpEnabled(true);
        View cView = getLayoutInflater().inflate(2130903126, null);
        ((Yekantext) cView.findViewById(2131361839)).setText(getResources().getString(2131492986));
        this.actionBar.setCustomView(cView);
        this.size = 12;
        TextView txtsample = (TextView) findViewById(2131362205);
        TextView txtsample2 = (TextView) findViewById(2131362204);
        TextView txtsample3 = (TextView) findViewById(2131362203);
        txtsample2.setVisibility(8);
        txtsample3.setVisibility(8);
        SeekBar sekb = (SeekBar) findViewById(2131362206);
        RadioButton rd1 = (RadioButton) findViewById(2131362207);
        RadioButton rd2 = (RadioButton) findViewById(2131362208);
        RadioButton rd3 = (RadioButton) findViewById(2131362209);
        rd1.setChecked(false);
        rd2.setChecked(false);
        rd3.setChecked(false);
        Button btn = (Button) findViewById(2131362210);
        rd1.setOnClickListener(new C07561(txtsample, txtsample2, txtsample3, rd2, rd3));
        rd2.setOnClickListener(new C07572(txtsample2, txtsample, txtsample3, rd1, rd3));
        rd3.setOnClickListener(new C07583(txtsample3, txtsample2, txtsample, rd1, rd2));
        btn.setOnClickListener(new C07594(rd1, rd2, rd3));
        sekb.setOnSeekBarChangeListener(new C07605(txtsample, txtsample2, txtsample3));
        try {
            this.sp = SHAMChatApplication.getMyApplicationContext().getSharedPreferences("setting", 0);
            int size = this.sp.getInt("sizee", 1);
            int font = this.sp.getInt("font", 1);
            sekb.setProgress(size);
            switch (font) {
                case Logger.SEVERE /*1*/:
                    rd1.setChecked(true);
                    return;
                case Logger.WARNING /*2*/:
                    rd2.setChecked(true);
                    return;
                case Logger.INFO /*3*/:
                    rd3.setChecked(true);
                    return;
                default:
                    return;
            }
        } catch (Exception e) {
            rd1.setChecked(true);
            sekb.setProgress(16);
        }
        rd1.setChecked(true);
        sekb.setProgress(16);
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

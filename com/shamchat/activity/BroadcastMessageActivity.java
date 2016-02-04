package com.shamchat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class BroadcastMessageActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903076);
        ((TextView) findViewById(2131361925)).setText(getIntent().getStringExtra("Message"));
    }
}

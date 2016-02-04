package com.shamchat.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class Setpass extends AppCompatActivity {
    ActionBar actionBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903100);
        this.actionBar = getSupportActionBar();
        this.actionBar.setDisplayOptions(12);
        this.actionBar.setCustomView(2130903221);
        this.actionBar.setCustomView(getLayoutInflater().inflate(2130903126, null));
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

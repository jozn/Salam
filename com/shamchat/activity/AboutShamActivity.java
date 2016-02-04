package com.shamchat.activity;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutShamActivity extends AppCompatActivity {
    private TextView appVersion;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903073);
        initializeActionBar();
        this.appVersion = (TextView) findViewById(2131361904);
        try {
            this.appVersion.setText(getResources().getString(2131492983) + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initializeActionBar() {
        getSupportActionBar().setDisplayOptions(31);
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

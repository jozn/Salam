package com.arellomobile.android.push;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import java.util.logging.Logger;

public class MessageActivity extends Activity {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = new Intent();
        String str = getApplicationContext().getPackageName() + ".MESSAGE";
        intent.setAction(str);
        intent.setFlags(603979776);
        intent.putExtras(getIntent().getExtras());
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Logger.getLogger(getClass().getSimpleName()).severe("Can't launch activity. Are you sure you have an activity with '" + str + "' action in your manifest?");
        }
        finish();
    }
}

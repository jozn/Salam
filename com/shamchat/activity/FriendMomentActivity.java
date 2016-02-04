package com.shamchat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.shamchat.moments.MomentsFragment;

public class FriendMomentActivity extends AppCompatActivity {
    protected void onCreate(Bundle arg0) {
        requestWindowFeature(1);
        super.onCreate(arg0);
        setContentView(2130903136);
        String userId = getIntent().getStringExtra("userId");
        Bundle args = new Bundle();
        args.putString("userId", userId);
        MomentsFragment fragment = new MomentsFragment();
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(2131362211, fragment).commit();
    }
}

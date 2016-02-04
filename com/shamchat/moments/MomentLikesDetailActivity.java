package com.shamchat.moments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import com.shamchat.activity.ChatActivity;
import com.shamchat.adapters.MomentLikesDetailAdapter;
import com.shamchat.androidclient.data.MomentProvider;

public class MomentLikesDetailActivity extends AppCompatActivity {
    private Cursor cursor;
    private ListView list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(2130903103);
        this.list = (ListView) findViewById(2131362087);
        String postId = getIntent().getStringExtra("postId");
        this.cursor = getContentResolver().query(MomentProvider.CONTENT_URI_LIKE, new String[]{ChatActivity.INTENT_EXTRA_USER_ID}, "post_id=? AND like_status=?", new String[]{postId, "1"}, "liked_datetime DESC");
        this.list.setAdapter(new MomentLikesDetailAdapter(getApplicationContext(), this.cursor, getSupportFragmentManager()));
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.cursor != null) {
            this.cursor.close();
        }
    }
}

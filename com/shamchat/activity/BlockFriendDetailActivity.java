package com.shamchat.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.RosterProvider;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.utils.Utils;

public class BlockFriendDetailActivity extends AppCompatActivity {
    String UserId;
    Button deleteFriend;
    CheckBox doNotShare;
    String friendId;
    String friendName;
    CheckBox hiddenList;
    Button message;
    ImageView profImg;

    /* renamed from: com.shamchat.activity.BlockFriendDetailActivity.1 */
    class C06861 implements OnClickListener {
        C06861() {
        }

        public final void onClick(View v) {
            BlockFriendDetailActivity.this.getContentResolver().delete(RosterProvider.CONTENT_URI, "jid=?", new String[]{Utils.createXmppUserIdByUserId(BlockFriendDetailActivity.this.friendId)});
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        setContentView(2130903075);
        initializeActionBar();
        super.onCreate(savedInstanceState);
        Bundle b = new Bundle(getIntent().getExtras());
        this.friendId = b.getString("FRIENDID");
        this.friendName = b.getString("USERNAME");
        setTitle(" " + this.friendName);
        this.UserId = SHAMChatApplication.getConfig().userId;
        this.hiddenList = (CheckBox) findViewById(2131361921);
        this.doNotShare = (CheckBox) findViewById(2131361924);
        this.deleteFriend = (Button) findViewById(2131361916);
        this.message = (Button) findViewById(2131361915);
        this.profImg = (ImageView) findViewById(2131361914);
        UserProvider userProvider = new UserProvider();
        Bitmap bitmap = UserProvider.getProfileImageByUserId(this.friendId);
        if (bitmap != null) {
            this.profImg.setImageBitmap(bitmap);
        }
        this.deleteFriend.setOnClickListener(new C06861());
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

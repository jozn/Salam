package com.shamchat.activity;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.shamchat.adapters.BlockedFriendsCheckboxCursorAdapter;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.RosterProvider;
import com.shamchat.androidclient.data.RosterProvider.YesNoStatus;
import java.util.Map.Entry;

public class BlockFriendActivity extends AppCompatActivity implements OnClickListener {
    public static final String EXTRA_CONTACTS = "extra_contacts";
    public static final int PICK_USERS_REQUEST = 1;
    private BlockedFriendsCheckboxCursorAdapter adapter;
    private ImageButton btnAdd;
    private ImageButton btnOk;
    private ListView listView;
    private String userId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903084);
        initializeActionBar();
        this.userId = SHAMChatApplication.getConfig().userId;
        this.listView = (ListView) findViewById(2131362012);
        this.btnAdd = (ImageButton) findViewById(2131361958);
        this.btnAdd.setVisibility(8);
        this.btnOk = (ImageButton) findViewById(2131362013);
        this.btnOk.setOnClickListener(this);
        String[] strArr = new String[PICK_USERS_REQUEST];
        strArr[0] = "1";
        this.adapter = new BlockedFriendsCheckboxCursorAdapter(this, getContentResolver().query(RosterProvider.CONTENT_URI, null, "user_status=?", strArr, null));
        this.listView.setAdapter(this.adapter);
    }

    private void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(31);
        actionBar.setCustomView(2130903043);
        ((TextView) findViewById(2131361839)).setText(getResources().getString(2131492989));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                break;
        }
        return false;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case 2131362013:
                for (Entry<String, Boolean> entry : this.adapter.getSelectedUsers().entrySet()) {
                    ContentValues values = new ContentValues();
                    values.put("user_status", Integer.valueOf(((Boolean) entry.getValue()).booleanValue() ? YesNoStatus.NO.ordinal() : YesNoStatus.YES.ordinal()));
                    String[] strArr = new String[PICK_USERS_REQUEST];
                    strArr[0] = (String) entry.getKey();
                    getContentResolver().update(RosterProvider.CONTENT_URI, values, "jid=?", strArr);
                }
                finish();
            default:
        }
    }

    protected void onResume() {
        this.listView.setAdapter(null);
        this.listView.setAdapter(this.adapter);
        this.adapter.notifyDataSetChanged();
        super.onResume();
    }
}

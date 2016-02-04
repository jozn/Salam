package com.shamchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.shamchat.adapters.ChatComposeGroupAdapter;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.models.FriendGroup;

public class ComposeGroupChatActivity extends AppCompatActivity {
    private ChatComposeGroupAdapter adapter;
    private EditText editSearch;
    private LinearLayout l1;
    private ListView list;
    TextWatcher textWatcher;

    /* renamed from: com.shamchat.activity.ComposeGroupChatActivity.1 */
    class C07221 implements TextWatcher {
        C07221() {
        }

        public final void onTextChanged(CharSequence s, int start, int before, int count) {
            String changedText = s.toString();
            ComposeGroupChatActivity.this.adapter.changeCursor(ComposeGroupChatActivity.this.getContentResolver().query(UserProvider.CONTENT_URI_GROUP, null, FriendGroup.DB_NAME + " LIKE ?", new String[]{"%" + changedText + "%"}, null));
        }

        public final void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public final void afterTextChanged(Editable s) {
        }
    }

    public ComposeGroupChatActivity() {
        this.textWatcher = new C07221();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903079);
        this.list = (ListView) findViewById(2131361964);
        this.editSearch = (EditText) findViewById(2131361960);
        this.editSearch.addTextChangedListener(this.textWatcher);
        this.l1 = (LinearLayout) findViewById(2131361961);
        initializeActionBar();
        this.l1.setVisibility(-1);
        this.adapter = new ChatComposeGroupAdapter(getApplicationContext(), getContentResolver().query(UserProvider.CONTENT_URI_GROUP, null, null, null, FriendGroup.DB_NAME + " ASC"), false);
        this.list.setAdapter(this.adapter);
    }

    public void onPause() {
        super.onPause();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(2131623936, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                setResult(0);
                finish();
                return true;
            case 2131362555:
                startActivity(new Intent(getApplicationContext(), CreateGroupActivity.class));
                return true;
            default:
                return false;
        }
    }

    private void initializeActionBar() {
        getSupportActionBar().setDisplayOptions(31);
    }
}

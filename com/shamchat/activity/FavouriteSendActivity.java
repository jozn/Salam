package com.shamchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.shamchat.adapters.ListMessagesAdapter;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.models.Message;
import com.shamchat.models.User;

public class FavouriteSendActivity extends AppCompatActivity {
    public static final String EXTRA_FAVOURITE = "favourite";
    public static final String EXTRA_FAVOURITE_TYPE = "favourite_TYPE";
    private ListMessagesAdapter adapter;
    private ListView list;
    private User user;

    /* renamed from: com.shamchat.activity.FavouriteSendActivity.1 */
    class C07541 implements OnItemClickListener {
        C07541() {
        }

        public final void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Message message = (Message) FavouriteSendActivity.this.adapter.list.get(position);
            Intent data = new Intent();
            data.putExtra(FavouriteSendActivity.EXTRA_FAVOURITE, message.messageContent);
            data.putExtra(FavouriteSendActivity.EXTRA_FAVOURITE_TYPE, message.type.type);
            FavouriteSendActivity.this.setResult(-1, data);
            FavouriteSendActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903085);
        initializeActionBar();
        UserProvider userProvider = new UserProvider();
        this.user = UserProvider.getCurrentUser();
        this.list = (ListView) findViewById(2131362014);
        this.adapter = new ListMessagesAdapter(this);
        this.list.setAdapter(this.adapter);
        refreshInfo();
        this.list.setOnItemClickListener(new C07541());
    }

    private void initializeActionBar() {
        getSupportActionBar().setDisplayOptions(31);
    }

    private void refreshInfo() {
        ListMessagesAdapter listMessagesAdapter = this.adapter;
        String str = this.user.userId;
        ChatProviderNew chatProviderNew = new ChatProviderNew();
        listMessagesAdapter.refreshMessagesFromDB$7c629dcd(str);
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

package com.shamchat.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import com.shamchat.adapters.GroupCheckboxCursorAdapter;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.models.User;
import com.shamchat.utils.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CreateGroupAddUsersActivity extends AppCompatActivity {
    public static final String EXTRA_IGNORED_USERS = "extra_ignored_users";
    public static final String EXTRA_IGNORED_USERS_GROUP = "extra_ignored_users_group";
    public static final int REQUEST_CODE_HOME_PRESSED = 0;
    public static final int REQUEST_CODE_SAVE_PRESSED = 1;
    protected static final String TAG = "CreateGroupAddUsersActivity";
    boolean FLAG;
    private GroupCheckboxCursorAdapter adapter;
    private String allUsers;
    private String filterSelection;
    private List<User> ignoredUsers;
    private ListView list;
    private EditText searchBar;
    private String selectionArgs;

    /* renamed from: com.shamchat.activity.CreateGroupAddUsersActivity.1 */
    class C07471 implements TextWatcher {
        C07471() {
        }

        public final void onTextChanged(CharSequence s, int start, int before, int count) {
            CreateGroupAddUsersActivity.this.displayData(s.toString());
        }

        public final void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public final void afterTextChanged(Editable s) {
        }
    }

    public CreateGroupAddUsersActivity() {
        this.ignoredUsers = new ArrayList();
        this.FLAG = false;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903081);
        this.list = (ListView) findViewById(2131361970);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(31);
        actionBar.setCustomView(2130903040);
        this.searchBar = (EditText) actionBar.getCustomView().findViewById(2131361836);
        this.searchBar.addTextChangedListener(new C07471());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(EXTRA_IGNORED_USERS)) {
                Parcelable[] parcelableArray = extras.getParcelableArray(EXTRA_IGNORED_USERS);
                int length = parcelableArray.length;
                for (int i = REQUEST_CODE_HOME_PRESSED; i < length; i += REQUEST_CODE_SAVE_PRESSED) {
                    Parcelable p = parcelableArray[i];
                    Log.e("user", p.toString());
                    this.ignoredUsers.add((User) p);
                }
            }
            if (extras.containsKey(EXTRA_IGNORED_USERS_GROUP)) {
                this.FLAG = true;
                Iterator<User> iter = ((ArrayList) extras.getSerializable(EXTRA_IGNORED_USERS_GROUP)).iterator();
                while (iter.hasNext()) {
                    this.ignoredUsers.add((User) iter.next());
                }
            }
        }
        displayData(BuildConfig.VERSION_NAME);
    }

    private void displayData(String searchText) {
        String[] userIds = new String[(this.ignoredUsers.size() + 3)];
        this.selectionArgs = "user_type =? AND name LIKE ? AND userId NOT IN (?";
        userIds[REQUEST_CODE_HOME_PRESSED] = "2";
        userIds[REQUEST_CODE_SAVE_PRESSED] = "%" + searchText + "%";
        userIds[2] = SHAMChatApplication.getConfig().userId;
        for (int i = REQUEST_CODE_HOME_PRESSED; i < this.ignoredUsers.size(); i += REQUEST_CODE_SAVE_PRESSED) {
            this.selectionArgs += ", ?";
            userIds[i + 3] = ((User) this.ignoredUsers.get(i)).userId;
        }
        this.selectionArgs += ")";
        Cursor cursor = getContentResolver().query(UserProvider.CONTENT_URI_USER, null, this.selectionArgs, userIds, null);
        if (this.adapter != null) {
            this.adapter.changeCursor(cursor);
            this.list.setAdapter(this.adapter);
            return;
        }
        this.adapter = new GroupCheckboxCursorAdapter(this, cursor);
        this.list.setAdapter(this.adapter);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                setResult(REQUEST_CODE_HOME_PRESSED, null);
                finish();
                return true;
            default:
                return false;
        }
    }

    public void finish() {
        Utils.hideKeyboard(this.searchBar, getApplicationContext());
        super.finish();
    }

    public void onClickAdd(View view) {
        if (this.FLAG) {
            ArrayList<User> users = this.adapter.getSelectedUsers();
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(CreateGroupActivity.EXTRA_CONTACTS, users);
            setResult(-1, intent);
            finish();
            return;
        }
        users = this.adapter.getSelectedUsers();
        intent = new Intent();
        intent.putParcelableArrayListExtra(CreateGroupActivity.EXTRA_CONTACTS, users);
        setResult(-1, intent);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

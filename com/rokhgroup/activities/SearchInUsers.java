package com.rokhgroup.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.rokhgroup.adapters.UserSearchAdapter;
import com.rokhgroup.adapters.item.UserSearchItem;
import com.rokhgroup.utils.LoadToast;
import com.rokhgroup.utils.RokhPref;
import com.shamchat.activity.MainWindow;
import com.shamchat.activity.Yekantext;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class SearchInUsers extends AppCompatActivity {
    private static String CURRENT_USER_TOKEN = null;
    private static String SearchText = null;
    private static String USER_ID = null;
    private static final String hostAddress = "http://social.rabtcdn.com/media/";
    boolean FOLLOW_W_USER;
    ArrayList<UserSearchItem> SearchDATA;
    RokhPref Session;
    String URL;
    String USER_AVATAR;
    String USER_NAME;
    String USER_USERNAME;
    JSONArray Users;
    private Intent intent;
    boolean isLoggedin;
    ListView list;
    private Context mContext;
    LoadToast mLoadToast;
    UserSearchAdapter mSAdapter;
    UserSearchItem mSearchItem;
    Yekantext noResult;
    Uri uri;

    /* renamed from: com.rokhgroup.activities.SearchInUsers.1 */
    class C05871 implements OnItemClickListener {
        C05871() {
        }

        public final void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            SearchInUsers.this.mSearchItem = (UserSearchItem) parent.getItemAtPosition(position);
            Intent intent = new Intent(SearchInUsers.this.mContext, UserProfile.class);
            intent.putExtra("USER_ID", SearchInUsers.this.mSearchItem.USER_ID);
            SearchInUsers.this.startActivity(intent);
        }
    }

    /* renamed from: com.rokhgroup.activities.SearchInUsers.2 */
    class C05912 implements Callback {

        /* renamed from: com.rokhgroup.activities.SearchInUsers.2.1 */
        class C05881 implements Runnable {
            C05881() {
            }

            public final void run() {
                SearchInUsers.this.mLoadToast.error();
            }
        }

        /* renamed from: com.rokhgroup.activities.SearchInUsers.2.2 */
        class C05892 implements Runnable {
            final /* synthetic */ JSONObject val$jsonResponse;

            C05892(JSONObject jSONObject) {
                this.val$jsonResponse = jSONObject;
            }

            public final void run() {
                try {
                    SearchInUsers.this.mLoadToast.success();
                    SearchInUsers.this.Users = this.val$jsonResponse.getJSONArray("objects");
                    if (SearchInUsers.this.Users.length() > 0) {
                        for (int i = 0; i < SearchInUsers.this.Users.length(); i++) {
                            JSONObject Item = SearchInUsers.this.Users.getJSONObject(i);
                            SearchInUsers.this.USER_AVATAR = "http://social.rabtcdn.com" + Item.getString("avatar");
                            SearchInUsers.this.USER_NAME = Item.getString("name");
                            SearchInUsers.this.USER_USERNAME = Item.getString("username");
                            SearchInUsers.this.FOLLOW_W_USER = Item.getBoolean("follow_by_user");
                            SearchInUsers.USER_ID = Item.getString("id");
                            SearchInUsers.this.SearchDATA.add(new UserSearchItem(SearchInUsers.USER_ID, SearchInUsers.this.USER_NAME, SearchInUsers.this.USER_AVATAR, SearchInUsers.this.FOLLOW_W_USER));
                        }
                        SearchInUsers.this.mSAdapter.notifyDataSetChanged();
                        return;
                    }
                    SearchInUsers.this.noResult.setVisibility(0);
                    SearchInUsers.this.list.setVisibility(8);
                } catch (Exception e) {
                    SearchInUsers.this.mLoadToast.error();
                }
            }
        }

        /* renamed from: com.rokhgroup.activities.SearchInUsers.2.3 */
        class C05903 implements Runnable {
            C05903() {
            }

            public final void run() {
                SearchInUsers.this.mLoadToast.error();
            }
        }

        C05912() {
        }

        public final void onFailure(Request request, IOException e) {
            SearchInUsers.this.runOnUiThread(new C05881());
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String stringResponse = response.body().string();
                    response.body().close();
                    System.out.println(stringResponse);
                    SearchInUsers.this.runOnUiThread(new C05892(new JSONObject(stringResponse)));
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                SearchInUsers.this.runOnUiThread(new C05903());
                e.printStackTrace();
            }
        }
    }

    public SearchInUsers() {
        this.SearchDATA = new ArrayList();
        this.Users = null;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903217);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(12);
        actionBar.setCustomView(2130903217);
        actionBar.setTitle(MainWindow.serch_titel.toString());
        this.noResult = (Yekantext) findViewById(2131362485);
        this.mContext = this;
        this.mLoadToast = new LoadToast(this.mContext);
        this.Session = new RokhPref(this.mContext);
        CURRENT_USER_TOKEN = this.Session.getUSERTOKEN();
        this.uri = getIntent().getData();
        this.mSAdapter = new UserSearchAdapter(this.mContext, this, this.SearchDATA, true);
        this.list = (ListView) findViewById(2131362484);
        this.list.setAdapter(this.mSAdapter);
        this.list.setOnItemClickListener(new C05871());
        if (this.uri == null) {
            this.intent = getIntent();
            SearchText = this.intent.getStringExtra("SearchText");
            load(false);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                break;
        }
        return false;
    }

    private void load(boolean refresh) {
        this.mLoadToast.setText("Retrieving data...").setProgressColor(getResources().getColor(2131230847)).show();
        if (this.isLoggedin) {
            this.URL = "http://social.rabtcdn.com/pin/api/search/?q=" + SearchText + "&token=" + CURRENT_USER_TOKEN;
        } else {
            this.URL = "http://social.rabtcdn.com/pin/api/search/?q=" + SearchText;
        }
        Log.e("URL", this.URL);
        new OkHttpClient().newCall(new Builder().url(this.URL).build()).enqueue(new C05912());
    }
}

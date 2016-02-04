package com.rokhgroup.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.rokhgroup.adapters.UserSearchAdapter;
import com.rokhgroup.adapters.item.UserSearchItem;
import com.rokhgroup.utils.LoadToast;
import com.rokhgroup.utils.RokhPref;
import com.shamchat.activity.ChatActivity;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class Followers extends AppCompatActivity {
    private static String CURRENT_USER_TOKEN = null;
    private static String FOLLOW_ID = null;
    private static String USER_ID = null;
    private static final String hostAddress = "http://social.rabtcdn.com/media/";
    boolean FOLLOW_W_USER;
    String NextPage;
    ArrayList<UserSearchItem> SearchDATA;
    RokhPref Session;
    String URL;
    String USER_AVATAR;
    String USER_NAME;
    JSONArray Users;
    private Intent intent;
    boolean isLoggedin;
    ListView list;
    private Context mContext;
    LoadToast mLoadToast;
    UserSearchAdapter mSAdapter;
    UserSearchItem mSearchItem;
    int preLast;
    Uri uri;

    /* renamed from: com.rokhgroup.activities.Followers.1 */
    class C05141 implements OnScrollListener {
        C05141() {
        }

        public final void onScrollStateChanged(AbsListView arg0, int arg1) {
        }

        public final void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            int lastItem = firstVisibleItem + visibleItemCount;
            if (lastItem >= totalItemCount && Followers.this.preLast != lastItem) {
                Followers.this.load(false, Followers.this.NextPage);
                Followers.this.preLast = lastItem;
            }
        }
    }

    /* renamed from: com.rokhgroup.activities.Followers.2 */
    class C05152 implements OnItemClickListener {
        C05152() {
        }

        public final void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            Followers.this.mSearchItem = (UserSearchItem) parent.getItemAtPosition(position);
            Intent intent = new Intent(Followers.this.mContext, UserProfile.class);
            intent.putExtra("USER_ID", Followers.this.mSearchItem.USER_ID);
            Followers.this.startActivity(intent);
        }
    }

    /* renamed from: com.rokhgroup.activities.Followers.3 */
    class C05193 implements Callback {

        /* renamed from: com.rokhgroup.activities.Followers.3.1 */
        class C05161 implements Runnable {
            C05161() {
            }

            public final void run() {
                Followers.this.mLoadToast.error();
            }
        }

        /* renamed from: com.rokhgroup.activities.Followers.3.2 */
        class C05172 implements Runnable {
            final /* synthetic */ JSONObject val$jsonResponse;

            C05172(JSONObject jSONObject) {
                this.val$jsonResponse = jSONObject;
            }

            public final void run() {
                try {
                    Followers.this.mLoadToast.success();
                    Followers.this.Users = this.val$jsonResponse.getJSONArray("objects");
                    for (int i = 0; i < Followers.this.Users.length(); i++) {
                        JSONObject Item = Followers.this.Users.getJSONObject(i);
                        Followers.this.USER_AVATAR = "http://social.rabtcdn.com" + Item.getString("user_avatar");
                        Followers.this.USER_NAME = Item.getString("user_name");
                        Followers.USER_ID = Item.getString(ChatActivity.INTENT_EXTRA_USER_ID);
                        Followers.this.FOLLOW_W_USER = Item.getBoolean("follow_by_user");
                        Followers.this.SearchDATA.add(new UserSearchItem(Followers.USER_ID, Followers.this.USER_NAME, Followers.this.USER_AVATAR, Followers.this.FOLLOW_W_USER));
                    }
                    if (Followers.this.NextPage == null) {
                        Followers.this.NextPage = "0";
                    }
                    int Nextpage = Integer.valueOf(Followers.this.NextPage).intValue() + 20;
                    Followers.this.NextPage = String.valueOf(Nextpage);
                    Followers.this.mSAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Followers.this.mLoadToast.error();
                }
            }
        }

        /* renamed from: com.rokhgroup.activities.Followers.3.3 */
        class C05183 implements Runnable {
            C05183() {
            }

            public final void run() {
                Followers.this.mLoadToast.error();
            }
        }

        C05193() {
        }

        public final void onFailure(Request request, IOException e) {
            Followers.this.runOnUiThread(new C05161());
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String stringResponse = response.body().string();
                    response.body().close();
                    System.out.println(stringResponse);
                    Followers.this.runOnUiThread(new C05172(new JSONObject(stringResponse)));
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                Followers.this.runOnUiThread(new C05183());
                e.printStackTrace();
            }
        }
    }

    public Followers() {
        this.SearchDATA = new ArrayList();
        this.Users = null;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903217);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(12);
        actionBar.setCustomView(2130903217);
        this.mContext = this;
        this.mLoadToast = new LoadToast(this.mContext);
        this.Session = new RokhPref(this.mContext);
        CURRENT_USER_TOKEN = this.Session.getUSERTOKEN();
        this.uri = getIntent().getData();
        this.mSAdapter = new UserSearchAdapter(this.mContext, this, this.SearchDATA, true);
        this.list = (ListView) findViewById(2131362484);
        this.list.setAdapter(this.mSAdapter);
        this.list.setOnScrollListener(new C05141());
        this.list.setOnItemClickListener(new C05152());
        if (this.uri == null) {
            this.intent = getIntent();
            FOLLOW_ID = this.intent.getStringExtra("FOLLOWER_ID");
            load(false, BuildConfig.VERSION_NAME);
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

    private void load(boolean refresh, String nextpage) {
        this.mLoadToast.setText("Retrieving data...").setProgressColor(getResources().getColor(2131230847)).show();
        String mNextPage = nextpage;
        if (nextpage == BuildConfig.VERSION_NAME) {
            if (this.isLoggedin) {
                this.URL = "http://social.rabtcdn.com/pin/api/follower/" + FOLLOW_ID + "/?token=" + CURRENT_USER_TOKEN + "&limit=20&offset=0";
            } else {
                this.URL = "http://social.rabtcdn.com/pin/api/follower/" + FOLLOW_ID + "/?limit=20&offset=0";
            }
        } else if (this.isLoggedin) {
            this.URL = "http://social.rabtcdn.com/pin/api/follower/" + FOLLOW_ID + "/?token=" + CURRENT_USER_TOKEN + "&limit=20&offset=" + mNextPage;
        } else {
            this.URL = "http://social.rabtcdn.com/pin/api/follower/" + FOLLOW_ID + "/?limit=20&offset=" + mNextPage;
        }
        Log.e("URL", this.URL);
        new OkHttpClient().newCall(new Builder().url(this.URL).build()).enqueue(new C05193());
    }
}

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
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class Likers extends AppCompatActivity {
    private static String CURRENT_USER_TOKEN = null;
    private static String POST_ID = null;
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

    /* renamed from: com.rokhgroup.activities.Likers.1 */
    class C05711 implements OnScrollListener {
        C05711() {
        }

        public final void onScrollStateChanged(AbsListView arg0, int arg1) {
        }

        public final void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            int lastItem = firstVisibleItem + visibleItemCount;
            if (lastItem >= totalItemCount && Likers.this.preLast != lastItem) {
                Likers.this.load(false, Likers.this.NextPage);
                Likers.this.preLast = lastItem;
            }
        }
    }

    /* renamed from: com.rokhgroup.activities.Likers.2 */
    class C05722 implements OnItemClickListener {
        C05722() {
        }

        public final void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            Likers.this.mSearchItem = (UserSearchItem) parent.getItemAtPosition(position);
            Intent intent = new Intent(Likers.this.mContext, UserProfile.class);
            intent.putExtra("USER_ID", Likers.this.mSearchItem.USER_ID);
            Likers.this.startActivity(intent);
        }
    }

    /* renamed from: com.rokhgroup.activities.Likers.3 */
    class C05763 implements Callback {

        /* renamed from: com.rokhgroup.activities.Likers.3.1 */
        class C05731 implements Runnable {
            C05731() {
            }

            public final void run() {
                Likers.this.mLoadToast.error();
            }
        }

        /* renamed from: com.rokhgroup.activities.Likers.3.2 */
        class C05742 implements Runnable {
            final /* synthetic */ JSONObject val$jsonResponse;

            C05742(JSONObject jSONObject) {
                this.val$jsonResponse = jSONObject;
            }

            public final void run() {
                try {
                    Likers.this.mLoadToast.success();
                    Likers.this.Users = this.val$jsonResponse.getJSONArray("objects");
                    for (int i = 0; i < Likers.this.Users.length(); i++) {
                        JSONObject Item = Likers.this.Users.getJSONObject(i);
                        Likers.this.USER_AVATAR = "http://social.rabtcdn.com" + Item.getString("user_avatar");
                        Likers.this.USER_NAME = Item.getString("user_name");
                        Likers.this.FOLLOW_W_USER = false;
                        Likers.USER_ID = Item.getString("user_url");
                        Likers.this.SearchDATA.add(new UserSearchItem(Likers.USER_ID, Likers.this.USER_NAME, Likers.this.USER_AVATAR, Likers.this.FOLLOW_W_USER));
                    }
                    if (Likers.this.NextPage == null) {
                        Likers.this.NextPage = "0";
                    }
                    int Nextpage = Integer.valueOf(Likers.this.NextPage).intValue() + 20;
                    Likers.this.NextPage = String.valueOf(Nextpage);
                    Likers.this.mSAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Likers.this.mLoadToast.error();
                }
            }
        }

        /* renamed from: com.rokhgroup.activities.Likers.3.3 */
        class C05753 implements Runnable {
            C05753() {
            }

            public final void run() {
                Likers.this.mLoadToast.error();
            }
        }

        C05763() {
        }

        public final void onFailure(Request request, IOException e) {
            Likers.this.runOnUiThread(new C05731());
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String stringResponse = response.body().string();
                    response.body().close();
                    System.out.println(stringResponse);
                    Likers.this.runOnUiThread(new C05742(new JSONObject(stringResponse)));
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                Likers.this.runOnUiThread(new C05753());
                e.printStackTrace();
            }
        }
    }

    public Likers() {
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
        this.mSAdapter = new UserSearchAdapter(this.mContext, this, this.SearchDATA, false);
        this.list = (ListView) findViewById(2131362484);
        this.list.setAdapter(this.mSAdapter);
        this.list.setOnScrollListener(new C05711());
        this.list.setOnItemClickListener(new C05722());
        if (this.uri == null) {
            this.intent = getIntent();
            POST_ID = this.intent.getStringExtra("POST_ID");
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
                this.URL = "http://social.rabtcdn.com/pin/api/like/likes/?post_id=" + POST_ID + "&token=" + CURRENT_USER_TOKEN + "&limit=20&offset=0";
            } else {
                this.URL = "http://social.rabtcdn.com/pin/api/like/likes/?post_id=" + POST_ID + "&limit=20&offset=0";
            }
        } else if (this.isLoggedin) {
            this.URL = "http://social.rabtcdn.com/pin/api/like/likes/?post_id=" + POST_ID + "&token=" + CURRENT_USER_TOKEN + "&limit=20&offset=" + mNextPage;
        } else {
            this.URL = "http://social.rabtcdn.com/pin/api/like/likes/?post_id=" + POST_ID + "&limit=20&offset=" + mNextPage;
        }
        Log.e("URL", this.URL);
        new OkHttpClient().newCall(new Builder().url(this.URL).build()).enqueue(new C05763());
    }
}

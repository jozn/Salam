package com.rokhgroup.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import com.rokhgroup.adapters.JahanbinItemAdapter;
import com.rokhgroup.adapters.item.JahanbinItem;
import com.rokhgroup.utils.LoadToast;
import com.rokhgroup.utils.RokhPref;
import com.rokhgroup.utils.Utils;
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

public class SearchInPost extends AppCompatActivity {
    private static String CURRENT_USER_TOKEN = null;
    private static String IMAGE_THUMB_URL = null;
    private static String IMAGE_URL = null;
    private static String POST_ID = null;
    private static String POST_TYPE = null;
    private static String SearchText = null;
    private static String USER_ID = null;
    private static final String hostAddress = "http://social.rabtcdn.com/media/";
    public JSONArray Images;
    ArrayList<JahanbinItem> JahanDATA;
    String NextPage;
    RokhPref Session;
    String URL;
    private Intent intent;
    boolean isLoggedin;
    private Context mContext;
    GridView mGridView;
    JahanbinItemAdapter mJAdapter;
    JahanbinItem mJahanbinItem;
    LoadToast mLoadToast;
    Yekantext noResult;
    int preLast;
    Uri uri;

    /* renamed from: com.rokhgroup.activities.SearchInPost.1 */
    class C05811 implements OnScrollListener {
        C05811() {
        }

        public final void onScrollStateChanged(AbsListView arg0, int arg1) {
        }

        public final void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            int lastItem = firstVisibleItem + visibleItemCount;
            if (lastItem >= totalItemCount && SearchInPost.this.preLast != lastItem) {
                SearchInPost.this.load(false, SearchInPost.this.NextPage);
                SearchInPost.this.preLast = lastItem;
            }
        }
    }

    /* renamed from: com.rokhgroup.activities.SearchInPost.2 */
    class C05822 implements OnItemClickListener {
        C05822() {
        }

        public final void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            SearchInPost.this.mJahanbinItem = (JahanbinItem) parent.getItemAtPosition(position);
            Intent intent = new Intent(SearchInPost.this.mContext, JahanbinDetailsActivity.class);
            intent.putExtra("POST_ID", SearchInPost.this.mJahanbinItem.POST_ID);
            intent.putExtra("USER_ID", SearchInPost.this.mJahanbinItem.USER_ID);
            SearchInPost.this.startActivity(intent);
        }
    }

    /* renamed from: com.rokhgroup.activities.SearchInPost.3 */
    class C05863 implements Callback {

        /* renamed from: com.rokhgroup.activities.SearchInPost.3.1 */
        class C05831 implements Runnable {
            C05831() {
            }

            public final void run() {
                SearchInPost.this.mLoadToast.error();
            }
        }

        /* renamed from: com.rokhgroup.activities.SearchInPost.3.2 */
        class C05842 implements Runnable {
            final /* synthetic */ JSONObject val$jsonResponse;

            C05842(JSONObject jSONObject) {
                this.val$jsonResponse = jSONObject;
            }

            public final void run() {
                try {
                    SearchInPost.this.mLoadToast.success();
                    SearchInPost.this.Images = this.val$jsonResponse.getJSONArray("objects");
                    if (SearchInPost.this.Images.length() > 0) {
                        for (int i = 0; i < SearchInPost.this.Images.length(); i++) {
                            JSONObject Item = SearchInPost.this.Images.getJSONObject(i);
                            int POST_TYPE_INT = Item.getInt("post_type");
                            if (POST_TYPE_INT == 2 || POST_TYPE_INT == 3) {
                                SearchInPost.IMAGE_THUMB_URL = new StringBuilder(SearchInPost.hostAddress).append(Item.getString("thumbnail")).toString();
                                SearchInPost.IMAGE_URL = new StringBuilder(SearchInPost.hostAddress).append(Item.getString("image")).toString();
                                SearchInPost.POST_ID = Item.getString("id");
                                SearchInPost.USER_ID = Item.getString("user");
                                SearchInPost.POST_TYPE = Item.getString("post_type");
                                SearchInPost.this.JahanDATA.add(new JahanbinItem(BuildConfig.VERSION_NAME, SearchInPost.IMAGE_URL, SearchInPost.IMAGE_THUMB_URL, SearchInPost.USER_ID, SearchInPost.POST_ID, SearchInPost.POST_TYPE));
                            }
                        }
                        if (SearchInPost.this.NextPage == BuildConfig.VERSION_NAME) {
                            SearchInPost.this.NextPage = "0";
                        }
                        int Nextpage = Integer.valueOf(SearchInPost.this.NextPage).intValue() + 20;
                        SearchInPost.this.NextPage = String.valueOf(Nextpage);
                        SearchInPost.this.mJAdapter.notifyDataSetChanged();
                        return;
                    }
                    Log.e("ARRAY list Size", String.valueOf(SearchInPost.this.JahanDATA.size()));
                    if (SearchInPost.this.JahanDATA.size() == 0) {
                        SearchInPost.this.noResult.setVisibility(0);
                        SearchInPost.this.mGridView.setVisibility(8);
                    }
                } catch (Exception e) {
                    SearchInPost.this.mLoadToast.error();
                }
            }
        }

        /* renamed from: com.rokhgroup.activities.SearchInPost.3.3 */
        class C05853 implements Runnable {
            C05853() {
            }

            public final void run() {
                SearchInPost.this.mLoadToast.error();
            }
        }

        C05863() {
        }

        public final void onFailure(Request request, IOException e) {
            SearchInPost.this.runOnUiThread(new C05831());
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String stringResponse = response.body().string();
                    response.body().close();
                    System.out.println(stringResponse);
                    JSONObject jsonResponse = new JSONObject(stringResponse);
                    Log.e("SEARCH RESULT", stringResponse);
                    SearchInPost.this.runOnUiThread(new C05842(jsonResponse));
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                SearchInPost.this.runOnUiThread(new C05853());
                e.printStackTrace();
            }
        }
    }

    public SearchInPost() {
        this.NextPage = BuildConfig.VERSION_NAME;
        this.JahanDATA = new ArrayList();
        this.Images = null;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903216);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(12);
        actionBar.setCustomView(2130903216);
        actionBar.setTitle(MainWindow.serch_titel.toString());
        this.noResult = (Yekantext) findViewById(2131362478);
        this.mContext = this;
        this.mLoadToast = new LoadToast(this.mContext);
        this.Session = new RokhPref(this.mContext);
        CURRENT_USER_TOKEN = this.Session.getUSERTOKEN();
        this.uri = getIntent().getData();
        this.mGridView = (GridView) findViewById(2131362483);
        getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());
        int width = (int) Utils.getWidthInPx(this.mContext);
        if (width > 480 && width < 780) {
            this.mGridView.setNumColumns(3);
        } else if (width <= 480) {
            this.mGridView.setNumColumns(3);
        } else if (width >= 780) {
            this.mGridView.setNumColumns(3);
        }
        this.mJAdapter = new JahanbinItemAdapter(this.mContext, this, this.JahanDATA, width / 3);
        this.mGridView.setAdapter(this.mJAdapter);
        this.mGridView.setOnScrollListener(new C05811());
        this.mGridView.setOnItemClickListener(new C05822());
        if (this.uri == null) {
            this.intent = getIntent();
            SearchText = this.intent.getStringExtra("SearchText");
            Log.e("SEARCH URL", "http://social.rabtcdn.com/pin/api/search/posts/?offset=0&q=" + SearchText);
        }
        load(false, BuildConfig.VERSION_NAME);
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
        if (nextpage != BuildConfig.VERSION_NAME) {
            if (this.isLoggedin) {
                this.URL = "http://social.rabtcdn.com/pin/api/hashtag/?offset=" + mNextPage + "&q=" + SearchText + "&token=" + CURRENT_USER_TOKEN;
            } else {
                this.URL = "http://social.rabtcdn.com/pin/api/hashtag/?offset=" + mNextPage + "&q=" + SearchText;
            }
        } else if (this.isLoggedin) {
            this.URL = "http://social.rabtcdn.com/pin/api/hashtag/?offset=0&q=" + SearchText + "&token=" + CURRENT_USER_TOKEN;
        } else {
            this.URL = "http://social.rabtcdn.com/pin/api/hashtag/?offset=0&q=" + SearchText;
        }
        Log.e("SEARCH URL", this.URL);
        new OkHttpClient().newCall(new Builder().url(this.URL).build()).enqueue(new C05863());
    }
}

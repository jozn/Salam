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
import com.shamchat.activity.Yekantext;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.ArrayList;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONArray;
import org.json.JSONObject;

public class HashTagActivity extends AppCompatActivity {
    private static String CURRENT_USER_TOKEN = null;
    private static String IMAGE_THUMB_URL = null;
    private static String IMAGE_URL = null;
    private static String POST_ID = null;
    private static String POST_TYPE = null;
    private static String USER_ID = null;
    private static final String hostAddress = "http://social.rabtcdn.com/media/";
    static Context mContext;
    public JSONArray Images;
    ArrayList<JahanbinItem> JahanDATA;
    String NextPage;
    RokhPref Session;
    String URL;
    String imageHeight;
    boolean isLoggedin;
    GridView mGridView;
    JahanbinItemAdapter mJAdapter;
    JahanbinItem mJahanbinItem;
    LoadToast mLoadToast;
    Yekantext noResult;
    int preLast;
    String tag;
    Uri uri;

    /* renamed from: com.rokhgroup.activities.HashTagActivity.1 */
    class C05261 implements OnScrollListener {
        C05261() {
        }

        public final void onScrollStateChanged(AbsListView arg0, int arg1) {
        }

        public final void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            int lastItem = firstVisibleItem + visibleItemCount;
            if (lastItem >= totalItemCount && HashTagActivity.this.preLast != lastItem) {
                HashTagActivity.this.load(false, HashTagActivity.this.NextPage);
                HashTagActivity.this.preLast = lastItem;
            }
        }
    }

    /* renamed from: com.rokhgroup.activities.HashTagActivity.2 */
    class C05272 implements OnItemClickListener {
        C05272() {
        }

        public final void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            HashTagActivity.this.mJahanbinItem = (JahanbinItem) parent.getItemAtPosition(position);
            Intent intent = new Intent(HashTagActivity.mContext, JahanbinDetailsActivity.class);
            intent.putExtra("POST_ID", HashTagActivity.this.mJahanbinItem.POST_ID);
            intent.putExtra("USER_ID", HashTagActivity.this.mJahanbinItem.USER_ID);
            HashTagActivity.this.startActivity(intent);
        }
    }

    /* renamed from: com.rokhgroup.activities.HashTagActivity.3 */
    class C05313 implements Callback {

        /* renamed from: com.rokhgroup.activities.HashTagActivity.3.1 */
        class C05281 implements Runnable {
            C05281() {
            }

            public final void run() {
                HashTagActivity.this.mLoadToast.error();
            }
        }

        /* renamed from: com.rokhgroup.activities.HashTagActivity.3.2 */
        class C05292 implements Runnable {
            final /* synthetic */ JSONObject val$jsonResponse;

            C05292(JSONObject jSONObject) {
                this.val$jsonResponse = jSONObject;
            }

            public final void run() {
                try {
                    HashTagActivity.this.mLoadToast.success();
                    HashTagActivity.this.Images = this.val$jsonResponse.getJSONArray("objects");
                    if (HashTagActivity.this.Images.length() > 0) {
                        for (int i = 0; i < HashTagActivity.this.Images.length(); i++) {
                            JSONObject Item = HashTagActivity.this.Images.getJSONObject(i);
                            HashTagActivity.IMAGE_THUMB_URL = new StringBuilder(HashTagActivity.hostAddress).append(Item.getString("thumbnail")).toString();
                            HashTagActivity.IMAGE_URL = new StringBuilder(HashTagActivity.hostAddress).append(Item.getString("image")).toString();
                            HashTagActivity.POST_ID = Item.getString("id");
                            HashTagActivity.USER_ID = Item.getString("user");
                            HashTagActivity.POST_TYPE = Item.getString("post_type");
                            HashTagActivity.this.JahanDATA.add(new JahanbinItem(BuildConfig.VERSION_NAME, HashTagActivity.IMAGE_URL, HashTagActivity.IMAGE_THUMB_URL, HashTagActivity.USER_ID, HashTagActivity.POST_ID, HashTagActivity.POST_TYPE));
                        }
                        if (HashTagActivity.this.NextPage == BuildConfig.VERSION_NAME) {
                            HashTagActivity.this.NextPage = "0";
                        }
                        int Nextpage = Integer.valueOf(HashTagActivity.this.NextPage).intValue() + 20;
                        HashTagActivity.this.NextPage = String.valueOf(Nextpage);
                        HashTagActivity.this.mJAdapter.notifyDataSetChanged();
                        HashTagActivity.this.JahanDATA.size();
                        return;
                    }
                    Log.e("ARRAY list Size", String.valueOf(HashTagActivity.this.JahanDATA.size()));
                    if (HashTagActivity.this.JahanDATA.size() == 0) {
                        HashTagActivity.this.noResult.setVisibility(0);
                        HashTagActivity.this.mGridView.setVisibility(8);
                    }
                } catch (Exception e) {
                    HashTagActivity.this.mLoadToast.error();
                }
            }
        }

        /* renamed from: com.rokhgroup.activities.HashTagActivity.3.3 */
        class C05303 implements Runnable {
            C05303() {
            }

            public final void run() {
                HashTagActivity.this.mLoadToast.error();
            }
        }

        C05313() {
        }

        public final void onFailure(Request request, IOException e) {
            HashTagActivity.this.runOnUiThread(new C05281());
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String stringResponse = response.body().string();
                    response.body().close();
                    System.out.println(stringResponse);
                    JSONObject jsonResponse = new JSONObject(stringResponse);
                    Log.e("HASHTAG RESULT", stringResponse);
                    HashTagActivity.this.runOnUiThread(new C05292(jsonResponse));
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                HashTagActivity.this.runOnUiThread(new C05303());
                e.printStackTrace();
            }
        }
    }

    public HashTagActivity() {
        this.JahanDATA = new ArrayList();
        this.Images = null;
        this.NextPage = BuildConfig.VERSION_NAME;
        this.isLoggedin = true;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903214);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(12);
        actionBar.setCustomView(2130903214);
        this.noResult = (Yekantext) findViewById(2131362478);
        mContext = this;
        this.Session = new RokhPref(mContext);
        CURRENT_USER_TOKEN = this.Session.getUSERTOKEN();
        this.uri = getIntent().getData();
        if (this.uri != null) {
            this.tag = this.uri.toString().split(MqttTopic.TOPIC_LEVEL_SEPARATOR)[3];
            this.tag = this.tag.startsWith(MqttTopic.MULTI_LEVEL_WILDCARD) ? this.tag.substring(1) : this.tag;
        }
        this.mLoadToast = new LoadToast(mContext);
        this.mGridView = (GridView) findViewById(2131362477);
        getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());
        int width = (int) Utils.getWidthInPx(mContext);
        if (width > 480 && width < 780) {
            this.mGridView.setNumColumns(3);
        } else if (width <= 480) {
            this.mGridView.setNumColumns(3);
        } else if (width >= 780) {
            this.mGridView.setNumColumns(3);
        }
        this.mJAdapter = new JahanbinItemAdapter(mContext, this, this.JahanDATA, width / 3);
        this.mGridView.setAdapter(this.mJAdapter);
        this.mGridView.setOnScrollListener(new C05261());
        this.mGridView.setOnItemClickListener(new C05272());
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
                this.URL = "http://social.rabtcdn.com/pin/api/hashtag/?offset=" + mNextPage + "&q=" + this.tag + "&token=" + CURRENT_USER_TOKEN;
            } else {
                this.URL = "http://social.rabtcdn.com/pin/api/hashtag/?offset=" + mNextPage + "&q=" + this.tag;
            }
        } else if (this.isLoggedin) {
            this.URL = "http://social.rabtcdn.com/pin/api/hashtag/?offset=0&q=" + this.tag + "&token=" + CURRENT_USER_TOKEN;
        } else {
            this.URL = "http://social.rabtcdn.com/pin/api/hashtag/?offset=0&q=" + this.tag;
        }
        Log.e("HASHTAG URL", this.URL);
        new OkHttpClient().newCall(new Builder().url(this.URL).build()).enqueue(new C05313());
    }
}

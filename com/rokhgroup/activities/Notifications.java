package com.rokhgroup.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import com.rokhgroup.adapters.NotificationAdapter;
import com.rokhgroup.adapters.item.NotificationItem;
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

public class Notifications extends AppCompatActivity {
    private static String CURRENT_USER_TOKEN = null;
    private static final Integer REQUEST_TAG;
    private static String USER_ID = null;
    private static final String hostAddress = "http://social.rabtcdn.com/media/";
    String NOTIF_TYPE;
    String NextPage;
    String POST_ID;
    String POST_IMAGE;
    String POST_TYPE;
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
    NotificationAdapter mNAdapter;
    NotificationItem mNotiItem;
    ArrayList<NotificationItem> notifDATA;
    JSONArray notifs;

    /* renamed from: com.rokhgroup.activities.Notifications.1 */
    class C05801 implements Callback {

        /* renamed from: com.rokhgroup.activities.Notifications.1.1 */
        class C05771 implements Runnable {
            C05771() {
            }

            public final void run() {
                if (Notifications.this.mLoadToast != null) {
                    Notifications.this.mLoadToast.error();
                }
            }
        }

        /* renamed from: com.rokhgroup.activities.Notifications.1.2 */
        class C05782 implements Runnable {
            final /* synthetic */ JSONObject val$jsonResponse;

            C05782(JSONObject jSONObject) {
                this.val$jsonResponse = jSONObject;
            }

            public final void run() {
                try {
                    if (Notifications.this.mLoadToast != null) {
                        Notifications.this.mLoadToast.success();
                    }
                    Notifications.this.notifs = this.val$jsonResponse.getJSONArray("objects");
                    for (int i = 0; i < Notifications.this.notifs.length(); i++) {
                        JSONObject Item = Notifications.this.notifs.getJSONObject(i);
                        Log.e("Item", Item.toString());
                        Notifications.this.NOTIF_TYPE = Item.getString("type");
                        JSONArray insidArray = (JSONArray) Item.getJSONArray("actors").get(0);
                        Notifications.USER_ID = insidArray.get(0).toString();
                        Notifications.this.USER_NAME = insidArray.get(1).toString();
                        Notifications.this.USER_AVATAR = "http://social.rabtcdn.com" + insidArray.get(2).toString();
                        Notifications.this.POST_ID = Item.getString("post_id");
                        Notifications.this.POST_IMAGE = new StringBuilder(Notifications.hostAddress).append(Item.getString("thumbnail")).toString();
                        Notifications.this.POST_TYPE = Item.getString("post_type");
                        Notifications.this.notifDATA.add(new NotificationItem(Notifications.USER_ID, Notifications.this.POST_ID, Notifications.this.POST_IMAGE, Notifications.this.USER_NAME, Notifications.this.USER_AVATAR, Notifications.this.NOTIF_TYPE, Notifications.this.POST_TYPE));
                    }
                    Notifications.this.mNAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    if (Notifications.this.mLoadToast != null) {
                        Notifications.this.mLoadToast.error();
                    }
                }
            }
        }

        /* renamed from: com.rokhgroup.activities.Notifications.1.3 */
        class C05793 implements Runnable {
            C05793() {
            }

            public final void run() {
                if (Notifications.this.mLoadToast != null) {
                    Notifications.this.mLoadToast.error();
                }
            }
        }

        C05801() {
        }

        public final void onFailure(Request request, IOException e) {
            Notifications.this.runOnUiThread(new C05771());
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String stringResponse = response.body().string();
                    response.body().close();
                    System.out.println(stringResponse);
                    Notifications.this.runOnUiThread(new C05782(new JSONObject(stringResponse)));
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                Notifications.this.runOnUiThread(new C05793());
                e.printStackTrace();
            }
        }
    }

    public Notifications() {
        this.isLoggedin = true;
        this.notifDATA = new ArrayList();
        this.Users = null;
    }

    static {
        REQUEST_TAG = Integer.valueOf(132435);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903217);
        this.mContext = this;
        this.mLoadToast = new LoadToast(this.mContext);
        this.Session = new RokhPref(this.mContext);
        CURRENT_USER_TOKEN = this.Session.getUSERTOKEN();
        this.mNAdapter = new NotificationAdapter(this.mContext, this, this.notifDATA);
        this.list = (ListView) findViewById(2131362484);
        this.list.setAdapter(this.mNAdapter);
        load();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(12);
        actionBar.setCustomView(2130903217);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                break;
        }
        return false;
    }

    private void load() {
        if (this.mLoadToast != null) {
            this.mLoadToast.setText("Retrieving data...").setProgressColor(getResources().getColor(2131230847)).show();
        }
        String URL = "http://social.rabtcdn.com/pin/api/notif/notify/?api_key=" + CURRENT_USER_TOKEN;
        Log.e("NOTIFICATIONS URL", URL);
        new OkHttpClient().newCall(new Builder().url(URL).tag(REQUEST_TAG).build()).enqueue(new C05801());
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mLoadToast = null;
        System.gc();
    }
}

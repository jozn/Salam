package com.rokhgroup.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.rokhgroup.adapters.JahanbinItemAdapter;
import com.rokhgroup.adapters.item.JahanbinItem;
import com.rokhgroup.utils.RokhPref;
import com.rokhgroup.utils.Utils;
import com.shamchat.activity.SettingsActivity;
import com.shamchat.activity.Yekantext;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.roundedimage.RoundedDrawable;
import com.shamchat.roundedimage.RoundedImageView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

public class UserProfile extends AppCompatActivity {
    private static String CURRENT_USER_ID = null;
    private static String CURRENT_USER_TOKEN = null;
    private static String IMAGE_THUMB_URL = null;
    private static String IMAGE_URL = null;
    private static String POST_ID = null;
    private static String POST_TYPE = null;
    private static final Integer REQUEST_TAG;
    private static String USER_ID = null;
    private static final String hostAddress = "http://social.rabtcdn.com/media/";
    public static String user_titel;
    private OnClickListener EditProfile;
    private boolean FollowState;
    private OnClickListener FollowUnfollow;
    String FollowerCount;
    String FollowingCount;
    public JSONArray Images;
    ArrayList<JahanbinItem> JahanDATA;
    String NextPage;
    String PostCount;
    String ProfileName;
    RokhPref Session;
    String URL;
    String UserAvatar;
    String UserName;
    ActionBar actionBar;
    Yekantext cntFollower;
    Yekantext cntFollowing;
    Yekantext cntPost;
    int displayHeight;
    int displayWidth;
    private OnClickListener goFollowers;
    private OnClickListener goFollowing;
    ImageView imgseting;
    private Intent intent;
    boolean isLoggedin;
    String locale;
    Yekantext mCaption;
    private Context mContext;
    ImageView mEditProfile;
    ImageView mFollow;
    GridView mGridView;
    JahanbinItemAdapter mJAdapter;
    JahanbinItem mJahanbinItem;
    LinearLayout mLoading;
    private final Transformation mTransformation;
    private MenuItem myActionMenuItem;
    private TextView myActiontext;
    int preLast;
    ImageView setingg;
    String tag;
    Uri uri;
    RoundedImageView userAvatar;
    Yekantext userName;

    /* renamed from: com.rokhgroup.activities.UserProfile.1 */
    class C06101 implements OnClickListener {
        C06101() {
        }

        public final void onClick(View v) {
            UserProfile.this.startActivity(new Intent(UserProfile.this.mContext, SettingsActivity.class));
        }
    }

    /* renamed from: com.rokhgroup.activities.UserProfile.2 */
    class C06112 implements OnScrollListener {
        C06112() {
        }

        public final void onScrollStateChanged(AbsListView arg0, int arg1) {
        }

        public final void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            int lastItem = firstVisibleItem + visibleItemCount;
            if (lastItem >= totalItemCount && UserProfile.this.preLast != lastItem) {
                UserProfile.this.load(false, UserProfile.this.NextPage);
                UserProfile.this.preLast = lastItem;
            }
        }
    }

    /* renamed from: com.rokhgroup.activities.UserProfile.3 */
    class C06123 implements OnItemClickListener {
        C06123() {
        }

        public final void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            UserProfile.this.mJahanbinItem = (JahanbinItem) parent.getItemAtPosition(position);
            Intent intent = new Intent(UserProfile.this.mContext, JahanbinDetailsActivity.class);
            intent.putExtra("POST_ID", UserProfile.this.mJahanbinItem.POST_ID);
            intent.putExtra("USER_ID", UserProfile.this.mJahanbinItem.USER_ID);
            intent.putExtra("POST_TYPE", UserProfile.this.mJahanbinItem.Post_TYPE);
            UserProfile.this.startActivity(intent);
        }
    }

    /* renamed from: com.rokhgroup.activities.UserProfile.4 */
    class C06164 implements Callback {

        /* renamed from: com.rokhgroup.activities.UserProfile.4.1 */
        class C06131 implements Runnable {
            C06131() {
            }

            public final void run() {
            }
        }

        /* renamed from: com.rokhgroup.activities.UserProfile.4.2 */
        class C06142 implements Runnable {
            final /* synthetic */ JSONObject val$jsonResponse;

            C06142(JSONObject jSONObject) {
                this.val$jsonResponse = jSONObject;
            }

            public final void run() {
                try {
                    JSONArray Details = this.val$jsonResponse.getJSONArray("objects");
                    if (Details.length() > 0) {
                        for (int i = 0; i < Details.length(); i++) {
                            JSONObject Item = Details.getJSONObject(i);
                            UserProfile.this.UserName = Item.getString("user_name");
                            UserProfile.this.actionBar.setTitle(UserProfile.this.UserName);
                            UserProfile.user_titel = UserProfile.this.UserName.toString();
                            UserProfile.this.ProfileName = Item.getString("name");
                            UserProfile.this.FollowerCount = Item.getString("cnt_followers");
                            UserProfile.this.FollowingCount = Item.getString("cnt_following");
                            UserProfile.this.FollowState = Item.getBoolean("follow_by_user");
                            UserProfile.this.PostCount = Item.getString("cnt_post");
                            UserProfile.this.UserAvatar = "http://social.rabtcdn.com" + Item.getString("user_avatar");
                            UserProfile.this.mCaption.setText(UserProfile.this.UserName);
                            if (UserProfile.USER_ID.equals(UserProfile.CURRENT_USER_ID)) {
                                UserProfile.this.mFollow.setVisibility(8);
                                UserProfile.this.mEditProfile.setVisibility(0);
                            } else {
                                UserProfile.this.mEditProfile.setVisibility(8);
                                UserProfile.this.mFollow.setVisibility(0);
                            }
                            if (UserProfile.this.FollowState) {
                                if (UserProfile.this.locale.equals("fa_IR")) {
                                    UserProfile.this.mFollow.setBackgroundResource(2130837870);
                                } else {
                                    UserProfile.this.mFollow.setBackgroundResource(2130837869);
                                }
                            } else if (UserProfile.this.locale.equals("fa_IR")) {
                                UserProfile.this.mFollow.setBackgroundResource(2130837868);
                            } else {
                                UserProfile.this.mFollow.setBackgroundResource(2130837867);
                            }
                            UserProfile.this.mFollow.setOnClickListener(UserProfile.this.FollowUnfollow);
                            UserProfile.this.mEditProfile.setOnClickListener(UserProfile.this.EditProfile);
                            UserProfile.this.userName.setText(UserProfile.this.ProfileName);
                            UserProfile.this.cntPost.setText(UserProfile.this.PostCount);
                            UserProfile.this.cntFollower.setText(UserProfile.this.FollowerCount);
                            UserProfile.this.cntFollowing.setText(UserProfile.this.FollowingCount);
                            RequestCreator load = Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(UserProfile.this.UserAvatar);
                            load.deferred = true;
                            load.transform(UserProfile.this.mTransformation).into(UserProfile.this.userAvatar, null);
                            Editor edit = SHAMChatApplication.getMyApplicationContext().getSharedPreferences("picture", 0).edit();
                            edit.putString("picture", UserProfile.this.UserAvatar.toString());
                            edit.apply();
                            UserProfile.this.mLoading.setVisibility(8);
                            UserProfile.this.load(false, BuildConfig.VERSION_NAME);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }

        /* renamed from: com.rokhgroup.activities.UserProfile.4.3 */
        class C06153 implements Runnable {
            C06153() {
            }

            public final void run() {
            }
        }

        C06164() {
        }

        public final void onFailure(Request request, IOException e) {
            UserProfile.this.runOnUiThread(new C06131());
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String stringResponse = response.body().string();
                    response.body().close();
                    System.out.println(stringResponse);
                    UserProfile.this.runOnUiThread(new C06142(new JSONObject(stringResponse)));
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                UserProfile.this.runOnUiThread(new C06153());
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.rokhgroup.activities.UserProfile.5 */
    class C06215 implements OnClickListener {

        /* renamed from: com.rokhgroup.activities.UserProfile.5.1 */
        class C06201 implements Callback {

            /* renamed from: com.rokhgroup.activities.UserProfile.5.1.1 */
            class C06171 implements Runnable {
                C06171() {
                }

                public final void run() {
                }
            }

            /* renamed from: com.rokhgroup.activities.UserProfile.5.1.2 */
            class C06182 implements Runnable {
                final /* synthetic */ String val$stringResponse;

                C06182(String str) {
                    this.val$stringResponse = str;
                }

                public final void run() {
                    try {
                        System.out.println(this.val$stringResponse);
                        if (!this.val$stringResponse.equals("1")) {
                            return;
                        }
                        if (UserProfile.this.FollowState) {
                            UserProfile.this.FollowState = false;
                            if (UserProfile.this.locale.equals("fa_IR")) {
                                UserProfile.this.mFollow.setBackgroundResource(2130837868);
                                return;
                            } else {
                                UserProfile.this.mFollow.setBackgroundResource(2130837867);
                                return;
                            }
                        }
                        UserProfile.this.FollowState = true;
                        if (UserProfile.this.locale.equals("fa_IR")) {
                            UserProfile.this.mFollow.setBackgroundResource(2130837870);
                        } else {
                            UserProfile.this.mFollow.setBackgroundResource(2130837869);
                        }
                    } catch (Exception e) {
                    }
                }
            }

            /* renamed from: com.rokhgroup.activities.UserProfile.5.1.3 */
            class C06193 implements Runnable {
                C06193() {
                }

                public final void run() {
                }
            }

            C06201() {
            }

            public final void onFailure(Request request, IOException e) {
                UserProfile.this.runOnUiThread(new C06171());
                e.printStackTrace();
            }

            public final void onResponse(Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String stringResponse = response.body().string();
                        response.body().close();
                        System.out.println(stringResponse);
                        UserProfile.this.runOnUiThread(new C06182(stringResponse));
                        return;
                    }
                    throw new IOException("Unexpected code " + response);
                } catch (Exception e) {
                    UserProfile.this.runOnUiThread(new C06193());
                    e.printStackTrace();
                }
            }
        }

        C06215() {
        }

        public final void onClick(View v) {
            String URL;
            System.out.println("Was Clicked!");
            if (UserProfile.this.FollowState) {
                URL = "http://social.rabtcdn.com/pin/d/follow/" + UserProfile.USER_ID + "/0/?token=" + UserProfile.CURRENT_USER_TOKEN;
            } else {
                URL = "http://social.rabtcdn.com/pin/d/follow/" + UserProfile.USER_ID + "/1/?token=" + UserProfile.CURRENT_USER_TOKEN;
            }
            System.out.println(URL);
            new OkHttpClient().newCall(new Builder().url(URL).build()).enqueue(new C06201());
        }
    }

    /* renamed from: com.rokhgroup.activities.UserProfile.6 */
    class C06226 implements OnClickListener {
        C06226() {
        }

        public final void onClick(View arg0) {
            Intent i = new Intent(UserProfile.this, EditProfile.class);
            i.putExtra("USER_AVATAR", UserProfile.this.UserAvatar);
            i.putExtra("USER_NAME", UserProfile.this.ProfileName);
            i.putExtra("USER_ID", UserProfile.USER_ID);
            UserProfile.this.startActivity(i);
        }
    }

    /* renamed from: com.rokhgroup.activities.UserProfile.7 */
    class C06267 implements Callback {

        /* renamed from: com.rokhgroup.activities.UserProfile.7.1 */
        class C06231 implements Runnable {
            C06231() {
            }

            public final void run() {
            }
        }

        /* renamed from: com.rokhgroup.activities.UserProfile.7.2 */
        class C06242 implements Runnable {
            final /* synthetic */ JSONObject val$jsonResponse;

            C06242(JSONObject jSONObject) {
                this.val$jsonResponse = jSONObject;
            }

            public final void run() {
                try {
                    UserProfile.this.Images = this.val$jsonResponse.getJSONArray("objects");
                    if (UserProfile.this.Images.length() > 0) {
                        for (int i = 0; i < UserProfile.this.Images.length(); i++) {
                            JSONObject Item = UserProfile.this.Images.getJSONObject(i);
                            UserProfile.IMAGE_THUMB_URL = new StringBuilder(UserProfile.hostAddress).append(Item.getString("thumbnail")).toString();
                            UserProfile.IMAGE_URL = new StringBuilder(UserProfile.hostAddress).append(Item.getString("image")).toString();
                            UserProfile.POST_ID = Item.getString("id");
                            UserProfile.USER_ID = Item.getString("user");
                            UserProfile.POST_TYPE = Item.getString("post_type");
                            UserProfile.this.JahanDATA.add(new JahanbinItem(BuildConfig.VERSION_NAME, UserProfile.IMAGE_URL, UserProfile.IMAGE_THUMB_URL, UserProfile.USER_ID, UserProfile.POST_ID, UserProfile.POST_TYPE));
                            if (i == UserProfile.this.Images.length() - 1) {
                                UserProfile.this.NextPage = Item.getString("id");
                            }
                        }
                        UserProfile.this.mJAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                }
            }
        }

        /* renamed from: com.rokhgroup.activities.UserProfile.7.3 */
        class C06253 implements Runnable {
            C06253() {
            }

            public final void run() {
            }
        }

        C06267() {
        }

        public final void onFailure(Request request, IOException e) {
            UserProfile.this.runOnUiThread(new C06231());
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    Log.e("PROFILE RESPONSE", response.toString());
                    String stringResponse = response.body().string();
                    response.body().close();
                    System.out.println(stringResponse);
                    UserProfile.this.runOnUiThread(new C06242(new JSONObject(stringResponse)));
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                UserProfile.this.runOnUiThread(new C06253());
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.rokhgroup.activities.UserProfile.8 */
    class C06278 implements OnClickListener {
        C06278() {
        }

        public final void onClick(View v) {
            Intent i = new Intent(UserProfile.this, Followers.class);
            i.putExtra("FOLLOWER_ID", UserProfile.USER_ID);
            UserProfile.this.startActivity(i);
        }
    }

    /* renamed from: com.rokhgroup.activities.UserProfile.9 */
    class C06289 implements OnClickListener {
        C06289() {
        }

        public final void onClick(View v) {
            Intent i = new Intent(UserProfile.this, Following.class);
            i.putExtra("FOLLOWER_ID", UserProfile.USER_ID);
            UserProfile.this.startActivity(i);
        }
    }

    public UserProfile() {
        this.JahanDATA = new ArrayList();
        this.Images = null;
        this.FollowUnfollow = new C06215();
        this.EditProfile = new C06226();
        this.goFollowers = new C06278();
        this.goFollowing = new C06289();
        this.mTransformation = new Transformation() {
            final boolean oval;
            final float radius;

            {
                this.radius = TypedValue.applyDimension(1, 200.0f, SHAMChatApplication.getMyApplicationContext().getResources().getDisplayMetrics());
                this.oval = false;
            }

            public final Bitmap transform(Bitmap bitmap) {
                Drawable fromBitmap = RoundedDrawable.fromBitmap(bitmap);
                fromBitmap.mCornerRadius = this.radius;
                fromBitmap.mOval = false;
                Bitmap transformed = RoundedDrawable.drawableToBitmap(fromBitmap);
                if (!bitmap.equals(transformed)) {
                    bitmap.recycle();
                }
                return transformed;
            }

            public final String key() {
                return "rounded_radius_" + this.radius + "_oval_false";
            }
        };
    }

    static {
        REQUEST_TAG = Integer.valueOf(132435);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903221);
        this.setingg = (ImageView) findViewById(2131362506);
        this.setingg.setVisibility(8);
        this.mContext = this;
        this.setingg.setOnClickListener(new C06101());
        this.actionBar = getSupportActionBar();
        this.actionBar.setDisplayOptions(28);
        this.actionBar.setDisplayHomeAsUpEnabled(true);
        View cView = getLayoutInflater().inflate(2130903126, null);
        this.actionBar.setCustomView(cView);
        this.locale = Locale.getDefault().toString();
        this.mCaption = (Yekantext) cView.findViewById(2131361839);
        this.Session = new RokhPref(this.mContext);
        CURRENT_USER_ID = this.Session.getUSERID();
        CURRENT_USER_TOKEN = this.Session.getUSERTOKEN();
        this.mLoading = (LinearLayout) findViewById(2131362500);
        this.uri = getIntent().getData();
        this.mJAdapter = new JahanbinItemAdapter(this.mContext, this, this.JahanDATA, this.displayHeight);
        this.cntPost = (Yekantext) findViewById(2131362502);
        this.cntFollower = (Yekantext) findViewById(2131362501);
        this.cntFollower.setOnClickListener(this.goFollowers);
        this.cntFollowing = (Yekantext) findViewById(2131362503);
        this.cntFollowing.setOnClickListener(this.goFollowing);
        this.userName = (Yekantext) findViewById(2131362397);
        this.userAvatar = (RoundedImageView) findViewById(2131361914);
        this.mFollow = (ImageView) findViewById(2131362504);
        this.mEditProfile = (ImageView) findViewById(2131362505);
        if (this.locale.equals("fa_IR")) {
            this.mEditProfile.setImageResource(2130837763);
        } else {
            this.mEditProfile.setImageResource(2130837762);
        }
        this.mEditProfile.setVisibility(8);
        this.mGridView = (GridView) findViewById(2131362044);
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
        this.mGridView.setOnScrollListener(new C06112());
        this.mGridView.setOnItemClickListener(new C06123());
        if (this.uri == null) {
            this.intent = getIntent();
            USER_ID = this.intent.getStringExtra("USER_ID");
            new OkHttpClient().newCall(new Builder().url("http://social.rabtcdn.com/pin/api/profile/profile/?token=" + CURRENT_USER_TOKEN + "&user=" + USER_ID + "&format=json").tag(REQUEST_TAG).build()).enqueue(new C06164());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(2131623947, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                break;
            case 2131362568:
                startActivity(new Intent(this.mContext, SettingsActivity.class));
                break;
        }
        return false;
    }

    private void load(boolean refresh, String nextpage) {
        String mNextPage = nextpage;
        if (nextpage != BuildConfig.VERSION_NAME) {
            if (this.isLoggedin) {
                this.URL = "http://social.rabtcdn.com/pin/api/post/?token=" + CURRENT_USER_TOKEN + "&user_id=" + USER_ID + "&before=" + mNextPage;
            } else {
                this.URL = "http://social.rabtcdn.com/pin/api/post/?user_id=" + USER_ID + "&before=" + mNextPage;
            }
        } else if (this.isLoggedin) {
            this.URL = "http://social.rabtcdn.com/pin/api/post/?token=" + CURRENT_USER_TOKEN + "&user_id=" + USER_ID;
        } else {
            this.URL = "http://social.rabtcdn.com/pin/api/post/?user_id=" + USER_ID;
        }
        Log.e("PROFILE URL", this.URL);
        new OkHttpClient().newCall(new Builder().url(this.URL).build()).enqueue(new C06267());
    }

    protected void onStop() {
        super.onStop();
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if ((view instanceof ViewGroup) && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        unbindDrawables(findViewById(2131362044));
        unbindDrawables(findViewById(2131361914));
        unbindDrawables(findViewById(2131361913));
        this.mGridView = null;
        this.userAvatar = null;
        Picasso.with(this.mContext).cancelExistingRequest(this.userAvatar);
        System.gc();
    }
}

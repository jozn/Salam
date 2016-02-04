package com.rokhgroup.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.rokhgroup.activities.JahanbinDetailsActivity;
import com.rokhgroup.adapters.StreamAdapter;
import com.rokhgroup.adapters.item.StreamItem;
import com.rokhgroup.utils.RokhPref;
import com.shamchat.activity.AddFavoriteTextActivity;
import com.shamchat.activity.Yekantext;
import com.shamchat.androidclient.SHAMChatApplication;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class UserStreamFragment extends Fragment {
    static Context mContext;
    String CURRENT_USER_TOKEN;
    private String IMAGE_THUMB_URL;
    String NextPage;
    private String POST_CMNT_CNT;
    private String POST_DATE;
    private String POST_ID;
    private String POST_LIKE_CNT;
    private String POST_LIKE_W_USER;
    private String POST_TEXT;
    private String POST_TYPE;
    RokhPref Session;
    ArrayList<StreamItem> StreamData;
    String URL;
    private String USER_AVATAR;
    private String USER_ID;
    private String USER_NAME;
    boolean isLoggedin;
    ListView list;
    StreamAdapter mAdapter;
    public JSONArray mPosts;
    StreamItem mStreamItem;
    Yekantext noResult;
    int preLast;

    /* renamed from: com.rokhgroup.fragments.UserStreamFragment.1 */
    class C06661 implements OnScrollListener {
        C06661() {
        }

        public final void onScrollStateChanged(AbsListView arg0, int arg1) {
        }

        public final void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            int lastItem = firstVisibleItem + visibleItemCount;
            if (lastItem >= totalItemCount && UserStreamFragment.this.preLast != lastItem) {
                UserStreamFragment.this.load(false, UserStreamFragment.this.NextPage);
                UserStreamFragment.this.preLast = lastItem;
            }
        }
    }

    /* renamed from: com.rokhgroup.fragments.UserStreamFragment.2 */
    class C06672 implements OnItemClickListener {
        C06672() {
        }

        public final void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            UserStreamFragment.this.mStreamItem = (StreamItem) parent.getItemAtPosition(position);
            Intent intent = new Intent(UserStreamFragment.mContext, JahanbinDetailsActivity.class);
            intent.putExtra("POST_ID", UserStreamFragment.this.mStreamItem.POST_ID);
            intent.putExtra("USER_ID", UserStreamFragment.this.mStreamItem.USER_ID);
            intent.putExtra("POST_TYPE", UserStreamFragment.this.mStreamItem.POST_TYPE);
            UserStreamFragment.this.startActivity(intent);
        }
    }

    /* renamed from: com.rokhgroup.fragments.UserStreamFragment.3 */
    class C06713 implements Callback {
        final /* synthetic */ boolean val$ReforNormal;

        /* renamed from: com.rokhgroup.fragments.UserStreamFragment.3.1 */
        class C06681 implements Runnable {
            C06681() {
            }

            public final void run() {
            }
        }

        /* renamed from: com.rokhgroup.fragments.UserStreamFragment.3.2 */
        class C06692 implements Runnable {
            final /* synthetic */ JSONObject val$jsonResponse;

            C06692(JSONObject jSONObject) {
                this.val$jsonResponse = jSONObject;
            }

            public final void run() {
                UserStreamFragment.this.noResult.setVisibility(8);
                UserStreamFragment.this.list.setVisibility(0);
                Log.e("****************RESULT STREAM", this.val$jsonResponse.toString());
                if (C06713.this.val$ReforNormal) {
                    UserStreamFragment.this.StreamData.clear();
                    UserStreamFragment.this.StreamData.removeAll(UserStreamFragment.this.StreamData);
                    StreamAdapter streamAdapter = UserStreamFragment.this.mAdapter;
                    streamAdapter.mData.clear();
                    streamAdapter.notifyDataSetChanged();
                    UserStreamFragment.this.mAdapter.notifyDataSetChanged();
                }
                try {
                    UserStreamFragment.this.mPosts = this.val$jsonResponse.getJSONArray("objects");
                    if (UserStreamFragment.this.mPosts.length() > 0) {
                        for (int i = 0; i < UserStreamFragment.this.mPosts.length(); i++) {
                            JSONObject Item = UserStreamFragment.this.mPosts.getJSONObject(i);
                            UserStreamFragment.this.IMAGE_THUMB_URL = "http://social.rabtcdn.com/media/" + Item.getString("thumbnail");
                            UserStreamFragment.this.USER_ID = Item.getString("user");
                            UserStreamFragment.this.USER_NAME = Item.getString("user_name");
                            UserStreamFragment.this.USER_AVATAR = "http://social.rabtcdn.com" + Item.getString("user_avatar");
                            UserStreamFragment.this.POST_ID = Item.getString("id");
                            UserStreamFragment.this.POST_DATE = Item.getString("timestamp");
                            UserStreamFragment.this.POST_LIKE_CNT = Item.getString("like");
                            UserStreamFragment.this.POST_LIKE_W_USER = Item.getString("like_with_user");
                            UserStreamFragment.this.POST_CMNT_CNT = Item.getString("cnt_comment");
                            UserStreamFragment.this.POST_TEXT = Item.getString(AddFavoriteTextActivity.EXTRA_RESULT_TEXT);
                            UserStreamFragment.this.POST_TYPE = Item.getString("post_type");
                            UserStreamFragment.this.StreamData.add(new StreamItem(UserStreamFragment.this.POST_ID, UserStreamFragment.this.IMAGE_THUMB_URL, UserStreamFragment.this.USER_ID, UserStreamFragment.this.USER_NAME, UserStreamFragment.this.USER_AVATAR, UserStreamFragment.this.POST_DATE, UserStreamFragment.this.POST_LIKE_CNT, UserStreamFragment.this.POST_LIKE_W_USER, UserStreamFragment.this.POST_CMNT_CNT, UserStreamFragment.this.POST_TEXT, UserStreamFragment.this.POST_TYPE));
                            if (i == UserStreamFragment.this.mPosts.length() - 1) {
                                UserStreamFragment.this.NextPage = Item.getString("id");
                            }
                        }
                        UserStreamFragment.this.mAdapter.notifyDataSetChanged();
                        return;
                    }
                    Log.e("ARRAY list Size", String.valueOf(UserStreamFragment.this.StreamData.size()));
                    if (UserStreamFragment.this.StreamData.size() == 0) {
                        UserStreamFragment.this.noResult.setVisibility(0);
                        UserStreamFragment.this.list.setVisibility(8);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /* renamed from: com.rokhgroup.fragments.UserStreamFragment.3.3 */
        class C06703 implements Runnable {
            C06703() {
            }

            public final void run() {
            }
        }

        C06713(boolean z) {
            this.val$ReforNormal = z;
        }

        public final void onFailure(Request request, IOException e) {
            if (UserStreamFragment.this.getActivity() != null) {
                UserStreamFragment.this.getActivity().runOnUiThread(new C06681());
                e.printStackTrace();
            }
        }

        public final void onResponse(Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String stringResponse = response.body().string();
                    response.body().close();
                    System.out.println(stringResponse);
                    JSONObject jsonResponse = new JSONObject(stringResponse);
                    if (UserStreamFragment.this.getActivity() != null) {
                        UserStreamFragment.this.getActivity().runOnUiThread(new C06692(jsonResponse));
                        return;
                    }
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                if (UserStreamFragment.this.getActivity() != null) {
                    UserStreamFragment.this.getActivity().runOnUiThread(new C06703());
                    e.printStackTrace();
                }
            }
        }
    }

    public UserStreamFragment() {
        this.StreamData = new ArrayList();
        this.mPosts = null;
        this.isLoggedin = true;
    }

    public static UserStreamFragment newInstance(Context context) {
        return new UserStreamFragment();
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = SHAMChatApplication.getMyApplicationContext();
        this.mAdapter = new StreamAdapter(mContext, getActivity(), this.StreamData);
        View fView = inflater.inflate(2130903224, container, false);
        mContext = fView.getContext();
        this.Session = new RokhPref(mContext);
        this.CURRENT_USER_TOKEN = this.Session.getUSERTOKEN();
        this.list = (ListView) fView.findViewById(2131362511);
        this.noResult = (Yekantext) fView.findViewById(2131362512);
        this.list.setAdapter(this.mAdapter);
        this.list.setOnScrollListener(new C06661());
        this.list.setOnItemClickListener(new C06672());
        load(false, BuildConfig.VERSION_NAME);
        return fView;
    }

    private void load(boolean refresh, String nextpage) {
        String mNextPage = nextpage;
        if (nextpage != BuildConfig.VERSION_NAME) {
            if (this.isLoggedin) {
                this.URL = "http://social.rabtcdn.com/pin/api/friends_post/?token=" + this.CURRENT_USER_TOKEN + "&before=" + mNextPage + "&timeStamp=" + System.currentTimeMillis();
            } else {
                this.URL = "http://social.rabtcdn.com/pin/api/friends_post/?before=" + mNextPage + "&timeStamp=" + System.currentTimeMillis();
            }
        } else if (this.isLoggedin) {
            this.URL = "http://social.rabtcdn.com/pin/api/friends_post/?token=" + this.CURRENT_USER_TOKEN + "&timeStamp=" + System.currentTimeMillis();
        } else {
            this.URL = "http://social.rabtcdn.com/pin/api/friends_post/&timeStamp=" + System.currentTimeMillis();
        }
        Log.e("****************URL STREAM", this.URL);
        new OkHttpClient().newCall(new Builder().url(this.URL).build()).enqueue(new C06713(refresh));
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisible() && isVisibleToUser) {
            load(true, BuildConfig.VERSION_NAME);
            this.preLast = 0;
        }
    }
}

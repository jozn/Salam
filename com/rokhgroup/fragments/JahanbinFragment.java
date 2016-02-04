package com.rokhgroup.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.appcompat.BuildConfig;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import com.rokhgroup.activities.JahanbinDetailsActivity;
import com.rokhgroup.adapters.JahanbinItemAdapter;
import com.rokhgroup.adapters.item.JahanbinItem;
import com.rokhgroup.fragments.ShakeListener.OnShakeListener;
import com.rokhgroup.utils.RokhPref;
import com.rokhgroup.utils.Utils;
import com.shamchat.activity.MainWindow;
import com.shamchat.androidclient.SHAMChatApplication;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.ArrayList;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JahanbinFragment extends Fragment {
    private static String CURRENT_USER_TOKEN = null;
    private static String IMAGE_THUMB_URL = null;
    private static String IMAGE_URL = null;
    private static String POST_ID = null;
    private static String POST_TYPE = null;
    private static String USER_ID = null;
    private static final String hostAddress = "http://social.rabtcdn.com";
    static Context mContext;
    public JSONArray Images;
    ArrayList<JahanbinItem> JahanDATA;
    String NextPage;
    RokhPref Session;
    String URL;
    Sensor accelerometerSensor;
    Animation animFadein;
    String imageHeight;
    boolean isLoggedin;
    GridView mGridView;
    JahanbinItemAdapter mJAdapter;
    JahanbinItem mJahanbinItem;
    private ShakeListener mShaker;
    int preLast;
    private int savedGridPosition;
    Vibrator vibe;

    /* renamed from: com.rokhgroup.fragments.JahanbinFragment.1 */
    class C06591 implements OnScrollListener {
        C06591() {
        }

        public final void onScrollStateChanged(AbsListView arg0, int arg1) {
        }

        public final void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            int lastItem = firstVisibleItem + visibleItemCount;
            if (lastItem >= totalItemCount && JahanbinFragment.this.preLast != lastItem) {
                JahanbinFragment.this.load(false, JahanbinFragment.this.NextPage);
                JahanbinFragment.this.preLast = lastItem;
            }
        }
    }

    /* renamed from: com.rokhgroup.fragments.JahanbinFragment.2 */
    class C06602 implements OnShakeListener {
        final /* synthetic */ View val$fView;

        C06602(View view) {
            this.val$fView = view;
        }

        public final void onShake() {
            if (MainWindow.shake == 1) {
                MediaPlayer.create(JahanbinFragment.mContext, 2131034117).start();
                this.val$fView.startAnimation(JahanbinFragment.this.animFadein);
                JahanbinFragment.this.vibe.vibrate(new long[]{0, 200, 0, 100}, -1);
                JahanbinFragment.this.load(true, BuildConfig.VERSION_NAME);
                Toast.makeText(JahanbinFragment.mContext, "\u062a\u0635\u0627\u0648\u06cc\u0631 \u0628\u0647 \u0631\u0648\u0632\u0631\u0633\u0627\u0646\u06cc \u0634\u062f", 0).show();
            }
        }
    }

    /* renamed from: com.rokhgroup.fragments.JahanbinFragment.3 */
    class C06613 implements OnItemClickListener {
        C06613() {
        }

        public final void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            JahanbinFragment.this.mJahanbinItem = (JahanbinItem) parent.getItemAtPosition(position);
            Intent intent = new Intent(JahanbinFragment.mContext, JahanbinDetailsActivity.class);
            intent.putExtra("POST_ID", JahanbinFragment.this.mJahanbinItem.POST_ID);
            intent.putExtra("USER_ID", JahanbinFragment.this.mJahanbinItem.USER_ID);
            intent.putExtra("POST_TYPE", JahanbinFragment.this.mJahanbinItem.Post_TYPE);
            intent.addFlags(67108864);
            intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
            JahanbinFragment.this.startActivity(intent);
        }
    }

    /* renamed from: com.rokhgroup.fragments.JahanbinFragment.4 */
    class C06654 implements Callback {
        final /* synthetic */ boolean val$ReforNormal;

        /* renamed from: com.rokhgroup.fragments.JahanbinFragment.4.1 */
        class C06621 implements Runnable {
            C06621() {
            }

            public final void run() {
            }
        }

        /* renamed from: com.rokhgroup.fragments.JahanbinFragment.4.2 */
        class C06632 implements Runnable {
            final /* synthetic */ JSONObject val$jsonResponse;

            C06632(JSONObject jSONObject) {
                this.val$jsonResponse = jSONObject;
            }

            public final void run() {
                Log.e("RESPONSE", this.val$jsonResponse.toString());
                if (C06654.this.val$ReforNormal) {
                    JahanbinFragment.this.JahanDATA.clear();
                    JahanbinFragment.this.JahanDATA.removeAll(JahanbinFragment.this.JahanDATA);
                    JahanbinItemAdapter jahanbinItemAdapter = JahanbinFragment.this.mJAdapter;
                    jahanbinItemAdapter.mData.clear();
                    jahanbinItemAdapter.notifyDataSetChanged();
                    JahanbinFragment.this.mJAdapter.notifyDataSetChanged();
                }
                try {
                    JahanbinFragment.this.Images = this.val$jsonResponse.getJSONArray("objects");
                    if (JahanbinFragment.this.Images.length() > 0) {
                        for (int i = 0; i < JahanbinFragment.this.Images.length(); i++) {
                            JSONObject Item = JahanbinFragment.this.Images.getJSONObject(i);
                            JahanbinFragment.IMAGE_THUMB_URL = "http://social.rabtcdn.com/media/" + Item.getString("thumbnail");
                            JahanbinFragment.IMAGE_URL = new StringBuilder(JahanbinFragment.hostAddress).append(Item.getString("image")).toString();
                            JahanbinFragment.POST_ID = Item.getString("id");
                            JahanbinFragment.USER_ID = Item.getString("user");
                            JahanbinFragment.POST_TYPE = Item.getString("post_type");
                            JahanbinFragment.this.JahanDATA.add(new JahanbinItem(BuildConfig.VERSION_NAME, JahanbinFragment.IMAGE_URL, JahanbinFragment.IMAGE_THUMB_URL, JahanbinFragment.USER_ID, JahanbinFragment.POST_ID, JahanbinFragment.POST_TYPE));
                            if (i == JahanbinFragment.this.Images.length() - 1) {
                                JahanbinFragment.this.NextPage = Item.getString("id");
                            }
                        }
                        JahanbinFragment.this.mJAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        /* renamed from: com.rokhgroup.fragments.JahanbinFragment.4.3 */
        class C06643 implements Runnable {
            C06643() {
            }

            public final void run() {
            }
        }

        C06654(boolean z) {
            this.val$ReforNormal = z;
        }

        public final void onFailure(Request request, IOException e) {
            if (JahanbinFragment.this.getActivity() != null) {
                JahanbinFragment.this.getActivity().runOnUiThread(new C06621());
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
                    if (JahanbinFragment.this.getActivity() != null) {
                        JahanbinFragment.this.getActivity().runOnUiThread(new C06632(jsonResponse));
                        return;
                    }
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                if (JahanbinFragment.this.getActivity() != null) {
                    JahanbinFragment.this.getActivity().runOnUiThread(new C06643());
                    e.printStackTrace();
                }
            }
        }
    }

    public JahanbinFragment() {
        this.JahanDATA = new ArrayList();
        this.Images = null;
        this.isLoggedin = true;
        this.savedGridPosition = 0;
    }

    public static JahanbinFragment newInstance(Context context) {
        return new JahanbinFragment();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onResume() {
        super.onResume();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = SHAMChatApplication.getMyApplicationContext();
        this.Session = new RokhPref(mContext);
        CURRENT_USER_TOKEN = this.Session.getUSERTOKEN();
        View fView = inflater.inflate(2130903214, container, false);
        mContext = fView.getContext();
        this.mGridView = (GridView) fView.findViewById(2131362477);
        this.animFadein = AnimationUtils.loadAnimation(mContext, 2130968601);
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());
        int width = (int) Utils.getWidthInPx(mContext);
        if (width > 480 && width < 780) {
            this.mGridView.setNumColumns(3);
        } else if (width <= 480) {
            this.mGridView.setNumColumns(3);
        } else if (width >= 780) {
            this.mGridView.setNumColumns(3);
        }
        this.mJAdapter = new JahanbinItemAdapter(mContext, getActivity(), this.JahanDATA, width / 3);
        this.mGridView.setAdapter(this.mJAdapter);
        this.mGridView.setOnScrollListener(new C06591());
        this.vibe = (Vibrator) mContext.getSystemService("vibrator");
        this.mShaker = new ShakeListener(mContext);
        this.accelerometerSensor = ((SensorManager) mContext.getSystemService("sensor")).getDefaultSensor(1);
        if (this.accelerometerSensor == null) {
            Toast.makeText(mContext, "\u062f\u0633\u062a\u06af\u0627\u0647 \u0634\u0645\u0627 \u0642\u0627\u0628\u0644\u06cc\u062a \u0628\u0631\u0648\u0632\u0631\u0633\u0627\u0646\u06cc \u0628\u0627 \u062a\u06a9\u0627\u0646  \u062f\u0627\u062f\u0646 \u0631\u0627 \u0646\u062f\u0627\u0631\u062f", 0).show();
        }
        if (this.accelerometerSensor != null) {
            this.mShaker.mShakeListener = new C06602(fView);
        }
        this.mGridView.setOnItemClickListener(new C06613());
        load(false, BuildConfig.VERSION_NAME);
        return fView;
    }

    private void load(boolean refresh, String nextpage) {
        String mNextPage = nextpage;
        if (nextpage != BuildConfig.VERSION_NAME) {
            if (this.isLoggedin) {
                this.URL = "http://social.rabtcdn.com/pin/api/post/?token=" + CURRENT_USER_TOKEN + "&before=" + mNextPage + "&timeStamp=" + System.currentTimeMillis();
            } else {
                this.URL = "http://social.rabtcdn.com/pin/api/post/?before=" + mNextPage + "&timeStamp=" + System.currentTimeMillis();
            }
        } else if (this.isLoggedin) {
            this.URL = "http://social.rabtcdn.com/pin/api/post/?token=" + CURRENT_USER_TOKEN + "&timeStamp=" + System.currentTimeMillis();
        } else {
            this.URL = "http://social.rabtcdn.com/pin/api/post/&timeStamp=" + System.currentTimeMillis();
        }
        Log.e("URL", this.URL);
        new OkHttpClient().newCall(new Builder().url(this.URL).build()).enqueue(new C06654(refresh));
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisible()) {
            Log.e("isVISIBLE", "================================== CALLED");
            load(true, BuildConfig.VERSION_NAME);
            this.preLast = 0;
        }
    }
}

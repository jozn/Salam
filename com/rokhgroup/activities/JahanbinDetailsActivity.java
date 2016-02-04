package com.rokhgroup.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.rokhgroup.adapters.CommentItemAdapter;
import com.rokhgroup.adapters.UserMentionAsyncTask;
import com.rokhgroup.adapters.item.CommentItem;
import com.rokhgroup.utils.RokhImageView;
import com.rokhgroup.utils.RokhPref;
import com.rokhgroup.utils.SendCommentButton;
import com.rokhgroup.utils.SendCommentButton.OnSendClickListener;
import com.rokhgroup.utils.URLSpanNoUnderLine;
import com.rokhgroup.utils.UsernameTokenizer;
import com.rokhgroup.utils.Utils;
import com.rokhgroup.video.RokhgroupVideoView;
import com.shamchat.activity.AddFavoriteTextActivity;
import com.shamchat.activity.Yekantext;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.roundedimage.RoundedDrawable;
import com.shamchat.roundedimage.RoundedImageView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;
import com.squareup.picasso.DeferredRequestCreator;
import com.squareup.picasso.Dispatcher;
import com.squareup.picasso.Dispatcher.C12321;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoExecutorService;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class JahanbinDetailsActivity extends AppCompatActivity implements TextWatcher, OnClickListener, OnSendClickListener {
    private static final int HIDE_TIME = 5000;
    private static final String hostAddress = "http://social.rabtcdn.com";
    private static final String hostMediaAddress = "http://social.rabtcdn.com/media/";
    private static final String likeAddress = "http://social.rabtcdn.com/pin/d_like2/?token=";
    private Runnable BufferLoading;
    private String CURRENT_USER_ID;
    private String CURRENT_USER_TOKEN;
    ArrayList<CommentItem> CommentDATA;
    public JSONArray Details;
    private String IMAGE_URL;
    private OnClickListener LoadMoreComment;
    public String LoadMore_mNext;
    private String POST_CMT_CNT;
    private String POST_DATE;
    private String POST_DESC;
    private String POST_ID;
    private boolean POST_LIKE;
    private String POST_LIKE_CNT;
    private String POST_TYPE;
    public JSONObject Page;
    private OnClickListener PlayVideoListener;
    private String URL;
    private String USER_AVA;
    private String USER_ID;
    private String USER_NAME;
    RokhPref UserPref;
    private String VIDEO_URL;
    SendCommentButton btnSendComment;
    private ImageView chatBtn;
    CommentItemAdapter cmtAdapter;
    private TextView commentCnt;
    private ProgressBar commentProgress;
    MultiAutoCompleteTextView etComment;
    private OnClickListener golikers;
    private Runnable hideRunnable;
    private ImageView icPlay;
    private boolean isLoggedin;
    private ImageView likeBtn;
    private TextView likeCnt;
    private OnClickListener likeListener;
    private ImageView likecntIcon;
    ListView list;
    Button loadMoreComment;
    private ImageView love_anim;
    private View mBottomView;
    private Context mContext;
    private int mCurrentPosition;
    private TextView mDurationTime;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    private Handler mHandler;
    final Handler mHandlerBuffering;
    private FrameLayout mImageVideoHolder;
    private Intent mIntent;
    private long mLastClickTime;
    LinearLayout mLoading;
    public String mNext;
    private int mOldPosition;
    private ImageView mPlay;
    private TextView mPlayTime;
    LinearLayout mPostComment;
    private ProgressBar mProgressBar;
    private SeekBar mSeekBar;
    private OnSeekBarChangeListener mSeekBarChangeListener;
    private OnTouchListener mTouchListener;
    private final Transformation mTransformation;
    private RokhgroupVideoView mVideo;
    Yekantext noCommentTxt;
    private Picasso f9p;
    private int playTime;
    private TextView postDate;
    private TextView postDesc;
    private RokhImageView postImage;
    private int post_cmt_cnt_temp;
    private int post_like_cnt_temp;
    private ImageView sendReport;
    Uri uri;
    private RoundedImageView userAvatar;
    private TextView userName;
    String f10z;

    /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.1 */
    class C05471 implements Callback {
        final /* synthetic */ String val$postId;

        /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.1.1 */
        class C05321 implements Runnable {
            C05321() {
            }

            public final void run() {
            }
        }

        /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.1.2 */
        class C05452 implements Runnable {
            final /* synthetic */ JSONObject val$jsonResponse;

            /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.1.2.1 */
            class C05371 implements OnClickListener {

                /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.1.2.1.1 */
                class C05361 implements Callback {

                    /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.1.2.1.1.1 */
                    class C05331 implements Runnable {
                        C05331() {
                        }

                        public final void run() {
                            Toast.makeText(JahanbinDetailsActivity.this.mContext, JahanbinDetailsActivity.this.getResources().getString(2131493323), 0).show();
                        }
                    }

                    /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.1.2.1.1.2 */
                    class C05342 implements Runnable {
                        final /* synthetic */ String val$stringResponse;

                        C05342(String str) {
                            this.val$stringResponse = str;
                        }

                        public final void run() {
                            try {
                                Log.e("REPORT RESULT", this.val$stringResponse);
                                if (this.val$stringResponse.equals("1")) {
                                    Toast.makeText(JahanbinDetailsActivity.this.mContext, SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493322), 0).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(JahanbinDetailsActivity.this.mContext, SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493323), 0).show();
                            }
                        }
                    }

                    /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.1.2.1.1.3 */
                    class C05353 implements Runnable {
                        C05353() {
                        }

                        public final void run() {
                            Toast.makeText(JahanbinDetailsActivity.this.mContext, SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493323), 0).show();
                        }
                    }

                    C05361() {
                    }

                    public final void onFailure(Request request, IOException e) {
                        JahanbinDetailsActivity.this.runOnUiThread(new C05331());
                        e.printStackTrace();
                    }

                    public final void onResponse(Response response) throws IOException {
                        try {
                            if (response.isSuccessful()) {
                                String stringResponse = response.body().string();
                                response.body().close();
                                JahanbinDetailsActivity.this.runOnUiThread(new C05342(stringResponse));
                                return;
                            }
                            throw new IOException("Unexpected code " + response);
                        } catch (Exception e) {
                            JahanbinDetailsActivity.this.runOnUiThread(new C05353());
                            e.printStackTrace();
                        }
                    }
                }

                C05371() {
                }

                public final void onClick(View v) {
                    String URL = "http://social.rabtcdn.com/pin/d_post_report/?token=" + JahanbinDetailsActivity.this.CURRENT_USER_TOKEN;
                    Log.e("REPORT URL", URL);
                    Log.e("Reported Post ID", C05471.this.val$postId);
                    new OkHttpClient().newCall(new Builder().url(URL).post(new FormEncodingBuilder().add("post_id", C05471.this.val$postId).build()).build()).enqueue(new C05361());
                }
            }

            /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.1.2.2 */
            class C05382 implements OnClickListener {
                C05382() {
                }

                public final void onClick(View arg0) {
                    Intent i = new Intent(JahanbinDetailsActivity.this.mContext, UserProfile.class);
                    i.putExtra("USER_ID", JahanbinDetailsActivity.this.USER_ID);
                    JahanbinDetailsActivity.this.startActivity(i);
                }
            }

            /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.1.2.3 */
            class C05433 implements OnClickListener {

                /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.1.2.3.1 */
                class C05421 implements Callback {

                    /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.1.2.3.1.1 */
                    class C05391 implements Runnable {
                        C05391() {
                        }

                        public final void run() {
                        }
                    }

                    /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.1.2.3.1.2 */
                    class C05402 implements Runnable {
                        final /* synthetic */ JSONObject val$jsonResponse;

                        C05402(JSONObject jSONObject) {
                            this.val$jsonResponse = jSONObject;
                        }

                        public final void run() {
                            try {
                                Integer likeStatus = Integer.valueOf(this.val$jsonResponse.getInt(NotificationCompatApi21.CATEGORY_STATUS));
                                Integer likeCounts = Integer.valueOf(this.val$jsonResponse.getInt("cnt_like"));
                                if (likeStatus.intValue() == -1) {
                                    if (VERSION.SDK_INT >= 11) {
                                        JahanbinDetailsActivity.this.likeBtn.startAnimation(AnimationUtils.loadAnimation(JahanbinDetailsActivity.this, 2130968609));
                                    }
                                    JahanbinDetailsActivity.this.likeBtn.setImageResource(17170445);
                                    JahanbinDetailsActivity.this.likeBtn.destroyDrawingCache();
                                    JahanbinDetailsActivity.this.likeBtn.setImageResource(2130837939);
                                    JahanbinDetailsActivity.this.post_like_cnt_temp = likeCounts.intValue();
                                } else if (likeStatus.intValue() == 1) {
                                    if (VERSION.SDK_INT >= 11) {
                                        JahanbinDetailsActivity.this.likeBtn.startAnimation(AnimationUtils.loadAnimation(JahanbinDetailsActivity.this, 2130968609));
                                        JahanbinDetailsActivity.this.love_anim.setVisibility(0);
                                        JahanbinDetailsActivity.this.love_anim.startAnimation(AnimationUtils.loadAnimation(JahanbinDetailsActivity.this, 2130968609));
                                        JahanbinDetailsActivity.this.love_anim.setVisibility(4);
                                    }
                                    JahanbinDetailsActivity.this.likeBtn.setImageResource(17170445);
                                    JahanbinDetailsActivity.this.likeBtn.destroyDrawingCache();
                                    JahanbinDetailsActivity.this.likeBtn.setImageResource(2130837940);
                                    JahanbinDetailsActivity.this.post_like_cnt_temp = likeCounts.intValue();
                                }
                                JahanbinDetailsActivity.this.likeCnt.setText(Utils.persianNum(String.valueOf(JahanbinDetailsActivity.this.post_like_cnt_temp)));
                            } catch (Exception e) {
                            }
                        }
                    }

                    /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.1.2.3.1.3 */
                    class C05413 implements Runnable {
                        C05413() {
                        }

                        public final void run() {
                        }
                    }

                    C05421() {
                    }

                    public final void onFailure(Request request, IOException e) {
                        JahanbinDetailsActivity.this.runOnUiThread(new C05391());
                        e.printStackTrace();
                    }

                    public final void onResponse(Response response) throws IOException {
                        try {
                            if (response.isSuccessful()) {
                                String stringResponse = response.body().string();
                                response.body().close();
                                System.out.println(stringResponse);
                                JahanbinDetailsActivity.this.runOnUiThread(new C05402(new JSONObject(stringResponse)));
                                return;
                            }
                            throw new IOException("Unexpected code " + response);
                        } catch (Exception e) {
                            JahanbinDetailsActivity.this.runOnUiThread(new C05413());
                            e.printStackTrace();
                        }
                    }
                }

                C05433() {
                }

                public final void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - JahanbinDetailsActivity.this.mLastClickTime < 1000) {
                        Log.e("LIKE ADDRESS", new StringBuilder(JahanbinDetailsActivity.likeAddress).append(JahanbinDetailsActivity.this.CURRENT_USER_TOKEN).toString());
                        new OkHttpClient().newCall(new Builder().url(new StringBuilder(JahanbinDetailsActivity.likeAddress).append(JahanbinDetailsActivity.this.CURRENT_USER_TOKEN).toString()).post(new FormEncodingBuilder().add("post_id", JahanbinDetailsActivity.this.POST_ID).build()).build()).enqueue(new C05421());
                    }
                    JahanbinDetailsActivity.this.mLastClickTime = SystemClock.elapsedRealtime();
                }
            }

            /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.1.2.4 */
            class C05444 implements com.squareup.picasso.Callback {
                C05444() {
                }

                public final void onSuccess() {
                    JahanbinDetailsActivity.this.mLoading.setVisibility(8);
                    JahanbinDetailsActivity.this.loadComments(false, System.currentTimeMillis());
                }
            }

            C05452(JSONObject jSONObject) {
                this.val$jsonResponse = jSONObject;
            }

            public final void run() {
                Log.e("RESPONSE", this.val$jsonResponse.toString());
                try {
                    JahanbinDetailsActivity.this.Details = this.val$jsonResponse.getJSONArray("objects");
                    if (JahanbinDetailsActivity.this.Details.length() > 0) {
                        for (int i = 0; i < JahanbinDetailsActivity.this.Details.length(); i++) {
                            JSONObject Item = JahanbinDetailsActivity.this.Details.getJSONObject(i);
                            JahanbinDetailsActivity.this.IMAGE_URL = new StringBuilder(JahanbinDetailsActivity.hostMediaAddress).append(Item.getString("thumbnail")).toString();
                            JahanbinDetailsActivity.this.USER_ID = Item.getString("user");
                            JahanbinDetailsActivity.this.USER_NAME = Item.getString("user_name");
                            JahanbinDetailsActivity.this.USER_AVA = new StringBuilder(JahanbinDetailsActivity.hostAddress).append(Item.getString("user_avatar")).toString();
                            JahanbinDetailsActivity.this.POST_DATE = Item.getString("timestamp");
                            JahanbinDetailsActivity.this.POST_DESC = Item.getString(AddFavoriteTextActivity.EXTRA_RESULT_TEXT);
                            JahanbinDetailsActivity.this.POST_LIKE = Item.getBoolean("like_with_user");
                            JahanbinDetailsActivity.this.POST_LIKE_CNT = Item.getString("like");
                            JahanbinDetailsActivity.this.post_like_cnt_temp = Integer.valueOf(JahanbinDetailsActivity.this.POST_LIKE_CNT).intValue();
                            JahanbinDetailsActivity.this.POST_CMT_CNT = Item.getString("cnt_comment");
                            JahanbinDetailsActivity.this.post_cmt_cnt_temp = Integer.valueOf(JahanbinDetailsActivity.this.POST_CMT_CNT).intValue();
                            if (JahanbinDetailsActivity.this.POST_TYPE.equals("3")) {
                                JahanbinDetailsActivity.this.VIDEO_URL = new StringBuilder(JahanbinDetailsActivity.hostMediaAddress).append(Item.getString("video")).toString();
                            }
                            if (JahanbinDetailsActivity.this.post_cmt_cnt_temp < 20) {
                                JahanbinDetailsActivity.this.loadMoreComment.setVisibility(8);
                            }
                            JahanbinDetailsActivity.this.userName.setTypeface(Utils.GetNaskhBold(JahanbinDetailsActivity.this.mContext));
                            JahanbinDetailsActivity.this.userName.setTextSize(13.0f);
                            JahanbinDetailsActivity.this.userName.setText(JahanbinDetailsActivity.this.USER_NAME);
                            JahanbinDetailsActivity.this.sendReport.setOnClickListener(new C05371());
                            JahanbinDetailsActivity.this.postDate.setTypeface(Utils.GetNaskhRegular(JahanbinDetailsActivity.this.mContext));
                            JahanbinDetailsActivity.this.postDate.setTextSize(11.0f);
                            TextView access$1700 = JahanbinDetailsActivity.this.postDate;
                            long longValue = Long.valueOf(JahanbinDetailsActivity.this.POST_DATE).longValue();
                            JahanbinDetailsActivity.this.mContext;
                            access$1700.setText(Utils.persianNum(Utils.getTimeAgo$6909e107(longValue)));
                            JahanbinDetailsActivity.this.likeCnt.setTypeface(Utils.GetNaskhBold(JahanbinDetailsActivity.this.mContext));
                            JahanbinDetailsActivity.this.likeCnt.setTextSize(14.0f);
                            JahanbinDetailsActivity.this.likeCnt.setText(Utils.persianNum(JahanbinDetailsActivity.this.POST_LIKE_CNT));
                            JahanbinDetailsActivity.this.commentCnt.setTypeface(Utils.GetNaskhBold(JahanbinDetailsActivity.this.mContext));
                            JahanbinDetailsActivity.this.commentCnt.setTextSize(14.0f);
                            JahanbinDetailsActivity.this.commentCnt.setText(Utils.persianNum(JahanbinDetailsActivity.this.POST_CMT_CNT));
                            if (JahanbinDetailsActivity.this.POST_LIKE) {
                                JahanbinDetailsActivity.this.likeBtn.setBackgroundResource(2130837940);
                            } else {
                                JahanbinDetailsActivity.this.likeBtn.setBackgroundResource(2130837939);
                            }
                            JahanbinDetailsActivity.this.likeBtn.setOnClickListener(JahanbinDetailsActivity.this.likeListener);
                            if (JahanbinDetailsActivity.this.POST_DESC.length() <= 1) {
                                JahanbinDetailsActivity.this.postDesc.setVisibility(8);
                            } else {
                                JahanbinDetailsActivity.this.postDesc.setTypeface(Utils.GetNaskhRegular(JahanbinDetailsActivity.this.mContext));
                                JahanbinDetailsActivity.this.postDesc.setTextSize(12.0f);
                                JahanbinDetailsActivity.this.postDesc.setText(JahanbinDetailsActivity.this.POST_DESC);
                                Linkify.addLinks(JahanbinDetailsActivity.this.postDesc, Pattern.compile("[#]+[\\w]+\\b", 64), "content://com.rokhgroup.activities.hashtagactivity/");
                                Linkify.addLinks(JahanbinDetailsActivity.this.postDesc, Pattern.compile("[@]+[\\w]+\\b", 64), "content://com.rokhgroup.activities.userprofileactivity/");
                                Linkify.addLinks(JahanbinDetailsActivity.this.postDesc, Patterns.WEB_URL, null, null, null);
                                JahanbinDetailsActivity.this.stripUnderlines(JahanbinDetailsActivity.this.postDesc);
                            }
                            RequestCreator networkPolicy$4ded38c = Picasso.with(JahanbinDetailsActivity.this.mContext).load(JahanbinDetailsActivity.this.USER_AVA).memoryPolicy$4b608e66(MemoryPolicy.NO_CACHE).networkPolicy$4ded38c(NetworkPolicy.NO_CACHE);
                            networkPolicy$4ded38c.deferred = true;
                            networkPolicy$4ded38c.transform(JahanbinDetailsActivity.this.mTransformation).into(JahanbinDetailsActivity.this.userAvatar, null);
                            JahanbinDetailsActivity.this.userAvatar.setOnClickListener(new C05382());
                            JahanbinDetailsActivity.this.mPlay.setOnClickListener(JahanbinDetailsActivity.this);
                            JahanbinDetailsActivity.this.icPlay.setOnClickListener(JahanbinDetailsActivity.this.PlayVideoListener);
                            JahanbinDetailsActivity.this.mSeekBar.setOnSeekBarChangeListener(JahanbinDetailsActivity.this.mSeekBarChangeListener);
                            JahanbinDetailsActivity.this.postImage.setOnClickListener(new C05433());
                            JahanbinDetailsActivity.this.f9p = new Picasso.Builder(JahanbinDetailsActivity.this.mContext).build();
                            JahanbinDetailsActivity.this.f9p;
                            Picasso.with(JahanbinDetailsActivity.this.mContext).load(JahanbinDetailsActivity.this.IMAGE_URL).memoryPolicy$4b608e66(MemoryPolicy.NO_CACHE).networkPolicy$4ded38c(NetworkPolicy.NO_CACHE).into(JahanbinDetailsActivity.this.postImage, new C05444());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.1.3 */
        class C05463 implements Runnable {
            C05463() {
            }

            public final void run() {
            }
        }

        C05471(String str) {
            this.val$postId = str;
        }

        public final void onFailure(Request request, IOException e) {
            JahanbinDetailsActivity.this.runOnUiThread(new C05321());
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String stringResponse = response.body().string();
                    response.body().close();
                    System.out.println(stringResponse);
                    JahanbinDetailsActivity.this.runOnUiThread(new C05452(new JSONObject(stringResponse)));
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                JahanbinDetailsActivity.this.runOnUiThread(new C05463());
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.2 */
    class C05482 implements OnClickListener {
        C05482() {
        }

        public final void onClick(View v) {
            JahanbinDetailsActivity.this.icPlay.setVisibility(8);
            JahanbinDetailsActivity.this.postImage.setVisibility(8);
            JahanbinDetailsActivity.this.playVideo();
            JahanbinDetailsActivity.this.showHide(false);
        }
    }

    /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.3 */
    class C05533 implements OnClickListener {

        /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.3.1 */
        class C05521 implements Callback {

            /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.3.1.1 */
            class C05491 implements Runnable {
                C05491() {
                }

                public final void run() {
                }
            }

            /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.3.1.2 */
            class C05502 implements Runnable {
                final /* synthetic */ JSONObject val$jsonResponse;

                C05502(JSONObject jSONObject) {
                    this.val$jsonResponse = jSONObject;
                }

                /* JADX WARNING: inconsistent code. */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public final void run() {
                    /*
                    r13 = this;
                    r12 = 11;
                    r11 = 1;
                    r9 = r13.val$jsonResponse;	 Catch:{ Exception -> 0x01c1 }
                    r10 = "status";
                    r9 = r9.getInt(r10);	 Catch:{ Exception -> 0x01c1 }
                    r6 = java.lang.Integer.valueOf(r9);	 Catch:{ Exception -> 0x01c1 }
                    r9 = r13.val$jsonResponse;	 Catch:{ Exception -> 0x01c1 }
                    r10 = "cnt_like";
                    r9 = r9.getInt(r10);	 Catch:{ Exception -> 0x01c1 }
                    r5 = java.lang.Integer.valueOf(r9);	 Catch:{ Exception -> 0x01c1 }
                    r9 = r6.intValue();	 Catch:{ Exception -> 0x01c1 }
                    r10 = -1;
                    if (r9 != r10) goto L_0x01c3;
                L_0x0022:
                    r9 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x01c1 }
                    if (r9 < r12) goto L_0x0040;
                L_0x0026:
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01c1 }
                    r10 = 2130968609; // 0x7f040021 float:1.7545876E38 double:1.052838382E-314;
                    r1 = android.view.animation.AnimationUtils.loadAnimation(r9, r10);	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = r9.likeBtn;	 Catch:{ Exception -> 0x01c1 }
                    r9.startAnimation(r1);	 Catch:{ Exception -> 0x01c1 }
                L_0x0040:
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01ac }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01ac }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01ac }
                    r9 = r9.postDesc;	 Catch:{ Exception -> 0x01ac }
                    r10 = 1;
                    r9.setClickable(r10);	 Catch:{ Exception -> 0x01ac }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01ac }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01ac }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01ac }
                    r9 = r9.postDesc;	 Catch:{ Exception -> 0x01ac }
                    r10 = android.text.method.LinkMovementMethod.getInstance();	 Catch:{ Exception -> 0x01ac }
                    r9.setMovementMethod(r10);	 Catch:{ Exception -> 0x01ac }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01ac }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01ac }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01ac }
                    r9 = r9.POST_DESC;	 Catch:{ Exception -> 0x01ac }
                    r10 = "http://";
                    r7 = r9.indexOf(r10);	 Catch:{ Exception -> 0x01ac }
                    r3 = 0;
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01ac }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01ac }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01ac }
                    r9 = r9.POST_DESC;	 Catch:{ Exception -> 0x01ac }
                    r10 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01ac }
                    r10 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01ac }
                    r10 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01ac }
                    r10 = r10.POST_DESC;	 Catch:{ Exception -> 0x01ac }
                    r10 = r10.length();	 Catch:{ Exception -> 0x01ac }
                    r10 = r10 + -1;
                    r8 = r9.substring(r7, r10);	 Catch:{ Exception -> 0x01ac }
                    r4 = 0;
                L_0x008f:
                    r9 = r8.length();	 Catch:{ Exception -> 0x01ac }
                    r9 = r9 + -1;
                    if (r4 >= r9) goto L_0x00aa;
                L_0x0097:
                    r3 = r4 + r7;
                    r9 = r4 + 1;
                    r9 = r8.substring(r4, r9);	 Catch:{ Exception -> 0x01ac }
                    r10 = " ";
                    r9 = r9.equals(r10);	 Catch:{ Exception -> 0x01ac }
                    if (r9 != 0) goto L_0x00aa;
                L_0x00a7:
                    r4 = r4 + 1;
                    goto L_0x008f;
                L_0x00aa:
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01ac }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01ac }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01ac }
                    r9 = r9.POST_DESC;	 Catch:{ Exception -> 0x01ac }
                    r9 = r9.substring(r7, r3);	 Catch:{ Exception -> 0x01ac }
                    r9.length();	 Catch:{ Exception -> 0x01ac }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01ac }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01ac }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01ac }
                    r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01ac }
                    r10.<init>();	 Catch:{ Exception -> 0x01ac }
                    r11 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01ac }
                    r11 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01ac }
                    r11 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01ac }
                    r11 = r11.POST_DESC;	 Catch:{ Exception -> 0x01ac }
                    r12 = 0;
                    r11 = r11.substring(r12, r7);	 Catch:{ Exception -> 0x01ac }
                    r10 = r10.append(r11);	 Catch:{ Exception -> 0x01ac }
                    r11 = " <a href='";
                    r10 = r10.append(r11);	 Catch:{ Exception -> 0x01ac }
                    r11 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01ac }
                    r11 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01ac }
                    r11 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01ac }
                    r11 = r11.POST_DESC;	 Catch:{ Exception -> 0x01ac }
                    r11 = r11.substring(r7, r3);	 Catch:{ Exception -> 0x01ac }
                    r10 = r10.append(r11);	 Catch:{ Exception -> 0x01ac }
                    r11 = "'>";
                    r10 = r10.append(r11);	 Catch:{ Exception -> 0x01ac }
                    r11 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01ac }
                    r11 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01ac }
                    r11 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01ac }
                    r11 = r11.POST_DESC;	 Catch:{ Exception -> 0x01ac }
                    r11 = r11.substring(r7, r3);	 Catch:{ Exception -> 0x01ac }
                    r10 = r10.append(r11);	 Catch:{ Exception -> 0x01ac }
                    r11 = "  </a> ";
                    r10 = r10.append(r11);	 Catch:{ Exception -> 0x01ac }
                    r11 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01ac }
                    r11 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01ac }
                    r11 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01ac }
                    r11 = r11.POST_DESC;	 Catch:{ Exception -> 0x01ac }
                    r12 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01ac }
                    r12 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01ac }
                    r12 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01ac }
                    r12 = r12.POST_DESC;	 Catch:{ Exception -> 0x01ac }
                    r12 = r12.length();	 Catch:{ Exception -> 0x01ac }
                    r12 = r12 + r3;
                    r12 = r12 - r3;
                    r12 = r12 + -1;
                    r11 = r11.substring(r3, r12);	 Catch:{ Exception -> 0x01ac }
                    r10 = r10.append(r11);	 Catch:{ Exception -> 0x01ac }
                    r10 = r10.toString();	 Catch:{ Exception -> 0x01ac }
                    r9.f10z = r10;	 Catch:{ Exception -> 0x01ac }
                L_0x0139:
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = r9.postDesc;	 Catch:{ Exception -> 0x01c1 }
                    r10 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01c1 }
                    r10 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01c1 }
                    r10 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01c1 }
                    r10 = r10.f10z;	 Catch:{ Exception -> 0x01c1 }
                    r10 = android.text.Html.fromHtml(r10);	 Catch:{ Exception -> 0x01c1 }
                    r9.setText(r10);	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = r9.likeBtn;	 Catch:{ Exception -> 0x01c1 }
                    r10 = 17170445; // 0x106000d float:2.461195E-38 double:8.483327E-317;
                    r9.setImageResource(r10);	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = r9.likeBtn;	 Catch:{ Exception -> 0x01c1 }
                    r9.destroyDrawingCache();	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = r9.likeBtn;	 Catch:{ Exception -> 0x01c1 }
                    r10 = 2130837939; // 0x7f0201b3 float:1.7280846E38 double:1.0527738225E-314;
                    r9.setImageResource(r10);	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01c1 }
                    r10 = r5.intValue();	 Catch:{ Exception -> 0x01c1 }
                    r9.post_like_cnt_temp = r10;	 Catch:{ Exception -> 0x01c1 }
                L_0x018c:
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = r9.likeCnt;	 Catch:{ Exception -> 0x01c1 }
                    r10 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01c1 }
                    r10 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01c1 }
                    r10 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01c1 }
                    r10 = r10.post_like_cnt_temp;	 Catch:{ Exception -> 0x01c1 }
                    r10 = java.lang.String.valueOf(r10);	 Catch:{ Exception -> 0x01c1 }
                    r10 = com.rokhgroup.utils.Utils.persianNum(r10);	 Catch:{ Exception -> 0x01c1 }
                    r9.setText(r10);	 Catch:{ Exception -> 0x01c1 }
                L_0x01ab:
                    return;
                L_0x01ac:
                    r9 = move-exception;
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01c1 }
                    r10 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01c1 }
                    r10 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01c1 }
                    r10 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01c1 }
                    r10 = r10.POST_DESC;	 Catch:{ Exception -> 0x01c1 }
                    r9.f10z = r10;	 Catch:{ Exception -> 0x01c1 }
                    goto L_0x0139;
                L_0x01c1:
                    r9 = move-exception;
                    goto L_0x01ab;
                L_0x01c3:
                    r9 = r6.intValue();	 Catch:{ Exception -> 0x01c1 }
                    if (r9 != r11) goto L_0x018c;
                L_0x01c9:
                    r9 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x01c1 }
                    if (r9 < r12) goto L_0x021d;
                L_0x01cd:
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01c1 }
                    r10 = 2130968609; // 0x7f040021 float:1.7545876E38 double:1.052838382E-314;
                    r2 = android.view.animation.AnimationUtils.loadAnimation(r9, r10);	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = r9.likeBtn;	 Catch:{ Exception -> 0x01c1 }
                    r9.startAnimation(r2);	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = r9.love_anim;	 Catch:{ Exception -> 0x01c1 }
                    r10 = 0;
                    r9.setVisibility(r10);	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01c1 }
                    r10 = 2130968609; // 0x7f040021 float:1.7545876E38 double:1.052838382E-314;
                    r0 = android.view.animation.AnimationUtils.loadAnimation(r9, r10);	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = r9.love_anim;	 Catch:{ Exception -> 0x01c1 }
                    r9.startAnimation(r0);	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = r9.love_anim;	 Catch:{ Exception -> 0x01c1 }
                    r10 = 4;
                    r9.setVisibility(r10);	 Catch:{ Exception -> 0x01c1 }
                L_0x021d:
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = r9.likeBtn;	 Catch:{ Exception -> 0x01c1 }
                    r10 = 17170445; // 0x106000d float:2.461195E-38 double:8.483327E-317;
                    r9.setImageResource(r10);	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = r9.likeBtn;	 Catch:{ Exception -> 0x01c1 }
                    r9.destroyDrawingCache();	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = r9.likeBtn;	 Catch:{ Exception -> 0x01c1 }
                    r10 = 2130837940; // 0x7f0201b4 float:1.7280848E38 double:1.052773823E-314;
                    r9.setImageResource(r10);	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.C05521.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.C05533.this;	 Catch:{ Exception -> 0x01c1 }
                    r9 = com.rokhgroup.activities.JahanbinDetailsActivity.this;	 Catch:{ Exception -> 0x01c1 }
                    r10 = r5.intValue();	 Catch:{ Exception -> 0x01c1 }
                    r9.post_like_cnt_temp = r10;	 Catch:{ Exception -> 0x01c1 }
                    goto L_0x018c;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.rokhgroup.activities.JahanbinDetailsActivity.3.1.2.run():void");
                }
            }

            /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.3.1.3 */
            class C05513 implements Runnable {
                C05513() {
                }

                public final void run() {
                }
            }

            C05521() {
            }

            public final void onFailure(Request request, IOException e) {
                JahanbinDetailsActivity.this.runOnUiThread(new C05491());
                e.printStackTrace();
            }

            public final void onResponse(Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String stringResponse = response.body().string();
                        response.body().close();
                        System.out.println(stringResponse);
                        JahanbinDetailsActivity.this.runOnUiThread(new C05502(new JSONObject(stringResponse)));
                        return;
                    }
                    throw new IOException("Unexpected code " + response);
                } catch (Exception e) {
                    JahanbinDetailsActivity.this.runOnUiThread(new C05513());
                    e.printStackTrace();
                }
            }
        }

        C05533() {
        }

        public final void onClick(View v) {
            Log.e("LIKE ADDRESS", new StringBuilder(JahanbinDetailsActivity.likeAddress).append(JahanbinDetailsActivity.this.CURRENT_USER_TOKEN).toString());
            new OkHttpClient().newCall(new Builder().url(new StringBuilder(JahanbinDetailsActivity.likeAddress).append(JahanbinDetailsActivity.this.CURRENT_USER_TOKEN).toString()).post(new FormEncodingBuilder().add("post_id", JahanbinDetailsActivity.this.POST_ID).build()).build()).enqueue(new C05521());
        }
    }

    /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.4 */
    class C05574 implements Callback {
        final /* synthetic */ boolean val$Reloading;

        /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.4.1 */
        class C05541 implements Runnable {
            C05541() {
            }

            public final void run() {
            }
        }

        /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.4.2 */
        class C05552 implements Runnable {
            final /* synthetic */ JSONObject val$jsonResponse;

            C05552(JSONObject jSONObject) {
                this.val$jsonResponse = jSONObject;
            }

            public final void run() {
                try {
                    if (C05574.this.val$Reloading) {
                        JahanbinDetailsActivity.this.CommentDATA.clear();
                        JahanbinDetailsActivity.this.CommentDATA.clear();
                    }
                    JahanbinDetailsActivity.this.Page = this.val$jsonResponse.getJSONObject("meta");
                    JahanbinDetailsActivity.this.mNext = JahanbinDetailsActivity.this.Page.getString("next");
                    JahanbinDetailsActivity.this.LoadMore_mNext = new StringBuilder(JahanbinDetailsActivity.hostAddress).append(JahanbinDetailsActivity.this.mNext).toString();
                    Log.e("NEXT COMMENT LINK", JahanbinDetailsActivity.this.mNext);
                    JSONArray comments = this.val$jsonResponse.getJSONArray("objects");
                    if (comments.length() > 0) {
                        for (int i = 0; i < comments.length(); i++) {
                            JSONObject Item = comments.getJSONObject(i);
                            String ID = Item.getString("user_url");
                            String body = Item.getString("comment");
                            String avatar = new StringBuilder(JahanbinDetailsActivity.hostAddress).append(Item.getString("user_avatar")).toString();
                            String date = Item.getString("submit_date");
                            String userID = Item.getString("user_url");
                            JahanbinDetailsActivity.this.CommentDATA.add(new CommentItem(ID, avatar, Item.getString("user_name"), userID, body, date));
                        }
                        JahanbinDetailsActivity.getTotalHeightofListView(JahanbinDetailsActivity.this.list);
                        JahanbinDetailsActivity.this.cmtAdapter.notifyDataSetChanged();
                        JahanbinDetailsActivity.this.commentProgress.setVisibility(8);
                        JahanbinDetailsActivity.this.noCommentTxt.setVisibility(8);
                        return;
                    }
                    JahanbinDetailsActivity.this.commentProgress.setVisibility(8);
                    Log.e("RESPONSE ERROR", "We got empty json array!!!");
                    JahanbinDetailsActivity.this.noCommentTxt.setVisibility(0);
                } catch (Exception e) {
                }
            }
        }

        /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.4.3 */
        class C05563 implements Runnable {
            C05563() {
            }

            public final void run() {
            }
        }

        C05574(boolean z) {
            this.val$Reloading = z;
        }

        public final void onFailure(Request request, IOException e) {
            JahanbinDetailsActivity.this.runOnUiThread(new C05541());
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String stringResponse = response.body().string();
                    response.body().close();
                    System.out.println(stringResponse);
                    JSONObject jsonResponse = new JSONObject(stringResponse);
                    Log.e("COMMENT RESPONSE", stringResponse);
                    JahanbinDetailsActivity.this.runOnUiThread(new C05552(jsonResponse));
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                JahanbinDetailsActivity.this.runOnUiThread(new C05563());
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.5 */
    class C05625 implements OnClickListener {

        /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.5.1 */
        class C05611 implements Callback {

            /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.5.1.1 */
            class C05581 implements Runnable {
                C05581() {
                }

                public final void run() {
                }
            }

            /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.5.1.2 */
            class C05592 implements Runnable {
                final /* synthetic */ JSONObject val$jsonResponse;

                C05592(JSONObject jSONObject) {
                    this.val$jsonResponse = jSONObject;
                }

                public final void run() {
                    try {
                        JahanbinDetailsActivity.this.Page = this.val$jsonResponse.getJSONObject("meta");
                        JahanbinDetailsActivity.this.mNext = JahanbinDetailsActivity.this.Page.getString("next");
                        JahanbinDetailsActivity.this.LoadMore_mNext = new StringBuilder(JahanbinDetailsActivity.hostAddress).append(JahanbinDetailsActivity.this.mNext).toString();
                        JSONArray comments = this.val$jsonResponse.getJSONArray("objects");
                        if (comments.length() == 0) {
                            JahanbinDetailsActivity.this.loadMoreComment.setVisibility(8);
                            return;
                        }
                        for (int i = 0; i < comments.length(); i++) {
                            JSONObject Item = comments.getJSONObject(i);
                            String ID = Item.getString("id");
                            String body = Item.getString("comment");
                            String avatar = new StringBuilder(JahanbinDetailsActivity.hostAddress).append(Item.getString("user_avatar")).toString();
                            String date = Item.getString("submit_date");
                            String userID = Item.getString("user_url");
                            JahanbinDetailsActivity.this.CommentDATA.add(new CommentItem(ID, avatar, Item.getString("user_name"), userID, body, date));
                        }
                        JahanbinDetailsActivity.getTotalHeightofListView(JahanbinDetailsActivity.this.list);
                        JahanbinDetailsActivity.this.cmtAdapter.notifyDataSetChanged();
                        JahanbinDetailsActivity.this.commentProgress.setVisibility(8);
                    } catch (Exception e) {
                    }
                }
            }

            /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.5.1.3 */
            class C05603 implements Runnable {
                C05603() {
                }

                public final void run() {
                }
            }

            C05611() {
            }

            public final void onFailure(Request request, IOException e) {
                JahanbinDetailsActivity.this.runOnUiThread(new C05581());
                e.printStackTrace();
            }

            public final void onResponse(Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String stringResponse = response.body().string();
                        response.body().close();
                        System.out.println(stringResponse);
                        JahanbinDetailsActivity.this.runOnUiThread(new C05592(new JSONObject(stringResponse)));
                        return;
                    }
                    throw new IOException("Unexpected code " + response);
                } catch (Exception e) {
                    JahanbinDetailsActivity.this.runOnUiThread(new C05603());
                    e.printStackTrace();
                }
            }
        }

        C05625() {
        }

        public final void onClick(View v) {
            new OkHttpClient().newCall(new Builder().url(JahanbinDetailsActivity.this.LoadMore_mNext).build()).enqueue(new C05611());
        }
    }

    /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.6 */
    class C05666 implements Callback {

        /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.6.1 */
        class C05631 implements Runnable {
            C05631() {
            }

            public final void run() {
                JahanbinDetailsActivity.this.etComment.setText(null);
            }
        }

        /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.6.2 */
        class C05642 implements Runnable {
            final /* synthetic */ String val$stringResponse;

            C05642(String str) {
                this.val$stringResponse = str;
            }

            public final void run() {
                try {
                    if (this.val$stringResponse.equals("1")) {
                        JahanbinDetailsActivity.this.post_cmt_cnt_temp = JahanbinDetailsActivity.this.post_cmt_cnt_temp + 1;
                        JahanbinDetailsActivity.this.commentCnt.setText(Utils.persianNum(String.valueOf(JahanbinDetailsActivity.this.post_cmt_cnt_temp)));
                        JahanbinDetailsActivity.this.etComment.setText(null);
                        JahanbinDetailsActivity.this.btnSendComment.setCurrentState(1);
                        JahanbinDetailsActivity.this.loadComments(true, System.currentTimeMillis());
                    }
                } catch (Exception e) {
                    JahanbinDetailsActivity.this.etComment.setText(null);
                }
            }
        }

        /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.6.3 */
        class C05653 implements Runnable {
            C05653() {
            }

            public final void run() {
                JahanbinDetailsActivity.this.etComment.setText(null);
            }
        }

        C05666() {
        }

        public final void onFailure(Request request, IOException e) {
            JahanbinDetailsActivity.this.runOnUiThread(new C05631());
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String stringResponse = response.body().string();
                    response.body().close();
                    JahanbinDetailsActivity.this.runOnUiThread(new C05642(stringResponse));
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                JahanbinDetailsActivity.this.runOnUiThread(new C05653());
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.7 */
    class C05677 implements OnClickListener {
        C05677() {
        }

        public final void onClick(View v) {
            Intent i = new Intent(JahanbinDetailsActivity.this, Likers.class);
            i.putExtra("POST_ID", JahanbinDetailsActivity.this.POST_ID);
            JahanbinDetailsActivity.this.startActivity(i);
        }
    }

    /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.8 */
    class C05688 implements Transformation {
        final boolean oval;
        final float radius;

        C05688() {
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
    }

    /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.9 */
    class C05709 implements OnPreparedListener {

        /* renamed from: com.rokhgroup.activities.JahanbinDetailsActivity.9.1 */
        class C05691 extends TimerTask {
            C05691() {
            }

            public final void run() {
                JahanbinDetailsActivity.this.mHandler.sendEmptyMessage(1);
            }
        }

        C05709() {
        }

        public final void onPrepared(MediaPlayer mp) {
            Log.e("video width", mp.getVideoWidth());
            Log.e("video height", mp.getVideoHeight());
            JahanbinDetailsActivity.this.mVideo.videoWidth = mp.getVideoWidth();
            JahanbinDetailsActivity.this.mVideo.videoHeight = mp.getVideoHeight();
            JahanbinDetailsActivity.this.mVideo.start();
            JahanbinDetailsActivity.this.mHandlerBuffering.postDelayed(JahanbinDetailsActivity.this.BufferLoading, 0);
            if (JahanbinDetailsActivity.this.playTime != 0) {
                JahanbinDetailsActivity.this.mVideo.seekTo(JahanbinDetailsActivity.this.playTime);
            }
            JahanbinDetailsActivity.this.mHandler.removeCallbacks(JahanbinDetailsActivity.this.hideRunnable);
            JahanbinDetailsActivity.this.mHandler.postDelayed(JahanbinDetailsActivity.this.hideRunnable, 5000);
            JahanbinDetailsActivity.this.mDurationTime.setText(JahanbinDetailsActivity.this.stringForTime(JahanbinDetailsActivity.this.mVideo.getDuration()));
            new Timer().schedule(new C05691(), 0, 1000);
        }
    }

    public JahanbinDetailsActivity() {
        this.POST_ID = null;
        this.mCurrentPosition = 0;
        this.mOldPosition = 0;
        this.mHandlerBuffering = new Handler();
        this.isLoggedin = true;
        this.Details = null;
        this.Page = null;
        this.CommentDATA = new ArrayList();
        this.mLastClickTime = 0;
        this.PlayVideoListener = new C05482();
        this.likeListener = new C05533();
        this.LoadMoreComment = new C05625();
        this.golikers = new C05677();
        this.mTransformation = new C05688();
        this.mSeekBarChangeListener = new OnSeekBarChangeListener() {
            public final void onStopTrackingTouch(SeekBar seekBar) {
                JahanbinDetailsActivity.this.mHandler.postDelayed(JahanbinDetailsActivity.this.hideRunnable, 5000);
            }

            public final void onStartTrackingTouch(SeekBar seekBar) {
                JahanbinDetailsActivity.this.mHandler.removeCallbacks(JahanbinDetailsActivity.this.hideRunnable);
            }

            public final void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    JahanbinDetailsActivity.this.mVideo.seekTo((JahanbinDetailsActivity.this.mVideo.getDuration() * progress) / 100);
                }
            }
        };
        this.mHandler = new Handler() {
            public final void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (JahanbinDetailsActivity.this.mVideo != null) {
                    switch (msg.what) {
                        case Logger.SEVERE /*1*/:
                            if (JahanbinDetailsActivity.this.mVideo.getCurrentPosition() > 0) {
                                JahanbinDetailsActivity.this.mPlayTime.setText(JahanbinDetailsActivity.this.stringForTime(JahanbinDetailsActivity.this.mVideo.getCurrentPosition()));
                                JahanbinDetailsActivity.this.mSeekBar.setProgress((JahanbinDetailsActivity.this.mVideo.getCurrentPosition() * 100) / JahanbinDetailsActivity.this.mVideo.getDuration());
                                if (JahanbinDetailsActivity.this.mVideo.getCurrentPosition() > JahanbinDetailsActivity.this.mVideo.getDuration() - 100) {
                                    JahanbinDetailsActivity.this.mPlayTime.setText("00:00");
                                    JahanbinDetailsActivity.this.mSeekBar.setProgress(0);
                                }
                                JahanbinDetailsActivity.this.mSeekBar.setSecondaryProgress(JahanbinDetailsActivity.this.mVideo.getBufferPercentage());
                                return;
                            }
                            JahanbinDetailsActivity.this.mPlayTime.setText("00:00");
                            JahanbinDetailsActivity.this.mSeekBar.setProgress(0);
                        case Logger.WARNING /*2*/:
                            JahanbinDetailsActivity.this.showHide(true);
                        default:
                    }
                }
            }
        };
        this.hideRunnable = new Runnable() {
            public final void run() {
                JahanbinDetailsActivity.this.showHide(true);
            }
        };
        this.BufferLoading = new Runnable() {
            public final void run() {
                if (JahanbinDetailsActivity.this.mVideo != null) {
                    JahanbinDetailsActivity.this.mCurrentPosition = JahanbinDetailsActivity.this.mVideo.getCurrentPosition();
                    if (JahanbinDetailsActivity.this.mOldPosition == JahanbinDetailsActivity.this.mCurrentPosition && JahanbinDetailsActivity.this.mVideo.isPlaying()) {
                        if (JahanbinDetailsActivity.this.mProgressBar.getVisibility() == 8) {
                            JahanbinDetailsActivity.this.mProgressBar.setVisibility(0);
                        }
                    } else if (JahanbinDetailsActivity.this.mProgressBar.getVisibility() == 0) {
                        JahanbinDetailsActivity.this.mProgressBar.setVisibility(8);
                    }
                    JahanbinDetailsActivity.this.mOldPosition = JahanbinDetailsActivity.this.mCurrentPosition;
                    JahanbinDetailsActivity.this.mHandlerBuffering.postDelayed(JahanbinDetailsActivity.this.BufferLoading, 1000);
                }
            }
        };
        this.mTouchListener = new OnTouchListener() {
            public final boolean onTouch(View v, MotionEvent event) {
                JahanbinDetailsActivity.this.showHide(true);
                return true;
            }
        };
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903213);
        this.mContext = this;
        this.UserPref = new RokhPref(this.mContext);
        this.CURRENT_USER_TOKEN = this.UserPref.getUSERTOKEN();
        this.CURRENT_USER_ID = this.UserPref.getUSERID();
        this.mLoading = (LinearLayout) findViewById(2131362446);
        this.mPostComment = (LinearLayout) findViewById(2131362474);
        this.postImage = (RokhImageView) findViewById(2131362460);
        this.userAvatar = (RoundedImageView) findViewById(2131362433);
        this.userName = (TextView) findViewById(2131362397);
        this.postDate = (TextView) findViewById(2131362450);
        this.postDesc = (TextView) findViewById(2131362451);
        this.likeCnt = (TextView) findViewById(2131362467);
        this.likecntIcon = (ImageView) findViewById(2131362466);
        this.sendReport = (ImageView) findViewById(2131362449);
        this.love_anim = (ImageView) findViewById(2131362461);
        this.commentCnt = (TextView) findViewById(2131362465);
        this.etComment = (MultiAutoCompleteTextView) findViewById(2131362475);
        this.btnSendComment = (SendCommentButton) findViewById(2131362476);
        this.list = (ListView) findViewById(2131362472);
        this.loadMoreComment = (Button) findViewById(2131362473);
        this.noCommentTxt = (Yekantext) findViewById(2131362470);
        this.mFormatBuilder = new StringBuilder();
        this.mFormatter = new Formatter(this.mFormatBuilder, Locale.getDefault());
        this.mImageVideoHolder = (FrameLayout) findViewById(2131362452);
        this.mVideo = (RokhgroupVideoView) findViewById(2131362453);
        this.mProgressBar = (ProgressBar) findViewById(2131362454);
        this.mPlayTime = (TextView) findViewById(2131362457);
        this.mDurationTime = (TextView) findViewById(2131362458);
        this.mPlay = (ImageView) findViewById(2131362456);
        this.icPlay = (ImageView) findViewById(2131362462);
        this.mSeekBar = (SeekBar) findViewById(2131362459);
        this.mBottomView = findViewById(2131362455);
        float ScreenWidth = Utils.getWidthInPx(this.mContext);
        float imageHeight = (ScreenWidth / 2.0f) + 67.0f;
        LayoutParams lp = (LayoutParams) this.mImageVideoHolder.getLayoutParams();
        lp.width = (int) (ScreenWidth - (getResources().getDimension(2131296323) * 2.0f));
        lp.height = (int) imageHeight;
        this.btnSendComment.onSendClickListener = this;
        this.loadMoreComment.setOnClickListener(this.LoadMoreComment);
        this.likeCnt.setOnClickListener(this.golikers);
        this.likecntIcon.setOnClickListener(this.golikers);
        this.cmtAdapter = new CommentItemAdapter(this.mContext, this, this.CommentDATA);
        this.list.setAdapter(this.cmtAdapter);
        this.commentProgress = (ProgressBar) findViewById(2131362471);
        this.likeBtn = (ImageView) findViewById(2131362463);
        this.chatBtn = (ImageView) findViewById(2131362448);
        this.uri = getIntent().getData();
        this.etComment.setTokenizer(new UsernameTokenizer());
        this.etComment.addTextChangedListener(this);
        if (this.uri == null) {
            this.mIntent = getIntent();
            this.POST_ID = this.mIntent.getStringExtra("POST_ID");
            this.USER_ID = this.mIntent.getStringExtra("USER_ID");
            this.POST_TYPE = this.mIntent.getStringExtra("POST_TYPE");
            load(this.POST_ID);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(12);
        actionBar.setCustomView(2130903213);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                break;
        }
        return false;
    }

    private void load(String postId) {
        String ID = postId;
        if (!this.POST_TYPE.equals("3")) {
            this.mVideo.setVisibility(8);
            this.mProgressBar.setVisibility(8);
            this.mBottomView.setVisibility(8);
            this.icPlay.setVisibility(8);
        }
        if (this.isLoggedin) {
            this.URL = "http://social.rabtcdn.com/pin/api/post/" + ID + "/details/?token=" + this.CURRENT_USER_TOKEN;
        } else {
            this.URL = "http://social.rabtcdn.com/pin/api/post/" + ID + "/details/";
        }
        Log.e("PAGE URL", this.URL);
        new OkHttpClient().newCall(new Builder().url(this.URL).build()).enqueue(new C05471(postId));
    }

    private void stripUnderlines(TextView textView) {
        Spannable s = new SpannableString(textView.getText());
        for (URLSpan span : (URLSpan[]) s.getSpans(0, s.length(), URLSpan.class)) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            s.setSpan(new URLSpanNoUnderLine(span.getURL()), start, end, 0);
        }
        textView.setText(s);
    }

    private void loadComments(boolean Reload, long miliSecond) {
        if (Reload) {
            this.commentProgress.setVisibility(0);
            this.noCommentTxt.setVisibility(8);
        }
        String URL = "http://social.rabtcdn.com/pin/api/com/comments/?object_pk=" + this.POST_ID + "&timeStamp=" + miliSecond;
        Log.e("COMMENT URL", URL);
        new OkHttpClient().newCall(new Builder().url(URL).build()).enqueue(new C05574(Reload));
    }

    public static void getTotalHeightofListView(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter != null) {
            int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
            int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), 1073741824);
            for (int i = 0; i < mAdapter.getCount(); i++) {
                View listItem = mAdapter.getView(i, null, listView);
                if (listItem != null) {
                    listItem.setLayoutParams(new RelativeLayout.LayoutParams(-2, -2));
                    listItem.measure(desiredWidth, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = (listView.getDividerHeight() * (mAdapter.getCount() - 1)) + totalHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }

    private boolean validateComment() {
        if (!TextUtils.isEmpty(this.etComment.getText())) {
            return true;
        }
        this.btnSendComment.startAnimation(AnimationUtils.loadAnimation(this, 2130968602));
        return false;
    }

    public void onSendClickListener(View v) {
        if (validateComment()) {
            String CommentURL = "http://social.rabtcdn.com/pin/d_post_comment/?token=" + this.CURRENT_USER_TOKEN;
            new OkHttpClient().newCall(new Builder().url(CommentURL).post(new FormEncodingBuilder().add("comment", this.etComment.getText().toString()).add("object_pk", this.POST_ID).build()).build()).enqueue(new C05666());
        }
    }

    protected void onDestroy() {
        Picasso picasso = this.f9p;
        if (picasso == Picasso.singleton) {
            throw new UnsupportedOperationException("Default singleton instance cannot be shutdown.");
        }
        if (!picasso.shutdown) {
            picasso.cache.clear();
            picasso.cleanupThread.interrupt();
            picasso.stats.statsThread.quit();
            Dispatcher dispatcher = picasso.dispatcher;
            if (dispatcher.service instanceof PicassoExecutorService) {
                dispatcher.service.shutdown();
            }
            dispatcher.downloader.shutdown();
            dispatcher.dispatcherThread.quit();
            Picasso.HANDLER.post(new C12321());
            for (DeferredRequestCreator cancel : picasso.targetToDeferredRequestCreator.values()) {
                cancel.cancel();
            }
            picasso.targetToDeferredRequestCreator.clear();
            picasso.shutdown = true;
        }
        this.postImage.setImageBitmap(null);
        this.postImage = null;
        this.userAvatar = null;
        this.love_anim = null;
        this.btnSendComment = null;
        this.list = null;
        this.sendReport = null;
        this.mImageVideoHolder = null;
        this.mProgressBar = null;
        this.mPlayTime = null;
        this.icPlay = null;
        this.mBottomView = null;
        if (this.mVideo.isPlaying()) {
            this.mVideo.stopPlayback();
        }
        this.mVideo = null;
        this.mSeekBar = null;
        this.mPlay = null;
        this.mProgressBar = null;
        Picasso.with(this.mContext).cancelExistingRequest(this.userAvatar);
        Picasso.with(this.mContext).cancelExistingRequest(this.postImage);
        recycleImagesFromView(findViewById(2131362460));
        recycleImagesFromView(findViewById(2131362433));
        unbindDrawables(findViewById(2131362460));
        unbindDrawables(findViewById(2131362433));
        unbindDrawables(findViewById(2131362453));
        unbindDrawables(findViewById(2131362446));
        unbindDrawables(findViewById(2131362445));
        super.onDestroy();
    }

    public static void recycleImagesFromView(View view) {
        if (view instanceof ImageView) {
            Drawable drawable = ((ImageView) view).getDrawable();
            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                if (bitmapDrawable.getBitmap() != null) {
                    bitmapDrawable.getBitmap().recycle();
                }
            }
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                recycleImagesFromView(((ViewGroup) view).getChildAt(i));
            }
        }
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

    public void afterTextChanged(Editable s) {
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int cursor = start;
        if (start >= s.length()) {
            cursor = s.length() - 1;
        }
        if (isValidToken(s, cursor)) {
            String token = getToken(s, start);
            new UserMentionAsyncTask(this).execute(new String[]{token});
        }
    }

    private boolean isValidToken(CharSequence text, int cursor) {
        for (int i = cursor; i >= 0; i--) {
            if (text.charAt(i) == '@') {
                return true;
            }
            if (text.charAt(i) == ' ') {
                return false;
            }
        }
        return false;
    }

    private String getToken(CharSequence text, int cursor) {
        return text.subSequence(findTokenStart(text, cursor), findTokenEnd(text, cursor)).toString();
    }

    private int findTokenStart(CharSequence text, int cursor) {
        int i = cursor;
        while (i > 0 && text.charAt(i - 1) != '@') {
            i--;
        }
        return i;
    }

    private int findTokenEnd(CharSequence text, int cursor) {
        int i = cursor;
        int len = text.length();
        while (i < len && text.charAt(i) != ' ' && text.charAt(i) != ',' && text.charAt(i) != '.') {
            i++;
        }
        return i;
    }

    private void showHide(boolean hide) {
        if (this.mBottomView == null) {
            return;
        }
        if (!hide) {
            this.mBottomView.setVisibility(8);
        } else if (this.mBottomView.getVisibility() == 0) {
            this.mBottomView.setVisibility(8);
        } else {
            this.mBottomView.setVisibility(0);
        }
    }

    private void playVideo() {
        this.mProgressBar.setVisibility(0);
        this.mVideo.setVideoPath(this.VIDEO_URL);
        this.mVideo.requestFocus();
        this.mVideo.setOnPreparedListener(new C05709());
        this.mVideo.setOnCompletionListener(new OnCompletionListener() {
            public final void onCompletion(MediaPlayer mp) {
                JahanbinDetailsActivity.this.mPlay.setImageResource(2130838107);
                JahanbinDetailsActivity.this.mPlayTime.setText("00:00");
                JahanbinDetailsActivity.this.mSeekBar.setProgress(0);
                JahanbinDetailsActivity.this.showHide(true);
            }
        });
        this.mVideo.setOnErrorListener(new OnErrorListener() {
            public final boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        this.mVideo.setOnTouchListener(this.mTouchListener);
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        this.mFormatBuilder.setLength(0);
        if (hours > 0) {
            return this.mFormatter.format("%d:%02d:%02d", new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
        }
        return this.mFormatter.format("%02d:%02d", new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("PausedVideoPosition", this.mCurrentPosition);
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.mCurrentPosition = savedInstanceState.getInt("PausedVideoPosition");
        this.mVideo.seekTo(this.mCurrentPosition);
    }

    protected void onPause() {
        super.onPause();
        try {
            if (this.mVideo != null) {
                this.mCurrentPosition = this.mVideo.getCurrentPosition();
                this.mVideo.pause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onResume() {
        super.onResume();
        try {
            if (this.mVideo != null) {
                this.mVideo.seekTo(this.mCurrentPosition);
                this.mVideo.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case 2131362456:
                if (this.mVideo.isPlaying()) {
                    this.mVideo.pause();
                    this.mPlay.setImageResource(2130838105);
                    return;
                }
                this.mVideo.start();
                this.mPlay.setImageResource(2130838106);
            default:
        }
    }
}

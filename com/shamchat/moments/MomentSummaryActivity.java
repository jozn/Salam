package com.shamchat.moments;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shamchat.activity.ChatActivity;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.MomentProvider;
import java.util.Arrays;
import java.util.List;

public class MomentSummaryActivity extends AppCompatActivity {
    private MomentSummaryViewPagerAdapter adapter;
    private TextView commentCount;
    private ViewPager imageViewPager;
    private TextView likeCount;
    private String myUserId;
    private ContentResolver resolver;

    /* renamed from: com.shamchat.moments.MomentSummaryActivity.1 */
    class C11401 implements OnClickListener {
        final /* synthetic */ TextView val$btLike;
        final /* synthetic */ String val$momentId;

        C11401(TextView textView, String str) {
            this.val$btLike = textView;
            this.val$momentId = str;
        }

        public final void onClick(View v) {
            if (this.val$btLike.getText().toString().equalsIgnoreCase("Like")) {
                this.val$btLike.setText("Unlike");
                MomentSummaryActivity.this.likePost(this.val$momentId, false);
                return;
            }
            this.val$btLike.setText("Like");
            MomentSummaryActivity.this.likePost(this.val$momentId, true);
        }
    }

    /* renamed from: com.shamchat.moments.MomentSummaryActivity.2 */
    class C11412 implements OnClickListener {
        final /* synthetic */ String val$momentId;

        C11412(String str) {
            this.val$momentId = str;
        }

        public final void onClick(View v) {
            Intent intent = new Intent(MomentSummaryActivity.this.getApplicationContext(), MomentDetailActivity.class);
            intent.putExtra("momentId", this.val$momentId);
            intent.setFlags(67108864);
            MomentSummaryActivity.this.startActivity(intent);
        }
    }

    /* renamed from: com.shamchat.moments.MomentSummaryActivity.3 */
    class C11423 implements OnClickListener {
        final /* synthetic */ String val$momentId;

        C11423(String str) {
            this.val$momentId = str;
        }

        public final void onClick(View v) {
            Intent intent = new Intent(MomentSummaryActivity.this.getApplicationContext(), MomentDetailActivity.class);
            intent.putExtra("momentId", this.val$momentId);
            intent.setFlags(67108864);
            MomentSummaryActivity.this.startActivity(intent);
        }
    }

    /* renamed from: com.shamchat.moments.MomentSummaryActivity.4 */
    class C11454 implements Runnable {
        final /* synthetic */ boolean val$isUnlike;
        final /* synthetic */ String val$momentId;

        /* renamed from: com.shamchat.moments.MomentSummaryActivity.4.1 */
        class C11431 implements Runnable {
            C11431() {
            }

            public final void run() {
                int count = Integer.valueOf(MomentSummaryActivity.this.likeCount.getText().toString()).intValue() + 1;
                MomentSummaryActivity.this.likeCount.setText(String.valueOf(count));
                if (count == 0) {
                    MomentSummaryActivity.this.likeCount.setVisibility(4);
                } else {
                    MomentSummaryActivity.this.likeCount.setVisibility(0);
                }
            }
        }

        /* renamed from: com.shamchat.moments.MomentSummaryActivity.4.2 */
        class C11442 implements Runnable {
            C11442() {
            }

            public final void run() {
                int count = Integer.valueOf(MomentSummaryActivity.this.likeCount.getText().toString()).intValue() - 1;
                MomentSummaryActivity.this.likeCount.setText(String.valueOf(count));
                if (count == 0) {
                    MomentSummaryActivity.this.likeCount.setVisibility(4);
                } else {
                    MomentSummaryActivity.this.likeCount.setVisibility(0);
                }
            }
        }

        C11454(boolean z, String str) {
            this.val$isUnlike = z;
            this.val$momentId = str;
        }

        public final void run() {
            ContentValues values = new ContentValues();
            if (this.val$isUnlike) {
                values.put("like_status", Integer.valueOf(0));
                values.put("post_action_requested", "DELETE");
                MomentSummaryActivity.this.resolver.update(MomentProvider.CONTENT_URI_LIKE, values, "post_id=? AND user_id=?", new String[]{this.val$momentId, MomentSummaryActivity.this.myUserId});
                MomentSummaryActivity.this.runOnUiThread(new C11442());
                return;
            }
            values.put("post_id", this.val$momentId);
            values.put(ChatActivity.INTENT_EXTRA_USER_ID, MomentSummaryActivity.this.myUserId);
            values.put("like_status", Integer.valueOf(1));
            values.put("post_action_requested", "UPDATE");
            if (MomentSummaryActivity.this.resolver.update(MomentProvider.CONTENT_URI_LIKE, values, "post_id=? AND user_id=?", new String[]{this.val$momentId, MomentSummaryActivity.this.myUserId}) == 0) {
                values.remove("post_action_requested");
                values.put("like_id", MomentSummaryActivity.this.myUserId + "_" + System.currentTimeMillis());
                MomentSummaryActivity.this.resolver.insert(MomentProvider.CONTENT_URI_LIKE, values);
            }
            MomentSummaryActivity.this.runOnUiThread(new C11431());
        }
    }

    public MomentSummaryActivity() {
        this.imageViewPager = null;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(2130903187);
        this.imageViewPager = (ViewPager) findViewById(2131362382);
        this.likeCount = (TextView) findViewById(2131362384);
        this.commentCount = (TextView) findViewById(2131362385);
        this.resolver = getContentResolver();
        this.myUserId = SHAMChatApplication.getConfig().userId;
        String listString = getIntent().getStringExtra("imgList");
        String momentId = getIntent().getStringExtra("momentId");
        List<String> imgList = Arrays.asList(listString.split("\\s*,\\s*"));
        System.out.println("Moments summary");
        this.adapter = new MomentSummaryViewPagerAdapter(getApplicationContext(), imgList);
        this.imageViewPager.setAdapter(this.adapter);
        TextView btLike = (TextView) findViewById(2131362310);
        btLike.setOnClickListener(new C11401(btLike, momentId));
        ((TextView) findViewById(2131362311)).setOnClickListener(new C11412(momentId));
        ((LinearLayout) findViewById(2131362312)).setOnClickListener(new C11423(momentId));
        Cursor likeCursor = null;
        Cursor likeCountCursor = null;
        Cursor commentCountCursor = null;
        try {
            likeCursor = this.resolver.query(MomentProvider.CONTENT_URI_LIKE, new String[]{"like_id"}, "post_id=? AND like_status=? AND user_id=?", new String[]{momentId, "1", this.myUserId}, null);
            if (likeCursor.getCount() > 0) {
                btLike.setText("Unlike");
            }
            likeCountCursor = this.resolver.query(MomentProvider.CONTENT_URI_LIKE, new String[]{"like_id"}, "post_id=? AND like_status=?", new String[]{momentId, "1"}, null);
            this.likeCount.setText(String.valueOf(likeCountCursor.getCount()));
            commentCountCursor = this.resolver.query(MomentProvider.CONTENT_URI_COMMENT, new String[]{"comment_id"}, "post_id=? AND comment_status=?", new String[]{momentId, "1"}, null);
            this.commentCount.setText(String.valueOf(commentCountCursor.getCount()));
            if (likeCursor != null) {
                likeCursor.close();
            }
            if (likeCountCursor != null) {
                likeCountCursor.close();
            }
            if (commentCountCursor != null) {
                commentCountCursor.close();
            }
        } catch (Throwable th) {
            if (likeCursor != null) {
                likeCursor.close();
            }
            if (likeCountCursor != null) {
                likeCountCursor.close();
            }
            if (commentCountCursor != null) {
                commentCountCursor.close();
            }
        }
    }

    private void likePost(String momentId, boolean isUnlike) {
        new Thread(new C11454(isUnlike, momentId)).start();
    }
}

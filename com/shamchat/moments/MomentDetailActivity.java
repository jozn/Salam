package com.shamchat.moments;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.media.TransportMediator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.text.Spannable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.path.android.jobqueue.JobManager;
import com.shamchat.activity.ChatActivity;
import com.shamchat.activity.LocalVideoFilePreview;
import com.shamchat.activity.MyProfileActivity;
import com.shamchat.activity.ProfileActivity;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.MomentProvider;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.jobs.MomentsUploadJob;
import com.shamchat.stickers.ShamSmileyAndStickerMoments;
import com.shamchat.utils.Emoticons;
import com.shamchat.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;
import com.squareup.picasso.RequestHandler.Result;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;

public class MomentDetailActivity extends AppCompatActivity implements OnClickListener {
    private LinearLayout commentContainer;
    private ContentObserver commentObserver;
    private List<View> commentViewList;
    private Bitmap currentUserBitmap;
    private Cursor cursor;
    private File folder;
    private LinearLayout fragmentContainer;
    private FragmentManager fragmentManager;
    private LinearLayout imageContainer;
    private List<String> imageList;
    private ImageLoader imageLoader;
    private LayoutInflater inflater;
    private boolean isInitLoadComplete;
    private JobManager jobManager;
    private LinearLayout likeContainer;
    private ContentObserver likeObserver;
    private File mainFolder;
    private Handler mainHanlder;
    private ScrollView mainScrollView;
    private DisplayImageOptions mediaImageOptions;
    private String momentId;
    private String myUserId;
    private ImageView playButton;
    private TextView postedTextView;
    private DisplayImageOptions profImgOptions;
    private ContentResolver resolver;
    private Fragment smileyFragment;
    private LinearLayout stickerContainer;
    private EditText textMessage;
    private ImageView thumbnailView;
    private String username;
    private String videoUrl;

    /* renamed from: com.shamchat.moments.MomentDetailActivity.11 */
    class AnonymousClass11 implements Runnable {
        final /* synthetic */ String val$commentId;
        final /* synthetic */ String val$message;

        AnonymousClass11(String str, String str2) {
            this.val$commentId = str;
            this.val$message = str2;
        }

        public final void run() {
            ContentValues values = new ContentValues();
            values.put("comment_id", this.val$commentId);
            values.put("commented_text", this.val$message);
            values.put(ChatActivity.INTENT_EXTRA_USER_ID, MomentDetailActivity.this.myUserId);
            values.put("post_id", MomentDetailActivity.this.momentId);
            MomentDetailActivity.this.resolver.insert(MomentProvider.CONTENT_URI_COMMENT, values);
            MomentDetailActivity.this.jobManager.addJobInBackground(new MomentsUploadJob());
        }
    }

    /* renamed from: com.shamchat.moments.MomentDetailActivity.14 */
    class AnonymousClass14 implements DialogInterface.OnClickListener {
        final /* synthetic */ String val$commentID;
        final /* synthetic */ View val$v;

        AnonymousClass14(String str, View view) {
            this.val$commentID = str;
            this.val$v = view;
        }

        public final void onClick(DialogInterface dialog, int which) {
            if (which == 0) {
                ContentValues values = new ContentValues();
                values.put("comment_status", Integer.valueOf(0));
                values.put("post_action_requested", "DELETE");
                MomentDetailActivity.this.resolver.update(MomentProvider.CONTENT_URI_COMMENT, values, "comment_id=?", new String[]{this.val$commentID});
                MomentDetailActivity.this.jobManager.addJobInBackground(new MomentsUploadJob());
                MomentDetailActivity.this.commentContainer.removeView(this.val$v);
            }
        }
    }

    /* renamed from: com.shamchat.moments.MomentDetailActivity.1 */
    class C11071 implements OnClickListener {
        C11071() {
        }

        public final void onClick(View v) {
            Intent intent = new Intent(MomentDetailActivity.this.getApplicationContext(), MomentLikesDetailActivity.class);
            intent.putExtra("postId", MomentDetailActivity.this.momentId);
            MomentDetailActivity.this.startActivity(intent);
        }
    }

    /* renamed from: com.shamchat.moments.MomentDetailActivity.2 */
    class C11092 implements OnTouchListener {

        /* renamed from: com.shamchat.moments.MomentDetailActivity.2.1 */
        class C11081 implements Runnable {
            C11081() {
            }

            public final void run() {
                MomentDetailActivity.this.mainScrollView.fullScroll(TransportMediator.KEYCODE_MEDIA_RECORD);
            }
        }

        C11092() {
        }

        public final boolean onTouch(View v, MotionEvent event) {
            MomentDetailActivity.this.fragmentContainer.setVisibility(8);
            MomentDetailActivity.this.mainScrollView.post(new C11081());
            return false;
        }
    }

    /* renamed from: com.shamchat.moments.MomentDetailActivity.3 */
    class C11103 implements OnClickListener {
        final /* synthetic */ String val$userId;

        C11103(String str) {
            this.val$userId = str;
        }

        public final void onClick(View v) {
            if (this.val$userId.equalsIgnoreCase(MomentDetailActivity.this.myUserId)) {
                Intent i = new Intent(MomentDetailActivity.this.getApplicationContext(), MyProfileActivity.class);
                i.setFlags(ClientDefaults.MAX_MSG_SIZE);
                MomentDetailActivity.this.startActivity(i);
                return;
            }
            Intent intent = new Intent(v.getContext(), ProfileActivity.class);
            intent.putExtra(ProfileActivity.EXTRA_USER_ID, Utils.createXmppUserIdByUserId(this.val$userId));
            intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
            MomentDetailActivity.this.startActivity(intent);
        }
    }

    /* renamed from: com.shamchat.moments.MomentDetailActivity.4 */
    class C11114 implements OnClickListener {
        final /* synthetic */ TextView val$btLike;

        C11114(TextView textView) {
            this.val$btLike = textView;
        }

        public final void onClick(View v) {
            if (this.val$btLike.getText().toString().equalsIgnoreCase("Like")) {
                this.val$btLike.setText("Unlike");
                MomentDetailActivity.this.likePost(MomentDetailActivity.this.momentId, false);
                return;
            }
            this.val$btLike.setText("Like");
            MomentDetailActivity.this.likePost(MomentDetailActivity.this.momentId, true);
        }
    }

    /* renamed from: com.shamchat.moments.MomentDetailActivity.5 */
    class C11135 implements OnClickListener {

        /* renamed from: com.shamchat.moments.MomentDetailActivity.5.1 */
        class C11121 implements Runnable {
            C11121() {
            }

            public final void run() {
                MomentDetailActivity.this.mainScrollView.fullScroll(TransportMediator.KEYCODE_MEDIA_RECORD);
            }
        }

        C11135() {
        }

        public final void onClick(View v) {
            MomentDetailActivity.this.mainScrollView.post(new C11121());
            Utils.showKeyboard(MomentDetailActivity.this.textMessage, MomentDetailActivity.this.getApplicationContext());
        }
    }

    /* renamed from: com.shamchat.moments.MomentDetailActivity.6 */
    class C11206 implements Runnable {

        /* renamed from: com.shamchat.moments.MomentDetailActivity.6.1 */
        class C11141 implements Runnable {
            C11141() {
            }

            public final void run() {
                MomentDetailActivity.this.commentContainer.removeAllViews();
            }
        }

        /* renamed from: com.shamchat.moments.MomentDetailActivity.6.2 */
        class C11152 implements OnClickListener {
            final /* synthetic */ String val$userId;

            C11152(String str) {
                this.val$userId = str;
            }

            public final void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProfileActivity.class);
                intent.putExtra(ProfileActivity.EXTRA_USER_ID, Utils.createXmppUserIdByUserId(this.val$userId));
                v.getContext().startActivity(intent);
            }
        }

        /* renamed from: com.shamchat.moments.MomentDetailActivity.6.3 */
        class C11193 implements Runnable {
            final /* synthetic */ ImageView val$imgProfile;
            final /* synthetic */ String val$userId;
            final /* synthetic */ View val$view;

            /* renamed from: com.shamchat.moments.MomentDetailActivity.6.3.1 */
            class C11161 implements OnClickListener {
                C11161() {
                }

                public final void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ProfileActivity.class);
                    intent.putExtra(ProfileActivity.EXTRA_USER_ID, Utils.createXmppUserIdByUserId(C11193.this.val$userId));
                    v.getContext().startActivity(intent);
                }
            }

            /* renamed from: com.shamchat.moments.MomentDetailActivity.6.3.2 */
            class C11172 implements OnClickListener {
                C11172() {
                }

                public final void onClick(View v) {
                    Intent i = new Intent(v.getContext(), MyProfileActivity.class);
                    i.setFlags(ClientDefaults.MAX_MSG_SIZE);
                    v.getContext().startActivity(i);
                }
            }

            /* renamed from: com.shamchat.moments.MomentDetailActivity.6.3.3 */
            class C11183 implements OnLongClickListener {
                C11183() {
                }

                public final boolean onLongClick(View v) {
                    MomentDetailActivity.this.showOptionsDialog((String) v.getTag(), v);
                    return false;
                }
            }

            C11193(String str, ImageView imageView, View view) {
                this.val$userId = str;
                this.val$imgProfile = imageView;
                this.val$view = view;
            }

            public final void run() {
                if (this.val$userId.equalsIgnoreCase(MomentDetailActivity.this.myUserId)) {
                    if (MomentDetailActivity.this.currentUserBitmap != null) {
                        this.val$imgProfile.setImageBitmap(MomentDetailActivity.this.currentUserBitmap);
                    }
                    this.val$imgProfile.setOnClickListener(new C11172());
                    this.val$view.setOnLongClickListener(new C11183());
                } else {
                    MomentDetailActivity.this.imageLoader.displayImage("file://" + MomentDetailActivity.this.folder.getAbsolutePath() + MqttTopic.TOPIC_LEVEL_SEPARATOR + this.val$userId + ".jpg", this.val$imgProfile, MomentDetailActivity.this.profImgOptions);
                    this.val$imgProfile.setOnClickListener(new C11161());
                }
                MomentDetailActivity.this.commentContainer.addView(this.val$view);
            }
        }

        C11206() {
        }

        public final void run() {
            Cursor cursor = null;
            Cursor userCursor = null;
            try {
                r5 = new String[2];
                r5[0] = MomentDetailActivity.this.momentId;
                r5[1] = "1";
                cursor = MomentDetailActivity.this.resolver.query(MomentProvider.CONTENT_URI_COMMENT, null, "post_id=? AND comment_status=?", r5, "comment_datetime ASC");
                MomentDetailActivity.this.runOnUiThread(new C11141());
                while (cursor.moveToNext()) {
                    View view = MomentDetailActivity.this.inflater.inflate(2130903161, null);
                    ((TextView) view.findViewById(2131362292)).setText(Emoticons.getSmiledText(SHAMChatApplication.getMyApplicationContext(), cursor.getString(cursor.getColumnIndex("commented_text"))), BufferType.SPANNABLE);
                    view.setTag(cursor.getString(cursor.getColumnIndex("comment_id")));
                    String userId = cursor.getString(cursor.getColumnIndex(ChatActivity.INTENT_EXTRA_USER_ID));
                    ImageView imgProfile = (ImageView) view.findViewById(2131361927);
                    imgProfile.setOnClickListener(new C11152(userId));
                    TextView userName = (TextView) view.findViewById(2131362291);
                    TextView postedTime = (TextView) view.findViewById(2131361923);
                    userCursor = MomentDetailActivity.this.resolver.query(UserProvider.CONTENT_URI_USER, new String[]{"name"}, "userId=?", new String[]{userId}, null);
                    if (userCursor == null || !userCursor.moveToFirst()) {
                        userName.setText("name");
                    } else {
                        userCursor.getString(userCursor.getColumnIndex("name"));
                        userName.setText(userCursor.getString(userCursor.getColumnIndex("name")));
                        postedTime.setText(cursor.getString(cursor.getColumnIndex("comment_datetime")));
                    }
                    MomentDetailActivity.this.runOnUiThread(new C11193(userId, imgProfile, view));
                }
                if (userCursor != null) {
                    userCursor.close();
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Throwable th) {
                if (userCursor != null) {
                    userCursor.close();
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }

    /* renamed from: com.shamchat.moments.MomentDetailActivity.7 */
    class C11237 implements Runnable {

        /* renamed from: com.shamchat.moments.MomentDetailActivity.7.1 */
        class C11211 implements Runnable {
            final /* synthetic */ Cursor val$cursor;

            C11211(Cursor cursor) {
                this.val$cursor = cursor;
            }

            public final void run() {
                if (this.val$cursor.getCount() == 0) {
                    MomentDetailActivity.this.likeContainer.setVisibility(8);
                } else {
                    MomentDetailActivity.this.likeContainer.setVisibility(0);
                }
                MomentDetailActivity.this.likeContainer.removeAllViews();
            }
        }

        /* renamed from: com.shamchat.moments.MomentDetailActivity.7.2 */
        class C11222 implements Runnable {
            final /* synthetic */ ImageView val$imageView;
            final /* synthetic */ String val$userId;

            C11222(String str, ImageView imageView) {
                this.val$userId = str;
                this.val$imageView = imageView;
            }

            public final void run() {
                if (!this.val$userId.equalsIgnoreCase(MomentDetailActivity.this.myUserId)) {
                    MomentDetailActivity.this.imageLoader.displayImage("file://" + MomentDetailActivity.this.folder.getAbsolutePath() + MqttTopic.TOPIC_LEVEL_SEPARATOR + this.val$userId + ".jpg", this.val$imageView, MomentDetailActivity.this.profImgOptions);
                } else if (MomentDetailActivity.this.currentUserBitmap != null) {
                    this.val$imageView.setImageBitmap(MomentDetailActivity.this.currentUserBitmap);
                } else {
                    this.val$imageView.setImageResource(2130837944);
                }
                MomentDetailActivity.this.likeContainer.addView(this.val$imageView);
            }
        }

        C11237() {
        }

        public final void run() {
            Cursor cursor = MomentDetailActivity.this.resolver.query(MomentProvider.CONTENT_URI_LIKE, new String[]{ChatActivity.INTENT_EXTRA_USER_ID}, "post_id=? AND like_status=?", new String[]{MomentDetailActivity.this.momentId, "1"}, "liked_datetime DESC");
            try {
                MomentDetailActivity.this.runOnUiThread(new C11211(cursor));
                while (cursor.moveToNext()) {
                    String userId = cursor.getString(cursor.getColumnIndex(ChatActivity.INTENT_EXTRA_USER_ID));
                    ImageView imageView = new ImageView(MomentDetailActivity.this.getApplicationContext());
                    LayoutParams params = new LayoutParams(70, 70);
                    params.setMargins(5, 5, 5, 5);
                    imageView.setScaleType(ScaleType.FIT_XY);
                    imageView.setLayoutParams(params);
                    MomentDetailActivity.this.runOnUiThread(new C11222(userId, imageView));
                }
            } finally {
                cursor.close();
            }
        }
    }

    /* renamed from: com.shamchat.moments.MomentDetailActivity.8 */
    class C11248 implements Runnable {
        final /* synthetic */ boolean val$isUnlike;
        final /* synthetic */ String val$momentId;

        C11248(boolean z, String str) {
            this.val$isUnlike = z;
            this.val$momentId = str;
        }

        public final void run() {
            ContentValues values = new ContentValues();
            if (this.val$isUnlike) {
                values.put("like_status", Integer.valueOf(0));
                values.put("post_action_requested", "DELETE");
                MomentDetailActivity.this.resolver.update(MomentProvider.CONTENT_URI_LIKE, values, "post_id=? AND user_id=?", new String[]{this.val$momentId, MomentDetailActivity.this.myUserId});
                MomentDetailActivity.this.jobManager.addJobInBackground(new MomentsUploadJob());
                return;
            }
            values.put("post_id", this.val$momentId);
            values.put(ChatActivity.INTENT_EXTRA_USER_ID, MomentDetailActivity.this.myUserId);
            values.put("like_status", Integer.valueOf(1));
            values.put("post_action_requested", "UPDATE");
            if (MomentDetailActivity.this.resolver.update(MomentProvider.CONTENT_URI_LIKE, values, "post_id=? AND user_id=?", new String[]{this.val$momentId, MomentDetailActivity.this.myUserId}) == 0) {
                values.remove("post_action_requested");
                values.put("like_id", MomentDetailActivity.this.myUserId + "_" + System.currentTimeMillis());
                MomentDetailActivity.this.resolver.insert(MomentProvider.CONTENT_URI_LIKE, values);
                MomentDetailActivity.this.jobManager.addJobInBackground(new MomentsUploadJob());
            }
        }
    }

    /* renamed from: com.shamchat.moments.MomentDetailActivity.9 */
    class C11259 implements OnLongClickListener {
        C11259() {
        }

        public final boolean onLongClick(View v) {
            MomentDetailActivity.this.showOptionsDialog((String) v.getTag(), v);
            return false;
        }
    }

    private class CommentObserver extends ContentObserver {

        /* renamed from: com.shamchat.moments.MomentDetailActivity.CommentObserver.1 */
        class C11271 implements Runnable {

            /* renamed from: com.shamchat.moments.MomentDetailActivity.CommentObserver.1.1 */
            class C11261 implements Runnable {
                C11261() {
                }

                public final void run() {
                    MomentDetailActivity.this.updateComments();
                }
            }

            C11271() {
            }

            public final void run() {
                MomentDetailActivity.this.mainHanlder.post(new C11261());
            }
        }

        public CommentObserver() {
            super(MomentDetailActivity.this.mainHanlder);
        }

        public final void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new Thread(new C11271()).start();
        }
    }

    private class LikeObserver extends ContentObserver {

        /* renamed from: com.shamchat.moments.MomentDetailActivity.LikeObserver.1 */
        class C11291 implements Runnable {

            /* renamed from: com.shamchat.moments.MomentDetailActivity.LikeObserver.1.1 */
            class C11281 implements Runnable {
                C11281() {
                }

                public final void run() {
                    MomentDetailActivity.this.updateLikes();
                }
            }

            C11291() {
            }

            public final void run() {
                MomentDetailActivity.this.mainHanlder.post(new C11281());
            }
        }

        public LikeObserver() {
            super(MomentDetailActivity.this.mainHanlder);
        }

        public final void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new Thread(new C11291()).start();
        }
    }

    class PicassoVideoFrameRequestHandler extends RequestHandler {
        public final boolean canHandleRequest(Request data) {
            System.out.println("can handle request " + data.uri.getScheme());
            return true;
        }

        public final Result load$71fa0c91(Request data) throws IOException {
            System.out.println("Loading request handler");
            try {
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                System.out.println("Loading request handler url " + data.uri);
                mediaMetadataRetriever.setDataSource(data.uri.toString(), new HashMap());
                System.out.println("Loading request handler setDataSource ");
                Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime();
                if (bitmap != null) {
                    System.out.println("Loading request handler bit map not null");
                    return new Result(bitmap, LoadedFrom.DISK);
                }
                System.out.println("Loading request handler bit map null");
                mediaMetadataRetriever.release();
                return null;
            } catch (Exception e) {
                System.out.println("Loading request handler exception " + e);
                return null;
            }
        }
    }

    public MomentDetailActivity() {
        this.imageList = null;
        this.videoUrl = null;
        this.mainHanlder = new Handler();
        this.commentObserver = new CommentObserver();
        this.likeObserver = new LikeObserver();
        this.isInitLoadComplete = false;
    }

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(1);
        setContentView(2130903183);
        this.mainFolder = Utils.getMomentsFolder();
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        this.resolver = getContentResolver();
        this.momentId = getIntent().getStringExtra("momentId");
        this.myUserId = SHAMChatApplication.getConfig().userId;
        Cursor userCursor = this.resolver.query(UserProvider.CONTENT_URI_USER, new String[]{"name"}, "userId=?", new String[]{this.myUserId}, null);
        userCursor.moveToFirst();
        this.username = userCursor.getString(userCursor.getColumnIndex("name"));
        userCursor.close();
        UserProvider userProvider = new UserProvider();
        this.currentUserBitmap = UserProvider.getMyProfileImage();
        this.cursor = this.resolver.query(MomentProvider.CONTENT_URI_MOMENT, null, "post_id=?", new String[]{this.momentId}, null);
        this.cursor.moveToFirst();
        this.resolver.registerContentObserver(MomentProvider.CONTENT_URI_LIKE, true, this.likeObserver);
        this.resolver.registerContentObserver(MomentProvider.CONTENT_URI_COMMENT, true, this.commentObserver);
        initComponents();
        displayData();
    }

    private void initComponents() {
        Cursor userCursor = null;
        Cursor likeCursor = null;
        try {
            this.fragmentManager = getSupportFragmentManager();
            this.smileyFragment = new ShamSmileyAndStickerMoments();
            ((ImageView) findViewById(2131361941)).setOnClickListener(this);
            ((ImageView) findViewById(2131361946)).setOnClickListener(this);
            this.imageLoader = ImageLoader.getInstance();
            Builder builder = new Builder();
            builder.imageResOnLoading = 2130838116;
            builder.imageResForEmptyUri = 2130837992;
            builder.imageResOnFail = 2130837992;
            builder.cacheInMemory = true;
            builder.cacheOnDisc = false;
            builder.imageScaleType$641b8ab2 = ImageScaleType.EXACTLY_STRETCHED$641b8ab2;
            this.mediaImageOptions = builder.bitmapConfig(Config.ALPHA_8).build();
            this.fragmentContainer = (LinearLayout) findViewById(2131361947);
            this.imageContainer = (LinearLayout) findViewById(2131362302);
            this.imageContainer.setOnClickListener(this);
            this.imageContainer.setVisibility(8);
            this.commentContainer = (LinearLayout) findViewById(2131362375);
            this.likeContainer = (LinearLayout) findViewById(2131362374);
            this.likeContainer.setOnClickListener(new C11071());
            this.mainScrollView = (ScrollView) findViewById(2131362371);
            this.postedTextView = (TextView) findViewById(2131362292);
            this.thumbnailView = (ImageView) findViewById(2131362304);
            this.thumbnailView.setOnClickListener(this);
            this.playButton = (ImageView) findViewById(2131362305);
            this.stickerContainer = (LinearLayout) findViewById(2131362300);
            this.textMessage = (EditText) findViewById(2131361945);
            this.textMessage.setOnTouchListener(new C11092());
            this.inflater = (LayoutInflater) getSystemService("layout_inflater");
            this.commentViewList = new ArrayList();
            String userId = this.cursor.getString(this.cursor.getColumnIndex(ChatActivity.INTENT_EXTRA_USER_ID));
            ImageView userProfImage = (ImageView) findViewById(2131361927);
            this.folder = new File(Environment.getExternalStorageDirectory() + "/salam/thumbnails");
            builder = new Builder();
            builder.imageResOnLoading = 2130837944;
            builder.imageResForEmptyUri = 2130837944;
            builder.imageResOnFail = 2130837944;
            builder.cacheInMemory = false;
            builder.cacheOnDisc = false;
            builder.imageScaleType$641b8ab2 = ImageScaleType.EXACTLY_STRETCHED$641b8ab2;
            this.profImgOptions = builder.bitmapConfig(Config.ALPHA_8).build();
            this.imageLoader.displayImage("file://" + this.folder.getAbsolutePath() + MqttTopic.TOPIC_LEVEL_SEPARATOR + userId + ".jpg", userProfImage, this.profImgOptions);
            userProfImage.setOnClickListener(new C11103(userId));
            userCursor = this.resolver.query(UserProvider.CONTENT_URI_USER, new String[]{"name"}, "userId=?", new String[]{userId}, null);
            userCursor.moveToFirst();
            String userName = userCursor.getString(userCursor.getColumnIndex("name"));
            ((TextView) findViewById(2131362291)).setText(userName);
            ((TextView) findViewById(2131362376)).setText(userName);
            ((TextView) findViewById(2131361923)).setText(this.cursor.getString(this.cursor.getColumnIndex("posted_datetime")));
            TextView btLike = (TextView) findViewById(2131362310);
            btLike.setOnClickListener(new C11114(btLike));
            likeCursor = this.resolver.query(MomentProvider.CONTENT_URI_LIKE, new String[]{"like_id"}, "post_id=? AND like_status=? AND user_id=?", new String[]{this.momentId, "1", this.myUserId}, null);
            if (likeCursor.getCount() > 0) {
                btLike.setText("Unlike");
            }
            ((TextView) findViewById(2131362311)).setOnClickListener(new C11135());
            if (userCursor != null) {
                userCursor.close();
            }
            if (likeCursor != null) {
                likeCursor.close();
            }
        } catch (Throwable th) {
            if (userCursor != null) {
                userCursor.close();
            }
            if (likeCursor != null) {
                likeCursor.close();
            }
        }
    }

    private void displayData() {
        Uri uri;
        int x;
        ImageView imageView;
        String imageUrl;
        LayoutParams params;
        String stickerUrlString;
        List<String> stickerList;
        int i;
        String currentfile;
        ImageView rowImageView;
        String postedText = this.cursor.getString(this.cursor.getColumnIndex("post_text"));
        if (postedText == null || postedText.length() <= 0) {
            this.postedTextView.setVisibility(8);
        } else {
            this.postedTextView.setVisibility(0);
            Spannable spannable = Emoticons.getSmiledText(SHAMChatApplication.getMyApplicationContext(), postedText);
            this.postedTextView.setText(spannable, BufferType.SPANNABLE);
        }
        String imgUrlString = this.cursor.getString(this.cursor.getColumnIndex("posted_image_url"));
        boolean isVideo = false;
        this.videoUrl = this.cursor.getString(this.cursor.getColumnIndex("posted_video_url"));
        if (this.videoUrl != null) {
            if (this.videoUrl.length() > 1) {
                this.thumbnailView.setVisibility(0);
                this.playButton.setVisibility(0);
                Uri parse;
                if (this.videoUrl.contains("http://")) {
                    parse = Uri.parse(this.videoUrl);
                    new Picasso.Builder(SHAMChatApplication.getMyApplicationContext()).addRequestHandler(new PicassoVideoFrameRequestHandler()).build().load(uri).into(this.thumbnailView, null);
                    System.out.println("Loading with picasso thumb remote " + this.videoUrl);
                } else {
                    parse = Uri.parse(this.videoUrl);
                    new Picasso.Builder(SHAMChatApplication.getMyApplicationContext()).addRequestHandler(new PicassoVideoFrameRequestHandler()).build().load(uri).into(this.thumbnailView, null);
                    System.out.println("Loading with picasso thumb local " + this.videoUrl);
                }
                isVideo = true;
                if (imgUrlString != null) {
                    this.imageList = Arrays.asList(imgUrlString.split("\\s*,\\s*"));
                    this.thumbnailView.setVisibility(0);
                    if (this.imageList.size() == 1 || isVideo) {
                        if (!isVideo) {
                            this.thumbnailView.setVisibility(8);
                        }
                        this.imageContainer.setVisibility(0);
                        x = 0;
                        while (true) {
                            if (x < this.imageList.size()) {
                                break;
                            }
                            imageView = new ImageView(getApplicationContext());
                            imageUrl = (String) this.imageList.get(x);
                            System.out.println("Loading with picasso details activity 1st " + imageUrl);
                            if (imageUrl.contains(this.mainFolder.getAbsolutePath())) {
                                uri = Uri.parse(imageUrl);
                                Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(uri).into(imageView, null);
                                System.out.println("Loading with picasso details activity" + imageUrl);
                            } else {
                                Picasso.with(SHAMChatApplication.getMyApplicationContext()).load("file://" + imageUrl).into(imageView, null);
                            }
                            params = new LayoutParams(250, 250);
                            params.setMargins(5, 5, 5, 5);
                            imageView.setLayoutParams(params);
                            imageView.setScaleType(ScaleType.FIT_XY);
                            this.imageContainer.addView(imageView);
                            x++;
                        }
                    } else {
                        imageUrl = (String) this.imageList.get(0);
                        if (imageUrl.contains(this.mainFolder.getAbsolutePath())) {
                            StringBuilder stringBuilder = new StringBuilder("file://");
                            Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(r20.append(imageUrl).toString()).into(this.thumbnailView, null);
                        } else {
                            uri = Uri.parse(imageUrl);
                            Picasso with = Picasso.with(SHAMChatApplication.getMyApplicationContext());
                            r19.load(uri).into(this.thumbnailView, null);
                            System.out.println("Loading with picasso details activity" + imageUrl);
                        }
                        this.imageContainer.setVisibility(8);
                    }
                } else if (!isVideo) {
                    this.thumbnailView.setVisibility(8);
                }
                stickerUrlString = this.cursor.getString(this.cursor.getColumnIndex("posted_sticker_url"));
                if (stickerUrlString != null) {
                    stickerList = Arrays.asList(stickerUrlString.split("\\s*,\\s*"));
                    this.stickerContainer.setVisibility(0);
                    for (i = 0; i < stickerList.size(); i++) {
                        currentfile = (String) stickerList.get(i);
                        rowImageView = new ImageView(SHAMChatApplication.getMyApplicationContext());
                        rowImageView.setId(i);
                        if (currentfile.contains(this.mainFolder.getAbsolutePath())) {
                            uri = Uri.parse(currentfile);
                            Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(uri).into(rowImageView, null);
                            System.out.println("Loading with picasso details activity sticker " + currentfile);
                        } else {
                            Picasso.with(SHAMChatApplication.getMyApplicationContext()).load("file://" + currentfile).into(rowImageView, null);
                        }
                        params = new LayoutParams(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD);
                        params.setMargins(5, 5, 5, 5);
                        params.gravity = 16;
                        rowImageView.setLayoutParams(params);
                        this.stickerContainer.addView(rowImageView);
                    }
                }
                updateLikes();
                updateComments();
            }
        }
        this.thumbnailView.setVisibility(8);
        if (imgUrlString != null) {
            this.imageList = Arrays.asList(imgUrlString.split("\\s*,\\s*"));
            this.thumbnailView.setVisibility(0);
            if (this.imageList.size() == 1) {
            }
            if (isVideo) {
                this.thumbnailView.setVisibility(8);
            }
            this.imageContainer.setVisibility(0);
            x = 0;
            while (true) {
                if (x < this.imageList.size()) {
                    break;
                }
                imageView = new ImageView(getApplicationContext());
                imageUrl = (String) this.imageList.get(x);
                System.out.println("Loading with picasso details activity 1st " + imageUrl);
                if (imageUrl.contains(this.mainFolder.getAbsolutePath())) {
                    uri = Uri.parse(imageUrl);
                    Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(uri).into(imageView, null);
                    System.out.println("Loading with picasso details activity" + imageUrl);
                } else {
                    Picasso.with(SHAMChatApplication.getMyApplicationContext()).load("file://" + imageUrl).into(imageView, null);
                }
                params = new LayoutParams(250, 250);
                params.setMargins(5, 5, 5, 5);
                imageView.setLayoutParams(params);
                imageView.setScaleType(ScaleType.FIT_XY);
                this.imageContainer.addView(imageView);
                x++;
            }
        } else if (isVideo) {
            this.thumbnailView.setVisibility(8);
        }
        stickerUrlString = this.cursor.getString(this.cursor.getColumnIndex("posted_sticker_url"));
        if (stickerUrlString != null) {
            stickerList = Arrays.asList(stickerUrlString.split("\\s*,\\s*"));
            this.stickerContainer.setVisibility(0);
            for (i = 0; i < stickerList.size(); i++) {
                currentfile = (String) stickerList.get(i);
                rowImageView = new ImageView(SHAMChatApplication.getMyApplicationContext());
                rowImageView.setId(i);
                if (currentfile.contains(this.mainFolder.getAbsolutePath())) {
                    uri = Uri.parse(currentfile);
                    Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(uri).into(rowImageView, null);
                    System.out.println("Loading with picasso details activity sticker " + currentfile);
                } else {
                    Picasso.with(SHAMChatApplication.getMyApplicationContext()).load("file://" + currentfile).into(rowImageView, null);
                }
                params = new LayoutParams(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD);
                params.setMargins(5, 5, 5, 5);
                params.gravity = 16;
                rowImageView.setLayoutParams(params);
                this.stickerContainer.addView(rowImageView);
            }
        }
        updateLikes();
        updateComments();
    }

    private void updateComments() {
        new Thread(new C11206()).start();
    }

    private void updateLikes() {
        new Thread(new C11237()).start();
    }

    private void likePost(String momentId, boolean isUnlike) {
        new Thread(new C11248(isUnlike, momentId)).start();
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case 2131361941:
                if (this.fragmentContainer.getVisibility() == 0) {
                    this.fragmentContainer.setVisibility(8);
                    return;
                }
                Utils.hideKeyboard(this.textMessage, getApplicationContext());
                this.fragmentContainer.setVisibility(0);
                this.fragmentManager.beginTransaction().setCustomAnimations(2130968589, 2130968590).addToBackStack(null).replace(2131361947, this.smileyFragment).commit();
            case 2131361946:
                String message = this.textMessage.getText().toString();
                if (!message.isEmpty()) {
                    View view = this.inflater.inflate(2130903161, null);
                    ((TextView) view.findViewById(2131362292)).setText(Emoticons.getSmiledText(SHAMChatApplication.getMyApplicationContext(), message.toString()), BufferType.SPANNABLE);
                    String commentId = this.myUserId + "_" + System.currentTimeMillis();
                    view.setTag(commentId);
                    ImageView imgProfile = (ImageView) view.findViewById(2131361927);
                    if (this.currentUserBitmap != null) {
                        imgProfile.setImageBitmap(this.currentUserBitmap);
                    }
                    ((TextView) view.findViewById(2131362291)).setText(this.username);
                    ((TextView) view.findViewById(2131361923)).setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
                    view.setOnLongClickListener(new C11259());
                    imgProfile.setOnClickListener(new OnClickListener() {
                        public final void onClick(View v) {
                            Intent i = new Intent(v.getContext(), MyProfileActivity.class);
                            i.setFlags(ClientDefaults.MAX_MSG_SIZE);
                            v.getContext().startActivity(i);
                        }
                    });
                    new Thread(new AnonymousClass11(commentId, message)).start();
                    view.setOnLongClickListener(new OnLongClickListener() {
                        public final boolean onLongClick(View v) {
                            MomentDetailActivity.this.showOptionsDialog((String) v.getTag(), v);
                            return false;
                        }
                    });
                    this.commentContainer.addView(view);
                    this.textMessage.setText(BuildConfig.VERSION_NAME);
                    this.mainScrollView.post(new Runnable() {
                        public final void run() {
                            MomentDetailActivity.this.mainScrollView.fullScroll(TransportMediator.KEYCODE_MEDIA_RECORD);
                        }
                    });
                }
            case 2131362302:
                if (this.imageList != null && this.imageList.size() > 0) {
                    intent = new Intent(getApplicationContext(), MomentSummaryActivity.class);
                    intent.putExtra("imgList", this.imageList.toString().replace("[", BuildConfig.VERSION_NAME).replace("]", BuildConfig.VERSION_NAME));
                    intent.putExtra("momentId", this.momentId);
                    startActivity(intent);
                }
            case 2131362304:
                if ((this.imageList == null || this.imageList.size() <= 0 || this.videoUrl != null) && this.videoUrl.length() > 0) {
                    intent = new Intent(getApplicationContext(), LocalVideoFilePreview.class);
                    intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
                    intent.putExtra("local_file_url", this.videoUrl);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(getApplicationContext(), MomentSummaryActivity.class);
                intent.putExtra("imgList", this.imageList.toString().replace("[", BuildConfig.VERSION_NAME).replace("]", BuildConfig.VERSION_NAME));
                intent.putExtra("momentId", this.momentId);
                startActivity(intent);
            default:
        }
    }

    private void showOptionsDialog(String commentID, View v) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setCancelable(true);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, 17367043);
        System.out.println("showOptionsDialog3");
        arrayAdapter.add(getString(2131493021));
        builderSingle.setAdapter(arrayAdapter, new AnonymousClass14(commentID, v));
        builderSingle.show();
    }

    public void addSmiley(Entry<Pattern, Integer> entry) {
        this.textMessage.setText(Emoticons.getSmiledText(SHAMChatApplication.getMyApplicationContext(), this.textMessage.getText() + ((Pattern) entry.getKey()).toString().replace("\\Q", BuildConfig.VERSION_NAME).replace("\\E", BuildConfig.VERSION_NAME)), BufferType.SPANNABLE);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        if (this.fragmentContainer.getVisibility() == 0) {
            this.fragmentContainer.setVisibility(8);
        } else {
            finish();
        }
        return true;
    }

    public void closeActivity(View view) {
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.cursor != null) {
            this.cursor.close();
        }
    }
}

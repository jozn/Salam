package com.shamchat.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v7.appcompat.BuildConfig;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.shamchat.activity.ChatActivity;
import com.shamchat.activity.MyProfileActivity;
import com.shamchat.activity.ProfileActivity;
import com.shamchat.activity.ProgressBarDialogLogin;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.RosterProvider;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.androidclient.service.SmackableImp;
import com.shamchat.models.User;
import com.shamchat.utils.Utils;
import java.io.File;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;

public final class MomentLikesDetailAdapter extends BaseAdapter {
    private ContentResolver contentResolver;
    private Context context;
    private FragmentManager fragmentManager;
    private ImageLoader imageLoader;
    private LayoutInflater inflater;
    private Cursor likesCursor;
    private Bitmap myBitmap;
    private String myUserId;
    private String thumbnailPath;
    private DisplayImageOptions userImageOptions;

    /* renamed from: com.shamchat.adapters.MomentLikesDetailAdapter.1 */
    class C09801 implements OnClickListener {
        final /* synthetic */ String val$userId;

        C09801(String str) {
            this.val$userId = str;
        }

        public final void onClick(View v) {
            MomentLikesDetailAdapter.access$000(MomentLikesDetailAdapter.this, this.val$userId, v.getContext());
        }
    }

    /* renamed from: com.shamchat.adapters.MomentLikesDetailAdapter.2 */
    class C09812 implements OnClickListener {
        final /* synthetic */ String val$userId;

        C09812(String str) {
            this.val$userId = str;
        }

        public final void onClick(View v) {
            MomentLikesDetailAdapter.access$000(MomentLikesDetailAdapter.this, this.val$userId, v.getContext());
        }
    }

    /* renamed from: com.shamchat.adapters.MomentLikesDetailAdapter.3 */
    class C09823 implements OnClickListener {
        C09823() {
        }

        public final void onClick(View v) {
            MomentLikesDetailAdapter.access$100$b1fb3c9(v.getContext());
        }
    }

    /* renamed from: com.shamchat.adapters.MomentLikesDetailAdapter.4 */
    class C09834 implements OnClickListener {
        C09834() {
        }

        public final void onClick(View v) {
            MomentLikesDetailAdapter.access$100$b1fb3c9(v.getContext());
        }
    }

    /* renamed from: com.shamchat.adapters.MomentLikesDetailAdapter.5 */
    class C09845 implements Runnable {
        final /* synthetic */ String val$userId;

        C09845(String str) {
            this.val$userId = str;
        }

        public final void run() {
            UserProvider userProvider = new UserProvider();
            User user = UserProvider.getUserDetailsFromServer(this.val$userId);
            try {
                SmackableImp.tryToAddRosterEntry$14e1ec6d(Utils.createXmppUserIdByUserId(this.val$userId), user.username + MqttTopic.TOPIC_LEVEL_SEPARATOR + user.mobileNo);
                try {
                    ProgressBarDialogLogin.getInstance().dismiss();
                } catch (Exception e) {
                }
                Intent intent = new Intent(MomentLikesDetailAdapter.this.context, ProfileActivity.class);
                intent.putExtra(ProfileActivity.EXTRA_USER_ID, Utils.createXmppUserIdByUserId(this.val$userId));
                intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
                MomentLikesDetailAdapter.this.context.startActivity(intent);
            } catch (Exception e2) {
                e2.printStackTrace();
                ProgressBarDialogLogin.getInstance().dismiss();
            }
        }
    }

    public class ViewHolder {
        public ImageView appContact;
        public TextView name;
        public ImageView profileImageView;
        public TextView textComment;
    }

    static /* synthetic */ void access$000(MomentLikesDetailAdapter x0, String x1, Context x2) {
        String createXmppUserIdByUserId = Utils.createXmppUserIdByUserId(x1);
        if (x0.contentResolver.query(RosterProvider.CONTENT_URI, new String[]{"alias"}, "jid=?", new String[]{createXmppUserIdByUserId}, null).getCount() > 0) {
            Intent intent = new Intent(x2, ProfileActivity.class);
            intent.putExtra(ProfileActivity.EXTRA_USER_ID, createXmppUserIdByUserId);
            intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
            x2.startActivity(intent);
        } else if (Utils.isInternetAvailable(x0.context)) {
            ProgressBarDialogLogin.getInstance().show(x0.fragmentManager, BuildConfig.VERSION_NAME);
            new Thread(new C09845(x1)).start();
        }
    }

    static /* synthetic */ void access$100$b1fb3c9(Context x1) {
        Intent intent = new Intent(x1, MyProfileActivity.class);
        intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
        x1.startActivity(intent);
    }

    public final /* bridge */ /* synthetic */ Object getItem(int i) {
        return null;
    }

    public MomentLikesDetailAdapter(Context context, Cursor likesCursor, FragmentManager fragmentManager) {
        this.myBitmap = null;
        this.imageLoader = ImageLoader.getInstance();
        this.likesCursor = likesCursor;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        Builder builder = new Builder();
        builder.cacheInMemory = false;
        builder.cacheOnDisc = false;
        builder.imageScaleType$641b8ab2 = ImageScaleType.EXACTLY_STRETCHED$641b8ab2;
        builder.imageResOnLoading = 2130837944;
        builder.imageResOnFail = 2130837944;
        builder.imageResForEmptyUri = 2130837944;
        this.userImageOptions = builder.bitmapConfig(Config.ALPHA_8).build();
        this.contentResolver = context.getContentResolver();
        this.fragmentManager = fragmentManager;
        this.myUserId = SHAMChatApplication.getConfig().userId;
        this.thumbnailPath = new File(Environment.getExternalStorageDirectory() + "/salam/thumbnails").getAbsolutePath();
    }

    public final int getCount() {
        return this.likesCursor.getCount();
    }

    public final long getItemId(int position) {
        return 0;
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = this.inflater.inflate(2130903164, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(2131362166);
            holder.textComment = (TextView) convertView.findViewById(2131362252);
            holder.profileImageView = (ImageView) convertView.findViewById(2131362251);
            holder.appContact = (ImageView) convertView.findViewById(2131362296);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.appContact.setVisibility(4);
        this.likesCursor.moveToPosition(position);
        String userId = this.likesCursor.getString(this.likesCursor.getColumnIndex(ChatActivity.INTENT_EXTRA_USER_ID));
        Cursor cursor = this.contentResolver.query(UserProvider.CONTENT_URI_USER, new String[]{"name"}, "userId=?", new String[]{userId}, null);
        if (cursor != null && cursor.moveToFirst()) {
            holder.name.setText(cursor.getString(cursor.getColumnIndex("name")));
        }
        if (userId.equalsIgnoreCase(this.myUserId)) {
            holder.name.setOnClickListener(new C09823());
            holder.profileImageView.setOnClickListener(new C09834());
            if (this.myBitmap == null) {
                UserProvider userProvider = new UserProvider();
                Bitmap me = UserProvider.getMyProfileImage();
                if (me == null) {
                    me = BitmapFactory.decodeResource(this.context.getResources(), 2130837944);
                }
                this.myBitmap = me;
            }
            holder.profileImageView.setImageBitmap(this.myBitmap);
        } else {
            holder.name.setOnClickListener(new C09801(userId));
            holder.profileImageView.setOnClickListener(new C09812(userId));
            this.imageLoader.displayImage(null, holder.profileImageView, this.userImageOptions);
            this.imageLoader.displayImage("file://" + this.thumbnailPath + MqttTopic.TOPIC_LEVEL_SEPARATOR + userId + ".jpg", holder.profileImageView, this.userImageOptions);
        }
        return convertView;
    }
}

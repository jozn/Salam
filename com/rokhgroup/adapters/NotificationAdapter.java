package com.rokhgroup.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.rokhgroup.activities.JahanbinDetailsActivity;
import com.rokhgroup.activities.UserProfile;
import com.rokhgroup.adapters.item.NotificationItem;
import com.rokhgroup.utils.RokhPref;
import com.rokhgroup.utils.Utils;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.roundedimage.RoundedDrawable;
import com.shamchat.roundedimage.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;
import java.util.ArrayList;

public final class NotificationAdapter extends BaseAdapter {
    private static LayoutInflater mInflater;
    String CURRENT_USER_TOKEN;
    RokhPref Session;
    String URL;
    private Activity mActivity;
    private Context mContext;
    private ArrayList<NotificationItem> mData;
    private final Transformation mTransformation;

    /* renamed from: com.rokhgroup.adapters.NotificationAdapter.1 */
    class C06321 implements OnClickListener {
        final /* synthetic */ NotificationItem val$Item;

        C06321(NotificationItem notificationItem) {
            this.val$Item = notificationItem;
        }

        public final void onClick(View v) {
            Intent i = new Intent(NotificationAdapter.this.mContext, UserProfile.class);
            i.putExtra("USER_ID", this.val$Item.USER_ID);
            NotificationAdapter.this.mActivity.startActivity(i);
        }
    }

    /* renamed from: com.rokhgroup.adapters.NotificationAdapter.2 */
    class C06332 implements OnClickListener {
        final /* synthetic */ NotificationItem val$Item;

        C06332(NotificationItem notificationItem) {
            this.val$Item = notificationItem;
        }

        public final void onClick(View arg0) {
            Intent i = new Intent(NotificationAdapter.this.mContext, UserProfile.class);
            i.putExtra("USER_ID", this.val$Item.USER_ID);
            NotificationAdapter.this.mActivity.startActivity(i);
        }
    }

    /* renamed from: com.rokhgroup.adapters.NotificationAdapter.3 */
    class C06343 implements OnClickListener {
        final /* synthetic */ NotificationItem val$Item;

        C06343(NotificationItem notificationItem) {
            this.val$Item = notificationItem;
        }

        public final void onClick(View v) {
            Intent i = new Intent(NotificationAdapter.this.mContext, JahanbinDetailsActivity.class);
            i.putExtra("POST_ID", this.val$Item.POST_ID);
            i.putExtra("USER_ID", this.val$Item.USER_ID);
            NotificationAdapter.this.mActivity.startActivity(i);
        }
    }

    /* renamed from: com.rokhgroup.adapters.NotificationAdapter.4 */
    class C06354 implements OnClickListener {
        final /* synthetic */ NotificationItem val$Item;

        C06354(NotificationItem notificationItem) {
            this.val$Item = notificationItem;
        }

        public final void onClick(View v) {
            Intent i = new Intent(NotificationAdapter.this.mContext, JahanbinDetailsActivity.class);
            i.putExtra("POST_ID", this.val$Item.POST_ID);
            i.putExtra("USER_ID", this.val$Item.USER_ID);
            i.putExtra("POST_TYPE", this.val$Item.POST_TYPE);
            NotificationAdapter.this.mActivity.startActivity(i);
        }
    }

    /* renamed from: com.rokhgroup.adapters.NotificationAdapter.5 */
    class C06365 implements Transformation {
        final boolean oval;
        final float radius;

        C06365() {
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

    static class ViewHolder {
        TextView messageText;
        ImageView postImage;
        ImageView postTypeImage;
        RoundedImageView userAvatar;
        TextView username;

        public ViewHolder(View row) {
            this.userAvatar = (RoundedImageView) row.findViewById(2131362479);
            this.postImage = (ImageView) row.findViewById(2131362460);
            this.postTypeImage = (ImageView) row.findViewById(2131362482);
            this.username = (TextView) row.findViewById(2131362397);
            this.messageText = (TextView) row.findViewById(2131362481);
        }
    }

    static {
        mInflater = null;
    }

    public NotificationAdapter(Context context, Activity a, ArrayList<NotificationItem> data) {
        this.URL = "http://social.rabtcdn.com/pin/d_like2/?token=";
        this.mTransformation = new C06365();
        this.mContext = context;
        this.mActivity = a;
        this.mData = data;
        mInflater = (LayoutInflater) this.mActivity.getSystemService("layout_inflater");
        this.Session = new RokhPref(this.mContext);
        this.CURRENT_USER_TOKEN = this.Session.getUSERTOKEN();
    }

    public final int getCount() {
        return this.mData.size();
    }

    public final Object getItem(int position) {
        return this.mData.get(position);
    }

    public final long getItemId(int position) {
        return (long) position;
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;
        if (convertView == null) {
            row = mInflater.inflate(2130903215, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        NotificationItem Item = (NotificationItem) this.mData.get(position);
        holder.userAvatar.setTag(Integer.valueOf(position));
        RequestCreator load = Picasso.with(this.mContext).load(Item.USER_AVA);
        load.deferred = true;
        load.transform(this.mTransformation).into(holder.userAvatar, null);
        holder.userAvatar.setOnClickListener(new C06321(Item));
        holder.username.setTypeface(Utils.GetNaskhBold(this.mContext));
        holder.username.setTextSize(13.0f);
        holder.username.setText(Item.USER_NAME);
        holder.username.setOnClickListener(new C06332(Item));
        holder.messageText.setTypeface(Utils.GetNaskhRegular(this.mContext));
        holder.messageText.setTextSize(12.0f);
        int NotifType = Integer.valueOf(Item.NOTIF_TYPE).intValue();
        if (NotifType == 1) {
            if (Item.POST_TYPE.equals("3")) {
                holder.postTypeImage.setVisibility(0);
            } else {
                holder.postTypeImage.setVisibility(8);
            }
            holder.messageText.setText(2131493331);
            load = Picasso.with(this.mContext).load(Item.POST_IMAGE);
            load.deferred = true;
            load.into(holder.postImage, null);
            holder.postImage.setOnClickListener(new C06343(Item));
        } else if (NotifType == 2) {
            if (Item.POST_TYPE.equals("3")) {
                holder.postTypeImage.setVisibility(0);
            } else {
                holder.postTypeImage.setVisibility(8);
            }
            holder.messageText.setText(SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493339));
            load = Picasso.with(this.mContext).load(Item.POST_IMAGE);
            load.deferred = true;
            load.into(holder.postImage, null);
            holder.postImage.setOnClickListener(new C06354(Item));
        } else if (NotifType == 10) {
            holder.messageText.setText(SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493321));
            holder.postImage.setVisibility(8);
            holder.postTypeImage.setVisibility(8);
        }
        return row;
    }
}

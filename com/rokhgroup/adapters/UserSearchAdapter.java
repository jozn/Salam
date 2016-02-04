package com.rokhgroup.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.rokhgroup.adapters.item.UserSearchItem;
import com.rokhgroup.utils.RokhPref;
import com.rokhgroup.utils.Utils;
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

public final class UserSearchAdapter extends BaseAdapter {
    private static LayoutInflater mInflater;
    String CURRENT_USER_ID;
    String CURRENT_USER_TOKEN;
    RokhPref Session;
    String locale;
    private Activity mActivity;
    private Context mContext;
    private ArrayList<UserSearchItem> mData;
    private boolean mShowFollowButton;
    private final Transformation mTransformation;

    /* renamed from: com.rokhgroup.adapters.UserSearchAdapter.1 */
    class C06561 implements OnClickListener {
        final /* synthetic */ UserSearchItem val$Item;

        /* renamed from: com.rokhgroup.adapters.UserSearchAdapter.1.1 */
        class C06551 implements Callback {
            final /* synthetic */ int val$position;

            C06551(int i) {
                this.val$position = i;
            }

            public final void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            public final void onResponse(Response response) throws IOException {
                String stringResponse = response.body().string();
                response.body().close();
                try {
                    if (response.isSuccessful()) {
                        Log.e("FOLLOW RESULT", stringResponse);
                        if (!stringResponse.equals("1")) {
                            return;
                        }
                        if (C06561.this.val$Item.FOLLOW_W_USER) {
                            C06561.this.val$Item.FOLLOW_W_USER = false;
                            ((UserSearchItem) UserSearchAdapter.this.mData.get(this.val$position)).FOLLOW_W_USER = false;
                            UserSearchAdapter.this.notifyDataSetChanged();
                            return;
                        }
                        C06561.this.val$Item.FOLLOW_W_USER = true;
                        ((UserSearchItem) UserSearchAdapter.this.mData.get(this.val$position)).FOLLOW_W_USER = true;
                        UserSearchAdapter.this.notifyDataSetChanged();
                        return;
                    }
                    throw new IOException("Unexpected code " + response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        C06561(UserSearchItem userSearchItem) {
            this.val$Item = userSearchItem;
        }

        public final void onClick(View v) {
            String URL;
            int position = ((Integer) v.getTag()).intValue();
            if (((UserSearchItem) UserSearchAdapter.this.mData.get(position)).FOLLOW_W_USER) {
                URL = "http://social.rabtcdn.com/pin/d/follow/" + this.val$Item.USER_ID + "/0/?token=" + UserSearchAdapter.this.CURRENT_USER_TOKEN;
            } else {
                URL = "http://social.rabtcdn.com/pin/d/follow/" + this.val$Item.USER_ID + "/1/?token=" + UserSearchAdapter.this.CURRENT_USER_TOKEN;
            }
            Log.e("FOLLOW URL", URL);
            new OkHttpClient().newCall(new Builder().url(URL).build()).enqueue(new C06551(position));
        }
    }

    /* renamed from: com.rokhgroup.adapters.UserSearchAdapter.2 */
    class C06572 implements Transformation {
        final boolean oval;
        final float radius;

        C06572() {
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
        RoundedImageView UserAvatar;
        ImageView UserFollow;
        TextView UserName;

        public ViewHolder(View row) {
            this.UserAvatar = (RoundedImageView) row.findViewById(2131362479);
            this.UserName = (TextView) row.findViewById(2131362397);
            this.UserFollow = (ImageView) row.findViewById(2131362510);
        }
    }

    static {
        mInflater = null;
    }

    public UserSearchAdapter(Context context, Activity a, ArrayList<UserSearchItem> data, boolean showfollowbtn) {
        this.mTransformation = new C06572();
        this.mContext = context;
        this.mActivity = a;
        this.mData = data;
        this.mShowFollowButton = showfollowbtn;
        mInflater = (LayoutInflater) this.mActivity.getSystemService("layout_inflater");
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
        this.Session = new RokhPref(this.mContext);
        this.CURRENT_USER_TOKEN = this.Session.getUSERTOKEN();
        this.CURRENT_USER_ID = this.Session.getUSERID();
        this.locale = Locale.getDefault().toString();
        View row = convertView;
        if (convertView == null) {
            row = mInflater.inflate(2130903223, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        UserSearchItem Item = (UserSearchItem) this.mData.get(position);
        holder.UserAvatar.setTag(Integer.valueOf(position));
        holder.UserFollow.setTag(Integer.valueOf(position));
        RequestCreator load = Picasso.with(this.mContext).load(Item.USER_AVATAR);
        load.deferred = true;
        load.transform(this.mTransformation).into(holder.UserAvatar, null);
        holder.UserName.setTypeface(Utils.GetNaskhBold(this.mContext));
        holder.UserName.setTextSize(13.0f);
        holder.UserName.setText(Item.USER_NAME);
        if (!this.mShowFollowButton || Item.USER_ID.equals(this.CURRENT_USER_ID)) {
            holder.UserFollow.setVisibility(8);
        } else {
            holder.UserFollow.setVisibility(0);
        }
        Log.e("FOLLOW STATE", String.valueOf(Item.FOLLOW_W_USER));
        if (Item.FOLLOW_W_USER) {
            if (this.locale.equals("fa_IR")) {
                holder.UserFollow.setBackgroundResource(2130837870);
            } else {
                holder.UserFollow.setBackgroundResource(2130837869);
            }
        } else if (this.locale.equals("fa_IR")) {
            holder.UserFollow.setBackgroundResource(2130837868);
        } else {
            holder.UserFollow.setBackgroundResource(2130837867);
        }
        holder.UserFollow.setOnClickListener(new C06561(Item));
        return row;
    }
}

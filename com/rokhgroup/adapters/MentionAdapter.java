package com.rokhgroup.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.rokhgroup.adapters.UserMentionAsyncTask.User;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.roundedimage.RoundedDrawable;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;
import java.util.ArrayList;

public final class MentionAdapter extends ArrayAdapter<User> {
    Context mContext;
    private final Transformation mTransformation;

    /* renamed from: com.rokhgroup.adapters.MentionAdapter.1 */
    class C06311 implements Transformation {
        final boolean oval;
        final float radius;

        C06311() {
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
        ImageView Avatar;
        TextView profileName;
        TextView userName;

        ViewHolder() {
        }
    }

    public MentionAdapter(Context context, ArrayList<User> users) {
        super(context, 2130903222, users);
        this.mTransformation = new C06311();
        this.mContext = context;
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        User user = (User) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(2130903222, parent, false);
            holder = new ViewHolder();
            holder.profileName = (TextView) convertView.findViewById(2131362508);
            holder.userName = (TextView) convertView.findViewById(2131362509);
            holder.Avatar = (ImageView) convertView.findViewById(2131362507);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.profileName.setText(user.username);
        holder.userName.setText(user.profilename);
        RequestCreator load = Picasso.with(this.mContext).load(user.avatar);
        load.deferred = true;
        load.transform(this.mTransformation).into(holder.Avatar, null);
        return convertView;
    }
}

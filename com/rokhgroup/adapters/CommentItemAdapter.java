package com.rokhgroup.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.rokhgroup.activities.UserProfile;
import com.rokhgroup.adapters.item.CommentItem;
import com.rokhgroup.utils.URLSpanNoUnderLine;
import com.rokhgroup.utils.Utils;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.roundedimage.RoundedDrawable;
import com.shamchat.roundedimage.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;

public final class CommentItemAdapter extends BaseAdapter {
    private Activity mActivity;
    private Context mContext;
    private ArrayList<CommentItem> mData;
    private LayoutInflater mInflater;
    private final Transformation mTransformation;

    /* renamed from: com.rokhgroup.adapters.CommentItemAdapter.1 */
    class C06291 implements OnClickListener {
        final /* synthetic */ String val$USER_ID;

        C06291(String str) {
            this.val$USER_ID = str;
        }

        public final void onClick(View arg0) {
            Intent i = new Intent(CommentItemAdapter.this.mActivity, UserProfile.class);
            i.putExtra("USER_ID", this.val$USER_ID);
            CommentItemAdapter.this.mActivity.startActivity(i);
        }
    }

    /* renamed from: com.rokhgroup.adapters.CommentItemAdapter.2 */
    class C06302 implements Transformation {
        final boolean oval;
        final float radius;

        C06302() {
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
        TextView cmtBody;
        TextView cmtDate;
        RoundedImageView userAva;
        TextView userName;

        public ViewHolder(View row) {
            this.userAva = (RoundedImageView) row.findViewById(2131362433);
            this.userName = (TextView) row.findViewById(2131362397);
            this.cmtDate = (TextView) row.findViewById(2131362434);
            this.cmtBody = (TextView) row.findViewById(2131362435);
        }
    }

    public CommentItemAdapter(Context context, Activity a, ArrayList<CommentItem> data) {
        this.mInflater = null;
        this.mTransformation = new C06302();
        this.mActivity = a;
        this.mData = data;
        this.mContext = context;
        this.mInflater = (LayoutInflater) this.mActivity.getSystemService("layout_inflater");
        this.mContext = context;
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
            row = this.mInflater.inflate(2130903209, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        CommentItem Item = (CommentItem) this.mData.get(position);
        String USER_ID = Item.ID;
        System.out.println(USER_ID);
        holder.userAva.setTag(Integer.valueOf(position));
        holder.userAva.setOnClickListener(new C06291(USER_ID));
        RequestCreator load = Picasso.with(this.mContext).load(Item.USER_AVA);
        load.deferred = true;
        load.centerInside().into(holder.userAva, null);
        holder.userName.setTypeface(Utils.GetNaskhRegular(this.mContext));
        holder.userName.setTextSize(11.0f);
        holder.userName.setText(Item.USER_NAME);
        Long time = null;
        try {
            time = Long.valueOf(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(Item.DATE).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.cmtDate.setTypeface(Utils.GetNaskhRegular(this.mContext));
        holder.cmtDate.setTextSize(9.0f);
        holder.cmtDate.setText(Utils.persianNum(Utils.getTimeAgo$6909e107(time.longValue())));
        holder.cmtBody.setTypeface(Utils.GetNaskhRegular(this.mContext));
        holder.cmtBody.setTextSize(11.0f);
        holder.cmtBody.setText(Item.BODY);
        Linkify.addLinks(holder.cmtBody, Pattern.compile("[#]+[\\w]+\\b", 64), "content://com.rokhgroup.activities.hashtagactivity/");
        Linkify.addLinks(holder.cmtBody, Pattern.compile("[@]+[\\w]+\\b", 64), "content://com.rokhgroup.activities.userprofileactivity/");
        Linkify.addLinks(holder.cmtBody, Patterns.WEB_URL, null, null, null);
        stripUnderlines(holder.cmtBody);
        return row;
    }

    private static void stripUnderlines(TextView textView) {
        Spannable s = new SpannableString(textView.getText());
        for (URLSpan span : (URLSpan[]) s.getSpans(0, s.length(), URLSpan.class)) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            s.setSpan(new URLSpanNoUnderLine(span.getURL()), start, end, 0);
        }
        textView.setText(s);
    }
}

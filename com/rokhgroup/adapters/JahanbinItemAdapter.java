package com.rokhgroup.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.rokhgroup.adapters.item.JahanbinItem;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import java.util.ArrayList;

public final class JahanbinItemAdapter extends BaseAdapter {
    private static LayoutInflater mInflater;
    private Activity mActivity;
    private Context mContext;
    public ArrayList<JahanbinItem> mData;
    private int mWidth;

    static class ViewHolder {
        ImageView imageView;
        ImageView videoPost;

        public ViewHolder(View row) {
            this.imageView = (ImageView) row.findViewById(2131362436);
            this.videoPost = (ImageView) row.findViewById(2131362437);
        }
    }

    static {
        mInflater = null;
    }

    public JahanbinItemAdapter(Context context, Activity a, ArrayList<JahanbinItem> data, int width) {
        this.mActivity = a;
        this.mData = data;
        mInflater = (LayoutInflater) this.mActivity.getSystemService("layout_inflater");
        this.mContext = context;
        this.mWidth = width;
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
            row = mInflater.inflate(2130903210, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        JahanbinItem Item = (JahanbinItem) this.mData.get(position);
        holder.imageView.setTag(Integer.valueOf(position));
        holder.imageView.getLayoutParams().width = this.mWidth;
        holder.imageView.getLayoutParams().height = this.mWidth;
        if (Item.Post_TYPE.equals("3")) {
            holder.videoPost.setVisibility(0);
        } else {
            holder.videoPost.setVisibility(8);
        }
        RequestCreator load = Picasso.with(this.mContext).load(Item.IMAGE_THUMB_URL);
        load.deferred = true;
        load.into(holder.imageView, null);
        return row;
    }
}

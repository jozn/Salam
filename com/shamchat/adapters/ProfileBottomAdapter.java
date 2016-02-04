package com.shamchat.adapters;

import android.content.Context;
import android.support.v7.appcompat.C0170R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.shamchat.androidclient.util.FontUtil;
import java.util.ArrayList;
import java.util.List;

public final class ProfileBottomAdapter extends BaseAdapter {
    private Context context;
    public List<BottomListItem> list;

    public static class BottomListItem {
        public int iconRes;
        public String text;

        public BottomListItem(String text, int iconRes) {
            this.text = text;
            this.iconRes = iconRes;
        }
    }

    private static class ViewHolder {
        public ImageView iconImageView;
        public TextView textView;

        private ViewHolder() {
        }
    }

    public ProfileBottomAdapter(Context context) {
        this.list = new ArrayList();
        this.context = context;
    }

    public final int getCount() {
        return this.list.size();
    }

    private BottomListItem getItem(int position) {
        return (BottomListItem) this.list.get(position);
    }

    public final long getItemId(int position) {
        return (long) position;
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(2130903107, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.textView = (TextView) convertView.findViewById(C0170R.id.text);
            FontUtil.applyFont(holder.textView);
            holder.iconImageView = (ImageView) convertView.findViewById(2131362095);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BottomListItem item = getItem(position);
        holder.textView.setText(item.text);
        holder.iconImageView.setImageResource(item.iconRes);
        return convertView;
    }
}

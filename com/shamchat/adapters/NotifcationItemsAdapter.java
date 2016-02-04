package com.shamchat.adapters;

import android.content.Context;
import android.support.v7.appcompat.C0170R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.shamchat.androidclient.util.FontUtil;
import java.util.ArrayList;
import java.util.List;

public final class NotifcationItemsAdapter extends BaseAdapter {
    private Context context;
    public List<NotificationListItem> list;

    public static class NotificationListItem {
        public String text;

        public NotificationListItem(String text) {
            this.text = text;
        }
    }

    private static class ViewHolder {
        public TextView textView;

        private ViewHolder() {
        }
    }

    public NotifcationItemsAdapter(Context context) {
        this.list = new ArrayList();
        this.context = context;
    }

    public final int getCount() {
        return this.list.size();
    }

    private NotificationListItem getItem(int position) {
        return (NotificationListItem) this.list.get(position);
    }

    public final long getItemId(int position) {
        return (long) position;
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(2130903170, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.textView = (TextView) convertView.findViewById(C0170R.id.text);
            FontUtil.applyFont(holder.textView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(getItem(position).text);
        return convertView;
    }
}

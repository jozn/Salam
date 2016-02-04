package com.shamchat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.shamchat.androidclient.util.FontUtil;
import java.util.ArrayList;
import java.util.List;

public final class MyAccountItemsAdapter extends BaseAdapter {
    private Context context;
    public List<MyAccountListItem> list;

    public static class MyAccountListItem {
        public String textTopic;
        public String textValue;

        public MyAccountListItem(String textTopic, String textValue) {
            this.textValue = textValue;
            this.textTopic = textTopic;
        }
    }

    private static class ViewHolder {
        public TextView textTopic;
        public TextView textValue;

        private ViewHolder() {
        }
    }

    public MyAccountItemsAdapter(Context context) {
        this.list = new ArrayList();
        this.context = context;
    }

    public final int getCount() {
        return this.list.size();
    }

    private MyAccountListItem getItem(int position) {
        return (MyAccountListItem) this.list.get(position);
    }

    public final long getItemId(int position) {
        return (long) position;
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(2130903169, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.textTopic = (TextView) convertView.findViewById(2131362319);
            holder.textValue = (TextView) convertView.findViewById(2131362320);
            FontUtil.applyFont(holder.textTopic);
            FontUtil.applyFont(holder.textValue);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyAccountListItem item = getItem(position);
        holder.textTopic.setText(item.textTopic);
        holder.textValue.setText(item.textValue);
        return convertView;
    }
}

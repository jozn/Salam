package com.shamchat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shamchat.androidclient.util.FontUtil;
import java.util.ArrayList;
import java.util.List;

public final class ProfileDetailsAdapter extends BaseAdapter {
    private Context context;
    public List<DetailsListItem> list;

    public static class DetailsListItem {
        public String leftText;
        public String rightText;

        public DetailsListItem(String left, String right) {
            this.leftText = left;
            this.rightText = right;
        }
    }

    private static class ViewHolder {
        public LinearLayout divider;
        public TextView leftTextView;
        public TextView rightTextView;

        private ViewHolder() {
        }
    }

    public ProfileDetailsAdapter(Context context) {
        this.list = new ArrayList();
        this.context = context;
    }

    public final int getCount() {
        return this.list.size();
    }

    private DetailsListItem getItem(int position) {
        return (DetailsListItem) this.list.get(position);
    }

    public final long getItemId(int position) {
        return (long) position;
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(2130903108, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.leftTextView = (TextView) convertView.findViewById(2131362097);
            holder.rightTextView = (TextView) convertView.findViewById(2131362098);
            holder.divider = (LinearLayout) convertView.findViewById(2131362099);
            FontUtil.applyFont(holder.leftTextView);
            FontUtil.applyFont(holder.rightTextView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DetailsListItem item = getItem(position);
        if (item.leftText == this.context.getResources().getString(2131493153)) {
            holder.divider.setVisibility(4);
        }
        holder.leftTextView.setText(item.leftText);
        holder.rightTextView.setText(item.rightText);
        return convertView;
    }
}

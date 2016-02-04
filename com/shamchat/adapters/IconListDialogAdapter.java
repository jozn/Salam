package com.shamchat.adapters;

import android.content.Context;
import android.support.v7.appcompat.C0170R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public final class IconListDialogAdapter extends BaseAdapter {
    private Context context;
    public List<ListRow> list;

    public static class ListRow {
        public int resIcon;
        public int resTitle;

        public ListRow(int icon, int text) {
            this.resIcon = icon;
            this.resTitle = text;
        }
    }

    public IconListDialogAdapter(Context context) {
        this.list = new ArrayList();
        this.context = context;
    }

    public final int getCount() {
        return this.list.size();
    }

    private ListRow getItem(int position) {
        return (ListRow) this.list.get(position);
    }

    public final long getItemId(int position) {
        return (long) position;
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(2130903106, null);
        }
        ImageView icon = (ImageView) convertView.findViewById(C0170R.id.icon);
        TextView title = (TextView) convertView.findViewById(C0170R.id.text);
        ListRow item = getItem(position);
        if (item.resIcon == 0) {
            icon.setImageDrawable(null);
        } else {
            icon.setImageResource(item.resIcon);
        }
        title.setText(item.resTitle);
        return convertView;
    }
}

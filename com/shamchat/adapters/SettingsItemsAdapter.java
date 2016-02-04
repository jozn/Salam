package com.shamchat.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.appcompat.C0170R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.kyleduo.switchbutton.SwitchButton;
import com.shamchat.androidclient.util.FontUtil;
import java.util.ArrayList;
import java.util.List;

public final class SettingsItemsAdapter extends BaseAdapter {
    private Context context;
    public List<SettingListItem> list;

    /* renamed from: com.shamchat.adapters.SettingsItemsAdapter.1 */
    class C10061 implements OnCheckedChangeListener {
        final /* synthetic */ ViewHolder val$holder;

        C10061(ViewHolder viewHolder) {
            this.val$holder = viewHolder;
        }

        public final void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (this.val$holder.sbMd.isChecked()) {
                this.val$holder.wifi.setTextColor(Color.parseColor("#00817c"));
                this.val$holder.data.setTextColor(Color.parseColor("#000000"));
                return;
            }
            this.val$holder.data.setTextColor(Color.parseColor("#00817c"));
            this.val$holder.wifi.setTextColor(Color.parseColor("#000000"));
        }
    }

    public static class SettingListItem {
        public String datatxt;
        public int iconRes;
        public String text;
        public Boolean vis;
        public String wifitxt;

        public SettingListItem(int iconRes, String text, Boolean vis, String wifitxt, String datatxt) {
            this.iconRes = iconRes;
            this.text = text;
            this.wifitxt = wifitxt;
            this.datatxt = datatxt;
            if (vis.booleanValue()) {
                this.vis = Boolean.valueOf(true);
            } else {
                this.vis = Boolean.valueOf(false);
            }
        }
    }

    private static class ViewHolder {
        public TextView data;
        public ImageView iconImageView;
        public LinearLayout lin;
        public LinearLayout line;
        public SwitchButton sbMd;
        public TextView textView;
        public TextView wifi;

        private ViewHolder() {
        }
    }

    public SettingsItemsAdapter(Context context) {
        this.list = new ArrayList();
        this.context = context;
    }

    public final int getCount() {
        return this.list.size();
    }

    private SettingListItem getItem(int position) {
        return (SettingListItem) this.list.get(position);
    }

    public final long getItemId(int position) {
        return (long) position;
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(2130903173, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.iconImageView = (ImageView) convertView.findViewById(2131362095);
            holder.textView = (TextView) convertView.findViewById(C0170R.id.text);
            holder.lin = (LinearLayout) convertView.findViewById(2131362332);
            holder.line = (LinearLayout) convertView.findViewById(2131362331);
            holder.sbMd = (SwitchButton) convertView.findViewById(2131362334);
            holder.data = (TextView) convertView.findViewById(2131362333);
            holder.wifi = (TextView) convertView.findViewById(2131362335);
            FontUtil.applyFont(holder.textView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SettingListItem item = getItem(position);
        holder.iconImageView.setImageResource(item.iconRes);
        if (item.vis.booleanValue()) {
            holder.lin.setVisibility(0);
        } else {
            holder.lin.setVisibility(8);
        }
        holder.wifi.setText(item.wifitxt);
        holder.data.setText(item.datatxt);
        holder.textView.setText(item.text);
        if (holder.sbMd.isChecked()) {
            holder.wifi.setTextColor(Color.parseColor("#00817c"));
            holder.data.setTextColor(Color.parseColor("#000000"));
        } else {
            holder.data.setTextColor(Color.parseColor("#00817c"));
            holder.wifi.setTextColor(Color.parseColor("#000000"));
        }
        holder.sbMd.setOnCheckedChangeListener(new C10061(holder));
        return convertView;
    }
}

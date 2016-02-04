package com.shamchat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.shamchat.activity.ChatActivity;
import com.shamchat.utils.Emoticons;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public final class ChatSmileyAdapter extends BaseAdapter {
    private ChatActivity chatActivity;
    private LayoutInflater inflater;
    private List<Entry<Pattern, Integer>> listEmoticons;

    /* renamed from: com.shamchat.adapters.ChatSmileyAdapter.1 */
    class C09421 implements OnClickListener {
        final /* synthetic */ Entry val$entry;

        C09421(Entry entry) {
            this.val$entry = entry;
        }

        public final void onClick(View v) {
            ChatSmileyAdapter.this.chatActivity.addSmiley(this.val$entry);
        }
    }

    class ViewHolder {
        public ImageView imgSmiley;

        ViewHolder() {
        }
    }

    public ChatSmileyAdapter(Context context, ChatActivity chatActivity) {
        this.listEmoticons = new ArrayList();
        addEntriesToList();
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.chatActivity = chatActivity;
    }

    private void addEntriesToList() {
        for (Entry<Pattern, Integer> entry : Emoticons.ANDROID_EMOTICONS.entrySet()) {
            this.listEmoticons.add(entry);
        }
    }

    public final int getCount() {
        return Emoticons.ANDROID_EMOTICONS.size();
    }

    private Entry<Pattern, Integer> getItem(int position) {
        return (Entry) this.listEmoticons.get(position);
    }

    public final long getItemId(int position) {
        return 0;
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = this.inflater.inflate(2130903159, null);
            holder = new ViewHolder();
            holder.imgSmiley = (ImageView) convertView.findViewById(2131362279);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Entry<Pattern, Integer> entry = getItem(position);
        holder.imgSmiley.setImageResource(((Integer) entry.getValue()).intValue());
        holder.imgSmiley.setOnClickListener(new C09421(entry));
        return convertView;
    }
}

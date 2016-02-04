package com.shamchat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.shamchat.activity.ChatInitialForGroupChatActivity;
import com.shamchat.utils.Emoticons;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public final class ChatSmileyAdapterGroup extends BaseAdapter {
    private ChatInitialForGroupChatActivity chatInitialActivity;
    private LayoutInflater inflater;
    private List<Entry<Pattern, Integer>> listEmoticons;

    /* renamed from: com.shamchat.adapters.ChatSmileyAdapterGroup.1 */
    class C09431 implements OnClickListener {
        final /* synthetic */ Entry val$entry;

        C09431(Entry entry) {
            this.val$entry = entry;
        }

        public final void onClick(View v) {
            ChatSmileyAdapterGroup.this.chatInitialActivity.addSmiley(this.val$entry);
        }
    }

    class ViewHolder {
        public ImageView imgSmiley;

        ViewHolder() {
        }
    }

    public ChatSmileyAdapterGroup(Context context, ChatInitialForGroupChatActivity chatInitialActivity) {
        this.listEmoticons = new ArrayList();
        addEntriesToList();
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.chatInitialActivity = chatInitialActivity;
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
        holder.imgSmiley.setOnClickListener(new C09431(entry));
        return convertView;
    }
}

package com.shamchat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.shamchat.activity.dateconvert;
import com.shamchat.models.ChatMessage;
import java.text.SimpleDateFormat;
import java.util.Locale;

public final class ChatDateHeader implements Row {
    ChatMessage chatMessage;
    LayoutInflater layoutInflater;
    String locale;

    private class ViewHolder {
        TextView tvChatDateHeader;

        public ViewHolder(TextView tvChatDateHeader) {
            this.tvChatDateHeader = tvChatDateHeader;
        }
    }

    public ChatDateHeader(LayoutInflater layoutInflater, ChatMessage chatMessage) {
        this.layoutInflater = layoutInflater;
        this.chatMessage = chatMessage;
    }

    public final View getView(View convertView) {
        ViewHolder holder;
        View view;
        this.locale = Locale.getDefault().toString();
        if (convertView == null) {
            View viewGroup = (ViewGroup) this.layoutInflater.inflate(2130903112, null);
            holder = new ViewHolder((TextView) viewGroup.findViewById(2131362117));
            viewGroup.setTag(holder);
            view = viewGroup;
        } else {
            holder = (ViewHolder) convertView.getTag();
            view = convertView;
        }
        String sDate;
        dateconvert com_shamchat_activity_dateconvert;
        Locale locale;
        try {
            sDate = new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this.chatMessage.messageDateTime.toString()));
            if (this.locale.equals("fa_IR")) {
                try {
                    com_shamchat_activity_dateconvert = new dateconvert();
                    locale = new Locale("en_US");
                    holder.tvChatDateHeader.setText(dateconvert.getCurrentShamsidate(sDate));
                } catch (Exception e) {
                    holder.tvChatDateHeader.setText(sDate);
                }
                return view;
            }
            holder.tvChatDateHeader.setText(sDate);
            return view;
        } catch (Exception e2) {
            try {
                sDate = new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this.chatMessage.messageDateTime.toString()));
                if (this.locale.equals("fa_IR")) {
                    try {
                        com_shamchat_activity_dateconvert = new dateconvert();
                        locale = new Locale("en_US");
                        holder.tvChatDateHeader.setText(dateconvert.getCurrentShamsidate(sDate));
                    } catch (Exception e3) {
                        holder.tvChatDateHeader.setText(sDate);
                    }
                } else {
                    holder.tvChatDateHeader.setText(sDate);
                }
            } catch (Exception e4) {
            }
        }
    }

    public final int getViewType() {
        return MyMessageType.HEADER_MSG.ordinal();
    }

    public final ChatMessage getChatMessageObject() {
        return this.chatMessage;
    }
}

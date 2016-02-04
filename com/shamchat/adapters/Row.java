package com.shamchat.adapters;

import android.view.View;
import com.shamchat.models.ChatMessage;

public interface Row {
    ChatMessage getChatMessageObject();

    View getView(View view);

    int getViewType();
}

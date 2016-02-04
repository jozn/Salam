package com.shamchat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.models.Message;

public class ImagePreviewActivity extends Activity {
    private ImageView imgPreview;
    private Message message;

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        getWindow().addFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        super.onCreate(savedInstanceState);
        setContentView(2130903087);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(MessageDetailsActivity.EXTRA_MESSAGE_ID)) {
            String messageId = extras.getString(MessageDetailsActivity.EXTRA_MESSAGE_ID);
            ChatProviderNew chatProviderNew = new ChatProviderNew();
            this.message = ChatProviderNew.getFavorite(messageId);
            this.imgPreview = (ImageView) findViewById(2131362018);
            System.out.println(this.message.messageContent);
            this.imgPreview.setScaleType(ScaleType.CENTER);
            Message.loadAsyncImageContent$78bd0eef();
        }
    }
}

package com.shamchat.moments;

import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.media.TransportMediator;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.shamchat.androidclient.SHAMChatApplication;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public class MomentStickerFragment extends Fragment {
    public static PopupWindow stickersWindow;
    private int STICKER_CODE;
    private MomentComposerActivity baseActivity;
    private HorizontalScrollView horizontalScrollView;
    private ImageLoader imageLoader;
    private DisplayImageOptions mediaImageOptions;
    private LinearLayout multipleImageContainer;
    private Button stickerButton;

    /* renamed from: com.shamchat.moments.MomentStickerFragment.1 */
    class C11371 implements OnClickListener {
        C11371() {
        }

        public final void onClick(View v) {
            MomentStickerFragment.this.startActivityForResult(new Intent(MomentStickerFragment.this.getActivity(), MomentStickerBrowserActivity.class), MomentStickerFragment.this.STICKER_CODE);
        }
    }

    /* renamed from: com.shamchat.moments.MomentStickerFragment.2 */
    class C11382 implements Runnable {
        final /* synthetic */ boolean val$isDrawable;
        final /* synthetic */ String val$localUrl;

        C11382(String str, boolean z) {
            this.val$localUrl = str;
            this.val$isDrawable = z;
        }

        public final void run() {
            String tempPath = this.val$localUrl;
            if (this.val$isDrawable) {
                File f = new File(new File(Environment.getExternalStorageDirectory() + "/salam/thumbnails").getAbsolutePath() + MqttTopic.TOPIC_LEVEL_SEPARATOR + System.currentTimeMillis() + ".jpg");
                try {
                    InputStream is = MomentStickerFragment.this.getResources().openRawResource(Integer.valueOf(this.val$localUrl).intValue());
                    OutputStream out = new FileOutputStream(f);
                    byte[] buf = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
                    while (true) {
                        int len = is.read(buf);
                        if (len <= 0) {
                            break;
                        }
                        out.write(buf, 0, len);
                    }
                    out.close();
                    is.close();
                    tempPath = f.getAbsolutePath();
                } catch (IOException e) {
                    return;
                }
            }
            MomentStickerFragment.this.displaySticker(tempPath);
            MomentStickerFragment.this.baseActivity.getStickerFileUrls().add(tempPath);
            MomentStickerFragment.this.baseActivity.btSmiley.setImageResource(2130838073);
        }
    }

    /* renamed from: com.shamchat.moments.MomentStickerFragment.3 */
    class C11393 implements Runnable {
        C11393() {
        }

        public final void run() {
            MomentStickerFragment.this.horizontalScrollView.fullScroll(66);
        }
    }

    public MomentStickerFragment() {
        this.STICKER_CODE = 100;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(2130903186, container, false);
        this.stickerButton = (Button) view.findViewById(2131362381);
        this.multipleImageContainer = (LinearLayout) view.findViewById(2131362302);
        this.imageLoader = ImageLoader.getInstance();
        this.horizontalScrollView = (HorizontalScrollView) view.findViewById(2131362301);
        this.stickerButton.setOnClickListener(new C11371());
        this.baseActivity = (MomentComposerActivity) getActivity();
        Builder builder = new Builder();
        builder.imageResOnLoading = 2130838116;
        builder.imageResForEmptyUri = 2130837992;
        builder.imageResOnFail = 2130837992;
        builder.cacheInMemory = true;
        builder.cacheOnDisc = false;
        builder.imageScaleType$641b8ab2 = ImageScaleType.EXACTLY_STRETCHED$641b8ab2;
        this.mediaImageOptions = builder.bitmapConfig(Config.ALPHA_8).build();
        List<String> tempList = this.baseActivity.getStickerFileUrls();
        if (tempList.size() > 0) {
            for (String url : tempList) {
                displaySticker(url);
            }
        }
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.STICKER_CODE && resultCode == -1) {
            String url = data.getStringExtra("stickerUrl");
            if (url.contains(".")) {
                addSticker(url, false);
            } else {
                addSticker(url, true);
            }
        }
    }

    public void addSticker(String localUrl, boolean isDrawable) {
        SHAMChatApplication.getInstance().runOnUiThread(new C11382(localUrl, isDrawable));
    }

    private void displaySticker(String localUrl) {
        ImageView rowImageView = new ImageView(SHAMChatApplication.getMyApplicationContext());
        this.imageLoader.displayImage("file://" + localUrl, rowImageView, this.mediaImageOptions);
        LayoutParams params = new LayoutParams(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD);
        params.setMargins(5, 5, 5, 5);
        params.gravity = 16;
        rowImageView.setLayoutParams(params);
        this.multipleImageContainer.addView(rowImageView);
        this.horizontalScrollView.post(new C11393());
    }
}

package com.shamchat.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.media.TransportMediator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.shamchat.androidclient.ChatController;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import com.shamchat.events.SendStickerToGroupEvent;
import com.shamchat.moments.MomentStickerBrowserActivity;
import com.shamchat.utils.Utils;
import com.squareup.picasso.Picasso;
import de.greenrobot.event.EventBus;
import java.util.List;

public final class StickerAdapter extends BaseAdapter {
    private ChatController chatController;
    private Context context;
    private List<String> downloadUrlList;
    private LayoutInflater inflater;
    private boolean isGroupChat;
    private boolean isMoment;
    private MomentStickerBrowserActivity momentStickerFragment;
    private String reciepientId;

    /* renamed from: com.shamchat.adapters.StickerAdapter.1 */
    class C10111 implements OnClickListener {
        final /* synthetic */ int val$position;

        /* renamed from: com.shamchat.adapters.StickerAdapter.1.1 */
        class C10101 implements Runnable {
            C10101() {
            }

            public final void run() {
                if (StickerAdapter.this.isMoment) {
                    StickerAdapter.this.momentStickerFragment.addSticker((String) StickerAdapter.this.downloadUrlList.get(C10111.this.val$position));
                    return;
                }
                System.out.println("sticker selected " + ((String) StickerAdapter.this.downloadUrlList.get(C10111.this.val$position)));
                if (StickerAdapter.this.isGroupChat) {
                    EventBus.getDefault().postSticky(new SendStickerToGroupEvent(StickerAdapter.this.reciepientId, (String) StickerAdapter.this.downloadUrlList.get(C10111.this.val$position)));
                    return;
                }
                StickerAdapter.this.chatController.sendMessage(Utils.createXmppUserIdByUserId(StickerAdapter.this.reciepientId), (String) StickerAdapter.this.downloadUrlList.get(C10111.this.val$position), MessageContentType.STICKER.toString(), StickerAdapter.this.isGroupChat, null, null, null, null);
            }
        }

        C10111(int i) {
            this.val$position = i;
        }

        public final void onClick(View v) {
            new Thread(new C10101()).start();
        }
    }

    class ViewHolder {
        public ImageView imgSmiley;

        ViewHolder() {
        }
    }

    public final /* bridge */ /* synthetic */ Object getItem(int i) {
        return (String) this.downloadUrlList.get(i);
    }

    public StickerAdapter(FragmentActivity activity, List<String> downloadUrlList, String reciepientId, boolean isGroupChat) {
        this.isMoment = false;
        this.context = activity;
        this.inflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
        this.downloadUrlList = downloadUrlList;
        this.reciepientId = reciepientId;
        this.isGroupChat = isGroupChat;
        this.chatController = ChatController.getInstance(activity);
    }

    public StickerAdapter(MomentStickerBrowserActivity momentStickerFragment) {
        this.isMoment = false;
        this.isMoment = true;
        this.momentStickerFragment = momentStickerFragment;
        this.context = momentStickerFragment.getApplicationContext();
        this.inflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
    }

    public final int getCount() {
        return this.downloadUrlList.size();
    }

    public final long getItemId(int position) {
        return 0;
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = this.inflater.inflate(2130903174, null);
            holder = new ViewHolder();
            holder.imgSmiley = (ImageView) convertView.findViewById(2131362279);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(Uri.parse((String) this.downloadUrlList.get(position))).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).into(holder.imgSmiley, null);
        holder.imgSmiley.setOnClickListener(new C10111(position));
        return convertView;
    }
}

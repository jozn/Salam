package com.shamchat.adapters;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.v4.media.TransportMediator;
import android.text.ClipboardManager;
import android.text.Spannable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import com.kyleduo.switchbutton.C0473R;
import com.path.android.jobqueue.JobManager;
import com.shamchat.activity.ChatActivity;
import com.shamchat.activity.DownloadedImageFilePreview;
import com.shamchat.activity.DownloadedVideoFilePreview;
import com.shamchat.activity.LocalVideoFilePreview;
import com.shamchat.activity.ShamChatMapPreview;
import com.shamchat.activity.ShareIntentChatActivity;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.jobs.ImageDownloadJob;
import com.shamchat.jobs.VideoDownloadJob;
import com.shamchat.models.ChatMessage;
import com.shamchat.utils.Emoticons;
import com.shamchat.utils.ShamMediaPlayer;
import com.shamchat.utils.ShamMediaPlayer.OnPlayingCompletetionListner;
import com.shamchat.utils.ShamMediaPlayer.OnProgressUpdateListener;
import com.shamchat.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class IncomingMsg implements OnClickListener, OnLongClickListener, Row {
    public static String friendId;
    public static String friendProfileImageUrl;
    Button btnPlay;
    private ChatActivity chatActivity;
    public ChatMessage chatMessage;
    private Context context;
    private JobManager jobManager;
    private LayoutInflater layoutInflater;
    final Set<Target> protectedFromGarbageCollectorTargets;
    public ViewHolder viewHolder;

    /* renamed from: com.shamchat.adapters.IncomingMsg.1 */
    class C09711 implements OnClickListener {
        C09711() {
        }

        public final void onClick(View v) {
            String str = IncomingMsg.this.chatMessage.sender;
        }
    }

    /* renamed from: com.shamchat.adapters.IncomingMsg.2 */
    class C09722 implements OnClickListener {
        C09722() {
        }

        public final void onClick(View v) {
            IncomingMsg.this.viewHolder.downloadStart.setVisibility(8);
            IncomingMsg.this.viewHolder.downloadingProgress.setVisibility(0);
            if (IncomingMsg.this.chatMessage.messageContentType == MessageContentType.IMAGE) {
                IncomingMsg.this.jobManager.addJobInBackground(new ImageDownloadJob(IncomingMsg.this.chatMessage.uploadedFileUrl, IncomingMsg.this.chatMessage.packetId));
            } else if (IncomingMsg.this.chatMessage.messageContentType == MessageContentType.VIDEO) {
                IncomingMsg.this.jobManager.addJobInBackground(new VideoDownloadJob(IncomingMsg.this.chatMessage.uploadedFileUrl, IncomingMsg.this.chatMessage.packetId));
            }
        }
    }

    /* renamed from: com.shamchat.adapters.IncomingMsg.3 */
    class C09733 implements OnPlayingCompletetionListner {
        final /* synthetic */ ProgressBar val$progressBar;

        C09733(ProgressBar progressBar) {
            this.val$progressBar = progressBar;
        }

        public final void onCompletion$4ce696ce() {
            IncomingMsg.this.btnPlay.setText(2131493259);
            this.val$progressBar.setVisibility(8);
            IncomingMsg.this.btnPlay.setBackgroundResource(2130837650);
        }
    }

    /* renamed from: com.shamchat.adapters.IncomingMsg.4 */
    class C09744 implements OnProgressUpdateListener {
        final /* synthetic */ ProgressBar val$progressBar;

        C09744(ProgressBar progressBar) {
            this.val$progressBar = progressBar;
        }

        public final void onProgessUpdate(int progress) {
            System.out.println("AUDIO DOWNLOADING: " + progress);
            if (progress == 100) {
                this.val$progressBar.setVisibility(8);
            }
        }
    }

    /* renamed from: com.shamchat.adapters.IncomingMsg.5 */
    class C09755 implements DialogInterface.OnClickListener {
        C09755() {
        }

        @SuppressLint({"NewApi"})
        @TargetApi(11)
        public final void onClick(DialogInterface dialog, int which) {
            if ((IncomingMsg.this.chatMessage.fileUrl == null || IncomingMsg.this.chatMessage.fileUrl.length() == 0) && which == 0 && IncomingMsg.this.chatMessage.messageContentType != MessageContentType.TEXT) {
                which = 1;
            }
            if (which == 0) {
                Intent intent = new Intent(IncomingMsg.this.context, ShareIntentChatActivity.class);
                if (IncomingMsg.this.chatMessage.messageContentType == MessageContentType.TEXT) {
                    intent.putExtra("android.intent.extra.TEXT", IncomingMsg.this.chatMessage.textMessage);
                    intent.setType("text/plain");
                    intent.setAction("android.intent.action.SEND");
                } else {
                    Log.i("intent", "OutGoingGroupMSG file url:  " + IncomingMsg.this.chatMessage.fileUrl);
                    intent.putExtra("android.intent.extra.STREAM", Uri.parse(IncomingMsg.this.chatMessage.fileUrl));
                    if (IncomingMsg.this.chatMessage.messageContentType == MessageContentType.IMAGE) {
                        intent.setType("image/jpg");
                    } else if (IncomingMsg.this.chatMessage.messageContentType == MessageContentType.VIDEO) {
                        intent.setType("video/mp4");
                    } else if (IncomingMsg.this.chatMessage.messageContentType == MessageContentType.VOICE_RECORD) {
                        intent.setType("audio/mp3");
                    } else {
                        return;
                    }
                    intent.setAction("android.intent.action.SEND");
                }
                IncomingMsg.this.context.startActivity(intent);
            }
            if (which == 1) {
                SHAMChatApplication.getMyApplicationContext().getContentResolver().delete(ChatProviderNew.CONTENT_URI_CHAT, "packet_id=?", new String[]{IncomingMsg.this.chatMessage.packetId});
            }
            if (which != 2) {
                return;
            }
            if (VERSION.SDK_INT < 11) {
                ((ClipboardManager) IncomingMsg.this.context.getSystemService("clipboard")).setText(IncomingMsg.this.chatMessage.textMessage);
            } else {
                ((android.content.ClipboardManager) IncomingMsg.this.context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("Copied Text", IncomingMsg.this.chatMessage.textMessage));
            }
        }
    }

    /* renamed from: com.shamchat.adapters.IncomingMsg.6 */
    class C09776 implements Target {
        final /* synthetic */ ImageView val$imageView;

        /* renamed from: com.shamchat.adapters.IncomingMsg.6.1 */
        class C09761 implements Runnable {
            final /* synthetic */ Bitmap val$bitmap;

            C09761(Bitmap bitmap) {
                this.val$bitmap = bitmap;
            }

            public final void run() {
                Bitmap bitmap2 = this.val$bitmap;
                if (IncomingMsg.this.chatMessage.messageContentType == MessageContentType.IMAGE) {
                    bitmap2 = Utils.fastblur$75eed7c6(bitmap2);
                }
                C09776.this.val$imageView.setImageBitmap(bitmap2);
                IncomingMsg.this.protectedFromGarbageCollectorTargets.remove(this);
            }
        }

        C09776(ImageView imageView) {
            this.val$imageView = imageView;
        }

        public final void onBitmapLoaded$dc1124d(Bitmap bitmap) {
            System.out.println("Bit map loaded");
            if (IncomingMsg.this.chatActivity != null) {
                IncomingMsg.this.chatActivity.runOnUiThread(new C09761(bitmap));
            }
        }

        public final void onBitmapFailed$130e17e7() {
            IncomingMsg.this.protectedFromGarbageCollectorTargets.remove(this);
        }
    }

    /* renamed from: com.shamchat.adapters.IncomingMsg.7 */
    static /* synthetic */ class C09787 {
        static final /* synthetic */ int[] f20x39e42954;

        static {
            f20x39e42954 = new int[MessageContentType.values().length];
            try {
                f20x39e42954[MessageContentType.FAVORITE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f20x39e42954[MessageContentType.IMAGE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f20x39e42954[MessageContentType.INCOMING_CALL.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f20x39e42954[MessageContentType.LOCATION.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f20x39e42954[MessageContentType.OUTGOING_CALL.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f20x39e42954[MessageContentType.STICKER.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                f20x39e42954[MessageContentType.TEXT.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                f20x39e42954[MessageContentType.VOICE_RECORD.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                f20x39e42954[MessageContentType.VIDEO.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
        }
    }

    public class ViewHolder {
        public Button btnPlay;
        public ImageView downloadStart;
        public ProgressBar downloadingProgress;
        ImageView imgChat;
        FrameLayout imgChatWrapper;
        ImageView imgMask;
        ImageView imgProfile;
        public ImageView imgRetry;
        ImageView imgSticker;
        RelativeLayout latoutChatMessage;
        RelativeLayout layoutChatImage;
        LinearLayout layoutTextContent;
        RelativeLayout mainLayout;
        ProgressBar pBar;
        ImageView playIcon;
        TextView txtChatContent;
        TextView txtDesc;
        TextView txtFileSize;
        TextView txtIncomingCall;
        TextView txtMessageTime;

        public ViewHolder(RelativeLayout latoutChatMessage, LinearLayout layoutTextContent, TextView txtChatContent, RelativeLayout layoutChatImage, ImageView imgChat, ImageView imgProfile, TextView txtDesc, Button btnPlay, TextView txtMessageTime, RelativeLayout mainLayout, TextView txtIncomingCall, ImageView playIcon, TextView txtFileSize, ProgressBar pBar, ImageView imgMask, ImageView imgSticker, ProgressBar downloadingProgress, ImageView imgRetry, ImageView downloadStart, FrameLayout imgChatWrapper) {
            this.latoutChatMessage = latoutChatMessage;
            this.layoutTextContent = layoutTextContent;
            this.txtChatContent = txtChatContent;
            this.layoutChatImage = layoutChatImage;
            this.imgChat = imgChat;
            this.imgProfile = imgProfile;
            this.txtDesc = txtDesc;
            this.btnPlay = btnPlay;
            this.txtMessageTime = txtMessageTime;
            this.mainLayout = mainLayout;
            this.txtIncomingCall = txtIncomingCall;
            this.playIcon = playIcon;
            this.txtFileSize = txtFileSize;
            this.pBar = pBar;
            this.imgMask = imgMask;
            this.imgSticker = imgSticker;
            this.downloadingProgress = downloadingProgress;
            this.imgRetry = imgRetry;
            this.downloadStart = downloadStart;
            this.imgChatWrapper = imgChatWrapper;
        }
    }

    public IncomingMsg(LayoutInflater inflater, ChatMessage message, Context ctx, ChatActivity chatActivity, String friendId, String friendProfileImageUrl) {
        this.protectedFromGarbageCollectorTargets = new HashSet();
        this.chatMessage = message;
        this.layoutInflater = inflater;
        this.context = ctx;
        this.chatActivity = chatActivity;
        friendProfileImageUrl = friendProfileImageUrl;
        friendId = friendId;
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
    }

    public final View getView(View convertView) {
        if (convertView == null) {
            View viewGroup = (ViewGroup) this.layoutInflater.inflate(2130903114, null);
            ImageView imgProfile = (ImageView) viewGroup.findViewById(2131361842);
            TextView txtDesc = (TextView) viewGroup.findViewById(2131362136);
            Button btnPlay = (Button) viewGroup.findViewById(2131362129);
            ImageView imgRetry = (ImageView) viewGroup.findViewById(2131362142);
            this.viewHolder = new ViewHolder((RelativeLayout) viewGroup.findViewById(2131362126), (LinearLayout) viewGroup.findViewById(2131362127), (TextView) viewGroup.findViewById(2131362130), (RelativeLayout) viewGroup.findViewById(2131362131), (ImageView) viewGroup.findViewById(2131362134), imgProfile, txtDesc, btnPlay, (TextView) viewGroup.findViewById(2131362143), (RelativeLayout) viewGroup.findViewById(2131362123), (TextView) viewGroup.findViewById(2131362145), (ImageView) viewGroup.findViewById(2131362137), (TextView) viewGroup.findViewById(2131362138), (ProgressBar) viewGroup.findViewById(2131362017), (ImageView) viewGroup.findViewById(2131362133), (ImageView) viewGroup.findViewById(2131362135), (ProgressBar) viewGroup.findViewById(2131362141), imgRetry, (ImageView) viewGroup.findViewById(2131362140), (FrameLayout) viewGroup.findViewById(2131362132));
            viewGroup.setTag(this.viewHolder);
            convertView = viewGroup;
        } else {
            this.viewHolder = (ViewHolder) convertView.getTag();
        }
        this.viewHolder.imgChat.setImageDrawable(null);
        this.viewHolder.mainLayout.setVisibility(0);
        this.viewHolder.txtIncomingCall.setVisibility(8);
        this.viewHolder.playIcon.setVisibility(8);
        this.viewHolder.txtFileSize.setVisibility(0);
        this.viewHolder.pBar.setVisibility(8);
        this.viewHolder.imgSticker.setVisibility(8);
        this.viewHolder.layoutTextContent.setVisibility(0);
        this.viewHolder.layoutChatImage.setVisibility(8);
        this.viewHolder.imgChat.setVisibility(0);
        this.viewHolder.downloadStart.setVisibility(8);
        this.viewHolder.downloadingProgress.setVisibility(8);
        this.viewHolder.imgMask.setVisibility(0);
        this.viewHolder.txtFileSize.setVisibility(0);
        this.viewHolder.txtChatContent.setOnLongClickListener(this);
        this.viewHolder.imgChat.setOnLongClickListener(this);
        this.viewHolder.btnPlay.setOnLongClickListener(this);
        this.viewHolder.latoutChatMessage.setOnLongClickListener(this);
        this.viewHolder.imgProfile.setOnClickListener(new C09711());
        Bitmap bitmap = (Bitmap) SHAMChatApplication.USER_IMAGES.get(friendId);
        if (bitmap != null) {
            System.out.println("Image in the map");
            this.viewHolder.imgProfile.setImageBitmap(bitmap);
        } else if (friendProfileImageUrl == null || !friendProfileImageUrl.contains("http://")) {
            this.viewHolder.imgProfile.setImageResource(2130837946);
        } else {
            Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(Uri.parse(friendProfileImageUrl)).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).tag("singleChatListViewImages").into(this.viewHolder.imgProfile, null);
            Utils.handleProfileImage(this.context, friendId, friendProfileImageUrl);
        }
        if (this.chatMessage.messageDateTime != null && this.chatMessage.messageDateTime.length() > 0) {
            this.viewHolder.txtMessageTime.setText(Utils.formatStringDate(this.chatMessage.messageDateTime, "HH:mm"));
        }
        String str;
        String string;
        Spannable smiledText;
        switch (C09787.f20x39e42954[this.chatMessage.messageContentType.ordinal()]) {
            case Logger.SEVERE /*1*/:
                break;
            case Logger.WARNING /*2*/:
                this.viewHolder.layoutTextContent.setVisibility(8);
                this.viewHolder.layoutChatImage.setVisibility(0);
                this.viewHolder.txtFileSize.setVisibility(0);
                if (this.chatMessage.fileUrl == null) {
                    this.viewHolder.downloadStart.setVisibility(0);
                }
                str = this.chatMessage.packetId;
                handleImage$4ef3acf9(this.viewHolder.imgChat);
                this.viewHolder.imgChat.setOnClickListener(this);
                string = this.context.getResources().getString(2131493135);
                string = Utils.readableFileSize(this.chatMessage.fileSize);
                this.viewHolder.txtFileSize.setText(r28 + " " + r28);
                break;
            case Logger.INFO /*3*/:
                this.viewHolder.mainLayout.setVisibility(8);
                this.viewHolder.txtIncomingCall.setVisibility(0);
                smiledText = Emoticons.getSmiledText(SHAMChatApplication.getMyApplicationContext(), this.chatMessage.textMessage);
                this.viewHolder.txtChatContent.setText(spannable, BufferType.SPANNABLE);
                this.viewHolder.txtFileSize.setVisibility(4);
                break;
            case Logger.CONFIG /*4*/:
                this.viewHolder.layoutTextContent.setVisibility(8);
                this.viewHolder.layoutChatImage.setVisibility(0);
                str = this.chatMessage.packetId;
                handleImage$4ef3acf9(this.viewHolder.imgChat);
                this.viewHolder.imgChat.setOnClickListener(this);
                this.viewHolder.txtFileSize.setVisibility(4);
                break;
            case Logger.FINE /*5*/:
                this.viewHolder.txtFileSize.setVisibility(4);
                break;
            case Logger.FINER /*6*/:
                this.viewHolder.txtFileSize.setVisibility(4);
                this.viewHolder.layoutTextContent.setVisibility(8);
                this.viewHolder.layoutChatImage.setVisibility(0);
                this.viewHolder.imgChat.setVisibility(8);
                this.viewHolder.imgMask.setVisibility(8);
                this.viewHolder.imgChatWrapper.setVisibility(8);
                this.viewHolder.imgSticker.setVisibility(0);
                str = this.chatMessage.packetId;
                handleImage$4ef3acf9(this.viewHolder.imgSticker);
                break;
            case Logger.FINEST /*7*/:
                this.viewHolder.layoutChatImage.setVisibility(8);
                this.viewHolder.layoutTextContent.setVisibility(0);
                this.viewHolder.txtChatContent.setVisibility(0);
                this.viewHolder.btnPlay.setVisibility(8);
                smiledText = Emoticons.getSmiledText(SHAMChatApplication.getMyApplicationContext(), this.chatMessage.textMessage);
                this.viewHolder.txtChatContent.setText(spannable, BufferType.SPANNABLE);
                this.viewHolder.txtFileSize.setVisibility(4);
                break;
            case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                this.viewHolder.layoutChatImage.setVisibility(8);
                this.viewHolder.layoutTextContent.setVisibility(0);
                this.viewHolder.txtChatContent.setVisibility(8);
                this.viewHolder.btnPlay.setVisibility(0);
                this.viewHolder.btnPlay.setTag(this.viewHolder.pBar);
                this.viewHolder.btnPlay.setOnClickListener(this);
                this.viewHolder.txtChatContent.setText(this.chatMessage.textMessage);
                this.viewHolder.txtFileSize.setVisibility(0);
                string = this.context.getResources().getString(2131493135);
                string = Utils.readableFileSize(this.chatMessage.fileSize);
                this.viewHolder.txtFileSize.setText(r28 + " " + r28);
                break;
            case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                this.viewHolder.playIcon.setVisibility(0);
                this.viewHolder.layoutTextContent.setVisibility(8);
                this.viewHolder.layoutChatImage.setVisibility(0);
                this.viewHolder.txtFileSize.setVisibility(0);
                string = this.context.getResources().getString(2131493135);
                string = Utils.readableFileSize(this.chatMessage.fileSize);
                this.viewHolder.txtFileSize.setText(r28 + " " + r28);
                str = this.chatMessage.packetId;
                handleImage$4ef3acf9(this.viewHolder.imgChat);
                this.viewHolder.imgChat.setOnClickListener(this);
                break;
            default:
                System.out.println("unknown message content type found");
                this.viewHolder.layoutTextContent.setVisibility(0);
                break;
        }
        this.viewHolder.downloadStart.setOnClickListener(new C09722());
        this.viewHolder.txtDesc.setText(this.chatMessage.description);
        return convertView;
    }

    public final ChatMessage getChatMessageObject() {
        return this.chatMessage;
    }

    public final void onClick(View v) {
        System.out.println("clicked on " + this.chatMessage.textMessage);
        switch (v.getId()) {
            case 2131362129:
                this.btnPlay = (Button) v;
                ProgressBar progressBar = (ProgressBar) v.getTag();
                if (this.btnPlay.getText().toString().equalsIgnoreCase(this.context.getResources().getString(2131493259))) {
                    this.btnPlay.setText(2131493399);
                    progressBar.setVisibility(0);
                    this.btnPlay.setBackgroundResource(2130837651);
                    ShamMediaPlayer instance = ShamMediaPlayer.getInstance();
                    instance.listener = new C09744(progressBar);
                    instance.playCompletionlistner = new C09733(progressBar);
                    instance.play(this.chatMessage.textMessage);
                    return;
                }
                ShamMediaPlayer.getInstance().stop();
                this.btnPlay.setText(2131493259);
                progressBar.setVisibility(8);
                this.btnPlay.setBackgroundResource(2130837650);
            case 2131362134:
                Intent intent;
                switch (C09787.f20x39e42954[this.chatMessage.messageContentType.ordinal()]) {
                    case Logger.WARNING /*2*/:
                        intent = new Intent(v.getContext(), DownloadedImageFilePreview.class);
                        intent.putExtra("url", this.chatMessage.uploadedFileUrl);
                        intent.putExtra("packet_id", String.valueOf(this.chatMessage.packetId));
                        intent.putExtra(MqttServiceConstants.MESSAGE_ID, String.valueOf(this.chatMessage.messageId));
                        intent.putExtra("ThreadId", String.valueOf(this.chatMessage.threadId));
                        v.getContext().startActivity(intent);
                    case Logger.CONFIG /*4*/:
                        Intent mapIntent;
                        if (Utils.checkPlayServices(SHAMChatApplication.getMyApplicationContext())) {
                            System.out.println("LOCATION");
                            mapIntent = new Intent(v.getContext(), ShamChatMapPreview.class);
                            mapIntent.putExtra("latitude", this.chatMessage.latitude);
                            mapIntent.putExtra("longitude", this.chatMessage.longitude);
                            mapIntent.putExtra("address", this.chatMessage.description);
                            v.getContext().startActivity(mapIntent);
                            return;
                        }
                        mapIntent = new Intent(v.getContext(), DownloadedImageFilePreview.class);
                        mapIntent.putExtra("url", this.chatMessage.textMessage);
                        mapIntent.putExtra(MqttServiceConstants.MESSAGE_ID, String.valueOf(this.chatMessage.messageId));
                        Intent intent2 = null;
                        intent2.putExtra("ThreadId", String.valueOf(this.chatMessage.threadId));
                        v.getContext().startActivity(mapIntent);
                    case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                        String localVideoFileUrl = this.chatMessage.fileUrl;
                        boolean fileExists = false;
                        if (localVideoFileUrl != null) {
                            fileExists = Utils.fileExists(localVideoFileUrl);
                        }
                        if (localVideoFileUrl == null || localVideoFileUrl.length() <= 0 || !fileExists) {
                            intent = new Intent(v.getContext(), DownloadedVideoFilePreview.class);
                            intent.putExtra("url", this.chatMessage.uploadedFileUrl);
                            intent.putExtra("packet_id", this.chatMessage.packetId);
                            intent.putExtra(MqttServiceConstants.MESSAGE_ID, String.valueOf(this.chatMessage.messageId));
                            v.getContext().startActivity(intent);
                            return;
                        }
                        intent = new Intent(v.getContext(), LocalVideoFilePreview.class);
                        intent.putExtra("local_file_url", localVideoFileUrl);
                        v.getContext().startActivity(intent);
                    default:
                        System.out.println("Unknown");
                }
            default:
        }
    }

    private void showOptionsDialog() {
        Builder builderSingle = new Builder(this.context);
        builderSingle.setTitle(this.chatMessage.textMessage);
        builderSingle.setCancelable(true);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this.context, 17367043);
        if ((this.chatMessage.fileUrl == null || this.chatMessage.fileUrl.length() == 0) && this.chatMessage.messageContentType != MessageContentType.TEXT) {
            arrayAdapter.add(this.context.getString(2131493020));
        } else {
            arrayAdapter.add(this.context.getString(2131493024));
            arrayAdapter.add(this.context.getString(2131493020));
        }
        if (this.chatMessage.messageContentType == MessageContentType.TEXT) {
            arrayAdapter.add(this.context.getString(2131493019));
        } else if (this.chatMessage.messageContentType != MessageContentType.STICKER) {
            builderSingle.setTitle("Media File");
        } else {
            return;
        }
        builderSingle.setAdapter(arrayAdapter, new C09755());
        builderSingle.show();
    }

    public final boolean onLongClick(View arg0) {
        switch (arg0.getId()) {
            case 2131362126:
                showOptionsDialog();
                break;
            case 2131362129:
                showOptionsDialog();
                break;
            case 2131362130:
                showOptionsDialog();
                break;
            case 2131362134:
                showOptionsDialog();
                break;
        }
        return false;
    }

    private void handleImage$4ef3acf9(ImageView imageView) {
        String mainImageUrl = this.chatMessage.uploadedFileUrl;
        if (mainImageUrl == null || mainImageUrl.isEmpty()) {
            mainImageUrl = this.chatMessage.textMessage;
            if (!mainImageUrl.startsWith("http://") || !mainImageUrl.startsWith("https://") || mainImageUrl == null || mainImageUrl.isEmpty()) {
                return;
            }
        }
        String localFilePath = this.chatMessage.fileUrl;
        if (!Utils.fileExists(localFilePath) || localFilePath.indexOf(".mp4") != -1) {
            String imageUrl = mainImageUrl.replaceAll("upload", "upload/thumb");
            if (imageUrl.indexOf(".mp4") != -1) {
                imageUrl = imageUrl.replaceAll(".mp4", ".jpg");
            }
            System.out.println("Image url for thumb " + imageUrl);
            Target target = new C09776(imageView);
            this.protectedFromGarbageCollectorTargets.add(target);
            Picasso.with(this.context).load(imageUrl).tag("singleChatListViewImages").into(target);
        } else if (localFilePath != null && localFilePath.length() > 0) {
            File file = new File(localFilePath);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager) SHAMChatApplication.getMyApplicationContext().getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
            int i = displayMetrics.heightPixels;
            int i2 = displayMetrics.widthPixels;
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            int i3 = options.outHeight;
            int i4 = options.outWidth;
            Bitmap decodeSampledBitmapFromFile = i2 > i4 ? Utils.decodeSampledBitmapFromFile(file.getAbsolutePath(), i4, i3) : Utils.decodeSampledBitmapFromFile(file.getAbsolutePath(), i2, i);
            if (decodeSampledBitmapFromFile != null) {
                imageView.setImageBitmap(decodeSampledBitmapFromFile);
            }
        }
    }

    public final int getViewType() {
        return MyMessageType.INCOMING_MSG.ordinal();
    }
}

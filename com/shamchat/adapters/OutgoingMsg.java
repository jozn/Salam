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
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.v4.media.TransportMediator;
import android.text.ClipboardManager;
import android.text.Spannable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;
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
import com.shamchat.jobs.DeleteChatMessageDBLoadJob;
import com.shamchat.jobs.FileUploadJob;
import com.shamchat.models.ChatMessage;
import com.shamchat.models.ChatMessage.MessageStatusType;
import com.shamchat.utils.Emoticons;
import com.shamchat.utils.ShamMediaPlayer;
import com.shamchat.utils.ShamMediaPlayer.OnPlayingCompletetionListner;
import com.shamchat.utils.ShamMediaPlayer.OnProgressUpdateListener;
import com.shamchat.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class OutgoingMsg implements OnClickListener, OnLongClickListener, Row {
    Button btnPlay;
    private ChatActivity chatActivity;
    ChatMessage chatMessage;
    Context context;
    private Map<String, Bitmap> images;
    private JobManager jobManager;
    LayoutInflater layoutInflater;
    private String myProfileImageUrl;
    String myUserId;
    final Set<Target> protectedFromGarbageCollectorTargets;
    ViewHolder viewHolder;

    /* renamed from: com.shamchat.adapters.OutgoingMsg.10 */
    class AnonymousClass10 implements Runnable {
        final /* synthetic */ ImageView val$imageView;

        AnonymousClass10(ImageView imageView) {
            this.val$imageView = imageView;
        }

        public final void run() {
            this.val$imageView.setImageResource(2130838111);
        }
    }

    /* renamed from: com.shamchat.adapters.OutgoingMsg.11 */
    static /* synthetic */ class AnonymousClass11 {
        static final /* synthetic */ int[] f22x39e42954;
        static final /* synthetic */ int[] $SwitchMap$com$shamchat$models$ChatMessage$MessageStatusType;

        static {
            f22x39e42954 = new int[MessageContentType.values().length];
            try {
                f22x39e42954[MessageContentType.FAVORITE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f22x39e42954[MessageContentType.IMAGE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f22x39e42954[MessageContentType.INCOMING_CALL.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f22x39e42954[MessageContentType.LOCATION.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f22x39e42954[MessageContentType.OUTGOING_CALL.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f22x39e42954[MessageContentType.STICKER.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                f22x39e42954[MessageContentType.TEXT.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                f22x39e42954[MessageContentType.VOICE_RECORD.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                f22x39e42954[MessageContentType.VIDEO.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            $SwitchMap$com$shamchat$models$ChatMessage$MessageStatusType = new int[MessageStatusType.values().length];
            try {
                $SwitchMap$com$shamchat$models$ChatMessage$MessageStatusType[MessageStatusType.QUEUED.ordinal()] = 1;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$shamchat$models$ChatMessage$MessageStatusType[MessageStatusType.SENDING.ordinal()] = 2;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$shamchat$models$ChatMessage$MessageStatusType[MessageStatusType.SENT.ordinal()] = 3;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$shamchat$models$ChatMessage$MessageStatusType[MessageStatusType.DELIVERED.ordinal()] = 4;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$shamchat$models$ChatMessage$MessageStatusType[MessageStatusType.SEEN.ordinal()] = 5;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$shamchat$models$ChatMessage$MessageStatusType[MessageStatusType.FAILED.ordinal()] = 6;
            } catch (NoSuchFieldError e15) {
            }
        }
    }

    /* renamed from: com.shamchat.adapters.OutgoingMsg.1 */
    class C09941 implements OnClickListener {
        C09941() {
        }

        public final void onClick(View v) {
            OutgoingMsg.this.viewHolder.txtSeen.setText(2131493297);
            OutgoingMsg.this.viewHolder.imgRetry.setVisibility(8);
            OutgoingMsg.this.viewHolder.txtProgress.bringToFront();
            OutgoingMsg.this.viewHolder.txtProgress.setText("0%");
            OutgoingMsg.this.viewHolder.txtProgress.setVisibility(0);
            OutgoingMsg.this.viewHolder.uploadingProgress.setVisibility(0);
            OutgoingMsg.this.jobManager.addJobInBackground(new FileUploadJob(OutgoingMsg.this.chatMessage.fileUrl, OutgoingMsg.this.chatMessage.sender, OutgoingMsg.this.chatMessage.recipient, false, OutgoingMsg.this.chatMessage.messageContentType.ordinal(), OutgoingMsg.this.chatMessage.description, true, OutgoingMsg.this.chatMessage.packetId, 0.0d, 0.0d));
        }
    }

    /* renamed from: com.shamchat.adapters.OutgoingMsg.2 */
    class C09952 implements OnClickListener {
        C09952() {
        }

        public final void onClick(View v) {
        }
    }

    /* renamed from: com.shamchat.adapters.OutgoingMsg.3 */
    class C09963 implements OnPlayingCompletetionListner {
        C09963() {
        }

        public final void onCompletion$4ce696ce() {
            OutgoingMsg.this.btnPlay.setText(2131493259);
            OutgoingMsg.this.btnPlay.setBackgroundResource(2130837650);
        }
    }

    /* renamed from: com.shamchat.adapters.OutgoingMsg.4 */
    class C09974 implements OnProgressUpdateListener {
        C09974() {
        }

        public final void onProgessUpdate(int progress) {
            System.out.println(progress);
        }
    }

    /* renamed from: com.shamchat.adapters.OutgoingMsg.5 */
    class C09985 implements DialogInterface.OnClickListener {
        C09985() {
        }

        @SuppressLint({"NewApi"})
        @TargetApi(11)
        public final void onClick(DialogInterface dialog, int which) {
            if ((OutgoingMsg.this.chatMessage.uploadedFileUrl == null || OutgoingMsg.this.chatMessage.uploadedFileUrl.length() == 0) && which == 0 && OutgoingMsg.this.chatMessage.messageContentType != MessageContentType.TEXT) {
                which = 1;
            }
            if (which == 0) {
                Intent intent = new Intent(OutgoingMsg.this.context, ShareIntentChatActivity.class);
                if (OutgoingMsg.this.chatMessage.messageContentType == MessageContentType.TEXT) {
                    intent.putExtra("android.intent.extra.TEXT", OutgoingMsg.this.chatMessage.textMessage);
                    intent.setType("text/plain");
                    intent.setAction("android.intent.action.SEND");
                } else {
                    Log.i("intent", "OutgoingMsg file url:  " + OutgoingMsg.this.chatMessage.fileUrl);
                    intent.putExtra("android.intent.extra.STREAM", Uri.parse(OutgoingMsg.this.chatMessage.fileUrl));
                    if (OutgoingMsg.this.chatMessage.messageContentType == MessageContentType.IMAGE) {
                        intent.setType("image/jpg");
                    } else if (OutgoingMsg.this.chatMessage.messageContentType == MessageContentType.VIDEO) {
                        intent.setType("video/mp4");
                    } else if (OutgoingMsg.this.chatMessage.messageContentType == MessageContentType.VOICE_RECORD) {
                        intent.setType("audio/mp3");
                    } else {
                        return;
                    }
                    intent.setAction("android.intent.action.SEND");
                }
                OutgoingMsg.this.context.startActivity(intent);
            }
            if (which == 1) {
                OutgoingMsg.this.jobManager.addJobInBackground(new DeleteChatMessageDBLoadJob(OutgoingMsg.this.chatMessage.packetId));
            }
            if (which != 2) {
                return;
            }
            if (VERSION.SDK_INT < 11) {
                ((ClipboardManager) OutgoingMsg.this.context.getSystemService("clipboard")).setText(OutgoingMsg.this.chatMessage.textMessage);
            } else {
                ((android.content.ClipboardManager) OutgoingMsg.this.context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("Copied Text", OutgoingMsg.this.chatMessage.textMessage));
            }
        }
    }

    /* renamed from: com.shamchat.adapters.OutgoingMsg.6 */
    class C09996 implements Runnable {
        final /* synthetic */ Bitmap val$b;
        final /* synthetic */ ImageView val$imageView;

        C09996(ImageView imageView, Bitmap bitmap) {
            this.val$imageView = imageView;
            this.val$b = bitmap;
        }

        public final void run() {
            this.val$imageView.setImageBitmap(this.val$b);
        }
    }

    /* renamed from: com.shamchat.adapters.OutgoingMsg.7 */
    class C10007 implements Runnable {
        final /* synthetic */ Bitmap val$bm;
        final /* synthetic */ ImageView val$imageView;

        C10007(ImageView imageView, Bitmap bitmap) {
            this.val$imageView = imageView;
            this.val$bm = bitmap;
        }

        public final void run() {
            this.val$imageView.setImageBitmap(this.val$bm);
        }
    }

    /* renamed from: com.shamchat.adapters.OutgoingMsg.8 */
    class C10018 implements Runnable {
        final /* synthetic */ ImageView val$imageView;
        final /* synthetic */ Uri val$uri;

        C10018(Uri uri, ImageView imageView) {
            this.val$uri = uri;
            this.val$imageView = imageView;
        }

        public final void run() {
            Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(this.val$uri).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).tag("singleChatListViewImages").into(this.val$imageView, null);
        }
    }

    /* renamed from: com.shamchat.adapters.OutgoingMsg.9 */
    class C10029 implements Runnable {
        final /* synthetic */ ImageView val$imageView;
        final /* synthetic */ Uri val$uri;

        C10029(Uri uri, ImageView imageView) {
            this.val$uri = uri;
            this.val$imageView = imageView;
        }

        public final void run() {
            Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(this.val$uri).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).tag("singleChatListViewImages").into(this.val$imageView, null);
        }
    }

    class ViewHolder {
        Button btnPlay;
        ImageView imgChat;
        FrameLayout imgChatWrapper;
        ImageView imgMask;
        ImageView imgProfile;
        ImageView imgRetry;
        ImageView imgSticker;
        ImageView isDelivered;
        RelativeLayout latoutChatMessage;
        RelativeLayout layoutChatImage;
        LinearLayout layoutTextContent;
        RelativeLayout mainLayout;
        ImageView playIcon;
        ProgressBar progressBarVoice;
        TextView txtChatContent;
        TextView txtDesc;
        TextView txtMessageTime;
        TextView txtOutgoing;
        TextView txtProgress;
        TextView txtSeen;
        RelativeLayout uploadingProgLayout;
        ProgressBar uploadingProgress;

        public ViewHolder(RelativeLayout latoutChatMessage, LinearLayout layoutTextContent, TextView txtChatContent, RelativeLayout layoutChatImage, ImageView imgChat, ImageView isDelivered, ImageView imgProfile, TextView txtSeen, TextView txtDesc, Button btnPlay, TextView txtMessageTime, ProgressBar uploadingProgress, RelativeLayout mainLayout, TextView txtOutgoing, ImageView playIcon, ProgressBar progressBarVoice, TextView txtProgress, RelativeLayout uploadingProgLayout, ImageView imgRetry, ImageView imgMask, ImageView imgSticker, FrameLayout imgChatWrapper) {
            this.latoutChatMessage = latoutChatMessage;
            this.layoutTextContent = layoutTextContent;
            this.txtChatContent = txtChatContent;
            this.layoutChatImage = layoutChatImage;
            this.imgChat = imgChat;
            this.isDelivered = isDelivered;
            this.imgProfile = imgProfile;
            this.txtSeen = txtSeen;
            this.txtDesc = txtDesc;
            this.playIcon = playIcon;
            this.btnPlay = btnPlay;
            this.txtMessageTime = txtMessageTime;
            this.uploadingProgress = uploadingProgress;
            this.mainLayout = mainLayout;
            this.progressBarVoice = progressBarVoice;
            this.txtOutgoing = txtOutgoing;
            this.txtProgress = txtProgress;
            this.imgRetry = imgRetry;
            this.uploadingProgLayout = uploadingProgLayout;
            this.imgMask = imgMask;
            this.imgSticker = imgSticker;
            this.imgChatWrapper = imgChatWrapper;
        }
    }

    public OutgoingMsg(LayoutInflater inflater, ChatMessage message, Context ctx, String myUserId, ChatActivity chatActivity, String myProfileImageUrl) {
        this.images = new HashMap();
        this.protectedFromGarbageCollectorTargets = new HashSet();
        this.chatMessage = message;
        this.layoutInflater = inflater;
        this.context = ctx;
        this.chatActivity = chatActivity;
        this.myUserId = myUserId;
        this.myProfileImageUrl = myProfileImageUrl;
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
    }

    public final View getView(View convertView) {
        if (convertView == null) {
            View viewGroup = (ViewGroup) this.layoutInflater.inflate(2130903115, null);
            Button btnPlay = (Button) viewGroup.findViewById(2131362129);
            TextView txtMessageTime = (TextView) viewGroup.findViewById(2131362143);
            ProgressBar uploadingProgress = (ProgressBar) viewGroup.findViewById(2131362152);
            this.viewHolder = new ViewHolder((RelativeLayout) viewGroup.findViewById(2131362126), (LinearLayout) viewGroup.findViewById(2131362127), (TextView) viewGroup.findViewById(2131362130), (RelativeLayout) viewGroup.findViewById(2131362131), (ImageView) viewGroup.findViewById(2131362134), (ImageView) viewGroup.findViewById(2131362153), (ImageView) viewGroup.findViewById(2131361842), (TextView) viewGroup.findViewById(2131362154), (TextView) viewGroup.findViewById(2131362136), btnPlay, txtMessageTime, uploadingProgress, (RelativeLayout) viewGroup.findViewById(2131362146), (TextView) viewGroup.findViewById(2131362156), (ImageView) viewGroup.findViewById(2131362137), (ProgressBar) viewGroup.findViewById(2131362150), (TextView) viewGroup.findViewById(2131362122), (RelativeLayout) viewGroup.findViewById(2131362151), (ImageView) viewGroup.findViewById(2131362142), (ImageView) viewGroup.findViewById(2131362133), (ImageView) viewGroup.findViewById(2131362135), (FrameLayout) viewGroup.findViewById(2131362132));
            viewGroup.setTag(this.viewHolder);
            convertView = viewGroup;
        } else {
            this.viewHolder = (ViewHolder) convertView.getTag();
        }
        this.viewHolder.imgChat.setImageDrawable(null);
        this.viewHolder.mainLayout.setVisibility(0);
        this.viewHolder.txtOutgoing.setVisibility(8);
        this.viewHolder.playIcon.setVisibility(8);
        this.viewHolder.uploadingProgLayout.setVisibility(0);
        this.viewHolder.progressBarVoice.setVisibility(0);
        this.viewHolder.imgSticker.setVisibility(8);
        this.viewHolder.layoutTextContent.setVisibility(0);
        this.viewHolder.layoutChatImage.setVisibility(8);
        this.viewHolder.imgChat.setVisibility(0);
        this.viewHolder.imgMask.setVisibility(0);
        this.viewHolder.txtChatContent.setOnLongClickListener(this);
        this.viewHolder.imgChat.setOnLongClickListener(this);
        this.viewHolder.btnPlay.setOnLongClickListener(this);
        this.viewHolder.latoutChatMessage.setOnLongClickListener(this);
        MessageStatusType messageStatusType = this.chatMessage.messageStatus;
        System.out.println("Message status " + messageStatusType);
        this.viewHolder.isDelivered.setVisibility(8);
        String str = this.chatMessage.packetId;
        String str2 = " content: ";
        str = this.chatMessage.textMessage;
        System.out.println("message id out " + str + str + str);
        switch (AnonymousClass11.$SwitchMap$com$shamchat$models$ChatMessage$MessageStatusType[messageStatusType.ordinal()]) {
            case Logger.SEVERE /*1*/:
                this.viewHolder.txtSeen.setVisibility(8);
                break;
            case Logger.WARNING /*2*/:
                System.out.println("Message status  case sending");
                this.viewHolder.txtSeen.setText(2131493358);
                this.viewHolder.txtSeen.setVisibility(0);
                break;
            case Logger.INFO /*3*/:
                this.viewHolder.txtSeen.setText(2131493359);
                this.viewHolder.txtSeen.setVisibility(0);
                this.viewHolder.uploadingProgLayout.setVisibility(8);
                this.viewHolder.progressBarVoice.setVisibility(8);
                break;
            case Logger.CONFIG /*4*/:
                this.viewHolder.txtSeen.setVisibility(8);
                this.viewHolder.isDelivered.setVisibility(0);
                this.viewHolder.uploadingProgLayout.setVisibility(8);
                this.viewHolder.progressBarVoice.setVisibility(8);
                break;
            case Logger.FINE /*5*/:
                System.out.println("Message status  case seen");
                this.viewHolder.txtSeen.setVisibility(0);
                this.viewHolder.txtSeen.setText(2131493354);
                this.viewHolder.isDelivered.setVisibility(0);
                break;
            case Logger.FINER /*6*/:
                this.viewHolder.txtSeen.setVisibility(0);
                this.viewHolder.txtSeen.setText(2131493123);
                this.viewHolder.isDelivered.setVisibility(8);
                break;
        }
        if (this.chatMessage.textMessage == null || this.chatMessage.textMessage.length() <= 0) {
            if (!(this.chatMessage.messageContentType == MessageContentType.TEXT || this.chatMessage.messageContentType == MessageContentType.STICKER)) {
                int i = this.chatMessage.uploadedPercentage;
                str2 = " url ";
                str = this.chatMessage.uploadedFileUrl;
                System.out.println("TEST UPLOADING per " + i + str + str);
                if (this.chatMessage.uploadedPercentage > 100 || this.chatMessage.uploadedFileUrl != null) {
                    System.out.println("TEST UPLOADING ELSE chatMessage.getUploadedPercentage() <= 100 && chatMessage.getUploadedFileUrl() == null ");
                } else {
                    this.viewHolder.txtSeen.setText(2131493433);
                    this.viewHolder.txtSeen.setVisibility(0);
                    System.out.println("TEST UPLOADING IF chatMessage.getUploadedPercentage() <= 100 && chatMessage.getUploadedFileUrl() == null ");
                }
            }
        } else if (this.chatMessage.messageStatus != MessageStatusType.DELIVERED) {
            if (this.chatMessage.messageContentType == MessageContentType.TEXT || this.chatMessage.messageContentType == MessageContentType.STICKER) {
                this.viewHolder.uploadingProgLayout.setVisibility(8);
                this.viewHolder.progressBarVoice.setVisibility(8);
            } else if (this.chatMessage.textMessage != null && this.chatMessage.textMessage.equalsIgnoreCase("failed")) {
                this.viewHolder.txtSeen.setText(2131493123);
                this.viewHolder.txtSeen.setVisibility(0);
            }
        }
        if (this.chatMessage.uploadedPercentage == 100) {
            this.viewHolder.uploadingProgLayout.setVisibility(8);
            this.viewHolder.progressBarVoice.setVisibility(8);
        }
        try {
            if (this.chatMessage.messageDateTime != null && this.chatMessage.messageDateTime.length() > 0) {
                this.viewHolder.txtMessageTime.setText(Utils.formatStringDate(this.chatMessage.messageDateTime, "HH:mm"));
            }
        } catch (Exception e) {
            this.viewHolder.txtMessageTime.setText("Unknown date");
        }
        this.viewHolder.txtProgress.bringToFront();
        if (this.chatMessage.uploadedPercentage != 9999) {
            str2 = "%";
            this.viewHolder.txtProgress.setText(this.chatMessage.uploadedPercentage + str);
            this.viewHolder.txtProgress.setVisibility(0);
            this.viewHolder.uploadingProgress.setVisibility(0);
            this.viewHolder.imgRetry.setVisibility(8);
        } else if (!(this.chatMessage.messageContentType == MessageContentType.TEXT || this.chatMessage.messageContentType == MessageContentType.VOICE_RECORD || this.chatMessage.messageContentType == MessageContentType.STICKER)) {
            this.viewHolder.txtProgress.setVisibility(8);
            this.viewHolder.uploadingProgress.setVisibility(8);
            this.viewHolder.imgRetry.setVisibility(0);
            this.viewHolder.imgRetry.bringToFront();
            this.viewHolder.txtSeen.setText(2131493123);
            this.viewHolder.txtSeen.setVisibility(0);
            this.viewHolder.imgRetry.setOnClickListener(new C09941());
        }
        this.viewHolder.imgProfile.setOnClickListener(new C09952());
        Bitmap bitmap = (Bitmap) SHAMChatApplication.USER_IMAGES.get(this.myUserId);
        if (bitmap != null) {
            System.out.println("Image in the map");
            this.viewHolder.imgProfile.setImageBitmap(bitmap);
        } else if (this.myProfileImageUrl == null || !this.myProfileImageUrl.contains("http://")) {
            this.viewHolder.imgProfile.setImageResource(2130837944);
        } else {
            Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(Uri.parse(this.myProfileImageUrl)).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).tag("singleChatListViewImages").into(this.viewHolder.imgProfile, null);
            Utils.handleProfileImage(this.context, this.myUserId, this.myProfileImageUrl);
        }
        String str3;
        Spannable smiledText;
        switch (AnonymousClass11.f22x39e42954[this.chatMessage.messageContentType.ordinal()]) {
            case Logger.SEVERE /*1*/:
            case Logger.INFO /*3*/:
                break;
            case Logger.WARNING /*2*/:
                this.viewHolder.layoutTextContent.setVisibility(8);
                this.viewHolder.layoutChatImage.setVisibility(0);
                str = this.chatMessage.fileUrl;
                System.out.println("File path " + str);
                str3 = this.chatMessage.packetId;
                handleImage(messageId, this.viewHolder.imgChat);
                this.viewHolder.imgChat.setOnClickListener(this);
                this.viewHolder.playIcon.setVisibility(8);
                break;
            case Logger.CONFIG /*4*/:
                this.viewHolder.layoutTextContent.setVisibility(8);
                this.viewHolder.layoutChatImage.setVisibility(0);
                handleImage(this.chatMessage.packetId, this.viewHolder.imgChat);
                this.viewHolder.imgChat.setOnClickListener(this);
                break;
            case Logger.FINE /*5*/:
                this.viewHolder.mainLayout.setVisibility(8);
                this.viewHolder.txtOutgoing.setVisibility(0);
                smiledText = Emoticons.getSmiledText(SHAMChatApplication.getMyApplicationContext(), this.chatMessage.textMessage);
                this.viewHolder.txtChatContent.setText(spannable, BufferType.SPANNABLE);
                break;
            case Logger.FINER /*6*/:
                this.viewHolder.layoutTextContent.setVisibility(8);
                this.viewHolder.layoutChatImage.setVisibility(0);
                this.viewHolder.imgChat.setVisibility(8);
                this.viewHolder.uploadingProgLayout.setVisibility(8);
                this.viewHolder.imgMask.setVisibility(8);
                this.viewHolder.imgChatWrapper.setVisibility(8);
                this.viewHolder.imgSticker.setVisibility(0);
                this.viewHolder.playIcon.setVisibility(8);
                Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(Uri.parse(this.chatMessage.textMessage)).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).tag("singleChatListViewImages").into(this.viewHolder.imgSticker, null);
                break;
            case Logger.FINEST /*7*/:
                this.viewHolder.layoutChatImage.setVisibility(8);
                this.viewHolder.layoutTextContent.setVisibility(0);
                this.viewHolder.txtChatContent.setVisibility(0);
                this.viewHolder.btnPlay.setVisibility(8);
                if (this.chatMessage.textMessage != null && this.chatMessage.textMessage.length() > 0) {
                    smiledText = Emoticons.getSmiledText(SHAMChatApplication.getMyApplicationContext(), this.chatMessage.textMessage);
                    this.viewHolder.txtChatContent.setText(spannable, BufferType.SPANNABLE);
                    break;
                }
            case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                this.viewHolder.layoutChatImage.setVisibility(8);
                this.viewHolder.layoutTextContent.setVisibility(0);
                this.viewHolder.txtChatContent.setVisibility(8);
                this.viewHolder.btnPlay.setVisibility(0);
                this.viewHolder.btnPlay.setOnClickListener(this);
                this.viewHolder.txtChatContent.setText(this.chatMessage.textMessage);
                break;
            case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                this.viewHolder.layoutTextContent.setVisibility(8);
                this.viewHolder.layoutChatImage.setVisibility(0);
                str3 = this.chatMessage.packetId;
                handleImage(messageId, this.viewHolder.imgChat);
                this.viewHolder.imgChat.setOnClickListener(this);
                this.viewHolder.playIcon.setVisibility(0);
                break;
            default:
                System.out.println("unknown message content type found");
                this.viewHolder.layoutTextContent.setVisibility(0);
                break;
        }
        if (this.chatMessage.description == null || this.chatMessage.description.length() == 0) {
            this.viewHolder.txtDesc.setVisibility(8);
        } else {
            this.viewHolder.txtDesc.setVisibility(0);
        }
        this.viewHolder.txtDesc.setText(this.chatMessage.description);
        return convertView;
    }

    public final ChatMessage getChatMessageObject() {
        return this.chatMessage;
    }

    public final void onClick(View v) {
        switch (v.getId()) {
            case 2131362129:
                if (this.chatMessage.uploadedFileUrl != null) {
                    this.btnPlay = (Button) v;
                    if (this.btnPlay.getText().toString().equalsIgnoreCase(this.context.getResources().getString(2131493259))) {
                        this.btnPlay.setText(2131493399);
                        this.btnPlay.setBackgroundResource(2130837651);
                        ShamMediaPlayer instance = ShamMediaPlayer.getInstance();
                        instance.listener = new C09974();
                        instance.playCompletionlistner = new C09963();
                        instance.play(this.chatMessage.uploadedFileUrl);
                        return;
                    }
                    this.btnPlay.setText(2131493259);
                    this.btnPlay.setBackgroundResource(2130837650);
                    ShamMediaPlayer.getInstance().stop();
                    return;
                }
                Toast.makeText(SHAMChatApplication.getMyApplicationContext(), 2131493442, 0).show();
            case 2131362134:
                Intent intent;
                switch (AnonymousClass11.f22x39e42954[this.chatMessage.messageContentType.ordinal()]) {
                    case Logger.WARNING /*2*/:
                        String fileUrl = this.chatMessage.uploadedFileUrl;
                        if (fileUrl != null && fileUrl.length() > 0 && this.chatMessage.textMessage != null && this.chatMessage.textMessage.length() > 0) {
                            intent = new Intent(v.getContext(), DownloadedImageFilePreview.class);
                            intent.putExtra("url", this.chatMessage.textMessage);
                            intent.putExtra("packet_id", String.valueOf(this.chatMessage.packetId));
                            intent.putExtra(MqttServiceConstants.MESSAGE_ID, String.valueOf(this.chatMessage.messageId));
                            intent.putExtra("ThreadId", String.valueOf(this.chatMessage.threadId));
                            v.getContext().startActivity(intent);
                        }
                    case Logger.CONFIG /*4*/:
                        System.out.println("LOCATION");
                        Intent mapIntent = new Intent(v.getContext(), ShamChatMapPreview.class);
                        mapIntent.putExtra("latitude", this.chatMessage.latitude);
                        mapIntent.putExtra("longitude", this.chatMessage.longitude);
                        mapIntent.putExtra("address", this.chatMessage.description);
                        v.getContext().startActivity(mapIntent);
                    case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                        String localVideoFileUrl = this.chatMessage.fileUrl;
                        boolean fileExists = false;
                        if (localVideoFileUrl != null) {
                            fileExists = Utils.fileExists(localVideoFileUrl);
                        }
                        if (localVideoFileUrl == null || localVideoFileUrl.length() <= 0 || !fileExists) {
                            intent = new Intent(v.getContext(), DownloadedVideoFilePreview.class);
                            intent.putExtra("url", this.chatMessage.textMessage);
                            intent.putExtra("packet_id", this.chatMessage.packetId);
                            intent.putExtra(MqttServiceConstants.MESSAGE_ID, String.valueOf(this.chatMessage.messageId));
                            v.getContext().startActivity(intent);
                            return;
                        }
                        intent = new Intent(v.getContext(), LocalVideoFilePreview.class);
                        intent.putExtra("local_file_url", localVideoFileUrl);
                        v.getContext().startActivity(intent);
                    default:
                        System.out.println("UNKNOWN CONTENT TYPE FOUND");
                }
            default:
        }
    }

    private void showOptionsDialog() {
        Builder builderSingle = new Builder(this.context);
        builderSingle.setTitle(this.chatMessage.textMessage);
        builderSingle.setCancelable(true);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this.context, 17367043);
        if ((this.chatMessage.uploadedFileUrl == null || this.chatMessage.uploadedFileUrl.length() == 0) && this.chatMessage.messageContentType != MessageContentType.TEXT) {
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
        builderSingle.setAdapter(arrayAdapter, new C09985());
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

    private void handleImage(String packetId, ImageView imageView) {
        Bitmap b = (Bitmap) this.images.get(packetId);
        if (b != null) {
            if (this.chatActivity != null) {
                this.chatActivity.runOnUiThread(new C09996(imageView, b));
            }
        } else if (this.chatMessage.fileUrl == null || this.chatMessage.fileUrl.contains(".mp4")) {
            mainImageUrl = this.chatMessage.uploadedFileUrl;
            if (mainImageUrl != null) {
                System.out.println("Video thum " + mainImageUrl);
                imageUrl = mainImageUrl.replaceAll("upload", "upload/thumb");
                if (imageUrl.indexOf(".mp4") != -1) {
                    imageUrl = imageUrl.replaceAll(".mp4", ".jpg");
                }
                System.out.println("Video thum imageUrl " + imageUrl);
                if (imageUrl != null && imageUrl.contains("http://")) {
                    System.out.println("Video thum imageUrl http");
                    uri = Uri.parse(imageUrl);
                    if (this.chatActivity != null) {
                        this.chatActivity.runOnUiThread(new C10029(uri, imageView));
                    }
                }
            } else if (this.chatActivity != null) {
                this.chatActivity.runOnUiThread(new AnonymousClass10(imageView));
            }
        } else {
            File f = new File(this.chatMessage.fileUrl);
            if (f.exists()) {
                try {
                    Bitmap bm = BitmapFactory.decodeFile(f.getAbsolutePath());
                    this.images.put(packetId, bm);
                    if (this.chatActivity != null) {
                        this.chatActivity.runOnUiThread(new C10007(imageView, bm));
                        return;
                    }
                    return;
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                    return;
                }
            }
            mainImageUrl = this.chatMessage.uploadedFileUrl;
            if (mainImageUrl == null || mainImageUrl.isEmpty()) {
                mainImageUrl = this.chatMessage.textMessage;
                if (!mainImageUrl.startsWith("http://") || !mainImageUrl.startsWith("https://") || mainImageUrl == null || mainImageUrl.isEmpty()) {
                    return;
                }
            }
            imageUrl = mainImageUrl.replaceAll("upload", "upload/thumb");
            if (imageUrl.indexOf(".mp4") != -1) {
                imageUrl = imageUrl.replaceAll(".mp4", "jpg");
            }
            if (imageUrl != null && imageUrl.contains("http://")) {
                uri = Uri.parse(imageUrl);
                if (this.chatActivity != null) {
                    this.chatActivity.runOnUiThread(new C10018(uri, imageView));
                }
            }
        }
    }

    public final int getViewType() {
        return MyMessageType.OUTGOING_MSG.ordinal();
    }
}

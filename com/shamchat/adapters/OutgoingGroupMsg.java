package com.shamchat.adapters;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.v4.media.TransportMediator;
import android.support.v7.appcompat.BuildConfig;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;
import com.kyleduo.switchbutton.C0473R;
import com.path.android.jobqueue.JobManager;
import com.shamchat.activity.AddFavoriteTextActivity;
import com.shamchat.activity.ChatInitialForGroupChatActivity;
import com.shamchat.activity.DownloadedImageFilePreview;
import com.shamchat.activity.DownloadedVideoFilePreview;
import com.shamchat.activity.LocalVideoFilePreview;
import com.shamchat.activity.ShamChatMapPreview;
import com.shamchat.activity.ShareIntentChatActivity;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.jobs.DeleteChatMessageDBLoadJob;
import com.shamchat.jobs.FileUploadJob;
import com.shamchat.jobs.PublishToTopicJob;
import com.shamchat.models.ChatMessage;
import com.shamchat.models.ChatMessage.MessageStatusType;
import com.shamchat.models.FriendGroup;
import com.shamchat.utils.Emoticons;
import com.shamchat.utils.ShamMediaPlayer;
import com.shamchat.utils.ShamMediaPlayer.OnPlayingCompletetionListner;
import com.shamchat.utils.ShamMediaPlayer.OnProgressUpdateListener;
import com.shamchat.utils.Utils;
import com.squareup.picasso.Picasso;
import java.io.File;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.json.JSONObject;

public final class OutgoingGroupMsg implements OnClickListener, OnLongClickListener, Row {
    Button btnPlay;
    private ChatInitialForGroupChatActivity chatInitialForGroupChatActivity;
    public ChatMessage chatMessage;
    Context context;
    private JobManager jobManager;
    LayoutInflater layoutInflater;
    String myUserId;
    public ViewHolder viewHolder;

    /* renamed from: com.shamchat.adapters.OutgoingGroupMsg.10 */
    static /* synthetic */ class AnonymousClass10 {
        static final /* synthetic */ int[] f21x39e42954;
        static final /* synthetic */ int[] $SwitchMap$com$shamchat$models$ChatMessage$MessageStatusType;

        static {
            f21x39e42954 = new int[MessageContentType.values().length];
            try {
                f21x39e42954[MessageContentType.FAVORITE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f21x39e42954[MessageContentType.IMAGE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f21x39e42954[MessageContentType.INCOMING_CALL.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f21x39e42954[MessageContentType.LOCATION.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f21x39e42954[MessageContentType.OUTGOING_CALL.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f21x39e42954[MessageContentType.STICKER.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                f21x39e42954[MessageContentType.TEXT.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                f21x39e42954[MessageContentType.VOICE_RECORD.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                f21x39e42954[MessageContentType.VIDEO.ordinal()] = 9;
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

    /* renamed from: com.shamchat.adapters.OutgoingGroupMsg.1 */
    class C09851 implements OnClickListener {
        C09851() {
        }

        public final void onClick(View v) {
            OutgoingGroupMsg.this.viewHolder.txtSeen.setText(2131493297);
            OutgoingGroupMsg.this.viewHolder.txtRetry.setVisibility(8);
            JSONObject jsonMessageObject = new JSONObject();
            FriendGroup group = null;
            try {
                group = UserProvider.groupFromCursor(SHAMChatApplication.getMyApplicationContext().getContentResolver().query(UserProvider.CONTENT_URI_GROUP, null, FriendGroup.DB_ID + "=?", new String[]{OutgoingGroupMsg.this.chatMessage.recipient}, null));
            } catch (Exception e) {
            }
            try {
                jsonMessageObject.put("packet_type", AddFavoriteTextActivity.EXTRA_MESSAGE);
                jsonMessageObject.put("to", OutgoingGroupMsg.this.chatMessage.recipient);
                jsonMessageObject.put("from", OutgoingGroupMsg.this.chatMessage.sender);
                jsonMessageObject.put("from_userid", SHAMChatApplication.getConfig().userId);
                jsonMessageObject.put("messageBody", OutgoingGroupMsg.this.chatMessage.textMessage);
                jsonMessageObject.put("messageType", OutgoingGroupMsg.this.chatMessage.messageContentType.type);
                jsonMessageObject.put("messageTypeDesc", BuildConfig.VERSION_NAME);
                jsonMessageObject.put("timestamp", Utils.getTimeStamp());
                jsonMessageObject.put("groupAlias", group.name);
                jsonMessageObject.put("isGroupChat", 1);
                jsonMessageObject.put("packetId", OutgoingGroupMsg.this.chatMessage.packetId);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            OutgoingGroupMsg.this.jobManager.addJobInBackground(new PublishToTopicJob(jsonMessageObject.toString(), "groups/" + group.id, (byte) 0));
        }
    }

    /* renamed from: com.shamchat.adapters.OutgoingGroupMsg.2 */
    class C09862 implements OnClickListener {
        C09862() {
        }

        public final void onClick(View v) {
            OutgoingGroupMsg.this.viewHolder.txtSeen.setText(2131493297);
            OutgoingGroupMsg.this.viewHolder.imgRetry.setVisibility(8);
            OutgoingGroupMsg.this.viewHolder.txtProgress.bringToFront();
            OutgoingGroupMsg.this.viewHolder.txtProgress.setText("0%");
            OutgoingGroupMsg.this.viewHolder.txtProgress.setVisibility(0);
            OutgoingGroupMsg.this.viewHolder.uploadingProgress.setVisibility(0);
            OutgoingGroupMsg.this.jobManager.addJobInBackground(new FileUploadJob(OutgoingGroupMsg.this.chatMessage.fileUrl, OutgoingGroupMsg.this.chatMessage.sender, OutgoingGroupMsg.this.chatMessage.recipient, true, OutgoingGroupMsg.this.chatMessage.messageContentType.ordinal(), OutgoingGroupMsg.this.chatMessage.description, true, OutgoingGroupMsg.this.chatMessage.packetId, 0.0d, 0.0d));
        }
    }

    /* renamed from: com.shamchat.adapters.OutgoingGroupMsg.3 */
    class C09873 implements OnPlayingCompletetionListner {
        C09873() {
        }

        public final void onCompletion$4ce696ce() {
            OutgoingGroupMsg.this.btnPlay.setText(2131493259);
            OutgoingGroupMsg.this.btnPlay.setBackgroundResource(2130837650);
        }
    }

    /* renamed from: com.shamchat.adapters.OutgoingGroupMsg.4 */
    class C09884 implements OnProgressUpdateListener {
        C09884() {
        }

        public final void onProgessUpdate(int progress) {
            System.out.println(progress);
        }
    }

    /* renamed from: com.shamchat.adapters.OutgoingGroupMsg.5 */
    class C09895 implements DialogInterface.OnClickListener {
        C09895() {
        }

        @SuppressLint({"NewApi"})
        @TargetApi(11)
        public final void onClick(DialogInterface dialog, int which) {
            if ((OutgoingGroupMsg.this.chatMessage.uploadedFileUrl == null || OutgoingGroupMsg.this.chatMessage.uploadedFileUrl.length() == 0) && which == 0 && OutgoingGroupMsg.this.chatMessage.messageContentType != MessageContentType.TEXT) {
                which = 1;
            }
            if (which == 0) {
                Intent intent = new Intent(OutgoingGroupMsg.this.context, ShareIntentChatActivity.class);
                if (OutgoingGroupMsg.this.chatMessage.messageContentType == MessageContentType.TEXT) {
                    intent.putExtra("android.intent.extra.TEXT", OutgoingGroupMsg.this.chatMessage.textMessage);
                    intent.setType("text/plain");
                    intent.setAction("android.intent.action.SEND");
                } else {
                    Log.i("intent", "OutGoingGroupMSG file url:  " + OutgoingGroupMsg.this.chatMessage.fileUrl);
                    intent.putExtra("android.intent.extra.STREAM", Uri.parse(OutgoingGroupMsg.this.chatMessage.fileUrl));
                    if (OutgoingGroupMsg.this.chatMessage.messageContentType == MessageContentType.IMAGE) {
                        intent.setType("image/jpg");
                    } else if (OutgoingGroupMsg.this.chatMessage.messageContentType == MessageContentType.VIDEO) {
                        intent.setType("video/mp4");
                    } else if (OutgoingGroupMsg.this.chatMessage.messageContentType == MessageContentType.VOICE_RECORD) {
                        intent.setType("audio/mp3");
                    } else {
                        return;
                    }
                    intent.setAction("android.intent.action.SEND");
                }
                OutgoingGroupMsg.this.context.startActivity(intent);
            }
            if (which == 1) {
                OutgoingGroupMsg.this.jobManager.addJobInBackground(new DeleteChatMessageDBLoadJob(OutgoingGroupMsg.this.chatMessage.packetId));
            }
            if (which != 2) {
                return;
            }
            if (VERSION.SDK_INT < 11) {
                ((ClipboardManager) OutgoingGroupMsg.this.context.getSystemService("clipboard")).setText(OutgoingGroupMsg.this.chatMessage.textMessage);
            } else {
                ((android.content.ClipboardManager) OutgoingGroupMsg.this.context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("Copied Text", OutgoingGroupMsg.this.chatMessage.textMessage));
            }
        }
    }

    /* renamed from: com.shamchat.adapters.OutgoingGroupMsg.6 */
    class C09906 implements Runnable {
        final /* synthetic */ Bitmap val$bm;
        final /* synthetic */ ImageView val$imageView;

        C09906(ImageView imageView, Bitmap bitmap) {
            this.val$imageView = imageView;
            this.val$bm = bitmap;
        }

        public final void run() {
            this.val$imageView.setImageBitmap(this.val$bm);
        }
    }

    /* renamed from: com.shamchat.adapters.OutgoingGroupMsg.7 */
    class C09917 implements Runnable {
        final /* synthetic */ String val$imageUrl;
        final /* synthetic */ ImageView val$imageView;

        C09917(String str, ImageView imageView) {
            this.val$imageUrl = str;
            this.val$imageView = imageView;
        }

        public final void run() {
            Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(Uri.parse(this.val$imageUrl)).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).tag("groupChatListViewImages").into(this.val$imageView, null);
        }
    }

    /* renamed from: com.shamchat.adapters.OutgoingGroupMsg.8 */
    class C09928 implements Runnable {
        final /* synthetic */ ImageView val$imageView;
        final /* synthetic */ Uri val$uri;

        C09928(Uri uri, ImageView imageView) {
            this.val$uri = uri;
            this.val$imageView = imageView;
        }

        public final void run() {
            Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(this.val$uri).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).tag("groupChatListViewImages").into(this.val$imageView, null);
        }
    }

    /* renamed from: com.shamchat.adapters.OutgoingGroupMsg.9 */
    class C09939 implements Runnable {
        final /* synthetic */ ImageView val$imageView;

        C09939(ImageView imageView) {
            this.val$imageView = imageView;
        }

        public final void run() {
            this.val$imageView.setImageResource(2130838111);
        }
    }

    public class ViewHolder {
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
        ProgressBar progressChatLoading;
        TextView txtChatContent;
        TextView txtDesc;
        TextView txtMessageTime;
        TextView txtProgress;
        ImageButton txtRetry;
        TextView txtSeen;
        RelativeLayout uploadingProgLayout;
        ProgressBar uploadingProgress;

        public ViewHolder(RelativeLayout latoutChatMessage, LinearLayout layoutTextContent, TextView txtChatContent, RelativeLayout layoutChatImage, ImageView imgChat, ImageView isDelivered, ImageView imgProfile, TextView txtSeen, TextView txtDesc, Button btnPlay, TextView txtMessageTime, ProgressBar uploadingProgress, ImageView playIcon, ProgressBar progressBarVoice, TextView txtProgress, RelativeLayout uploadingProgLayout, ImageView imgRetry, RelativeLayout mainLayout, ImageView imgMask, ImageView imgSticker, ImageButton txtRetry, ProgressBar progressChatLoading, FrameLayout imgChatWrapper) {
            this.latoutChatMessage = latoutChatMessage;
            this.layoutTextContent = layoutTextContent;
            this.txtChatContent = txtChatContent;
            this.layoutChatImage = layoutChatImage;
            this.imgChat = imgChat;
            this.isDelivered = isDelivered;
            this.imgProfile = imgProfile;
            this.txtSeen = txtSeen;
            this.txtRetry = txtRetry;
            this.imgRetry = imgRetry;
            this.txtDesc = txtDesc;
            this.btnPlay = btnPlay;
            this.playIcon = playIcon;
            this.txtMessageTime = txtMessageTime;
            this.uploadingProgress = uploadingProgress;
            this.progressBarVoice = progressBarVoice;
            this.mainLayout = mainLayout;
            this.txtProgress = txtProgress;
            this.uploadingProgLayout = uploadingProgLayout;
            this.imgSticker = imgSticker;
            this.imgMask = imgMask;
            this.progressChatLoading = progressChatLoading;
            this.imgChatWrapper = imgChatWrapper;
        }
    }

    public OutgoingGroupMsg(LayoutInflater inflater, ChatMessage message, Context ctx, String myUserId, ChatInitialForGroupChatActivity chatInitialForGroupChatActivity) {
        this.chatMessage = message;
        this.layoutInflater = inflater;
        this.context = ctx;
        this.myUserId = myUserId;
        this.chatInitialForGroupChatActivity = chatInitialForGroupChatActivity;
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
    }

    public final View getView(View convertView) {
        String str;
        if (convertView == null) {
            View viewGroup = (ViewGroup) this.layoutInflater.inflate(2130903115, null);
            TextView txtDesc = (TextView) viewGroup.findViewById(2131362136);
            Button btnPlay = (Button) viewGroup.findViewById(2131362129);
            ImageView playIcon = (ImageView) viewGroup.findViewById(2131362137);
            TextView txtMessageTime = (TextView) viewGroup.findViewById(2131362143);
            ProgressBar uploadingProgress = (ProgressBar) viewGroup.findViewById(2131362152);
            ProgressBar progressBarVoice = (ProgressBar) viewGroup.findViewById(2131362150);
            ImageView imgRetry = (ImageView) viewGroup.findViewById(2131362142);
            this.viewHolder = new ViewHolder((RelativeLayout) viewGroup.findViewById(2131362126), (LinearLayout) viewGroup.findViewById(2131362127), (TextView) viewGroup.findViewById(2131362130), (RelativeLayout) viewGroup.findViewById(2131362131), (ImageView) viewGroup.findViewById(2131362134), (ImageView) viewGroup.findViewById(2131362153), (ImageView) viewGroup.findViewById(2131361842), (TextView) viewGroup.findViewById(2131362154), txtDesc, btnPlay, txtMessageTime, uploadingProgress, playIcon, progressBarVoice, (TextView) viewGroup.findViewById(2131362122), (RelativeLayout) viewGroup.findViewById(2131362151), imgRetry, (RelativeLayout) viewGroup.findViewById(2131362146), (ImageView) viewGroup.findViewById(2131362133), (ImageView) viewGroup.findViewById(2131362135), (ImageButton) viewGroup.findViewById(2131362155), (ProgressBar) viewGroup.findViewById(2131361933), (FrameLayout) viewGroup.findViewById(2131362132));
            viewGroup.setTag(this.viewHolder);
            convertView = viewGroup;
        } else {
            this.viewHolder = (ViewHolder) convertView.getTag();
        }
        Log.v("OutgoingGroupMsg messages:", this.chatMessage.textMessage);
        String str2 = this.chatMessage.textMessage;
        this.viewHolder.imgChat.setImageDrawable(null);
        this.viewHolder.imgChatWrapper.setVisibility(0);
        this.viewHolder.playIcon.setVisibility(8);
        this.viewHolder.uploadingProgLayout.setVisibility(0);
        this.viewHolder.progressBarVoice.setVisibility(0);
        this.viewHolder.imgSticker.setVisibility(8);
        this.viewHolder.txtChatContent.setOnLongClickListener(this);
        this.viewHolder.imgChat.setOnLongClickListener(this);
        this.viewHolder.btnPlay.setOnLongClickListener(this);
        this.viewHolder.latoutChatMessage.setOnLongClickListener(this);
        this.viewHolder.layoutTextContent.setVisibility(0);
        this.viewHolder.layoutChatImage.setVisibility(8);
        this.viewHolder.imgChat.setVisibility(0);
        this.viewHolder.imgMask.setVisibility(0);
        if (this.chatMessage.user != null) {
            Bitmap bitmap = (Bitmap) SHAMChatApplication.USER_IMAGES.get(this.chatMessage.user.userId);
            String myProfileImageUrl = this.chatMessage.user.profileImageUrl;
            if (bitmap != null) {
                System.out.println("Image in the map");
                this.viewHolder.imgProfile.setImageBitmap(bitmap);
            } else {
                if (myProfileImageUrl != null) {
                    if (myProfileImageUrl.contains("http://")) {
                        Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(Uri.parse(myProfileImageUrl)).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).tag("groupChatListViewImages").into(this.viewHolder.imgProfile, null);
                        Utils.handleProfileImage(this.context, this.chatMessage.user.userId, myProfileImageUrl);
                    }
                }
                this.viewHolder.imgProfile.setImageResource(2130837944);
            }
        } else {
            this.viewHolder.imgProfile.setImageResource(2130837944);
        }
        MessageStatusType messageStatusType = this.chatMessage.messageStatus;
        this.viewHolder.isDelivered.setVisibility(8);
        this.viewHolder.txtRetry.setVisibility(8);
        switch (AnonymousClass10.$SwitchMap$com$shamchat$models$ChatMessage$MessageStatusType[messageStatusType.ordinal()]) {
            case Logger.SEVERE /*1*/:
                this.viewHolder.txtSeen.setVisibility(8);
                break;
            case Logger.WARNING /*2*/:
                this.viewHolder.txtSeen.setText(2131493358);
                this.viewHolder.txtSeen.setVisibility(0);
                break;
            case Logger.INFO /*3*/:
                this.viewHolder.txtSeen.setText(2131493359);
                this.viewHolder.txtSeen.setVisibility(0);
                break;
            case Logger.CONFIG /*4*/:
                this.viewHolder.txtSeen.setVisibility(8);
                this.viewHolder.isDelivered.setVisibility(0);
                this.viewHolder.uploadingProgLayout.setVisibility(8);
                this.viewHolder.progressBarVoice.setVisibility(8);
                break;
            case Logger.FINE /*5*/:
                this.viewHolder.txtSeen.setVisibility(0);
                this.viewHolder.txtSeen.setText(2131493354);
                this.viewHolder.isDelivered.setVisibility(0);
                break;
            case Logger.FINER /*6*/:
                this.viewHolder.txtSeen.setVisibility(0);
                this.viewHolder.txtSeen.setText(2131493123);
                this.viewHolder.txtRetry.setVisibility(0);
                this.viewHolder.txtRetry.bringToFront();
                this.viewHolder.isDelivered.setVisibility(8);
                break;
        }
        this.viewHolder.txtRetry.setOnClickListener(new C09851());
        if (this.chatMessage.textMessage == null || this.chatMessage.textMessage.length() <= 0) {
            if (this.chatMessage.messageContentType != MessageContentType.TEXT && this.chatMessage.messageContentType != MessageContentType.STICKER && this.chatMessage.uploadedPercentage <= 100 && this.chatMessage.uploadedFileUrl == null) {
                this.viewHolder.txtSeen.setVisibility(8);
            }
        } else if (this.chatMessage.messageStatus != MessageStatusType.DELIVERED) {
            if (this.chatMessage.messageContentType == MessageContentType.TEXT || this.chatMessage.messageContentType == MessageContentType.STICKER) {
                this.viewHolder.uploadingProgLayout.setVisibility(8);
                this.viewHolder.progressBarVoice.setVisibility(8);
            } else {
                if (this.chatMessage.uploadedPercentage == 100 && this.chatMessage.messageStatus != MessageStatusType.SEEN) {
                    this.viewHolder.txtSeen.setText(2131493359);
                    this.viewHolder.txtSeen.setVisibility(0);
                }
                if (this.chatMessage.textMessage != null && this.chatMessage.textMessage.equalsIgnoreCase("failed")) {
                    this.viewHolder.txtSeen.setText(2131493123);
                    this.viewHolder.txtSeen.setVisibility(0);
                }
            }
        }
        if (this.chatMessage.uploadedPercentage == 100) {
            this.viewHolder.uploadingProgLayout.setVisibility(8);
            this.viewHolder.progressBarVoice.setVisibility(8);
        }
        if (this.chatMessage.messageDateTime != null && this.chatMessage.messageDateTime.length() > 0) {
            this.viewHolder.txtMessageTime.setText(Utils.formatStringDate(this.chatMessage.messageDateTime, "HH:mm"));
        }
        this.viewHolder.txtProgress.bringToFront();
        if (this.chatMessage.uploadedPercentage != 9999) {
            String str3 = "%";
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
            this.viewHolder.imgRetry.setOnClickListener(new C09862());
        }
        Spannable smiledText;
        switch (AnonymousClass10.f21x39e42954[this.chatMessage.messageContentType.ordinal()]) {
            case Logger.SEVERE /*1*/:
            case Logger.INFO /*3*/:
                break;
            case Logger.WARNING /*2*/:
                this.viewHolder.layoutTextContent.setVisibility(8);
                this.viewHolder.layoutChatImage.setVisibility(0);
                str = this.chatMessage.fileUrl;
                System.out.println("File path " + str);
                str2 = this.chatMessage.packetId;
                handleImage$4ef3acf9(this.viewHolder.imgChat);
                this.viewHolder.imgChat.setOnClickListener(this);
                break;
            case Logger.CONFIG /*4*/:
                this.viewHolder.layoutTextContent.setVisibility(8);
                this.viewHolder.layoutChatImage.setVisibility(0);
                str2 = this.chatMessage.packetId;
                handleImage$4ef3acf9(this.viewHolder.imgChat);
                this.viewHolder.imgChat.setOnClickListener(this);
                break;
            case Logger.FINE /*5*/:
                this.viewHolder.layoutChatImage.setVisibility(8);
                this.viewHolder.layoutTextContent.setVisibility(0);
                this.viewHolder.txtChatContent.setVisibility(0);
                this.viewHolder.btnPlay.setVisibility(8);
                this.viewHolder.txtSeen.setVisibility(8);
                smiledText = Emoticons.getSmiledText(this.context, this.chatMessage.textMessage);
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
                Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(Uri.parse(this.chatMessage.textMessage)).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).tag("groupChatListViewImages").into(this.viewHolder.imgSticker, null);
                break;
            case Logger.FINEST /*7*/:
                this.viewHolder.layoutChatImage.setVisibility(8);
                this.viewHolder.layoutTextContent.setVisibility(0);
                this.viewHolder.txtChatContent.setVisibility(0);
                this.viewHolder.btnPlay.setVisibility(8);
                smiledText = Emoticons.getSmiledText(this.context, this.chatMessage.textMessage);
                this.viewHolder.txtChatContent.setText(spannable, BufferType.SPANNABLE);
                break;
            case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                this.viewHolder.layoutChatImage.setVisibility(8);
                this.viewHolder.layoutTextContent.setVisibility(0);
                this.viewHolder.txtChatContent.setVisibility(8);
                this.viewHolder.btnPlay.setVisibility(0);
                this.viewHolder.btnPlay.setOnClickListener(this);
                this.viewHolder.txtChatContent.setText(this.chatMessage.textMessage);
                break;
            case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                this.viewHolder.playIcon.setVisibility(0);
                this.viewHolder.layoutTextContent.setVisibility(8);
                this.viewHolder.layoutChatImage.setVisibility(0);
                str2 = this.chatMessage.packetId;
                handleImage$4ef3acf9(this.viewHolder.imgChat);
                this.viewHolder.imgChat.setOnClickListener(this);
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

    public final int getViewType() {
        return MyMessageType.OUTGOING_MSG.ordinal();
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
                        instance.listener = new C09884();
                        instance.playCompletionlistner = new C09873();
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
                switch (AnonymousClass10.f21x39e42954[this.chatMessage.messageContentType.ordinal()]) {
                    case Logger.WARNING /*2*/:
                        String localFileUrl = this.chatMessage.fileUrl;
                        if (localFileUrl == null || localFileUrl.length() <= 0) {
                            intent = new Intent(v.getContext(), DownloadedImageFilePreview.class);
                            intent.putExtra("url", this.chatMessage.textMessage);
                            intent.putExtra(MqttServiceConstants.MESSAGE_ID, String.valueOf(this.chatMessage.messageId));
                            v.getContext().startActivity(intent);
                            return;
                        }
                        intent = new Intent(v.getContext(), DownloadedImageFilePreview.class);
                        intent.putExtra("url", this.chatMessage.textMessage);
                        intent.putExtra("packet_id", String.valueOf(this.chatMessage.packetId));
                        intent.putExtra(MqttServiceConstants.MESSAGE_ID, String.valueOf(this.chatMessage.messageId));
                        intent.putExtra("ThreadId", String.valueOf(this.chatMessage.threadId));
                        v.getContext().startActivity(intent);
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
        builderSingle.setAdapter(arrayAdapter, new C09895());
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
        String mainImageUrl;
        String imageUrl;
        if (this.chatMessage.fileUrl == null || this.chatMessage.fileUrl.contains(".mp4")) {
            mainImageUrl = this.chatMessage.uploadedFileUrl;
            if (mainImageUrl != null && mainImageUrl.length() > 0) {
                imageUrl = mainImageUrl.replaceAll("upload", "upload/thumb");
                if (imageUrl.indexOf(".mp4") != -1) {
                    imageUrl = imageUrl.replaceAll(".mp4", ".jpg");
                }
                System.out.println("Video thum imageUrl " + imageUrl);
                if (imageUrl != null && imageUrl.contains("http://")) {
                    System.out.println("Video thum imageUrl http");
                    Uri uri = Uri.parse(imageUrl);
                    if (this.chatInitialForGroupChatActivity != null) {
                        this.chatInitialForGroupChatActivity.runOnUiThread(new C09928(uri, imageView));
                        return;
                    }
                    return;
                }
                return;
            } else if (this.chatInitialForGroupChatActivity != null) {
                this.chatInitialForGroupChatActivity.runOnUiThread(new C09939(imageView));
                return;
            } else {
                return;
            }
        }
        File f = new File(this.chatMessage.fileUrl);
        if (f.exists()) {
            System.out.println("file exists" + f.getAbsolutePath());
            Bitmap bm = Utils.decodeSampledBitmapFromResource$295fb07d(f.getAbsolutePath());
            if (this.chatInitialForGroupChatActivity != null) {
                this.chatInitialForGroupChatActivity.runOnUiThread(new C09906(imageView, bm));
                return;
            }
            return;
        }
        mainImageUrl = this.chatMessage.uploadedFileUrl;
        if (mainImageUrl == null || mainImageUrl.isEmpty()) {
            mainImageUrl = this.chatMessage.textMessage;
            if (!mainImageUrl.startsWith("http://") || !mainImageUrl.startsWith("https://") || mainImageUrl == null || mainImageUrl.isEmpty()) {
                return;
            }
        }
        imageUrl = mainImageUrl.replaceAll("upload", "upload/thumb");
        if (imageUrl != null && imageUrl.contains("http://") && this.chatInitialForGroupChatActivity != null) {
            this.chatInitialForGroupChatActivity.runOnUiThread(new C09917(imageUrl, imageView));
        }
    }
}

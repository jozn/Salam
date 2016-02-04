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
import com.shamchat.activity.ChatInitialForGroupChatActivity;
import com.shamchat.activity.DownloadedImageFilePreview;
import com.shamchat.activity.DownloadedVideoFilePreview;
import com.shamchat.activity.LocalVideoFilePreview;
import com.shamchat.activity.ShamChatMapPreview;
import com.shamchat.activity.ShareIntentChatActivity;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import com.shamchat.jobs.DeleteChatMessageDBLoadJob;
import com.shamchat.jobs.ImageDownloadJob;
import com.shamchat.jobs.VideoDownloadJob;
import com.shamchat.models.ChatMessage;
import com.shamchat.roundedimage.RoundedImageView;
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

public final class IncomingGroupMsg implements OnClickListener, OnLongClickListener, Row {
    Button btnPlay;
    private ChatInitialForGroupChatActivity chatInitialForGroupChatActivity;
    public ChatMessage chatMessage;
    Context context;
    public ViewHolder holder;
    private JobManager jobManager;
    private int lastPosition;
    LayoutInflater layoutInflater;
    private int position;
    final Set<Target> protectedFromGarbageCollectorTargets;

    /* renamed from: com.shamchat.adapters.IncomingGroupMsg.1 */
    class C09641 implements OnClickListener {
        C09641() {
        }

        public final void onClick(View v) {
            IncomingGroupMsg.this.holder.downloadStart.setVisibility(8);
            IncomingGroupMsg.this.holder.downloadingProgress.setVisibility(0);
            if (IncomingGroupMsg.this.chatMessage.messageContentType == MessageContentType.IMAGE) {
                IncomingGroupMsg.this.jobManager.addJobInBackground(new ImageDownloadJob(IncomingGroupMsg.this.chatMessage.uploadedFileUrl, IncomingGroupMsg.this.chatMessage.packetId));
            } else if (IncomingGroupMsg.this.chatMessage.messageContentType == MessageContentType.VIDEO) {
                IncomingGroupMsg.this.jobManager.addJobInBackground(new VideoDownloadJob(IncomingGroupMsg.this.chatMessage.uploadedFileUrl, IncomingGroupMsg.this.chatMessage.packetId));
            }
        }
    }

    /* renamed from: com.shamchat.adapters.IncomingGroupMsg.2 */
    class C09652 implements OnPlayingCompletetionListner {
        final /* synthetic */ ProgressBar val$progressBar;

        C09652(ProgressBar progressBar) {
            this.val$progressBar = progressBar;
        }

        public final void onCompletion$4ce696ce() {
            IncomingGroupMsg.this.btnPlay.setText(2131493259);
            this.val$progressBar.setVisibility(8);
            IncomingGroupMsg.this.btnPlay.setBackgroundResource(2130837650);
        }
    }

    /* renamed from: com.shamchat.adapters.IncomingGroupMsg.3 */
    class C09663 implements OnProgressUpdateListener {
        final /* synthetic */ ProgressBar val$progressBar;

        C09663(ProgressBar progressBar) {
            this.val$progressBar = progressBar;
        }

        public final void onProgessUpdate(int progress) {
            System.out.println("AUDIO DOWNLOADING: " + progress);
            if (progress == 100) {
                this.val$progressBar.setVisibility(8);
            }
        }
    }

    /* renamed from: com.shamchat.adapters.IncomingGroupMsg.4 */
    class C09674 implements DialogInterface.OnClickListener {
        C09674() {
        }

        @SuppressLint({"NewApi"})
        @TargetApi(11)
        public final void onClick(DialogInterface dialog, int which) {
            if ((IncomingGroupMsg.this.chatMessage.fileUrl == null || IncomingGroupMsg.this.chatMessage.fileUrl.length() == 0) && which == 0 && IncomingGroupMsg.this.chatMessage.messageContentType != MessageContentType.TEXT) {
                which = 1;
            }
            if (which == 0) {
                Intent intent = new Intent(IncomingGroupMsg.this.context, ShareIntentChatActivity.class);
                if (IncomingGroupMsg.this.chatMessage.messageContentType == MessageContentType.TEXT) {
                    intent.putExtra("android.intent.extra.TEXT", IncomingGroupMsg.this.chatMessage.textMessage);
                    intent.setType("text/plain");
                    intent.setAction("android.intent.action.SEND");
                } else {
                    Log.i("intent", "OutGoingGroupMSG file url:  " + IncomingGroupMsg.this.chatMessage.fileUrl);
                    intent.putExtra("android.intent.extra.STREAM", Uri.parse(IncomingGroupMsg.this.chatMessage.fileUrl));
                    if (IncomingGroupMsg.this.chatMessage.messageContentType == MessageContentType.IMAGE) {
                        intent.setType("image/jpg");
                    } else if (IncomingGroupMsg.this.chatMessage.messageContentType == MessageContentType.VIDEO) {
                        intent.setType("video/mp4");
                    } else if (IncomingGroupMsg.this.chatMessage.messageContentType == MessageContentType.VOICE_RECORD) {
                        intent.setType("audio/mp3");
                    } else {
                        return;
                    }
                    intent.setAction("android.intent.action.SEND");
                }
                IncomingGroupMsg.this.context.startActivity(intent);
            }
            if (which == 1) {
                SHAMChatApplication.getInstance().jobManager.addJobInBackground(new DeleteChatMessageDBLoadJob(IncomingGroupMsg.this.chatMessage.packetId));
            }
            if (which != 2) {
                return;
            }
            if (VERSION.SDK_INT < 11) {
                ((ClipboardManager) IncomingGroupMsg.this.context.getSystemService("clipboard")).setText(IncomingGroupMsg.this.chatMessage.textMessage);
            } else {
                ((android.content.ClipboardManager) IncomingGroupMsg.this.context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("Copied Text", IncomingGroupMsg.this.chatMessage.textMessage));
            }
        }
    }

    /* renamed from: com.shamchat.adapters.IncomingGroupMsg.5 */
    class C09695 implements Target {
        final /* synthetic */ ImageView val$imageView;

        /* renamed from: com.shamchat.adapters.IncomingGroupMsg.5.1 */
        class C09681 implements Runnable {
            final /* synthetic */ Bitmap val$bitmap;

            C09681(Bitmap bitmap) {
                this.val$bitmap = bitmap;
            }

            public final void run() {
                Bitmap bitmap2 = this.val$bitmap;
                if (IncomingGroupMsg.this.chatMessage.messageContentType == MessageContentType.IMAGE) {
                    bitmap2 = Utils.fastblur$75eed7c6(bitmap2);
                }
                C09695.this.val$imageView.setImageBitmap(bitmap2);
                IncomingGroupMsg.this.protectedFromGarbageCollectorTargets.remove(this);
            }
        }

        C09695(ImageView imageView) {
            this.val$imageView = imageView;
        }

        public final void onBitmapLoaded$dc1124d(Bitmap bitmap) {
            System.out.println("Bit map loaded");
            if (IncomingGroupMsg.this.chatInitialForGroupChatActivity != null) {
                IncomingGroupMsg.this.chatInitialForGroupChatActivity.runOnUiThread(new C09681(bitmap));
            }
        }

        public final void onBitmapFailed$130e17e7() {
            IncomingGroupMsg.this.protectedFromGarbageCollectorTargets.remove(this);
        }
    }

    /* renamed from: com.shamchat.adapters.IncomingGroupMsg.6 */
    static /* synthetic */ class C09706 {
        static final /* synthetic */ int[] f19x39e42954;

        static {
            f19x39e42954 = new int[MessageContentType.values().length];
            try {
                f19x39e42954[MessageContentType.FAVORITE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f19x39e42954[MessageContentType.IMAGE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f19x39e42954[MessageContentType.INCOMING_CALL.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f19x39e42954[MessageContentType.LOCATION.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f19x39e42954[MessageContentType.OUTGOING_CALL.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f19x39e42954[MessageContentType.STICKER.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                f19x39e42954[MessageContentType.TEXT.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                f19x39e42954[MessageContentType.VOICE_RECORD.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                f19x39e42954[MessageContentType.VIDEO.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                f19x39e42954[MessageContentType.GROUP_INFO.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
        }
    }

    public class ViewHolder {
        public Button btnPlay;
        public ImageView downloadStart;
        public ProgressBar downloadingProgress;
        public ImageView imgChat;
        FrameLayout imgChatWrapper;
        ImageView imgMask;
        RoundedImageView imgProfile;
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
        TextView txtGroupImageSernder;
        TextView txtIncomingCall;
        TextView txtMessageTime;

        public ViewHolder(RelativeLayout latoutChatMessage, LinearLayout layoutTextContent, TextView txtChatContent, RelativeLayout layoutChatImage, ImageView imgChat, RoundedImageView imgProfile, TextView txtDesc, Button btnPlay, TextView txtMessageTime, ImageView playIcon, TextView txtFileSize, ProgressBar pBar, RelativeLayout mainLayout, TextView txtIncomingCall, TextView txtGroupImageSernder, ImageView imgMask, ImageView imgSticker, ProgressBar downloadingProgress, ImageView imgRetry, ImageView downloadStart, FrameLayout imgChatWrapper) {
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
            this.txtGroupImageSernder = txtGroupImageSernder;
            this.imgMask = imgMask;
            this.imgSticker = imgSticker;
            this.downloadingProgress = downloadingProgress;
            this.imgRetry = imgRetry;
            this.downloadStart = downloadStart;
            this.imgChatWrapper = imgChatWrapper;
        }
    }

    public IncomingGroupMsg(LayoutInflater inflater, ChatMessage message, Context ctx, ChatInitialForGroupChatActivity chatInitialForGroupChatActivity, int position) {
        this.lastPosition = -1;
        this.protectedFromGarbageCollectorTargets = new HashSet();
        this.chatMessage = message;
        this.layoutInflater = inflater;
        this.context = ctx;
        this.position = position;
        this.chatInitialForGroupChatActivity = chatInitialForGroupChatActivity;
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
    }

    public final View getView(View convertView) {
        String str;
        if (convertView == null) {
            View viewGroup = (ViewGroup) this.layoutInflater.inflate(2130903114, null);
            Button btnPlay = (Button) viewGroup.findViewById(2131362129);
            ImageView imgRetry = (ImageView) viewGroup.findViewById(2131362142);
            this.holder = new ViewHolder((RelativeLayout) viewGroup.findViewById(2131362126), (LinearLayout) viewGroup.findViewById(2131362127), (TextView) viewGroup.findViewById(2131362130), (RelativeLayout) viewGroup.findViewById(2131362131), (ImageView) viewGroup.findViewById(2131362134), (RoundedImageView) viewGroup.findViewById(2131361842), (TextView) viewGroup.findViewById(2131362136), btnPlay, (TextView) viewGroup.findViewById(2131362143), (ImageView) viewGroup.findViewById(2131362137), (TextView) viewGroup.findViewById(2131362138), (ProgressBar) viewGroup.findViewById(2131362017), (RelativeLayout) viewGroup.findViewById(2131362123), (TextView) viewGroup.findViewById(2131362145), (TextView) viewGroup.findViewById(2131362144), (ImageView) viewGroup.findViewById(2131362133), (ImageView) viewGroup.findViewById(2131362135), (ProgressBar) viewGroup.findViewById(2131362141), imgRetry, (ImageView) viewGroup.findViewById(2131362140), (FrameLayout) viewGroup.findViewById(2131362132));
            viewGroup.setTag(this.holder);
            convertView = viewGroup;
        } else {
            this.holder = (ViewHolder) convertView.getTag();
        }
        this.holder.imgChat.setImageDrawable(null);
        this.holder.imgChatWrapper.setVisibility(0);
        this.holder.mainLayout.setVisibility(0);
        this.holder.txtIncomingCall.setVisibility(8);
        this.holder.playIcon.setVisibility(8);
        this.holder.txtFileSize.setVisibility(8);
        this.holder.pBar.setVisibility(8);
        this.holder.imgSticker.setVisibility(8);
        this.holder.txtGroupImageSernder.setVisibility(0);
        this.holder.txtFileSize.setVisibility(8);
        this.holder.layoutTextContent.setVisibility(0);
        this.holder.layoutChatImage.setVisibility(8);
        this.holder.imgChat.setVisibility(0);
        this.holder.downloadStart.setVisibility(8);
        this.holder.downloadingProgress.setVisibility(8);
        this.holder.imgMask.setVisibility(0);
        this.holder.txtChatContent.setOnLongClickListener(this);
        this.holder.imgChat.setOnLongClickListener(this);
        this.holder.btnPlay.setOnLongClickListener(this);
        this.holder.latoutChatMessage.setOnLongClickListener(this);
        if (this.chatMessage.user != null) {
            String myProfileImageUrl = this.chatMessage.user.profileImageUrl;
            if (myProfileImageUrl != null) {
                if (myProfileImageUrl.contains("http://")) {
                    Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(Uri.parse(myProfileImageUrl)).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).tag("groupChatListViewImages").into(this.holder.imgProfile, null);
                    Utils.handleProfileImage(this.context, this.chatMessage.user.userId, myProfileImageUrl);
                }
            }
            this.holder.imgProfile.setImageResource(2130837945);
        } else {
            this.holder.imgProfile.setImageResource(2130837945);
        }
        if (this.chatMessage.messageDateTime != null && this.chatMessage.messageDateTime.length() > 0) {
            this.holder.txtMessageTime.setText(Utils.formatStringDate(this.chatMessage.messageDateTime, "HH:mm"));
        }
        String str2;
        Spannable smiledText;
        switch (C09706.f19x39e42954[this.chatMessage.messageContentType.ordinal()]) {
            case Logger.SEVERE /*1*/:
                str = this.chatMessage.textMessage;
                System.out.println("INCOMING FAVORITE " + str);
                this.holder.txtFileSize.setVisibility(4);
                break;
            case Logger.WARNING /*2*/:
                str = this.chatMessage.textMessage;
                System.out.println("INCOMING IMAGE " + str);
                this.holder.layoutTextContent.setVisibility(8);
                this.holder.layoutChatImage.setVisibility(0);
                this.holder.imgChat.setOnClickListener(this);
                this.holder.txtGroupImageSernder.setText(this.chatMessage.textMessage);
                if (this.chatMessage.fileUrl == null) {
                    this.holder.downloadStart.setVisibility(0);
                }
                str2 = this.chatMessage.packetId;
                handleImage$4ef3acf9(this.holder.imgChat);
                break;
            case Logger.INFO /*3*/:
                str = this.chatMessage.textMessage;
                System.out.println("INCOMING CALL " + str);
                this.holder.mainLayout.setVisibility(8);
                this.holder.layoutChatImage.setVisibility(8);
                this.holder.layoutTextContent.setVisibility(0);
                smiledText = Emoticons.getSmiledText(this.context, this.chatMessage.textMessage);
                this.holder.txtChatContent.setText(spannable, BufferType.SPANNABLE);
                break;
            case Logger.CONFIG /*4*/:
                str = this.chatMessage.textMessage;
                System.out.println("INCOMING LOCATION " + str);
                this.holder.layoutTextContent.setVisibility(8);
                this.holder.layoutChatImage.setVisibility(0);
                this.holder.imgChat.setOnClickListener(this);
                this.holder.txtFileSize.setVisibility(4);
                this.holder.txtFileSize.setVisibility(4);
                this.holder.txtGroupImageSernder.setText(this.chatMessage.textMessage);
                str2 = this.chatMessage.packetId;
                handleImage$4ef3acf9(this.holder.imgChat);
                break;
            case Logger.FINE /*5*/:
                str = this.chatMessage.textMessage;
                System.out.println("INCOMING OUTGOING CALL " + str);
                this.holder.txtFileSize.setVisibility(4);
                break;
            case Logger.FINER /*6*/:
                this.holder.txtFileSize.setVisibility(4);
                this.holder.layoutTextContent.setVisibility(8);
                this.holder.layoutChatImage.setVisibility(0);
                this.holder.imgChat.setVisibility(8);
                this.holder.imgMask.setVisibility(8);
                this.holder.imgChatWrapper.setVisibility(8);
                this.holder.imgSticker.setVisibility(0);
                this.holder.txtGroupImageSernder.setText(this.chatMessage.textMessage);
                str2 = this.chatMessage.packetId;
                handleImage$4ef3acf9(this.holder.imgSticker);
                break;
            case Logger.FINEST /*7*/:
                str = this.chatMessage.textMessage;
                System.out.println("INCOMING TEXT " + str);
                this.holder.layoutChatImage.setVisibility(8);
                this.holder.txtChatContent.setVisibility(0);
                this.holder.layoutTextContent.setVisibility(0);
                this.holder.btnPlay.setVisibility(8);
                smiledText = Emoticons.getSmiledText(this.context, this.chatMessage.textMessage);
                this.holder.txtChatContent.setText(spannable, BufferType.SPANNABLE);
                this.holder.txtFileSize.setVisibility(4);
                this.holder.txtGroupImageSernder.setVisibility(8);
                break;
            case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                str = this.chatMessage.textMessage;
                System.out.println("INCOMING VOICE " + str);
                this.holder.layoutChatImage.setVisibility(8);
                this.holder.layoutTextContent.setVisibility(0);
                this.holder.txtChatContent.setVisibility(8);
                this.holder.btnPlay.setVisibility(0);
                this.holder.btnPlay.setTag(this.holder.pBar);
                this.holder.btnPlay.setOnClickListener(this);
                this.holder.txtChatContent.setText(this.chatMessage.textMessage);
                this.holder.txtGroupImageSernder.setText(this.chatMessage.textMessage);
                break;
            case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                str = this.chatMessage.textMessage;
                System.out.println("INCOMING VIDEO " + str);
                this.holder.playIcon.setVisibility(0);
                this.holder.layoutTextContent.setVisibility(8);
                this.holder.layoutChatImage.setVisibility(0);
                str2 = this.chatMessage.packetId;
                handleImage$4ef3acf9(this.holder.imgChat);
                this.holder.imgChat.setOnClickListener(this);
                this.holder.txtGroupImageSernder.setText(this.chatMessage.textMessage);
                break;
            case C0473R.styleable.SwitchButton_onColor /*10*/:
                str = this.chatMessage.textMessage;
                System.out.println("INCOMING GROUP INFO " + str);
                if (!this.chatMessage.textMessage.equals("!!msgRandomInvisible!!")) {
                    this.holder.mainLayout.setVisibility(8);
                    this.holder.layoutChatImage.setVisibility(8);
                    this.holder.layoutTextContent.setVisibility(0);
                    this.holder.txtIncomingCall.setVisibility(0);
                    this.holder.txtIncomingCall.setText(this.chatMessage.textMessage);
                    break;
                }
                this.holder.mainLayout.setVisibility(8);
                this.holder.layoutChatImage.setVisibility(8);
                this.holder.layoutTextContent.setVisibility(8);
                this.holder.txtIncomingCall.setVisibility(8);
                this.holder.txtIncomingCall.setVisibility(8);
                break;
            default:
                str = this.chatMessage.textMessage;
                System.out.println("INCOMING UNKNOWN " + str);
                this.holder.layoutTextContent.setVisibility(0);
                break;
        }
        this.holder.downloadStart.setOnClickListener(new C09641());
        this.holder.txtDesc.setText(this.chatMessage.description);
        String string = this.context.getResources().getString(2131493135);
        string = Utils.readableFileSize(this.chatMessage.fileSize);
        this.holder.txtFileSize.setText(str + " " + str);
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
                    instance.listener = new C09663(progressBar);
                    instance.playCompletionlistner = new C09652(progressBar);
                    instance.play(this.chatMessage.uploadedFileUrl);
                    return;
                }
                ShamMediaPlayer.getInstance().stop();
                this.btnPlay.setText(2131493259);
                progressBar.setVisibility(8);
                this.btnPlay.setBackgroundResource(2130837650);
            case 2131362134:
                Intent intent;
                switch (C09706.f19x39e42954[this.chatMessage.messageContentType.ordinal()]) {
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
        builderSingle.setAdapter(arrayAdapter, new C09674());
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
            Target target = new C09695(imageView);
            this.protectedFromGarbageCollectorTargets.add(target);
            Picasso.with(this.context).load(imageUrl).tag("groupChatListViewImages").into(target);
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

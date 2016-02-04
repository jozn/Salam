package com.shamchat.activity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.appcompat.BuildConfig;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.path.android.jobqueue.JobManager;
import com.shamchat.adapters.MyMessageType;
import com.shamchat.androidclient.ChatController;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import com.shamchat.androidclient.listeners.OnLocationListener;
import com.shamchat.jobs.FileUploadJob;
import com.shamchat.map.GPSTracker;
import com.shamchat.utils.PopUpUtil;
import com.shamchat.utils.URIExtractor;
import com.shamchat.utils.Utils;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class MainChatItemsFragment extends Fragment implements OnClickListener, OnLocationListener {
    private static final int REQUEST_CAMERA = 2;
    private static final int REQUEST_DESCRIPTION = 3;
    public static final int REQUEST_FAVOURITE = 70;
    private static final int REQUEST_LOCATION = 6;
    public static final int REQUEST_NEW_IMAGES = 25;
    private static final int REQUEST_VIDEO_CAPTURE = 5;
    private static final int REQUEST_VIDEO_GALLERY = 4;
    private RelativeLayout btnPhotoGallery;
    private RelativeLayout btnSendFavorite;
    private RelativeLayout btnSendLocation;
    private RelativeLayout btnTakePhoto;
    private RelativeLayout btnVideoCapture;
    private RelativeLayout btnVideoGallery;
    private Location currentLocation;
    private String fullFilePath;
    private GPSTracker gps;
    private boolean isGroupChat;
    private JobManager jobManager;
    private Dialog popUp;
    private String recipientId;
    private String senderId;
    private Uri videoFileUri;

    /* renamed from: com.shamchat.activity.MainChatItemsFragment.1 */
    class C07671 implements OnClickListener {
        C07671() {
        }

        public final void onClick(View v) {
            MainChatItemsFragment.this.popUp.dismiss();
        }
    }

    public MainChatItemsFragment() {
        this.isGroupChat = false;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(2130903144, container, false);
        this.btnTakePhoto = (RelativeLayout) inflate.findViewById(2131362235);
        this.btnPhotoGallery = (RelativeLayout) inflate.findViewById(2131362238);
        this.btnSendFavorite = (RelativeLayout) inflate.findViewById(2131362240);
        this.btnSendLocation = (RelativeLayout) inflate.findViewById(2131362242);
        this.btnVideoCapture = (RelativeLayout) inflate.findViewById(2131362244);
        this.btnVideoGallery = (RelativeLayout) inflate.findViewById(2131362246);
        this.btnTakePhoto.setOnClickListener(this);
        this.btnPhotoGallery.setOnClickListener(this);
        this.btnSendFavorite.setOnClickListener(this);
        this.btnSendLocation.setOnClickListener(this);
        this.btnVideoCapture.setOnClickListener(this);
        this.btnVideoGallery.setOnClickListener(this);
        this.senderId = getArguments().getString("message_sender");
        this.recipientId = getArguments().getString("message_recipient");
        this.isGroupChat = getArguments().getBoolean("is_group_chat");
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        this.gps = new GPSTracker(getActivity(), this);
        return inflate;
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void onClick(View v) {
        this.fullFilePath = BuildConfig.VERSION_NAME;
        switch (v.getId()) {
            case 2131362235:
                Intent camIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                camIntent.putExtra("output", setImageUri());
                startActivityForResult(camIntent, REQUEST_CAMERA);
            case 2131362238:
                startActivityForResult(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), REQUEST_NEW_IMAGES);
            case 2131362240:
                startActivityForResult(new Intent(getActivity(), FavouriteSendActivity.class), REQUEST_FAVOURITE);
            case 2131362242:
                if (!Utils.isInternetAvailable(getActivity())) {
                    PopUpUtil popUpUtil = new PopUpUtil();
                    this.popUp = PopUpUtil.getPopFailed$478dbc03(getActivity(), "No Internet Connection Found", new C07671());
                    this.popUp.show();
                } else if (Utils.checkPlayServices(getActivity())) {
                    ProgressBarLoadingDialog.getInstance().show(getFragmentManager(), BuildConfig.VERSION_NAME);
                    this.gps.getLocation();
                }
            case 2131362244:
                Intent intent = new Intent("android.media.action.VIDEO_CAPTURE");
                this.videoFileUri = getOutputMediaFileUri(REQUEST_VIDEO_CAPTURE);
                intent.putExtra("output", this.videoFileUri);
                intent.putExtra("android.intent.extra.videoQuality", 0);
                startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
            case 2131362246:
                Intent videoIntent = new Intent("android.intent.action.PICK", null);
                videoIntent.setType("video/*");
                startActivityForResult(videoIntent, REQUEST_VIDEO_GALLERY);
            default:
        }
    }

    public Uri setImageUri() {
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.fullFilePath = file.getAbsolutePath();
        return imgUri;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent(getActivity(), VideoPreviewAndComment.class);
        String description = BuildConfig.VERSION_NAME;
        if (resultCode == -1) {
            switch (requestCode) {
                case REQUEST_CAMERA /*2*/:
                    intent = new Intent(getActivity(), ImagePreviewAndCommentDialog.class);
                    if (this.fullFilePath == null && data.getData() != null) {
                        this.fullFilePath = URIExtractor.getPath(getActivity(), data.getData());
                    }
                    intent = intent;
                    intent.putExtra("fullFilePath", this.fullFilePath);
                    intent.putExtra("content_type", MessageContentType.IMAGE.ordinal());
                    startActivityForResult(intent, REQUEST_DESCRIPTION);
                    System.out.println("selected an image " + this.fullFilePath);
                case REQUEST_DESCRIPTION /*3*/:
                    int contentType;
                    if (data != null) {
                        if (data.hasExtra("description")) {
                            description = data.getStringExtra("description");
                            contentType = data.getIntExtra("type", 1);
                            if (contentType == MessageContentType.IMAGE.ordinal()) {
                                if (this.fullFilePath == null) {
                                    if (data.hasExtra("fullFilePath")) {
                                        this.fullFilePath = data.getStringExtra("fullFilePath");
                                    }
                                }
                                this.jobManager.addJobInBackground(new FileUploadJob(this.fullFilePath, this.senderId, this.recipientId, this.isGroupChat, MessageContentType.IMAGE.ordinal(), description, false, null, 0.0d, 0.0d));
                            } else if (contentType == MessageContentType.VIDEO.ordinal()) {
                                if (this.fullFilePath == null) {
                                    if (data.hasExtra("fullFilePath")) {
                                        this.fullFilePath = data.getStringExtra("fullFilePath");
                                    }
                                }
                                this.jobManager.addJobInBackground(new FileUploadJob(this.fullFilePath, this.senderId, this.recipientId, this.isGroupChat, MessageContentType.VIDEO.ordinal(), description, false, null, 0.0d, 0.0d));
                            }
                        }
                    }
                    description = BuildConfig.VERSION_NAME;
                    contentType = data.getIntExtra("type", 1);
                    if (contentType == MessageContentType.IMAGE.ordinal()) {
                        if (this.fullFilePath == null) {
                            if (data.hasExtra("fullFilePath")) {
                                this.fullFilePath = data.getStringExtra("fullFilePath");
                            }
                        }
                        this.jobManager.addJobInBackground(new FileUploadJob(this.fullFilePath, this.senderId, this.recipientId, this.isGroupChat, MessageContentType.IMAGE.ordinal(), description, false, null, 0.0d, 0.0d));
                    } else if (contentType == MessageContentType.VIDEO.ordinal()) {
                        if (this.fullFilePath == null) {
                            if (data.hasExtra("fullFilePath")) {
                                this.fullFilePath = data.getStringExtra("fullFilePath");
                            }
                        }
                        this.jobManager.addJobInBackground(new FileUploadJob(this.fullFilePath, this.senderId, this.recipientId, this.isGroupChat, MessageContentType.VIDEO.ordinal(), description, false, null, 0.0d, 0.0d));
                    }
                case REQUEST_VIDEO_GALLERY /*4*/:
                    System.out.println("SELETCED " + data.getData());
                    intent.setData(data.getData());
                    this.fullFilePath = URIExtractor.getPath(getActivity(), data.getData());
                    intent = intent;
                    intent.putExtra("fullFilePath", this.fullFilePath);
                    startActivityForResult(intent, REQUEST_DESCRIPTION);
                case REQUEST_VIDEO_CAPTURE /*5*/:
                    System.out.println("CAPTURED " + this.videoFileUri);
                    if (this.videoFileUri == null) {
                        this.videoFileUri = data.getData();
                    }
                    intent.setData(this.videoFileUri);
                    this.fullFilePath = URIExtractor.getPath(getActivity(), this.videoFileUri);
                    intent = intent;
                    intent.putExtra("fullFilePath", this.fullFilePath);
                    startActivityForResult(intent, REQUEST_DESCRIPTION);
                case REQUEST_LOCATION /*6*/:
                    data.getBundleExtra("description");
                    if (data != null) {
                        if (data.hasExtra("description")) {
                            description = data.getStringExtra("description");
                            this.jobManager.addJobInBackground(new FileUploadJob(this.fullFilePath, this.senderId, this.recipientId, this.isGroupChat, MessageContentType.LOCATION.ordinal(), description, false, null, this.currentLocation.getLongitude(), this.currentLocation.getLatitude()));
                        }
                    }
                    description = BuildConfig.VERSION_NAME;
                    this.jobManager.addJobInBackground(new FileUploadJob(this.fullFilePath, this.senderId, this.recipientId, this.isGroupChat, MessageContentType.LOCATION.ordinal(), description, false, null, this.currentLocation.getLongitude(), this.currentLocation.getLatitude()));
                case REQUEST_NEW_IMAGES /*25*/:
                    this.fullFilePath = getRealPathFromURI(data.getData());
                    intent = new Intent(getActivity(), ImagePreviewAndCommentDialog.class);
                    intent = intent;
                    intent.putExtra("fullFilePath", this.fullFilePath);
                    intent.putExtra("content_type", MessageContentType.IMAGE.ordinal());
                    startActivityForResult(intent, REQUEST_DESCRIPTION);
                    System.out.println("selected an image " + this.fullFilePath);
                case REQUEST_FAVOURITE /*70*/:
                    String favourite = data.getStringExtra(FavouriteSendActivity.EXTRA_FAVOURITE);
                    int messageType = data.getIntExtra(FavouriteSendActivity.EXTRA_FAVOURITE_TYPE, -1);
                    System.out.println(favourite);
                    if (messageType == -1 || messageType != 0) {
                        sendTextMessage(favourite);
                        return;
                    }
                    getActivity();
                    this.jobManager.addJobInBackground(new FileUploadJob(Utils.createFileFromBase64$2b27f8d0(favourite).getAbsolutePath(), this.senderId, this.recipientId, this.isGroupChat, MessageContentType.IMAGE.ordinal(), description, false, null, 0.0d, 0.0d));
                default:
            }
        } else if (resultCode == 0 && requestCode == REQUEST_VIDEO_CAPTURE) {
            int currentapiVersion = VERSION.SDK_INT;
            if ((currentapiVersion == 9 || currentapiVersion == 10) && new File(this.videoFileUri.getPath()).exists()) {
                intent.setData(this.videoFileUri);
                startActivityForResult(intent, REQUEST_DESCRIPTION);
            }
        }
    }

    private void sendTextMessage(String favourite) {
        ChatController.getInstance(getActivity()).sendMessage(Utils.createXmppUserIdByUserId(this.recipientId), favourite, MessageContentType.TEXT.toString(), this.isGroupChat, null, null, null, BuildConfig.VERSION_NAME);
    }

    private boolean isFileBiggerThanTheLimit(String filePath) {
        return Integer.parseInt(String.valueOf(new File(filePath).length() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) > 5120;
    }

    private MyMessageType readMessageType(int type) {
        MyMessageType messageType = MyMessageType.HEADER_MSG;
        switch (type) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                return MyMessageType.OUTGOING_MSG;
            case Logger.SEVERE /*1*/:
                return MyMessageType.INCOMING_MSG;
            default:
                return messageType;
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = null;
        try {
            cursor = getActivity().getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow("_data");
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            return path;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "SalamVideos");
        if (mediaStorageDir.exists() || mediaStorageDir.mkdirs()) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Long.valueOf(new Date().getTime()));
            if (type != REQUEST_VIDEO_CAPTURE) {
                return null;
            }
            File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
            this.fullFilePath = mediaFile.getAbsolutePath();
            return mediaFile;
        }
        Toast.makeText(getActivity(), 2131493125, 1).show();
        return null;
    }

    public void onAddressReceived(Location location, String address) {
        this.currentLocation = location;
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "salam");
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        this.fullFilePath = new File(mediaStorageDir.getPath() + File.separator + "map" + new Date().getTime() + ".png").getAbsolutePath();
        try {
            Intent mapIntent = new Intent(getActivity(), ShamChatMapView.class);
            mapIntent.putExtra("latitude", location.getLatitude());
            mapIntent.putExtra("longitude", location.getLongitude());
            mapIntent.putExtra("address", address);
            mapIntent.putExtra("fullFilePath", this.fullFilePath);
            startActivityForResult(mapIntent, REQUEST_LOCATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

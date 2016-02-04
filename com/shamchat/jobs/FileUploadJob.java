package com.shamchat.jobs;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.media.TransportMediator;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import com.kyleduo.switchbutton.C0473R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.shamchat.activity.ChatInitialForGroupChatActivity;
import com.shamchat.adapters.MyMessageType;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.events.FileUploadingProgressEvent;
import com.shamchat.events.NewMessageEvent;
import com.shamchat.models.ChatMessage.MessageStatusType;
import com.shamchat.models.MessageThread;
import com.shamchat.utils.Utils;
import cz.msebera.android.httpclient.Header;
import de.greenrobot.event.EventBus;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.json.JSONException;
import org.json.JSONObject;

public final class FileUploadJob extends Job {
    private static final AtomicInteger jobCounter;
    private transient ContentResolver contentResolver;
    private transient Context context;
    private String description;
    private String fullFilePath;
    private final int id;
    private boolean isGroupChat;
    private boolean isRetry;
    private double latitude;
    private double longitude;
    private int messageContentType;
    private String oldPacketId;
    private String packetId;
    private int progress;
    private String recipientId;
    private String senderId;
    private Uri updateUri;
    boolean uploadFailed;

    /* renamed from: com.shamchat.jobs.FileUploadJob.1 */
    class C10851 extends JsonHttpResponseHandler {
        final /* synthetic */ String val$threadId;
        final /* synthetic */ byte[] val$thumbailBytes;

        C10851(byte[] bArr, String str) {
            this.val$thumbailBytes = bArr;
            this.val$threadId = str;
        }

        public final void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            if (!FileUploadJob.this.uploadFailed) {
                System.out.println("SENDING BLOB onSuccess " + response);
                Object[] result = new Object[3];
                result[0] = response;
                result[1] = this.val$thumbailBytes;
                try {
                    if (response.getString(NotificationCompatApi21.CATEGORY_STATUS).equalsIgnoreCase("success")) {
                        System.out.println("SENDING BLOB onSuccess SUCCESS");
                        JSONObject urlData = response.getJSONObject("data");
                        String url = urlData.getString("file_view_url");
                        ContentValues values = new ContentValues();
                        if (urlData.has("thumbnail_file_view_url")) {
                            values.put("text_message", urlData.getString("thumbnail_file_view_url"));
                        }
                        values.put("uploaded_file_url", url);
                        values.put("uploaded_percentage", Integer.valueOf(100));
                        System.out.println("SENDING BLOB onSuccess UPLOADED_FILE_URL " + url);
                        FileUploadJob.this.contentResolver.update(FileUploadJob.this.updateUri, values, null, null);
                        FileUploadingProgressEvent fileUploadingProgressEvent = new FileUploadingProgressEvent();
                        fileUploadingProgressEvent.threadId = this.val$threadId;
                        fileUploadingProgressEvent.packetId = FileUploadJob.this.packetId;
                        fileUploadingProgressEvent.uploadedPercentage = 100;
                        EventBus.getDefault().postSticky(fileUploadingProgressEvent);
                        return;
                    }
                    System.out.println("SENDING BLOB  failed 9999");
                    FileUploadJob.this.uploadFailed = true;
                } catch (JSONException e) {
                    System.out.println("SENDING BLOB josn exception " + e);
                    FileUploadJob.this.uploadFailed = true;
                }
            }
        }

        public final void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            FileUploadJob.this.uploadFailed = true;
        }

        public final void onRetry(int retryNo) {
            FileUploadJob.this.uploadFailed = true;
        }

        public final void onProgress(long bytesWritten, long totalSize) {
            super.onProgress(bytesWritten, totalSize);
            FileUploadJob.this.progress = (int) ((((float) bytesWritten) / ((float) totalSize)) * 100.0f);
            if (!FileUploadJob.this.uploadFailed) {
                System.out.println("FileUploadingProgressEvent % " + FileUploadJob.this.progress);
                if (FileUploadJob.this.progress < 100) {
                    System.out.println("FileUploadingProgressEvent inside % " + FileUploadJob.this.progress);
                    ContentValues values = new ContentValues();
                    values.put("uploaded_percentage", Integer.valueOf(FileUploadJob.this.progress));
                    FileUploadJob.this.contentResolver.update(FileUploadJob.this.updateUri, values, null, null);
                    FileUploadingProgressEvent fileUploadingProgressEvent = new FileUploadingProgressEvent();
                    fileUploadingProgressEvent.threadId = this.val$threadId;
                    fileUploadingProgressEvent.packetId = FileUploadJob.this.packetId;
                    fileUploadingProgressEvent.uploadedPercentage = FileUploadJob.this.progress;
                    EventBus.getDefault().post(fileUploadingProgressEvent);
                }
            }
        }
    }

    static {
        jobCounter = new AtomicInteger(0);
    }

    public FileUploadJob(String fullFilePath, String senderId, String recipientId, boolean isGroupChat, int messageContentType, String description, boolean isRetry, String oldPacketId, double longitude, double latitude) {
        Params params = new Params(1000);
        params.persistent = true;
        super(params);
        this.isRetry = false;
        this.updateUri = null;
        this.uploadFailed = false;
        this.id = jobCounter.incrementAndGet();
        this.fullFilePath = fullFilePath;
        this.messageContentType = messageContentType;
        this.description = description;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.isGroupChat = isGroupChat;
        this.isRetry = isRetry;
        this.longitude = longitude;
        this.latitude = latitude;
        if (oldPacketId != null) {
            this.oldPacketId = oldPacketId;
        }
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        if (this.id == jobCounter.get()) {
            String currentPacketID;
            this.context = SHAMChatApplication.getMyApplicationContext();
            this.contentResolver = this.context.getContentResolver();
            File myFile = new File(this.fullFilePath);
            System.out.println("SENDING BLOB fullFilePath " + this.fullFilePath);
            if (!this.isRetry) {
                try {
                    this.fullFilePath = compressImage(this.fullFilePath);
                    myFile = new File(this.fullFilePath);
                } catch (OutOfMemoryError e) {
                    System.out.println("SENDING BLOB out of memory " + e);
                } catch (Exception e2) {
                    System.out.println("SENDING BLOB exception " + e2);
                }
            }
            byte[] thumbailBytes = getThumbnailBytes(myFile);
            System.out.println("SENDING BLOB thumbailBytes " + thumbailBytes.length);
            if (this.isGroupChat) {
                currentPacketID = saveBlobMessageGroup();
            } else {
                currentPacketID = saveBlobMessage();
            }
            if (currentPacketID != null) {
                System.out.println("Uploading BLOB to php script: " + currentPacketID);
                this.packetId = currentPacketID;
                String[] strArr = new String[1];
                strArr[0] = this.packetId;
                Cursor cursor = this.contentResolver.query(ChatProviderNew.CONTENT_URI_CHAT, null, "packet_id=?", strArr, null);
                cursor.moveToFirst();
                String columnID = Integer.toString(cursor.getInt(cursor.getColumnIndex("_id")));
                String threadId = cursor.getString(cursor.getColumnIndex(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID));
                cursor.close();
                this.updateUri = Uri.parse("content://org.zamin.androidclient.provider.Messages/chat_message/" + columnID);
                RequestParams params = new RequestParams();
                try {
                    System.out.println("FileUpload sender " + this.senderId + " " + myFile.getAbsolutePath());
                    RequestParams requestParams = params;
                    requestParams.put("userId", this.senderId);
                    params.put("file", myFile);
                    SyncHttpClient client = new SyncHttpClient();
                    client.setMaxRetriesAndTimeout(5, 5555);
                    client.setTimeout(300000);
                    client.setResponseTimeout(300000);
                    client.post(this.context, "http://static.rabtcdn.com/upload_file.php", params, new C10851(thumbailBytes, threadId));
                    if (this.uploadFailed) {
                        ContentValues values = new ContentValues();
                        values.put("uploaded_file_url", "failed");
                        values.put("uploaded_percentage", Integer.valueOf(9999));
                        this.contentResolver.update(this.updateUri, values, null, null);
                        FileUploadingProgressEvent fileUploadingProgressEvent = new FileUploadingProgressEvent();
                        fileUploadingProgressEvent.threadId = threadId;
                        fileUploadingProgressEvent.packetId = this.packetId;
                        fileUploadingProgressEvent.uploadedPercentage = 9999;
                        EventBus.getDefault().postSticky(fileUploadingProgressEvent);
                        throw new IOException("Unexpected reponse code ");
                    }
                    return;
                } catch (Exception e22) {
                    System.out.println("SENDING BLOB myFile exception " + e22);
                    this.uploadFailed = true;
                    return;
                }
            }
            System.out.println("SENDING BLOB currentPacketID null");
        }
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        System.out.println("SENDING FILE shouldReRunOnThrowable");
        return false;
    }

    private String saveBlobMessage() {
        String lastMessage;
        Message message = new Message();
        boolean isLocation = false;
        switch (this.messageContentType) {
            case Logger.SEVERE /*1*/:
                lastMessage = this.context.getString(2131493360);
                break;
            case Logger.WARNING /*2*/:
                lastMessage = this.context.getString(2131493362);
                break;
            case Logger.INFO /*3*/:
                lastMessage = this.context.getString(2131493364);
                break;
            case Logger.FINER /*6*/:
                lastMessage = this.context.getString(2131493361);
                isLocation = true;
                break;
            case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                lastMessage = this.context.getString(2131493363);
                break;
            default:
                lastMessage = "Unknown file type";
                break;
        }
        if (this.isRetry) {
            return this.oldPacketId;
        }
        MessageThread thread = new MessageThread();
        thread.threadOwner = this.senderId;
        thread.friendId = this.recipientId;
        thread.isGroupChat = this.isGroupChat;
        Cursor threadCursor = null;
        try {
            ContentValues values;
            threadCursor = this.contentResolver.query(ChatProviderNew.CONTENT_URI_THREAD, new String[]{ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID}, "thread_id=?", new String[]{thread.getThreadId()}, null);
            if (threadCursor.getCount() == 0) {
                values = new ContentValues();
                values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, thread.getThreadId());
                values.put("friend_id", thread.friendId);
                values.put("read_status", Integer.valueOf(0));
                values.put("last_message", lastMessage);
                values.put("last_message_content_type", Integer.valueOf(this.messageContentType));
                values.put("last_updated_datetime", Utils.formatDate(new Date().getTime(), "yyyy/MM/dd HH:mm:ss"));
                values.put("is_group_chat", Integer.valueOf(message.type == Type.groupchat ? 1 : 0));
                values.put("thread_owner", this.senderId);
                values.put("last_message_direction", Integer.valueOf(MyMessageType.OUTGOING_MSG.ordinal()));
                this.contentResolver.insert(ChatProviderNew.CONTENT_URI_THREAD, values);
            } else {
                values = new ContentValues();
                values.put("last_message", lastMessage);
                values.put("last_message_content_type", Integer.valueOf(this.messageContentType));
                values.put("read_status", Integer.valueOf(0));
                values.put("last_message_direction", Integer.valueOf(MyMessageType.OUTGOING_MSG.ordinal()));
                values.put("last_updated_datetime", Utils.formatDate(new Date().getTime(), "yyyy/MM/dd HH:mm:ss"));
                this.contentResolver.update(ChatProviderNew.CONTENT_URI_THREAD, values, "thread_id=?", new String[]{thread.getThreadId()});
            }
            if (threadCursor != null) {
                threadCursor.close();
            }
            values = new ContentValues();
            values.put("message_recipient", this.recipientId);
            values.put("message_type", Integer.valueOf(MyMessageType.OUTGOING_MSG.ordinal()));
            values.put("packet_id", message.packetID);
            values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, thread.getThreadId());
            values.put("text_message", message.getBody(null));
            values.put("message_sender", this.senderId);
            values.put("message_last_updated_datetime", Utils.formatDate(new Date().getTime(), "yyyy/MM/dd HH:mm:ss"));
            values.put("description", this.description);
            values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_MESSAGE_CONTENT_TYPE, Integer.valueOf(this.messageContentType));
            values.put("message_status", Integer.valueOf(MessageStatusType.SENDING.ordinal()));
            values.put("file_url", this.fullFilePath);
            System.out.println("Save message full file path " + this.fullFilePath);
            values.put("text_message", BuildConfig.VERSION_NAME);
            if (this.latitude > 0.0d && this.longitude > 0.0d && isLocation) {
                values.put("latitude", Double.valueOf(this.latitude));
                values.put("longitude", Double.valueOf(this.longitude));
            }
            Cursor cursor = null;
            try {
                cursor = this.contentResolver.query(this.contentResolver.insert(ChatProviderNew.CONTENT_URI_CHAT, values), null, null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    cursor.getInt(cursor.getColumnIndex("_id"));
                }
                if (cursor != null) {
                    cursor.close();
                }
                String packetID = message.packetID;
                NewMessageEvent newMessageSentEvent = new NewMessageEvent();
                newMessageSentEvent.threadId = thread.getThreadId();
                newMessageSentEvent.direction = MyMessageType.OUTGOING_MSG.ordinal();
                System.out.println("FileUploadJob: saved BLOB in database and now sending event to update UINewMessageEvent ");
                EventBus.getDefault().post(newMessageSentEvent);
                return packetID;
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Throwable th2) {
            if (threadCursor != null) {
                threadCursor.close();
            }
        }
    }

    private String saveBlobMessageGroup() {
        Message message = new Message();
        boolean isLocation = false;
        switch (this.messageContentType) {
            case Logger.SEVERE /*1*/:
                this.context.getString(2131493360);
                break;
            case Logger.WARNING /*2*/:
                this.context.getString(2131493362);
                break;
            case Logger.INFO /*3*/:
                this.context.getString(2131493364);
                break;
            case Logger.FINER /*6*/:
                this.context.getString(2131493361);
                isLocation = true;
                break;
            case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                this.context.getString(2131493363);
                break;
        }
        if (this.isRetry) {
            return this.oldPacketId;
        }
        String myId = SHAMChatApplication.getConfig().userId;
        String threadId = myId + "-" + this.recipientId;
        String packetID = Utils.makePacketId(myId);
        this.packetId = packetID;
        ContentValues values = new ContentValues();
        values.put("message_recipient", this.recipientId);
        values.put("message_type", Integer.valueOf(MyMessageType.OUTGOING_MSG.ordinal()));
        values.put("packet_id", packetID);
        values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, threadId);
        values.put("text_message", message.getBody(null));
        values.put("message_sender", this.senderId);
        values.put("message_last_updated_datetime", Utils.formatDate(new Date().getTime(), "yyyy/MM/dd HH:mm:ss"));
        values.put("description", this.description);
        values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_MESSAGE_CONTENT_TYPE, Integer.valueOf(this.messageContentType));
        values.put("message_status", Integer.valueOf(MessageStatusType.SENDING.ordinal()));
        values.put("file_url", this.fullFilePath);
        System.out.println("Save message full file path " + this.fullFilePath);
        values.put("text_message", BuildConfig.VERSION_NAME);
        if (this.latitude > 0.0d && this.longitude > 0.0d && isLocation) {
            values.put("latitude", Double.valueOf(this.latitude));
            values.put("longitude", Double.valueOf(this.longitude));
        }
        Cursor cursor = null;
        try {
            cursor = this.contentResolver.query(this.contentResolver.insert(ChatProviderNew.CONTENT_URI_CHAT, values), null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                cursor.getInt(cursor.getColumnIndex("_id"));
            }
            if (cursor != null) {
                cursor.close();
            }
            NewMessageEvent newMessageSentEvent = new NewMessageEvent();
            newMessageSentEvent.threadId = threadId;
            newMessageSentEvent.packetId = packetID;
            newMessageSentEvent.direction = MyMessageType.OUTGOING_MSG.ordinal();
            EventBus.getDefault().post(newMessageSentEvent);
            return packetID;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private byte[] getThumbnailBytes(File sourceFile) {
        Bitmap thumb = null;
        try {
            if (this.messageContentType == MessageContentType.IMAGE.ordinal() || this.messageContentType == MessageContentType.LOCATION.ordinal()) {
                thumb = Utils.decodeSampledBitmapFromFile(sourceFile.getAbsolutePath(), 150, TransportMediator.KEYCODE_MEDIA_RECORD);
            } else if (this.messageContentType == MessageContentType.VIDEO.ordinal()) {
                thumb = ThumbnailUtils.createVideoThumbnail(sourceFile.getAbsolutePath(), 3);
            }
            if (thumb == null) {
                return new byte[0];
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            thumb.compress(CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } catch (Exception e) {
            System.out.println("SENDING error in getThumbnailBytes " + e);
            e.printStackTrace();
            return null;
        }
    }

    private static String compressImage(String filePath) {
        Bitmap scaledBitmap = null;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float imgRatio = (float) (actualWidth / actualHeight);
        if (((float) actualHeight) > 816.0f || ((float) actualWidth) > 612.0f) {
            if (imgRatio < 0.75f) {
                actualWidth = (int) ((816.0f / ((float) actualHeight)) * ((float) actualWidth));
                actualHeight = 816;
            } else if (imgRatio > 0.75f) {
                actualHeight = (int) ((612.0f / ((float) actualWidth)) * ((float) actualHeight));
                actualWidth = 612;
            } else {
                actualHeight = 816;
                actualWidth = 612;
            }
        }
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[AccessibilityNodeInfoCompat.ACTION_COPY];
        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Config.ARGB_8888);
        } catch (OutOfMemoryError e2) {
            e2.printStackTrace();
        }
        float ratioX = ((float) actualWidth) / ((float) options.outWidth);
        float ratioY = ((float) actualHeight) / ((float) options.outHeight);
        float middleX = ((float) actualWidth) / 2.0f;
        float middleY = ((float) actualHeight) / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - ((float) (bmp.getWidth() / 2)), middleY - ((float) (bmp.getHeight() / 2)), new Paint(2));
        try {
            int orientation = new ExifInterface(filePath).getAttributeInt("Orientation", 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90.0f);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180.0f);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270.0f);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "salam/images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String filename = file.getAbsolutePath() + MqttTopic.TOPIC_LEVEL_SEPARATOR + System.currentTimeMillis() + ".jpg";
        try {
            scaledBitmap.compress(CompressFormat.JPEG, 80, new FileOutputStream(filename));
        } catch (FileNotFoundException e4) {
            e4.printStackTrace();
        }
        return filename;
    }

    private static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round(((float) height) / ((float) reqHeight));
            int widthRatio = Math.round(((float) width) / ((float) reqWidth));
            if (heightRatio < widthRatio) {
                inSampleSize = heightRatio;
            } else {
                inSampleSize = widthRatio;
            }
        }
        while (((float) (width * height)) / ((float) (inSampleSize * inSampleSize)) > ((float) ((reqWidth * reqHeight) * 2))) {
            inSampleSize++;
        }
        return inSampleSize;
    }
}

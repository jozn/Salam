package com.shamchat.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Images.Media;
import android.support.v4.content.CursorLoader;
import android.support.v4.os.EnvironmentCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.appcompat.BuildConfig;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.kyleduo.switchbutton.C0473R;
import com.shamchat.activity.ProgressBarLoadingDialog;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

@TargetApi(9)
public final class Utils {

    /* renamed from: com.shamchat.utils.Utils.1 */
    static class C11781 implements Target {
        final /* synthetic */ String val$userId;

        C11781(String str) {
            this.val$userId = str;
        }

        public final void onBitmapLoaded$dc1124d(Bitmap bitmap) {
            System.out.println("Bit map loaded");
            SHAMChatApplication.USER_IMAGES.put(this.val$userId, bitmap);
        }

        public final void onBitmapFailed$130e17e7() {
        }
    }

    public static class ContactDetails {
        public String displayName;
        public boolean isExist;
    }

    public static boolean isInternetAvailable(Context context) {
        if (((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo() != null) {
            return true;
        }
        return false;
    }

    public static String convertToEnglishDigits(String value) {
        return value.replace("\u0661", "1").replace("\u0661", "1").replace("\u0662", "2").replace("\u0664", "4").replace("\u0665", "5").replace("\u0666", "6").replace("\u0667", "7").replace("\u0668", "8").replace("\u0669", "9").replace("\u0660", "0").replace("\u0663", "3").replace("\u06f1", "1").replace("\u06f2", "2").replace("\u06f3", "3").replace("\u06f4", "4").replace("\u06f5", "5").replace("\u06f6", "6").replace("\u06f7", "7").replace("\u06f8", "8").replace("\u06f9", "9").replace("\u06f0", "0");
    }

    public static Bitmap scaleDownImageSize$1c855778(Bitmap bm, int orientation) {
        Bitmap photo = rotateImage(Bitmap.createScaledBitmap(bm, 480, 320, false), orientation);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + "/salam/profileimage" + File.separator + "Imagename.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return photo;
    }

    private static Bitmap rotateImage(Bitmap b, int degrees) {
        if (degrees == 0 || b == null) {
            return b;
        }
        Matrix m = new Matrix();
        m.setRotate((float) degrees, ((float) b.getWidth()) / 2.0f, ((float) b.getHeight()) / 2.0f);
        try {
            Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
            if (b == b2) {
                return b;
            }
            b.recycle();
            return b2;
        } catch (OutOfMemoryError e) {
            throw e;
        }
    }

    public static int getExifRotation(String imgPath) {
        try {
            String rotationAmount = new ExifInterface(imgPath).getAttribute("Orientation");
            if (TextUtils.isEmpty(rotationAmount)) {
                return 0;
            }
            switch (Integer.parseInt(rotationAmount)) {
                case Logger.INFO /*3*/:
                    return 180;
                case Logger.FINER /*6*/:
                    return 90;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    return 270;
                default:
                    return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public static Bitmap scaleDownImageSizeProfile$1c855778(Bitmap bm) {
        Bitmap photo = bm;
        try {
            photo = Bitmap.createScaledBitmap(photo, 32, 32, false);
            photo.compress(CompressFormat.JPEG, 5, new ByteArrayOutputStream());
            return photo;
        } catch (Exception e) {
            return photo;
        }
    }

    public static String encodeImage(Bitmap bitmap) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 80, byteArrayOutputStream);
            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void showKeyboard(EditText editText, Context context) {
        ((InputMethodManager) context.getSystemService("input_method")).showSoftInput(editText, 1);
    }

    public static String readableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        int digitGroups = (int) (Math.log10((double) size) / Math.log10(1024.0d));
        return new DecimalFormat("#,##0.#").format(((double) size) / Math.pow(1024.0d, (double) digitGroups)) + " " + new String[]{"B", "KB", "MB", "GB", "TB"}[digitGroups];
    }

    public static String formatStringDate(String dateInString, String format) {
        Date formatedDate = null;
        try {
            formatedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatDate(formatedDate.getTime(), format);
    }

    public static Bitmap fixOrientation(Bitmap mBitmap) {
        if (mBitmap.getWidth() <= mBitmap.getHeight()) {
            return mBitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(90.0f);
        return Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
    }

    public static String createXmppUserIdByUserId(String userId) {
        if (userId == null || userId.contains("@")) {
            return userId;
        }
        return userId + "@rabtcdn.com";
    }

    public static Date getDateFromStringDate(String dateInString) {
        Date formatedDate = null;
        try {
            formatedDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatedDate;
    }

    public static Date getDateFromStringDate(String dateInString, String existingFormat) {
        Date formatedDate = null;
        try {
            formatedDate = new SimpleDateFormat(existingFormat).parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatedDate;
    }

    public static String formatDate(long yourDate, String format) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(new Date(yourDate));
    }

    public static String getUserIdFromXmppUserId(String xmppUserId) {
        try {
            return xmppUserId.substring(0, xmppUserId.indexOf("@"));
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap base64ToBitmap(String base64) {
        if (base64 != null) {
            return BitmapFactory.decodeStream(new ByteArrayInputStream(Base64.decode(base64, 0)));
        }
        return null;
    }

    public static byte[] downloadImageFromUrl(String profileUrl) {
        byte[] blobMessage = null;
        if (profileUrl != null) {
            try {
                if (!profileUrl.equalsIgnoreCase(MqttServiceConstants.TRACE_EXCEPTION)) {
                    String strUrl = profileUrl;
                    URL url = new URL(strUrl);
                    File folder = new File(Environment.getExternalStorageDirectory() + "/salam/profileimage");
                    if (!folder.exists()) {
                        folder.mkdirs();
                        File file = new File(folder.getAbsolutePath() + "/.nomedia");
                    }
                    File downloadedFile = new File(folder.getAbsolutePath() + MqttTopic.TOPIC_LEVEL_SEPARATOR + strUrl.substring(strUrl.lastIndexOf(MqttTopic.TOPIC_LEVEL_SEPARATOR) + 1));
                    if (downloadedFile.exists()) {
                        System.out.println("This file has been already downloaded " + downloadedFile.getName());
                    } else {
                        url.openConnection().connect();
                        InputStream input = new BufferedInputStream(url.openStream(), AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD);
                        OutputStream output = new FileOutputStream(downloadedFile);
                        byte[] data = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
                        while (true) {
                            int count = input.read(data);
                            if (count == -1) {
                                break;
                            }
                            output.write(data, 0, count);
                        }
                        output.flush();
                        output.close();
                        input.close();
                    }
                    blobMessage = getBytesFromFilePath(downloadedFile);
                    return blobMessage;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static byte[] getBytesFromFilePath(File filePath) {
        byte[] bFile = new byte[((int) filePath.length())];
        try {
            System.out.println("Converting to byte[]");
            FileInputStream fileInputStream = new FileInputStream(filePath);
            fileInputStream.read(bFile);
            fileInputStream.close();
            System.out.println("Done");
        } catch (Exception e) {
            System.out.println("error converting to byte[] " + e.getMessage());
        }
        return bFile;
    }

    public static Bitmap scaleDownImageSize$5c843e57(Bitmap bm) {
        Bitmap photo = Bitmap.createScaledBitmap(bm, 280, 120, false);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + "/salam/profileimage" + File.separator + "Imagename.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return photo;
    }

    public static boolean isEditTextEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        }
        return true;
    }

    public static void hideKeyboard(EditText editText, Context context) {
        ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static ContactDetails getConactExists(Context context, String number) {
        ContactDetails details = new ContactDetails();
        details.isExist = false;
        details.displayName = BuildConfig.VERSION_NAME;
        Cursor cur = context.getContentResolver().query(Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number)), new String[]{"_id", "number", "display_name"}, null, null, null);
        try {
            if (cur.moveToFirst()) {
                details.isExist = true;
                details.displayName = cur.getString(cur.getColumnIndex("display_name"));
            }
            if (cur != null) {
                cur.close();
            }
            return details;
        } catch (Throwable th) {
            if (cur != null) {
                cur.close();
            }
        }
    }

    public static Bitmap decodeSampledBitmapFromResource$295fb07d(String path) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize$50b5669a(options);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static boolean contactExists(Context context, String number) {
        Cursor cur = context.getContentResolver().query(Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number)), new String[]{"_id", "number", "display_name"}, null, null, null);
        try {
            if (cur.moveToFirst()) {
                return true;
            }
            if (cur != null) {
                cur.close();
            }
            return false;
        } finally {
            if (cur != null) {
                cur.close();
            }
        }
    }

    private static int calculateInSampleSize$50b5669a(Options options) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > 240 || width > 320) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while (halfHeight / inSampleSize > 240 && halfWidth / inSampleSize > 320) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static boolean checkPlayServices(Context context) {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (status == 0) {
            return true;
        }
        if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
            Toast.makeText(context, 2131493154, 0).show();
            try {
                ProgressBarLoadingDialog.getInstance().dismiss();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        Toast.makeText(context, 2131493098, 1).show();
        try {
            ProgressBarLoadingDialog.getInstance().dismiss();
            return false;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static Map<String, Long> getDurationBreakdownArray(long millis) {
        String[] units = new String[]{"Days", "Hours", "Minutes", "Seconds"};
        Long[] values = new Long[4];
        Map<String, Long> jo = new HashMap();
        boolean startPrinting = false;
        int i;
        if (millis <= 0) {
            for (i = 0; i < 4; i++) {
                try {
                    jo.put(units[i], Long.valueOf(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            values[0] = Long.valueOf(TimeUnit.MILLISECONDS.toDays(millis));
            millis -= TimeUnit.DAYS.toMillis(values[0].longValue());
            values[1] = Long.valueOf(TimeUnit.MILLISECONDS.toHours(millis));
            millis -= TimeUnit.HOURS.toMillis(values[1].longValue());
            values[2] = Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(millis));
            values[3] = Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(millis - TimeUnit.MINUTES.toMillis(values[2].longValue())));
            i = 0;
            while (i < 4) {
                if (!(startPrinting || values[i].longValue() == 0)) {
                    startPrinting = true;
                }
                if (startPrinting) {
                    try {
                        jo.put(units[i], values[i]);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                i++;
            }
        }
        return jo;
    }

    public static String createXmppRoomIDByUserId(String room) {
        if (room == null || room.contains("@")) {
            return room;
        }
        return room + "@conference.rabtcdn.com";
    }

    public static File createFileFromBase64$2b27f8d0(String base64ImageData) {
        FileOutputStream fos;
        Throwable th;
        FileOutputStream fos2 = null;
        File file = null;
        if (base64ImageData != null) {
            try {
                File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "temp_asasjakska32jaac.png");
                try {
                    if (!file2.exists()) {
                        file2.createNewFile();
                    }
                    fos = new FileOutputStream(file2);
                } catch (Exception e) {
                    file = file2;
                    try {
                        System.out.println("Error creating file, Utils");
                        if (fos2 != null) {
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    file = file2;
                    throw th;
                }
                try {
                    fos.write(Base64.decode(base64ImageData, 0));
                    fos.flush();
                    fos.close();
                    file = file2;
                    fos2 = fos;
                } catch (Exception e2) {
                    file = file2;
                    fos2 = fos;
                    System.out.println("Error creating file, Utils");
                    if (fos2 != null) {
                    }
                } catch (Throwable th4) {
                    th = th4;
                    file = file2;
                    fos2 = fos;
                    throw th;
                }
            } catch (Exception e3) {
                System.out.println("Error creating file, Utils");
                if (fos2 != null) {
                }
            }
        }
        return fos2 != null ? file : file;
    }

    public static File getMomentsFolder() {
        File mainFolder = new File(Environment.getExternalStorageDirectory() + "/salam/moments");
        if (!mainFolder.exists()) {
            mainFolder.mkdirs();
        }
        return mainFolder;
    }

    public static void handleProfileImage(Context context, String userId, String url) {
        if (userId != null && url != null && url.contains("http://")) {
            Picasso.with(context).load(url).into(new C11781(userId));
        }
    }

    public static int getFileSize(URL url) {
        HttpURLConnection conn = null;
        int contentLength;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            conn.getInputStream();
            contentLength = conn.getContentLength();
            return contentLength;
        } catch (IOException e) {
            contentLength = e;
            return -1;
        } finally {
            conn.disconnect();
        }
    }

    public static String getTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    public static String makePacketId(String userId) {
        return "packet-" + userId + "-" + Long.valueOf(System.currentTimeMillis() / 1000).toString() + (new Random().nextInt(20000) + 60000);
    }

    public static String detectPacketType(String jsonMessageString) {
        String packetType;
        try {
            packetType = new JSONObject(jsonMessageString).getString("packet_type");
        } catch (JSONException e1) {
            if (jsonMessageString.equalsIgnoreCase("ping")) {
                packetType = "ping";
            } else {
                packetType = EnvironmentCompat.MEDIA_UNKNOWN;
            }
            e1.printStackTrace();
        }
        return packetType;
    }

    public static int detectMessageContentType(String jsonMessageString) {
        int messageType = -1;
        try {
            messageType = new JSONObject(jsonMessageString).getInt("messageType");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messageType;
    }

    public static String getPacketId(String jsonMessageString) {
        try {
            JSONObject SampleMsg = new JSONObject(jsonMessageString);
            if (SampleMsg.has("packetId")) {
                return SampleMsg.getString("packetId");
            }
            if (SampleMsg.has("packet_id")) {
                return SampleMsg.getString("packet_id");
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MessageContentType readMessageContentType(int type) {
        MessageContentType messageType = MessageContentType.TEXT;
        switch (type) {
            case Logger.SEVERE /*1*/:
                return MessageContentType.IMAGE;
            case Logger.WARNING /*2*/:
                return MessageContentType.STICKER;
            case Logger.INFO /*3*/:
                return MessageContentType.VOICE_RECORD;
            case Logger.CONFIG /*4*/:
                return MessageContentType.FAVORITE;
            case Logger.FINE /*5*/:
                return MessageContentType.MESSAGE_WITH_IMOTICONS;
            case Logger.FINER /*6*/:
                return MessageContentType.LOCATION;
            case Logger.FINEST /*7*/:
                return MessageContentType.INCOMING_CALL;
            case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                return MessageContentType.OUTGOING_CALL;
            case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                return MessageContentType.VIDEO;
            case C0473R.styleable.SwitchButton_offColor /*11*/:
                return MessageContentType.GROUP_INFO;
            default:
                return messageType;
        }
    }

    public static boolean isMyOwnPacket(String jsonMessageString) {
        int fromUserId = -1;
        try {
            fromUserId = new JSONObject(jsonMessageString).getInt("from_userid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(SHAMChatApplication.getConfig().userId) == fromUserId) {
            return true;
        }
        return false;
    }

    public static String getMimeType(String url) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return null;
    }

    @TargetApi(19)
    public static String getFilePathImage(Context context, Uri uri) {
        int currentApiVersion;
        try {
            currentApiVersion = VERSION.SDK_INT;
        } catch (NumberFormatException e) {
            currentApiVersion = 3;
        }
        Cursor cursor;
        if (currentApiVersion >= 19) {
            String filePath = BuildConfig.VERSION_NAME;
            String id = DocumentsContract.getDocumentId(uri).split(":")[1];
            String[] column = new String[]{"_data"};
            cursor = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, column, "_id=?", new String[]{id}, null);
            int columnIndex = cursor.getColumnIndex(column[0]);
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        } else if (currentApiVersion > 13 || currentApiVersion < 11) {
            cursor = context.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
            column_index = cursor.getColumnIndexOrThrow("_data");
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else {
            String result = null;
            cursor = new CursorLoader(context, uri, new String[]{"_data"}, null, null, null).loadInBackground();
            if (cursor != null) {
                column_index = cursor.getColumnIndexOrThrow("_data");
                cursor.moveToFirst();
                result = cursor.getString(column_index);
            }
            return result;
        }
    }

    public static String packetIdToFileName(String packetId) {
        String fileName = packetId;
        if (StringUtils.countMatches(packetId, "-") >= 2) {
            fileName = packetId.substring(packetId.indexOf(45) + 1);
        }
        return fileName.replace('-', '_').replaceAll("[^a-zA-Z0-9_\\-\\.]", "_");
    }

    public static boolean fileExists(String filePath) {
        if (filePath != null && new File(filePath).exists()) {
            return true;
        }
        return false;
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        Bitmap bm = null;
        try {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int height = options.outHeight;
            int width = options.outWidth;
            int inSampleSize = 1;
            if (height > reqHeight) {
                inSampleSize = Math.round(((float) height) / ((float) reqHeight));
            }
            if (width / inSampleSize > reqWidth) {
                inSampleSize = Math.round(((float) width) / ((float) reqWidth));
            }
            options.inSampleSize = inSampleSize;
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(path, options);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(SHAMChatApplication.getMyApplicationContext(), e.toString(), 1).show();
        }
        return bm;
    }

    public static String getImageLocalFilePath(String packetId) {
        Cursor cursor = SHAMChatApplication.getMyApplicationContext().getContentResolver().query(ChatProviderNew.CONTENT_URI_CHAT, null, "packet_id=?", new String[]{packetId}, null);
        cursor.moveToFirst();
        String fileUrl = cursor.getString(cursor.getColumnIndex("file_url"));
        cursor.close();
        return fileUrl;
    }

    public static Bitmap fastblur$75eed7c6(Bitmap sentBitmap) {
        int i;
        int y;
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(sentBitmap, Math.round(((float) sentBitmap.getWidth()) * 3.0f), Math.round(((float) sentBitmap.getHeight()) * 3.0f), false);
        Bitmap bitmap = createScaledBitmap.copy(createScaledBitmap.getConfig(), true);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[(w * h)];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int[] r = new int[wh];
        int[] g = new int[wh];
        int[] b = new int[wh];
        int[] vmin = new int[Math.max(w, h)];
        int[] dv = new int[43264];
        for (i = 0; i < 43264; i++) {
            dv[i] = i / 169;
        }
        int yi = 0;
        int yw = 0;
        int[][] stack = (int[][]) Array.newInstance(Integer.TYPE, new int[]{25, 3});
        for (y = 0; y < h; y++) {
            int x;
            int bsum = 0;
            int gsum = 0;
            int rsum = 0;
            int boutsum = 0;
            int goutsum = 0;
            int routsum = 0;
            int binsum = 0;
            int ginsum = 0;
            int rinsum = 0;
            for (i = -12; i <= 12; i++) {
                int p = pix[Math.min(wm, Math.max(i, 0)) + yi];
                int[] sir = stack[i + 12];
                sir[0] = (16711680 & p) >> 16;
                sir[1] = (MotionEventCompat.ACTION_POINTER_INDEX_MASK & p) >> 8;
                sir[2] = p & MotionEventCompat.ACTION_MASK;
                int rbs = 13 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            int stackpointer = 12;
            for (x = 0; x < w; x++) {
                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                sir = stack[((stackpointer - 12) + 25) % 25];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (y == 0) {
                    vmin[x] = Math.min((x + 12) + 1, wm);
                }
                p = pix[vmin[x] + yw];
                sir[0] = (16711680 & p) >> 16;
                sir[1] = (MotionEventCompat.ACTION_POINTER_INDEX_MASK & p) >> 8;
                sir[2] = p & MotionEventCompat.ACTION_MASK;
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % 25;
                sir = stack[stackpointer % 25];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            bsum = 0;
            gsum = 0;
            rsum = 0;
            boutsum = 0;
            goutsum = 0;
            routsum = 0;
            binsum = 0;
            ginsum = 0;
            rinsum = 0;
            int yp = w * -12;
            for (i = -12; i <= 12; i++) {
                yi = Math.max(0, yp) + x;
                sir = stack[i + 12];
                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];
                rbs = 13 - Math.abs(i);
                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = 12;
            for (y = 0; y < h; y++) {
                pix[yi] = (((ViewCompat.MEASURED_STATE_MASK & pix[yi]) | (dv[rsum] << 16)) | (dv[gsum] << 8)) | dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                sir = stack[((stackpointer - 12) + 25) % 25];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (x == 0) {
                    vmin[y] = Math.min(y + 13, hm) * w;
                }
                p = x + vmin[y];
                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % 25;
                sir = stack[stackpointer];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi += w;
            }
        }
        Log.e("pix", w + " " + h + " " + wh);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return bitmap;
    }
}

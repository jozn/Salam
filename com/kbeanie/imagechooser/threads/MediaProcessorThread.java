package com.kbeanie.imagechooser.threads;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.appcompat.BuildConfig;
import android.text.TextUtils;
import android.util.Log;
import com.kbeanie.imagechooser.api.FileUtils;
import com.kbeanie.imagechooser.exceptions.ChooserException;
import com.kbeanie.imagechooser.helpers.StreamHelper;
import com.kyleduo.switchbutton.C0473R;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Locale;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public abstract class MediaProcessorThread extends Thread {
    private static final String TAG;
    protected boolean clearOldFiles;
    protected Context context;
    protected String filePath;
    protected String foldername;
    protected String mediaExtension;
    protected boolean shouldCreateThumnails;

    /* renamed from: com.kbeanie.imagechooser.threads.MediaProcessorThread.1 */
    class C04721 implements FileFilter {
        final /* synthetic */ String val$extension;
        final /* synthetic */ long val$today;

        C04721(long j, String str) {
            this.val$today = j;
            this.val$extension = str;
        }

        public final boolean accept(File pathname) {
            if (this.val$today - pathname.lastModified() <= 864000000 || !pathname.getAbsolutePath().toUpperCase(Locale.ENGLISH).endsWith(this.val$extension.toUpperCase(Locale.ENGLISH))) {
                return false;
            }
            return true;
        }
    }

    protected abstract void processingDone(String str, String str2, String str3);

    static {
        TAG = MediaProcessorThread.class.getSimpleName();
    }

    public MediaProcessorThread(String filePath, String foldername, boolean shouldCreateThumbnails) {
        this.clearOldFiles = false;
        this.filePath = filePath;
        this.foldername = foldername;
        this.shouldCreateThumnails = shouldCreateThumbnails;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public final void setMediaExtension(String extension) {
        this.mediaExtension = extension;
    }

    public final void clearOldFiles(boolean clearOldFiles) {
        this.clearOldFiles = clearOldFiles;
    }

    protected final void downloadAndProcess(String url) throws ChooserException {
        this.filePath = downloadFile(url);
        process();
    }

    protected void process() throws ChooserException {
        Closeable fileInputStream;
        Closeable bufferedOutputStream;
        IOException e;
        Throwable th;
        Closeable closeable = null;
        if (!this.filePath.contains(this.foldername)) {
            try {
                File file = new File(Uri.parse(this.filePath).getPath());
                File file2 = new File(FileUtils.getDirectory(this.foldername) + File.separator + file.getName());
                fileInputStream = new FileInputStream(file);
                try {
                    bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
                } catch (IOException e2) {
                    e = e2;
                    bufferedOutputStream = null;
                    closeable = fileInputStream;
                    try {
                        throw new ChooserException(e);
                    } catch (Throwable th2) {
                        th = th2;
                        fileInputStream = closeable;
                        closeable = bufferedOutputStream;
                        StreamHelper.flush(closeable);
                        StreamHelper.close(fileInputStream);
                        StreamHelper.close(closeable);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    StreamHelper.flush(closeable);
                    StreamHelper.close(fileInputStream);
                    StreamHelper.close(closeable);
                    throw th;
                }
                try {
                    byte[] bArr = new byte[AccessibilityNodeInfoCompat.ACTION_PREVIOUS_HTML_ELEMENT];
                    while (true) {
                        int read = fileInputStream.read(bArr);
                        if (read > 0) {
                            bufferedOutputStream.write(bArr, 0, read);
                        } else {
                            this.filePath = file2.getAbsolutePath();
                            StreamHelper.flush(bufferedOutputStream);
                            StreamHelper.close(fileInputStream);
                            StreamHelper.close(bufferedOutputStream);
                            return;
                        }
                    }
                } catch (IOException e3) {
                    e = e3;
                    closeable = fileInputStream;
                    throw new ChooserException(e);
                } catch (Throwable th4) {
                    th = th4;
                    closeable = bufferedOutputStream;
                    StreamHelper.flush(closeable);
                    StreamHelper.close(fileInputStream);
                    StreamHelper.close(closeable);
                    throw th;
                }
            } catch (IOException e4) {
                e = e4;
                bufferedOutputStream = null;
                throw new ChooserException(e);
            } catch (Throwable th5) {
                th = th5;
                fileInputStream = null;
                StreamHelper.flush(closeable);
                StreamHelper.close(fileInputStream);
                StreamHelper.close(closeable);
                throw th;
            }
        }
    }

    protected static String[] createThumbnails(String image) throws ChooserException {
        return new String[]{compressAndSaveImage(image, 1), compressAndSaveImage(image, 2)};
    }

    private static String compressAndSaveImage(String fileImage, int scale) throws ChooserException {
        OutputStream stream;
        Throwable th;
        FileOutputStream stream2 = null;
        try {
            int what;
            Options optionsForGettingDimensions = new Options();
            optionsForGettingDimensions.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(fileImage, optionsForGettingDimensions);
            if (bitmap != null) {
                bitmap.recycle();
            }
            int w = optionsForGettingDimensions.outWidth;
            int l = optionsForGettingDimensions.outHeight;
            int rotate = 0;
            switch (new ExifInterface(fileImage).getAttributeInt("Orientation", 1)) {
                case Logger.INFO /*3*/:
                    rotate = 180;
                    break;
                case Logger.FINER /*6*/:
                    rotate = 90;
                    break;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    rotate = -90;
                    break;
            }
            if (w > l) {
                what = w;
            } else {
                what = l;
            }
            Options options = new Options();
            if (what > 3000) {
                options.inSampleSize = scale * 6;
            } else if (what > 2000 && what <= 3000) {
                options.inSampleSize = scale * 5;
            } else if (what > 1500 && what <= 2000) {
                options.inSampleSize = scale * 4;
            } else if (what > 1000 && what <= 1500) {
                options.inSampleSize = scale * 3;
            } else if (what <= 400 || what > 1000) {
                options.inSampleSize = scale;
            } else {
                options.inSampleSize = scale * 2;
            }
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(fileImage, options);
            File original = new File(fileImage);
            File file = new File(original.getParent() + File.separator + original.getName().replace(".", "_fact_" + scale + "."));
            OutputStream fileOutputStream = new FileOutputStream(file);
            if (rotate != 0) {
                try {
                    Matrix matrix = new Matrix();
                    matrix.setRotate((float) rotate);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                } catch (IOException e) {
                    stream = fileOutputStream;
                    StreamHelper.close(null);
                    StreamHelper.flush(stream2);
                    StreamHelper.close(stream2);
                    return fileImage;
                } catch (Exception e2) {
                    stream = fileOutputStream;
                    StreamHelper.close(null);
                    StreamHelper.flush(stream2);
                    StreamHelper.close(stream2);
                    return fileImage;
                } catch (Throwable th2) {
                    th = th2;
                    stream = fileOutputStream;
                    StreamHelper.close(null);
                    StreamHelper.flush(stream2);
                    StreamHelper.close(stream2);
                    throw th;
                }
            }
            bitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
            fileImage = file.getAbsolutePath();
            StreamHelper.close(null);
            StreamHelper.flush(fileOutputStream);
            StreamHelper.close(fileOutputStream);
            stream = fileOutputStream;
        } catch (IOException e3) {
            StreamHelper.close(null);
            StreamHelper.flush(stream2);
            StreamHelper.close(stream2);
            return fileImage;
        } catch (Exception e4) {
            StreamHelper.close(null);
            StreamHelper.flush(stream2);
            StreamHelper.close(stream2);
            return fileImage;
        } catch (Throwable th3) {
            th = th3;
            StreamHelper.close(null);
            StreamHelper.flush(stream2);
            StreamHelper.close(stream2);
            throw th;
        }
        return fileImage;
    }

    private String downloadFile(String url) {
        String localFilePath = BuildConfig.VERSION_NAME;
        try {
            InputStream stream = new DefaultHttpClient().execute(new HttpGet(url)).getEntity().getContent();
            localFilePath = FileUtils.getDirectory(this.foldername) + File.separator + Calendar.getInstance().getTimeInMillis() + "." + this.mediaExtension;
            FileOutputStream fileOutputStream = new FileOutputStream(new File(localFilePath));
            byte[] buffer = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
            while (true) {
                int len = stream.read(buffer);
                if (len <= 0) {
                    break;
                }
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            stream.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return localFilePath;
    }

    protected final void manageDirectoryCache(String extension) {
        if (this.clearOldFiles) {
            File directory = new File(FileUtils.getDirectory(this.foldername));
            File[] files = directory.listFiles();
            long count = 0;
            if (files != null) {
                for (File file : files) {
                    count += file.length();
                }
                if (count > 524288000) {
                    int deletedFileCount = 0;
                    for (File file2 : directory.listFiles(new C04721(Calendar.getInstance().getTimeInMillis(), extension))) {
                        deletedFileCount++;
                        file2.delete();
                    }
                    Log.i(TAG, "Deleted " + deletedFileCount + " files");
                }
            }
        }
    }

    protected final void processPicasaMedia(String path, String extension) throws ChooserException {
        IOException e;
        Throwable th;
        InputStream inputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            inputStream = this.context.getContentResolver().openInputStream(Uri.parse(path));
            StreamHelper.verifyStream(path, inputStream);
            this.filePath = FileUtils.getDirectory(this.foldername) + File.separator + Calendar.getInstance().getTimeInMillis() + extension;
            BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(this.filePath));
            try {
                byte[] buf = new byte[AccessibilityNodeInfoCompat.ACTION_PREVIOUS_HTML_ELEMENT];
                while (true) {
                    int len = inputStream.read(buf);
                    if (len > 0) {
                        outStream.write(buf, 0, len);
                    } else {
                        process();
                        StreamHelper.close(inputStream);
                        StreamHelper.close(outStream);
                        return;
                    }
                }
            } catch (IOException e2) {
                e = e2;
                bufferedOutputStream = outStream;
                try {
                    throw new ChooserException(e);
                } catch (Throwable th2) {
                    th = th2;
                    StreamHelper.close(inputStream);
                    StreamHelper.close(bufferedOutputStream);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedOutputStream = outStream;
                StreamHelper.close(inputStream);
                StreamHelper.close(bufferedOutputStream);
                throw th;
            }
        } catch (IOException e3) {
            e = e3;
            throw new ChooserException(e);
        }
    }

    protected final void processGooglePhotosMedia(String path, String extension) throws ChooserException {
        BufferedOutputStream outStream;
        IOException e;
        Throwable th;
        String retrievedExtension = checkExtension(Uri.parse(path));
        if (!(retrievedExtension == null || TextUtils.isEmpty(retrievedExtension))) {
            extension = "." + retrievedExtension;
        }
        InputStream inputStream = null;
        BufferedOutputStream outStream2 = null;
        try {
            BufferedInputStream reader;
            this.filePath = FileUtils.getDirectory(this.foldername) + File.separator + Calendar.getInstance().getTimeInMillis() + extension;
            ParcelFileDescriptor parcelFileDescriptor = this.context.getContentResolver().openFileDescriptor(Uri.parse(path), "r");
            StreamHelper.verifyStream(path, parcelFileDescriptor);
            InputStream inputStream2 = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
            try {
                reader = new BufferedInputStream(inputStream2);
                outStream = new BufferedOutputStream(new FileOutputStream(this.filePath));
            } catch (IOException e2) {
                e = e2;
                inputStream = inputStream2;
                try {
                    throw new ChooserException(e);
                } catch (Throwable th2) {
                    th = th2;
                    StreamHelper.flush(outStream2);
                    StreamHelper.close(outStream2);
                    StreamHelper.close(inputStream);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                inputStream = inputStream2;
                StreamHelper.flush(outStream2);
                StreamHelper.close(outStream2);
                StreamHelper.close(inputStream);
                throw th;
            }
            try {
                byte[] buf = new byte[AccessibilityNodeInfoCompat.ACTION_PREVIOUS_HTML_ELEMENT];
                while (true) {
                    int len = reader.read(buf);
                    if (len > 0) {
                        outStream.write(buf, 0, len);
                    } else {
                        process();
                        StreamHelper.flush(outStream);
                        StreamHelper.close(outStream);
                        StreamHelper.close(inputStream2);
                        return;
                    }
                }
            } catch (IOException e3) {
                e = e3;
                outStream2 = outStream;
                inputStream = inputStream2;
                throw new ChooserException(e);
            } catch (Throwable th4) {
                th = th4;
                outStream2 = outStream;
                inputStream = inputStream2;
                StreamHelper.flush(outStream2);
                StreamHelper.close(outStream2);
                StreamHelper.close(inputStream);
                throw th;
            }
        } catch (IOException e4) {
            e = e4;
            throw new ChooserException(e);
        }
    }

    private String checkExtension(Uri uri) {
        String extension = BuildConfig.VERSION_NAME;
        Cursor cursor = this.context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    String size;
                    String displayName = cursor.getString(cursor.getColumnIndex("_display_name"));
                    extension = displayName.substring(displayName.indexOf(".") + 1);
                    Log.i(TAG, "Display Name: " + displayName);
                    int sizeIndex = cursor.getColumnIndex("_size");
                    if (cursor.isNull(sizeIndex)) {
                        size = "Unknown";
                    } else {
                        size = cursor.getString(sizeIndex);
                    }
                    Log.i(TAG, "Size: " + size);
                }
            } catch (Throwable th) {
                cursor.close();
            }
        }
        cursor.close();
        return extension;
    }

    protected final void processContentProviderMedia(String path, String extension) throws ChooserException {
        IOException e;
        Throwable th;
        checkExtension(Uri.parse(path));
        InputStream inputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            inputStream = this.context.getContentResolver().openInputStream(Uri.parse(path));
            StreamHelper.verifyStream(path, inputStream);
            this.filePath = FileUtils.getDirectory(this.foldername) + File.separator + Calendar.getInstance().getTimeInMillis() + extension;
            BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(this.filePath));
            try {
                byte[] buf = new byte[AccessibilityNodeInfoCompat.ACTION_PREVIOUS_HTML_ELEMENT];
                while (true) {
                    int len = inputStream.read(buf);
                    if (len > 0) {
                        outStream.write(buf, 0, len);
                    } else {
                        process();
                        StreamHelper.close(inputStream);
                        StreamHelper.close(outStream);
                        return;
                    }
                }
            } catch (IOException e2) {
                e = e2;
                bufferedOutputStream = outStream;
                try {
                    Log.e(TAG, e.getMessage(), e);
                    throw new ChooserException(e);
                } catch (Throwable th2) {
                    th = th2;
                    StreamHelper.close(inputStream);
                    StreamHelper.close(bufferedOutputStream);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedOutputStream = outStream;
                StreamHelper.close(inputStream);
                StreamHelper.close(bufferedOutputStream);
                throw th;
            }
        } catch (IOException e3) {
            e = e3;
            Log.e(TAG, e.getMessage(), e);
            throw new ChooserException(e);
        }
    }

    @SuppressLint({"NewApi"})
    protected final String getAbsoluteImagePathFromUri(Uri imageUri) {
        String filePath;
        Uri uri = null;
        String[] proj = new String[]{"_data", "_display_name"};
        if (imageUri.toString().startsWith("content://com.android.gallery3d.provider")) {
            imageUri = Uri.parse(imageUri.toString().replace("com.android.gallery3d", "com.google.android.gallery3d"));
        }
        String imageUriString = imageUri.toString();
        if (imageUriString.startsWith("content://com.google.android.gallery3d") || imageUriString.startsWith("content://com.google.android.apps.photos.content") || imageUriString.startsWith("content://com.android.providers.media.documents") || imageUriString.startsWith("content://com.google.android.apps.docs.storage") || imageUriString.startsWith("content://com.microsoft.skydrive.content.external") || imageUriString.startsWith("content://com.android.externalstorage.documents") || imageUriString.startsWith("content://com.android.internalstorage.documents")) {
            filePath = imageUri.toString();
        } else {
            Cursor cursor = this.context.getContentResolver().query(imageUri, proj, null, null, null);
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
            cursor.close();
        }
        if (filePath != null || !isDownloadsDocument(imageUri) || VERSION.SDK_INT < 19) {
            return filePath;
        }
        int i;
        Context context = this.context;
        if (VERSION.SDK_INT >= 19) {
            i = 1;
        } else {
            i = 0;
        }
        if (i == 0 || !DocumentsContract.isDocumentUri(context, imageUri)) {
            if ("content".equalsIgnoreCase(imageUri.getScheme())) {
                return getDataColumn(context, imageUri, null, null);
            }
            if ("file".equalsIgnoreCase(imageUri.getScheme())) {
                return imageUri.getPath();
            }
        } else if ("com.android.externalstorage.documents".equals(imageUri.getAuthority())) {
            r0 = DocumentsContract.getDocumentId(imageUri).split(":");
            if ("primary".equalsIgnoreCase(r0[0])) {
                return Environment.getExternalStorageDirectory() + MqttTopic.TOPIC_LEVEL_SEPARATOR + r0[1];
            }
        } else if (isDownloadsDocument(imageUri)) {
            return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(imageUri)).longValue()), null, null);
        } else if ("com.android.providers.media.documents".equals(imageUri.getAuthority())) {
            Object obj = DocumentsContract.getDocumentId(imageUri).split(":")[0];
            if ("image".equals(obj)) {
                uri = Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(obj)) {
                uri = Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(obj)) {
                uri = Audio.Media.EXTERNAL_CONTENT_URI;
            }
            return getDataColumn(context, uri, "_id=?", new String[]{r0[1]});
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, new String[]{"_data"}, selection, selectionArgs, null);
            if (cursor == null || !cursor.moveToFirst()) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            String string = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
            if (cursor == null) {
                return string;
            }
            cursor.close();
            return string;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
}

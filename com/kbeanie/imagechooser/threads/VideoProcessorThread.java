package com.kbeanie.imagechooser.threads;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.kbeanie.imagechooser.api.ChosenVideo;
import com.kbeanie.imagechooser.api.FileUtils;
import com.kbeanie.imagechooser.exceptions.ChooserException;
import com.kbeanie.imagechooser.helpers.StreamHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class VideoProcessorThread extends MediaProcessorThread {
    private static final String TAG;
    public VideoProcessorListener listener;
    private String previewImage;

    static {
        TAG = VideoProcessorThread.class.getSimpleName();
    }

    public VideoProcessorThread(String filePath, String foldername, boolean shouldCreateThumbnails) {
        super(filePath, foldername, shouldCreateThumbnails);
        setMediaExtension("mp4");
    }

    public final void setContext(Context context) {
        this.context = context;
    }

    public void run() {
        try {
            manageDirectoryCache("mp4");
            if (this.filePath != null && this.filePath.startsWith("content:")) {
                this.filePath = getAbsoluteImagePathFromUri(Uri.parse(this.filePath));
            }
            if (this.filePath == null || TextUtils.isEmpty(this.filePath)) {
                if (this.listener != null) {
                    this.listener.onError("Couldn't process a null file");
                }
            } else if (this.filePath.startsWith("http")) {
                downloadAndProcess(this.filePath);
            } else if (this.filePath.startsWith("content://com.google.android.gallery3d") || this.filePath.startsWith("content://com.microsoft.skydrive.content.external")) {
                processPicasaMedia(this.filePath, ".mp4");
            } else if (this.filePath.startsWith("content://com.google.android.apps.photos.content") || this.filePath.startsWith("content://com.android.providers.media.documents") || this.filePath.startsWith("content://com.google.android.apps.docs.storage")) {
                processGooglePhotosMedia(this.filePath, ".mp4");
            } else if (this.filePath.startsWith("content://media/external/video")) {
                processContentProviderMedia(this.filePath, ".mp4");
            } else {
                process();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            if (this.listener != null) {
                this.listener.onError(e.getMessage());
            }
        }
    }

    protected final void process() throws ChooserException {
        super.process();
        if (this.shouldCreateThumnails) {
            createPreviewImage();
            String[] thumbnails = MediaProcessorThread.createThumbnails(createThumbnailOfVideo());
            processingDone(this.filePath, thumbnails[0], thumbnails[1]);
            return;
        }
        processingDone(this.filePath, this.filePath, this.filePath);
    }

    private String createPreviewImage() throws ChooserException {
        IOException e;
        Throwable th;
        this.previewImage = null;
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(this.filePath, 2);
        if (bitmap != null) {
            this.previewImage = FileUtils.getDirectory(this.foldername) + File.separator + Calendar.getInstance().getTimeInMillis() + ".jpg";
            FileOutputStream stream = null;
            try {
                FileOutputStream stream2 = new FileOutputStream(new File(this.previewImage));
                try {
                    bitmap.compress(CompressFormat.JPEG, 100, stream2);
                    StreamHelper.flush(stream2);
                } catch (IOException e2) {
                    e = e2;
                    stream = stream2;
                    try {
                        throw new ChooserException(e);
                    } catch (Throwable th2) {
                        th = th2;
                        StreamHelper.flush(stream);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    stream = stream2;
                    StreamHelper.flush(stream);
                    throw th;
                }
            } catch (IOException e3) {
                e = e3;
                throw new ChooserException(e);
            }
        }
        return this.previewImage;
    }

    private String createThumbnailOfVideo() throws ChooserException {
        IOException e;
        Throwable th;
        String thumbnailPath = null;
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(this.filePath, 1);
        if (bitmap != null) {
            thumbnailPath = FileUtils.getDirectory(this.foldername) + File.separator + Calendar.getInstance().getTimeInMillis() + ".jpg";
            FileOutputStream stream = null;
            try {
                FileOutputStream stream2 = new FileOutputStream(new File(thumbnailPath));
                try {
                    bitmap.compress(CompressFormat.JPEG, 100, stream2);
                    StreamHelper.flush(stream2);
                } catch (IOException e2) {
                    e = e2;
                    stream = stream2;
                    try {
                        throw new ChooserException(e);
                    } catch (Throwable th2) {
                        th = th2;
                        StreamHelper.flush(stream);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    stream = stream2;
                    StreamHelper.flush(stream);
                    throw th;
                }
            } catch (IOException e3) {
                e = e3;
                throw new ChooserException(e);
            }
        }
        return thumbnailPath;
    }

    protected final void processingDone(String original, String thumbnail, String thunbnailSmall) {
        if (this.listener != null) {
            ChosenVideo video = new ChosenVideo();
            video.videoFilePath = original;
            video.thumbnailPath = thumbnail;
            video.thumbnailSmallPath = thunbnailSmall;
            video.videoPreviewImage = this.previewImage;
            this.listener.onProcessedVideo(video);
        }
    }
}

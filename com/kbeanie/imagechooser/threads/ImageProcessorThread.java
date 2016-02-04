package com.kbeanie.imagechooser.threads;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.exceptions.ChooserException;

public final class ImageProcessorThread extends MediaProcessorThread {
    public ImageProcessorListener listener;

    public ImageProcessorThread(String filePath, String foldername, boolean shouldCreateThumbnails) {
        super(filePath, foldername, shouldCreateThumbnails);
        setMediaExtension("jpg");
    }

    public final void setContext(Context context) {
        this.context = context;
    }

    public final void run() {
        try {
            manageDirectoryCache("jpg");
            if (this.filePath != null && this.filePath.startsWith("content:")) {
                this.filePath = getAbsoluteImagePathFromUri(Uri.parse(this.filePath));
            }
            if (this.filePath == null || TextUtils.isEmpty(this.filePath)) {
                if (this.listener != null) {
                    this.listener.onError("Couldn't process a null file");
                }
            } else if (this.filePath.startsWith("http")) {
                downloadAndProcess(this.filePath);
            } else if (this.filePath.startsWith("content://com.google.android.gallery3d") || this.filePath.startsWith("content://com.microsoft.skydrive.content")) {
                processPicasaMedia(this.filePath, ".jpg");
            } else if (this.filePath.startsWith("content://com.google.android.apps.photos.content") || this.filePath.startsWith("content://com.android.providers.media.documents") || this.filePath.startsWith("content://com.google.android.apps.docs.storage") || this.filePath.startsWith("content://com.android.externalstorage.documents") || this.filePath.startsWith("content://com.android.internalstorage.documents")) {
                processGooglePhotosMedia(this.filePath, ".jpg");
            } else {
                process();
            }
        } catch (Exception e) {
            Log.e("ImageProcessorThread", e.getMessage(), e);
            if (this.listener != null) {
                this.listener.onError(e.getMessage());
            }
        }
    }

    protected final void process() throws ChooserException {
        super.process();
        if (this.shouldCreateThumnails) {
            String[] thumbnails = MediaProcessorThread.createThumbnails(this.filePath);
            processingDone(this.filePath, thumbnails[0], thumbnails[1]);
            return;
        }
        processingDone(this.filePath, this.filePath, this.filePath);
    }

    protected final void processingDone(String original, String thumbnail, String thunbnailSmall) {
        if (this.listener != null) {
            ChosenImage image = new ChosenImage();
            image.filePathOriginal = original;
            image.fileThumbnail = thumbnail;
            image.fileThumbnailSmall = thunbnailSmall;
            this.listener.onProcessedImage(image);
        }
    }
}

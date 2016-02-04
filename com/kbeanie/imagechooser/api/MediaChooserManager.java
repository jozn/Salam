package com.kbeanie.imagechooser.api;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.text.TextUtils;
import com.kbeanie.imagechooser.exceptions.ChooserException;
import com.kbeanie.imagechooser.threads.ImageProcessorListener;
import com.kbeanie.imagechooser.threads.ImageProcessorThread;
import com.kbeanie.imagechooser.threads.MediaProcessorThread;
import com.kbeanie.imagechooser.threads.VideoProcessorListener;
import com.kbeanie.imagechooser.threads.VideoProcessorThread;

public class MediaChooserManager extends BChooser implements ImageProcessorListener, VideoProcessorListener {
    private static final String TAG;
    public MediaChooserListener listener;

    static {
        TAG = MediaChooserManager.class.getSimpleName();
    }

    public MediaChooserManager(Activity activity) {
        super(activity, 300);
    }

    public final String choose() throws ChooserException {
        if (this.listener == null) {
            throw new ChooserException("MediaChooserListener cannot be null. Forgot to set MediaChooserListener???");
        }
        switch (this.type) {
            case 300:
                checkDirectory();
                try {
                    Intent intent = new Intent("android.intent.action.PICK");
                    if (this.extras != null) {
                        intent.putExtras(this.extras);
                    }
                    intent.setType("video/*, image/*");
                    startActivity(intent);
                    return null;
                } catch (ActivityNotFoundException e) {
                    throw new ChooserException(e);
                }
            default:
                throw new ChooserException("This chooser type is unappropriate with MediaChooserManager: " + this.type);
        }
    }

    public final void submit(int requestCode, Intent data) {
        if (requestCode != this.type) {
            onError("onActivityResult requestCode is different from the type the chooser was initialized with.");
            return;
        }
        switch (requestCode) {
            case 300:
                if (data == null || data.getDataString() == null) {
                    onError("Image Uri was null!");
                    return;
                }
                sanitizeURI(data.getData().toString());
                if (this.filePathOriginal == null || TextUtils.isEmpty(this.filePathOriginal)) {
                    onError("File path was null");
                    return;
                }
                MediaProcessorThread videoProcessorThread;
                String str = this.filePathOriginal;
                if (wasVideoSelected(data)) {
                    videoProcessorThread = new VideoProcessorThread(str, this.foldername, this.shouldCreateThumbnails);
                    ((VideoProcessorThread) videoProcessorThread).listener = this;
                } else {
                    videoProcessorThread = new ImageProcessorThread(str, this.foldername, this.shouldCreateThumbnails);
                    ((ImageProcessorThread) videoProcessorThread).listener = this;
                }
                videoProcessorThread.setContext(getContext());
                videoProcessorThread.start();
            default:
        }
    }

    public final void onProcessedImage(ChosenImage image) {
        if (this.listener != null) {
            this.listener.onImageChosen(image);
        }
    }

    public final void onProcessedVideo(ChosenVideo video) {
        if (this.listener != null) {
            this.listener.onVideoChosen(video);
        }
    }

    public final void onError(String reason) {
        if (this.listener != null) {
            this.listener.onError(reason);
        }
    }
}

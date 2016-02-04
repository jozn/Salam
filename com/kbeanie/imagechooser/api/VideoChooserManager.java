package com.kbeanie.imagechooser.api;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.kbeanie.imagechooser.exceptions.ChooserException;
import com.kbeanie.imagechooser.threads.VideoProcessorListener;
import com.kbeanie.imagechooser.threads.VideoProcessorThread;

public class VideoChooserManager extends BChooser implements VideoProcessorListener {
    private static final String TAG;
    public VideoChooserListener listener;

    static {
        TAG = VideoChooserManager.class.getSimpleName();
    }

    public VideoChooserManager(Activity activity) {
        super(activity, 292);
    }

    public VideoChooserManager(Activity activity, int type) {
        super(activity, type);
    }

    public final String choose() throws ChooserException {
        if (this.listener == null) {
            throw new ChooserException("ImageChooserListener cannot be null. Forgot to set ImageChooserListener???");
        }
        switch (this.type) {
            case 292:
                int i = VERSION.SDK_INT;
                return (i < 9 || i > 10) ? captureVideoCurrent() : captureVideoPatchedMethodForGingerbread();
            case 295:
                checkDirectory();
                try {
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    if (this.extras != null) {
                        intent.putExtras(this.extras);
                    }
                    intent.setType("video/*");
                    startActivity(intent);
                    return null;
                } catch (ActivityNotFoundException e) {
                    throw new ChooserException(e);
                }
            default:
                throw new ChooserException("Cannot choose an image in VideoChooserManager");
        }
    }

    private String captureVideoCurrent() throws ChooserException {
        checkDirectory();
        try {
            Intent intent = new Intent("android.media.action.VIDEO_CAPTURE");
            this.filePathOriginal = BChooser.buildFilePathOriginal(this.foldername, "mp4");
            intent.putExtra("output", BChooser.buildCaptureUri(this.filePathOriginal));
            if (this.extras != null) {
                intent.putExtras(this.extras);
            }
            startActivity(intent);
            return this.filePathOriginal;
        } catch (ActivityNotFoundException e) {
            throw new ChooserException(e);
        }
    }

    private String captureVideoPatchedMethodForGingerbread() throws ChooserException {
        try {
            Intent intent = new Intent("android.media.action.VIDEO_CAPTURE");
            if (this.extras != null) {
                intent.putExtras(this.extras);
            }
            startActivity(intent);
            return null;
        } catch (ActivityNotFoundException e) {
            throw new ChooserException(e);
        }
    }

    public final void submit$10b55c15(Intent data) {
        VideoProcessorThread videoProcessorThread;
        switch (this.type) {
            case 292:
                int i = VERSION.SDK_INT;
                String dataString = (i < 9 || i > 10) ? this.filePathOriginal : data.getDataString();
                videoProcessorThread = new VideoProcessorThread(dataString, this.foldername, this.shouldCreateThumbnails);
                videoProcessorThread.clearOldFiles(this.clearOldFiles);
                videoProcessorThread.listener = this;
                videoProcessorThread.setContext(getContext());
                videoProcessorThread.start();
            case 295:
                if (data != null && data.getDataString() != null) {
                    sanitizeURI(data.getData().toString());
                    if (this.filePathOriginal == null || TextUtils.isEmpty(this.filePathOriginal)) {
                        onError("File path was null");
                        return;
                    }
                    videoProcessorThread = new VideoProcessorThread(this.filePathOriginal, this.foldername, this.shouldCreateThumbnails);
                    videoProcessorThread.listener = this;
                    videoProcessorThread.setContext(getContext());
                    videoProcessorThread.start();
                }
            default:
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

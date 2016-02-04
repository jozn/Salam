package com.kbeanie.imagechooser.api;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.text.TextUtils;
import com.kbeanie.imagechooser.exceptions.ChooserException;
import com.kbeanie.imagechooser.threads.ImageProcessorListener;
import com.kbeanie.imagechooser.threads.ImageProcessorThread;

public class ImageChooserManager extends BChooser implements ImageProcessorListener {
    private static final String TAG;
    public ImageChooserListener listener;

    static {
        TAG = ImageChooserManager.class.getSimpleName();
    }

    public ImageChooserManager(Activity activity) {
        super(activity, 294);
    }

    @Deprecated
    public ImageChooserManager(Activity activity, int type, String foldername) {
        super(activity, type, foldername);
    }

    public final String choose() throws ChooserException {
        if (this.listener == null) {
            throw new ChooserException("ImageChooserListener cannot be null. Forgot to set ImageChooserListener???");
        }
        switch (this.type) {
            case 291:
                checkDirectory();
                try {
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    if (this.extras != null) {
                        intent.putExtras(this.extras);
                    }
                    startActivity(intent);
                    return null;
                } catch (ActivityNotFoundException e) {
                    throw new ChooserException(e);
                }
            case 294:
                return takePicture();
            default:
                throw new ChooserException("Cannot choose a video in ImageChooserManager");
        }
    }

    private String takePicture() throws ChooserException {
        checkDirectory();
        try {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            this.filePathOriginal = BChooser.buildFilePathOriginal(this.foldername, "jpg");
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

    public final void submit(int requestCode, Intent data) {
        if (requestCode != this.type) {
            onError("onActivityResult requestCode is different from the type the chooser was initialized with.");
            return;
        }
        ImageProcessorThread imageProcessorThread;
        switch (requestCode) {
            case 291:
                if (data == null || data.getDataString() == null) {
                    onError("Image Uri was null!");
                    return;
                }
                sanitizeURI(data.getData().toString());
                if (this.filePathOriginal == null || TextUtils.isEmpty(this.filePathOriginal)) {
                    onError("File path was null");
                    return;
                }
                imageProcessorThread = new ImageProcessorThread(this.filePathOriginal, this.foldername, this.shouldCreateThumbnails);
                imageProcessorThread.clearOldFiles(this.clearOldFiles);
                imageProcessorThread.listener = this;
                imageProcessorThread.setContext(getContext());
                imageProcessorThread.start();
            case 294:
                imageProcessorThread = new ImageProcessorThread(this.filePathOriginal, this.foldername, this.shouldCreateThumbnails);
                imageProcessorThread.listener = this;
                imageProcessorThread.start();
            default:
        }
    }

    public final void onProcessedImage(ChosenImage image) {
        if (this.listener != null) {
            this.listener.onImageChosen(image);
        }
    }

    public final void onError(String reason) {
        if (this.listener != null) {
            this.listener.onError(reason);
        }
    }
}

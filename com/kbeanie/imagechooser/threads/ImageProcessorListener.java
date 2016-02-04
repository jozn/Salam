package com.kbeanie.imagechooser.threads;

import com.kbeanie.imagechooser.api.ChosenImage;

public interface ImageProcessorListener {
    void onError(String str);

    void onProcessedImage(ChosenImage chosenImage);
}

package com.kbeanie.imagechooser.threads;

import com.kbeanie.imagechooser.api.ChosenVideo;

public interface VideoProcessorListener {
    void onError(String str);

    void onProcessedVideo(ChosenVideo chosenVideo);
}

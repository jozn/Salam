package com.kbeanie.imagechooser.api;

public interface VideoChooserListener {
    void onError(String str);

    void onVideoChosen(ChosenVideo chosenVideo);
}

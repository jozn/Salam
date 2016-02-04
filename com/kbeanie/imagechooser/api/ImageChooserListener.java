package com.kbeanie.imagechooser.api;

public interface ImageChooserListener {
    void onError(String str);

    void onImageChosen(ChosenImage chosenImage);
}

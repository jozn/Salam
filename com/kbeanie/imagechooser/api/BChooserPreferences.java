package com.kbeanie.imagechooser.api;

import android.content.Context;
import android.content.SharedPreferences;

public final class BChooserPreferences {
    SharedPreferences preferences;

    public BChooserPreferences(Context context) {
        this.preferences = context.getSharedPreferences("b_chooser_prefs", 0);
    }
}

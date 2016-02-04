package com.kbeanie.imagechooser.exceptions;

import android.content.ActivityNotFoundException;
import java.io.IOException;

public final class ChooserException extends Exception {
    public ChooserException(String msg) {
        super(msg);
    }

    public ChooserException(ActivityNotFoundException e) {
        super(e);
    }

    public ChooserException(IOException e) {
        super(e);
    }
}

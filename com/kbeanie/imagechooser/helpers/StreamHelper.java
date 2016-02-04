package com.kbeanie.imagechooser.helpers;

import android.os.ParcelFileDescriptor;
import com.kbeanie.imagechooser.exceptions.ChooserException;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamHelper {
    static final String TAG;

    static {
        TAG = StreamHelper.class.getSimpleName();
    }

    public static void close(Closeable stream) throws ChooserException {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                throw new ChooserException(e);
            }
        }
    }

    public static void flush(OutputStream stream) throws ChooserException {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                throw new ChooserException(e);
            }
        }
    }

    public static void verifyStream(String path, ParcelFileDescriptor descriptor) throws ChooserException {
        if (descriptor == null) {
            throw new ChooserException("Could not read file descriptor from file at path = " + path);
        }
    }

    public static void verifyStream(String path, InputStream is) throws ChooserException {
        if (is == null) {
            throw new ChooserException("Could not open stream to read path = " + path);
        }
    }
}

package com.kbeanie.imagechooser.api;

import android.os.Environment;
import android.support.v7.appcompat.BuildConfig;
import java.io.File;

public final class FileUtils {
    public static String getDirectory(String foldername) {
        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + foldername);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory.getAbsolutePath();
    }

    public static String getFileExtension(String filename) {
        String extension = BuildConfig.VERSION_NAME;
        try {
            extension = filename.substring(filename.lastIndexOf(".") + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return extension;
    }
}

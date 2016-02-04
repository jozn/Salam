package com.kbeanie.imagechooser.api;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.kbeanie.imagechooser.exceptions.ChooserException;
import com.kbeanie.imagechooser.factory.DateFactory;
import com.kbeanie.imagechooser.factory.UriFactory;
import java.io.File;
import java.util.Calendar;

public abstract class BChooser {
    static final String TAG;
    protected Activity activity;
    protected Fragment appFragment;
    protected boolean clearOldFiles;
    protected Bundle extras;
    protected String filePathOriginal;
    protected String foldername;
    protected android.support.v4.app.Fragment fragment;
    protected boolean shouldCreateThumbnails;
    protected int type;

    static {
        TAG = BChooser.class.getSimpleName();
    }

    @Deprecated
    public BChooser(Activity activity, int type, String folderName) {
        this.activity = activity;
        this.type = type;
        this.foldername = folderName;
        this.shouldCreateThumbnails = true;
    }

    public BChooser(Activity activity, int type) {
        this.activity = activity;
        this.type = type;
        this.shouldCreateThumbnails = true;
        this.foldername = new BChooserPreferences(activity.getApplicationContext()).preferences.getString("folder_name", "bichooser");
    }

    protected final void checkDirectory() throws ChooserException {
        File directory = new File(FileUtils.getDirectory(this.foldername));
        if (!directory.exists() && !directory.mkdirs() && !directory.isDirectory()) {
            throw new ChooserException("Error creating directory: " + directory);
        }
    }

    @SuppressLint({"NewApi"})
    protected final void startActivity(Intent intent) {
        if (this.activity != null) {
            this.activity.startActivityForResult(intent, this.type);
        } else if (this.fragment != null) {
            this.fragment.startActivityForResult(intent, this.type);
        } else if (this.appFragment != null) {
            this.appFragment.startActivityForResult(intent, this.type);
        }
    }

    public final void reinitialize(String path) {
        this.filePathOriginal = path;
    }

    protected final void sanitizeURI(String uri) {
        this.filePathOriginal = uri;
        if (uri.matches("https?://\\w+\\.googleusercontent\\.com/.+")) {
            this.filePathOriginal = uri;
        }
        if (uri.startsWith("file://")) {
            this.filePathOriginal = uri.substring(7);
        }
    }

    @SuppressLint({"NewApi"})
    protected final Context getContext() {
        if (this.activity != null) {
            return this.activity.getApplicationContext();
        }
        if (this.fragment != null) {
            return this.fragment.getActivity().getApplicationContext();
        }
        if (this.appFragment != null) {
            return this.appFragment.getActivity().getApplicationContext();
        }
        return null;
    }

    protected final boolean wasVideoSelected(Intent data) {
        if (data == null) {
            return false;
        }
        if (data.getType() != null && data.getType().startsWith("video")) {
            return true;
        }
        String type = getContext().getContentResolver().getType(data.getData());
        if (type == null || !type.startsWith("video")) {
            return false;
        }
        return true;
    }

    protected static String buildFilePathOriginal(String foldername, String extension) {
        UriFactory instance = UriFactory.getInstance();
        if (instance.filePathOriginal != null) {
            Log.d(UriFactory.TAG, "File path set. We return: " + instance.filePathOriginal);
            return instance.filePathOriginal;
        }
        long longValue;
        StringBuilder append = new StringBuilder().append(FileUtils.getDirectory(foldername)).append(File.separator);
        DateFactory instance2 = DateFactory.getInstance();
        if (instance2.timeInMillis != null) {
            Log.d(DateFactory.TAG, "Time set. Is: " + instance2.timeInMillis);
            longValue = instance2.timeInMillis.longValue();
        } else {
            longValue = Calendar.getInstance().getTimeInMillis();
        }
        return append.append(longValue).append(".").append(extension).toString();
    }

    protected static Uri buildCaptureUri(String filePathOriginal) {
        return Uri.fromFile(new File(filePathOriginal));
    }
}

package com.commonsware.cwac.updater;

import android.content.Context;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;

public class InternalHttpDownloadStrategy extends SimpleHttpDownloadStrategy {
    protected final File getDownloadFile(Context ctxt) {
        File updateDir = new File(ctxt.getFilesDir(), ".CWAC-Update");
        updateDir.mkdirs();
        return new File(updateDir, "update.apk");
    }

    protected final OutputStream openDownloadFile(Context ctxt, File apk) throws FileNotFoundException {
        return ctxt.openFileOutput("update.apk", 1);
    }
}

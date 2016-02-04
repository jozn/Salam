package com.appaholics.updatechecker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public final class DownloadManager extends AsyncTask<String, Integer, String> {
    private String TAG;
    private boolean downloaded;
    private boolean installAfterDownload;
    private Context mContext;
    private ProgressDialog progressDialog;

    protected final /* bridge */ /* synthetic */ void onPostExecute(Object obj) {
        this.progressDialog.dismiss();
        this.downloaded = true;
        if (this.installAfterDownload && this.downloaded) {
            Uri fromFile = Uri.fromFile(new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append("/appupdate.apk").toString()));
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(fromFile, "application/vnd.android.package-archive");
            this.mContext.startActivity(intent);
        }
    }

    protected final /* bridge */ /* synthetic */ void onProgressUpdate(Object... objArr) {
        this.progressDialog.setProgress(((Integer[]) objArr)[0].intValue());
    }

    public DownloadManager(Context context) {
        this.TAG = "UpdateDownloadManager";
        this.installAfterDownload = true;
        this.downloaded = false;
        this.mContext = context;
        this.installAfterDownload = true;
    }

    private boolean isOnline() {
        try {
            return ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    private String doInBackground(String... sUrl) {
        if (isOnline()) {
            try {
                URL url = new URL(sUrl[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                int fileLength = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append("/appupdate.apk").toString());
                byte[] data = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
                long total = 0;
                while (true) {
                    int count = input.read(data);
                    if (count == -1) {
                        break;
                    }
                    total += (long) count;
                    Integer[] numArr = new Integer[1];
                    numArr[0] = Integer.valueOf((int) ((100 * total) / ((long) fileLength)));
                    publishProgress(numArr);
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (IOException e) {
                Log.e(this.TAG, "There was an IOException when downloading the update file");
            }
        }
        return null;
    }

    protected final void onPreExecute() {
        this.progressDialog = new ProgressDialog(this.mContext);
        this.progressDialog.setProgressStyle(1);
        this.progressDialog.setMessage("Downloading ...");
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
    }
}

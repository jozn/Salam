package com.commonsware.cwac.updater;

import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.Log;
import com.commonsware.cwac.updater.UpdateRequest.Builder;
import com.commonsware.cwac.wakeful.WakefulIntentService;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;

public class UpdateService extends WakefulIntentService {
    public UpdateService() {
        super("UpdateService");
    }

    protected final void doWakefulWork(Intent cmd) {
        UpdateRequest req = new UpdateRequest(cmd);
        VersionCheckStrategy vcs = req.getVersionCheckStrategy();
        try {
            if (req.getPhase() == 3) {
                downloadAndInstall(cmd, req, req.getUpdateURL());
            } else if (req.getPhase() == 5) {
                install$467334be(req.getInstallUri());
            } else if (vcs.getVersionCode() > getPackageManager().getPackageInfo(getPackageName(), 0).versionCode) {
                ConfirmationStrategy strategy = req.getPreDownloadConfirmationStrategy();
                if (strategy != null) {
                    String updateURL = vcs.getUpdateURL();
                    Builder builder = new Builder(this, cmd);
                    builder.setPhase(3);
                    builder.setUpdateURL(updateURL);
                    if (!strategy.confirm(this, builder.buildPendingIntent())) {
                        return;
                    }
                }
                downloadAndInstall(cmd, req, vcs.getUpdateURL());
            }
        } catch (Exception e) {
            Log.e("CWAC-Update", "Exception in applying update", e);
        }
    }

    private void downloadAndInstall(Intent cmd, UpdateRequest req, String updateURL) throws Exception {
        Uri apk = req.getDownloadStrategy().downloadAPK(this, updateURL);
        if (apk != null) {
            ConfirmationStrategy preInstallConfirmationStrategy = req.getPreInstallConfirmationStrategy();
            if (preInstallConfirmationStrategy != null) {
                Builder builder = new Builder(this, cmd);
                builder.setPhase(5);
                builder.setInstallUri(apk);
                if (!preInstallConfirmationStrategy.confirm(this, builder.buildPendingIntent())) {
                    return;
                }
            }
            install$467334be(apk);
        }
    }

    private void install$467334be(Uri apk) {
        Intent i;
        if (VERSION.SDK_INT >= 14) {
            i = new Intent("android.intent.action.INSTALL_PACKAGE");
            i.putExtra("android.intent.extra.ALLOW_REPLACE", true);
        } else {
            i = new Intent("android.intent.action.VIEW");
        }
        i.setDataAndType(apk, "application/vnd.android.package-archive");
        i.setFlags(ClientDefaults.MAX_MSG_SIZE);
        startActivity(i);
    }
}

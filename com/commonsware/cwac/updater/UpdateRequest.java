package com.commonsware.cwac.updater;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public final class UpdateRequest {
    protected Intent cmd;

    public static class Builder {
        protected Intent cmd;
        protected Context ctxt;

        Builder(Context ctxt, Intent cmd) {
            this.ctxt = null;
            this.cmd = null;
            this.ctxt = ctxt;
            this.cmd = new Intent(cmd);
        }

        final void setPhase(int phase) {
            this.cmd.putExtra("com.commonsware.cwac.updater.EXTRA_PHASE", phase);
        }

        final void setUpdateURL(String updateURL) {
            this.cmd.putExtra("com.commonsware.cwac.updater.EXTRA_UPDATE_URL", updateURL);
        }

        final void setInstallUri(Uri apk) {
            this.cmd.putExtra("com.commonsware.cwac.updater.EXTRA_INSTALL_URI", apk.toString());
        }

        final PendingIntent buildPendingIntent() {
            Intent i = new Intent(this.ctxt, WakefulReceiver.class);
            i.putExtra("com.commonsware.cwac.updater.EXTRA_COMMAND", this.cmd);
            return PendingIntent.getBroadcast(this.ctxt, 0, i, 134217728);
        }
    }

    UpdateRequest(Intent cmd) {
        this.cmd = null;
        this.cmd = cmd;
    }

    final VersionCheckStrategy getVersionCheckStrategy() {
        return (VersionCheckStrategy) this.cmd.getParcelableExtra("com.commonsware.cwac.updater.EXTRA_VCS");
    }

    final ConfirmationStrategy getPreDownloadConfirmationStrategy() {
        return (ConfirmationStrategy) this.cmd.getParcelableExtra("com.commonsware.cwac.updater.EXTRA_CONFIRM_DOWNLOAD");
    }

    final ConfirmationStrategy getPreInstallConfirmationStrategy() {
        return (ConfirmationStrategy) this.cmd.getParcelableExtra("com.commonsware.cwac.updater.EXTRA_CONFIRM_INSTALL");
    }

    final DownloadStrategy getDownloadStrategy() {
        return (DownloadStrategy) this.cmd.getParcelableExtra("com.commonsware.cwac.updater.EXTRA_DS");
    }

    final int getPhase() {
        return this.cmd.getIntExtra("com.commonsware.cwac.updater.EXTRA_PHASE", 1);
    }

    final String getUpdateURL() {
        return this.cmd.getStringExtra("com.commonsware.cwac.updater.EXTRA_UPDATE_URL");
    }

    final Uri getInstallUri() {
        return Uri.parse(this.cmd.getStringExtra("com.commonsware.cwac.updater.EXTRA_INSTALL_URI"));
    }
}

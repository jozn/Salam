package com.rokhgroup.dialog;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import java.util.ArrayList;
import java.util.List;

public class DoodleDialogActivity extends FragmentActivity {
    private List<Dialog> mDialogs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mDialogs = new ArrayList();
    }

    public void startManageDialog(Dialog dialog) {
        this.mDialogs.add(dialog);
    }

    public void stopManageDialog(Dialog dialog) {
        this.mDialogs.remove(dialog);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dismissDialogs();
    }

    protected void onDestroy() {
        dismissDialogs();
        super.onDestroy();
    }

    protected void dismissDialogs() {
        while (this.mDialogs.size() > 0) {
            Dialog dialog = (Dialog) this.mDialogs.get(0);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                this.mDialogs.remove(dialog);
            }
        }
    }
}

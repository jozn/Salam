package com.shamchat.activity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProgressBarDialogLogin extends DialogFragment {
    private static ProgressBarDialogLogin instance;
    private TextView txtMessage;

    static {
        instance = new ProgressBarDialogLogin();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(2130903142, container);
        getDialog().requestWindowFeature(1);
        getDialog().getWindow().setGravity(17);
        getDialog().getWindow().addFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        setCancelable(false);
        return view;
    }

    public static ProgressBarDialogLogin getInstance() {
        return instance;
    }

    public void setMessage(String message) {
        instance.txtMessage.setText(message);
    }
}

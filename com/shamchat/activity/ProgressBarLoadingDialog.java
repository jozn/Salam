package com.shamchat.activity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProgressBarLoadingDialog extends DialogFragment {
    private static ProgressBarLoadingDialog instance;
    private TextView txtMessage;

    static {
        instance = new ProgressBarLoadingDialog();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(2130903142, container);
        this.txtMessage = (TextView) view.findViewById(2131361925);
        getDialog().requestWindowFeature(1);
        getDialog().getWindow().setGravity(17);
        setCancelable(true);
        getDialog().getWindow().addFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        return view;
    }

    public static ProgressBarLoadingDialog getInstance() {
        return instance;
    }

    public void setMessage(String message) {
        if (this.txtMessage != null) {
            instance.txtMessage.setText(message);
        }
    }
}

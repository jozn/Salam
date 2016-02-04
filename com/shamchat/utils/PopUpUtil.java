package com.shamchat.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public final class PopUpUtil {

    /* renamed from: com.shamchat.utils.PopUpUtil.1 */
    class C11731 implements OnClickListener {
        final /* synthetic */ Dialog val$popUp;

        C11731(Dialog dialog) {
            this.val$popUp = dialog;
        }

        public final void onClick(View v) {
            this.val$popUp.dismiss();
        }
    }

    public final Dialog getPopFailed$40a28a58(Context context, String message) {
        View view = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(2130903204, null);
        Dialog popUp = new Dialog(context, 16973840);
        popUp.show();
        popUp.setContentView(view);
        ((TextView) view.findViewById(2131362424)).setText(message);
        ((Button) view.findViewById(2131362422)).setOnClickListener(new C11731(popUp));
        return popUp;
    }

    public static Dialog getPopFailed$478dbc03(Context context, String message, OnClickListener onOkClick) {
        View view = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(2130903204, null);
        Dialog popUp = new Dialog(context, 16973840);
        try {
            popUp.show();
            popUp.setContentView(view);
            ((TextView) view.findViewById(2131362424)).setText(message);
            ((Button) view.findViewById(2131362422)).setOnClickListener(onOkClick);
        } catch (Exception e) {
        }
        return popUp;
    }
}

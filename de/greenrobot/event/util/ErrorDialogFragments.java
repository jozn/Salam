package de.greenrobot.event.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public final class ErrorDialogFragments {
    public static int ERROR_DIALOG_ICON;
    public static Class<?> EVENT_TYPE_ON_CLICK;

    @TargetApi(11)
    public static class Honeycomb extends DialogFragment implements OnClickListener {
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return ErrorDialogFragments.createDialog(getActivity(), getArguments(), this);
        }

        public void onClick(DialogInterface dialog, int which) {
            ErrorDialogFragments.handleOnClick$40307cf0(getActivity(), getArguments());
        }
    }

    public static class Support extends android.support.v4.app.DialogFragment implements OnClickListener {
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return ErrorDialogFragments.createDialog(getActivity(), getArguments(), this);
        }

        public void onClick(DialogInterface dialog, int which) {
            ErrorDialogFragments.handleOnClick$40307cf0(getActivity(), getArguments());
        }
    }

    static {
        ERROR_DIALOG_ICON = 0;
    }

    public static Dialog createDialog(Context context, Bundle arguments, OnClickListener onClickListener) {
        Builder builder = new Builder(context);
        builder.setTitle(arguments.getString("de.greenrobot.eventbus.errordialog.title"));
        builder.setMessage(arguments.getString("de.greenrobot.eventbus.errordialog.message"));
        if (ERROR_DIALOG_ICON != 0) {
            builder.setIcon(ERROR_DIALOG_ICON);
        }
        builder.setPositiveButton(17039370, onClickListener);
        return builder.create();
    }

    public static void handleOnClick$40307cf0(Activity activity, Bundle arguments) {
        if (EVENT_TYPE_ON_CLICK != null) {
            try {
                ErrorDialogManager.factory.config.getEventBus().post(EVENT_TYPE_ON_CLICK.newInstance());
            } catch (Exception e) {
                throw new RuntimeException("Event cannot be constructed", e);
            }
        }
        if (arguments.getBoolean("de.greenrobot.eventbus.errordialog.finish_after_dialog", false) && activity != null) {
            activity.finish();
        }
    }
}

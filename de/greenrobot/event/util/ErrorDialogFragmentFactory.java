package de.greenrobot.event.util;

import android.os.Bundle;
import android.util.Log;
import de.greenrobot.event.EventBus;

public abstract class ErrorDialogFragmentFactory<T> {
    protected final ErrorDialogConfig config;

    protected abstract T createErrorFragment$29ecaad0();

    protected final T prepareErrorFragment(ThrowableFailureEvent event, boolean finishAfterDialog, Bundle argumentsForErrorDialog) {
        if (event.isSuppressErrorUi()) {
            return null;
        }
        Bundle bundle;
        if (argumentsForErrorDialog != null) {
            bundle = (Bundle) argumentsForErrorDialog.clone();
        } else {
            bundle = new Bundle();
        }
        if (!bundle.containsKey("de.greenrobot.eventbus.errordialog.title")) {
            bundle.putString("de.greenrobot.eventbus.errordialog.title", this.config.resources.getString(this.config.defaultTitleId));
        }
        if (!bundle.containsKey("de.greenrobot.eventbus.errordialog.message")) {
            int intValue;
            ErrorDialogConfig errorDialogConfig = this.config;
            Throwable th = event.throwable;
            Integer mapThrowable = errorDialogConfig.mapping.mapThrowable(th);
            if (mapThrowable != null) {
                intValue = mapThrowable.intValue();
            } else {
                Log.d(EventBus.TAG, "No specific message ressource ID found for " + th);
                intValue = errorDialogConfig.defaultErrorMsgId;
            }
            bundle.putString("de.greenrobot.eventbus.errordialog.message", this.config.resources.getString(intValue));
        }
        if (!bundle.containsKey("de.greenrobot.eventbus.errordialog.finish_after_dialog")) {
            bundle.putBoolean("de.greenrobot.eventbus.errordialog.finish_after_dialog", finishAfterDialog);
        }
        if (!(bundle.containsKey("de.greenrobot.eventbus.errordialog.event_type_on_close") || this.config.defaultEventTypeOnDialogClosed == null)) {
            bundle.putSerializable("de.greenrobot.eventbus.errordialog.event_type_on_close", this.config.defaultEventTypeOnDialogClosed);
        }
        if (!(bundle.containsKey("de.greenrobot.eventbus.errordialog.icon_id") || this.config.defaultDialogIconId == 0)) {
            bundle.putInt("de.greenrobot.eventbus.errordialog.icon_id", this.config.defaultDialogIconId);
        }
        return createErrorFragment$29ecaad0();
    }
}

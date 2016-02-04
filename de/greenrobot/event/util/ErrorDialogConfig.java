package de.greenrobot.event.util;

import android.content.res.Resources;
import de.greenrobot.event.EventBus;

public final class ErrorDialogConfig {
    int defaultDialogIconId;
    final int defaultErrorMsgId;
    Class<?> defaultEventTypeOnDialogClosed;
    final int defaultTitleId;
    EventBus eventBus;
    boolean logExceptions;
    final ExceptionToResourceMapping mapping;
    final Resources resources;
    String tagForLoggingExceptions;

    final EventBus getEventBus() {
        return this.eventBus != null ? this.eventBus : EventBus.getDefault();
    }
}

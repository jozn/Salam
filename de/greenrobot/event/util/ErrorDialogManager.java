package de.greenrobot.event.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import de.greenrobot.event.EventBus;

public final class ErrorDialogManager {
    public static ErrorDialogFragmentFactory<?> factory;

    @TargetApi(11)
    public static class HoneycombManagerFragment extends Fragment {
        protected Bundle argumentsForErrorDialog;
        private EventBus eventBus;
        private Object executionScope;
        protected boolean finishAfterDialog;

        public void onResume() {
            super.onResume();
            this.eventBus = ErrorDialogManager.factory.config.getEventBus();
            this.eventBus.register(this, false, 0);
        }

        public void onPause() {
            this.eventBus.unregister(this);
            super.onPause();
        }

        public void onEventMainThread(ThrowableFailureEvent event) {
            if (ErrorDialogManager.access$000(this.executionScope, event)) {
                ErrorDialogManager.checkLogException(event);
                FragmentManager fm = getFragmentManager();
                fm.executePendingTransactions();
                DialogFragment existingFragment = (DialogFragment) fm.findFragmentByTag("de.greenrobot.eventbus.error_dialog");
                if (existingFragment != null) {
                    existingFragment.dismiss();
                }
                DialogFragment errorFragment = (DialogFragment) ErrorDialogManager.factory.prepareErrorFragment(event, this.finishAfterDialog, this.argumentsForErrorDialog);
                if (errorFragment != null) {
                    errorFragment.show(fm, "de.greenrobot.eventbus.error_dialog");
                }
            }
        }
    }

    public static class SupportManagerFragment extends android.support.v4.app.Fragment {
        protected Bundle argumentsForErrorDialog;
        private EventBus eventBus;
        private Object executionScope;
        protected boolean finishAfterDialog;
        private boolean skipRegisterOnNextResume;

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.eventBus = ErrorDialogManager.factory.config.getEventBus();
            this.eventBus.register(this, false, 0);
            this.skipRegisterOnNextResume = true;
        }

        public void onResume() {
            super.onResume();
            if (this.skipRegisterOnNextResume) {
                this.skipRegisterOnNextResume = false;
                return;
            }
            this.eventBus = ErrorDialogManager.factory.config.getEventBus();
            this.eventBus.register(this, false, 0);
        }

        public void onPause() {
            this.eventBus.unregister(this);
            super.onPause();
        }

        public void onEventMainThread(ThrowableFailureEvent event) {
            if (ErrorDialogManager.access$000(this.executionScope, event)) {
                ErrorDialogManager.checkLogException(event);
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                fm.executePendingTransactions();
                android.support.v4.app.DialogFragment existingFragment = (android.support.v4.app.DialogFragment) fm.findFragmentByTag("de.greenrobot.eventbus.error_dialog");
                if (existingFragment != null) {
                    existingFragment.dismiss();
                }
                android.support.v4.app.DialogFragment errorFragment = (android.support.v4.app.DialogFragment) ErrorDialogManager.factory.prepareErrorFragment(event, this.finishAfterDialog, this.argumentsForErrorDialog);
                if (errorFragment != null) {
                    errorFragment.show(fm, "de.greenrobot.eventbus.error_dialog");
                }
            }
        }

        public static void attachTo(Activity activity, Object executionScope, boolean finishAfterDialog, Bundle argumentsForErrorDialog) {
            android.support.v4.app.FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();
            SupportManagerFragment fragment = (SupportManagerFragment) fm.findFragmentByTag("de.greenrobot.eventbus.error_dialog_manager");
            if (fragment == null) {
                fragment = new SupportManagerFragment();
                fm.beginTransaction().add((android.support.v4.app.Fragment) fragment, "de.greenrobot.eventbus.error_dialog_manager").commit();
                fm.executePendingTransactions();
            }
            fragment.finishAfterDialog = finishAfterDialog;
            fragment.argumentsForErrorDialog = argumentsForErrorDialog;
            fragment.executionScope = executionScope;
        }
    }

    static /* synthetic */ boolean access$000(Object x0, ThrowableFailureEvent x1) {
        if (x1 != null) {
            Object obj = x1.executionContext;
            if (!(obj == null || obj.equals(x0))) {
                return false;
            }
        }
        return true;
    }

    protected static void checkLogException(ThrowableFailureEvent event) {
        if (factory.config.logExceptions) {
            String tag = factory.config.tagForLoggingExceptions;
            if (tag == null) {
                tag = EventBus.TAG;
            }
            Log.i(tag, "Error dialog manager received exception", event.throwable);
        }
    }
}

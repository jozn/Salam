package com.shamchat.androidclient.service;

import android.app.Notification;
import android.app.Service;
import android.util.Log;

abstract class ServiceNotification {

    private static class EclairAndBeyond extends ServiceNotification {

        private static class Holder {
            private static final EclairAndBeyond sInstance;

            static {
                sInstance = new EclairAndBeyond();
            }
        }

        private EclairAndBeyond() {
        }

        public final void showNotification(Service context, int id, Notification n) {
            Log.d("EclairAndBeyond", "showNotification " + id + " " + n);
            context.startForeground(id, n);
        }

        public final void hideNotification$4289b6e2(Service context) {
            Log.d("EclairAndBeyond", "hideNotification");
            context.stopForeground(true);
        }
    }

    public abstract void hideNotification$4289b6e2(Service service);

    public abstract void showNotification(Service service, int i, Notification notification);

    ServiceNotification() {
    }
}

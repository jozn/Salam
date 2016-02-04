package com.commonsware.cwac.wakeful;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.XmlResourceParser;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.commonsware.cwac.wakeful.WakefulIntentService.AlarmListener;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

public class AlarmReceiver extends BroadcastReceiver {
    public void onReceive(Context ctxt, Intent intent) {
        if (getListener(ctxt) == null) {
            return;
        }
        if (intent.getAction() == null) {
            ctxt.getSharedPreferences("com.commonsware.cwac.wakeful.WakefulIntentService", 0).edit().putLong("lastAlarm", System.currentTimeMillis()).commit();
        } else {
            WakefulIntentService.scheduleAlarms$779ace80(ctxt);
        }
    }

    private AlarmListener getListener(Context ctxt) {
        PackageManager pm = ctxt.getPackageManager();
        try {
            XmlResourceParser xpp = pm.getReceiverInfo(new ComponentName(ctxt, getClass()), AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS).loadXmlMetaData(pm, "com.commonsware.cwac.wakeful");
            while (xpp.getEventType() != 1) {
                if (xpp.getEventType() == 2 && xpp.getName().equals("WakefulIntentService")) {
                    return (AlarmListener) Class.forName(xpp.getAttributeValue(null, "listener")).newInstance();
                }
                xpp.next();
            }
            return null;
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Cannot find own info???", e);
        } catch (XmlPullParserException e2) {
            throw new RuntimeException("Malformed metadata resource XML", e2);
        } catch (IOException e3) {
            throw new RuntimeException("Could not read resource XML", e3);
        } catch (ClassNotFoundException e4) {
            throw new RuntimeException("Listener class not found", e4);
        } catch (IllegalAccessException e5) {
            throw new RuntimeException("Listener is not public or lacks public constructor", e5);
        } catch (InstantiationException e6) {
            throw new RuntimeException("Could not create instance of listener", e6);
        }
    }
}

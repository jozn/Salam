package com.shamchat.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.appcompat.BuildConfig;
import android.telephony.SmsMessage;
import android.util.Log;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;

public class SmsReceiver extends BroadcastReceiver {
    public String str;

    public SmsReceiver() {
        this.str = BuildConfig.VERSION_NAME;
    }

    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                String msg_from = msgs[i].getOriginatingAddress();
                Log.v("msg_from >>", msg_from);
                if (msg_from.equals("+9810009699")) {
                    this.str += "SMS from " + msgs[i].getOriginatingAddress();
                    this.str += " :";
                    this.str += msgs[i].getMessageBody().toString();
                    this.str += "\n";
                }
            }
            Intent act = new Intent(context, VerifyAccountActivity.class);
            act.addFlags(ClientDefaults.MAX_MSG_SIZE);
            act.putExtra(AddFavoriteTextActivity.EXTRA_MESSAGE, this.str);
            context.startActivity(act);
        }
        abortBroadcast();
    }
}

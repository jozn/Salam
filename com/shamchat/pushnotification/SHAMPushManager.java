package com.shamchat.pushnotification;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import com.arellomobile.android.push.BasePushMessageReceiver;
import com.arellomobile.android.push.PushManager;
import com.arellomobile.android.push.utils.RegisterBroadcastReceiver;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.service.SmackableImp;
import com.shamchat.androidclient.service.XMPPService;
import com.shamchat.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;

public final class SHAMPushManager {
    public Context context;
    private FragmentManager fragmentManager;
    public BroadcastReceiver mBroadcastReceiver;
    public BroadcastReceiver mReceiver;
    private PushManager pushManager;

    /* renamed from: com.shamchat.pushnotification.SHAMPushManager.1 */
    class C11641 extends RegisterBroadcastReceiver {
        C11641() {
        }

        public final void onRegisterActionReceive$3b2d1350(Intent intent) {
            SHAMPushManager.this.checkMessage(intent);
        }
    }

    /* renamed from: com.shamchat.pushnotification.SHAMPushManager.2 */
    class C11652 extends BasePushMessageReceiver {
        C11652() {
        }

        public final void onReceive(Context context, Intent intent) {
            System.out.println("PUSH onReceive ");
            System.out.println("PUSH onReceive push message is " + intent.getExtras().getString("pw_data_json_string"));
            try {
                new JSONObject(intent.getExtras().getString("pw_data_json_string")).getString("title");
                System.out.println("PUSH onMessageReceive title is " + intent.getExtras().getString("title"));
                if (!SmackableImp.isXmppConnected() && Utils.isInternetAvailable(context)) {
                    Intent xmppServiceIntent = new Intent(context, XMPPService.class);
                    xmppServiceIntent.setAction("reconnect");
                    context.startService(xmppServiceIntent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String citUserId = SHAMChatApplication.getConfig().userId;
            if (citUserId != null && citUserId.length() > 0) {
                SHAMPushManager.this.checkAndSendTagsIfWeCan(citUserId);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public SHAMPushManager(android.content.Context r15, android.support.v4.app.FragmentManager r16) {
        /*
        r14 = this;
        r3 = 0;
        r14.<init>();
        r2 = new com.shamchat.pushnotification.SHAMPushManager$1;
        r2.<init>();
        r14.mBroadcastReceiver = r2;
        r2 = new com.shamchat.pushnotification.SHAMPushManager$2;
        r2.<init>();
        r14.mReceiver = r2;
        r14.context = r15;
        r0 = r16;
        r14.fragmentManager = r0;
        r14.registerReceivers();
        r2 = new com.arellomobile.android.push.PushManager;
        r4 = "34922-D91F3";
        r5 = "913691173954";
        r2.<init>(r15, r4, r5);
        r14.pushManager = r2;
        r4 = r14.pushManager;	 Catch:{ Exception -> 0x0075 }
        r2 = r4.pushRegistrar;	 Catch:{ Exception -> 0x0075 }
        r2.checkDevice(r15);	 Catch:{ Exception -> 0x0075 }
        r2 = new android.os.Handler;	 Catch:{ Exception -> 0x0075 }
        r5 = r15.getMainLooper();	 Catch:{ Exception -> 0x0075 }
        r2.<init>(r5);	 Catch:{ Exception -> 0x0075 }
        r5 = new com.arellomobile.android.push.PushManager$6;	 Catch:{ Exception -> 0x0075 }
        r5.<init>(r15);	 Catch:{ Exception -> 0x0075 }
        r2.post(r5);	 Catch:{ Exception -> 0x0075 }
        r5 = com.google.android.gcm.GCMRegistrar.getRegistrationId(r15);	 Catch:{ Exception -> 0x0075 }
        r2 = "";
        r2 = r5.equals(r2);	 Catch:{ Exception -> 0x0075 }
        if (r2 == 0) goto L_0x0059;
    L_0x004a:
        r2 = r4.pushRegistrar;	 Catch:{ Exception -> 0x0075 }
        r2.registerPW(r15);	 Catch:{ Exception -> 0x0075 }
    L_0x004f:
        r15 = (android.app.Activity) r15;
        r2 = r15.getIntent();
        r14.checkMessage(r2);
        return;
    L_0x0059:
        r2 = r15 instanceof android.app.Activity;	 Catch:{ Exception -> 0x0075 }
        if (r2 == 0) goto L_0x006d;
    L_0x005d:
        r0 = r15;
        r0 = (android.app.Activity) r0;	 Catch:{ Exception -> 0x0075 }
        r2 = r0;
        r2 = r2.getIntent();	 Catch:{ Exception -> 0x0075 }
        r6 = "PUSH_RECEIVE_EVENT";
        r2 = r2.hasExtra(r6);	 Catch:{ Exception -> 0x0075 }
        if (r2 != 0) goto L_0x004f;
    L_0x006d:
        r2 = r4.forceRegister;	 Catch:{ Exception -> 0x0075 }
        if (r2 == 0) goto L_0x0077;
    L_0x0071:
        r4.registerOnPushWoosh(r15, r5);	 Catch:{ Exception -> 0x0075 }
        goto L_0x004f;
    L_0x0075:
        r2 = move-exception;
        goto L_0x004f;
    L_0x0077:
        r2 = java.util.Calendar.getInstance();	 Catch:{ Exception -> 0x0075 }
        r6 = java.util.Calendar.getInstance();	 Catch:{ Exception -> 0x0075 }
        r7 = 12;
        r8 = -10;
        r6.add(r7, r8);	 Catch:{ Exception -> 0x0075 }
        r7 = java.util.Calendar.getInstance();	 Catch:{ Exception -> 0x0075 }
        r8 = new java.util.Date;	 Catch:{ Exception -> 0x0075 }
        r9 = "com.pushwoosh.pushnotifications";
        r10 = 0;
        r9 = r15.getSharedPreferences(r9, r10);	 Catch:{ Exception -> 0x0075 }
        r10 = "last_registration_change";
        r12 = 0;
        r10 = r9.getLong(r10, r12);	 Catch:{ Exception -> 0x0075 }
        r8.<init>(r10);	 Catch:{ Exception -> 0x0075 }
        r7.setTime(r8);	 Catch:{ Exception -> 0x0075 }
        r6 = r6.before(r7);	 Catch:{ Exception -> 0x0075 }
        if (r6 == 0) goto L_0x00b4;
    L_0x00a7:
        r2 = r7.before(r2);	 Catch:{ Exception -> 0x0075 }
        if (r2 == 0) goto L_0x00b4;
    L_0x00ad:
        r2 = r3;
    L_0x00ae:
        if (r2 == 0) goto L_0x00b6;
    L_0x00b0:
        r4.registerOnPushWoosh(r15, r5);	 Catch:{ Exception -> 0x0075 }
        goto L_0x004f;
    L_0x00b4:
        r2 = 1;
        goto L_0x00ae;
    L_0x00b6:
        com.arellomobile.android.push.PushEventsTransmitter.onRegistered(r15, r5);	 Catch:{ Exception -> 0x0075 }
        goto L_0x004f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shamchat.pushnotification.SHAMPushManager.<init>(android.content.Context, android.support.v4.app.FragmentManager):void");
    }

    public final void registerReceivers() {
        this.context.registerReceiver(this.mReceiver, new IntentFilter(this.context.getPackageName() + ".action.PUSH_MESSAGE_RECEIVE"));
        this.context.registerReceiver(this.mBroadcastReceiver, new IntentFilter(this.context.getPackageName() + ".com.arellomobile.android.push.REGISTER_BROAD_CAST_ACTION"));
        String citUserId = SHAMChatApplication.getConfig().userId;
        if (citUserId != null && citUserId.length() > 0) {
            checkAndSendTagsIfWeCan(citUserId);
        }
    }

    final void checkMessage(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra("PUSH_RECEIVE_EVENT")) {
                System.out.println("PUSH_RECEIVE_EVENT");
            } else if (intent.hasExtra("REGISTER_EVENT")) {
                System.out.println("PUSH REGISTERD PUSHWOOSH");
            } else if (intent.hasExtra("UNREGISTER_EVENT")) {
                System.out.println("PUSH UNREGISTERED PUSHWOOSH");
            } else if (intent.hasExtra("REGISTER_ERROR_EVENT")) {
                System.out.println("PUSH REGISTER ERROR PUSHWOOSH");
            } else if (intent.hasExtra("UNREGISTER_ERROR_EVENT")) {
                System.out.println("PUSH UNREGISTER ERROR PUSHWOOSH");
            }
            Intent intent2 = ((Activity) this.context).getIntent();
            if (intent2.hasExtra("PUSH_RECEIVE_EVENT")) {
                intent2.removeExtra("PUSH_RECEIVE_EVENT");
            } else if (intent2.hasExtra("REGISTER_EVENT")) {
                intent2.removeExtra("REGISTER_EVENT");
            } else if (intent2.hasExtra("UNREGISTER_EVENT")) {
                intent2.removeExtra("UNREGISTER_EVENT");
            } else if (intent2.hasExtra("REGISTER_ERROR_EVENT")) {
                intent2.removeExtra("REGISTER_ERROR_EVENT");
            } else if (intent2.hasExtra("UNREGISTER_ERROR_EVENT")) {
                intent2.removeExtra("UNREGISTER_ERROR_EVENT");
            }
            ((Activity) this.context).setIntent(intent2);
        }
    }

    public final void checkAndSendTagsIfWeCan(String citUserId) {
        SendTagsFragment sendTagsFragment;
        System.out.println("PUSH checkAndSendTagsIfWeCan citUserId " + citUserId);
        this.fragmentManager = ((FragmentActivity) this.context).getSupportFragmentManager();
        SendTagsFragment sendTagsFragment2 = (SendTagsFragment) this.fragmentManager.findFragmentByTag("send_tags_status_fragment_tag");
        if (sendTagsFragment2 == null) {
            sendTagsFragment = new SendTagsFragment();
            sendTagsFragment.setRetainInstance(true);
            this.fragmentManager.beginTransaction().add((Fragment) sendTagsFragment, "send_tags_status_fragment_tag").commit();
            this.fragmentManager.executePendingTransactions();
        } else {
            sendTagsFragment = sendTagsFragment2;
        }
        if (sendTagsFragment.canSendTags()) {
            sendTagsFragment.submitTags(this.context, citUserId);
        }
    }
}

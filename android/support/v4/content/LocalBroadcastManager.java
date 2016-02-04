package android.support.v4.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.util.ArrayList;
import java.util.HashMap;

public class LocalBroadcastManager {
    private static final boolean DEBUG = false;
    static final int MSG_EXEC_PENDING_BROADCASTS = 1;
    private static final String TAG = "LocalBroadcastManager";
    private static LocalBroadcastManager mInstance;
    private static final Object mLock;
    private final HashMap<String, ArrayList<ReceiverRecord>> mActions;
    private final Context mAppContext;
    private final Handler mHandler;
    private final ArrayList<BroadcastRecord> mPendingBroadcasts;
    private final HashMap<BroadcastReceiver, ArrayList<IntentFilter>> mReceivers;

    /* renamed from: android.support.v4.content.LocalBroadcastManager.1 */
    class C00251 extends Handler {
        C00251(Looper x0) {
            super(x0);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LocalBroadcastManager.MSG_EXEC_PENDING_BROADCASTS /*1*/:
                    LocalBroadcastManager.this.executePendingBroadcasts();
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private static class BroadcastRecord {
        final Intent intent;
        final ArrayList<ReceiverRecord> receivers;

        BroadcastRecord(Intent _intent, ArrayList<ReceiverRecord> _receivers) {
            this.intent = _intent;
            this.receivers = _receivers;
        }
    }

    private static class ReceiverRecord {
        boolean broadcasting;
        final IntentFilter filter;
        final BroadcastReceiver receiver;

        ReceiverRecord(IntentFilter _filter, BroadcastReceiver _receiver) {
            this.filter = _filter;
            this.receiver = _receiver;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder(AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
            builder.append("Receiver{");
            builder.append(this.receiver);
            builder.append(" filter=");
            builder.append(this.filter);
            builder.append("}");
            return builder.toString();
        }
    }

    static {
        mLock = new Object();
    }

    public static LocalBroadcastManager getInstance(Context context) {
        LocalBroadcastManager localBroadcastManager;
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new LocalBroadcastManager(context.getApplicationContext());
            }
            localBroadcastManager = mInstance;
        }
        return localBroadcastManager;
    }

    private LocalBroadcastManager(Context context) {
        this.mReceivers = new HashMap();
        this.mActions = new HashMap();
        this.mPendingBroadcasts = new ArrayList();
        this.mAppContext = context;
        this.mHandler = new C00251(context.getMainLooper());
    }

    public void registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        synchronized (this.mReceivers) {
            ReceiverRecord entry = new ReceiverRecord(filter, receiver);
            ArrayList<IntentFilter> filters = (ArrayList) this.mReceivers.get(receiver);
            if (filters == null) {
                filters = new ArrayList(MSG_EXEC_PENDING_BROADCASTS);
                this.mReceivers.put(receiver, filters);
            }
            filters.add(filter);
            for (int i = 0; i < filter.countActions(); i += MSG_EXEC_PENDING_BROADCASTS) {
                String action = filter.getAction(i);
                ArrayList<ReceiverRecord> entries = (ArrayList) this.mActions.get(action);
                if (entries == null) {
                    entries = new ArrayList(MSG_EXEC_PENDING_BROADCASTS);
                    this.mActions.put(action, entries);
                }
                entries.add(entry);
            }
        }
    }

    public void unregisterReceiver(BroadcastReceiver receiver) {
        synchronized (this.mReceivers) {
            ArrayList<IntentFilter> filters = (ArrayList) this.mReceivers.remove(receiver);
            if (filters == null) {
                return;
            }
            for (int i = 0; i < filters.size(); i += MSG_EXEC_PENDING_BROADCASTS) {
                IntentFilter filter = (IntentFilter) filters.get(i);
                for (int j = 0; j < filter.countActions(); j += MSG_EXEC_PENDING_BROADCASTS) {
                    String action = filter.getAction(j);
                    ArrayList<ReceiverRecord> receivers = (ArrayList) this.mActions.get(action);
                    if (receivers != null) {
                        int k = 0;
                        while (k < receivers.size()) {
                            if (((ReceiverRecord) receivers.get(k)).receiver == receiver) {
                                receivers.remove(k);
                                k--;
                            }
                            k += MSG_EXEC_PENDING_BROADCASTS;
                        }
                        if (receivers.size() <= 0) {
                            this.mActions.remove(action);
                        }
                    }
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean sendBroadcast(android.content.Intent r18) {
        /*
        r17 = this;
        r0 = r17;
        r15 = r0.mReceivers;
        monitor-enter(r15);
        r2 = r18.getAction();	 Catch:{ all -> 0x00fb }
        r0 = r17;
        r1 = r0.mAppContext;	 Catch:{ all -> 0x00fb }
        r1 = r1.getContentResolver();	 Catch:{ all -> 0x00fb }
        r0 = r18;
        r3 = r0.resolveTypeIfNeeded(r1);	 Catch:{ all -> 0x00fb }
        r5 = r18.getData();	 Catch:{ all -> 0x00fb }
        r4 = r18.getScheme();	 Catch:{ all -> 0x00fb }
        r6 = r18.getCategories();	 Catch:{ all -> 0x00fb }
        r1 = r18.getFlags();	 Catch:{ all -> 0x00fb }
        r1 = r1 & 8;
        if (r1 == 0) goto L_0x00c2;
    L_0x002b:
        r8 = 1;
    L_0x002c:
        if (r8 == 0) goto L_0x005e;
    L_0x002e:
        r1 = "LocalBroadcastManager";
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00fb }
        r16 = "Resolving type ";
        r0 = r16;
        r7.<init>(r0);	 Catch:{ all -> 0x00fb }
        r7 = r7.append(r3);	 Catch:{ all -> 0x00fb }
        r16 = " scheme ";
        r0 = r16;
        r7 = r7.append(r0);	 Catch:{ all -> 0x00fb }
        r7 = r7.append(r4);	 Catch:{ all -> 0x00fb }
        r16 = " of intent ";
        r0 = r16;
        r7 = r7.append(r0);	 Catch:{ all -> 0x00fb }
        r0 = r18;
        r7 = r7.append(r0);	 Catch:{ all -> 0x00fb }
        r7 = r7.toString();	 Catch:{ all -> 0x00fb }
        android.util.Log.v(r1, r7);	 Catch:{ all -> 0x00fb }
    L_0x005e:
        r0 = r17;
        r1 = r0.mActions;	 Catch:{ all -> 0x00fb }
        r7 = r18.getAction();	 Catch:{ all -> 0x00fb }
        r9 = r1.get(r7);	 Catch:{ all -> 0x00fb }
        r9 = (java.util.ArrayList) r9;	 Catch:{ all -> 0x00fb }
        if (r9 == 0) goto L_0x0161;
    L_0x006e:
        if (r8 == 0) goto L_0x0086;
    L_0x0070:
        r1 = "LocalBroadcastManager";
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00fb }
        r16 = "Action list: ";
        r0 = r16;
        r7.<init>(r0);	 Catch:{ all -> 0x00fb }
        r7 = r7.append(r9);	 Catch:{ all -> 0x00fb }
        r7 = r7.toString();	 Catch:{ all -> 0x00fb }
        android.util.Log.v(r1, r7);	 Catch:{ all -> 0x00fb }
    L_0x0086:
        r14 = 0;
        r10 = 0;
    L_0x0088:
        r1 = r9.size();	 Catch:{ all -> 0x00fb }
        if (r10 >= r1) goto L_0x0128;
    L_0x008e:
        r13 = r9.get(r10);	 Catch:{ all -> 0x00fb }
        r13 = (android.support.v4.content.LocalBroadcastManager.ReceiverRecord) r13;	 Catch:{ all -> 0x00fb }
        if (r8 == 0) goto L_0x00b2;
    L_0x0096:
        r1 = "LocalBroadcastManager";
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00fb }
        r16 = "Matching against filter ";
        r0 = r16;
        r7.<init>(r0);	 Catch:{ all -> 0x00fb }
        r0 = r13.filter;	 Catch:{ all -> 0x00fb }
        r16 = r0;
        r0 = r16;
        r7 = r7.append(r0);	 Catch:{ all -> 0x00fb }
        r7 = r7.toString();	 Catch:{ all -> 0x00fb }
        android.util.Log.v(r1, r7);	 Catch:{ all -> 0x00fb }
    L_0x00b2:
        r1 = r13.broadcasting;	 Catch:{ all -> 0x00fb }
        if (r1 == 0) goto L_0x00c5;
    L_0x00b6:
        if (r8 == 0) goto L_0x00bf;
    L_0x00b8:
        r1 = "LocalBroadcastManager";
        r7 = "  Filter's target already added";
        android.util.Log.v(r1, r7);	 Catch:{ all -> 0x00fb }
    L_0x00bf:
        r10 = r10 + 1;
        goto L_0x0088;
    L_0x00c2:
        r8 = 0;
        goto L_0x002c;
    L_0x00c5:
        r1 = r13.filter;	 Catch:{ all -> 0x00fb }
        r7 = "LocalBroadcastManager";
        r11 = r1.match(r2, r3, r4, r5, r6, r7);	 Catch:{ all -> 0x00fb }
        if (r11 < 0) goto L_0x00fe;
    L_0x00cf:
        if (r8 == 0) goto L_0x00ed;
    L_0x00d1:
        r1 = "LocalBroadcastManager";
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00fb }
        r16 = "  Filter matched!  match=0x";
        r0 = r16;
        r7.<init>(r0);	 Catch:{ all -> 0x00fb }
        r16 = java.lang.Integer.toHexString(r11);	 Catch:{ all -> 0x00fb }
        r0 = r16;
        r7 = r7.append(r0);	 Catch:{ all -> 0x00fb }
        r7 = r7.toString();	 Catch:{ all -> 0x00fb }
        android.util.Log.v(r1, r7);	 Catch:{ all -> 0x00fb }
    L_0x00ed:
        if (r14 != 0) goto L_0x00f4;
    L_0x00ef:
        r14 = new java.util.ArrayList;	 Catch:{ all -> 0x00fb }
        r14.<init>();	 Catch:{ all -> 0x00fb }
    L_0x00f4:
        r14.add(r13);	 Catch:{ all -> 0x00fb }
        r1 = 1;
        r13.broadcasting = r1;	 Catch:{ all -> 0x00fb }
        goto L_0x00bf;
    L_0x00fb:
        r1 = move-exception;
        monitor-exit(r15);	 Catch:{ all -> 0x00fb }
        throw r1;
    L_0x00fe:
        if (r8 == 0) goto L_0x00bf;
    L_0x0100:
        switch(r11) {
            case -4: goto L_0x011f;
            case -3: goto L_0x011c;
            case -2: goto L_0x0122;
            case -1: goto L_0x0125;
            default: goto L_0x0103;
        };
    L_0x0103:
        r12 = "unknown reason";
    L_0x0105:
        r1 = "LocalBroadcastManager";
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00fb }
        r16 = "  Filter did not match: ";
        r0 = r16;
        r7.<init>(r0);	 Catch:{ all -> 0x00fb }
        r7 = r7.append(r12);	 Catch:{ all -> 0x00fb }
        r7 = r7.toString();	 Catch:{ all -> 0x00fb }
        android.util.Log.v(r1, r7);	 Catch:{ all -> 0x00fb }
        goto L_0x00bf;
    L_0x011c:
        r12 = "action";
        goto L_0x0105;
    L_0x011f:
        r12 = "category";
        goto L_0x0105;
    L_0x0122:
        r12 = "data";
        goto L_0x0105;
    L_0x0125:
        r12 = "type";
        goto L_0x0105;
    L_0x0128:
        if (r14 == 0) goto L_0x0161;
    L_0x012a:
        r10 = 0;
    L_0x012b:
        r1 = r14.size();	 Catch:{ all -> 0x00fb }
        if (r10 >= r1) goto L_0x013d;
    L_0x0131:
        r1 = r14.get(r10);	 Catch:{ all -> 0x00fb }
        r1 = (android.support.v4.content.LocalBroadcastManager.ReceiverRecord) r1;	 Catch:{ all -> 0x00fb }
        r7 = 0;
        r1.broadcasting = r7;	 Catch:{ all -> 0x00fb }
        r10 = r10 + 1;
        goto L_0x012b;
    L_0x013d:
        r0 = r17;
        r1 = r0.mPendingBroadcasts;	 Catch:{ all -> 0x00fb }
        r7 = new android.support.v4.content.LocalBroadcastManager$BroadcastRecord;	 Catch:{ all -> 0x00fb }
        r0 = r18;
        r7.<init>(r0, r14);	 Catch:{ all -> 0x00fb }
        r1.add(r7);	 Catch:{ all -> 0x00fb }
        r0 = r17;
        r1 = r0.mHandler;	 Catch:{ all -> 0x00fb }
        r7 = 1;
        r1 = r1.hasMessages(r7);	 Catch:{ all -> 0x00fb }
        if (r1 != 0) goto L_0x015e;
    L_0x0156:
        r0 = r17;
        r1 = r0.mHandler;	 Catch:{ all -> 0x00fb }
        r7 = 1;
        r1.sendEmptyMessage(r7);	 Catch:{ all -> 0x00fb }
    L_0x015e:
        r1 = 1;
        monitor-exit(r15);	 Catch:{ all -> 0x00fb }
    L_0x0160:
        return r1;
    L_0x0161:
        monitor-exit(r15);	 Catch:{ all -> 0x00fb }
        r1 = 0;
        goto L_0x0160;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.content.LocalBroadcastManager.sendBroadcast(android.content.Intent):boolean");
    }

    public void sendBroadcastSync(Intent intent) {
        if (sendBroadcast(intent)) {
            executePendingBroadcasts();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void executePendingBroadcasts() {
        /*
        r8 = this;
    L_0x0000:
        r6 = r8.mReceivers;
        monitor-enter(r6);
        r5 = r8.mPendingBroadcasts;	 Catch:{ all -> 0x003d }
        r0 = r5.size();	 Catch:{ all -> 0x003d }
        if (r0 > 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r6);	 Catch:{ all -> 0x003d }
        return;
    L_0x000d:
        r2 = new android.support.v4.content.LocalBroadcastManager.BroadcastRecord[r0];	 Catch:{ all -> 0x003d }
        r5 = r8.mPendingBroadcasts;	 Catch:{ all -> 0x003d }
        r5.toArray(r2);	 Catch:{ all -> 0x003d }
        r5 = r8.mPendingBroadcasts;	 Catch:{ all -> 0x003d }
        r5.clear();	 Catch:{ all -> 0x003d }
        monitor-exit(r6);	 Catch:{ all -> 0x003d }
        r3 = 0;
    L_0x001b:
        r5 = r2.length;
        if (r3 >= r5) goto L_0x0000;
    L_0x001e:
        r1 = r2[r3];
        r4 = 0;
    L_0x0021:
        r5 = r1.receivers;
        r5 = r5.size();
        if (r4 >= r5) goto L_0x0040;
    L_0x0029:
        r5 = r1.receivers;
        r5 = r5.get(r4);
        r5 = (android.support.v4.content.LocalBroadcastManager.ReceiverRecord) r5;
        r5 = r5.receiver;
        r6 = r8.mAppContext;
        r7 = r1.intent;
        r5.onReceive(r6, r7);
        r4 = r4 + 1;
        goto L_0x0021;
    L_0x003d:
        r5 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x003d }
        throw r5;
    L_0x0040:
        r3 = r3 + 1;
        goto L_0x001b;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.content.LocalBroadcastManager.executePendingBroadcasts():void");
    }
}

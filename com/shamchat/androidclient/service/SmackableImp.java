package com.shamchat.androidclient.service;

import android.app.AlarmManager;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.preference.PreferenceManager;
import android.support.v7.appcompat.BuildConfig;
import android.util.Base64;
import android.util.Log;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.rokhgroup.mqtt.NotifySimple;
import com.shamchat.activity.ChatActivity;
import com.shamchat.activity.ChatInitialForGroupChatActivity;
import com.shamchat.activity.MessageDetailsActivity;
import com.shamchat.adapters.MyMessageType;
import com.shamchat.androidclient.IXMPPChatCallback;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.chat.extension.ChatStateExtension;
import com.shamchat.androidclient.chat.extension.LocationDetails;
import com.shamchat.androidclient.chat.extension.LocationReceivedProvider;
import com.shamchat.androidclient.chat.extension.MessageContentTypeExtention;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import com.shamchat.androidclient.chat.extension.SeenReceived;
import com.shamchat.androidclient.chat.extension.SeenReceivedProvider;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.androidclient.data.RosterProvider;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.androidclient.data.ZaminConfiguration;
import com.shamchat.androidclient.exceptions.ZaminXMPPException;
import com.shamchat.androidclient.util.ConnectionState;
import com.shamchat.androidclient.util.PreferenceConstants;
import com.shamchat.androidclient.util.StatusMode;
import com.shamchat.events.NewMessageEvent;
import com.shamchat.events.NewMessageReceivedEvent;
import com.shamchat.events.PresenceChangedEvent;
import com.shamchat.events.SyncGroupJoinChatroomEvent;
import com.shamchat.events.SyncGroupMessageProcessEvent;
import com.shamchat.events.SyncInviteProcessEvent;
import com.shamchat.events.TypingStatusEvent;
import com.shamchat.jobs.ChangeXMPPPasswordJob;
import com.shamchat.jobs.FriendDBLoadJob;
import com.shamchat.jobs.MessageStateChangedJob;
import com.shamchat.jobs.SHAMContactsDBLoadJob;
import com.shamchat.jobs.SyncContactsJob;
import com.shamchat.models.ChatMessage.MessageStatusType;
import com.shamchat.models.FriendGroup;
import com.shamchat.models.FriendGroupMember;
import com.shamchat.models.MessageThread;
import com.shamchat.models.User;
import com.shamchat.utils.Utils;
import com.shamchat.utils.Utils.ContactDetails;
import de.greenrobot.event.EventBus;
import gnu.inet.encoding.IDNA;
import gnu.inet.encoding.IDNAException;
import java.io.File;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ssl.StrictHostnameVerifier;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.security.SSLSocketFactoryFactory;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.packet.RosterPacket.Item;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.sm.StreamManagementException.StreamManagementNotEnabledException;
import org.jivesoftware.smack.tcp.sm.provider.StreamManagementStreamFeatureProvider;
import org.jivesoftware.smack.util.DNSUtil;
import org.jivesoftware.smack.util.StringTransformer;
import org.jivesoftware.smackx.caps.EntityCapsManager;
import org.jivesoftware.smackx.caps.cache.SimpleDirectoryPersistentCache;
import org.jivesoftware.smackx.caps.provider.CapsExtensionProvider;
import org.jivesoftware.smackx.carbons.CarbonManager;
import org.jivesoftware.smackx.carbons.CarbonManager.C13132;
import org.jivesoftware.smackx.carbons.packet.Carbon.Disable;
import org.jivesoftware.smackx.carbons.packet.Carbon.Enable;
import org.jivesoftware.smackx.carbons.packet.CarbonExtension;
import org.jivesoftware.smackx.carbons.packet.CarbonExtension.Direction;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.delay.packet.DelayInformation;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo.Identity;
import org.jivesoftware.smackx.disco.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.disco.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.iqlast.LastActivityManager;
import org.jivesoftware.smackx.iqlast.packet.LastActivity;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.iqregister.packet.Registration;
import org.jivesoftware.smackx.iqversion.VersionManager;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.packet.MUCOwner;
import org.jivesoftware.smackx.muc.packet.MUCUser;
import org.jivesoftware.smackx.muc.packet.MUCUser.Invite;
import org.jivesoftware.smackx.ping.packet.Ping;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceipt.Provider;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jivesoftware.smackx.vcardtemp.provider.VCardProvider;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jxmpp.util.XmppStringUtils;

public final class SmackableImp implements Smackable, InvitationListener {
    static final Identity ZAMIN_IDENTITY;
    static File capsCacheDir;
    private static Roster mRoster;
    private static XMPPTCPConnection mXMPPConnection;
    boolean DEBUG;
    private RemoteCallbackList<IXMPPChatCallback> chatCallbacks;
    protected boolean is_user_watching;
    private JobManager jobManager;
    private int keepAliveSeconds;
    private AlarmManager mAlarmManager;
    private final ZaminConfiguration mConfig;
    private Thread mConnectingThread;
    private Object mConnectingThreadMutex;
    private ConnectionListener mConnectionListener;
    private final ContentResolver mContentResolver;
    private String mLastError;
    private PacketListener mPacketListener;
    private Intent mPingAlarmIntent;
    private PendingIntent mPingAlarmPendIntent;
    private BroadcastReceiver mPingAlarmReceiver;
    private String mPingID;
    private long mPingTimestamp;
    private PacketListener mPongListener;
    private Intent mPongTimeoutAlarmIntent;
    private PendingIntent mPongTimeoutAlarmPendIntent;
    private PongTimeoutAlarmReceiver mPongTimeoutAlarmReceiver;
    private PacketListener mPresenceListener;
    private ConnectionState mRequestedState;
    private RosterListener mRosterListener;
    private Service mService;
    private XMPPServiceCallback mServiceCallBack;
    private ConnectionState mState;
    private ConnectionConfiguration mXMPPConfig;
    private NotificationManager notificationManager;
    private SharedPreferences preferences;

    /* renamed from: com.shamchat.androidclient.service.SmackableImp.17 */
    static /* synthetic */ class AnonymousClass17 {
        static final /* synthetic */ int[] f26x39e42954;
        static final /* synthetic */ int[] $SwitchMap$com$shamchat$androidclient$util$ConnectionState;
        static final /* synthetic */ int[] $SwitchMap$org$jivesoftware$smack$packet$Presence$Type;

        static {
            f26x39e42954 = new int[MessageContentType.values().length];
            try {
                f26x39e42954[MessageContentType.TEXT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f26x39e42954[MessageContentType.IMAGE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f26x39e42954[MessageContentType.VIDEO.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f26x39e42954[MessageContentType.STICKER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f26x39e42954[MessageContentType.LOCATION.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f26x39e42954[MessageContentType.VOICE_RECORD.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            $SwitchMap$org$jivesoftware$smack$packet$Presence$Type = new int[Type.values().length];
            try {
                $SwitchMap$org$jivesoftware$smack$packet$Presence$Type[Type.subscribe.ordinal()] = 1;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$org$jivesoftware$smack$packet$Presence$Type[Type.unsubscribe.ordinal()] = 2;
            } catch (NoSuchFieldError e8) {
            }
            $SwitchMap$com$shamchat$androidclient$util$ConnectionState = new int[ConnectionState.values().length];
            try {
                $SwitchMap$com$shamchat$androidclient$util$ConnectionState[ConnectionState.RECONNECT_DELAYED.ordinal()] = 1;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$shamchat$androidclient$util$ConnectionState[ConnectionState.RECONNECT_NETWORK.ordinal()] = 2;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$shamchat$androidclient$util$ConnectionState[ConnectionState.OFFLINE.ordinal()] = 3;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$shamchat$androidclient$util$ConnectionState[ConnectionState.CONNECTING.ordinal()] = 4;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$shamchat$androidclient$util$ConnectionState[ConnectionState.DISCONNECTING.ordinal()] = 5;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$shamchat$androidclient$util$ConnectionState[ConnectionState.ONLINE.ordinal()] = 6;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$shamchat$androidclient$util$ConnectionState[ConnectionState.DISCONNECTED.ordinal()] = 7;
            } catch (NoSuchFieldError e15) {
            }
        }
    }

    /* renamed from: com.shamchat.androidclient.service.SmackableImp.1 */
    static class C10711 implements StringTransformer {
        C10711() {
        }

        public final String transform(String string) {
            try {
                StringBuffer stringBuffer = new StringBuffer();
                StringBuffer stringBuffer2 = new StringBuffer();
                for (int i = 0; i < string.length(); i++) {
                    char charAt = string.charAt(i);
                    if (charAt == '.' || charAt == '\u3002' || charAt == '\uff0e' || charAt == '\uff61') {
                        stringBuffer.append(IDNA.toASCII$4ff2de3f(stringBuffer2.toString()));
                        stringBuffer.append('.');
                        stringBuffer2 = new StringBuffer();
                    } else {
                        stringBuffer2.append(charAt);
                    }
                }
                stringBuffer.append(IDNA.toASCII$4ff2de3f(stringBuffer2.toString()));
                return stringBuffer.toString();
            } catch (IDNAException e) {
                Log.w("Could not perform IDNA transformation", e);
                return string;
            }
        }
    }

    /* renamed from: com.shamchat.androidclient.service.SmackableImp.2 */
    class C10722 implements Runnable {
        C10722() {
        }

        public final void run() {
            try {
                VCard vcard = new VCard();
                if (SmackableImp.getXmppConnection() != null) {
                    UserProvider userProvider = new UserProvider();
                    User user = UserProvider.getCurrentUserForMyProfile();
                    vcard.setJabberId(Utils.createXmppUserIdByUserId(user.userId));
                    vcard.setNickName(user.chatId);
                    vcard.setFirstName(user.username);
                    vcard.setPhoneHome("CELL", user.mobileNo);
                    if (user.profileImageUrl != null) {
                        vcard.setMiddleName(user.profileImageUrl);
                    }
                    try {
                        if (user.profileImage != null) {
                            byte[] profPic = Base64.decode(user.profileImage, 0);
                            byte[] decode = Base64.decode(Utils.encodeImage(Utils.scaleDownImageSizeProfile$1c855778(BitmapFactory.decodeByteArray(profPic, 0, profPic.length))), 0);
                            String str = "image/jpeg";
                            if (decode == null) {
                                vcard.photoBinval = null;
                                vcard.photoMimeType = null;
                            } else {
                                vcard.setAvatar(org.jivesoftware.smack.util.stringencoder.Base64.encodeToString(decode), str);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("SmackableImpl vCard setAvtar e " + e);
                    } catch (OutOfMemoryError e2) {
                        Process.killProcess(Process.myPid());
                    }
                    if (user.cityOrRegion != null) {
                        vcard.setAddressFieldHome("LOCALITY", user.cityOrRegion);
                    }
                    if (user.gender != null) {
                        vcard.setField("GENDER", user.gender);
                    }
                    vcard.save(SmackableImp.getXmppConnection());
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    }

    /* renamed from: com.shamchat.androidclient.service.SmackableImp.3 */
    class C10733 extends Thread {
        final /* synthetic */ boolean val$create_account;

        C10733(boolean z) {
            this.val$create_account = z;
        }

        public final void run() {
            SmackableImp.access$200(SmackableImp.this, this);
            try {
                SmackableImp.this.doConnect$138603();
            } catch (IllegalArgumentException e) {
                SmackableImp.access$300(SmackableImp.this, e);
            } catch (ZaminXMPPException e2) {
                SmackableImp.access$300(SmackableImp.this, e2);
            } finally {
                SmackableImp.this.mAlarmManager.cancel(SmackableImp.this.mPongTimeoutAlarmPendIntent);
                SmackableImp.access$600(SmackableImp.this);
            }
        }
    }

    /* renamed from: com.shamchat.androidclient.service.SmackableImp.4 */
    class C10744 extends Thread {
        C10744() {
        }

        public final void run() {
            SmackableImp.access$200(SmackableImp.this, this);
            SmackableImp.mXMPPConnection.instantShutdown();
            SmackableImp.this.onDisconnected("forced disconnect completed");
            SmackableImp.access$600(SmackableImp.this);
        }
    }

    /* renamed from: com.shamchat.androidclient.service.SmackableImp.5 */
    class C10755 extends Thread {
        C10755() {
        }

        public final void run() {
            SmackableImp.access$200(SmackableImp.this, this);
            try {
                SmackableImp.mXMPPConnection.disconnect();
            } catch (NotConnectedException e) {
                e.printStackTrace();
            }
            SmackableImp.this.mAlarmManager.cancel(SmackableImp.this.mPongTimeoutAlarmPendIntent);
            SmackableImp.this.mConfig.reconnect_required = true;
            SmackableImp.access$600(SmackableImp.this);
            if (SmackableImp.this.mRequestedState == ConnectionState.ONLINE) {
                SmackableImp.this.requestConnectionState(ConnectionState.ONLINE, false);
            }
        }
    }

    /* renamed from: com.shamchat.androidclient.service.SmackableImp.6 */
    class C10766 implements ReceiptReceivedListener {
        C10766() {
        }

        public final void onReceiptReceived$14e1ec6d(String fromJid, String receiptId) {
            if (fromJid.contains(MqttTopic.TOPIC_LEVEL_SEPARATOR)) {
                Log.d("zamin.SmackableImp", "got delivery receipt for " + receiptId);
                SmackableImp.this.updateMessageStatus(receiptId, MessageStatusType.DELIVERED.ordinal());
            }
        }
    }

    /* renamed from: com.shamchat.androidclient.service.SmackableImp.7 */
    class C10777 implements ConnectionListener {
        C10777() {
        }

        public final void connectionClosedOnError(Exception e) {
            if (!SmackableImp.mXMPPConnection.isSmResumptionPossible()) {
                SHAMChatApplication.CHAT_ROOMS.clear();
            }
            SmackableImp.access$300(SmackableImp.this, e);
        }

        public final void connectionClosed() {
            SHAMChatApplication.CHAT_ROOMS.clear();
            SmackableImp.this.updateConnectionState(ConnectionState.OFFLINE);
        }

        public final void connected(XMPPConnection connection) {
        }

        public final void authenticated(XMPPConnection connection) {
        }

        public final void reconnectionSuccessful() {
        }
    }

    /* renamed from: com.shamchat.androidclient.service.SmackableImp.8 */
    class C10788 implements Runnable {
        final /* synthetic */ String val$receipient;

        C10788(String str) {
            this.val$receipient = str;
        }

        public final void run() {
            try {
                new VCard().load(SmackableImp.mXMPPConnection, this.val$receipient);
            } catch (Exception e) {
            }
        }
    }

    /* renamed from: com.shamchat.androidclient.service.SmackableImp.9 */
    class C10799 implements Runnable {
        C10799() {
        }

        public final void run() {
            Cursor cursor = SmackableImp.this.mContentResolver.query(ChatProviderNew.CONTENT_URI_CHAT, null, "message_status=?", new String[]{String.valueOf(MessageStatusType.QUEUED.ordinal())}, null);
            while (cursor.moveToNext()) {
                String toJID = cursor.getString(cursor.getColumnIndex("message_recipient"));
                if (!toJID.equalsIgnoreCase(SmackableImp.this.mConfig.userId)) {
                    int messageTypeInt = cursor.getInt(cursor.getColumnIndex(ChatInitialForGroupChatActivity.INTENT_EXTRA_MESSAGE_CONTENT_TYPE));
                    System.out.println("sending offline messages " + messageTypeInt);
                    MessageContentType messageTypeExtention = ChatProviderNew.readMessageContentType(messageTypeInt);
                    if (messageTypeExtention.type == MessageContentType.TEXT.ordinal() || messageTypeExtention.type == MessageContentType.STICKER.ordinal()) {
                        System.out.println("sending offline type text or sticker");
                    } else {
                        try {
                            String uploadedFileUrl = cursor.getString(cursor.getColumnIndex("uploaded_file_url"));
                            if (uploadedFileUrl.length() == 0) {
                                continue;
                            } else if (uploadedFileUrl == null) {
                            }
                        } catch (Exception e) {
                        }
                    }
                    String message = cursor.getString(cursor.getColumnIndex("text_message"));
                    String packetId = cursor.getString(cursor.getColumnIndex("packet_id"));
                    String threadId = cursor.getString(cursor.getColumnIndex(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID));
                    double d = 0.0d;
                    double latitude = 0.0d;
                    try {
                        d = cursor.getDouble(cursor.getColumnIndex("longitude"));
                        latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    if (!threadId.contains("g")) {
                        SmackableImp.this.sendMessage(Utils.createXmppUserIdByUserId(toJID), message, messageTypeExtention.toString(), false, packetId, String.valueOf(latitude), String.valueOf(d), cursor.getString(cursor.getColumnIndex("description")));
                    } else {
                        return;
                    }
                }
            }
            cursor.close();
        }
    }

    private class PingAlarmReceiver extends BroadcastReceiver {
        private PingAlarmReceiver() {
        }

        public final void onReceive(Context ctx, Intent i) {
            if (SmackableImp.this.DEBUG) {
                SmackableImp.notifyUser("pingAlaram received");
            }
            SmackableImp.this.sendServerPing();
        }
    }

    private class PongTimeoutAlarmReceiver extends BroadcastReceiver {
        private PongTimeoutAlarmReceiver() {
        }

        public final void onReceive(Context ctx, Intent i) {
            Log.d("zamin.SmackableImp", "Ping: timeout for " + SmackableImp.this.mPingID);
            SmackableImp.this.onDisconnected("Ping timeout");
        }
    }

    static /* synthetic */ ContentValues access$1500(SmackableImp x0, RosterEntry x1) {
        ContentValues contentValues = new ContentValues();
        Cursor query = x0.mContentResolver.query(RosterProvider.CONTENT_URI, new String[]{"jid"}, "jid=?", new String[]{getBareJID(x1.user)}, null);
        int i = query.getCount() > 0 ? 1 : 0;
        query.close();
        contentValues.put("jid", r9);
        contentValues.put("alias", getName(x1));
        Packet presence = mRoster.getPresence(x1.user);
        contentValues.put("status_mode", Integer.valueOf(getStatusInt(presence)));
        if (presence.type == Type.error) {
            contentValues.put("status_message", presence.error.toString());
        } else {
            contentValues.put("status_message", presence.status);
        }
        contentValues.put("roster_group", getGroup(x1.getGroups()));
        if (i == 0) {
            return contentValues;
        }
        x0.mContentResolver.update(RosterProvider.CONTENT_URI, contentValues, "jid=?", new String[]{x1.user});
        return null;
    }

    static /* synthetic */ void access$1700(SmackableImp x0) {
        Log.d("zamin.SmackableImp", "removeOldRosterEntries()");
        Roster roster = mRoster;
        Collection hashSet = new HashSet();
        for (RosterGroup entries : roster.getGroups()) {
            hashSet.addAll(entries.getEntries());
        }
        hashSet.addAll(roster.unfiledEntries);
        Collection<RosterEntry> unmodifiableCollection = Collections.unmodifiableCollection(hashSet);
        StringBuilder stringBuilder = new StringBuilder("jid NOT IN (");
        Object obj = 1;
        for (RosterEntry rosterEntry : unmodifiableCollection) {
            x0.updateRosterEntryInDB(rosterEntry);
            if (obj != null) {
                obj = null;
            } else {
                stringBuilder.append(",");
            }
            stringBuilder.append("'").append(rosterEntry.user).append("'");
        }
        stringBuilder.append(")");
        Log.d("zamin.SmackableImp", "deleted " + x0.mContentResolver.delete(RosterProvider.CONTENT_URI, stringBuilder.toString(), null) + " old roster entries");
    }

    static /* synthetic */ void access$200(SmackableImp x0, Thread x1) {
        synchronized (x0.mConnectingThreadMutex) {
            if (x0.mConnectingThread == null) {
                x0.mConnectingThread = x1;
            } else {
                try {
                    Log.d("zamin.SmackableImp", "updateConnectingThread: old thread is still running, killing it.");
                    x0.mConnectingThread.interrupt();
                    x0.mConnectingThread.join(50);
                } catch (InterruptedException e) {
                    Log.d("zamin.SmackableImp", "updateConnectingThread: failed to join(): " + e);
                } finally {
                    x0.mConnectingThread = x1;
                }
            }
        }
    }

    static /* synthetic */ void access$2400(SmackableImp x0, String x1) {
        if (x0.DEBUG) {
            notifyUser("gotServerPong ");
        }
        long currentTimeMillis = System.currentTimeMillis() - x0.mPingTimestamp;
        if (x1 == null || !x1.equals(x0.mPingID)) {
            Log.i("zamin.SmackableImp", String.format("Ping: server latency %1.3fs (estimated)", new Object[]{Double.valueOf(((double) currentTimeMillis) / 1000.0d)}));
        } else {
            Log.i("zamin.SmackableImp", String.format("Ping: server latency %1.3fs", new Object[]{Double.valueOf(((double) currentTimeMillis) / 1000.0d)}));
        }
        x0.mPingID = null;
        x0.mAlarmManager.cancel(x0.mPongTimeoutAlarmPendIntent);
    }

    static /* synthetic */ void access$2500(SmackableImp x0, VCard x1) {
        String userIdFromXmppUserId = Utils.getUserIdFromXmppUserId(x1.getJabberId());
        System.out.println("updateDbWithVcardEntry ====START=====" + x1.getJabberId());
        System.out.println("updateDbWithVcardEntry User id " + userIdFromXmppUserId);
        if (!(userIdFromXmppUserId == null || userIdFromXmppUserId.contains("-"))) {
            String str;
            ContentValues contentValues;
            ContactDetails conactExists;
            String str2;
            ContentValues contentValues2;
            Cursor query;
            Intent intent;
            Job sHAMContactsDBLoadJob;
            SHAMChatApplication.USER_IMAGES.remove(userIdFromXmppUserId);
            System.out.println("updateDbWithVcardEntry userId != null && (!userId.contains(-))");
            String str3 = (String) x1.homePhones.get("CELL");
            if (str3 == null) {
                str3 = (String) x1.workPhones.get("CELL");
                if (str3 == null) {
                    String charSequence = x1.toXML().toString();
                    System.out.println("updateDbWithVcardEntry xml " + charSequence);
                    if (charSequence.indexOf("<GIVEN>") != -1) {
                        str3 = charSequence.substring(charSequence.indexOf("<GIVEN>"), charSequence.indexOf("</GIVEN>")).replaceAll("<GIVEN>", BuildConfig.VERSION_NAME).replaceAll("</GIVEN>", BuildConfig.VERSION_NAME);
                        System.out.println("updateDbWithVcardEntry tmp" + str3);
                        str = str3;
                        System.out.println("updateDbWithVcardEntry mobileNo " + str);
                        if (str == null) {
                            contentValues = new ContentValues();
                            userIdFromXmppUserId.equalsIgnoreCase(SHAMChatApplication.getConfig().userId);
                            System.out.println("updateDbWithVcardEntry mobileNo != null");
                            conactExists = Utils.getConactExists(SHAMChatApplication.getMyApplicationContext(), str);
                            if (conactExists.isExist) {
                                str3 = x1.firstName == null ? x1.firstName : null;
                                if (x1.getNickName() != null) {
                                    str3 = x1.getNickName();
                                }
                                str2 = str3 != null ? str : str3;
                            } else {
                                str2 = conactExists.displayName;
                            }
                            System.out.println("updateDbWithVcardEntry username " + str2);
                            contentValues.put("userId", userIdFromXmppUserId);
                            contentValues.put("name", str2);
                            contentValues.put("chatId", str2);
                            contentValues.put("mobileNo", str);
                            contentValues.put(NotificationCompatApi21.CATEGORY_EMAIL, x1.emailHome);
                            contentValues.put("gender", (String) x1.otherSimpleFields.get("GENDER"));
                            contentValues.put("user_type", Integer.valueOf(2));
                            contentValues.put("region", (String) x1.homeAddr.get("LOCALITY"));
                            contentValues.put("is_vcard_downloaded", Integer.valueOf(1));
                            str3 = (x1.middleName == null ? x1.middleName : BuildConfig.VERSION_NAME).replaceAll("<MIDDLE>", BuildConfig.VERSION_NAME).replaceAll("</MIDDLE>", BuildConfig.VERSION_NAME).replaceAll("null", BuildConfig.VERSION_NAME);
                            if (str3 != null && str3.length() > 0) {
                                System.out.println("Middle " + str3);
                                contentValues.put("profileimage_url", str3);
                            }
                            contentValues2 = new ContentValues();
                            if (conactExists.isExist) {
                                System.out.println("updateDbWithVcardEntry contactDetails.isExist else");
                                contentValues2.put("show_in_chat", Integer.valueOf(0));
                                x0.mContentResolver.update(RosterProvider.CONTENT_URI, contentValues2, "jid=?", new String[]{x1.getJabberId()});
                            } else {
                                System.out.println("updateDbWithVcardEntry contactDetails.isExist");
                                contentValues2.put("show_in_chat", Integer.valueOf(1));
                                x0.mContentResolver.update(RosterProvider.CONTENT_URI, contentValues2, "jid=?", new String[]{x1.getJabberId()});
                            }
                            query = x0.mContentResolver.query(UserProvider.CONTENT_URI_USER, new String[]{"userId"}, "userId=?", new String[]{userIdFromXmppUserId}, null);
                            if (query.getCount() <= 0) {
                                System.out.println("REFRESHING CONTACTS BUG");
                                System.out.println("updateDbWithVcardEntry cursor.getCount() > 0");
                                x0.mService.getApplicationContext().getContentResolver().update(Uri.parse(UserProvider.CONTENT_URI_USER.toString() + MqttTopic.TOPIC_LEVEL_SEPARATOR + userIdFromXmppUserId), contentValues, null, null);
                            } else {
                                System.out.println("updateDbWithVcardEntry cursor.getCount() == 0");
                                x0.mService.getApplicationContext().getContentResolver().insert(UserProvider.CONTENT_URI_USER, contentValues);
                                try {
                                    tryToAddRosterEntry$14e1ec6d(x1.getJabberId(), str2 + MqttTopic.TOPIC_LEVEL_SEPARATOR + str);
                                } catch (ZaminXMPPException e) {
                                    e.printStackTrace();
                                }
                                str3 = x0.mConfig.userId + "-" + userIdFromXmppUserId;
                                intent = new Intent(x0.mService.getApplicationContext(), ChatActivity.class);
                                intent.putExtra(ChatActivity.INTENT_EXTRA_USER_ID, userIdFromXmppUserId);
                                intent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, str3);
                                intent.setFlags(536870912);
                                x0.notificationManager.notify(0, new Builder(SHAMChatApplication.getMyApplicationContext()).setContentTitle(contentValues.get("name") + " has added you as a friend").setContentText("Mobile no : " + contentValues.get("mobileNo")).setSmallIcon(2130837905).setAutoCancel(true).setContentIntent(PendingIntent.getActivity(x0.mService.getApplicationContext(), 0, intent, 134217728)).build());
                                sHAMContactsDBLoadJob = new SHAMContactsDBLoadJob();
                                sHAMContactsDBLoadJob.search = BuildConfig.VERSION_NAME;
                                x0.jobManager.addJobInBackground(sHAMContactsDBLoadJob);
                            }
                            query.close();
                            x0.jobManager.addJobInBackground(new FriendDBLoadJob(userIdFromXmppUserId));
                        } else {
                            System.out.println("Mobile number is null ");
                            System.exit(0);
                        }
                    }
                }
            }
            str = str3;
            System.out.println("updateDbWithVcardEntry mobileNo " + str);
            if (str == null) {
                System.out.println("Mobile number is null ");
                System.exit(0);
            } else {
                contentValues = new ContentValues();
                userIdFromXmppUserId.equalsIgnoreCase(SHAMChatApplication.getConfig().userId);
                System.out.println("updateDbWithVcardEntry mobileNo != null");
                conactExists = Utils.getConactExists(SHAMChatApplication.getMyApplicationContext(), str);
                if (conactExists.isExist) {
                    if (x1.firstName == null) {
                    }
                    if (x1.getNickName() != null) {
                        str3 = x1.getNickName();
                    }
                    if (str3 != null) {
                    }
                } else {
                    str2 = conactExists.displayName;
                }
                System.out.println("updateDbWithVcardEntry username " + str2);
                contentValues.put("userId", userIdFromXmppUserId);
                contentValues.put("name", str2);
                contentValues.put("chatId", str2);
                contentValues.put("mobileNo", str);
                contentValues.put(NotificationCompatApi21.CATEGORY_EMAIL, x1.emailHome);
                contentValues.put("gender", (String) x1.otherSimpleFields.get("GENDER"));
                contentValues.put("user_type", Integer.valueOf(2));
                contentValues.put("region", (String) x1.homeAddr.get("LOCALITY"));
                contentValues.put("is_vcard_downloaded", Integer.valueOf(1));
                if (x1.middleName == null) {
                }
                str3 = (x1.middleName == null ? x1.middleName : BuildConfig.VERSION_NAME).replaceAll("<MIDDLE>", BuildConfig.VERSION_NAME).replaceAll("</MIDDLE>", BuildConfig.VERSION_NAME).replaceAll("null", BuildConfig.VERSION_NAME);
                System.out.println("Middle " + str3);
                contentValues.put("profileimage_url", str3);
                contentValues2 = new ContentValues();
                if (conactExists.isExist) {
                    System.out.println("updateDbWithVcardEntry contactDetails.isExist else");
                    contentValues2.put("show_in_chat", Integer.valueOf(0));
                    x0.mContentResolver.update(RosterProvider.CONTENT_URI, contentValues2, "jid=?", new String[]{x1.getJabberId()});
                } else {
                    System.out.println("updateDbWithVcardEntry contactDetails.isExist");
                    contentValues2.put("show_in_chat", Integer.valueOf(1));
                    x0.mContentResolver.update(RosterProvider.CONTENT_URI, contentValues2, "jid=?", new String[]{x1.getJabberId()});
                }
                query = x0.mContentResolver.query(UserProvider.CONTENT_URI_USER, new String[]{"userId"}, "userId=?", new String[]{userIdFromXmppUserId}, null);
                if (query.getCount() <= 0) {
                    System.out.println("updateDbWithVcardEntry cursor.getCount() == 0");
                    x0.mService.getApplicationContext().getContentResolver().insert(UserProvider.CONTENT_URI_USER, contentValues);
                    tryToAddRosterEntry$14e1ec6d(x1.getJabberId(), str2 + MqttTopic.TOPIC_LEVEL_SEPARATOR + str);
                    str3 = x0.mConfig.userId + "-" + userIdFromXmppUserId;
                    intent = new Intent(x0.mService.getApplicationContext(), ChatActivity.class);
                    intent.putExtra(ChatActivity.INTENT_EXTRA_USER_ID, userIdFromXmppUserId);
                    intent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, str3);
                    intent.setFlags(536870912);
                    x0.notificationManager.notify(0, new Builder(SHAMChatApplication.getMyApplicationContext()).setContentTitle(contentValues.get("name") + " has added you as a friend").setContentText("Mobile no : " + contentValues.get("mobileNo")).setSmallIcon(2130837905).setAutoCancel(true).setContentIntent(PendingIntent.getActivity(x0.mService.getApplicationContext(), 0, intent, 134217728)).build());
                    sHAMContactsDBLoadJob = new SHAMContactsDBLoadJob();
                    sHAMContactsDBLoadJob.search = BuildConfig.VERSION_NAME;
                    x0.jobManager.addJobInBackground(sHAMContactsDBLoadJob);
                } else {
                    System.out.println("REFRESHING CONTACTS BUG");
                    System.out.println("updateDbWithVcardEntry cursor.getCount() > 0");
                    x0.mService.getApplicationContext().getContentResolver().update(Uri.parse(UserProvider.CONTENT_URI_USER.toString() + MqttTopic.TOPIC_LEVEL_SEPARATOR + userIdFromXmppUserId), contentValues, null, null);
                }
                query.close();
                x0.jobManager.addJobInBackground(new FriendDBLoadJob(userIdFromXmppUserId));
            }
        }
        System.out.println("updateDbWithVcardEntry ====END=====");
    }

    static /* synthetic */ void access$2700(SmackableImp x0, Presence x1) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("jid", x1.from);
        contentValues.put("alias", x1.from);
        contentValues.put("roster_group", BuildConfig.VERSION_NAME);
        contentValues.put("status_mode", Integer.valueOf(getStatusInt(x1)));
        contentValues.put("status_message", x1.status);
        try {
            String bareJID = getBareJID(x1.from);
            Packet presence = new Presence(Type.subscribed);
            presence.to = bareJID;
            mXMPPConnection.sendPacket(presence);
            presence = new Presence(Type.subscribe);
            presence.to = bareJID;
            mXMPPConnection.sendPacket(presence);
            new Thread(new C10788(bareJID)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object obj = null;
        if (x0.mContentResolver.update(RosterProvider.CONTENT_URI, contentValues, "jid = ?", new String[]{x1.from}) == 0) {
            obj = x0.mContentResolver.insert(RosterProvider.CONTENT_URI, contentValues);
        }
        Log.d("zamin.SmackableImp", "handleIncomingSubscribe: faked " + obj);
    }

    static /* synthetic */ void access$300(SmackableImp x0, Throwable x1) {
        Log.e("zamin.SmackableImp", "onDisconnected: " + x1);
        x1.printStackTrace();
        while (x1.getCause() != null) {
            x1 = x1.getCause();
        }
        x0.onDisconnected(x1.getLocalizedMessage());
    }

    static /* synthetic */ void access$600(SmackableImp x0) {
        synchronized (x0.mConnectingThreadMutex) {
            x0.mConnectingThread = null;
        }
    }

    static {
        ZAMIN_IDENTITY = new Identity("client", "shamchat", "phone");
        capsCacheDir = null;
        ProviderManager providerManager = new ProviderManager();
        ProviderManager.addIQProvider("query", "http://jabber.org/protocol/disco#info", new DiscoverInfoProvider());
        ProviderManager.addIQProvider("query", "http://jabber.org/protocol/disco#items", new DiscoverItemsProvider());
        ProviderManager.addExtensionProvider("received", "urn:xmpp:receipts", new Provider());
        ProviderManager.addExtensionProvider("request", "urn:xmpp:receipts", new DeliveryReceiptRequest.Provider());
        ProviderManager.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
        ProviderManager.addIQProvider("vCard", "vcard-temp", new VCardProvider());
        ServiceDiscoveryManager.setDefaultIdentity(ZAMIN_IDENTITY);
        ProviderManager.addExtensionProvider("c", "http://jabber.org/protocol/caps", new CapsExtensionProvider());
        ProviderManager.addExtensionProvider("messagecontenttype", "urn:xmpp:messagetype", new MessageContentTypeProvider());
        ProviderManager.addExtensionProvider("seen_received", "urn:xmpp:receipts", new SeenReceivedProvider());
        ProviderManager.addExtensionProvider("locationdetails", "urn:xmpp:location", new LocationReceivedProvider());
        ProviderManager.addExtensionProvider(new ChatStateExtension(ChatState.composing).state.name(), "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
        ProviderManager.addExtensionProvider(new ChatStateExtension(ChatState.active).state.name(), "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
        XMPPTCPConnection.setUseStreamManagementDefault$1385ff();
        ProviderManager.addStreamFeatureProvider("sm", "urn:xmpp:sm:3", new StreamManagementStreamFeatureProvider());
        ServiceDiscoveryManager.setDefaultIdentity(ZAMIN_IDENTITY);
        SmackConfiguration.setDefaultPacketReplyTimeout$13462e();
        DNSUtil.setIdnaTransformer(new C10711());
    }

    public SmackableImp(ZaminConfiguration config, ContentResolver contentResolver, Service service) {
        this.DEBUG = false;
        this.keepAliveSeconds = 240000;
        this.chatCallbacks = new RemoteCallbackList();
        this.mConnectingThreadMutex = new Object();
        this.mRequestedState = ConnectionState.OFFLINE;
        this.mState = ConnectionState.OFFLINE;
        this.mPingAlarmIntent = new Intent("org.zamin.androidclient.PING_ALARM");
        this.mPongTimeoutAlarmIntent = new Intent("org.zamin.androidclient.PONG_TIMEOUT_ALARM");
        this.mPongTimeoutAlarmReceiver = new PongTimeoutAlarmReceiver();
        this.mPingAlarmReceiver = new PingAlarmReceiver();
        this.is_user_watching = false;
        this.mConfig = config;
        this.mContentResolver = contentResolver;
        this.mService = service;
        this.mAlarmManager = (AlarmManager) this.mService.getSystemService(NotificationCompatApi21.CATEGORY_ALARM);
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        this.notificationManager = (NotificationManager) SHAMChatApplication.getMyApplicationContext().getSystemService("notification");
        this.preferences = PreferenceManager.getDefaultSharedPreferences(SHAMChatApplication.getMyApplicationContext());
    }

    private synchronized void initXMPPConnection() {
        if (this.mConfig.customServer.length() > 0) {
            this.mXMPPConfig = new ConnectionConfiguration(this.mConfig.customServer, this.mConfig.port, this.mConfig.server);
        } else {
            this.mXMPPConfig = new ConnectionConfiguration(this.mConfig.server);
        }
        this.mXMPPConfig.reconnectionAllowed = true;
        this.mXMPPConfig.sendPresence = true;
        if (PreferenceManager.getDefaultSharedPreferences(SHAMChatApplication.getMyApplicationContext()).getBoolean(PreferenceConstants.INITIAL_LOGIN, true)) {
            this.mXMPPConfig.rosterLoadedAtLogin = true;
        } else {
            this.mXMPPConfig.rosterLoadedAtLogin = false;
        }
        this.mXMPPConfig.debuggerEnabled = true;
        if (this.mConfig.require_ssl) {
            this.mXMPPConfig.securityMode$21bc7c29 = SecurityMode.required$21bc7c29;
        }
        try {
            SSLContext sc = SSLContext.getInstance(SSLSocketFactoryFactory.DEFAULT_PROTOCOL);
            sc.init(null, new X509TrustManager[]{SHAMChatApplication.getApp(this.mService).mMTM}, new SecureRandom());
            this.mXMPPConfig.customSSLContext = sc;
            this.mXMPPConfig.hostnameVerifier = new MemorizingHostnameVerifier(new StrictHostnameVerifier());
        } catch (GeneralSecurityException e) {
            Log.d("zamin.SmackableImp", "initialize MemorizingTrustManager: " + e);
        }
        mXMPPConnection = new XMPPTCPConnection(this.mXMPPConfig);
        this.mConfig.reconnect_required = false;
        XMPPTCPConnection.setUseStreamManagementDefault$1385ff();
        ProviderManager.addStreamFeatureProvider("sm", "urn:xmpp:sm:3", new StreamManagementStreamFeatureProvider());
        ServiceDiscoveryManager.getInstanceFor(mXMPPConnection);
        if (capsCacheDir == null) {
            File file = new File(this.mService.getCacheDir(), "entity-caps-cache");
            capsCacheDir = file;
            file.mkdirs();
            EntityCapsManager.setPersistentCache(new SimpleDirectoryPersistentCache(capsCacheDir));
        }
        VersionManager.setDefaultVersion(this.mService.getString(2131492978), this.mService.getString(2131492993), "Android");
        DeliveryReceiptManager instanceFor = DeliveryReceiptManager.getInstanceFor(mXMPPConnection);
        instanceFor.auto_receipts_enabled = true;
        instanceFor.auto_receipts_enabled = true;
        instanceFor.receiptReceivedListeners.add(new C10766());
    }

    public final boolean doConnect$138603() throws ZaminXMPPException {
        this.mRequestedState = ConnectionState.ONLINE;
        updateConnectionState(ConnectionState.CONNECTING);
        if (mXMPPConnection == null || this.mConfig.reconnect_required) {
            initXMPPConnection();
        }
        try {
            if (mXMPPConnection.connected) {
                mXMPPConnection.instantShutdown();
                Log.d("zamin.SmackableImp", "conn.shutdown() failed ");
            }
            Roster roster = mXMPPConnection.getRoster();
            mRoster = roster;
            roster.subscriptionMode$68076adb = SubscriptionMode.manual$68076adb;
            if (this.mRosterListener != null) {
                mRoster.removeRosterListener(this.mRosterListener);
            }
            this.mRosterListener = new RosterListener() {
                private boolean first_roster;

                /* renamed from: com.shamchat.androidclient.service.SmackableImp.10.1 */
                class C10691 implements Runnable {
                    final /* synthetic */ String val$receipient;

                    C10691(String str) {
                        this.val$receipient = str;
                    }

                    public final void run() {
                        try {
                            new VCard().load(SmackableImp.mXMPPConnection, this.val$receipient);
                        } catch (Exception e) {
                        }
                    }
                }

                /* renamed from: com.shamchat.androidclient.service.SmackableImp.10.2 */
                class C10702 implements Runnable {
                    final /* synthetic */ String val$jabberID;

                    C10702(String str) {
                        this.val$jabberID = str;
                    }

                    public final void run() {
                        try {
                            new VCard().load(SmackableImp.mXMPPConnection, this.val$jabberID);
                        } catch (Exception e) {
                        }
                    }
                }

                {
                    this.first_roster = true;
                }

                public final void entriesAdded(Collection<String> entries) {
                    List<ContentValues> cvs = new ArrayList();
                    for (String entry : entries) {
                        RosterEntry rosterEntry = SmackableImp.mRoster.getEntry(entry);
                        ContentValues contentValues = SmackableImp.access$1500(SmackableImp.this, rosterEntry);
                        if (contentValues != null) {
                            cvs.add(contentValues);
                        }
                        try {
                            String receipient = SmackableImp.getBareJID(rosterEntry.user);
                            System.out.println("Entries added entry " + receipient);
                            Presence accept = new Presence(Type.subscribed);
                            accept.to = receipient;
                            SmackableImp.mXMPPConnection.sendPacket(accept);
                            Presence req = new Presence(Type.subscribe);
                            req.to = receipient;
                            SmackableImp.mXMPPConnection.sendPacket(req);
                            ContentValues cv = new ContentValues();
                            cv.put("is_added_to_roster", Integer.valueOf(1));
                            String userId = Utils.getUserIdFromXmppUserId(receipient);
                            String str = "userId=?";
                            int result = SmackableImp.this.mContentResolver.update(UserProvider.CONTENT_URI_USER, cv, r18, new String[]{userId});
                            System.out.println("Entries added user " + userId + " result " + result);
                            new Thread(new C10691(receipient)).start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (cvs.size() > 0) {
                        ContentValues[] dsf = new ContentValues[cvs.size()];
                        cvs.toArray(dsf);
                        SmackableImp.this.mContentResolver.bulkInsert(RosterProvider.CONTENT_URI, dsf);
                    }
                    if (this.first_roster) {
                        SmackableImp.access$1700(SmackableImp.this);
                        this.first_roster = false;
                        SmackableImp.this.mServiceCallBack;
                    }
                    SHAMContactsDBLoadJob shamContactsDBLoadJob = new SHAMContactsDBLoadJob();
                    shamContactsDBLoadJob.search = BuildConfig.VERSION_NAME;
                    SmackableImp.this.jobManager.addJobInBackground(shamContactsDBLoadJob);
                    Log.d("zamin.SmackableImp", "entriesAdded() done");
                }

                public final void entriesDeleted(Collection<String> entries) {
                    Log.d("zamin.SmackableImp", "entriesDeleted(" + entries + ")");
                    for (String entry : entries) {
                        SmackableImp.this.deleteRosterEntryFromDB(entry);
                    }
                    SmackableImp.this.mServiceCallBack;
                }

                public final void entriesUpdated(Collection<String> entries) {
                    Log.d("zamin.SmackableImp", "entriesUpdated(" + entries + ")");
                    for (String entry : entries) {
                        SmackableImp.this.updateRosterEntryInDB(SmackableImp.mRoster.getEntry(entry));
                    }
                    SmackableImp.this.mServiceCallBack;
                }

                public final void presenceChanged(Presence presence) {
                    System.out.println("entriesUpdated presenceChanged(" + presence.from + "): " + presence);
                    String jabberID = SmackableImp.getBareJID(presence.from);
                    RosterEntry rosterEntry = SmackableImp.mRoster.getEntry(jabberID);
                    if (rosterEntry != null) {
                        SmackableImp.this.updateRosterEntryInDB(rosterEntry);
                        new Thread(new C10702(jabberID)).start();
                    }
                }
            };
            roster = mRoster;
            RosterListener rosterListener = this.mRosterListener;
            if (!roster.rosterListeners.contains(rosterListener)) {
                roster.rosterListeners.add(rosterListener);
            }
            if (this.mConnectionListener != null) {
                mXMPPConnection.removeConnectionListener(this.mConnectionListener);
            }
            this.mConnectionListener = new C10777();
            mXMPPConnection.addConnectionListener(this.mConnectionListener);
            mXMPPConnection.connect();
            if (!mXMPPConnection.authenticated) {
                Log.d("zamin.SmackableImp", "DATAAAAAA " + this.mConfig.userName + " " + this.mConfig.password + " " + this.mConfig.ressource);
                mXMPPConnection.login(this.mConfig.userName, this.mConfig.password, this.mConfig.ressource);
            }
        } catch (Exception e) {
            System.out.println("error connecting smack to server " + e);
            Log.e("pass", e.toString());
            if (e.toString().contains("not-authorized")) {
                Log.e("pass", "changing password now...");
                this.jobManager.addJobInBackground(new ChangeXMPPPasswordJob(this.mConfig.userName, this.mConfig.password));
            }
        }
        System.out.println("Connection time end " + System.currentTimeMillis());
        if (isAuthenticated()) {
            if (this.mPacketListener != null) {
                mXMPPConnection.removePacketListener(this.mPacketListener);
            }
            PacketFilter packetFilter = PacketTypeFilter.MESSAGE;
            this.mPacketListener = new PacketListener() {
                public final void processPacket(Packet packet) {
                    try {
                        Log.d("zamin.SmackableImp", "PACKET " + packet.toXML());
                        if (packet instanceof Message) {
                            SmackableImp.this.processMessage(packet);
                        } else {
                            System.out.println("UNKNOWN PACKET");
                        }
                    } catch (Exception e) {
                        Log.e("zamin.SmackableImp", "failed to process packet:");
                        e.printStackTrace();
                    }
                }
            };
            mXMPPConnection.addPacketListener(this.mPacketListener, packetFilter);
            mXMPPConnection.addPacketListener(new PacketListener() {
                public final void processPacket(Packet arg0) {
                    System.out.println("vCard from server: " + arg0.toString());
                    SmackableImp.access$2500(SmackableImp.this, (VCard) arg0);
                }
            }, new PacketTypeFilter(VCard.class));
            mXMPPConnection.addPacketListener(new PacketListener() {
                public final void processPacket(Packet arg0) {
                    Presence presence = (Presence) arg0;
                    String from = presence.from;
                    System.out.println("PRESENCE(smackableimp) got from " + from + " : " + presence.type);
                    String userId;
                    ContentValues contentValues;
                    if (from.contains("conference.rabtcdn.com") && presence.type == Type.available) {
                        System.out.println("Conference service " + arg0.toXML());
                        String username = from.substring(from.indexOf("-") + 1);
                        userId = from.substring(from.indexOf(MqttTopic.TOPIC_LEVEL_SEPARATOR) + 1, from.indexOf("-"));
                        String roomname = Utils.getUserIdFromXmppUserId(from.substring(0, from.indexOf(MqttTopic.TOPIC_LEVEL_SEPARATOR)));
                        Cursor cursor = SmackableImp.this.mContentResolver.query(UserProvider.CONTENT_URI_GROUP_MEMBER, new String[]{FriendGroupMember.DB_ID}, FriendGroupMember.DB_FRIEND + "=? AND " + FriendGroupMember.DB_GROUP + "=? AND " + FriendGroupMember.DB_FRIEND_DID_JOIN + "=?", new String[]{userId, roomname, "1"}, null);
                        if (cursor.getCount() == 0) {
                            FriendGroupMember myself = new FriendGroupMember(roomname, userId);
                            myself.assignUniqueId(SmackableImp.this.mConfig.userId);
                            ContentValues vals = new ContentValues();
                            vals.put(FriendGroupMember.DB_ID, myself.id);
                            vals.put(FriendGroupMember.DB_FRIEND, myself.friendId);
                            vals.put(FriendGroupMember.DB_GROUP, myself.groupID);
                            vals.put(FriendGroupMember.DB_FRIEND_DID_JOIN, "1");
                            if (SmackableImp.this.mContentResolver.update(UserProvider.CONTENT_URI_GROUP_MEMBER, vals, FriendGroupMember.DB_GROUP + "=? AND " + FriendGroupMember.DB_FRIEND + "=?", new String[]{roomname, userId}) == 0) {
                                SmackableImp.this.mContentResolver.insert(UserProvider.CONTENT_URI_GROUP_MEMBER, vals);
                            }
                            Cursor userCursor = SmackableImp.this.mContentResolver.query(UserProvider.CONTENT_URI_USER, new String[]{"userId", "name"}, "userId=?", new String[]{userId}, null);
                            if (!userId.equals(SmackableImp.this.mConfig.userId)) {
                                if (userCursor.getCount() == 0) {
                                    ContentValues user = new ContentValues();
                                    user.put("userId", userId);
                                    user.put("name", username);
                                    SmackableImp.this.mContentResolver.insert(UserProvider.CONTENT_URI_USER, user);
                                } else {
                                    userCursor.moveToFirst();
                                    username = userCursor.getString(userCursor.getColumnIndex("name"));
                                }
                                String message = username + " " + SmackableImp.this.mService.getResources().getString(2131493161);
                                userCursor.close();
                                ContentValues values = new ContentValues();
                                values.put("last_message", message);
                                values.put("last_message_content_type", Integer.valueOf(MessageContentType.GROUP_INFO.ordinal()));
                                values.put("read_status", Integer.valueOf(0));
                                values.put("last_updated_datetime", Utils.formatDate(new Date().getTime(), "yyyy/MM/dd HH:mm:ss"));
                                values.put("last_message_direction", Integer.valueOf(MyMessageType.INCOMING_MSG.ordinal()));
                                contentValues = values;
                                SmackableImp.this.mContentResolver.update(ChatProviderNew.CONTENT_URI_THREAD, contentValues, "thread_id=?", new String[]{SmackableImp.this.mConfig.userId + "-" + roomname});
                                ContentValues chatmessageVals = new ContentValues();
                                chatmessageVals.put("message_recipient", SmackableImp.this.mConfig.userId);
                                chatmessageVals.put("message_type", Integer.valueOf(MyMessageType.INCOMING_MSG.ordinal()));
                                chatmessageVals.put("packet_id", presence.packetID);
                                chatmessageVals.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, threadId);
                                chatmessageVals.put("description", BuildConfig.VERSION_NAME);
                                chatmessageVals.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_MESSAGE_CONTENT_TYPE, Integer.valueOf(MessageContentType.GROUP_INFO.ordinal()));
                                chatmessageVals.put("message_status", Integer.valueOf(MessageStatusType.SEEN.ordinal()));
                                chatmessageVals.put("file_size", Integer.valueOf(0));
                                chatmessageVals.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_GROUP_ID, roomname);
                                chatmessageVals.put("message_sender", roomname);
                                chatmessageVals.put("text_message", message);
                                if (SmackableImp.this.mContentResolver.update(ChatProviderNew.CONTENT_URI_CHAT, chatmessageVals, "packet_id=?", new String[]{presence.packetID}) == 0) {
                                    SmackableImp.this.mContentResolver.insert(ChatProviderNew.CONTENT_URI_CHAT, chatmessageVals);
                                }
                            } else {
                                return;
                            }
                        }
                        cursor.close();
                    } else if (from == null) {
                    } else {
                        if (presence.type == Type.available || presence.type == Type.unavailable) {
                            int statusMode;
                            userId = Utils.getUserIdFromXmppUserId(from);
                            ContentValues userValues = new ContentValues();
                            userValues.put("onlineStatus", presence.type.toString());
                            contentValues = userValues;
                            SmackableImp.this.mContentResolver.update(UserProvider.CONTENT_URI_USER, contentValues, "userId=?", new String[]{userId});
                            ContentValues rosterValues = new ContentValues();
                            Mode.available.ordinal();
                            if (presence.type == Type.available) {
                                statusMode = Mode.available.ordinal();
                            } else {
                                statusMode = Mode.xa.ordinal();
                            }
                            rosterValues.put("status_mode", Integer.valueOf(statusMode));
                            contentValues = rosterValues;
                            SmackableImp.this.mContentResolver.update(RosterProvider.CONTENT_URI, contentValues, "jid = ?", new String[]{from});
                            EventBus.getDefault().post(new PresenceChangedEvent(userId, presence.type.ordinal()));
                        }
                    }
                }
            }, new PacketTypeFilter(Presence.class));
            if (this.mPresenceListener != null) {
                mXMPPConnection.removePacketListener(this.mPresenceListener);
            }
            this.mPresenceListener = new PacketListener() {
                public final void processPacket(Packet packet) {
                    try {
                        Presence p = (Presence) packet;
                        switch (AnonymousClass17.$SwitchMap$org$jivesoftware$smack$packet$Presence$Type[p.type.ordinal()]) {
                            case Logger.SEVERE /*1*/:
                                SmackableImp.access$2700(SmackableImp.this, p);
                            default:
                        }
                    } catch (Exception e) {
                        Log.e("zamin.SmackableImp", "failed to process presence:");
                        e.printStackTrace();
                    }
                }
            };
            mXMPPConnection.addPacketListener(this.mPresenceListener, new PacketTypeFilter(Presence.class));
            this.mPingID = null;
            if (this.mPongListener != null) {
                mXMPPConnection.removePacketListener(this.mPongListener);
            }
            this.mPongListener = new PacketListener() {
                public final void processPacket(Packet packet) {
                    if (packet != null) {
                        SmackableImp.access$2400(SmackableImp.this, packet.packetID);
                    }
                }
            };
            mXMPPConnection.addPacketListener(this.mPongListener, new PacketTypeFilter(IQ.class));
            this.mPingAlarmPendIntent = PendingIntent.getBroadcast(this.mService.getApplicationContext(), 0, this.mPingAlarmIntent, 134217728);
            this.mPongTimeoutAlarmPendIntent = PendingIntent.getBroadcast(this.mService.getApplicationContext(), 0, this.mPongTimeoutAlarmIntent, 134217728);
            this.mAlarmManager.setInexactRepeating(0, System.currentTimeMillis() + ((long) this.keepAliveSeconds), (long) this.keepAliveSeconds, this.mPingAlarmPendIntent);
            new Thread(new C10799()).start();
            sendUserWatching();
            removeDeletedRosters();
            new Thread(new C10722()).start();
            try {
                System.out.println("ppp RESUME");
                EventBus.getDefault().register(this, true, 0);
            } catch (Throwable t) {
                System.out.println("Chat thread " + t);
            }
            MultiUserChat.addInvitationListener(mXMPPConnection, this);
            this.mContentResolver.notifyChange(RosterProvider.CONTENT_URI, null);
            updateConnectionState(ConnectionState.ONLINE);
            if (this.preferences.getBoolean(PreferenceConstants.INITIAL_LOGIN, true)) {
                this.jobManager.addJobInBackground(new SyncContactsJob(0));
            } else {
                boolean z;
                if (((ConnectivityManager) SHAMChatApplication.getMyApplicationContext().getSystemService("connectivity")).getNetworkInfo(1).isConnected()) {
                    z = true;
                } else {
                    z = false;
                }
                if (z) {
                    this.jobManager.addJobInBackground(new SyncContactsJob(18000000));
                }
            }
            Editor editor = this.preferences.edit();
            editor.putBoolean(PreferenceConstants.INITIAL_LOGIN, false);
            editor.commit();
            if (this.preferences.getBoolean(PreferenceConstants.IS_OLD_USER, false)) {
                this.preferences.getString("user_password", null);
            }
            System.out.println("Connection time end DoConnect " + System.currentTimeMillis());
            return true;
        }
        throw new ZaminXMPPException("SMACK connected, but authentication failed");
    }

    private void removeDeletedRosters() {
        if (isAuthenticated()) {
            Cursor cursor = this.mContentResolver.query(RosterProvider.CONTENT_URI, new String[]{"jid"}, "user_status=?", new String[]{"2"}, null);
            while (cursor.moveToNext()) {
                try {
                    tryToRemoveRosterEntry(cursor.getString(cursor.getColumnIndex("jid")));
                } catch (ZaminXMPPException e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        }
    }

    public final synchronized void requestConnectionState(ConnectionState new_state, boolean create_account) {
        if (this.DEBUG) {
            notifyUser("requestConnectionState: " + new_state);
        }
        Log.d("zamin.SmackableImp", "requestConnState: " + this.mState + " -> " + new_state + (create_account ? " create_account!" : BuildConfig.VERSION_NAME));
        this.mRequestedState = new_state;
        if (new_state != this.mState) {
            switch (AnonymousClass17.$SwitchMap$com$shamchat$androidclient$util$ConnectionState[new_state.ordinal()]) {
                case Logger.SEVERE /*1*/:
                case Logger.WARNING /*2*/:
                    switch (AnonymousClass17.$SwitchMap$com$shamchat$androidclient$util$ConnectionState[this.mState.ordinal()]) {
                        case Logger.SEVERE /*1*/:
                        case Logger.WARNING /*2*/:
                        case Logger.FINEST /*7*/:
                            updateConnectionState(new_state);
                            break;
                        default:
                            throw new IllegalArgumentException("Can not go from " + this.mState + " to " + new_state);
                    }
                case Logger.INFO /*3*/:
                    switch (AnonymousClass17.$SwitchMap$com$shamchat$androidclient$util$ConnectionState[this.mState.ordinal()]) {
                        case Logger.SEVERE /*1*/:
                        case Logger.WARNING /*2*/:
                            updateConnectionState(ConnectionState.OFFLINE);
                            break;
                        case Logger.CONFIG /*4*/:
                        case Logger.FINER /*6*/:
                            if (this.DEBUG) {
                                notifyUser("requestConnectionState: Disconnecting2");
                            }
                            updateConnectionState(ConnectionState.DISCONNECTING);
                            registerPongTimeout(30000, "manual disconnect");
                            new C10755().start();
                            break;
                        case Logger.FINE /*5*/:
                            break;
                        default:
                            break;
                    }
                case Logger.FINER /*6*/:
                    switch (AnonymousClass17.$SwitchMap$com$shamchat$androidclient$util$ConnectionState[this.mState.ordinal()]) {
                        case Logger.SEVERE /*1*/:
                        case Logger.WARNING /*2*/:
                        case Logger.INFO /*3*/:
                            SHAMChatApplication.CHAT_ROOMS.clear();
                            if (this.DEBUG) {
                                notifyUser("requestConnectionState: Connecting");
                            }
                            updateConnectionState(ConnectionState.CONNECTING);
                            registerPongTimeout(63000, "connection");
                            new C10733(create_account).start();
                            break;
                        case Logger.CONFIG /*4*/:
                        case Logger.FINE /*5*/:
                            if (this.DEBUG) {
                                notifyUser("requestConnectionState: Nothing to do");
                                break;
                            }
                            break;
                        default:
                            break;
                    }
                case Logger.FINEST /*7*/:
                    SHAMChatApplication.CHAT_ROOMS.clear();
                    if (this.mState == ConnectionState.ONLINE) {
                        if (this.DEBUG) {
                            notifyUser("requestConnectionState: Disconnecting");
                        }
                        updateConnectionState(ConnectionState.DISCONNECTING);
                        registerPongTimeout(30000, "forced disconnect");
                        new C10744().start();
                        break;
                    }
                    break;
                default:
                    break;
            }
        }
        System.out.println("new state = mState");
    }

    public final void requestConnectionState(ConnectionState new_state) {
        requestConnectionState(new_state, false);
    }

    public final ConnectionState getConnectionState() {
        return this.mState;
    }

    private synchronized void updateConnectionState(ConnectionState new_state) {
        if (new_state == ConnectionState.ONLINE || new_state == ConnectionState.CONNECTING) {
            this.mLastError = null;
        }
        Log.d("zamin.SmackableImp", "updateConnectionState: " + this.mState + " -> " + new_state + " (" + this.mLastError + ")");
        if (new_state != this.mState) {
            this.mState = new_state;
            if (this.mServiceCallBack != null) {
                this.mServiceCallBack.connectionStateChanged();
            }
        }
    }

    public final void addRosterItem$14e1ec6d(String user, String alias) throws ZaminXMPPException {
        tryToAddRosterEntry$14e1ec6d(user, alias);
    }

    public final void removeRosterItem(String user) throws ZaminXMPPException {
        Log.d("zamin.SmackableImp", "removeRosterItem(" + user + ")");
        tryToRemoveRosterEntry(user);
    }

    public final void renameRosterItem(String user, String newName) throws ZaminXMPPException {
        RosterEntry rosterEntry = mRoster.getEntry(user);
        if (newName.length() <= 0 || rosterEntry == null) {
            throw new ZaminXMPPException("JabberID to rename is invalid!");
        }
        if (newName != null) {
            try {
                if (newName.equals(rosterEntry.name)) {
                    return;
                }
            } catch (NotConnectedException e) {
                throw new ZaminXMPPException(e.getLocalizedMessage());
            }
        }
        rosterEntry.name = newName;
        Packet rosterPacket = new RosterPacket();
        rosterPacket.setType(IQ.Type.set);
        rosterPacket.addRosterItem(RosterEntry.toRosterItem(rosterEntry));
        rosterEntry.connection.sendPacket(rosterPacket);
    }

    public final void addRosterGroup(String group) {
        mRoster.createGroup(group);
    }

    public final void renameRosterGroup(String group, String newGroup) {
        RosterGroup groupToRename = mRoster.getGroup(group);
        try {
            synchronized (groupToRename.entries) {
                for (RosterEntry rosterEntry : groupToRename.entries) {
                    Packet rosterPacket = new RosterPacket();
                    rosterPacket.setType(IQ.Type.set);
                    Item toRosterItem = RosterEntry.toRosterItem(rosterEntry);
                    toRosterItem.removeGroupName(groupToRename.name);
                    toRosterItem.addGroupName(newGroup);
                    rosterPacket.addRosterItem(toRosterItem);
                    groupToRename.connection.sendPacket(rosterPacket);
                }
            }
        } catch (NotConnectedException e) {
            throw new IllegalStateException(e);
        }
    }

    public final void moveRosterItemToGroup(String user, String group) throws ZaminXMPPException {
        RosterGroup group2 = mRoster.getGroup(group);
        RosterGroup createGroup = (group.length() <= 0 || group2 != null) ? group2 : mRoster.createGroup(group);
        RosterEntry entry = mRoster.getEntry(user);
        for (RosterGroup group22 : entry.getGroups()) {
            PacketCollector createPacketCollectorAndSend;
            try {
                synchronized (group22.entries) {
                    if (group22.entries.contains(entry)) {
                        IQ rosterPacket = new RosterPacket();
                        rosterPacket.setType(IQ.Type.set);
                        Item toRosterItem = RosterEntry.toRosterItem(entry);
                        toRosterItem.removeGroupName(group22.name);
                        rosterPacket.addRosterItem(toRosterItem);
                        createPacketCollectorAndSend = group22.connection.createPacketCollectorAndSend(rosterPacket);
                    } else {
                        createPacketCollectorAndSend = null;
                    }
                }
                if (createPacketCollectorAndSend != null) {
                    createPacketCollectorAndSend.nextResultOrThrow();
                }
            } catch (Throwable e) {
                throw new ZaminXMPPException("tryToRemoveUserFromGroup", e);
            } catch (Throwable e2) {
                throw new ZaminXMPPException("tryToMoveRosterEntryToGroup", e2);
            } catch (Throwable e22) {
                throw new ZaminXMPPException("tryToMoveRosterEntryToGroup", e22);
            }
        }
        if (group.length() != 0) {
            try {
                synchronized (createGroup.entries) {
                    if (createGroup.entries.contains(entry)) {
                        createPacketCollectorAndSend = null;
                    } else {
                        IQ rosterPacket2 = new RosterPacket();
                        rosterPacket2.setType(IQ.Type.set);
                        Item toRosterItem2 = RosterEntry.toRosterItem(entry);
                        toRosterItem2.addGroupName(createGroup.name);
                        rosterPacket2.addRosterItem(toRosterItem2);
                        createPacketCollectorAndSend = createGroup.connection.createPacketCollectorAndSend(rosterPacket2);
                    }
                }
                if (createPacketCollectorAndSend != null) {
                    createPacketCollectorAndSend.nextResultOrThrow();
                }
            } catch (Throwable e222) {
                throw new ZaminXMPPException("tryToMoveRosterEntryToGroup", e222);
            } catch (Throwable e2222) {
                throw new ZaminXMPPException("tryToMoveRosterEntryToGroup", e2222);
            } catch (Throwable e22222) {
                throw new ZaminXMPPException("tryToMoveRosterEntryToGroup", e22222);
            }
        }
    }

    public final void sendPresenceRequest(String user, String type) {
        if ("unsubscribed".equals(type)) {
            deleteRosterEntryFromDB(user);
        }
        Presence response = new Presence(Type.valueOf(type));
        response.to = user;
        try {
            mXMPPConnection.sendPacket(response);
        } catch (NotConnectedException e) {
            throw new IllegalStateException(e);
        }
    }

    public final String changePassword(String newPassword) {
        try {
            AccountManager instance = AccountManager.getInstance(mXMPPConnection);
            Map hashMap = new HashMap();
            hashMap.put("username", XmppStringUtils.parseLocalpart(instance.connection().getUser()));
            hashMap.put("password", newPassword);
            Packet registration = new Registration(hashMap);
            registration.setType(IQ.Type.set);
            registration.to = instance.connection().getServiceName();
            PacketCollector createPacketCollector = instance.connection().createPacketCollector(new PacketIDFilter(registration.packetID));
            instance.connection().sendPacket(registration);
            createPacketCollector.nextResultOrThrow();
            return "OK";
        } catch (XMPPErrorException e) {
            if (e.error != null) {
                return e.error.toString();
            }
            return e.getLocalizedMessage();
        } catch (NoResponseException e2) {
            throw new IllegalStateException(e2);
        } catch (NotConnectedException e3) {
            throw new IllegalStateException(e3);
        }
    }

    private void onDisconnected(String reason) {
        if (this.DEBUG) {
            notifyUser("DISCONNECTING completely");
        }
        unregisterPongListener();
        this.mLastError = reason;
        updateConnectionState(ConnectionState.DISCONNECTED);
    }

    private static void tryToRemoveRosterEntry(String user) throws ZaminXMPPException {
        try {
            RosterEntry rosterEntry = mRoster.getEntry(user);
            if (rosterEntry != null) {
                Presence unsub = new Presence(Type.unsubscribed);
                unsub.to = rosterEntry.user;
                mXMPPConnection.sendPacket(unsub);
                Roster roster = mRoster;
                if (!roster.connection.isAuthenticated()) {
                    throw new NotLoggedInException();
                } else if (roster.connection.isAnonymous()) {
                    throw new IllegalStateException("Anonymous users can't have a roster.");
                } else if (roster.entries.containsKey(rosterEntry.user)) {
                    IQ rosterPacket = new RosterPacket();
                    rosterPacket.setType(IQ.Type.set);
                    Item toRosterItem = RosterEntry.toRosterItem(rosterEntry);
                    toRosterItem.itemType = ItemType.remove;
                    rosterPacket.addRosterItem(toRosterItem);
                    roster.connection.createPacketCollectorAndSend(rosterPacket).nextResultOrThrow();
                }
            }
        } catch (Exception e) {
            throw new ZaminXMPPException("tryToRemoveRosterEntry", e);
        }
    }

    public static void tryToAddRosterEntry$14e1ec6d(String user, String alias) throws ZaminXMPPException {
        try {
            Roster roster = mRoster;
            if (!roster.connection.isAuthenticated()) {
                throw new NotLoggedInException();
            } else if (roster.connection.isAnonymous()) {
                throw new IllegalStateException("Anonymous users can't have a roster.");
            } else {
                IQ rosterPacket = new RosterPacket();
                rosterPacket.setType(IQ.Type.set);
                rosterPacket.addRosterItem(new Item(user, alias));
                roster.connection.createPacketCollectorAndSend(rosterPacket).nextResultOrThrow();
                Packet presence = new Presence(Type.subscribe);
                presence.to = user;
                roster.connection.sendPacket(presence);
                System.out.println("try to add Roster " + user);
                ContentValues cv = new ContentValues();
                cv.put("is_added_to_roster", Integer.valueOf(1));
                System.out.println("Entries added try to add user " + userId + " result " + SHAMChatApplication.getInstance().getContentResolver().update(UserProvider.CONTENT_URI_USER, cv, "userId=?", new String[]{Utils.getUserIdFromXmppUserId(user)}));
            }
        } catch (Exception e) {
            throw new ZaminXMPPException("tryToAddRosterEntry", e);
        }
    }

    public final void setStatusFromConfig() {
        if (isAuthenticated()) {
            try {
                IQ enable;
                CarbonManager instanceFor = CarbonManager.getInstanceFor(mXMPPConnection);
                boolean z = this.mConfig.messageCarbons;
                if (z) {
                    enable = new Enable();
                } else {
                    enable = new Disable();
                }
                instanceFor.connection().sendIqWithResponseCallback(enable, new C13132(z));
            } catch (NotConnectedException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public final boolean isAuthenticated() {
        if (mXMPPConnection != null && mXMPPConnection.connected && mXMPPConnection.authenticated) {
            return true;
        }
        return false;
    }

    public static boolean isXmppConnected() {
        if (mXMPPConnection != null && mXMPPConnection.connected && mXMPPConnection.authenticated) {
            return true;
        }
        return false;
    }

    public static XMPPTCPConnection getXmppConnection() {
        return mXMPPConnection;
    }

    public final void registerCallback(XMPPServiceCallback callBack) {
        this.mServiceCallBack = callBack;
        this.mService.registerReceiver(this.mPingAlarmReceiver, new IntentFilter("org.zamin.androidclient.PING_ALARM"));
        this.mService.registerReceiver(this.mPongTimeoutAlarmReceiver, new IntentFilter("org.zamin.androidclient.PONG_TIMEOUT_ALARM"));
    }

    public final void unRegisterCallback() {
        Log.d("zamin.SmackableImp", "unRegisterCallback()");
        try {
            mXMPPConnection.getRoster().removeRosterListener(this.mRosterListener);
            mXMPPConnection.removePacketListener(this.mPacketListener);
            mXMPPConnection.removePacketListener(this.mPresenceListener);
            mXMPPConnection.removePacketListener(this.mPongListener);
            unregisterPongListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestConnectionState(ConnectionState.OFFLINE, false);
        ContentValues contentValues = new ContentValues();
        contentValues.put("status_mode", Integer.valueOf(StatusMode.offline.ordinal()));
        this.mContentResolver.update(RosterProvider.CONTENT_URI, contentValues, null, null);
        this.mService.unregisterReceiver(this.mPingAlarmReceiver);
        this.mService.unregisterReceiver(this.mPongTimeoutAlarmReceiver);
        this.mServiceCallBack = null;
    }

    public final String getNameForJID(String jid) {
        if (mRoster.getEntry(jid) == null || mRoster.getEntry(jid).name == null || mRoster.getEntry(jid).name.length() <= 0) {
            return jid;
        }
        return mRoster.getEntry(jid).name;
    }

    private static String[] getJabberID(String from) {
        if (from.contains(MqttTopic.TOPIC_LEVEL_SEPARATOR)) {
            String[] res = from.split(MqttTopic.TOPIC_LEVEL_SEPARATOR);
            return new String[]{res[0], res[1]};
        }
        return new String[]{from, BuildConfig.VERSION_NAME};
    }

    private static String getBareJID(String from) {
        return from.split(MqttTopic.TOPIC_LEVEL_SEPARATOR)[0].toLowerCase();
    }

    private boolean updateMessageStatus(String packetID, int newStatus) {
        ContentValues cv = new ContentValues();
        cv.put("message_status", Integer.valueOf(newStatus));
        cv.put("message_last_updated_datetime", Utils.formatDate(new Date().getTime(), "yyyy/MM/dd HH:mm:ss"));
        boolean isSuccess = true;
        Cursor cursor = null;
        try {
            cursor = this.mContentResolver.query(ChatProviderNew.CONTENT_URI_CHAT, null, "packet_id=?", new String[]{packetID}, null);
            String threadId = null;
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                threadId = cursor.getString(cursor.getColumnIndex(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID));
                int currentMessageStatus = cursor.getInt(cursor.getColumnIndex("message_status"));
                System.out.println("Update current status " + currentMessageStatus + "with new message status" + newStatus + " packet " + packetID);
                if (currentMessageStatus < newStatus) {
                    System.out.println("Update status");
                    isSuccess = this.mContentResolver.update(Uri.parse(new StringBuilder("content://org.zamin.androidclient.provider.Messages/chat_message/").append(Integer.toString(cursor.getInt(cursor.getColumnIndex("_id")))).toString()), cv, new StringBuilder("_id = ? AND message_type = ").append(MyMessageType.OUTGOING_MSG.ordinal()).toString(), new String[]{Integer.toString(cursor.getInt(cursor.getColumnIndex("_id")))}) > 0;
                    System.out.println("Message id and status Updated database: " + isSuccess + " packet id " + packetID + "\npacket sent status from server:" + newStatus);
                }
            } else {
                isSuccess = false;
            }
            if (!(!isSuccess || threadId == null || packetID == null)) {
                NewMessageReceivedEvent newMessageReceivedEvent = new NewMessageReceivedEvent();
                newMessageReceivedEvent.threadId = threadId;
                newMessageReceivedEvent.packetId = packetID;
                EventBus.getDefault().postSticky(newMessageReceivedEvent);
                this.jobManager.addJobInBackground(new MessageStateChangedJob(threadId, packetID, newStatus));
            }
            cursor.close();
            return isSuccess;
        } catch (Throwable th) {
            cursor.close();
        }
    }

    public final void setUserWatching(boolean user_watching) {
        if (this.is_user_watching != user_watching) {
            this.is_user_watching = user_watching;
            if (mXMPPConnection != null && mXMPPConnection.authenticated) {
                sendUserWatching();
            }
        }
    }

    private void sendUserWatching() {
        IQ toggle_google_queue = new IQ() {
            public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
                return "<query xmlns='google:queue'><" + (SmackableImp.this.is_user_watching ? "disable" : "enable") + "/></query>";
            }
        };
        toggle_google_queue.setType(IQ.Type.set);
        try {
            mXMPPConnection.sendPacket(toggle_google_queue);
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public final void sendServerPing() {
        if (this.DEBUG) {
            notifyUser("sendServerPing called");
        }
        if (mXMPPConnection == null || !mXMPPConnection.authenticated) {
            if (this.DEBUG) {
                notifyUser("sendServerPing mXMPPConnection null");
            }
            Log.d("zamin.SmackableImp", "Ping: requested, but not connected to server.");
            if (this.DEBUG) {
                notifyUser("sendServerPing offline - reconnecting");
            }
            requestConnectionState(ConnectionState.ONLINE, false);
        } else if (this.mPingID != null) {
            if (this.DEBUG) {
                notifyUser("sendServerPing still waiting for " + this.mPingID);
            }
            Log.d("zamin.SmackableImp", "Ping: requested, but still waiting for " + this.mPingID);
        } else {
            Ping ping = new Ping();
            ping.setType(IQ.Type.get);
            ping.to = this.mConfig.server;
            this.mPingID = ping.packetID;
            Log.d("zamin.SmackableImp", "Ping: sending ping " + this.mPingID);
            try {
                mXMPPConnection.sendPacket(ping);
                if (this.DEBUG) {
                    notifyUser("sendServerPing success ping");
                }
                if (mXMPPConnection.smEnabledSyncPoint.wasSuccessful()) {
                    Log.d("zamin.SmackableImp", "Ping: sending SM request");
                    try {
                        XMPPTCPConnection xMPPTCPConnection = mXMPPConnection;
                        if (xMPPTCPConnection.smEnabledSyncPoint.wasSuccessful()) {
                            xMPPTCPConnection.requestSmAcknowledgementInternal();
                            if (this.DEBUG) {
                                notifyUser("sendServerPing success SmAck");
                            }
                        } else {
                            throw new StreamManagementNotEnabledException();
                        }
                    } catch (StreamManagementNotEnabledException e) {
                        if (this.DEBUG) {
                            notifyUser("sendServerPing fail ping2");
                        }
                        throw new IllegalStateException(e);
                    } catch (NotConnectedException e2) {
                        if (this.DEBUG) {
                            notifyUser("sendServerPing fail ping3");
                        }
                        throw new IllegalStateException(e2);
                    }
                }
                registerPongTimeout(33000, this.mPingID);
            } catch (NotConnectedException e22) {
                if (this.DEBUG) {
                    notifyUser("sendServerPing fail ping");
                }
                throw new IllegalStateException(e22);
            }
        }
    }

    private void registerPongTimeout(long wait_time, String id) {
        this.mPingID = id;
        this.mPingTimestamp = System.currentTimeMillis();
        Log.d("zamin.SmackableImp", String.format("Ping: registering timeout for %s: %1.3fs", new Object[]{id, Double.valueOf(((double) wait_time) / 1000.0d)}));
        if (this.mAlarmManager == null || this.mPongTimeoutAlarmPendIntent == null) {
            this.mAlarmManager = (AlarmManager) SHAMChatApplication.getInstance().getSystemService(NotificationCompatApi21.CATEGORY_ALARM);
            this.mPongTimeoutAlarmPendIntent = PendingIntent.getBroadcast(this.mService.getApplicationContext(), 0, this.mPongTimeoutAlarmIntent, 134217728);
            this.mAlarmManager.set(0, System.currentTimeMillis() + wait_time, this.mPongTimeoutAlarmPendIntent);
            Editor editor = this.preferences.edit();
            editor.putBoolean("unsupported_device", true);
            editor.apply();
            return;
        }
        this.mAlarmManager.set(0, System.currentTimeMillis() + wait_time, this.mPongTimeoutAlarmPendIntent);
    }

    private void unregisterPongListener() {
        this.mAlarmManager.cancel(this.mPingAlarmPendIntent);
        this.mAlarmManager.cancel(this.mPongTimeoutAlarmPendIntent);
    }

    public final void processMessage(Packet packet) {
        Throwable th;
        MessageContentTypeExtention messageContentType = null;
        Packet msg = (Message) packet;
        System.out.println("processMessage packet " + msg.toXML());
        if (msg.thread != null && msg.type == Message.Type.groupchat && msg.thread.startsWith(this.mConfig.userId)) {
            System.out.println("processMessage msg.getType() == Message.Type.groupchat and it's from me");
            return;
        }
        if (!(msg.thread == null || msg.type == Message.Type.groupchat)) {
            System.out.println("a single chat message received");
            Cursor cursorBlock = null;
            try {
                ContentResolver contentResolver = this.mContentResolver;
                Uri uri = RosterProvider.CONTENT_URI;
                String[] strArr = new String[]{"user_status"};
                r8 = new String[3];
                r8[0] = msg.from;
                r8[1] = "1";
                r8[2] = "2";
                cursorBlock = contentResolver.query(uri, strArr, "jid=? AND (user_status=? OR user_status=?)", r8, null);
                if (cursorBlock == null || cursorBlock.getCount() <= 0) {
                    cursorBlock.close();
                } else {
                    System.out.println("processMessage single chat, blocked friend");
                    return;
                }
            } finally {
                cursorBlock.close();
            }
        }
        for (PacketExtension packetExtension : msg.getExtensions()) {
            if (!(packetExtension instanceof MUCUser)) {
                if (packetExtension instanceof MessageContentTypeExtention) {
                    messageContentType = (MessageContentTypeExtention) packetExtension;
                } else if (packetExtension instanceof SeenReceived) {
                    System.out.println("processMessage packetExtension instanceof SeenReceived");
                    updateMessageStatus(((SeenReceived) packetExtension).id, MessageStatusType.SEEN.ordinal());
                    return;
                } else if (packetExtension instanceof ChatStateExtension) {
                    if (msg.type != Message.Type.error) {
                        String str;
                        String str2;
                        ChatState chatState = ((ChatStateExtension) packetExtension).state;
                        String str3 = msg.from;
                        Message.Type type = msg.type;
                        String str4 = msg.to;
                        System.out.println("onUserComposing " + str3);
                        if (type == Message.Type.groupchat) {
                            Cursor query;
                            try {
                                query = this.mContentResolver.query(UserProvider.CONTENT_URI_USER, null, "userId=?", new String[]{str3.substring(str3.indexOf(MqttTopic.TOPIC_LEVEL_SEPARATOR) + 1, str3.indexOf("-"))}, null);
                                try {
                                    query.moveToFirst();
                                    String string = query.getString(query.getColumnIndex("name"));
                                    query.close();
                                    str = string;
                                    str2 = r28;
                                } catch (Throwable th2) {
                                    th = th2;
                                    query.close();
                                    throw th;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                query = null;
                                query.close();
                                throw th;
                            }
                        }
                        str = null;
                        str2 = str3;
                        if (str2 != null && !str2.equalsIgnoreCase(Utils.getUserIdFromXmppUserId(str4))) {
                            boolean z = chatState == ChatState.composing;
                            if (type == Message.Type.chat) {
                                EventBus.getDefault().post(new TypingStatusEvent(getBareJID(str2), z));
                                return;
                            } else {
                                EventBus.getDefault().post(new TypingStatusEvent(Utils.getUserIdFromXmppUserId(str3), str, z));
                                return;
                            }
                        }
                        return;
                    }
                    return;
                }
            }
        }
        String[] fromJID = getJabberID(msg.from);
        String fromJID_string = getBareJID(msg.from);
        int direction = 1;
        CarbonExtension cc = CarbonExtension.getFrom(msg);
        DelayInformation timestamp = DelayInformation.getFrom(msg);
        if (cc != null) {
            timestamp = cc.fwd.delay;
        }
        if (timestamp != null) {
            timestamp.stamp.getTime();
        } else {
            System.currentTimeMillis();
        }
        if (cc != null) {
            Log.d("zamin.SmackableImp", "carbon: " + cc.toXML());
            msg = (Message) cc.fwd.forwardedPacket;
            if (cc.dir == Direction.sent) {
                fromJID = getJabberID(msg.to);
                direction = 0;
            } else {
                fromJID = getJabberID(msg.from);
                DeliveryReceipt dr = (DeliveryReceipt) msg.getExtension("received", "urn:xmpp:receipts");
                if (dr != null) {
                    Log.d("zamin.SmackableImp", "got CC'ed delivery receipt for " + dr.id);
                    updateMessageStatus(dr.id, MessageStatusType.DELIVERED.ordinal());
                }
            }
        }
        String chatMessage = msg.getBody(null);
        if (msg.type == Message.Type.error) {
            System.out.println("processMessage msg.getType() == Message.Type.error");
            if (fromJID[0] == "g") {
                System.out.println("Error related to group");
                if (chatMessage == null || !chatMessage.contains("Only occupants are allowed")) {
                    System.out.println("MUC Error related to group ELSE not in the group " + packet.toXML());
                } else {
                    System.out.println("MUC Error related to group and not in the group so rejoining " + packet.toXML());
                }
            }
        } else if (chatMessage == null) {
            System.out.println("processMessage chatMessage == null");
        } else {
            int is_new;
            boolean isUpdate;
            NewMessageReceivedEvent newMessageReceivedEvent;
            if (cc == null) {
                is_new = MessageStatusType.QUEUED.ordinal();
            } else {
                is_new = MessageStatusType.SENDING.ordinal();
            }
            if (msg.type == Message.Type.error) {
                is_new = MessageStatusType.FAILED.ordinal();
            }
            String message = msg.getBody(null);
            if (!message.equalsIgnoreCase(BuildConfig.VERSION_NAME)) {
                if (!message.contains("@rabtcdn.com")) {
                    System.out.println("processMessage calling addChatMessageToDB");
                    isUpdate = addChatMessageToDB$5fd7dbd4(direction, is_new, messageContentType, msg);
                    if (direction == MyMessageType.INCOMING_MSG.ordinal()) {
                        System.out.println("PROCESS MESSAGE INCOMING");
                        if (!isUpdate) {
                            System.out.println("PROCESS MESSAGE INCOMING");
                            this.mServiceCallBack.newMessage(fromJID_string, chatMessage, cc == null);
                        }
                        if (msg.thread != null) {
                            newMessageReceivedEvent = new NewMessageReceivedEvent();
                            newMessageReceivedEvent.threadId = msg.thread;
                            newMessageReceivedEvent.packetId = msg.packetID;
                            EventBus.getDefault().postSticky(newMessageReceivedEvent);
                        }
                    }
                }
            }
            isUpdate = true;
            if (direction == MyMessageType.INCOMING_MSG.ordinal()) {
                System.out.println("PROCESS MESSAGE INCOMING");
                if (isUpdate) {
                    System.out.println("PROCESS MESSAGE INCOMING");
                    if (cc == null) {
                    }
                    this.mServiceCallBack.newMessage(fromJID_string, chatMessage, cc == null);
                }
                if (msg.thread != null) {
                    newMessageReceivedEvent = new NewMessageReceivedEvent();
                    newMessageReceivedEvent.threadId = msg.thread;
                    newMessageReceivedEvent.packetId = msg.packetID;
                    EventBus.getDefault().postSticky(newMessageReceivedEvent);
                }
            }
        }
    }

    private boolean addChatMessageToDB$5fd7dbd4(int direction, int messageStatus, MessageContentTypeExtention messageContentType, Message message) {
        Cursor c;
        if (message.from.equalsIgnoreCase("rabtcdn.com") || message.packetID == null) {
            return false;
        }
        String threadId;
        String groupId;
        String friendId;
        boolean isExistingMessage = false;
        System.out.println("processMessage addChatMessageToDB !(message.getFrom().equalsIgnoreCase(PreferenceConstants.XMPP_SERVER))&& message.getPacketID() != null");
        String from = Utils.getUserIdFromXmppUserId(message.from);
        String to = Utils.getUserIdFromXmppUserId(message.to);
        String str = SHAMChatApplication.getConfig().userId;
        if (direction == MyMessageType.INCOMING_MSG.ordinal()) {
            System.out.println("processMessage addChatMessageToDB direction == MyMessageType.INCOMING_MSG.ordinal()");
            threadId = to + "-" + from;
            groupId = from;
            friendId = from;
        } else {
            System.out.println("processMessage addChatMessageToDB direction == MyMessageType.OUTGOING_MSG.ordinal()");
            threadId = message.thread;
            groupId = to;
            friendId = to;
        }
        saveOrUpdateThread(threadId, message, messageContentType, friendId, direction);
        System.out.println("processMessage addChatMessageToDB threadSaveOrUpdateSuccess");
        Cursor chatCursor = null;
        try {
            chatCursor = this.mContentResolver.query(ChatProviderNew.CONTENT_URI_CHAT, new String[]{"_id"}, "packet_id=?", new String[]{message.packetID}, null);
            if (chatCursor != null && chatCursor.getCount() > 0) {
                isExistingMessage = true;
            }
            if (!isExistingMessage) {
                System.out.println("processMessage addChatMessageToDB !isExistingMessage");
                ContentValues values = new ContentValues();
                values.put("message_recipient", to);
                values.put("message_type", Integer.valueOf(direction));
                ContentValues contentValues = values;
                contentValues.put("packet_id", message.packetID);
                values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, threadId);
                contentValues = values;
                contentValues.put("description", messageContentType.description);
                values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_MESSAGE_CONTENT_TYPE, Integer.valueOf(messageContentType.getMessageContentType().ordinal()));
                values.put("message_status", Integer.valueOf(messageStatus));
                System.out.println("Message id and status " + message.packetID + " " + messageStatus);
                if (message.type == Message.Type.groupchat) {
                    values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_GROUP_ID, groupId);
                }
                LocationDetails locDetails = (LocationDetails) message.getExtension(null, "urn:xmpp:location");
                if (locDetails != null) {
                    values.put("latitude", Double.valueOf(Double.parseDouble(locDetails.latitude)));
                    values.put("longitude", Double.valueOf(Double.parseDouble(locDetails.longitude)));
                }
                String body;
                if (message.type == Message.Type.groupchat && direction == MyMessageType.INCOMING_MSG.ordinal()) {
                    System.out.println("processMessage addChatMessageToDB message.getType() == Message.Type.groupchat && direction == MyMessageType.INCOMING_MSG.ordinal()");
                    String fromGroup = message.from;
                    String userId = fromGroup.substring(fromGroup.indexOf(MqttTopic.TOPIC_LEVEL_SEPARATOR) + 1, fromGroup.indexOf("-"));
                    String username = getUsernameToDisplayForGroup(userId, fromGroup);
                    values.put("message_sender", userId);
                    if (messageContentType.getMessageContentType() != MessageContentType.GROUP_INFO) {
                        String formattedMessage;
                        System.out.println("processMessage addChatMessageToDB messageContentType.getMessageContentType() != MessageContentType.GROUP_INFO");
                        if (messageContentType.getMessageContentType() == MessageContentType.TEXT) {
                            System.out.println("processMessage addChatMessageToDB messageContentType.getMessageContentType() == MessageContentType.TEXT");
                            formattedMessage = username + " " + this.mService.getResources().getString(2131493350) + " \n" + message.getBody(null);
                        } else {
                            System.out.println("processMessage addChatMessageToDB messageContentType.getMessageContentType() != MessageContentType.TEXT");
                            formattedMessage = username + " " + this.mService.getResources().getString(2131493359);
                            body = message.getBody(null);
                            values.put("uploaded_file_url", body);
                            if (body != null && body.contains("http://")) {
                                contentValues = values;
                                contentValues.put("file_size", Integer.valueOf(Utils.getFileSize(new URL(body))));
                            }
                        }
                        values.put("text_message", formattedMessage);
                    }
                } else {
                    System.out.println("processMessage addChatMessageToDB Single chat, both directions, Group chat outgoing");
                    values.put("message_sender", from);
                    contentValues = values;
                    contentValues.put("text_message", message.getBody(null));
                    if (direction == MyMessageType.INCOMING_MSG.ordinal()) {
                        body = message.getBody(null);
                        values.put("uploaded_file_url", body);
                        if (body != null && body.contains("http://")) {
                            try {
                                contentValues = values;
                                contentValues.put("file_size", Integer.valueOf(Utils.getFileSize(new URL(body))));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                System.out.println("processMessage addChatMessageToDB Updating chat message");
                c = null;
                c = this.mContentResolver.query(this.mContentResolver.insert(ChatProviderNew.CONTENT_URI_CHAT, values), null, null, null, null);
                c.moveToFirst();
                int dbId = c.getInt(c.getColumnIndex("_id"));
                if (c != null) {
                    c.close();
                }
                NewMessageEvent newMessageEvent = new NewMessageEvent();
                newMessageEvent.threadId = threadId;
                if (direction == MyMessageType.OUTGOING_MSG.ordinal()) {
                    newMessageEvent.direction = MyMessageType.OUTGOING_MSG.ordinal();
                } else {
                    newMessageEvent.direction = MyMessageType.INCOMING_MSG.ordinal();
                }
                System.out.println("SENDING MESSAGE event " + System.currentTimeMillis());
                EventBus.getDefault().postSticky(newMessageEvent);
                System.out.println("Chat test chat message id " + dbId + " thread id " + threadId);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        } catch (Throwable th) {
            chatCursor.close();
        }
        chatCursor.close();
        return isExistingMessage;
    }

    private boolean saveOrUpdateThread(String threadId, Message message, MessageContentTypeExtention messageContentType, String friendId, int direction) {
        System.out.println("processMessage addChatMessageToDB saveOrUpdateThread");
        Cursor threadCursor = null;
        try {
            threadCursor = this.mContentResolver.query(ChatProviderNew.CONTENT_URI_THREAD, new String[]{ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID}, "thread_id=?", new String[]{threadId}, null);
            boolean isExistingThread = false;
            if (threadCursor != null && threadCursor.getCount() > 0) {
                System.out.println("processMessage addChatMessageToDB saveOrUpdateThread isExisting thread");
                isExistingThread = true;
            }
            String lastMessage = message.getBody(null);
            boolean isRead = false;
            if (message.type != Message.Type.groupchat || direction != MyMessageType.INCOMING_MSG.ordinal()) {
                if (message.type == Message.Type.groupchat && direction == MyMessageType.OUTGOING_MSG.ordinal() && messageContentType.getMessageContentType() != MessageContentType.GROUP_INFO) {
                    System.out.println("processMessage addChatMessageToDB saveOrUpdateThread NOT group info");
                    switch (AnonymousClass17.f26x39e42954[messageContentType.getMessageContentType().ordinal()]) {
                        case Logger.SEVERE /*1*/:
                            lastMessage = this.mService.getResources().getString(2131493359) + " " + message.getBody(null);
                            System.out.println("processMessage addChatMessageToDB saveOrUpdateThread IMAGE " + lastMessage);
                            break;
                        case Logger.WARNING /*2*/:
                            lastMessage = this.mService.getResources().getString(2131493360);
                            System.out.println("processMessage addChatMessageToDB saveOrUpdateThread IMAGE " + lastMessage);
                            break;
                        case Logger.INFO /*3*/:
                            lastMessage = this.mService.getResources().getString(2131493363);
                            System.out.println("processMessage addChatMessageToDB saveOrUpdateThread VIDEO" + lastMessage);
                            break;
                        case Logger.CONFIG /*4*/:
                            lastMessage = this.mService.getResources().getString(2131493362);
                            System.out.println("processMessage addChatMessageToDB saveOrUpdateThread STICKER" + lastMessage);
                            break;
                        case Logger.FINE /*5*/:
                            lastMessage = this.mService.getResources().getString(2131493361);
                            System.out.println("processMessage addChatMessageToDB saveOrUpdateThread LOCATION" + lastMessage);
                            break;
                        case Logger.FINER /*6*/:
                            lastMessage = this.mService.getResources().getString(2131493364);
                            System.out.println("processMessage addChatMessageToDB saveOrUpdateThread VOICE" + lastMessage);
                            break;
                        default:
                            System.out.println("processMessage addChatMessageToDB saveOrUpdateThread DEFAULT" + lastMessage);
                            break;
                    }
                }
            }
            System.out.println("processMessage addChatMessageToDB saveOrUpdateThread groupChat and incoming");
            if (messageContentType.getMessageContentType() != MessageContentType.GROUP_INFO) {
                System.out.println("processMessage addChatMessageToDB saveOrUpdateThread NOT group info");
                String from = message.from;
                String username = getUsernameToDisplayForGroup(from.substring(from.indexOf(MqttTopic.TOPIC_LEVEL_SEPARATOR) + 1, from.indexOf("-")), message.from);
                System.out.println("processMessage addChatMessageToDB username received " + username);
                switch (AnonymousClass17.f26x39e42954[messageContentType.getMessageContentType().ordinal()]) {
                    case Logger.SEVERE /*1*/:
                        lastMessage = username + " " + this.mService.getResources().getString(2131493350) + " " + message.getBody(null);
                        System.out.println("processMessage addChatMessageToDB saveOrUpdateThread IMAGE " + lastMessage);
                        break;
                    case Logger.WARNING /*2*/:
                        lastMessage = username + " " + this.mService.getResources().getString(2131493360);
                        System.out.println("processMessage addChatMessageToDB saveOrUpdateThread IMAGE " + lastMessage);
                        break;
                    case Logger.INFO /*3*/:
                        lastMessage = username + " " + this.mService.getResources().getString(2131493363);
                        System.out.println("processMessage addChatMessageToDB saveOrUpdateThread VIDEO" + lastMessage);
                        break;
                    case Logger.CONFIG /*4*/:
                        lastMessage = username + " " + this.mService.getResources().getString(2131493362);
                        System.out.println("processMessage addChatMessageToDB saveOrUpdateThread STICKER" + lastMessage);
                        break;
                    case Logger.FINE /*5*/:
                        lastMessage = username + " " + this.mService.getResources().getString(2131493361);
                        System.out.println("processMessage addChatMessageToDB saveOrUpdateThread LOCATION" + lastMessage);
                        break;
                    case Logger.FINER /*6*/:
                        lastMessage = username + " " + this.mService.getResources().getString(2131493364);
                        System.out.println("processMessage addChatMessageToDB saveOrUpdateThread VOICE" + lastMessage);
                        break;
                    default:
                        System.out.println("processMessage addChatMessageToDB saveOrUpdateThread DEFAULT" + lastMessage);
                        break;
                }
            }
            System.out.println("processMessage addChatMessageToDB saveOrUpdateThread GROUP INFO");
            lastMessage = handleGroupJoinsLeaving(message, friendId, threadId);
            isRead = true;
            ContentValues values;
            if (isExistingThread) {
                System.out.println("processMessage addChatMessageToDB saveOrUpdateThread isExistingThread " + lastMessage);
                System.out.println("MessageContentType: " + messageContentType.getMessageContentType());
                System.out.println("MessageContentType ordinal: " + messageContentType.getMessageContentType().ordinal());
                values = new ContentValues();
                values.put("last_message", lastMessage.trim());
                values.put("last_message_content_type", Integer.valueOf(messageContentType.getMessageContentType().ordinal()));
                values.put("read_status", Integer.valueOf(isRead ? 1 : 0));
                values.put("last_updated_datetime", Utils.formatDate(new Date().getTime(), "yyyy/MM/dd HH:mm:ss"));
                values.put("last_message_direction", Integer.valueOf(direction));
                values.put("thread_owner", SHAMChatApplication.getConfig().userId);
                values.put("is_group_chat", Integer.valueOf(message.type == Message.Type.groupchat ? 1 : 0));
                this.mContentResolver.update(ChatProviderNew.CONTENT_URI_THREAD, values, "thread_id=?", new String[]{threadId});
            } else {
                System.out.println("processMessage addChatMessageToDB saveOrUpdateThread new thread " + lastMessage);
                values = new ContentValues();
                values.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, threadId);
                values.put("friend_id", friendId);
                values.put("read_status", Integer.valueOf(isRead ? 1 : 0));
                values.put("last_message", lastMessage.trim());
                values.put("last_message_content_type", Integer.valueOf(messageContentType.getMessageContentType().ordinal()));
                values.put("last_updated_datetime", Utils.formatDate(new Date().getTime(), "yyyy/MM/dd HH:mm:ss"));
                values.put("is_group_chat", Integer.valueOf(message.type == Message.Type.groupchat ? 1 : 0));
                values.put("thread_owner", SHAMChatApplication.getConfig().userId);
                values.put("last_message_direction", Integer.valueOf(direction));
                this.mContentResolver.insert(ChatProviderNew.CONTENT_URI_THREAD, values);
            }
            threadCursor.close();
            return true;
        } catch (Throwable th) {
            threadCursor.close();
        }
    }

    private String handleGroupJoinsLeaving(Message message, String userId, String threadId) {
        System.out.println("processMessage addChatMessageToDB handleGroupJoinsLeaving");
        String type = message.getBody(null);
        String displayMessage = null;
        String groupId = Utils.getUserIdFromXmppUserId(message.from);
        if (type.equalsIgnoreCase("l") || type.equalsIgnoreCase("k")) {
            System.out.println("processMessage addChatMessageToDB handleGroupJoinsLeaving l or k");
            this.mContentResolver.delete(UserProvider.CONTENT_URI_GROUP_MEMBER, FriendGroupMember.DB_GROUP + "=? AND " + FriendGroupMember.DB_FRIEND + "=?", new String[]{groupId, userId});
            if (userId.equalsIgnoreCase(this.mConfig.userId)) {
                displayMessage = this.mService.getResources().getString(2131493449);
                System.out.println("processMessage addChatMessageToDB handleGroupJoinsLeaving userId.equalsIgnoreCase(mConfig.getUserId())");
                try {
                    Presence leavePresence = new Presence(Type.unavailable);
                    leavePresence.to = Utils.createXmppRoomIDByUserId(groupId) + MqttTopic.TOPIC_LEVEL_SEPARATOR + userId;
                    mXMPPConnection.sendPacket(leavePresence);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ContentValues cvs = new ContentValues();
                cvs.put("last_message", displayMessage);
                this.mContentResolver.update(ChatProviderNew.CONTENT_URI_THREAD, cvs, "thread_id=?", new String[]{threadId});
                ContentValues cv2 = new ContentValues();
                cv2.put(FriendGroup.DID_LEAVE, Integer.valueOf(1));
                this.mContentResolver.update(UserProvider.CONTENT_URI_GROUP, cv2, FriendGroup.DB_ID + "=?", new String[]{groupId});
            }
        }
        return displayMessage;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String getUsernameToDisplayForGroup(java.lang.String r9, java.lang.String r10) {
        /*
        r8 = this;
        r7 = 0;
        r0 = java.lang.System.out;
        r1 = new java.lang.StringBuilder;
        r2 = "addChatMessageToDB getUsernameToDisplayForGroup from ";
        r1.<init>(r2);
        r1 = r1.append(r10);
        r2 = " userid ";
        r1 = r1.append(r2);
        r1 = r1.append(r9);
        r1 = r1.toString();
        r0.println(r1);
        r6 = 0;
        r0 = "g";
        r0 = r10.startsWith(r0);
        if (r0 == 0) goto L_0x00dc;
    L_0x0028:
        r0 = java.lang.System.out;
        r1 = "addChatMessageToDB getUsernameToDisplayForGroup from.startsWith(g)";
        r0.println(r1);
        r0 = r8.mContentResolver;	 Catch:{ Exception -> 0x00c6 }
        r1 = com.shamchat.androidclient.data.UserProvider.CONTENT_URI_USER;	 Catch:{ Exception -> 0x00c6 }
        r2 = 1;
        r2 = new java.lang.String[r2];	 Catch:{ Exception -> 0x00c6 }
        r3 = 0;
        r4 = "name";
        r2[r3] = r4;	 Catch:{ Exception -> 0x00c6 }
        r3 = "userId=?";
        r4 = 1;
        r4 = new java.lang.String[r4];	 Catch:{ Exception -> 0x00c6 }
        r5 = 0;
        r4[r5] = r9;	 Catch:{ Exception -> 0x00c6 }
        r5 = 0;
        r6 = r0.query(r1, r2, r3, r4, r5);	 Catch:{ Exception -> 0x00c6 }
        if (r6 == 0) goto L_0x00a5;
    L_0x004a:
        r0 = r6.getCount();	 Catch:{ Exception -> 0x00c6 }
        if (r0 <= 0) goto L_0x00a5;
    L_0x0050:
        r6.moveToFirst();	 Catch:{ Exception -> 0x00c6 }
        r0 = "name";
        r0 = r6.getColumnIndex(r0);	 Catch:{ Exception -> 0x00c6 }
        r7 = r6.getString(r0);	 Catch:{ Exception -> 0x00c6 }
        r0 = java.lang.System.out;	 Catch:{ Exception -> 0x00c6 }
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c6 }
        r2 = "addChatMessageToDB getUsernameToDisplayForGroup user found ";
        r1.<init>(r2);	 Catch:{ Exception -> 0x00c6 }
        r1 = r1.append(r7);	 Catch:{ Exception -> 0x00c6 }
        r1 = r1.toString();	 Catch:{ Exception -> 0x00c6 }
        r0.println(r1);	 Catch:{ Exception -> 0x00c6 }
        if (r7 == 0) goto L_0x0081;
    L_0x0073:
        r0 = r7.length();	 Catch:{ Exception -> 0x00c6 }
        if (r0 == 0) goto L_0x0081;
    L_0x0079:
        r0 = "null";
        r0 = r7.equalsIgnoreCase(r0);	 Catch:{ Exception -> 0x00c6 }
        if (r0 == 0) goto L_0x00a1;
    L_0x0081:
        r0 = "-";
        r0 = r10.indexOf(r0);	 Catch:{ Exception -> 0x00c6 }
        r0 = r0 + 1;
        r7 = r10.substring(r0);	 Catch:{ Exception -> 0x00c6 }
        r0 = java.lang.System.out;	 Catch:{ Exception -> 0x00c6 }
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c6 }
        r2 = "addChatMessageToDB getUsernameToDisplayForGroup username == null || username.equalsIgnoreCase(null) ";
        r1.<init>(r2);	 Catch:{ Exception -> 0x00c6 }
        r1 = r1.append(r7);	 Catch:{ Exception -> 0x00c6 }
        r1 = r1.toString();	 Catch:{ Exception -> 0x00c6 }
        r0.println(r1);	 Catch:{ Exception -> 0x00c6 }
    L_0x00a1:
        r6.close();
    L_0x00a4:
        return r7;
    L_0x00a5:
        r0 = "-";
        r0 = r10.indexOf(r0);	 Catch:{ Exception -> 0x00c6 }
        r0 = r0 + 1;
        r7 = r10.substring(r0);	 Catch:{ Exception -> 0x00c6 }
        r0 = java.lang.System.out;	 Catch:{ Exception -> 0x00c6 }
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c6 }
        r2 = "addChatMessageToDB getUsernameToDisplayForGroup ELSE ";
        r1.<init>(r2);	 Catch:{ Exception -> 0x00c6 }
        r1 = r1.append(r7);	 Catch:{ Exception -> 0x00c6 }
        r1 = r1.toString();	 Catch:{ Exception -> 0x00c6 }
        r0.println(r1);	 Catch:{ Exception -> 0x00c6 }
        goto L_0x00a1;
    L_0x00c6:
        r0 = move-exception;
        r0 = "-";
        r0 = r10.indexOf(r0);	 Catch:{ all -> 0x00d7 }
        r0 = r0 + 1;
        r7 = r10.substring(r0);	 Catch:{ all -> 0x00d7 }
        r6.close();
        goto L_0x00a4;
    L_0x00d7:
        r0 = move-exception;
        r6.close();
        throw r0;
    L_0x00dc:
        r0 = java.lang.System.out;
        r1 = "addChatMessageToDB getUsernameToDisplayForGroup not group";
        r0.println(r1);
        goto L_0x00a4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shamchat.androidclient.service.SmackableImp.getUsernameToDisplayForGroup(java.lang.String, java.lang.String):java.lang.String");
    }

    private void deleteRosterEntryFromDB(String jabberID) {
        String str = "zamin.SmackableImp";
        Log.d(str, "deleteRosterEntryFromDB: Deleted " + this.mContentResolver.delete(RosterProvider.CONTENT_URI, "jid = ?", new String[]{jabberID}) + " entries");
    }

    private void updateRosterEntryInDB(RosterEntry entry) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("jid", getBareJID(entry.user));
        contentValues.put("alias", getName(entry));
        Packet presence = mRoster.getPresence(entry.user);
        contentValues.put("status_mode", Integer.valueOf(getStatusInt(presence)));
        if (presence.type == Type.error) {
            contentValues.put("status_message", presence.error.toString());
        } else {
            System.out.println("Presence " + presence.status);
            contentValues.put("status_message", presence.status);
        }
        contentValues.put("roster_group", getGroup(entry.getGroups()));
        String str = entry.user;
        if (this.mContentResolver.update(RosterProvider.CONTENT_URI, contentValues, "jid = ?", new String[]{str}) == 0) {
            this.mContentResolver.insert(RosterProvider.CONTENT_URI, contentValues);
        }
    }

    private static String getGroup(Collection<RosterGroup> groups) {
        Iterator it = groups.iterator();
        if (it.hasNext()) {
            return ((RosterGroup) it.next()).name;
        }
        return BuildConfig.VERSION_NAME;
    }

    private static String getName(RosterEntry rosterEntry) {
        String name = rosterEntry.name;
        if (name != null && name.length() > 0) {
            return name;
        }
        name = XmppStringUtils.parseLocalpart(rosterEntry.user);
        if (name.length() > 0) {
            return name;
        }
        return rosterEntry.user;
    }

    private static int getStatusInt(Presence presence) {
        StatusMode valueOf = presence.type == Type.subscribe ? StatusMode.subscribe : presence.type == Type.available ? presence.mode != null ? StatusMode.valueOf(presence.mode.name()) : StatusMode.available : StatusMode.offline;
        return valueOf.ordinal();
    }

    public final String getLastError() {
        return this.mLastError;
    }

    public final String createRoom$7157d249(String roomName) {
        Log.d("zamin.SmackableImp", "createRoom Start");
        MultiUserChat muc = new MultiUserChat(mXMPPConnection, roomName);
        try {
            muc.create(roomName);
        } catch (NoResponseException e) {
            e.printStackTrace();
        } catch (SmackException e2) {
            e2.printStackTrace();
        }
        Form form = null;
        try {
            Packet mUCOwner = new MUCOwner();
            mUCOwner.to = muc.room;
            mUCOwner.setType(IQ.Type.get);
            form = Form.getFormFrom((IQ) muc.connection.createPacketCollectorAndSend(mUCOwner).nextResultOrThrow());
        } catch (NoResponseException e3) {
            e3.printStackTrace();
        } catch (NotConnectedException e4) {
            e4.printStackTrace();
        }
        try {
            if ("form".equals(form.dataForm.type)) {
                Form submitForm = new Form("submit");
                for (FormField formField : form.dataForm.getFields()) {
                    if (formField.variable != null) {
                        FormField formField2 = new FormField(formField.variable);
                        formField2.type = formField.type;
                        submitForm.dataForm.addField(formField2);
                        if ("hidden".equals(formField.type)) {
                            List arrayList = new ArrayList();
                            for (String add : formField.getValues()) {
                                arrayList.add(add);
                            }
                            submitForm.setAnswer(formField.variable, arrayList);
                        }
                    }
                }
                for (int i = 0; i < form.dataForm.getFields().size(); i++) {
                    FormField field = (FormField) form.dataForm.getFields().get(i);
                    if (!("hidden".equals(field.type) || field.variable == null)) {
                        submitForm.setDefaultAnswer(field.variable);
                    }
                }
                FormField formField3 = submitForm.getField("muc#roomconfig_roomname");
                if (formField3 == null) {
                    throw new IllegalArgumentException("Field not found for the specified variable name.");
                } else if ("text-multi".equals(formField3.type) || "text-private".equals(formField3.type) || "text-single".equals(formField3.type) || "jid-single".equals(formField3.type) || "hidden".equals(formField3.type)) {
                    submitForm.setAnswer(formField3, (Object) roomName);
                    formField3 = submitForm.getField("muc#roomconfig_persistentroom");
                    if (formField3 == null) {
                        throw new IllegalArgumentException("Field not found for the specified variable name.");
                    } else if (MessageDetailsActivity.EXTRA_BOOLEAN.equals(formField3.type)) {
                        submitForm.setAnswer(formField3, (Object) "1");
                        try {
                            mUCOwner = new MUCOwner();
                            mUCOwner.to = muc.room;
                            mUCOwner.setType(IQ.Type.set);
                            mUCOwner.addExtension(submitForm.getDataFormToSend());
                            muc.connection.createPacketCollectorAndSend(mUCOwner).nextResultOrThrow();
                        } catch (NoResponseException e32) {
                            e32.printStackTrace();
                        } catch (NotConnectedException e42) {
                            e42.printStackTrace();
                        }
                        Log.d("zamin.SmackableImp", "createRoom End");
                        return "success";
                    } else {
                        throw new IllegalArgumentException("This field is not of type boolean.");
                    }
                } else {
                    throw new IllegalArgumentException("This field is not of type String.");
                }
            }
            throw new IllegalStateException("Only forms of type \"form\" could be answered");
        } catch (XMPPException e5) {
            return MqttServiceConstants.TRACE_EXCEPTION;
        }
    }

    public final String addFriendsToChatRoom(String roomName, List<String> friendsJIDs) {
        MultiUserChat muc = new MultiUserChat(mXMPPConnection, roomName);
        Cursor cursor = this.mContentResolver.query(UserProvider.CONTENT_URI_GROUP, new String[]{FriendGroup.DB_NAME}, FriendGroup.CHAT_ROOM_NAME + "=?", new String[]{roomName}, null);
        String name = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(FriendGroup.DB_NAME));
        }
        String groupName = name;
        cursor.close();
        for (String friendsJID : friendsJIDs) {
            String friendsJID2 = friendsJID2 + "@rabtcdn.com";
            try {
                String str = groupName + MqttTopic.TOPIC_LEVEL_SEPARATOR + this.mConfig.userId;
                Packet message = new Message();
                message.to = muc.room;
                PacketExtension mUCUser = new MUCUser();
                Invite invite = new Invite();
                invite.to = friendsJID2;
                invite.reason = str;
                mUCUser.invite = invite;
                message.addExtension(mUCUser);
                muc.connection.sendPacket(message);
            } catch (NotConnectedException e) {
                e.printStackTrace();
            }
        }
        return "success";
    }

    public final String sendMessage(String toJID, String message, String messageType, boolean isGroupChat, String packetId, String latitude, String longitude, String description) {
        Message newMessage;
        boolean isMultimediaMessage = false;
        if (isGroupChat) {
            newMessage = new Message(toJID, Message.Type.groupchat);
        } else {
            newMessage = new Message(toJID, Message.Type.chat);
        }
        if (packetId != null) {
            isMultimediaMessage = true;
            newMessage.packetID = packetId;
        }
        String threadId = Utils.getUserIdFromXmppUserId(this.mConfig.jabberID) + "-" + Utils.getUserIdFromXmppUserId(toJID);
        newMessage.setBody(message);
        newMessage.from = this.mConfig.jabberID;
        newMessage.thread = threadId;
        newMessage.addExtension(new DeliveryReceiptRequest());
        MessageContentTypeExtention messageContentType = null;
        if (messageType.equals(MessageContentType.TEXT.toString())) {
            messageContentType = new MessageContentTypeExtention(MessageContentType.TEXT.ordinal(), BuildConfig.VERSION_NAME);
            newMessage.addExtension(messageContentType);
        } else if (messageType.equals(MessageContentType.IMAGE.toString())) {
            messageContentType = new MessageContentTypeExtention(MessageContentType.IMAGE.ordinal(), description);
            newMessage.addExtension(messageContentType);
        } else if (messageType.equals(MessageContentType.VIDEO.toString())) {
            messageContentType = new MessageContentTypeExtention(MessageContentType.VIDEO.ordinal(), description);
            newMessage.addExtension(messageContentType);
        } else if (messageType.equals(MessageContentType.VOICE_RECORD.toString())) {
            messageContentType = new MessageContentTypeExtention(MessageContentType.VOICE_RECORD.ordinal(), BuildConfig.VERSION_NAME);
            newMessage.addExtension(messageContentType);
        } else if (messageType.equals(MessageContentType.GROUP_INFO.toString())) {
            messageContentType = new MessageContentTypeExtention(MessageContentType.GROUP_INFO.ordinal(), BuildConfig.VERSION_NAME);
            newMessage.addExtension(messageContentType);
        } else if (messageType.equals(MessageContentType.STICKER.toString())) {
            messageContentType = new MessageContentTypeExtention(MessageContentType.STICKER.ordinal(), BuildConfig.VERSION_NAME);
            newMessage.addExtension(messageContentType);
        } else if (messageType.equals(MessageContentType.LOCATION.toString())) {
            messageContentType = new MessageContentTypeExtention(MessageContentType.LOCATION.ordinal(), description);
            newMessage.addExtension(messageContentType);
            newMessage.addExtension(new LocationDetails(String.valueOf(latitude), String.valueOf(longitude)));
        }
        int ordinal;
        int ordinal2;
        if (isAuthenticated()) {
            if (!isMultimediaMessage) {
                System.out.println("SENDING MESSAGE 2 " + System.currentTimeMillis());
                ordinal = MyMessageType.OUTGOING_MSG.ordinal();
                ordinal2 = MessageStatusType.SENDING.ordinal();
                System.currentTimeMillis();
                addChatMessageToDB$5fd7dbd4(ordinal, ordinal2, messageContentType, newMessage);
                System.out.println("SENDING MESSAGE 3 " + System.currentTimeMillis());
            }
            try {
                mXMPPConnection.sendPacket(newMessage);
            } catch (NotConnectedException e) {
                e.printStackTrace();
            }
            Log.d("zamin.SmackableImp", "Message sent " + newMessage.toXML());
        } else {
            Log.d("zamin.SmackableImp", "Not authenticated");
            if (!isMultimediaMessage) {
                ordinal = MyMessageType.OUTGOING_MSG.ordinal();
                ordinal2 = MessageStatusType.QUEUED.ordinal();
                System.currentTimeMillis();
                addChatMessageToDB$5fd7dbd4(ordinal, ordinal2, messageContentType, newMessage);
            }
        }
        return "success";
    }

    public final String sendSeenPacket(String packetId, String to, String threadId) {
        if (!isAuthenticated()) {
            return MqttServiceConstants.TRACE_EXCEPTION;
        }
        Message message = new Message(Utils.createXmppUserIdByUserId(to));
        message.thread = threadId;
        message.addExtension(new SeenReceived(packetId));
        ContentValues contentValues = new ContentValues();
        contentValues.put("message_status", Integer.valueOf(MessageStatusType.SEEN.ordinal()));
        Cursor query = this.mContentResolver.query(ChatProviderNew.CONTENT_URI_CHAT, null, "packet_id=?", new String[]{packetId}, null);
        if (query.getCount() > 0) {
            query.moveToFirst();
            String string = query.getString(query.getColumnIndex(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID));
            System.out.println("NEW way thread id " + string);
            Uri parse = Uri.parse("content://org.zamin.androidclient.provider.Messages/chat_message/" + Integer.toString(query.getInt(query.getColumnIndex("_id"))));
            this.mContentResolver.update(parse, contentValues, "_id = ? AND message_type = " + MyMessageType.INCOMING_MSG.ordinal(), new String[]{r2});
            this.jobManager.addJobInBackground(new MessageStateChangedJob(string, packetId, MessageStatusType.SEEN.type));
        }
        query.close();
        try {
            mXMPPConnection.sendPacket(message);
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
        return "success";
    }

    public final long getLastActivity(String friendJabberId) {
        try {
            LastActivityManager lastManager = LastActivityManager.getInstanceFor(mXMPPConnection);
            LastActivity activity = null;
            try {
                activity = (LastActivity) lastManager.connection().createPacketCollectorAndSend(new LastActivity(friendJabberId)).nextResultOrThrow();
            } catch (NoResponseException e) {
                e.printStackTrace();
            } catch (NotConnectedException e2) {
                e2.printStackTrace();
            }
            return activity.lastActivity;
        } catch (XMPPException e3) {
            e3.printStackTrace();
            return 0;
        }
    }

    public final void invitationReceived$fdc76a0(String room, String reason) {
        System.out.println("Group chat test process invitation start");
        processInvitation$3b99f9eb(room, reason);
    }

    private void processInvitation$3b99f9eb(String room, String reason) {
        Cursor groupCursor = this.mContentResolver.query(UserProvider.CONTENT_URI_GROUP, new String[]{FriendGroup.DB_ID}, FriendGroup.CHAT_ROOM_NAME + "=?", new String[]{room}, null);
        boolean isUpdate = false;
        if (groupCursor.getCount() > 0) {
            isUpdate = true;
        }
        String groupName = reason.substring(0, reason.indexOf(MqttTopic.TOPIC_LEVEL_SEPARATOR));
        String str = reason;
        String ownerID = str.substring(reason.indexOf(MqttTopic.TOPIC_LEVEL_SEPARATOR) + 1);
        groupCursor.close();
        System.out.println(" invitation group name " + groupName);
        FriendGroup group = new FriendGroup();
        group.id = Utils.getUserIdFromXmppUserId(room);
        group.name = groupName;
        group.recordOwnerId = ownerID;
        group.chatRoomName = room;
        ContentValues values = new ContentValues();
        values.put(FriendGroup.DB_ID, group.id);
        values.put(FriendGroup.DB_NAME, group.name);
        values.put(FriendGroup.DB_RECORD_OWNER, group.recordOwnerId);
        values.put(FriendGroup.CHAT_ROOM_NAME, group.chatRoomName);
        MessageThread thread = new MessageThread();
        thread.friendId = Utils.getUserIdFromXmppUserId(room);
        thread.isGroupChat = true;
        thread.threadOwner = this.mConfig.userId;
        ContentValues vals = new ContentValues();
        vals.put(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, thread.getThreadId());
        ContentValues contentValues = vals;
        contentValues.put("friend_id", thread.friendId);
        vals.put("read_status", Integer.valueOf(0));
        vals.put("last_updated_datetime", Utils.formatDate(new Date().getTime(), "yyyy/MM/dd HH:mm:ss"));
        vals.put("is_group_chat", Integer.valueOf(1));
        contentValues = vals;
        contentValues.put("thread_owner", thread.threadOwner);
        if (isUpdate) {
            this.mContentResolver.update(UserProvider.CONTENT_URI_GROUP, values, FriendGroup.DB_ID + "=?", new String[]{group.id});
            contentValues = vals;
            this.mContentResolver.update(ChatProviderNew.CONTENT_URI_THREAD, contentValues, "thread_id=?", new String[]{thread.getThreadId()});
        } else {
            this.mContentResolver.insert(UserProvider.CONTENT_URI_GROUP, values);
            vals.put("last_message", "New Group");
            vals.put("last_message_direction", Integer.valueOf(MyMessageType.INCOMING_MSG.ordinal()));
            vals.put("last_message_content_type", Integer.valueOf(0));
            this.mContentResolver.insert(ChatProviderNew.CONTENT_URI_THREAD, vals);
        }
        FriendGroupMember admin = new FriendGroupMember(group.id, ownerID);
        admin.assignUniqueId(this.mConfig.userId);
        ContentValues adminCv = new ContentValues();
        adminCv.put(FriendGroupMember.DB_ID, admin.id);
        adminCv.put(FriendGroupMember.DB_FRIEND, admin.friendId);
        adminCv.put(FriendGroupMember.DB_GROUP, admin.groupID);
        adminCv.put(FriendGroupMember.DB_FRIEND_IS_ADMIN, Integer.valueOf(1));
        if (this.mContentResolver.update(UserProvider.CONTENT_URI_GROUP_MEMBER, adminCv, FriendGroupMember.DB_GROUP + "=? AND " + FriendGroupMember.DB_FRIEND + "=?", new String[]{admin.groupID, ownerID}) == 0) {
            this.mContentResolver.insert(UserProvider.CONTENT_URI_GROUP_MEMBER, adminCv);
        }
        FriendGroupMember myself = new FriendGroupMember(group.id, this.mConfig.userId);
        myself.assignUniqueId(this.mConfig.userId);
        ContentValues groupMember = new ContentValues();
        groupMember.put(FriendGroupMember.DB_ID, myself.id);
        groupMember.put(FriendGroupMember.DB_FRIEND, myself.friendId);
        groupMember.put(FriendGroupMember.DB_GROUP, myself.groupID);
        groupMember.put(FriendGroupMember.DB_FRIEND_DID_JOIN, Integer.valueOf(1));
        if (this.mContentResolver.update(UserProvider.CONTENT_URI_GROUP_MEMBER, groupMember, FriendGroupMember.DB_GROUP + "=? AND " + FriendGroupMember.DB_FRIEND + "=?", new String[]{myself.groupID, this.mConfig.userId}) == 0) {
            this.mContentResolver.insert(UserProvider.CONTENT_URI_GROUP_MEMBER, groupMember);
        }
    }

    public final String onComposing(String toJabberId, String composingText, boolean isGroupChat) {
        System.out.println("On composing composing toJabberId " + toJabberId);
        Message message = new Message();
        if (isGroupChat) {
            message.setType(Message.Type.groupchat);
        } else {
            message.setType(Message.Type.chat);
        }
        message.to = toJabberId;
        if (composingText.length() == 0) {
            message.addExtension(new ChatStateExtension(ChatState.active));
        } else {
            message.addExtension(new ChatStateExtension(ChatState.composing));
        }
        System.out.println("onComposing jabber id " + SHAMChatApplication.getConfig().jabberID);
        message.from = SHAMChatApplication.getConfig().jabberID;
        message.packetID = "composing_test";
        if (isAuthenticated()) {
            try {
                mXMPPConnection.sendPacket(message);
            } catch (NotConnectedException e) {
                e.printStackTrace();
            }
        }
        return "success";
    }

    public final void onEvent(SyncInviteProcessEvent event) {
        System.out.println("SyncInvitation group process: " + event.reason);
        processInvitation$3b99f9eb(Utils.createXmppRoomIDByUserId(event.groupId), event.reason);
    }

    public final void onEvent(SyncGroupMessageProcessEvent event) {
        System.out.println("offline group message process: " + event.message.getBody(null));
        processMessage(event.message);
    }

    public final void onEvent(SyncGroupJoinChatroomEvent event) {
        System.out.println("group join chatroom: " + event.roomname);
    }

    public static void notifyUser(String message) {
        NotifySimple.notifcation$34410889(SHAMChatApplication.getMyApplicationContext(), message, new Intent());
    }
}

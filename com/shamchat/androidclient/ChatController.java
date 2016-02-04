package com.shamchat.androidclient;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import com.shamchat.androidclient.IXMPPChatCallback.Stub;
import com.shamchat.androidclient.chat.XMPPChatServiceAdapter;
import com.shamchat.androidclient.data.ZaminConfiguration;
import com.shamchat.androidclient.service.IXMPPChatService;
import com.shamchat.androidclient.service.XMPPService;
import com.shamchat.androidclient.util.ConnectionState;
import com.shamchat.androidclient.util.StatusMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class ChatController {
    private static ChatController chatController;
    public Context f23c;
    public XMPPChatServiceAdapter chatServiceAdapter;
    ZaminConfiguration mConfig;
    public String mWithJabberID;
    private Handler mainHandler;
    private ServiceConnection xmppServiceConnection;
    public Intent xmppServiceIntent;

    /* renamed from: com.shamchat.androidclient.ChatController.1 */
    class C10591 implements ServiceConnection {
        final /* synthetic */ Stub val$callback;

        C10591(Stub stub) {
            this.val$callback = stub;
        }

        @TargetApi(11)
        public final void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("com.shamchat.androidclient.ChatController", "called onServiceConnected()");
            ChatController.this.chatServiceAdapter = new XMPPChatServiceAdapter(IXMPPChatService.Stub.asInterface(service));
            XMPPChatServiceAdapter xMPPChatServiceAdapter = ChatController.this.chatServiceAdapter;
            try {
                xMPPChatServiceAdapter.xmppServiceStub.registerChatCallback(this.val$callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NullPointerException e2) {
                e2.printStackTrace();
            }
            Log.i("com.shamchat.androidclient.ChatController", "getConnectionState(): " + ChatController.this.chatServiceAdapter.getConnectionState());
            ConnectionState cs = ChatController.this.chatServiceAdapter.getConnectionState();
            Log.d("com.shamchat.androidclient.ChatController", "updateConnectionState: " + cs);
            switch (C10602.$SwitchMap$com$shamchat$androidclient$util$ConnectionState[cs.ordinal()]) {
                case Logger.SEVERE /*1*/:
                case Logger.WARNING /*2*/:
                case Logger.INFO /*3*/:
                case Logger.CONFIG /*4*/:
                case Logger.FINE /*5*/:
                case Logger.FINER /*6*/:
                    ConnectionState connectionState = ConnectionState.OFFLINE;
                    break;
            }
            if (ChatController.this.mConfig.reconnect_required && cs == ConnectionState.ONLINE) {
                ChatController.this.chatServiceAdapter.disconnect();
                try {
                    ChatController.this.chatServiceAdapter.xmppServiceStub.connect();
                } catch (RemoteException e3) {
                    e3.printStackTrace();
                }
            } else if (ChatController.this.mConfig.presence_required && ChatController.this.isConnected()) {
                ChatController.this.chatServiceAdapter.setStatusFromConfig();
            }
        }

        public final void onServiceDisconnected(ComponentName name) {
            Log.i("com.shamchat.androidclient.ChatController", "called onServiceDisconnected()");
        }
    }

    /* renamed from: com.shamchat.androidclient.ChatController.2 */
    static /* synthetic */ class C10602 {
        static final /* synthetic */ int[] $SwitchMap$com$shamchat$androidclient$util$ConnectionState;

        static {
            $SwitchMap$com$shamchat$androidclient$util$ConnectionState = new int[ConnectionState.values().length];
            try {
                $SwitchMap$com$shamchat$androidclient$util$ConnectionState[ConnectionState.CONNECTING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$shamchat$androidclient$util$ConnectionState[ConnectionState.DISCONNECTING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$shamchat$androidclient$util$ConnectionState[ConnectionState.DISCONNECTED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$shamchat$androidclient$util$ConnectionState[ConnectionState.RECONNECT_NETWORK.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$shamchat$androidclient$util$ConnectionState[ConnectionState.RECONNECT_DELAYED.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$shamchat$androidclient$util$ConnectionState[ConnectionState.OFFLINE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$shamchat$androidclient$util$ConnectionState[ConnectionState.ONLINE.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    private ChatController() {
        this.mainHandler = new Handler();
    }

    public final String sendMessage(String toJID, String message, String messageType, boolean isGroupChat, String packetId, String latitude, String longitude, String description) {
        return this.chatServiceAdapter.sendMessage(toJID, message, messageType, isGroupChat, packetId, latitude, longitude, description);
    }

    public final String sendSeenPacket(String packetId, String to, String threadId) {
        try {
            return this.chatServiceAdapter.sendSeenPacket(packetId, to, threadId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ChatController getInstance(Context c) {
        if (chatController != null) {
            chatController.f23c = c;
            return chatController;
        }
        ChatController chatController = new ChatController();
        chatController = chatController;
        chatController.f23c = c;
        return chatController;
    }

    public final void registerXMPPService(Stub callback) {
        Log.i("com.shamchat.androidclient.ChatController", "called startXMPPService()");
        this.mConfig = SHAMChatApplication.getConfig();
        this.xmppServiceIntent = new Intent(this.f23c, XMPPService.class);
        this.xmppServiceIntent.setAction("com.shamchat.androidclient.XMPPSERVICE");
        this.xmppServiceConnection = new C10591(callback);
    }

    public final boolean isConnected() {
        if (this.chatServiceAdapter != null) {
            if (this.chatServiceAdapter.getConnectionState() == ConnectionState.ONLINE) {
                return true;
            }
        }
        return false;
    }

    public final void unbindXMPPService() {
        try {
            this.f23c.unbindService(this.xmppServiceConnection);
        } catch (IllegalArgumentException e) {
            Log.e("com.shamchat.androidclient.ChatController", "Service wasn't bound!");
        }
    }

    public final void bindXMPPService() {
        try {
            this.f23c.bindService(this.xmppServiceIntent, this.xmppServiceConnection, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final boolean isConnecting() {
        return this.chatServiceAdapter != null && this.chatServiceAdapter.getConnectionState() == ConnectionState.CONNECTING;
    }

    public final void startConnection(boolean create_account) {
        this.xmppServiceIntent.putExtra("create_account", create_account);
        this.f23c.startService(this.xmppServiceIntent);
        Log.d("com.shamchat.androidclient.ChatController", "Start Connection END");
    }

    public final void login(String username, String password) {
        boolean oldState;
        boolean z = true;
        Log.d("com.shamchat.androidclient.ChatController", "Login start");
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this.f23c).edit();
        this.mWithJabberID = username + "@rabtcdn.com";
        editor.putString("account_jabberID", this.mWithJabberID);
        editor.putString("account_jabberPW", password);
        editor.commit();
        if (isConnected() || isConnecting()) {
            oldState = true;
        } else {
            oldState = false;
        }
        Editor edit = PreferenceManager.getDefaultSharedPreferences(this.f23c).edit();
        String str = "connstartup";
        if (oldState) {
            z = false;
        }
        edit.putBoolean(str, z).commit();
        if (!isMyServiceRunning(XMPPService.class)) {
            startConnection(false);
        }
    }

    private static boolean isMyServiceRunning(Class<?> serviceClass) {
        for (RunningServiceInfo service : ((ActivityManager) SHAMChatApplication.getMyApplicationContext().getSystemService("activity")).getRunningServices(ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public final void unRegisterChatCallback(Stub callback) {
        if (this.chatServiceAdapter != null && callback != null) {
            try {
                this.chatServiceAdapter.xmppServiceStub.unregisterChatCallback(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NullPointerException e2) {
                e2.printStackTrace();
            }
            this.f23c.stopService(this.xmppServiceIntent);
        }
    }

    public final void setStatus(StatusMode statusMode, String message) {
        Editor prefedit = PreferenceManager.getDefaultSharedPreferences(SHAMChatApplication.getMyApplicationContext()).edit();
        prefedit.putString("status_mode", statusMode.name());
        ZaminConfiguration mConfig = SHAMChatApplication.getConfig();
        System.out.println("Message " + message + " status mode " + statusMode);
        if (!message.equals(mConfig.statusMessage)) {
            List<String> smh = new ArrayList(Arrays.asList(mConfig.statusMessageHistory));
            if (!smh.contains(message)) {
                smh.add(message);
            }
            prefedit.putString("status_message_history", TextUtils.join("\u001e", smh));
        }
        prefedit.putString("status_message", message);
        prefedit.commit();
        try {
            this.chatServiceAdapter.setStatusFromConfig();
        } catch (Exception e) {
        }
    }

    public final void onComposing$3b99f9eb(String toJabberId, String composingText) {
        try {
            this.chatServiceAdapter.xmppServiceStub.onComposing(toJabberId, composingText, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

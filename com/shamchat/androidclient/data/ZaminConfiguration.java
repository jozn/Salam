package com.shamchat.androidclient.data;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.support.v4.media.TransportMediator;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import com.shamchat.androidclient.exceptions.ZaminXMPPAdressMalformedException;
import com.shamchat.androidclient.util.XMPPHelper;
import com.shamchat.utils.Utils;
import java.util.Arrays;
import java.util.HashSet;

public final class ZaminConfiguration implements OnSharedPreferenceChangeListener {
    private static final HashSet<String> PRESENCE_PREFS;
    private static final HashSet<String> RECONNECT_PREFS;
    public boolean autoConnect;
    public String chatFontSize;
    public String customServer;
    public boolean enableGroups;
    public boolean foregroundService;
    public boolean isLEDNotify;
    public String jabberID;
    public boolean jid_configured;
    public boolean messageCarbons;
    public Uri notifySound;
    public String password;
    public int port;
    private final SharedPreferences prefs;
    public boolean presence_required;
    public int priority;
    public boolean reconnect_required;
    public boolean reportCrash;
    public boolean require_ssl;
    public String ressource;
    public String server;
    public boolean showOffline;
    public boolean smackdebug;
    public String statusMessage;
    public String[] statusMessageHistory;
    public String statusMode;
    public String theme;
    public boolean ticker;
    public String userId;
    public String userName;
    public String vibraNotify;

    static {
        RECONNECT_PREFS = new HashSet(Arrays.asList(new String[]{"account_jabberID", "account_jabberPW", "account_customserver", "account_port", "account_resource", "foregroundService", "require_ssl", "smackdebug"}));
        PRESENCE_PREFS = new HashSet(Arrays.asList(new String[]{"carbons", "account_prio", "status_mode", "status_message"}));
    }

    public ZaminConfiguration(SharedPreferences _prefs) {
        this.reconnect_required = false;
        this.presence_required = false;
        this.prefs = _prefs;
        this.prefs.registerOnSharedPreferenceChangeListener(this);
        loadPrefs(this.prefs);
    }

    protected final void finalize() {
        this.prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    public final void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        Log.i("zamin.Configuration", "onSharedPreferenceChanged(): " + key);
        loadPrefs(prefs);
        if (RECONNECT_PREFS.contains(key)) {
            this.reconnect_required = true;
        }
        if (PRESENCE_PREFS.contains(key)) {
            this.presence_required = true;
        }
    }

    private void loadPrefs(SharedPreferences prefs) {
        int i = TransportMediator.KEYCODE_MEDIA_PAUSE;
        this.jid_configured = false;
        this.isLEDNotify = prefs.getBoolean("led", false);
        this.vibraNotify = prefs.getString("vibration_list", "SYSTEM");
        this.notifySound = Uri.parse(prefs.getString("ringtone", BuildConfig.VERSION_NAME));
        this.ticker = prefs.getBoolean("ticker", true);
        this.password = prefs.getString("account_jabberPW", BuildConfig.VERSION_NAME);
        this.ressource = prefs.getString("account_resource", "zamin");
        this.port = XMPPHelper.tryToParseInt(prefs.getString("account_port", "5222"), 5222);
        int tryToParseInt = XMPPHelper.tryToParseInt(prefs.getString("account_prio", "0"), 0);
        if (tryToParseInt <= TransportMediator.KEYCODE_MEDIA_PAUSE) {
            i = tryToParseInt < -127 ? -127 : tryToParseInt;
        }
        this.priority = i;
        this.foregroundService = prefs.getBoolean("foregroundService", false);
        this.autoConnect = prefs.getBoolean("connstartup", false);
        this.messageCarbons = prefs.getBoolean("carbons", false);
        this.smackdebug = prefs.getBoolean("smackdebug", false);
        this.reportCrash = prefs.getBoolean("reportcrash", false);
        this.jabberID = prefs.getString("account_jabberID", BuildConfig.VERSION_NAME);
        this.userId = Utils.getUserIdFromXmppUserId(this.jabberID);
        this.customServer = prefs.getString("account_customserver", BuildConfig.VERSION_NAME);
        this.require_ssl = prefs.getBoolean("require_ssl", false);
        this.statusMode = prefs.getString("status_mode", "available");
        this.statusMessage = prefs.getString("status_message", BuildConfig.VERSION_NAME);
        this.statusMessageHistory = prefs.getString("status_message_history", this.statusMessage).split("\u001e");
        this.theme = prefs.getString("theme", "dark");
        this.chatFontSize = prefs.getString("setSizeChat", "18");
        this.showOffline = prefs.getBoolean("showOffline", false);
        this.enableGroups = prefs.getBoolean("enableGroups", true);
        try {
            String[] split = XMPPHelper.verifyJabberID(this.jabberID).split("@");
            this.userName = split[0];
            this.server = split[1];
            this.jid_configured = true;
        } catch (ZaminXMPPAdressMalformedException e) {
            Log.e("zamin.Configuration", "Exception in getPreferences(): " + e);
        }
    }
}

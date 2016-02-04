package com.shamchat.androidclient;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.util.Log;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.JobManager.DefaultQueueFactory;
import com.path.android.jobqueue.config.Configuration.Builder;
import com.path.android.jobqueue.log.CustomLogger;
import com.path.android.jobqueue.network.NetworkUtilImpl;
import com.shamchat.androidclient.data.ZaminConfiguration;
import de.duenndns.ssl.MemorizingTrustManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SHAMChatApplication extends Application {
    public static Map<String, Bitmap> CHAT_IMAGES;
    public static Map<String, Boolean> CHAT_ROOMS;
    public static Map<String, Bitmap> MOMENT_IMAGES;
    public static Map<String, Bitmap> USER_IMAGES;
    public static Context context;
    private static SHAMChatApplication instance;
    public final Handler handler;
    public JobManager jobManager;
    private ZaminConfiguration mConfig;
    public MemorizingTrustManager mMTM;

    /* renamed from: com.shamchat.androidclient.SHAMChatApplication.1 */
    class C10611 implements CustomLogger {
        C10611() {
        }

        public final boolean isDebugEnabled() {
            return true;
        }

        public final void m41d(String text, Object... args) {
            Log.d("JOBS", String.format(text, args));
        }

        public final void m43e(Throwable t, String text, Object... args) {
            Log.e("JOBS", String.format(text, args), t);
        }

        public final void m42e(String text, Object... args) {
            Log.e("JOBS", String.format(text, args));
        }
    }

    static {
        CHAT_ROOMS = new HashMap();
        USER_IMAGES = new HashMap();
        MOMENT_IMAGES = new HashMap();
        CHAT_IMAGES = new HashMap();
    }

    public SHAMChatApplication() {
        instance = this;
        this.handler = new Handler();
    }

    public void attachBaseContext(Context base) {
        MultiDex.install(base);
        super.attachBaseContext(base);
    }

    public static SHAMChatApplication getInstance() {
        if (instance != null) {
            return instance;
        }
        throw new IllegalStateException();
    }

    public void onCreate() {
        this.mMTM = new MemorizingTrustManager(this);
        this.mConfig = new ZaminConfiguration(PreferenceManager.getDefaultSharedPreferences(this));
        Builder builder = new Builder(this);
        builder.configuration.customLogger = new C10611();
        builder.configuration.minConsumerCount = 5;
        builder.configuration.maxConsumerCount = 10;
        builder.configuration.loadFactor = 3;
        builder.configuration.consumerKeepAlive = 120;
        if (builder.configuration.queueFactory == null) {
            builder.configuration.queueFactory = new DefaultQueueFactory();
        }
        if (builder.configuration.networkUtil == null) {
            builder.configuration.networkUtil = new NetworkUtilImpl(builder.appContext);
        }
        this.jobManager = new JobManager(this, builder.configuration);
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        context = getApplicationContext();
        String resource = String.format("%s.%08X", new Object[]{getString(2131492978), Integer.valueOf(new Random().nextInt())});
        editor.putString("account_customserver", "s9.rabtcdn.com");
        editor.putString("account_resource", resource);
        editor.putString("account_port", "5222");
        editor.apply();
    }

    public static SHAMChatApplication getApp(Context ctx) {
        return (SHAMChatApplication) ctx.getApplicationContext();
    }

    public static Context getMyApplicationContext() {
        return context;
    }

    public static ZaminConfiguration getConfig() {
        return ((SHAMChatApplication) context.getApplicationContext()).mConfig;
    }

    public final void runOnUiThread(Runnable runnable) {
        this.handler.post(runnable);
    }
}

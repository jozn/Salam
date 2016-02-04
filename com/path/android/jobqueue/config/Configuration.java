package com.path.android.jobqueue.config;

import android.content.Context;
import com.path.android.jobqueue.QueueFactory;
import com.path.android.jobqueue.di.DependencyInjector;
import com.path.android.jobqueue.log.CustomLogger;
import com.path.android.jobqueue.network.NetworkUtil;

public final class Configuration {
    public int consumerKeepAlive;
    public CustomLogger customLogger;
    public DependencyInjector dependencyInjector;
    public String id;
    public boolean inTestMode;
    public int loadFactor;
    public int maxConsumerCount;
    public int minConsumerCount;
    public NetworkUtil networkUtil;
    public QueueFactory queueFactory;

    public static final class Builder {
        public Context appContext;
        public Configuration configuration;

        public Builder(Context context) {
            this.configuration = new Configuration();
            this.appContext = context.getApplicationContext();
        }
    }

    private Configuration() {
        this.id = "default_job_manager";
        this.maxConsumerCount = 5;
        this.minConsumerCount = 0;
        this.consumerKeepAlive = 15;
        this.loadFactor = 3;
        this.inTestMode = false;
    }
}

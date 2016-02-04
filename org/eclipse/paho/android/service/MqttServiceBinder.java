package org.eclipse.paho.android.service;

import android.os.Binder;

class MqttServiceBinder extends Binder {
    private String activityToken;
    private MqttService mqttService;

    MqttServiceBinder(MqttService mqttService) {
        this.mqttService = mqttService;
    }

    public MqttService getService() {
        return this.mqttService;
    }

    void setActivityToken(String activityToken) {
        this.activityToken = activityToken;
    }

    public String getActivityToken() {
        return this.activityToken;
    }
}

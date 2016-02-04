package com.rokhgroup.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public final class RokhPref {
    int Private_Mode;
    Context _context;
    public Editor editor;
    public SharedPreferences pref;

    public RokhPref(Context context) {
        this.Private_Mode = 0;
        this._context = context;
        this.pref = this._context.getSharedPreferences("RokhgroupPref", this.Private_Mode);
        this.editor = this.pref.edit();
    }

    public final String getUSERID() {
        return this.pref.getString("userId", null);
    }

    public final String getUSERTOKEN() {
        return this.pref.getString("token", null);
    }

    public final String getUsername() {
        return this.pref.getString("username", null);
    }

    public final String getClientHandle() {
        return this.pref.getString(MqttServiceConstants.CLIENT_HANDLE, null);
    }

    public final void setFirstRun(boolean firstRun) {
        this.editor.putBoolean("isFirstRun", firstRun);
        this.editor.commit();
    }
}

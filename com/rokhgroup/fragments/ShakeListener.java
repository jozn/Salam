package com.rokhgroup.fragments;

import android.content.Context;
import android.hardware.SensorListener;
import android.hardware.SensorManager;

public final class ShakeListener implements SensorListener {
    private Context mContext;
    private long mLastForce;
    private long mLastShake;
    private long mLastTime;
    private float mLastX;
    private float mLastY;
    private float mLastZ;
    private SensorManager mSensorMgr;
    private int mShakeCount;
    OnShakeListener mShakeListener;

    public interface OnShakeListener {
        void onShake();
    }

    public ShakeListener(Context context) {
        this.mLastX = -1.0f;
        this.mLastY = -1.0f;
        this.mLastZ = -1.0f;
        this.mShakeCount = 0;
        this.mContext = context;
        this.mSensorMgr = (SensorManager) this.mContext.getSystemService("sensor");
        if (this.mSensorMgr == null) {
            throw new UnsupportedOperationException("Sensors not supported");
        } else if (!this.mSensorMgr.registerListener(this, 2, 1)) {
            this.mSensorMgr.unregisterListener(this, 2);
            throw new UnsupportedOperationException("Accelerometer not supported");
        }
    }

    public final void onAccuracyChanged(int sensor, int accuracy) {
    }

    public final void onSensorChanged(int sensor, float[] values) {
        if (sensor == 2) {
            long now = System.currentTimeMillis();
            if (now - this.mLastForce > 350) {
                this.mShakeCount = 0;
            }
            if (now - this.mLastTime > 200) {
                if ((Math.abs(((((values[0] + values[1]) + values[2]) - this.mLastX) - this.mLastY) - this.mLastZ) / ((float) (now - this.mLastTime))) * 10000.0f > 350.0f) {
                    int i = this.mShakeCount + 1;
                    this.mShakeCount = i;
                    if (i >= 3 && now - this.mLastShake > 600) {
                        this.mLastShake = now;
                        this.mShakeCount = 0;
                        if (this.mShakeListener != null) {
                            this.mShakeListener.onShake();
                        }
                    }
                    this.mLastForce = now;
                }
                this.mLastTime = now;
                this.mLastX = values[0];
                this.mLastY = values[1];
                this.mLastZ = values[2];
            }
        }
    }
}

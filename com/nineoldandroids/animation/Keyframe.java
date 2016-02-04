package com.nineoldandroids.animation;

import android.view.animation.Interpolator;

public abstract class Keyframe implements Cloneable {
    float mFraction;
    boolean mHasValue;
    Interpolator mInterpolator;
    Class mValueType;

    static class FloatKeyframe extends Keyframe {
        float mValue;

        FloatKeyframe(float fraction, float value) {
            this.mFraction = fraction;
            this.mValue = value;
            this.mValueType = Float.TYPE;
            this.mHasValue = true;
        }

        FloatKeyframe() {
            this.mFraction = 0.0f;
            this.mValueType = Float.TYPE;
        }

        public final Object getValue() {
            return Float.valueOf(this.mValue);
        }

        private FloatKeyframe clone() {
            FloatKeyframe kfClone = new FloatKeyframe(this.mFraction, this.mValue);
            kfClone.mInterpolator = this.mInterpolator;
            return kfClone;
        }
    }

    public abstract Keyframe clone();

    public abstract Object getValue();

    public Keyframe() {
        this.mInterpolator = null;
        this.mHasValue = false;
    }

    public static Keyframe ofFloat(float fraction, float value) {
        return new FloatKeyframe(fraction, value);
    }
}

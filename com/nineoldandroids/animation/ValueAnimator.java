package com.nineoldandroids.animation;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.appcompat.BuildConfig;
import android.util.AndroidRuntimeException;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class ValueAnimator extends Animator {
    private static ThreadLocal<AnimationHandler> sAnimationHandler;
    private static final ThreadLocal<ArrayList<ValueAnimator>> sAnimations;
    private static final Interpolator sDefaultInterpolator;
    private static final ThreadLocal<ArrayList<ValueAnimator>> sDelayedAnims;
    private static final ThreadLocal<ArrayList<ValueAnimator>> sEndingAnims;
    private static final TypeEvaluator sFloatEvaluator;
    private static long sFrameDelay;
    private static final TypeEvaluator sIntEvaluator;
    private static final ThreadLocal<ArrayList<ValueAnimator>> sPendingAnimations;
    private static final ThreadLocal<ArrayList<ValueAnimator>> sReadyAnims;
    public float mCurrentFraction;
    private int mCurrentIteration;
    private long mDelayStartTime;
    private long mDuration;
    boolean mInitialized;
    private Interpolator mInterpolator;
    private boolean mPlayingBackwards;
    int mPlayingState;
    public int mRepeatCount;
    public int mRepeatMode;
    private boolean mRunning;
    long mSeekTime;
    public long mStartDelay;
    long mStartTime;
    private boolean mStarted;
    private boolean mStartedDelay;
    public ArrayList<AnimatorUpdateListener> mUpdateListeners;
    PropertyValuesHolder[] mValues;
    HashMap<String, PropertyValuesHolder> mValuesMap;

    /* renamed from: com.nineoldandroids.animation.ValueAnimator.1 */
    static class C04841 extends ThreadLocal<ArrayList<ValueAnimator>> {
        C04841() {
        }

        protected final /* bridge */ /* synthetic */ Object initialValue() {
            return new ArrayList();
        }
    }

    /* renamed from: com.nineoldandroids.animation.ValueAnimator.2 */
    static class C04852 extends ThreadLocal<ArrayList<ValueAnimator>> {
        C04852() {
        }

        protected final /* bridge */ /* synthetic */ Object initialValue() {
            return new ArrayList();
        }
    }

    /* renamed from: com.nineoldandroids.animation.ValueAnimator.3 */
    static class C04863 extends ThreadLocal<ArrayList<ValueAnimator>> {
        C04863() {
        }

        protected final /* bridge */ /* synthetic */ Object initialValue() {
            return new ArrayList();
        }
    }

    /* renamed from: com.nineoldandroids.animation.ValueAnimator.4 */
    static class C04874 extends ThreadLocal<ArrayList<ValueAnimator>> {
        C04874() {
        }

        protected final /* bridge */ /* synthetic */ Object initialValue() {
            return new ArrayList();
        }
    }

    /* renamed from: com.nineoldandroids.animation.ValueAnimator.5 */
    static class C04885 extends ThreadLocal<ArrayList<ValueAnimator>> {
        C04885() {
        }

        protected final /* bridge */ /* synthetic */ Object initialValue() {
            return new ArrayList();
        }
    }

    private static class AnimationHandler extends Handler {
        private AnimationHandler() {
        }

        public final void handleMessage(Message msg) {
            int i;
            ValueAnimator anim;
            boolean callAgain = true;
            ArrayList<ValueAnimator> animations = (ArrayList) ValueAnimator.sAnimations.get();
            ArrayList<ValueAnimator> delayedAnims = (ArrayList) ValueAnimator.sDelayedAnims.get();
            switch (msg.what) {
                case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                    ArrayList<ValueAnimator> pendingAnimations = (ArrayList) ValueAnimator.sPendingAnimations.get();
                    if (animations.size() > 0 || delayedAnims.size() > 0) {
                        callAgain = false;
                    }
                    while (pendingAnimations.size() > 0) {
                        ArrayList<ValueAnimator> pendingCopy = (ArrayList) pendingAnimations.clone();
                        pendingAnimations.clear();
                        int count = pendingCopy.size();
                        for (i = 0; i < count; i++) {
                            anim = (ValueAnimator) pendingCopy.get(i);
                            if (anim.mStartDelay == 0) {
                                ValueAnimator.access$400(anim);
                            } else {
                                delayedAnims.add(anim);
                            }
                        }
                    }
                    break;
                case Logger.SEVERE /*1*/:
                    break;
                default:
                    return;
            }
            long currentTime = AnimationUtils.currentAnimationTimeMillis();
            ArrayList<ValueAnimator> readyAnims = (ArrayList) ValueAnimator.sReadyAnims.get();
            ArrayList<ValueAnimator> endingAnims = (ArrayList) ValueAnimator.sEndingAnims.get();
            int numDelayedAnims = delayedAnims.size();
            for (i = 0; i < numDelayedAnims; i++) {
                anim = (ValueAnimator) delayedAnims.get(i);
                if (ValueAnimator.access$700(anim, currentTime)) {
                    readyAnims.add(anim);
                }
            }
            int numReadyAnims = readyAnims.size();
            if (numReadyAnims > 0) {
                for (i = 0; i < numReadyAnims; i++) {
                    anim = (ValueAnimator) readyAnims.get(i);
                    ValueAnimator.access$400(anim);
                    anim.mRunning = true;
                    delayedAnims.remove(anim);
                }
                readyAnims.clear();
            }
            int numAnims = animations.size();
            i = 0;
            while (i < numAnims) {
                anim = (ValueAnimator) animations.get(i);
                if (anim.animationFrame(currentTime)) {
                    endingAnims.add(anim);
                }
                if (animations.size() == numAnims) {
                    i++;
                } else {
                    numAnims--;
                    endingAnims.remove(anim);
                }
            }
            if (endingAnims.size() > 0) {
                for (i = 0; i < endingAnims.size(); i++) {
                    ((ValueAnimator) endingAnims.get(i)).endAnimation();
                }
                endingAnims.clear();
            }
            if (!callAgain) {
                return;
            }
            if (!animations.isEmpty() || !delayedAnims.isEmpty()) {
                sendEmptyMessageDelayed(1, Math.max(0, ValueAnimator.sFrameDelay - (AnimationUtils.currentAnimationTimeMillis() - currentTime)));
            }
        }
    }

    public interface AnimatorUpdateListener {
        void onAnimationUpdate(ValueAnimator valueAnimator);
    }

    static /* synthetic */ void access$400(ValueAnimator x0) {
        x0.initAnimation();
        ((ArrayList) sAnimations.get()).add(x0);
        if (x0.mStartDelay > 0 && x0.mListeners != null) {
            ArrayList arrayList = (ArrayList) x0.mListeners.clone();
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                ((AnimatorListener) arrayList.get(i)).onAnimationStart(x0);
            }
        }
    }

    static /* synthetic */ boolean access$700(ValueAnimator x0, long x1) {
        if (x0.mStartedDelay) {
            long j = x1 - x0.mDelayStartTime;
            if (j > x0.mStartDelay) {
                x0.mStartTime = x1 - (j - x0.mStartDelay);
                x0.mPlayingState = 1;
                return true;
            }
        }
        x0.mStartedDelay = true;
        x0.mDelayStartTime = x1;
        return false;
    }

    static {
        sAnimationHandler = new ThreadLocal();
        sAnimations = new C04841();
        sPendingAnimations = new C04852();
        sDelayedAnims = new C04863();
        sEndingAnims = new C04874();
        sReadyAnims = new C04885();
        sDefaultInterpolator = new AccelerateDecelerateInterpolator();
        sIntEvaluator = new IntEvaluator();
        sFloatEvaluator = new FloatEvaluator();
        sFrameDelay = 10;
    }

    public ValueAnimator() {
        this.mSeekTime = -1;
        this.mPlayingBackwards = false;
        this.mCurrentIteration = 0;
        this.mCurrentFraction = 0.0f;
        this.mStartedDelay = false;
        this.mPlayingState = 0;
        this.mRunning = false;
        this.mStarted = false;
        this.mInitialized = false;
        this.mDuration = 300;
        this.mStartDelay = 0;
        this.mRepeatCount = 0;
        this.mRepeatMode = 1;
        this.mInterpolator = sDefaultInterpolator;
        this.mUpdateListeners = null;
    }

    public static ValueAnimator ofFloat(float... values) {
        ValueAnimator anim = new ValueAnimator();
        if (values.length != 0) {
            if (anim.mValues == null || anim.mValues.length == 0) {
                PropertyValuesHolder[] propertyValuesHolderArr = new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(BuildConfig.VERSION_NAME, values)};
                anim.mValues = propertyValuesHolderArr;
                anim.mValuesMap = new HashMap(1);
                for (int i = 0; i <= 0; i++) {
                    PropertyValuesHolder propertyValuesHolder = propertyValuesHolderArr[0];
                    anim.mValuesMap.put(propertyValuesHolder.mPropertyName, propertyValuesHolder);
                }
                anim.mInitialized = false;
            } else {
                anim.mValues[0].setFloatValues(values);
            }
            anim.mInitialized = false;
        }
        return anim;
    }

    private void initAnimation() {
        if (!this.mInitialized) {
            for (PropertyValuesHolder propertyValuesHolder : this.mValues) {
                if (propertyValuesHolder.mEvaluator == null) {
                    TypeEvaluator typeEvaluator = propertyValuesHolder.mValueType == Integer.class ? PropertyValuesHolder.sIntEvaluator : propertyValuesHolder.mValueType == Float.class ? PropertyValuesHolder.sFloatEvaluator : null;
                    propertyValuesHolder.mEvaluator = typeEvaluator;
                }
                if (propertyValuesHolder.mEvaluator != null) {
                    propertyValuesHolder.mKeyframeSet.mEvaluator = propertyValuesHolder.mEvaluator;
                }
            }
            this.mInitialized = true;
        }
    }

    public final ValueAnimator setDuration(long duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("Animators cannot have negative duration: " + duration);
        }
        this.mDuration = duration;
        return this;
    }

    private void setCurrentPlayTime(long playTime) {
        initAnimation();
        long currentTime = AnimationUtils.currentAnimationTimeMillis();
        if (this.mPlayingState != 1) {
            this.mSeekTime = playTime;
            this.mPlayingState = 2;
        }
        this.mStartTime = currentTime - playTime;
        animationFrame(currentTime);
    }

    public final void addUpdateListener(AnimatorUpdateListener listener) {
        if (this.mUpdateListeners == null) {
            this.mUpdateListeners = new ArrayList();
        }
        this.mUpdateListeners.add(listener);
    }

    public final void setInterpolator(Interpolator value) {
        if (value != null) {
            this.mInterpolator = value;
        } else {
            this.mInterpolator = new LinearInterpolator();
        }
    }

    public final void start() {
        if (Looper.myLooper() == null) {
            throw new AndroidRuntimeException("Animators may only be run on Looper threads");
        }
        this.mPlayingBackwards = false;
        this.mCurrentIteration = 0;
        this.mPlayingState = 0;
        this.mStarted = true;
        this.mStartedDelay = false;
        ((ArrayList) sPendingAnimations.get()).add(this);
        if (this.mStartDelay == 0) {
            long currentAnimationTimeMillis = (!this.mInitialized || this.mPlayingState == 0) ? 0 : AnimationUtils.currentAnimationTimeMillis() - this.mStartTime;
            setCurrentPlayTime(currentAnimationTimeMillis);
            this.mPlayingState = 0;
            this.mRunning = true;
            if (this.mListeners != null) {
                ArrayList arrayList = (ArrayList) this.mListeners.clone();
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    ((AnimatorListener) arrayList.get(i)).onAnimationStart(this);
                }
            }
        }
        AnimationHandler animationHandler = (AnimationHandler) sAnimationHandler.get();
        if (animationHandler == null) {
            animationHandler = new AnimationHandler();
            sAnimationHandler.set(animationHandler);
        }
        animationHandler.sendEmptyMessage(0);
    }

    public final void cancel() {
        if (this.mPlayingState != 0 || ((ArrayList) sPendingAnimations.get()).contains(this) || ((ArrayList) sDelayedAnims.get()).contains(this)) {
            if (this.mRunning && this.mListeners != null) {
                Iterator i$ = ((ArrayList) this.mListeners.clone()).iterator();
                while (i$.hasNext()) {
                    ((AnimatorListener) i$.next()).onAnimationCancel(this);
                }
            }
            endAnimation();
        }
    }

    private void endAnimation() {
        ((ArrayList) sAnimations.get()).remove(this);
        ((ArrayList) sPendingAnimations.get()).remove(this);
        ((ArrayList) sDelayedAnims.get()).remove(this);
        this.mPlayingState = 0;
        if (this.mRunning && this.mListeners != null) {
            ArrayList<AnimatorListener> tmpListeners = (ArrayList) this.mListeners.clone();
            int numListeners = tmpListeners.size();
            for (int i = 0; i < numListeners; i++) {
                ((AnimatorListener) tmpListeners.get(i)).onAnimationEnd(this);
            }
        }
        this.mRunning = false;
        this.mStarted = false;
    }

    final boolean animationFrame(long currentTime) {
        int i = 0;
        boolean done = false;
        if (this.mPlayingState == 0) {
            this.mPlayingState = 1;
            if (this.mSeekTime < 0) {
                this.mStartTime = currentTime;
            } else {
                this.mStartTime = currentTime - this.mSeekTime;
                this.mSeekTime = -1;
            }
        }
        switch (this.mPlayingState) {
            case Logger.SEVERE /*1*/:
            case Logger.WARNING /*2*/:
                float fraction;
                if (this.mDuration > 0) {
                    fraction = ((float) (currentTime - this.mStartTime)) / ((float) this.mDuration);
                } else {
                    fraction = 1.0f;
                }
                if (fraction >= 1.0f) {
                    if (this.mCurrentIteration < this.mRepeatCount || this.mRepeatCount == -1) {
                        if (this.mListeners != null) {
                            int numListeners = this.mListeners.size();
                            for (int i2 = 0; i2 < numListeners; i2++) {
                                ((AnimatorListener) this.mListeners.get(i2)).onAnimationRepeat(this);
                            }
                        }
                        if (this.mRepeatMode == 2) {
                            this.mPlayingBackwards = !this.mPlayingBackwards;
                        }
                        this.mCurrentIteration += (int) fraction;
                        fraction %= 1.0f;
                        this.mStartTime += this.mDuration;
                    } else {
                        done = true;
                        fraction = Math.min(fraction, 1.0f);
                    }
                }
                if (this.mPlayingBackwards) {
                    fraction = 1.0f - fraction;
                }
                float interpolation = this.mInterpolator.getInterpolation(fraction);
                this.mCurrentFraction = interpolation;
                for (PropertyValuesHolder calculateValue : this.mValues) {
                    calculateValue.calculateValue(interpolation);
                }
                if (this.mUpdateListeners != null) {
                    int size = this.mUpdateListeners.size();
                    while (i < size) {
                        ((AnimatorUpdateListener) this.mUpdateListeners.get(i)).onAnimationUpdate(this);
                        i++;
                    }
                    break;
                }
                break;
        }
        return done;
    }

    private ValueAnimator clone() {
        int i;
        ValueAnimator anim = (ValueAnimator) super.clone();
        if (this.mUpdateListeners != null) {
            ArrayList<AnimatorUpdateListener> oldListeners = this.mUpdateListeners;
            anim.mUpdateListeners = new ArrayList();
            int numListeners = oldListeners.size();
            for (i = 0; i < numListeners; i++) {
                anim.mUpdateListeners.add(oldListeners.get(i));
            }
        }
        anim.mSeekTime = -1;
        anim.mPlayingBackwards = false;
        anim.mCurrentIteration = 0;
        anim.mInitialized = false;
        anim.mPlayingState = 0;
        anim.mStartedDelay = false;
        PropertyValuesHolder[] oldValues = this.mValues;
        if (oldValues != null) {
            int numValues = oldValues.length;
            anim.mValues = new PropertyValuesHolder[numValues];
            anim.mValuesMap = new HashMap(numValues);
            for (i = 0; i < numValues; i++) {
                PropertyValuesHolder newValuesHolder = oldValues[i].clone();
                anim.mValues[i] = newValuesHolder;
                anim.mValuesMap.put(newValuesHolder.mPropertyName, newValuesHolder);
            }
        }
        return anim;
    }

    public final String toString() {
        String returnVal = "ValueAnimator@" + Integer.toHexString(hashCode());
        if (this.mValues != null) {
            for (PropertyValuesHolder propertyValuesHolder : this.mValues) {
                returnVal = returnVal + "\n    " + propertyValuesHolder.toString();
            }
        }
        return returnVal;
    }
}

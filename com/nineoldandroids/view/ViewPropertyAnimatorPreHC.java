package com.nineoldandroids.view;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.appcompat.C0170R;
import android.view.View;
import android.view.animation.Interpolator;
import com.kyleduo.switchbutton.C0473R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nineoldandroids.view.animation.AnimatorProxy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.paho.client.mqttv3.logging.Logger;

final class ViewPropertyAnimatorPreHC extends ViewPropertyAnimator {
    private Runnable mAnimationStarter;
    private AnimatorEventListener mAnimatorEventListener;
    private HashMap<Animator, PropertyBundle> mAnimatorMap;
    private long mDuration;
    private boolean mDurationSet;
    private Interpolator mInterpolator;
    private boolean mInterpolatorSet;
    private AnimatorListener mListener;
    ArrayList<NameValuesHolder> mPendingAnimations;
    private final AnimatorProxy mProxy;
    private long mStartDelay;
    private boolean mStartDelaySet;
    private final WeakReference<View> mView;

    /* renamed from: com.nineoldandroids.view.ViewPropertyAnimatorPreHC.1 */
    class C04901 implements Runnable {
        C04901() {
        }

        public final void run() {
            ViewPropertyAnimatorPreHC.this.startAnimation();
        }
    }

    private class AnimatorEventListener implements AnimatorListener, AnimatorUpdateListener {
        private AnimatorEventListener() {
        }

        public final void onAnimationStart(Animator animation) {
            if (ViewPropertyAnimatorPreHC.this.mListener != null) {
                ViewPropertyAnimatorPreHC.this.mListener.onAnimationStart(animation);
            }
        }

        public final void onAnimationCancel(Animator animation) {
            if (ViewPropertyAnimatorPreHC.this.mListener != null) {
                ViewPropertyAnimatorPreHC.this.mListener.onAnimationCancel(animation);
            }
        }

        public final void onAnimationRepeat(Animator animation) {
            if (ViewPropertyAnimatorPreHC.this.mListener != null) {
                ViewPropertyAnimatorPreHC.this.mListener.onAnimationRepeat(animation);
            }
        }

        public final void onAnimationEnd(Animator animation) {
            if (ViewPropertyAnimatorPreHC.this.mListener != null) {
                ViewPropertyAnimatorPreHC.this.mListener.onAnimationEnd(animation);
            }
            ViewPropertyAnimatorPreHC.this.mAnimatorMap.remove(animation);
            if (ViewPropertyAnimatorPreHC.this.mAnimatorMap.isEmpty()) {
                ViewPropertyAnimatorPreHC.this.mListener = null;
            }
        }

        public final void onAnimationUpdate(ValueAnimator animation) {
            View v;
            float fraction = animation.mCurrentFraction;
            PropertyBundle propertyBundle = (PropertyBundle) ViewPropertyAnimatorPreHC.this.mAnimatorMap.get(animation);
            if ((propertyBundle.mPropertyMask & 511) != 0) {
                v = (View) ViewPropertyAnimatorPreHC.this.mView.get();
                if (v != null) {
                    v.invalidate();
                }
            }
            ArrayList<NameValuesHolder> valueList = propertyBundle.mNameValuesHolder;
            if (valueList != null) {
                int count = valueList.size();
                for (int i = 0; i < count; i++) {
                    NameValuesHolder values = (NameValuesHolder) valueList.get(i);
                    ViewPropertyAnimatorPreHC.access$500(ViewPropertyAnimatorPreHC.this, values.mNameConstant, values.mFromValue + (values.mDeltaValue * fraction));
                }
            }
            v = (View) ViewPropertyAnimatorPreHC.this.mView.get();
            if (v != null) {
                v.invalidate();
            }
        }
    }

    private static class NameValuesHolder {
        float mDeltaValue;
        float mFromValue;
        int mNameConstant;

        NameValuesHolder(int nameConstant, float fromValue, float deltaValue) {
            this.mNameConstant = nameConstant;
            this.mFromValue = fromValue;
            this.mDeltaValue = deltaValue;
        }
    }

    private static class PropertyBundle {
        ArrayList<NameValuesHolder> mNameValuesHolder;
        int mPropertyMask;

        PropertyBundle(int propertyMask, ArrayList<NameValuesHolder> nameValuesHolder) {
            this.mPropertyMask = propertyMask;
            this.mNameValuesHolder = nameValuesHolder;
        }
    }

    static /* synthetic */ void access$500(ViewPropertyAnimatorPreHC x0, int x1, float x2) {
        AnimatorProxy animatorProxy;
        AnimatorProxy animatorProxy2;
        View view;
        switch (x1) {
            case Logger.SEVERE /*1*/:
                x0.mProxy.setTranslationX(x2);
            case Logger.WARNING /*2*/:
                x0.mProxy.setTranslationY(x2);
            case Logger.CONFIG /*4*/:
                animatorProxy = x0.mProxy;
                if (animatorProxy.mScaleX != x2) {
                    animatorProxy.prepareForUpdate();
                    animatorProxy.mScaleX = x2;
                    animatorProxy.invalidateAfterUpdate();
                }
            case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                animatorProxy = x0.mProxy;
                if (animatorProxy.mScaleY != x2) {
                    animatorProxy.prepareForUpdate();
                    animatorProxy.mScaleY = x2;
                    animatorProxy.invalidateAfterUpdate();
                }
            case C0473R.styleable.SwitchButton_measureFactor /*16*/:
                animatorProxy = x0.mProxy;
                if (animatorProxy.mRotationZ != x2) {
                    animatorProxy.prepareForUpdate();
                    animatorProxy.mRotationZ = x2;
                    animatorProxy.invalidateAfterUpdate();
                }
            case C0170R.styleable.Theme_actionModeCutDrawable /*32*/:
                animatorProxy = x0.mProxy;
                if (animatorProxy.mRotationX != x2) {
                    animatorProxy.prepareForUpdate();
                    animatorProxy.mRotationX = x2;
                    animatorProxy.invalidateAfterUpdate();
                }
            case C0170R.styleable.Theme_imageButtonStyle /*64*/:
                animatorProxy = x0.mProxy;
                if (animatorProxy.mRotationY != x2) {
                    animatorProxy.prepareForUpdate();
                    animatorProxy.mRotationY = x2;
                    animatorProxy.invalidateAfterUpdate();
                }
            case AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS /*128*/:
                animatorProxy2 = x0.mProxy;
                view = (View) animatorProxy2.mView.get();
                if (view != null) {
                    animatorProxy2.setTranslationX(x2 - ((float) view.getLeft()));
                }
            case AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY /*256*/:
                animatorProxy2 = x0.mProxy;
                view = (View) animatorProxy2.mView.get();
                if (view != null) {
                    animatorProxy2.setTranslationY(x2 - ((float) view.getTop()));
                }
            case AccessibilityNodeInfoCompat.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY /*512*/:
                x0.mProxy.setAlpha(x2);
            default:
        }
    }

    ViewPropertyAnimatorPreHC(View view) {
        this.mDurationSet = false;
        this.mStartDelay = 0;
        this.mStartDelaySet = false;
        this.mInterpolatorSet = false;
        this.mListener = null;
        this.mAnimatorEventListener = new AnimatorEventListener();
        this.mPendingAnimations = new ArrayList();
        this.mAnimationStarter = new C04901();
        this.mAnimatorMap = new HashMap();
        this.mView = new WeakReference(view);
        this.mProxy = AnimatorProxy.wrap(view);
    }

    public final ViewPropertyAnimator setDuration$601ab2d9() {
        if (300 < 0) {
            throw new IllegalArgumentException("Animators cannot have negative duration: 300");
        }
        this.mDurationSet = true;
        this.mDuration = 300;
        return this;
    }

    public final ViewPropertyAnimator setStartDelay(long startDelay) {
        if (startDelay < 0) {
            throw new IllegalArgumentException("Animators cannot have negative duration: " + startDelay);
        }
        this.mStartDelaySet = true;
        this.mStartDelay = startDelay;
        return this;
    }

    public final ViewPropertyAnimator setInterpolator(Interpolator interpolator) {
        this.mInterpolatorSet = true;
        this.mInterpolator = interpolator;
        return this;
    }

    public final void start() {
        startAnimation();
    }

    public final ViewPropertyAnimator translationY(float value) {
        animateProperty(2, value);
        return this;
    }

    public final ViewPropertyAnimator alpha(float value) {
        animateProperty(AccessibilityNodeInfoCompat.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY, value);
        return this;
    }

    private void startAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f);
        ArrayList<NameValuesHolder> nameValueList = (ArrayList) this.mPendingAnimations.clone();
        this.mPendingAnimations.clear();
        int propertyMask = 0;
        for (int i = 0; i < nameValueList.size(); i++) {
            propertyMask |= ((NameValuesHolder) nameValueList.get(i)).mNameConstant;
        }
        this.mAnimatorMap.put(animator, new PropertyBundle(propertyMask, nameValueList));
        animator.addUpdateListener(this.mAnimatorEventListener);
        animator.addListener(this.mAnimatorEventListener);
        if (this.mStartDelaySet) {
            animator.mStartDelay = this.mStartDelay;
        }
        if (this.mDurationSet) {
            animator.setDuration(this.mDuration);
        }
        if (this.mInterpolatorSet) {
            animator.setInterpolator(this.mInterpolator);
        }
        animator.start();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void animateProperty(int r11, float r12) {
        /*
        r10 = this;
        r6 = 0;
        switch(r11) {
            case 1: goto L_0x0083;
            case 2: goto L_0x0089;
            case 4: goto L_0x00a1;
            case 8: goto L_0x00a7;
            case 16: goto L_0x008f;
            case 32: goto L_0x0095;
            case 64: goto L_0x009b;
            case 128: goto L_0x00ad;
            case 256: goto L_0x00c4;
            case 512: goto L_0x00db;
            default: goto L_0x0004;
        };
    L_0x0004:
        r1 = 0;
    L_0x0005:
        r0 = r12 - r1;
        r2 = r10.mAnimatorMap;
        r2 = r2.size();
        if (r2 <= 0) goto L_0x0064;
    L_0x000f:
        r5 = 0;
        r2 = r10.mAnimatorMap;
        r2 = r2.keySet();
        r8 = r2.iterator();
    L_0x001a:
        r2 = r8.hasNext();
        if (r2 == 0) goto L_0x00e9;
    L_0x0020:
        r2 = r8.next();
        r2 = (com.nineoldandroids.animation.Animator) r2;
        r3 = r10.mAnimatorMap;
        r3 = r3.get(r2);
        r3 = (com.nineoldandroids.view.ViewPropertyAnimatorPreHC.PropertyBundle) r3;
        r4 = r3.mPropertyMask;
        r4 = r4 & r11;
        if (r4 == 0) goto L_0x00e6;
    L_0x0033:
        r4 = r3.mNameValuesHolder;
        if (r4 == 0) goto L_0x00e6;
    L_0x0037:
        r4 = r3.mNameValuesHolder;
        r9 = r4.size();
        r7 = r6;
    L_0x003e:
        if (r7 >= r9) goto L_0x00e6;
    L_0x0040:
        r4 = r3.mNameValuesHolder;
        r4 = r4.get(r7);
        r4 = (com.nineoldandroids.view.ViewPropertyAnimatorPreHC.NameValuesHolder) r4;
        r4 = r4.mNameConstant;
        if (r4 != r11) goto L_0x00e1;
    L_0x004c:
        r4 = r3.mNameValuesHolder;
        r4.remove(r7);
        r4 = r3.mPropertyMask;
        r7 = r11 ^ -1;
        r4 = r4 & r7;
        r3.mPropertyMask = r4;
        r4 = 1;
    L_0x0059:
        if (r4 == 0) goto L_0x001a;
    L_0x005b:
        r3 = r3.mPropertyMask;
        if (r3 != 0) goto L_0x001a;
    L_0x005f:
        if (r2 == 0) goto L_0x0064;
    L_0x0061:
        r2.cancel();
    L_0x0064:
        r2 = new com.nineoldandroids.view.ViewPropertyAnimatorPreHC$NameValuesHolder;
        r2.<init>(r11, r1, r0);
        r3 = r10.mPendingAnimations;
        r3.add(r2);
        r2 = r10.mView;
        r2 = r2.get();
        r2 = (android.view.View) r2;
        if (r2 == 0) goto L_0x0082;
    L_0x0078:
        r3 = r10.mAnimationStarter;
        r2.removeCallbacks(r3);
        r3 = r10.mAnimationStarter;
        r2.post(r3);
    L_0x0082:
        return;
    L_0x0083:
        r2 = r10.mProxy;
        r1 = r2.mTranslationX;
        goto L_0x0005;
    L_0x0089:
        r2 = r10.mProxy;
        r1 = r2.mTranslationY;
        goto L_0x0005;
    L_0x008f:
        r2 = r10.mProxy;
        r1 = r2.mRotationZ;
        goto L_0x0005;
    L_0x0095:
        r2 = r10.mProxy;
        r1 = r2.mRotationX;
        goto L_0x0005;
    L_0x009b:
        r2 = r10.mProxy;
        r1 = r2.mRotationY;
        goto L_0x0005;
    L_0x00a1:
        r2 = r10.mProxy;
        r1 = r2.mScaleX;
        goto L_0x0005;
    L_0x00a7:
        r2 = r10.mProxy;
        r1 = r2.mScaleY;
        goto L_0x0005;
    L_0x00ad:
        r3 = r10.mProxy;
        r2 = r3.mView;
        r2 = r2.get();
        r2 = (android.view.View) r2;
        if (r2 == 0) goto L_0x0004;
    L_0x00b9:
        r2 = r2.getLeft();
        r2 = (float) r2;
        r3 = r3.mTranslationX;
        r1 = r2 + r3;
        goto L_0x0005;
    L_0x00c4:
        r3 = r10.mProxy;
        r2 = r3.mView;
        r2 = r2.get();
        r2 = (android.view.View) r2;
        if (r2 == 0) goto L_0x0004;
    L_0x00d0:
        r2 = r2.getTop();
        r2 = (float) r2;
        r3 = r3.mTranslationY;
        r1 = r2 + r3;
        goto L_0x0005;
    L_0x00db:
        r2 = r10.mProxy;
        r1 = r2.mAlpha;
        goto L_0x0005;
    L_0x00e1:
        r4 = r7 + 1;
        r7 = r4;
        goto L_0x003e;
    L_0x00e6:
        r4 = r6;
        goto L_0x0059;
    L_0x00e9:
        r2 = r5;
        goto L_0x005f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nineoldandroids.view.ViewPropertyAnimatorPreHC.animateProperty(int, float):void");
    }
}

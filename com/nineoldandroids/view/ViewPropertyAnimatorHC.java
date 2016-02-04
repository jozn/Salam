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
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.paho.client.mqttv3.logging.Logger;

final class ViewPropertyAnimatorHC extends ViewPropertyAnimator {
    private Runnable mAnimationStarter;
    private AnimatorEventListener mAnimatorEventListener;
    private HashMap<Animator, PropertyBundle> mAnimatorMap;
    private long mDuration;
    private boolean mDurationSet;
    private Interpolator mInterpolator;
    private boolean mInterpolatorSet;
    private AnimatorListener mListener;
    ArrayList<NameValuesHolder> mPendingAnimations;
    private long mStartDelay;
    private boolean mStartDelaySet;
    private final WeakReference<View> mView;

    /* renamed from: com.nineoldandroids.view.ViewPropertyAnimatorHC.1 */
    class C04891 implements Runnable {
        C04891() {
        }

        public final void run() {
            ViewPropertyAnimatorHC.this.startAnimation();
        }
    }

    private class AnimatorEventListener implements AnimatorListener, AnimatorUpdateListener {
        private AnimatorEventListener() {
        }

        public final void onAnimationStart(Animator animation) {
            if (ViewPropertyAnimatorHC.this.mListener != null) {
                ViewPropertyAnimatorHC.this.mListener.onAnimationStart(animation);
            }
        }

        public final void onAnimationCancel(Animator animation) {
            if (ViewPropertyAnimatorHC.this.mListener != null) {
                ViewPropertyAnimatorHC.this.mListener.onAnimationCancel(animation);
            }
        }

        public final void onAnimationRepeat(Animator animation) {
            if (ViewPropertyAnimatorHC.this.mListener != null) {
                ViewPropertyAnimatorHC.this.mListener.onAnimationRepeat(animation);
            }
        }

        public final void onAnimationEnd(Animator animation) {
            if (ViewPropertyAnimatorHC.this.mListener != null) {
                ViewPropertyAnimatorHC.this.mListener.onAnimationEnd(animation);
            }
            ViewPropertyAnimatorHC.this.mAnimatorMap.remove(animation);
            if (ViewPropertyAnimatorHC.this.mAnimatorMap.isEmpty()) {
                ViewPropertyAnimatorHC.this.mListener = null;
            }
        }

        public final void onAnimationUpdate(ValueAnimator animation) {
            View v;
            float fraction = animation.mCurrentFraction;
            PropertyBundle propertyBundle = (PropertyBundle) ViewPropertyAnimatorHC.this.mAnimatorMap.get(animation);
            if ((propertyBundle.mPropertyMask & 511) != 0) {
                v = (View) ViewPropertyAnimatorHC.this.mView.get();
                if (v != null) {
                    v.invalidate();
                }
            }
            ArrayList<NameValuesHolder> valueList = propertyBundle.mNameValuesHolder;
            if (valueList != null) {
                int count = valueList.size();
                for (int i = 0; i < count; i++) {
                    NameValuesHolder values = (NameValuesHolder) valueList.get(i);
                    ViewPropertyAnimatorHC.access$500(ViewPropertyAnimatorHC.this, values.mNameConstant, values.mFromValue + (values.mDeltaValue * fraction));
                }
            }
            v = (View) ViewPropertyAnimatorHC.this.mView.get();
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

    static /* synthetic */ void access$500(ViewPropertyAnimatorHC x0, int x1, float x2) {
        View view = (View) x0.mView.get();
        if (view != null) {
            switch (x1) {
                case Logger.SEVERE /*1*/:
                    view.setTranslationX(x2);
                case Logger.WARNING /*2*/:
                    view.setTranslationY(x2);
                case Logger.CONFIG /*4*/:
                    view.setScaleX(x2);
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    view.setScaleY(x2);
                case C0473R.styleable.SwitchButton_measureFactor /*16*/:
                    view.setRotation(x2);
                case C0170R.styleable.Theme_actionModeCutDrawable /*32*/:
                    view.setRotationX(x2);
                case C0170R.styleable.Theme_imageButtonStyle /*64*/:
                    view.setRotationY(x2);
                case AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS /*128*/:
                    view.setX(x2);
                case AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY /*256*/:
                    view.setY(x2);
                case AccessibilityNodeInfoCompat.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY /*512*/:
                    view.setAlpha(x2);
                default:
            }
        }
    }

    ViewPropertyAnimatorHC(View view) {
        this.mDurationSet = false;
        this.mStartDelay = 0;
        this.mStartDelaySet = false;
        this.mInterpolatorSet = false;
        this.mListener = null;
        this.mAnimatorEventListener = new AnimatorEventListener();
        this.mPendingAnimations = new ArrayList();
        this.mAnimationStarter = new C04891();
        this.mAnimatorMap = new HashMap();
        this.mView = new WeakReference(view);
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

    private void animateProperty(int constantName, float toValue) {
        float fromValue;
        View view = (View) this.mView.get();
        if (view != null) {
            switch (constantName) {
                case Logger.SEVERE /*1*/:
                    fromValue = view.getTranslationX();
                    break;
                case Logger.WARNING /*2*/:
                    fromValue = view.getTranslationY();
                    break;
                case Logger.CONFIG /*4*/:
                    fromValue = view.getScaleX();
                    break;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    fromValue = view.getScaleY();
                    break;
                case C0473R.styleable.SwitchButton_measureFactor /*16*/:
                    fromValue = view.getRotation();
                    break;
                case C0170R.styleable.Theme_actionModeCutDrawable /*32*/:
                    fromValue = view.getRotationX();
                    break;
                case C0170R.styleable.Theme_imageButtonStyle /*64*/:
                    fromValue = view.getRotationY();
                    break;
                case AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS /*128*/:
                    fromValue = view.getX();
                    break;
                case AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY /*256*/:
                    fromValue = view.getY();
                    break;
                case AccessibilityNodeInfoCompat.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY /*512*/:
                    fromValue = view.getAlpha();
                    break;
            }
        }
        fromValue = 0.0f;
        float deltaValue = toValue - fromValue;
        if (this.mAnimatorMap.size() > 0) {
            for (Animator animator : this.mAnimatorMap.keySet()) {
                Object obj;
                PropertyBundle propertyBundle = (PropertyBundle) this.mAnimatorMap.get(animator);
                if (!((propertyBundle.mPropertyMask & constantName) == 0 || propertyBundle.mNameValuesHolder == null)) {
                    int size = propertyBundle.mNameValuesHolder.size();
                    int i = 0;
                    while (i < size) {
                        if (((NameValuesHolder) propertyBundle.mNameValuesHolder.get(i)).mNameConstant == constantName) {
                            propertyBundle.mNameValuesHolder.remove(i);
                            propertyBundle.mPropertyMask &= constantName ^ -1;
                            obj = 1;
                            if (obj == null && propertyBundle.mPropertyMask == 0) {
                                if (animator != null) {
                                    animator.cancel();
                                }
                            }
                        } else {
                            i++;
                        }
                    }
                }
                obj = null;
                if (obj == null) {
                }
            }
            Animator animator2 = null;
            if (animator2 != null) {
                animator2.cancel();
            }
        }
        this.mPendingAnimations.add(new NameValuesHolder(constantName, fromValue, deltaValue));
        view = (View) this.mView.get();
        if (view != null) {
            view.removeCallbacks(this.mAnimationStarter);
            view.post(this.mAnimationStarter);
        }
    }
}

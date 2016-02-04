package com.nineoldandroids.view;

import android.os.Build.VERSION;
import android.view.View;
import android.view.animation.Interpolator;
import java.util.WeakHashMap;

public abstract class ViewPropertyAnimator {
    private static final WeakHashMap<View, ViewPropertyAnimator> ANIMATORS;

    public abstract ViewPropertyAnimator alpha(float f);

    public abstract ViewPropertyAnimator setDuration$601ab2d9();

    public abstract ViewPropertyAnimator setInterpolator(Interpolator interpolator);

    public abstract ViewPropertyAnimator setStartDelay(long j);

    public abstract void start();

    public abstract ViewPropertyAnimator translationY(float f);

    static {
        ANIMATORS = new WeakHashMap(0);
    }

    public static ViewPropertyAnimator animate(View view) {
        ViewPropertyAnimator animator = (ViewPropertyAnimator) ANIMATORS.get(view);
        if (animator == null) {
            int version = Integer.valueOf(VERSION.SDK).intValue();
            if (version >= 14) {
                animator = new ViewPropertyAnimatorICS(view);
            } else if (version >= 11) {
                animator = new ViewPropertyAnimatorHC(view);
            } else {
                animator = new ViewPropertyAnimatorPreHC(view);
            }
            ANIMATORS.put(view, animator);
        }
        return animator;
    }
}

package com.nineoldandroids.view;

import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import java.lang.ref.WeakReference;

final class ViewPropertyAnimatorICS extends ViewPropertyAnimator {
    private final WeakReference<ViewPropertyAnimator> mNative;

    ViewPropertyAnimatorICS(View view) {
        this.mNative = new WeakReference(view.animate());
    }

    public final ViewPropertyAnimator setDuration$601ab2d9() {
        ViewPropertyAnimator n = (ViewPropertyAnimator) this.mNative.get();
        if (n != null) {
            n.setDuration(300);
        }
        return this;
    }

    public final ViewPropertyAnimator setStartDelay(long startDelay) {
        ViewPropertyAnimator n = (ViewPropertyAnimator) this.mNative.get();
        if (n != null) {
            n.setStartDelay(startDelay);
        }
        return this;
    }

    public final ViewPropertyAnimator setInterpolator(Interpolator interpolator) {
        ViewPropertyAnimator n = (ViewPropertyAnimator) this.mNative.get();
        if (n != null) {
            n.setInterpolator(interpolator);
        }
        return this;
    }

    public final void start() {
        ViewPropertyAnimator n = (ViewPropertyAnimator) this.mNative.get();
        if (n != null) {
            n.start();
        }
    }

    public final ViewPropertyAnimator translationY(float value) {
        ViewPropertyAnimator n = (ViewPropertyAnimator) this.mNative.get();
        if (n != null) {
            n.translationY(value);
        }
        return this;
    }

    public final ViewPropertyAnimator alpha(float value) {
        ViewPropertyAnimator n = (ViewPropertyAnimator) this.mNative.get();
        if (n != null) {
            n.alpha(value);
        }
        return this;
    }
}

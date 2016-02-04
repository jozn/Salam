package com.rokhgroup.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v7.appcompat.BuildConfig;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public final class LoadToast {
    boolean mInflated;
    ViewGroup mParentView;
    boolean mShowCalled;
    private String mText;
    boolean mToastCanceled;
    int mTranslationY;
    LoadToastView mView;

    /* renamed from: com.rokhgroup.utils.LoadToast.1 */
    class C06801 implements Runnable {
        C06801() {
        }

        public final void run() {
            ViewHelper.setTranslationX(LoadToast.this.mView, (float) ((LoadToast.this.mParentView.getWidth() - LoadToast.this.mView.getWidth()) / 2));
            ViewHelper.setTranslationY(LoadToast.this.mView, (float) ((-LoadToast.this.mView.getHeight()) + LoadToast.this.mTranslationY));
            LoadToast.this.mInflated = true;
            if (!LoadToast.this.mToastCanceled && LoadToast.this.mShowCalled) {
                LoadToast.this.show();
            }
        }
    }

    /* renamed from: com.rokhgroup.utils.LoadToast.2 */
    class C06812 implements OnGlobalLayoutListener {
        C06812() {
        }

        public final void onGlobalLayout() {
            LoadToast loadToast = LoadToast.this;
            if (loadToast.mParentView.indexOfChild(loadToast.mView) != loadToast.mParentView.getChildCount() - 1) {
                loadToast.mParentView.removeView(loadToast.mView);
                loadToast.mParentView.requestLayout();
                loadToast.mParentView.addView(loadToast.mView, new LayoutParams(-2, -2));
            }
        }
    }

    public LoadToast(Context context) {
        this.mText = BuildConfig.VERSION_NAME;
        this.mTranslationY = 0;
        this.mShowCalled = false;
        this.mToastCanceled = false;
        this.mInflated = false;
        this.mView = new LoadToastView(context);
        this.mParentView = (ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(16908290);
        this.mParentView.addView(this.mView, new LayoutParams(-2, -2));
        ViewHelper.setAlpha$5359e7dd(this.mView);
        this.mParentView.postDelayed(new C06801(), 1);
        this.mParentView.getViewTreeObserver().addOnGlobalLayoutListener(new C06812());
    }

    public final LoadToast setText(String message) {
        this.mText = message;
        LoadToastView loadToastView = this.mView;
        loadToastView.mText = this.mText;
        loadToastView.calculateBounds();
        return this;
    }

    public final LoadToast setProgressColor(int color) {
        this.mView.loaderPaint.setColor(color);
        return this;
    }

    public final LoadToast show() {
        if (this.mInflated) {
            LoadToastView loadToastView = this.mView;
            loadToastView.WIDTH_SCALE = 0.0f;
            if (loadToastView.cmp != null) {
                ValueAnimator valueAnimator = loadToastView.cmp;
                if (valueAnimator.mUpdateListeners != null) {
                    valueAnimator.mUpdateListeners.clear();
                    valueAnimator.mUpdateListeners = null;
                }
            }
            ViewHelper.setTranslationX(this.mView, (float) ((this.mParentView.getWidth() - this.mView.getWidth()) / 2));
            ViewHelper.setAlpha$5359e7dd(this.mView);
            ViewHelper.setTranslationY(this.mView, (float) ((-this.mView.getHeight()) + this.mTranslationY));
            ViewPropertyAnimator.animate(this.mView).alpha(1.0f).translationY((float) (this.mTranslationY + 25)).setInterpolator(new DecelerateInterpolator()).setDuration$601ab2d9().setStartDelay(0).start();
        } else {
            this.mShowCalled = true;
        }
        return this;
    }

    public final void success() {
        if (this.mInflated) {
            LoadToastView loadToastView = this.mView;
            loadToastView.success = true;
            loadToastView.done();
            slideUp();
            return;
        }
        this.mToastCanceled = true;
    }

    public final void error() {
        if (this.mInflated) {
            LoadToastView loadToastView = this.mView;
            loadToastView.success = false;
            loadToastView.done();
            slideUp();
            return;
        }
        this.mToastCanceled = true;
    }

    private void slideUp() {
        ViewPropertyAnimator.animate(this.mView).setStartDelay(1000).alpha(0.0f).translationY((float) ((-this.mView.getHeight()) + this.mTranslationY)).setInterpolator(new AccelerateInterpolator()).setDuration$601ab2d9().start();
    }
}

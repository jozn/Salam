package com.nineoldandroids.view.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public final class AnimatorProxy extends Animation {
    public static final boolean NEEDS_PROXY;
    private static final WeakHashMap<View, AnimatorProxy> PROXIES;
    private final RectF mAfter;
    public float mAlpha;
    private final RectF mBefore;
    private final Camera mCamera;
    private boolean mHasPivot;
    private float mPivotX;
    private float mPivotY;
    public float mRotationX;
    public float mRotationY;
    public float mRotationZ;
    public float mScaleX;
    public float mScaleY;
    private final Matrix mTempMatrix;
    public float mTranslationX;
    public float mTranslationY;
    public final WeakReference<View> mView;

    static {
        NEEDS_PROXY = Integer.valueOf(VERSION.SDK).intValue() < 11;
        PROXIES = new WeakHashMap();
    }

    public static AnimatorProxy wrap(View view) {
        Animation proxy = (AnimatorProxy) PROXIES.get(view);
        if (proxy != null && proxy == view.getAnimation()) {
            return proxy;
        }
        AnimatorProxy proxy2 = new AnimatorProxy(view);
        PROXIES.put(view, proxy2);
        return proxy2;
    }

    private AnimatorProxy(View view) {
        this.mCamera = new Camera();
        this.mAlpha = 1.0f;
        this.mScaleX = 1.0f;
        this.mScaleY = 1.0f;
        this.mBefore = new RectF();
        this.mAfter = new RectF();
        this.mTempMatrix = new Matrix();
        setDuration(0);
        setFillAfter(true);
        view.setAnimation(this);
        this.mView = new WeakReference(view);
    }

    public final void setAlpha(float alpha) {
        if (this.mAlpha != alpha) {
            this.mAlpha = alpha;
            View view = (View) this.mView.get();
            if (view != null) {
                view.invalidate();
            }
        }
    }

    public final void setTranslationX(float translationX) {
        if (this.mTranslationX != translationX) {
            prepareForUpdate();
            this.mTranslationX = translationX;
            invalidateAfterUpdate();
        }
    }

    public final void setTranslationY(float translationY) {
        if (this.mTranslationY != translationY) {
            prepareForUpdate();
            this.mTranslationY = translationY;
            invalidateAfterUpdate();
        }
    }

    public final void prepareForUpdate() {
        View view = (View) this.mView.get();
        if (view != null) {
            computeRect(this.mBefore, view);
        }
    }

    public final void invalidateAfterUpdate() {
        View view = (View) this.mView.get();
        if (view != null && view.getParent() != null) {
            RectF after = this.mAfter;
            computeRect(after, view);
            after.union(this.mBefore);
            ((View) view.getParent()).invalidate((int) Math.floor((double) after.left), (int) Math.floor((double) after.top), (int) Math.ceil((double) after.right), (int) Math.ceil((double) after.bottom));
        }
    }

    private void computeRect(RectF r, View view) {
        r.set(0.0f, 0.0f, (float) view.getWidth(), (float) view.getHeight());
        Matrix m = this.mTempMatrix;
        m.reset();
        transformMatrix(m, view);
        this.mTempMatrix.mapRect(r);
        r.offset((float) view.getLeft(), (float) view.getTop());
        if (r.right < r.left) {
            float f = r.right;
            r.right = r.left;
            r.left = f;
        }
        if (r.bottom < r.top) {
            f = r.top;
            r.top = r.bottom;
            r.bottom = f;
        }
    }

    private void transformMatrix(Matrix m, View view) {
        float w = (float) view.getWidth();
        float h = (float) view.getHeight();
        boolean hasPivot = this.mHasPivot;
        float pX = hasPivot ? this.mPivotX : w / 2.0f;
        float pY = hasPivot ? this.mPivotY : h / 2.0f;
        float rX = this.mRotationX;
        float rY = this.mRotationY;
        float rZ = this.mRotationZ;
        if (!(rX == 0.0f && rY == 0.0f && rZ == 0.0f)) {
            Camera camera = this.mCamera;
            camera.save();
            camera.rotateX(rX);
            camera.rotateY(rY);
            camera.rotateZ(-rZ);
            camera.getMatrix(m);
            camera.restore();
            m.preTranslate(-pX, -pY);
            m.postTranslate(pX, pY);
        }
        float sX = this.mScaleX;
        float sY = this.mScaleY;
        if (!(sX == 1.0f && sY == 1.0f)) {
            m.postScale(sX, sY);
            m.postTranslate((-(pX / w)) * ((sX * w) - w), (-(pY / h)) * ((sY * h) - h));
        }
        m.postTranslate(this.mTranslationX, this.mTranslationY);
    }

    protected final void applyTransformation(float interpolatedTime, Transformation t) {
        View view = (View) this.mView.get();
        if (view != null) {
            t.setAlpha(this.mAlpha);
            transformMatrix(t.getMatrix(), view);
        }
    }
}

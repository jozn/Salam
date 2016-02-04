package com.nineoldandroids.view;

import android.view.View;
import com.nineoldandroids.view.animation.AnimatorProxy;

public final class ViewHelper {
    public static void setAlpha$5359e7dd(View view) {
        if (AnimatorProxy.NEEDS_PROXY) {
            AnimatorProxy.wrap(view).setAlpha(0.0f);
        } else {
            view.setAlpha(0.0f);
        }
    }

    public static void setTranslationX(View view, float translationX) {
        if (AnimatorProxy.NEEDS_PROXY) {
            AnimatorProxy.wrap(view).setTranslationX(translationX);
        } else {
            view.setTranslationX(translationX);
        }
    }

    public static void setTranslationY(View view, float translationY) {
        if (AnimatorProxy.NEEDS_PROXY) {
            AnimatorProxy.wrap(view).setTranslationY(translationY);
        } else {
            view.setTranslationY(translationY);
        }
    }
}

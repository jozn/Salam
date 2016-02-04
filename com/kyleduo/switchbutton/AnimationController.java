package com.kyleduo.switchbutton;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;

final class AnimationController {
    private static int ANI_WHAT;
    private static int DEFAULT_FRAME_DURATION;
    static int DEFAULT_VELOCITY;
    boolean isAnimating;
    int mFrame;
    int mFrom;
    private AnimationHandler mHandler;
    OnAnimateListener mOnAnimateListener;
    int mTo;
    int mVelocity;

    private static class AnimationHandler extends Handler {
        private AnimationHandler() {
        }

        public final void handleMessage(Message msg) {
            if (msg.what == AnimationController.ANI_WHAT && msg.obj != null) {
                ((Runnable) msg.obj).run();
            }
        }
    }

    interface OnAnimateListener {
        boolean continueAnimating();

        void onAnimateComplete();

        void onAnimationStart();

        void onFrameUpdate(int i);
    }

    class RequireNextFrame implements Runnable {
        RequireNextFrame() {
        }

        public final void run() {
            if (AnimationController.this.isAnimating) {
                AnimationController.this.mOnAnimateListener.onFrameUpdate(AnimationController.this.mFrame);
                if (AnimationController.this.mOnAnimateListener.continueAnimating()) {
                    Message obtainMessage = AnimationController.this.mHandler.obtainMessage();
                    obtainMessage.what = AnimationController.ANI_WHAT;
                    obtainMessage.obj = this;
                    AnimationController.this.mHandler.sendMessageDelayed(obtainMessage, (long) AnimationController.DEFAULT_FRAME_DURATION);
                    return;
                }
                AnimationController.this.isAnimating = false;
                AnimationController.this.mOnAnimateListener.onAnimateComplete();
            }
        }
    }

    static {
        ANI_WHAT = AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
        DEFAULT_VELOCITY = 7;
        DEFAULT_FRAME_DURATION = 16;
    }

    private AnimationController() {
        this.isAnimating = false;
        this.mVelocity = DEFAULT_VELOCITY;
        this.mHandler = new AnimationHandler();
    }

    static AnimationController getDefault() {
        return new AnimationController();
    }
}

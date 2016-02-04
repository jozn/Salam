package android.support.v4.view;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public class GestureDetectorCompat {
    private final GestureDetectorCompatImpl mImpl;

    interface GestureDetectorCompatImpl {
        boolean isLongpressEnabled();

        boolean onTouchEvent(MotionEvent motionEvent);

        void setIsLongpressEnabled(boolean z);

        void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener);
    }

    static class GestureDetectorCompatImplBase implements GestureDetectorCompatImpl {
        private static final int DOUBLE_TAP_TIMEOUT;
        private static final int LONGPRESS_TIMEOUT;
        private static final int LONG_PRESS = 2;
        private static final int SHOW_PRESS = 1;
        private static final int TAP = 3;
        private static final int TAP_TIMEOUT;
        private boolean mAlwaysInBiggerTapRegion;
        private boolean mAlwaysInTapRegion;
        private MotionEvent mCurrentDownEvent;
        private boolean mDeferConfirmSingleTap;
        private OnDoubleTapListener mDoubleTapListener;
        private int mDoubleTapSlopSquare;
        private float mDownFocusX;
        private float mDownFocusY;
        private final Handler mHandler;
        private boolean mInLongPress;
        private boolean mIsDoubleTapping;
        private boolean mIsLongpressEnabled;
        private float mLastFocusX;
        private float mLastFocusY;
        private final OnGestureListener mListener;
        private int mMaximumFlingVelocity;
        private int mMinimumFlingVelocity;
        private MotionEvent mPreviousUpEvent;
        private boolean mStillDown;
        private int mTouchSlopSquare;
        private VelocityTracker mVelocityTracker;

        private class GestureHandler extends Handler {
            GestureHandler() {
            }

            GestureHandler(Handler handler) {
                super(handler.getLooper());
            }

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case GestureDetectorCompatImplBase.SHOW_PRESS /*1*/:
                        GestureDetectorCompatImplBase.this.mListener.onShowPress(GestureDetectorCompatImplBase.this.mCurrentDownEvent);
                    case GestureDetectorCompatImplBase.LONG_PRESS /*2*/:
                        GestureDetectorCompatImplBase.this.dispatchLongPress();
                    case GestureDetectorCompatImplBase.TAP /*3*/:
                        if (GestureDetectorCompatImplBase.this.mDoubleTapListener == null) {
                            return;
                        }
                        if (GestureDetectorCompatImplBase.this.mStillDown) {
                            GestureDetectorCompatImplBase.this.mDeferConfirmSingleTap = true;
                        } else {
                            GestureDetectorCompatImplBase.this.mDoubleTapListener.onSingleTapConfirmed(GestureDetectorCompatImplBase.this.mCurrentDownEvent);
                        }
                    default:
                        throw new RuntimeException("Unknown message " + msg);
                }
            }
        }

        static {
            LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
            TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
            DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
        }

        public GestureDetectorCompatImplBase(Context context, OnGestureListener listener, Handler handler) {
            if (handler != null) {
                this.mHandler = new GestureHandler(handler);
            } else {
                this.mHandler = new GestureHandler();
            }
            this.mListener = listener;
            if (listener instanceof OnDoubleTapListener) {
                setOnDoubleTapListener((OnDoubleTapListener) listener);
            }
            init(context);
        }

        private void init(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null");
            } else if (this.mListener == null) {
                throw new IllegalArgumentException("OnGestureListener must not be null");
            } else {
                this.mIsLongpressEnabled = true;
                ViewConfiguration configuration = ViewConfiguration.get(context);
                int touchSlop = configuration.getScaledTouchSlop();
                int doubleTapSlop = configuration.getScaledDoubleTapSlop();
                this.mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
                this.mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
                this.mTouchSlopSquare = touchSlop * touchSlop;
                this.mDoubleTapSlopSquare = doubleTapSlop * doubleTapSlop;
            }
        }

        public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
            this.mDoubleTapListener = onDoubleTapListener;
        }

        public void setIsLongpressEnabled(boolean isLongpressEnabled) {
            this.mIsLongpressEnabled = isLongpressEnabled;
        }

        public boolean isLongpressEnabled() {
            return this.mIsLongpressEnabled;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTouchEvent(android.view.MotionEvent r43) {
            /*
            r42 = this;
            r6 = r43.getAction();
            r0 = r42;
            r0 = r0.mVelocityTracker;
            r35 = r0;
            if (r35 != 0) goto L_0x0016;
        L_0x000c:
            r35 = android.view.VelocityTracker.obtain();
            r0 = r35;
            r1 = r42;
            r1.mVelocityTracker = r0;
        L_0x0016:
            r0 = r42;
            r0 = r0.mVelocityTracker;
            r35 = r0;
            r0 = r35;
            r1 = r43;
            r0.addMovement(r1);
            r0 = r6 & 255;
            r35 = r0;
            r36 = 6;
            r0 = r35;
            r1 = r36;
            if (r0 != r1) goto L_0x0062;
        L_0x002f:
            r21 = 1;
        L_0x0031:
            if (r21 == 0) goto L_0x0065;
        L_0x0033:
            r24 = android.support.v4.view.MotionEventCompat.getActionIndex(r43);
        L_0x0037:
            r25 = 0;
            r26 = 0;
            r7 = android.support.v4.view.MotionEventCompat.getPointerCount(r43);
            r17 = 0;
        L_0x0041:
            r0 = r17;
            if (r0 >= r7) goto L_0x0068;
        L_0x0045:
            r0 = r24;
            r1 = r17;
            if (r0 == r1) goto L_0x005f;
        L_0x004b:
            r0 = r43;
            r1 = r17;
            r35 = android.support.v4.view.MotionEventCompat.getX(r0, r1);
            r25 = r25 + r35;
            r0 = r43;
            r1 = r17;
            r35 = android.support.v4.view.MotionEventCompat.getY(r0, r1);
            r26 = r26 + r35;
        L_0x005f:
            r17 = r17 + 1;
            goto L_0x0041;
        L_0x0062:
            r21 = 0;
            goto L_0x0031;
        L_0x0065:
            r24 = -1;
            goto L_0x0037;
        L_0x0068:
            if (r21 == 0) goto L_0x0080;
        L_0x006a:
            r12 = r7 + -1;
        L_0x006c:
            r0 = (float) r12;
            r35 = r0;
            r13 = r25 / r35;
            r0 = (float) r12;
            r35 = r0;
            r14 = r26 / r35;
            r16 = 0;
            r0 = r6 & 255;
            r35 = r0;
            switch(r35) {
                case 0: goto L_0x012e;
                case 1: goto L_0x037f;
                case 2: goto L_0x0288;
                case 3: goto L_0x04c2;
                case 4: goto L_0x007f;
                case 5: goto L_0x0082;
                case 6: goto L_0x0096;
                default: goto L_0x007f;
            };
        L_0x007f:
            return r16;
        L_0x0080:
            r12 = r7;
            goto L_0x006c;
        L_0x0082:
            r0 = r42;
            r0.mLastFocusX = r13;
            r0 = r42;
            r0.mDownFocusX = r13;
            r0 = r42;
            r0.mLastFocusY = r14;
            r0 = r42;
            r0.mDownFocusY = r14;
            r42.cancelTaps();
            goto L_0x007f;
        L_0x0096:
            r0 = r42;
            r0.mLastFocusX = r13;
            r0 = r42;
            r0.mDownFocusX = r13;
            r0 = r42;
            r0.mLastFocusY = r14;
            r0 = r42;
            r0.mDownFocusY = r14;
            r0 = r42;
            r0 = r0.mVelocityTracker;
            r35 = r0;
            r36 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
            r0 = r42;
            r0 = r0.mMaximumFlingVelocity;
            r37 = r0;
            r0 = r37;
            r0 = (float) r0;
            r37 = r0;
            r35.computeCurrentVelocity(r36, r37);
            r27 = android.support.v4.view.MotionEventCompat.getActionIndex(r43);
            r0 = r43;
            r1 = r27;
            r18 = android.support.v4.view.MotionEventCompat.getPointerId(r0, r1);
            r0 = r42;
            r0 = r0.mVelocityTracker;
            r35 = r0;
            r0 = r35;
            r1 = r18;
            r32 = android.support.v4.view.VelocityTrackerCompat.getXVelocity(r0, r1);
            r0 = r42;
            r0 = r0.mVelocityTracker;
            r35 = r0;
            r0 = r35;
            r1 = r18;
            r34 = android.support.v4.view.VelocityTrackerCompat.getYVelocity(r0, r1);
            r17 = 0;
        L_0x00e6:
            r0 = r17;
            if (r0 >= r7) goto L_0x007f;
        L_0x00ea:
            r0 = r17;
            r1 = r27;
            if (r0 == r1) goto L_0x012b;
        L_0x00f0:
            r0 = r43;
            r1 = r17;
            r19 = android.support.v4.view.MotionEventCompat.getPointerId(r0, r1);
            r0 = r42;
            r0 = r0.mVelocityTracker;
            r35 = r0;
            r0 = r35;
            r1 = r19;
            r35 = android.support.v4.view.VelocityTrackerCompat.getXVelocity(r0, r1);
            r31 = r32 * r35;
            r0 = r42;
            r0 = r0.mVelocityTracker;
            r35 = r0;
            r0 = r35;
            r1 = r19;
            r35 = android.support.v4.view.VelocityTrackerCompat.getYVelocity(r0, r1);
            r33 = r34 * r35;
            r35 = r31 + r33;
            r36 = 0;
            r35 = (r35 > r36 ? 1 : (r35 == r36 ? 0 : -1));
            if (r35 >= 0) goto L_0x012b;
        L_0x0120:
            r0 = r42;
            r0 = r0.mVelocityTracker;
            r35 = r0;
            r35.clear();
            goto L_0x007f;
        L_0x012b:
            r17 = r17 + 1;
            goto L_0x00e6;
        L_0x012e:
            r0 = r42;
            r0 = r0.mDoubleTapListener;
            r35 = r0;
            if (r35 == 0) goto L_0x01a5;
        L_0x0136:
            r0 = r42;
            r0 = r0.mHandler;
            r35 = r0;
            r36 = 3;
            r15 = r35.hasMessages(r36);
            if (r15 == 0) goto L_0x014f;
        L_0x0144:
            r0 = r42;
            r0 = r0.mHandler;
            r35 = r0;
            r36 = 3;
            r35.removeMessages(r36);
        L_0x014f:
            r0 = r42;
            r0 = r0.mCurrentDownEvent;
            r35 = r0;
            if (r35 == 0) goto L_0x026e;
        L_0x0157:
            r0 = r42;
            r0 = r0.mPreviousUpEvent;
            r35 = r0;
            if (r35 == 0) goto L_0x026e;
        L_0x015f:
            if (r15 == 0) goto L_0x026e;
        L_0x0161:
            r0 = r42;
            r0 = r0.mCurrentDownEvent;
            r35 = r0;
            r0 = r42;
            r0 = r0.mPreviousUpEvent;
            r36 = r0;
            r0 = r42;
            r1 = r35;
            r2 = r36;
            r3 = r43;
            r35 = r0.isConsideredDoubleTap(r1, r2, r3);
            if (r35 == 0) goto L_0x026e;
        L_0x017b:
            r35 = 1;
            r0 = r35;
            r1 = r42;
            r1.mIsDoubleTapping = r0;
            r0 = r42;
            r0 = r0.mDoubleTapListener;
            r35 = r0;
            r0 = r42;
            r0 = r0.mCurrentDownEvent;
            r36 = r0;
            r35 = r35.onDoubleTap(r36);
            r35 = r35 | 0;
            r0 = r42;
            r0 = r0.mDoubleTapListener;
            r36 = r0;
            r0 = r36;
            r1 = r43;
            r36 = r0.onDoubleTapEvent(r1);
            r16 = r35 | r36;
        L_0x01a5:
            r0 = r42;
            r0.mLastFocusX = r13;
            r0 = r42;
            r0.mDownFocusX = r13;
            r0 = r42;
            r0.mLastFocusY = r14;
            r0 = r42;
            r0.mDownFocusY = r14;
            r0 = r42;
            r0 = r0.mCurrentDownEvent;
            r35 = r0;
            if (r35 == 0) goto L_0x01c6;
        L_0x01bd:
            r0 = r42;
            r0 = r0.mCurrentDownEvent;
            r35 = r0;
            r35.recycle();
        L_0x01c6:
            r35 = android.view.MotionEvent.obtain(r43);
            r0 = r35;
            r1 = r42;
            r1.mCurrentDownEvent = r0;
            r35 = 1;
            r0 = r35;
            r1 = r42;
            r1.mAlwaysInTapRegion = r0;
            r35 = 1;
            r0 = r35;
            r1 = r42;
            r1.mAlwaysInBiggerTapRegion = r0;
            r35 = 1;
            r0 = r35;
            r1 = r42;
            r1.mStillDown = r0;
            r35 = 0;
            r0 = r35;
            r1 = r42;
            r1.mInLongPress = r0;
            r35 = 0;
            r0 = r35;
            r1 = r42;
            r1.mDeferConfirmSingleTap = r0;
            r0 = r42;
            r0 = r0.mIsLongpressEnabled;
            r35 = r0;
            if (r35 == 0) goto L_0x0238;
        L_0x0200:
            r0 = r42;
            r0 = r0.mHandler;
            r35 = r0;
            r36 = 2;
            r35.removeMessages(r36);
            r0 = r42;
            r0 = r0.mHandler;
            r35 = r0;
            r36 = 2;
            r0 = r42;
            r0 = r0.mCurrentDownEvent;
            r37 = r0;
            r38 = r37.getDownTime();
            r37 = TAP_TIMEOUT;
            r0 = r37;
            r0 = (long) r0;
            r40 = r0;
            r38 = r38 + r40;
            r37 = LONGPRESS_TIMEOUT;
            r0 = r37;
            r0 = (long) r0;
            r40 = r0;
            r38 = r38 + r40;
            r0 = r35;
            r1 = r36;
            r2 = r38;
            r0.sendEmptyMessageAtTime(r1, r2);
        L_0x0238:
            r0 = r42;
            r0 = r0.mHandler;
            r35 = r0;
            r36 = 1;
            r0 = r42;
            r0 = r0.mCurrentDownEvent;
            r37 = r0;
            r38 = r37.getDownTime();
            r37 = TAP_TIMEOUT;
            r0 = r37;
            r0 = (long) r0;
            r40 = r0;
            r38 = r38 + r40;
            r0 = r35;
            r1 = r36;
            r2 = r38;
            r0.sendEmptyMessageAtTime(r1, r2);
            r0 = r42;
            r0 = r0.mListener;
            r35 = r0;
            r0 = r35;
            r1 = r43;
            r35 = r0.onDown(r1);
            r16 = r16 | r35;
            goto L_0x007f;
        L_0x026e:
            r0 = r42;
            r0 = r0.mHandler;
            r35 = r0;
            r36 = 3;
            r37 = DOUBLE_TAP_TIMEOUT;
            r0 = r37;
            r0 = (long) r0;
            r38 = r0;
            r0 = r35;
            r1 = r36;
            r2 = r38;
            r0.sendEmptyMessageDelayed(r1, r2);
            goto L_0x01a5;
        L_0x0288:
            r0 = r42;
            r0 = r0.mInLongPress;
            r35 = r0;
            if (r35 != 0) goto L_0x007f;
        L_0x0290:
            r0 = r42;
            r0 = r0.mLastFocusX;
            r35 = r0;
            r22 = r35 - r13;
            r0 = r42;
            r0 = r0.mLastFocusY;
            r35 = r0;
            r23 = r35 - r14;
            r0 = r42;
            r0 = r0.mIsDoubleTapping;
            r35 = r0;
            if (r35 == 0) goto L_0x02ba;
        L_0x02a8:
            r0 = r42;
            r0 = r0.mDoubleTapListener;
            r35 = r0;
            r0 = r35;
            r1 = r43;
            r35 = r0.onDoubleTapEvent(r1);
            r16 = r35 | 0;
            goto L_0x007f;
        L_0x02ba:
            r0 = r42;
            r0 = r0.mAlwaysInTapRegion;
            r35 = r0;
            if (r35 == 0) goto L_0x0347;
        L_0x02c2:
            r0 = r42;
            r0 = r0.mDownFocusX;
            r35 = r0;
            r35 = r13 - r35;
            r0 = r35;
            r9 = (int) r0;
            r0 = r42;
            r0 = r0.mDownFocusY;
            r35 = r0;
            r35 = r14 - r35;
            r0 = r35;
            r10 = (int) r0;
            r35 = r9 * r9;
            r36 = r10 * r10;
            r11 = r35 + r36;
            r0 = r42;
            r0 = r0.mTouchSlopSquare;
            r35 = r0;
            r0 = r35;
            if (r11 <= r0) goto L_0x0333;
        L_0x02e8:
            r0 = r42;
            r0 = r0.mListener;
            r35 = r0;
            r0 = r42;
            r0 = r0.mCurrentDownEvent;
            r36 = r0;
            r0 = r35;
            r1 = r36;
            r2 = r43;
            r3 = r22;
            r4 = r23;
            r16 = r0.onScroll(r1, r2, r3, r4);
            r0 = r42;
            r0.mLastFocusX = r13;
            r0 = r42;
            r0.mLastFocusY = r14;
            r35 = 0;
            r0 = r35;
            r1 = r42;
            r1.mAlwaysInTapRegion = r0;
            r0 = r42;
            r0 = r0.mHandler;
            r35 = r0;
            r36 = 3;
            r35.removeMessages(r36);
            r0 = r42;
            r0 = r0.mHandler;
            r35 = r0;
            r36 = 1;
            r35.removeMessages(r36);
            r0 = r42;
            r0 = r0.mHandler;
            r35 = r0;
            r36 = 2;
            r35.removeMessages(r36);
        L_0x0333:
            r0 = r42;
            r0 = r0.mTouchSlopSquare;
            r35 = r0;
            r0 = r35;
            if (r11 <= r0) goto L_0x007f;
        L_0x033d:
            r35 = 0;
            r0 = r35;
            r1 = r42;
            r1.mAlwaysInBiggerTapRegion = r0;
            goto L_0x007f;
        L_0x0347:
            r35 = java.lang.Math.abs(r22);
            r36 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r35 = (r35 > r36 ? 1 : (r35 == r36 ? 0 : -1));
            if (r35 >= 0) goto L_0x035b;
        L_0x0351:
            r35 = java.lang.Math.abs(r23);
            r36 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            r35 = (r35 > r36 ? 1 : (r35 == r36 ? 0 : -1));
            if (r35 < 0) goto L_0x007f;
        L_0x035b:
            r0 = r42;
            r0 = r0.mListener;
            r35 = r0;
            r0 = r42;
            r0 = r0.mCurrentDownEvent;
            r36 = r0;
            r0 = r35;
            r1 = r36;
            r2 = r43;
            r3 = r22;
            r4 = r23;
            r16 = r0.onScroll(r1, r2, r3, r4);
            r0 = r42;
            r0.mLastFocusX = r13;
            r0 = r42;
            r0.mLastFocusY = r14;
            goto L_0x007f;
        L_0x037f:
            r35 = 0;
            r0 = r35;
            r1 = r42;
            r1.mStillDown = r0;
            r8 = android.view.MotionEvent.obtain(r43);
            r0 = r42;
            r0 = r0.mIsDoubleTapping;
            r35 = r0;
            if (r35 == 0) goto L_0x03f9;
        L_0x0393:
            r0 = r42;
            r0 = r0.mDoubleTapListener;
            r35 = r0;
            r0 = r35;
            r1 = r43;
            r35 = r0.onDoubleTapEvent(r1);
            r16 = r35 | 0;
        L_0x03a3:
            r0 = r42;
            r0 = r0.mPreviousUpEvent;
            r35 = r0;
            if (r35 == 0) goto L_0x03b4;
        L_0x03ab:
            r0 = r42;
            r0 = r0.mPreviousUpEvent;
            r35 = r0;
            r35.recycle();
        L_0x03b4:
            r0 = r42;
            r0.mPreviousUpEvent = r8;
            r0 = r42;
            r0 = r0.mVelocityTracker;
            r35 = r0;
            if (r35 == 0) goto L_0x03d1;
        L_0x03c0:
            r0 = r42;
            r0 = r0.mVelocityTracker;
            r35 = r0;
            r35.recycle();
            r35 = 0;
            r0 = r35;
            r1 = r42;
            r1.mVelocityTracker = r0;
        L_0x03d1:
            r35 = 0;
            r0 = r35;
            r1 = r42;
            r1.mIsDoubleTapping = r0;
            r35 = 0;
            r0 = r35;
            r1 = r42;
            r1.mDeferConfirmSingleTap = r0;
            r0 = r42;
            r0 = r0.mHandler;
            r35 = r0;
            r36 = 1;
            r35.removeMessages(r36);
            r0 = r42;
            r0 = r0.mHandler;
            r35 = r0;
            r36 = 2;
            r35.removeMessages(r36);
            goto L_0x007f;
        L_0x03f9:
            r0 = r42;
            r0 = r0.mInLongPress;
            r35 = r0;
            if (r35 == 0) goto L_0x0415;
        L_0x0401:
            r0 = r42;
            r0 = r0.mHandler;
            r35 = r0;
            r36 = 3;
            r35.removeMessages(r36);
            r35 = 0;
            r0 = r35;
            r1 = r42;
            r1.mInLongPress = r0;
            goto L_0x03a3;
        L_0x0415:
            r0 = r42;
            r0 = r0.mAlwaysInTapRegion;
            r35 = r0;
            if (r35 == 0) goto L_0x044a;
        L_0x041d:
            r0 = r42;
            r0 = r0.mListener;
            r35 = r0;
            r0 = r35;
            r1 = r43;
            r16 = r0.onSingleTapUp(r1);
            r0 = r42;
            r0 = r0.mDeferConfirmSingleTap;
            r35 = r0;
            if (r35 == 0) goto L_0x03a3;
        L_0x0433:
            r0 = r42;
            r0 = r0.mDoubleTapListener;
            r35 = r0;
            if (r35 == 0) goto L_0x03a3;
        L_0x043b:
            r0 = r42;
            r0 = r0.mDoubleTapListener;
            r35 = r0;
            r0 = r35;
            r1 = r43;
            r0.onSingleTapConfirmed(r1);
            goto L_0x03a3;
        L_0x044a:
            r0 = r42;
            r0 = r0.mVelocityTracker;
            r28 = r0;
            r35 = 0;
            r0 = r43;
            r1 = r35;
            r20 = android.support.v4.view.MotionEventCompat.getPointerId(r0, r1);
            r35 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
            r0 = r42;
            r0 = r0.mMaximumFlingVelocity;
            r36 = r0;
            r0 = r36;
            r0 = (float) r0;
            r36 = r0;
            r0 = r28;
            r1 = r35;
            r2 = r36;
            r0.computeCurrentVelocity(r1, r2);
            r0 = r28;
            r1 = r20;
            r30 = android.support.v4.view.VelocityTrackerCompat.getYVelocity(r0, r1);
            r0 = r28;
            r1 = r20;
            r29 = android.support.v4.view.VelocityTrackerCompat.getXVelocity(r0, r1);
            r35 = java.lang.Math.abs(r30);
            r0 = r42;
            r0 = r0.mMinimumFlingVelocity;
            r36 = r0;
            r0 = r36;
            r0 = (float) r0;
            r36 = r0;
            r35 = (r35 > r36 ? 1 : (r35 == r36 ? 0 : -1));
            if (r35 > 0) goto L_0x04a6;
        L_0x0493:
            r35 = java.lang.Math.abs(r29);
            r0 = r42;
            r0 = r0.mMinimumFlingVelocity;
            r36 = r0;
            r0 = r36;
            r0 = (float) r0;
            r36 = r0;
            r35 = (r35 > r36 ? 1 : (r35 == r36 ? 0 : -1));
            if (r35 <= 0) goto L_0x03a3;
        L_0x04a6:
            r0 = r42;
            r0 = r0.mListener;
            r35 = r0;
            r0 = r42;
            r0 = r0.mCurrentDownEvent;
            r36 = r0;
            r0 = r35;
            r1 = r36;
            r2 = r43;
            r3 = r29;
            r4 = r30;
            r16 = r0.onFling(r1, r2, r3, r4);
            goto L_0x03a3;
        L_0x04c2:
            r42.cancel();
            goto L_0x007f;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v4.view.GestureDetectorCompat.GestureDetectorCompatImplBase.onTouchEvent(android.view.MotionEvent):boolean");
        }

        private void cancel() {
            this.mHandler.removeMessages(SHOW_PRESS);
            this.mHandler.removeMessages(LONG_PRESS);
            this.mHandler.removeMessages(TAP);
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
            this.mIsDoubleTapping = false;
            this.mStillDown = false;
            this.mAlwaysInTapRegion = false;
            this.mAlwaysInBiggerTapRegion = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mInLongPress) {
                this.mInLongPress = false;
            }
        }

        private void cancelTaps() {
            this.mHandler.removeMessages(SHOW_PRESS);
            this.mHandler.removeMessages(LONG_PRESS);
            this.mHandler.removeMessages(TAP);
            this.mIsDoubleTapping = false;
            this.mAlwaysInTapRegion = false;
            this.mAlwaysInBiggerTapRegion = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mInLongPress) {
                this.mInLongPress = false;
            }
        }

        private boolean isConsideredDoubleTap(MotionEvent firstDown, MotionEvent firstUp, MotionEvent secondDown) {
            if (!this.mAlwaysInBiggerTapRegion || secondDown.getEventTime() - firstUp.getEventTime() > ((long) DOUBLE_TAP_TIMEOUT)) {
                return false;
            }
            int deltaX = ((int) firstDown.getX()) - ((int) secondDown.getX());
            int deltaY = ((int) firstDown.getY()) - ((int) secondDown.getY());
            if ((deltaX * deltaX) + (deltaY * deltaY) < this.mDoubleTapSlopSquare) {
                return true;
            }
            return false;
        }

        private void dispatchLongPress() {
            this.mHandler.removeMessages(TAP);
            this.mDeferConfirmSingleTap = false;
            this.mInLongPress = true;
            this.mListener.onLongPress(this.mCurrentDownEvent);
        }
    }

    static class GestureDetectorCompatImplJellybeanMr2 implements GestureDetectorCompatImpl {
        private final GestureDetector mDetector;

        public GestureDetectorCompatImplJellybeanMr2(Context context, OnGestureListener listener, Handler handler) {
            this.mDetector = new GestureDetector(context, listener, handler);
        }

        public boolean isLongpressEnabled() {
            return this.mDetector.isLongpressEnabled();
        }

        public boolean onTouchEvent(MotionEvent ev) {
            return this.mDetector.onTouchEvent(ev);
        }

        public void setIsLongpressEnabled(boolean enabled) {
            this.mDetector.setIsLongpressEnabled(enabled);
        }

        public void setOnDoubleTapListener(OnDoubleTapListener listener) {
            this.mDetector.setOnDoubleTapListener(listener);
        }
    }

    public GestureDetectorCompat(Context context, OnGestureListener listener) {
        this(context, listener, null);
    }

    public GestureDetectorCompat(Context context, OnGestureListener listener, Handler handler) {
        if (VERSION.SDK_INT > 17) {
            this.mImpl = new GestureDetectorCompatImplJellybeanMr2(context, listener, handler);
        } else {
            this.mImpl = new GestureDetectorCompatImplBase(context, listener, handler);
        }
    }

    public boolean isLongpressEnabled() {
        return this.mImpl.isLongpressEnabled();
    }

    public boolean onTouchEvent(MotionEvent event) {
        return this.mImpl.onTouchEvent(event);
    }

    public void setIsLongpressEnabled(boolean enabled) {
        this.mImpl.setIsLongpressEnabled(enabled);
    }

    public void setOnDoubleTapListener(OnDoubleTapListener listener) {
        this.mImpl.setOnDoubleTapListener(listener);
    }
}

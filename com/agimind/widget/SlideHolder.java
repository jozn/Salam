package com.agimind.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import java.util.LinkedList;
import java.util.Queue;

public class SlideHolder extends FrameLayout {
    private boolean mAlwaysOpened;
    private Bitmap mCachedBitmap;
    private Canvas mCachedCanvas;
    private Paint mCachedPaint;
    private AnimationListener mCloseListener;
    private boolean mCloseOnRelease;
    private int mDirection;
    private boolean mDispatchWhenOpened;
    private boolean mEnabled;
    private int mEndOffset;
    private byte mFrame;
    private int mHistoricalX;
    private boolean mInterceptTouch;
    private View mMenuView;
    private int mMode;
    private int mOffset;
    private AnimationListener mOpenListener;
    private int mStartOffset;
    private Queue<Runnable> mWhenReady;

    /* renamed from: com.agimind.widget.SlideHolder.1 */
    class C02141 implements AnimationListener {
        C02141() {
        }

        public final void onAnimationStart(Animation animation) {
        }

        public final void onAnimationRepeat(Animation animation) {
        }

        public final void onAnimationEnd(Animation animation) {
            SlideHolder.access$0(SlideHolder.this);
        }
    }

    /* renamed from: com.agimind.widget.SlideHolder.2 */
    class C02152 implements AnimationListener {
        C02152() {
        }

        public final void onAnimationStart(Animation animation) {
        }

        public final void onAnimationRepeat(Animation animation) {
        }

        public final void onAnimationEnd(Animation animation) {
            SlideHolder.access$1(SlideHolder.this);
        }
    }

    /* renamed from: com.agimind.widget.SlideHolder.3 */
    class C02163 implements Runnable {
        C02163() {
        }

        public final void run() {
            SlideHolder.this.open();
        }
    }

    /* renamed from: com.agimind.widget.SlideHolder.5 */
    class C02175 implements Runnable {
        C02175() {
        }

        public final void run() {
            SlideHolder.this.close();
        }
    }

    /* renamed from: com.agimind.widget.SlideHolder.6 */
    class C02186 implements Runnable {
        C02186() {
        }

        public final void run() {
            SlideHolder.this.closeImmediately();
        }
    }

    /* renamed from: com.agimind.widget.SlideHolder.7 */
    class C02197 implements Runnable {
        C02197() {
        }

        public final void run() {
            SlideHolder.this.mMode = 2;
            SlideHolder.this.mMenuView.setVisibility(0);
        }
    }

    /* renamed from: com.agimind.widget.SlideHolder.8 */
    class C02208 implements Runnable {
        C02208() {
        }

        public final void run() {
            SlideHolder.this.mMode = 0;
            SlideHolder.this.mMenuView.setVisibility(8);
        }
    }

    private class SlideAnimation extends Animation {
        private float mEnd;
        private float mStart;

        public SlideAnimation(float fromX, float toX) {
            this.mStart = fromX;
            this.mEnd = toX;
            setInterpolator(new DecelerateInterpolator());
            setDuration((long) (Math.abs(this.mEnd - this.mStart) / 0.6f));
        }

        protected final void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            SlideHolder.this.mOffset = (int) (((this.mEnd - this.mStart) * interpolatedTime) + this.mStart);
            SlideHolder.this.postInvalidate();
        }
    }

    public SlideHolder(Context context) {
        super(context);
        this.mMode = 0;
        this.mDirection = 1;
        this.mOffset = 0;
        this.mEnabled = true;
        this.mInterceptTouch = true;
        this.mAlwaysOpened = false;
        this.mDispatchWhenOpened = false;
        this.mWhenReady = new LinkedList();
        this.mFrame = (byte) 0;
        this.mHistoricalX = 0;
        this.mCloseOnRelease = false;
        this.mOpenListener = new C02141();
        this.mCloseListener = new C02152();
        initView();
    }

    public SlideHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mMode = 0;
        this.mDirection = 1;
        this.mOffset = 0;
        this.mEnabled = true;
        this.mInterceptTouch = true;
        this.mAlwaysOpened = false;
        this.mDispatchWhenOpened = false;
        this.mWhenReady = new LinkedList();
        this.mFrame = (byte) 0;
        this.mHistoricalX = 0;
        this.mCloseOnRelease = false;
        this.mOpenListener = new C02141();
        this.mCloseListener = new C02152();
        initView();
    }

    public SlideHolder(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mMode = 0;
        this.mDirection = 1;
        this.mOffset = 0;
        this.mEnabled = true;
        this.mInterceptTouch = true;
        this.mAlwaysOpened = false;
        this.mDispatchWhenOpened = false;
        this.mWhenReady = new LinkedList();
        this.mFrame = (byte) 0;
        this.mHistoricalX = 0;
        this.mCloseOnRelease = false;
        this.mOpenListener = new C02141();
        this.mCloseListener = new C02152();
        initView();
    }

    private void initView() {
        this.mCachedPaint = new Paint(7);
    }

    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public final void setDirection$13462e() {
        closeImmediately();
        this.mDirection = -1;
    }

    private boolean isOpened() {
        return this.mAlwaysOpened || this.mMode == 2;
    }

    public final boolean open() {
        if (isOpened() || this.mAlwaysOpened || this.mMode == 1) {
            return false;
        }
        if (isReadyForSlide()) {
            initSlideMode();
            Animation anim = new SlideAnimation((float) this.mOffset, (float) this.mEndOffset);
            anim.setAnimationListener(this.mOpenListener);
            startAnimation(anim);
            invalidate();
            return true;
        }
        this.mWhenReady.add(new C02163());
        return true;
    }

    public final boolean close() {
        if (!isOpened() || this.mAlwaysOpened || this.mMode == 1) {
            return false;
        }
        if (isReadyForSlide()) {
            initSlideMode();
            Animation anim = new SlideAnimation((float) this.mOffset, (float) this.mEndOffset);
            anim.setAnimationListener(this.mCloseListener);
            startAnimation(anim);
            invalidate();
            return true;
        }
        this.mWhenReady.add(new C02175());
        return true;
    }

    public final boolean closeImmediately() {
        if (!isOpened() || this.mAlwaysOpened || this.mMode == 1) {
            return false;
        }
        if (isReadyForSlide()) {
            this.mMenuView.setVisibility(8);
            this.mMode = 0;
            requestLayout();
            return true;
        }
        this.mWhenReady.add(new C02186());
        return true;
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int parentRight = r - l;
        int parentBottom = b - t;
        View menu = getChildAt(0);
        int menuWidth = menu.getMeasuredWidth();
        if (this.mDirection == 1) {
            menu.layout(0, 0, menuWidth + 0, parentBottom);
        } else {
            menu.layout(parentRight - menuWidth, 0, parentRight, parentBottom);
        }
        if (this.mAlwaysOpened) {
            if (this.mDirection == 1) {
                this.mOffset = menuWidth;
            } else {
                this.mOffset = 0;
            }
        } else if (this.mMode == 2) {
            this.mOffset = this.mDirection * menuWidth;
        } else if (this.mMode == 0) {
            this.mOffset = 0;
        }
        View main = getChildAt(1);
        main.layout(this.mOffset + 0, 0, (this.mOffset + 0) + main.getMeasuredWidth(), parentBottom);
        invalidate();
        while (true) {
            Runnable rn = (Runnable) this.mWhenReady.poll();
            if (rn != null) {
                rn.run();
            } else {
                return;
            }
        }
    }

    private boolean isReadyForSlide() {
        return getWidth() > 0 && getHeight() > 0;
    }

    protected void onMeasure(int wSp, int hSp) {
        this.mMenuView = getChildAt(0);
        if (this.mAlwaysOpened) {
            View main = getChildAt(1);
            if (!(this.mMenuView == null || main == null)) {
                measureChild(this.mMenuView, wSp, hSp);
                LayoutParams lp = (LayoutParams) main.getLayoutParams();
                if (this.mDirection == 1) {
                    lp.leftMargin = this.mMenuView.getMeasuredWidth();
                } else {
                    lp.rightMargin = this.mMenuView.getMeasuredWidth();
                }
            }
        }
        super.onMeasure(wSp, hSp);
    }

    protected void dispatchDraw(Canvas canvas) {
        try {
            if (this.mMode == 1) {
                View main = getChildAt(1);
                if (VERSION.SDK_INT < 11) {
                    byte b = (byte) (this.mFrame + 1);
                    this.mFrame = b;
                    if (b % 5 == 0) {
                        this.mCachedCanvas.drawColor(0, Mode.CLEAR);
                        main.draw(this.mCachedCanvas);
                    }
                } else if (main.isDirty()) {
                    this.mCachedCanvas.drawColor(0, Mode.CLEAR);
                    main.draw(this.mCachedCanvas);
                }
                View menu = getChildAt(0);
                int scrollX = menu.getScrollX();
                int scrollY = menu.getScrollY();
                canvas.save();
                if (this.mDirection == 1) {
                    canvas.clipRect(0.0f, 0.0f, (float) this.mOffset, (float) menu.getHeight(), Op.REPLACE);
                } else {
                    int menuWidth = menu.getWidth();
                    int menuLeft = menu.getLeft();
                    canvas.clipRect((menuLeft + menuWidth) + this.mOffset, 0, menuLeft + menuWidth, menu.getHeight());
                }
                canvas.translate((float) menu.getLeft(), (float) menu.getTop());
                canvas.translate((float) (-scrollX), (float) (-scrollY));
                menu.draw(canvas);
                canvas.restore();
                canvas.drawBitmap(this.mCachedBitmap, (float) this.mOffset, 0.0f, this.mCachedPaint);
                return;
            }
            if (!this.mAlwaysOpened && this.mMode == 0) {
                this.mMenuView.setVisibility(8);
            }
            super.dispatchDraw(canvas);
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (((!this.mEnabled || !this.mInterceptTouch) && this.mMode == 0) || this.mAlwaysOpened) {
            return super.dispatchTouchEvent(ev);
        }
        if (this.mMode != 2) {
            onTouchEvent(ev);
            if (this.mMode != 1) {
                super.dispatchTouchEvent(ev);
                return true;
            }
            MotionEvent cancelEvent = MotionEvent.obtain(ev);
            cancelEvent.setAction(3);
            super.dispatchTouchEvent(cancelEvent);
            cancelEvent.recycle();
            return true;
        }
        int action = ev.getAction();
        Rect rect = new Rect();
        View menu = getChildAt(0);
        menu.getHitRect(rect);
        if (rect.contains((int) ev.getX(), (int) ev.getY())) {
            onTouchEvent(ev);
            ev.offsetLocation((float) (-menu.getLeft()), (float) (-menu.getTop()));
            menu.dispatchTouchEvent(ev);
            return true;
        }
        if (action == 1 && this.mCloseOnRelease && !this.mDispatchWhenOpened) {
            close();
            this.mCloseOnRelease = false;
        } else {
            if (action == 0 && !this.mDispatchWhenOpened) {
                this.mCloseOnRelease = true;
            }
            onTouchEvent(ev);
        }
        if (!this.mDispatchWhenOpened) {
            return true;
        }
        super.dispatchTouchEvent(ev);
        return true;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        boolean handled = true;
        if (this.mEnabled) {
            float x = ev.getX();
            if (ev.getAction() == 0) {
                this.mHistoricalX = (int) x;
            } else {
                if (ev.getAction() == 2) {
                    float f = x - ((float) this.mHistoricalX);
                    if ((((float) this.mDirection) * f > 50.0f && this.mMode == 0) || (((float) this.mDirection) * f < -50.0f && this.mMode == 2)) {
                        this.mHistoricalX = (int) x;
                        initSlideMode();
                    } else if (this.mMode == 1) {
                        this.mOffset = (int) (f + ((float) this.mOffset));
                        this.mHistoricalX = (int) x;
                        boolean z = (this.mDirection * this.mEndOffset > 0 && this.mDirection * this.mOffset < this.mDirection * this.mEndOffset && this.mDirection * this.mOffset >= this.mDirection * this.mStartOffset) || (this.mEndOffset == 0 && this.mDirection * this.mOffset > this.mDirection * this.mEndOffset && this.mDirection * this.mOffset <= this.mDirection * this.mStartOffset);
                        if (!z) {
                            finishSlide();
                        }
                    } else {
                        handled = false;
                    }
                }
                if (ev.getAction() == 1) {
                    if (this.mMode == 1) {
                        finishSlide();
                    }
                    this.mCloseOnRelease = false;
                } else if (this.mMode == 1) {
                }
            }
            invalidate();
            return handled;
        }
        handled = false;
        invalidate();
        return handled;
    }

    private void initSlideMode() {
        this.mCloseOnRelease = false;
        View v = getChildAt(1);
        if (this.mMode == 0) {
            this.mStartOffset = 0;
            this.mEndOffset = this.mDirection * getChildAt(0).getWidth();
        } else {
            this.mStartOffset = this.mDirection * getChildAt(0).getWidth();
            this.mEndOffset = 0;
        }
        this.mOffset = this.mStartOffset;
        if (this.mCachedBitmap == null || this.mCachedBitmap.isRecycled() || this.mCachedBitmap.getWidth() != v.getWidth()) {
            this.mCachedBitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Config.ARGB_8888);
            this.mCachedCanvas = new Canvas(this.mCachedBitmap);
        } else {
            this.mCachedCanvas.drawColor(0, Mode.CLEAR);
        }
        v.setVisibility(0);
        this.mCachedCanvas.translate((float) (-v.getScrollX()), (float) (-v.getScrollY()));
        v.draw(this.mCachedCanvas);
        this.mMode = 1;
        this.mMenuView.setVisibility(0);
    }

    static /* synthetic */ void access$0(SlideHolder slideHolder) {
        slideHolder.mOffset = slideHolder.mDirection * slideHolder.mMenuView.getWidth();
        slideHolder.requestLayout();
        slideHolder.post(new C02197());
    }

    static /* synthetic */ void access$1(SlideHolder slideHolder) {
        slideHolder.mOffset = 0;
        slideHolder.requestLayout();
        slideHolder.post(new C02208());
    }

    private void finishSlide() {
        Animation anim;
        if (this.mDirection * this.mEndOffset > 0) {
            if (this.mDirection * this.mOffset > (this.mDirection * this.mEndOffset) / 2) {
                if (this.mDirection * this.mOffset > this.mDirection * this.mEndOffset) {
                    this.mOffset = this.mEndOffset;
                }
                anim = new SlideAnimation((float) this.mOffset, (float) this.mEndOffset);
                anim.setAnimationListener(this.mOpenListener);
                startAnimation(anim);
                return;
            }
            if (this.mDirection * this.mOffset < this.mDirection * this.mStartOffset) {
                this.mOffset = this.mStartOffset;
            }
            anim = new SlideAnimation((float) this.mOffset, (float) this.mStartOffset);
            anim.setAnimationListener(this.mCloseListener);
            startAnimation(anim);
        } else if (this.mDirection * this.mOffset < (this.mDirection * this.mStartOffset) / 2) {
            if (this.mDirection * this.mOffset < this.mDirection * this.mEndOffset) {
                this.mOffset = this.mEndOffset;
            }
            anim = new SlideAnimation((float) this.mOffset, (float) this.mEndOffset);
            anim.setAnimationListener(this.mCloseListener);
            startAnimation(anim);
        } else {
            if (this.mDirection * this.mOffset > this.mDirection * this.mStartOffset) {
                this.mOffset = this.mStartOffset;
            }
            anim = new SlideAnimation((float) this.mOffset, (float) this.mStartOffset);
            anim.setAnimationListener(this.mOpenListener);
            startAnimation(anim);
        }
    }
}

package com.rokhgroup.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.BuildConfig;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

public final class LoadToastView extends View {
    private int BASE_TEXT_SIZE;
    private int IMAGE_WIDTH;
    private int MARQUE_STEP;
    private int MAX_TEXT_WIDTH;
    private int TOAST_HEIGHT;
    float WIDTH_SCALE;
    private Paint backPaint;
    ValueAnimator cmp;
    private Drawable completeicon;
    private AccelerateDecelerateInterpolator easeinterpol;
    private Paint errorPaint;
    private Drawable failedicon;
    private Paint iconBackPaint;
    private Rect iconBounds;
    Paint loaderPaint;
    String mText;
    private Rect mTextBounds;
    private boolean outOfBounds;
    private long prevUpdate;
    private RectF spinnerRect;
    boolean success;
    private Paint successPaint;
    private Paint textPaint;
    private Path toastPath;
    private ValueAnimator va;

    /* renamed from: com.rokhgroup.utils.LoadToastView.1 */
    class C06821 implements AnimatorUpdateListener {
        C06821() {
        }

        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            LoadToastView.this.postInvalidate();
        }
    }

    /* renamed from: com.rokhgroup.utils.LoadToastView.2 */
    class C06832 implements AnimatorUpdateListener {
        C06832() {
        }

        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            LoadToastView.this.WIDTH_SCALE = 2.0f * valueAnimator.mCurrentFraction;
            LoadToastView.this.postInvalidate();
        }
    }

    public LoadToastView(Context context) {
        int color;
        super(context);
        this.mText = BuildConfig.VERSION_NAME;
        this.textPaint = new Paint();
        this.backPaint = new Paint();
        this.iconBackPaint = new Paint();
        this.loaderPaint = new Paint();
        this.successPaint = new Paint();
        this.errorPaint = new Paint();
        this.mTextBounds = new Rect();
        this.spinnerRect = new RectF();
        this.MAX_TEXT_WIDTH = 100;
        this.BASE_TEXT_SIZE = 20;
        this.IMAGE_WIDTH = 40;
        this.TOAST_HEIGHT = 48;
        this.WIDTH_SCALE = 0.0f;
        this.MARQUE_STEP = 1;
        this.prevUpdate = 0;
        this.success = true;
        this.outOfBounds = false;
        this.toastPath = new Path();
        this.easeinterpol = new AccelerateDecelerateInterpolator();
        this.textPaint.setTextSize(15.0f);
        this.textPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.textPaint.setAntiAlias(true);
        this.backPaint.setColor(-1);
        this.backPaint.setAntiAlias(true);
        this.iconBackPaint.setColor(-16776961);
        this.iconBackPaint.setAntiAlias(true);
        this.loaderPaint.setStrokeWidth((float) dpToPx(4));
        this.loaderPaint.setAntiAlias(true);
        Paint paint = this.loaderPaint;
        if (VERSION.SDK_INT >= 21) {
            TypedValue typedValue = new TypedValue();
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(typedValue.data, new int[]{16843829});
            color = obtainStyledAttributes.getColor(0, 0);
            obtainStyledAttributes.recycle();
        } else {
            color = Color.rgb(155, 155, 155);
        }
        paint.setColor(color);
        this.loaderPaint.setStyle(Style.STROKE);
        this.successPaint.setColor(getResources().getColor(2131230770));
        this.errorPaint.setColor(getResources().getColor(2131230769));
        this.successPaint.setAntiAlias(true);
        this.errorPaint.setAntiAlias(true);
        this.MAX_TEXT_WIDTH = dpToPx(this.MAX_TEXT_WIDTH);
        this.BASE_TEXT_SIZE = dpToPx(this.BASE_TEXT_SIZE);
        this.IMAGE_WIDTH = dpToPx(this.IMAGE_WIDTH);
        this.TOAST_HEIGHT = dpToPx(this.TOAST_HEIGHT);
        this.MARQUE_STEP = dpToPx(this.MARQUE_STEP);
        int padding = (this.TOAST_HEIGHT - this.IMAGE_WIDTH) / 2;
        this.iconBounds = new Rect((this.TOAST_HEIGHT + this.MAX_TEXT_WIDTH) - padding, padding, ((this.TOAST_HEIGHT + this.MAX_TEXT_WIDTH) - padding) + this.IMAGE_WIDTH, this.IMAGE_WIDTH + padding);
        this.completeicon = getResources().getDrawable(2130837917);
        this.completeicon.setBounds(this.iconBounds);
        this.failedicon = getResources().getDrawable(2130837902);
        this.failedicon.setBounds(this.iconBounds);
        this.va = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.va.setDuration(6000);
        this.va.addUpdateListener(new C06821());
        this.va.mRepeatMode = -1;
        this.va.mRepeatCount = 9999999;
        this.va.setInterpolator(new LinearInterpolator());
        this.va.start();
        calculateBounds();
    }

    public final void setBackgroundColor(int color) {
        this.backPaint.setColor(color);
        this.iconBackPaint.setColor(color);
    }

    final void done() {
        this.cmp = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.cmp.setDuration(600);
        this.cmp.addUpdateListener(new C06832());
        this.cmp.setInterpolator(new DecelerateInterpolator());
        this.cmp.start();
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(1, (float) dp, getResources().getDisplayMetrics());
    }

    final void calculateBounds() {
        this.outOfBounds = false;
        this.prevUpdate = 0;
        this.textPaint.setTextSize((float) this.BASE_TEXT_SIZE);
        this.textPaint.getTextBounds(this.mText, 0, this.mText.length(), this.mTextBounds);
        if (this.mTextBounds.width() > this.MAX_TEXT_WIDTH) {
            int textSize = this.BASE_TEXT_SIZE;
            while (textSize > dpToPx(13) && this.mTextBounds.width() > this.MAX_TEXT_WIDTH) {
                textSize--;
                this.textPaint.setTextSize((float) textSize);
                this.textPaint.getTextBounds(this.mText, 0, this.mText.length(), this.mTextBounds);
            }
            if (this.mTextBounds.width() > this.MAX_TEXT_WIDTH) {
                this.outOfBounds = true;
            }
        }
    }

    protected final void onDraw(Canvas c) {
        super.onDraw(c);
        float ws = Math.max(1.0f - this.WIDTH_SCALE, 0.0f);
        if (this.mText.length() == 0) {
            ws = 0.0f;
        }
        float translateLoad = (1.0f - ws) * ((float) (this.IMAGE_WIDTH + this.MAX_TEXT_WIDTH));
        float leftMargin = translateLoad / 2.0f;
        this.textPaint.setAlpha((int) (255.0f * Math.max(0.0f, (10.0f * ws) - 9.0f)));
        this.spinnerRect.set(((float) (this.iconBounds.left + dpToPx(4))) - (translateLoad / 2.0f), (float) (this.iconBounds.top + dpToPx(4)), ((float) (this.iconBounds.right - dpToPx(4))) - (translateLoad / 2.0f), (float) (this.iconBounds.bottom - dpToPx(4)));
        int circleOffset = (int) ((((double) (this.TOAST_HEIGHT * 2)) * (Math.sqrt(2.0d) - 1.0d)) / 3.0d);
        int th = this.TOAST_HEIGHT;
        int pd = (this.TOAST_HEIGHT - this.IMAGE_WIDTH) / 2;
        int iconoffset = (int) ((((double) (this.IMAGE_WIDTH * 2)) * (Math.sqrt(2.0d) - 1.0d)) / 3.0d);
        int iw = this.IMAGE_WIDTH;
        this.toastPath.reset();
        this.toastPath.moveTo(((float) (th / 2)) + leftMargin, 0.0f);
        this.toastPath.rLineTo(((float) (this.IMAGE_WIDTH + this.MAX_TEXT_WIDTH)) * ws, 0.0f);
        this.toastPath.rCubicTo((float) circleOffset, 0.0f, (float) (th / 2), (float) ((th / 2) - circleOffset), (float) (th / 2), (float) (th / 2));
        this.toastPath.rLineTo((float) (-pd), 0.0f);
        this.toastPath.rCubicTo(0.0f, (float) (-iconoffset), (float) (((-iw) / 2) + iconoffset), (float) ((-iw) / 2), (float) ((-iw) / 2), (float) ((-iw) / 2));
        this.toastPath.rCubicTo((float) (-iconoffset), 0.0f, (float) ((-iw) / 2), (float) ((iw / 2) - iconoffset), (float) ((-iw) / 2), (float) (iw / 2));
        this.toastPath.rCubicTo(0.0f, (float) iconoffset, (float) ((iw / 2) - iconoffset), (float) (iw / 2), (float) (iw / 2), (float) (iw / 2));
        this.toastPath.rCubicTo((float) iconoffset, 0.0f, (float) (iw / 2), (float) (((-iw) / 2) + iconoffset), (float) (iw / 2), (float) ((-iw) / 2));
        this.toastPath.rLineTo((float) pd, 0.0f);
        this.toastPath.rCubicTo(0.0f, (float) circleOffset, (float) (circleOffset - (th / 2)), (float) (th / 2), (float) ((-th) / 2), (float) (th / 2));
        this.toastPath.rLineTo(((float) ((-this.IMAGE_WIDTH) - this.MAX_TEXT_WIDTH)) * ws, 0.0f);
        this.toastPath.rCubicTo((float) (-circleOffset), 0.0f, (float) ((-th) / 2), (float) (((-th) / 2) + circleOffset), (float) ((-th) / 2), (float) ((-th) / 2));
        this.toastPath.rCubicTo(0.0f, (float) (-circleOffset), (float) ((-circleOffset) + (th / 2)), (float) ((-th) / 2), (float) (th / 2), (float) ((-th) / 2));
        c.drawCircle(this.spinnerRect.centerX(), this.spinnerRect.centerY(), ((float) this.iconBounds.height()) / 1.9f, this.backPaint);
        c.drawPath(this.toastPath, this.backPaint);
        float prog = this.va.mCurrentFraction * 6.0f;
        float progrot = prog % 2.0f;
        float proglength = (this.easeinterpol.getInterpolation((prog % 3.0f) / 3.0f) * 3.0f) - 0.75f;
        if (proglength > 0.75f) {
            proglength = 0.75f - ((prog % 3.0f) - 1.5f);
            progrot += (((prog % 3.0f) - 1.5f) / 1.5f) * 2.0f;
        }
        this.toastPath.reset();
        if (this.mText.length() == 0) {
            ws = Math.max(1.0f - this.WIDTH_SCALE, 0.0f);
        }
        this.toastPath.arcTo(this.spinnerRect, 180.0f * progrot, Math.min(((266.66666f * proglength) + 1.0f) + (560.0f * (1.0f - ws)), 359.9999f));
        this.loaderPaint.setAlpha((int) (255.0f * ws));
        c.drawPath(this.toastPath, this.loaderPaint);
        if (this.WIDTH_SCALE > 1.0f) {
            Paint paint;
            Drawable icon = this.success ? this.completeicon : this.failedicon;
            float circleProg = this.WIDTH_SCALE - 1.0f;
            this.textPaint.setAlpha((int) ((128.0f * circleProg) + 127.0f));
            int paddingicon = (int) (((1.0f - (0.25f + (0.75f * circleProg))) * ((float) this.TOAST_HEIGHT)) / 2.0f);
            int completeoff = (int) (((1.0f - circleProg) * ((float) this.TOAST_HEIGHT)) / 8.0f);
            icon.setBounds(((int) this.spinnerRect.left) + paddingicon, (((int) this.spinnerRect.top) + paddingicon) + completeoff, ((int) this.spinnerRect.right) - paddingicon, (((int) this.spinnerRect.bottom) - paddingicon) + completeoff);
            float f = leftMargin + ((float) (this.TOAST_HEIGHT / 2));
            float f2 = ((float) (this.TOAST_HEIGHT / 2)) + (((1.0f - circleProg) * ((float) this.TOAST_HEIGHT)) / 8.0f);
            float f3 = ((0.25f + (0.75f * circleProg)) * ((float) this.TOAST_HEIGHT)) / 2.0f;
            if (this.success) {
                paint = this.successPaint;
            } else {
                paint = this.errorPaint;
            }
            c.drawCircle(f, f2, f3, paint);
            c.save();
            c.rotate(90.0f * (1.0f - circleProg), ((float) (this.TOAST_HEIGHT / 2)) + leftMargin, (float) (this.TOAST_HEIGHT / 2));
            icon.draw(c);
            c.restore();
            this.prevUpdate = 0;
            return;
        }
        int yPos = (int) (((float) (th / 2)) - ((this.textPaint.descent() + this.textPaint.ascent()) / 2.0f));
        if (this.outOfBounds) {
            float shift = 0.0f;
            if (this.prevUpdate == 0) {
                this.prevUpdate = System.currentTimeMillis();
            } else {
                shift = (((float) (System.currentTimeMillis() - this.prevUpdate)) / 16.0f) * ((float) this.MARQUE_STEP);
                if (shift - ((float) this.MAX_TEXT_WIDTH) > ((float) this.mTextBounds.width())) {
                    this.prevUpdate = 0;
                }
            }
            c.clipRect(th / 2, 0, (th / 2) + this.MAX_TEXT_WIDTH, this.TOAST_HEIGHT);
            c.drawText(this.mText, (((float) (th / 2)) - shift) + ((float) this.MAX_TEXT_WIDTH), (float) yPos, this.textPaint);
            return;
        }
        c.drawText(this.mText, 0, this.mText.length(), (float) ((th / 2) + ((this.MAX_TEXT_WIDTH - this.mTextBounds.width()) / 2)), (float) yPos, this.textPaint);
    }

    protected final void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode != 1073741824) {
            i = (this.IMAGE_WIDTH + this.MAX_TEXT_WIDTH) + this.TOAST_HEIGHT;
            size = mode == RtlSpacingHelper.UNDEFINED ? Math.min(i, size) : i;
        }
        int mode2 = MeasureSpec.getMode(heightMeasureSpec);
        i = MeasureSpec.getSize(heightMeasureSpec);
        if (mode2 != 1073741824) {
            mode = this.TOAST_HEIGHT;
            i = mode2 == RtlSpacingHelper.UNDEFINED ? Math.min(mode, i) : mode;
        }
        setMeasuredDimension(size, i);
    }
}

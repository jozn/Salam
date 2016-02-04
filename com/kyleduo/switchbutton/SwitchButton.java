package com.kyleduo.switchbutton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class SwitchButton extends CompoundButton {
    private static boolean SHOW_RECT;
    private boolean isAnimating;
    private AnimationController mAnimationController;
    private Rect mBackZone;
    private Rect mBounds;
    private float mCenterPos;
    private int mClickTimeout;
    private Configuration mConf;
    private boolean mIsChecked;
    private float mLastX;
    private SBAnimationListener mOnAnimateListener;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private Paint mRectPaint;
    private Rect mSafeZone;
    private RectF mSaveLayerZone;
    private float mStartX;
    private float mStartY;
    private Rect mThumbZone;
    private int mTouchSlop;

    class SBAnimationListener implements OnAnimateListener {
        SBAnimationListener() {
        }

        public final void onAnimationStart() {
            SwitchButton.this.isAnimating = true;
        }

        public final boolean continueAnimating() {
            return SwitchButton.this.mThumbZone.right < SwitchButton.this.mSafeZone.right && SwitchButton.this.mThumbZone.left > SwitchButton.this.mSafeZone.left;
        }

        public final void onFrameUpdate(int frame) {
            SwitchButton.this.moveThumb(frame);
            SwitchButton.this.postInvalidate();
        }

        public final void onAnimateComplete() {
            SwitchButton.this.setCheckedInClass$25decb5(SwitchButton.this.getStatusBasedOnPos());
            SwitchButton.this.isAnimating = false;
        }
    }

    static {
        SHOW_RECT = false;
    }

    @SuppressLint({"NewApi"})
    public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mIsChecked = false;
        this.mOnAnimateListener = new SBAnimationListener();
        this.isAnimating = false;
        this.mBounds = null;
        this.mConf = Configuration.getDefault(getContext().getResources().getDisplayMetrics().density);
        this.mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        this.mClickTimeout = ViewConfiguration.getPressedStateDuration() + ViewConfiguration.getTapTimeout();
        AnimationController animationController = AnimationController.getDefault();
        OnAnimateListener onAnimateListener = this.mOnAnimateListener;
        if (onAnimateListener == null) {
            throw new IllegalArgumentException("onAnimateListener can not be null");
        }
        animationController.mOnAnimateListener = onAnimateListener;
        this.mAnimationController = animationController;
        this.mBounds = new Rect();
        if (SHOW_RECT) {
            this.mRectPaint = new Paint();
            this.mRectPaint.setStyle(Style.STROKE);
        }
        TypedArray ta = context.obtainStyledAttributes(attrs, C0473R.styleable.SwitchButton);
        this.mConf.setThumbMarginInPixel(ta.getDimensionPixelSize(C0473R.styleable.SwitchButton_thumb_margin, this.mConf.getDefaultThumbMarginInPixel()));
        this.mConf.setThumbMarginInPixel(ta.getDimensionPixelSize(C0473R.styleable.SwitchButton_thumb_marginTop, this.mConf.mThumbMarginTop), ta.getDimensionPixelSize(C0473R.styleable.SwitchButton_thumb_marginBottom, this.mConf.mThumbMarginBottom), ta.getDimensionPixelSize(C0473R.styleable.SwitchButton_thumb_marginLeft, this.mConf.mThumbMarginLeft), ta.getDimensionPixelSize(C0473R.styleable.SwitchButton_thumb_marginRight, this.mConf.mThumbMarginRight));
        this.mConf.mRadius = (float) ta.getInt(C0473R.styleable.SwitchButton_radius, Default.DEFAULT_RADIUS);
        Configuration configuration = this.mConf;
        int dimensionPixelSize = ta.getDimensionPixelSize(C0473R.styleable.SwitchButton_thumb_width, -1);
        int dimensionPixelSize2 = ta.getDimensionPixelSize(C0473R.styleable.SwitchButton_thumb_height, -1);
        if (dimensionPixelSize > 0) {
            configuration.mThumbWidth = dimensionPixelSize;
        }
        if (dimensionPixelSize2 > 0) {
            configuration.mThumbHeight = dimensionPixelSize2;
        }
        configuration = this.mConf;
        float f = ta.getFloat(C0473R.styleable.SwitchButton_measureFactor, -1.0f);
        if (f <= 0.0f) {
            configuration.mMeasureFactor = Default.DEFAULT_MEASURE_FACTOR;
        }
        configuration.mMeasureFactor = f;
        Configuration configuration2 = this.mConf;
        int dimensionPixelSize3 = ta.getDimensionPixelSize(C0473R.styleable.SwitchButton_insetLeft, 0);
        dimensionPixelSize = ta.getDimensionPixelSize(C0473R.styleable.SwitchButton_insetTop, 0);
        dimensionPixelSize2 = ta.getDimensionPixelSize(C0473R.styleable.SwitchButton_insetRight, 0);
        int dimensionPixelSize4 = ta.getDimensionPixelSize(C0473R.styleable.SwitchButton_insetBottom, 0);
        if (dimensionPixelSize3 > 0) {
            dimensionPixelSize3 = -dimensionPixelSize3;
        }
        configuration2.mInsetBounds.left = dimensionPixelSize3;
        if (dimensionPixelSize > 0) {
            dimensionPixelSize3 = -dimensionPixelSize;
        } else {
            dimensionPixelSize3 = dimensionPixelSize;
        }
        configuration2.mInsetBounds.top = dimensionPixelSize3;
        if (dimensionPixelSize2 > 0) {
            dimensionPixelSize3 = -dimensionPixelSize2;
        } else {
            dimensionPixelSize3 = dimensionPixelSize2;
        }
        configuration2.mInsetBounds.right = dimensionPixelSize3;
        if (dimensionPixelSize4 > 0) {
            dimensionPixelSize3 = -dimensionPixelSize4;
        } else {
            dimensionPixelSize3 = dimensionPixelSize4;
        }
        configuration2.mInsetBounds.bottom = dimensionPixelSize3;
        int velocity = ta.getInteger(C0473R.styleable.SwitchButton_animationVelocity, -1);
        animationController = this.mAnimationController;
        if (velocity <= 0) {
            animationController.mVelocity = AnimationController.DEFAULT_VELOCITY;
        } else {
            animationController.mVelocity = velocity;
        }
        if (this.mConf != null) {
            configuration = this.mConf;
            Drawable fetchDrawable = fetchDrawable(ta, C0473R.styleable.SwitchButton_offDrawable, C0473R.styleable.SwitchButton_offColor, Default.DEFAULT_OFF_COLOR);
            if (fetchDrawable == null) {
                throw new IllegalArgumentException("off drawable can not be null");
            }
            configuration.mOffDrawable = fetchDrawable;
            configuration = this.mConf;
            fetchDrawable = fetchDrawable(ta, C0473R.styleable.SwitchButton_onDrawable, C0473R.styleable.SwitchButton_onColor, Default.DEFAULT_ON_COLOR);
            if (fetchDrawable == null) {
                throw new IllegalArgumentException("on drawable can not be null");
            }
            configuration.mOnDrawable = fetchDrawable;
            Configuration configuration3 = this.mConf;
            Drawable drawable = ta.getDrawable(C0473R.styleable.SwitchButton_thumbDrawable);
            if (drawable == null) {
                dimensionPixelSize2 = ta.getColor(C0473R.styleable.SwitchButton_thumbColor, Default.DEFAULT_THUMB_COLOR);
                dimensionPixelSize4 = ta.getColor(C0473R.styleable.SwitchButton_thumbPressedColor, Default.DEFAULT_THUMB_PRESSED_COLOR);
                drawable = new StateListDrawable();
                Drawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setCornerRadius(this.mConf.getRadius());
                gradientDrawable.setColor(dimensionPixelSize2);
                Drawable gradientDrawable2 = new GradientDrawable();
                gradientDrawable2.setCornerRadius(this.mConf.getRadius());
                gradientDrawable2.setColor(dimensionPixelSize4);
                drawable.addState(View.PRESSED_ENABLED_STATE_SET, gradientDrawable2);
                drawable.addState(new int[0], gradientDrawable);
            }
            if (drawable == null) {
                throw new IllegalArgumentException("thumb drawable can not be null");
            }
            configuration3.mThumbDrawable = drawable;
        }
        ta.recycle();
        if (VERSION.SDK_INT >= 11) {
            setLayerType(1, null);
        }
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchButton(Context context) {
        this(context, null);
    }

    private Drawable fetchDrawable(TypedArray ta, int attrId, int alterColorId, int defaultColor) {
        Drawable tempDrawable = ta.getDrawable(attrId);
        if (tempDrawable != null) {
            return tempDrawable;
        }
        int tempColor = ta.getColor(alterColorId, defaultColor);
        tempDrawable = new GradientDrawable();
        ((GradientDrawable) tempDrawable).setCornerRadius(this.mConf.getRadius());
        ((GradientDrawable) tempDrawable).setColor(tempColor);
        return tempDrawable;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        float thumbWidth = (float) this.mConf.getThumbWidth();
        Configuration configuration = this.mConf;
        if (configuration.mMeasureFactor <= 0.0f) {
            configuration.mMeasureFactor = Default.DEFAULT_MEASURE_FACTOR;
        }
        int paddingLeft = (int) (((thumbWidth * configuration.mMeasureFactor) + ((float) getPaddingLeft())) + ((float) getPaddingRight()));
        int i = this.mConf.mThumbMarginLeft + this.mConf.mThumbMarginRight;
        if (i > 0) {
            paddingLeft += i;
        }
        if (mode == 1073741824) {
            paddingLeft = Math.max(size, paddingLeft);
        } else if (mode == RtlSpacingHelper.UNDEFINED) {
            paddingLeft = Math.min(size, paddingLeft);
        }
        mode = (this.mConf.mInsetBounds.left + this.mConf.mInsetBounds.right) + paddingLeft;
        size = MeasureSpec.getMode(heightMeasureSpec);
        i = MeasureSpec.getSize(heightMeasureSpec);
        paddingLeft = (this.mConf.getThumbHeight() + getPaddingTop()) + getPaddingBottom();
        int i2 = this.mConf.mThumbMarginTop + this.mConf.mThumbMarginBottom;
        if (i2 > 0) {
            paddingLeft += i2;
        }
        if (size == 1073741824) {
            paddingLeft = Math.max(i, paddingLeft);
        } else if (size == RtlSpacingHelper.UNDEFINED) {
            paddingLeft = Math.min(i, paddingLeft);
        }
        setMeasuredDimension(mode, paddingLeft + (this.mConf.mInsetBounds.top + this.mConf.mInsetBounds.bottom));
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (measuredWidth <= 0 || measuredHeight <= 0) {
            this.mBackZone = null;
        } else {
            if (this.mBackZone == null) {
                this.mBackZone = new Rect();
            }
            this.mBackZone.set(getPaddingLeft() + (this.mConf.mThumbMarginLeft > 0 ? 0 : -this.mConf.mThumbMarginLeft), getPaddingTop() + (this.mConf.mThumbMarginTop > 0 ? 0 : -this.mConf.mThumbMarginTop), (-this.mConf.getShrinkX()) + ((measuredWidth - getPaddingRight()) - (this.mConf.mThumbMarginRight > 0 ? 0 : -this.mConf.mThumbMarginRight)), ((measuredHeight - getPaddingBottom()) - (this.mConf.mThumbMarginBottom > 0 ? 0 : -this.mConf.mThumbMarginBottom)) + (-this.mConf.getShrinkY()));
        }
        measuredWidth = getMeasuredWidth();
        measuredHeight = getMeasuredHeight();
        if (measuredWidth <= 0 || measuredHeight <= 0) {
            this.mSafeZone = null;
        } else {
            if (this.mSafeZone == null) {
                this.mSafeZone = new Rect();
            }
            this.mSafeZone.set(getPaddingLeft() + (this.mConf.mThumbMarginLeft > 0 ? this.mConf.mThumbMarginLeft : 0), getPaddingTop() + (this.mConf.mThumbMarginTop > 0 ? this.mConf.mThumbMarginTop : 0), (-this.mConf.getShrinkX()) + ((measuredWidth - getPaddingRight()) - (this.mConf.mThumbMarginRight > 0 ? this.mConf.mThumbMarginRight : 0)), ((measuredHeight - getPaddingBottom()) - (this.mConf.mThumbMarginBottom > 0 ? this.mConf.mThumbMarginBottom : 0)) + (-this.mConf.getShrinkY()));
            this.mCenterPos = (float) (this.mSafeZone.left + (((this.mSafeZone.right - this.mSafeZone.left) - this.mConf.getThumbWidth()) / 2));
        }
        int measuredWidth2 = getMeasuredWidth();
        measuredWidth = getMeasuredHeight();
        if (measuredWidth2 <= 0 || measuredWidth <= 0) {
            this.mThumbZone = null;
        } else {
            if (this.mThumbZone == null) {
                this.mThumbZone = new Rect();
            }
            measuredWidth2 = this.mIsChecked ? this.mSafeZone.right - this.mConf.getThumbWidth() : this.mSafeZone.left;
            measuredWidth = this.mConf.getThumbWidth() + measuredWidth2;
            measuredHeight = this.mSafeZone.top;
            this.mThumbZone.set(measuredWidth2, measuredHeight, measuredWidth, this.mConf.getThumbHeight() + measuredHeight);
        }
        if (this.mBackZone != null) {
            this.mConf.mOnDrawable.setBounds(this.mBackZone);
            this.mConf.mOffDrawable.setBounds(this.mBackZone);
        }
        if (this.mThumbZone != null) {
            this.mConf.mThumbDrawable.setBounds(this.mThumbZone);
        }
        if (getMeasuredWidth() > 0 && getMeasuredHeight() > 0) {
            this.mSaveLayerZone = new RectF(0.0f, 0.0f, (float) getMeasuredWidth(), (float) getMeasuredHeight());
        }
        ViewGroup viewGroup = (ViewGroup) getParent();
        if (viewGroup != null) {
            viewGroup.setClipChildren(false);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void onDraw(android.graphics.Canvas r6) {
        /*
        r5 = this;
        r0 = 1;
        r1 = 0;
        super.onDraw(r6);
        r2 = r5.mBounds;
        r6.getClipBounds(r2);
        r2 = r5.mBounds;
        if (r2 == 0) goto L_0x0043;
    L_0x000e:
        r2 = r5.mConf;
        r2 = r2.needShrink();
        if (r2 == 0) goto L_0x0043;
    L_0x0016:
        r2 = r5.mBounds;
        r3 = r5.mConf;
        r3 = r3.getShrinkX();
        r3 = r3 / 2;
        r4 = r5.mConf;
        r4 = r4.getShrinkY();
        r4 = r4 / 2;
        r2.inset(r3, r4);
        r2 = r5.mBounds;
        r3 = android.graphics.Region.Op.REPLACE;
        r6.clipRect(r2, r3);
        r2 = r5.mConf;
        r2 = r2.mInsetBounds;
        r2 = r2.left;
        r2 = (float) r2;
        r3 = r5.mConf;
        r3 = r3.mInsetBounds;
        r3 = r3.top;
        r3 = (float) r3;
        r6.translate(r2, r3);
    L_0x0043:
        r2 = r5.isEnabled();
        if (r2 != 0) goto L_0x00fc;
    L_0x0049:
        r2 = r5.mConf;
        r2 = r2.mThumbDrawable;
        r2 = r2 instanceof android.graphics.drawable.StateListDrawable;
        r3 = r5.mConf;
        r3 = r3.mOnDrawable;
        r3 = r3 instanceof android.graphics.drawable.StateListDrawable;
        r4 = r5.mConf;
        r4 = r4.mOffDrawable;
        r4 = r4 instanceof android.graphics.drawable.StateListDrawable;
        if (r2 == 0) goto L_0x0061;
    L_0x005d:
        if (r3 == 0) goto L_0x0061;
    L_0x005f:
        if (r4 != 0) goto L_0x00f9;
    L_0x0061:
        r2 = r0;
    L_0x0062:
        if (r2 == 0) goto L_0x00fc;
    L_0x0064:
        if (r0 == 0) goto L_0x006f;
    L_0x0066:
        r1 = r5.mSaveLayerZone;
        r2 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r3 = 31;
        r6.saveLayerAlpha(r1, r2, r3);
    L_0x006f:
        r1 = r5.mConf;
        r1 = r1.mOffDrawable;
        r1.draw(r6);
        r1 = r5.mConf;
        r2 = r1.mOnDrawable;
        r1 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r3 = r5.mSafeZone;
        if (r3 == 0) goto L_0x00a8;
    L_0x0080:
        r3 = r5.mSafeZone;
        r3 = r3.right;
        r4 = r5.mSafeZone;
        r4 = r4.left;
        if (r3 == r4) goto L_0x00a8;
    L_0x008a:
        r3 = r5.mSafeZone;
        r3 = r3.right;
        r4 = r5.mConf;
        r4 = r4.getThumbWidth();
        r3 = r3 - r4;
        r4 = r5.mSafeZone;
        r4 = r4.left;
        r3 = r3 - r4;
        if (r3 <= 0) goto L_0x00a8;
    L_0x009c:
        r1 = r5.mThumbZone;
        r1 = r1.left;
        r4 = r5.mSafeZone;
        r4 = r4.left;
        r1 = r1 - r4;
        r1 = r1 * 255;
        r1 = r1 / r3;
    L_0x00a8:
        r2.setAlpha(r1);
        r1 = r5.mConf;
        r1 = r1.mOnDrawable;
        r1.draw(r6);
        r1 = r5.mConf;
        r1 = r1.mThumbDrawable;
        r1.draw(r6);
        if (r0 == 0) goto L_0x00be;
    L_0x00bb:
        r6.restore();
    L_0x00be:
        r1 = SHOW_RECT;
        if (r1 == 0) goto L_0x00f8;
    L_0x00c2:
        r1 = r5.mRectPaint;
        r2 = "#AA0000";
        r2 = android.graphics.Color.parseColor(r2);
        r1.setColor(r2);
        r1 = r5.mBackZone;
        r2 = r5.mRectPaint;
        r6.drawRect(r1, r2);
        r1 = r5.mRectPaint;
        r2 = "#00FF00";
        r2 = android.graphics.Color.parseColor(r2);
        r1.setColor(r2);
        r1 = r5.mSafeZone;
        r2 = r5.mRectPaint;
        r6.drawRect(r1, r2);
        r1 = r5.mRectPaint;
        r2 = "#0000FF";
        r2 = android.graphics.Color.parseColor(r2);
        r1.setColor(r2);
        r1 = r5.mThumbZone;
        r2 = r5.mRectPaint;
        r6.drawRect(r1, r2);
    L_0x00f8:
        return;
    L_0x00f9:
        r2 = r1;
        goto L_0x0062;
    L_0x00fc:
        r0 = r1;
        goto L_0x0064;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.kyleduo.switchbutton.SwitchButton.onDraw(android.graphics.Canvas):void");
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.isAnimating || !isEnabled()) {
            return false;
        }
        float deltaX = event.getX() - this.mStartX;
        float deltaY = event.getY() - this.mStartY;
        switch (event.getAction()) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                ViewParent parent = getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                this.mStartX = event.getX();
                this.mStartY = event.getY();
                this.mLastX = this.mStartX;
                setPressed(true);
                break;
            case Logger.SEVERE /*1*/:
            case Logger.INFO /*3*/:
                setPressed(false);
                boolean nextStatus = getStatusBasedOnPos();
                float time = (float) (event.getEventTime() - event.getDownTime());
                if (deltaX < ((float) this.mTouchSlop) && deltaY < ((float) this.mTouchSlop) && time < ((float) this.mClickTimeout)) {
                    performClick();
                    break;
                }
                slideToChecked(nextStatus);
                break;
                break;
            case Logger.WARNING /*2*/:
                float x = event.getX();
                moveThumb((int) (x - this.mLastX));
                this.mLastX = x;
                break;
        }
        invalidate();
        return true;
    }

    private boolean getStatusBasedOnPos() {
        return ((float) this.mThumbZone.left) > this.mCenterPos;
    }

    public void invalidate() {
        if (this.mBounds == null || !this.mConf.needShrink()) {
            super.invalidate();
        } else {
            invalidate(this.mBounds);
        }
    }

    public boolean performClick() {
        return super.performClick();
    }

    public void setChecked(boolean checked) {
        if (this.mThumbZone != null) {
            moveThumb(checked ? getMeasuredWidth() : -getMeasuredWidth());
        }
        setCheckedInClass$25decb5(checked);
    }

    public boolean isChecked() {
        return this.mIsChecked;
    }

    public void toggle() {
        slideToChecked(!this.mIsChecked);
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mConf != null) {
            setDrawableState(this.mConf.mThumbDrawable);
            setDrawableState(this.mConf.mOnDrawable);
            setDrawableState(this.mConf.mOffDrawable);
        }
    }

    private void setDrawableState(Drawable drawable) {
        if (drawable != null) {
            drawable.setState(getDrawableState());
            invalidate();
        }
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        if (onCheckedChangeListener == null) {
            throw new IllegalArgumentException("onCheckedChangeListener can not be null");
        }
        this.mOnCheckedChangeListener = onCheckedChangeListener;
    }

    private void setCheckedInClass$25decb5(boolean checked) {
        if (this.mIsChecked != checked) {
            this.mIsChecked = checked;
            refreshDrawableState();
            if (this.mOnCheckedChangeListener != null) {
                this.mOnCheckedChangeListener.onCheckedChanged(this, this.mIsChecked);
            }
        }
    }

    private void slideToChecked(boolean checked) {
        if (!this.isAnimating) {
            int from = this.mThumbZone.left;
            int to = checked ? this.mSafeZone.right - this.mConf.getThumbWidth() : this.mSafeZone.left;
            AnimationController animationController = this.mAnimationController;
            animationController.isAnimating = true;
            animationController.mFrom = from;
            animationController.mTo = to;
            animationController.mFrame = animationController.mVelocity;
            if (animationController.mTo > animationController.mFrom) {
                animationController.mFrame = Math.abs(animationController.mVelocity);
            } else if (animationController.mTo < animationController.mFrom) {
                animationController.mFrame = -Math.abs(animationController.mVelocity);
            } else {
                animationController.isAnimating = false;
                animationController.mOnAnimateListener.onAnimateComplete();
                return;
            }
            animationController.mOnAnimateListener.onAnimationStart();
            new RequireNextFrame().run();
        }
    }

    private void moveThumb(int delta) {
        int newLeft = this.mThumbZone.left + delta;
        int newRight = this.mThumbZone.right + delta;
        if (newLeft < this.mSafeZone.left) {
            newLeft = this.mSafeZone.left;
            newRight = newLeft + this.mConf.getThumbWidth();
        }
        if (newRight > this.mSafeZone.right) {
            newRight = this.mSafeZone.right;
            newLeft = newRight - this.mConf.getThumbWidth();
        }
        this.mThumbZone.set(newLeft, this.mThumbZone.top, newRight, this.mThumbZone.bottom);
        this.mConf.mThumbDrawable.setBounds(this.mThumbZone);
    }
}

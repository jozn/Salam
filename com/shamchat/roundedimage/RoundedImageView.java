package com.shamchat.roundedimage;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.shamchat.activity.C0853R;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class RoundedImageView extends ImageView {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final ScaleType[] SCALE_TYPES;
    private ColorStateList borderColor;
    private float borderWidth;
    private float cornerRadius;
    private boolean isOval;
    private Drawable mBackgroundDrawable;
    private Drawable mDrawable;
    private int mResource;
    private ScaleType mScaleType;
    private boolean mutateBackground;

    /* renamed from: com.shamchat.roundedimage.RoundedImageView.1 */
    static /* synthetic */ class C11681 {
        static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType;

        static {
            $SwitchMap$android$widget$ImageView$ScaleType = new int[ScaleType.values().length];
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER_CROP.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER_INSIDE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_CENTER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_START.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_END.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_XY.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    static {
        $assertionsDisabled = !RoundedImageView.class.desiredAssertionStatus();
        SCALE_TYPES = new ScaleType[]{ScaleType.MATRIX, ScaleType.FIT_XY, ScaleType.FIT_START, ScaleType.FIT_CENTER, ScaleType.FIT_END, ScaleType.CENTER, ScaleType.CENTER_CROP, ScaleType.CENTER_INSIDE};
    }

    public RoundedImageView(Context context) {
        super(context);
        this.cornerRadius = 0.0f;
        this.borderWidth = 0.0f;
        this.borderColor = ColorStateList.valueOf(ViewCompat.MEASURED_STATE_MASK);
        this.isOval = false;
        this.mutateBackground = false;
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.cornerRadius = 0.0f;
        this.borderWidth = 0.0f;
        this.borderColor = ColorStateList.valueOf(ViewCompat.MEASURED_STATE_MASK);
        this.isOval = false;
        this.mutateBackground = false;
        TypedArray a = context.obtainStyledAttributes(attrs, C0853R.styleable.RoundedImageView, defStyle, 0);
        int index = a.getInt(0, -1);
        if (index >= 0) {
            setScaleType(SCALE_TYPES[index]);
        } else {
            setScaleType(ScaleType.FIT_CENTER);
        }
        this.cornerRadius = (float) a.getDimensionPixelSize(1, -1);
        this.borderWidth = (float) a.getDimensionPixelSize(2, -1);
        if (this.cornerRadius < 0.0f) {
            this.cornerRadius = 0.0f;
        }
        if (this.borderWidth < 0.0f) {
            this.borderWidth = 0.0f;
        }
        this.borderColor = a.getColorStateList(3);
        if (this.borderColor == null) {
            this.borderColor = ColorStateList.valueOf(ViewCompat.MEASURED_STATE_MASK);
        }
        this.mutateBackground = a.getBoolean(4, false);
        this.isOval = a.getBoolean(5, false);
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(true);
        a.recycle();
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    public ScaleType getScaleType() {
        return this.mScaleType;
    }

    public void setScaleType(ScaleType scaleType) {
        if (!$assertionsDisabled && scaleType == null) {
            throw new AssertionError();
        } else if (this.mScaleType != scaleType) {
            this.mScaleType = scaleType;
            switch (C11681.$SwitchMap$android$widget$ImageView$ScaleType[scaleType.ordinal()]) {
                case Logger.SEVERE /*1*/:
                case Logger.WARNING /*2*/:
                case Logger.INFO /*3*/:
                case Logger.CONFIG /*4*/:
                case Logger.FINE /*5*/:
                case Logger.FINER /*6*/:
                case Logger.FINEST /*7*/:
                    super.setScaleType(ScaleType.FIT_XY);
                    break;
                default:
                    super.setScaleType(scaleType);
                    break;
            }
            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            invalidate();
        }
    }

    public void setImageDrawable(Drawable drawable) {
        this.mResource = 0;
        this.mDrawable = RoundedDrawable.fromDrawable(drawable);
        updateDrawableAttrs();
        super.setImageDrawable(this.mDrawable);
    }

    public void setImageBitmap(Bitmap bm) {
        this.mResource = 0;
        this.mDrawable = RoundedDrawable.fromBitmap(bm);
        updateDrawableAttrs();
        super.setImageDrawable(this.mDrawable);
    }

    public void setImageResource(int resId) {
        if (this.mResource != resId) {
            this.mResource = resId;
            this.mDrawable = resolveResource();
            updateDrawableAttrs();
            super.setImageDrawable(this.mDrawable);
        }
    }

    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setImageDrawable(getDrawable());
    }

    private Drawable resolveResource() {
        Resources rsrc = getResources();
        if (rsrc == null) {
            return null;
        }
        Drawable d = null;
        if (this.mResource != 0) {
            try {
                d = rsrc.getDrawable(this.mResource);
            } catch (Exception e) {
                Log.w("RoundedImageView", "Unable to find resource: " + this.mResource, e);
                this.mResource = 0;
            }
        }
        return RoundedDrawable.fromDrawable(d);
    }

    public void setBackground(Drawable background) {
        setBackgroundDrawable(background);
    }

    private void updateDrawableAttrs() {
        updateAttrs(this.mDrawable);
    }

    private void updateBackgroundDrawableAttrs(boolean convert) {
        if (this.mutateBackground) {
            if (convert) {
                this.mBackgroundDrawable = RoundedDrawable.fromDrawable(this.mBackgroundDrawable);
            }
            updateAttrs(this.mBackgroundDrawable);
        }
    }

    private void updateAttrs(Drawable drawable) {
        if (drawable != null) {
            if (drawable instanceof RoundedDrawable) {
                RoundedDrawable roundedDrawable = (RoundedDrawable) drawable;
                ScaleType scaleType = this.mScaleType;
                if (scaleType == null) {
                    scaleType = ScaleType.FIT_CENTER;
                }
                if (roundedDrawable.mScaleType != scaleType) {
                    roundedDrawable.mScaleType = scaleType;
                    roundedDrawable.updateShaderMatrix();
                }
                roundedDrawable.mCornerRadius = this.cornerRadius;
                roundedDrawable.mBorderWidth = this.borderWidth;
                roundedDrawable.mBorderPaint.setStrokeWidth(roundedDrawable.mBorderWidth);
                ColorStateList colorStateList = this.borderColor;
                if (colorStateList == null) {
                    colorStateList = ColorStateList.valueOf(0);
                }
                roundedDrawable.mBorderColor = colorStateList;
                roundedDrawable.mBorderPaint.setColor(roundedDrawable.mBorderColor.getColorForState(roundedDrawable.getState(), ViewCompat.MEASURED_STATE_MASK));
                roundedDrawable.mOval = this.isOval;
            } else if (drawable instanceof LayerDrawable) {
                LayerDrawable ld = (LayerDrawable) drawable;
                int layers = ld.getNumberOfLayers();
                for (int i = 0; i < layers; i++) {
                    updateAttrs(ld.getDrawable(i));
                }
            }
        }
    }

    @Deprecated
    public void setBackgroundDrawable(Drawable background) {
        this.mBackgroundDrawable = background;
        updateBackgroundDrawableAttrs(true);
        super.setBackgroundDrawable(this.mBackgroundDrawable);
    }
}

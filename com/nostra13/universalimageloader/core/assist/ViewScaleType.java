package com.nostra13.universalimageloader.core.assist;

import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class ViewScaleType {
    private static final /* synthetic */ int[] $VALUES$127942bf;
    public static final int CROP$3b550fbc;
    public static final int FIT_INSIDE$3b550fbc;

    /* renamed from: com.nostra13.universalimageloader.core.assist.ViewScaleType.1 */
    static /* synthetic */ class C04951 {
        static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType;

        static {
            $SwitchMap$android$widget$ImageView$ScaleType = new int[ScaleType.values().length];
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_CENTER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_XY.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_START.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_END.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER_INSIDE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.MATRIX.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER_CROP.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    static {
        FIT_INSIDE$3b550fbc = 1;
        CROP$3b550fbc = 2;
        $VALUES$127942bf = new int[]{FIT_INSIDE$3b550fbc, CROP$3b550fbc};
    }

    public static int fromImageView$11a60ad5(ImageView imageView) {
        switch (C04951.$SwitchMap$android$widget$ImageView$ScaleType[imageView.getScaleType().ordinal()]) {
            case Logger.SEVERE /*1*/:
            case Logger.WARNING /*2*/:
            case Logger.INFO /*3*/:
            case Logger.CONFIG /*4*/:
            case Logger.FINE /*5*/:
                return FIT_INSIDE$3b550fbc;
            default:
                return CROP$3b550fbc;
        }
    }
}

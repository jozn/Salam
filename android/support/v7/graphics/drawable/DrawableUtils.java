package android.support.v7.graphics.drawable;

import android.graphics.PorterDuff.Mode;
import android.os.Build.VERSION;
import com.kyleduo.switchbutton.C0473R;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class DrawableUtils {
    public static Mode parseTintMode(int value, Mode defaultMode) {
        switch (value) {
            case Logger.INFO /*3*/:
                return Mode.SRC_OVER;
            case Logger.FINE /*5*/:
                return Mode.SRC_IN;
            case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                return Mode.SRC_ATOP;
            case C0473R.styleable.SwitchButton_animationVelocity /*14*/:
                return Mode.MULTIPLY;
            case C0473R.styleable.SwitchButton_radius /*15*/:
                return Mode.SCREEN;
            case C0473R.styleable.SwitchButton_measureFactor /*16*/:
                if (VERSION.SDK_INT >= 11) {
                    return Mode.valueOf("ADD");
                }
                return defaultMode;
            default:
                return defaultMode;
        }
    }
}

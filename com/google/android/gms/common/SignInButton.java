package com.google.android.gms.common;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import com.google.android.gms.C0237R;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zzab;
import com.google.android.gms.common.internal.zzac;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zzg.zza;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class SignInButton extends FrameLayout implements OnClickListener {
    private int mColor;
    private int mSize;
    private Scope[] zzaem;
    private View zzaen;
    private OnClickListener zzaeo;

    public SignInButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignInButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.zzaeo = null;
        zza(context, attrs);
        int i = this.mSize;
        int i2 = this.mColor;
        Scope[] scopeArr = this.zzaem;
        this.mSize = i;
        this.mColor = i2;
        this.zzaem = scopeArr;
        Context context2 = getContext();
        if (this.zzaen != null) {
            removeView(this.zzaen);
        }
        try {
            this.zzaen = zzab.zzb(context2, this.mSize, this.mColor, this.zzaem);
        } catch (zza e) {
            Log.w("SignInButton", "Sign in button not found, using placeholder instead");
            i2 = this.mSize;
            int i3 = this.mColor;
            Scope[] scopeArr2 = this.zzaem;
            View com_google_android_gms_common_internal_zzac = new zzac(context2);
            Resources resources = context2.getResources();
            boolean zza = zzac.zza(scopeArr2);
            com_google_android_gms_common_internal_zzac.setTypeface(Typeface.DEFAULT_BOLD);
            com_google_android_gms_common_internal_zzac.setTextSize(14.0f);
            float f = resources.getDisplayMetrics().density;
            com_google_android_gms_common_internal_zzac.setMinHeight((int) ((f * 48.0f) + 0.5f));
            com_google_android_gms_common_internal_zzac.setMinWidth((int) ((f * 48.0f) + 0.5f));
            com_google_android_gms_common_internal_zzac.setBackgroundDrawable(resources.getDrawable(zza ? zzac.zzd(i2, zzac.zzf(i3, C0237R.drawable.common_plus_signin_btn_icon_dark, C0237R.drawable.common_plus_signin_btn_icon_light, C0237R.drawable.common_plus_signin_btn_icon_dark), zzac.zzf(i3, C0237R.drawable.common_plus_signin_btn_text_dark, C0237R.drawable.common_plus_signin_btn_text_light, C0237R.drawable.common_plus_signin_btn_text_dark)) : zzac.zzd(i2, zzac.zzf(i3, C0237R.drawable.common_google_signin_btn_icon_dark, C0237R.drawable.common_google_signin_btn_icon_light, C0237R.drawable.common_google_signin_btn_icon_light), zzac.zzf(i3, C0237R.drawable.common_google_signin_btn_text_dark, C0237R.drawable.common_google_signin_btn_text_light, C0237R.drawable.common_google_signin_btn_text_light))));
            com_google_android_gms_common_internal_zzac.setTextColor((ColorStateList) zzx.zzy(resources.getColorStateList(zza ? zzac.zzf(i3, C0237R.color.common_plus_signin_btn_text_dark, C0237R.color.common_plus_signin_btn_text_light, C0237R.color.common_plus_signin_btn_text_dark) : zzac.zzf(i3, C0237R.color.common_google_signin_btn_text_dark, C0237R.color.common_google_signin_btn_text_light, C0237R.color.common_google_signin_btn_text_light))));
            switch (i2) {
                case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                    com_google_android_gms_common_internal_zzac.setText(resources.getString(C0237R.string.common_signin_button_text));
                    break;
                case Logger.SEVERE /*1*/:
                    com_google_android_gms_common_internal_zzac.setText(resources.getString(C0237R.string.common_signin_button_text_long));
                    break;
                case Logger.WARNING /*2*/:
                    com_google_android_gms_common_internal_zzac.setText(null);
                    break;
                default:
                    throw new IllegalStateException("Unknown button size: " + i2);
            }
            com_google_android_gms_common_internal_zzac.setTransformationMethod(null);
            this.zzaen = com_google_android_gms_common_internal_zzac;
        }
        addView(this.zzaen);
        this.zzaen.setEnabled(isEnabled());
        this.zzaen.setOnClickListener(this);
    }

    private void zza(Context context, AttributeSet attributeSet) {
        int i = 0;
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, C0237R.styleable.SignInButton, 0, 0);
        try {
            this.mSize = obtainStyledAttributes.getInt(C0237R.styleable.SignInButton_buttonSize, 0);
            this.mColor = obtainStyledAttributes.getInt(C0237R.styleable.SignInButton_colorScheme, 2);
            String string = obtainStyledAttributes.getString(C0237R.styleable.SignInButton_scopeUris);
            if (string == null) {
                this.zzaem = null;
            } else {
                String[] split = string.trim().split("\\s+");
                this.zzaem = new Scope[split.length];
                while (i < split.length) {
                    this.zzaem[i] = new Scope(split[i].toString());
                    i++;
                }
            }
            obtainStyledAttributes.recycle();
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
        }
    }

    public final void onClick(View view) {
        if (this.zzaeo != null && view == this.zzaen) {
            this.zzaeo.onClick(this);
        }
    }

    public final void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.zzaen.setEnabled(enabled);
    }

    public final void setOnClickListener(OnClickListener listener) {
        this.zzaeo = listener;
        if (this.zzaen != null) {
            this.zzaen.setOnClickListener(this);
        }
    }
}

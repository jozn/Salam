package com.google.android.gms.common.images;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.google.android.gms.common.images.ImageManager.OnImageLoadedListener;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.internal.zzmv;
import com.google.android.gms.internal.zzmx;
import java.lang.ref.WeakReference;
import java.util.Arrays;

public abstract class zza {
    final zza zzaig;
    protected int zzaii;
    protected int zzaio;

    static final class zza {
        public final Uri uri;

        public zza(Uri uri) {
            this.uri = uri;
        }

        public final boolean equals(Object obj) {
            if (obj instanceof zza) {
                return this == obj ? true : zzw.equal(((zza) obj).uri, this.uri);
            } else {
                return false;
            }
        }

        public final int hashCode() {
            return Arrays.hashCode(new Object[]{this.uri});
        }
    }

    public static final class zzc extends zza {
        private WeakReference<OnImageLoadedListener> zzaiq;

        public final boolean equals(Object obj) {
            if (!(obj instanceof zzc)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            zzc com_google_android_gms_common_images_zza_zzc = (zzc) obj;
            OnImageLoadedListener onImageLoadedListener = (OnImageLoadedListener) this.zzaiq.get();
            OnImageLoadedListener onImageLoadedListener2 = (OnImageLoadedListener) com_google_android_gms_common_images_zza_zzc.zzaiq.get();
            return onImageLoadedListener2 != null && onImageLoadedListener != null && zzw.equal(onImageLoadedListener2, onImageLoadedListener) && zzw.equal(com_google_android_gms_common_images_zza_zzc.zzaig, this.zzaig);
        }

        public final int hashCode() {
            return Arrays.hashCode(new Object[]{this.zzaig});
        }

        protected final void zza$7259e265$782ea059() {
            this.zzaiq.get();
        }
    }

    final void zza$1cfc2d67(Context context, Bitmap bitmap) {
        if (bitmap == null) {
            throw new IllegalArgumentException("null reference");
        }
        if ((this.zzaio & 1) != 0) {
            bitmap = zzmv.zza(bitmap);
        }
        BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
        zza$7259e265$782ea059();
    }

    final void zza$4e565205(Context context, zzmx com_google_android_gms_internal_zzmx) {
        if (this.zzaii != 0) {
            int i = this.zzaii;
            Resources resources = context.getResources();
            if (this.zzaio > 0) {
                com.google.android.gms.internal.zzmx.zza com_google_android_gms_internal_zzmx_zza = new com.google.android.gms.internal.zzmx.zza(i, this.zzaio);
                if (((Drawable) com_google_android_gms_internal_zzmx.get(com_google_android_gms_internal_zzmx_zza)) == null) {
                    Object drawable = resources.getDrawable(i);
                    if ((this.zzaio & 1) != 0) {
                        Bitmap bitmap;
                        if (drawable == null) {
                            bitmap = null;
                        } else if (drawable instanceof BitmapDrawable) {
                            bitmap = ((BitmapDrawable) drawable).getBitmap();
                        } else {
                            Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
                            Canvas canvas = new Canvas(createBitmap);
                            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                            drawable.draw(canvas);
                            bitmap = createBitmap;
                        }
                        drawable = new BitmapDrawable(resources, zzmv.zza(bitmap));
                    }
                    com_google_android_gms_internal_zzmx.put(com_google_android_gms_internal_zzmx_zza, drawable);
                }
            } else {
                resources.getDrawable(i);
            }
        }
        zza$7259e265$782ea059();
    }

    protected abstract void zza$7259e265$782ea059();
}

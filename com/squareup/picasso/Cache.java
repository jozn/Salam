package com.squareup.picasso;

import android.graphics.Bitmap;

public interface Cache {
    public static final Cache NONE;

    /* renamed from: com.squareup.picasso.Cache.1 */
    static class C12311 implements Cache {
        C12311() {
        }

        public final Bitmap get(String key) {
            return null;
        }

        public final void set(String key, Bitmap bitmap) {
        }

        public final int size() {
            return 0;
        }

        public final int maxSize() {
            return 0;
        }

        public final void clear() {
        }
    }

    void clear();

    Bitmap get(String str);

    int maxSize();

    void set(String str, Bitmap bitmap);

    int size();

    static {
        NONE = new C12311();
    }
}

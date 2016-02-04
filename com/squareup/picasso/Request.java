package com.squareup.picasso;

import android.graphics.Bitmap.Config;
import android.net.Uri;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class Request {
    private static final long TOO_LONG_LOG;
    public final boolean centerCrop;
    public final boolean centerInside;
    public final Config config;
    public final boolean hasRotationPivot;
    int id;
    int networkPolicy;
    public final boolean onlyScaleDown;
    public final int priority$159b5429;
    public final int resourceId;
    public final float rotationDegrees;
    public final float rotationPivotX;
    public final float rotationPivotY;
    public final String stableKey;
    long started;
    public final int targetHeight;
    public final int targetWidth;
    public final List<Transformation> transformations;
    public final Uri uri;

    public static final class Builder {
        boolean centerCrop;
        boolean centerInside;
        Config config;
        boolean hasRotationPivot;
        boolean onlyScaleDown;
        int priority$159b5429;
        int resourceId;
        float rotationDegrees;
        float rotationPivotX;
        float rotationPivotY;
        String stableKey;
        int targetHeight;
        int targetWidth;
        List<Transformation> transformations;
        Uri uri;

        Builder(Uri uri, Config bitmapConfig) {
            this.uri = uri;
            this.resourceId = 0;
            this.config = bitmapConfig;
        }

        final boolean hasImage() {
            return (this.uri == null && this.resourceId == 0) ? false : true;
        }

        public final Builder resize(int targetWidth, int targetHeight) {
            if (targetWidth < 0) {
                throw new IllegalArgumentException("Width must be positive number or 0.");
            } else if (targetHeight < 0) {
                throw new IllegalArgumentException("Height must be positive number or 0.");
            } else if (targetHeight == 0 && targetWidth == 0) {
                throw new IllegalArgumentException("At least one dimension has to be positive number.");
            } else {
                this.targetWidth = targetWidth;
                this.targetHeight = targetHeight;
                return this;
            }
        }
    }

    static {
        TOO_LONG_LOG = TimeUnit.SECONDS.toNanos(5);
    }

    private Request(Uri uri, int resourceId, String stableKey, List<Transformation> transformations, int targetWidth, int targetHeight, boolean centerCrop, boolean centerInside, boolean onlyScaleDown, float rotationDegrees, float rotationPivotX, float rotationPivotY, boolean hasRotationPivot, Config config, int priority) {
        this.uri = uri;
        this.resourceId = resourceId;
        this.stableKey = stableKey;
        if (transformations == null) {
            this.transformations = null;
        } else {
            this.transformations = Collections.unmodifiableList(transformations);
        }
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
        this.centerCrop = centerCrop;
        this.centerInside = centerInside;
        this.onlyScaleDown = onlyScaleDown;
        this.rotationDegrees = rotationDegrees;
        this.rotationPivotX = rotationPivotX;
        this.rotationPivotY = rotationPivotY;
        this.hasRotationPivot = hasRotationPivot;
        this.config = config;
        this.priority$159b5429 = priority;
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder("Request{");
        if (this.resourceId > 0) {
            sb.append(this.resourceId);
        } else {
            sb.append(this.uri);
        }
        if (!(this.transformations == null || this.transformations.isEmpty())) {
            for (Transformation transformation : this.transformations) {
                sb.append(' ').append(transformation.key());
            }
        }
        if (this.stableKey != null) {
            sb.append(" stableKey(").append(this.stableKey).append(')');
        }
        if (this.targetWidth > 0) {
            sb.append(" resize(").append(this.targetWidth).append(',').append(this.targetHeight).append(')');
        }
        if (this.centerCrop) {
            sb.append(" centerCrop");
        }
        if (this.centerInside) {
            sb.append(" centerInside");
        }
        if (this.rotationDegrees != 0.0f) {
            sb.append(" rotation(").append(this.rotationDegrees);
            if (this.hasRotationPivot) {
                sb.append(" @ ").append(this.rotationPivotX).append(',').append(this.rotationPivotY);
            }
            sb.append(')');
        }
        if (this.config != null) {
            sb.append(' ').append(this.config);
        }
        sb.append('}');
        return sb.toString();
    }

    final String logId() {
        long delta = System.nanoTime() - this.started;
        if (delta > TOO_LONG_LOG) {
            return plainId() + '+' + TimeUnit.NANOSECONDS.toSeconds(delta) + 's';
        }
        return plainId() + '+' + TimeUnit.NANOSECONDS.toMillis(delta) + "ms";
    }

    final String plainId() {
        return "[R" + this.id + ']';
    }

    public final boolean hasSize() {
        return (this.targetWidth == 0 && this.targetHeight == 0) ? false : true;
    }

    final boolean needsMatrixTransform() {
        return hasSize() || this.rotationDegrees != 0.0f;
    }

    final boolean hasCustomTransformations() {
        return this.transformations != null;
    }
}

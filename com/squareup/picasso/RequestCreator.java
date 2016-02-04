package com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Picasso.Priority;
import com.squareup.picasso.Request.Builder;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public final class RequestCreator {
    private static final AtomicInteger nextId;
    private final Builder data;
    public boolean deferred;
    private Drawable errorDrawable;
    private int errorResId;
    private int memoryPolicy;
    private int networkPolicy;
    private boolean noFade;
    private final Picasso picasso;
    private Drawable placeholderDrawable;
    private int placeholderResId;
    private boolean setPlaceholder;
    private Object tag;

    static {
        nextId = new AtomicInteger();
    }

    RequestCreator(Picasso picasso, Uri uri) {
        this.setPlaceholder = true;
        if (picasso.shutdown) {
            throw new IllegalStateException("Picasso instance already shut down. Cannot submit new requests.");
        }
        this.picasso = picasso;
        this.data = new Builder(uri, picasso.defaultBitmapConfig);
    }

    RequestCreator() {
        this.setPlaceholder = true;
        this.picasso = null;
        this.data = new Builder(null, null);
    }

    public final RequestCreator tag(Object tag) {
        if (this.tag != null) {
            throw new IllegalStateException("Tag already set.");
        }
        this.tag = tag;
        return this;
    }

    public final RequestCreator resize(int targetWidth, int targetHeight) {
        this.data.resize(targetWidth, targetHeight);
        return this;
    }

    public final RequestCreator centerInside() {
        Builder builder = this.data;
        if (builder.centerCrop) {
            throw new IllegalStateException("Center inside can not be used after calling centerCrop");
        }
        builder.centerInside = true;
        return this;
    }

    public final RequestCreator transform(Transformation transformation) {
        Builder builder = this.data;
        if (transformation == null) {
            throw new IllegalArgumentException("Transformation must not be null.");
        } else if (transformation.key() == null) {
            throw new IllegalArgumentException("Transformation key must not be null.");
        } else {
            if (builder.transformations == null) {
                builder.transformations = new ArrayList(2);
            }
            builder.transformations.add(transformation);
            return this;
        }
    }

    public final RequestCreator memoryPolicy$4b608e66(MemoryPolicy policy) {
        if (policy == null) {
            throw new IllegalArgumentException("Memory policy cannot be null.");
        }
        this.memoryPolicy |= policy.index;
        return this;
    }

    public final RequestCreator networkPolicy$4ded38c(NetworkPolicy policy) {
        if (policy == null) {
            throw new IllegalArgumentException("Network policy cannot be null.");
        }
        this.networkPolicy |= policy.index;
        return this;
    }

    public final void into(Target target) {
        long started = System.nanoTime();
        Utils.checkMain();
        if (this.deferred) {
            throw new IllegalStateException("Fit cannot be used with a Target.");
        } else if (this.data.hasImage()) {
            Request request = createRequest(started);
            String requestKey = Utils.createKey(request);
            if (MemoryPolicy.shouldReadFromMemoryCache(this.memoryPolicy)) {
                Bitmap bitmap = this.picasso.quickMemoryCacheCheck(requestKey);
                if (bitmap != null) {
                    this.picasso.cancelExistingRequest(target);
                    LoadedFrom loadedFrom = LoadedFrom.MEMORY;
                    target.onBitmapLoaded$dc1124d(bitmap);
                    return;
                }
            }
            if (this.setPlaceholder) {
                getPlaceholderDrawable();
            }
            this.picasso.enqueueAndSubmit(new TargetAction(this.picasso, target, request, this.memoryPolicy, this.networkPolicy, this.errorDrawable, requestKey, this.tag, this.errorResId));
        } else {
            this.picasso.cancelExistingRequest(target);
            if (this.setPlaceholder) {
                getPlaceholderDrawable();
            }
        }
    }

    public final void into(ImageView target, Callback callback) {
        long started = System.nanoTime();
        Utils.checkMain();
        if (target == null) {
            throw new IllegalArgumentException("Target must not be null.");
        } else if (this.data.hasImage()) {
            if (this.deferred) {
                Builder builder = this.data;
                Object obj = (builder.targetWidth == 0 && builder.targetHeight == 0) ? null : 1;
                if (obj != null) {
                    throw new IllegalStateException("Fit cannot be used with resize.");
                }
                int width = target.getWidth();
                int height = target.getHeight();
                if (width == 0 || height == 0) {
                    if (this.setPlaceholder) {
                        PicassoDrawable.setPlaceholder(target, getPlaceholderDrawable());
                    }
                    this.picasso.targetToDeferredRequestCreator.put(target, new DeferredRequestCreator(this, target, callback));
                    return;
                }
                this.data.resize(width, height);
            }
            Request request = createRequest(started);
            String requestKey = Utils.createKey(request);
            if (MemoryPolicy.shouldReadFromMemoryCache(this.memoryPolicy)) {
                Bitmap bitmap = this.picasso.quickMemoryCacheCheck(requestKey);
                if (bitmap != null) {
                    this.picasso.cancelExistingRequest(target);
                    PicassoDrawable.setBitmap(target, this.picasso.context, bitmap, LoadedFrom.MEMORY, this.noFade, this.picasso.indicatorsEnabled);
                    if (this.picasso.loggingEnabled) {
                        Utils.log("Main", "completed", request.plainId(), "from " + LoadedFrom.MEMORY);
                    }
                    if (callback != null) {
                        callback.onSuccess();
                        return;
                    }
                    return;
                }
            }
            if (this.setPlaceholder) {
                PicassoDrawable.setPlaceholder(target, getPlaceholderDrawable());
            }
            this.picasso.enqueueAndSubmit(new ImageViewAction(this.picasso, target, request, this.memoryPolicy, this.networkPolicy, this.errorResId, this.errorDrawable, requestKey, this.tag, callback, this.noFade));
        } else {
            this.picasso.cancelExistingRequest(target);
            if (this.setPlaceholder) {
                PicassoDrawable.setPlaceholder(target, getPlaceholderDrawable());
            }
        }
    }

    private Drawable getPlaceholderDrawable() {
        if (this.placeholderResId != 0) {
            return this.picasso.context.getResources().getDrawable(this.placeholderResId);
        }
        return this.placeholderDrawable;
    }

    private Request createRequest(long started) {
        int id = nextId.getAndIncrement();
        Builder builder = this.data;
        if (builder.centerInside && builder.centerCrop) {
            throw new IllegalStateException("Center crop and center inside can not be used together.");
        } else if (builder.centerCrop && builder.targetWidth == 0 && builder.targetHeight == 0) {
            throw new IllegalStateException("Center crop requires calling resize with positive width and height.");
        } else if (builder.centerInside && builder.targetWidth == 0 && builder.targetHeight == 0) {
            throw new IllegalStateException("Center inside requires calling resize with positive width and height.");
        } else {
            if (builder.priority$159b5429 == 0) {
                builder.priority$159b5429 = Priority.NORMAL$159b5429;
            }
            Request request = new Request(builder.resourceId, builder.stableKey, builder.transformations, builder.targetWidth, builder.targetHeight, builder.centerCrop, builder.centerInside, builder.onlyScaleDown, builder.rotationDegrees, builder.rotationPivotX, builder.rotationPivotY, builder.hasRotationPivot, builder.config, builder.priority$159b5429, (byte) 0);
            request.id = id;
            request.started = started;
            boolean loggingEnabled = this.picasso.loggingEnabled;
            if (loggingEnabled) {
                Utils.log("Main", "created", request.plainId(), request.toString());
            }
            Picasso picasso = this.picasso;
            Request transformed = picasso.requestTransformer.transformRequest(request);
            if (transformed == null) {
                throw new IllegalStateException("Request transformer " + picasso.requestTransformer.getClass().getCanonicalName() + " returned null for " + request);
            }
            if (transformed != request) {
                transformed.id = id;
                transformed.started = started;
                if (loggingEnabled) {
                    Utils.log("Main", "changed", transformed.logId(), "into " + transformed);
                }
            }
            return transformed;
        }
    }
}

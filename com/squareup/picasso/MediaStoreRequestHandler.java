package com.squareup.picasso;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video.Thumbnails;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.RequestHandler.Result;
import java.io.IOException;

final class MediaStoreRequestHandler extends ContentStreamRequestHandler {
    private static final String[] CONTENT_ORIENTATION;

    enum PicassoKind {
        MICRO(3, 96, 96),
        MINI(1, AccessibilityNodeInfoCompat.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY, 384),
        FULL(2, -1, -1);
        
        final int androidKind;
        final int height;
        final int width;

        private PicassoKind(int androidKind, int width, int height) {
            this.androidKind = androidKind;
            this.width = width;
            this.height = height;
        }
    }

    private static int getExifOrientation(android.content.ContentResolver r8, android.net.Uri r9) {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x002d in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:58)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
        /*
        r7 = 0;
        r6 = 0;
        r2 = CONTENT_ORIENTATION;	 Catch:{ RuntimeException -> 0x0027, all -> 0x002f }
        r3 = 0;	 Catch:{ RuntimeException -> 0x0027, all -> 0x002f }
        r4 = 0;	 Catch:{ RuntimeException -> 0x0027, all -> 0x002f }
        r5 = 0;	 Catch:{ RuntimeException -> 0x0027, all -> 0x002f }
        r0 = r8;	 Catch:{ RuntimeException -> 0x0027, all -> 0x002f }
        r1 = r9;	 Catch:{ RuntimeException -> 0x0027, all -> 0x002f }
        r6 = r0.query(r1, r2, r3, r4, r5);	 Catch:{ RuntimeException -> 0x0027, all -> 0x002f }
        if (r6 == 0) goto L_0x0015;	 Catch:{ RuntimeException -> 0x0027, all -> 0x002f }
    L_0x000f:
        r0 = r6.moveToFirst();	 Catch:{ RuntimeException -> 0x0027, all -> 0x002f }
        if (r0 != 0) goto L_0x001c;
    L_0x0015:
        if (r6 == 0) goto L_0x001a;
    L_0x0017:
        r6.close();
    L_0x001a:
        r0 = r7;
    L_0x001b:
        return r0;
    L_0x001c:
        r0 = 0;
        r0 = r6.getInt(r0);	 Catch:{ RuntimeException -> 0x0027, all -> 0x002f }
        if (r6 == 0) goto L_0x001b;
    L_0x0023:
        r6.close();
        goto L_0x001b;
    L_0x0027:
        r0 = move-exception;
        if (r6 == 0) goto L_0x002d;
    L_0x002a:
        r6.close();
    L_0x002d:
        r0 = r7;
        goto L_0x001b;
    L_0x002f:
        r0 = move-exception;
        if (r6 == 0) goto L_0x0035;
    L_0x0032:
        r6.close();
    L_0x0035:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.picasso.MediaStoreRequestHandler.getExifOrientation(android.content.ContentResolver, android.net.Uri):int");
    }

    static {
        CONTENT_ORIENTATION = new String[]{"orientation"};
    }

    MediaStoreRequestHandler(Context context) {
        super(context);
    }

    public final boolean canHandleRequest(Request data) {
        Uri uri = data.uri;
        return "content".equals(uri.getScheme()) && "media".equals(uri.getAuthority());
    }

    public final Result load$71fa0c91(Request request) throws IOException {
        ContentResolver contentResolver = this.context.getContentResolver();
        int exifOrientation = getExifOrientation(contentResolver, request.uri);
        String mimeType = contentResolver.getType(request.uri);
        boolean isVideo = mimeType != null && mimeType.startsWith("video/");
        if (request.hasSize()) {
            int i = request.targetWidth;
            int i2 = request.targetHeight;
            PicassoKind picassoKind = (i > PicassoKind.MICRO.width || i2 > PicassoKind.MICRO.height) ? (i > PicassoKind.MINI.width || i2 > PicassoKind.MINI.height) ? PicassoKind.FULL : PicassoKind.MINI : PicassoKind.MICRO;
            if (!isVideo && picassoKind == PicassoKind.FULL) {
                return new Result(null, getInputStream(request), LoadedFrom.DISK, exifOrientation);
            }
            Bitmap bitmap;
            long id = ContentUris.parseId(request.uri);
            Options options = RequestHandler.createBitmapOptions(request);
            options.inJustDecodeBounds = true;
            RequestHandler.calculateInSampleSize(request.targetWidth, request.targetHeight, picassoKind.width, picassoKind.height, options, request);
            if (isVideo) {
                bitmap = Thumbnails.getThumbnail(contentResolver, id, picassoKind == PicassoKind.FULL ? 1 : picassoKind.androidKind, options);
            } else {
                bitmap = Images.Thumbnails.getThumbnail(contentResolver, id, picassoKind.androidKind, options);
            }
            if (bitmap != null) {
                return new Result(bitmap, null, LoadedFrom.DISK, exifOrientation);
            }
        }
        return new Result(null, getInputStream(request), LoadedFrom.DISK, exifOrientation);
    }
}

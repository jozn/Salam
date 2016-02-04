package com.squareup.picasso;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.RequestHandler.Result;
import java.io.IOException;

final class AssetRequestHandler extends RequestHandler {
    private static final int ASSET_PREFIX_LENGTH;
    private final AssetManager assetManager;

    static {
        ASSET_PREFIX_LENGTH = 22;
    }

    public AssetRequestHandler(Context context) {
        this.assetManager = context.getAssets();
    }

    public final boolean canHandleRequest(Request data) {
        Uri uri = data.uri;
        if ("file".equals(uri.getScheme()) && !uri.getPathSegments().isEmpty() && "android_asset".equals(uri.getPathSegments().get(0))) {
            return true;
        }
        return false;
    }

    public final Result load$71fa0c91(Request request) throws IOException {
        return new Result(this.assetManager.open(request.uri.toString().substring(ASSET_PREFIX_LENGTH)), LoadedFrom.DISK);
    }
}

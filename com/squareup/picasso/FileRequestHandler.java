package com.squareup.picasso;

import android.content.Context;
import android.media.ExifInterface;
import com.kyleduo.switchbutton.C0473R;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.RequestHandler.Result;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.paho.client.mqttv3.logging.Logger;

final class FileRequestHandler extends ContentStreamRequestHandler {
    FileRequestHandler(Context context) {
        super(context);
    }

    public final boolean canHandleRequest(Request data) {
        return "file".equals(data.uri.getScheme());
    }

    public final Result load$71fa0c91(Request request) throws IOException {
        int i;
        InputStream inputStream = getInputStream(request);
        LoadedFrom loadedFrom = LoadedFrom.DISK;
        switch (new ExifInterface(request.uri.getPath()).getAttributeInt("Orientation", 1)) {
            case Logger.INFO /*3*/:
                i = 180;
                break;
            case Logger.FINER /*6*/:
                i = 90;
                break;
            case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                i = 270;
                break;
            default:
                i = 0;
                break;
        }
        return new Result(null, inputStream, loadedFrom, i);
    }
}

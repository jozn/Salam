package com.shamchat.moments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.shamchat.utils.URIExtractor;
import java.io.File;
import java.util.Date;
import java.util.List;

public class MomentPhotoFragment extends Fragment {
    private static final int REQUEST_CAMERA = 2;
    private static final int REQUEST_GALLERY = 1;
    private MomentComposerActivity baseActivity;
    private String fullFilePath;
    private HorizontalScrollView horizontalScrollView;
    private ImageLoader imageLoader;
    private DisplayImageOptions mediaImageOptions;
    private LinearLayout multipleImageContainer;

    /* renamed from: com.shamchat.moments.MomentPhotoFragment.1 */
    class C11301 implements OnClickListener {
        C11301() {
        }

        public final void onClick(View v) {
            Intent camIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            camIntent.putExtra("output", MomentPhotoFragment.this.setImageUri());
            MomentPhotoFragment.this.startActivityForResult(camIntent, MomentPhotoFragment.REQUEST_CAMERA);
        }
    }

    /* renamed from: com.shamchat.moments.MomentPhotoFragment.2 */
    class C11312 implements OnClickListener {
        C11312() {
        }

        public final void onClick(View v) {
            MomentPhotoFragment.this.startActivityForResult(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), MomentPhotoFragment.REQUEST_GALLERY);
        }
    }

    /* renamed from: com.shamchat.moments.MomentPhotoFragment.3 */
    class C11323 implements Runnable {
        C11323() {
        }

        public final void run() {
            MomentPhotoFragment.this.horizontalScrollView.fullScroll(66);
        }
    }

    public MomentPhotoFragment() {
        this.fullFilePath = null;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(2130903184, container, false);
        ((Button) view.findViewById(2131362378)).setOnClickListener(new C11301());
        this.horizontalScrollView = (HorizontalScrollView) view.findViewById(2131362301);
        ((Button) view.findViewById(2131362379)).setOnClickListener(new C11312());
        this.multipleImageContainer = (LinearLayout) view.findViewById(2131362302);
        this.imageLoader = ImageLoader.getInstance();
        Builder builder = new Builder();
        builder.imageResOnLoading = 2130838116;
        builder.imageResForEmptyUri = 2130837992;
        builder.imageResOnFail = 2130837992;
        builder.cacheInMemory = true;
        builder.cacheOnDisc = false;
        builder.imageScaleType$641b8ab2 = ImageScaleType.EXACTLY_STRETCHED$641b8ab2;
        this.mediaImageOptions = builder.bitmapConfig(Config.ALPHA_8).build();
        this.baseActivity = (MomentComposerActivity) getActivity();
        List<String> tempList = this.baseActivity.getPhotoFileUrls();
        if (tempList.size() > 0) {
            for (String url : tempList) {
                this.fullFilePath = url;
                addImageToView();
            }
        }
        return view;
    }

    public Uri setImageUri() {
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.fullFilePath = file.getAbsolutePath();
        return imgUri;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                case REQUEST_GALLERY /*1*/:
                    this.fullFilePath = getRealPathFromURI(data.getData());
                    this.baseActivity.getPhotoFileUrls().add(this.fullFilePath);
                    this.baseActivity.btCamera.setImageResource(2130838009);
                    addImageToView();
                case REQUEST_CAMERA /*2*/:
                    if (this.fullFilePath == null && data.getData() != null) {
                        this.fullFilePath = URIExtractor.getPath(getActivity(), data.getData());
                    }
                    this.baseActivity.getPhotoFileUrls().add(this.fullFilePath);
                    this.baseActivity.btCamera.setImageResource(2130838009);
                    addImageToView();
                default:
            }
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = new String[REQUEST_GALLERY];
        projection[0] = "_data";
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void addImageToView() {
        ImageView imageView = new ImageView(getActivity());
        this.imageLoader.displayImage("file://" + this.fullFilePath, imageView, this.mediaImageOptions);
        LayoutParams params = new LayoutParams(250, 250);
        params.setMargins(5, REQUEST_GALLERY, 5, REQUEST_GALLERY);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ScaleType.FIT_XY);
        this.multipleImageContainer.addView(imageView);
        this.horizontalScrollView.postDelayed(new C11323(), 200);
    }
}

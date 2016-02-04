package com.shamchat.moments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.shamchat.utils.URIExtractor;
import java.io.File;
import java.util.Date;

public class MomentVideoFragment extends Fragment {
    private static final int REQUEST_CAMERA = 2;
    private static final int REQUEST_GALLERY = 1;
    private MomentComposerActivity baseActivity;
    private String fullFilePath;
    private ImageView imageContainer;

    /* renamed from: com.shamchat.moments.MomentVideoFragment.1 */
    class C11461 implements OnClickListener {
        C11461() {
        }

        public final void onClick(View v) {
            if (MomentVideoFragment.this.fullFilePath == null) {
                Intent intent = new Intent("android.media.action.VIDEO_CAPTURE");
                intent.putExtra("android.intent.extra.videoQuality", 0);
                MomentVideoFragment.this.startActivityForResult(intent, MomentVideoFragment.REQUEST_CAMERA);
                return;
            }
            Toast.makeText(MomentVideoFragment.this.getActivity(), "Only one video per post is allowed", 0).show();
        }
    }

    /* renamed from: com.shamchat.moments.MomentVideoFragment.2 */
    class C11472 implements OnClickListener {
        C11472() {
        }

        public final void onClick(View v) {
            if (MomentVideoFragment.this.fullFilePath == null) {
                Intent videoIntent = new Intent("android.intent.action.PICK", null);
                videoIntent.setType("video/*");
                videoIntent.putExtra("output", MomentVideoFragment.this.setVideoUri());
                MomentVideoFragment.this.startActivityForResult(videoIntent, MomentVideoFragment.REQUEST_GALLERY);
                return;
            }
            Toast.makeText(MomentVideoFragment.this.getActivity(), "Only one video per post is allowed", 0).show();
        }
    }

    public MomentVideoFragment() {
        this.fullFilePath = null;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(2130903188, container, false);
        ((Button) view.findViewById(2131362378)).setOnClickListener(new C11461());
        ((Button) view.findViewById(2131362379)).setOnClickListener(new C11472());
        this.imageContainer = (ImageView) view.findViewById(2131362386);
        this.baseActivity = (MomentComposerActivity) getActivity();
        String videoUrl = this.baseActivity.getVideoUrl();
        if (videoUrl.length() > REQUEST_GALLERY) {
            this.fullFilePath = videoUrl;
            addImageToView(ThumbnailUtils.extractThumbnail(ThumbnailUtils.createVideoThumbnail(this.fullFilePath, REQUEST_GALLERY), 100, 100));
        }
        return view;
    }

    public Uri setVideoUri() {
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "video" + new Date().getTime() + ".mp4");
        Uri imgUri = Uri.fromFile(file);
        this.fullFilePath = file.getAbsolutePath();
        return imgUri;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                case REQUEST_GALLERY /*1*/:
                    this.fullFilePath = URIExtractor.getPath(getActivity(), data.getData());
                    this.baseActivity.setVideoUrl(this.fullFilePath);
                    addImageToView(ThumbnailUtils.extractThumbnail(ThumbnailUtils.createVideoThumbnail(this.fullFilePath, REQUEST_GALLERY), 100, 100));
                case REQUEST_CAMERA /*2*/:
                    if (this.fullFilePath == null && data.getData() != null) {
                        this.fullFilePath = URIExtractor.getPath(getActivity(), data.getData());
                    }
                    this.baseActivity.setVideoUrl(this.fullFilePath);
                    addImageToView(ThumbnailUtils.extractThumbnail(ThumbnailUtils.createVideoThumbnail(this.fullFilePath, REQUEST_GALLERY), 100, 100));
                default:
            }
        }
    }

    private void addImageToView(Bitmap bmThumbnail) {
        this.imageContainer.setImageBitmap(bmThumbnail);
    }
}

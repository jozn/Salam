package com.shamchat.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.gallery.ZoomableImageView;
import com.shamchat.utils.Utils;
import java.io.File;

public class ImagePreviewAndCommentDialog extends AppCompatActivity {
    private EditText editDesc;
    private String fullFilePath;
    private ZoomableImageView imgPreview;

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(2130903088);
        this.editDesc = (EditText) findViewById(2131362021);
        initializeActionBar();
        try {
            loadData();
        } catch (Exception e) {
            System.out.println("Image preview " + e);
        }
    }

    private void initializeActionBar() {
        getSupportActionBar().setDisplayOptions(31);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(2131623945, menu);
        return true;
    }

    private void loadData() throws Exception {
        this.fullFilePath = getIntent().getStringExtra("fullFilePath");
        System.out.println("Uri full file://" + this.fullFilePath);
        if (new File(this.fullFilePath).exists()) {
            displayImage(this.fullFilePath);
        } else {
            System.out.println("file doesnt exist");
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                setResult(0);
                finish();
                return true;
            case 2131362566:
                Intent data = new Intent();
                data.putExtra("description", this.editDesc.getText().toString());
                data.putExtra("fullFilePath", this.fullFilePath);
                data.putExtra("type", 1);
                setResult(-1, data);
                finish();
                return true;
            default:
                return false;
        }
    }

    public boolean displayImage(String localFilePath) {
        this.imgPreview = (ZoomableImageView) findViewById(2131362019);
        if (localFilePath == null || localFilePath.length() <= 0) {
            Log.e("DownloadedImageFilePreview", "localFilePath is empty - displayImage");
            return false;
        }
        Bitmap bitmap;
        File path = new File(localFilePath);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) SHAMChatApplication.getMyApplicationContext().getSystemService("window")).getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        int screenWidth = displaymetrics.widthPixels;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path.getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        if (screenWidth > imageWidth) {
            bitmap = Utils.decodeSampledBitmapFromFile(path.getAbsolutePath(), imageWidth, imageHeight);
        } else {
            bitmap = Utils.decodeSampledBitmapFromFile(path.getAbsolutePath(), screenWidth, screenHeight);
        }
        if (bitmap != null) {
            this.imgPreview.setImageBitmap(bitmap);
        }
        return true;
    }

    protected void onDestroy() {
        super.onDestroy();
        View v = findViewById(2131362120);
        if (v != null) {
            unbindDrawables(v);
        }
        this.imgPreview = null;
        System.gc();
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if ((view instanceof ViewGroup) && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
}

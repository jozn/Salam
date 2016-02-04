package com.rokhgroup.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.FileUtils;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.rokhgroup.utils.RokhPref;
import com.rokhgroup.utils.RokhgroupRestClient;
import com.shamchat.activity.ProgressBarDialogLogin;
import com.shamchat.activity.ProgressBarLoadingDialog;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.roundedimage.RoundedDrawable;
import com.shamchat.roundedimage.RoundedImageView;
import com.shamchat.utils.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;
import cz.msebera.android.httpclient.Header;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public class EditProfile extends AppCompatActivity implements ImageChooserListener {
    static String CURRENT_USER_ID;
    static String CURRENT_USER_TOKEN;
    private static boolean Status;
    static String USER_ID;
    static String User_Avatar;
    static String User_Name;
    private String Path;
    private int RESULT_OK;
    RokhPref Session;
    public RoundedImageView avatar;
    private int chooserType;
    private String filePath;
    private ChosenImage imageChoosed;
    private ImageChooserManager imageChooserManager;
    Intent intent;
    private Button mBtnCamera;
    private Button mBtnGallery;
    private Button mBtnUpdate;
    Context mContext;
    private LinearLayout mLoading;
    private EditText mName;
    private final Transformation mTransformation;
    Uri uri;

    /* renamed from: com.rokhgroup.activities.EditProfile.1 */
    class C05031 implements OnClickListener {
        C05031() {
        }

        public final void onClick(View v) {
            EditProfile.this.takePicture();
        }
    }

    /* renamed from: com.rokhgroup.activities.EditProfile.2 */
    class C05042 implements OnClickListener {
        C05042() {
        }

        public final void onClick(View arg0) {
            EditProfile.this.chooseImage();
        }
    }

    /* renamed from: com.rokhgroup.activities.EditProfile.3 */
    class C05053 implements OnClickListener {
        C05053() {
        }

        public final void onClick(View arg0) {
            EditProfile.this.UpdateProfile();
        }
    }

    /* renamed from: com.rokhgroup.activities.EditProfile.4 */
    class C05064 implements Runnable {
        final /* synthetic */ ChosenImage val$image;

        C05064(ChosenImage chosenImage) {
            this.val$image = chosenImage;
        }

        public final void run() {
            EditProfile.this.mLoading.setVisibility(8);
            if (this.val$image != null) {
                EditProfile.this.avatar.setImageURI(Uri.parse(new File(this.val$image.fileThumbnailSmall).toString()));
                EditProfile.this.Path = this.val$image.filePathOriginal.toString();
                EditProfile.this.imageChoosed = this.val$image;
                EditProfile.this.mBtnUpdate.setEnabled(true);
            }
        }
    }

    /* renamed from: com.rokhgroup.activities.EditProfile.5 */
    class C05075 implements Runnable {
        C05075() {
        }

        public final void run() {
            EditProfile.this.mLoading.setVisibility(8);
            Toast.makeText(EditProfile.this.mContext, SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493313), 1).show();
        }
    }

    /* renamed from: com.rokhgroup.activities.EditProfile.6 */
    class C05116 implements Callback {

        /* renamed from: com.rokhgroup.activities.EditProfile.6.1 */
        class C05081 implements Runnable {
            C05081() {
            }

            public final void run() {
                try {
                    ProgressBarDialogLogin.getInstance().dismiss();
                } catch (Exception e) {
                }
                EditProfile.this.mBtnUpdate.setEnabled(true);
            }
        }

        /* renamed from: com.rokhgroup.activities.EditProfile.6.2 */
        class C05092 implements Runnable {
            final /* synthetic */ String val$stringResponse;

            C05092(String str) {
                this.val$stringResponse = str;
            }

            public final void run() {
                try {
                    if (this.val$stringResponse.equals("profile saved")) {
                        try {
                            ProgressBarDialogLogin.getInstance().dismiss();
                        } catch (Exception e) {
                        }
                        EditProfile.this.finish();
                    }
                } catch (Exception e2) {
                    try {
                        ProgressBarDialogLogin.getInstance().dismiss();
                    } catch (Exception e3) {
                    }
                    EditProfile.this.mBtnUpdate.setEnabled(true);
                }
            }
        }

        /* renamed from: com.rokhgroup.activities.EditProfile.6.3 */
        class C05103 implements Runnable {
            C05103() {
            }

            public final void run() {
                try {
                    ProgressBarDialogLogin.getInstance().dismiss();
                } catch (Exception e) {
                }
                EditProfile.this.mBtnUpdate.setEnabled(true);
            }
        }

        C05116() {
        }

        public final void onFailure(Request request, IOException e) {
            EditProfile.this.runOnUiThread(new C05081());
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String stringResponse = response.body().string();
                    response.body().close();
                    System.out.println("***** UpdateProfile Response:" + stringResponse);
                    EditProfile.this.runOnUiThread(new C05092(stringResponse));
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                EditProfile.this.runOnUiThread(new C05103());
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.rokhgroup.activities.EditProfile.7 */
    class C05127 extends TextHttpResponseHandler {
        C05127() {
        }

        public final void onSuccess$79de7b53(String response) {
            if (response.equals("profile saved")) {
                try {
                    ProgressBarDialogLogin.getInstance().dismiss();
                } catch (Exception e) {
                }
                EditProfile.this.finish();
            }
        }

        public final void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
            try {
                ProgressBarDialogLogin.getInstance().dismiss();
            } catch (Exception e) {
            }
        }
    }

    /* renamed from: com.rokhgroup.activities.EditProfile.8 */
    class C05138 implements Transformation {
        final boolean oval;
        final float radius;

        C05138() {
            this.radius = TypedValue.applyDimension(1, 200.0f, SHAMChatApplication.getMyApplicationContext().getResources().getDisplayMetrics());
            this.oval = false;
        }

        public final Bitmap transform(Bitmap bitmap) {
            Drawable fromBitmap = RoundedDrawable.fromBitmap(bitmap);
            fromBitmap.mCornerRadius = this.radius;
            fromBitmap.mOval = false;
            Bitmap transformed = RoundedDrawable.drawableToBitmap(fromBitmap);
            if (!bitmap.equals(transformed)) {
                bitmap.recycle();
            }
            return transformed;
        }

        public final String key() {
            return "rounded_radius_" + this.radius + "_oval_false";
        }
    }

    public EditProfile() {
        this.Path = BuildConfig.VERSION_NAME;
        this.RESULT_OK = -1;
        this.mTransformation = new C05138();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903211);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(12);
        actionBar.setCustomView(2130903211);
        actionBar.setTitle(UserProfile.user_titel.toString());
        this.mContext = this;
        this.uri = getIntent().getData();
        if (this.uri == null) {
            this.intent = getIntent();
            User_Avatar = this.intent.getStringExtra("USER_AVATAR");
            User_Name = this.intent.getStringExtra("USER_NAME");
            USER_ID = this.intent.getStringExtra("USER_ID");
        }
        this.Session = new RokhPref(this);
        CURRENT_USER_ID = this.Session.getUSERID();
        CURRENT_USER_TOKEN = this.Session.getUSERTOKEN();
        this.mLoading = (LinearLayout) findViewById(2131362439);
        this.mLoading.setVisibility(8);
        this.mName = (EditText) findViewById(2131362442);
        this.mName.setText(User_Name);
        this.mBtnCamera = (Button) findViewById(2131362440);
        this.mBtnCamera.setOnClickListener(new C05031());
        this.mBtnGallery = (Button) findViewById(2131362441);
        this.mBtnGallery.setOnClickListener(new C05042());
        this.mBtnUpdate = (Button) findViewById(2131362443);
        this.mBtnUpdate.setOnClickListener(new C05053());
        this.avatar = (RoundedImageView) findViewById(2131362438);
        RequestCreator load = Picasso.with(this.mContext).load(User_Avatar);
        load.deferred = true;
        load.centerInside().transform(this.mTransformation).into(this.avatar, null);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                break;
        }
        return false;
    }

    private void takePicture() {
        this.chooserType = 294;
        this.imageChooserManager = new ImageChooserManager(this, 294, "Salam/Avatars");
        this.imageChooserManager.listener = this;
        try {
            this.mLoading.setVisibility(0);
            this.filePath = this.imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void chooseImage() {
        this.chooserType = 291;
        this.imageChooserManager = new ImageChooserManager(this, 291, "Salam/Avatars");
        this.imageChooserManager.listener = this;
        try {
            this.mLoading.setVisibility(0);
            this.filePath = this.imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == this.RESULT_OK && (requestCode == 291 || requestCode == 294)) {
            if (this.imageChooserManager == null) {
                reinitializeImageChooser();
            }
            this.imageChooserManager.submit(requestCode, data);
            return;
        }
        this.mLoading.setVisibility(8);
    }

    public void onImageChosen(ChosenImage image) {
        runOnUiThread(new C05064(image));
    }

    private void reinitializeImageChooser() {
        this.imageChooserManager = new ImageChooserManager(this, this.chooserType, "Parsvid/Avatars");
        this.imageChooserManager.listener = this;
        this.imageChooserManager.reinitialize(this.filePath);
    }

    public void onError(String reason) {
        runOnUiThread(new C05075());
    }

    private void UpdateProfile2() {
        RequestBody requestBody;
        this.mBtnUpdate.setEnabled(false);
        try {
            ProgressBarLoadingDialog.getInstance().show(getSupportFragmentManager(), BuildConfig.VERSION_NAME);
        } catch (Exception e) {
        }
        String URL = "http://social.rabtcdn.com/profile/d_change/?token=" + CURRENT_USER_TOKEN;
        String upName = this.mName.getText().toString();
        OkHttpClient client = new OkHttpClient();
        if (this.Path == null || this.imageChoosed == null) {
            requestBody = new MultipartBuilder().type(MultipartBuilder.FORM).addFormDataPart("name", upName).addFormDataPart("jens", "M").build();
        } else {
            String fileExt = FileUtils.getFileExtension(this.Path);
            String fileName = this.Path.substring(this.Path.lastIndexOf(MqttTopic.TOPIC_LEVEL_SEPARATOR) + 1);
            Utils.getMimeType(this.Path);
            if (fileExt.equals(fileName)) {
                new StringBuilder().append(fileName).append(".jpg");
            }
            requestBody = new MultipartBuilder().type(MultipartBuilder.FORM).addFormDataPart("name", upName).addFormDataPart("jens", "M").build();
        }
        client.newCall(new Builder().url(URL).post(requestBody).build()).enqueue(new C05116());
    }

    private void UpdateProfile() {
        this.mBtnUpdate.setEnabled(false);
        try {
            ProgressBarLoadingDialog.getInstance().show(getSupportFragmentManager(), BuildConfig.VERSION_NAME);
        } catch (Exception e) {
        }
        String URL = "http://social.rabtcdn.com/profile/d_change/?token=" + CURRENT_USER_TOKEN;
        String upName = this.mName.getText().toString();
        RequestParams params = new RequestParams();
        try {
            params.put("name", upName);
            params.put("jens", "M");
            params.put("avatar", new File(this.Path));
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        RokhgroupRestClient.post(URL, params, new C05127());
    }
}

package com.shamchat.activity;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.shamchat.adapters.IconListDialogAdapter;
import com.shamchat.adapters.IconListDialogAdapter.ListRow;
import com.shamchat.adapters.ListMessagesAdapter;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.models.Message;
import com.shamchat.models.Message.MessageType;
import com.shamchat.utils.Utils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FavoriteMessagesActivity extends AppCompatActivity {
    public static final int REQUEST_IMAGE_CAPTURE = 26;
    public static final int REQUEST_NEW_IMAGES = 25;
    public static final int REQUEST_NEW_TEXT = 24;
    protected static final String TAG = "FavoriteMessagesActivity";
    private ListMessagesAdapter adapter;
    private ChatProviderNew chatProvider;
    private ListView listMessages;
    private File photoFile;
    private ProgressDialog progressDialog;
    private String userId;

    /* renamed from: com.shamchat.activity.FavoriteMessagesActivity.1 */
    class C07511 implements OnItemClickListener {
        C07511() {
        }

        public final void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Message message = (Message) FavoriteMessagesActivity.this.adapter.list.get(position);
            Intent intent = new Intent(view.getContext(), MessageDetailsActivity.class);
            intent.putExtra(MessageDetailsActivity.EXTRA_MESSAGE_ID, message.messageId);
            FavoriteMessagesActivity.this.startActivity(intent);
        }
    }

    /* renamed from: com.shamchat.activity.FavoriteMessagesActivity.2 */
    class C07522 implements OnClickListener {
        C07522() {
        }

        public final void onClick(DialogInterface dialog, int which) {
            if (which == 0) {
                FavoriteMessagesActivity.this.startActivityForResult(new Intent(FavoriteMessagesActivity.this, AddFavoriteTextActivity.class), FavoriteMessagesActivity.REQUEST_NEW_TEXT);
                return;
            }
            if (FavoriteMessagesActivity.this.getPackageManager().hasSystemFeature("android.hardware.camera")) {
                if ((new Intent("android.media.action.IMAGE_CAPTURE").resolveActivity(FavoriteMessagesActivity.this.getPackageManager()) != null ? 1 : null) != null) {
                    FavoriteMessagesActivity.this.showAddImageDialog();
                    return;
                }
            }
            FavoriteMessagesActivity.this.startChoosePhotoActivity();
        }
    }

    /* renamed from: com.shamchat.activity.FavoriteMessagesActivity.3 */
    class C07533 implements OnClickListener {
        C07533() {
        }

        public final void onClick(DialogInterface dialog, int which) {
            if (which == 0) {
                Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                try {
                    FavoriteMessagesActivity favoriteMessagesActivity = FavoriteMessagesActivity.this;
                    Context applicationContext = FavoriteMessagesActivity.this.getApplicationContext();
                    favoriteMessagesActivity.photoFile = File.createTempFile("JPEG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_", ".jpg", applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                } catch (IOException ex) {
                    Log.e(FavoriteMessagesActivity.TAG, "Error creating temporal file for image:" + ex);
                }
                if (takePictureIntent.resolveActivity(FavoriteMessagesActivity.this.getPackageManager()) != null && FavoriteMessagesActivity.this.photoFile != null) {
                    takePictureIntent.putExtra("output", Uri.fromFile(FavoriteMessagesActivity.this.photoFile));
                    FavoriteMessagesActivity.this.startActivityForResult(takePictureIntent, FavoriteMessagesActivity.REQUEST_IMAGE_CAPTURE);
                    return;
                }
                return;
            }
            FavoriteMessagesActivity.this.startChoosePhotoActivity();
        }
    }

    private class LoadImages extends AsyncTask<String, Integer, Void> {
        private LoadImages() {
        }

        protected final /* bridge */ /* synthetic */ void onPostExecute(Object obj) {
            FavoriteMessagesActivity.this.refreshInfo();
            FavoriteMessagesActivity.this.hideProgressDialog();
        }

        protected final void onPreExecute() {
            super.onPreExecute();
            FavoriteMessagesActivity.this.showProgressDialog();
        }

        private Void doInBackground(String... params) {
            try {
                for (String path : params) {
                    new Options().inSampleSize = 3;
                    Bitmap bitmap = Utils.scaleDownImageSize$1c855778(Utils.decodeSampledBitmapFromResource$295fb07d(path), Utils.getExifRotation(path));
                    Message msg = new Message();
                    FavoriteMessagesActivity.this.chatProvider;
                    msg.assignUniqueId(ChatProviderNew.geLasttFavoriteMessageId());
                    msg.time = System.currentTimeMillis();
                    msg.messageContent = Utils.encodeImage(bitmap);
                    msg.type = MessageType.IMAGE;
                    msg.userId = FavoriteMessagesActivity.this.userId;
                    FavoriteMessagesActivity.this.chatProvider;
                    ChatProviderNew.insertFavorite(msg);
                }
            } catch (OutOfMemoryError e) {
                Toast.makeText(FavoriteMessagesActivity.this, 2131493246, 0).show();
            }
            return null;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903085);
        initializeActionBar();
        this.listMessages = (ListView) findViewById(2131362014);
        this.adapter = new ListMessagesAdapter(this);
        this.userId = SHAMChatApplication.getConfig().userId;
        this.chatProvider = new ChatProviderNew();
        this.listMessages.setAdapter(this.adapter);
        this.listMessages.setOnItemClickListener(new C07511());
    }

    private void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(31);
        actionBar.setCustomView(2130903043);
        ((TextView) findViewById(2131361839)).setText(getResources().getString(2131493128));
    }

    protected void onResume() {
        super.onResume();
        this.adapter.refreshMessagesFromDB$7c629dcd(this.userId);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(2131623942, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                return true;
            case 2131362563:
                showAddToFavoriteDialog();
                return true;
            default:
                return false;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult. Request:" + requestCode + " Result:" + resultCode);
        if (requestCode == REQUEST_NEW_TEXT) {
            if (resultCode == -1) {
                String text = data.getStringExtra(AddFavoriteTextActivity.EXTRA_RESULT_TEXT);
                Message msg = new Message();
                msg.assignUniqueId(ChatProviderNew.geLasttFavoriteMessageId());
                msg.time = System.currentTimeMillis();
                msg.messageContent = text;
                msg.type = MessageType.TEXT;
                msg.userId = this.userId;
                ChatProviderNew.insertFavorite(msg);
                refreshInfo();
            }
        } else if (requestCode == REQUEST_NEW_IMAGES) {
            if (resultCode == -1) {
                String fullFilePath = getRealPathFromURI(data.getData());
                new LoadImages().execute(new String[]{fullFilePath});
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == -1 && this.photoFile != null) {
            new LoadImages().execute(new String[]{this.photoFile.getAbsolutePath()});
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        String colIndex = cursor.getString(column_index);
        cursor.close();
        return colIndex;
    }

    private void showAddToFavoriteDialog() {
        Builder builder = new Builder(this);
        IconListDialogAdapter adap = new IconListDialogAdapter(this);
        adap.list.add(new ListRow(2130837865, 2131493409));
        adap.list.add(new ListRow(2130837864, 2131493168));
        builder.setTitle(2131492972).setAdapter(adap, new C07522());
        builder.show();
    }

    private void startChoosePhotoActivity() {
        startActivityForResult(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), REQUEST_NEW_IMAGES);
    }

    private void showAddImageDialog() {
        IconListDialogAdapter adap = new IconListDialogAdapter(this);
        adap.list.add(new ListRow(0, 2131493406));
        adap.list.add(new ListRow(0, 2131493015));
        Builder builder = new Builder(this);
        builder.setTitle(2131492968).setAdapter(adap, new C07533());
        builder.show();
    }

    private void refreshInfo() {
        this.adapter.refreshMessagesFromDB$7c629dcd(this.userId);
    }

    private void showProgressDialog() {
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(this);
            this.progressDialog.setTitle(2131493184);
            this.progressDialog.setIndeterminate(true);
        }
        this.progressDialog.show();
    }

    private void hideProgressDialog() {
        if (this.progressDialog != null && this.progressDialog.isShowing()) {
            this.progressDialog.hide();
        }
    }
}

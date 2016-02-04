package com.shamchat.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.models.Message;
import com.shamchat.models.Message.MessageType;
import com.shamchat.models.User;
import com.shamchat.utils.Utils;

public class MessageDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_BOOLEAN = "boolean";
    public static final String EXTRA_IMAGE = "extra_image_favourite";
    public static final String EXTRA_IMAGE_CONTENT = "image_content";
    public static final String EXTRA_MESSAGE_ID = "message_id";
    public static final int REQUEST_EDIT_TEXT = 24;
    private static final String TAG;
    private ChatProviderNew chatProvider;
    private ImageButton copyButton;
    private ImageButton deleteButton;
    private ImageButton editButton;
    private ImageView imageMessage;
    private ImageView imageUser;
    private Message message;
    private ImageButton momentButton;
    private ImageButton sendButton;
    private TextView textDetailsDate;
    private TextView textMessageContent;
    private TextView textName;
    private User user;

    /* renamed from: com.shamchat.activity.MessageDetailsActivity.1 */
    class C07881 implements OnClickListener {
        C07881() {
        }

        public final void onClick(View arg0) {
            Intent intent = new Intent(MessageDetailsActivity.this, ImagePreviewActivity.class);
            intent.putExtra(MessageDetailsActivity.EXTRA_MESSAGE_ID, MessageDetailsActivity.this.message.messageId);
            MessageDetailsActivity.this.startActivity(intent);
        }
    }

    /* renamed from: com.shamchat.activity.MessageDetailsActivity.2 */
    class C07892 implements OnClickListener {
        C07892() {
        }

        public final void onClick(View arg0) {
        }
    }

    /* renamed from: com.shamchat.activity.MessageDetailsActivity.3 */
    class C07903 implements OnClickListener {
        C07903() {
        }

        public final void onClick(View arg0) {
            ((ClipboardManager) MessageDetailsActivity.this.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", MessageDetailsActivity.this.message.messageContent));
            Toast.makeText(MessageDetailsActivity.this, 2131493078, 0).show();
        }
    }

    /* renamed from: com.shamchat.activity.MessageDetailsActivity.4 */
    class C07914 implements OnClickListener {
        C07914() {
        }

        public final void onClick(View v) {
            MessageDetailsActivity.this.editTextMessage();
        }
    }

    /* renamed from: com.shamchat.activity.MessageDetailsActivity.5 */
    class C07925 implements OnClickListener {
        C07925() {
        }

        public final void onClick(View v) {
            MessageDetailsActivity.this.chatProvider;
            if (ChatProviderNew.removeFavorite(MessageDetailsActivity.this.message.messageId)) {
                Toast.makeText(MessageDetailsActivity.this, 2131493193, 0).show();
            } else {
                Toast.makeText(MessageDetailsActivity.this, 2131493192, 0).show();
            }
            MessageDetailsActivity.this.finish();
        }
    }

    static {
        TAG = MessageDetailsActivity.class.getSimpleName();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903163);
        initializeActionBar();
        Bundle extras = getIntent().getExtras();
        if (extras == null || !extras.containsKey(EXTRA_MESSAGE_ID)) {
            Log.e(TAG, "Error: You need to pass EXTRA message!");
            return;
        }
        String messageId = extras.getString(EXTRA_MESSAGE_ID);
        this.chatProvider = new ChatProviderNew();
        this.message = ChatProviderNew.getFavorite(messageId);
        if (this.message == null) {
            Log.e(TAG, "Attempted to load a message with id '" + messageId + "' not found in db");
            return;
        }
        Cursor cursor = getContentResolver().query(UserProvider.CONTENT_URI_USER, null, "userId=?", new String[]{this.message.userId}, null);
        cursor.moveToFirst();
        this.user = UserProvider.userFromCursor(cursor);
        if (this.user == null) {
            Log.e(TAG, "Could not get user from db of message " + this.message);
        }
        cursor.close();
        Log.d(TAG, "Message received:" + this.message);
        initializeViews();
    }

    private void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(31);
        actionBar.setCustomView(2130903043);
        ((TextView) findViewById(2131361839)).setText(getResources().getString(2131493097));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_TEXT && resultCode == -1) {
            this.message = (Message) data.getParcelableExtra(AddFavoriteTextActivity.EXTRA_RESULT_EDITED_MESSAGE);
            ChatProviderNew.updateFavorite(this.message);
            refreshInfo();
        }
    }

    private void refreshInfo() {
        this.textMessageContent.setText(this.message.messageContent);
    }

    private void initializeViews() {
        ((TextView) findViewById(2131362282)).setVisibility(4);
        this.imageUser = (ImageView) findViewById(2131361986);
        if (this.user.profileImage != null && this.user.profileImage.length() > 0) {
            this.imageUser.setImageBitmap(Utils.base64ToBitmap(this.user.profileImage));
        }
        this.textName = (TextView) findViewById(2131362293);
        this.textName.setText(this.user.username);
        if (this.message.type == MessageType.IMAGE) {
            ((ViewStub) findViewById(2131362294)).inflate();
            this.imageMessage = (ImageView) findViewById(2131362268);
            System.out.println(this.message.messageContent);
            this.imageMessage.setOnClickListener(new C07881());
            this.imageMessage.setScaleType(ScaleType.CENTER_CROP);
            Message.loadAsyncImageContent$78bd0eef();
            ((ViewStub) findViewById(2131362269)).inflate();
            this.momentButton = (ImageButton) findViewById(2131362271);
            this.momentButton.setOnClickListener(new C07892());
        } else {
            ((ViewStub) findViewById(2131362295)).inflate();
            this.textMessageContent = (TextView) findViewById(2131362275);
            this.textMessageContent.setText(this.message.messageContent);
            ((ViewStub) findViewById(2131362276)).inflate();
            this.copyButton = (ImageButton) findViewById(2131362277);
            this.copyButton.setOnClickListener(new C07903());
            this.editButton = (ImageButton) findViewById(2131362278);
            this.editButton.setOnClickListener(new C07914());
            this.sendButton = (ImageButton) findViewById(2131362272);
        }
        this.textDetailsDate = (TextView) findViewById(2131362270);
        this.textDetailsDate.setText(Utils.formatDate(this.message.time, "yyyy-MM-dd hh:mm:ss aaa"));
        this.deleteButton = (ImageButton) findViewById(2131362273);
        this.deleteButton.setOnClickListener(new C07925());
    }

    private void editTextMessage() {
        Intent intent = new Intent(this, AddFavoriteTextActivity.class);
        intent.putExtra(AddFavoriteTextActivity.EXTRA_MESSAGE, this.message);
        startActivityForResult(intent, REQUEST_EDIT_TEXT);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                return true;
            default:
                return false;
        }
    }
}

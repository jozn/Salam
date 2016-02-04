package com.shamchat.activity;

import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.internal.view.SupportMenu;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shamchat.adapters.MyAccountItemsAdapter;
import com.shamchat.adapters.MyAccountItemsAdapter.MyAccountListItem;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.models.User;
import java.util.regex.Pattern;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public class MyAccountActivity extends AppCompatActivity {
    private boolean email_error;
    private MyAccountItemsAdapter myAccountItemsAdapter;
    LinearLayout settingsItems;
    private TextView txtEmail;
    private User user;

    /* renamed from: com.shamchat.activity.MyAccountActivity.1 */
    class C07931 implements OnClickListener {
        C07931() {
        }

        public final void onClick(View v) {
        }
    }

    /* renamed from: com.shamchat.activity.MyAccountActivity.2 */
    class C07942 implements DialogInterface.OnClickListener {
        final /* synthetic */ EditText val$txtEmail;

        C07942(EditText editText) {
            this.val$txtEmail = editText;
        }

        public final void onClick(DialogInterface dialog, int which) {
            MyAccountActivity myAccountActivity = MyAccountActivity.this;
            EditText editText = this.val$txtEmail;
            boolean z = false;
            if (editText != null) {
                z = Pattern.compile("^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", 2).matcher(editText.getText()).matches();
                if (!z) {
                    editText.setHint("Invalid Email");
                    editText.setHintTextColor(SupportMenu.CATEGORY_MASK);
                }
            }
            myAccountActivity.email_error = z;
            if (MyAccountActivity.this.email_error) {
                MyAccountActivity.this.changeEmail(this.val$txtEmail.getText().toString());
            }
        }
    }

    /* renamed from: com.shamchat.activity.MyAccountActivity.3 */
    class C07953 implements DialogInterface.OnClickListener {
        C07953() {
        }

        public final void onClick(DialogInterface dialog, int which) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903093);
        initializeActionBar();
        this.settingsItems = (LinearLayout) findViewById(2131362047);
        this.myAccountItemsAdapter = new MyAccountItemsAdapter(this);
        addMyAccountListItems();
    }

    private void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(31);
        actionBar.setCustomView(2130903043);
        ((TextView) findViewById(2131361839)).setText(getResources().getString(2131493217));
    }

    private void addMyAccountListItems() {
        UserProvider userProvider = new UserProvider();
        this.user = UserProvider.getCurrentUser();
        addListItem("Sham ID", this.user.chatId, new C07931(), false);
    }

    private void showEmail() {
        this.email_error = false;
        Builder builder = new Builder(this);
        builder.setTitle("Email");
        View layout = getLayoutInflater().inflate(2130903127, null);
        EditText txtEmail = (EditText) layout.findViewById(2131362182);
        builder.setView(layout);
        builder.setPositiveButton("OK", new C07942(txtEmail));
        builder.setNegativeButton("Cancel", new C07953());
        builder.show();
    }

    private View addListItem(String textTopic, String textValue, OnClickListener listener, boolean refresh) {
        this.myAccountItemsAdapter.list.add(new MyAccountListItem(textTopic, textValue));
        int position = this.myAccountItemsAdapter.list.size() - 1;
        this.settingsItems.addView(LayoutInflater.from(this).inflate(2130903192, null));
        View rowView = this.myAccountItemsAdapter.getView(position, null, null);
        rowView.setBackgroundResource(2130837576);
        this.settingsItems.addView(rowView);
        rowView.setOnClickListener(listener);
        return rowView;
    }

    private void changeEmail(String textValue) {
        if (this.txtEmail != null) {
            this.txtEmail.setText(textValue);
            ContentValues contentValues = new ContentValues();
            contentValues.put(NotificationCompatApi21.CATEGORY_EMAIL, textValue);
            getContentResolver().update(Uri.parse(UserProvider.CONTENT_URI_USER.toString() + MqttTopic.TOPIC_LEVEL_SEPARATOR + this.user.userId), contentValues, null, null);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                break;
        }
        return false;
    }
}

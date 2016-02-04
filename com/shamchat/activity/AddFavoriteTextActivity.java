package com.shamchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.C0170R;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import com.shamchat.models.Message;

public class AddFavoriteTextActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_RESULT_EDITED_MESSAGE = "edited_message";
    public static final String EXTRA_RESULT_TEXT = "text";
    private EditText editText;
    private Message message;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903074);
        initializeActionBar();
        Bundle extras = getIntent().getExtras();
        this.editText = (EditText) findViewById(C0170R.id.text);
        if (extras != null) {
            Message message = (Message) extras.getParcelable(EXTRA_MESSAGE);
            this.message = message;
            if (message != null) {
                this.editText.setText(this.message.messageContent);
            }
        }
    }

    private void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(31);
        actionBar.setCustomView(2130903043);
        ((TextView) findViewById(2131361839)).setText(getResources().getString(2131493409));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(2131623940, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                return true;
            case 2131362561:
                if (this.editText.getText().toString().trim().length() == 0) {
                    return true;
                }
                Intent data = new Intent();
                String text = this.editText.getText().toString();
                if (this.message != null) {
                    this.message.messageContent = text;
                    data.putExtra(EXTRA_RESULT_EDITED_MESSAGE, this.message);
                } else {
                    data.putExtra(EXTRA_RESULT_TEXT, text);
                }
                setResult(-1, data);
                finish();
                return true;
            default:
                return false;
        }
    }
}

package com.shamchat.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.C0170R;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shamchat.adapters.PrivacyItemsAdapter;
import com.shamchat.adapters.PrivacyItemsAdapter.PrivacyListItem;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.models.User;
import com.shamchat.models.User.BooleanStatus;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class PrivacyActivity extends AppCompatActivity {
    private boolean isFindMeByPhoneNoChecked;
    private boolean isFindMeByShamIdChecked;
    private PrivacyItemsAdapter privacyItemAdapter;
    private LinearLayout privacyListView;
    private Boolean save;
    private User user;

    /* renamed from: com.shamchat.activity.PrivacyActivity.1 */
    class C08281 implements OnCheckedChangeListener {
        C08281() {
        }

        public final void onCheckedChanged(CompoundButton view, boolean isCheckChange) {
            if (isCheckChange) {
                PrivacyActivity.this.user.findMeByPhoneNo = BooleanStatus.TRUE;
                return;
            }
            PrivacyActivity.this.user.findMeByPhoneNo = BooleanStatus.FALSE;
            PrivacyActivity.this.save = Boolean.valueOf(true);
        }
    }

    /* renamed from: com.shamchat.activity.PrivacyActivity.1HttpAsyncTask */
    class AnonymousClass1HttpAsyncTask extends AsyncTask<String, String, String> {
        JSONArray array;
        JSONObject arrayObject;
        JSONObject main;
        final /* synthetic */ String val$email;
        final /* synthetic */ String val$gender;
        final /* synthetic */ String val$myStatus;
        final /* synthetic */ BooleanStatus val$phoneStatus;
        final /* synthetic */ String val$profilePicture;
        final /* synthetic */ String val$region;
        final /* synthetic */ String val$shamID;
        final /* synthetic */ BooleanStatus val$shamIdSatus;
        final /* synthetic */ String val$userID;
        final /* synthetic */ String val$userName;

        AnonymousClass1HttpAsyncTask(String str, String str2, String str3, String str4, String str5, String str6, String str7, BooleanStatus booleanStatus, BooleanStatus booleanStatus2, String str8) {
            this.val$profilePicture = str;
            this.val$shamID = str2;
            this.val$userName = str3;
            this.val$email = str4;
            this.val$gender = str5;
            this.val$myStatus = str6;
            this.val$region = str7;
            this.val$phoneStatus = booleanStatus;
            this.val$shamIdSatus = booleanStatus2;
            this.val$userID = str8;
            this.main = new JSONObject();
            this.array = new JSONArray();
            this.arrayObject = new JSONObject();
        }

        protected final /* bridge */ /* synthetic */ Object doInBackground(Object[] objArr) {
            return doInBackground$4af589aa();
        }

        protected final /* bridge */ /* synthetic */ void onPostExecute(Object obj) {
            String str = (String) obj;
            super.onPostExecute(str);
            if (str != null) {
                try {
                    if (new JSONObject(str).getString(NotificationCompatApi21.CATEGORY_STATUS).equals("success")) {
                        System.out.println("EDITED PROFILE");
                        PrivacyActivity.this.user.userId = this.val$userID;
                        PrivacyActivity.this.user.chatId = this.val$shamID;
                        PrivacyActivity.this.user.username = this.val$userName;
                        PrivacyActivity.this.user.profileImage = this.val$profilePicture;
                        PrivacyActivity.this.user.email = this.val$email;
                        PrivacyActivity.this.user.myStatus = this.val$myStatus;
                        PrivacyActivity.this.user.findMeByPhoneNo = this.val$phoneStatus;
                        PrivacyActivity.this.user.findMeByShamId = this.val$shamIdSatus;
                        UserProvider userProvider = new UserProvider();
                        UserProvider.updateUser(PrivacyActivity.this.user);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        protected final void onPreExecute() {
            super.onPreExecute();
            if (this.val$profilePicture != null) {
                Log.d("profileurl", this.val$profilePicture);
            }
            try {
                this.arrayObject.put("chatId", this.val$shamID);
                this.arrayObject.put("userName", this.val$userName);
                this.arrayObject.put(NotificationCompatApi21.CATEGORY_EMAIL, this.val$email);
                this.arrayObject.put("profileImageBytes", this.val$profilePicture);
                this.arrayObject.put("mobileNo", PrivacyActivity.this.user.mobileNo);
                this.arrayObject.put("gender", this.val$gender);
                this.arrayObject.put("myStatus", this.val$myStatus);
                this.arrayObject.put("region", this.val$region);
                this.arrayObject.put("findMeByMobileNo", Integer.toString(this.val$phoneStatus.status));
                this.arrayObject.put("findMeByChatId", Integer.toString(this.val$shamIdSatus.status));
                this.main.put("userId", this.val$userID);
                this.array.put(this.arrayObject);
                this.main.put("dataFields", this.array);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String doInBackground$4af589aa() {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            if (this.main == null) {
                System.out.println("null main");
            } else {
                System.out.println(this.main.toString());
            }
            try {
                HttpPost httpPost = new HttpPost(PrivacyActivity.this.getResources().getString(2131493167) + "updateUserDataField.htm");
                List<NameValuePair> data = new ArrayList();
                data.add(new BasicNameValuePair("userDataFiledJson", this.main.toString()));
                httpPost.entity = new UrlEncodedFormEntity(data);
                System.out.println("httppost " + httpPost.uri);
                InputStream inputStream = httpclient.execute((HttpUriRequest) httpPost).getEntity().getContent();
                BufferedReader bufferreader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder responseStr = new StringBuilder();
                while (true) {
                    String responseLineStr = bufferreader.readLine();
                    if (responseLineStr != null) {
                        responseStr.append(responseLineStr);
                    } else {
                        bufferreader.close();
                        inputStream.close();
                        return responseStr.toString();
                    }
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    /* renamed from: com.shamchat.activity.PrivacyActivity.2 */
    class C08292 implements OnCheckedChangeListener {
        C08292() {
        }

        public final void onCheckedChanged(CompoundButton view, boolean isCheckChange) {
            if (isCheckChange) {
                PrivacyActivity.this.user.findMeByShamId = BooleanStatus.TRUE;
            } else {
                PrivacyActivity.this.user.findMeByShamId = BooleanStatus.FALSE;
            }
            PrivacyActivity.this.save = Boolean.valueOf(true);
        }
    }

    /* renamed from: com.shamchat.activity.PrivacyActivity.3 */
    class C08303 implements OnClickListener {
        C08303() {
        }

        public final void onClick(View v) {
            PrivacyActivity.this.startActivity(new Intent(PrivacyActivity.this.getApplicationContext(), BlockFriendActivity.class));
        }
    }

    public PrivacyActivity() {
        this.isFindMeByPhoneNoChecked = false;
        this.isFindMeByShamIdChecked = false;
        this.save = Boolean.valueOf(false);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903096);
        initializeActionBar();
        this.privacyItemAdapter = new PrivacyItemsAdapter(this);
        this.privacyListView = (LinearLayout) findViewById(2131362058);
        this.save = Boolean.valueOf(false);
        UserProvider userProvider = new UserProvider();
        this.user = UserProvider.getCurrentUser();
        addPrivacyListItems();
        if (this.user.findMeByPhoneNo == BooleanStatus.FALSE) {
            this.isFindMeByPhoneNoChecked = false;
        } else if (this.user.findMeByPhoneNo == BooleanStatus.TRUE) {
            this.isFindMeByPhoneNoChecked = true;
        }
        if (this.user.findMeByShamId == BooleanStatus.FALSE) {
            this.isFindMeByShamIdChecked = false;
        } else if (this.user.findMeByShamId == BooleanStatus.TRUE) {
            this.isFindMeByShamIdChecked = true;
        }
    }

    private void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(31);
        actionBar.setCustomView(2130903043);
        ((TextView) findViewById(2131361839)).setText(getResources().getString(2131493268));
    }

    private void addPrivacyListItems() {
        boolean z;
        String string = getResources().getString(2131493136);
        C08281 c08281 = new C08281();
        if (this.user.findMeByPhoneNo == BooleanStatus.TRUE) {
            z = true;
        } else {
            z = false;
        }
        addListItem(string, c08281, false, false, z);
        string = getResources().getString(2131493137);
        C08292 c08292 = new C08292();
        if (this.user.findMeByShamId == BooleanStatus.TRUE) {
            z = true;
        } else {
            z = false;
        }
        addListItem(string, c08292, false, false, z);
        addListItem(getResources().getString(2131492990), new C08303(), false, true, false);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                updateUserInformation(this.user.userId, this.user.chatId, this.user.username, this.user.profileImage, this.user.email, this.user.gender, this.user.myStatus, this.user.cityOrRegion, this.user.findMeByPhoneNo, this.user.findMeByShamId);
                finish();
                break;
        }
        return false;
    }

    protected void onResume() {
        super.onResume();
    }

    private View addListItem(String text, Object listener, boolean refresh, boolean includeSelector, boolean isChecked) {
        this.privacyItemAdapter.list.add(new PrivacyListItem(text));
        int position = this.privacyItemAdapter.list.size() - 1;
        this.privacyListView.addView(LayoutInflater.from(this).inflate(2130903192, null));
        View rowView = this.privacyItemAdapter.getView(position, null, null);
        CheckBox checkBox = (CheckBox) rowView.findViewById(C0170R.id.checkbox);
        checkBox.setChecked(isChecked);
        if (includeSelector) {
            rowView.setBackgroundResource(2130837576);
            checkBox.setVisibility(8);
        }
        this.privacyListView.addView(rowView);
        if (listener instanceof OnCheckedChangeListener) {
            checkBox.setOnCheckedChangeListener((OnCheckedChangeListener) listener);
        } else {
            rowView.setOnClickListener((OnClickListener) listener);
        }
        if (refresh) {
            this.privacyItemAdapter.notifyDataSetChanged();
        }
        return rowView;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        updateUserInformation(this.user.userId, this.user.chatId, this.user.username, this.user.profileImage, this.user.email, this.user.gender, this.user.myStatus, this.user.cityOrRegion, this.user.findMeByPhoneNo, this.user.findMeByShamId);
        finish();
        return false;
    }

    private void updateUserInformation(String userID, String shamID, String userName, String profilePicture, String email, String gender, String myStatus, String region, BooleanStatus phoneStatus, BooleanStatus shamIdSatus) {
        try {
            System.out.println("updated user details" + userID + phoneStatus.status + shamIdSatus.status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AnonymousClass1HttpAsyncTask asyncTask = new AnonymousClass1HttpAsyncTask(profilePicture, shamID, userName, email, gender, myStatus, region, phoneStatus, shamIdSatus, userID);
        if (asyncTask.getStatus() != Status.RUNNING) {
            asyncTask.execute(new String[0]);
        }
    }
}

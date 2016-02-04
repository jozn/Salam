package com.rokhgroup.adapters;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.MultiAutoCompleteTextView;
import com.rokhgroup.utils.RokhPref;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONArray;
import org.json.JSONObject;

public final class UserMentionAsyncTask extends AsyncTask<String, String, String> {
    private String AVATAR;
    String CURRENT_USER_ID;
    String CURRENT_USER_TOKEN;
    private String PROFILENAME;
    RokhPref Session;
    private String USERNAME;
    MentionAdapter adapter;
    ArrayList<User> arrayOfUsers;
    private Activity context;
    public String data;

    /* renamed from: com.rokhgroup.adapters.UserMentionAsyncTask.1 */
    class C06541 implements Runnable {
        C06541() {
        }

        public final void run() {
            if (!UserMentionAsyncTask.this.context.isFinishing() && UserMentionAsyncTask.this.adapter != null) {
                ((MultiAutoCompleteTextView) UserMentionAsyncTask.this.context.findViewById(2131362475)).setAdapter(UserMentionAsyncTask.this.adapter);
                UserMentionAsyncTask.this.adapter.notifyDataSetChanged();
            }
        }
    }

    public class User {
        public String avatar;
        public String profilename;
        public String username;

        public User(String username, String profilename, String avatar) {
            this.username = username;
            this.profilename = profilename;
            this.avatar = avatar;
        }

        public final String toString() {
            return this.username;
        }
    }

    public UserMentionAsyncTask(Activity cntxt) {
        this.arrayOfUsers = new ArrayList();
        this.context = cntxt;
    }

    private String doInBackground(String... key) {
        this.Session = new RokhPref(this.context);
        String newText = key[0].trim().replace(" ", MqttTopic.SINGLE_LEVEL_WILDCARD);
        try {
            HttpClient hClient = new DefaultHttpClient();
            if (this.Session.pref.getBoolean("IsLoggedin", false)) {
                this.CURRENT_USER_TOKEN = this.Session.getUSERTOKEN();
                this.CURRENT_USER_ID = this.Session.getUSERID();
            }
            this.data = (String) hClient.execute(new HttpGet("http://social.rabtcdn.com/pin/api/search/?q=" + URLEncoder.encode(newText, "utf-8") + "&token=" + this.CURRENT_USER_TOKEN), new BasicResponseHandler());
            this.adapter = new MentionAdapter(this.context, this.arrayOfUsers);
            JSONArray jArray = new JSONObject(this.data).getJSONArray("objects");
            if (jArray.length() > 0) {
                for (int i = 0; i < jArray.length(); i++) {
                    this.USERNAME = jArray.getJSONObject(i).getString("username");
                    this.PROFILENAME = jArray.getJSONObject(i).getString("name");
                    this.AVATAR = "http://social.rabtcdn.com" + jArray.getJSONObject(i).getString("avatar");
                    this.adapter.add(new User(this.USERNAME, this.PROFILENAME, this.AVATAR));
                }
            }
        } catch (Exception e) {
            Log.w("Error", e.getMessage());
        }
        this.context.runOnUiThread(new C06541());
        return null;
    }
}

package com.shamchat.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.shamchat.activity.SearchFriendsViewPagerActivity.FragmentCommunicator;
import com.shamchat.adapters.SearchFriendsListAdapter;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.RosterProvider;
import com.shamchat.models.Friend;
import com.shamchat.utils.DefaultLogin;
import com.shamchat.utils.Utils;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class DefaultSearchFragment extends Fragment implements FragmentCommunicator {
    private String TAG;
    private SearchFriendsListAdapter adapter;
    private Context context;
    private Friend friendsInfo;
    private List<Friend> list;
    private ListView listView;
    private LinearLayout ln1;
    private LinearLayout ln2;
    DefaultLogin login;
    private SharedPreferences preferences;
    private ProgressDialog progressDialog;
    private String searchTerm;

    public DefaultSearchFragment() {
        this.list = new ArrayList();
        this.TAG = "DefaultSearchFragment";
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = getActivity();
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        ((SearchFriendsViewPagerActivity) this.context).fragmentCommunicator = this;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View inflate = inflater.inflate(2130903141, container, false);
        this.context = getActivity();
        this.progressDialog = new ProgressDialog(this.context);
        this.ln1 = (LinearLayout) inflate.findViewById(2131362230);
        this.ln2 = (LinearLayout) inflate.findViewById(2131362231);
        this.listView = (ListView) inflate.findViewById(2131362232);
        return inflate;
    }

    private void performSearch(String searchQuery) {
        new AsyncTask<String, String, String>() {
            String password;
            String phoneNumber;

            /* renamed from: com.shamchat.activity.DefaultSearchFragment.1HttpAsyncTask.1 */
            class C07481 implements OnItemClickListener {
                C07481() {
                }

                public final void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                }
            }

            {
                this.phoneNumber = BuildConfig.VERSION_NAME;
                this.password = BuildConfig.VERSION_NAME;
            }

            protected final /* bridge */ /* synthetic */ void onPostExecute(Object obj) {
                String str = (String) obj;
                super.onPostExecute(str);
                Log.d(DefaultSearchFragment.this.TAG, "super.onPostExecute(result) " + str);
                if (str != null) {
                    try {
                        JSONObject jSONObject = new JSONObject(str);
                        String string = jSONObject.getString(NotificationCompatApi21.CATEGORY_STATUS);
                        ContentResolver contentResolver = SHAMChatApplication.getMyApplicationContext().getContentResolver();
                        if (string.equals("success")) {
                            DefaultSearchFragment.this.ln1.setVisibility(0);
                            DefaultSearchFragment.this.ln2.setVisibility(8);
                            JSONArray jSONArray = jSONObject.getJSONObject("data").getJSONArray("friends");
                            jSONArray.getJSONObject(0).getString("chatId");
                            DefaultSearchFragment.this.list.clear();
                            int i = 0;
                            int i2 = 0;
                            while (i < jSONArray.length()) {
                                int i3;
                                DefaultSearchFragment.this.friendsInfo = new Friend();
                                JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                                DefaultSearchFragment.this.friendsInfo.accountStatus = jSONObject2.getString("accountStatus");
                                DefaultSearchFragment.this.friendsInfo.chatId = jSONObject2.getString("chatId");
                                DefaultSearchFragment.this.friendsInfo.email = jSONObject2.getString(NotificationCompatApi21.CATEGORY_EMAIL);
                                DefaultSearchFragment.this.friendsInfo.emailVerificationStatus = jSONObject2.getString("emailVerificationStatus");
                                DefaultSearchFragment.this.friendsInfo.inAppAlert = jSONObject2.getString("inAppAlert");
                                DefaultSearchFragment.this.friendsInfo.mobileNo = jSONObject2.getString("mobileNo");
                                DefaultSearchFragment.this.friendsInfo.name = jSONObject2.getString("name");
                                DefaultSearchFragment.this.friendsInfo.newMessageAlert = jSONObject2.getString("newMessageAlert");
                                DefaultSearchFragment.this.friendsInfo.profileImageUrl = jSONObject2.getString("profileImageUrl");
                                DefaultSearchFragment.this.friendsInfo.tempUserId = jSONObject2.getString("tempUserId");
                                DefaultSearchFragment.this.friendsInfo.userId = jSONObject2.getString("userId");
                                DefaultSearchFragment.this.friendsInfo.cityOrRegion = jSONObject2.getString("region");
                                DefaultSearchFragment.this.friendsInfo.myStatus = jSONObject2.getString("myStatus");
                                DefaultSearchFragment.this.friendsInfo.gender = jSONObject2.getString("gender");
                                Cursor query = contentResolver.query(RosterProvider.CONTENT_URI, new String[]{"jid", "user_status"}, "jid=?", new String[]{Utils.createXmppUserIdByUserId(DefaultSearchFragment.this.friendsInfo.userId)}, null);
                                if (query.getCount() == 0) {
                                    DefaultSearchFragment.this.list.add(DefaultSearchFragment.this.friendsInfo);
                                    i3 = i2 + 1;
                                } else {
                                    query.moveToFirst();
                                    if (query.getInt(query.getColumnIndex("user_status")) == 2) {
                                        DefaultSearchFragment.this.list.add(DefaultSearchFragment.this.friendsInfo);
                                        i3 = i2 + 1;
                                    } else {
                                        i3 = i2;
                                    }
                                }
                                i++;
                                i2 = i3;
                            }
                            if (i2 != 0) {
                                DefaultSearchFragment.this.adapter = new SearchFriendsListAdapter(DefaultSearchFragment.this.context, (Friend[]) DefaultSearchFragment.this.list.toArray(new Friend[DefaultSearchFragment.this.list.size()]));
                                DefaultSearchFragment.this.ln1.setVisibility(8);
                                DefaultSearchFragment.this.listView.setVisibility(0);
                                DefaultSearchFragment.this.listView.setAdapter(DefaultSearchFragment.this.adapter);
                                DefaultSearchFragment.this.listView.setOnItemClickListener(new C07481());
                            } else {
                                DefaultSearchFragment.this.ln1.setVisibility(8);
                                DefaultSearchFragment.this.ln2.setVisibility(0);
                            }
                        } else {
                            DefaultSearchFragment.this.ln1.setVisibility(8);
                            DefaultSearchFragment.this.ln2.setVisibility(0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (DefaultSearchFragment.this.progressDialog.isShowing()) {
                    DefaultSearchFragment.this.progressDialog.dismiss();
                }
            }

            protected final void onPreExecute() {
                this.phoneNumber = DefaultSearchFragment.this.preferences.getString("user_mobileNo", BuildConfig.VERSION_NAME);
                this.password = DefaultSearchFragment.this.preferences.getString("user_password", BuildConfig.VERSION_NAME);
                DefaultSearchFragment.this.progressDialog.setMessage(DefaultSearchFragment.this.getString(2131493271));
                DefaultSearchFragment.this.progressDialog.show();
                super.onPreExecute();
            }

            private String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient();
                try {
                    String encodedPhoneNum = URLEncoder.encode(this.phoneNumber, "UTF-8");
                    String encodedPassword = URLEncoder.encode(this.password, "UTF-8");
                    HttpGet httpGet = new HttpGet(DefaultSearchFragment.this.getResources().getString(2131493167) + "findFriends.htm?mobileNo=" + encodedPhoneNum + "&password=" + encodedPassword + "&searchTerm=" + URLEncoder.encode(params[0], "UTF-8"));
                    Log.d(DefaultSearchFragment.this.TAG, "PHONE NUMBER : " + httpGet.uri);
                    InputStream inputStream = httpclient.execute((HttpUriRequest) httpGet).getEntity().getContent();
                    BufferedReader bufferreader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder responseStr = new StringBuilder();
                    while (true) {
                        String responseLineStr = bufferreader.readLine();
                        if (responseLineStr != null) {
                            responseStr.append(responseLineStr);
                        } else {
                            bufferreader.close();
                            inputStream.close();
                            return responseStr.toString().trim();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute(new String[]{searchQuery});
    }

    public void passDataToFragment(String someValue) {
        if (Utils.isInternetAvailable(this.context)) {
            performSearch(someValue);
        }
    }

    public void onCompleteLogin(boolean hasLogged) {
        if (hasLogged) {
            performSearch(this.searchTerm);
        }
    }
}

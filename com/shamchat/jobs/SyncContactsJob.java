package com.shamchat.jobs;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.support.v7.appcompat.BuildConfig;
import android.widget.Toast;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.RosterProvider;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.androidclient.service.SmackableImp;
import com.shamchat.androidclient.util.PreferenceConstants;
import com.shamchat.androidclient.util.StatusMode;
import com.shamchat.events.SyncContactsCompletedEvent;
import com.shamchat.models.PhoneContacts;
import com.shamchat.models.User;
import com.shamchat.utils.Utils;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;
import de.greenrobot.event.EventBus;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class SyncContactsJob extends Job {
    private static final AtomicInteger jobCounter;
    private final int id;

    /* renamed from: com.shamchat.jobs.SyncContactsJob.1 */
    class C10921 extends Thread {
        final /* synthetic */ String val$message;

        C10921(String str) {
            this.val$message = str;
        }

        public final void run() {
            Looper.prepare();
            Toast.makeText(SHAMChatApplication.getMyApplicationContext(), this.val$message, 1).show();
            Looper.loop();
        }
    }

    static {
        jobCounter = new AtomicInteger(0);
    }

    public SyncContactsJob(long delay) {
        Params params = new Params(9000);
        params.delayMs = delay;
        params.persistent = true;
        params.requiresNetwork = true;
        super(params);
        this.id = jobCounter.incrementAndGet();
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
        showToast("\u062e\u0637\u0627\u06cc\u06cc \u062f\u0631 \u0628\u0631\u0648\u0632\u0631\u0633\u0627\u0646\u06cc \u0645\u062e\u0627\u0637\u0628\u06cc\u0646 \u0631\u062e \u062f\u0627\u062f \u0644\u0637\u0641\u0627 \u0645\u062c\u062f\u062f \u0633\u0639\u06cc \u0646\u0645\u0627\u06cc\u06cc\u062f.");
        EventBus.getDefault().postSticky(new SyncContactsCompletedEvent());
    }

    protected final int getRetryLimit() {
        return 2;
    }

    public final void onRun() throws Throwable {
        if (this.id == jobCounter.get()) {
            List<User> phoneContacts = new ArrayList();
            PhoneContacts contacts = getPhoneContacts();
            if (contacts != null) {
                phoneContacts.addAll(contacts.newlyAddedContacts);
                JSONObject jObject = new JSONObject();
                jObject.put("phoneNumbers", contacts.contactsJson);
                String value = jObject.toString();
                System.out.println("value" + value);
                String URL = SHAMChatApplication.getMyApplicationContext().getResources().getString(2131493167) + "getFreindsForPhoneNumbers.htm";
                OkHttpClient client = new OkHttpClient();
                client.setConnectTimeout(90, TimeUnit.SECONDS);
                client.setReadTimeout(90, TimeUnit.SECONDS);
                Response response = client.newCall(new Builder().url(URL).post(new FormEncodingBuilder().add("json", value).build()).build()).execute();
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    response.body().close();
                    if (result != null) {
                        JSONObject serverResponse = new JSONObject(result);
                        if (serverResponse.getString(NotificationCompatApi21.CATEGORY_STATUS).equalsIgnoreCase("success")) {
                            markCurrentAppUsers(serverResponse.getJSONObject("data").getJSONArray("friends"), phoneContacts);
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SHAMChatApplication.getMyApplicationContext());
                            preferences.edit().putBoolean(PreferenceConstants.INITIAL_LOGIN, false);
                            preferences.edit().apply();
                        }
                    }
                    showToast("\u0628\u0631\u0648\u0632 \u0631\u0633\u0627\u0646\u06cc \u0645\u062e\u0627\u0637\u0628\u06cc\u0646 \u0628\u0627 \u0645\u0648\u0641\u0642\u06cc\u062a \u0627\u0646\u062c\u0627\u0645 \u0634\u062f ");
                    EventBus.getDefault().postSticky(new SyncContactsCompletedEvent());
                    return;
                }
                throw new IOException("Unexpected code " + response);
            }
        }
    }

    private void showToast(String message) {
        try {
            new C10921(message).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        System.out.println("Sync Contacts should retry? tries 2 times ");
        return true;
    }

    private static PhoneContacts getPhoneContacts() throws JSONException {
        List<User> newlyAddedContacts = new ArrayList();
        List<String> contactListOnyNumbers = new ArrayList();
        JSONArray contactsJson = new JSONArray();
        ContentResolver cr = SHAMChatApplication.getMyApplicationContext().getContentResolver();
        Cursor cur = cr.query(Contacts.CONTENT_URI, null, null, null, "_id ASC");
        Editor editor = PreferenceManager.getDefaultSharedPreferences(SHAMChatApplication.getMyApplicationContext()).edit();
        editor.putInt(PreferenceConstants.CONTACT_LAST_COUNT, cur.getCount());
        editor.apply();
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                User user = new User();
                String id = cur.getString(cur.getColumnIndex("_id"));
                user.username = cur.getString(cur.getColumnIndex("display_name"));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex("has_phone_number"))) > 0) {
                    Cursor phonesCursor = cr.query(Phone.CONTENT_URI, null, "contact_id = ?", new String[]{id}, null);
                    while (phonesCursor.moveToNext()) {
                        String contactNumber = phonesCursor.getString(phonesCursor.getColumnIndex("data1"));
                        if (contactNumber != null) {
                            contactNumber = contactNumber.replace(" ", BuildConfig.VERSION_NAME).trim();
                            String phoneNumber = contactNumber.replaceAll("[^0-9]", BuildConfig.VERSION_NAME);
                            if (phoneNumber == null || phoneNumber.length() <= 0) {
                                System.out.println("Not a valid Number" + contactNumber);
                            } else {
                                user.mobileNo = phoneNumber;
                                user.userId = "Contact" + id;
                                contactListOnyNumbers.add(phoneNumber);
                                newlyAddedContacts.add(user);
                                contactsJson.put(phoneNumber);
                            }
                        }
                    }
                    phonesCursor.close();
                }
            }
            cur.moveToLast();
        }
        long lastId = cur.getLong(cur.getColumnIndex("_id"));
        cur.close();
        if (newlyAddedContacts.size() > 0) {
            return new PhoneContacts(newlyAddedContacts, contactsJson, contactListOnyNumbers, lastId);
        }
        return null;
    }

    private static boolean markCurrentAppUsers(JSONArray contactsInAppJSONArray, List<User> phoneContacts) {
        System.out.println("contactsInAppJSONArray " + contactsInAppJSONArray);
        if (contactsInAppJSONArray != null && contactsInAppJSONArray.length() > 0) {
            ContentResolver cr = SHAMChatApplication.getMyApplicationContext().getContentResolver();
            for (int i = 0; i < contactsInAppJSONArray.length(); i++) {
                try {
                    JSONObject contact = contactsInAppJSONArray.getJSONObject(i);
                    for (User user : phoneContacts) {
                        String mobileNo = user.mobileNo;
                        if (mobileNo.contains(" ")) {
                            mobileNo = mobileNo.replace(" ", BuildConfig.VERSION_NAME);
                        }
                        if (mobileNo.length() >= 9 && contact != null && contact.has("mobileNo") && contact.getString("mobileNo").length() > 9 && contact.getString("mobileNo").substring(contact.getString("mobileNo").length() - 9).contains(mobileNo.substring(mobileNo.length() - 9))) {
                            String userId = contact.getString("userId");
                            ContentValues values = new ContentValues();
                            values.put("userId", userId);
                            values.put("chatId", contact.getString("chatId"));
                            values.put("mobileNo", mobileNo);
                            values.put("name", user.username);
                            values.put(NotificationCompatApi21.CATEGORY_EMAIL, contact.getString(NotificationCompatApi21.CATEGORY_EMAIL));
                            values.put("region", contact.getString("region"));
                            values.put("gender", contact.getString("gender"));
                            values.put("profileimage_url", contact.getString("profileImageUrl"));
                            values.put("myStatus", contact.getString("myStatus"));
                            values.put("user_type", Integer.valueOf(2));
                            int result = cr.update(UserProvider.CONTENT_URI_USER, values, "userId=?", new String[]{user.userId});
                            if (result == 0) {
                                cr.insert(UserProvider.CONTENT_URI_USER, values);
                            } else {
                                System.out.println("SynContacts result " + result + " " + mobileNo);
                            }
                            String userJabberId = Utils.createXmppUserIdByUserId(userId);
                            ContentValues rosterValues = new ContentValues();
                            rosterValues.put("jid", userJabberId);
                            rosterValues.put("alias", user.username + MqttTopic.TOPIC_LEVEL_SEPARATOR + mobileNo);
                            rosterValues.put("status_mode", Integer.valueOf(StatusMode.offline.ordinal()));
                            rosterValues.put("status_message", BuildConfig.VERSION_NAME);
                            rosterValues.put("roster_group", BuildConfig.VERSION_NAME);
                            rosterValues.put("show_in_chat", Integer.valueOf(1));
                            if (cr.update(RosterProvider.CONTENT_URI, rosterValues, "jid=?", new String[]{userJabberId}) == 0) {
                                cr.insert(RosterProvider.CONTENT_URI, rosterValues);
                                SmackableImp.tryToAddRosterEntry$14e1ec6d(userJabberId, user.username + MqttTopic.TOPIC_LEVEL_SEPARATOR + mobileNo);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}

package com.shamchat.jobs;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.support.v7.appcompat.BuildConfig;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.androidclient.util.PreferenceConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class UpdatePhoneContactsDBJob extends Job {
    private static final AtomicInteger jobCounter;
    private final int id;

    static {
        jobCounter = new AtomicInteger(0);
    }

    public UpdatePhoneContactsDBJob() {
        super(new Params(100));
        this.id = jobCounter.incrementAndGet();
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        if (this.id == jobCounter.get()) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SHAMChatApplication.getMyApplicationContext());
            String lastContactID = preferences.getString(PreferenceConstants.CONTACT_LAST_ID, null);
            if (lastContactID == null) {
                lastContactID = "0";
            }
            ContentResolver cr = SHAMChatApplication.getMyApplicationContext().getContentResolver();
            Cursor cur = null;
            try {
                cur = cr.query(Contacts.CONTENT_URI, null, null, null, "_id ASC LIMIT 1000000 OFFSET " + lastContactID);
                boolean isInitialSync = preferences.getBoolean(PreferenceConstants.INITIAL_LOGIN, true);
                String lastIndex = String.valueOf(Integer.valueOf(lastContactID).intValue() + cur.getCount());
                if (cur.getCount() != 0) {
                    String id;
                    ContentValues values;
                    String contactNumber;
                    Cursor phones;
                    ContentValues contentValues;
                    Editor editor;
                    if (isInitialSync) {
                        List<ContentValues> cvs = new ArrayList();
                        while (cur.moveToNext()) {
                            id = cur.getString(cur.getColumnIndex("_id"));
                            values = new ContentValues();
                            values.put("userId", "Contact" + id);
                            values.put("name", cur.getString(cur.getColumnIndex("display_name")));
                            contactNumber = null;
                            if (Integer.parseInt(cur.getString(cur.getColumnIndex("has_phone_number"))) > 0) {
                                phones = cr.query(Phone.CONTENT_URI, null, "contact_id = ?", new String[]{id}, null);
                                while (phones.moveToNext()) {
                                    contactNumber = phones.getString(phones.getColumnIndex("data1"));
                                }
                                phones.close();
                            }
                            if (contactNumber != null) {
                                contentValues = values;
                                contentValues.put("mobileNo", contactNumber.replace(" ", BuildConfig.VERSION_NAME));
                                values.put("user_type", Integer.valueOf(0));
                                cvs.add(values);
                            }
                        }
                        ContentValues[] dsf = new ContentValues[cvs.size()];
                        cvs.toArray(dsf);
                        cr.bulkInsert(UserProvider.CONTENT_URI_USER, dsf);
                        editor = preferences.edit();
                        editor.putString(PreferenceConstants.CONTACT_LAST_ID, lastIndex);
                        editor.apply();
                    } else {
                        while (cur.moveToNext()) {
                            id = cur.getString(cur.getColumnIndex("_id"));
                            values = new ContentValues();
                            values.put("userId", "Contact" + id);
                            values.put("name", cur.getString(cur.getColumnIndex("display_name")));
                            contactNumber = null;
                            if (Integer.parseInt(cur.getString(cur.getColumnIndex("has_phone_number"))) > 0) {
                                phones = cr.query(Phone.CONTENT_URI, null, "contact_id = ?", new String[]{id}, null);
                                while (phones.moveToNext()) {
                                    contactNumber = phones.getString(phones.getColumnIndex("data1"));
                                }
                                phones.close();
                            }
                            if (contactNumber != null) {
                                contentValues = values;
                                contentValues.put("mobileNo", contactNumber.replace(" ", BuildConfig.VERSION_NAME));
                                values.put("user_type", Integer.valueOf(0));
                                if (cr.update(UserProvider.CONTENT_URI_USER, values, "mobileNo=?", new String[]{contactNumber}) == 0) {
                                    cr.insert(UserProvider.CONTENT_URI_USER, values);
                                }
                            }
                        }
                        editor = preferences.edit();
                        editor.putString(PreferenceConstants.CONTACT_LAST_ID, lastIndex);
                        editor.apply();
                    }
                    if (cur != null) {
                        cur.close();
                    }
                    System.out.println("UpdatePhoneContactsDBJob end");
                } else if (cur != null) {
                    cur.close();
                }
            } catch (Exception e) {
                System.out.println(" ERROR >>>>> " + e);
                e.printStackTrace();
                System.out.println("UpdatePhoneContactsDBJob end");
            } finally {
                if (cur != null) {
                    cur.close();
                }
            }
        }
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        System.out.println("AAA db load should re run on throwable");
        return false;
    }
}

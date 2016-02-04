package com.rokhgroup.mqtt;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationCompat.InboxStyle;
import android.support.v4.app.NotificationCompat.Style;
import android.util.Log;
import android.widget.Toast;
import com.kyleduo.switchbutton.C0473R;
import com.shamchat.activity.ChatInitialForGroupChatActivity;
import com.shamchat.activity.MainWindow;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import java.util.Map;
import java.util.Map.Entry;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public final class Notify {
    private static int MessageID;
    static Context context;
    static String groupId;
    static String groupName;
    static JSONObject json;
    static String msgBody;
    static String msgSender;
    static String msgSenderId;
    static int msgType;
    static SharedPreferences notifySharedPref;
    static String threadOwner;

    static {
        MessageID = 0;
        threadOwner = null;
        groupId = null;
        groupName = null;
        msgSender = null;
        msgSenderId = null;
        msgBody = null;
        msgType = 0;
        json = null;
    }

    static void notifcation(Context context2, String messageString) {
        notifySharedPref = SHAMChatApplication.getMyApplicationContext().getSharedPreferences("notify", 0);
        context = context2;
        try {
            json = new JSONObject(messageString);
            threadOwner = SHAMChatApplication.getConfig().userId;
            groupId = json.getString("to");
            groupName = json.getString("groupAlias");
            msgSender = json.getString("from");
            msgSenderId = json.getString("from_userid");
            msgBody = json.getString("messageBody");
            msgType = json.getInt("messageType");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService("notification");
        String str = groupId;
        SharedPreferences sharedPreferences = SHAMChatApplication.getMyApplicationContext().getSharedPreferences("notify", 0);
        notifySharedPref = sharedPreferences;
        Editor edit = sharedPreferences.edit();
        String[] split = notifySharedPref.getString(str, groupName + "::::0").split("::::");
        edit.putString(str, split[0] + "::::" + String.valueOf(Integer.valueOf(split[1]).intValue() + 1));
        edit.apply();
        mNotificationManager.notify(MessageID, makeNotification().build());
    }

    public static void toast$4475a3b4(Context context, CharSequence text) {
        Toast.makeText(context, text, 0).show();
    }

    public static void updateNotidication(Context context2) {
        context = context2;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService("notification");
        if (getAllNotifCount() == 0) {
            mNotificationManager.cancel(MessageID);
            return;
        }
        PendingIntent activity;
        long currentTimeMillis = System.currentTimeMillis();
        Intent intent;
        if (getAllNotifCount() == 0) {
            intent = new Intent(context, ChatInitialForGroupChatActivity.class);
            intent.setData(Uri.parse(msgSender));
            String str = threadOwner + "-" + groupId;
            intent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_GROUP_ID, groupId);
            intent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, str);
            intent.setFlags(536870912);
            activity = PendingIntent.getActivity(context, 0, intent, 134217728);
        } else {
            intent = new Intent(context, MainWindow.class);
            intent.putExtra("goto", 2);
            intent.setFlags(536870912);
            activity = PendingIntent.getActivity(context, 0, intent, 134217728);
        }
        Builder builder = new Builder(context);
        builder.setAutoCancel(true).setContentIntent(activity).setWhen(currentTimeMillis).setSmallIcon(2130837905);
        Style inboxStyle = new InboxStyle();
        SharedPreferences sharedPreferences = SHAMChatApplication.getMyApplicationContext().getSharedPreferences("notify", 0);
        notifySharedPref = sharedPreferences;
        Map all = sharedPreferences.getAll();
        int size = all.size();
        inboxStyle.setBigContentTitle(size + " " + context.getString(2131493431));
        builder.setNumber(size);
        builder.setContentTitle(size + " " + context.getString(2131493431));
        for (Entry entry : all.entrySet()) {
            Log.d("map values", ((String) entry.getKey()) + "::::" + entry.getValue().toString());
            entry.getKey();
            String[] split = ((String) entry.getValue()).split("::::");
            inboxStyle.addLine(split[0] + ": " + String.valueOf(Integer.valueOf(split[1]).intValue()) + " " + context.getString(2131493222));
        }
        builder.setStyle(inboxStyle);
        mNotificationManager.notify(MessageID, builder.build());
    }

    private static Builder makeNotification() {
        PendingIntent pendingIntent;
        CharSequence contentTitle = groupName;
        String ticker = (String) contentTitle;
        String content = null;
        String m1 = "\u0628\u0631\u0627\u06cc \u0634\u0645\u0627 \u062a\u0635\u0648\u06cc\u0631\u06cc \u0627\u0631\u0633\u0627\u0644 \u0646\u0645\u0648\u062f\u0647 \u0627\u0633\u062a";
        String m2 = "\u0628\u0631\u0627\u06cc \u0634\u0645\u0627 \u0648\u06cc\u062f\u0626\u0648\u06cc\u06cc \u0627\u0631\u0633\u0627\u0644 \u0646\u0645\u0648\u062f\u0647 \u0627\u0633\u062a";
        String m3 = "\u0628\u0631\u0627\u06cc \u0634\u0645\u0627 \u0627\u0633\u062a\u06cc\u06a9\u0631 \u0627\u0631\u0633\u0627\u0644 \u0646\u0645\u0648\u062f\u0647 \u0627\u0633\u062a";
        String m4 = "\u0628\u0631\u0627\u06cc \u0634\u0645\u0627 \u0622\u062f\u0631\u06cc \u062c\u063a\u0631\u0627\ufffd?\u06cc\u0627\u06cc\u06cc \u0627\u0631\u0633\u0627\u0644 \u0646\u0645\u0648\u062f\u0647 \u0627\u0633\u062a";
        String m5 = "\u0628\u0631\u0627\u06cc \u0634\u0645\u0627 \u067e\u06cc\u0627\u0645 \u0635\u0648\u062a\u06cc \u0627\u0631\u0633\u0627\u0644 \u0646\u0645\u0648\u062f\u0647 \u0627\u0633\u062a";
        String m6 = "\u0628\u0631\u0627\u06cc \u0634\u0645\u0627 \u0634\u06a9\u0644\u06a9 \u0627\u0631\u0633\u0627\u0644 \u0646\u0645\u0648\u062f\u0647 \u0627\u0633\u062a";
        if (msgType == MessageContentType.TEXT.ordinal()) {
            ticker = ticker + "@" + getContactNameFromPhone(context, msgSender) + ": " + msgBody;
            content = getContactNameFromPhone(context, msgSender) + ": " + msgBody;
        } else {
            if (msgType == MessageContentType.IMAGE.ordinal()) {
                ticker = ticker + "@" + getContactNameFromPhone(context, msgSender) + ": " + m1;
                content = getContactNameFromPhone(context, msgSender) + ": " + m1;
            } else {
                if (msgType == MessageContentType.VIDEO.ordinal()) {
                    ticker = ticker + "@" + getContactNameFromPhone(context, msgSender) + ": " + m2;
                    content = getContactNameFromPhone(context, msgSender) + ": " + m2;
                } else {
                    if (msgType == MessageContentType.STICKER.ordinal()) {
                        ticker = ticker + "@" + getContactNameFromPhone(context, msgSender) + ": " + m3;
                        content = getContactNameFromPhone(context, msgSender) + ": " + m3;
                    } else {
                        if (msgType == MessageContentType.LOCATION.ordinal()) {
                            ticker = ticker + "@" + getContactNameFromPhone(context, msgSender) + ": " + m4;
                            content = getContactNameFromPhone(context, msgSender) + ": " + m4;
                        } else {
                            if (msgType == MessageContentType.VOICE_RECORD.ordinal()) {
                                ticker = ticker + "@" + getContactNameFromPhone(context, msgSender) + ": " + m5;
                                content = getContactNameFromPhone(context, msgSender) + ": " + m5;
                            } else {
                                if (msgType == MessageContentType.MESSAGE_WITH_IMOTICONS.ordinal()) {
                                    ticker = ticker + "@" + getContactNameFromPhone(context, msgSender) + ": " + m6;
                                    content = getContactNameFromPhone(context, msgSender) + ": " + m6;
                                }
                            }
                        }
                    }
                }
            }
        }
        long when = System.currentTimeMillis();
        Intent intent;
        if (getAllNotifCount() == 1) {
            intent = new Intent(context, ChatInitialForGroupChatActivity.class);
            intent.setData(Uri.parse(msgSender));
            String threadId = threadOwner + "-" + groupId;
            intent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_GROUP_ID, groupId);
            intent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, threadId);
            intent.setFlags(536870912);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 134217728);
        } else {
            intent = new Intent(context, MainWindow.class);
            intent.putExtra("goto", 2);
            intent.setFlags(536870912);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 134217728);
        }
        Builder builder = new Builder(context);
        builder.setAutoCancel(true).setContentTitle(contentTitle).setContentIntent(pendingIntent).setContentText(content).setTicker(ticker).setWhen(when).setPriority(2);
        SharedPreferences sp = SHAMChatApplication.getMyApplicationContext().getSharedPreferences("setting", 0);
        switch (sp.getInt("notif_icon", 1)) {
            case Logger.SEVERE /*1*/:
                builder.setSmallIcon(2130837905);
                break;
            case Logger.WARNING /*2*/:
                builder.setSmallIcon(2130837906);
                break;
            case Logger.INFO /*3*/:
                builder.setSmallIcon(2130837907);
                break;
            case Logger.CONFIG /*4*/:
                builder.setSmallIcon(2130837908);
                break;
            case Logger.FINE /*5*/:
                builder.setSmallIcon(2130837909);
                break;
            case Logger.FINER /*6*/:
                builder.setSmallIcon(2130837910);
                break;
            case Logger.FINEST /*7*/:
                builder.setSmallIcon(2130837911);
                break;
            case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                builder.setSmallIcon(2130837912);
                break;
            case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                builder.setSmallIcon(2130837913);
                break;
            default:
                builder.setSmallIcon(2130837905);
                break;
        }
        int notifSound = sp.getInt("sound", 1);
        Log.i("Notify", "Sound " + notifSound);
        switch (notifSound) {
            case Logger.SEVERE /*1*/:
                builder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/2131034112"));
                break;
            case Logger.WARNING /*2*/:
                builder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/2131034113"));
                break;
            case Logger.INFO /*3*/:
                builder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/2131034114"));
                break;
            case Logger.CONFIG /*4*/:
                builder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/2131034116"));
                break;
            case Logger.FINE /*5*/:
                builder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/2131034118"));
                break;
            default:
                builder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/2131034112"));
                break;
        }
        builder.setLights(-16776961, 500, 500);
        InboxStyle inboxStyle = new InboxStyle();
        SharedPreferences sharedPreferences = SHAMChatApplication.getMyApplicationContext().getSharedPreferences("notify", 0);
        notifySharedPref = sharedPreferences;
        Map<String, ?> allEntries = sharedPreferences.getAll();
        int numberOfNotifications = allEntries.size();
        inboxStyle.setBigContentTitle(numberOfNotifications + " " + context.getString(2131493431));
        builder.setNumber(numberOfNotifications);
        for (Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", ((String) entry.getKey()) + "::::" + entry.getValue().toString());
            entry.getKey();
            String[] separated = ((String) entry.getValue()).split("::::");
            String groupNAME = separated[0];
            int existingNumber = Integer.valueOf(separated[1]).intValue();
            inboxStyle.addLine(groupNAME + ": " + String.valueOf(existingNumber) + " " + context.getString(2131493222));
        }
        builder.setStyle(inboxStyle);
        return builder;
    }

    private static int getAllNotifCount() {
        SharedPreferences sharedPreferences = SHAMChatApplication.getMyApplicationContext().getSharedPreferences("notify", 0);
        notifySharedPref = sharedPreferences;
        return sharedPreferences.getAll().size();
    }

    private static String getContactNameFromPhone(Context context, String number) {
        String[] projection = new String[]{"display_name", "_id"};
        Cursor cursor = context.getContentResolver().query(Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number)), projection, null, null, null);
        if (cursor.moveToFirst()) {
            String contactId = cursor.getString(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("display_name"));
            Contacts.openContactPhotoInputStream(context.getContentResolver(), ContentUris.withAppendedId(Contacts.CONTENT_URI, Long.parseLong(contactId)));
            Log.v("ffnet", "Started : Contact Found @ " + number);
            Log.v("ffnet", "Started : Contact name  = " + name);
            Log.v("ffnet", "Started : Contact id    = " + contactId);
            cursor.close();
            return name;
        }
        Log.v("ffnet", "Started : Contact Not Found @ " + number);
        cursor.close();
        return number;
    }
}

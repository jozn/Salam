package com.shamchat.adapters;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.appcompat.BuildConfig;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.RosterProvider;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.androidclient.service.SmackableImp;
import com.shamchat.androidclient.util.PreferenceConstants;
import com.shamchat.androidclient.util.StatusMode;
import com.shamchat.models.Friend;
import com.shamchat.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public final class SearchFriendsListAdapter extends BaseAdapter {
    private static LayoutInflater inflater;
    private Context context;
    ContentResolver cr;
    private Friend[] friendInfo;
    byte[] imageByte;
    SharedPreferences preference;
    List<Friend> selectedFriends;
    String url;

    /* renamed from: com.shamchat.adapters.SearchFriendsListAdapter.1 */
    class C10031 implements OnClickListener {
        final /* synthetic */ ImageButton val$addFriendButton;
        final /* synthetic */ int val$position;

        C10031(int i, ImageButton imageButton) {
            this.val$position = i;
            this.val$addFriendButton = imageButton;
        }

        public final void onClick(View v) {
            if (SearchFriendsListAdapter.this.selectedFriends.contains(SearchFriendsListAdapter.this.friendInfo[this.val$position])) {
                SearchFriendsListAdapter.this.selectedFriends.remove(SearchFriendsListAdapter.this.friendInfo[this.val$position]);
                this.val$addFriendButton.setImageResource(2130838062);
            } else {
                SearchFriendsListAdapter.this.selectedFriends.add(SearchFriendsListAdapter.this.friendInfo[this.val$position]);
                SearchFriendsListAdapter.access$100(SearchFriendsListAdapter.this, SearchFriendsListAdapter.this.friendInfo[this.val$position]);
                this.val$addFriendButton.setImageResource(2130838061);
            }
            Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(SearchFriendsListAdapter.this.context).edit();
            prefEditor.putLong(PreferenceConstants.MOMENT_LAST_SYNC_TIME, 0);
            prefEditor.commit();
            System.out.println("SELECTED COUNT " + SearchFriendsListAdapter.this.selectedFriends.size());
        }
    }

    /* renamed from: com.shamchat.adapters.SearchFriendsListAdapter.2 */
    class C10042 extends Thread {
        C10042() {
        }

        public final void run() {
            if (SearchFriendsListAdapter.this.url != null && SearchFriendsListAdapter.this.url.length() > 0) {
                try {
                    SearchFriendsListAdapter searchFriendsListAdapter = SearchFriendsListAdapter.this;
                    Utils utils = new Utils();
                    searchFriendsListAdapter.imageByte = Utils.downloadImageFromUrl(SearchFriendsListAdapter.this.url);
                } catch (Exception e) {
                    System.out.println("Search Friend" + e.getMessage());
                }
            }
            super.run();
        }
    }

    /* renamed from: com.shamchat.adapters.SearchFriendsListAdapter.3 */
    class C10053 implements Target {
        final /* synthetic */ ImageView val$imageView;
        final /* synthetic */ String val$userId;

        C10053(String str, ImageView imageView) {
            this.val$userId = str;
            this.val$imageView = imageView;
        }

        public final void onBitmapLoaded$dc1124d(Bitmap bitmap) {
            System.out.println("Bit map loaded");
            SHAMChatApplication.USER_IMAGES.put(this.val$userId, bitmap);
            this.val$imageView.setImageBitmap(bitmap);
        }

        public final void onBitmapFailed$130e17e7() {
        }
    }

    static /* synthetic */ void access$100(SearchFriendsListAdapter x0, Friend x1) {
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put("chatId", x1.chatId);
            contentValues.put(NotificationCompatApi21.CATEGORY_EMAIL, x1.email);
            contentValues.put("mobileNo", x1.mobileNo);
            contentValues.put("name", x1.name);
            x0.url = x1.profileImageUrl;
            contentValues.put("profileimage_url", x0.url);
            Thread c10042 = new C10042();
            c10042.start();
            try {
                c10042.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            contentValues.put("userId", x1.userId);
            contentValues.put("gender", x1.gender);
            contentValues.put("region", x1.cityOrRegion);
            if (x0.cr.update(UserProvider.CONTENT_URI_USER, contentValues, "userId=?", new String[]{x1.userId}) == 0) {
                x0.cr.insert(UserProvider.CONTENT_URI_USER, contentValues);
            }
            String createXmppUserIdByUserId = Utils.createXmppUserIdByUserId(r1);
            String str = x1.mobileNo;
            String str2 = x1.name;
            SmackableImp.tryToAddRosterEntry$14e1ec6d(createXmppUserIdByUserId, str2 + MqttTopic.TOPIC_LEVEL_SEPARATOR + str);
            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("jid", createXmppUserIdByUserId);
            contentValues2.put("alias", str2 + MqttTopic.TOPIC_LEVEL_SEPARATOR + str);
            contentValues2.put("status_mode", Integer.valueOf(StatusMode.offline.ordinal()));
            contentValues2.put("status_message", BuildConfig.VERSION_NAME);
            contentValues2.put("roster_group", BuildConfig.VERSION_NAME);
            contentValues2.put("show_in_chat", Integer.valueOf(1));
            contentValues2.put("user_status", Integer.valueOf(0));
            if (x0.cr.update(RosterProvider.CONTENT_URI, contentValues2, "jid=?", new String[]{createXmppUserIdByUserId}) == 0) {
                x0.cr.insert(RosterProvider.CONTENT_URI, contentValues2);
            }
        } catch (Exception e2) {
            System.out.println("Error occured while adding searched friend");
        }
    }

    static {
        inflater = null;
    }

    public SearchFriendsListAdapter(Context ctx, Friend[] fInfo) {
        this.url = BuildConfig.VERSION_NAME;
        this.context = ctx;
        this.friendInfo = fInfo;
        inflater = (LayoutInflater) ctx.getSystemService("layout_inflater");
        this.selectedFriends = new ArrayList();
        this.preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        this.cr = this.context.getContentResolver();
    }

    public final int getCount() {
        return this.friendInfo.length;
    }

    public final Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public final long getItemId(int position) {
        return (long) position;
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        View vi = inflater.inflate(2130903128, null);
        ImageView userProfilePhoto = (ImageView) vi.findViewById(2131362184);
        ImageButton addFriendButton = (ImageButton) vi.findViewById(2131362186);
        ((TextView) vi.findViewById(2131362185)).setText(this.friendInfo[position].chatId);
        String profileUrl = this.friendInfo[position].profileImageUrl;
        System.out.println("SEARCH FRIENS URL: " + profileUrl);
        if (profileUrl == null || !profileUrl.startsWith("http://")) {
            userProfilePhoto.setImageResource(2130837944);
        } else {
            String userId = this.friendInfo[position].userId;
            if (SHAMChatApplication.USER_IMAGES.containsKey(userId)) {
                userProfilePhoto.setImageBitmap((Bitmap) SHAMChatApplication.USER_IMAGES.get(userId));
            } else {
                Picasso.with(this.context).load(profileUrl).into(new C10053(this.friendInfo[position].userId, userProfilePhoto));
            }
        }
        addFriendButton.setOnClickListener(new C10031(position, addFriendButton));
        if (this.selectedFriends.contains(this.friendInfo[position])) {
            addFriendButton.setImageResource(2130838061);
        } else {
            addFriendButton.setImageResource(2130838062);
        }
        return vi;
    }
}

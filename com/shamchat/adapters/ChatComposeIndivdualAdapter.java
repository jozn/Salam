package com.shamchat.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.appcompat.BuildConfig;
import android.support.v7.appcompat.C0170R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.shamchat.activity.ComposeIndividualChatActivity;
import com.shamchat.activity.ProfileActivity;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;

public class ChatComposeIndivdualAdapter extends CursorAdapter {
    private static final int VIEW_TYPE_COUNT = 3;
    private static final int VIEW_TYPE_GROUP_CONT = 1;
    private static final int VIEW_TYPE_GROUP_START = 0;
    private ComposeIndividualChatActivity baseActivity;
    private Context context;
    private int indexColName;
    private int indexMobileNo;
    private int indexUserId;
    private LayoutInflater mInflater;
    private List<String> selectedUsers;
    private boolean showYou;
    private int uriuser;

    /* renamed from: com.shamchat.adapters.ChatComposeIndivdualAdapter.1 */
    class C09361 implements OnClickListener {
        final /* synthetic */ String val$currentUserID;

        C09361(String str) {
            this.val$currentUserID = str;
        }

        public final void onClick(View v) {
            ChatComposeIndivdualAdapter.this.baseActivity.createChat(this.val$currentUserID);
        }
    }

    /* renamed from: com.shamchat.adapters.ChatComposeIndivdualAdapter.2 */
    class C09372 implements Target {
        final /* synthetic */ ImageView val$imageView;
        final /* synthetic */ String val$userId;

        C09372(String str, ImageView imageView) {
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

    public ChatComposeIndivdualAdapter(Context context, Cursor cursor, boolean showYou, ComposeIndividualChatActivity baseActivity) {
        super(context, cursor, 2);
        this.showYou = showYou;
        this.mInflater = LayoutInflater.from(context);
        this.indexColName = cursor.getColumnIndex("name");
        this.indexUserId = cursor.getColumnIndex("userId");
        this.indexMobileNo = cursor.getColumnIndex("mobileNo");
        this.selectedUsers = new ArrayList();
        this.context = context;
        this.baseActivity = baseActivity;
    }

    public int getViewTypeCount() {
        return this.showYou ? VIEW_TYPE_COUNT : 2;
    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        if (isNewGroup(cursor, position)) {
            return 0;
        }
        return VIEW_TYPE_GROUP_CONT;
    }

    private boolean isNewGroup(Cursor cursor, int position) {
        if (position == 0) {
            return true;
        }
        String newName = cursor.getString(this.indexColName);
        if (newName == null || newName.isEmpty()) {
            newName = "no name";
        }
        cursor.moveToPosition(position - 1);
        String oldName = cursor.getString(this.indexColName);
        if (oldName == null || oldName.isEmpty()) {
            oldName = "no name";
        }
        cursor.moveToPosition(position);
        if (oldName.substring(0, VIEW_TYPE_GROUP_CONT).toUpperCase().equals(newName.substring(0, VIEW_TYPE_GROUP_CONT).toUpperCase())) {
            return false;
        }
        return true;
    }

    public void bindView(View view, Context context, Cursor cursor) {
        if (((Integer) view.getTag()).intValue() == 0) {
            TextView textHeader = (TextView) view.findViewById(2131362164);
            if (cursor.getString(this.indexColName) == null || cursor.getString(this.indexColName).equals(BuildConfig.VERSION_NAME) || cursor.getString(this.indexColName).length() <= 0) {
                textHeader.setText(2131493229);
            } else {
                textHeader.setText(cursor.getString(this.indexColName).substring(0, VIEW_TYPE_GROUP_CONT));
            }
        }
        TextView textOwnName = (TextView) view.findViewById(2131362166);
        ImageView usesalam = (ImageView) view.findViewById(2131362167);
        if (cursor.getString(this.indexColName) != null && cursor.getString(this.indexColName).length() > 0) {
            textOwnName.setText(cursor.getString(this.indexColName));
        } else if (cursor.getString(this.indexMobileNo) == null || cursor.getString(this.indexMobileNo).length() <= 0) {
            textOwnName.setText(2131493229);
        } else {
            textOwnName.setText(cursor.getString(this.indexMobileNo));
        }
        ImageView profileImageView = (ImageView) view.findViewById(C0170R.id.image);
        String currentUserID = cursor.getString(this.indexUserId);
        String profileUrl = cursor.getString(cursor.getColumnIndex("profileimage_url"));
        if (this.indexUserId == VIEW_TYPE_GROUP_CONT) {
            usesalam.setVisibility(0);
        } else {
            usesalam.setVisibility(8);
        }
        if (profileUrl == null || !profileUrl.startsWith("http://")) {
            profileImageView.setImageResource(2130837944);
        } else if (SHAMChatApplication.USER_IMAGES.containsKey(currentUserID)) {
            profileImageView.setImageBitmap((Bitmap) SHAMChatApplication.USER_IMAGES.get(currentUserID));
        } else {
            loadImage(currentUserID, profileUrl, profileImageView);
        }
        ((CheckBox) view.findViewById(C0170R.id.checkbox)).setVisibility(4);
        view.setOnClickListener(new C09361(currentUserID));
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layout;
        int viewType = getItemViewType(cursor.getPosition());
        switch (viewType) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                layout = 2130903119;
                break;
            default:
                layout = 2130903152;
                break;
        }
        View viewResult = this.mInflater.inflate(layout, parent, false);
        viewResult.setTag(Integer.valueOf(viewType));
        return viewResult;
    }

    protected void onUserClick(View v, String userId) {
        Intent intent = new Intent(v.getContext(), ProfileActivity.class);
        intent.putExtra(ProfileActivity.EXTRA_USER_ID, Utils.createXmppUserIdByUserId(userId));
        intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
        v.getContext().startActivity(intent);
    }

    public List<String> getSelectedUsers() {
        return this.selectedUsers;
    }

    private void loadImage(String userId, String url, ImageView imageView) {
        Picasso.with(this.context).load(url).into(new C09372(userId, imageView));
    }
}

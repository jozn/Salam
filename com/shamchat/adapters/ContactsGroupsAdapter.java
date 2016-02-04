package com.shamchat.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.appcompat.BuildConfig;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.shamchat.activity.ChatInitialForGroupChatActivity;
import com.shamchat.activity.ContactGroupsFragment;
import com.shamchat.activity.CreateGroupActivity;
import com.shamchat.activity.ProgressBarLoadingDialog;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.models.FriendGroup;
import java.util.Random;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class ContactsGroupsAdapter extends CursorAdapter {
    protected static final String TAG;
    private static final int VIEW_TYPE_COUNT = 3;
    private static final int VIEW_TYPE_GROUP_CONT = 2;
    private static final int VIEW_TYPE_GROUP_START = 1;
    private ContactGroupsFragment contactGroupsFragment;
    private String currentUserId;
    private int indexColName;
    private int indexUserId;
    private LayoutInflater mInflater;

    /* renamed from: com.shamchat.adapters.ContactsGroupsAdapter.1 */
    class C09501 implements OnClickListener {
        C09501() {
        }

        public final void onClick(View v) {
            ProgressBarLoadingDialog.getInstance().show(ContactsGroupsAdapter.this.contactGroupsFragment.getFragmentManager(), BuildConfig.VERSION_NAME);
            ContactsGroupsAdapter.this.onGroupClick(v, (String) v.getTag());
        }
    }

    /* renamed from: com.shamchat.adapters.ContactsGroupsAdapter.2 */
    class C09512 implements OnClickListener {
        C09512() {
        }

        public final void onClick(View v) {
            ProgressBarLoadingDialog.getInstance().show(ContactsGroupsAdapter.this.contactGroupsFragment.getFragmentManager(), BuildConfig.VERSION_NAME);
            ContactsGroupsAdapter.this.onGroupClick(v, (String) v.getTag());
        }
    }

    static {
        TAG = ContactsGroupsAdapter.class.getName();
    }

    public ContactsGroupsAdapter(Context context, Cursor cursor, ContactGroupsFragment contactGroupsFragment, String currentUserId) {
        super(context, cursor, (int) VIEW_TYPE_GROUP_CONT);
        this.mInflater = LayoutInflater.from(context);
        this.indexColName = cursor.getColumnIndex(FriendGroup.DB_NAME);
        this.indexUserId = cursor.getColumnIndex(FriendGroup.DB_ID);
        this.contactGroupsFragment = contactGroupsFragment;
        this.currentUserId = currentUserId;
    }

    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_GROUP_START;
        }
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        if (isNewGroup(cursor, position)) {
            return VIEW_TYPE_GROUP_START;
        }
        return VIEW_TYPE_GROUP_CONT;
    }

    private boolean isNewGroup(Cursor cursor, int position) {
        String newName = cursor.getString(this.indexColName);
        cursor.moveToPosition(position - 1);
        String oldName = cursor.getString(this.indexColName);
        cursor.moveToPosition(position);
        if (oldName.substring(0, VIEW_TYPE_GROUP_START).toUpperCase().equals(newName.substring(0, VIEW_TYPE_GROUP_START).toUpperCase())) {
            return false;
        }
        return true;
    }

    public void bindView(View view, Context context, Cursor cursor) {
        View containerContact;
        switch (((Integer) view.getTag()).intValue()) {
            case VIEW_TYPE_GROUP_START /*1*/:
                TextView textHeader = (TextView) view.findViewById(2131362164);
                try {
                    textHeader.setText(cursor.getString(this.indexColName).substring(0, VIEW_TYPE_GROUP_START));
                } catch (Exception e) {
                    textHeader.setText("`");
                }
                containerContact = view.findViewById(2131362250);
                containerContact.setTag(cursor.getString(this.indexUserId));
                containerContact.setOnClickListener(new C09501());
                break;
            case VIEW_TYPE_GROUP_CONT /*2*/:
                containerContact = view.findViewById(2131362250);
                containerContact.setTag(cursor.getString(this.indexUserId));
                containerContact.setOnClickListener(new C09512());
                break;
        }
        ((TextView) view.findViewById(2131362166)).setText(cursor.getString(this.indexColName));
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View viewResult;
        int nViewType = getItemViewType(cursor.getPosition());
        switch (nViewType) {
            case VIEW_TYPE_GROUP_START /*1*/:
                viewResult = this.mInflater.inflate(2130903166, parent, false);
                break;
            default:
                viewResult = this.mInflater.inflate(2130903164, parent, false);
                break;
        }
        ImageView img = (ImageView) viewResult.findViewById(2131362251);
        if (img != null) {
            switch (new Random().nextInt(7) + 0) {
                case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                    img.setImageResource(2130837738);
                    break;
                case VIEW_TYPE_GROUP_START /*1*/:
                    img.setImageResource(2130837739);
                    break;
                case VIEW_TYPE_GROUP_CONT /*2*/:
                    img.setImageResource(2130837740);
                    break;
                case VIEW_TYPE_COUNT /*3*/:
                    img.setImageResource(2130837741);
                    break;
                case Logger.CONFIG /*4*/:
                    img.setImageResource(2130837742);
                    break;
                case Logger.FINE /*5*/:
                    img.setImageResource(2130837743);
                    break;
                case Logger.FINER /*6*/:
                    img.setImageResource(2130837744);
                    break;
                default:
                    img.setImageResource(2130837738);
                    break;
            }
        }
        ImageView citContactImageView = (ImageView) viewResult.findViewById(2131362296);
        if (citContactImageView != null) {
            citContactImageView.setVisibility(8);
        }
        TextView statusImageView = (TextView) viewResult.findViewById(2131362252);
        if (statusImageView != null) {
            statusImageView.setVisibility(8);
        }
        viewResult.setTag(Integer.valueOf(nViewType));
        return viewResult;
    }

    protected void onGroupClick(View v, String groupId) {
        ContentResolver contentResolver = SHAMChatApplication.getMyApplicationContext().getContentResolver();
        Uri uri = UserProvider.CONTENT_URI_GROUP;
        String str = FriendGroup.DB_ID + "=?";
        String[] strArr = new String[VIEW_TYPE_GROUP_START];
        strArr[0] = groupId;
        Cursor cursor = contentResolver.query(uri, null, str, strArr, null);
        FriendGroup group = UserProvider.groupFromCursor(cursor);
        cursor.close();
        String chatRoomName = group.chatRoomName;
        if (chatRoomName.substring(VIEW_TYPE_GROUP_START, chatRoomName.indexOf("_")).equalsIgnoreCase(this.currentUserId)) {
            Intent intent = new Intent(v.getContext(), CreateGroupActivity.class);
            intent.putExtra(CreateGroupActivity.EXTRA_GROUP_ID, groupId);
            v.getContext().startActivity(intent);
            return;
        }
        ProgressBarLoadingDialog.getInstance().dismiss();
        intent = new Intent(v.getContext(), ChatInitialForGroupChatActivity.class);
        intent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, this.currentUserId + "-" + groupId);
        intent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_GROUP_ID, groupId);
        v.getContext().startActivity(intent);
    }

    protected void onOwnUserClick(View v) {
    }
}

package com.shamchat.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.appcompat.C0170R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.shamchat.activity.BlockFriendDetailActivity;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlockedFriendsCheckboxCursorAdapter extends SimpleCursorAdapter {
    private ArrayList<Boolean> arrayCheckbox;
    private Context context;
    private int indexBlockedStatus;
    private int indexName;
    private int indexRecordOwnerId;
    private UserProvider provider;
    private Map<String, Boolean> usersList;

    /* renamed from: com.shamchat.adapters.BlockedFriendsCheckboxCursorAdapter.1 */
    class C09331 implements OnClickListener {
        final /* synthetic */ int val$position;

        C09331(int i) {
            this.val$position = i;
        }

        public final void onClick(View v) {
            BlockedFriendsCheckboxCursorAdapter.this.arrayCheckbox.set(this.val$position, Boolean.valueOf(((CheckBox) v.findViewById(C0170R.id.checkbox)).isChecked()));
        }
    }

    /* renamed from: com.shamchat.adapters.BlockedFriendsCheckboxCursorAdapter.2 */
    class C09342 implements OnClickListener {
        final /* synthetic */ String val$friendId;
        final /* synthetic */ String val$username;

        C09342(String str, String str2) {
            this.val$username = str;
            this.val$friendId = str2;
        }

        public final void onClick(View v) {
            BlockedFriendsCheckboxCursorAdapter.this.openDetailView(this.val$username, this.val$friendId);
        }
    }

    public BlockedFriendsCheckboxCursorAdapter(Context context, Cursor cursor) {
        super(context, 2130903162, cursor, new String[]{"alias"}, new int[]{2131362166}, 2);
        this.arrayCheckbox = new ArrayList();
        this.usersList = new HashMap();
        this.context = context;
        this.indexRecordOwnerId = cursor.getColumnIndex("jid");
        this.indexName = cursor.getColumnIndex("alias");
        this.indexBlockedStatus = cursor.getColumnIndex("user_status");
        inicializeArrayCheckBox(cursor.getCount());
        this.provider = new UserProvider();
    }

    public void inicializeArrayCheckBox(int count) {
        this.arrayCheckbox.clear();
        for (int i = 0; i < count; i++) {
            this.arrayCheckbox.add(Boolean.valueOf(false));
        }
    }

    public View getView(int position, View view, ViewGroup parent) {
        boolean z = true;
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(2130903162, null);
        }
        TextView textName = (TextView) view.findViewById(2131362166);
        ImageView profileImage = (ImageView) view.findViewById(C0170R.id.image);
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        textName.setText(cursor.getString(this.indexName));
        CheckBox cBox = (CheckBox) view.findViewById(C0170R.id.checkbox);
        ArrayList arrayList = this.arrayCheckbox;
        if (cursor.getInt(this.indexBlockedStatus) != 1) {
            z = false;
        }
        arrayList.set(position, Boolean.valueOf(z));
        cBox.setOnClickListener(new C09331(position));
        cBox.setChecked(((Boolean) this.arrayCheckbox.get(position)).booleanValue());
        String username = cursor.getString(this.indexName);
        String friendId = Utils.getUserIdFromXmppUserId(cursor.getString(this.indexRecordOwnerId));
        Bitmap bitmap = UserProvider.getProfileImageByUserId(friendId);
        if (bitmap != null) {
            profileImage.setImageBitmap(bitmap);
        }
        view.setOnClickListener(new C09342(username, friendId));
        return view;
    }

    public Map<String, Boolean> getSelectedUsers() {
        Cursor cursor = getCursor();
        for (int i = 0; i < this.arrayCheckbox.size(); i++) {
            cursor.moveToPosition(i);
            this.usersList.put(cursor.getString(this.indexRecordOwnerId), this.arrayCheckbox.get(i));
        }
        return this.usersList;
    }

    private void openDetailView(String username, String friendId) {
        Intent intent = new Intent(this.context, BlockFriendDetailActivity.class);
        Bundle b = new Bundle();
        b.putString("USERNAME", username);
        b.putString("FRIENDID", friendId);
        intent.putExtras(b);
        this.context.startActivity(intent);
    }
}

package com.shamchat.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.appcompat.C0170R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.models.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.util.ArrayList;
import java.util.Iterator;

public class GroupCheckboxCursorAdapter extends SimpleCursorAdapter {
    private ArrayList<ContactItem> arrayContactItems;
    private Context context;
    ContactItem data;
    private int indexId;
    private int indexName;

    /* renamed from: com.shamchat.adapters.GroupCheckboxCursorAdapter.1 */
    class C09561 implements OnCheckedChangeListener {
        final /* synthetic */ String val$userId;

        C09561(String str) {
            this.val$userId = str;
        }

        public final void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            GroupCheckboxCursorAdapter.this.data = GroupCheckboxCursorAdapter.this.getItem(Integer.valueOf(this.val$userId).intValue());
            if (GroupCheckboxCursorAdapter.this.data == null) {
                GroupCheckboxCursorAdapter.this.data = new ContactItem(Integer.valueOf(this.val$userId).intValue());
                GroupCheckboxCursorAdapter.this.arrayContactItems.add(GroupCheckboxCursorAdapter.this.data);
            }
            GroupCheckboxCursorAdapter.this.data.checked = isChecked;
        }
    }

    /* renamed from: com.shamchat.adapters.GroupCheckboxCursorAdapter.2 */
    class C09572 implements OnClickListener {
        final /* synthetic */ CheckBox val$cBox;

        C09572(CheckBox checkBox) {
            this.val$cBox = checkBox;
        }

        public final void onClick(View v) {
            v.findViewById(C0170R.id.checkbox);
            if (this.val$cBox.isChecked()) {
                this.val$cBox.setChecked(false);
            } else {
                this.val$cBox.setChecked(true);
            }
        }
    }

    /* renamed from: com.shamchat.adapters.GroupCheckboxCursorAdapter.3 */
    class C09583 implements OnClickListener {
        final /* synthetic */ CheckBox val$cBox;

        C09583(CheckBox checkBox) {
            this.val$cBox = checkBox;
        }

        public final void onClick(View v) {
            if (this.val$cBox.isChecked()) {
                this.val$cBox.setChecked(false);
            } else {
                this.val$cBox.setChecked(true);
            }
        }
    }

    /* renamed from: com.shamchat.adapters.GroupCheckboxCursorAdapter.4 */
    class C09594 implements Target {
        final /* synthetic */ ImageView val$imageView;
        final /* synthetic */ String val$userId;

        C09594(String str, ImageView imageView) {
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

    public class ContactItem {
        boolean checked;
        int userid;

        public ContactItem(int userid) {
            this.userid = userid;
            this.checked = false;
        }
    }

    public GroupCheckboxCursorAdapter(Context context, Cursor cursor) {
        super(context, 2130903162, cursor, new String[]{"name"}, new int[]{2131362166}, 2);
        this.arrayContactItems = new ArrayList();
        this.context = context;
        this.indexId = cursor.getColumnIndex("userId");
        this.indexName = cursor.getColumnIndex("name");
    }

    public View getView(int position, View view, ViewGroup parent) {
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        view = LayoutInflater.from(this.context).inflate(2130903162, null);
        ImageView profileImageView = (ImageView) view.findViewById(C0170R.id.image);
        ((TextView) view.findViewById(2131362166)).setText(cursor.getString(this.indexName));
        String userId = cursor.getString(this.indexId);
        String profileUrl = cursor.getString(cursor.getColumnIndex("profileimage_url"));
        if (profileUrl == null || !profileUrl.startsWith("http://")) {
            profileImageView.setImageResource(2130837944);
        } else if (SHAMChatApplication.USER_IMAGES.containsKey(userId)) {
            profileImageView.setImageBitmap((Bitmap) SHAMChatApplication.USER_IMAGES.get(userId));
        } else {
            loadImage(userId, profileUrl, profileImageView);
        }
        this.data = getItem(Integer.valueOf(userId).intValue());
        if (this.data == null) {
            this.data = new ContactItem(Integer.valueOf(userId).intValue());
            this.arrayContactItems.add(this.data);
        }
        CheckBox cBox = (CheckBox) view.findViewById(C0170R.id.checkbox);
        LinearLayout lin = (LinearLayout) view.findViewById(2131362250);
        cBox.setOnCheckedChangeListener(null);
        lin.setOnClickListener(null);
        cBox.setOnCheckedChangeListener(new C09561(userId));
        lin.setOnClickListener(new C09572(cBox));
        lin.setOnClickListener(new C09583(cBox));
        cBox.setChecked(this.data.checked);
        return view;
    }

    public ArrayList<User> getSelectedUsers() {
        ContentResolver contentResolver = SHAMChatApplication.getMyApplicationContext().getContentResolver();
        ArrayList<User> contacts = new ArrayList();
        Cursor cursor = getCursor();
        for (int i = 0; i < this.arrayContactItems.size(); i++) {
            if (((ContactItem) this.arrayContactItems.get(i)).checked) {
                cursor.moveToPosition(i);
                Cursor cursor2 = contentResolver.query(UserProvider.CONTENT_URI_USER, null, "userId=?", new String[]{String.valueOf(((ContactItem) this.arrayContactItems.get(i)).userid)}, null);
                cursor2.moveToFirst();
                contacts.add(UserProvider.userFromCursor(cursor2));
                cursor2.close();
            }
        }
        return contacts;
    }

    public ContactItem getItem(int itemUserId) {
        Iterator it = this.arrayContactItems.iterator();
        while (it.hasNext()) {
            ContactItem c = (ContactItem) it.next();
            if (c.userid == itemUserId) {
                return c;
            }
        }
        return null;
    }

    private void loadImage(String userId, String url, ImageView imageView) {
        Picasso.with(this.context).load(url).into(new C09594(userId, imageView));
    }
}

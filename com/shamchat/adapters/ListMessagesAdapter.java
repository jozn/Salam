package com.shamchat.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.androidclient.data.ChatProviderNew.ChatDatabaseHelper;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.models.Message;
import com.shamchat.models.Message.MessageType;
import com.shamchat.models.User;
import com.shamchat.roundedimage.RoundedImageView;
import com.shamchat.utils.Utils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class ListMessagesAdapter extends BaseAdapter {
    private Context context;
    public List<Message> list;

    /* renamed from: com.shamchat.adapters.ListMessagesAdapter.1 */
    static /* synthetic */ class C09791 {
        static final /* synthetic */ int[] $SwitchMap$com$shamchat$models$Message$MessageType;

        static {
            $SwitchMap$com$shamchat$models$Message$MessageType = new int[MessageType.values().length];
            try {
                $SwitchMap$com$shamchat$models$Message$MessageType[MessageType.IMAGE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
        }
    }

    private static class ViewHolder {
        ImageView contentImage;
        TextView contentText;
        TextView date;
        TextView name;
        RoundedImageView profileImage;

        private ViewHolder() {
        }
    }

    public ListMessagesAdapter(Context context) {
        this.list = new ArrayList();
        this.context = context;
    }

    public final void refreshMessagesFromDB$7c629dcd(String userId) {
        Collection arrayList = new ArrayList();
        SQLiteDatabase writableDatabase = new ChatDatabaseHelper(SHAMChatApplication.getMyApplicationContext()).getWritableDatabase();
        Cursor query = writableDatabase.query("favorite", null, "user_id=? AND is_deleted=?", new String[]{userId, "0"}, null, null, "time DESC");
        if (query.moveToFirst()) {
            do {
                arrayList.add(ChatProviderNew.favoriteToCursor(query));
            } while (query.moveToNext());
        }
        query.close();
        writableDatabase.close();
        this.list.clear();
        this.list.addAll(arrayList);
        notifyDataSetChanged();
    }

    public final int getCount() {
        return this.list.size();
    }

    private Message getItem(int position) {
        return (Message) this.list.get(position);
    }

    public final long getItemId(int position) {
        return (long) position;
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int type = getItemViewType(position);
        Message message = getItem(position);
        Cursor cursor = this.context.getContentResolver().query(UserProvider.CONTENT_URI_USER, null, "userId=?", new String[]{message.userId}, null);
        cursor.moveToFirst();
        User user = UserProvider.userFromCursor(cursor);
        cursor.close();
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(2130903163, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.date = (TextView) convertView.findViewById(2131362282);
            holder.name = (TextView) convertView.findViewById(2131362293);
            holder.profileImage = (RoundedImageView) convertView.findViewById(2131361986);
            View stub;
            if (type == 1) {
                stub = convertView.findViewById(2131362294);
                if (stub != null) {
                    ((ViewStub) stub).setVisibility(0);
                }
                holder.contentImage = (ImageView) convertView.findViewById(2131362268);
            } else {
                stub = convertView.findViewById(2131362295);
                if (stub != null) {
                    ((ViewStub) stub).setVisibility(0);
                }
                holder.contentText = (TextView) convertView.findViewById(2131362275);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (type == 1) {
            Message.loadAsyncImageContent$78bd0eef();
        } else {
            holder.contentText.setText(message.messageContent);
        }
        try {
            holder.name.setText(user.username);
        } catch (Exception e) {
            holder.name.setText("no name");
        }
        if (user.profileImage == null || user.profileImage.equalsIgnoreCase("null")) {
            holder.profileImage.setImageResource(2130837944);
        } else {
            holder.profileImage.setImageBitmap(Utils.base64ToBitmap(user.profileImage));
        }
        TextView textView = holder.date;
        Context context = this.context;
        long j = message.time;
        long currentTimeMillis = System.currentTimeMillis() - j;
        CharSequence string = currentTimeMillis < 86400000 ? context.getResources().getString(2131493423) : currentTimeMillis < 172800000 ? context.getResources().getString(2131493446) : new SimpleDateFormat("d MMM kk.mm", Locale.getDefault()).format(new Date(j));
        textView.setText(string);
        if (user.profileImage != null && user.profileImage.length() > 0) {
            holder.profileImage.setImageBitmap(Utils.base64ToBitmap(user.profileImage));
        }
        return convertView;
    }

    public final int getViewTypeCount() {
        return 2;
    }

    public final int getItemViewType(int position) {
        switch (C09791.$SwitchMap$com$shamchat$models$Message$MessageType[((Message) this.list.get(position)).type.ordinal()]) {
            case Logger.SEVERE /*1*/:
                return 1;
            default:
                return 0;
        }
    }
}

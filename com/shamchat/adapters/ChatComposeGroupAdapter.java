package com.shamchat.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.appcompat.BuildConfig;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.shamchat.activity.ChatInitialForGroupChatActivity;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.models.FriendGroup;
import java.util.Random;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class ChatComposeGroupAdapter extends CursorAdapter {
    protected static final String TAG;
    private static final int VIEW_TYPE_COUNT = 3;
    private static final int VIEW_TYPE_GROUP_CONT = 1;
    private static final int VIEW_TYPE_GROUP_START = 0;
    private static final int VIEW_TYPE_OWN_USER = 2;
    private int indexColName;
    private int indexGroupiD;
    private LayoutInflater mInflater;
    private boolean showYou;

    /* renamed from: com.shamchat.adapters.ChatComposeGroupAdapter.1 */
    class C09351 implements OnClickListener {
        C09351() {
        }

        public final void onClick(View v) {
            ChatComposeGroupAdapter.this.onUserClick(v, (String) v.getTag());
        }
    }

    static {
        TAG = ChatComposeGroupAdapter.class.getName();
    }

    public ChatComposeGroupAdapter(Context context, Cursor cursor) {
        this(context, cursor, true);
    }

    public ChatComposeGroupAdapter(Context context, Cursor cursor, boolean showYou) {
        super(context, cursor, (int) VIEW_TYPE_OWN_USER);
        this.showYou = showYou;
        this.mInflater = LayoutInflater.from(context);
        this.indexColName = cursor.getColumnIndex(FriendGroup.DB_NAME);
        this.indexGroupiD = cursor.getColumnIndex(FriendGroup.DB_ID);
    }

    public int getViewTypeCount() {
        return this.showYou ? VIEW_TYPE_COUNT : VIEW_TYPE_OWN_USER;
    }

    public int getItemViewType(int position) {
        if (position != 0) {
            Cursor cursor = getCursor();
            cursor.moveToPosition(position);
            if (isNewGroup(cursor, position)) {
                return VIEW_TYPE_GROUP_START;
            }
            return VIEW_TYPE_GROUP_CONT;
        } else if (this.showYou) {
            return VIEW_TYPE_OWN_USER;
        } else {
            return VIEW_TYPE_GROUP_START;
        }
    }

    private boolean isNewGroup(Cursor cursor, int position) {
        if (position == 0) {
            return true;
        }
        String newName = cursor.getString(this.indexColName);
        cursor.moveToPosition(position - 1);
        String oldName = cursor.getString(this.indexColName);
        cursor.moveToPosition(position);
        if (oldName.substring(VIEW_TYPE_GROUP_START, VIEW_TYPE_GROUP_CONT).toUpperCase().equals(newName.substring(VIEW_TYPE_GROUP_START, VIEW_TYPE_GROUP_CONT).toUpperCase())) {
            return false;
        }
        return true;
    }

    public void bindView(View view, Context context, Cursor cursor) {
        int tag = ((Integer) view.getTag()).intValue();
        if (tag == VIEW_TYPE_OWN_USER || tag == 0) {
            TextView textHeader = (TextView) view.findViewById(2131362164);
            if (cursor.getString(this.indexColName) == null || cursor.getString(this.indexColName).equals(BuildConfig.VERSION_NAME) || cursor.getString(this.indexColName).length() <= 0) {
                textHeader.setText(2131493229);
            } else {
                textHeader.setText(cursor.getString(this.indexColName).substring(VIEW_TYPE_GROUP_START, VIEW_TYPE_GROUP_CONT));
            }
        }
        View containerContact = view.findViewById(2131362250);
        containerContact.setTag(cursor.getString(this.indexGroupiD));
        containerContact.setOnClickListener(new C09351());
        ImageView img = (ImageView) view.findViewById(2131362251);
        if (img != null) {
            switch (new Random().nextInt(7) + VIEW_TYPE_GROUP_START) {
                case VIEW_TYPE_GROUP_START /*0*/:
                    img.setImageResource(2130837738);
                    break;
                case VIEW_TYPE_GROUP_CONT /*1*/:
                    img.setImageResource(2130837739);
                    break;
                case VIEW_TYPE_OWN_USER /*2*/:
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
        ImageView citContactImageView = (ImageView) view.findViewById(2131362296);
        if (citContactImageView != null) {
            citContactImageView.setVisibility(8);
        }
        TextView statusImageView = (TextView) view.findViewById(2131362252);
        if (statusImageView != null) {
            statusImageView.setVisibility(8);
        }
        ((TextView) view.findViewById(2131362166)).setText(cursor.getString(this.indexColName));
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layout;
        int viewType = getItemViewType(cursor.getPosition());
        switch (viewType) {
            case VIEW_TYPE_GROUP_START /*0*/:
                layout = 2130903166;
                break;
            default:
                layout = 2130903164;
                break;
        }
        View viewResult = this.mInflater.inflate(layout, parent, false);
        viewResult.setTag(Integer.valueOf(viewType));
        return viewResult;
    }

    protected void onUserClick(View v, String groupId) {
        Context context = SHAMChatApplication.getMyApplicationContext();
        Intent intent = new Intent(context, ChatInitialForGroupChatActivity.class);
        intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
        intent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, SHAMChatApplication.getConfig().userId + "-" + groupId);
        intent.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_GROUP_ID, groupId);
        context.startActivity(intent);
    }
}

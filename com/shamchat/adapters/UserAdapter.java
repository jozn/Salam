package com.shamchat.adapters;

import android.content.Context;
import android.support.v4.media.TransportMediator;
import android.support.v7.appcompat.C0170R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.rokhgroup.utils.RokhgroupRestClient;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.models.User;
import com.squareup.picasso.Picasso;
import cz.msebera.android.httpclient.Header;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public final class UserAdapter extends BaseAdapter {
    private Context context;
    public boolean editMode;
    String imageUrl;
    public List<User> list;

    /* renamed from: com.shamchat.adapters.UserAdapter.1 */
    class C10131 implements OnCheckedChangeListener {
        final /* synthetic */ User val$user;

        C10131(User user) {
            this.val$user = user;
        }

        public final void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            this.val$user.checked = isChecked;
        }
    }

    /* renamed from: com.shamchat.adapters.UserAdapter.2 */
    class C10142 extends JsonHttpResponseHandler {
        C10142() {
        }

        public final void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            if (response != null) {
                try {
                    UserAdapter.this.imageUrl = "http://social.rabtcdn.com" + response.getString("avatar");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class ViewHolder {
        CheckBox checkbox;
        ImageView image;
        TextView name;

        private ViewHolder() {
        }
    }

    public UserAdapter(Context context) {
        this.list = new ArrayList();
        this.imageUrl = null;
        this.context = context;
    }

    public final void addUsers(ArrayList<User> users) {
        this.list.addAll(users);
        setCheckboxes$1385ff();
        notifyDataSetChanged();
    }

    public final void setCheckboxes$1385ff() {
        for (User user : this.list) {
            user.checked = true;
        }
    }

    public final int getCount() {
        return this.list.size();
    }

    private User getItem(int position) {
        return (User) this.list.get(position);
    }

    public final long getItemId(int position) {
        return (long) position;
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(this.context).inflate(2130903158, null);
        ViewHolder holder = new ViewHolder();
        holder.name = (TextView) convertView.findViewById(2131362166);
        holder.image = (ImageView) convertView.findViewById(C0170R.id.image);
        holder.checkbox = (CheckBox) convertView.findViewById(C0170R.id.checkbox);
        convertView.setTag(holder);
        User user = getItem(position);
        if (this.editMode) {
            holder.checkbox.setVisibility(0);
        } else {
            holder.checkbox.setVisibility(8);
        }
        holder.checkbox.setChecked(user.checked);
        holder.checkbox.setOnCheckedChangeListener(new C10131(user));
        holder.name.setText(user.username);
        String profileUrl = user.profileImageUrl;
        String userId = user.userId;
        if (profileUrl == null || !profileUrl.startsWith("http://")) {
            holder.image.setImageResource(2130837944);
        } else {
            RokhgroupRestClient.get$6a529083("http://social.rabtcdn.com/pin/api/get/jid/avatar/" + userId, new C10142());
            Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(this.imageUrl).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).into(holder.image, null);
        }
        return convertView;
    }

    public final User[] getUsers() {
        return (User[]) this.list.toArray(new User[0]);
    }
}

package com.shamchat.adapters;

import android.content.Context;
import android.support.v7.appcompat.C0170R;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filter.FilterResults;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.shamchat.activity.ShareIntentChatActivity;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.models.ShareIntentItem;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class ShareIntentChatAdapter extends BaseAdapter implements Filterable {
    private ShareIntentChatActivity baseActivity;
    private Context context;
    private ArrayList<ShareIntentItem> mDisplayedValues;
    private LayoutInflater mInflater;
    private ArrayList<ShareIntentItem> mOriginalValues;

    /* renamed from: com.shamchat.adapters.ShareIntentChatAdapter.2 */
    class C10072 implements OnClickListener {
        final /* synthetic */ int val$position;

        C10072(int i) {
            this.val$position = i;
        }

        public final void onClick(View v) {
            if (((ShareIntentItem) ShareIntentChatAdapter.this.mDisplayedValues.get(this.val$position)).isGroup) {
                String threadOwner = SHAMChatApplication.getConfig().userId;
                String groupId = ((ShareIntentItem) ShareIntentChatAdapter.this.mDisplayedValues.get(this.val$position)).userIdOrGroupId;
                ShareIntentChatAdapter.this.baseActivity.sendToChat(groupId, threadOwner + "-" + groupId, true);
                return;
            }
            ShareIntentChatAdapter.this.baseActivity.createChatSingle(((ShareIntentItem) ShareIntentChatAdapter.this.mDisplayedValues.get(this.val$position)).userIdOrGroupId);
        }
    }

    /* renamed from: com.shamchat.adapters.ShareIntentChatAdapter.3 */
    class C10083 implements OnClickListener {
        final /* synthetic */ int val$position;

        C10083(int i) {
            this.val$position = i;
        }

        public final void onClick(View v) {
            if (((ShareIntentItem) ShareIntentChatAdapter.this.mDisplayedValues.get(this.val$position)).isGroup) {
                String threadOwner = SHAMChatApplication.getConfig().userId;
                String groupId = ((ShareIntentItem) ShareIntentChatAdapter.this.mDisplayedValues.get(this.val$position)).userIdOrGroupId;
                ShareIntentChatAdapter.this.baseActivity.sendToChat(groupId, threadOwner + "-" + groupId, true);
                return;
            }
            ShareIntentChatAdapter.this.baseActivity.createChatSingle(((ShareIntentItem) ShareIntentChatAdapter.this.mDisplayedValues.get(this.val$position)).userIdOrGroupId);
        }
    }

    /* renamed from: com.shamchat.adapters.ShareIntentChatAdapter.4 */
    class C10094 extends Filter {
        C10094() {
        }

        protected final void publishResults(CharSequence constraint, FilterResults results) {
            ShareIntentChatAdapter.this.mDisplayedValues = (ArrayList) results.values;
            ShareIntentChatAdapter.this.notifyDataSetChanged();
        }

        protected final FilterResults performFiltering(CharSequence constraint) {
            Log.i("intent", "search word:" + String.valueOf(constraint));
            FilterResults results = new FilterResults();
            ArrayList<ShareIntentItem> FilteredArrList = new ArrayList();
            if (ShareIntentChatAdapter.this.mOriginalValues == null) {
                ShareIntentChatAdapter.this.mOriginalValues = new ArrayList(ShareIntentChatAdapter.this.mDisplayedValues);
            }
            if (constraint == null || constraint.length() == 0) {
                results.count = ShareIntentChatAdapter.this.mOriginalValues.size();
                results.values = ShareIntentChatAdapter.this.mOriginalValues;
            } else {
                constraint = constraint.toString().toLowerCase(Locale.getDefault());
                for (int i = 0; i < ShareIntentChatAdapter.this.mOriginalValues.size(); i++) {
                    if (((ShareIntentItem) ShareIntentChatAdapter.this.mOriginalValues.get(i)).toString().toLowerCase(Locale.getDefault()).contains(constraint)) {
                        ShareIntentItem item = new ShareIntentItem(((ShareIntentItem) ShareIntentChatAdapter.this.mOriginalValues.get(i)).phoneNumberOrGroupAlias, ((ShareIntentItem) ShareIntentChatAdapter.this.mOriginalValues.get(i)).userIdOrGroupId, ((ShareIntentItem) ShareIntentChatAdapter.this.mOriginalValues.get(i)).isGroup);
                        item.displayName = ((ShareIntentItem) ShareIntentChatAdapter.this.mOriginalValues.get(i)).displayName;
                        FilteredArrList.add(item);
                    }
                }
                results.count = FilteredArrList.size();
                results.values = FilteredArrList;
            }
            return results;
        }
    }

    private class ViewHolder {
        CheckBox check;
        TextView displayName;
        ImageView profileImageView;
        ImageView useSalamImageView;

        private ViewHolder() {
        }
    }

    public ShareIntentChatAdapter(Context context, ArrayList<ShareIntentItem> listItemsArray, ShareIntentChatActivity shareIntentChatActivity) {
        this.mOriginalValues = listItemsArray;
        this.mDisplayedValues = listItemsArray;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.baseActivity = shareIntentChatActivity;
    }

    public final int getCount() {
        Log.i("intent", "count of filtered values:" + String.valueOf(this.mDisplayedValues.size()));
        return this.mDisplayedValues.size();
    }

    public final Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public final long getItemId(int position) {
        return (long) position;
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.mInflater.inflate(2130903152, null);
            holder.useSalamImageView = (ImageView) convertView.findViewById(2131362167);
            holder.profileImageView = (ImageView) convertView.findViewById(C0170R.id.image);
            holder.displayName = (TextView) convertView.findViewById(2131362166);
            holder.check = (CheckBox) convertView.findViewById(C0170R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.displayName.setText(((ShareIntentItem) this.mDisplayedValues.get(position)).displayName);
        if (((ShareIntentItem) this.mDisplayedValues.get(position)).isGroup) {
            switch (new Random().nextInt(7) + 0) {
                case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                    holder.profileImageView.setImageResource(2130837738);
                    break;
                case Logger.SEVERE /*1*/:
                    holder.profileImageView.setImageResource(2130837739);
                    break;
                case Logger.WARNING /*2*/:
                    holder.profileImageView.setImageResource(2130837740);
                    break;
                case Logger.INFO /*3*/:
                    holder.profileImageView.setImageResource(2130837741);
                    break;
                case Logger.CONFIG /*4*/:
                    holder.profileImageView.setImageResource(2130837742);
                    break;
                case Logger.FINE /*5*/:
                    holder.profileImageView.setImageResource(2130837743);
                    break;
                case Logger.FINER /*6*/:
                    holder.profileImageView.setImageResource(2130837744);
                    break;
                default:
                    holder.profileImageView.setImageResource(2130837738);
                    break;
            }
        }
        holder.profileImageView.setImageResource(2130837944);
        holder.check.setVisibility(4);
        holder.displayName.setOnClickListener(new C10072(position));
        convertView.setOnClickListener(new C10083(position));
        return convertView;
    }

    public final Filter getFilter() {
        return new C10094();
    }
}

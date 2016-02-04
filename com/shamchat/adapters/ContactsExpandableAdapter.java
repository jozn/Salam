package com.shamchat.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.media.TransportMediator;
import android.support.v7.appcompat.BuildConfig;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.shamchat.activity.MyProfileActivity;
import com.shamchat.activity.ProfileActivity;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.models.ContactFriend;
import com.shamchat.utils.Utils;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;

public final class ContactsExpandableAdapter extends BaseExpandableListAdapter {
    private List<ArrayList<ContactFriend>> allContacts;
    private Context context;
    private LayoutInflater inflater;

    /* renamed from: com.shamchat.adapters.ContactsExpandableAdapter.1 */
    class C09491 implements OnClickListener {
        final /* synthetic */ ContactFriend val$friend;
        final /* synthetic */ int val$i;

        C09491(int i, ContactFriend contactFriend) {
            this.val$i = i;
            this.val$friend = contactFriend;
        }

        public final void onClick(View v) {
            if (this.val$i == 0) {
                Intent i = new Intent(ContactsExpandableAdapter.this.context, MyProfileActivity.class);
                i.putExtra(ProfileActivity.EXTRA_USER_ID, this.val$friend.userId);
                i.setFlags(ClientDefaults.MAX_MSG_SIZE);
                ContactsExpandableAdapter.this.context.startActivity(i);
                return;
            }
            ContactsExpandableAdapter.onUserClick(v, (String) v.getTag());
        }
    }

    public class ContactViewHolderEx {
        public TextView name;
        public ImageView profileImageView;
        public TextView textComment;
    }

    public ContactsExpandableAdapter(Context context, List<ArrayList<ContactFriend>> allContacts) {
        this.inflater = (LayoutInflater) SHAMChatApplication.getMyApplicationContext().getSystemService("layout_inflater");
        this.allContacts = allContacts;
        this.context = context;
    }

    public final View getGroupView(int i, boolean b, View view, ViewGroup parent) {
        view = this.inflater.inflate(2130903167, parent, false);
        ((TextView) view.findViewById(2131362164)).setText(((ContactFriend) getGroup(i).get(0)).userStartingLetter);
        return view;
    }

    public final View getChildView(int i, int i2, boolean b, View child, ViewGroup parent) {
        ContactFriend friend = getChild(i, i2);
        String name = friend.userName;
        if (name.indexOf(MqttTopic.TOPIC_LEVEL_SEPARATOR) != -1) {
            name = name.substring(0, name.indexOf(MqttTopic.TOPIC_LEVEL_SEPARATOR));
            System.out.println("REFRESH get chicl " + name);
        } else {
            System.out.println("REFRESH get chicl ELSE " + name);
        }
        child = this.inflater.inflate(2130903164, parent, false);
        ContactViewHolderEx holder = new ContactViewHolderEx();
        holder.name = (TextView) child.findViewById(2131362166);
        holder.textComment = (TextView) child.findViewById(2131362252);
        holder.profileImageView = (ImageView) child.findViewById(2131362251);
        child.setTag(holder);
        View containerContact = child.findViewById(2131362250);
        containerContact.setTag(friend.userId);
        containerContact.setOnClickListener(new C09491(i, friend));
        String status = friend.status;
        if (status == null) {
            status = BuildConfig.VERSION_NAME;
        }
        holder.textComment.setText(status.replace("null", BuildConfig.VERSION_NAME));
        System.out.println("REFRESH about to set " + name);
        holder.name.setText(name);
        String profileImageUrl;
        Bitmap bitmap;
        if (i == 0) {
            profileImageUrl = friend.profileImg;
            System.out.println("Profile My  image " + profileImageUrl);
            bitmap = (Bitmap) SHAMChatApplication.USER_IMAGES.get(friend.userId);
            if (bitmap != null) {
                System.out.println("Image in the map");
                holder.profileImageView.setImageBitmap(bitmap);
                if (profileImageUrl != null && profileImageUrl.contains("http://")) {
                    Utils.handleProfileImage(this.context, friend.userId, profileImageUrl);
                }
            } else {
                System.out.println("Image not in the map");
                if (profileImageUrl == null || !profileImageUrl.contains("http://")) {
                    bitmap = Utils.base64ToBitmap(friend.profileImg);
                    if (bitmap != null) {
                        holder.profileImageView.setImageBitmap(bitmap);
                        SHAMChatApplication.USER_IMAGES.put(friend.userId, bitmap);
                    } else {
                        holder.profileImageView.setImageResource(2130837944);
                    }
                } else {
                    Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(Uri.parse(profileImageUrl)).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).into(holder.profileImageView, null);
                }
            }
        } else {
            profileImageUrl = friend.profileImg;
            System.out.println("Profile image " + profileImageUrl);
            bitmap = (Bitmap) SHAMChatApplication.USER_IMAGES.get(friend.userId);
            if (bitmap != null) {
                System.out.println("Image in the map");
                holder.profileImageView.setImageBitmap(bitmap);
                Utils.handleProfileImage(this.context, friend.userId, profileImageUrl);
            } else {
                System.out.println("Image not in the map");
                if (profileImageUrl == null || !profileImageUrl.contains("http://")) {
                    holder.profileImageView.setImageResource(2130837944);
                } else {
                    Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(Uri.parse(profileImageUrl)).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).into(holder.profileImageView, null);
                    Utils.handleProfileImage(this.context, friend.userId, profileImageUrl);
                    Utils.handleProfileImage(this.context, friend.userId, profileImageUrl);
                }
            }
        }
        return child;
    }

    public final boolean isChildSelectable(int i, int i2) {
        return true;
    }

    public final int getGroupCount() {
        return this.allContacts.size();
    }

    public final int getChildrenCount(int i) {
        if (this.allContacts.size() > 0) {
            return ((ArrayList) this.allContacts.get(i)).size();
        }
        return 0;
    }

    private ArrayList<ContactFriend> getGroup(int i) {
        return (ArrayList) this.allContacts.get(i);
    }

    private ContactFriend getChild(int i, int i2) {
        System.out.println("GET CHILD VALUE i " + i + " i2 " + i2 + " all con " + this.allContacts.size());
        return (ContactFriend) ((ArrayList) this.allContacts.get(i)).get(i2);
    }

    public final long getGroupId(int i) {
        return (long) i;
    }

    public final long getChildId(int i, int i2) {
        return (long) i2;
    }

    public final boolean hasStableIds() {
        return true;
    }

    protected static void onUserClick(View v, String friendId) {
        Intent intent = new Intent(v.getContext(), ProfileActivity.class);
        intent.putExtra(ProfileActivity.EXTRA_USER_ID, friendId);
        intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
        v.getContext().startActivity(intent);
    }
}

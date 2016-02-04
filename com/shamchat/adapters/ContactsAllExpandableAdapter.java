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
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;

public final class ContactsAllExpandableAdapter extends BaseExpandableListAdapter {
    private List<ArrayList<ContactFriend>> allContacts;
    private Context context;
    private LayoutInflater inflater;

    /* renamed from: com.shamchat.adapters.ContactsAllExpandableAdapter.1 */
    class C09481 implements OnClickListener {
        final /* synthetic */ int val$i;

        C09481(int i) {
            this.val$i = i;
        }

        public final void onClick(View v) {
            if (this.val$i == 0) {
                Intent i = new Intent(ContactsAllExpandableAdapter.this.context, MyProfileActivity.class);
                i.setFlags(ClientDefaults.MAX_MSG_SIZE);
                ContactsAllExpandableAdapter.this.context.startActivity(i);
                return;
            }
            ContactsAllExpandableAdapter.onUserClick(v, (String) v.getTag());
        }
    }

    public class ContactViewHolderEx {
        public TextView name;
        public ImageView profileImageView;
        public ImageView shamContact;
        public TextView textComment;
    }

    public ContactsAllExpandableAdapter(Context context, List<ArrayList<ContactFriend>> allContacts) {
        this.inflater = (LayoutInflater) SHAMChatApplication.getMyApplicationContext().getSystemService("layout_inflater");
        this.context = context;
        this.allContacts = allContacts;
    }

    public final View getGroupView(int i, boolean b, View view, ViewGroup parent) {
        view = this.inflater.inflate(2130903167, parent, false);
        ((TextView) view.findViewById(2131362164)).setText(((ContactFriend) getGroup(i).get(0)).userStartingLetter);
        return view;
    }

    public final View getChildView(int i, int i2, boolean b, View child, ViewGroup parent) {
        String profileImageUrl;
        ContactFriend friend = getChild(i, i2);
        String name = friend.userName;
        child = this.inflater.inflate(2130903164, parent, false);
        ContactViewHolderEx holder = new ContactViewHolderEx();
        holder.name = (TextView) child.findViewById(2131362166);
        holder.textComment = (TextView) child.findViewById(2131362252);
        holder.profileImageView = (ImageView) child.findViewById(2131362251);
        holder.shamContact = (ImageView) child.findViewById(2131362296);
        child.setTag(holder);
        View containerContact = child.findViewById(2131362250);
        containerContact.setTag(friend.userId);
        containerContact.setOnClickListener(new C09481(i));
        String status = friend.status;
        if (status == null) {
            status = BuildConfig.VERSION_NAME;
        }
        holder.textComment.setText(status.replace("null", BuildConfig.VERSION_NAME));
        holder.name.setText(name);
        Bitmap bitmap;
        if (i == 0) {
            profileImageUrl = friend.profileImg;
            System.out.println("Profile My  image " + profileImageUrl);
            if (profileImageUrl == null || !profileImageUrl.contains("http://")) {
                bitmap = Utils.base64ToBitmap(friend.profileImg);
                if (bitmap != null) {
                    holder.profileImageView.setImageBitmap(bitmap);
                } else {
                    holder.profileImageView.setImageResource(2130837944);
                }
            } else {
                Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(Uri.parse(profileImageUrl)).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).into(holder.profileImageView, null);
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
                }
            }
        }
        if (friend.inChat == 2) {
            holder.shamContact.setVisibility(0);
        } else {
            holder.shamContact.setVisibility(4);
            if (profileImageUrl != null) {
                System.out.println("FOUND THE ISSUEE " + friend.inChat + " " + profileImageUrl);
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
        return ((ArrayList) this.allContacts.get(i)).size();
    }

    private ArrayList<ContactFriend> getGroup(int i) {
        return (ArrayList) this.allContacts.get(i);
    }

    private ContactFriend getChild(int i, int i2) {
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

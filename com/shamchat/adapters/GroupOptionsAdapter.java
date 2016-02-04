package com.shamchat.adapters;

import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.media.TransportMediator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.jobs.RoomKickJob;
import com.shamchat.models.FriendGroupMember;
import com.shamchat.utils.Utils;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public final class GroupOptionsAdapter extends BaseAdapter {
    private Builder builder;
    private ContentResolver contentResolver;
    Context context;
    private String currentUserId;
    private FragmentManager fragmentManager;
    private String groupID;
    public List<FriendGroupMember> groupMembers;
    ViewHolder holder;
    private ImageLoader imageLoader;
    private boolean isNonContact;
    private boolean isUserAdmin;
    private UserProvider provider;
    private String thumbnailPath;
    private DisplayImageOptions userImageOptions;

    /* renamed from: com.shamchat.adapters.GroupOptionsAdapter.1 */
    class C09621 implements OnClickListener {
        final /* synthetic */ FriendGroupMember val$groupMember;
        final /* synthetic */ int val$position;

        /* renamed from: com.shamchat.adapters.GroupOptionsAdapter.1.1 */
        class C09601 implements DialogInterface.OnClickListener {
            C09601() {
            }

            public final void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }

        /* renamed from: com.shamchat.adapters.GroupOptionsAdapter.1.2 */
        class C09612 implements DialogInterface.OnClickListener {
            C09612() {
            }

            public final void onClick(DialogInterface arg0, int arg1) {
                SHAMChatApplication.getInstance().jobManager.addJobInBackground(new RoomKickJob(SHAMChatApplication.getConfig().userId, C09621.this.val$groupMember.friendId, GroupOptionsAdapter.this.groupID, C09621.this.val$position));
            }
        }

        C09621(FriendGroupMember friendGroupMember, int i) {
            this.val$groupMember = friendGroupMember;
            this.val$position = i;
        }

        public final void onClick(View v) {
            GroupOptionsAdapter.this.builder.setTitle(SHAMChatApplication.getMyApplicationContext().getResources().getString(2131493310)).setMessage(SHAMChatApplication.getMyApplicationContext().getResources().getString(2131493341)).setCancelable(false).setPositiveButton(SHAMChatApplication.getMyApplicationContext().getResources().getString(2131493349), new C09612()).setNegativeButton(SHAMChatApplication.getMyApplicationContext().getResources().getString(2131493334), new C09601());
            GroupOptionsAdapter.this.builder.create().show();
        }
    }

    /* renamed from: com.shamchat.adapters.GroupOptionsAdapter.2 */
    class C09632 implements OnClickListener {
        C09632() {
        }

        public final void onClick(View v) {
        }
    }

    public static class ViewHolder {
        ImageView kick;
        LinearLayout linear_contact_list_item;
        ImageView profPic;
        TextView status;
        TextView username;
    }

    public GroupOptionsAdapter(Context context, List<FriendGroupMember> groupMembers, ContentResolver contentResolver, FragmentManager fragmentManager, boolean isUserAdmin, String groupid) {
        this.imageLoader = ImageLoader.getInstance();
        this.context = context;
        this.groupMembers = groupMembers;
        this.contentResolver = contentResolver;
        this.provider = new UserProvider();
        this.currentUserId = SHAMChatApplication.getConfig().userId;
        this.thumbnailPath = new File(Environment.getExternalStorageDirectory() + "/salam/thumbnails").getAbsolutePath();
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.cacheInMemory = true;
        builder.imageScaleType$641b8ab2 = ImageScaleType.EXACTLY_STRETCHED$641b8ab2;
        builder.imageResOnLoading = 2130837944;
        builder.imageResOnFail = 2130837944;
        builder.imageResForEmptyUri = 2130837944;
        this.userImageOptions = builder.bitmapConfig(Config.ALPHA_8).build();
        this.fragmentManager = fragmentManager;
        this.isUserAdmin = isUserAdmin;
        this.groupID = groupid;
        this.builder = new Builder(this.context);
    }

    public final int getCount() {
        return this.groupMembers.size();
    }

    public final Object getItem(int position) {
        return null;
    }

    public final long getItemId(int position) {
        return 0;
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        String username;
        FriendGroupMember groupMember = (FriendGroupMember) this.groupMembers.get(position);
        Cursor cursor = this.contentResolver.query(Uri.parse(UserProvider.CONTENT_URI_USER.toString() + MqttTopic.TOPIC_LEVEL_SEPARATOR + groupMember.friendId), new String[]{"name", "user_type"}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            username = cursor.getString(cursor.getColumnIndex("name"));
            if (cursor.getInt(cursor.getColumnIndex("user_type")) != 1) {
                this.isNonContact = false;
            } else {
                this.isNonContact = true;
            }
        } else {
            username = new StringBuilder(MqttTopic.SINGLE_LEVEL_WILDCARD).append(groupMember.phoneNumber).toString();
        }
        cursor.close();
        this.holder = new ViewHolder();
        View view = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(2130903147, parent, false);
        this.holder.profPic = (ImageView) view.findViewById(2131362251);
        if (groupMember.user != null) {
            Bitmap bitmap = (Bitmap) SHAMChatApplication.USER_IMAGES.get(groupMember.user.userId);
            String myProfileImageUrl = groupMember.user.profileImageUrl;
            if (bitmap != null) {
                System.out.println("Image in the map");
                this.holder.profPic.setImageBitmap(bitmap);
            } else if (myProfileImageUrl == null || !myProfileImageUrl.contains("http://")) {
                this.holder.profPic.setImageResource(2130837944);
            } else {
                Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(Uri.parse(myProfileImageUrl)).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).into(this.holder.profPic, null);
                Utils.handleProfileImage(this.context, groupMember.user.userId, myProfileImageUrl);
            }
        } else {
            this.holder.profPic.setImageResource(2130837944);
        }
        this.holder.username = (TextView) view.findViewById(2131362166);
        this.holder.username.setText(username);
        this.holder.status = (TextView) view.findViewById(2131362252);
        this.holder.status.setText("Group Admin");
        this.holder.kick = (ImageView) view.findViewById(2131362253);
        this.holder.linear_contact_list_item = (LinearLayout) view.findViewById(2131362250);
        if (groupMember.isAdmin) {
            this.holder.status.setVisibility(0);
        } else {
            this.holder.status.setVisibility(8);
        }
        if (this.isUserAdmin) {
            if (groupMember.isAdmin) {
                this.holder.kick.setVisibility(8);
            } else {
                this.holder.kick.setVisibility(0);
            }
            if (!groupMember.didJoin) {
                try {
                    this.holder.linear_contact_list_item.setAlpha(0.4f);
                } catch (NoSuchMethodError e) {
                }
            }
        } else {
            this.holder.kick.setVisibility(8);
            try {
                this.holder.linear_contact_list_item.setAlpha(1.0f);
            } catch (NoSuchMethodError e2) {
            }
        }
        this.holder.kick.setOnClickListener(new C09621(groupMember, position));
        this.holder.profPic.setOnClickListener(new C09632());
        return view;
    }
}

package com.shamchat.adapters;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.media.TransportMediator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import com.kyleduo.switchbutton.C0473R;
import com.path.android.jobqueue.JobManager;
import com.shamchat.activity.ChatActivity;
import com.shamchat.activity.ChatInitialForGroupChatActivity;
import com.shamchat.activity.ChatThreadFragment;
import com.shamchat.activity.dateconvert;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.jobs.RemoveChatThreadDBJob;
import com.shamchat.models.MessageThread;
import com.shamchat.utils.Emoticons;
import com.shamchat.utils.Utils;
import com.squareup.picasso.Picasso;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class ChatThreadsListAdapter extends BaseAdapter {
    private ChatThreadFragment chatThreadFragment;
    private Context context;
    private JobManager jobManager;
    String locale;
    private LayoutInflater mInflater;
    public List<MessageThread> messageThreads;
    private Map<String, ViewHolder> viewHolders;

    /* renamed from: com.shamchat.adapters.ChatThreadsListAdapter.1 */
    class C09451 implements OnClickListener {
        final /* synthetic */ String val$friendId;
        final /* synthetic */ boolean val$isGroupChat;
        final /* synthetic */ String val$threadId;

        C09451(boolean z, String str, String str2) {
            this.val$isGroupChat = z;
            this.val$threadId = str;
            this.val$friendId = str2;
        }

        public final void onClick(View v) {
            if (this.val$isGroupChat) {
                Intent i = new Intent(ChatThreadsListAdapter.this.context, ChatInitialForGroupChatActivity.class);
                i.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, this.val$threadId);
                i.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_GROUP_ID, this.val$friendId);
                i.setFlags(ClientDefaults.MAX_MSG_SIZE);
                ChatThreadsListAdapter.this.context.startActivity(i);
                return;
            }
            i = new Intent(ChatThreadsListAdapter.this.context, ChatActivity.class);
            i.putExtra(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID, this.val$threadId);
            i.putExtra(ChatActivity.INTENT_EXTRA_USER_ID, this.val$friendId);
            i.setFlags(ClientDefaults.MAX_MSG_SIZE);
            ChatThreadsListAdapter.this.context.startActivity(i);
        }
    }

    /* renamed from: com.shamchat.adapters.ChatThreadsListAdapter.2 */
    class C09462 implements OnLongClickListener {
        final /* synthetic */ String val$threadId;

        C09462(String str) {
            this.val$threadId = str;
        }

        public final boolean onLongClick(View v) {
            ChatThreadsListAdapter.access$100(ChatThreadsListAdapter.this, this.val$threadId);
            return false;
        }
    }

    /* renamed from: com.shamchat.adapters.ChatThreadsListAdapter.3 */
    class C09473 implements DialogInterface.OnClickListener {
        final /* synthetic */ String val$threadId;

        C09473(String str) {
            this.val$threadId = str;
        }

        public final void onClick(DialogInterface dialog, int which) {
            if (which == 0) {
                RemoveChatThreadDBJob removeChatThreadDBJob = new RemoveChatThreadDBJob();
                removeChatThreadDBJob.chatThreadId = this.val$threadId;
                ChatThreadsListAdapter.this.jobManager.addJobInBackground(removeChatThreadDBJob);
                System.out.println("Remove job initiated " + this.val$threadId);
            }
        }
    }

    public class ViewHolder {
        LinearLayout backlist;
        ImageView gicon;
        ImageView imgProfile;
        TextView lastMessage;
        TextView messageCount;
        RelativeLayout messageCountContainer;
        TextView messageTime;
        TextView username;
        View view;
    }

    static /* synthetic */ void access$100(ChatThreadsListAdapter x0, String x1) {
        Context activity = x0.chatThreadFragment.getActivity();
        Builder builder = new Builder(activity);
        builder.setCancelable(true);
        ListAdapter arrayAdapter = new ArrayAdapter(activity, 17367043);
        arrayAdapter.add(activity.getString(2131493022));
        builder.setAdapter(arrayAdapter, new C09473(x1));
        builder.show();
    }

    public ChatThreadsListAdapter(Context context, List<MessageThread> messageThreads, ChatThreadFragment chatThreadFragment) {
        this.viewHolders = new HashMap();
        this.context = context;
        this.messageThreads = messageThreads;
        this.mInflater = LayoutInflater.from(context);
        this.chatThreadFragment = chatThreadFragment;
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
    }

    public final int getCount() {
        return this.messageThreads.size();
    }

    public final Object getItem(int position) {
        return null;
    }

    public final long getItemId(int position) {
        return 0;
    }

    private View setRow(MessageThread messageThread) {
        int i;
        ViewHolder holder = (ViewHolder) this.viewHolders.get(messageThread.getThreadId());
        View view = holder.view;
        String friendId = messageThread.friendId;
        boolean isGroupChat = messageThread.isGroupChat;
        String threadId = messageThread.getThreadId();
        view.setOnClickListener(new C09451(isGroupChat, threadId, friendId));
        view.setOnLongClickListener(new C09462(threadId));
        messageThread.friendId.startsWith("g");
        holder.username.setText(messageThread.username);
        Date lastUpdatedDate = messageThread.lastUpdatedDate;
        Calendar c1 = Calendar.getInstance();
        c1.setTime(lastUpdatedDate);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        if (c1.get(1) != c2.get(1)) {
            i = c1.get(1) - c2.get(1);
        } else {
            i = c1.get(2) != c2.get(2) ? c1.get(2) - c2.get(2) : c1.get(5) - c2.get(5);
        }
        if (i == 0) {
            holder.messageTime.setText(Utils.formatDate(lastUpdatedDate.getTime(), "HH:mm"));
        } else {
            this.locale = Locale.getDefault().toString();
            if (this.locale.equals("fa_IR")) {
                try {
                    dateconvert com_shamchat_activity_dateconvert = new dateconvert();
                    Locale locale = new Locale("en_US");
                    holder.messageTime.setText(dateconvert.getCurrentShamsidate(Utils.formatDate(lastUpdatedDate.getTime(), "dd-MM-yyyy")));
                } catch (Exception e) {
                }
            }
            holder.messageTime.setText(Utils.formatDate(lastUpdatedDate.getTime(), "dd-MM-yyyy"));
        }
        int lastMessageMedium = messageThread.lastMessageMedium;
        String lastMessage = messageThread.lastMessage;
        int lastMessageDirection = messageThread.lastMessageDirection;
        if (!isGroupChat) {
            switch (lastMessageMedium) {
                case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                    holder.lastMessage.setText(Emoticons.getSmiledText(SHAMChatApplication.getMyApplicationContext(), lastMessage), BufferType.SPANNABLE);
                    break;
                case Logger.SEVERE /*1*/:
                    if (lastMessageDirection != MyMessageType.INCOMING_MSG.ordinal()) {
                        if (lastMessageDirection == MyMessageType.OUTGOING_MSG.ordinal()) {
                            holder.lastMessage.setText(2131493360);
                            break;
                        }
                    }
                    holder.lastMessage.setText(2131493279);
                    break;
                    break;
                case Logger.WARNING /*2*/:
                    if (lastMessageDirection != MyMessageType.INCOMING_MSG.ordinal()) {
                        if (lastMessageDirection == MyMessageType.OUTGOING_MSG.ordinal()) {
                            holder.lastMessage.setText(2131493362);
                            break;
                        }
                    }
                    holder.lastMessage.setText(2131493281);
                    break;
                    break;
                case Logger.INFO /*3*/:
                    if (lastMessageDirection != MyMessageType.INCOMING_MSG.ordinal()) {
                        if (lastMessageDirection == MyMessageType.OUTGOING_MSG.ordinal()) {
                            holder.lastMessage.setText(2131493364);
                            break;
                        }
                    }
                    holder.lastMessage.setText(2131493283);
                    break;
                    break;
                case Logger.FINER /*6*/:
                    if (lastMessageDirection != MyMessageType.INCOMING_MSG.ordinal()) {
                        if (lastMessageDirection == MyMessageType.OUTGOING_MSG.ordinal()) {
                            holder.lastMessage.setText(2131493361);
                            break;
                        }
                    }
                    holder.lastMessage.setText(2131493280);
                    break;
                    break;
                case Logger.FINEST /*7*/:
                    holder.lastMessage.setText(2131493171);
                    break;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    holder.lastMessage.setText(2131493247);
                    break;
                case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                    if (lastMessageDirection != MyMessageType.INCOMING_MSG.ordinal()) {
                        if (lastMessageDirection == MyMessageType.OUTGOING_MSG.ordinal()) {
                            holder.lastMessage.setText(2131493363);
                            break;
                        }
                    }
                    holder.lastMessage.setText(2131493282);
                    break;
                    break;
            }
        }
        holder.lastMessage.setText(lastMessage);
        holder.messageCountContainer.setVisibility(8);
        if (messageThread.messageCount > 0) {
            holder.messageCountContainer.setVisibility(0);
            System.out.println("loadMessageThreadAndUpdateListView REFRESHING " + String.valueOf(messageThread.messageCount));
            holder.messageCount.setText(String.valueOf(messageThread.messageCount));
        } else {
            holder.messageCountContainer.setVisibility(8);
        }
        if (isGroupChat) {
            switch (new Random().nextInt(7) + 0) {
                case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                    holder.imgProfile.setImageResource(2130837738);
                    break;
                case Logger.SEVERE /*1*/:
                    holder.imgProfile.setImageResource(2130837739);
                    break;
                case Logger.WARNING /*2*/:
                    holder.imgProfile.setImageResource(2130837740);
                    break;
                case Logger.INFO /*3*/:
                    holder.imgProfile.setImageResource(2130837741);
                    break;
                case Logger.CONFIG /*4*/:
                    holder.imgProfile.setImageResource(2130837742);
                    break;
                case Logger.FINE /*5*/:
                    holder.imgProfile.setImageResource(2130837743);
                    break;
                case Logger.FINER /*6*/:
                    holder.imgProfile.setImageResource(2130837744);
                    break;
                default:
                    holder.imgProfile.setImageResource(2130837738);
                    break;
            }
            System.out.println("Testing group " + messageThread.friendId);
            holder.gicon.setVisibility(0);
            new StringBuilder().append(friendId).append("@conference.rabtcdn.com");
        } else {
            holder.gicon.setVisibility(8);
            String profileImageUrl = messageThread.friendProfileImageUrl;
            System.out.println("Profile image " + profileImageUrl);
            Bitmap bitmap = (Bitmap) SHAMChatApplication.USER_IMAGES.get(friendId);
            if (bitmap != null) {
                System.out.println("Image in the map");
                holder.imgProfile.setImageBitmap(bitmap);
                Utils.handleProfileImage(this.context, friendId, profileImageUrl);
            } else {
                System.out.println("Image not in the map");
                if (profileImageUrl != null) {
                    if (profileImageUrl.contains("http://")) {
                        Uri uri = Uri.parse(profileImageUrl);
                        Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(uri).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).into(holder.imgProfile, null);
                        Utils.handleProfileImage(this.context, friendId, profileImageUrl);
                    }
                }
                holder.imgProfile.setImageResource(2130837944);
            }
        }
        return view;
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        MessageThread messageThread = (MessageThread) this.messageThreads.get(position);
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = this.mInflater.inflate(2130903160, null);
            holder.username = (TextView) convertView.findViewById(2131362281);
            holder.messageTime = (TextView) convertView.findViewById(2131362282);
            holder.lastMessage = (TextView) convertView.findViewById(2131362284);
            holder.messageCountContainer = (RelativeLayout) convertView.findViewById(2131362286);
            holder.messageCount = (TextView) convertView.findViewById(2131362287);
            holder.imgProfile = (ImageView) convertView.findViewById(2131361986);
            holder.gicon = (ImageView) convertView.findViewById(2131362283);
            holder.backlist = (LinearLayout) convertView.findViewById(2131362280);
            holder.view = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.view = convertView;
        }
        if (messageThread.isGroupChat) {
            holder.imgProfile.setImageResource(2130837739);
        } else {
            holder.imgProfile.setImageResource(2130837944);
        }
        this.viewHolders.put(messageThread.getThreadId(), holder);
        return setRow(messageThread);
    }
}

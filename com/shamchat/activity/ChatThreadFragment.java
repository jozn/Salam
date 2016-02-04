package com.shamchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.path.android.jobqueue.JobManager;
import com.shamchat.adapters.ChatThreadsListAdapter;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.events.ChatThreadDBRemoveCompletedEvent;
import com.shamchat.events.ChatThreadsDBLoadCompletedEvent;
import com.shamchat.events.NewMessageEvent;
import com.shamchat.jobs.ChatThreadsDBLoadJob;
import com.shamchat.models.MessageThread;
import de.greenrobot.event.EventBus;
import java.util.List;

public class ChatThreadFragment extends Fragment {
    private ChatThreadsListAdapter adapter;
    private Button buttonCompose;
    private ListView chatThreadsListView;
    private JobManager jobManager;
    private LinearLayout layout;
    private View view;

    /* renamed from: com.shamchat.activity.ChatThreadFragment.1 */
    class C07191 implements OnClickListener {
        C07191() {
        }

        public final void onClick(View v) {
            ChatThreadFragment.this.startActivity(new Intent(ChatThreadFragment.this.getActivity(), ComposeIndividualChatActivity.class));
        }
    }

    /* renamed from: com.shamchat.activity.ChatThreadFragment.2 */
    class C07202 implements OnScrollListener {
        C07202() {
        }

        public final void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        public final void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    }

    /* renamed from: com.shamchat.activity.ChatThreadFragment.3 */
    class C07213 implements Runnable {
        final /* synthetic */ List val$messageThreads;

        C07213(List list) {
            this.val$messageThreads = list;
        }

        public final void run() {
            System.out.println("onEventBackgroundThread ChatThreadsDBLoadCompletedEvent " + this.val$messageThreads.size());
            if (this.val$messageThreads != null && this.val$messageThreads.size() > 0) {
                ChatThreadFragment.this.layout.setVisibility(8);
                if (ChatThreadFragment.this.chatThreadsListView.getAdapter() == null) {
                    ChatThreadFragment.this.adapter = new ChatThreadsListAdapter(ChatThreadFragment.this.getActivity(), this.val$messageThreads, ChatThreadFragment.this);
                    ChatThreadFragment.this.chatThreadsListView.setAdapter(ChatThreadFragment.this.adapter);
                    return;
                }
                ChatThreadFragment.this.adapter.messageThreads = this.val$messageThreads;
                ChatThreadFragment.this.adapter.notifyDataSetChanged();
            } else if (ChatThreadFragment.this.layout.getVisibility() == 8) {
                ChatThreadFragment.this.layout.setVisibility(0);
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(2130903137, container, false);
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        try {
            EventBus.getDefault().register(this, true, 15);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.layout = (LinearLayout) this.view.findViewById(2131362212);
        this.buttonCompose = (Button) this.view.findViewById(2131362215);
        this.buttonCompose.setOnClickListener(new C07191());
        this.chatThreadsListView = (ListView) this.view.findViewById(2131362216);
        this.chatThreadsListView.setOnScrollListener(new C07202());
        return this.view;
    }

    public void onDestroy() {
        super.onDestroy();
        unbindDrawables(this.view.findViewById(2131362216));
        unbindDrawables(this.view.findViewById(2131362213));
        unbindDrawables(this.view.findViewById(2131362212));
        this.chatThreadsListView = null;
        this.adapter = null;
        this.buttonCompose = null;
        System.gc();
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if ((view instanceof ViewGroup) && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        try {
            System.out.println("ppp RESUME");
        } catch (Throwable t) {
            System.out.println("Chat thread " + t);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            refreshAdapter();
        }
    }

    private void refreshAdapter() {
        if (this.jobManager != null) {
            this.jobManager.addJobInBackground(new ChatThreadsDBLoadJob());
        }
    }

    public void onEventBackgroundThread(ChatThreadsDBLoadCompletedEvent event) {
        List<MessageThread> messageThreads = event.messageThreads;
        System.out.println("ppp onEventBackgroundThread ChatThreadsDBLoadCompletedEvent " + messageThreads.size());
        if (getActivity() != null) {
            getActivity().runOnUiThread(new C07213(messageThreads));
        }
    }

    public void onEventBackgroundThread(ChatThreadDBRemoveCompletedEvent event) {
        System.out.println("Chat thread removed");
        refreshAdapter();
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            System.out.println("Visible");
            refreshAdapter();
        }
    }

    public void onEventBackgroundThread(NewMessageEvent event) {
        System.out.println("NewMessageEvent from ChatThreadFragment:  " + event.threadId);
        refreshAdapter();
    }
}

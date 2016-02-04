package com.shamchat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.appcompat.BuildConfig;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import com.path.android.jobqueue.JobManager;
import com.shamchat.adapters.ContactsAllExpandableAdapter;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.util.PreferenceConstants;
import com.shamchat.events.AllContactsDBLoadCompletedEvent;
import com.shamchat.events.SyncContactsCompletedEvent;
import com.shamchat.jobs.AllContactsDBLoadJob;
import com.shamchat.models.ContactFriend;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.List;

public class ContactAllFragment extends Fragment {
    private static ContactsAllExpandableAdapter adapter;
    private static List<ArrayList<ContactFriend>> allContacts;
    private static ExpandableListView listContacts;
    private boolean didDbLoadComplete;
    private boolean isInitialSync;
    private JobManager jobManager;
    private LinearLayout loadingContactsView;
    private SharedPreferences preferences;
    private String search;

    /* renamed from: com.shamchat.activity.ContactAllFragment.1 */
    class C07301 implements OnScrollListener {
        C07301() {
        }

        public final void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        public final void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            System.out.println("Change happen 1 first " + firstVisibleItem);
        }
    }

    /* renamed from: com.shamchat.activity.ContactAllFragment.2 */
    class C07312 implements Runnable {
        C07312() {
        }

        public final void run() {
            ContactAllFragment.listContacts.setAdapter(ContactAllFragment.adapter);
            for (int i = 0; i < ContactAllFragment.adapter.getGroupCount(); i++) {
                ContactAllFragment.listContacts.expandGroup(i);
            }
            ContactAllFragment.adapter.notifyDataSetChanged();
        }
    }

    /* renamed from: com.shamchat.activity.ContactAllFragment.3 */
    class C07323 implements Runnable {
        C07323() {
        }

        public final void run() {
            ContactAllFragment.listContacts.setVisibility(0);
            ContactAllFragment.this.loadingContactsView.setVisibility(8);
            System.out.println("ContactShamFragment SyncContactsCompletedEvent completed hiding the progress bar");
        }
    }

    public ContactAllFragment() {
        this.search = BuildConfig.VERSION_NAME;
        this.didDbLoadComplete = true;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(2130903139, container, false);
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        try {
            EventBus.getDefault().register(this, true, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        listContacts = (ExpandableListView) inflate.findViewById(2131362222);
        this.loadingContactsView = (LinearLayout) inflate.findViewById(2131362223);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        this.isInitialSync = this.preferences.getBoolean(PreferenceConstants.INITIAL_CONTACT_SYNC, true);
        if (this.isInitialSync) {
            listContacts.setVisibility(8);
            this.loadingContactsView.setVisibility(0);
        } else {
            listContacts.setVisibility(0);
            this.loadingContactsView.setVisibility(8);
        }
        refreshAdapter(this.search);
        listContacts.setOnScrollListener(new C07301());
        return inflate;
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshAdapter(String search) {
        if (this.jobManager != null) {
            AllContactsDBLoadJob allContactsDBLoadJob = new AllContactsDBLoadJob();
            allContactsDBLoadJob.search = search;
            this.jobManager.addJobInBackground(allContactsDBLoadJob);
            System.out.println("ContactAllFragment refresh adapter");
        }
    }

    public void onEventBackgroundThread(AllContactsDBLoadCompletedEvent event) {
        System.out.println("ContactAllFragment ContactsDBLoadCompletedEvent downloaded ");
        allContacts = event.contactFriends;
        System.out.println("all contacts search " + allContacts.size());
        adapter = new ContactsAllExpandableAdapter(SHAMChatApplication.getMyApplicationContext(), allContacts);
        if (getActivity() != null) {
            getActivity().runOnUiThread(new C07312());
        }
    }

    public void onEventBackgroundThread(SyncContactsCompletedEvent event) {
        System.out.println("ContactShamFragment SyncContactsCompletedEvent completed");
        getActivity().runOnUiThread(new C07323());
        refreshAdapter(this.search);
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            System.out.println("Visible");
        }
    }
}

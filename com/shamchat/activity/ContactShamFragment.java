package com.shamchat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.appcompat.BuildConfig;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.path.android.jobqueue.JobManager;
import com.shamchat.adapters.ContactsExpandableAdapter;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.util.PreferenceConstants;
import com.shamchat.events.CitContactsDBLoadCompletedEvent;
import com.shamchat.events.SyncContactsCompletedEvent;
import com.shamchat.jobs.SHAMContactsDBLoadJob;
import com.shamchat.jobs.SyncContactsJob;
import com.shamchat.models.ContactFriend;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContactShamFragment extends Fragment {
    private ContactsExpandableAdapter adapter;
    private List<ArrayList<ContactFriend>> allContacts;
    private boolean didDbLoadComplete;
    private boolean isInitialSync;
    private JobManager jobManager;
    private ExpandableListView listContacts;
    private LinearLayout loadingContactsView;
    private SharedPreferences preferences;
    private String search;
    private ImageView syncContacts;
    private ProgressBar syncContactsLoader;

    /* renamed from: com.shamchat.activity.ContactShamFragment.1 */
    class C07341 implements OnClickListener {
        C07341() {
        }

        public final void onClick(View v) {
            if (PreferenceConstants.CONNECTED_TO_NETWORK) {
                ContactShamFragment.this.syncContacts.setVisibility(8);
                ContactShamFragment.this.syncContactsLoader.setVisibility(0);
                ContactShamFragment.this.jobManager.addJobInBackground(new SyncContactsJob(0));
                return;
            }
            Toast.makeText(ContactShamFragment.this.getActivity(), 2131493124, 0).show();
        }
    }

    /* renamed from: com.shamchat.activity.ContactShamFragment.2 */
    class C07352 implements OnScrollListener {
        C07352() {
        }

        public final void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        public final void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            System.out.println("CITTAB Change happen first " + firstVisibleItem);
        }
    }

    /* renamed from: com.shamchat.activity.ContactShamFragment.3 */
    class C07363 implements Runnable {
        C07363() {
        }

        public final void run() {
            ContactShamFragment.this.syncContacts.setVisibility(0);
            ContactShamFragment.this.syncContactsLoader.setVisibility(8);
            ContactShamFragment.this.listContacts.setVisibility(0);
            ContactShamFragment.this.loadingContactsView.setVisibility(8);
            ContactShamFragment.this.listContacts.setAdapter(ContactShamFragment.this.adapter);
            for (int i = 0; i < ContactShamFragment.this.adapter.getGroupCount(); i++) {
                ContactShamFragment.this.listContacts.expandGroup(i);
            }
            ContactShamFragment.this.adapter.notifyDataSetChanged();
        }
    }

    public ContactShamFragment() {
        this.allContacts = new ArrayList();
        this.search = BuildConfig.VERSION_NAME;
        this.didDbLoadComplete = true;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(2130903139, container, false);
        System.out.println("oncreate czf");
        this.preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        this.listContacts = (ExpandableListView) inflate.findViewById(2131362222);
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        try {
            EventBus.getDefault().register(this, true, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.syncContacts = (ImageView) inflate.findViewById(2131361962);
        this.syncContactsLoader = (ProgressBar) inflate.findViewById(2131361963);
        this.loadingContactsView = (LinearLayout) inflate.findViewById(2131362223);
        this.adapter = new ContactsExpandableAdapter(getActivity(), this.allContacts);
        refreshAdapter(this.search);
        this.isInitialSync = this.preferences.getBoolean(PreferenceConstants.INITIAL_CONTACT_SYNC, true);
        this.syncContacts.setOnClickListener(new C07341());
        this.listContacts.setOnScrollListener(new C07352());
        return inflate;
    }

    public void onResume() {
        super.onResume();
        System.out.println("CITTAB XXX CZF onResume");
    }

    public void onPause() {
        super.onPause();
        System.out.println("CITTAB XXX CZF onPause");
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
            this.didDbLoadComplete = false;
            SHAMContactsDBLoadJob shamContactsDBLoadJob = new SHAMContactsDBLoadJob();
            shamContactsDBLoadJob.search = search;
            this.jobManager.addJobInBackground(shamContactsDBLoadJob);
            System.out.println("CITTAB YYY refresh adapter");
        }
    }

    public void onEventBackgroundThread(CitContactsDBLoadCompletedEvent event) {
        this.didDbLoadComplete = true;
        Map<String, List<ContactFriend>> contacts = event.contactFriends;
        this.allContacts.clear();
        this.allContacts.add((ArrayList) contacts.get("You"));
        for (String key : contacts.keySet()) {
            if (!key.equals("You")) {
                this.allContacts.add((ArrayList) contacts.get(key));
            }
        }
        System.out.println("SynContacts ui " + System.currentTimeMillis());
        System.out.println("CITTAB REFRESHING CONTACTS BUG 2");
        System.out.println("CITTAB ContactShamFragment ContactsDBLoadCompletedEvent downloaded " + this.allContacts.size());
        this.adapter = new ContactsExpandableAdapter(SHAMChatApplication.getMyApplicationContext(), this.allContacts);
        if (this.allContacts.size() > 0) {
            Editor editor = PreferenceManager.getDefaultSharedPreferences(SHAMChatApplication.getMyApplicationContext()).edit();
            editor.putBoolean(PreferenceConstants.INITIAL_CONTACT_SYNC, false);
            editor.commit();
            if (getActivity() != null) {
                getActivity().runOnUiThread(new C07363());
            } else {
                System.out.println("CITTAB ContactShamFragment no results");
            }
        }
    }

    public void onEventBackgroundThread(SyncContactsCompletedEvent event) {
        System.out.println("CITTAB ContactShamFragment SyncContactsCompletedEvent completed");
        refreshAdapter(this.search);
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}

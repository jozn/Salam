package com.shamchat.activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.path.android.jobqueue.JobManager;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.util.PreferenceConstants;
import com.shamchat.jobs.SyncContactsJob;
import com.shamchat.jobs.UpdatePhoneContactsDBJob;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class ContactsFragment extends Fragment {
    private int activeFragment$31cfc1ab;
    private Button buttonAll;
    private Button buttonCit;
    private int content;
    private JobManager jobManager;
    private SharedPreferences preferences;

    /* renamed from: com.shamchat.activity.ContactsFragment.1 */
    class C07371 implements OnClickListener {
        C07371() {
        }

        public final void onClick(View v) {
            ContactsFragment.this.switchContent$1445be8a(Fragments.FRAGMENT_CIT$31cfc1ab);
        }
    }

    /* renamed from: com.shamchat.activity.ContactsFragment.2 */
    class C07382 implements OnClickListener {
        C07382() {
        }

        public final void onClick(View v) {
            ContactsFragment.this.switchContent$1445be8a(Fragments.FRAGMENT_ALL$31cfc1ab);
        }
    }

    /* renamed from: com.shamchat.activity.ContactsFragment.3 */
    static /* synthetic */ class C07393 {
        static final /* synthetic */ int[] $SwitchMap$com$shamchat$activity$ContactsFragment$Fragments;

        static {
            $SwitchMap$com$shamchat$activity$ContactsFragment$Fragments = new int[Fragments.values$3980474f().length];
            try {
                $SwitchMap$com$shamchat$activity$ContactsFragment$Fragments[Fragments.FRAGMENT_CIT$31cfc1ab - 1] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$shamchat$activity$ContactsFragment$Fragments[Fragments.FRAGMENT_ALL$31cfc1ab - 1] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public enum Fragments {
        ;

        public static int[] values$3980474f() {
            return (int[]) $VALUES$2f5c2e10.clone();
        }

        static {
            FRAGMENT_CIT$31cfc1ab = 1;
            FRAGMENT_ALL$31cfc1ab = 2;
            $VALUES$2f5c2e10 = new int[]{FRAGMENT_CIT$31cfc1ab, FRAGMENT_ALL$31cfc1ab};
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activeFragment$31cfc1ab = Fragments.FRAGMENT_CIT$31cfc1ab;
        this.content = 2131362229;
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (this.preferences.getBoolean(PreferenceConstants.INITIAL_LOGIN, true)) {
            this.jobManager.addJobInBackground(new SyncContactsJob(0));
        } else if (isWifiConnected()) {
            this.jobManager.addJobInBackground(new SyncContactsJob(18000000));
        }
        Editor editor = this.preferences.edit();
        editor.putBoolean(PreferenceConstants.INITIAL_LOGIN, false);
        editor.commit();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(2130903140, container, false);
        this.buttonCit = (Button) view.findViewById(2131362226);
        this.buttonAll = (Button) view.findViewById(2131362227);
        return view;
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.buttonCit.setOnClickListener(new C07371());
        this.buttonAll.setOnClickListener(new C07382());
        switchContent$1445be8a(this.activeFragment$31cfc1ab);
    }

    private void switchContent$1445be8a(int fragmentToSwitch) {
        Fragment fragment = null;
        switch (C07393.$SwitchMap$com$shamchat$activity$ContactsFragment$Fragments[fragmentToSwitch - 1]) {
            case Logger.SEVERE /*1*/:
                fragment = new ContactShamFragment();
                break;
            case Logger.WARNING /*2*/:
                fragment = new ContactAllFragment();
                break;
        }
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(this.content, fragment);
        transaction.commitAllowingStateLoss();
        this.activeFragment$31cfc1ab = fragmentToSwitch;
        switchStateButtonMenuContact();
    }

    public void switchStateButtonMenuContact() {
        this.buttonCit.setSelected(false);
        this.buttonAll.setSelected(false);
        switch (C07393.$SwitchMap$com$shamchat$activity$ContactsFragment$Fragments[this.activeFragment$31cfc1ab - 1]) {
            case Logger.SEVERE /*1*/:
                this.buttonCit.setSelected(true);
            case Logger.WARNING /*2*/:
                this.buttonAll.setSelected(true);
            default:
        }
    }

    public boolean getUserVisibleHint() {
        return super.getUserVisibleHint();
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        System.out.println("setUserVisibleHint contacts fragment ");
        UpdatePhoneContactsDBJob updatePhoneContactsDBJob = new UpdatePhoneContactsDBJob();
        if (this.jobManager != null) {
            this.jobManager.addJobInBackground(updatePhoneContactsDBJob);
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    private boolean isWifiConnected() {
        if (((ConnectivityManager) SHAMChatApplication.getMyApplicationContext().getSystemService("connectivity")).getNetworkInfo(1).isConnected()) {
            return true;
        }
        return false;
    }
}

package com.shamchat.moments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.appcompat.BuildConfig;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;
import com.path.android.jobqueue.JobManager;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.events.MomentsDBLoadCompletedEvent;
import com.shamchat.events.MomentsDownloadedEvent;
import com.shamchat.events.OldMomentsDownloadedEvent;
import com.shamchat.jobs.MomentsDBLoadJob;
import com.shamchat.jobs.MomentsDownloadJob;
import com.shamchat.jobs.OldMomentsDownloadJob;
import com.shamchat.models.Moment;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MomentsFragment extends Fragment {
    private MomentsBaseAdapter adapter;
    private ArrayList<Moment> allMoments;
    private Map<String, Moment> allMomentsMap;
    private final Handler handler;
    private JobManager jobManager;
    private String lastPostId;
    private int lastVisiblePosition;
    public PullAndLoadListView list;
    private String mobileNumber;
    private LinearLayout noMomentContainer;
    private int numberOfItemsToBeReturned;
    private String password;
    private SharedPreferences preferences;
    private Timer timer;
    private String userId;

    /* renamed from: com.shamchat.moments.MomentsFragment.1 */
    class C11571 implements OnLoadMoreListener {
        C11571() {
        }

        public final void onLoadMore() {
            System.out.println("AAA onLoadMore");
            MomentsFragment.this.numberOfItemsToBeReturned = MomentsFragment.this.numberOfItemsToBeReturned + 10;
            MomentsFragment.this.jobManager.addJobInBackground(new OldMomentsDownloadJob(MomentsFragment.this.mobileNumber, MomentsFragment.this.password, MomentsFragment.this.lastPostId));
        }
    }

    /* renamed from: com.shamchat.moments.MomentsFragment.2 */
    class C11582 implements OnRefreshListener {
        C11582() {
        }

        public final void onRefresh() {
            System.out.println("AAA On refresh");
            MomentsFragment.this.jobManager.addJobInBackground(new MomentsDownloadJob(MomentsFragment.this.mobileNumber, MomentsFragment.this.password));
        }
    }

    /* renamed from: com.shamchat.moments.MomentsFragment.3 */
    class C11593 implements OnScrollListener {
        C11593() {
        }

        public final void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        public final void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    }

    /* renamed from: com.shamchat.moments.MomentsFragment.4 */
    class C11604 implements Runnable {
        final /* synthetic */ ArrayList val$moments;

        C11604(ArrayList arrayList) {
            this.val$moments = arrayList;
        }

        public final void run() {
            MomentsFragment.this.lastVisiblePosition = MomentsFragment.this.allMoments.size();
            MomentsFragment.this.allMoments.addAll(this.val$moments);
            System.out.println("AAA refresh start");
            MomentsFragment.this.lastPostId = ((Moment) MomentsFragment.this.allMoments.get(MomentsFragment.this.allMoments.size() - 1)).serverPostId;
            System.out.println("Size moments " + this.val$moments.size() + " all moments " + MomentsFragment.this.allMoments.size() + " last visible " + MomentsFragment.this.lastVisiblePosition);
            System.out.println("Last pot Id " + MomentsFragment.this.lastPostId);
            MomentsFragment.this.adapter.momentList = MomentsFragment.this.allMoments;
            MomentsFragment.this.adapter.notifyDataSetChanged();
            MomentsFragment.this.list.onRefreshComplete();
            MomentsFragment.this.list.onLoadMoreComplete();
            MomentsFragment.this.list.setSelectionFromTop(MomentsFragment.this.lastVisiblePosition, 5);
            if (MomentsFragment.this.noMomentContainer != null) {
                System.out.println("AAA noMomentContainer != null");
                MomentsFragment.this.noMomentContainer.setVisibility(8);
            }
        }
    }

    /* renamed from: com.shamchat.moments.MomentsFragment.5 */
    class C11615 implements Runnable {
        C11615() {
        }

        public final void run() {
            MomentsFragment.this.adapter.momentList = MomentsFragment.this.allMoments;
            MomentsFragment.this.adapter.notifyDataSetChanged();
            MomentsFragment.this.list.onRefreshComplete();
            MomentsFragment.this.list.onLoadMoreComplete();
            MomentsFragment.this.list.setSelectionFromTop(MomentsFragment.this.lastVisiblePosition, 5);
        }
    }

    /* renamed from: com.shamchat.moments.MomentsFragment.6 */
    class C11636 extends TimerTask {

        /* renamed from: com.shamchat.moments.MomentsFragment.6.1 */
        class C11621 implements Runnable {
            C11621() {
            }

            public final void run() {
                try {
                    System.out.println("Synching the moments");
                    MomentsFragment.this.jobManager.addJobInBackground(new MomentsDownloadJob(MomentsFragment.this.mobileNumber, MomentsFragment.this.password));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        C11636() {
        }

        public final void run() {
            MomentsFragment.this.handler.post(new C11621());
        }
    }

    public MomentsFragment() {
        this.userId = null;
        this.noMomentContainer = null;
        this.numberOfItemsToBeReturned = 10;
        this.lastVisiblePosition = 0;
        this.handler = new Handler();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("Moments oncreateview");
        View view = inflater.inflate(2130903206, container, false);
        this.allMomentsMap = new HashMap();
        this.allMoments = new ArrayList();
        this.preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.userId = bundle.getString("userId");
            System.out.println("AAA fri not null " + this.userId);
        } else {
            System.out.println("AAA fri null");
        }
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        try {
            EventBus.getDefault().register(this, true, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.list = (PullAndLoadListView) view.findViewById(2131362427);
        this.list.mOnLoadMoreListener = new C11571();
        this.list.mOnRefreshListener = new C11582();
        this.list.setOnScrollListener(new C11593());
        this.noMomentContainer = (LinearLayout) view.findViewById(2131362426);
        this.noMomentContainer.setVisibility(0);
        if (this.userId != null) {
            ((TextView) view.findViewById(2131361925)).setText(2131493227);
        }
        this.adapter = new MomentsBaseAdapter(this, new ArrayList());
        this.list.setAdapter(this.adapter);
        this.mobileNumber = this.preferences.getString("user_mobileNo", BuildConfig.VERSION_NAME);
        this.password = this.preferences.getString("user_password", BuildConfig.VERSION_NAME);
        refreshAdapter();
        return view;
    }

    private void refreshAdapter() {
        if (this.jobManager != null) {
            this.jobManager.addJobInBackground(new MomentsDBLoadJob(this.userId, this.numberOfItemsToBeReturned));
            this.jobManager.addJobInBackground(new MomentsDownloadJob(this.mobileNumber, this.password));
        }
    }

    public void onResume() {
        super.onResume();
        System.out.println("AAAA MF onResume");
        this.adapter.momentList = this.allMoments;
        this.adapter.notifyDataSetChanged();
        this.list.onRefreshComplete();
        this.list.onLoadMoreComplete();
    }

    public void onPause() {
        super.onPause();
        System.out.println("AAA MF onPause");
        if (this.timer != null) {
            this.timer.cancel();
        }
    }

    public void onEventBackgroundThread(MomentsDownloadedEvent event) {
        System.out.println("AAA Moments downloaded " + this.numberOfItemsToBeReturned);
        this.jobManager.addJobInBackground(new MomentsDBLoadJob(this.userId, this.numberOfItemsToBeReturned));
    }

    public void onEventBackgroundThread(MomentsDBLoadCompletedEvent event) {
        System.out.println("AAA Moments DB load completed, now refresh list " + event.moments.size());
        ArrayList<Moment> tmpMoments = event.moments;
        ArrayList<Moment> moments = new ArrayList();
        System.out.println("lastVisible " + this.lastVisiblePosition);
        Iterator it = tmpMoments.iterator();
        while (it.hasNext()) {
            Moment m = (Moment) it.next();
            if (this.allMomentsMap.containsKey(m.momentId)) {
                System.out.println("Duplicates found");
            } else {
                moments.add(m);
                this.allMomentsMap.put(m.momentId, m);
            }
        }
        if (moments.size() > 0) {
            getActivity().runOnUiThread(new C11604(moments));
            System.out.println("AAA refresh end");
            return;
        }
        System.out.println("AAA refresh ELSE");
        getActivity().runOnUiThread(new C11615());
    }

    public void onEventBackgroundThread(OldMomentsDownloadedEvent event) {
        System.out.println("YYY OldMomentsDownloadedEvent downloaded " + this.numberOfItemsToBeReturned);
        this.jobManager.addJobInBackground(new MomentsDBLoadJob(this.userId, this.numberOfItemsToBeReturned));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("AAAA on ac MF");
        this.allMoments.clear();
        this.allMomentsMap.clear();
        this.jobManager.addJobInBackground(new MomentsDBLoadJob(this.userId, this.numberOfItemsToBeReturned));
    }

    public void setMenuVisibility(boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            System.out.println("Visible");
            refreshAdapter();
            if (this.timer != null) {
                this.timer.cancel();
                this.timer = null;
            }
            startTimerToDownload();
        }
    }

    private void startTimerToDownload() {
        if (this.timer == null) {
            this.timer = new Timer();
            this.timer.schedule(new C11636(), 0, 120000);
        }
    }
}

package com.shamchat.moments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import com.shamchat.activity.PostFragment;

public class MomentsBaseFragment extends Fragment implements OnClickListener {
    public static String ADD_EXTRA_PHOTO_LIST;
    private AlertDialog alert;
    private FragmentTabHost mTabHost;

    @SuppressLint({"NewApi"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(2130903189, container, false);
        setUpTabs(inflate);
        return inflate;
    }

    private void setUpTabs(View rootView) {
        this.mTabHost = (FragmentTabHost) rootView.findViewById(16908306);
        Log.d("nis", "check if anything is null " + this.mTabHost + " activity " + getChildFragmentManager());
        this.mTabHost.setup(getActivity(), getChildFragmentManager(), 2131362387);
        this.mTabHost.addTab(this.mTabHost.newTabSpec("fragmentb").setIndicator(BuildConfig.VERSION_NAME), MomentsFragment.class, null);
        this.mTabHost.addTab(this.mTabHost.newTabSpec("fragmentc").setIndicator(BuildConfig.VERSION_NAME), PostFragment.class, null);
        this.mTabHost.getTabWidget().getChildAt(0).setBackgroundResource(2130837973);
        if ((getResources().getConfiguration().screenLayout & 15) == 1) {
            this.mTabHost.getTabWidget().getChildAt(0).setLayoutParams(new LayoutParams(100, 30));
        }
        this.mTabHost.getTabWidget().getChildAt(1).setBackgroundResource(2130837986);
        if ((getResources().getConfiguration().screenLayout & 15) == 1) {
            this.mTabHost.getTabWidget().getChildAt(1).setLayoutParams(new LayoutParams(100, 30));
        }
        this.mTabHost.getTabWidget().setDividerDrawable(null);
        setMargin();
    }

    private void setMargin() {
        ((LayoutParams) this.mTabHost.getTabWidget().getChildAt(0).getLayoutParams()).setMargins(20, 20, 0, 10);
        ((LayoutParams) this.mTabHost.getTabWidget().getChildAt(1).getLayoutParams()).setMargins(0, 20, 20, 10);
        this.mTabHost.getTabWidget().requestLayout();
    }

    public void onClick(DialogInterface arg0, int arg1) {
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment onActivityResult : getChildFragmentManager().getFragments()) {
            onActivityResult.onActivityResult(requestCode, resultCode, data);
        }
    }
}

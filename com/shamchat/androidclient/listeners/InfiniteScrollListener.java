package com.shamchat.androidclient.listeners;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public abstract class InfiniteScrollListener implements OnScrollListener {
    private int bufferItemCount;
    private int check;
    private int currentPage;
    private int itemCount;

    public abstract void loadMore$255f295();

    public InfiniteScrollListener() {
        this.bufferItemCount = 10;
        this.currentPage = 0;
        this.itemCount = 0;
        this.check = 0;
        this.bufferItemCount = 10;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (this.check != 0 && firstVisibleItem == 0) {
            loadMore$255f295();
            this.check++;
        }
        this.check++;
    }
}

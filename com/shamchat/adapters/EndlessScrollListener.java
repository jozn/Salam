package com.shamchat.adapters;

import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public abstract class EndlessScrollListener implements OnScrollListener {
    public static int visibleItemsCount;
    public static int visibleThreshold;
    private int currentPage;
    public int firstVisibleItem;
    public boolean loading;
    private int previousTotalItemCount;
    private int previousVisibleItem;
    public int scrollDirection;
    private int startingPageIndex;
    private boolean userStartedScroll;

    public abstract boolean onLoadMore$255f299(int i);

    static {
        visibleThreshold = 7;
        visibleItemsCount = 0;
    }

    public EndlessScrollListener() {
        this.currentPage = 0;
        this.previousTotalItemCount = 0;
        this.loading = false;
        this.startingPageIndex = 0;
        this.scrollDirection = -1;
        this.firstVisibleItem = 0;
        this.previousVisibleItem = 0;
        this.userStartedScroll = false;
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.v("EndlessScroll", "firstVisibleItem: " + firstVisibleItem);
        Log.v("EndlessScroll", "visibleItemCount: " + visibleItemCount);
        Log.v("EndlessScroll", "totalItemCount: " + totalItemCount);
        this.firstVisibleItem = firstVisibleItem;
        visibleItemsCount = visibleItemCount;
        if (this.firstVisibleItem > this.previousVisibleItem) {
            this.scrollDirection = 1;
            Log.i("EndlessScroll", "scrolling down...");
        } else if (this.firstVisibleItem < this.previousVisibleItem) {
            this.scrollDirection = 0;
            Log.i("EndlessScroll", "scrolling up...");
        } else {
            this.scrollDirection = -1;
        }
        this.previousVisibleItem = this.firstVisibleItem;
        if (totalItemCount < this.previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }
        if (this.loading && totalItemCount > this.previousTotalItemCount) {
            this.previousTotalItemCount = totalItemCount;
            this.currentPage++;
        }
        Log.v("EndlessScroll", "userStartedScroll: " + this.userStartedScroll);
        if (!this.loading && this.scrollDirection != -1 && this.userStartedScroll) {
            if (this.scrollDirection == 1 && totalItemCount - visibleItemCount <= visibleThreshold + firstVisibleItem) {
                onLoadMore$255f299(firstVisibleItem);
                this.loading = true;
            } else if (this.scrollDirection == 0 && firstVisibleItem <= visibleThreshold) {
                onLoadMore$255f299(firstVisibleItem);
                this.loading = true;
            }
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == 1) {
            this.userStartedScroll = true;
        }
    }
}

package com.costum.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.android.widget.C0221R;

public class PullAndLoadListView extends PullToRefreshListView {
    private RelativeLayout mFooterView;
    private boolean mIsLoadingMore;
    public OnLoadMoreListener mOnLoadMoreListener;
    private ProgressBar mProgressBarLoadMore;

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public PullAndLoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mIsLoadingMore = false;
        initComponent$faab20d();
    }

    public PullAndLoadListView(Context context) {
        super(context);
        this.mIsLoadingMore = false;
        initComponent$faab20d();
    }

    public PullAndLoadListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mIsLoadingMore = false;
        initComponent$faab20d();
    }

    private void initComponent$faab20d() {
        this.mFooterView = (RelativeLayout) this.mInflater.inflate(C0221R.layout.load_more_footer, this, false);
        this.mProgressBarLoadMore = (ProgressBar) this.mFooterView.findViewById(C0221R.id.load_more_progressBar);
        addFooterView(this.mFooterView);
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        if (this.mOnLoadMoreListener == null) {
            return;
        }
        if (visibleItemCount == totalItemCount) {
            this.mProgressBarLoadMore.setVisibility(8);
            return;
        }
        boolean loadMore;
        if (firstVisibleItem + visibleItemCount >= totalItemCount) {
            loadMore = true;
        } else {
            loadMore = false;
        }
        if (!this.mIsLoadingMore && loadMore && this.mRefreshState != 4 && this.mCurrentScrollState != 0) {
            this.mProgressBarLoadMore.setVisibility(0);
            this.mIsLoadingMore = true;
            Log.d("PullToRefreshListView", "onLoadMore");
            if (this.mOnLoadMoreListener != null) {
                this.mOnLoadMoreListener.onLoadMore();
            }
        }
    }

    public final void onLoadMoreComplete() {
        this.mIsLoadingMore = false;
        this.mProgressBarLoadMore.setVisibility(8);
    }
}

package com.costum.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.android.widget.C0221R;

public class LoadMoreListView extends ListView implements OnScrollListener {
    private static Context mContext;
    private int mCurrentScrollState;
    private RelativeLayout mFooterView;
    private LayoutInflater mInflater;
    private boolean mIsLoadingMore;
    private OnLoadMoreListener mOnLoadMoreListener;
    private OnScrollListener mOnScrollListener;
    private ProgressBar mProgressBarLoadMore;

    public interface OnLoadMoreListener {
    }

    public LoadMoreListView(Context context) {
        super(context);
        this.mIsLoadingMore = false;
        mContext = context;
        init(context);
    }

    public LoadMoreListView() {
        super(mContext);
        this.mIsLoadingMore = false;
        init(mContext);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mIsLoadingMore = false;
        mContext = context;
        init(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mIsLoadingMore = false;
        mContext = context;
        init(context);
    }

    private void init(Context context) {
        this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mFooterView = (RelativeLayout) this.mInflater.inflate(C0221R.layout.load_more_footer, this, false);
        this.mProgressBarLoadMore = (ProgressBar) this.mFooterView.findViewById(C0221R.id.load_more_progressBar);
        addFooterView(this.mFooterView);
        super.setOnScrollListener(this);
    }

    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    public void setOnScrollListener(OnScrollListener l) {
        this.mOnScrollListener = l;
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (this.mOnScrollListener != null) {
            this.mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
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
        if (!this.mIsLoadingMore && loadMore && this.mCurrentScrollState != 0) {
            this.mProgressBarLoadMore.setVisibility(0);
            this.mIsLoadingMore = true;
            Log.d("LoadMoreListView", "onLoadMore");
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == 0) {
            view.invalidateViews();
        }
        this.mCurrentScrollState = scrollState;
        if (this.mOnScrollListener != null) {
            this.mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }
}

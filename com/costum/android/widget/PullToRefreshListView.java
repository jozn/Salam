package com.costum.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.widget.C0221R;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class PullToRefreshListView extends ListView implements OnScrollListener {
    private boolean mBounceHack;
    protected int mCurrentScrollState;
    private RotateAnimation mFlipAnimation;
    protected LayoutInflater mInflater;
    private int mLastMotionY;
    public OnRefreshListener mOnRefreshListener;
    private OnScrollListener mOnScrollListener;
    private int mRefreshOriginalTopPadding;
    protected int mRefreshState;
    private RelativeLayout mRefreshView;
    private int mRefreshViewHeight;
    private ImageView mRefreshViewImage;
    private TextView mRefreshViewLastUpdated;
    private ProgressBar mRefreshViewProgress;
    private TextView mRefreshViewText;
    private RotateAnimation mReverseFlipAnimation;

    private class OnClickRefreshListener implements OnClickListener {
        private OnClickRefreshListener() {
        }

        public final void onClick(View v) {
            if (PullToRefreshListView.this.mRefreshState != 4) {
                PullToRefreshListView.this.prepareForRefresh();
                PullToRefreshListView.this.onRefresh();
            }
        }
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public PullToRefreshListView(Context context) {
        super(context);
        init(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mFlipAnimation = new RotateAnimation(0.0f, -180.0f, 1, 0.5f, 1, 0.5f);
        this.mFlipAnimation.setInterpolator(new LinearInterpolator());
        this.mFlipAnimation.setDuration(250);
        this.mFlipAnimation.setFillAfter(true);
        this.mReverseFlipAnimation = new RotateAnimation(-180.0f, 0.0f, 1, 0.5f, 1, 0.5f);
        this.mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
        this.mReverseFlipAnimation.setDuration(250);
        this.mReverseFlipAnimation.setFillAfter(true);
        this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mRefreshView = (RelativeLayout) this.mInflater.inflate(C0221R.layout.pull_to_refresh_header, this, false);
        this.mRefreshViewText = (TextView) this.mRefreshView.findViewById(C0221R.id.pull_to_refresh_text);
        this.mRefreshViewImage = (ImageView) this.mRefreshView.findViewById(C0221R.id.pull_to_refresh_image);
        this.mRefreshViewProgress = (ProgressBar) this.mRefreshView.findViewById(C0221R.id.pull_to_refresh_progress);
        this.mRefreshViewLastUpdated = (TextView) this.mRefreshView.findViewById(C0221R.id.pull_to_refresh_updated_at);
        this.mRefreshViewImage.setMinimumHeight(50);
        this.mRefreshView.setOnClickListener(new OnClickRefreshListener());
        this.mRefreshOriginalTopPadding = this.mRefreshView.getPaddingTop();
        this.mRefreshState = 1;
        addHeaderView(this.mRefreshView);
        super.setOnScrollListener(this);
        View view = this.mRefreshView;
        LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(-1, -2);
        }
        int childMeasureSpec = ViewGroup.getChildMeasureSpec(0, 0, layoutParams.width);
        int i = layoutParams.height;
        view.measure(childMeasureSpec, i > 0 ? MeasureSpec.makeMeasureSpec(i, 1073741824) : MeasureSpec.makeMeasureSpec(0, 0));
        this.mRefreshViewHeight = this.mRefreshView.getMeasuredHeight();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setSelection(1);
    }

    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        setSelection(1);
    }

    public void setOnScrollListener(OnScrollListener l) {
        this.mOnScrollListener = l;
    }

    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        this.mBounceHack = false;
        switch (event.getAction()) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                this.mLastMotionY = y;
                break;
            case Logger.SEVERE /*1*/:
                if (!isVerticalScrollBarEnabled()) {
                    setVerticalScrollBarEnabled(true);
                }
                if (getFirstVisiblePosition() == 0 && this.mRefreshState != 4) {
                    if ((this.mRefreshView.getBottom() < this.mRefreshViewHeight && this.mRefreshView.getTop() < 0) || this.mRefreshState != 3) {
                        if (this.mRefreshView.getBottom() < this.mRefreshViewHeight || this.mRefreshView.getTop() <= 0) {
                            resetHeader();
                            setSelection(1);
                            break;
                        }
                    }
                    this.mRefreshState = 4;
                    prepareForRefresh();
                    onRefresh();
                    break;
                }
                break;
            case Logger.WARNING /*2*/:
                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) {
                    if (this.mRefreshState == 3) {
                        if (isVerticalFadingEdgeEnabled()) {
                            setVerticalScrollBarEnabled(false);
                        }
                        this.mRefreshView.setPadding(this.mRefreshView.getPaddingLeft(), (int) (((double) ((((int) event.getHistoricalY(i)) - this.mLastMotionY) - this.mRefreshViewHeight)) / 1.7d), this.mRefreshView.getPaddingRight(), this.mRefreshView.getPaddingBottom());
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void resetHeaderPadding() {
        this.mRefreshView.setPadding(this.mRefreshView.getPaddingLeft(), this.mRefreshOriginalTopPadding, this.mRefreshView.getPaddingRight(), this.mRefreshView.getPaddingBottom());
    }

    private void resetHeader() {
        if (this.mRefreshState != 1) {
            this.mRefreshState = 1;
            resetHeaderPadding();
            this.mRefreshViewText.setText(C0221R.string.pull_to_refresh_tap_label);
            this.mRefreshViewImage.setImageResource(C0221R.drawable.ic_pulltorefresh_arrow);
            this.mRefreshViewImage.clearAnimation();
            this.mRefreshViewImage.setVisibility(8);
            this.mRefreshViewProgress.setVisibility(8);
        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (this.mCurrentScrollState != 1 || this.mRefreshState == 4) {
            if (this.mCurrentScrollState == 2 && firstVisibleItem == 0 && this.mRefreshState != 4) {
                setSelection(1);
                this.mBounceHack = true;
            } else if (this.mBounceHack && this.mCurrentScrollState == 2) {
                setSelection(1);
            }
        } else if (firstVisibleItem == 0) {
            this.mRefreshViewImage.setVisibility(0);
            if ((this.mRefreshView.getBottom() >= this.mRefreshViewHeight + 20 || this.mRefreshView.getTop() >= 0) && this.mRefreshState != 3) {
                this.mRefreshViewText.setText(C0221R.string.pull_to_refresh_release_label);
                this.mRefreshViewImage.clearAnimation();
                this.mRefreshViewImage.startAnimation(this.mFlipAnimation);
                this.mRefreshState = 3;
            } else if (this.mRefreshView.getBottom() < this.mRefreshViewHeight + 20 && this.mRefreshState != 2) {
                this.mRefreshViewText.setText(C0221R.string.pull_to_refresh_pull_label);
                if (this.mRefreshState != 1) {
                    this.mRefreshViewImage.clearAnimation();
                    this.mRefreshViewImage.startAnimation(this.mReverseFlipAnimation);
                }
                this.mRefreshState = 2;
            }
        } else {
            this.mRefreshViewImage.setVisibility(8);
            resetHeader();
        }
        if (this.mOnScrollListener != null) {
            this.mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.mCurrentScrollState = scrollState;
        if (this.mCurrentScrollState == 0) {
            this.mBounceHack = false;
        }
        if (this.mOnScrollListener != null) {
            this.mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    public final void prepareForRefresh() {
        resetHeaderPadding();
        this.mRefreshViewImage.setVisibility(8);
        this.mRefreshViewImage.setImageDrawable(null);
        this.mRefreshViewProgress.setVisibility(0);
        this.mRefreshViewText.setText(C0221R.string.pull_to_refresh_refreshing_label);
        this.mRefreshState = 4;
    }

    public final void onRefresh() {
        Log.d("PullToRefreshListView", "onRefresh");
        if (this.mOnRefreshListener != null) {
            this.mOnRefreshListener.onRefresh();
        }
    }

    public final void onRefreshComplete() {
        Log.d("PullToRefreshListView", "onRefreshComplete");
        resetHeader();
        if (this.mRefreshView.getBottom() > 0) {
            invalidateViews();
            setSelection(1);
        }
    }
}

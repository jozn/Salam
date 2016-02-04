package com.rokhgroup.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ViewAnimator;

public class SendCommentButton extends ViewAnimator implements OnClickListener {
    private int currentState;
    public OnSendClickListener onSendClickListener;
    private Runnable revertStateRunnable;
    private TextView tvDone;
    private TextView tvSend;

    public interface OnSendClickListener {
        void onSendClickListener(View view);
    }

    /* renamed from: com.rokhgroup.utils.SendCommentButton.1 */
    class C06851 implements Runnable {
        C06851() {
        }

        public final void run() {
            SendCommentButton.this.setCurrentState(0);
        }
    }

    public SendCommentButton(Context context) {
        super(context);
        this.revertStateRunnable = new C06851();
        init();
    }

    public SendCommentButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.revertStateRunnable = new C06851();
        init();
    }

    private void init() {
        View v = LayoutInflater.from(getContext()).inflate(2130903218, this, true);
        this.tvSend = (TextView) v.findViewById(2131362486);
        this.tvDone = (TextView) v.findViewById(2131362487);
        this.tvSend.setTypeface(Utils.GetNaskhBold(getContext()));
        this.tvDone.setTypeface(Utils.GetNaskhBold(getContext()));
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.currentState = 0;
        super.setOnClickListener(this);
    }

    protected void onDetachedFromWindow() {
        removeCallbacks(this.revertStateRunnable);
        super.onDetachedFromWindow();
    }

    public final void setCurrentState(int state) {
        if (state != this.currentState) {
            this.currentState = state;
            if (state == 1) {
                setEnabled(false);
                postDelayed(this.revertStateRunnable, 2000);
                setInAnimation(getContext(), 2130968596);
                setOutAnimation(getContext(), 2130968599);
            } else if (state == 0) {
                setEnabled(true);
                setInAnimation(getContext(), 2130968597);
                setOutAnimation(getContext(), 2130968598);
            }
            showNext();
        }
    }

    public void onClick(View v) {
        if (this.onSendClickListener != null) {
            this.onSendClickListener.onSendClickListener(this);
        }
    }

    public void setOnClickListener(OnClickListener l) {
    }
}

package com.shamchat.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class BlockingListView extends ListView {
    private boolean mBlockLayoutChildren;

    public BlockingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void layoutChildren() {
        if (!this.mBlockLayoutChildren) {
            super.layoutChildren();
        }
    }
}

package com.rokhgroup.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import com.rokhgroup.dialog.BaseDialog.Builder;

public abstract class DialogBuilder {
    public Builder ParsvidBuilder;
    protected Context ParsvidContext;
    public LayoutInflater ParsvidInflater;
    protected String ParsvidLeftButtonText;
    protected String ParsvidRightButtonText;

    public abstract Dialog build();

    public DialogBuilder(Context context) {
        this.ParsvidContext = context;
        this.ParsvidLeftButtonText = this.ParsvidContext.getString(2131493099);
        this.ParsvidRightButtonText = this.ParsvidContext.getString(2131493100);
        this.ParsvidBuilder = new Builder(this.ParsvidContext);
        this.ParsvidInflater = (LayoutInflater) this.ParsvidContext.getSystemService("layout_inflater");
    }
}

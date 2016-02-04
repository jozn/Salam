package com.shamchat.androidclient.preferences;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.view.View;

public class EditTextPreferenceWithValue extends EditTextPreference {
    CharSequence mSummary;

    public EditTextPreferenceWithValue(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextPreferenceWithValue(Context context) {
        super(context);
    }

    private void setSummaryToText(String text) {
        if (this.mSummary == null) {
            this.mSummary = getSummary();
        }
        if (text == null || text.length() == 0) {
            setSummary(this.mSummary);
        } else {
            setSummary(text);
        }
    }

    protected void onBindView(View view) {
        super.onBindView(view);
        setSummaryToText(getText());
    }

    public void setText(String text) {
        super.setText(text);
        setSummaryToText(text);
    }
}

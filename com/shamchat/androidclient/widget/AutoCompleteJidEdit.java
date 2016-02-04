package com.shamchat.androidclient.widget;

import android.content.Context;
import android.support.v7.appcompat.BuildConfig;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import java.util.ArrayList;

public class AutoCompleteJidEdit extends AutoCompleteTextView {
    boolean auto_appended;
    boolean ignore_selection_change;
    TextWatcher jtw;
    private ArrayAdapter<String> mServerAdapter;
    private String[] servers;
    ForegroundColorSpan span;
    private String userpart;

    private class JidTextWatcher implements TextWatcher {
        private JidTextWatcher() {
        }

        public final void afterTextChanged(Editable e) {
            String jid = e.toString();
            int len = e.length();
            int atpos = jid.indexOf("@");
            int secondat = atpos == -1 ? -1 : jid.indexOf("@", atpos + 1);
            if (AutoCompleteJidEdit.this.auto_appended && secondat >= 0) {
                AutoCompleteJidEdit.this.auto_appended = false;
                e.delete(secondat, len);
                e.removeSpan(AutoCompleteJidEdit.this.span);
            } else if (AutoCompleteJidEdit.this.auto_appended && atpos == 0) {
                AutoCompleteJidEdit.this.auto_appended = false;
                e.removeSpan(AutoCompleteJidEdit.this.span);
                e.delete(0, len);
            } else if (!AutoCompleteJidEdit.this.auto_appended && atpos == -1 && len > 0) {
                AutoCompleteJidEdit.this.auto_appended = true;
                AutoCompleteJidEdit.this.ignore_selection_change = true;
                e.append("@" + AutoCompleteJidEdit.this.servers[0]);
                atpos = len;
                AutoCompleteJidEdit.this.setSelection(len);
            }
            len = e.length();
            if (AutoCompleteJidEdit.this.auto_appended) {
                e.setSpan(AutoCompleteJidEdit.this.span, atpos, len, 18);
            } else if (len > 1) {
                String u = jid.split("@")[0];
                if (!u.equals(AutoCompleteJidEdit.this.userpart)) {
                    AutoCompleteJidEdit.this.userpart = u;
                    AutoCompleteJidEdit.this.mServerAdapter.setNotifyOnChange(false);
                    AutoCompleteJidEdit.this.mServerAdapter.clear();
                    for (String domain : AutoCompleteJidEdit.this.servers) {
                        AutoCompleteJidEdit.this.mServerAdapter.add(u + "@" + domain);
                    }
                    AutoCompleteJidEdit.this.mServerAdapter.notifyDataSetChanged();
                    AutoCompleteJidEdit.this.performFiltering(BuildConfig.VERSION_NAME, 0);
                    AutoCompleteJidEdit.this.showDropDown();
                }
            }
        }

        public final void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public final void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    public AutoCompleteJidEdit(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        this.userpart = null;
        this.auto_appended = false;
        this.ignore_selection_change = false;
        this.servers = getResources().getStringArray(2131099655);
        this.mServerAdapter = new ArrayAdapter(ctx, 17367050, new ArrayList(this.servers.length));
        setAdapter(this.mServerAdapter);
        this.span = new ForegroundColorSpan(getCurrentHintTextColor());
        setThreshold(3);
    }

    protected void onAttachedToWindow() {
        if (this.jtw == null) {
            this.jtw = new JidTextWatcher();
            addTextChangedListener(this.jtw);
        }
    }

    protected void onSelectionChanged(int selStart, int selEnd) {
        if (this.ignore_selection_change) {
            this.ignore_selection_change = false;
            return;
        }
        int atpos = getText().toString().indexOf("@");
        if (selStart > atpos + 1 || selEnd > atpos + 1) {
            this.auto_appended = false;
            getText().removeSpan(this.span);
        }
    }

    public boolean enoughToFilter() {
        return true;
    }
}

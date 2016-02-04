package com.rokhgroup.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import com.shamchat.androidclient.SHAMChatApplication;

public final class Utils {
    private static Typeface NaskhBold;
    private static Typeface NaskhRegular;

    public static String getTimeAgo$6909e107(long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }
        long diff = System.currentTimeMillis() - time;
        if (diff < 60000) {
            return SHAMChatApplication.getMyApplicationContext().getResources().getString(2131493320);
        }
        if (diff < 120000) {
            return SHAMChatApplication.getMyApplicationContext().getResources().getString(2131493337);
        }
        if (diff < 3000000) {
            return SHAMChatApplication.getMyApplicationContext().getResources().getString(2131493332);
        }
        if (diff < 5400000) {
            return SHAMChatApplication.getMyApplicationContext().getResources().getString(2131493319);
        }
        if (diff < 86400000) {
            return SHAMChatApplication.getMyApplicationContext().getResources().getString(2131493324);
        }
        if (diff < 172800000) {
            return SHAMChatApplication.getMyApplicationContext().getResources().getString(2131493326);
        }
        if (diff < 604800000) {
            return SHAMChatApplication.getMyApplicationContext().getResources().getString(2131493309);
        }
        if (diff < 1209600000) {
            return SHAMChatApplication.getMyApplicationContext().getResources().getString(2131493327);
        }
        if (diff < 31449600000L) {
            return SHAMChatApplication.getMyApplicationContext().getResources().getString(2131493347);
        }
        if (diff < 62899200000L) {
            return SHAMChatApplication.getMyApplicationContext().getResources().getString(2131493328);
        }
        return "2131493348";
    }

    public static String persianNum(String str) {
        char[] persianChars = new char[]{'\u0660', '\u0661', '\u0662', '\u0663', '\u0664', '\u0665', '\u0666', '\u0667', '\u0668', '\u0669'};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                builder.append(persianChars[str.charAt(i) - 48]);
            } else {
                builder.append(str.charAt(i));
            }
        }
        return builder.toString();
    }

    public static final Typeface GetNaskhRegular(Context context) {
        if (NaskhRegular == null) {
            NaskhRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Yekan.ttf");
        }
        return NaskhRegular;
    }

    public static final Typeface GetNaskhBold(Context context) {
        if (NaskhBold == null) {
            NaskhBold = Typeface.createFromAsset(context.getAssets(), "fonts/Yekan.ttf");
        }
        return NaskhBold;
    }

    public static final float getWidthInPx(Context context) {
        return (float) context.getResources().getDisplayMetrics().widthPixels;
    }

    public static void getTotalHeightofListView(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter != null) {
            int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
            int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), 1073741824);
            for (int i = 0; i < mAdapter.getCount(); i++) {
                View listItem = mAdapter.getView(i, null, listView);
                if (listItem != null) {
                    listItem.setLayoutParams(new LayoutParams(-2, -2));
                    listItem.measure(desiredWidth, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = (listView.getDividerHeight() * (mAdapter.getCount() - 1)) + totalHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }
}

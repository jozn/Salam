package com.shamchat.utils;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

public final class SoftKeyboardUtil {

    public interface OnSoftKeyBoardHideListener {
        void onSoftKeyBoardVisible(boolean z);
    }

    /* renamed from: com.shamchat.utils.SoftKeyboardUtil.1 */
    static class C11771 implements OnGlobalLayoutListener {
        final /* synthetic */ View val$decorView;
        final /* synthetic */ OnSoftKeyBoardHideListener val$listener;

        public C11771(View view, OnSoftKeyBoardHideListener onSoftKeyBoardHideListener) {
            this.val$decorView = view;
            this.val$listener = onSoftKeyBoardHideListener;
        }

        public final void onGlobalLayout() {
            boolean hide;
            boolean z = true;
            Rect rect = new Rect();
            this.val$decorView.getWindowVisibleDisplayFrame(rect);
            int displayHight = rect.bottom - rect.top;
            int hight = this.val$decorView.getHeight();
            if (((double) displayHight) / ((double) hight) > 0.8d) {
                hide = true;
            } else {
                hide = false;
            }
            if (Log.isLoggable("SoftKeyboardUtil", 3)) {
                boolean z2;
                Log.d("SoftKeyboardUtil", "DecorView display hight = " + displayHight);
                Log.d("SoftKeyboardUtil", "DecorView hight = " + hight);
                String str = "SoftKeyboardUtil";
                StringBuilder stringBuilder = new StringBuilder("softkeyboard visible = ");
                if (hide) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                Log.d(str, stringBuilder.append(z2).toString());
            }
            OnSoftKeyBoardHideListener onSoftKeyBoardHideListener = this.val$listener;
            if (hide) {
                z = false;
            }
            onSoftKeyBoardHideListener.onSoftKeyBoardVisible(z);
        }
    }
}

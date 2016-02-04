package com.rokhgroup.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;
import com.rokhgroup.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class BaseDialog extends Dialog {

    public static class Builder {
        private List<Pair<String, OnClickListener>> ParsvidBtnList;
        public View ParsvidContentView;
        private boolean ParsvidContentViewFillParentWithNoScroll;
        private Context ParsvidContext;
        public CharSequence ParsvidMsg;
        private int ParsvidTheme;
        public String ParsvidTitle;
        private View ParsvidTitleView;

        /* renamed from: com.rokhgroup.dialog.BaseDialog.Builder.1 */
        class C06581 implements OnTouchListener {
            final /* synthetic */ TextView val$textView;

            C06581(TextView textView) {
                this.val$textView = textView;
            }

            public final boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                        this.val$textView.setPressed(true);
                        break;
                    case Logger.SEVERE /*1*/:
                    case Logger.INFO /*3*/:
                        this.val$textView.setPressed(false);
                        break;
                }
                return false;
            }
        }

        public Builder(Context activity) {
            this(activity, (byte) 0);
        }

        private Builder(Context context, byte b) {
            this.ParsvidContentViewFillParentWithNoScroll = false;
            this.ParsvidContext = context;
            this.ParsvidTheme = 2131558548;
            this.ParsvidBtnList = new ArrayList();
        }

        public final Builder addButton(String buttonText, OnClickListener listener) {
            if (buttonText != null) {
                this.ParsvidBtnList.add(new Pair(buttonText, listener));
            }
            return this;
        }

        public final BaseDialog create() {
            LayoutInflater inflater = (LayoutInflater) this.ParsvidContext.getSystemService("layout_inflater");
            BaseDialog dialog = new BaseDialog(this.ParsvidContext, this.ParsvidTheme);
            View layout = inflater.inflate(2130903129, null);
            TextView titleView = (TextView) layout.findViewById(2131362188);
            titleView.setTypeface(Utils.GetNaskhBold(this.ParsvidContext));
            LinearLayout titleLayout = (LinearLayout) layout.findViewById(2131362187);
            LinearLayout contentLayout = (LinearLayout) layout.findViewById(2131362190);
            LinearLayout btnLayout = (LinearLayout) layout.findViewById(2131362191);
            ScrollView contentScrollView = (ScrollView) layout.findViewById(2131362189);
            if (this.ParsvidContentViewFillParentWithNoScroll) {
                ViewGroup vg = (ViewGroup) layout;
                int index = vg.indexOfChild(contentScrollView);
                contentScrollView.removeAllViews();
                vg.removeView(contentScrollView);
                vg.addView(contentLayout, index);
                contentLayout.setLayoutParams(new LayoutParams(-1, -2, 3.0f));
                contentLayout.setBackgroundDrawable(contentScrollView.getBackground());
            }
            if (this.ParsvidContentView == null) {
                this.ParsvidContentView = new TextView(this.ParsvidContext);
                this.ParsvidContentView.setPadding(30, 30, 20, 30);
                ((TextView) this.ParsvidContentView).setTextSize(16.0f);
                View view = this.ParsvidContentView;
                ((TextView) r0).setText(this.ParsvidMsg);
            }
            contentLayout.addView(this.ParsvidContentView);
            titleLayout.setGravity(16);
            if (this.ParsvidTitleView != null) {
                titleLayout.removeAllViews();
                titleLayout.addView(this.ParsvidTitleView);
            } else if (this.ParsvidTitle != null) {
                titleView.setText(this.ParsvidTitle);
            } else {
                titleLayout.setVisibility(8);
            }
            if (this.ParsvidBtnList.size() <= 0) {
                btnLayout.setVisibility(8);
            } else {
                if (this.ParsvidBtnList.size() == 1) {
                    Pair<String, OnClickListener> btn = (Pair) this.ParsvidBtnList.get(0);
                    btnLayout.addView(makeBtn(2130838031, (String) btn.first, (OnClickListener) btn.second));
                } else {
                    Pair<String, OnClickListener> first = (Pair) this.ParsvidBtnList.get(0);
                    btnLayout.addView(makeBtn(2130838032, (String) first.first, (OnClickListener) first.second));
                    ImageView divider = new ImageButton(this.ParsvidContext);
                    divider.setBackgroundResource(2130837618);
                    divider.setLayoutParams(new LinearLayout.LayoutParams(4, -2));
                    btnLayout.addView(divider);
                    int i = 1;
                    while (true) {
                        if (i >= this.ParsvidBtnList.size() - 1) {
                            break;
                        }
                        Pair<String, OnClickListener> item = (Pair) this.ParsvidBtnList.get(i);
                        btnLayout.addView(makeBtn(2130838033, (String) item.first, (OnClickListener) item.second));
                        ImageView divider1 = new ImageButton(this.ParsvidContext);
                        divider1.setBackgroundResource(2130837618);
                        divider1.setLayoutParams(new LinearLayout.LayoutParams(4, -2));
                        btnLayout.addView(divider1);
                        i++;
                    }
                    Pair<String, OnClickListener> last = (Pair) this.ParsvidBtnList.get(this.ParsvidBtnList.size() - 1);
                    btnLayout.addView(makeBtn(2130838034, (String) last.first, (OnClickListener) last.second));
                }
            }
            btnLayout.setPadding(0, 0, 0, 0);
            dialog.setContentView(layout, new ViewGroup.LayoutParams(-1, -1));
            return dialog;
        }

        private View makeBtn(int backgroundResid, String text, OnClickListener listener) {
            LayoutInflater layoutInflater = (LayoutInflater) this.ParsvidContext.getSystemService("layout_inflater");
            Typeface GetNaskhBold = Utils.GetNaskhBold(this.ParsvidContext);
            View inflate = layoutInflater.inflate(2130903130, null);
            int paddingLeft = inflate.getPaddingLeft();
            int paddingTop = inflate.getPaddingTop();
            int paddingRight = inflate.getPaddingRight();
            int paddingBottom = inflate.getPaddingBottom();
            inflate.setBackgroundResource(backgroundResid);
            inflate.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            inflate.setLayoutParams(new LayoutParams(-2, -2, 3.0f));
            TextView textView = (TextView) inflate.findViewById(2131362192);
            textView.setText(text);
            textView.setTypeface(GetNaskhBold);
            inflate.setOnClickListener(listener);
            inflate.setOnTouchListener(new C06581(textView));
            return inflate;
        }
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
    }

    public final void show() {
        super.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = -1;
        lp.height = -2;
        window.setAttributes(lp);
    }
}

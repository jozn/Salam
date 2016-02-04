package com.shamchat.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.shamchat.adapters.PagerAdapter;
import com.shamchat.utils.Utils;
import java.util.List;
import java.util.Vector;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class SearchFriendsViewPagerActivity extends FragmentActivity implements OnPageChangeListener {
    private ImageButton backButton;
    TextView defaulttext;
    private EditText editText;
    TextView facebooktext;
    public FragmentCommunicator fragmentCommunicator;
    LinearLayout ln1;
    LinearLayout ln2;
    LinearLayout ln3;
    LinearLayout lncontainer;
    private PagerAdapter mPagerAdapter;
    TextView twittertext;

    public interface FragmentCommunicator {
        void passDataToFragment(String str);
    }

    /* renamed from: com.shamchat.activity.SearchFriendsViewPagerActivity.1 */
    class C08611 implements OnEditorActionListener {
        C08611() {
        }

        public final boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId != 3) {
                return false;
            }
            String searchQuery = SearchFriendsViewPagerActivity.this.editText.getText().toString().trim();
            if (searchQuery == null || searchQuery.length() <= 5) {
                SearchFriendsViewPagerActivity.this.editText.setError(SearchFriendsViewPagerActivity.this.getText(2131493195));
                return false;
            }
            System.out.println("Searching.....");
            SearchFriendsViewPagerActivity.this.fragmentCommunicator.passDataToFragment(searchQuery);
            Utils.hideKeyboard(SearchFriendsViewPagerActivity.this.editText, SearchFriendsViewPagerActivity.this.getApplicationContext());
            return true;
        }
    }

    /* renamed from: com.shamchat.activity.SearchFriendsViewPagerActivity.2 */
    class C08622 implements OnClickListener {
        C08622() {
        }

        public final void onClick(View v) {
            SearchFriendsViewPagerActivity.this.finish();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        super.setContentView(2130903225);
        initialisePaging();
    }

    private void initialisePaging() {
        List<Fragment> fragments = new Vector();
        fragments.add(Fragment.instantiate(this, DefaultSearchFragment.class.getName()));
        this.backButton = (ImageButton) findViewById(2131362513);
        this.editText = (EditText) findViewById(2131362514);
        this.editText.setOnEditorActionListener(new C08611());
        this.backButton.setOnClickListener(new C08622());
        this.defaulttext = (TextView) findViewById(2131362516);
        this.mPagerAdapter = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager) super.findViewById(2131362521);
        pager.setOnPageChangeListener(this);
        pager.setAdapter(this.mPagerAdapter);
    }

    public void onPageScrollStateChanged(int arg0) {
    }

    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    public void onPageSelected(int arg0) {
        Log.d("position", String.valueOf(arg0));
        switch (arg0) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                this.defaulttext.setBackgroundColor(Color.parseColor("#8b8c8e"));
                this.defaulttext.setTextColor(Color.parseColor("#ffffff"));
            case Logger.SEVERE /*1*/:
                this.defaulttext.setBackgroundColor(Color.parseColor("#ffffff"));
                this.defaulttext.setTextColor(Color.parseColor("#8b8c8e"));
            case Logger.WARNING /*2*/:
                this.defaulttext.setBackgroundColor(Color.parseColor("#ffffff"));
                this.defaulttext.setTextColor(Color.parseColor("#8b8c8e"));
            default:
        }
    }
}

package com.shamchat.moments;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.utils.Utils;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.List;

public class MomentSummaryViewPagerAdapter extends PagerAdapter {
    private Context context;
    private List<String> imgUrls;
    private LayoutInflater inflater;
    private File mainFolder;

    public MomentSummaryViewPagerAdapter(Context context, List<String> imgUrls) {
        this.imgUrls = imgUrls;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mainFolder = Utils.getMomentsFolder();
    }

    public int getCount() {
        return this.imgUrls.size();
    }

    public boolean isViewFromObject(View view, Object arg1) {
        return view == ((ImageView) arg1);
    }

    public View instantiateItem(View container, int position) {
        ImageView imageView = new ImageView(this.context);
        imageView.setScaleType(ScaleType.FIT_CENTER);
        try {
            String imageUrl = (String) this.imgUrls.get(position);
            System.out.println("AA momentsummary " + imageUrl);
            if (imageUrl.contains("http://")) {
                Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(Uri.parse(imageUrl)).resize(250, 250).into(imageView, null);
                System.out.println("AA Loading with picasso remote " + imageUrl);
            } else {
                Picasso.with(SHAMChatApplication.getMyApplicationContext()).load("file://" + ((String) this.imgUrls.get(position))).resize(250, 250).into(imageView, null);
                System.out.println("AA Loading with picasso local " + imageUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}

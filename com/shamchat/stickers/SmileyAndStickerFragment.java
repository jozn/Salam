package com.shamchat.stickers;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.media.TransportMediator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.StickerProvider;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class SmileyAndStickerFragment extends Fragment implements OnClickListener {
    public static String STICKER_PACK_ID;
    private ContentResolver contentResolver;
    private Bundle fragmentBundle;
    private FragmentManager fragmentManager;
    private boolean isMoment;
    private Handler mainHandler;
    private Fragment smileyFragment;
    private LinearLayout stickerBottomLayout;
    private List<ImageView> stickerButtonList;
    private Cursor stickerCursor;
    private Fragment stickerFragment;
    private ContentObserver stickerObserver;

    /* renamed from: com.shamchat.stickers.SmileyAndStickerFragment.1 */
    class C11691 implements OnClickListener {
        C11691() {
        }

        public final void onClick(View v) {
            SmileyAndStickerFragment.this.fragmentManager.beginTransaction().setCustomAnimations(2130968589, 2130968590).replace(2131362340, SmileyAndStickerFragment.this.smileyFragment).addToBackStack(null).commit();
        }
    }

    private class StickerObserver extends ContentObserver {
        public StickerObserver() {
            super(SmileyAndStickerFragment.this.mainHandler);
        }

        public final void onChange(boolean selfChange) {
            super.onChange(selfChange);
            try {
                SmileyAndStickerFragment.this.stickerCursor = SmileyAndStickerFragment.this.contentResolver.query(StickerProvider.CONTENT_URI_STICKER, null, "is_sticker_downloaded=?", new String[]{"1"}, null);
                for (ImageView imageView : SmileyAndStickerFragment.this.stickerButtonList) {
                    SmileyAndStickerFragment.this.stickerBottomLayout.removeView(imageView);
                }
                if (SmileyAndStickerFragment.this.stickerCursor != null) {
                    SmileyAndStickerFragment.this.updateFragments(SmileyAndStickerFragment.this.stickerCursor);
                }
            } catch (Exception e) {
            }
        }
    }

    public SmileyAndStickerFragment() {
        this.mainHandler = new Handler();
        this.stickerObserver = new StickerObserver();
        this.isMoment = false;
    }

    static {
        STICKER_PACK_ID = "sticker_pack_id";
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(2130903177, container, false);
        this.contentResolver = getActivity().getContentResolver();
        this.stickerCursor = this.contentResolver.query(StickerProvider.CONTENT_URI_STICKER, null, "is_sticker_downloaded=?", new String[]{"1"}, null);
        initComponentAndFragments(view);
        if (this.stickerCursor != null) {
            updateFragments(this.stickerCursor);
        }
        this.contentResolver.registerContentObserver(StickerProvider.CONTENT_URI_STICKER, true, this.stickerObserver);
        return view;
    }

    private void initComponentAndFragments(View view) {
        this.fragmentManager = getFragmentManager();
        this.stickerBottomLayout = (LinearLayout) view.findViewById(2131362343);
        this.stickerButtonList = new ArrayList();
        this.fragmentBundle = getArguments();
        if (this.fragmentBundle == null || !this.fragmentBundle.containsKey("is_group_chat")) {
            this.fragmentBundle = new Bundle();
            this.isMoment = true;
            return;
        }
        if (this.fragmentBundle.getBoolean("is_group_chat")) {
            this.smileyFragment = new ShamSmileyAndStickerGroup();
        } else {
            this.smileyFragment = new ShamSmileyAndSticker();
        }
        this.fragmentManager.beginTransaction().setCustomAnimations(2130968589, 2130968590).replace(2131362340, this.smileyFragment).addToBackStack(null).commit();
        ((ImageView) view.findViewById(2131362344)).setOnClickListener(new C11691());
    }

    private void updateFragments(Cursor cursor) {
        int i = 0;
        while (i < cursor.getCount()) {
            try {
                cursor.moveToPosition(i);
                ImageView rowImageView = new ImageView(SHAMChatApplication.getMyApplicationContext());
                String stickerPackIconUrl = cursor.getString(cursor.getColumnIndex("sticker_pack_icon"));
                rowImageView.setId(i);
                rowImageView.setOnClickListener(this);
                rowImageView.setTag(cursor.getString(cursor.getColumnIndex("pack_id")));
                Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(Uri.parse(stickerPackIconUrl)).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).into(rowImageView, null);
                LayoutParams params = new LayoutParams(-2, -2);
                params.setMargins(5, 10, 5, 10);
                rowImageView.setLayoutParams(params);
                this.stickerBottomLayout.addView(rowImageView);
                this.stickerButtonList.add(rowImageView);
                i++;
            } catch (OutOfMemoryError e) {
                System.exit(0);
                return;
            }
        }
        if (!this.isMoment) {
            ImageView stickerShop = new ImageView(SHAMChatApplication.getMyApplicationContext());
            stickerShop.setId(99);
            stickerShop.setOnClickListener(this);
            stickerShop.setImageResource(2130838067);
            params = new LayoutParams(-2, -2);
            params.setMargins(5, 10, 5, 10);
            stickerShop.setLayoutParams(params);
            this.stickerBottomLayout.addView(stickerShop);
            this.stickerButtonList.add(stickerShop);
        }
    }

    public void onClick(View v) {
        if (v.getId() != 99) {
            for (ImageView id : this.stickerButtonList) {
                if (id.getId() == v.getId()) {
                    this.fragmentBundle.putString(STICKER_PACK_ID, (String) v.getTag());
                    this.stickerFragment = new StickerFragment();
                    this.stickerFragment.setArguments(this.fragmentBundle);
                    this.fragmentManager.beginTransaction().setCustomAnimations(2130968589, 2130968590).replace(2131362340, this.stickerFragment).addToBackStack(null).commit();
                }
            }
            return;
        }
        startActivity(new Intent(getActivity(), StickerShopActivity.class));
    }
}

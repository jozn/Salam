package com.shamchat.moments;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.shamchat.adapters.StickerAdapter;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.StickerProvider;
import com.shamchat.stickers.StickerShopActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MomentStickerBrowserActivity extends AppCompatActivity {
    private ContentResolver contentResolver;
    private Handler handler;
    private List<String> smileyResources;
    private LinearLayout stickerBottomLayout;
    private Cursor stickerCursor;
    private GridView stickerGridView;
    private ContentObserver stickerObserver;

    /* renamed from: com.shamchat.moments.MomentStickerBrowserActivity.1 */
    class C11331 implements OnClickListener {
        C11331() {
        }

        public final void onClick(View v) {
            MomentStickerBrowserActivity momentStickerBrowserActivity = MomentStickerBrowserActivity.this;
            MomentStickerBrowserActivity.this.smileyResources;
            StickerAdapter adapter = new StickerAdapter(momentStickerBrowserActivity);
            MomentStickerBrowserActivity.this.stickerGridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    /* renamed from: com.shamchat.moments.MomentStickerBrowserActivity.2 */
    class C11342 implements OnClickListener {
        C11342() {
        }

        public final void onClick(View v) {
            MomentStickerBrowserActivity.this.stickerCursor.moveToPosition(v.getId());
            Arrays.asList(MomentStickerBrowserActivity.this.stickerCursor.getString(MomentStickerBrowserActivity.this.stickerCursor.getColumnIndex("local_file_url")).split("\\s*,\\s*"));
            StickerAdapter adapter = new StickerAdapter(MomentStickerBrowserActivity.this);
            MomentStickerBrowserActivity.this.stickerGridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    /* renamed from: com.shamchat.moments.MomentStickerBrowserActivity.3 */
    class C11353 implements OnClickListener {
        C11353() {
        }

        public final void onClick(View v) {
            MomentStickerBrowserActivity.this.startActivity(new Intent(MomentStickerBrowserActivity.this.getApplicationContext(), StickerShopActivity.class));
        }
    }

    private class StickerObserver extends ContentObserver {

        /* renamed from: com.shamchat.moments.MomentStickerBrowserActivity.StickerObserver.1 */
        class C11361 implements Runnable {
            C11361() {
            }

            public final void run() {
                MomentStickerBrowserActivity.this.stickerCursor = MomentStickerBrowserActivity.this.contentResolver.query(StickerProvider.CONTENT_URI_STICKER, null, "is_sticker_downloaded=?", new String[]{"1"}, null);
                MomentStickerBrowserActivity.this.loadAndDisplayStickers();
            }
        }

        public StickerObserver() {
            super(MomentStickerBrowserActivity.this.handler);
        }

        public final void onChange(boolean selfChange) {
            super.onChange(selfChange);
            MomentStickerBrowserActivity.this.runOnUiThread(new C11361());
        }
    }

    public MomentStickerBrowserActivity() {
        this.stickerObserver = new StickerObserver();
        this.handler = new Handler();
    }

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(1);
        setContentView(2130903185);
        this.stickerBottomLayout = (LinearLayout) findViewById(2131362343);
        this.stickerGridView = (GridView) findViewById(2131362234);
        this.contentResolver = getContentResolver();
        this.contentResolver.registerContentObserver(StickerProvider.CONTENT_URI_STICKER, true, this.stickerObserver);
        this.stickerCursor = this.contentResolver.query(StickerProvider.CONTENT_URI_STICKER, null, "is_sticker_downloaded=?", new String[]{"1"}, null);
        this.smileyResources = loadAllSmileys();
        loadAndDisplayStickers();
    }

    private void loadAndDisplayStickers() {
        LayoutParams params;
        this.stickerBottomLayout.removeAllViews();
        ImageView smileyView = new ImageView(SHAMChatApplication.getMyApplicationContext());
        smileyView.setId(99);
        smileyView.setOnClickListener(new C11331());
        smileyView.setImageResource(2130837661);
        LayoutParams parameters = new LayoutParams(-2, -2);
        parameters.setMargins(20, 10, 5, 10);
        smileyView.setLayoutParams(parameters);
        this.stickerBottomLayout.addView(smileyView);
        if (this.stickerCursor.getCount() > 0) {
            for (int i = 0; i < this.stickerCursor.getCount(); i++) {
                this.stickerCursor.moveToPosition(i);
                ImageView rowImageView = new ImageView(SHAMChatApplication.getMyApplicationContext());
                Bitmap bitmap = BitmapFactory.decodeFile(this.stickerCursor.getString(this.stickerCursor.getColumnIndex("sticker_pack_icon")));
                rowImageView.setId(i);
                rowImageView.setOnClickListener(new C11342());
                rowImageView.setTag(this.stickerCursor.getString(this.stickerCursor.getColumnIndex("pack_id")));
                rowImageView.setImageBitmap(bitmap);
                params = new LayoutParams(-2, -2);
                params.setMargins(5, 10, 5, 10);
                rowImageView.setLayoutParams(params);
                this.stickerBottomLayout.addView(rowImageView);
            }
        }
        ImageView stickerShop = new ImageView(SHAMChatApplication.getMyApplicationContext());
        stickerShop.setId(99);
        stickerShop.setOnClickListener(new C11353());
        stickerShop.setImageResource(2130838067);
        params = new LayoutParams(-2, -2);
        params.setMargins(5, 10, 5, 10);
        stickerShop.setLayoutParams(params);
        this.stickerBottomLayout.addView(stickerShop);
        StickerAdapter adapter = new StickerAdapter(this);
        this.stickerGridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private List<String> loadAllSmileys() {
        List<String> res = new ArrayList();
        res.add("2130837765");
        res.add("2130837766");
        res.add("2130837767");
        res.add("2130837768");
        res.add("2130837769");
        res.add("2130837770");
        res.add("2130837771");
        res.add("2130837772");
        res.add("2130837773");
        res.add("2130837775");
        res.add("2130837776");
        res.add("2130837777");
        res.add("2130837778");
        res.add("2130837779");
        res.add("2130837780");
        res.add("2130837781");
        res.add("2130837782");
        res.add("2130837783");
        res.add("2130837784");
        res.add("2130837785");
        res.add("2130837786");
        res.add("2130837787");
        res.add("2130837788");
        res.add("2130837789");
        res.add("2130837790");
        res.add("2130837791");
        res.add("2130837792");
        res.add("2130837793");
        res.add("2130837794");
        res.add("2130837795");
        res.add("2130837796");
        res.add("2130837797");
        res.add("2130837799");
        res.add("2130837800");
        res.add("2130837801");
        res.add("2130837802");
        res.add("2130837803");
        res.add("2130837804");
        res.add("2130837805");
        res.add("2130837806");
        res.add("2130837807");
        res.add("2130837808");
        res.add("2130837809");
        res.add("2130837810");
        res.add("2130837811");
        res.add("2130837812");
        res.add("2130837813");
        res.add("2130837814");
        res.add("2130837815");
        res.add("2130837816");
        res.add("2130837817");
        res.add("2130837818");
        res.add("2130837819");
        res.add("2130837821");
        res.add("2130837822");
        res.add("2130837823");
        res.add("2130837824");
        res.add("2130837825");
        res.add("2130837826");
        res.add("2130837828");
        res.add("2130837827");
        res.add("2130837829");
        res.add("2130837830");
        res.add("2130837831");
        res.add("2130837832");
        res.add("2130837833");
        res.add("2130837834");
        res.add("2130837835");
        res.add("2130837836");
        res.add("2130837837");
        res.add("2130837839");
        res.add("2130837838");
        res.add("2130837840");
        res.add("2130837841");
        res.add("2130837842");
        res.add("2130837843");
        res.add("2130837844");
        res.add("2130837845");
        res.add("2130837846");
        res.add("2130837847");
        res.add("2130837849");
        res.add("2130837850");
        res.add("2130837851");
        return res;
    }

    public void addSticker(String url) {
        Intent data = new Intent();
        data.putExtra("stickerUrl", url);
        setResult(-1, data);
        finish();
    }
}

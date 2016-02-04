package com.shamchat.stickers;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import com.path.android.jobqueue.JobManager;
import com.shamchat.adapters.StickerShopAdapter;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.StickerProvider;
import com.shamchat.events.StickerPackDBCompletedEvent;
import de.greenrobot.event.EventBus;

public class StickerShopActivity extends AppCompatActivity {
    private StickerShopAdapter adapter;
    private JobManager jobManager;
    private ListView list;

    /* renamed from: com.shamchat.stickers.StickerShopActivity.1 */
    class C11701 implements Runnable {
        final /* synthetic */ Cursor val$cursor;

        C11701(Cursor cursor) {
            this.val$cursor = cursor;
        }

        public final void run() {
            StickerShopActivity.this.adapter = new StickerShopAdapter(StickerShopActivity.this.getApplicationContext(), this.val$cursor);
            StickerShopActivity.this.list.setAdapter(StickerShopActivity.this.adapter);
            StickerShopActivity.this.adapter.notifyDataSetChanged();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        setContentView(2130903103);
        this.list = (ListView) findViewById(2131362087);
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        try {
            EventBus.getDefault().register(this, true, 0);
        } catch (Throwable th) {
        }
        refreshAdapter();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    private void refreshAdapter() {
        runOnUiThread(new C11701(getContentResolver().query(StickerProvider.CONTENT_URI_STICKER, null, null, null, null)));
    }

    public void onEventBackgroundThread(StickerPackDBCompletedEvent event) {
        System.out.println("StickerShopActivity StickerPackDBCompletedEvent completed");
        refreshAdapter();
    }
}

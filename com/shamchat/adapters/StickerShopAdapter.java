package com.shamchat.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.media.TransportMediator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.path.android.jobqueue.JobManager;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.jobs.DisableStickerPackDBLoadJob;
import com.shamchat.jobs.StickerPackDownloadJob;
import com.squareup.picasso.Picasso;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class StickerShopAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private JobManager jobManager;
    private Cursor stickerCursor;

    /* renamed from: com.shamchat.adapters.StickerShopAdapter.1 */
    class C10121 implements OnClickListener {
        final /* synthetic */ int val$status;
        final /* synthetic */ String val$stickerId;

        C10121(int i, String str) {
            this.val$status = i;
            this.val$stickerId = str;
        }

        public final void onClick(View v) {
            if (this.val$status != 1) {
                StickerPackDownloadJob stickerPackDownloadJob = new StickerPackDownloadJob();
                stickerPackDownloadJob.stickerPackId = this.val$stickerId;
                StickerShopAdapter.this.jobManager.addJobInBackground(stickerPackDownloadJob);
                return;
            }
            DisableStickerPackDBLoadJob disableStickerPackDBLoadJob = new DisableStickerPackDBLoadJob();
            disableStickerPackDBLoadJob.stickerPackId = this.val$stickerId;
            StickerShopAdapter.this.jobManager.addJobInBackground(disableStickerPackDBLoadJob);
        }
    }

    class ViewHolder {
        public TextView description;
        public Button download;
        public ImageView imgSmiley;
        public TextView name;

        ViewHolder() {
        }
    }

    public final /* bridge */ /* synthetic */ Object getItem(int i) {
        return null;
    }

    public StickerShopAdapter(Context context, Cursor stickerCursor) {
        this.stickerCursor = stickerCursor;
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public final int getCount() {
        return this.stickerCursor.getCount();
    }

    public final long getItemId(int position) {
        return 0;
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = this.inflater.inflate(2130903234, null);
            holder = new ViewHolder();
            holder.imgSmiley = (ImageView) convertView.findViewById(2131362542);
            holder.description = (TextView) convertView.findViewById(2131362545);
            holder.name = (TextView) convertView.findViewById(2131362544);
            holder.download = (Button) convertView.findViewById(2131362549);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        this.stickerCursor.moveToPosition(position);
        String stickerId = this.stickerCursor.getString(this.stickerCursor.getColumnIndex("pack_id"));
        String name = this.stickerCursor.getString(this.stickerCursor.getColumnIndex("pack_name"));
        String desc = this.stickerCursor.getString(this.stickerCursor.getColumnIndex("pack_desc"));
        String thumbUrl = this.stickerCursor.getString(this.stickerCursor.getColumnIndex("thumnail_url"));
        int status = this.stickerCursor.getInt(this.stickerCursor.getColumnIndex("is_sticker_downloaded"));
        holder.name.setText(name);
        holder.description.setText(desc);
        Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(Uri.parse(thumbUrl)).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).into(holder.imgSmiley, null);
        switch (status) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                holder.download.setText(2131493105);
                break;
            case Logger.SEVERE /*1*/:
                holder.download.setText(2131493101);
                holder.download.setEnabled(true);
                break;
            case Logger.WARNING /*2*/:
                holder.download.setText(2131493106);
                holder.download.setEnabled(false);
                break;
            case Logger.INFO /*3*/:
                holder.download.setText(2131493432);
                holder.download.setEnabled(true);
                break;
        }
        holder.download.setOnClickListener(new C10121(status, stickerId));
        return convertView;
    }
}

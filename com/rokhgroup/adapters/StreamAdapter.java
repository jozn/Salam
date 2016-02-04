package com.rokhgroup.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.rokhgroup.activities.JahanbinDetailsActivity;
import com.rokhgroup.activities.Likers;
import com.rokhgroup.activities.UserProfile;
import com.rokhgroup.adapters.item.StreamItem;
import com.rokhgroup.dialog.DialogBuilder;
import com.rokhgroup.utils.RokhImageView;
import com.rokhgroup.utils.RokhPref;
import com.rokhgroup.utils.RokhgroupRestClient;
import com.rokhgroup.utils.Utils;
import com.shamchat.activity.AddFavoriteTextActivity;
import com.shamchat.activity.Yekanedit;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.roundedimage.RoundedDrawable;
import com.shamchat.roundedimage.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;
import cz.msebera.android.httpclient.Header;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class StreamAdapter extends BaseAdapter {
    String CURRENT_USER_ID;
    String CURRENT_USER_TOKEN;
    String POST_DESC;
    RokhPref Session;
    String URL;
    Dialog dialog;
    Activity mActivity;
    private Context mContext;
    public ArrayList<StreamItem> mData;
    private LayoutInflater mInflater;
    private final Transformation mTransformation;

    /* renamed from: com.rokhgroup.adapters.StreamAdapter.10 */
    class AnonymousClass10 extends DialogBuilder {
        final /* synthetic */ int val$postId;

        /* renamed from: com.rokhgroup.adapters.StreamAdapter.10.1 */
        class C06371 extends JsonHttpResponseHandler {
            final /* synthetic */ Yekanedit val$posttext;

            C06371(Yekanedit yekanedit) {
                this.val$posttext = yekanedit;
            }

            public final void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("POST DETAILS RESULT", response.toString());
                if (response != null) {
                    try {
                        JSONArray result = response.getJSONArray("objects");
                        if (result.length() > 0) {
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject Item = result.getJSONObject(i);
                                StreamAdapter.this.POST_DESC = Item.getString(AddFavoriteTextActivity.EXTRA_RESULT_TEXT);
                                this.val$posttext.setText(StreamAdapter.this.POST_DESC);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            public final void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                StreamAdapter.this.dialog.dismiss();
                Toast.makeText(StreamAdapter.this.mContext, "An error occurred, try again later", 0).show();
            }
        }

        /* renamed from: com.rokhgroup.adapters.StreamAdapter.10.2 */
        class C06382 implements OnClickListener {
            C06382() {
            }

            public final void onClick(View v) {
                StreamAdapter.this.dialog.dismiss();
            }
        }

        /* renamed from: com.rokhgroup.adapters.StreamAdapter.10.3 */
        class C06403 implements OnClickListener {
            final /* synthetic */ Yekanedit val$posttext;

            /* renamed from: com.rokhgroup.adapters.StreamAdapter.10.3.1 */
            class C06391 extends TextHttpResponseHandler {
                C06391() {
                }

                public final void onSuccess$79de7b53(String response) {
                    Log.e("UPDATE POST RESULT", response);
                    if (response.equals("success")) {
                        StreamAdapter.this.dialog.dismiss();
                        Toast.makeText(StreamAdapter.this.mContext, "Post updated successfully", 0).show();
                    }
                }

                public final void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                    StreamAdapter.this.dialog.dismiss();
                    Toast.makeText(StreamAdapter.this.mContext, "An error occurred, try again later", 0).show();
                }
            }

            C06403(Yekanedit yekanedit) {
                this.val$posttext = yekanedit;
            }

            public final void onClick(View v) {
                String newDesc = this.val$posttext.getText().toString();
                String URL = "http://social.rabtcdn.com/pin/d/post/update/" + AnonymousClass10.this.val$postId + "/?token=" + StreamAdapter.this.CURRENT_USER_TOKEN;
                RequestParams params = new RequestParams();
                params.put(AddFavoriteTextActivity.EXTRA_RESULT_TEXT, newDesc);
                params.put("category", "1");
                Log.e("UPDATE POST LINK", URL);
                Log.e("UPDATE PARAMS", params.toString());
                RokhgroupRestClient.post(URL, params, new C06391());
            }
        }

        AnonymousClass10(Context context, int i) {
            this.val$postId = i;
            super(context);
        }

        public final Dialog build() {
            this.ParsvidBuilder.ParsvidTitle = BuildConfig.VERSION_NAME;
            View contentView = this.ParsvidInflater.inflate(2130903236, null);
            Yekanedit posttext = (Yekanedit) contentView.findViewById(2131362550);
            String URL_GET_POST = "http://social.rabtcdn.com/pin/api/post/" + this.val$postId + "/details/?token=" + StreamAdapter.this.CURRENT_USER_TOKEN;
            Log.e("POST DETAILS URL", URL_GET_POST);
            RokhgroupRestClient.get$6a529083(URL_GET_POST, new C06371(posttext));
            this.ParsvidBuilder.ParsvidContentView = contentView;
            this.ParsvidBuilder.addButton("Cancel", new C06382());
            this.ParsvidBuilder.addButton("Save", new C06403(posttext));
            return this.ParsvidBuilder.create();
        }
    }

    /* renamed from: com.rokhgroup.adapters.StreamAdapter.1 */
    class C06411 implements OnClickListener {
        final /* synthetic */ StreamItem val$Item;

        C06411(StreamItem streamItem) {
            this.val$Item = streamItem;
        }

        public final void onClick(View v) {
            Intent intent = new Intent(StreamAdapter.this.mContext, JahanbinDetailsActivity.class);
            intent.putExtra("POST_ID", this.val$Item.POST_ID);
            intent.putExtra("POST_TYPE", this.val$Item.POST_TYPE);
            intent.putExtra("USER_ID", this.val$Item.USER_ID);
            StreamAdapter.this.mActivity.startActivity(intent);
        }
    }

    /* renamed from: com.rokhgroup.adapters.StreamAdapter.2 */
    class C06422 implements OnClickListener {
        final /* synthetic */ StreamItem val$Item;

        C06422(StreamItem streamItem) {
            this.val$Item = streamItem;
        }

        public final void onClick(View v) {
            Intent i = new Intent(StreamAdapter.this.mContext, UserProfile.class);
            i.putExtra("USER_ID", this.val$Item.USER_ID);
            StreamAdapter.this.mActivity.startActivity(i);
        }
    }

    /* renamed from: com.rokhgroup.adapters.StreamAdapter.3 */
    class C06433 implements OnClickListener {
        final /* synthetic */ StreamItem val$Item;

        C06433(StreamItem streamItem) {
            this.val$Item = streamItem;
        }

        public final void onClick(View v) {
            int position = ((Integer) v.getTag()).intValue();
            StreamAdapter streamAdapter = StreamAdapter.this;
            streamAdapter.dialog = new C06539(streamAdapter.mActivity, this.val$Item.POST_ID, position).build();
            streamAdapter.dialog.show();
        }
    }

    /* renamed from: com.rokhgroup.adapters.StreamAdapter.4 */
    class C06444 implements OnClickListener {
        final /* synthetic */ StreamItem val$Item;

        C06444(StreamItem streamItem) {
            this.val$Item = streamItem;
        }

        public final void onClick(View v) {
            StreamAdapter streamAdapter = StreamAdapter.this;
            streamAdapter.dialog = new AnonymousClass10(streamAdapter.mActivity, Integer.valueOf(this.val$Item.POST_ID).intValue()).build();
            streamAdapter.dialog.show();
        }
    }

    /* renamed from: com.rokhgroup.adapters.StreamAdapter.5 */
    class C06455 implements OnClickListener {
        final /* synthetic */ StreamItem val$Item;

        C06455(StreamItem streamItem) {
            this.val$Item = streamItem;
        }

        public final void onClick(View arg0) {
            Intent i = new Intent(StreamAdapter.this.mContext, Likers.class);
            i.putExtra("POST_ID", this.val$Item.POST_ID);
            StreamAdapter.this.mActivity.startActivity(i);
        }
    }

    /* renamed from: com.rokhgroup.adapters.StreamAdapter.6 */
    class C06466 implements OnClickListener {
        final /* synthetic */ StreamItem val$Item;

        C06466(StreamItem streamItem) {
            this.val$Item = streamItem;
        }

        public final void onClick(View arg0) {
            Intent i = new Intent(StreamAdapter.this.mContext, Likers.class);
            i.putExtra("POST_ID", this.val$Item.POST_ID);
            StreamAdapter.this.mActivity.startActivity(i);
        }
    }

    /* renamed from: com.rokhgroup.adapters.StreamAdapter.7 */
    class C06487 implements OnClickListener {
        final /* synthetic */ StreamItem val$Item;

        /* renamed from: com.rokhgroup.adapters.StreamAdapter.7.1 */
        class C06471 extends JsonHttpResponseHandler {
            final /* synthetic */ int val$position;

            C06471(int i) {
                this.val$position = i;
            }

            public final void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Integer likeStatus = Integer.valueOf(response.getInt(NotificationCompatApi21.CATEGORY_STATUS));
                    Integer likeCounts = Integer.valueOf(response.getInt("cnt_like"));
                    if (likeStatus.intValue() == -1) {
                        C06487.this.val$Item.POST_LIKE_W_USER = "false";
                        ((StreamItem) StreamAdapter.this.mData.get(this.val$position)).POST_LIKE_W_USER = "false";
                        ((StreamItem) StreamAdapter.this.mData.get(this.val$position)).POST_LIKE_CNT = String.valueOf(likeCounts);
                        StreamAdapter.this.notifyDataSetChanged();
                    } else if (likeStatus.intValue() == 1) {
                        C06487.this.val$Item.POST_LIKE_W_USER = "true";
                        ((StreamItem) StreamAdapter.this.mData.get(this.val$position)).POST_LIKE_W_USER = "true";
                        ((StreamItem) StreamAdapter.this.mData.get(this.val$position)).POST_LIKE_CNT = String.valueOf(likeCounts);
                        StreamAdapter.this.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                }
            }

            public final void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("ERROR", throwable.toString());
            }
        }

        C06487(StreamItem streamItem) {
            this.val$Item = streamItem;
        }

        public final void onClick(View v) {
            int position = ((Integer) v.getTag()).intValue();
            RequestParams params = new RequestParams("post_id", this.val$Item.POST_ID);
            Log.e("POST ID", this.val$Item.POST_ID);
            Log.e("LIKE URL", StreamAdapter.this.URL + StreamAdapter.this.CURRENT_USER_TOKEN);
            RokhgroupRestClient.post(StreamAdapter.this.URL + StreamAdapter.this.CURRENT_USER_TOKEN, params, new C06471(position));
        }
    }

    /* renamed from: com.rokhgroup.adapters.StreamAdapter.8 */
    class C06498 implements Transformation {
        final boolean oval;
        final float radius;

        C06498() {
            this.radius = TypedValue.applyDimension(1, 200.0f, SHAMChatApplication.getMyApplicationContext().getResources().getDisplayMetrics());
            this.oval = false;
        }

        public final Bitmap transform(Bitmap bitmap) {
            Drawable fromBitmap = RoundedDrawable.fromBitmap(bitmap);
            fromBitmap.mCornerRadius = this.radius;
            fromBitmap.mOval = false;
            Bitmap transformed = RoundedDrawable.drawableToBitmap(fromBitmap);
            if (!bitmap.equals(transformed)) {
                bitmap.recycle();
            }
            return transformed;
        }

        public final String key() {
            return "rounded_radius_" + this.radius + "_oval_false";
        }
    }

    /* renamed from: com.rokhgroup.adapters.StreamAdapter.9 */
    class C06539 extends DialogBuilder {
        final /* synthetic */ int val$position;
        final /* synthetic */ String val$postId;

        /* renamed from: com.rokhgroup.adapters.StreamAdapter.9.1 */
        class C06501 implements OnClickListener {
            C06501() {
            }

            public final void onClick(View v) {
                StreamAdapter.this.dialog.dismiss();
            }
        }

        /* renamed from: com.rokhgroup.adapters.StreamAdapter.9.2 */
        class C06522 implements OnClickListener {

            /* renamed from: com.rokhgroup.adapters.StreamAdapter.9.2.1 */
            class C06511 extends TextHttpResponseHandler {
                C06511() {
                }

                public final void onSuccess$79de7b53(String response) {
                    if (response.equals("1")) {
                        StreamAdapter.this.mData.remove(C06539.this.val$position);
                        StreamAdapter.this.notifyDataSetChanged();
                        StreamAdapter.this.dialog.dismiss();
                    }
                }

                public final void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                    Toast.makeText(StreamAdapter.this.mContext, "An error occurred, try again later", 0).show();
                }
            }

            C06522() {
            }

            public final void onClick(View v) {
                String URL = "http://social.rabtcdn.com/pin/d/post/delete/" + C06539.this.val$postId + "?token=" + StreamAdapter.this.CURRENT_USER_TOKEN;
                Log.e("DELETE LINK", URL);
                RokhgroupRestClient.get$6a529083(URL, new C06511());
            }
        }

        C06539(Context context, String str, int i) {
            this.val$postId = str;
            this.val$position = i;
            super(context);
        }

        public final Dialog build() {
            this.ParsvidBuilder.ParsvidTitle = BuildConfig.VERSION_NAME;
            this.ParsvidBuilder.ParsvidMsg = "Do you really want to delete this post?";
            this.ParsvidBuilder.addButton("NO", new C06501());
            this.ParsvidBuilder.addButton("YES", new C06522());
            return this.ParsvidBuilder.create();
        }
    }

    static class ViewHolder {
        TextView postDate;
        RokhImageView postImage;
        RoundedImageView postUserAvatar;
        TextView postUserName;
        ImageView postcomment;
        TextView postcommentcnt;
        ImageView postdelete;
        ImageView postedit;
        ImageView postlike;
        TextView postlikecnt;
        ImageView postlikecnticon;
        ImageView videoPostSign;

        public ViewHolder(View row) {
            this.postUserAvatar = (RoundedImageView) row.findViewById(2131362433);
            this.postUserName = (TextView) row.findViewById(2131362397);
            this.postDate = (TextView) row.findViewById(2131362450);
            this.postImage = (RokhImageView) row.findViewById(2131362460);
            this.postlikecnt = (TextView) row.findViewById(2131362467);
            this.postlikecnticon = (ImageView) row.findViewById(2131362466);
            this.postcommentcnt = (TextView) row.findViewById(2131362465);
            this.postlike = (ImageView) row.findViewById(2131362496);
            this.postcomment = (ImageView) row.findViewById(2131362497);
            this.postdelete = (ImageView) row.findViewById(2131362498);
            this.postedit = (ImageView) row.findViewById(2131362499);
            this.videoPostSign = (ImageView) row.findViewById(2131362495);
        }
    }

    public StreamAdapter(Context context, Activity a, ArrayList<StreamItem> data) {
        this.mInflater = null;
        this.URL = "http://social.rabtcdn.com/pin/d_like2/?token=";
        this.mTransformation = new C06498();
        this.mContext = context;
        this.mActivity = a;
        this.mData = data;
        this.mInflater = (LayoutInflater) this.mActivity.getSystemService("layout_inflater");
        this.Session = new RokhPref(this.mContext);
        this.CURRENT_USER_TOKEN = this.Session.getUSERTOKEN();
        this.CURRENT_USER_ID = this.Session.getUSERID();
    }

    public final int getCount() {
        return this.mData.size();
    }

    public final Object getItem(int position) {
        return this.mData.get(position);
    }

    public final long getItemId(int position) {
        return (long) position;
    }

    public final View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;
        if (convertView == null) {
            row = this.mInflater.inflate(2130903220, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        StreamItem Item = (StreamItem) this.mData.get(position);
        holder.postlike.setTag(Integer.valueOf(position));
        holder.postdelete.setTag(Integer.valueOf(position));
        holder.postedit.setTag(Integer.valueOf(position));
        if (Item.POST_TYPE.equals("3")) {
            holder.videoPostSign.setVisibility(0);
        } else {
            holder.videoPostSign.setVisibility(8);
        }
        holder.postcomment.setOnClickListener(new C06411(Item));
        holder.postUserAvatar.setOnClickListener(new C06422(Item));
        if (Item.USER_ID.equals(this.CURRENT_USER_ID)) {
            holder.postdelete.setVisibility(0);
            holder.postdelete.setOnClickListener(new C06433(Item));
            holder.postedit.setVisibility(0);
            holder.postedit.setOnClickListener(new C06444(Item));
        } else {
            holder.postdelete.setVisibility(8);
            holder.postedit.setVisibility(8);
        }
        holder.postUserName.setTypeface(Utils.GetNaskhBold(this.mContext));
        holder.postDate.setTextSize(13.0f);
        holder.postUserName.setText(Item.USER_NAME);
        holder.postDate.setTypeface(Utils.GetNaskhRegular(this.mContext));
        holder.postDate.setTextSize(11.0f);
        holder.postDate.setText(Utils.persianNum(Utils.getTimeAgo$6909e107(Long.valueOf(Item.POST_DATE).longValue())));
        RequestCreator load = Picasso.with(this.mContext).load(Item.USER_AVATAR);
        load.deferred = true;
        load.transform(this.mTransformation).into(holder.postUserAvatar, null);
        Picasso.with(this.mContext).load(Item.IMAGE_THUMB_URL).into(holder.postImage, null);
        holder.postlikecnt.setTypeface(Utils.GetNaskhBold(this.mContext));
        holder.postDate.setTextSize(14.0f);
        holder.postlikecnt.setText(Utils.persianNum(Item.POST_LIKE_CNT));
        holder.postlikecnt.setOnClickListener(new C06455(Item));
        holder.postlikecnticon.setOnClickListener(new C06466(Item));
        holder.postcommentcnt.setTypeface(Utils.GetNaskhBold(this.mContext));
        holder.postDate.setTextSize(14.0f);
        holder.postcommentcnt.setText(Utils.persianNum(Item.POST_CMNT_CNT));
        if (Boolean.valueOf(Item.POST_LIKE_W_USER).booleanValue()) {
            holder.postlike.setBackgroundResource(2130837940);
        } else {
            holder.postlike.setBackgroundResource(2130837939);
        }
        holder.postlike.setOnClickListener(new C06487(Item));
        return row;
    }
}

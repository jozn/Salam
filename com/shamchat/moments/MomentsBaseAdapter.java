package com.shamchat.moments;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.v4.media.TransportMediator;
import android.support.v7.appcompat.BuildConfig;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import com.shamchat.activity.ChatActivity;
import com.shamchat.activity.LocalVideoFilePreview;
import com.shamchat.activity.MyProfileActivity;
import com.shamchat.activity.ProfileActivity;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.MomentProvider;
import com.shamchat.models.Moment;
import com.shamchat.utils.Emoticons;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.Builder;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;
import com.squareup.picasso.RequestHandler.Result;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.internal.ClientDefaults;

public final class MomentsBaseAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    ArrayList<Moment> momentList;
    private MomentsFragment momentsFragment;
    private String myUserId;
    private ContentResolver resolver;
    private Map<String, Bitmap> videoThumbnails;

    /* renamed from: com.shamchat.moments.MomentsBaseAdapter.1 */
    class C11481 implements OnClickListener {
        final /* synthetic */ Moment val$moment;

        C11481(Moment moment) {
            this.val$moment = moment;
        }

        public final void onClick(View v) {
            String userId = this.val$moment.userId;
            if (userId.equalsIgnoreCase(MomentsBaseAdapter.this.myUserId)) {
                Intent i = new Intent(MomentsBaseAdapter.this.momentsFragment.getActivity(), MyProfileActivity.class);
                i.setFlags(ClientDefaults.MAX_MSG_SIZE);
                MomentsBaseAdapter.this.momentsFragment.startActivity(i);
                return;
            }
            Intent intent = new Intent(v.getContext(), ProfileActivity.class);
            intent.putExtra(ProfileActivity.EXTRA_USER_ID, userId);
            intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
            v.getContext().startActivity(intent);
        }
    }

    /* renamed from: com.shamchat.moments.MomentsBaseAdapter.2 */
    class C11492 implements OnClickListener {
        final /* synthetic */ Moment val$moment;

        C11492(Moment moment) {
            this.val$moment = moment;
        }

        public final void onClick(View v) {
            Intent intent = new Intent(MomentsBaseAdapter.this.momentsFragment.getActivity(), LocalVideoFilePreview.class);
            intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
            intent.putExtra("local_file_url", this.val$moment.postedVideoUrl);
            MomentsBaseAdapter.this.momentsFragment.startActivity(intent);
        }
    }

    /* renamed from: com.shamchat.moments.MomentsBaseAdapter.3 */
    class C11503 implements OnClickListener {
        final /* synthetic */ List val$imageList;
        final /* synthetic */ Moment val$moment;

        C11503(Moment moment, List list) {
            this.val$moment = moment;
            this.val$imageList = list;
        }

        public final void onClick(View v) {
            System.out.println("image clicked ");
            Intent intent = new Intent(MomentsBaseAdapter.this.momentsFragment.getActivity(), MomentSummaryActivity.class);
            intent.putExtra("momentId", this.val$moment.momentId);
            intent.putExtra("imgList", this.val$imageList.toString().replace("[", BuildConfig.VERSION_NAME).replace("]", BuildConfig.VERSION_NAME));
            intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
            MomentsBaseAdapter.this.momentsFragment.startActivity(intent);
        }
    }

    /* renamed from: com.shamchat.moments.MomentsBaseAdapter.4 */
    class C11514 implements OnClickListener {
        final /* synthetic */ List val$imngUrls;
        final /* synthetic */ Moment val$moment;

        C11514(Moment moment, List list) {
            this.val$moment = moment;
            this.val$imngUrls = list;
        }

        public final void onClick(View v) {
            Intent intent = new Intent(MomentsBaseAdapter.this.momentsFragment.getActivity(), MomentSummaryActivity.class);
            intent.putExtra("momentId", this.val$moment.momentId);
            intent.putExtra("imgList", this.val$imngUrls.toString().replace("[", BuildConfig.VERSION_NAME).replace("]", BuildConfig.VERSION_NAME));
            intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
            MomentsBaseAdapter.this.momentsFragment.startActivity(intent);
        }
    }

    /* renamed from: com.shamchat.moments.MomentsBaseAdapter.5 */
    class C11525 implements OnClickListener {
        final /* synthetic */ Moment val$moment;

        C11525(Moment moment) {
            this.val$moment = moment;
        }

        public final void onClick(View v) {
            MomentsBaseAdapter.access$1800(MomentsBaseAdapter.this, this.val$moment.momentId);
        }
    }

    /* renamed from: com.shamchat.moments.MomentsBaseAdapter.6 */
    class C11536 implements OnClickListener {
        final /* synthetic */ ViewHolder val$holder;
        final /* synthetic */ Moment val$moment;

        C11536(ViewHolder viewHolder, Moment moment) {
            this.val$holder = viewHolder;
            this.val$moment = moment;
        }

        public final void onClick(View v) {
            if (this.val$holder.btLike.getText().toString().equalsIgnoreCase("Like")) {
                this.val$holder.btLike.setText("Unlike");
                int likeCnt = this.val$moment.likeCount + 1;
                this.val$holder.likeCount.setText(String.valueOf(likeCnt));
                this.val$holder.btLike.setText("Unlike");
                this.val$holder.likeCount.setVisibility(0);
                this.val$moment.likeCount = likeCnt;
                this.val$moment.likedByCurrentUser = true;
                new Thread(new C11569(this.val$moment, false)).start();
                return;
            }
            likeCnt = this.val$moment.likeCount - 1;
            this.val$holder.likeCount.setText(String.valueOf(likeCnt));
            if (likeCnt == 0) {
                this.val$holder.likeCount.setVisibility(4);
            } else {
                this.val$holder.likeCount.setVisibility(0);
            }
            this.val$holder.btLike.setText("Like");
            this.val$moment.likeCount--;
            this.val$moment.likedByCurrentUser = false;
            new Thread(new C11569(this.val$moment, true)).start();
        }
    }

    /* renamed from: com.shamchat.moments.MomentsBaseAdapter.7 */
    class C11547 implements OnClickListener {
        final /* synthetic */ Moment val$moment;

        C11547(Moment moment) {
            this.val$moment = moment;
        }

        public final void onClick(View v) {
            MomentsBaseAdapter.access$1800(MomentsBaseAdapter.this, this.val$moment.momentId);
        }
    }

    /* renamed from: com.shamchat.moments.MomentsBaseAdapter.8 */
    class C11558 implements OnClickListener {
        final /* synthetic */ Moment val$moment;

        C11558(Moment moment) {
            this.val$moment = moment;
        }

        public final void onClick(View v) {
            MomentsBaseAdapter.access$1800(MomentsBaseAdapter.this, this.val$moment.momentId);
        }
    }

    /* renamed from: com.shamchat.moments.MomentsBaseAdapter.9 */
    class C11569 implements Runnable {
        final /* synthetic */ boolean val$isUnlike;
        final /* synthetic */ Moment val$moment;

        C11569(Moment moment, boolean z) {
            this.val$moment = moment;
            this.val$isUnlike = z;
        }

        public final void run() {
            ContentValues values = new ContentValues();
            String momentId = this.val$moment.momentId;
            if (this.val$isUnlike) {
                values.put("like_status", Integer.valueOf(0));
                values.put("post_action_requested", "DELETE");
                MomentsBaseAdapter.this.resolver.update(MomentProvider.CONTENT_URI_LIKE, values, "post_id=? AND user_id=?", new String[]{momentId, MomentsBaseAdapter.this.myUserId});
                return;
            }
            values.put("post_id", momentId);
            values.put(ChatActivity.INTENT_EXTRA_USER_ID, MomentsBaseAdapter.this.myUserId);
            values.put("like_status", Integer.valueOf(1));
            values.put("post_action_requested", "UPDATE");
            if (MomentsBaseAdapter.this.resolver.update(MomentProvider.CONTENT_URI_LIKE, values, "post_id=? AND user_id=?", new String[]{momentId, MomentsBaseAdapter.this.myUserId}) == 0) {
                values.remove("post_action_requested");
                values.put("like_id", MomentsBaseAdapter.this.myUserId + "_" + System.currentTimeMillis());
                MomentsBaseAdapter.this.resolver.insert(MomentProvider.CONTENT_URI_LIKE, values);
            }
        }
    }

    class PicassoVideoFrameRequestHandler extends RequestHandler {
        private String momentId;

        public PicassoVideoFrameRequestHandler(String momentId) {
            this.momentId = momentId;
        }

        public final boolean canHandleRequest(Request data) {
            System.out.println("can handle request " + data.uri.getScheme());
            return true;
        }

        public final Result load$71fa0c91(Request data) throws IOException {
            System.out.println("Loading request handler");
            try {
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                System.out.println("Loading request handler url " + data.uri);
                mediaMetadataRetriever.setDataSource(data.uri.toString(), new HashMap());
                System.out.println("Loading request handler setDataSource ");
                Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime();
                MomentsBaseAdapter.this.videoThumbnails.put(this.momentId, bitmap);
                if (bitmap != null) {
                    System.out.println("Loading request handler bit map not null");
                    return new Result(bitmap, LoadedFrom.DISK);
                }
                System.out.println("Loading request handler bit map null");
                mediaMetadataRetriever.release();
                return null;
            } catch (Exception e) {
                System.out.println("Loading request handler exception " + e);
                return null;
            }
        }
    }

    class ViewHolder {
        TextView btComment;
        TextView btLike;
        public LinearLayout comment1;
        public LinearLayout comment2;
        public LinearLayout comment3;
        TextView commentCount;
        TextView likeCount;
        LinearLayout likesCommentsLayout;
        public LinearLayout locationLayout;
        LinearLayout mainLayout;
        LinearLayout multiImageContainer;
        LinearLayout multiStickerContainer;
        ImageView playButton;
        int position;
        TextView postedLocation;
        TextView postedText;
        TextView postedTime;
        ImageView profileImage;
        ImageView thumbnailImage;
        TextView userName;

        ViewHolder() {
        }
    }

    static /* synthetic */ void access$1800(MomentsBaseAdapter x0, String x1) {
        System.out.println("open detail view");
        Intent intent = new Intent(x0.momentsFragment.getActivity(), MomentDetailActivity.class);
        intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
        intent.putExtra("momentId", x1);
        x0.momentsFragment.getActivity().startActivityForResult(intent, 100);
    }

    public MomentsBaseAdapter(MomentsFragment momentsFragment, ArrayList<Moment> moment) {
        this.momentsFragment = momentsFragment;
        this.momentList = moment;
        this.myUserId = SHAMChatApplication.getConfig().userId;
        this.resolver = momentsFragment.getActivity().getContentResolver();
        this.inflater = (LayoutInflater) momentsFragment.getActivity().getSystemService("layout_inflater");
        this.videoThumbnails = new HashMap();
    }

    public final int getCount() {
        return this.momentList.size();
    }

    public final Object getItem(int position) {
        return this.momentList.get(position);
    }

    public final long getItemId(int position) {
        return 0;
    }

    public final View getView(int position, View view, ViewGroup parent) {
        boolean isVideo;
        Picasso picasso;
        Uri parse;
        Uri uri;
        List<String> imageList;
        int i;
        String imageUrl;
        View imageView;
        LayoutParams layoutParams;
        File f;
        Bitmap bm;
        Picasso with;
        List<String> imngUrls;
        String currentfile;
        int likeCnt;
        int commentCnt;
        view = this.inflater.inflate(2130903168, null);
        ViewHolder holder = new ViewHolder();
        holder.multiStickerContainer = (LinearLayout) view.findViewById(2131362300);
        holder.profileImage = (ImageView) view.findViewById(2131361927);
        holder.thumbnailImage = (ImageView) view.findViewById(2131362304);
        holder.playButton = (ImageView) view.findViewById(2131362305);
        holder.userName = (TextView) view.findViewById(2131362291);
        holder.postedTime = (TextView) view.findViewById(2131361923);
        holder.postedText = (TextView) view.findViewById(2131362292);
        holder.postedLocation = (TextView) view.findViewById(2131362307);
        holder.btLike = (TextView) view.findViewById(2131362310);
        holder.btComment = (TextView) view.findViewById(2131362311);
        holder.likeCount = (TextView) view.findViewById(2131362314);
        holder.commentCount = (TextView) view.findViewById(2131362315);
        holder.multiImageContainer = (LinearLayout) view.findViewById(2131362302);
        holder.mainLayout = (LinearLayout) view.findViewById(2131362297);
        holder.likesCommentsLayout = (LinearLayout) view.findViewById(2131362312);
        holder.locationLayout = (LinearLayout) view.findViewById(2131362306);
        holder.comment1 = (LinearLayout) view.findViewById(2131362316);
        holder.comment2 = (LinearLayout) view.findViewById(2131362317);
        holder.comment3 = (LinearLayout) view.findViewById(2131362318);
        view.setTag(holder);
        Moment moment = (Moment) this.momentList.get(position);
        holder.position = position;
        holder.playButton.setVisibility(8);
        holder.multiImageContainer.setVisibility(8);
        holder.locationLayout.setVisibility(8);
        holder.comment1.setVisibility(8);
        holder.comment2.setVisibility(8);
        holder.comment3.setVisibility(8);
        holder.profileImage.setOnClickListener(new C11481(moment));
        Bitmap userBitmap = (Bitmap) SHAMChatApplication.USER_IMAGES.get(moment.userId);
        if (userBitmap != null) {
            System.out.println("moments User Image in the map");
            holder.profileImage.setImageBitmap(userBitmap);
        }
        String str = moment.userName;
        str = moment.momentId;
        System.out.println("Moment id in the adapter " + r0 + " " + r0 + " " + moment.postedDateTime);
        holder.userName.setText(moment.userName);
        holder.postedTime.setText(moment.postedDateTime);
        if (moment.postedText != null) {
            if (moment.postedText.length() > 0) {
                holder.postedText.setVisibility(0);
                Spannable spannable = Emoticons.getSmiledText(SHAMChatApplication.getMyApplicationContext(), moment.postedText);
                holder.postedText.setText(spannable, BufferType.SPANNABLE);
                if (moment.postedLocation != null) {
                    holder.locationLayout.setVisibility(0);
                    holder.postedLocation.setText(moment.postedLocation);
                }
                isVideo = false;
                if (moment.postedVideoUrl != null) {
                    if (moment.postedVideoUrl.length() > 1) {
                        holder.thumbnailImage.setVisibility(0);
                        holder.playButton.setVisibility(0);
                        if (this.videoThumbnails.containsKey(moment.momentId)) {
                            if (moment.postedVideoUrl.contains("http://")) {
                                picasso = new Builder(SHAMChatApplication.getMyApplicationContext()).addRequestHandler(new PicassoVideoFrameRequestHandler(moment.momentId)).build();
                                parse = Uri.parse(moment.postedVideoUrl);
                                picasso.load(uri).into(holder.thumbnailImage, null);
                                System.out.println("Loading with picasso thumb local " + moment.postedVideoUrl);
                            } else {
                                picasso = new Builder(SHAMChatApplication.getMyApplicationContext()).addRequestHandler(new PicassoVideoFrameRequestHandler(moment.momentId)).build();
                                parse = Uri.parse(moment.postedVideoUrl);
                                picasso.load(uri).into(holder.thumbnailImage, null);
                                System.out.println("Loading with picasso thumb remote " + moment.postedVideoUrl);
                            }
                        } else {
                            Bitmap bitmap = (Bitmap) this.videoThumbnails.get(moment.momentId);
                            holder.thumbnailImage.setImageBitmap(bitmap);
                        }
                        isVideo = true;
                        holder.playButton.setOnClickListener(new C11492(moment));
                        if (moment.imgUrls == null) {
                            holder.thumbnailImage.setVisibility(0);
                            imageList = moment.imgUrls;
                            if (imageList.size() != 1) {
                                if (!isVideo) {
                                    holder.thumbnailImage.setVisibility(8);
                                }
                                holder.multiImageContainer.setVisibility(0);
                                holder.multiImageContainer.removeAllViews();
                                for (i = 0; i < imageList.size(); i++) {
                                    imageUrl = (String) imageList.get(i);
                                    imageView = new ImageView(SHAMChatApplication.getMyApplicationContext());
                                    imageView.setScaleType(ScaleType.FIT_XY);
                                    imageView.setId(i);
                                    if (imageUrl.contains("http://")) {
                                        Picasso.with(SHAMChatApplication.getMyApplicationContext()).load("file://" + imageUrl).resize(250, 250).into(imageView, null);
                                        System.out.println("Loading with picasso local multiple" + imageList.size());
                                    } else {
                                        uri = Uri.parse(imageUrl);
                                        Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(uri).resize(250, 250).into(imageView, null);
                                        System.out.println("Loading with picasso remote multiple" + imageList.size());
                                    }
                                    layoutParams = new LinearLayout.LayoutParams(250, 250);
                                    layoutParams.setMargins(5, 0, 5, 0);
                                    imageView.setLayoutParams(layoutParams);
                                    imageView.setOnClickListener(new C11503(moment, imageList));
                                    holder.multiImageContainer.addView(imageView);
                                    System.out.println("Loading with picasso multiple end");
                                }
                            } else if (((String) imageList.get(0)).contains("http://")) {
                                System.out.println("Loading with picasso == 1bc");
                                f = new File((String) imageList.get(0));
                                if (f.exists()) {
                                    System.out.println("file exists" + f.getAbsolutePath());
                                    bm = BitmapFactory.decodeFile(f.getAbsolutePath());
                                    holder.thumbnailImage.setImageBitmap(bm);
                                }
                            } else {
                                System.out.println("moment image " + ((String) imageList.get(0)));
                                uri = Uri.parse((String) imageList.get(0));
                                with = Picasso.with(SHAMChatApplication.getMyApplicationContext());
                                r24.load(uri).into(holder.thumbnailImage, null);
                            }
                        } else if (!isVideo) {
                            holder.thumbnailImage.setVisibility(8);
                        }
                        if (moment.stickerUrls == null) {
                            holder.multiStickerContainer.setVisibility(0);
                            holder.multiStickerContainer.removeAllViews();
                            imngUrls = moment.stickerUrls;
                            for (i = 0; i < imngUrls.size(); i++) {
                                currentfile = (String) imngUrls.get(i);
                                imageView = new ImageView(SHAMChatApplication.getMyApplicationContext());
                                imageView.setId(i);
                                if (currentfile.contains("http://")) {
                                    Picasso.with(SHAMChatApplication.getMyApplicationContext()).load("file://" + currentfile).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).into(imageView, null);
                                    System.out.println("Loading with picasso sticker local multiple" + imngUrls.size());
                                } else {
                                    uri = Uri.parse(currentfile);
                                    Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(uri).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).into(imageView, null);
                                    System.out.println("Loading with picasso sticker remote multiple" + imngUrls.size());
                                }
                                layoutParams = new LinearLayout.LayoutParams(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD);
                                layoutParams.setMargins(5, 5, 5, 5);
                                layoutParams.gravity = 16;
                                imageView.setLayoutParams(layoutParams);
                                imageView.setOnClickListener(new C11514(moment, imngUrls));
                                holder.multiStickerContainer.addView(imageView);
                            }
                        } else {
                            holder.multiStickerContainer.setVisibility(8);
                        }
                        likeCnt = moment.likeCount;
                        holder.likeCount.setText(String.valueOf(likeCnt));
                        if (likeCnt != 0) {
                            holder.likeCount.setVisibility(4);
                        } else {
                            holder.likeCount.setVisibility(0);
                        }
                        if (moment.likedByCurrentUser) {
                            holder.btLike.setText("Like");
                        } else {
                            holder.btLike.setText("Unlike");
                        }
                        commentCnt = moment.commentCount;
                        holder.commentCount.setText(String.valueOf(commentCnt));
                        if (commentCnt != 0) {
                            holder.commentCount.setVisibility(4);
                        } else {
                            holder.commentCount.setVisibility(0);
                        }
                        holder.btComment.setOnClickListener(new C11525(moment));
                        holder.btLike.setOnClickListener(new C11536(holder, moment));
                        holder.mainLayout.setOnClickListener(new C11547(moment));
                        holder.likesCommentsLayout.setOnClickListener(new C11558(moment));
                        return view;
                    }
                }
                holder.thumbnailImage.setVisibility(8);
                if (moment.imgUrls == null) {
                    holder.thumbnailImage.setVisibility(0);
                    imageList = moment.imgUrls;
                    if (imageList.size() != 1) {
                        if (isVideo) {
                            holder.thumbnailImage.setVisibility(8);
                        }
                        holder.multiImageContainer.setVisibility(0);
                        holder.multiImageContainer.removeAllViews();
                        for (i = 0; i < imageList.size(); i++) {
                            imageUrl = (String) imageList.get(i);
                            imageView = new ImageView(SHAMChatApplication.getMyApplicationContext());
                            imageView.setScaleType(ScaleType.FIT_XY);
                            imageView.setId(i);
                            if (imageUrl.contains("http://")) {
                                Picasso.with(SHAMChatApplication.getMyApplicationContext()).load("file://" + imageUrl).resize(250, 250).into(imageView, null);
                                System.out.println("Loading with picasso local multiple" + imageList.size());
                            } else {
                                uri = Uri.parse(imageUrl);
                                Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(uri).resize(250, 250).into(imageView, null);
                                System.out.println("Loading with picasso remote multiple" + imageList.size());
                            }
                            layoutParams = new LinearLayout.LayoutParams(250, 250);
                            layoutParams.setMargins(5, 0, 5, 0);
                            imageView.setLayoutParams(layoutParams);
                            imageView.setOnClickListener(new C11503(moment, imageList));
                            holder.multiImageContainer.addView(imageView);
                            System.out.println("Loading with picasso multiple end");
                        }
                    } else if (((String) imageList.get(0)).contains("http://")) {
                        System.out.println("Loading with picasso == 1bc");
                        f = new File((String) imageList.get(0));
                        if (f.exists()) {
                            System.out.println("file exists" + f.getAbsolutePath());
                            bm = BitmapFactory.decodeFile(f.getAbsolutePath());
                            holder.thumbnailImage.setImageBitmap(bm);
                        }
                    } else {
                        System.out.println("moment image " + ((String) imageList.get(0)));
                        uri = Uri.parse((String) imageList.get(0));
                        with = Picasso.with(SHAMChatApplication.getMyApplicationContext());
                        r24.load(uri).into(holder.thumbnailImage, null);
                    }
                } else if (isVideo) {
                    holder.thumbnailImage.setVisibility(8);
                }
                if (moment.stickerUrls == null) {
                    holder.multiStickerContainer.setVisibility(8);
                } else {
                    holder.multiStickerContainer.setVisibility(0);
                    holder.multiStickerContainer.removeAllViews();
                    imngUrls = moment.stickerUrls;
                    for (i = 0; i < imngUrls.size(); i++) {
                        currentfile = (String) imngUrls.get(i);
                        imageView = new ImageView(SHAMChatApplication.getMyApplicationContext());
                        imageView.setId(i);
                        if (currentfile.contains("http://")) {
                            Picasso.with(SHAMChatApplication.getMyApplicationContext()).load("file://" + currentfile).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).into(imageView, null);
                            System.out.println("Loading with picasso sticker local multiple" + imngUrls.size());
                        } else {
                            uri = Uri.parse(currentfile);
                            Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(uri).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).into(imageView, null);
                            System.out.println("Loading with picasso sticker remote multiple" + imngUrls.size());
                        }
                        layoutParams = new LinearLayout.LayoutParams(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD);
                        layoutParams.setMargins(5, 5, 5, 5);
                        layoutParams.gravity = 16;
                        imageView.setLayoutParams(layoutParams);
                        imageView.setOnClickListener(new C11514(moment, imngUrls));
                        holder.multiStickerContainer.addView(imageView);
                    }
                }
                likeCnt = moment.likeCount;
                holder.likeCount.setText(String.valueOf(likeCnt));
                if (likeCnt != 0) {
                    holder.likeCount.setVisibility(0);
                } else {
                    holder.likeCount.setVisibility(4);
                }
                if (moment.likedByCurrentUser) {
                    holder.btLike.setText("Like");
                } else {
                    holder.btLike.setText("Unlike");
                }
                commentCnt = moment.commentCount;
                holder.commentCount.setText(String.valueOf(commentCnt));
                if (commentCnt != 0) {
                    holder.commentCount.setVisibility(0);
                } else {
                    holder.commentCount.setVisibility(4);
                }
                holder.btComment.setOnClickListener(new C11525(moment));
                holder.btLike.setOnClickListener(new C11536(holder, moment));
                holder.mainLayout.setOnClickListener(new C11547(moment));
                holder.likesCommentsLayout.setOnClickListener(new C11558(moment));
                return view;
            }
        }
        holder.postedText.setVisibility(8);
        if (moment.postedLocation != null) {
            holder.locationLayout.setVisibility(0);
            holder.postedLocation.setText(moment.postedLocation);
        }
        isVideo = false;
        if (moment.postedVideoUrl != null) {
            if (moment.postedVideoUrl.length() > 1) {
                holder.thumbnailImage.setVisibility(0);
                holder.playButton.setVisibility(0);
                if (this.videoThumbnails.containsKey(moment.momentId)) {
                    if (moment.postedVideoUrl.contains("http://")) {
                        picasso = new Builder(SHAMChatApplication.getMyApplicationContext()).addRequestHandler(new PicassoVideoFrameRequestHandler(moment.momentId)).build();
                        parse = Uri.parse(moment.postedVideoUrl);
                        picasso.load(uri).into(holder.thumbnailImage, null);
                        System.out.println("Loading with picasso thumb local " + moment.postedVideoUrl);
                    } else {
                        picasso = new Builder(SHAMChatApplication.getMyApplicationContext()).addRequestHandler(new PicassoVideoFrameRequestHandler(moment.momentId)).build();
                        parse = Uri.parse(moment.postedVideoUrl);
                        picasso.load(uri).into(holder.thumbnailImage, null);
                        System.out.println("Loading with picasso thumb remote " + moment.postedVideoUrl);
                    }
                } else {
                    Bitmap bitmap2 = (Bitmap) this.videoThumbnails.get(moment.momentId);
                    holder.thumbnailImage.setImageBitmap(bitmap2);
                }
                isVideo = true;
                holder.playButton.setOnClickListener(new C11492(moment));
                if (moment.imgUrls == null) {
                    holder.thumbnailImage.setVisibility(0);
                    imageList = moment.imgUrls;
                    if (imageList.size() != 1) {
                        if (isVideo) {
                            holder.thumbnailImage.setVisibility(8);
                        }
                        holder.multiImageContainer.setVisibility(0);
                        holder.multiImageContainer.removeAllViews();
                        for (i = 0; i < imageList.size(); i++) {
                            imageUrl = (String) imageList.get(i);
                            imageView = new ImageView(SHAMChatApplication.getMyApplicationContext());
                            imageView.setScaleType(ScaleType.FIT_XY);
                            imageView.setId(i);
                            if (imageUrl.contains("http://")) {
                                uri = Uri.parse(imageUrl);
                                Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(uri).resize(250, 250).into(imageView, null);
                                System.out.println("Loading with picasso remote multiple" + imageList.size());
                            } else {
                                Picasso.with(SHAMChatApplication.getMyApplicationContext()).load("file://" + imageUrl).resize(250, 250).into(imageView, null);
                                System.out.println("Loading with picasso local multiple" + imageList.size());
                            }
                            layoutParams = new LinearLayout.LayoutParams(250, 250);
                            layoutParams.setMargins(5, 0, 5, 0);
                            imageView.setLayoutParams(layoutParams);
                            imageView.setOnClickListener(new C11503(moment, imageList));
                            holder.multiImageContainer.addView(imageView);
                            System.out.println("Loading with picasso multiple end");
                        }
                    } else if (((String) imageList.get(0)).contains("http://")) {
                        System.out.println("moment image " + ((String) imageList.get(0)));
                        uri = Uri.parse((String) imageList.get(0));
                        with = Picasso.with(SHAMChatApplication.getMyApplicationContext());
                        r24.load(uri).into(holder.thumbnailImage, null);
                    } else {
                        System.out.println("Loading with picasso == 1bc");
                        f = new File((String) imageList.get(0));
                        if (f.exists()) {
                            System.out.println("file exists" + f.getAbsolutePath());
                            bm = BitmapFactory.decodeFile(f.getAbsolutePath());
                            holder.thumbnailImage.setImageBitmap(bm);
                        }
                    }
                } else if (isVideo) {
                    holder.thumbnailImage.setVisibility(8);
                }
                if (moment.stickerUrls == null) {
                    holder.multiStickerContainer.setVisibility(0);
                    holder.multiStickerContainer.removeAllViews();
                    imngUrls = moment.stickerUrls;
                    for (i = 0; i < imngUrls.size(); i++) {
                        currentfile = (String) imngUrls.get(i);
                        imageView = new ImageView(SHAMChatApplication.getMyApplicationContext());
                        imageView.setId(i);
                        if (currentfile.contains("http://")) {
                            uri = Uri.parse(currentfile);
                            Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(uri).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).into(imageView, null);
                            System.out.println("Loading with picasso sticker remote multiple" + imngUrls.size());
                        } else {
                            Picasso.with(SHAMChatApplication.getMyApplicationContext()).load("file://" + currentfile).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).into(imageView, null);
                            System.out.println("Loading with picasso sticker local multiple" + imngUrls.size());
                        }
                        layoutParams = new LinearLayout.LayoutParams(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD);
                        layoutParams.setMargins(5, 5, 5, 5);
                        layoutParams.gravity = 16;
                        imageView.setLayoutParams(layoutParams);
                        imageView.setOnClickListener(new C11514(moment, imngUrls));
                        holder.multiStickerContainer.addView(imageView);
                    }
                } else {
                    holder.multiStickerContainer.setVisibility(8);
                }
                likeCnt = moment.likeCount;
                holder.likeCount.setText(String.valueOf(likeCnt));
                if (likeCnt != 0) {
                    holder.likeCount.setVisibility(4);
                } else {
                    holder.likeCount.setVisibility(0);
                }
                if (moment.likedByCurrentUser) {
                    holder.btLike.setText("Unlike");
                } else {
                    holder.btLike.setText("Like");
                }
                commentCnt = moment.commentCount;
                holder.commentCount.setText(String.valueOf(commentCnt));
                if (commentCnt != 0) {
                    holder.commentCount.setVisibility(4);
                } else {
                    holder.commentCount.setVisibility(0);
                }
                holder.btComment.setOnClickListener(new C11525(moment));
                holder.btLike.setOnClickListener(new C11536(holder, moment));
                holder.mainLayout.setOnClickListener(new C11547(moment));
                holder.likesCommentsLayout.setOnClickListener(new C11558(moment));
                return view;
            }
        }
        holder.thumbnailImage.setVisibility(8);
        if (moment.imgUrls == null) {
            holder.thumbnailImage.setVisibility(0);
            imageList = moment.imgUrls;
            if (imageList.size() != 1) {
                if (isVideo) {
                    holder.thumbnailImage.setVisibility(8);
                }
                holder.multiImageContainer.setVisibility(0);
                holder.multiImageContainer.removeAllViews();
                for (i = 0; i < imageList.size(); i++) {
                    imageUrl = (String) imageList.get(i);
                    imageView = new ImageView(SHAMChatApplication.getMyApplicationContext());
                    imageView.setScaleType(ScaleType.FIT_XY);
                    imageView.setId(i);
                    if (imageUrl.contains("http://")) {
                        Picasso.with(SHAMChatApplication.getMyApplicationContext()).load("file://" + imageUrl).resize(250, 250).into(imageView, null);
                        System.out.println("Loading with picasso local multiple" + imageList.size());
                    } else {
                        uri = Uri.parse(imageUrl);
                        Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(uri).resize(250, 250).into(imageView, null);
                        System.out.println("Loading with picasso remote multiple" + imageList.size());
                    }
                    layoutParams = new LinearLayout.LayoutParams(250, 250);
                    layoutParams.setMargins(5, 0, 5, 0);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setOnClickListener(new C11503(moment, imageList));
                    holder.multiImageContainer.addView(imageView);
                    System.out.println("Loading with picasso multiple end");
                }
            } else if (((String) imageList.get(0)).contains("http://")) {
                System.out.println("Loading with picasso == 1bc");
                f = new File((String) imageList.get(0));
                if (f.exists()) {
                    System.out.println("file exists" + f.getAbsolutePath());
                    bm = BitmapFactory.decodeFile(f.getAbsolutePath());
                    holder.thumbnailImage.setImageBitmap(bm);
                }
            } else {
                System.out.println("moment image " + ((String) imageList.get(0)));
                uri = Uri.parse((String) imageList.get(0));
                with = Picasso.with(SHAMChatApplication.getMyApplicationContext());
                r24.load(uri).into(holder.thumbnailImage, null);
            }
        } else if (isVideo) {
            holder.thumbnailImage.setVisibility(8);
        }
        if (moment.stickerUrls == null) {
            holder.multiStickerContainer.setVisibility(8);
        } else {
            holder.multiStickerContainer.setVisibility(0);
            holder.multiStickerContainer.removeAllViews();
            imngUrls = moment.stickerUrls;
            for (i = 0; i < imngUrls.size(); i++) {
                currentfile = (String) imngUrls.get(i);
                imageView = new ImageView(SHAMChatApplication.getMyApplicationContext());
                imageView.setId(i);
                if (currentfile.contains("http://")) {
                    Picasso.with(SHAMChatApplication.getMyApplicationContext()).load("file://" + currentfile).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).into(imageView, null);
                    System.out.println("Loading with picasso sticker local multiple" + imngUrls.size());
                } else {
                    uri = Uri.parse(currentfile);
                    Picasso.with(SHAMChatApplication.getMyApplicationContext()).load(uri).resize(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD).into(imageView, null);
                    System.out.println("Loading with picasso sticker remote multiple" + imngUrls.size());
                }
                layoutParams = new LinearLayout.LayoutParams(TransportMediator.KEYCODE_MEDIA_RECORD, TransportMediator.KEYCODE_MEDIA_RECORD);
                layoutParams.setMargins(5, 5, 5, 5);
                layoutParams.gravity = 16;
                imageView.setLayoutParams(layoutParams);
                imageView.setOnClickListener(new C11514(moment, imngUrls));
                holder.multiStickerContainer.addView(imageView);
            }
        }
        likeCnt = moment.likeCount;
        holder.likeCount.setText(String.valueOf(likeCnt));
        if (likeCnt != 0) {
            holder.likeCount.setVisibility(0);
        } else {
            holder.likeCount.setVisibility(4);
        }
        if (moment.likedByCurrentUser) {
            holder.btLike.setText("Like");
        } else {
            holder.btLike.setText("Unlike");
        }
        commentCnt = moment.commentCount;
        holder.commentCount.setText(String.valueOf(commentCnt));
        if (commentCnt != 0) {
            holder.commentCount.setVisibility(0);
        } else {
            holder.commentCount.setVisibility(4);
        }
        holder.btComment.setOnClickListener(new C11525(moment));
        holder.btLike.setOnClickListener(new C11536(holder, moment));
        holder.mainLayout.setOnClickListener(new C11547(moment));
        holder.likesCommentsLayout.setOnClickListener(new C11558(moment));
        return view;
    }
}

package com.shamchat.stickers;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.shamchat.activity.ChatInitialForGroupChatActivity;
import com.shamchat.adapters.StickerAdapter;
import com.shamchat.androidclient.data.StickerProvider;
import java.util.Arrays;

public class StickerFragment extends Fragment {
    private Bundle fragmentBundle;
    private GridView grid;
    private Cursor stickerCursor;
    private String stickerPackId;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(2130903143, container, false);
        this.grid = (GridView) view.findViewById(2131362234);
        this.fragmentBundle = getArguments();
        this.stickerPackId = this.fragmentBundle.getString(SmileyAndStickerFragment.STICKER_PACK_ID);
        this.fragmentBundle.getString(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID);
        String reciepientId = this.fragmentBundle.getString("message_recipient");
        boolean isGroupChat = this.fragmentBundle.getBoolean("is_group_chat", false);
        this.stickerCursor = getActivity().getContentResolver().query(StickerProvider.CONTENT_URI_STICKER, null, "pack_id=?", new String[]{this.stickerPackId}, null);
        this.stickerCursor.moveToFirst();
        StickerAdapter adapter = new StickerAdapter(getActivity(), Arrays.asList(this.stickerCursor.getString(this.stickerCursor.getColumnIndex("download_url")).split("\\s*,\\s*")), reciepientId, isGroupChat);
        this.grid.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;
    }
}

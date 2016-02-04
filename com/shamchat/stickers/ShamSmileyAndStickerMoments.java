package com.shamchat.stickers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.shamchat.adapters.ChatSmileyAdapterMoments;
import com.shamchat.moments.MomentDetailActivity;

public class ShamSmileyAndStickerMoments extends Fragment {
    private GridView gridView;
    private ChatSmileyAdapterMoments smileyAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(2130903145, container, false);
        this.gridView = (GridView) inflate.findViewById(2131362248);
        this.smileyAdapter = new ChatSmileyAdapterMoments(getActivity(), (MomentDetailActivity) getActivity());
        this.gridView.setAdapter(this.smileyAdapter);
        return inflate;
    }
}

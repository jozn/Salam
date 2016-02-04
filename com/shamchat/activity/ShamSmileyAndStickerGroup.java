package com.shamchat.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.shamchat.adapters.ChatSmileyAdapterGroup;

public class ShamSmileyAndStickerGroup extends Fragment {
    private GridView gridView;
    private ChatSmileyAdapterGroup smileyAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(2130903145, container, false);
        this.gridView = (GridView) inflate.findViewById(2131362248);
        this.smileyAdapter = new ChatSmileyAdapterGroup(getActivity(), (ChatInitialForGroupChatActivity) getActivity());
        this.gridView.setAdapter(this.smileyAdapter);
        return inflate;
    }
}

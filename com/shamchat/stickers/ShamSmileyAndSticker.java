package com.shamchat.stickers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.shamchat.activity.ChatActivity;
import com.shamchat.adapters.ChatSmileyAdapter;

public class ShamSmileyAndSticker extends Fragment {
    private GridView gridView;
    private ChatSmileyAdapter smileyAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(2130903145, container, false);
        this.gridView = (GridView) inflate.findViewById(2131362248);
        this.smileyAdapter = new ChatSmileyAdapter(getActivity(), (ChatActivity) getActivity());
        this.gridView.setAdapter(this.smileyAdapter);
        return inflate;
    }
}

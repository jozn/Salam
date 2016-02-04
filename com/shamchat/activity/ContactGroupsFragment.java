package com.shamchat.activity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.shamchat.adapters.ContactsGroupsAdapter;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.UserProvider;
import com.shamchat.androidclient.util.FontUtil;
import com.shamchat.androidclient.util.PreferenceConstants;
import com.shamchat.models.FriendGroup;
import com.shamchat.utils.PopUpUtil;

public class ContactGroupsFragment extends Fragment {
    private ContactsGroupsAdapter adapter;
    private Cursor cursor;
    ListView listContacts;
    private Dialog popUp;

    /* renamed from: com.shamchat.activity.ContactGroupsFragment.1 */
    class C07331 implements OnClickListener {
        C07331() {
        }

        public final void onClick(View v) {
            ContactGroupsFragment.this.onClickCreateGroup(v);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onResume() {
        super.onResume();
        String userId = SHAMChatApplication.getConfig().userId;
        this.cursor = getActivity().getContentResolver().query(UserProvider.CONTENT_URI_GROUP, null, null, null, FriendGroup.DB_NAME + " ASC");
        this.adapter = new ContactsGroupsAdapter(getActivity(), this.cursor, this, userId);
        this.listContacts.setAdapter(this.adapter);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(2130903138, container, false);
        this.listContacts = (ListView) view.findViewById(2131362217);
        View createGroupButton = view.findViewById(2131362218);
        FontUtil.applyFont((TextView) view.findViewById(2131362220));
        createGroupButton.setOnClickListener(new C07331());
        return view;
    }

    protected void onClickCreateGroup(View v) {
        if (PreferenceConstants.CONNECTED_TO_NETWORK) {
            getActivity().startActivity(new Intent(getActivity(), CreateGroupActivity.class));
            return;
        }
        this.popUp = new PopUpUtil().getPopFailed$40a28a58(getActivity(), "Please verify internet availabilty");
        this.popUp.show();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onPause() {
        super.onPause();
        try {
            if (this.cursor != null) {
                this.cursor.close();
            }
        } catch (Exception e) {
        }
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            if (this.cursor != null) {
                this.cursor.close();
            }
        } catch (Exception e) {
        }
    }
}

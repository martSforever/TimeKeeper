package com.martsforever.owa.timekeeper.main.self;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.github.zagum.switchicon.SwitchIconView;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.FriendShip;

import java.util.List;

/**
 * Created by OWA on 2017/4/20.
 */

public class JurisdictionAdapter extends BaseAdapter implements View.OnClickListener {

    List<AVObject> selfFriendShips;
    List<AVObject> friendFriendships;

    LayoutInflater layoutInflater;
    Context context;


    public JurisdictionAdapter(List<AVObject> selfFriendShips, List<AVObject> friendFriendships, Context context) {
        this.selfFriendShips = selfFriendShips;
        this.friendFriendships = friendFriendships;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return selfFriendShips.size();
    }

    @Override
    public Object getItem(int position) {
        return selfFriendShips.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_jurisdiction_list, null);
            viewHolder = new ViewHolder();
            viewHolder.friendName = (TextView) convertView.findViewById(R.id.item_jurisdiction_friend_name_text);
            viewHolder.friendScheduleAvailable = (SwitchIconView) convertView.findViewById(R.id.item_jurisdiction_friend_schedule_icon);
            viewHolder.friendInvitationAvailable = (SwitchIconView) convertView.findViewById(R.id.item_jurisdiction_friend_invitation_icon);
            viewHolder.selfScheduleAvailable = (SwitchIconView) convertView.findViewById(R.id.item_jurisdiction_self_schedule_icon);
            viewHolder.selfInvitationAvailable = (SwitchIconView) convertView.findViewById(R.id.item_jurisdiction_self_invitation_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AVObject selfFriendship = selfFriendShips.get(position);
        AVObject friendFriendship = friendFriendships.get(position);

        viewHolder.friendName.setText(selfFriendship.getString(FriendShip.FRIEND_NAME));
        viewHolder.friendScheduleAvailable.setIconEnabled(friendFriendship.getBoolean(FriendShip.SCHEDULE_AVAILABLE));
        viewHolder.friendInvitationAvailable.setIconEnabled(friendFriendship.getBoolean(FriendShip.INVITATION_AVAILABLE));
        viewHolder.selfScheduleAvailable.setIconEnabled(selfFriendship.getBoolean(FriendShip.SCHEDULE_AVAILABLE));
        viewHolder.selfInvitationAvailable.setIconEnabled(selfFriendship.getBoolean(FriendShip.INVITATION_AVAILABLE));
        viewHolder.selfScheduleAvailable.setTag(position);
        viewHolder.selfInvitationAvailable.setTag(position);
        viewHolder.selfScheduleAvailable.setOnClickListener(this);
        viewHolder.selfInvitationAvailable.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        SwitchIconView switchIconView = (SwitchIconView) v;
        Integer position = (Integer) switchIconView.getTag();
        switchIconView.switchState();
        AVObject selfFriendship = selfFriendShips.get(position);
        switch (v.getId()) {
            case R.id.item_jurisdiction_self_schedule_icon:
                selfFriendship.put(FriendShip.SCHEDULE_AVAILABLE, switchIconView.isIconEnabled());
                selfFriendship.saveInBackground();
                break;
            case R.id.item_jurisdiction_self_invitation_icon:
                selfFriendship.put(FriendShip.INVITATION_AVAILABLE, switchIconView.isIconEnabled());
                selfFriendship.saveInBackground();
        }
    }

    class ViewHolder {
        TextView friendName;
        SwitchIconView friendScheduleAvailable;
        SwitchIconView friendInvitationAvailable;
        SwitchIconView selfScheduleAvailable;
        SwitchIconView selfInvitationAvailable;
    }
}

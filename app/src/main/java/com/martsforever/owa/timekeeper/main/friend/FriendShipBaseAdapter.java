package com.martsforever.owa.timekeeper.main.friend;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.FriendShip;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.main.message.MessageActivity;
import com.martsforever.owa.timekeeper.util.DataUtils;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import java.util.List;

/**
 * Created by OWA on 2017/3/20.
 */

public class FriendShipBaseAdapter extends BaseAdapter implements SlideAndDragListView.OnItemDeleteListener, SlideAndDragListView.OnListItemClickListener, SlideAndDragListView.OnMenuItemClickListener {

    private Activity activity;
    private List<AVObject> friendships;
    private LayoutInflater inflater;
    private SlideAndDragListView<AVObject> listView;

    public FriendShipBaseAdapter(List<AVObject> friendships, Activity activity, SlideAndDragListView<AVObject> listView) {
        this.friendships = friendships;
        inflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.listView = listView;
    }

    @Override
    public int getCount() {
        return friendships.size();
    }

    @Override
    public Object getItem(int position) {
        return friendships.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_friend_swip_list, null);
            viewHolder = new ViewHolder();
            viewHolder.usernameText = (TextView) convertView.findViewById(R.id.item_friend_username_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final AVObject friendship = friendships.get(position);
        viewHolder.usernameText.setText(friendship.getAVUser(FriendShip.FRIEND).getString(Person.NICK_NAME));
        return convertView;
    }

    @Override
    public void onItemDelete(View view, int position) {
        System.out.println(friendships == null);
        System.out.println(listView == null);
        friendships.remove(position - listView.getHeaderViewsCount());
        this.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(View v, int position) {
        AVObject friendShip = friendships.get(position);
        FriendDetailActivity.actionStart(activity, friendShip.toString(), position);
    }

    @Override
    public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
        switch (direction) {
            case MenuItem.DIRECTION_RIGHT:
                switch (buttonPosition) {
                    case 0:
                        System.out.println("menu item click");
                        return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                }
        }
        return Menu.ITEM_NOTHING;
    }

    class ViewHolder {
        TextView usernameText;
    }

    public List<AVObject> getFriendships() {
        return friendships;
    }

    public void setFriendships(List<AVObject> friendships) {
        this.friendships = friendships;
    }

    public Menu getListMenu() {
        Menu menu = new Menu(true, true);
        menu.addItem(new MenuItem.Builder().setWidth(DataUtils.dp2px(90, activity))
                .setBackground((new ColorDrawable(Color.argb(0xdd, 222, 140, 104))))
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setIcon(activity.getResources().getDrawable(R.drawable.icon_delete))
                .build());
        return menu;
    }
}

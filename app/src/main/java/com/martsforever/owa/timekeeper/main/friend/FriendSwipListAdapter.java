package com.martsforever.owa.timekeeper.main.friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.main.common.BaseSwipListAdapter;

import java.util.List;

/**
 * Created by owa on 2017/1/18.
 */

public class FriendSwipListAdapter extends BaseSwipListAdapter {

    List<AVUser> persons;
    LayoutInflater inflater;

    public FriendSwipListAdapter(List<AVUser> persons, Context context) {
        this.persons = persons;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return persons.size();
    }

    @Override
    public Object getItem(int position) {
        return persons.get(position);
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

        final AVUser person = persons.get(position);
        viewHolder.usernameText.setText(person.getUsername());
        viewHolder.usernameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(person.getUsername() + " had been clicked");
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView usernameText;
    }
}

package com.martsforever.owa.timekeeper.main.friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Person;

import java.util.List;

/**
 * Created by OWA on 2017/3/20.
 */

public class FriendBaseAdapter extends BaseAdapter {

    List<AVUser> persons;
    LayoutInflater inflater;
    ViewOnclickListener viewOnclickListener;

    public FriendBaseAdapter(List<AVUser> persons, Context context) {
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
        viewHolder.usernameText.setText((String) person.get(Person.NICK_NAME));

        if (viewOnclickListener != null) {
            viewOnclickListener.viewOnClick(convertView,persons.get(position));
        }

        return convertView;
    }

    class ViewHolder {
        TextView usernameText;
    }

    public List<AVUser> getPersons() {
        return persons;
    }

    public void setPersons(List<AVUser> persons) {
        this.persons = persons;
    }

    public ViewOnclickListener getViewOnclickListener() {
        return viewOnclickListener;
    }

    public void setViewOnclickListener(ViewOnclickListener viewOnclickListener) {
        this.viewOnclickListener = viewOnclickListener;
    }

    interface ViewOnclickListener {
        public void viewOnClick(View v,AVUser user);
    }
}

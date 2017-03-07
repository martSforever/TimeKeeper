package com.martsforever.owa.timekeeper.main.todo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.javabean.Todo;
import com.martsforever.owa.timekeeper.main.common.BaseSwipListAdapter;
import com.martsforever.owa.timekeeper.main.friend.FriendAdapter;

import java.util.List;

import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by owa on 2017/1/24.
 */

public class TodoAdapter extends BaseSwipListAdapter {

    List<Todo> todos;
    LayoutInflater layoutInflater;

    public TodoAdapter(Context context, List<Todo> todos) {
        this.todos = todos;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return todos.size();
    }

    @Override
    public Object getItem(int position) {
        return todos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_todo_swip_list, null);
            viewHolder = new ViewHolder();
            viewHolder.titleText = (TextView) convertView.findViewById(R.id.item_todo_title_text);
            viewHolder.timeText = (TextView) convertView.findViewById(R.id.item_todo_time_text);
            viewHolder.stateText = (TextView) convertView.findViewById(R.id.item_todo_state_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Todo todo = todos.get(position);
        viewHolder.titleText.setText(todo.getTitle());
        viewHolder.timeText.setText(todo.getStartTime().getDate() + " to " + todo.getEndTime().getDate());
        viewHolder.stateText.setText(todo.getState()+"");

        return convertView;
    }

    class ViewHolder {
        TextView titleText;
        TextView timeText;
        TextView stateText;
    }

}

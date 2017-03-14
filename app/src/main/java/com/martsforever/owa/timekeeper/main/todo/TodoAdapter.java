package com.martsforever.owa.timekeeper.main.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Todo;
import com.martsforever.owa.timekeeper.main.common.BaseSwipListAdapter;
import com.martsforever.owa.timekeeper.util.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by owa on 2017/1/24.
 */

public class TodoAdapter extends BaseSwipListAdapter {

    List<AVObject> todos;
    LayoutInflater layoutInflater;

    public TodoAdapter(Context context, List<AVObject> todos) {
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
            viewHolder.endTimeText = (TextView) convertView.findViewById(R.id.item_todo_end_time_text);
            viewHolder.descriptionText = (TextView) convertView.findViewById(R.id.item_todo_description_text);
            viewHolder.levelImg = (ImageView) convertView.findViewById(R.id.item_todo_level_img);
            viewHolder.statusImg = (ImageView) convertView.findViewById(R.id.item_todo_status_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final AVObject todo = todos.get(position);
        viewHolder.titleText.setText(todo.getString(Todo.TITLE));
        viewHolder.descriptionText.setText(todo.getString(Todo.DESCRIPTION));

        Date endTime = todo.getDate(Todo.END_TIME);
        viewHolder.endTimeText.setText(DateUtil.dateToString(endTime,DateUtil.COMPLICATE_DATE_TOW_LINE));
        viewHolder.levelImg.setImageResource(Todo.getLevelImage(todo.getInt(Todo.LEVEL)));
        viewHolder.statusImg.setImageResource(Todo.getStatusImage(todo.getInt(Todo.STATE)));
        return convertView;
    }

    class ViewHolder {
        TextView titleText;
        TextView endTimeText;
        TextView descriptionText;
        ImageView levelImg;
        ImageView statusImg;
    }


}

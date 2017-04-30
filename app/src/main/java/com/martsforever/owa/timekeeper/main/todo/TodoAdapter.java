package com.martsforever.owa.timekeeper.main.todo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.github.zagum.switchicon.SwitchIconView;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Message;
import com.martsforever.owa.timekeeper.javabean.Todo;
import com.martsforever.owa.timekeeper.javabean.User2Todo;
import com.martsforever.owa.timekeeper.util.DateUtil;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by owa on 2017/1/24.
 */

public class TodoAdapter extends BaseAdapter {

    List<AVObject> user2todos;
    LayoutInflater layoutInflater;
    Context context;

    public TodoAdapter(Context context, List<AVObject> user2todos) {
        this.context = context;
        this.user2todos = user2todos;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return user2todos.size();
    }

    @Override
    public Object getItem(int position) {
        return user2todos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_todo_all_listview, null);
            viewHolder = new ViewHolder();
            viewHolder.titleText = (TextView) convertView.findViewById(R.id.item_todo_title_text);
            viewHolder.timeText = (TextView) convertView.findViewById(R.id.item_todo_time_text);
            viewHolder.descriptionText = (TextView) convertView.findViewById(R.id.item_todo_description_text);
            viewHolder.levelImg = (ImageView) convertView.findViewById(R.id.item_todo_level_img);
            viewHolder.statusImg = (ImageView) convertView.findViewById(R.id.item_todo_status_img);
            viewHolder.switchIconView = (SwitchIconView) convertView.findViewById(R.id.item_todo_switch);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AVObject user2todo = user2todos.get(position);
        AVObject todo = (AVObject) user2todo.get(User2Todo.TODO);

        viewHolder.titleText.setText(todo.getString(Todo.TITLE));
        viewHolder.descriptionText.setText(todo.getString(Todo.DESCRIPTION));
        System.out.println(todo.getDate(Todo.START_TIME).toString());
        viewHolder.timeText.setText(DateUtil.date2String(todo.getDate(Todo.START_TIME), DateUtil.COMPLICATED_DATE) + " - " + DateUtil.date2String(todo.getDate(Todo.END_TIME), DateUtil.COMPLICATED_DATE));
        viewHolder.levelImg.setImageResource(Todo.getLevelImage(todo.getInt(Todo.LEVEL)));
        viewHolder.statusImg.setImageResource(Todo.getStatusImage(todo.getInt(Todo.STATE)));

        viewHolder.switchIconView.setTag(position);
        viewHolder.switchIconView.setIconEnabled(user2todo.getBoolean(User2Todo.SWITCH));
        viewHolder.switchIconView.setOnClickListener(mOnClickListener);
        return convertView;
    }

    class ViewHolder {
        TextView titleText;
        TextView timeText;
        TextView descriptionText;
        ImageView levelImg;
        ImageView statusImg;
        SwitchIconView switchIconView;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            Integer position = (Integer) v.getTag();
            SwitchIconView switchIconView = (SwitchIconView) v;
            switchIconView.switchState();
            user2todos.get(position).put(User2Todo.SWITCH, switchIconView.isIconEnabled());
            user2todos.get(position).saveInBackground();
        }
    };


}

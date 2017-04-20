package com.martsforever.owa.timekeeper.main.todo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.avos.avoscloud.AVObject;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Todo;
import com.martsforever.owa.timekeeper.util.DateUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ContentView(R.layout.activity_all_schedule)
public class AllScheduleActivity extends AppCompatActivity {

    private List<AVObject> todos;
    private TodoAdapter todoAdapter;
    @ViewInject(R.id.todo_swip_list_view)
    private SwipeMenuListView todoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initTodosInterface();
    }

    private void initTodosInterface() {
        /*todos interface*/
        todos = getTodosData();
        todoAdapter = new TodoAdapter(this, todos);
        todoListView.setAdapter(todoAdapter);
        todoListView.setMenuCreator(TodoMenuCreater.getFriendsMenuCreater(this));
        todoListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 1:
                        // delete
                        todos.remove(position);
                        todoAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });
    }

    /*get todos data to test*/
    private List<AVObject> getTodosData() {
        List<AVObject> todos = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            AVObject todo = new AVObject(Todo.TABLE_TODO);
            todo.put(Todo.TITLE, "title:" + (i + 1));
            todo.put(Todo.DESCRIPTION, "this is description:" + (i + 1));
            todo.put(Todo.END_TIME, DateUtil.getRandomDate());
//            todo.put(Todo.END_TIME, new Date());
            todo.put(Todo.STATE, random.nextInt(4) + 1);
            todo.put(Todo.LEVEL, random.nextInt(4) + 1);
            todos.add(todo);
        }
        return todos;
    }

    public static void actionStart(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, AllScheduleActivity.class);
        activity.startActivityForResult(intent, 0);
    }

    @Event(R.id.all_schedule_back_img)
    private void back(View view){
        finish();
    }
}

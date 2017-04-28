package com.martsforever.owa.timekeeper.main.todo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Message;
import com.martsforever.owa.timekeeper.javabean.Todo;
import com.martsforever.owa.timekeeper.javabean.User2Todo;
import com.martsforever.owa.timekeeper.main.message.MessageActivity;
import com.martsforever.owa.timekeeper.main.message.MessageAdapter;
import com.martsforever.owa.timekeeper.main.push.MessageHandler;
import com.martsforever.owa.timekeeper.util.DataUtils;
import com.martsforever.owa.timekeeper.util.DateUtil;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ContentView(R.layout.activity_all_schedule)
public class AllScheduleActivity extends AppCompatActivity implements SlideAndDragListView.OnListItemClickListener, SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {

    List<AVObject> user2todos;
    Menu listViewMenu;
    TodoAdapter todoAdapter;
    @ViewInject(R.id.todo_all_listview)
    SlideAndDragListView<AVObject> todoListview;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            user2todos = (List<AVObject>) msg.obj;
            todoAdapter = new TodoAdapter(AllScheduleActivity.this,user2todos);
            initUiAndListener();
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initData();
    }
    private void initData() {
        AVUser currentUser = AVUser.getCurrentUser();
        AVQuery<AVObject> query = new AVQuery<>(User2Todo.TABLE_USER_2_TODO);
        query.whereEqualTo(User2Todo.USER, currentUser);
        query.include(User2Todo.TODO);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    android.os.Message message = new android.os.Message();
                    message.obj = list;
                    handler.sendMessage(message);
                } else {
                    ShowMessageUtil.tosatFast(e.getMessage(), AllScheduleActivity.this);
                }
            }
        });
    }
    public void initMenu() {
        listViewMenu = new Menu(false, false);
        listViewMenu.addItem(new MenuItem.Builder().setWidth(DataUtils.dp2px(90, AllScheduleActivity.this))
                .setBackground((new ColorDrawable(Color.argb(0xdd, 222, 140, 104))))
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setIcon(getResources().getDrawable(R.drawable.icon_delete))
                .build());
    }

    public void initUiAndListener() {
        initMenu();
        todoListview.setMenu(listViewMenu);
        todoListview.setAdapter(todoAdapter);
        todoListview.setOnListItemClickListener(this);
        todoListview.setOnMenuItemClickListener(this);
        todoListview.setOnItemDeleteListener(this);
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
    private void back(View view) {
        finish();
    }

    @Override
    public void onListItemClick(View v, int position) {
        ShowMessageUtil.tosatFast("item click",this);
    }

    @Override
    public void onItemDelete(View view, int position) {
        user2todos.remove(position - todoListview.getHeaderViewsCount());
        todoAdapter.notifyDataSetChanged();
    }

    @Override
    public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
        switch (direction) {
            case MenuItem.DIRECTION_RIGHT:
                switch (buttonPosition) {
                    case 0:
                        ShowMessageUtil.tosatFast("delete",this);
                        return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                }
        }
        return Menu.ITEM_NOTHING;
    }
}

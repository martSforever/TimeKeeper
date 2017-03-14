package com.martsforever.owa.timekeeper.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.javabean.Todo;
import com.martsforever.owa.timekeeper.main.friend.FriendMenuCreater;
import com.martsforever.owa.timekeeper.main.friend.FriendAdapter;
import com.martsforever.owa.timekeeper.main.todo.TodoAdapter;
import com.martsforever.owa.timekeeper.main.todo.TodoMenuCreater;
import com.martsforever.owa.timekeeper.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /*main interface element*/
    private NoScrollViewPager labelViewPager;
    private ImageView underlineImg;

    private int currentIndex;

    private List<View> pagerItems;

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public List<View> getPagerItems() {
        return pagerItems;
    }

    public void setPagerItems(List<View> pagerItems) {
        this.pagerItems = pagerItems;
    }

    /*friend interface element*/
    List<AVUser> friends;
    private FriendAdapter friendAdapter;
    private SwipeMenuListView friendListView;

    /*to do interface element*/
    List<AVObject> todos;
    private TodoAdapter todoAdapter;
    private SwipeMenuListView todoListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

        /*main interface*/
        LayoutInflater inflater = LayoutInflater.from(this);
        View todoView = inflater.inflate(R.layout.page_view_schedule, null);
        View tomatoView = inflater.inflate(R.layout.page_view_tomato, null);
        View friendsView = inflater.inflate(R.layout.page_view_friends, null);
        View meView = inflater.inflate(R.layout.page_view_me, null);

        pagerItems = new ArrayList<>();
        pagerItems.add(todoView);
        pagerItems.add(tomatoView);
        pagerItems.add(friendsView);
        pagerItems.add(meView);

        labelViewPager = (NoScrollViewPager) findViewById(R.id.label_pager_view);
        MainInterfacePagerAdapter adapter = new MainInterfacePagerAdapter(pagerItems, this);
        labelViewPager.setAdapter(adapter);
        labelViewPager.setOnPageChangeListener(adapter);
        labelViewPager.setNoScroll(true);

        findViewById(R.id.label_schedule).setOnClickListener(this);
        findViewById(R.id.label_tomato).setOnClickListener(this);
        findViewById(R.id.label_friends).setOnClickListener(this);
        findViewById(R.id.label_me).setOnClickListener(this);

        /*friends interface*/
        friends = getFriendsData();
        friendAdapter = new FriendAdapter(friends, this);
        friendListView = (SwipeMenuListView) friendsView.findViewById(R.id.friend_swip_list_view);
        friendListView.setAdapter(friendAdapter);
        friendListView.setMenuCreator(FriendMenuCreater.getFriendsMenuCreater(this));
        friendListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // delete
                        friends.remove(position);
                        friendAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });

        /*todos interface*/
        todos = getTodosData();
        final TodoAdapter todoAdapter = new TodoAdapter(this, todos);
        todoListView = (SwipeMenuListView) todoView.findViewById(R.id.todo_swip_list_view);
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

    @Override
    public void onClick(View v) {
        int index = 0;
        switch (v.getId()) {
            case R.id.label_schedule:
                index = 0;
                break;
            case R.id.label_tomato:
                index = 1;
                break;
            case R.id.label_friends:
                index = 2;
                break;
            case R.id.label_me:
                index = 3;
                break;
        }
        labelViewPager.setCurrentItem(index);
    }

    /*produce friends data to test*/
    private List<AVUser> getFriendsData() {
        List<AVUser> persons = new ArrayList<>();
        AVUser p1 = new AVUser();
        p1.setUsername("alice");
        AVUser p2 = new AVUser();
        p2.setUsername("bob");
        AVUser p3 = new AVUser();
        p3.setUsername("marts");
        persons.add(p1);
        persons.add(p2);
        persons.add(p3);
        persons.add(p1);
        persons.add(p2);
        persons.add(p3);
        persons.add(p1);
        persons.add(p2);
        persons.add(p3);
        persons.add(p1);
        persons.add(p2);
        persons.add(p3);
        persons.add(p1);
        persons.add(p2);
        persons.add(p3);
        return persons;
    }

    /*get todos data to test*/
    private List<AVObject> getTodosData() {

        List<AVObject> todos = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 15; i++) {
            AVObject todo = new AVObject(Todo.TABLE_TODO);

            todo.put(Todo.TITLE,"title:" + (i + 1));
            todo.put(Todo.DESCRIPTION,"this is description:"+(i+1));
            todo.put(Todo.END_TIME, DateUtil.getRandomDate());
//            todo.put(Todo.END_TIME, new Date());
            todo.put(Todo.STATE, random.nextInt(4)+1);
            todo.put(Todo.LEVEL,random.nextInt(4)+1);
            todos.add(todo);
        }
        return todos;
    }
}

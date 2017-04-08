package com.martsforever.owa.timekeeper.main;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Todo;
import com.martsforever.owa.timekeeper.main.friend.AddFriendsActivity;
import com.martsforever.owa.timekeeper.main.friend.FriendBaseAdapter;
import com.martsforever.owa.timekeeper.main.push.MessageReceiver;
import com.martsforever.owa.timekeeper.main.todo.TodoAdapter;
import com.martsforever.owa.timekeeper.main.todo.TodoMenuCreater;
import com.martsforever.owa.timekeeper.util.DataUtils;
import com.martsforever.owa.timekeeper.util.DateUtil;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;
import com.skyfishjy.library.RippleBackground;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /*main interface element*/
    private NoScrollViewPager labelViewPager;

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
    private FriendBaseAdapter friendAdapter;
    private ListView friendListView;
    private ImageView turnToAddFriendBtn;

    /*to do interface element*/
    List<AVObject> todos;
    private TodoAdapter todoAdapter;
    private SwipeMenuListView todoListView;

    /*toamto interface element*/
    RippleBackground rippleBackground;
    TextView tomatoTimeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        /*xutils注入*/
        x.view().inject(this);
        System.out.println("init");
        initView();
        registerReceiver();
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
        friends = DataUtils.getFriendsData();
        friendAdapter = new FriendBaseAdapter(friends, this);
        friendListView = (ListView) friendsView.findViewById(R.id.friend_swip_list_view);
        friendListView.setAdapter(friendAdapter);
        turnToAddFriendBtn = (ImageView) friendsView.findViewById(R.id.friend_add_firends_btn);
        turnToAddFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddFriendsActivity.class);
                startActivity(intent);
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

        rippleBackground = (RippleBackground) tomatoView.findViewById(R.id.tomato_ripple_view);
        tomatoTimeText = (TextView) tomatoView.findViewById(R.id.tomato_time_text);
        tomatoTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (rippleBackground.isRippleAnimationRunning()) {
                    /*结束动画*/
                    rippleBackground.stopRippleAnimation();
                } else
                    /*开始动画*/
                    rippleBackground.startRippleAnimation();
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

    /**
     * register push message receiver
     */
    private void registerReceiver() {
        System.out.println("retgister receiver 1111111");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("com.avos.UPDATE_STATUS");
        MessageReceiver messageReceiver = new MessageReceiver();
        messageReceiver.setHandleMessage(new MessageReceiver.HandleMessage() {
            @Override
            public void receiveMessage(JSONObject jsonObject) {
                ShowMessageUtil.tosatFast(jsonObject.getString("message"),MainActivity.this);
            }
        });
        registerReceiver(messageReceiver, intentFilter);
    }

}

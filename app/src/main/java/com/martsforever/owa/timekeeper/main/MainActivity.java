package com.martsforever.owa.timekeeper.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.dbbean.DBFriendShip;
import com.martsforever.owa.timekeeper.dbbean.DBMessage;
import com.martsforever.owa.timekeeper.dbbean.DBOfflineUser2Todo;
import com.martsforever.owa.timekeeper.dbbean.DBUser2Todo;
import com.martsforever.owa.timekeeper.dbbean.DBUtils;
import com.martsforever.owa.timekeeper.javabean.FriendShip;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.javabean.Todo;
import com.martsforever.owa.timekeeper.javabean.User2Todo;
import com.martsforever.owa.timekeeper.main.friend.AddFriendsActivity;
import com.martsforever.owa.timekeeper.main.friend.FriendDetailActivity;
import com.martsforever.owa.timekeeper.main.friend.FriendShipBaseAdapter;
import com.martsforever.owa.timekeeper.main.message.MessageActivity;
import com.martsforever.owa.timekeeper.main.push.MessageHandler;
import com.martsforever.owa.timekeeper.main.push.MessageReceiver;
import com.martsforever.owa.timekeeper.main.self.JurisdictionActivity;
import com.martsforever.owa.timekeeper.main.self.PersonInfoActivity;
import com.martsforever.owa.timekeeper.main.self.SecurityActivity;
import com.martsforever.owa.timekeeper.main.todo.AddTodosActivity;
import com.martsforever.owa.timekeeper.main.todo.AllTodosActivity;
import com.martsforever.owa.timekeeper.main.todo.CategoryTodoActivity;
import com.martsforever.owa.timekeeper.main.todo.OfflineTodoActivity;
import com.martsforever.owa.timekeeper.main.todo.OfflineTodoDetailActivity;
import com.martsforever.owa.timekeeper.util.ActivityManager;
import com.martsforever.owa.timekeeper.util.NetWorkUtils;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;
import com.skyfishjy.library.RippleBackground;
import com.yydcdut.sdlv.SlideAndDragListView;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int INIT_FRIENDSHIPS = 0x001;
    public static final int FRIENDSHIP_CHANGE = 0x002;
    public static final int MESSAGE_BADGE_CHANGE = 0x003;
    public static final int FRIENDSHIP_ALL_CHANGE = 0x004;
    public static final int INIT_TODO = 0x004;
    public static final int INIT_MESSAGE = 0x005;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INIT_FRIENDSHIPS:
                    friendShips = (List<AVObject>) msg.obj;
                    initFriendInterface();
                    break;
                case INIT_TODO:
                    allUser2todoList = (List<AVObject>) msg.obj;
                    for (AVObject user2todo : allUser2todoList)
                        categoryUser2todo(user2todo);
                    initTodoInterface();
                    break;
                case INIT_MESSAGE:
                    List<AVObject> list = (List<AVObject>) msg.obj;
                    int unReadNumberOfMessage = 0;
                    for (AVObject message : list) {
                        if (message.getInt(com.martsforever.owa.timekeeper.javabean.Message.IS_READ) == com.martsforever.owa.timekeeper.javabean.Message.UNREAD)
                            unReadNumberOfMessage++;
                    }
                    messageTextBadge.setBadgeNumber(unReadNumberOfMessage);
            }
        }
    };

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

    private View todoView;
    private View tomatoView;
    private View friendsView;
    private View meView;
    /*friend interface element*/
    List<AVObject> friendShips;
    private FriendShipBaseAdapter friendAdapter;
    private SlideAndDragListView<AVObject> friendListView;
    private ImageView turnToAddFriendBtn;
    /*to do interface element*/
    private List<AVObject> allUser2todoList;
    private List<AVObject> todayUser2todoList = new ArrayList<>();
    private List<AVObject> importantUser2todoList = new ArrayList<>();
    private List<AVObject> doingUser2todoList = new ArrayList<>();
    private List<AVObject> completeUser2todoList = new ArrayList<>();
    private List<AVObject> unfinishedUser2todoList = new ArrayList<>();
    private List<AVObject> readyUser2todoList = new ArrayList<>();
    private List<AVObject> offlineUser2todoList = new ArrayList<>();

    private ImageView allScheduleImg;
    private QBadgeView badgeViewAllSchedule;
    private ImageView todayScheduleImg;
    private QBadgeView badgeViewTodaySchedule;
    private ImageView importantScheduleImg;
    private QBadgeView badgeViewImportantSchedule;
    private ImageView doingScheduleImg;
    private QBadgeView badgeViewDoingSchedule;
    private ImageView completeScheduleImg;
    private QBadgeView badgeViewCompleteySchedule;
    private ImageView unfinishedScheduleImg;
    private QBadgeView badgeViewUnfinishedSchedule;
    private ImageView readyScheduleImg;
    private QBadgeView badgeViewReadySchedule;
    private ImageView addScheduleImg;
    private QBadgeView badgeViewOfflineSchedule;
    private ImageView offlineScheduleImg;
    /*toamto interface element*/
    private RippleBackground rippleBackground;
    private TextView tomatoTimeText;
    /*me interface element*/
    private TextView messageText;
    private ImageView messageInformImg;
    private QBadgeView messageTextBadge;
    private TextView informationText;
    private TextView securityText;
    private TextView juridictionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*xutils注入*/
        x.view().inject(this);
        System.out.println("init");
        initView();
        registerPushReceiver();
    }


    private void initView() {
        checkNetworkIsAvailable();
        initMainInterface();
        initDataFriendShips();
        initTomatoInterface();
        initMeInterface();
        initOfflineTodoData();
        initTodoListData();
        registerAddOnlineTodoReceiver();
        registerAddOfflineTodoReceiver();
    }

    private void checkNetworkIsAvailable() {
        if (NetWorkUtils.isNetworkAvailable(this)) {
            try {
                List<DBMessage> dbMessageList = DBUtils.getDbManager().selector(DBMessage.class)
                        .findAll();
                if (dbMessageList != null)
                    for (DBMessage dbMessage : dbMessageList)
                        DBMessage.delete(dbMessage);

                List<DBUser2Todo> user2TodoList = DBUtils.getDbManager().selector(DBUser2Todo.class)
                        .findAll();
                if (user2TodoList != null)
                    for (DBUser2Todo dbUser2Todo : user2TodoList)
                        DBUser2Todo.delete(dbUser2Todo);
                List<DBFriendShip> dbFriendShipList = DBUtils.getDbManager().selector(DBFriendShip.class)
                        .findAll();
                if (dbFriendShipList != null)
                    for (DBFriendShip dbFriendShip : dbFriendShipList)
                        DBFriendShip.delete(dbFriendShip);

            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    private void initMainInterface() {
        /*main interface*/
        LayoutInflater inflater = LayoutInflater.from(this);
        todoView = inflater.inflate(R.layout.page_view_schedule, null);
        tomatoView = inflater.inflate(R.layout.page_view_tomato, null);
        friendsView = inflater.inflate(R.layout.page_view_friends, null);
        meView = inflater.inflate(R.layout.page_view_me, null);

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
    }

    private void initFriendInterface() {
         /*friendShips interface*/
        friendListView = (SlideAndDragListView) friendsView.findViewById(R.id.friend_swip_list_view);
        friendAdapter = new FriendShipBaseAdapter(friendShips, this, friendListView);
        friendListView.setMenu(friendAdapter.getListMenu());
        friendListView.setOnItemDeleteListener(friendAdapter);
        friendListView.setOnMenuItemClickListener(friendAdapter);
        friendListView.setOnListItemClickListener(friendAdapter);

        friendListView.setAdapter(friendAdapter);
        turnToAddFriendBtn = (ImageView) friendsView.findViewById(R.id.friend_add_firends_btn);
        turnToAddFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetWorkUtils.isNetworkAvailable(MainActivity.this)) {
                    NetWorkUtils.showNetworkNotAvailable(MainActivity.this);
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddFriendsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initTodoInterface() {
        allScheduleImg = (ImageView) todoView.findViewById(R.id.me_schedule_all_img);
        todayScheduleImg = (ImageView) todoView.findViewById(R.id.me_schedule_today_img);
        importantScheduleImg = (ImageView) todoView.findViewById(R.id.me_schedule_important_img);
        doingScheduleImg = (ImageView) todoView.findViewById(R.id.me_schedule_doing_img);
        completeScheduleImg = (ImageView) todoView.findViewById(R.id.me_schedule_complete_img);
        unfinishedScheduleImg = (ImageView) todoView.findViewById(R.id.me_schedule_unfinished_img);
        readyScheduleImg = (ImageView) todoView.findViewById(R.id.me_schedule_ready_img);
        addScheduleImg = (ImageView) todoView.findViewById(R.id.me_schedule_add_img);
        offlineScheduleImg = (ImageView) todoView.findViewById(R.id.me_schedule_offine_img);

        badgeViewAllSchedule = new QBadgeView(this);
        badgeViewAllSchedule.bindTarget(allScheduleImg);
        badgeViewAllSchedule.setBadgeTextSize(10, true);
        badgeViewAllSchedule.setBadgeBackgroundColor(Color.parseColor("#c3bed4"));
        badgeViewTodaySchedule = new QBadgeView(this);
        badgeViewTodaySchedule.bindTarget(todayScheduleImg);
        badgeViewTodaySchedule.setBadgeTextSize(10, true);
        badgeViewTodaySchedule.setBadgeBackgroundColor(Color.parseColor("#7e884f"));
        badgeViewImportantSchedule = new QBadgeView(this);
        badgeViewImportantSchedule.bindTarget(importantScheduleImg);
        badgeViewImportantSchedule.setBadgeTextSize(10, true);
        badgeViewImportantSchedule.setBadgeBackgroundColor(Color.parseColor("#f17c67"));
        badgeViewDoingSchedule = new QBadgeView(this);
        badgeViewDoingSchedule.bindTarget(doingScheduleImg);
        badgeViewDoingSchedule.setBadgeTextSize(10, true);
        badgeViewDoingSchedule.setBadgeBackgroundColor(Color.parseColor("#495a80"));
        badgeViewCompleteySchedule = new QBadgeView(this);
        badgeViewCompleteySchedule.bindTarget(completeScheduleImg);
        badgeViewCompleteySchedule.setBadgeTextSize(10, true);
        badgeViewCompleteySchedule.setBadgeBackgroundColor(Color.parseColor("#00ff80"));
        badgeViewUnfinishedSchedule = new QBadgeView(this);
        badgeViewUnfinishedSchedule.bindTarget(unfinishedScheduleImg);
        badgeViewUnfinishedSchedule.setBadgeTextSize(10, true);
        badgeViewUnfinishedSchedule.setBadgeBackgroundColor(Color.parseColor("#9966cc"));
        badgeViewReadySchedule = new QBadgeView(this);
        badgeViewReadySchedule.bindTarget(readyScheduleImg);
        badgeViewReadySchedule.setBadgeTextSize(10, true);
        badgeViewReadySchedule.setBadgeBackgroundColor(Color.parseColor("#c9cabb"));
        badgeViewOfflineSchedule = new QBadgeView(this);
        badgeViewOfflineSchedule.bindTarget(offlineScheduleImg);
        badgeViewOfflineSchedule.setBadgeTextSize(10, true);
        badgeViewOfflineSchedule.setBadgeBackgroundColor(Color.parseColor("#c9cabb"));


        flashScheduleBadge();
        allScheduleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllTodosActivity.actionStart(MainActivity.this, allUser2todoList);
            }
        });
        addScheduleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTodosActivity.actionStart(MainActivity.this);
            }
        });
        todayScheduleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryTodoActivity.actionStart(MainActivity.this, todayUser2todoList, "Today");
            }
        });
        importantScheduleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryTodoActivity.actionStart(MainActivity.this, importantUser2todoList, "Very Important");
            }
        });
        doingScheduleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryTodoActivity.actionStart(MainActivity.this, doingUser2todoList, "Very Important");
            }
        });
        completeScheduleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryTodoActivity.actionStart(MainActivity.this, completeUser2todoList, "Complete");
            }
        });
        unfinishedScheduleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryTodoActivity.actionStart(MainActivity.this, unfinishedUser2todoList, "Complete");
            }
        });
        readyScheduleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryTodoActivity.actionStart(MainActivity.this, readyUser2todoList, "Complete");
            }
        });
        offlineScheduleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OfflineTodoActivity.actionStart(MainActivity.this, offlineUser2todoList, "Offline");
            }
        });

    }

    private void flashScheduleBadge() {
        badgeViewAllSchedule.setBadgeNumber(allUser2todoList.size());
        badgeViewTodaySchedule.setBadgeNumber(todayUser2todoList.size());
        badgeViewImportantSchedule.setBadgeNumber(importantUser2todoList.size());
        badgeViewDoingSchedule.setBadgeNumber(doingUser2todoList.size());
        badgeViewCompleteySchedule.setBadgeNumber(completeUser2todoList.size());
        badgeViewUnfinishedSchedule.setBadgeNumber(unfinishedUser2todoList.size());
        badgeViewReadySchedule.setBadgeNumber(readyUser2todoList.size());
        badgeViewOfflineSchedule.setBadgeNumber(offlineUser2todoList.size());
    }

    private void initTomatoInterface() {
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

    private void initMeInterface() {
        /*me interface*/
        messageText = (TextView) meView.findViewById(R.id.me_message_text);
        messageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageActivity.actionStart(MainActivity.this);
            }
        });
        messageInformImg = (ImageView) meView.findViewById(R.id.me_message_badge);
        messageTextBadge = new QBadgeView(this);
        messageTextBadge.bindTarget(messageInformImg);
        messageTextBadge.setBadgeTextSize(10, true);
        messageTextBadge.setGravityOffset(0, 0, true);
        initMessageTextBadge();
        informationText = (TextView) meView.findViewById(R.id.me_information_text);
        informationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonInfoActivity.actionStart(MainActivity.this);
            }
        });
        securityText = (TextView) meView.findViewById(R.id.me_security_text);
        securityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetWorkUtils.isNetworkAvailable(MainActivity.this)) {
                    NetWorkUtils.showNetworkNotAvailable(MainActivity.this);
                    return;
                }
                SecurityActivity.actionStart(MainActivity.this);
                ActivityManager.addDestoryActivity(MainActivity.this, MainActivity.class.getName());
            }
        });
        juridictionText = (TextView) meView.findViewById(R.id.me_juridiction_text);
        juridictionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JurisdictionActivity.actionStart(MainActivity.this);
            }
        });
    }

    private void initDataFriendShips() {
        if (NetWorkUtils.isNetworkAvailable(this)) {
            List<DBFriendShip> dbFriendShipList = null;
            try {
                dbFriendShipList = DBUtils.getDbManager().selector(DBFriendShip.class)
                        .findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
            if (dbFriendShipList != null)
                for (DBFriendShip dbFriendShip : dbFriendShipList)
                    DBFriendShip.delete(dbFriendShip);

            System.out.println("query friendship from network");
            AVQuery<AVObject> query = new AVQuery<>(FriendShip.TABLE_FRIENDSHIP);
            query.whereEqualTo(FriendShip.SELF, AVUser.getCurrentUser());
            query.include(FriendShip.FRIEND);
            query.orderByAscending(FriendShip.FRIEND);
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (e == null) {
                        for (AVObject object : list)
                            DBFriendShip.save(object);
                        Message message = new Message();
                        message.what = INIT_FRIENDSHIPS;
                        message.obj = list;
                        handler.sendMessage(message);
                    } else {
                        ShowMessageUtil.tosatFast(e.getMessage(), MainActivity.this);
                    }
                }
            });
            query = new AVQuery<>(FriendShip.TABLE_FRIENDSHIP);
            query.whereEqualTo(FriendShip.FRIEND, AVUser.getCurrentUser());
            query.orderByAscending(FriendShip.SELF);
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (e == null) {
                        for (AVObject object : list)
                            DBFriendShip.save(object);
                    } else {
                        ShowMessageUtil.tosatFast(e.getMessage(), MainActivity.this);
                    }
                }
            });


        } else {
            System.out.println("query friendship from database");
            try {
                List<DBFriendShip> dbFriendShipList = DBUtils.getDbManager().selector(DBFriendShip.class).where("selfObjectId", "=", AVUser.getCurrentUser().getObjectId()).findAll();
                if (dbFriendShipList == null || dbFriendShipList.size() == 0) {
                    System.out.println("dbFriendShipList empty");
                    return;
                } else {
                    List<AVObject> user2todoList = new ArrayList<>();
                    for (DBFriendShip dbFriendShip : dbFriendShipList)
                        user2todoList.add(DBFriendShip.getFriendship(dbFriendShip));
                    Message message = new Message();
                    message.what = INIT_FRIENDSHIPS;
                    message.obj = user2todoList;
                    handler.sendMessage(message);
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    private void initTodoListData() {
        if (NetWorkUtils.isNetworkAvailable(this)) {
            System.out.println("query user2todo from network");
            AVQuery<AVObject> query = new AVQuery<>(User2Todo.TABLE_USER_2_TODO);
            query.whereEqualTo(User2Todo.USER, AVUser.getCurrentUser());
            query.orderByDescending("createdAt");
            query.include(User2Todo.TODO);
            query.include(User2Todo.USER + "." + Person.NICK_NAME);
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (e == null) {
                        for (AVObject object : list)
                            DBUser2Todo.save(object);
                        Message message = new Message();
                        message.what = INIT_TODO;
                        message.obj = list;
                        handler.sendMessage(message);
                    } else {
                        ShowMessageUtil.tosatFast(e.getMessage(), MainActivity.this);
                    }
                }
            });
        } else {
            System.out.println("query user2todo from database");
            try {
                List<DBMessage> dbMessageList = DBUtils.getDbManager().selector(DBMessage.class).findAll();
                List<DBUser2Todo> dbUser2TodoList = DBUtils.getDbManager().selector(DBUser2Todo.class).findAll();
                Iterator<DBUser2Todo> iterator = dbUser2TodoList.iterator();
                if (dbMessageList != null)
                    while (iterator.hasNext()) {
                        DBUser2Todo dbUser2Todo = iterator.next();
                        for (DBMessage dbMessage : dbMessageList) {
                            if (dbMessage.getUser2todoId() == dbUser2Todo.getId()) {
                                iterator.remove();
                                break;
                            }
                        }
                    }
                if (dbUser2TodoList == null || dbUser2TodoList.size() == 0) {
                    System.out.println("dbUser2TodoList empty");
                    return;
                } else {
                    List<AVObject> user2todoList = new ArrayList<>();
                    for (DBUser2Todo dbUser2Todo : dbUser2TodoList)
                        user2todoList.add(DBUser2Todo.getUser2todo(dbUser2Todo));
                    Message message = new Message();
                    message.what = INIT_TODO;
                    message.obj = user2todoList;
                    handler.sendMessage(message);
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

    }

    private void initOfflineTodoData() {
        try {
            List<DBOfflineUser2Todo> dBOfflineUser2TodoList = DBUtils.getDbManager().selector(DBOfflineUser2Todo.class).findAll();
            if (dBOfflineUser2TodoList == null || dBOfflineUser2TodoList.size() == 0) {
                System.out.println("dbUser2TodoList empty");
                return;
            } else {
                List<AVObject> user2todoList = new ArrayList<>();
                for (DBOfflineUser2Todo dBOfflineUser2Todo : dBOfflineUser2TodoList)
                    user2todoList.add(DBOfflineUser2Todo.getUser2todo(dBOfflineUser2Todo));
                offlineUser2todoList = user2todoList;
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void initMessageTextBadge() {

            /*       AVQuery<AVObject> query = new AVQuery<>(com.martsforever.owa.timekeeper.javabean.Message.TABLE_MESSAGE);
            query.whereEqualTo(com.martsforever.owa.timekeeper.javabean.Message.RECEIVER, AVUser.getCurrentUser());
            query.whereEqualTo(com.martsforever.owa.timekeeper.javabean.Message.IS_READ, com.martsforever.owa.timekeeper.javabean.Message.UNREAD);
            query.countInBackground(new CountCallback() {
                @Override
                public void done(int i, AVException e) {
                    if (e == null) {
                        messageTextBadge.setBadgeNumber(i);
                    } else {
                        ShowMessageUtil.tosatSlow(e.getMessage(), MainActivity.this);
                    }
                }
            });*/
        if (NetWorkUtils.isNetworkAvailable(this)) {
            System.out.println("query message from network");
            AVQuery<AVObject> query = new AVQuery<>(com.martsforever.owa.timekeeper.javabean.Message.TABLE_MESSAGE);
            query.whereEqualTo(com.martsforever.owa.timekeeper.javabean.Message.RECEIVER, AVUser.getCurrentUser());
            query.orderByDescending("updatedAt");
            query.include(com.martsforever.owa.timekeeper.javabean.Message.SENDER);
            query.include(com.martsforever.owa.timekeeper.javabean.Message.RECEIVER);
            query.include(com.martsforever.owa.timekeeper.javabean.Message.USER2TODO);
            query.include(com.martsforever.owa.timekeeper.javabean.Message.USER2TODO + "." + User2Todo.TODO);
            query.include(com.martsforever.owa.timekeeper.javabean.Message.USER2TODO + "." + User2Todo.TODO + "." + Todo.CREATED_BY + "." + Person.NICK_NAME);
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (e == null) {
                        for (AVObject message : list)
                            DBMessage.save(message);
                        Message message = new Message();
                        message.what = INIT_MESSAGE;
                        message.obj = list;
                        handler.sendMessage(message);
                    } else {
                        ShowMessageUtil.tosatSlow(e.getMessage(), MainActivity.this);
                    }

                }
            });
        } else {
            System.out.println("query message from database");
            try {
                List<DBMessage> dbMessageList = DBUtils.getDbManager().selector(DBMessage.class).findAll();

                if (dbMessageList == null || dbMessageList.size() == 0) {
                    System.out.println("dbMessageList empty");
                    return;
                } else {
                    List<AVObject> messageList = new ArrayList<>();
                    for (DBMessage dbMessage : dbMessageList)
                        messageList.add(DBMessage.getMessage(dbMessage));
                    Message message = new Message();
                    message.what = INIT_MESSAGE;
                    message.obj = messageList;
                    handler.sendMessage(message);
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }


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


    /**
     * register the push message receiver
     */
    private void registerPushReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("com.avos.UPDATE_STATUS");
        MessageReceiver myCustomReceiver = new MessageReceiver();
        myCustomReceiver.setHandleMessage(new MessageReceiver.HandleMessage() {
            @Override
            public void receiveMessage(JSONObject jsonObject) {
                ShowMessageUtil.tosatFast("You have new Message", MainActivity.this);
                messageTextBadge.setBadgeNumber(messageTextBadge.getBadgeNumber() + 1);

                Boolean addNewFriend = jsonObject.getBoolean(MessageHandler.MESSAGE_ADD_NEW_FRIEND);
                if (addNewFriend != null && addNewFriend) {
                    String friendshipId = jsonObject.getString(MessageHandler.MESSAGE_FRIENDSHIP_ID);
                    AVQuery<AVObject> query = new AVQuery<AVObject>(FriendShip.TABLE_FRIENDSHIP);
                    query.getInBackground(friendshipId, new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            friendShips.add(0, avObject);
                            friendAdapter.notifyDataSetChanged();
                        }
                    });
                }
                Boolean addNewTodo = jsonObject.getBoolean(MessageHandler.MESSAGE_ADD_NEW_TODO);
                if (addNewTodo != null && addNewTodo) {
                    badgeViewAllSchedule.setBadgeNumber(badgeViewAllSchedule.getBadgeNumber() + 1);
                    try {
                        AVObject user2todo = AVObject.parseAVObject(jsonObject.getString(MessageHandler.MESSAGE_USER2TODO));
                        allUser2todoList.add(0, user2todo);
                        categoryUser2todo(user2todo);
                        flashScheduleBadge();
                        DBUser2Todo.save(user2todo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        registerReceiver(myCustomReceiver, intentFilter);
    }

    private void registerAddOnlineTodoReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AddTodosActivity.ADD_NEW_ONLINE_TODO);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                badgeViewAllSchedule.setBadgeNumber(badgeViewAllSchedule.getBadgeNumber() + 1);
                try {
                    AVObject user2todo = AVObject.parseAVObject(intent.getStringExtra(AddTodosActivity.ADD_NEW_ONLINE_TODO));
                    allUser2todoList.add(0, user2todo);
                    categoryUser2todo(user2todo);
                    flashScheduleBadge();
                    DBUser2Todo.save(user2todo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, intentFilter);
    }

    private void registerAddOfflineTodoReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AddTodosActivity.ADD_NEW_OFFLINE_TODO);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    AVObject user2todo = AVObject.parseAVObject(intent.getStringExtra(AddTodosActivity.ADD_NEW_OFFLINE_TODO));
                    offlineUser2todoList.add(0, user2todo);
                    flashScheduleBadge();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, intentFilter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case FRIENDSHIP_CHANGE:
                String friendshipString = data.getStringExtra(FriendDetailActivity.SERIALIZE_FRIENDSHIP);
                try {
                    int position = data.getIntExtra(FriendDetailActivity.POSITION_FRIENDSHIP, -1);
                    AVObject changeFriendShip = AVObject.parseAVObject(friendshipString);
                    AVObject friendship = friendShips.get(position);
                    friendship.put(FriendShip.INVITATION_AVAILABLE, changeFriendShip.getBoolean(FriendShip.INVITATION_AVAILABLE));
                    friendship.put(FriendShip.SCHEDULE_AVAILABLE, changeFriendShip.getBoolean(FriendShip.SCHEDULE_AVAILABLE));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MESSAGE_BADGE_CHANGE:
                initMessageTextBadge();
                break;
            case FRIENDSHIP_ALL_CHANGE:
                initDataFriendShips();
                break;
        }
    }

    private void categoryUser2todo(AVObject user2todo) {
        AVObject todo = user2todo.getAVObject(User2Todo.TODO);
        Date startTime = todo.getDate(Todo.START_TIME);
        Date endTime = todo.getDate(Todo.END_TIME);
        Date todayTime = new Date();
        if (todayTime.getDate() == startTime.getDate()) todayUser2todoList.add(user2todo);
        if (todo.getInt(Todo.LEVEL) == Todo.LEVEL_IMPORTANT_HEIGHT)
            importantUser2todoList.add(user2todo);
        if (todo.getInt(Todo.STATE) == Todo.STATUS_DOING) doingUser2todoList.add(user2todo);
        else if (todo.getInt(Todo.STATE) == Todo.STATUS_NOTCOMPLETE)
            unfinishedUser2todoList.add(user2todo);
        else if (todo.getInt(Todo.STATE) == Todo.STATUS_COMPLETE)
            completeUser2todoList.add(user2todo);
        if (startTime.getTime() > todayTime.getTime()) readyUser2todoList.add(user2todo);
    }


}

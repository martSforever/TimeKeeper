package com.martsforever.owa.timekeeper.main;

import android.content.Intent;
import android.content.IntentFilter;
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
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.FriendShip;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.javabean.Todo;
import com.martsforever.owa.timekeeper.main.friend.AddFriendsActivity;
import com.martsforever.owa.timekeeper.main.friend.FriendDetailActivity;
import com.martsforever.owa.timekeeper.main.friend.FriendShipBaseAdapter;
import com.martsforever.owa.timekeeper.main.message.MessageActivity;
import com.martsforever.owa.timekeeper.main.push.MessageHandler;
import com.martsforever.owa.timekeeper.main.push.MessageReceiver;
import com.martsforever.owa.timekeeper.main.self.JurisdictionActivity;
import com.martsforever.owa.timekeeper.main.self.PersonInfoActivity;
import com.martsforever.owa.timekeeper.main.self.SecurityActivity;
import com.martsforever.owa.timekeeper.main.todo.TodoAdapter;
import com.martsforever.owa.timekeeper.main.todo.TodoMenuCreater;
import com.martsforever.owa.timekeeper.util.ActivityManager;
import com.martsforever.owa.timekeeper.util.DateUtil;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;
import com.skyfishjy.library.RippleBackground;
import com.yydcdut.sdlv.SlideAndDragListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import q.rorbin.badgeview.QBadgeView;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int INIT_FRIENDSHIPS = 0x001;
    public static final int FRIENDSHIP_CHANGE = 0x002;
    public static final int MESSAGE_BADGE_CHANGE = 0x003;
    public static final int FRIENDSHIP_ALL_CHANGE = 0x004;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INIT_FRIENDSHIPS:
                    friendShips = (List<AVObject>) msg.obj;
                    initFriendInterface();
                    break;
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
    private List<AVObject> todos;
    private TodoAdapter todoAdapter;
    private SwipeMenuListView todoListView;
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
        registerReceiver();
    }

    private void initView() {
        initMainInterface();
        initDataFriendShips();
        initTodosInterface();
        initTomatoInterface();
        initMeInterface();
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
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddFriendsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initTodosInterface() {
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
        messageTextBadge.setBadgeTextSize(5, true);
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
        AVQuery<AVObject> query = new AVQuery<>(FriendShip.TABLE_FRIENDSHIP);
        query.whereEqualTo(FriendShip.SELF, AVUser.getCurrentUser());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Message message = new Message();
                    message.what = INIT_FRIENDSHIPS;
                    message.obj = list;
                    handler.sendMessage(message);
                } else {
                    ShowMessageUtil.tosatFast(e.getMessage(), MainActivity.this);
                }
            }
        });
    }

    private void initMessageTextBadge() {
        AVQuery<AVObject> query = new AVQuery<>(com.martsforever.owa.timekeeper.javabean.Message.TABLE_MESSAGE);
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
     * register the push message receiver
     */
    private void registerReceiver() {
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
            }
        });
        registerReceiver(myCustomReceiver, intentFilter);
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

}

package com.martsforever.owa.timekeeper.main.self;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.dbbean.DBFriendShip;
import com.martsforever.owa.timekeeper.dbbean.DBUtils;
import com.martsforever.owa.timekeeper.javabean.FriendShip;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.main.MainActivity;
import com.martsforever.owa.timekeeper.main.message.MessageActivity;
import com.martsforever.owa.timekeeper.util.DataUtils;
import com.martsforever.owa.timekeeper.util.NetWorkUtils;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_jurisdiction)
public class JurisdictionActivity extends AppCompatActivity {

    List<AVObject> selfFriendships;
    List<AVObject> friendFriendships;

    JurisdictionAdapter jurisdictionAdapter;

    @ViewInject(R.id.jurisdiction_list_view)
    ListView jurisdictionListView;

    private static final int INIT_SELF_FRIENDSHIP = 0x001;
    private static final int INIT_FRIEND_FRIENDSHIP = 0x002;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INIT_SELF_FRIENDSHIP:
                    selfFriendships = (List<AVObject>) msg.obj;
                    initFriendFriendship();
                    break;
                case INIT_FRIEND_FRIENDSHIP:
                    friendFriendships = (List<AVObject>) msg.obj;
                    jurisdictionAdapter = new JurisdictionAdapter(selfFriendships, friendFriendships, JurisdictionActivity.this);
                    initUiAndListener();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initSelfFriendship();
    }

    public static void actionStart(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, JurisdictionActivity.class);
        activity.startActivityForResult(intent, 0);
    }

    private void initSelfFriendship() {
        if (NetWorkUtils.isNetworkAvailable(this)) {
            AVQuery<AVObject> query = new AVQuery<AVObject>(FriendShip.TABLE_FRIENDSHIP);
            query.whereEqualTo(FriendShip.SELF, AVUser.getCurrentUser());
            query.include(FriendShip.FRIEND + "." + Person.NICK_NAME);
            query.orderByAscending(FriendShip.FRIEND);
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    Message msg = new Message();
                    msg.what = INIT_SELF_FRIENDSHIP;
                    msg.obj = list;
                    handler.sendMessage(msg);
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
                    List<AVObject> friendshipList = new ArrayList<>();
                    for (DBFriendShip dbFriendShip : dbFriendShipList)
                        friendshipList.add(DBFriendShip.getFriendship(dbFriendShip));
                    Message message = new Message();
                    message.what = INIT_SELF_FRIENDSHIP;
                    message.obj = friendshipList;
                    handler.sendMessage(message);
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    private void initFriendFriendship() {
        if (NetWorkUtils.isNetworkAvailable(this)) {

            AVQuery<AVObject> query = new AVQuery<>(FriendShip.TABLE_FRIENDSHIP);
            query.whereEqualTo(FriendShip.FRIEND, AVUser.getCurrentUser());
            query.orderByAscending(FriendShip.SELF);
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    Message msg = new Message();
                    msg.what = INIT_FRIEND_FRIENDSHIP;
                    msg.obj = list;
                    handler.sendMessage(msg);
                }
            });
        } else {
            System.out.println("query friendship from database");
            try {
                List<DBFriendShip> dbFriendShipList = DBUtils.getDbManager().selector(DBFriendShip.class).where("friendObjectId", "=", AVUser.getCurrentUser().getObjectId()).findAll();

                if (dbFriendShipList == null || dbFriendShipList.size() == 0) {
                    System.out.println("dbFriendShipList empty");
                    return;
                } else {
                    List<AVObject> friendshipList = new ArrayList<>();
                    for (DBFriendShip dbFriendShip : dbFriendShipList)
                        friendshipList.add(DBFriendShip.getFriendship(dbFriendShip));
                    Message message = new Message();
                    message.what = INIT_FRIEND_FRIENDSHIP;
                    message.obj = friendshipList;
                    handler.sendMessage(message);
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    public void initUiAndListener() {
        jurisdictionListView.setAdapter(jurisdictionAdapter);
    }

    @Event(R.id.jurisdiction_back_btn)
    private void backToMainactivity(View view) {
        back();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        setResult(MainActivity.FRIENDSHIP_ALL_CHANGE);
        finish();
    }


}

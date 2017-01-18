package com.martsforever.owa.timekeeper.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.main.friend.SwipListUtil;
import com.martsforever.owa.timekeeper.main.friend.FriendAdapter;

import java.util.ArrayList;
import java.util.List;

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
    List<Person> friends;
    private FriendAdapter friendAdapter;
    private SwipeMenuListView friendListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

        /*main interface*/
        LayoutInflater inflater = LayoutInflater.from(this);
        View scheduleView = inflater.inflate(R.layout.page_view_schedule, null);
        View tomatoView = inflater.inflate(R.layout.page_view_tomato, null);
        View friendsView = inflater.inflate(R.layout.page_view_friends, null);
        View meView = inflater.inflate(R.layout.page_view_me, null);

        pagerItems = new ArrayList<>();
        pagerItems.add(scheduleView);
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
        friendListView.setMenuCreator(SwipListUtil.getFriendsMenuCreater(this));
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

    private List<Person> getFriendsData() {
        List<Person> persons = new ArrayList<>();
        Person p1 = new Person();
        p1.setUsername("alice");
        Person p2 = new Person();
        p2.setUsername("bob");
        Person p3 = new Person();
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
}

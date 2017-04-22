package com.martsforever.owa.timekeeper.main.friend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Person;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ContentView(R.layout.activity_add_friends)
public class AddFriendsActivity extends AppCompatActivity {
    @ViewInject(R.id.friend_add_swipe_list_view)
    SwipeMenuListView friendListView;
    @ViewInject(R.id.friend_add_search_edit)
    EditText searchEdit;

    List<AVUser> friends;
    private FriendBaseAdapter friendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initData();
    }

    /**
     * exit the add friend interface
     *
     * @param view
     */
    @Event(R.id.friend_add_back_btn)
    private void back(View view) {
        AddFriendsActivity.this.finish();
    }

    /**
     * search button click event, search relative users information
     *
     * @param view
     */
    @Event(R.id.friend_add_search_btn)
    private void search(View view) {
        System.out.println("key : " + searchEdit.getText().toString());
        searchUsers(searchEdit.getText().toString());
    }

    /**
     * initialize view and data
     */
    private void initData() {
        friends = new ArrayList<>();
        friendAdapter = new FriendBaseAdapter(friends, this);
        friendAdapter.setViewOnclickListener(new FriendBaseAdapter.ViewOnclickListener() {
            @Override
            public void viewOnClick(View v, final AVUser user) {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddFriendDetailActivity.actionStart(AddFriendsActivity.this, user.getObjectId());
                    }
                });
            }
        });
        friendListView.setAdapter(friendAdapter);
    }

    private void searchUsers(String nickName) {
        AVQuery<AVUser> query = new AVQuery<>(Person.TABLE_PERSON);
        query.addDescendingOrder("updatedAt");
        query.whereContains(Person.NICK_NAME, nickName);
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                AVUser currentUser = AVUser.getCurrentUser();
                Iterator<AVUser> iterator = list.iterator();
                while (iterator.hasNext()){
                    AVUser user = iterator.next();
                    if (user.getObjectId().equals(currentUser.getObjectId())) iterator.remove();
                }

                if (e == null) {
                    friendAdapter.setPersons(list);
                    friendAdapter.notifyDataSetChanged();
                } else {
                    System.out.println(e.getMessage());
                }
            }
        });
    }
}

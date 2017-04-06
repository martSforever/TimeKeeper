package com.martsforever.owa.timekeeper.main.friend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.util.DataUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

@ContentView(R.layout.activity_add_friends)
public class AddFriendsActivity extends AppCompatActivity{
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

    /** exit the add friend interface
     * @param view
     */
    @Event(R.id.friend_add_back_btn)
    private void back(View view) {
        AddFriendsActivity.this.finish();
    }

    /** search button click event, search relative users information
     * @param view
     */
    @Event(R.id.friend_add_search_btn)
        private void search(View view){
        System.out.println("key : "+searchEdit.getText().toString());
        searchUsers(searchEdit.getText().toString());
    }

    /**
     * initialize view and data
     */
    private void initData(){
        friends = DataUtils.getFriendsData();
        friendAdapter = new FriendBaseAdapter(friends,this);
        friendListView.setAdapter(friendAdapter);
    }

    private void searchUsers(String nickName){
        AVQuery<AVUser> query = new AVQuery<>(Person.TABLE_PERSON);
        query.whereContains(Person.NICK_NAME,nickName);
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if (e == null){
                    friendAdapter.setPersons(list);
                    friendAdapter.notifyDataSetChanged();
                }else {
                    System.out.println(e.getMessage());
                }
            }
        });
    }
}

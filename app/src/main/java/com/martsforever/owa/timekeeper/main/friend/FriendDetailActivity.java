package com.martsforever.owa.timekeeper.main.friend;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.github.zagum.switchicon.SwitchIconView;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.FriendShip;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.main.MainActivity;
import com.martsforever.owa.timekeeper.util.NetWorkUtils;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_friend_detail)
public class FriendDetailActivity extends AppCompatActivity {

    @ViewInject(R.id.friend_detail_username_text)
    private TextView usernateText;
    @ViewInject(R.id.friend_detail_nickname_text)
    private TextView nicknameText;
    @ViewInject(R.id.friend_detail_email_text)
    private TextView emailText;
    @ViewInject(R.id.friend_detail_mobile_text)
    private TextView mobileText;
    @ViewInject(R.id.friend_detail_switch_schedule_btn)
    private SwitchIconView switchScheduleAvailableBtn;
    @ViewInject(R.id.friend_detail_switch_invitation_btn)
    private SwitchIconView switchInvitationAvailableBtn;

    public static final String SERIALIZE_FRIENDSHIP = "friendshipSerializedString";
    public static final String POSITION_FRIENDSHIP = "position";

    private AVObject friendShip;
    private AVUser friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initData();
    }

    private void initData() {
        try {
            friendShip = AVObject.parseAVObject(getIntent().getStringExtra(SERIALIZE_FRIENDSHIP));
            friend = friendShip.getAVUser(FriendShip.FRIEND);
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        switchScheduleAvailableBtn.setIconEnabled(friendShip.getBoolean(FriendShip.SCHEDULE_AVAILABLE));
        switchInvitationAvailableBtn.setIconEnabled(friendShip.getBoolean(FriendShip.INVITATION_AVAILABLE));
        usernateText.setText(friend.getUsername());
        nicknameText.setText(friend.getString(Person.NICK_NAME));
        emailText.setText(friend.getEmail());
        mobileText.setText(friend.getMobilePhoneNumber());
    }

    @Event(R.id.friend_detail_switch_schedule_btn)
    private void switchSchedule(View view) {
        if (!NetWorkUtils.isNetworkAvailable(FriendDetailActivity.this)) {
            NetWorkUtils.showNetworkNotAvailable(FriendDetailActivity.this);
            return;
        }
        SwitchIconView switchIconView = (SwitchIconView) view;
        switchIconView.switchState();
        friendShip.put(FriendShip.SCHEDULE_AVAILABLE, switchIconView.isIconEnabled());
        friendShip.saveInBackground();
    }

    @Event(R.id.friend_detail_switch_invitation_btn)
    private void switchInvitation(View view) {
        if (!NetWorkUtils.isNetworkAvailable(FriendDetailActivity.this)) {
            NetWorkUtils.showNetworkNotAvailable(FriendDetailActivity.this);
            return;
        }
        SwitchIconView switchIconView = (SwitchIconView) view;
        switchIconView.switchState();
        friendShip.put(FriendShip.INVITATION_AVAILABLE, switchIconView.isIconEnabled());
        friendShip.saveInBackground();
    }

    public static void actionStart(Activity activity, String friendshipSerializedString, int position) {
        Intent intent = new Intent();
        intent.setClass(activity, FriendDetailActivity.class);
        intent.putExtra(SERIALIZE_FRIENDSHIP, friendshipSerializedString);
        intent.putExtra(POSITION_FRIENDSHIP, position);
        activity.startActivityForResult(intent, 0);
    }

    @Override
    public void onBackPressed() {
        returnFriendShipState();
    }

    @Event(R.id.friend_detail_back_btn)
    private void back(View view) {
        returnFriendShipState();
    }

    private void returnFriendShipState() {
        Intent intent = getIntent();
        intent.putExtra(SERIALIZE_FRIENDSHIP, friendShip.toString());
        setResult(MainActivity.FRIENDSHIP_CHANGE,intent);
        finish();
    }


}

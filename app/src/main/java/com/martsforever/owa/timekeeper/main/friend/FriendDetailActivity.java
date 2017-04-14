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
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

import org.w3c.dom.Text;
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

    private AVObject friendShip;
    private AVUser friend;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            friend = (AVUser) msg.obj;
            initView();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initData();
    }

    private void initData() {
        try {
            friendShip = AVObject.parseAVObject(getIntent().getStringExtra("friendshipSerializedString"));
            AVQuery<AVUser> query = new AVQuery<>(Person.TABLE_PERSON);
            query.getInBackground(((AVUser) friendShip.get(FriendShip.FRIEND)).getObjectId(), new GetCallback<AVUser>() {
                @Override
                public void done(AVUser user, AVException e) {
                    if (e == null){
                        Message message = new Message();
                        message.obj = user;
                        handler.sendMessage(message);
                    }
                    else {
                        ShowMessageUtil.tosatSlow(e.getMessage(),FriendDetailActivity.this);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView(){
        switchScheduleAvailableBtn.setIconEnabled(friendShip.getBoolean(FriendShip.SCHEUDLE_AVAILABLE));
        switchInvitationAvailableBtn.setIconEnabled(friendShip.getBoolean(FriendShip.INVITATION_AVAILABLE));
        usernateText.setText(friend.getUsername());
        nicknameText.setText(friend.getString(Person.NICK_NAME));
        emailText.setText(friend.getEmail());
        mobileText.setText(friend.getMobilePhoneNumber());
    }

    @Event(R.id.friend_detail_switch_schedule_btn)
    private void switchSchedule(View view) {
        ((SwitchIconView) view).switchState();
    }

    @Event(R.id.friend_detail_switch_invitation_btn)
    private void switchInvitation(View view) {
        ((SwitchIconView) view).switchState();
    }

    public static void actionStart(Activity activity, String friendshipSerializedString) {
        Intent intent = new Intent();
        intent.setClass(activity, FriendDetailActivity.class);
        intent.putExtra("friendshipSerializedString", friendshipSerializedString);
        activity.startActivity(intent);
    }

}

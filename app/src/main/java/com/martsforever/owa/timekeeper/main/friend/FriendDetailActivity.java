package com.martsforever.owa.timekeeper.main.friend;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Message;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.leanCloud.LeanCloudUtil;
import com.martsforever.owa.timekeeper.main.push.FriendsInvitationMessageHandler;
import com.martsforever.owa.timekeeper.main.push.MessageHandler;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;

@ContentView(R.layout.activity_friend_detail)
public class FriendDetailActivity extends AppCompatActivity {

    @ViewInject(R.id.friend_detail_username_text)
    TextView usernameText;
    @ViewInject(R.id.friend_detail_nickname_text)
    TextView nicknameText;
    @ViewInject(R.id.friend_detail_email_text)
    TextView emailText;
    @ViewInject(R.id.friend_detail_mobile_text)
    TextView mobileText;

    @ViewInject(R.id.friend_detail_verify_message_edit)
    EditText verifyMessageEdit;

    AVUser friend;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            friend = (AVUser) msg.obj;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        init();
//        testPushService();
//        registerReceiver();
    }

    private void init() {
        AVQuery<AVUser> query = new AVQuery<>(Person.TABLE_PERSON);
        query.getInBackground(getIntent().getStringExtra("userId"), new GetCallback<AVUser>() {
            @Override
            public void done(AVUser user, AVException e) {
                if (e == null) {
                    usernameText.setText(user.getUsername());
                    nicknameText.setText(user.get(Person.NICK_NAME).toString());
                    emailText.setText(user.getEmail());
                    mobileText.setText(user.getMobilePhoneNumber());

                    AVUser currentUser = AVUser.getCurrentUser();
                    verifyMessageEdit.setText("I'm " + currentUser.get(Person.NICK_NAME).toString());

                    android.os.Message message = new android.os.Message();
                    message.obj = user;
                    handler.sendMessage(message);
                } else {
                    ShowMessageUtil.tosatFast(e.getMessage(), FriendDetailActivity.this);
                }
            }
        });
    }

    public static void actionStart(Context context, String userId) {
        Intent intent = new Intent(context, FriendDetailActivity.class);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }

    @Event(R.id.friend_detail_back_btn)
    private void back(View v) {
        this.finish();
    }

    @Event(R.id.friend_detail_cancel_btn)
    private void cancel(View view) {
        this.finish();
    }

    @Event(R.id.friend_detail_confirm_btn)
    private void confirm(View view) {
        if (friend != null) {
            addNewMessage(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null){
                        pushFriendsInviteMessage();
                    }
                    else {
                        ShowMessageUtil.tosatFast(e.getMessage(), FriendDetailActivity.this);
                    }
                }
            });
        }
        else {
            ShowMessageUtil.tosatFast("System error!", FriendDetailActivity.this);
        }
    }

    /**
     * push friend's invitation message
     */
    private void pushFriendsInviteMessage(){
        String installationId = friend.get(Person.INSTALLATION_ID).toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MessageHandler.MESSAGE_SENDER_MESSAGE, verifyMessageEdit.getText().toString().trim());
        jsonObject.put(MessageHandler.MESSAGE_SENDER_NAME,AVUser.getCurrentUser().get(Person.NICK_NAME).toString());
        jsonObject.put(MessageHandler.MESSAGE_HANDLE_CLASS, FriendsInvitationMessageHandler.class.getName());
        LeanCloudUtil.pushMessage(installationId, jsonObject, this);
    }

    private void addNewMessage(SaveCallback saveCallback){
        AVObject message = new AVObject(com.martsforever.owa.timekeeper.javabean.Message.TABLE_MESSAGE);
        message.put(Message.MESSAGE_TYPE, Message.MESSAGE_TYPE_FRIENDS_INVITATION);
        message.put(Message.SENDER,AVUser.getCurrentUser());
        message.put(Message.RECEIVER,friend);
        message.put(Message.IS_READ,Message.UNREAD);
        message.put(Message.TIME,new Date());
        message.put(Message.VERIFY_MESSAGE,verifyMessageEdit.getText().toString().trim());
        message.saveInBackground(saveCallback);
    }

}

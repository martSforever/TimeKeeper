package com.martsforever.owa.timekeeper.main.message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.FriendShip;
import com.martsforever.owa.timekeeper.javabean.Message;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.leanCloud.LeanCloudUtil;
import com.martsforever.owa.timekeeper.main.push.FriendsInvitationMessageHandler;
import com.martsforever.owa.timekeeper.main.push.MessageHandler;
import com.martsforever.owa.timekeeper.main.push.SystemMessageHandler;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;

@ContentView(R.layout.activity_message_friends_invite_detail)
public class FriendsInviteMessageDetailActivity extends AppCompatActivity {

    @ViewInject(R.id.message_detail_title_text)
    private TextView titleText;
    @ViewInject(R.id.message_detail_vertify_message_text)
    private TextView veritfyMessageText;

    AVObject message;
    AVUser sender;
    AVUser currentUser = AVUser.getCurrentUser();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    message = (AVObject) msg.obj;
                    initView();
                    initSender();
                    break;
                case 2:
                    sender = (AVUser) msg.obj;
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initMessage();
    }

    public static void actionStart(Activity activity, String messageId, int messagePosition) {
        Intent intent = new Intent();
        intent.setClass(activity, FriendsInviteMessageDetailActivity.class);
        intent.putExtra("messageId", messageId);
        intent.putExtra("messagePosition", messagePosition);
        activity.startActivityForResult(intent, 0);//这里采用startActivityForResult来做跳转，此处的0为一个依据，可以写其他的值，但一定要>=0
    }

    private void initMessage() {
        String messageId = getIntent().getStringExtra("messageId");
        AVQuery<AVObject> query = new AVQuery<>(Message.TABLE_MESSAGE);
        query.getInBackground(messageId, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    android.os.Message message = new android.os.Message();
                    message.obj = avObject;
                    message.what = 1;
                    handler.sendMessage(message);
                } else {
                    ShowMessageUtil.tosatFast(e.getMessage(), FriendsInviteMessageDetailActivity.this);
                }
            }
        });
    }

    private void initSender() {
        final AVUser sender = (AVUser) message.get(Message.SENDER);
        AVQuery<AVUser> query = new AVQuery<>(Person.TABLE_PERSON);
        query.getInBackground(sender.getObjectId(), new GetCallback<AVUser>() {
            @Override
            public void done(AVUser user, AVException e) {
                android.os.Message msg = new android.os.Message();
                msg.what = 2;
                msg.obj = user;
                handler.sendMessage(msg);
            }
        });
    }

    private void initView() {
        final AVUser sender = (AVUser) message.get(Message.SENDER);
        AVQuery<AVUser> query = new AVQuery<>(Person.TABLE_PERSON);
        query.getInBackground(sender.getObjectId(), new GetCallback<AVUser>() {
            @Override
            public void done(AVUser user, AVException e) {
                titleText.setText(user.get(Person.NICK_NAME) + " apply to add you as friend!");
                veritfyMessageText.setText(message.get(Message.VERIFY_MESSAGE).toString());
            }
        });
    }

    @Event(R.id.message_detail_reject_btn)
    private void reject(View view) {
        ShowMessageUtil.tosatFast(sender.getUsername(), this);
        /*push message*/
        pushRejectMessage();
        addRejectMessage();
        Intent intent = getIntent();
        intent.putExtra("messageIsRead", Message.REJECT);
        message.put(Message.IS_READ, Message.REJECT);
        message.saveInBackground();
        setResult(MessageActivity.RESULT_MESSAGE_CHANGE, intent);
        finish();
    }

    private void pushRejectMessage() {
        String installationId = sender.get(Person.INSTALLATION_ID).toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MessageHandler.MESSAGE_SENDER_MESSAGE, currentUser.get(Person.NICK_NAME).toString() + " has rejected your friend's invitation!");
        jsonObject.put(MessageHandler.MESSAGE_SENDER_NAME, currentUser.get(Person.NICK_NAME).toString());
        jsonObject.put(MessageHandler.MESSAGE_HANDLE_CLASS, SystemMessageHandler.class.getName());
        LeanCloudUtil.pushMessage(installationId, jsonObject, this);
    }

    private void addRejectMessage() {
        /*add new Message*/
        AVObject avMessage = new AVObject(Message.TABLE_MESSAGE);
        avMessage.put(Message.MESSAGE_TYPE, Message.MESSAGE_TYPE_SYATEM);
        avMessage.put(Message.SENDER, currentUser);
        avMessage.put(Message.RECEIVER, sender);
        avMessage.put(Message.VERIFY_MESSAGE, currentUser.get(Person.NICK_NAME) + " has rejected you friend's invitation!");
        avMessage.put(Message.IS_READ, Message.UNREAD);
        avMessage.put(Message.TIME, new Date());
        avMessage.put(Message.HANDLE_CLASS_NAME, SystemMessageHandler.class.getName());
        avMessage.saveInBackground();
    }

    @Event(R.id.message_detail_accept_btn)
    private void accept(View view) {
        pushAcceptMessage();
        addAcceptMessage();
        addFriendShip();
        Intent intent = getIntent();
        intent.putExtra("messageIsRead", Message.ACCEPT);
        message.put(Message.IS_READ, Message.ACCEPT);
        message.saveInBackground();
        setResult(MessageActivity.RESULT_MESSAGE_CHANGE, intent);
        finish();
    }

    private void pushAcceptMessage() {
        String installationId = sender.get(Person.INSTALLATION_ID).toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MessageHandler.MESSAGE_SENDER_MESSAGE, currentUser.get(Person.NICK_NAME).toString() + " has accepted your friend's invitation!");
        jsonObject.put(MessageHandler.MESSAGE_SENDER_NAME, currentUser.get(Person.NICK_NAME).toString());
        jsonObject.put(MessageHandler.MESSAGE_HANDLE_CLASS, SystemMessageHandler.class.getName());
        LeanCloudUtil.pushMessage(installationId, jsonObject, this);
    }

    private void addAcceptMessage() {
        /*add new Message*/
        AVObject avMessage = new AVObject(Message.TABLE_MESSAGE);
        avMessage.put(Message.MESSAGE_TYPE, Message.MESSAGE_TYPE_SYATEM);
        avMessage.put(Message.SENDER, currentUser);
        avMessage.put(Message.RECEIVER, sender);
        avMessage.put(Message.VERIFY_MESSAGE, currentUser.get(Person.NICK_NAME) + " has accspted you friend's invitation!");
        avMessage.put(Message.IS_READ, Message.UNREAD);
        avMessage.put(Message.TIME, new Date());
        avMessage.put(Message.HANDLE_CLASS_NAME, SystemMessageHandler.class.getName());
        avMessage.saveInBackground();
    }

    private void addFriendShip() {
        AVObject friendship = new AVObject(FriendShip.TABLE_FRIENDSHIP);
        friendship.put(FriendShip.SELF, currentUser);
        friendship.put(FriendShip.FRIEND, sender);
        friendship.put(FriendShip.FRIEND_NAME, sender.get(Person.NICK_NAME));
        friendship.put(FriendShip.SCHEUDLE_AVAILABLE,true);
        friendship.put(FriendShip.INVITATION_AVAILABLE,true);
        friendship.saveInBackground();
        friendship = new AVObject(FriendShip.TABLE_FRIENDSHIP);
        friendship.put(FriendShip.FRIEND, currentUser);
        friendship.put(FriendShip.SELF, sender);
        friendship.put(FriendShip.FRIEND_NAME, currentUser.get(Person.NICK_NAME));
        friendship.put(FriendShip.SCHEUDLE_AVAILABLE,true);
        friendship.put(FriendShip.INVITATION_AVAILABLE,true);
        friendship.saveInBackground();
    }

    @Event(R.id.message_detail_back_btn)
    private void back(View view) {
        this.finish();
    }

}

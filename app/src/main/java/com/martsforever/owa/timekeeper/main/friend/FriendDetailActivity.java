package com.martsforever.owa.timekeeper.main.friend;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SendCallback;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Person;
import com.martsforever.owa.timekeeper.leanCloud.LeanCloudUtil;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

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
    @ViewInject(R.id.friend_detail_confirm_btn)
    Button confirmBtn;
    @ViewInject(R.id.friend_detail_cancel_btn)
    Button cancelBtn;

    AVUser friend;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            friend = (AVUser) msg.obj;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        init();
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

                    Message message = new Message();
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
        /*if (friend == null) {
            ShowMessageUtil.tosatFast("internal error!", FriendDetailActivity.this);
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", com.martsforever.owa.timekeeper.javabean.Message.MESSAGE_TYPE_FRIENDS_INVITATION);
            jsonObject.put("message", verifyMessageEdit.getText().toString().trim());
            LeanCloudUtil.push(friend.get(Person.INSTALLATION_ID).toString(), jsonObject, this);
        }*/

        String installationId = friend.get(Person.INSTALLATION_ID).toString();

        /*push to specific android device|*/
        AVQuery pushQuery = AVInstallation.getQuery();
        // 假设 THE_INSTALLATION_ID 是保存在用户表里的 installationId，
        // 可以在应用启动的时候获取并保存到用户表
        AVPush push = new AVPush();
        String action = "com.avos.UPDATE_STATUS";
        String name = "weishengjian";
        String tag = "test leancloud push";
        JSONObject data;
        data = new JSONObject();
        data.put("action", action);
        data.put("name", name);
        data.put("tag", tag);
        data.put("message",verifyMessageEdit.getText().toString().trim());

        push.setData(data);
        push.setCloudQuery("select * from _Installation where installationId ='" + installationId + "'");
        push.sendInBackground(new SendCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // push successfully.
                    ShowMessageUtil.tosatFast("push successful", FriendDetailActivity.this);
                } else {
                    // something wrong.
                    ShowMessageUtil.tosatFast(e.getMessage(), FriendDetailActivity.this);
                }
            }
        });

    }
}

package com.martsforever.owa.timekeeper.main.friend;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Person;
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
                    verifyMessageEdit.setText("I'm " + user.get(Person.NICK_NAME).toString());
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
}

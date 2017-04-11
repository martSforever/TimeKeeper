package com.martsforever.owa.timekeeper.main.message;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Message;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

@ContentView(R.layout.activity_message)
public class MessageActivity extends AppCompatActivity {

    List<AVObject> messages;
    MessageAdapter messageAdapter;
    @ViewInject(R.id.message_swip_list_view)
    SwipeMenuListView messageListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        init();
    }

    private void init() {
        AVUser currentUser = AVUser.getCurrentUser();
        AVQuery<AVObject> query = new AVQuery<>(Message.TABLE_MESSAGE);
        query.whereEqualTo(Message.RECEIVER, currentUser);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    messageAdapter = new MessageAdapter(list, MessageActivity.this);
                    messageListView.setAdapter(messageAdapter);
                } else {
                    ShowMessageUtil.tosatFast(e.getMessage(), MessageActivity.this);
                }

            }
        });
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MessageActivity.class);
        context.startActivity(intent);
    }
}

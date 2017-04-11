package com.martsforever.owa.timekeeper.main.message;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.BounceInterpolator;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Message;
import com.martsforever.owa.timekeeper.main.push.MessageHandler;
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            messages = (List<AVObject>) msg.obj;
            messageAdapter = new MessageAdapter(messages, MessageActivity.this);
            messageListView.setAdapter(messageAdapter);
            messageListView.setMenuCreator(MessageMenuCreater.getFriendsMenuCreater(MessageActivity.this));
            messageListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                    switch (index) {
                        case 0:
                            try {
                                AVObject message = messages.get(position);
                                MessageHandler messageHandler = (MessageHandler) Class.forName(message.get(Message.HANDLE_CLASS_NAME).toString()).newInstance();
                                messageHandler.onMessageClick(MessageActivity.this, message.getObjectId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            System.out.println(position);
                            break;
                        case 1:
                            // delete
                            messages.remove(position);
                            messageAdapter.notifyDataSetChanged();
                            break;
                    }
                    return false;
                }
            });
            super.handleMessage(msg);
        }
    };


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
                    android.os.Message message = new android.os.Message();
                    message.obj = list;
                    handler.sendMessage(message);
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

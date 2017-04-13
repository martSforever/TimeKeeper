package com.martsforever.owa.timekeeper.main.message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Message;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_message_system_detail)
public class SystemMessageDetailActivity extends AppCompatActivity {

    @ViewInject(R.id.message_system_verify_message_text)
    private TextView vertifyMessageText;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            message = (AVObject) msg.obj;
            initView();
        }
    };

    AVObject message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initData();
    }

    public static void actionStart(Activity activity, String messageId) {
        Intent intent = new Intent();
        intent.putExtra("messageId", messageId);
        intent.setClass(activity, SystemMessageDetailActivity.class);
        activity.startActivity(intent);
    }

    private void initData() {
        String messageId = getIntent().getStringExtra("messageId");
        AVQuery<AVObject> query = new AVQuery<>(Message.TABLE_MESSAGE);
        query.getInBackground(messageId, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    android.os.Message msg = new android.os.Message();
                    msg.obj = avObject;
                    handler.sendMessage(msg);
                } else {
                    ShowMessageUtil.tosatFast(e.getMessage(), SystemMessageDetailActivity.this);
                }
            }
        });
    }

    private void initView() {
        vertifyMessageText.setText(message.get(Message.VERIFY_MESSAGE).toString());
    }

    @Event(R.id.message_system_close_btn)
    private void close(View view) {
        this.finish();
    }

    @Event(R.id.message_system_detail_back_btn)
    private void back(View view) {
        this.finish();
    }

}

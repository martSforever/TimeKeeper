package com.martsforever.owa.timekeeper.main.message;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.javabean.Message;
import com.martsforever.owa.timekeeper.main.push.MessageHandler;
import com.martsforever.owa.timekeeper.util.DataUtils;
import com.martsforever.owa.timekeeper.util.ShowMessageUtil;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

@ContentView(R.layout.activity_message)
public class MessageActivity extends AppCompatActivity implements SlideAndDragListView.OnListItemClickListener, SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {

    List<AVObject> messages;
    Menu listViewMenu;
    MessageAdapter messageAdapter;
    @ViewInject(R.id.message_swip_list_view)
    SlideAndDragListView<Message> messageListView;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            messages = (List<AVObject>) msg.obj;
            messageAdapter = new MessageAdapter(messages, MessageActivity.this);
            initUiAndListener();
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initData();
        initMenu();
    }

    private void initData() {
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

    public void initMenu() {
        listViewMenu = new Menu(false, false);
        listViewMenu.addItem(new MenuItem.Builder().setWidth(DataUtils.dp2px(90, MessageActivity.this))
                .setBackground((new ColorDrawable(Color.argb(0xdd, 222, 140, 104))))
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setIcon(getResources().getDrawable(R.drawable.icon_delete))
                .build());
    }

    public void initUiAndListener() {
        messageListView.setMenu(listViewMenu);
        messageListView.setAdapter(messageAdapter);
        messageListView.setOnListItemClickListener(this);
        messageListView.setOnMenuItemClickListener(this);
        messageListView.setOnItemDeleteListener(this);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onListItemClick(View v, int position) {
        AVObject message = messages.get(position);
        MessageHandler messageHandler = null;
        try {
            message.put(Message.IS_READ,Message.READ);
            message.saveInBackground();
            messageAdapter.notifyDataSetChanged();
            messageHandler = (MessageHandler) Class.forName(message.get(Message.HANDLE_CLASS_NAME).toString()).newInstance();
            messageHandler.onMessageClick(this, message.getObjectId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemDelete(View view, int position) {
        messages.remove(position - messageListView.getHeaderViewsCount());
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
        switch (direction) {
            case MenuItem.DIRECTION_RIGHT:
                switch (buttonPosition) {
                    case 0:
                        messages.get(itemPosition).deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    ShowMessageUtil.tosatFast("delete message successful!", MessageActivity.this);
                                } else {
                                    ShowMessageUtil.tosatFast(e.getMessage(), MessageActivity.this);
                                }
                            }
                        });
                        return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                }
        }
        return Menu.ITEM_NOTHING;
    }

    @Event(R.id.message_back_btn)
    private void back(View view) {
        this.finish();
    }
}

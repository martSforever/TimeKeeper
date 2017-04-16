package com.martsforever.owa.timekeeper.main.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.LogUtil;
import com.martsforever.owa.timekeeper.R;
import com.martsforever.owa.timekeeper.util.NotificationUtils;

import java.util.Set;

/**
 * Created by OWA on 2017/4/7.
 */

public class MessageReceiver extends BroadcastReceiver {
    private static final String TAG_DEBUG = "MessageReceiver";

    /*by this handleMessage, activity can do something by implement it's method*/
    private HandleMessage handleMessage;

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.log.d(TAG_DEBUG, "Get Message");
        try {
            String action = intent.getAction();
            String channel = intent.getExtras().getString("com.avos.avoscloud.Channel");
            Log.d(TAG_DEBUG, "got action " + action + " on channel " + channel + " with:");
            //获取消息内容
            JSONObject jsonObject = JSON.parseObject(intent.getExtras().getString("com.avos.avoscloud.Data"));
            if (jsonObject != null) {

                MessageHandler messageHandler = (MessageHandler) Class.forName(jsonObject.get(MessageHandler.MESSAGE_HANDLE_CLASS).toString()).newInstance();
                JSONObject notificationMessage = messageHandler.getNotificationMessage(jsonObject);
                NotificationUtils.sendNotification(context,notificationMessage);
                if (handleMessage != null) {
                    handleMessage.receiveMessage(jsonObject);
                } else {
                    System.out.println("handleMessage is null");
                }
            }
        } catch (Exception e) {
            Log.d(TAG_DEBUG, "JSONException: " + e.getMessage());
        }
    }

    public interface HandleMessage {
        public void receiveMessage(JSONObject jsonObject);
    }

    public void setHandleMessage(HandleMessage handleMessage) {
        this.handleMessage = handleMessage;
    }
}

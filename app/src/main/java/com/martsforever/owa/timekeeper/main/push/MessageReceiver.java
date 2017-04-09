package com.martsforever.owa.timekeeper.main.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.LogUtil;
import com.martsforever.owa.timekeeper.R;

import java.util.Set;

/**
 * Created by OWA on 2017/4/7.
 */

public class MessageReceiver extends BroadcastReceiver {
    private static final String TAG_DEBUG = "MessageReceiver";
    public static final String TAG_DATA = "com.avos.avoscloud.Data";

    public static final String NOTIFICATION_TITLE = "notification title";
    public static final String NOTIFICATION_MESSAGE = "notification message";
    public static final String NOTIFICATION_TICKER = "notification ticker";

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

                /*通知栏*/
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setContentTitle(notificationMessage.getString(NOTIFICATION_TITLE))//设置通知栏标题
                        .setContentText(notificationMessage.getString(NOTIFICATION_MESSAGE)) /*设置通知栏显示内容*/
                        //  .setNumber(number) //设置通知集合的数量
                        .setTicker(notificationMessage.getString(NOTIFICATION_TICKER)) //通知首次出现在通知栏，带上升动画效果的
                        .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                        .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                        //  .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                        .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                        .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                        //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                        .setSmallIcon(R.drawable.timekeeper_logo_white);//设置通知小ICON
                mNotificationManager.notify(0, mBuilder.build());

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

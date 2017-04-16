package com.martsforever.owa.timekeeper.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;

import com.alibaba.fastjson.JSONObject;
import com.martsforever.owa.timekeeper.R;

/**
 * Created by OWA on 2017/4/16.
 */

public class NotificationUtils {

    public static final String NOTIFICATION_TITLE = "notification title";
    public static final String NOTIFICATION_MESSAGE = "notification message";
    public static final String NOTIFICATION_TICKER = "notification ticker";

    public static void sendNotification(Context context, JSONObject notificationMessage) {
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
                .setSmallIcon(R.drawable.timekeeper_logo_white_small)//设置通知小ICON
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.timekeeper_logo_white_small));
        mNotificationManager.notify(0, mBuilder.build());
    }
}

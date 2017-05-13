package com.martsforever.owa.timekeeper.main.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.avos.avoscloud.AVObject;
import com.martsforever.owa.timekeeper.javabean.Todo;
import com.martsforever.owa.timekeeper.javabean.User2Todo;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by OWA on 2017/5/13.
 */

public class AlarmUtils {

    public static final String ACTION_ADD_ALARM = "com.martsforever.owa.timekeeper.main.alarm.ACTION_ADD_ALARM";
    public static final String ACTION_START_ALARM = "com.martsforever.owa.timekeeper.main.alarm.ACTION_START_ALARM";

    public static final String PARAMETER_USER2TODO_DB_ID = "PARAMETER_USER2TODO_DB_ID";

    public static void addAlarm(Context context, AVObject user2todo) {
        Date startTime = user2todo.getAVObject(User2Todo.TODO).getDate(Todo.START_TIME);
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        long timeMills = cal.getTimeInMillis();
        int alarmId = user2todo.getInt("id");

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent();
        intent.putExtra(PARAMETER_USER2TODO_DB_ID, alarmId);
        intent.setAction(ACTION_START_ALARM);
        PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, timeMills, sender);
        System.out.println("add alarm:" + alarmId);
    }

    public static void deleteAlarm(Context context, AVObject user2todo) {
        int alarmId = user2todo.getInt("id");
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent();
        intent.setAction(ACTION_START_ALARM);
        PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.cancel(sender);
        System.out.println("delete alarm:" + alarmId);
    }
}

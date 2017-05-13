package com.martsforever.owa.timekeeper.main.alarm;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.martsforever.owa.timekeeper.R;

/**
 * Created by OWA on 2017/5/13.
 */

public class AlarmService extends Service {

    private static String ACTION_START_MUSIC = "ACTION_START_MUSIC";
    private static String ACTION_STOP_MUSIC = "ACTION_STOP_MUSIC";

    MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.samsara);
        mediaPlayer.setLooping(true);
        registerStartAlarmReceiver();
        registerStartMusicReceiver();
        registerStopMusicReceiver();
        System.out.println("AlarmService onCreate");
    }

    private void registerStartMusicReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_START_MUSIC);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mediaPlayer.start();
            }
        }, intentFilter);
    }

    private void registerStopMusicReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_STOP_MUSIC);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mediaPlayer.pause();
            }
        }, intentFilter);
    }

    public static void startMusic(Context context) {
        Intent intent = new Intent(ACTION_START_MUSIC);
        context.sendBroadcast(intent);
    }

    public static void stopMusic(Context context) {
        Intent intent = new Intent(ACTION_STOP_MUSIC);
        context.sendBroadcast(intent);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void registerStartAlarmReceiver() {
        IntentFilter intentFilter = new IntentFilter(AlarmUtils.ACTION_START_ALARM);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                AlarmTodoDetailActivity.actionStart(AlarmService.this, intent.getIntExtra(AlarmUtils.PARAMETER_USER2TODO_DB_ID, -1));
            }
        }, intentFilter);
    }
}
